Get Organized
=============

This is the source code for the Get Organized project, a digital student planner.

This repository contains three NetBeans projects (App, JCalendar_GO, and Updater). The App project contains Get Organized, which has the following specialized Ant targets available:
* package-for-store
* verify-bundles
* launch4jtarget
* create-installer

**Building Get Organized**

Once you have downloaded a copy of the Get Organized source code, unzip the project. You can open the project using NetBeans. To ensure compatibility, Get Organized can reliably be built on Windows XP, Vista, or 7 using JDK 6. You can download a bundle of NetBeans and JDK 6 by clicking here.

Necessary dependencies are included in the Get Organized source zip, and build paths are relative, so simply opening the unzipped project with NetBeans should allow for an immediate build.

**Building Windows Installer**

To build the Get Organized Windows installer, do the following from a Windows installation of NetBeans: right-click on build.xml in the File viewer and select Run Target->Other Targets->create-installer.

**Building OS X Package**

To build the Get Organized OS X package, do the following from a OS X installation of NetBeans: right-click on build.xml in the File viewer and select Run Target->Other Targets->create-installer.

**Building Portable Zip**

When you run create-installer on Windows to build the Windows installer, the Get Organized Portable zip file will also be generated.
