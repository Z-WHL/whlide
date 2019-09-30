package com.windhoverlabs.cfside.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.EditorPart;

/**
 * Tracks which files or editors are opened and closes and opens them on demand.
 * @author vagrant
 *
 */
public class WorkbenchEditorsTracker implements IPartListener {
	
	//Editors per perspective
	private HashMap<String, ArrayList<IEditorReference>> perspectiveEditors = new HashMap<String, ArrayList<IEditorReference>>();
	
	//Current editor per perspective
	private HashMap<String, IEditorReference> lastActiveEditors = new HashMap<String, IEditorReference>();
	
	@Override
	public void partActivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		if (part instanceof EditorPart) {
			IWorkbenchPage page = part.getSite().getPage();
			IPerspectiveDescriptor activePerspective = page.getPerspective();
			ArrayList<IEditorReference> editors = perspectiveEditors.get(activePerspective.getId());
			if (editors == null) {
				return;
			}
			Iterator<IEditorReference> iterator = editors.iterator();
            IEditorReference referenceToRemmove=null;
            while (iterator.hasNext())
               {
                   IEditorReference reference = iterator.next();
                   if (reference.getPart(false) == part)
                   {
                       referenceToRemmove = reference;
                       break;
                   }
               }
               if (referenceToRemmove!=null)
                   editors.remove(referenceToRemmove);
        }
   }

    
   @Override
   public void partDeactivated(IWorkbenchPart part) {
       // TODO Auto-generated method stub
        
   }

   @Override
   public void partOpened(IWorkbenchPart part) {
       if (part instanceof EditorPart) {
           EditorPart editor = (EditorPart)part;
           IWorkbenchPage page = part.getSite().getPage();
           IEditorInput editorInput = editor.getEditorInput();
           IPerspectiveDescriptor activePerspective = page.getPerspective();
 
           ArrayList<IEditorReference> editors = perspectiveEditors.get(activePerspective.getId());
           if (editors == null)
               editors = new ArrayList<IEditorReference>();
 
           // Find the editor reference that relates to this editor input
           IEditorReference[] editorRefs = page.findEditors(editorInput, null, IWorkbenchPage.MATCH_INPUT);
 
           if (editorRefs.length > 0) {
               editors.add(editorRefs[0]);
               perspectiveEditors.put(activePerspective.getId(), editors);
           }
       }
   }

   public ArrayList<IEditorReference> getEditorsForPerspective(String activePerspectiveId )
   {
       return perspectiveEditors.get(activePerspectiveId);
   }
    
   public IEditorReference getLastActiveEditor(String activePerspectiveId )
   {
       return lastActiveEditors.get(activePerspectiveId);
   }
    
   public void setLastActiveEditor(String perspectiveID, IEditorReference editor) 
   {
       lastActiveEditors.put(perspectiveID, editor);
        
   }
}
