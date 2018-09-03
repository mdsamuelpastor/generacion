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
package admincommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.skill.SkillList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 */
public class GiveMissingSkills extends ChatCommand
{

	public GiveMissingSkills()
	{
		super("givemissingskills");
	}

	@Override
	public void execute(Player admin, String... params)
	{
			
		VisibleObject target = admin.getTarget();
		if (target != null && target instanceof Player)
			addMissingSkills((Player) target);
		else
			PacketSendUtility.sendMessage(admin, "Kein g?ltiges Ziel ausgew?hlt.");
	}
	
	public static void addMissingSkills(Player player)
	{
		int level = player.getCommonData().getLevel();
		PlayerClass playerClass = player.getCommonData().getPlayerClass();
		Race playerRace = player.getCommonData().getRace();
		
		for(int i = 0; i <= level; i++)
		{
			addSkills(player, i, playerClass, playerRace, false);
		}
		
		if(!playerClass.isStartingClass())
		{
			PlayerClass startinClass = PlayerClass.getStartingClassFor(playerClass);
			
			for(int i = 1; i < 10; i++)
			{
				addSkills(player, i, startinClass, playerRace, false);
			}

			if (player.getSkillList().getSkillEntry(30001) != null)
			{
				int skillLevel = player.getSkillList().getSkillLevel(30001);
				player.getSkillList().removeSkill(30001);
				PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player));
				player.getSkillList().addSkill(player, 30002, skillLevel);
				player.getSkillList().addSkill(player, 30003, 1);
				player.getSkillList().addSkill(player, 40009, 1);
				player.getRecipeList().autoLearnRecipe(player, 40009, 1);
			}
		}		
	}
	
	private static void addSkills(Player player, int level, PlayerClass playerClass, Race playerRace, boolean isNewCharacter)
	{
		SkillLearnTemplate[] skillTemplates =
			DataManager.SKILL_TREE_DATA.getTemplatesFor(playerClass, level, playerRace);
		
		SkillList<Player> playerSkillList = player.getSkillList();
		
		for(SkillLearnTemplate template : skillTemplates)
		{
			if(!checkLearnIsPossible(playerSkillList, template))
				continue;
			if (player.getCommonData().getLevel() <= 9 || template.getSkillId() != 30001 && player.getCommonData().getLevel() > 9)
				playerSkillList.addSkill(player, template.getSkillId(), template.getSkillLevel());
		}
	}
	
	/**
	 *  Check SKILL_AUTOLEARN property
	 *  Check skill already learned
	 *  Check skill template auto-learn attribute
	 *  
	 * @param playerSkillList
	 * @param template
	 * @return
	 */
	private static boolean checkLearnIsPossible(SkillList<Player> playerSkillList, SkillLearnTemplate template)
	{
		if (playerSkillList.isSkillPresent(template.getSkillId()))
			return true;

		if (!template.isStigma())
			return true;
		
		if(template.isAutolearn())
			return true;
		
		return false;
	}
}
