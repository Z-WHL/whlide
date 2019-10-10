package com.windhoverlabs.cfside.ui.trees;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.dialogs.CFSDialog;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTreeViewer extends TreeViewer implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private JsonContentProvider jsonContentProvider;
	private Menu menu;
	private MenuManager contextMenu;
	private NamedObject namedObject;
	private CfsConfig cfsConfig;
	private Tree tree;
	private String jsonPath;
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, String jsonPath, CfsConfig cfsConfig) {
		super(parent, style);
		this.jsonPath = jsonPath;
		this.cfsConfig = cfsConfig;
		tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));

		FontData[] boldFontData = getModifiedFontData(tree.getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		setLabelProvider(new JsonLabelProvider(boldFont));
	
		createLabelColumn(tree, "Label");
		createContextMenu(this);
		
		jsonContentProvider = new JsonContentProvider(cfsConfig);
		setContentProvider(jsonContentProvider);
		setInput(namedObject);
		
		addSelectionChangedListener(this);	
		toolkit.paintBordersFor(tree);
	}

	private void setNamedObject() {
		namedObject = new NamedObject();
		namedObject.setName("ROOT");
		JsonObject module = cfsConfig.fullGetElement(jsonPath).getAsJsonObject();
		namedObject.setObject(module);
		namedObject.setPath(jsonPath);
	}
	
	private void createLabelColumn(Tree currentTree, String columnLabel) {
		TreeColumn column = new TreeColumn(currentTree, SWT.NONE);
		column.setWidth(150);
		column.setText(columnLabel);
		currentTree.setHeaderVisible(true);
		currentTree.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
		Object selectedNode = thisSelection.getFirstElement();
		if (selectedNode != null) {
			NamedObject namedObject = (NamedObject) selectedNode; 
			JsonElement selectedElem = (JsonElement) namedObject.getObject();
			ModuleConfigEditor s = (ModuleConfigEditor) getTree().getParent();
			
			if (selectedElem.isJsonObject()) {
				s.goUpdate(namedObject.getName(), selectedElem, namedObject);
				getTree().getParent().layout(true, true);
			} else {
				NamedObject parentObject = (NamedObject) jsonContentProvider.getEntryParent(namedObject);
				s.setNewKeyValue(namedObject.getName(), selectedElem.getAsString(), parentObject);
			}
		}
	}
	
	public void refreshTreeViewer() {
		refresh();
	}
	
	private static FontData[] getModifiedFontData(FontData[] originalData, int additionalStyle) {
		FontData[] styleData = new FontData[originalData.length];
		for (int i = 0; i < styleData.length; i++) {
			FontData base = originalData[i];
			styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
		}
		return styleData;
	}
	
	protected void createContextMenu(Viewer viewer) {
		contextMenu = new MenuManager("Json Menu");
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});
		menu = contextMenu.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
	}
	
	protected void fillContextMenu(IMenuManager contextMenu) {
		contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		contextMenu.add(new Action("Add") {
			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				Shell shell = window.getShell();
				String fields = null;
				
				if (getSelection().isEmpty()) {
					return;
				}
				if (getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) getSelection();
					Object selectedNode = selection.getFirstElement();
					
					if (selectedNode != null) {
						NamedObject namedObject = (NamedObject) selectedNode; 
						JsonElement selectedElem = (JsonElement) namedObject.getObject();
						ModuleConfigEditor s = (ModuleConfigEditor) getTree().getParent();
						CFSDialog dialog = null;
						if (selectedElem.isJsonObject()) {
							/** restrictive or ease of use portion
							Set<Entry<String, JsonElement>> temp = selectedElem.getAsJsonObject().entrySet();
							if (temp.isEmpty()) {
								HashMap<String, String> empty = new HashMap<String, String>();
								dialog = new CFSDialog(shell, empty, true); 
							} else {
								Iterator it = temp.iterator();
								HashMap<String, String> childrenKeysTypes = getKeys(it.next());
								dialog = new CFSDialog(shell, childrenKeysTypes, false);
							} **/
							dialog = new CFSDialog(shell); 
							JsonElement toAdd = null;
							String name = null;
							if (dialog.open() == Window.OK) {
								toAdd = dialog.getJsonElement();
								name = d
							}
							
						} 
						
					}
				}

				if(dia.open() == Window.OK) {
					pathA = dia.pathOne();
					//pathB = dia.pathTwo();
					//pathSaved = dia.pathSaved();
				} else if (dia.open() == Window.CANCEL) {
					
				}
			}
		});
	}
	
	protected HashMap<String, String> getKeys(Object entry) {
		HashMap<String, String> keys = new HashMap<String, String>();
		JsonElement exampleJson = (JsonElement) entry;
		for (Map.Entry<String, JsonElement> field : exampleJson.getAsJsonObject().entrySet()) {
			if (field.getValue().isJsonPrimitive() || field.getValue().isJsonNull()) {
				keys.put(field.getKey(), "primitive");
			} else if (field.getValue().isJsonObject()) {
				keys.put(field.getKey(), "object");
			}
		}
		return keys;		
	}
}
