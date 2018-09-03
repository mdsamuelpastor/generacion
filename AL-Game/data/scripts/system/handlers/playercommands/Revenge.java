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
package playercommands;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;
import com.aionemu.gameserver.world.World;

public class Revenge extends ChatCommand {

    public int adminscore;
    public int playerscore;
    public boolean admIsWin1;
    public boolean playerIsWin1;
    public boolean admIsWin2;
    public boolean playerIsWin2;
    public boolean isDraw1;
    public boolean isDraw2;

    public Revenge() {
        super("revenge");
    }

    @Override
    public void execute(final Player admin, String... params) {
        if (params == null || params.length < 1) {
            PacketSendUtility.sendMessage(admin, "syntax //revenge <Character>");
            return;
        }

        final Player player = World.getInstance().findPlayer(Util.convertName(params[0]));
        if (player == null) {
            PacketSendUtility.sendMessage(admin, "The specified player is not online.");
            return;
        }

        if (player == admin) {
            PacketSendUtility.sendMessage(admin, "Cannot use this command on yourself.");
            return;
        }
        String message = "Player [" + admin.getName() + "] want's to fight with you, do you accept the invitation?";
        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
                start(player, admin);
            }

            @Override
            public void denyRequest(Creature requester, Player responder) {
            }
        };
    }

    @Override
    public void onFail(Player player, String message) {
        PacketSendUtility.sendMessage(player, "syntax //revenge <PlayerName>");
    }

    private void start(final Player admin, final Player player) {
        TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        PacketSendUtility.sendMessage(player, "[Revenge]: You have 15 seconds to prepare for battle!\nAfter 15 seconds you will be teleported to the enemy!");
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                TeleportService2.teleportTo(admin, player.getWorldId(), player.getX(), player.getY(), player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                PacketSendUtility.sendMessage(player, "[Revenge]The battle has begun!");
                if (player.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    adminscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]You lost the battle! Player" + admin.getName() + "won 1 point");
                    PacketSendUtility.sendMessage(admin, "[Revenge]Congratulations! You Won, you got one point into your account");
                    admIsWin1 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                } else if (player.getLifeStats().isAlreadyDead() & admin.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    PacketSendUtility.sendMessage(player, "[Revenge]There was an tie, no one gets points");
                    PacketSendUtility.sendMessage(admin, "[Revenge]There was an tie, no one gets points");
                    isDraw1 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                } else if (admin.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    playerscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]Congratulations You Won! You got one extra point!");
                    PacketSendUtility.sendMessage(admin, "[Revenge]You lose, your opponent gets the extra point!");
                    playerIsWin1 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (player.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                }
                if (admin.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                }

            }
        }, 18000);

    }

    private void stage2(final Player admin, final Player player) {
        TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        PacketSendUtility.sendMessage(player, "[Revenge]: You have 15 seconds to prepare for battle!\nAfter 15 seconds you will be teleported to the enemy!");
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                TeleportService2.teleportTo(admin, player.getWorldId(), player.getX(), player.getY(), player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                PacketSendUtility.sendMessage(player, "[Revenge]The battle has begun! Round 2");
                if (player.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    adminscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]You lost the battle! Player" + admin.getName() + "got one point");
                    PacketSendUtility.sendMessage(admin, "[Revenge]Congratulations! You Won, you got one point into your account");
                    admIsWin2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (player.getLifeStats().isAlreadyDead() & admin.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    PacketSendUtility.sendMessage(player, "[Revenge]There was an tie, no one gets points");
                    PacketSendUtility.sendMessage(admin, "[Revenge]There was an tie, no one gets points");
                    isDraw2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (admin.getLifeStats().getCurrentHp() == 0) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    playerscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]Congratulations You Won! You got one extra point!");
                    PacketSendUtility.sendMessage(admin, "[Revenge]You lose, your opponent gets the extra point!");
                    playerIsWin2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (admIsWin2 || playerIsWin2 || isDraw2) {
                    stage3(player, admin);
                }
                if (player.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                }
                if (admin.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                }


            }
        }, 18000);

    }

    private void stage3(final Player admin, final Player player) {
        TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
        PacketSendUtility.sendMessage(player, "[Revenge]: You have 15 seconds to prepare for battle!\nAfter 15 seconds you will be teleported to the enemy!");
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                TeleportService2.teleportTo(admin, player.getWorldId(), player.getX(), player.getY(), player.getZ(), (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                PacketSendUtility.sendMessage(player, "[Revenge]The battle has begun! Round 3");
                if (player.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    adminscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]You lost the battle! Player" + admin.getName() + "got one point");
                    PacketSendUtility.sendMessage(admin, "[Revenge]Congratulations! You Won, you got one point into your account");
                    admIsWin2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (player.getLifeStats().isAlreadyDead() & admin.getLifeStats().isAlreadyDead()) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    PlayerReviveService.revive(player, 100, 100, false, 0);
                    PacketSendUtility.sendMessage(player, "[Revenge]There was an tie, no one gets points");
                    PacketSendUtility.sendMessage(admin, "[Revenge]There was an tie, no one gets points");
                    isDraw2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (admin.getLifeStats().getCurrentHp() == 0) {
                    PlayerReviveService.revive(admin, 100, 100, false, 0);
                    playerscore += 1;
                    PacketSendUtility.sendMessage(player, "[Revenge]Congratulations You Won! You got one extra point!");
                    PacketSendUtility.sendMessage(admin, "[Revenge]You lose, your opponent gets the extra point!");
                    playerIsWin2 = true;
                    restore(player);
                    restore(admin);
                    TeleportService2.teleportTo(player, 510010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                    TeleportService2.teleportTo(admin, 520010000, 256f, 256f, 49f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
                }
                if (admIsWin2 || playerIsWin2 || isDraw2) {
                    onEnd(player, admin);
                }
                if (player.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + player.getName() + "is out of battle!\nThe winner is " + admin.getName());
                }
                if (admin.getWorldId() != 510010000) {
                    PacketSendUtility.sendMessage(player, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                    PacketSendUtility.sendMessage(admin, "[Revenge]: Player" + admin.getName() + "is out of battle!\nThe winner is " + player.getName());
                }


            }
        }, 19000);

    }

    private void onEnd(final Player player, final Player admin) {
        PacketSendUtility.sendMessage(admin, "[Revenge]:The results of the battle " + admin.getName() + "vs" + player.getName());
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {

                if (adminscore == playerscore) {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " <DRAW> !");
                    PacketSendUtility.sendMessage(player, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " <DRAW> !");
                }
                if (adminscore > playerscore) {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " - " + admin.getName() + " wins!");
                    PacketSendUtility.sendMessage(player, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " - " + admin.getName() + " wins!");
                } else {
                    PacketSendUtility.sendMessage(admin, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " - " + player.getName() + " wins!");
                    PacketSendUtility.sendMessage(player, "[Revenge]: The results of the battle " + admin.getName() + "vs" + player.getName() + " - " + player.getName() + " wins!");
                }
                TeleportService2.moveToBindLocation(player, true);
                TeleportService2.moveToBindLocation(admin, true);
                admIsWin1 = false;
                playerIsWin1 = false;
                admIsWin2 = false;
                playerIsWin2 = false;
                isDraw1 = false;
                isDraw2 = false;
            }
        }, 15000);

    }

    void restore(Player player) {
        player.getLifeStats().restoreMp();
        player.getLifeStats().restoreHp();
        player.getLifeStats().restoreFp();
    }
}