/*
 * This file is part of EasyGaming-Development <http://easy-gaming.de>.
 *
 * EG-DEV <http://www.easy-gaming.de> is free software: you
 * can  redistribute  it and/or modify it under the terms
 * of  the GNU General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * EG-DEV <http://www.easy-gaming.de> is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without  even  the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See  the  GNU General Public License for more details.
 * You  should  have  received  a copy of the GNU General
 * Public License along with EG-DEV <http://www.easy-gaming.de>.
 * If not,see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.gameserver.serverpackets;

import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.GsServerPacket;

/**
 * @author Divinity
 */
public class SM_REQUEST_TELNET_CONNECTION_RESPONSE extends GsServerPacket
{
    private String playerName;
    private int    accessLevel;

    public SM_REQUEST_TELNET_CONNECTION_RESPONSE(String playerName, int accessLevel)
    {
        this.playerName = playerName;
        this.accessLevel = accessLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(GsConnection con)
    {
        writeC(6);

        writeS(playerName);
        writeD(accessLevel);
    }
}
