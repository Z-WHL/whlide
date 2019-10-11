package com.windhoverlabs.cfside.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.dialogs.AddItemDialog;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.ui.tables.KeyValueTable.KeyValueContentProvider;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class TableContextMenuActions {
	
	public static Action createActionAddItem(TableViewer tableViewer, NamedObject namedObj, CfsConfig cfsConfig, KeyValueContentProvider keyValueContentProvider) {
		return new Action("Add Item") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				String fields = null;
				NamedObject addTo = cfsConfig.getObject(namedObj.getPath(), "local", cfsConfig);
				NamedObject full = cfsConfig.getObject(namedObj.getPath(), "full", cfsConfig);
						
				AddItemDialog dialog = new AddItemDialog(shell);
				String name = null;
				if (dialog.open() == Window.OK) {
					if (addTo == null) {
						name = dialog.getName();

						NamedObject newOne = new NamedObject();
						newOne.setPath(full.getPath());
						newOne.setOverridden(true);
						newOne.setName(full.getName());
						JsonObject obj = new JsonObject();
						JsonObject pointer = obj;
						// obj = obj.get(newOne.getName());
						obj.addProperty(name, " ");
						newOne.setObject(obj);
						
						JsonObject child = (JsonObject) full.getObject();
						child.addProperty(name, " ");
						full.setObject(child);
						System.out.println("3333 " + child.toString());
						cfsConfig.saveLocal(newOne);
						cfsConfig.saveFull(newOne);
						ModuleConfigEditor cf = (ModuleConfigEditor) tableViewer.getTable().getParent().getParent().getParent();
						cf.goUpdate(full.getName(), (JsonElement) child, full, cfsConfig);
					} else {
						name = dialog.getName();
						NamedObject newOne = new NamedObject();
						newOne.setPath(full.getPath());
						newOne.setOverridden(true);
						newOne.setName(full.getName());
						JsonObject obj = new JsonObject();
						JsonObject pointer = obj;
						// obj = obj.get(newOne.getName());
						obj.addProperty(name, " ");
						newOne.setObject(obj);
						
						JsonObject child = (JsonObject) full.getObject();
						child.addProperty(name, " ");
						full.setObject(child);
						
						cfsConfig.saveLocal(newOne);
						cfsConfig.saveFull(newOne);
						ModuleConfigEditor cf = (ModuleConfigEditor) tableViewer.getTable().getParent().getParent().getParent();
						System.out.println(child.toString());
						cf.goUpdate(full.getName(), child, full, cfsConfig);
					}

				}
			}
		};
	}

	public static Action createActionDeleteItem() {
		return new Action("DeleteItem Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"DeleteItem",
						"This Action is to be implemented!");
			}
		};
	}
	
	public static Action createActionUnoverrideItem() {
		return new Action("UnoverrideItem Action") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				MessageDialog.openInformation(
						shell,
						"UnoverrideItem",
						"This Action is to be implemented!");
			}
		};
	}
}
