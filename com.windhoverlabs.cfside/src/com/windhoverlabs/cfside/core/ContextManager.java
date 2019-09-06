package com.windhoverlabs.cfside.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jface.preference.IPreferenceStore;

import com.windhoverlabs.cfside.model.CFSProperties.CFSProperties;


/**
 * The activator class controls the plug-in life cycle
 */
public class ContextManager extends AbstractUIPlugin implements IDebugEventSetListener, ILaunchesListener2 {
	//static private final Logger log = Logger.getLogger( ContextManager.class );
	
	private Hashtable<String, CFSProperties> CFSModelMap;
	private static HashSet<String> cfsXML;
	public static final String PLUGIN_ID = "com.windhoverlabs.cfside";
	// The shared instance
	private static ContextManager PLUGIN;
	private ResourceBundle _coreResourcesBundle;
	private boolean _isSuspended;
	
	{
		try {
			_coreResourcesBundle = ResourceBundle.getBundle("CorePluginResources");
		} catch (MissingResourceException e) {
			_coreResourcesBundle = null;
		}
	}
	
	/**
	 * The constructor
	 */
	public ContextManager() {
		PLUGIN = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		CFSModelMap = new Hashtable<String, CFSProperties>();
		cfsXML = new HashSet<String>();
		
		IPreferenceStore psc = ContextManager.getDefault().getPreferenceStore();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
		super.stop(context);
		CFSModelMap = null;

		PLUGIN = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ContextManager getDefault() {
		return PLUGIN;
	}
	
	/**
	 * Retrieve the IProject of the current project workspace. 
	 * Editor must be opened and selected.
	 */
	

	
	public static void setcfsXML(String temp) {
		cfsXML.add(temp);
		
	}
	
	public static HashSet<String> getcfsXML() {
		return cfsXML;
	}

	@Override
	public void handleDebugEvents(DebugEvent[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesAdded(ILaunch[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesChanged(ILaunch[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesRemoved(ILaunch[] arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void launchesTerminated(ILaunch[] arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
