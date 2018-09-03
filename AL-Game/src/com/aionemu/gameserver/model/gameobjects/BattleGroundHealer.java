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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.BattleGroundHealerController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author Maestross
 */
public class BattleGroundHealer extends Npc
{
	public BattleGroundHealer(int objId, BattleGroundHealerController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate, objectTemplate.getLevel());
	}

	@Override
	public BattleGroundHealerController getController()
	{
		return (BattleGroundHealerController) super.getController();
	}

	public BattleGroundHealer getOwner()
	{
		return (BattleGroundHealer) this;
	}

	public boolean isEnemy(VisibleObject visibleObject)
	{
		return false;
	}

	protected boolean isEnemyNpc(Npc visibleObject)
	{
		return false;
	}

	protected boolean isEnemyPlayer(Player visibleObject)
	{
		return false;
	}

	protected boolean isEnemySummon(Summon summon)
	{
		return false;
	}

	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.NORMAL;
	}

	private Race	race;

	public Race getRace()
	{
		return race;
	}

	public void setRace(Race race)
	{
		this.race = race;
	}

}
