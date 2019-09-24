package com.windhoverlabs.cfside.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import com.google.gson.JsonArray;
/** 
 * 
 * Helper class for projects
 *
 */

public class ProjectUtils {
	private final static String PATH_OBJECT_A = "/home/vagrant/development/airliner/config/config.json";
	private final static String PATH_OBJECT_B = "/home/vagrant/development/airliner/apps/sch/fsw/for_build/design.json";
	private final static String PATH_OBJECT_C = "/home/vagrant/development/airliner/config/bebop2/sitl/target/prebuild.json";
	
	
	/**
	 * Finds and returns the given project for a selected portion
	 * @return IProject
	 */
	public static IProject getProjectSelection() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if ( window != null ) {
			if ( isStructuredSelection(window) ) {
				return getProjectFromSelection(window);
			} else {
				return getProjectFromEditor(window);
			}
		}
		return null;
	}
	
	public static IProject getProjectWithName(String projectName) {
		IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			if (project.getName().equalsIgnoreCase(projectName)) {
				return project;
			}
		}
		return null;
	}
	
	public static String getProjectName() {
		IProject project = getProjectSelection();
		return project.getName();
	}
	
	/**
	 * Checks if the current selection is a structured selection
	 * @param window
	 * @return boolean
	 */
	private static boolean isStructuredSelection(IWorkbenchWindow window) {
		ISelection iselection = window.getSelectionService().getSelection();
		if ( iselection.getClass() != IStructuredSelection.class ) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the project based off of the current active selection.
	 * @param window
	 * @return IProject
	 */
	
	private static IProject getProjectFromSelection(IWorkbenchWindow window) {
		IProject project = null;
		ISelection iselection = window.getSelectionService().getSelection();
		IStructuredSelection selection = (IStructuredSelection) iselection;
		if ( selection == null ) {
			return project;
		} else {
			Object firstElement = selection.getFirstElement();
			if ( firstElement instanceof IResource ) {
				project = ((IResource) firstElement).getProject();
			} else {
				return project;
			}
		}
		return project;
	}
	
	/**
	 * Get the project based off of the current file that is opened by the editor.
	 * @param window
	 * @return IProject
	 */
	private static IProject getProjectFromEditor(IWorkbenchWindow window) {
		IProject project = null;
		IWorkbenchPage activePage = window.getActivePage();
		IEditorPart activeEditor = activePage.getActiveEditor();
		if ( activeEditor != null ) {
			IEditorInput input = activeEditor.getEditorInput();
			project = input.getAdapter(IProject.class);
			if ( project == null ) {
				IResource resource = input.getAdapter(IResource.class);
				if ( resource != null ) {
					project = resource.getProject();
				}
			}
		}
		return project;
	}
	
	/**
	 * Helper method to help retrieve the path of the currently open file.
	 * @param window
	 * @return IPath
	 */
	public static IPath getPathFromActiveFile() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IPath path = null;
		if ( window != null ) {
			IWorkbenchPage page = window.getActivePage();
			IEditorPart editor = page.getActiveEditor();
			if (editor != null) {
				IEditorInput input = editor.getEditorInput();
				path = input instanceof FileEditorInput 
						? ( (FileEditorInput) input).getPath()
						: null;
				return path;
			}
		}
		return path;
	}
	
	public static IPackageFragmentRoot[] getProjectSourceFolders(IProject project) {
		IJavaProject iJavaProject = JavaCore.create(project);
		ArrayList<IPackageFragmentRoot> sourceRoots = new ArrayList<IPackageFragmentRoot>();
		if (iJavaProject.exists() && iJavaProject.isOpen()) {
			try {
				IPackageFragmentRoot[] roots = iJavaProject.getAllPackageFragmentRoots();
				for (IPackageFragmentRoot root : roots) {
					if (IPackageFragmentRoot.K_SOURCE == root.getKind()) {
						sourceRoots.add(root);
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
				return new IPackageFragmentRoot[0];
			}
		}
		return sourceRoots.toArray(new IPackageFragmentRoot[sourceRoots.size()]);
	}
	
	public static IFile getProjectIFile(IProject project, String fileName) {
		IFile file = null;
		
		IPackageFragmentRoot roots[] = getProjectSourceFolders(project);
		
		for (IPackageFragmentRoot root : roots) {
			try {
				IJavaElement elements[] = root.getChildren();
				for (IJavaElement element : elements) {
					if (element.getElementType() == IJavaElement.PACKAGE_FRAGMENT) {
						IPackageFragment packageFragment = (IPackageFragment) element;
						Object packageChildren[] = packageFragment.getNonJavaResources();
						for (Object child : packageChildren) {
							if (child instanceof IFile) {
								IFile childFile = (IFile) child;
								if (childFile.getName().equals(fileName)) {
									return childFile;
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	
	public static IResource getResource(IProject project, File file) {
		IJavaProject javaProject = JavaCore.create(project);
		IPath filePath = new Path(file.getAbsolutePath());
		try {
			IClasspathEntry[] classPathEntries = javaProject.getResolvedClasspath(true);
			IFile input = null;
			for (IClasspathEntry classPathEntry : classPathEntries) {
				if (classPathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
					IWorkspaceRoot workspaceRoot = project.getWorkspace().getRoot();
					IResource resource = workspaceRoot.findMember(classPathEntry.getPath());
					if (resource instanceof IContainer) {
						IContainer sourceContainer = (IContainer) resource;
						File sourceContainerFile = resource.getLocation().toFile();
						IPath sourceFolderPath = new Path(sourceContainerFile.getAbsolutePath());
						
						if (sourceFolderPath.isPrefixOf(filePath)) {
							int segmentCount = sourceFolderPath.segmentCount();
							IPath relativePath = filePath.removeFirstSegments(segmentCount);
							input = sourceContainer.getFile(relativePath);
							break;
						}
					}
				}
			}
			return input;
		} catch (JavaModelException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean containsProject() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projects.length > 0;
	}
	
	public static File createFile(File file) {
		if (!file.exists()) {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!file.exists()) {
			return null;
		}
		return file;
	}

}
