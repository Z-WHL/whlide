package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLabelProvider extends LabelProvider implements ILabelProvider, ITableColorProvider {
	
	ILabelProvider provider;
	ILabelDecorator decorator;
	

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

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		NamedObject namedObj = (NamedObject) element;
		if (namedObj.getOverridden()) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		}
		return  Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
	}
}
