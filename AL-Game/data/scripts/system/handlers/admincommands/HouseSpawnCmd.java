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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import com.aionemu.gameserver.dataholders.HouseNpcsData;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.housing.HousingLand;
import com.aionemu.gameserver.model.templates.spawns.HouseSpawn;
import com.aionemu.gameserver.model.templates.spawns.HouseSpawns;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnType;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.ChatCommand;

/**
 * @author Rolandas
 */
public class HouseSpawnCmd extends ChatCommand {

	public HouseSpawnCmd() {
		super("housespawn");
	}

	@Override
	public void execute(Player admin, String... params) {

		if (params.length == 0) {
			PacketSendUtility.sendMessage(admin, "Syntax: //housespawn <new | rotate>");
			return;
		}

		float rotateRad = 0f;
		boolean rotating = false;
		if (params[0].equals("rotate")) {
			if (params.length != 2) {
				PacketSendUtility.sendMessage(admin, "Syntax: //housespawn rotate <degrees>");
				return;
			}
			try {
				rotateRad = Float.parseFloat(params[1]);
				rotateRad *= 0.0174532925f;
				rotating = true;
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "Only float numbers!!!");
				return;
			}
		}

		List<House> houses = HousingService.getInstance().getCloseHousesForPlayer(admin);
		if (houses.size() == 0) {
			PacketSendUtility.sendMessage(admin, "You must be inside house!!!");
			return;
		}

		double minDistance = Double.MAX_VALUE;
		House spawnHouse = null;

		for (House house : houses) {
			double distance = MathUtil.getDistance(admin.getX(), admin.getY(), house.getAddress().getX(), house.getAddress().getY());
			if (distance < minDistance) {
				minDistance = distance;
				spawnHouse = house;
			}
		}
		if (minDistance > 10) {
			PacketSendUtility.sendMessage(admin, "You must be inside house!!!");
			return;
		}

		byte h = admin.getHeading();
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(h)) + rotateRad;
		float x, y;
		byte heading;
		if (h > 60)
			heading = (byte) (h - 60);
		else
			heading = (byte) (h + 60);

		HousingLand land = spawnHouse.getLand();
		HouseNpcsData data = null;
		try {
			data = loadExistingData(admin);
		}
		catch (IOException e) {
		}

		Npc npc = null;

		if (data == null) {
			if (rotating) {
				PacketSendUtility.sendMessage(admin, "Nothing to rotate!!!");
				return;
			}
			data = new HouseNpcsData();
		}
		else {
			List<HouseSpawn> existing = data.getSpawnsByAddress(spawnHouse.getAddress().getId());
			if (existing != null && existing.size() > 0) {
				if (!rotating) {
					PacketSendUtility.sendMessage(admin, "Already has spawns for the house!!!");
					return;
				}
				npc = spawnHouse.getButler();
				if (npc != null && npc.isSpawned())
					npc.getController().onDelete();
				npc = spawnHouse.getTeleport();
				if (npc != null && npc.isSpawned())
					npc.getController().onDelete();
				npc = spawnHouse.getCurrentSign();
				if (npc != null && npc.isSpawned())
					npc.getController().onDelete();

				for (int i = data.getHouseSpawns().size() - 1; i >= 0; i++) {
					HouseSpawns oldSpawns = data.getHouseSpawns().get(i);
					if (oldSpawns.getAddress() == spawnHouse.getAddress().getId()) {
						data.getHouseSpawns().remove(i);
						break;
					}
				}
			}
		}

		HouseSpawns spawns = new HouseSpawns();
		spawns.setAddress(spawnHouse.getAddress().getId());
		HouseSpawn spawn = null;

		// Butler at left side, looks at you
		spawn = new HouseSpawn();
		x = (float) (Math.cos(Math.PI * 0.5d + radian) * 2);
		y = (float) (Math.sin(Math.PI * 0.5d + radian) * 2);
		spawn.setX(admin.getX() + x);
		spawn.setY(admin.getY() + y);
		spawn.setH(heading);
		npc = spawnAndAdd(spawnHouse, spawns, SpawnType.MANAGER, land.getManagerNpcId(), spawn, admin);
		spawnHouse.setSpawn(SpawnType.MANAGER, npc);

		// Relational Crystal at right side, looks at you
		spawn = new HouseSpawn();
		x = (float) (Math.cos(Math.PI * 1.5d + radian) * 2);
		y = (float) (Math.sin(Math.PI * 1.5d + radian) * 2);
		spawn.setX(admin.getX() + x);
		spawn.setY(admin.getY() + y);
		spawn.setH(heading);
		npc = spawnAndAdd(spawnHouse, spawns, SpawnType.TELEPORT, land.getTeleportNpcId(), spawn, admin);
		spawnHouse.setSpawn(SpawnType.TELEPORT, npc);

		// Sign is almost in front, looks in the same direction
		spawn = new HouseSpawn();
		x = (float) (Math.cos(radian) * 10.2);
		y = (float) (Math.sin(radian) * 10.2);
		spawn.setX(admin.getX() + x);
		spawn.setY(admin.getY() + y);
		spawn.setH(h);
		npc = spawnAndAdd(spawnHouse, spawns, SpawnType.SIGN, land.getHomeSignNpcId(), spawn, admin);
		spawnHouse.setSpawn(SpawnType.SIGN, npc);

		try {
			data.getHouseSpawns().add(spawns);
			if (saveSpawns(admin, data))
				PacketSendUtility.sendMessage(admin, "House npcs spawned and saved.");
		}
		catch (IOException e) {
			PacketSendUtility.sendMessage(admin, "File write error!");
		}
	}

	private Npc spawnAndAdd(House house, HouseSpawns spawns, SpawnType type, int npcId, HouseSpawn spawn, Player admin) {
		spawn.setType(type);
		spawns.getSpawns().add(spawn);
		float z = 0;
		if (HouseType.HOUSE.toString().equals(house.getBuilding().getSize())) {
			z = type == SpawnType.SIGN ? admin.getZ() - 0.7f : admin.getZ();
		}
		if (HouseType.MANSION.toString().equals(house.getBuilding().getSize())) {
			z = type == SpawnType.SIGN ? admin.getZ() - 1 : admin.getZ();
		}
		if (HouseType.ESTATE.toString().equals(house.getBuilding().getSize())) {
			z = type == SpawnType.SIGN ? admin.getZ() - 1.35f : admin.getZ();
		}
		if (HouseType.PALACE.toString().equals(house.getBuilding().getSize())) {
			z = type == SpawnType.SIGN ? admin.getZ() - 2 : admin.getZ();
		}
		spawn.setZ(z);

		SpawnTemplate template = SpawnEngine.addNewSingleTimeSpawn(admin.getWorldId(), npcId, spawn.getX(), spawn.getY(), z, spawn.getH());
		return VisibleObjectSpawner.spawnHouseNpc(template, admin.getInstanceId(), house, "");
	}

	private boolean saveSpawns(Player admin, HouseNpcsData data) throws IOException {
		File xml = new File("./data/static_data/housing/house_npcs.xml");
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		JAXBContext jc = null;

		try {
			schema = sf.newSchema(new File("./data/static_data/housing/house_npcs.xsd"));
			jc = JAXBContext.newInstance(HouseNpcsData.class);
		}
		catch (Exception e) {
			PacketSendUtility.sendMessage(admin, "Failed to load XSD!");
			return false;
		}

		Collections.sort(data.getHouseSpawns());

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(xml);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setSchema(schema);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(data, fos);
		}
		catch (Exception e) {
			PacketSendUtility.sendMessage(admin, "Could not save XML file!");
			return false;
		}
		finally {
			if (fos != null)
				fos.close();
		}
		return true;
	}

	private HouseNpcsData loadExistingData(Player admin) throws IOException {
		File xml = new File("./data/static_data/housing/house_npcs.xml");
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		JAXBContext jc = null;

		try {
			schema = sf.newSchema(new File("./data/static_data/housing/house_npcs.xsd"));
			jc = JAXBContext.newInstance(HouseNpcsData.class);
		}
		catch (Exception e) {
			PacketSendUtility.sendMessage(admin, "Failed to load XSD!");
			return null;
		}

		FileInputStream fin = null;
		if (xml.exists()) {
			try {
				fin = new FileInputStream(xml);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				unmarshaller.setSchema(schema);
				return (HouseNpcsData) unmarshaller.unmarshal(fin);
			}
			catch (Exception e) {
				PacketSendUtility.sendMessage(admin, "Could not load old XML file!");
				return null;
			}
			finally {
				if (fin != null)
					fin.close();
			}
		}
		return null;
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //housespawn <new | rotate>");
		PacketSendUtility.sendMessage(player, "Note: You must be inside house and stand in front of doors.");
	}

}
