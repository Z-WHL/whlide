package com.windhoverlabs.cfside.ui.trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.windhoverlabs.cfside.ui.composites.ConfigTableComposite;
import com.windhoverlabs.cfside.ui.tables.KeyValueTable;
import com.windhoverlabs.cfside.ui.tables.ScrollableGroups;

public class ConfigComposite extends Composite {
	
	Composite configTreeHolder;
	Tree tree;
	TreeViewer treeViewer;
	TreeColumn objectLabel;
	TreeColumn objectChildrenCount;
	
	Composite tableKeyValueHolder;
	ConfigTableComposite tableKeyValueViewer;
	KeyValueTable keyValueTable;

	ScrollableGroups scrollableGroups;
	ScrolledComposite scrollableHolder;
	
	ScrollableGroups aGroup;
	
	HashMap<String, ArrayList<String>> groupLabels = new HashMap<String, ArrayList<String>>();
	HashMap<String, JsonObject> commonGroups = new HashMap<String, JsonObject>();
	HashMap<String, TableViewer> tableViewerMaps = new HashMap<String, TableViewer>();
	HashMap<String, Table> tableMaps = new HashMap<String, Table>();
	
	String currentFilePath;
	String currentFileName;
	JsonElement currentJsonElement;
	
	JsonParser jsonParser;
	
	public ConfigComposite(Composite parent, int style, String currentFile) {
		super(parent, style);
		this.currentFilePath = currentFile;
		createObjectViewer(parent);
		createTableKeyValueViewer(parent);
		treeViewer.refresh();
	}

	
	private void createObjectViewer(Composite parent) {
		jsonParser = new JsonParser();
		tree = new Tree(this, SWT.NONE);
		tree.setHeaderVisible(true);
		
		objectLabel = new TreeColumn(tree, SWT.NONE);
		objectLabel.setText("Current file : " + this.currentFileName);
		objectLabel.setWidth(300);
		
		objectChildrenCount = new TreeColumn(tree, SWT.NONE);
		objectChildrenCount.setText("Count");
		objectChildrenCount.setWidth(100);
			
		Reader rd = null;
		try {
			rd = new FileReader(new File(currentFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.currentJsonElement = jsonParser.parse(rd);

		
		//JsonElement jsonElement = JsonObjectsUtil.goMerge(new File("/home/vagrant/development/airliner/config/bebop2/config.json"));
		treeViewer = new TreeViewer(this.tree);
		treeViewer.setContentProvider(new JsonContentProvider());
		treeViewer.setLabelProvider(new JsonLabelProvider());
		treeViewer.setInput(new JsonObjectTreeNode(currentJsonElement, "root"));
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				if (selectedNode instanceof JsonObjectTreeNode) {
					JsonObjectTreeNode jtn = (JsonObjectTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, parent);
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, parent);
				}
			}
		});
	
	}
	
	private void updateTable(String name, JsonElement newInput, Composite parent) {
		aGroup.dispose();
		aGroup = new ScrollableGroups(parent, SWT.NONE, newInput, name); 
		parent.layout(true, true);
		parent.pack(true);
	}
	
	private void createTableKeyValueViewer(Composite parent) {
		String empty = "{}";
		JsonElement je = jsonParser.parse(empty);

		keyValueTable = new KeyValueTable(this, SWT.FILL, je);
				
				//scrollableHolder = new ScrolledComposite(this, SWT.FILL | SWT.V_SCROLL); 
				//Composite contentHolder = new Composite(this, SWT.NONE);
						//contentHolder.setLayout(new FillLayout(SWT.HORIZONTAL));
				/**
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER);
		scrolledComposite.setExpandHorizontal(true);			
		scrolledComposite.setExpandVertical(true);
		
		Composite compositeHolder = new Composite(scrolledComposite, SWT.NONE);
		compositeHolder.setLayout(new GridLayout(1, false));
		scrolledComposite.setContent(compositeHolder);
		scrolledComposite.setSize(compositeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		
		Composite comp = new Composite(compositeHolder, SWT.NONE);
		**/
		aGroup = new ScrollableGroups(parent, SWT.NONE, (JsonElement) je, "Empty");
		//scrolledComposite.layout(true, true);
		//scrolledComposite.setMinSize(compositeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
}
