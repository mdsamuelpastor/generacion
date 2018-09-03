/*
 * This file is part of NextGenCore <Ver:3.9>.
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

import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.services.mail.SystemMailService;


/**
 * @author Maestross
 *
 */
public final class ChallengeTaskService {

	public static int maxCountQuest1 = 81;//quest1 , task1 , level5
	public static int maxCountQuest2 = 150;//quest2 , task1 , level5
	public static int maxCountQuest3 = 174;//quest3 , task1 , level5
	public static int maxCountQuest4 = 111;//quest4 , task2 , level6
	public static int maxCountQuest5 = 159;//quest5 , task2 , level6
	public static int maxCountQuest6 = 720;//quest6 , task2 , level6
	public static int maxCountQuest7 = 150;//quest7 , task3 , level7
	public static int maxCountQuest8 = 165;//quest8 , task3 , level7
	public static int maxCountQuest9 = 720;//quest9 , task3 , level7
	public static int maxCountQuest10 = 150;//quest10 , task4 , level8
	public static int maxCountQuest11 = 165;//quest11 , task4 , level8
	public static int maxCountQuest12 = 180;//quest12 , task4 , level8
	public static int maxCountQuest13 = 30;//quest13 , task5 , level8
	public static int maxCountQuest14 = 55;//quest14 , task5 , level8
	public static int maxCountQuest15 = 60;//quest15 , task5 , level8
	public static int maxCountQuest16 = 1080;//quest16 , task6 , level8
	public static int maxCountQuest17 = 1080;//quest17 , task6 , level8
	public static int maxCountQuest18 = 1200;//quest18 , task6 , level8
	
	private static String message = "Young Daeva, here you got your Reward for the completed task. Be in future also so active!";
	private static String senderName = "Legion Task HQ";
	private static String title = "Your Reward";
	private static int coinItemId = 186000199;
	
	public static boolean updateTask (int questId, Legion legion, Player player) {
		if (legion == null)
			return false;
		if (questId == 0 || questId < 0)
			return false;
		if (!player.isLegionMember())
			return false;
		
		if (questId == 17000 || questId == 27000) {
			legion.setDoneCountQuest1(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17001 || questId == 27001) {
			legion.setDoneCountQuest2(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17002 || questId == 27002) {
			legion.setDoneCountQuest3(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17003 || questId == 27003) {
			legion.setDoneCountQuest4(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17004 || questId == 27004) {
			legion.setDoneCountQuest5(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17005 || questId == 27005) {
			legion.setDoneCountQuest6(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17006 || questId == 27006) {
			legion.setDoneCountQuest7(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17007 || questId == 27007) {
			legion.setDoneCountQuest8(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17008 || questId == 27008) {
			legion.setDoneCountQuest9(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17009 || questId == 27009) {
			legion.setDoneCountQuest10(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17010 || questId == 27010) {
			legion.setDoneCountQuest11(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17011 || questId == 27011) {
			legion.setDoneCountQuest12(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17012 || questId == 27012) {
			legion.setDoneCountQuest13(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17013 || questId == 27013) {
			legion.setDoneCountQuest14(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17014 || questId == 27014) {
			legion.setDoneCountQuest15(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17015 || questId == 27015) {
			legion.setDoneCountQuest16(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17016 || questId == 27016) {
			legion.setDoneCountQuest17(1);
			checkTaskComplete(legion, player);
			return true;
		} else if (questId == 17017 || questId == 27017) {
			legion.setDoneCountQuest18(1);
			checkTaskComplete(legion, player);
			return true;
		} else {
			return false;
		}
	}
	
	public static void checkTaskComplete(Legion legion, Player player) {
		if (legion == null)
			return;
		
		if (legion.getDoneCountQuest1() == maxCountQuest1 && legion.getDoneCountQuest2() == maxCountQuest2 && legion.getDoneCountQuest3() == maxCountQuest3) {
			if (legion.getLegionMembers().size() > 7) {
				completeTask1(5, legion, player);
			} else if (legion.getLegionMembers().size() > 12) {
				completeTask1(10, legion, player);
			} else if (legion.getLegionMembers().size() > 15) {
				completeTask1(15, legion, player);
			} else {
				completeTask1(1, legion, player);
			}
		} else if (legion.getDoneCountQuest4() == maxCountQuest4 && legion.getDoneCountQuest5() == maxCountQuest5 && legion.getDoneCountQuest6() == maxCountQuest6) {
			if (legion.getLegionMembers().size() > 17) {
				completeTask2(5, legion, player);
			} else if (legion.getLegionMembers().size() > 20) {
				completeTask2(10, legion, player);
			} else if (legion.getLegionMembers().size() > 24) {
				completeTask2(15, legion, player);
			} else {
				completeTask2(1, legion, player);
			}
		} else if (legion.getDoneCountQuest7() == maxCountQuest7 && legion.getDoneCountQuest8() == maxCountQuest8 && legion.getDoneCountQuest9() == maxCountQuest9) {
			if (legion.getLegionMembers().size() > 45) {
				completeTask3(5, legion, player);
			} else if (legion.getLegionMembers().size() > 55) {
				completeTask3(10, legion, player);
			} else if (legion.getLegionMembers().size() > 65) {
				completeTask3(15, legion, player);
			} else {
				completeTask3(1, legion, player);
			}
		} else if (legion.getDoneCountQuest10() == maxCountQuest10 && legion.getDoneCountQuest11() == maxCountQuest11 && legion.getDoneCountQuest12() == maxCountQuest12) {
			if (legion.getLegionMembers().size() > 73) {
				completeTask4(5, legion, player);
			} else if (legion.getLegionMembers().size() > 78) {
				completeTask4(10, legion, player);
			} else if (legion.getLegionMembers().size() > 80) {
				completeTask4(15, legion, player);
			} else {
				completeTask4(1, legion, player);
			}
		} else if (legion.getDoneCountQuest13() == maxCountQuest13 && legion.getDoneCountQuest14() == maxCountQuest14 && legion.getDoneCountQuest15() == maxCountQuest15) {
			if (legion.getLegionMembers().size() > 90) {
				completeTask5(5, legion, player);
			} else if (legion.getLegionMembers().size() > 95) {
				completeTask5(10, legion, player);
			} else if (legion.getLegionMembers().size() > 99) {
				completeTask5(15, legion, player);
			} else {
				completeTask5(1, legion, player);
			}
		} else if (legion.getDoneCountQuest16() == maxCountQuest16 && legion.getDoneCountQuest17() == maxCountQuest17 && legion.getDoneCountQuest18() == maxCountQuest18) {
			if (legion.getLegionMembers().size() > 101) {
				completeTask6(5, legion, player);
			} else if (legion.getLegionMembers().size() > 107) {
				completeTask6(10, legion, player);
			} else if (legion.getLegionMembers().size() > 110) {
				completeTask6(15, legion, player);
			} else {
				completeTask6(1, legion, player);
			}
		} else {
			return;
		}
	}
	
	public static void completeTask1(int chance, Legion legion, Player player) {
		legion.setLegionLevel(6);
		if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 46, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 23, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 17, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 1, 0, LetterType.NORMAL);
		}
	}
	
  public static void completeTask2(int chance, Legion legion, Player player) {
		legion.setLegionLevel(7);
    if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 68, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 38, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 30, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 5, 0, LetterType.NORMAL);
		}
	}
  
  public static void completeTask3(int chance, Legion legion, Player player) {
		legion.setLegionLevel(8);
    if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 106, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 64, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 53, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 11, 0, LetterType.NORMAL);
		}
	}
  
  public static void completeTask4(int chance, Legion legion, Player player) {
    if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000147, 6, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000055, 5, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 167000518, 5, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 9, 0, LetterType.NORMAL);
		}
	}
  
  public static void completeTask5(int chance, Legion legion, Player player) {
    if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000147, 10, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000051, 5, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000052, 5, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 17, 0, LetterType.NORMAL);
		}
	}
  
  public static void completeTask6(int chance, Legion legion, Player player) {
    if(chance == 15) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 100001394, 1, 0, LetterType.NORMAL);
		} else if (chance == 10) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000147, 10, 0, LetterType.NORMAL);
		} else if (chance == 5) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, 186000056, 5, 0, LetterType.NORMAL);
		} else if (chance == 1) {
			    SystemMailService.getInstance().sendMail(senderName, player.getName(), title, message, coinItemId, 34, 0, LetterType.NORMAL);
		}
	}

}
