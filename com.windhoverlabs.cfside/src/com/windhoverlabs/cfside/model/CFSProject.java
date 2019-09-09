package com.windhoverlabs.cfside.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IModuleDescription;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.eval.IEvaluationContext;

import com.windhoverlabs.cfside.core.ContextManager;
import com.windhoverlabs.cfside.natures.CFSProjectCoreNature;

public final class CFSProject implements ICFSProject, IAdaptable {
	public static final String METAFILE = "CFS_Project_Descriptor.xml";
	public static final String CLDC_APPLICATION = "CFS Project";
	
	private final IJavaProject _eclipseJavaProject;
	private CFSProperties _properties;
	private IFile _metaFileHandler;
	
	public CFSProject(final IJavaProject eclipseJavaProject) {
		if (eclipseJavaProject == null) {
			throw new IllegalArgumentException("Eclipse Java Project can't be null.");
		}
		
		_eclipseJavaProject = eclipseJavaProject;
		_properties = ContextManager.PLUGIN.getCFSProperties(eclipseJavaProject.getProject().getName(), false);
		addStore();
	}
	
	public void addStore() {
		final IProject eclipseProject = getProject();
		try {
			if (!eclipseProject.hasNature(CFSProjectCoreNature.NATURE_ID)) {
				return;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		_metaFileHandler = eclipseProject.getFile(CFSProject.METAFILE);
		
		if (!_metaFileHandler.exists()) {
			final File metaFile = _metaFileHandler.getLocation().toFile();
			
			if (!metaFile.exists()) {
				try {
					metaFile.createNewFile();
					_metaFileHandler.create(new FileInputStream(metaFile), true, new NullProgressMonitor());
				} catch (final Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}

	@Override
	public IClasspathEntry decodeClasspathEntry(final String encodedEntry) {
        return _eclipseJavaProject.decodeClasspathEntry( encodedEntry );

	}

	@Override
	public Set<String> determineModulesOfProjectsWithNonEmptyClasspath() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeClasspathEntry(final IClasspathEntry classpathEntry) {
        return _eclipseJavaProject.encodeClasspathEntry( classpathEntry );

	}

	@Override
	public IJavaElement findElement(final IPath path) throws JavaModelException {
        return _eclipseJavaProject.findElement( path );

	}

	@Override
	public IJavaElement findElement(final IPath path, final WorkingCopyOwner owner) throws JavaModelException {
        return _eclipseJavaProject.findElement( path, owner );

	}

	@Override
	public IJavaElement findElement(final String bindingKey, final WorkingCopyOwner owner) throws JavaModelException {
        return _eclipseJavaProject.findElement( bindingKey, owner );

	}

	@Override
	public IModuleDescription findModule(final String bindingKey, final WorkingCopyOwner owner) throws JavaModelException {
		return null;
	}

	@Override
	public IPackageFragment findPackageFragment(final IPath path) throws JavaModelException {
        return _eclipseJavaProject.findPackageFragment( path );

	}

	@Override
	public IPackageFragmentRoot findPackageFragmentRoot(final IPath path) throws JavaModelException {
        return _eclipseJavaProject.findPackageFragmentRoot( path );

	}

	@Override
	public IPackageFragmentRoot[] findPackageFragmentRoots(final IClasspathEntry entry) {
        return _eclipseJavaProject.findPackageFragmentRoots( entry );

	}

	@Override
	public IType findType(final String fullyQualifiedName ) throws JavaModelException {
        return _eclipseJavaProject.findType( fullyQualifiedName );

	}

	@Override
	public IType findType(final String fullyQualifiedName, final IProgressMonitor progressMonitor ) throws JavaModelException {
        return _eclipseJavaProject.findType( fullyQualifiedName, progressMonitor );

	}

	@Override
	public IType findType(final String fullyQualifiedName, final WorkingCopyOwner owner) throws JavaModelException {
        return _eclipseJavaProject.findType( fullyQualifiedName, owner );

	}

	@Override
	public IType findType(final String packageName, final String typeQualifiedName) throws JavaModelException {
        return _eclipseJavaProject.findType( packageName, typeQualifiedName );

	}

	@Override
	public IType findType(final String fullyQualifiedName, final WorkingCopyOwner owner, final IProgressMonitor progressMonitor) throws JavaModelException {
        return _eclipseJavaProject.findType( fullyQualifiedName, owner, progressMonitor );

	}

	@Override
	public IType findType(final String packageName, final String typeQualifiedName, final IProgressMonitor progressMonitor) throws JavaModelException {
        return _eclipseJavaProject.findType( packageName, typeQualifiedName, progressMonitor );

	}

	@Override
	public IType findType(final String packageName, final String typeQualifiedName, final WorkingCopyOwner owner) throws JavaModelException {
        return _eclipseJavaProject.findType( packageName, typeQualifiedName, owner );

	}

	@Override
	public IType findType(final String packageName, final String typeQualifiedName, final WorkingCopyOwner owner,
            final IProgressMonitor progressMonitor)
			throws JavaModelException {
        return _eclipseJavaProject.findType( packageName, typeQualifiedName, owner, progressMonitor );

	}

	@Override
	public IPackageFragmentRoot[] findUnfilteredPackageFragmentRoots(IClasspathEntry arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPackageFragmentRoot[] getAllPackageFragmentRoots() throws JavaModelException {
        return _eclipseJavaProject.getAllPackageFragmentRoots();

	}

	@Override
	public IClasspathEntry getClasspathEntryFor(IPath arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModuleDescription getModuleDescription() throws JavaModelException {
		return null;
	}

	@Override
	public Object[] getNonJavaResources() throws JavaModelException {
        return _eclipseJavaProject.getNonJavaResources();

	}

	@Override
	public String getOption(final String optionName, final boolean inheritJavaCoreOptions) {
        return _eclipseJavaProject.getOption( optionName, inheritJavaCoreOptions );

	}

	@Override
	public Map<String, String> getOptions(final boolean inheritJavaCoreOptions) {
        return _eclipseJavaProject.getOptions( inheritJavaCoreOptions );

	}

	@Override
	public IPath getOutputLocation() throws JavaModelException {
        return _eclipseJavaProject.getOutputLocation();

	}

	@Override
	public IPackageFragmentRoot getPackageFragmentRoot(final String externalLibraryPath) {
        return _eclipseJavaProject.getPackageFragmentRoot( externalLibraryPath );

	}

	@Override
	public IPackageFragmentRoot getPackageFragmentRoot(final IResource resource) {
        return _eclipseJavaProject.getPackageFragmentRoot( resource );

	}

	@Override
	public IPackageFragmentRoot[] getPackageFragmentRoots() throws JavaModelException {
        return _eclipseJavaProject.getAllPackageFragmentRoots();

	}

	@Override
	public IPackageFragmentRoot[] getPackageFragmentRoots(final IClasspathEntry entry) {
        return _eclipseJavaProject.getPackageFragmentRoots( entry );

	}

	@Override
	public IPackageFragment[] getPackageFragments() throws JavaModelException {
        return _eclipseJavaProject.getPackageFragments();

	}

	@Override
	public IProject getProject() {
        return _eclipseJavaProject.getProject();

	}

	@Override
	public IClasspathEntry[] getRawClasspath() throws JavaModelException {
        return _eclipseJavaProject.getRawClasspath();

	}

	@Override
	public IClasspathEntry[] getReferencedClasspathEntries() throws JavaModelException {
        return _eclipseJavaProject.getReferencedClasspathEntries();

	}

	@Override
	public String[] getRequiredProjectNames() throws JavaModelException {
        return _eclipseJavaProject.getRequiredProjectNames();

	}

	@Override
	public IClasspathEntry[] getResolvedClasspath(final boolean ignoreUnresolvedEntry) throws JavaModelException {
        return _eclipseJavaProject.getResolvedClasspath( ignoreUnresolvedEntry );

	}

	@Override
	public boolean hasBuildState() {
        return _eclipseJavaProject.hasBuildState();

	}

	@Override
	public boolean hasClasspathCycle(final IClasspathEntry[] entries) {
        return _eclipseJavaProject.hasClasspathCycle( entries );

	}

	@Override
	public boolean isOnClasspath(final IJavaElement element) {
        return _eclipseJavaProject.isOnClasspath( element );

	}

	@Override
	public boolean isOnClasspath(final IResource resource) {
        return _eclipseJavaProject.isOnClasspath( resource );

	}

	@Override
	public IEvaluationContext newEvaluationContext() {
        return _eclipseJavaProject.newEvaluationContext();

	}

	@Override
	public ITypeHierarchy newTypeHierarchy(final IRegion region, final IProgressMonitor monitor) throws JavaModelException {
        return _eclipseJavaProject.newTypeHierarchy( region, monitor );

	}

	@Override
	public ITypeHierarchy newTypeHierarchy(final IRegion region, final WorkingCopyOwner owner, final IProgressMonitor monitor)
			throws JavaModelException {
        return _eclipseJavaProject.newTypeHierarchy( region, owner, monitor );

	}

	@Override
	public ITypeHierarchy newTypeHierarchy(final IType type, final IRegion region, final IProgressMonitor monitor) throws JavaModelException {
        return _eclipseJavaProject.newTypeHierarchy( type, region, monitor );

	}

	@Override
	public ITypeHierarchy newTypeHierarchy(final IType type, final IRegion region, final WorkingCopyOwner owner,
            final IProgressMonitor monitor)
			throws JavaModelException {
        return _eclipseJavaProject.newTypeHierarchy( type, region, owner, monitor );

	}

	@Override
	public IPath readOutputLocation() {
        return _eclipseJavaProject.readOutputLocation();

	}

	@Override
	public IClasspathEntry[] readRawClasspath() {
        return _eclipseJavaProject.readRawClasspath();

	}

	@Override
	public void setOption(final String optionName, final String optionValue) {
        _eclipseJavaProject.setOption( optionName, optionValue );
		
	}

	@Override
	public void setOptions(final Map newOptions) {
        _eclipseJavaProject.setOptions( newOptions );
		
	}

	@Override
	public void setOutputLocation(final IPath path, final IProgressMonitor monitor) throws JavaModelException {
        _eclipseJavaProject.setOutputLocation( path, monitor );
		
	}
	@Override
	public void setRawClasspath(final IClasspathEntry[] entries, final IProgressMonitor monitor) throws JavaModelException {
		// TODO Auto-generated method stub
        _eclipseJavaProject.setRawClasspath( entries, monitor );

	}

	@Override
	public void setRawClasspath(final IClasspathEntry[] entries, final boolean canModifyResources, final IProgressMonitor monitor) throws JavaModelException {
        _eclipseJavaProject.setRawClasspath( entries, canModifyResources, monitor );
		
	}

	@Override
	public void setRawClasspath(final IClasspathEntry[] entries, final IPath outputLocation, final IProgressMonitor monitor) throws JavaModelException {
        _eclipseJavaProject.setRawClasspath( entries, outputLocation, monitor );
		
	}

	@Override
	public void setRawClasspath(final IClasspathEntry[] entries, final IPath outputLocation, final boolean canModifyResources,
            final IProgressMonitor monitor)
			throws JavaModelException {
        _eclipseJavaProject.setRawClasspath( entries, outputLocation, canModifyResources, monitor );
		
	}

	@Override
	public void setRawClasspath(IClasspathEntry[] entries, IClasspathEntry[] referencedEntries, IPath outputLocation,
            IProgressMonitor monitor)
			throws JavaModelException {
		 _eclipseJavaProject.setRawClasspath( entries, referencedEntries, outputLocation, monitor );		
	}

	@Override
	public IJavaElement[] getChildren() throws JavaModelException {
		return _eclipseJavaProject.getChildren();
	}

	@Override
	public boolean hasChildren() throws JavaModelException {
		return _eclipseJavaProject.hasChildren();
	}

	@Override
	public boolean exists() {
		return _eclipseJavaProject.exists();
	}

	@Override
	public IJavaElement getAncestor(final int ancestorType) {
		return _eclipseJavaProject.getAncestor( ancestorType );
	}

	@Override
	public String getAttachedJavadoc(final IProgressMonitor monitor) throws JavaModelException {
        return _eclipseJavaProject.getAttachedJavadoc( monitor );

	}

	@Override
	public IResource getCorrespondingResource() throws JavaModelException {
        return _eclipseJavaProject.getCorrespondingResource();

	}

	@Override
	public String getElementName() {
        return _eclipseJavaProject.getElementName();

	}

	@Override
	public int getElementType() {
        return _eclipseJavaProject.getElementType();

	}

	@Override
	public String getHandleIdentifier() {
        return _eclipseJavaProject.getHandleIdentifier();

	}

	@Override
	public IJavaModel getJavaModel() {
        return _eclipseJavaProject.getJavaModel();

	}

	@Override
	public IJavaProject getJavaProject() {
        return _eclipseJavaProject.getJavaProject();

	}

	@Override
	public IOpenable getOpenable() {
        return _eclipseJavaProject.getOpenable();

	}

	@Override
	public IJavaElement getParent() {
        return _eclipseJavaProject.getParent();

	}

	@Override
	public IPath getPath() {
		return _eclipseJavaProject.getPath();
	}

	@Override
	public IJavaElement getPrimaryElement() {
		return _eclipseJavaProject.getPrimaryElement();
	}

	@Override
	public IResource getResource() {
		return _eclipseJavaProject.getResource();
	}

	@Override
	public ISchedulingRule getSchedulingRule() {
		 return _eclipseJavaProject.getSchedulingRule();
	}

	@Override
	public IResource getUnderlyingResource() throws JavaModelException {
		return _eclipseJavaProject.getUnderlyingResource();
	}

	@Override
	public boolean isReadOnly() {
		return _eclipseJavaProject.isReadOnly();
	}

	@Override
	public boolean isStructureKnown() throws JavaModelException {
		return _eclipseJavaProject.isStructureKnown();
	}

	@Override
	public void close() throws JavaModelException {
		_eclipseJavaProject.close();
		
	}

	@Override
	public String findRecommendedLineSeparator() throws JavaModelException {
		return _eclipseJavaProject.findRecommendedLineSeparator();
	}

	@Override
	public IBuffer getBuffer() throws JavaModelException {
		return _eclipseJavaProject.getBuffer();
	}

	@Override
	public boolean hasUnsavedChanges() throws JavaModelException {
		return _eclipseJavaProject.hasUnsavedChanges();
	}

	@Override
	public boolean isConsistent() throws JavaModelException {
		return _eclipseJavaProject.isConsistent();
	}

	@Override
	public boolean isOpen() {
		return _eclipseJavaProject.isOpen();
	}

	@Override
	public void makeConsistent(final IProgressMonitor progress) throws JavaModelException {
		_eclipseJavaProject.makeConsistent(progress);
		
	}

	@Override
	public void open(final IProgressMonitor progress) throws JavaModelException {
		_eclipseJavaProject.open(progress);
	}

	@Override
	public void save(final IProgressMonitor progress, boolean force) throws JavaModelException {
		_eclipseJavaProject.save(progress, force);
		
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return _eclipseJavaProject.getAdapter(adapter);
	}

	@Override
	public CFSProperties getProperties() {
		return _properties;
	}
}
