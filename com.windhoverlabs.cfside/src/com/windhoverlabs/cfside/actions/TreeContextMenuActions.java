package com.windhoverlabs.cfside.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.dialogs.AddItemDialog;
import com.windhoverlabs.cfside.ui.dialogs.AddObjectDialog;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class TreeContextMenuActions {

	public static Action createPlaceholderAction(TreeViewer treeViewer, CfsConfig cfsConfig) {
		return new Action("PlaceHolder Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"PlaceHolder",
						"This Action is to be implemented!");
			}
		};
	}
	
	public static Action createActionAddObject(TreeViewer treeViewer, CfsConfig cfsConfig ) {
		return new Action("Add Object") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				String fields = null;
				
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}
				if (treeViewer.getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
					Object selectedNode = selection.getFirstElement();
					
					if (selectedNode != null) {
						NamedObject namedObject = (NamedObject) selectedNode; 
						JsonElement selectedElem = (JsonElement) namedObject.getObject();
						ModuleConfigEditor s = (ModuleConfigEditor) treeViewer.getTree().getParent();
						AddObjectDialog dialog = null;
						if (selectedElem.isJsonObject()) {
							dialog = new AddObjectDialog(shell);
							NamedObject addToConfig = new NamedObject();
							JsonElement toAdd = null;
							String name = null;
							if (dialog.open() == Window.OK) {
								toAdd = dialog.getJsonElement();
								name = dialog.getName();
								addToConfig.setName(name);
								addToConfig.setPath(namedObject.getPath().concat("."+name));
								addToConfig.setObject(toAdd);
								addToConfig.setOverridden(true);
								cfsConfig.save(addToConfig);
							}
							treeViewer.refresh(namedObject);
						} 
					}
				}
			}
		};
	}

	public static Action createActionUnoverride(TreeViewer treeViewer, CfsConfig cfsConfig) {
		return new Action("Unoverride Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"Unoverride",
						"This Action is to be implemented!");
			}
		};
	}
	
	public static Action createActionDelete(TreeViewer treeViewer, CfsConfig cfsConfig) {
		return new Action("Delete Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"Delete",
						"This Action is to be implemented!");
			}
		};
	}
	
	// Not being considered.
	public static Action createActionAddArray(TreeViewer treeViewer, CfsConfig cfsConfig) {
		return new Action("Add Array Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"Add Array",
						"This Action is to be implemented!");
			}
		};
	}	
	
	
}
