# TOUGH2Viewer

# 1. Introduction
TOUGH2Viewer is a pre and post-processing program dedicated for TOUGH (Pruess, 1999) users who want to edit grid, view and explore the results of numerical simulations in a fast, simple and intuitive mode. It is developed in Java ™, which provides portability between operating systems.
TOUGH2Viewer allows to display grids, locally refined or less, structured (made with the MESHMAKER module of TOUGH2), unstructured Voronoi 2.5D (made with a version of AMESH modified by the authors) and fully unstructured 3D Voronoi grids created with VORO2MESH.
Related publications can be found in Bonduà et al. (2012, 2017).

# 2. System requirements
The minimum system requirements are:
•	1GB RAM;
•	100 MB of free hard disk space;
•	Graphics card of 512 Mb; 
•	It is independent of the operating system

# 3.	Installation, execution and uninstallation
The program requires the Java ™ Runtime Environment (JRE) 1.6.14 version or higher installed on the operating system (Windows, Linux or Solaris). After installing JRE it is necessary to install Java libraries 3D ™ 1.5.1 or later. For installing Java ™, it is suggested to refer to the official website of the Oracle Web site.

# 3.1.	Installation Notes of Java 3D 64-bit version in Windows
These instructions are for the 64-bit version of Java on Windows 7, 8 or 10 (64 bit version).
The Java 3D installation package may not properly install libraries on operating systems with Windows 7 or higher versions. If the user, after installing Java 3D receives an error message during TOUGH2Viewer execution, it means that Java3D is not installed properly.
The following instructions allow you to correct the error
1.	Open Windows Explorer and navigate to the folder:
"C:\Program Files\Java\Java3D\1.5.X\bin\”, where X is the version that you have installed.
2.	In another window, open the folder "C:\Program Files\Java\jre6\bin\”;
3.	Copy and paste the file j3dcore-ogl.dll from "C:\Program Files\Java\Java3D\1.5.X\bin\" to "C:\Program Files\Java\jre6\bin\";
4.	Navigate to the folder "C:\Program Files\Java\Java3D\1.5.X\lib\ext" and "C:\Program Files\Java\jre6\lib\ext";
5.	Copy and paste the file file j3dcode.jar, j3dutils.jar and vecmath.jar from C:\Program files\Java\Java3D\1.5.X\lib\ext" to "C:\Program Files\Java\jre6\lib\ext".



