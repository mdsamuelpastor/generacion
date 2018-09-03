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
package com.aionemu.gameserver.configs.shedule;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.aionemu.commons.utils.xml.JAXBUtil;

/**
 * @author Cloudious
 */
@XmlRootElement(name = "crazy_daeva_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrazySchedule {
	
	@XmlElement(name = "schedule", required = true)
	private List<Schedule> schedule;

	public List<Schedule> getScheduleList() {
		return schedule;
	}

	public void setScheduleList(List<Schedule> scheduleList) {
		this.schedule = scheduleList;
	}

	public static CrazySchedule load() {
		CrazySchedule ss;
		try {
			String xml = FileUtils.readFileToString(new File("./config/shedule/crazy_daeva_schedule.xml"));
			ss = (CrazySchedule) JAXBUtil.deserialize(xml, CrazySchedule.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to initialize Crazy Daeva Schedule...", e);
		}
		return ss;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "schedule")
	public static class Schedule {
		
		@XmlAttribute(required = true)
		private int id;

		@XmlElement(name = "scheduleTime", required = true)
		private List<String> scheduleTime;
		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<String> getScheduleTimes() {
			return scheduleTime;
		}

		public void setScheduleTimes(List<String> scheduleTimes) {
			this.scheduleTime = scheduleTimes;
		}
	}
}