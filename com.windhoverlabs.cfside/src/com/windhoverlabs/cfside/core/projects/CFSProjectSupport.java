package com.windhoverlabs.cfside.core.projects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URI;
import java.util.Iterator;
import java.util.stream.Stream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.windhoverlabs.cfside.natures.ProjectNature;
import com.windhoverlabs.cfside.core.ContextManager;;

public class CFSProjectSupport {
	
	private static String pname;

   public static IProject createProject(String projectName, URI location) {
	   Assert.isNotNull(projectName);
	   Assert.isTrue(projectName.trim().length() > 0);
       IProject project = createBaseProject(projectName, location);
       try {
           addNature(project);

           String[] paths = { "/parent/child1-1/child2", "/parent/child1-2/child2/child3" };
           addToProjectStructure(project, paths);
       } catch (CoreException e) {
    	   System.out.println("CreateProject");
           e.printStackTrace();
           project = null;
       }
       
       return project;
   }

   /**
    * Just do the basics: create a basic project.
    *
    * @param location
    * @param projectName
    */
   private static IProject createBaseProject(String projectName, URI location) {
       // it is acceptable to use the ResourcesPlugin class
       IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

       if (!newProject.exists()) {
           URI projectLocation = location;
           IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
           
           desc.setLocationURI(projectLocation);
           try {
               newProject.create(desc, null);
               if (!newProject.isOpen()) {
                   newProject.open(null);
               }
           } catch (CoreException e) {
        	   System.out.println("createBaseProject");
               e.printStackTrace();
           }
       }

       setCFSXMLs(newProject);
       return newProject;
   }

   private static void createFolder(IFolder folder) throws CoreException {
       IContainer parent = folder.getParent();
       if (parent instanceof IFolder) {
           createFolder((IFolder) parent);
       }
       if (!folder.exists()) {
           folder.create(false, true, null);
           createFile(folder);
       }
       
   }
   
   private static void createFile(IFolder folder) {
	   String f = "test.txt";
	   IFile file = folder.getFile(f);
	   
	   if (!file.exists()) {
		   byte[] bytes = "Testing the creation of files".getBytes();
		   InputStream source = new ByteArrayInputStream(bytes);
		   try {
			file.create(source, IResource.NONE, null);
		} catch (CoreException e) {
			System.out.println("createFile()");
			e.printStackTrace();
		}
	   }
   }

   /**
    * Create a folder structure with a parent root, overlay, and a few child
    * folders.
    *
    * @param newProject
    * @param paths
    * @throws CoreException
    */
   private static void addToProjectStructure(IProject newProject, String[] paths) throws CoreException {
       for (String path : paths) {
           IFolder etcFolders = newProject.getFolder(path);
           
           createFolder(etcFolders);
       }
   }

   private static void addNature(IProject project) throws CoreException {
       if (!project.hasNature(ProjectNature.NATURE_ID)) {
           IProjectDescription description = project.getDescription();
           String[] prevNatures = description.getNatureIds();
           String[] newNatures = new String[prevNatures.length + 1];
           System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
           newNatures[prevNatures.length] = ProjectNature.NATURE_ID;
           description.setNatureIds(newNatures);

           IProgressMonitor monitor = null;
           project.setDescription(description, monitor);
       }
   }

   private static void setCFSXMLs(IProject project) {
		IFile file = project.getFile("cfsXML.txt");
		
		if (!file.exists()) {
			byte[] bytes = "".getBytes();
		    InputStream source = new ByteArrayInputStream(bytes);
		    try {
		    	file.create(source, IResource.NONE, null);
		    } catch (CoreException e) {
		    	e.printStackTrace();
		    }
		}
		try {
			Reader reader = new InputStreamReader(file.getContents(), file.getCharset());
			BufferedReader br = new BufferedReader(reader);
			Stream<String> str = br.lines();
			Iterator it = str.iterator();
			while(it.hasNext()) {
				String temp = (String) it.next();
				ContextManager.setcfsXML(temp);
			}
			str.close();
			br.close();
			reader.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
   
   public static IProject getCurrentEditorProject() {
	   IWorkbench workbench = PlatformUI.getWorkbench();
	   IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	   IWorkbenchPage activePage = window.getActivePage();
	   IEditorPart activeEditor = activePage.getActiveEditor();
	   if(activeEditor != null) {
		   IEditorInput input = activeEditor.getEditorInput();
		   IProject project = input.getAdapter(IProject.class);
		   if(project == null) {
			   IResource resource = input.getAdapter(IResource.class);
			   if(resource != null) {
				   project = resource.getProject();
			   }
		   }
		   return project;
	   } else {
		   return null;
	   }  
   	}
   
   public static String getCurrentFileName() {
	   IWorkbench workbench = PlatformUI.getWorkbench();
	   IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
	   IWorkbenchPage activePage = window.getActivePage();
	   IEditorPart activeEditor = activePage.getActiveEditor();
	   if(activeEditor != null) {
		   IEditorInput input = activeEditor.getEditorInput();
		   String fileName = input.getName();
				   
		   return fileName;
	   } else {
		   return null;
	   } 
   }

   
}

