package com.windhoverlabs.cfside.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import com.windhoverlabs.cfside.model.CFSProject;
import com.windhoverlabs.cfside.model.CFSProperties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.jface.preference.IPreferenceStore;

import com.thoughtworks.xstream.XStream;

/**
 * The activator class controls the plug-in life cycle
 */
public class ContextManager extends AbstractUIPlugin implements IDebugEventSetListener, ILaunchesListener2 {
	//static private final Logger log = Logger.getLogger( ContextManager.class );
	
	private Hashtable<String, CFSProperties> CFSModelMap;
	private static HashSet<String> cfsXML;
	private XStream _xStream;
	
	public static final String PLUGIN_ID = "com.windhoverlabs.cfside";
	// The shared instance
	public static ContextManager PLUGIN;
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
	
	public CFSProperties getCFSProperties(String projectName, boolean forceload) {
		CFSProperties properties = CFSModelMap.get(projectName);
		if (properties != null && !forceload) {
			return properties;
		}
		IWorkspace eclipseWS = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot eclipseWSRoot = eclipseWS.getRoot();
		IProject project = eclipseWSRoot.getProject(projectName);
		IFile propertiesFile = project.getFile(CFSProject.METAFILE);
		if (propertiesFile.exists()) {
			properties = loadModelFromStore(propertiesFile.getLocation().toFile());
			if (properties != null) {
				CFSModelMap.put(projectName, properties);
			}
		} else {
			properties = new CFSProperties();
			properties.setValidOutputFileName(project.getName());
		}
		
		return properties;
		
	}
	
	public CFSProperties loadModelFromStore(File file) {
		CFSProperties cfsProperties = null;
		
		if (file == null || !file.exists() || !file.isFile()) {
			return null;
		}
		
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			if (0 < fileInputStream.available()) {
				XStream xStream = getXStream();
				cfsProperties = (CFSProperties) xStream.fromXML(fileInputStream);
			} else {
				cfsProperties = new CFSProperties();
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return cfsProperties;
	} 

	
	public XStream getXStream() {
		if (_xStream == null) {
			_xStream = new XStream();
			
		}
		return _xStream;
	}
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
