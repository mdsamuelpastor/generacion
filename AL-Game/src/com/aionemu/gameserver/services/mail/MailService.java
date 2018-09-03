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
package com.aionemu.gameserver.services.mail;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.InventoryDAO;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.templates.mail.MailMessage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AdminService;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.item.ItemFactory;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;

public class MailService {

	private static final Logger log = LoggerFactory.getLogger("MAIL_LOG");
	protected Queue<Player> newPlayers = new ConcurrentLinkedQueue<Player>();

	public static final MailService getInstance() {
		return SingletonHolder.instance;
	}

	public void sendMail(Player sender, String recipientName, String title, String message, int attachedItemObjId, int attachedItemCount, int attachedKinahCount, LetterType letterType) {

		if (letterType == LetterType.BLACKCLOUD || recipientName.length() > 16) {
			return;
		}
		if (title.length() > 20) {
			title = title.substring(0, 20);
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000);
		}
		PlayerCommonData recipientCommonData = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonDataByName(recipientName);

		if (recipientCommonData == null) {
			PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.NO_SUCH_CHARACTER_NAME));
			return;
		}

		if (recipientCommonData.getRace() != sender.getRace() && sender.getAccessLevel() < AdminConfig.GM_LEVEL) {
			PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.MAIL_IS_ONE_RACE_ONLY));
			return;
		}

		Player recipient = World.getInstance().findPlayer(recipientCommonData.getPlayerObjId());
		if (recipient != null && !recipient.getMailbox().haveFreeSlots() || recipientCommonData.getMailboxLetters() > 99) {
			PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.RECIPIENT_MAILBOX_FULL));
			return;
		}

		if (!validateMailSendPrice(sender, attachedKinahCount, attachedItemObjId, attachedItemCount)) {
			return;
		}
		Item attachedItem = null;
		int finalAttachedKinahCount = 0;

		int kinahMailCommission = 0;
		int itemMailCommission = 0;

		Storage senderInventory = sender.getInventory();

		if (attachedItemObjId != 0 && attachedItemCount > 0) {
			Item senderItem = senderInventory.getItemByObjId(attachedItemObjId);

			if (senderItem == null) {
				return;
			}

			if (!senderItem.isTradeable(sender)) {
				return;
			}
			if (!AdminService.getInstance().canOperate(sender, null, senderItem, "mail"))
				return;
			float qualityPriceRate;
			switch (senderItem.getItemTemplate().getItemQuality()) {
				case JUNK:
				case COMMON:
					qualityPriceRate = 0.02F;
					break;
				case RARE:
					qualityPriceRate = 0.03F;
					break;
				case LEGEND:
				case UNIQUE:
					qualityPriceRate = 0.04F;
					break;
				case EPIC:
				case MYTHIC:
					qualityPriceRate = 0.05F;
					break;
				default:
					qualityPriceRate = 0.02F;
			}

			if (senderItem.getItemCount() < attachedItemCount) {
				return;
			}

			if (senderItem.getItemCount() == attachedItemCount) {
				senderInventory.remove(senderItem);
				PacketSendUtility.sendPacket(sender, new SM_DELETE_ITEM(attachedItemObjId));
				attachedItem = senderItem;
			}
			else if (senderItem.getItemCount() > attachedItemCount) {
				attachedItem = ItemFactory.newItem(senderItem.getItemTemplate().getTemplateId(), attachedItemCount);
				senderInventory.decreaseItemCount(senderItem, attachedItemCount);
			}

			if (attachedItem == null) {
				return;
			}
			attachedItem.setEquipped(false);
			attachedItem.setEquipmentSlot(0);
			attachedItem.setItemLocation(StorageType.MAILBOX.getId());
			itemMailCommission = Math.round((attachedItem.getItemTemplate().getPrice() * attachedItem.getItemCount()) * qualityPriceRate);
		}

		if (attachedKinahCount > 0 && senderInventory.getKinah() - attachedKinahCount >= 0) {
			finalAttachedKinahCount = attachedKinahCount;
			kinahMailCommission = Math.round(attachedKinahCount * 0.01F);
		}

		int finalMailKinah = 10 + kinahMailCommission + itemMailCommission + finalAttachedKinahCount;

		if (senderInventory.getKinah() > finalMailKinah) {
			senderInventory.decreaseKinah(finalMailKinah);
		}
		else {
			AuditLogger.info(sender, "Mail kinah exploit.");
			return;
		}

		Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());

		Letter newLetter = new Letter(IDFactory.getInstance().nextId(), recipientCommonData.getPlayerObjId(), attachedItem, finalAttachedKinahCount, title, message, sender.getName(), time, true,
			letterType);

		if (attachedItem != null && !DAOManager.getDAO(InventoryDAO.class).store(attachedItem, recipientCommonData.getPlayerObjId())) {
			return;
		}
		if (!DAOManager.getDAO(MailDAO.class).storeLetter(time, newLetter)) {
			return;
		}

		if (recipient != null) {
			Mailbox recipientMailbox = recipient.getMailbox();
			recipientMailbox.putLetterToMailbox(newLetter);

			PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.MAIL_SEND_SECCESS));

			PacketSendUtility.sendPacket(recipient, new SM_MAIL_SERVICE(recipientMailbox));
			recipientMailbox.isMailListUpdateRequired = true;

			if (recipientMailbox.mailBoxState != 0) {
				boolean isPostman = (recipientMailbox.mailBoxState & 0x2) == 2;
				PacketSendUtility.sendPacket(recipient, new SM_MAIL_SERVICE(recipient, recipientMailbox.getLetters(), isPostman));
			}

			if (letterType == LetterType.EXPRESS) {
				PacketSendUtility.sendPacket(recipient, SM_SYSTEM_MESSAGE.STR_POSTMAN_NOTIFY);
			}
		}
		if (attachedItem != null && LoggingConfig.LOG_MAIL) {
			log.info("[MAILSERVICE] [Player: " + sender.getName() + "] send [Item: " + attachedItem.getItemId()
				+ (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "] [Item Name: " + attachedItem.getItemName() + "]" : "]") + " [Count: " + attachedItem.getItemCount() + "] to [Reciever: " + recipientName + "]");
		}

		if (!recipientCommonData.isOnline()) {
			PacketSendUtility.sendPacket(sender, new SM_MAIL_SERVICE(MailMessage.MAIL_SEND_SECCESS));
			recipientCommonData.setMailboxLetters(recipientCommonData.getMailboxLetters() + 1);
			DAOManager.getDAO(MailDAO.class).updateOfflineMailCounter(recipientCommonData);
		}
	}

	public void readMail(Player player, int letterId) {
		Letter letter = player.getMailbox().getLetterFromMailbox(letterId);
		if (letter == null) {
			log.warn("Cannot read mail " + player.getObjectId() + " " + letterId);
			return;
		}

		PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player, letter, letter.getTimeStamp().getTime()));
		letter.setReadLetter();
	}

	public void getAttachments(Player player, int letterId, int attachmentType) {
		Letter letter = player.getMailbox().getLetterFromMailbox(letterId);

		if (letter == null) {
			return;
		}
		switch (attachmentType) {
			case 0:
				Item attachedItem = letter.getAttachedItem();
				if (attachedItem == null)
					return;
				if (player.getInventory().isFull()) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
					return;
				}
				player.getInventory().add(attachedItem);
				if (!DAOManager.getDAO(InventoryDAO.class).store(attachedItem, player.getObjectId())) {
					return;
				}
				PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(letterId, attachmentType));
				letter.removeAttachedItem();
				break;
			case 1:
				player.getInventory().increaseKinah(letter.getAttachedKinah());
				PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(letterId, attachmentType));
				letter.removeAttachedKinah();
		}
	}

	public void deleteMail(Player player, int[] mailObjId) {
		Mailbox mailbox = player.getMailbox();

		for (int letterId : mailObjId) {
			mailbox.removeLetter(letterId);
			DAOManager.getDAO(MailDAO.class).deleteLetter(letterId);
		}
		PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(mailObjId));
	}

	private boolean validateMailSendPrice(Player sender, int attachedKinahCount, int attachedItemObjId, int attachedItemCount) {
		int itemMailCommission = 0;
		int kinahMailCommission = Math.round(attachedKinahCount * 0.01F);
		if (attachedItemObjId != 0) {
			Item senderItem = sender.getInventory().getItemByObjId(attachedItemObjId);
			if (senderItem == null || senderItem.getItemTemplate() == null)
				return false;
			float qualityPriceRate;
			switch (senderItem.getItemTemplate().getItemQuality()) {
				case JUNK:
				case COMMON:
					qualityPriceRate = 0.02F;
					break;
				case RARE:
					qualityPriceRate = 0.03F;
					break;
				case LEGEND:
				case UNIQUE:
					qualityPriceRate = 0.04F;
					break;
				case EPIC:
				case MYTHIC:
					qualityPriceRate = 0.05F;
					break;
				default:
					qualityPriceRate = 0.02F;
			}

			itemMailCommission = Math.round(senderItem.getItemTemplate().getPrice() * attachedItemCount * qualityPriceRate);
		}

		int finalMailPrice = 10 + itemMailCommission + kinahMailCommission;

		if (sender.getInventory().getKinah() >= finalMailPrice) {
			return true;
		}
		return false;
	}

	public void onPlayerLogin(Player player) {
		ThreadPoolManager.getInstance().schedule(new MailLoadTask(player), 5000);
	}

	public void refreshMail(Player player) {
		PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player.getMailbox()));
		PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player, player.getMailbox().getLetters(), false));
	}

	private class MailLoadTask implements Runnable {

		private Player player;

		private MailLoadTask(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			player.setMailbox(DAOManager.getDAO(MailDAO.class).loadPlayerMailbox(player));
			PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player.getMailbox()));
			HousingBidService.getInstance().onPlayerLogin(player);
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {

		protected static final MailService instance = new MailService();
	}
}
