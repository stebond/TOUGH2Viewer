# TOUGH2Viewer 2.0

# 1. Introduction
TOUGH2Viewer is a pre and post-processing program dedicated for TOUGH (Pruess, 1999) users who want to edit grid, view and explore the results of numerical simulations in a fast, simple and intuitive mode. It is developed in Java ™, which provides portability between operating systems.
TOUGH2Viewer allows to display grids, locally refined or less, structured (made with the MESHMAKER module of TOUGH2), unstructured Voronoi 2.5D (made with a version of AMESH modified by the authors) and fully unstructured 3D Voronoi grids created with VORO2MESH.
Related publications can be found in Bonduà et al. (2012, 2017).

# 2. License
Apache License, Version 2.0 

# 3. System requirements
The minimum system requirements are:
•	1GB RAM;
•	100 MB of free hard disk space;
•	Graphics card of 512 Mb; 
•	It is independent of the operating system

# 4. Installation, execution and uninstallation
The program requires the Java ™ Runtime Environment (JRE) 1.6.14 version or higher installed on the operating system (Windows, Linux or Solaris). After installing JRE it is necessary to install Java libraries 3D ™ 1.5.1 or later. For installing Java ™, it is suggested to refer to the official website of the Oracle Web site.

# 4.1. Installation Notes of Java 3D 64-bit version in Windows
These instructions are for the 64-bit version of Java on Windows 7, 8 or 10 (64 bit version).
The Java 3D installation package may not properly install libraries on operating systems with Windows 7 or higher versions. If the user, after installing Java 3D receives an error message during TOUGH2Viewer execution, it means that Java3D is not installed properly.
The following instructions allow you to correct the error
1.	Open Windows Explorer and navigate to the folder:
"C:\Program Files\Java\Java3D\1.5.X\bin\”, where X is the version that you have installed.
2.	In another window, open the folder "C:\Program Files\Java\jre6\bin\”;
3.	Copy and paste the file j3dcore-ogl.dll from "C:\Program Files\Java\Java3D\1.5.X\bin\" to "C:\Program Files\Java\jre6\bin\";
4.	Navigate to the folder "C:\Program Files\Java\Java3D\1.5.X\lib\ext" and "C:\Program Files\Java\jre6\lib\ext";
5.	Copy and paste the file file j3dcode.jar, j3dutils.jar and vecmath.jar from C:\Program files\Java\Java3D\1.5.X\lib\ext" to "C:\Program Files\Java\jre6\lib\ext".
# 4.2. TOUGH2Viewer binary installation
To install TOUGH2Viewer copy the user's chosen folder in the hard disk directory containing the compressed program (e.g. TOUGH2Viewer_v.X.zip where X indicates the version).
The operating system and system logs are not modified by the installation.
Once the copy finished, you must unzip the directory. In the QuickTutorial book, the files are copied in the directory “d:\T2Viewer\” (Windows Operative System).
The program consists of two sub-compressed, dist (distribution) and ExampleData folders.
# 4.3. TOUGH2Viewer execution in Windows
To run the program, use the following command line (dos):

d:\T2Viewer\dist\>java -Xmx512M -jar Tough2Viewer.jar

Or double click on the file icon go.bat present in the folder dist.
Both options require to the JRE to allocate 512 MB of RAM.
To reserve more memory to the execution of TOUGH2Viewer, and to allow viewing of models with greater number of blocks, it is possible to change the number 512 (Mega Byte) to 4096 or 8192 etc (Windows, 64 bit. The 32 bit version is limited to 2048 MB), as long as the machine has enough space for the operating system. It is advised not to allocate more than half of the physical memory of the machine. For operating systems UBUNTU (versions 12 and later, the limit order of TB, as long as the machine has enough RAM).
# 4.4 Cloning, modify and Run with NetBeans
install Java3D on you OS. Clone the repository with NetBeans:
1. team->Git->Clone...
2. enter the specified path: https://github.com/stebond/TOUGH2Viewer.git
3. open the project
4. click on Run->Run project (TOUGH2Viewer). Note that by running TOUGH2Viewer inside the NetBeans platform, the RAM reservation has been set to 2048 MB. This property can be changend in Run->Set Project Configuration->Customize... . In the Project properties dialog box select Run-> VM options and modify the default value "-2048M" to a desidered value.

# References
Bonduá S, Berry P, Bortolotti V, et al. TOUGH2Viewer: A post-processing tool for interactive 3D visualization of locally refined unstructured grids for TOUGH2. Comput Geosci 2012;46. doi:10.1016/j.cageo.2012.04.008

Bonduà S, Battistelli A, Berry P, et al. 3D Voronoi grid dedicated software for modeling gas migration in deep layered sedimentary formations with TOUGH2-TMGAS. Comput Geosci 2017;108:50–5. doi:https://doi.org/10.1016/j.cageo.2017.03.008





