package com.windhoverlabs.cfside.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.windhoverlabs.cfside.model.GsonTools;
import com.windhoverlabs.cfside.model.GsonTools.ConflictStrategy;
import com.windhoverlabs.cfside.model.GsonTools.JsonObjectExtensionConflictExeception;

public class JsonObjectsUtil {
	private final static String PATH_OBJECT_A = "/home/vagrant/development/airliner/config/config.json";
	private final static String PATH_OBJECT_B = "/home/vagrant/development/airliner/apps/sch/fsw/for_build/design.json";
	private final static String PATH_OBJECT_C = "/home/vagrant/development/airliner/config/bebop2/sitl/target/prebuild.json";

	static Gson gson = new Gson();
	static JsonParser jp = new JsonParser();
	
	public static JsonObject goMerge(File pathToConfig) {
		//Load file into reader.
		Reader rd = null;
		try {
			rd = new FileReader(pathToConfig);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//Parse into jsonelement
		JsonElement localConfigElm = jp.parse(rd);
		JsonObject localConfigObj = localConfigElm.getAsJsonObject();
		//Obtain jsonObject for 'modules'
		JsonElement moduleConfigElm = localConfigObj.get("modules");
		JsonObject moduleConfigObj = moduleConfigElm.getAsJsonObject();
		
		// Iterate through the modules
		for (Map.Entry<String, JsonElement> entry : moduleConfigObj.entrySet()) {
			// Retrieve current module as json object
			JsonElement currentModuleElm = moduleConfigObj.get(entry.getKey());
			JsonObject currentModuleObj = currentModuleElm.getAsJsonObject();
			
			// Get the definition base path
			String spathtoconfig = pathToConfig.getParent().toString().concat("/");
			String basePath = currentModuleObj.get("definition").getAsString();
			String concat = spathtoconfig.concat(basePath);
			// Open the definition file for this module
			Path path = Paths.get(concat);
			File defFile = new File(path.toString());
			// Load into JsonElement
			try {
				JsonElement defEle = jp.parse(new FileReader(defFile));
				// Deep Merge this definition object into the 'module/<name>/' object of the local config.
				merge(defEle, currentModuleElm);
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		// Get the base directory, i.e. "../../../.."
		String p1 = pathToConfig.toPath().getParent().toString().concat("/");
		String configBase = localConfigObj.get("config_base").getAsString();
		String p2 = p1.concat(configBase);
		String basePath = Paths.get(p2).normalize().toString();
		String pathLocalConfig = pathToConfig.toPath().getParent().toString();
		
		localConfigElm = mergeParentConfig(localConfigElm, basePath, pathLocalConfig);
		
		//System.out.println(beautifyJson(localConfigElm.toString()));
		return localConfigElm.getAsJsonObject();
		
	}
	
	public static JsonElement mergeParentConfig(JsonElement mergedConfig, String absConfigBase, String absCurrentDir) {
		// Can we go further up?
		String configfile = "config.json";
		if (!absCurrentDir.equals(absConfigBase)) {
			// Yes, get parent directory of the current directory
			String absParentPath = Paths.get(absCurrentDir).getParent().toString();
			// Merge the parent object
			mergedConfig = mergeParentConfig(mergedConfig, absConfigBase, absParentPath);
		}
		// Now merge the current config file. First check for ocnfiguration file
		File cfgFileNameCurrent = new File(absCurrentDir.concat("/".concat(configfile)));
		
		if (cfgFileNameCurrent.exists()) {
			// Create Json Element
			try {
				JsonElement cfgCurrentEle = jp.parse(new FileReader(cfgFileNameCurrent));
				JsonObject cfgCurrentObj = cfgCurrentEle.getAsJsonObject();
				// Iterate through module objects and only merge what is defined in local config
				JsonElement currentObjEle = cfgCurrentObj.get("modules");
				JsonObject currentObjObj = currentObjEle.getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : mergedConfig.getAsJsonObject().get("modules").getAsJsonObject().entrySet()) {
					if (currentObjObj.has(entry.getKey())) {
						merge(currentObjObj.get(entry.getKey()), mergedConfig.getAsJsonObject().get("modules").getAsJsonObject().get(entry.getKey()));
					}
				}
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return mergedConfig;	
	}
	
	public static JsonElement merge(JsonElement sourceJE, JsonElement destinationJE) {
		JsonObject sourceJO = sourceJE.getAsJsonObject();
		JsonObject destinationJO = destinationJE.getAsJsonObject();
		
		for(Map.Entry<String, JsonElement> entry : sourceJO.entrySet()) {
			if (entry.getValue().isJsonObject()) {
				JsonElement tempElement = null;
				if (destinationJO.has(entry.getKey())) {
					tempElement = destinationJO.get(entry.getKey());
				} else {
					JsonElement emptyElement = jp.parse("{}");
					destinationJO.add(entry.getKey(), emptyElement);
					tempElement = destinationJO.get(entry.getKey());
				}
				merge(entry.getValue().getAsJsonObject(), tempElement);				
			} else {
				destinationJO.addProperty(entry.getKey(), entry.getValue().getAsString());
			}
		}
		
		destinationJE = (JsonElement) destinationJO;
		return destinationJE;
	}
		
	public static boolean deepMerge(String pathA, String pathb, String pathSaved) {
		//Let's assume there is no choice of which module and so we choose sch
		String module = "sch";
		JsonObject base = JsonObjectsUtil.createSkeletonConfig(module);
		JsonObject objA = JsonObjectsUtil.createJsonObjectFromFile(pathA);
		//System.out.println(objA.toString());
		JsonObject objB = JsonObjectsUtil.createJsonObjectFromFile(pathb);
		//System.out.println(objB.toString());
		
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
		//System.out.println(ret.toString());
		if (!JsonObjectsUtil.writeToFile(pathSaved, ret)) return false;
		
		return true;
	}

	public static String deepMergeString(String pathA, String pathb, String pathSaved) {
		//Let's assume there is no choice of which module and so we choose sch
		String module = "sch";
		JsonObject base = JsonObjectsUtil.createSkeletonConfig(module);
		JsonObject objA = JsonObjectsUtil.createJsonObjectFromFile(pathA);
		//System.out.println(objA.toString());
		JsonObject objB = JsonObjectsUtil.createJsonObjectFromFile(pathb);
		//System.out.println(objB.toString());
		
		JsonObject module1 = objA.get("modules").getAsJsonObject();
		JsonObject moduleObj = module1.get(module).getAsJsonObject();
		
		try {
			GsonTools.extendJsonObject(objB, ConflictStrategy.PREFER_SECOND_OBJECT, moduleObj);
		} catch (JsonObjectExtensionConflictExeception e) {
		}
		base.add(module, objB);
		JsonObject ret = new JsonObject();
		ret.add("modules", base);
		//System.out.println(ret.toString());
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
