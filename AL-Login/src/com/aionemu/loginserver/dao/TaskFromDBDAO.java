package com.aionemu.loginserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.loginserver.taskmanager.trigger.TaskFromDBTrigger;
import java.util.ArrayList;

public abstract class TaskFromDBDAO implements DAO {

	public abstract ArrayList<TaskFromDBTrigger> getAllTasks();

	public final String getClassName() {
		return TaskFromDBDAO.class.getName();
	}
}
