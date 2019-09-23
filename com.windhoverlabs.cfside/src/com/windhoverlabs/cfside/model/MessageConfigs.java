package com.windhoverlabs.cfside.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.runtime.Path;

import com.google.gson.Gson;


public class MessageConfigs {
	private List<String> parentMessagesString;
	private List<String> childMessageString = null;
	private HashMap<String, Message> configs;
	
	private Gson gson = new Gson();
	
	/**
	 * Constructor which creates the message configs for the root config.
	 * Should act as a template for all children configs to be created from.
	 * File path should be in the form of absolute to allow hierarchies of configurations.
	 * @param parentConfig
	 */
	public MessageConfigs(File parentConfig) {
		this.parentMessagesString = fileToStringList(parentConfig);
		this.parentMessagesString.forEach(message -> addToConfigs(message));
	}
	
	/**
	 * Constructor which creates the message configs of a child config given the parent's config.
	 * Creates a file based on the parent configs.
	 * Allows the child to make their own unique changes to the configs of the parent.
	 * Should not affect the parent's configs, only the childs.
	 * File path should be absolute path to allow hierarchies of configurations.
	 * @param parentConfig
	 * @param childConfig
	 */
	public MessageConfigs(File parentConfig, File childConfig) {
		this.parentMessagesString = fileToStringList(parentConfig);
		this.childMessageString = fileToStringList(childConfig);
		
		this.parentMessagesString.forEach(message -> addToConfigs(message));
		this.childMessageString.forEach(message -> addToConfigs(message));
	}
	
	public List<Message> getMessageList() {
		List<Message> list = new ArrayList<Message>();
		this.configs.forEach((key, value) -> list.add(value));
		
		return list;
	}
	private void addToConfigs(String message) {
		Message singleMessage = gson.fromJson(message, Message.class);
		int identifier = singleMessage.getMiid();
		String sIdentifier = Integer.toString(identifier);
		
		configs.put(sIdentifier, singleMessage);
	}
	
	private String fileToStringArray(File inputFile) {
		String pathOfFile = inputFile.getPath();
		String fileAsString = "";
		
		try {
			fileAsString = new String(Files.readAllBytes(Paths.get(pathOfFile)));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return fileAsString;
	}
	
	private List<String> fileToStringList(File inputFile) {
		List<String> fileAsStringList = new ArrayList<String>();
		String pathOfFile = inputFile.getPath();
		
		try (Stream<String> stream = Files.lines(Paths.get(pathOfFile), StandardCharsets.UTF_8)){
			stream.forEach( string -> fileAsStringList.add(string));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileAsStringList;
	}

	@Override
	public String toString() {
		StringBuilder contentBuilder = new StringBuilder();
		configs.forEach((key, value) -> {
			contentBuilder.append(gson.toJson(value)).append("\n");
		});
		
		return contentBuilder.toString();
		
	}
}
