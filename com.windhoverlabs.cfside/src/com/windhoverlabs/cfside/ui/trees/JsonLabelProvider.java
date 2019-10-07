package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class JsonLabelProvider extends BaseLabelProvider implements ILabelProvider {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		NamedObject namedObject = (NamedObject) element;
		JsonElement jsonElem = (JsonElement) namedObject.getObject();
		String name = namedObject.getName();
		
		if(jsonElem.isJsonArray()) {
			JsonArray jsonArray = (JsonArray) jsonElem.getAsJsonArray();
			
			return name;
		} else if(jsonElem.isJsonObject() ) {
			JsonObject jsonObject = (JsonObject) jsonElem.getAsJsonObject();
			
			return name;
		} else if(jsonElem.isJsonNull() ) {
			return "<NULL>";
		} else if(jsonElem.isJsonPrimitive() ) {
			return name;
		} else {
			return "<UNKNOWN>";
		}
	}
}
