/*******************************************************************************
 * Copyright (c) 2014 Proxima Centauri SRL <info@proxima-centauri.it>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Proxima Centauri SRL <info@proxima-centauri.it> - initial API and implementation
 ******************************************************************************/
package it.proximacentauri.ienergy.da.domain;

import java.util.HashMap;
import java.util.Map;

public abstract class VoteOptions {

	private static Map<CategoryType, Map<String, Integer>> valueMap = null;

	static {
		valueMap = new HashMap<CategoryType, Map<String, Integer>>();

		// Append options for Rate
		Map<String, Integer> values = new HashMap<String, Integer>();
		values.put("1", 1);
		values.put("2", 2);
		values.put("3", 3);
		values.put("4", 4);
		values.put("5", 5);
		valueMap.put(CategoryType.Rate, values);

		values = new HashMap<String, Integer>();
		values.put("Boiling", 3);
		values.put("Hot", 2);
		values.put("Warm", 1);
		values.put("Good", 0);
		values.put("Chilly", -1);
		values.put("Cold", -2);
		values.put("Freezing", -3);

		valueMap.put(CategoryType.Temperature, values);

		values = new HashMap<String, Integer>();
		values.put("Wet", 3);
		values.put("Muggy", 2);
		values.put("Humid", 1);
		values.put("Good", 0);
		values.put("Dry", -1);
		values.put("Very dry", -2);
		values.put("Arid", -3);
		valueMap.put(CategoryType.Humidity, values);

		values = new HashMap<String, Integer>();
		values.put("Perfect", 3);
		values.put("Fresh", 2);
		values.put("Very good", 1);
		values.put("Good", 0);
		values.put("So so", -1);
		values.put("Bad", -2);
		values.put("Foul", -3);

		valueMap.put(CategoryType.AirQuality, values);

	}

	static public Map<String, Integer> getOptions(CategoryType type) {
		if (valueMap.containsKey(type))
			return valueMap.get(type);
		return null;
	}

	static public boolean validateOptions(CategoryType type, int value) {
		final Map<String, Integer> maps = getOptions(type);

		if (maps.containsValue(value))
			return true;
		return false;
	}
}
