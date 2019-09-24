package com.windhoverlabs.cfside.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.windhoverlabs.cfside.model.GsonTools;
import com.windhoverlabs.cfside.model.GsonTools.ConflictStrategy;
import com.windhoverlabs.cfside.model.GsonTools.JsonObjectExtensionConflictExeception;

public class JsonObjectsUtil {

	public static boolean deepMerge(String pathA, String pathb, String pathSaved) {
		//Let's assume there is no choice of which module and so we choose sch
		String module = "sch";
		JsonObject base = JsonObjectsUtil.createSkeletonConfig(module);
		JsonObject objA = JsonObjectsUtil.createJsonObjectFromFile(pathA);
		System.out.println(objA.toString());
		JsonObject objB = JsonObjectsUtil.createJsonObjectFromFile(pathb);
		System.out.println(objB.toString());
		
		JsonObject module1 = objA.get("modules").getAsJsonObject();
		JsonObject moduleObj = module1.get(module).getAsJsonObject();
		
		try {
			GsonTools.extendJsonObject(objB, ConflictStrategy.PREFER_SECOND_OBJECT, moduleObj);
		} catch (JsonObjectExtensionConflictExeception e) {
			return false;
		}
		base.add(module, objB);
		JsonObject ret = new JsonObject();
		ret.add("modules", base);
		System.out.println(ret.toString());
		if (!JsonObjectsUtil.writeToFile(pathSaved, ret)) return false;
		
		return true;
	}

	public static String deepMergeString(String pathA, String pathb, String pathSaved) {
		//Let's assume there is no choice of which module and so we choose sch
		String module = "sch";
		JsonObject base = JsonObjectsUtil.createSkeletonConfig(module);
		JsonObject objA = JsonObjectsUtil.createJsonObjectFromFile(pathA);
		System.out.println(objA.toString());
		JsonObject objB = JsonObjectsUtil.createJsonObjectFromFile(pathb);
		System.out.println(objB.toString());
		
		JsonObject module1 = objA.get("modules").getAsJsonObject();
		JsonObject moduleObj = module1.get(module).getAsJsonObject();
		
		try {
			GsonTools.extendJsonObject(objB, ConflictStrategy.PREFER_SECOND_OBJECT, moduleObj);
		} catch (JsonObjectExtensionConflictExeception e) {
		}
		base.add(module, objB);
		JsonObject ret = new JsonObject();
		ret.add("modules", base);
		System.out.println(ret.toString());
		JsonObjectsUtil.writeToFile(pathSaved, ret);
		
		return ret.getAsString();
	}

	public static JsonObject deepMergeObject(String pathA, String pathb, String pathSaved) {
		//Let's assume there is no choice of which module and so we choose sch
		String module = "sch";
		JsonObject base = JsonObjectsUtil.createSkeletonConfig(module);
		JsonObject objA = JsonObjectsUtil.createJsonObjectFromFile(pathA);
		JsonObject objB = JsonObjectsUtil.createJsonObjectFromFile(pathb);
		
		JsonObject module1 = objA.get("modules").getAsJsonObject();
		JsonObject moduleObj = module1.get(module).getAsJsonObject();
		
		try {
			GsonTools.extendJsonObject(objB, ConflictStrategy.PREFER_SECOND_OBJECT, moduleObj);
		} catch (JsonObjectExtensionConflictExeception e) {
		}
		base.add(module, objB);
		JsonObject ret = new JsonObject();
		ret.add("modules", base);
	
		
		return ret;
	}

	public static String beautifyJson(String uglyString) {
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(uglyString).getAsJsonObject();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);
		return prettyJson;
	}
	
	private static boolean writeToFile(String path, JsonObject obj) {
		
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file, false);
				String uglyString = obj.toString();
				String prettyString = beautifyJson(uglyString);
				byte[] strToBytes = prettyString.getBytes();
				fos.write(strToBytes);
				fos.close();
				return true;
			} catch (IOException e) {
				return false;
			}
		} else {
			return false;
		}
		
		
	}

	private static JsonObject createSkeletonConfig(String module) {
		JsonObject skeleton = new JsonObject();
		skeleton.add(module, new JsonObject());
		return skeleton;
	}

	private static JsonObject createJsonObjectFromFile(String filePath) {
		//file parameter is not being used, using hard coded static paths for testing.
		JsonObject jsonObj = null;
		
		try {
			String jsonString = Files.readString(Paths.get(filePath), StandardCharsets.US_ASCII);
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonString);
			JsonObject jo = je.getAsJsonObject();
			
			return jo;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonObj;
	}

}
