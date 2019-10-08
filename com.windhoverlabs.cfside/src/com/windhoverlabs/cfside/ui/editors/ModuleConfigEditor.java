package com.windhoverlabs.cfside.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.tables.ConfigTableEditor;
import com.windhoverlabs.cfside.ui.trees.ConfigTreeViewer;
import com.windhoverlabs.cfside.ui.trees.NamedObject;
import com.windhoverlabs.cfside.utils.CfsConfig;

public class ModuleConfigEditor extends SashForm {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	private ConfigTreeViewer treeViewer;
	private ConfigTableEditor editor;
	private CfsConfig cfsConfig;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ModuleConfigEditor(Composite parent, int style, String jsonPath, CfsConfig cfsConfig) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(null);
		
		
		this.cfsConfig = cfsConfig;
		treeViewer = new ConfigTreeViewer(this, SWT.BORDER, jsonPath, cfsConfig);
		editor = new ConfigTableEditor(this, SWT.BORDER, cfsConfig.getFull(), null, cfsConfig);
		
		/**
		Composite composite = toolkit.createComposite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		toolkit.paintBordersFor(composite);
		composite.setLayout(null);
		setWeights(new int[] {137, 310});
		**/
	}
	
	public void goUpdate(String name, JsonElement newInput, NamedObject namedObj) {
		editor.dispose();
		editor = new ConfigTableEditor(this, SWT.BORDER, newInput, namedObj, cfsConfig);
		layout(true, true);
	}
}
