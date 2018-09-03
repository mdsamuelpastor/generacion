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
package com.aionemu.gameserver.utils.stats;

import javax.xml.bind.annotation.XmlEnum;

import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 * @author Sarynth
 * @author Imaginary
 * @author Maestross
 * @author GoodT
 * INFO:
 * Needed points and how much people can get these rank was added.
 * Its now base on pvp/highrate server needs. If you want the official needs:
 * first number is the offi like and the second number is the custom like.
 */
 
@XmlEnum
public enum AbyssRankEnum {
	GRADE9_SOLDIER(1, 120, 24, 0, 0, 1802431),//Zero:Zero
	GRADE8_SOLDIER(2, 168, 37, RankingConfig.GRADE8_SOLDIER, 0, 1802433),//1200:15000
	GRADE7_SOLDIER(3, 235, 58, RankingConfig.GRADE7_SOLDIER, 0, 1802435),//4220:18225
	GRADE6_SOLDIER(4, 329, 91, RankingConfig.GRADE6_SOLDIER, 0, 1802437),//10990:26585
	GRADE5_SOLDIER(5, 461, 143, RankingConfig.GRADE5_SOLDIER, 0, 1802439),//23500:30455
	GRADE4_SOLDIER(6, 645, 225, RankingConfig.GRADE4_SOLDIER, 0, 1802441),//42780:78899
	GRADE3_SOLDIER(7, 903, 356, RankingConfig.GRADE3_SOLDIER, 0, 1802443),//69700:120588
	GRADE2_SOLDIER(8, 1264, 561, RankingConfig.GRADE2_SOLDIER, 0, 1802445),//105600:150900
	GRADE1_SOLDIER(9, 1770, 885, RankingConfig.GRADE1_SOLDIER, 0, 1802447),//150800:235655
	STAR1_OFFICER(10, 2124, 1428, RankingConfig.STAR1_OFFICER, RankingConfig.STAR1_OFFICERC, 1802449),//214100:280699 //1000:200
	STAR2_OFFICER(11, 2549, 1973, RankingConfig.STAR2_OFFICER, RankingConfig.STAR2_OFFICERC, 1802451),//278700:350899 //700:150
	STAR3_OFFICER(12, 3059, 2704, RankingConfig.STAR3_OFFICER, RankingConfig.STAR3_OFFICERC, 1802453),//344500:490665 //500:100
	STAR4_OFFICER(13, 3671, 3683, RankingConfig.STAR4_OFFICER, RankingConfig.STAR4_OFFICERC, 1802455),//411700:625005 //300:80
	STAR5_OFFICER(14, 4405, 4994, RankingConfig.STAR5_OFFICER, RankingConfig.STAR5_OFFICERC, 1802457),//488200:890555 //100:20
	GENERAL(15, 5286, 6749, RankingConfig.GENERAL, RankingConfig.GENERALC, 1802459),//565400:110000000 //30:10
	GREAT_GENERAL(16, 6343, 115000000, RankingConfig.GREAT_GENERAL, RankingConfig.GREAT_GENERALC, 1802461),//643200:115000000 //10:5
	COMMANDER(17, 7612, 11418, RankingConfig.COMMANDER, RankingConfig.COMMANDERC, 1802463),//721600:135000000 //3:2
	SUPREME_COMMANDER(18, 9134, 13701, RankingConfig.SUPREME_COMMANDER, RankingConfig.SUPREME_COMMANDERC, 1802465);//800700:150000000

	private int id;
	private int pointsGained;
	private int pointsLost;
	private int required;
	private int quota;
	private int descriptionId;

	/**
	 * @param id
	 * @param pointsGained
	 * @param pointsLost
	 * @param required
	 * @param quota
	 */
	private AbyssRankEnum(int id, int pointsGained, int pointsLost, int required, int quota, int descriptionId) {
		this.id = id;
		this.pointsGained = pointsGained;
		this.pointsLost = pointsLost;
		this.required = required * RateConfig.ABYSS_RANK_RATE;
		this.quota = quota;
		this.descriptionId = descriptionId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pointsLost
	 */
	public int getPointsLost() {
		return pointsLost;
	}

	/**
	 * @return the pointsGained
	 */
	public int getPointsGained() {
		return pointsGained;
	}

	/**
	 * @return AP required for Rank
	 */
	public int getRequired() {
		return required;
	}

	/**
	 * @return The quota is the maximum number of allowed player to have the rank
	 */
	public int getQuota() {
		return quota;
	}

	public int getDescriptionId() {
		return descriptionId;
	}

	public static DescriptionId getRankDescriptionId(Player player) {
		int pRankId = player.getAbyssRank().getRank().getId();
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == pRankId) {
				int descId = rank.getDescriptionId();
				return (player.getRace() == Race.ELYOS) ? new DescriptionId(descId) : new DescriptionId(descId + 36);
			}
		}
		throw new IllegalArgumentException("No rank Description Id found for player: " + player);
	}

	/**
	 * @param id
	 * @return The abyss rank enum by his id
	 */
	public static AbyssRankEnum getRankById(int id) {
		for (AbyssRankEnum rank : values()) {
			if (rank.getId() == id)
				return rank;
		}
		throw new IllegalArgumentException("Invalid abyss rank provided" + id);
	}

	/**
	 * @param ap
	 * @return The abyss rank enum for his needed ap
	 */
	public static AbyssRankEnum getRankForAp(int ap) {
		AbyssRankEnum r = AbyssRankEnum.GRADE9_SOLDIER;
		for (AbyssRankEnum rank : values()) {
			if (rank.getRequired() <= ap)
				r = rank;
			else
				break;
		}
		return r;
	}
}
