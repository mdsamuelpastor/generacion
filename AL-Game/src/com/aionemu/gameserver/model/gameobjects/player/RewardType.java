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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.stats.container.StatEnum;


/**
 * @author antness, Maestross
 */
public enum RewardType {
	HUNTING {

		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_HUNTING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getXpRate() * statRate);
		}
	},
	GROUP_HUNTING {

		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_GROUP_HUNTING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getGroupXpRate() * statRate);
		}
	},
	PVP_KILL {

		@Override
		public long calcReward(Player player, long reward) {
			return (reward);
		}
	},
	QUEST {

		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_QUEST_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getQuestXpRate() * statRate);
		}
	},
	CRAFTING {

		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_CRAFTING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getCraftingXPRate() * statRate);
		}
	},
	GATHERING {

		@Override
		public long calcReward(Player player, long reward) {
			float statRate = player.getGameStats().getStat(StatEnum.BOOST_GATHERING_XP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getGatheringXPRate() * statRate);
		}
	},
	PVE_AP {
	    
		@Override
		public long calcReward(Player player, long reward) {
		    float statRate = player.getGameStats().getStat(StatEnum.BOOST_PVE_AP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getApNpcRate() * statRate);
		}
	},
	PVP_AP {
	
	    @Override
		public long calcReward(Player player, long reward) {
		    float statRate = player.getGameStats().getStat(StatEnum.BOOST_PVP_AP_RATE, 100).getCurrent() / 100f;
			return (long) (reward * player.getRates().getApPlayerGainRate() * statRate);
		}
	};

	public abstract long calcReward(Player player, long reward);
}
