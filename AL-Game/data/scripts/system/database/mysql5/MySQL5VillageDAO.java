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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.VillageDAO;
import com.aionemu.gameserver.model.village.VillageLocation;

/**
 * 
 * @author -Enomine-
 *
 */

public class MySQL5VillageDAO  extends VillageDAO{
	
	private static final Logger log = LoggerFactory.getLogger(MySQL5VillageDAO.class);
	
	public static final String SELECT_QUERY = "SELECT `id` FROM `village_locations`";
	public static final String INSERT_QUERY = "INSERT INTO `village_locations` (`id`) VALUES(?)";

	

	public boolean loadVillageLocations(final Map<Integer, VillageLocation> locations) {
		boolean success = true;
		Connection con = null;
		List<Integer> loaded = new ArrayList<Integer>();

		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				VillageLocation loc = locations.get(resultSet.getInt("id"));
				loaded.add(loc.getVillageId());
			}
			resultSet.close();
		}
		catch (Exception e) {
			log.warn("Error loading Village informaiton from database: " + e.getMessage(), e);
			success = false;
		}
		finally {
			DatabaseFactory.close(stmt, con);
		}

		for (Map.Entry<Integer, VillageLocation> entry : locations.entrySet()) {
			VillageLocation sLoc = entry.getValue();
			if (!loaded.contains(sLoc.getVillageId())) {
				insertVillageLocation(sLoc);
			}
		}

		return success;
	}
	
	private boolean insertVillageLocation(final VillageLocation villageLocation) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, villageLocation.getVillageId());
			stmt.execute();
		}
		catch (Exception e) {
			log.error("Error insert Siege Location: " + villageLocation.getVillageId(), e);
			return false;
		}
		finally {
			DatabaseFactory.close(stmt, con);

		}
		return true;
	}
	

	public boolean supports(String databaseName, int majorVersion, int minorVersion) {
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
