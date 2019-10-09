package com.windhoverlabs.cfside.ui.trees;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.osgi.framework.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonLabelProvider extends StyledCellLabelProvider implements IColorProvider {
	
	Bundle bundle = Platform.getBundle("com.windhoverlabs.cfside");
	final URL fullPathIconOne = FileLocator.find(bundle, new Path("icons/testjsonicon.png"), null);
	ImageDescriptor imgDesc = ImageDescriptor.createFromURL(fullPathIconOne);
	Image imgOne = imgDesc.createImage();
	
	final URL fullPathIconTwo = FileLocator.find(bundle, new Path("icons/testjsonicon2.png"), null);
	ImageDescriptor imgDesc2 = ImageDescriptor.createFromURL(fullPathIconTwo);
	Image imgTwo = imgDesc2.createImage();
	
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
		}
	
		if(jsonElem.isJsonPrimitive()) {
			cell.setText(name);
			cell.setImage(imgTwo);
			cell.setText(name);
		} else {
			JsonObject jsonObject = (JsonObject) jsonElem.getAsJsonObject();
			cell.setText(name);
			cell.setImage(imgOne);
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
