package com.windhoverlabs.cfside.ui.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.google.gson.JsonElement;
import com.windhoverlabs.cfside.ui.trees.ConfigComposite;
import com.windhoverlabs.cfside.ui.trees.JsonContentProvider;
import com.windhoverlabs.cfside.ui.trees.JsonLabelProvider;
import com.windhoverlabs.cfside.ui.trees.JsonObjectTreeNode;
import com.windhoverlabs.cfside.ui.trees.JsonValueTreeNode;

public class SingleConfigTreeViewer extends Composite {
	
	private Tree tree;
	private TreeColumn objectLabel;
	private TreeColumn objectChildrenCount;
	private TreeViewer treeViewer;
	private String currentFileName;
	private JsonElement currentJson;
	private ConfigComposite parentHolder;
	
	public SingleConfigTreeViewer(SashForm parent, int style, JsonElement root) {
		super(parent, style);
		this.currentJson = root;
		this.currentFileName = currentFileName;	
		
		createTree(parent, root);
	}
	
	private void createTree(Composite parent, JsonElement root) {
		tree = new Tree(parent, SWT.NONE);
		tree.setHeaderVisible(true);
		
		objectLabel = new TreeColumn(tree, SWT.NONE);
		objectLabel.setText("Current file : ");
		objectLabel.setWidth(300);
		
		objectChildrenCount = new TreeColumn(tree, SWT.NONE);
		objectChildrenCount.setText("Count");
		objectChildrenCount.setWidth(100);
	
		//JsonElement jsonElement = JsonObjectsUtil.goMerge(new File("/home/vagrant/development/airliner/config/bebop2/config.json"));
		treeViewer = new TreeViewer(this.tree);
		treeViewer.setContentProvider(new JsonContentProvider());
		treeViewer.setLabelProvider(new JsonLabelProvider());
		treeViewer.setInput(new JsonObjectTreeNode(currentJson, "root"));
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				if (selectedNode instanceof JsonObjectTreeNode) {
					JsonObjectTreeNode jtn = (JsonObjectTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					//keyValueTable.setNewConfig(newElement);
				//	updateTable(jtn.getName(), newElement, holder);
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					//keyValueTable.setNewConfig(newElement);
				//	updateTable(jtn.getName(), newElement, holder);
				}
			}
			
		});
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}

}
