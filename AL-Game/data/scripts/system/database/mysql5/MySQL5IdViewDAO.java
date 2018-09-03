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
package mysql5;

import com.aionemu.commons.database.DatabaseFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.aionemu.gameserver.dao.IdViewDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author GoodT 	
*/
public class MySQL5IdViewDAO extends IdViewDAO {

    private static final Logger log = LoggerFactory.getLogger(MySQL5IdViewDAO.class);
    public static final String SELECT_QUERY = "SELECT `players`.`id` AS `id` from `players` UNION SELECT `item_unique_id` FROM `inventory` UNION SELECT `id` FROM `legions` UNION SELECT `item_unique_id` FROM `player_registered_items` WHERE item_unique_id <> 0 UNION SELECT `mail_unique_id` FROM `mail` UNION SELECT `guide_id` FROM `guides` UNION SELECT DISTINCT `id` FROM `houses`";

    @Override
    public int[] getUsedIDs() {
        Connection con = null;
        try {
            con = DatabaseFactory.getConnection(); 
            con.setReadOnly(false);           

            PreparedStatement statement = con.prepareStatement(SELECT_QUERY, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            int[] ids = new int[count];
            for (int i = 0; i < count; i++) {
                rs.next();
                ids[i] = rs.getInt("id");
            }
            statement.close();
            return ids;
        } catch (SQLException e) {
            log.error("Can't get list of id's from union query", e);
        } finally {
            DatabaseFactory.close(con);
        }

        return new int[0];
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }
}
