NOTE NOTE com.windhoverlabs.cfside is the more aesthetically pleasing project.


GNU/Linux 16.04 Xubuntu/Ubuntu x86_64
Kernel : 4.15.0
Java : 11.0.2
Java SE Runtime Envirtonment : 18.9


1) Update System.
    sudo apt-get update
    sudo apt-get upgrade

1.5) Install Java 11.

2) Create development environment.
    ex: mkdir ~/development/eclipse0906

3) Install Eclipse CDT.
    Navigate to development environment.
    Download latest Eclipse CDT tar.
        ex: wget http://eclipse.mirror.rafal.ca/technology/epp/downloads/release/2019-06/R/eclipse-cpp-2019-06-R-linux-gtk-x86_64.tar.gz
    Untar
        ex: tar -xvf eclipse-cpp-2019-06-R-linux-gtk-x86_64.tar.gz eclipse/

    Open eclipse by ~/development/eclipse0906/eclipse/eclipse
    Optional : create symlink, create launcher.

4) Update Eclipse.
    Help --> Check For Updates.
    Install Updates then Restart.

---------------------------------------------------------------

To install as standalone plugin :
    1) Clone plugin from github.
        git clone https://github.com/Z-WHL/whlplugin.git

    2) Move plugin to developmental eclipse plugins folder.
        ex: mv ~/development/whlplugin/whlide/com.windhoverlabs.e4.rcp_1.0.0.201909031230.jar ~/development/eclipse0906/eclipse/plugins

        TODO : Use Software sites for installing plugin.

---------------------------------------------------------------

To develop as plug-in project :
    Prerequisite : Install RCP components.
        Help --> Install New Software...
        0.1) PDE Tools (Plugin Development Environment)
            Work with : --All Available Sites--
            Type filter text : pde
            Check Eclipse PDE Plug-in Developer Resources.
            Install.
        
        0.2) e4 Tools
            Add...
            Location : http://download.eclipse.org/releases/latest
            Work with : http://download.eclipse.org/releases/latest
            Type filter text : e4
            Check Eclipse e4 Tools Developer Resources.
            Install.
        
        0.3) e4 spies
            Add...
            Location : http://download.eclipse.org/e4/snapshots/org.eclipse.e4.tools/latest/
            Work with : http://download.eclipse.org/e4/snapshots/org.eclipse.e4.tools/latest/
            Check Eclipse 4 - All spies.
            Install.
       
Import Project :
    1) Clone plugin project from github.
        git clone https://github.com/Z-WHL/whlide.git

    2) File --> Open Project From File System.
        Click Directory and choose where you saved the above repo.
        Choose com.windhoverlabs.e4.rcp.

Test Plugin :
    1) Open plugin.xml under project.
    
    2) Click Overview.

    3) Under Testing Click Launch an Eclipse application.

Build Plugin :
    1) Open plugin.xml under project.

    2) Click Overview.

    3) Under Exporting
        Optional : Click Organize Manifests Wizard.
        Click Externalize Strings Wizard.
        Click Build Configuration.
            Select all except .classpath, .gitignore, .project.
    
    4) Click Overview
        Under Exporting click Export Wizard.
        Under Destination choose Directory.
        Browse and choose the whlplugin git repo or anywhere else.
        Optional : Push whlplugin git repo

Required Plugins : 
NatTable
    Help --> Install New Software...
    Add site  http://download.eclipse.org/nattable/releases/1.5.0/repository/ (1.5 or newest)

Gson jar 
https://repo1.maven.org/maven2/com/google/code/gson/gson/2.8.5/
Download and add jar to build path.
Right click project --> build path --> configure --> add library --> choose jar