package com.windhoverlabs.e4.rcp.projects;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
 
import com.windhoverlabs.e4.rcp.natures.ProjectNature;

public class CFSProjectSupport {

    /* For this marvelous project we need to:
    * - create the default Eclipse project
    * - add the custom project nature
    * - create the folder structure
    *
    * @param projectName
    * @param location
    * @param natureId
    * @return
    */
   public static IProject createProject(String projectName, URI location) {


       IProject project = createBaseProject(projectName, location);
       try {
           addNature(project);

           String[] paths = { "/parent/child1-1/child2", "/parent/child1-2/child2/child3" }; //$NON-NLS-1$ //$NON-NLS-2$
           addToProjectStructure(project, paths);
       } catch (CoreException e) {
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
               e.printStackTrace();
           }
       }

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
	   IFile file = folder.getFile("test.txt");
	   
	   if (!file.exists()) {
		   byte[] bytes = "Testing the creation of files".getBytes();
		   InputStream source = new ByteArrayInputStream(bytes);
		   try {
			file.create(source, IResource.NONE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
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

}

