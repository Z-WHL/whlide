package com.windhoverlabs.cfside.ui.trees;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.editors.ModuleConfigEditor;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ConfigTreeViewer extends TreeViewer implements ISelectionChangedListener {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	/**
	 * @wbp.parser.entryPoint
	 */
	public ConfigTreeViewer(Composite parent, int style, String jsonPath, CfsConfig cfsConfig) {
		super(parent, style);
		
		NamedObject namedObject = new NamedObject();
		Tree tree = getTree();
		tree.setBackground(SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
		toolkit.paintBordersFor(tree);
		

		FontData[] boldFontData = getModifiedFontData(tree.getFont().getFontData(), SWT.BOLD);
		Font boldFont = new Font(Display.getCurrent(), boldFontData);
		
		setLabelProvider(new JsonLabelProvider(boldFont));
		setContentProvider(new JsonContentProvider(cfsConfig));

		namedObject.setName("ROOT");
		JsonObject module = cfsConfig.fullGetElement(jsonPath).getAsJsonObject();
		namedObject.setObject(module);
		namedObject.setPath(jsonPath);
		
		// Let's create columns for feauture extensibility. For now, just one column to display the names of the jsonObjects or primitives.
		createLabelColumn(tree, "Label");
		
		final Action a = new Action("") {
		};
		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection selection = getStructuredSelection();
				if (!selection.isEmpty()) {
					a.setText((String) selection.getFirstElement());
					menuManager.add(a);
				}
			}
			
		});
		
		getControl().setMenu(menuManager.createContextMenu(getControl()));
		setInput(namedObject);

		addSelectionChangedListener(this);
		
		
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
}
