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
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.HousesDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.house.HouseStatus;
import com.aionemu.gameserver.model.templates.housing.Building;
import com.aionemu.gameserver.model.templates.housing.BuildingType;
import com.aionemu.gameserver.model.templates.housing.HouseAddress;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.housing.PartType;

/**
 * @author Rolandas, Maestross
 */
public class MySQL5HousesDAO extends HousesDAO {

    private static final Logger log = LoggerFactory.getLogger(MySQL5HousesDAO.class);

    private static final String SELECT_HOUSES_QUERY = "SELECT * FROM houses WHERE address <> 2001 AND address <> 3001";
    private static final String SELECT_STUDIOS_QUERY = "SELECT * FROM houses WHERE address = 2001 OR address = 3001";
    private static final String STORE_HOUSE_APPEARANCE_QUERY = "INSERT INTO houses " + "(id, building_id, address, roof, outwall, inwall, infloor, frame, door, garden, fence, addon, status) VALUES "
        + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String STORE_PLAYER_HOUSE_QUERY = "UPDATE houses SET roof=?, outwall=?, inwall=?, infloor=?, frame=?, door=?, garden=?, fence=?, addon=?,"
        + " player_id=?, acquire_time=?, settings=?, status=?, fee_paid=?, next_pay=?, sell_started=? WHERE id=? AND building_id=?";
    private static final String STORE_STUDIO_QUERY = "INSERT INTO houses (id, address, building_id, inwall, infloor, door, player_id, acquire_time, settings, status, fee_paid, next_pay, sell_started) "
        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_STUDIO_QUERY = "DELETE FROM houses WHERE player_id=?";

    @Override
    public int[] getUsedIDs() {
        PreparedStatement statement = DB.prepareStatement("SELECT DISTINCT id FROM houses", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        try {
            ResultSet rs = statement.executeQuery();
            rs.last();
            int count = rs.getRow();
            rs.beforeFirst();
            int[] ids = new int[count];
            for (int i = 0; i < count; i++) {
                rs.next();
                ids[i] = rs.getInt(1);
            }
            return ids;
        }
        catch (SQLException e) {
            log.error("Can't get list of id's from houses table", e);
        }
        finally {
            DB.close(statement);
        }

        return new int[0];
    }

    @Override
    public boolean supports(String databaseName, int majorVersion, int minorVersion) {
        return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
    }

    @Override
    public boolean isIdUsed(int houseObjectId) {
        PreparedStatement s = DB.prepareStatement("SELECT count(id) as cnt FROM houses WHERE ? = houses.id");
        try {
            s.setInt(1, houseObjectId);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
        catch (SQLException e) {
            log.error("Can't check if house " + houseObjectId + ", is used, returning possitive result", e);
            return true;
        }
        finally {
            DB.close(s);
        }
    }

    @Override
    public void storeNewHouse(House house) {

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(STORE_HOUSE_APPEARANCE_QUERY);
            stmt.setInt(1, house.getObjectId());
            stmt.setInt(2, house.getBuilding().getId());
            stmt.setInt(3, house.getAddress().getId());

            // roof, outwall, inwall, infloor, frame, door, garden, fence, addon
            if (house.getRenderPart(PartType.ROOF) != null)
                stmt.setInt(4, house.getRenderPart(PartType.ROOF).getTemplate().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            if (house.getRenderPart(PartType.OUTWALL) != null)
                stmt.setInt(5, house.getRenderPart(PartType.OUTWALL).getTemplate().getId());
            else
                stmt.setNull(5, Types.INTEGER);

            if (house.getRenderPart(PartType.INWALL_ANY) != null)
                stmt.setInt(6, house.getRenderPart(PartType.INWALL_ANY).getTemplate().getId());
            else
                stmt.setNull(6, Types.INTEGER);

            if (house.getRenderPart(PartType.INFLOOR_ANY) != null)
                stmt.setInt(7, house.getRenderPart(PartType.INFLOOR_ANY).getTemplate().getId());
            else
                stmt.setNull(7, Types.INTEGER);

            if (house.getRenderPart(PartType.FRAME) != null)
                stmt.setInt(8, house.getRenderPart(PartType.FRAME).getTemplate().getId());
            else
                stmt.setNull(8, Types.INTEGER);

            if (house.getRenderPart(PartType.DOOR) != null)
                stmt.setInt(9, house.getRenderPart(PartType.DOOR).getTemplate().getId());
            else
                stmt.setNull(9, Types.INTEGER);

            if (house.getRenderPart(PartType.GARDEN) != null)
                stmt.setInt(10, house.getRenderPart(PartType.GARDEN).getTemplate().getId());
            else
                stmt.setNull(10, Types.INTEGER);

            if (house.getRenderPart(PartType.FENCE) != null)
                stmt.setInt(11, house.getRenderPart(PartType.FENCE).getTemplate().getId());
            else
                stmt.setNull(11, Types.INTEGER);

            if (house.getRenderPart(PartType.ADDON) != null)
                stmt.setInt(12, house.getRenderPart(PartType.ADDON).getTemplate().getId());
            else
                stmt.setNull(12, Types.INTEGER);

            stmt.setString(13, house.getStatus().toString());

            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Could not store new house data. " + e.getMessage(), e);
            return;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return;

    }

    @Override
    public void storeHouse(House house) {

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(STORE_PLAYER_HOUSE_QUERY);

            // roof, outwall, inwall, infloor, frame, door, garden, fence, addon
            if (house.getDefaultPart(PartType.ROOF) != null)
                stmt.setInt(1, house.getDefaultPart(PartType.ROOF).getTemplate().getId());
            else
                stmt.setNull(1, Types.INTEGER);

            if (house.getRenderPart(PartType.OUTWALL) != null)
                stmt.setInt(2, house.getDefaultPart(PartType.OUTWALL).getTemplate().getId());
            else
                stmt.setNull(2, Types.INTEGER);

            if (house.getRenderPart(PartType.INWALL_ANY) != null)
                stmt.setInt(3, house.getDefaultPart(PartType.INWALL_ANY).getTemplate().getId());
            else
                stmt.setNull(3, Types.INTEGER);

            if (house.getRenderPart(PartType.INFLOOR_ANY) != null)
                stmt.setInt(4, house.getDefaultPart(PartType.INFLOOR_ANY).getTemplate().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            if (house.getRenderPart(PartType.FRAME) != null)
                stmt.setInt(5, house.getDefaultPart(PartType.FRAME).getTemplate().getId());
            else
                stmt.setNull(5, Types.INTEGER);

            if (house.getRenderPart(PartType.DOOR) != null)
                stmt.setInt(6, house.getDefaultPart(PartType.DOOR).getTemplate().getId());
            else
                stmt.setNull(6, Types.INTEGER);

            if (house.getRenderPart(PartType.GARDEN) != null)
                stmt.setInt(7, house.getDefaultPart(PartType.GARDEN).getTemplate().getId());
            else
                stmt.setNull(7, Types.INTEGER);

            if (house.getRenderPart(PartType.FENCE) != null)
                stmt.setInt(8, house.getDefaultPart(PartType.FENCE).getTemplate().getId());
            else
                stmt.setNull(8, Types.INTEGER);

            if (house.getRenderPart(PartType.ADDON) != null)
                stmt.setInt(9, house.getDefaultPart(PartType.ADDON).getTemplate().getId());
            else
                stmt.setNull(9, Types.INTEGER);

            stmt.setInt(10, house.getOwnerId());
            if (house.getAcquiredTime() == null)
                stmt.setNull(11, Types.TIMESTAMP);
            else
                stmt.setTimestamp(11, house.getAcquiredTime());

            stmt.setInt(12, house.getSettingFlags());
            stmt.setString(13, house.getStatus().toString());
            stmt.setInt(14, house.isFeePaid() ? 1 : 0);

            if (house.getNextPay() == null)
                stmt.setNull(15, Types.TIMESTAMP);
            else
                stmt.setTimestamp(15, house.getNextPay());

            if (house.getSellStarted() == null)
                stmt.setNull(16, Types.TIMESTAMP);
            else
                stmt.setTimestamp(16, house.getSellStarted());

            stmt.setInt(17, house.getObjectId());
            stmt.setInt(18, house.getBuilding().getId());

            stmt.execute();
            stmt.close();
        }
        catch (Exception e) {
            log.error("Could not store house data. " + e.getMessage(), e);
            return;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return;

    }

    @Override
    public void insertHouse(House house) {

        Connection con = null;
        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(STORE_STUDIO_QUERY);

            stmt.setInt(1, house.getObjectId());
            stmt.setInt(2, house.getAddress().getId());
            stmt.setInt(3, house.getBuilding().getId());

            // inwall, infloor, door
            if (house.getRenderPart(PartType.INWALL_ANY) != null)
                stmt.setInt(4, house.getDefaultPart(PartType.INWALL_ANY).getTemplate().getId());
            else
                stmt.setNull(4, Types.INTEGER);

            if (house.getRenderPart(PartType.INFLOOR_ANY) != null)
                stmt.setInt(5, house.getDefaultPart(PartType.INFLOOR_ANY).getTemplate().getId());
            else
                stmt.setNull(5, Types.INTEGER);

            if (house.getRenderPart(PartType.DOOR) != null)
                stmt.setInt(6, house.getDefaultPart(PartType.DOOR).getTemplate().getId());
            else
                stmt.setNull(6, Types.INTEGER);

            stmt.setInt(7, house.getOwnerId());
            if (house.getAcquiredTime() == null)
                stmt.setNull(8, Types.TIMESTAMP);
            else
                stmt.setTimestamp(8, house.getAcquiredTime());

            stmt.setInt(9, house.getSettingFlags());
            stmt.setString(10, house.getStatus().toString());
            stmt.setInt(11, house.isFeePaid() ? 1 : 0);

            if (house.getNextPay() == null)
                stmt.setNull(12, Types.TIMESTAMP);
            else
                stmt.setTimestamp(12, house.getNextPay());

            if (house.getSellStarted() == null)
                stmt.setNull(13, Types.TIMESTAMP);
            else
                stmt.setTimestamp(13, house.getSellStarted());

            stmt.execute();
            stmt.close();
            house.setPersistentState(PersistentState.UPDATED);
        }
        catch (Exception e) {
            log.error("Could not store studio data. " + e.getMessage(), e);
            return;
        }
        finally {
            DatabaseFactory.close(con);
        }
        return;

    }

    @Override
    public Map<Integer, Map<Integer, House>> loadHouses(Collection<HousingLand> lands, boolean studios) {
        Map<Integer, Map<Integer, House>> houses = new HashMap<Integer, Map<Integer, House>>();
        Map<Integer, HouseAddress> addressesById = new HashMap<Integer, HouseAddress>();
        Map<Integer, List<Building>> buildingsForAddress = new HashMap<Integer, List<Building>>();
        for (HousingLand land : lands) {
            for (HouseAddress address : land.getAddresses()) {
                addressesById.put(address.getId(), address);
                buildingsForAddress.put(address.getId(), land.getBuildings());
            }
        }

        HashMap<Integer, Integer> addressHouseIds = new HashMap<Integer, Integer>();

        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = DatabaseFactory.getConnection();
            stmt = con.prepareStatement(studios ? SELECT_STUDIOS_QUERY : SELECT_HOUSES_QUERY);
            ResultSet rset = stmt.executeQuery();
            int templateId = -1;

            while (rset.next()) {
                int houseId = rset.getInt("id");
                int buildingId = rset.getInt("building_id");
                HouseAddress address = addressesById.get(rset.getInt("address"));
                Building building = null;
                for (Building b : buildingsForAddress.get(address.getId())) {
                    if (b.getId() == buildingId) {
                        building = b;
                        break;
                    }
                }

                Map<PartType, Integer> parts = new HashMap<PartType, Integer>();
                int partId = rset.getInt(PartType.ROOF.getDbName());
                if (partId > 0)
                    parts.put(PartType.ROOF, partId);
                partId = rset.getInt(PartType.OUTWALL.getDbName());
                if (partId > 0)
                    parts.put(PartType.OUTWALL, partId);
                partId = rset.getInt(PartType.INFLOOR_ANY.getDbName());
                if (partId > 0)
                    parts.put(PartType.INFLOOR_ANY, partId);
                partId = rset.getInt(PartType.INWALL_ANY.getDbName());
                if (partId > 0)
                    parts.put(PartType.INWALL_ANY, partId);
                partId = rset.getInt(PartType.DOOR.getDbName());
                if (partId > 0)
                    parts.put(PartType.DOOR, partId);
                partId = rset.getInt(PartType.GARDEN.getDbName());
                if (partId > 0)
                    parts.put(PartType.GARDEN, partId);
                partId = rset.getInt(PartType.FENCE.getDbName());
                if (partId > 0)
                    parts.put(PartType.FENCE, partId);
                partId = rset.getInt(PartType.FRAME.getDbName());
                if (partId > 0)
                    parts.put(PartType.FRAME, partId);
                partId = rset.getInt(PartType.ADDON.getDbName());
                if (partId > 0)
                    parts.put(PartType.ADDON, partId);

                int objectId = 0;
                House house = null;
                if (addressHouseIds.containsKey(address.getId())) {
                    objectId = addressHouseIds.get(address.getId());
                    if (houseId != objectId) {
                        log.warn("House address " + address.getId() + " should have the same id for buildings!");
                    }
                    house = new House(objectId, building, address, 0);
                }
                else {
                    house = new House(houseId, building, address, 0);
                    if (building.getType() == BuildingType.PERSONAL_FIELD)
                        addressHouseIds.put(address.getId(), houseId);
                }

                for (Entry<PartType, Integer> entry : parts.entrySet()) {
                    HouseDecoration rendered = new HouseDecoration(0, entry.getValue());
                    house.getRegistry().putDefaultPart(rendered);
                    rendered.setPersistentState(PersistentState.UPDATED);
                }

                house.setOwnerId(rset.getInt("player_id"));
                house.setAcquiredTime(rset.getTimestamp("acquire_time"));
                house.setSettingFlags(rset.getInt("settings"));
                house.setStatus(HouseStatus.valueOf(rset.getString("status")));
                house.setFeePaid(rset.getInt("fee_paid") != 0);
                house.setNextPay(rset.getTimestamp("next_pay"));
                house.setSellStarted(rset.getTimestamp("sell_started"));
                int id = 0;
                if (studios) {
                    int ownerId = house.getOwnerId();
                    if (ownerId == 0) {
                        ownerId = templateId--;
                        house.setOwnerId(ownerId);
                    }
                    id = ownerId;
                }
                else {
                    id = address.getId();
                }

                if (houses.get(id) == null) {
                    houses.put(id, new HashMap<Integer, House>());
                }
                houses.get(id).put(building.getId(), house);
            }
            rset.close();
        }
        catch (Exception e) {
            log.error("Could not restore House data from DB: " + e.getMessage(), e);
        }
        finally {
            DatabaseFactory.close(stmt, con);
        }
        return houses;
    }

    @Override
    public void deleteStudio(int playerId) {
        Connection con = null;

        try {
            con = DatabaseFactory.getConnection();
            PreparedStatement stmt = con.prepareStatement(DELETE_STUDIO_QUERY);

            stmt.setInt(1, playerId);
            stmt.execute();
        }
        catch (SQLException e) {
            log.error("Delete Studio failed", e);
        }
        finally {
            DatabaseFactory.close(con);
        }
    }

}
