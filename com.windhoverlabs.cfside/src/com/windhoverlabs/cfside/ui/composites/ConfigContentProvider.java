package com.windhoverlabs.cfside.ui.composites;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import com.windhoverlabs.cfside.model.Message;
import com.windhoverlabs.cfside.model.MessageConfigs;

public class ConfigContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		ArrayList<Message> list = (ArrayList<Message>) inputElement;
		return list.toArray();
	}

}
