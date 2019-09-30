package com.windhoverlabs.cfside.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PerspectiveAdapter;

public class PerspectiveListenerAdapater extends PerspectiveAdapter {
	private WorkbenchEditorsTracker workbenchEditorsTracker;
    
    public PerspectiveListenerAdapater(WorkbenchEditorsTracker workbenchEditorsTracker )
    {
        this.workbenchEditorsTracker = workbenchEditorsTracker;
    }
     
    @Override
    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspectiveDescriptor) {
        super.perspectiveActivated(page, perspectiveDescriptor);
        
        // Hide all the editors
        IEditorReference[] editors = page.getEditorReferences();
        for (int i = 0; i < editors.length; i++) {
            page.hideEditor(editors[i]);
        }
  
        // Show the editors associated with this perspective
        ArrayList<IEditorReference> editorRefs = workbenchEditorsTracker.getEditorsForPerspective(perspectiveDescriptor.getId());
        if (editorRefs != null) {
            for (Iterator<IEditorReference> it = editorRefs.iterator(); it.hasNext(); ) {
                IEditorReference editorInput = it.next();
                page.showEditor(editorInput);
            }
  
            // Send the last active editor to the top
            IEditorReference lastActiveRef = workbenchEditorsTracker.getLastActiveEditor(perspectiveDescriptor.getId());
            if (lastActiveRef!=null)
                page.bringToTop(lastActiveRef.getPart(true));
        }
    }
 
     
    public void perspectiveDeactivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        IEditorPart activeEditor = page.getActiveEditor();
        if (activeEditor != null) {
            // Find the editor reference that relates to this editor input
            IEditorReference[] editorRefs = page.findEditors(activeEditor.getEditorInput(), null, IWorkbenchPage.MATCH_INPUT);
            if (editorRefs.length > 0) {
                workbenchEditorsTracker.setLastActiveEditor(perspective.getId(), editorRefs[0]);
            }
        }
    }
}
