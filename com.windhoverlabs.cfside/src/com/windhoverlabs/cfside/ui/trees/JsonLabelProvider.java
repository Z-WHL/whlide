package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLabelProvider extends StyledCellLabelProvider implements IColorProvider {
	
	ILabelProvider provider;
	ILabelDecorator decorator;
	
	private final Styler fBoldStyler;
	
	public JsonLabelProvider(final Font boldFont) {
		fBoldStyler = new Styler() {
			@Override
			public void applyStyles(TextStyle textStyle) {
				textStyle.font = boldFont;
			}
		};
	}
	
	@Override
	public void update(ViewerCell cell) {
		NamedObject namedObject = (NamedObject) cell.getElement();
		JsonElement jsonElem = (JsonElement) namedObject.getObject();
		String name = namedObject.getName();
		
		if (namedObject.getOverridden()) {
			Styler style = fBoldStyler;
			StyledString styledString = new StyledString(name, style);
			cell.setText(styledString.toString());
			cell.setStyleRanges(styledString.getStyleRanges());
		} else {
			if(jsonElem.isJsonArray()) {
				JsonArray jsonArray = (JsonArray) jsonElem.getAsJsonArray();
				cell.setText(name);
			} else if(jsonElem.isJsonObject() ) {
				JsonObject jsonObject = (JsonObject) jsonElem.getAsJsonObject();
				cell.setText(name);
			} else if(jsonElem.isJsonNull() ) {
				cell.setText("<NULL>");
			} else if(jsonElem.isJsonPrimitive() ) {
				cell.setText(name);
			} else {
				cell.setText("<UNKNOWN>");
			}
		}
		
		super.update(cell);
	}
	
	@Override
	protected void measure(Event event, Object element) {
		super.measure(event,  element);
	}
	
	
	
	@Override
	public Color getForeground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		NamedObject namedObj = (NamedObject) element;
		if (namedObj.getOverridden()) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
		}
		return  Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	}

}
