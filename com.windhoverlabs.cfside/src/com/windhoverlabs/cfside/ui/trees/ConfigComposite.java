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
import org.eclipse.swt.layout.FillLayout;
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
	Composite holder;
	
	ScrollableGroups aGroup;
	
	HashMap<String, ArrayList<String>> groupLabels = new HashMap<String, ArrayList<String>>();
	HashMap<String, JsonObject> commonGroups = new HashMap<String, JsonObject>();
	HashMap<String, TableViewer> tableViewerMaps = new HashMap<String, TableViewer>();
	HashMap<String, Table> tableMaps = new HashMap<String, Table>();
	
	String currentFilePath;
	String currentFileName;
	JsonElement currentJsonElement;
	
	JsonParser jsonParser;
	
	public ConfigComposite(Composite parent, int style, JsonElement currentJsonElement) {
		super(parent, style);
		this.currentJsonElement = currentJsonElement;
		createObjectViewer(parent);
		createTableKeyValueViewer(parent);
		treeViewer.refresh();
	}
	
	public ConfigComposite(Composite parent, int style, String currentJsonPath) {
		super(parent, style);
		readFile(currentJsonPath);
		createObjectViewer(parent);
		createTableKeyValueViewer(parent);
		treeViewer.refresh();
	}

	private void readFile(String pathToJson) {
		JsonParser jsonParser = new JsonParser();
		
		Reader rd = null;
		try {
			rd = new FileReader(new File(pathToJson));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.currentJsonElement = jsonParser.parse(rd);
		
	}
	private void createObjectViewer(Composite parent) {
		jsonParser = new JsonParser();
		tree = new Tree(this, SWT.FILL);
		tree.setHeaderVisible(true);
		
		objectLabel = new TreeColumn(tree, SWT.NONE);
		objectLabel.setText("Current file : " + this.currentFileName);
		objectLabel.setWidth(300);
		
		objectChildrenCount = new TreeColumn(tree, SWT.NONE);
		objectChildrenCount.setText("Count");
		objectChildrenCount.setWidth(100);
			
		
		
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
					updateTable(jtn.getName(), newElement, holder);
					//parent.layout();
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, holder);
					//parent.layout();
				}
			}
		});
	
	}
	
	private void updateTable(String name, JsonElement newInput, Composite parent) {
		aGroup.dispose();
		aGroup = new ScrollableGroups(parent, SWT.FILL, newInput, name); 
		parent.layout(true, true);
	}
	
	private void createTableKeyValueViewer(Composite parent) {
		String empty = "{}";
		JsonElement je = jsonParser.parse(empty);
		holder = new Composite(parent, SWT.FILL);
		holder.setLayout(new FillLayout(SWT.VERTICAL));
		
		keyValueTable = new KeyValueTable(holder, SWT.FILL, je);
				
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
		aGroup = new ScrollableGroups(holder, SWT.FILL, (JsonElement) je, "Empty");
		//scrolledComposite.layout(true, true);
		//scrolledComposite.setMinSize(compositeHolder.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
}
