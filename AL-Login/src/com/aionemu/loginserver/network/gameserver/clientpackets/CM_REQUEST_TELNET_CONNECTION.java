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
package com.aionemu.loginserver.network.gameserver.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;

import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.network.gameserver.GsClientPacket;
import com.aionemu.loginserver.network.gameserver.GsConnection;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_REQUEST_TELNET_CONNECTION_RESPONSE;

/**
 * @author Divinity
 */
public class CM_REQUEST_TELNET_CONNECTION extends GsClientPacket
{
    private String playerName;
    private String hashPass;

    /**
     * @param buf
     * @param client
     */
    
    /**public CM_REQUEST_TELNET_CONNECTION(GsConnection client)
    {
        super(client, 0x07);
    }*/

    /*
     * (non-Javadoc)
     * @see net.spack.commons.network.packet.BaseClientPacket#readImpl()
     */
    @Override
    protected void readImpl()
    {
        this.playerName = readS();
        this.hashPass = readS();
    }

    /*
     * (non-Javadoc)
     * @see net.spack.commons.network.packet.BaseClientPacket#runImpl()
     */
    @Override
    protected void runImpl()
    {
        int accessLevel = DAOManager.getDAO(AccountDAO.class).getPlayerAccess(this.playerName, this.hashPass);
        sendPacket(new SM_REQUEST_TELNET_CONNECTION_RESPONSE(this.playerName, accessLevel));
    }
}
