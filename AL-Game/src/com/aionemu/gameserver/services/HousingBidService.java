/*
 * This file is part of NextGenCore <Ver:3.7>.
 *
 *  NextGenCore is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  NextGenCore is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NextGenCore. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.aionemu.gameserver.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;

import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.HouseBidsDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseBidEntry;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.house.PlayerHouseBid;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.Sale;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_ACQUIRE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_OWNER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECEIVE_BIDS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.mail.AuctionResult;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.taskmanager.AbstractCronTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapType;

/**
 * @author Maestross
 */

public class HousingBidService extends AbstractCronTask {

	private static final Logger log = LoggerFactory.getLogger("HOUSE_AUCTION_LOG");

	private static final String registerEndExpression = HousingConfig.HOUSE_REGISTER_END;
	private static CronExpression registerDateExpr;
	private static final FastMap<Integer, HouseBidEntry> houseBids;
	private static final FastMap<Integer, HouseBidEntry> playerBids;
	private static final FastMap<Integer, HouseBidEntry> bidsByIndex;
	private static int timeProlonged = 0;
	private static HousingBidService instance;
	private HousingBidService(String auctionTime) throws ParseException {
		super(auctionTime);
	}

	public static final HousingBidService getInstance() {
		return instance;
	}

	@Override
	protected long getRunDelay() {
		return getMinutesTillAuction() * 60 * 1000;
	}

	@Override
	protected String getServerTimeVariable() {
		return "auctionTime";
	}

	@Override
	protected void postInit() {
		log.info("Loading house bids...");
		try {
			registerDateExpr = new CronExpression(registerEndExpression);
		}
		catch (ParseException e) {
		}
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		timeProlonged = dao.load("auctionProlonged");
		loadBidData();
		log.info("HousingBidService loaded. Minutes till start: " + getMinutesTillAuction());
	}

	private HousingBidService() throws ParseException {
		super(HousingConfig.HOUSE_AUCTION_TIME);
	}

	public void startAuction()
	{
		executeTask();
	}
	
	public static int getNextAuctionStartInSeconds()
	{		
		int Systemtime = (int) (System.currentTimeMillis() / 1000L);
		int NextAuction = DAOManager.getDAO(ServerVariablesDAO.class).load("auctionTime");
		int aT = NextAuction - Systemtime;  
		log.info("SysTime Sec:"+Systemtime+" Next Auction: "+NextAuction+" Sec remaining: "+aT);
		return Math.abs(aT);
	}
	
	public static void loadBidData() {
		Set<PlayerHouseBid> playerBidData = DAOManager.getDAO(HouseBidsDAO.class).loadBids();

		List<PlayerHouseBid> sortedBids = new ArrayList<PlayerHouseBid>(playerBidData);
		Collections.sort(sortedBids);

		FastMap<Integer, House> housesById = FastMap.newInstance();
		for (House house : HousingService.getInstance().getCustomHouses()) {
			housesById.put(house.getObjectId(), house);
		}

		int entryIndex = 1;
		for (PlayerHouseBid playerBid : sortedBids) {
			House house = housesById.get(playerBid.getHouseId());
			if (house == null) {
				//log.warn("Missing house " + playerBid.getHouseId() + " player " + playerBid.getPlayerId() + " bid.");//Not really needed, other way it should be improved
			}
			else {
				HouseBidEntry entry = houseBids.get(house.getObjectId());
				if (entry == null) {
					entry = new HouseBidEntry(house, entryIndex, playerBid.getBidOffer(), getRunTime() * 1000);
					houseBids.put(house.getObjectId(), entry);
					bidsByIndex.put(entryIndex++, entry);
				}
				else if (entry.getBidPrice() < playerBid.getBidOffer()) {
					entry.setBidPrice(playerBid.getBidOffer());
				}

				if (playerBid.getPlayerId() != 0) {
					HouseBidEntry playerEntry = (HouseBidEntry) entry.Clone();
					playerEntry.setBidPrice(playerBid.getBidOffer());
					playerBids.put(playerBid.getPlayerId(), playerEntry);

					entry.setLastBiddingPlayer(playerBid.getPlayerId());
					entry.setLastBidTime(playerBid.getTime().getTime());
					entry.incrementBidCount();
				}
			}
		}

		for (House house : housesById.values())
			if (house.getOwnerId() != 0) {
				if (house.getStatus() == HouseStatus.SELL_WAIT && !houseBids.containsKey(house.getObjectId())) {
					log.warn("House address=" + house.getAddress().getId() + " has status SELL_WAIT but no bid exists. Activated.");
					house.setStatus(HouseStatus.ACTIVE);
					house.setSellStarted(null);
				}
			}
	}

	@Override
	protected void executeTask() {
		if (!HousingConfig.ENABLE_HOUSE_AUCTIONS) {
			return;
		}
		Map<HouseBidEntry, Integer> winners = new HashMap<HouseBidEntry, Integer>();
		Map<HouseBidEntry, Integer> successSell = new HashMap<HouseBidEntry, Integer>();
		Map<HouseBidEntry, Integer> failedSell = new HashMap<HouseBidEntry, Integer>();

		for (Map.Entry<Integer, HouseBidEntry> playerBid : playerBids.entrySet()) {
			int playerId = playerBid.getKey();
			HouseBidEntry houseBid = getBidByEntryIndex(playerBid.getValue().getEntryIndex());
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			if (playerBid.getValue().getBidPrice() == houseBid.getBidPrice()) {
				if (house.getOwnerId() == 0) {
					winners.put(houseBid, playerId);
					break;
				}
				successSell.put(houseBid, playerId);
				break;
			}
		}

		for (HouseBidEntry houseBid : houseBids.values()) {
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			if (houseBid.getBidCount() <= 0) {
				if (house.getOwnerId() != 0) {
					failedSell.put(houseBid, house.getOwnerId());
				}
			}

		}

		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("##### Houses sold by admins #####");
		}

		for (Map.Entry<HouseBidEntry, Integer> winData : winners.entrySet()) {
			House wonHouse = HousingService.getInstance().getHouseByAddress(winData.getKey().getAddress());
			AuctionResult result = completeHouseSell(getPlayerData(winData.getValue()), wonHouse);
			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("Address " + wonHouse.getAddress().getId() + " sold for price " + winData.getKey().getBidPrice() + " (bid count: " + winData.getKey().getBidCount() + "; result: " + result
					+ ") to player " + winData.getKey().getLastBiddingPlayer());
			}

		}

		long time = System.currentTimeMillis();

		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("##### Houses auctioned by players #####");
		}
		for (Map.Entry<HouseBidEntry, Integer> sellData : successSell.entrySet()) {
			House soldHouse = HousingService.getInstance().getHouseByAddress(sellData.getKey().getAddress());
			PlayerCommonData buyerPcd = getPlayerData(sellData.getValue());
			PlayerCommonData sellerPcd = getPlayerData(soldHouse.getOwnerId());
			House currentHouse = HousingService.getInstance().getHouseByAddress(buyerPcd.getPlayer().getHouseRegistry().getOwner().getAddress().getId());

			if (sellerPcd.isOnline()) {
				PacketSendUtility.sendPacket(sellerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_SUCCESS(sellData.getKey().getAddress()));
				soldHouse.revokeOwner();
			}

			if (buyerPcd.isOnline()) {
					PacketSendUtility.broadcastPacketAndReceive(buyerPcd.getPlayer(), new SM_HOUSE_ACQUIRE(buyerPcd.getPlayerObjId(), soldHouse.getAddress().getId(), true));

					PacketSendUtility.broadcastPacketAndReceive(buyerPcd.getPlayer(), new SM_HOUSE_OWNER_INFO(buyerPcd.getPlayer(), soldHouse));
				PacketSendUtility.sendPacket(buyerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_WIN(soldHouse.getAddress().getId()));
			} else {
				HousingService.getInstance().switchHouseBuilding(currentHouse, soldHouse.getAddress().getId());
			}

			long returnKinah = sellData.getKey().getBidPrice() + sellData.getKey().getRefundKinah();
			if (soldHouse.isInGracePeriod()) {
				House newHouse = HousingService.getInstance().activateBoughtHouse(sellerPcd.getPlayerObjId());
				if (sellerPcd.isOnline()) {
					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_ACQUIRE(sellerPcd.getPlayerObjId(), newHouse.getAddress().getId(), true));
					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_OWNER_INFO(sellerPcd.getPlayer(), newHouse));
				}
				MailFormatter.sendHouseAuctionMail(newHouse, sellerPcd, AuctionResult.GRACE_SUCCESS, time, returnKinah);
			}
			else {
				MailFormatter.sendHouseAuctionMail(soldHouse, sellerPcd, AuctionResult.SUCCESS_SALE, time, returnKinah);
			}
			
			AuctionResult result = completeHouseSell(buyerPcd, soldHouse);
			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("Address " + soldHouse.getAddress().getId() + " sold by player " + sellerPcd.getPlayerObjId() + " for price " + sellData.getKey().getBidPrice() + " (bid count: "
					+ sellData.getKey().getBidCount() + "; result: " + result + ") to player " + buyerPcd.getPlayerObjId());
			}

		}

		for (Map.Entry<HouseBidEntry, Integer> notSoldData : failedSell.entrySet()) {
			HouseBidEntry bidEntry = notSoldData.getKey();
			PlayerCommonData sellerPcd = getPlayerData(notSoldData.getValue());
			House bidHouse = HousingService.getInstance().getHouseByAddress(bidEntry.getAddress());

			if (sellerPcd.isOnline()) {
				PacketSendUtility.sendPacket(sellerPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_FAIL(bidHouse.getAddress().getId()));
			}

			AuctionResult result = null;
			long compensation = 0L;

			if (bidHouse.isInGracePeriod()) {
				long timePassed = (getAuctionStartTime() - bidHouse.getSellStarted().getTime()) / 1000;
				if (timePassed > 604800) {
					bidHouse.revokeOwner();

					House activatedHouse = HousingService.getInstance().activateBoughtHouse(sellerPcd.getPlayerObjId());
					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_ACQUIRE(sellerPcd.getPlayerObjId(), activatedHouse.getAddress().getId(), true));

					PacketSendUtility.sendPacket(sellerPcd.getPlayer(), new SM_HOUSE_OWNER_INFO(sellerPcd.getPlayer(), activatedHouse));

					result = AuctionResult.GRACE_FAIL;
					compensation = bidHouse.getDefaultAuctionPrice();
					activatedHouse.save();
				}
			}
			else {
				result = AuctionResult.FAILED_SALE;
			}
			MailFormatter.sendHouseAuctionMail(bidHouse, sellerPcd, result, System.currentTimeMillis(), compensation);
			if (LoggingConfig.LOG_HOUSE_AUCTION) {
				log.info("Address " + bidHouse.getAddress().getId() + " not sold for price " + bidEntry.getBidPrice() + " (result: " + result + "; return: " + compensation + " kinah) by player "
					+ sellerPcd.getPlayerObjId());
			}

		}

		List<HouseBidEntry> copy = new ArrayList<HouseBidEntry>();
		copy.addAll(houseBids.values());

		houseBids.clear();
		playerBids.clear();
		bidsByIndex.clear();

		if (LoggingConfig.LOG_HOUSE_AUCTION) {
			log.info("##### Houses added back to auction #####");
		}

		for (HouseBidEntry houseBid : copy) {
			House house = HousingService.getInstance().getHouseByAddress(houseBid.getAddress());
			DAOManager.getDAO(HouseBidsDAO.class).deleteHouseBids(house.getObjectId());
			if (house.getOwnerId() == 0) {
				addHouseToAuction(house);
				house.save();
				if (LoggingConfig.LOG_HOUSE_AUCTION)
					log.info("Address " + houseBid.getAddress() + " not sold for price " + houseBid.getBidPrice());
			}
		}

	}

	@Override
	protected void postRun() {
		ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		timeProlonged = 0;
		dao.store("auctionProlonged", timeProlonged);
	}

	public static long getAuctionStartTime() {
		return getRunTime() * 1000 - 604800;
	}

	public static int getMinutesTillAuction() {
		int left = (int) (getRunTime() - System.currentTimeMillis() / 1000) / 60;
		left += timeProlonged;
		if (left < 0)
			return 0;
		return left;
	}

	public static boolean isBiddingAllowed() {
		DateTime.now();
		new DateTime((getRunTime() + timeProlonged * 60) * 1000);
		/*if ((now.getDayOfWeek() == auctionEnd.getDayOfWeek()) && (now.getHourOfDay() >= auctionEnd.getHourOfDay())) {
			return false;
		}*///here is some error in exception
		return true;
	}

	public static boolean isRegisteringAllowed() {
		DateTime now = DateTime.now();
		DateTime registerEnd = new DateTime(registerDateExpr.getTimeAfter(now.toDate()));
		if ((now.getDayOfWeek() == registerEnd.getDayOfWeek()) && (now.getHourOfDay() >= registerEnd.getHourOfDay())) {
			return false;
		}
		return true;
	}

	private PlayerCommonData getPlayerData(int objectId) {
		Player player = World.getInstance().findPlayer(objectId);
		if (player == null)
			return DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(objectId);
		return player.getCommonData();
	}
	

	public AuctionResult completeHouseSell(PlayerCommonData winner, House obtainedHouse) {
		House winnerHouse = HousingService.getInstance().getPlayerStudio(winner.getPlayerObjId());
		AuctionResult result = AuctionResult.WIN_BID;
		if (winnerHouse != null) {
			HousingService.getInstance().removeStudio(winner.getPlayerObjId());
		}
		else {
			int address = HousingService.getInstance().getPlayerAddress(winner.getPlayerObjId());
			obtainedHouse.setOwnerId(winner.getPlayerObjId());
			if (address > 0) {
				winnerHouse = HousingService.getInstance().getHouseByAddress(address);

				winnerHouse.setSellStarted(new Timestamp(getAuctionStartTime()));

				obtainedHouse.setStatus(HouseStatus.INACTIVE);
				result = AuctionResult.GRACE_START;
			}
		}
		MailFormatter.sendHouseAuctionMail(obtainedHouse, winner, result, System.currentTimeMillis(), 0L);
		return result;
	}

	public boolean addHouseToAuctionAgain(House house, long initialPrice) {
		if (house.getStatus() != HouseStatus.SELL_WAIT)
		{
			log.info("SellStatus for House is wrong: "+house.getObjectId()+"- "+house.getStatus());
			return false;
		}

		int maxIndex = 1;
		HouseBidEntry bidEntry = null;

		synchronized (bidsByIndex) {
			for (Integer index : bidsByIndex.keySet())
				if (index > maxIndex)
					maxIndex = index;
			bidEntry = new HouseBidEntry(house, ++maxIndex, initialPrice, getAuctionStartTime());
			bidsByIndex.putEntry(maxIndex, bidEntry);
		}

		synchronized (houseBids) {
			houseBids.put(house.getObjectId(), bidEntry);
		}

		//log.info("getRunTime :"+getRunTime()+" getRuntime*1000L:"+(getRunTime() *1000L)+"Current Time in Millis: "+System.currentTimeMillis());
		return true;
	}
	
	public boolean addHouseToAuction(House house) {
		return addHouseToAuction(house, house.getDefaultAuctionPrice());
	}
	
	public boolean addHouseToAuction(House house, long initialPrice) {
		if (house.getStatus() == HouseStatus.SELL_WAIT)
			return false;
		house.setStatus(HouseStatus.SELL_WAIT);
		int maxIndex = 1;
		HouseBidEntry bidEntry = null;

		synchronized (bidsByIndex) {
			for (Integer index : bidsByIndex.keySet())
				if (index > maxIndex)
					maxIndex = index;
			bidEntry = new HouseBidEntry(house, ++maxIndex, initialPrice, getRunTime() * 1000L);
			bidsByIndex.putEntry(maxIndex, bidEntry);
		}

		synchronized (houseBids) {
			houseBids.put(house.getObjectId(), bidEntry);
		}

		Timestamp time = new Timestamp(System.currentTimeMillis());
		house.setSellStarted(time);

		return DAOManager.getDAO(HouseBidsDAO.class).addBid(0, house.getObjectId(), initialPrice, time);
	}

	public boolean removeHouseFromAuction(House house, boolean noSale) {
		if (house.getStatus() != HouseStatus.SELL_WAIT) {
			return false;
		}
		HouseBidEntry bidEntry = null;
		HouseBidEntry playerBid = null;
		Integer lastPlayer = null;

		synchronized (houseBids) {
			bidEntry = houseBids.remove(house.getObjectId());
			if (bidEntry == null)
				return false;
			lastPlayer = bidEntry.getLastBiddingPlayer();
			playerBid = playerBids.remove(lastPlayer);
		}

		synchronized (bidsByIndex) {
			bidsByIndex.remove(bidEntry.getEntryIndex());
		}

		PlayerCommonData pcd = null;
		if (house.getOwnerId() != 0) {
			if (house.isInGracePeriod())
				house.setSellStarted(null);
			pcd = getPlayerData(house.getOwnerId());
			MailFormatter.sendHouseAuctionMail(house, pcd, AuctionResult.CANCELED_BID, System.currentTimeMillis(), bidEntry.getBidPrice() + bidEntry.getRefundKinah());

			house.setStatus(HouseStatus.ACTIVE);
		}
		else {
			house.setStatus(noSale ? HouseStatus.NOSALE : HouseStatus.ACTIVE);
		}
		if (lastPlayer != null) {
			pcd = getPlayerData(lastPlayer);
			MailFormatter.sendHouseAuctionMail(house, pcd, AuctionResult.CANCELED_BID, System.currentTimeMillis(), playerBid.getBidPrice());
		}

		DAOManager.getDAO(HouseBidsDAO.class).deleteHouseBids(house.getObjectId());
		house.save();

		return true;
	}

	public synchronized void placeBid(Player player, int entryIndex, long bidOffer) {
		int minutesLeft = getMinutesTillAuction();
		/*if (minutesLeft == 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_TIMEOUT);
			return;
		}*/

		HouseBidEntry entry = getBidByEntryIndex(entryIndex);
		if (entry == null) {
			return;
		}

		House bidHouse = HousingService.getInstance().getHouseByAddress(entry.getAddress());
		if (player.getObjectId() == bidHouse.getOwnerId()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_MY_HOUSE);
			return;
		}

		int playerAddress = HousingService.getInstance().getPlayerAddress(player.getObjectId());
		House playerHouse = HousingService.getInstance().getHouseByAddress(playerAddress);
		if ((playerHouse != null) && (playerHouse.isInGracePeriod())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_GRACE_HOUSE);
			return;
		}
		
		if ((playerHouse != null)) {
		    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_TIMEOUT);//Maybe not the right system message, but you cant bet on house if you own an house
			return;
		}

		int minLevel = getMinBidLevel(player, entry.getMapId(), entry.getLandId());
		if (minLevel > player.getLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_LOW_LEVEL(minLevel));
			return;
		}

		if ((bidOffer - entry.getBidPrice()) >= entry.getBidPrice() * HousingConfig.HOUSE_AUCTION_BID_LIMIT / 100) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_EXCESS_ACCOUNT);
			return;
		}

		HouseBidEntry currentBid = playerBids.get(player.getObjectId());
		if (currentBid != null) {
			if (entry.getLastBiddingPlayer() == player.getObjectId()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_SUCC_BID_HOUSE);
				return;
			}
			HouseBidEntry houseBid = getBidByEntryIndex(currentBid.getEntryIndex());
			if (houseBid.getBidPrice() == currentBid.getBidPrice()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_OTHER_HOUSE);
				return;
			}
		}

		if ((minutesLeft <= 5) && (timeProlonged < 30)) {
			timeProlonged += 5;

			ThreadPoolManager.getInstance().execute(new Runnable() {

				@Override
				public void run() {
					DAOManager.getDAO(ServerVariablesDAO.class).store("auctionProlonged", HousingBidService.timeProlonged);
				}

			});
		}
		/*
		else if (!isBiddingAllowed()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_BID_TIMEOUT);
			return;
		}*/

		if ((bidOffer > entry.getBidPrice()) || (entry.getBidCount() == 0)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_SUCCESS(entry.getAddress()));

			Timestamp time = new Timestamp(System.currentTimeMillis());

			int previousPlayer = entry.getLastBiddingPlayer();
			entry.incrementBidCount();
			entry.setLastBiddingPlayer(player.getObjectId());
			entry.setLastBidTime(time.getTime());
			entry.setBidPrice(bidOffer);

			HouseBidEntry playerBid = (HouseBidEntry) entry.Clone();
			playerBids.put(player.getObjectId(), playerBid);

			DAOManager.getDAO(HouseBidsDAO.class).addBid(player.getObjectId(), bidHouse.getObjectId(), bidOffer, time);

			if (previousPlayer > 0) {
				PlayerCommonData prevPcd = getPlayerData(previousPlayer);
				if (prevPcd.isOnline())
					PacketSendUtility.sendPacket(prevPcd.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_CANCEL);
				MailFormatter.sendHouseAuctionMail(bidHouse, prevPcd, AuctionResult.FAILED_BID, time.getTime(), bidHouse.getDefaultAuctionPrice());
			}

			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_PRICE_CHANGE(bidOffer));
			PacketSendUtility.sendPacket(player, new SM_RECEIVE_BIDS(0));
		}
	}

	public void onPlayerLogin(Player player) {
		if (player.getMailbox() == null) {
			return;
		}
		List<Letter> letters = player.getMailbox().getNewSystemLetters("$$HS_AUCTION_MAIL");
		boolean needsRefresh = false;

		for (Letter letter : letters) {
			String[] titleParts = letter.getTitle().split(",");
			String[] bodyParts = letter.getMessage().split(",");
			AuctionResult result = AuctionResult.getResultFromId(Integer.parseInt(titleParts[0]));
			if (result == AuctionResult.FAILED_BID) {
				needsRefresh = true;
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_CANCEL);
			}
			else if ((result == AuctionResult.WIN_BID) || (result == AuctionResult.GRACE_START)) {
				needsRefresh = true;
				int address = HousingService.getInstance().getPlayerAddress(player.getObjectId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_BID_WIN(address));
			}
			else if (result == AuctionResult.FAILED_SALE) {
				needsRefresh = true;
				int address = Integer.parseInt(bodyParts[1]);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_FAIL(address));
			}
			else if ((result == AuctionResult.SUCCESS_SALE) || (result == AuctionResult.GRACE_SUCCESS)) {
				needsRefresh = true;
				int address = Integer.parseInt(bodyParts[1]);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_AUCTION_SUCCESS(address));
			}
		}

		if (needsRefresh) {
			PacketSendUtility.sendPacket(player, new SM_RECEIVE_BIDS(0));
		}
		letters = player.getMailbox().getNewSystemLetters("$$HS_OVERDUE_");
		for (Letter letter : letters)
			if ((letter.getSenderName().endsWith("FINAL")) || (letter.getSenderName().endsWith("3RD")))
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SEQUESTRATE);
			else
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_OVERDUE);
	}

	public HouseBidEntry getHouseBid(int houseObjectId) {
		synchronized (houseBids) {
			return houseBids.get(houseObjectId);
		}
	}

	public List<HouseBidEntry> getHouseBidEntries(Race playerRace) {
		synchronized (houseBids) {
			List<HouseBidEntry> bids = new ArrayList<HouseBidEntry>();
			for (HouseBidEntry bid : houseBids.values()) {
				HousingLand land = DataManager.HOUSE_DATA.getLand(bid.getLandId());
				boolean isEly = DataManager.NPC_DATA.getNpcTemplate(land.getManagerNpcId()).getTribe() == TribeClass.GENERAL;
				if (isEly && playerRace == Race.ELYOS)
					bids.add(bid);
				else if ((!isEly) && (playerRace == Race.ASMODIANS))
					bids.add(bid);
			}
			return bids;
		}
	}

	public HouseBidEntry getLastPlayerBid(int playerId) {
		return playerBids.get(playerId);
	}

	public HouseBidEntry getBidByEntryIndex(int index) {
		synchronized (bidsByIndex) {
			return bidsByIndex.get(index);
		}
	}

	private static int getMinBidLevel(Player player, int mapId, int landId) {
		HousingLand land = DataManager.HOUSE_DATA.getLand(landId);
		Sale saleOptions = land.getSaleOptions();
		HouseType houseType = HouseType.fromValue(land.getBuildings().get(0).getSize());
		if (player.getRace() == Race.ELYOS) {
			if ((mapId == WorldMapType.HEIRON.getId()) || (mapId == WorldMapType.INGGISON.getId()))
				return saleOptions.getMinLevel();
		}
		else if ((mapId == WorldMapType.BELUSLAN.getId()) || (mapId == WorldMapType.GELKMAROS.getId())) {
			return saleOptions.getMinLevel();
		}
		switch (houseType) {
			case HOUSE:
				if (saleOptions.getMinLevel() != HousingConfig.HOUSE_MIN_BID_LEVEL)
					return HousingConfig.HOUSE_MIN_BID_LEVEL;
				break;
			case MANSION:
				if (saleOptions.getMinLevel() != HousingConfig.MANSION_MIN_BID_LEVEL)
					return HousingConfig.MANSION_MIN_BID_LEVEL;
				break;
			case ESTATE:
				if (saleOptions.getMinLevel() != HousingConfig.ESTATE_MIN_BID_LEVEL)
					return HousingConfig.ESTATE_MIN_BID_LEVEL;
				break;
			case PALACE:
				if (saleOptions.getMinLevel() != HousingConfig.PALACE_MIN_BID_LEVEL) {
					return HousingConfig.PALACE_MIN_BID_LEVEL;
				}
				break;
			default:
				break;
		}
		return saleOptions.getMinLevel();
	}

	public static boolean canBidHouse(Player player, int mapId, int landId) {
		return player.getLevel() >= getMinBidLevel(player, mapId, landId);
	}

	static {
		houseBids = FastMap.newInstance();
		playerBids = FastMap.newInstance();
		bidsByIndex = FastMap.newInstance();
		try {
			instance = new HousingBidService(HousingConfig.HOUSE_AUCTION_TIME);
		}
		catch (ParseException pe) {
		}
	}
}