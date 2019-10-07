package com.windhoverlabs.cfside.ui.trees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.windhoverlabs.cfside.ui.composites.ConfigTableComposite;
import com.windhoverlabs.cfside.ui.tables.KeyValueTable;
import com.windhoverlabs.cfside.ui.tables.ScrollableGroups;


public class ConfigComposite extends SashForm {
	
	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
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
	
	JsonParser jsonParser = new JsonParser();
	
	SashForm sashForm;
	
	/**
	 * @wbp.parser.constructor
	 */
	public ConfigComposite(Composite parent, int style, JsonElement currentJsonElement) {
		super(parent, style);
		this.currentJsonElement = currentJsonElement;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		sashForm = new SashForm(this, SWT.HORIZONTAL);
		sashForm.setLayout(new FillLayout());
		
		//Composite singleConfigTree = new SingleConfigTreeViewer(sashForm, SWT.FILL, currentJsonElement);
		createObjectViewer(sashForm);
		createTableKeyValueViewer(sashForm);
		//sashForm.setWeights(new int[] {372, 435});
		//((SingleConfigTreeViewer) singleConfigTree).refreshViewer();
		parent.layout();
		treeViewer.refresh();
	}
	
	/**
	public ConfigComposite(Composite parent, int style, String currentJsonPath) {
		super(parent, style);
		readFile(currentJsonPath);
		createObjectViewer(parent);
		createTableKeyValueViewer(parent);
		treeViewer.refresh();
	}
	**/

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
		tree = new Tree(parent, SWT.NONE);
		tree.setLayout(new FillLayout());
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
		
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				if (selectedNode instanceof JsonObjectTreeNode) {
					JsonObjectTreeNode jtn = (JsonObjectTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, holder);
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, holder);
				}
			}
			
		});
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
				} else {
					JsonValueTreeNode jtn = (JsonValueTreeNode) selectedNode;
					JsonElement newElement = jtn.getJsonElement();
					keyValueTable.setNewConfig(newElement);
					updateTable(jtn.getName(), newElement, holder);
				}
			}
		});
	}
	
	private void updateTable(String name, JsonElement newInput, Composite parent) {
		aGroup.dispose();
		aGroup = new ScrollableGroups(parent, SWT.FILL, newInput, name); 
		parent.layout(true, true);
	}
	
	protected void newKeyValueConfig(JsonElement replace) {
		keyValueTable.setNewConfig(replace);
	}
	
	private void createTableKeyValueViewer(Composite hold) {
		String empty = "{}";
		JsonElement je = jsonParser.parse(empty);
		holder = new Composite(hold, SWT.FILL);
		holder.setLayout(new FillLayout(SWT.VERTICAL));
		
		keyValueTable = new KeyValueTable(holder, SWT.FILL, je);
		
		aGroup = new ScrollableGroups(holder, SWT.FILL, (JsonElement) je, "Empty");
		
	}
	
	public void refreshViewer() {
		treeViewer.refresh();
	}
}
