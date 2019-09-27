package com.windhoverlabs.cfside.ui.views;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

public final class TreeViewerFactory {
	/**
	 * @wbp.factory
	 */
	public static Composite createComposite(Composite parent, String name) {
		Composite spj = new SingleProjectTreeViewer(parent, SWT.NONE, name);
		return spj;
	}
}