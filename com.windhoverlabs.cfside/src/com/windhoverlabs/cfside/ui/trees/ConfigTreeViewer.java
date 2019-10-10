package com.windhoverlabs.cfside.ui.trees;

import java.util.HashMap;
import java.util.Map;

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
	private CfsConfig cfsConfig;
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, String jsonPath, CfsConfig cfsConfig) {
		super(parent, style);
		this.cfsConfig = cfsConfig;
		NamedObject namedObject = new NamedObject();
		Tree tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		toolkit.paintBordersFor(tree);
		

		FontData[] boldFontData = getModifiedFontData(tree.getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		
		setLabelProvider(new JsonLabelProvider(boldFont));
		jsonContentProvider = new JsonContentProvider(cfsConfig);
		setContentProvider(jsonContentProvider);

		namedObject.setName("ROOT");
		JsonObject module = cfsConfig.fullGetElement(jsonPath).getAsJsonObject();
		namedObject.setObject(module);
		namedObject.setPath(jsonPath);
		
		// Let's create columns for feauture extensibility. For now, just one column to display the names of the jsonObjects or primitives.
		createLabelColumn(tree, "Label");
		
		
		
		setInput(namedObject);

		addSelectionChangedListener(this);
		createMenu(ConfigTreeViewer.this);
		
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
	public void createMenu(Viewer viewer) {
		final Action a = new Action("Add") {
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
							}
							cfsConfig.save(addToConfig);
							refresh(namedObject);
						} 
						
					}
				}
			}
		};
		
		MenuManager menumgr = new MenuManager();
		Menu men = menumgr.createContextMenu(viewer.getControl());
		
		menumgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				a.setText("Add");
				menumgr.add(a);
				ConfigTreeViewer.this.fillContextMenu(mgr);
			}
		});
		menumgr.setRemoveAllWhenShown(true);

		viewer.getControl().setMenu(men);
		ConfigTreeViewer.this.refresh();
	}
	
	protected void fillContextMenu(IMenuManager contextMenu) {
		contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
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
