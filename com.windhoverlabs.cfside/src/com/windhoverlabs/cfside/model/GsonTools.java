package com.windhoverlabs.cfside.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

public class GsonTools {
	public static enum ConflictStrategy {
		THROW_EXCEPTION, PREFER_FIRST_OBJECT, PREFER_SECOND_OBJECT, PREFER_NON_NULL;
	}
	
	public static class JsonObjectExtensionConflictExeception extends Exception {
		public JsonObjectExtensionConflictExeception(String message) {
			super(message);
		}
	}
	
	public static void extendJsonObject(JsonObject destinationObject, ConflictStrategy conflictResolutionStrategy, JsonObject ... objs) throws JsonObjectExtensionConflictExeception {
		for (JsonObject obj : objs) {
			extendJsonObject(destinationObject, obj, conflictResolutionStrategy);
		}
	}
	
	private static void extendJsonObject(JsonObject leftObj, JsonObject rightObj, ConflictStrategy conflictStrategy) throws JsonObjectExtensionConflictExeception {
		for (Map.Entry<String, JsonElement> rightEntry : rightObj.entrySet()) {
			String rightKey = rightEntry.getKey();
			JsonElement rightVal = rightEntry.getValue();
			if (leftObj.has(rightKey)) {
				JsonElement leftVal = leftObj.get(rightKey);
				if (leftVal.isJsonArray() && rightVal.isJsonArray()) {
					JsonArray leftArr = leftVal.getAsJsonArray();
					JsonArray rightArr = rightVal.getAsJsonArray();
					for (int i = 0; i < rightArr.size(); i++) {
						leftArr.add(rightArr.get(i));
					}
				} else if (leftVal.isJsonObject() && rightVal.isJsonObject()) {
					extendJsonObject(leftVal.getAsJsonObject(), rightVal.getAsJsonObject(), conflictStrategy);
				} else {
					handleMergeConflict(rightKey, leftObj, leftVal, rightVal, conflictStrategy);
				}
			} else {
				leftObj.add(rightKey,  rightVal);
			}
		}
	}
	
	private static void handleMergeConflict(String key, JsonObject leftObj, JsonElement leftVal, JsonElement rightVal, ConflictStrategy conflictStrategy) throws JsonObjectExtensionConflictExeception {
		switch (conflictStrategy) {
			case PREFER_FIRST_OBJECT:
				break;
			case PREFER_SECOND_OBJECT:
				leftObj.add(key, rightVal);
				break;
			case PREFER_NON_NULL:
				if (leftVal.isJsonNull() && !rightVal.isJsonNull()) {
					leftObj.add(key, rightVal);
				}
				break;
			case THROW_EXCEPTION:
				throw new JsonObjectExtensionConflictExeception("Key " + key + " exists in both objects and the conflict resolution strategy is " + conflictStrategy);
			default:
				throw new JsonObjectExtensionConflictExeception("The conflict resolution strategy " + conflictStrategy + " is unknown and cannot be processed");
		}
	}
}
