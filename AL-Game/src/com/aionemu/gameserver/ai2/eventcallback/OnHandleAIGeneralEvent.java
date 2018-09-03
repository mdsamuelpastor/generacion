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
package com.aionemu.gameserver.ai2.eventcallback;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.event.AIEventType;

/**
 * Callback that is broadcasted when general ai event occurs.
 * 
 * @author SoulKeeper
 */
@SuppressWarnings("rawtypes")
public abstract class OnHandleAIGeneralEvent implements Callback<AbstractAI> {

	@Override
	public CallbackResult beforeCall(AbstractAI obj, Object[] args) {
		return CallbackResult.newContinue();
	}

	@Override
	public CallbackResult afterCall(AbstractAI obj, Object[] args, Object methodResult) {
		AIEventType eventType = (AIEventType) args[0];
		onAIHandleGeneralEvent(obj, eventType);
		return CallbackResult.newContinue();
	}

	@Override
	public Class<? extends Callback> getBaseClass() {
		return OnHandleAIGeneralEvent.class;
	}

	protected abstract void onAIHandleGeneralEvent(AbstractAI obj, AIEventType eventType);
}
