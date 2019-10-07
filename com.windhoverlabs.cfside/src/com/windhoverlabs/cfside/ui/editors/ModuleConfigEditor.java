package com.windhoverlabs.cfside.ui.editors;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.gson.JsonObject;
import com.windhoverlabs.cfside.ui.trees.JsonContentProvider;
import com.windhoverlabs.cfside.ui.trees.JsonLabelProvider;
import com.windhoverlabs.cfside.ui.trees.JsonObjectTreeNode;
import com.windhoverlabs.cfside.ui.editors.ConfigTreeViewer;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;

public class ModuleConfigEditor extends SashForm {

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ModuleConfigEditor(Composite parent, int style, JsonObject moduleConfig, JsonObject fullConfig) {
		super(parent, style);
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(null);
		
		ConfigTreeViewer treeViewer = new ConfigTreeViewer(this, SWT.BORDER, moduleConfig);
		
		Composite composite = toolkit.createComposite(this, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		toolkit.paintBordersFor(composite);
		composite.setLayout(null);
		setWeights(new int[] {137, 310});

	}
}
