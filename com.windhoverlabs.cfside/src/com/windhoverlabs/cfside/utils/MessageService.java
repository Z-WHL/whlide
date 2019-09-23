package com.windhoverlabs.cfside.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.windhoverlabs.cfside.model.Message;

public class MessageService {

	public static final String PROP_ADD = "add";
	public static final String PROP_CHANGE = "change";
	private static final int MI = 0;
	private Map messages = new TreeMap();
	private ListenerList listeners = new ListenerList(ListenerList.IDENTITY);
	
	public MessageService() {
		fillModel();
	}
	
	private static final String[] datafill = {
			"SCH_CMD_MID", "SCH Ground Commands Message ID", "SCH_SEND_HK_MID", "SCH Send Houskeeping Message ID", 
			"SCH_UNUSED_MID", "SCH MDT Unused Message Messag ID", "SCH_HK_TLM_MID", "SCH Houskeeping Telemetry Message ID", 
			"SCH_DIAG_TLM_MID", "SCH Diagnostic Telemetry Message ID"
	};
	
	private void fillModel() {
		int counter = MI;
		for (int i = 0; i < datafill.length; i += 2) {
			int miid = counter++;
			Message mes = new Message(counter, datafill[i], datafill[i+1], null, null);
			messages.put(miid, mes);
		}
	}
	
	public void addMessageChangeListener(IPropertyChangeListener listener) {
		listeners.add(listener);
	}
	
	public Collection getMessagesCollection() {
		return Collections.unmodifiableCollection(messages.values());
	}
	
	public List getMessagesList() {
		List<Message> ls = new ArrayList<Message>();
		Set<Integer> set = messages.keySet();
		Iterator it = set.iterator();
		
		while (it.hasNext()) {
			Object temp = it.next();
			ls.add((Message)messages.get(temp));
		}
		
		return ls;
	}
	
	
	
	public Message getMessage(int miid) {
		Message mes = (Message) messages.get(miid);
		if ( mes == null ) {
			return null;
		} else {
			return mes.copy();
		}
	}
	
	public void removeMessageChangeListener(IPropertyChangeListener listener) {
		listeners.remove(listener);
	}
	
	public void updateMessage(Message message) {
		if ( message != null ) {
			Message mes = (Message) messages.get(message.getMiid());
			if ( mes != null ) {
				Message oldMes = mes.copy();
				mes.setIdentifier(message.getIdentifier());
				mes.setDescription(message.getDescription());
				fireMessageChanged(PROP_CHANGE, oldMes, mes);
			} else return;
		} else return;
		
	}

	private void fireMessageChanged(String propChange, Message oldMes, Message mes) {
		if (listeners.isEmpty()) {
			return;
		}
		PropertyChangeEvent event = new PropertyChangeEvent(this, propChange, oldMes, mes);
		Object[] array = listeners.getListeners();
		for (int i = 0; i < array.length; i++) {
			((IPropertyChangeListener) array[i]).propertyChange(event);
		}
	}
	
	public Message createMessage(int miid) {
		if (messages.containsKey(miid)) {
			return null;
		}
		Message mes = new Message(miid, "identifier", "description");
		messages.put(miid, mes);
		fireMessageChanged(PROP_ADD, null, mes);
		
		return mes;
	}
	
	public void dispose() {
		listeners.clear();
	}
}

if (!file.exists()) {
	byte[] bytes = "".getBytes();
    InputStream source = new ByteArrayInputStream(bytes);
    try {
    	file.create(source, IResource.NONE, null);
    } catch (CoreException e) {
    	e.printStackTrace();
    }
}


