package com.windhoverlabs.cfside.ui.trees;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.actions.TreeContextMenuActions;
import com.windhoverlabs.cfside.ui.dialogs.AddObjectDialog;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTreeViewer extends TreeViewer implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private JsonContentProvider jsonContentProvider;
	private CfsConfig cfsConfig;
	NamedObject namedObject;
	String moduleName;
	String jsonPath;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, String jsonPath, CfsConfig cfsConfig, String moduleName) {
		super(parent, style);
		this.cfsConfig = cfsConfig;
		this.jsonPath = jsonPath;
		this.moduleName = moduleName;

		Tree tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		toolkit.paintBordersFor(tree);
	
		FontData[] boldFontData = getModifiedFontData(tree.getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		
		setLabelProvider(new JsonLabelProvider(boldFont));
		jsonContentProvider = new JsonContentProvider(cfsConfig);
		setContentProvider(jsonContentProvider);
		namedObject = new NamedObject();
		namedObject.setName("ROOT");
		JsonObject partial = new JsonObject();
		JsonObject pointerToPartial = partial;
		partial.add("modules", new JsonObject());
		partial = partial.get("modules").getAsJsonObject();
		JsonObject module = cfsConfig.fullGetElement(jsonPath).getAsJsonObject();
		partial.add(moduleName, module);
		namedObject.setObject(partial);
		namedObject.setPath("modules");
		
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
				s.goUpdate(namedObject.getName(), selectedElem, namedObject, cfsConfig);
				getTree().getParent().layout(true, true);
			}
		}
	}
	
	public void refreshTreeViewer(NamedObject namedObject, CfsConfig cfsConfig) {
		this.cfsConfig = cfsConfig;
		
		/**
		final Object[] expanded = getExpandedElements();
		NamedObject[] named = new NamedObject[expanded.length];
		TreePath[] expandedTreePath = getExpandedTreePaths();
		final ISelection selection = getSelection();
		
		setInput(this.namedObject);
		for (int i = 0; i < expanded.length; i++) {
			NamedObject temp = (NamedObject) expanded[i];
			named[i] = temp;
			System.out.println(expanded[i].toString());
		}
		setExpandedElements(expanded);
		setExpandedTreePaths(expandedTreePath);
		setSelection(selection);
			**/
		refresh();
		System.out.println("before refreshing, waht is the value of overriden " + namedObject.getOverridden());
		
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
		final Action addObject = TreeContextMenuActions.createActionAddObject(ConfigTreeViewer.this, cfsConfig);
		final Action addArray = TreeContextMenuActions.createActionAddArray(ConfigTreeViewer.this, cfsConfig);
		final Action delete = TreeContextMenuActions.createActionDelete(ConfigTreeViewer.this, cfsConfig);
		final Action unoverride = TreeContextMenuActions.createActionUnoverride(ConfigTreeViewer.this, cfsConfig);

		
		MenuManager menumgr = new MenuManager();
		Menu men = menumgr.createContextMenu(viewer.getControl());
		
		menumgr.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				if (getSelection() instanceof IStructuredSelection) {
					IStructuredSelection selection = (IStructuredSelection) getSelection();
					Object selectedNode = selection.getFirstElement();
					if (selectedNode != null) {
						NamedObject namedObject = (NamedObject) selectedNode; 
						JsonElement selectedElem = (JsonElement) namedObject.getObject();
						ModuleConfigEditor s = (ModuleConfigEditor) getTree().getParent();
						AddObjectDialog dialog = null;
						if (selectedElem.isJsonObject() || selectedElem.isJsonArray()) {
							addObject.setText("Add Object");
							menumgr.add(addObject);
							addArray.setText("Add Array");
							menumgr.add(addArray);
							delete.setText("Delete");
							menumgr.add(delete);
							if (namedObject.getOverridden()) {
								unoverride.setText("Unoverride");
								menumgr.add(unoverride);
							}
						}
				}
				ConfigTreeViewer.this.fillContextMenu(mgr);
			}
		}});
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
