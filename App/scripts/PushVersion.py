#!/usr/bin/env python

##
# Get Organized - Organize your schedule, course assignments, and grades
# Copyright (c) 2012 Alex Laird
# getorganized@alexlaird.name
# alexlaird.name
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

# Import system modules
import ftplib, getpass, os, platform, sys

# Import my modules
import ValidateVersion

## Retrieves login credentials from a secure location (off administrator's
# Dropbox) and uploads the latest installers and executables to the
# server, updating version.txt to the latest version as well.
#
# @author Alex Laird
# @date 04/13/11
#
# @file PushVersion.py
# @version 1.1

## The relative path to directories
REL_SRC = ".." + os.sep + "src" + os.sep
REL_STORE = ".." + os.sep + "store" + os.sep

## Push the latest installer to the server.
#
def server(verNum, getOrganizedDir, windowsPortable, jarPortable, windowsSetupFile, osxSetupFile, zipFile, releaseNotes, license):
    # Establish a connection to the FTP server
    print ("\nConnecting to Get Organized server...")
    ftp = ftplib.FTP("ftp.alexlaird.name")
    print ("Connection established")
    username = raw_input("Username: ")
    password = getpass.getpass()
    validCredentials = True
    try:
        print ("Logging in to Get Organized server...")
        ftp.login(username, password)
        print ("...Login succeeded\n")
    except:
        validCredentials = False
        print ("Invalid username or password\n")
    
    if validCredentials:
        try:
            ftp.cwd("/home/adlaird/domains/updates.alexlaird.name/public_html/get-organized")
            print ("Pulling down current version.txt")
            versionOut = open(getOrganizedDir + "version.txt", "wb")
            ftp.retrbinary("RETR version.txt", versionOut.write)
            versionOut.close()
            fileStream = open(getOrganizedDir + "version.txt", "r")
            serverVer = None
            for line in fileStream:
                if "version" in line:
                    serverVer = line.split("=")[1].strip().strip("\n")
            fileStream.close()
            os.remove(getOrganizedDir + "version.txt")
            
            if not serverVer == None:
                answer = None
                if serverVer < verNum:
                    answer = raw_input ("\nThe version on the server is " + serverVer + ", which is older than the version you are trying to push. Would you like to upload the latest version? ")
                elif serverVer == verNum:
                    answer = raw_input ("\nThe version on the server is " + serverVer + ", which is the same as the version you are trying to upload. Are you sure you want to continue pushing this update? ")
                else:
                    answer = raw_input ("\nThe version on the server is " + serverVer + ", which is newer than the version you are trying to upload. It is not recommended that you continue. Would you like to force an update anyway? ")
                
                # If the user doesn't want to continue stop the upload
                if "yes" not in answer.lower() and answer.lower() != "y":
                    return
                print ("")
                
            print ("")
            
            print ("::UPLOADS BEGINNING FOR v" + str(verNum) + "::")
            print ("::DO NOT TERMINATE UNTIL COMPLETE::")
            
            print ("Uploading Windows installation (" + str(round(float(os.path.getsize(windowsSetupFile)) / float(1048576), 1)) + "MB)...")
            ftp.storbinary("STOR GetOrganizedSetup.exe", open(windowsSetupFile, "rb"))
            
            print ("Uploading OS X package (" + str(round(float(os.path.getsize(osxSetupFile)) / float(1048576), 1)) + "MB)...")
            ftp.storbinary("STOR GetOrganizedSetup.pkg.zip", open(osxSetupFile, "rb"))
            
            print ("Uploading Portable archive (" + str(round(float(os.path.getsize(zipFile)) / float(1048576), 1)) + "MB)...")
            ftp.storbinary("STOR GetOrganized.zip", open(zipFile, "rb"))
            
            print ("Uploading Portable Windows update (" + str(round(float(os.path.getsize(windowsPortable)) / float(1048576), 1)) + "MB)...")
            ftp.storbinary("STOR GetOrganizedPortable.exe", open(windowsPortable, "rb"))
            print ("Uploading Portable JAR update (" + str(round(float(os.path.getsize(jarPortable)) / float(1048576), 1)) + "MB)...")
            ftp.storbinary("STOR GetOrganizedPortable.jar", open(jarPortable, "rb"))
            
            print ("Uploading Portable release notes")
            ftp.storbinary("STOR releasenotes.html", open(releaseNotes, "rb"))
            print ("Uploading Portable license")
            ftp.storbinary("STOR license.html", open(license, "rb"))
            
            fileStream = open (getOrganizedDir + "version.txt", "w")
            print ("Writing new version.txt")
            fileStream.write("version=" + verNum + "\n")
            fileStream.write("portable=" + str(os.path.getsize(windowsPortable)) + "\n")
            fileStream.write("win=" + str(os.path.getsize(windowsSetupFile)) + "\n")
            fileStream.write("mac=" + str(os.path.getsize(osxSetupFile)) + "\n")
            fileStream.close()
            print ("Updating version.txt to v" + verNum +  "...")
            ftp.storbinary("STOR version.txt", open(getOrganizedDir + "version.txt", "rb"))
            os.remove(getOrganizedDir + "version.txt")
            
            print ("::UPLOADS COMPLETE::")
            
            print ("\nClosing FTP connection\n")
            ftp.quit()
            
            print ("Get Organized was successfully updated to v" + verNum + " on the Get Organized server")
        except:
            print ("An unknown error occured. Try running the push again. If this error persists, it is recommended that you ensure the validity of all version files and installers on the Get Organized server immediately.")

## Calls respective helper methods to complete overall task of ensuring the
# version numbers are validate where needed.
#
# @param args The command-line arguments.
def main(args):
    getOrganizedDir = os.path.abspath(args[0])[:os.path.abspath(args[0]).rfind(os.sep)] + os.sep
    domainSrc = getOrganizedDir + REL_SRC + "adl" + os.sep + "go" + os.sep + "gui" + os.sep + "Domain.java"
    storeDir = getOrganizedDir + REL_STORE
    windowsPortable = storeDir + "Get Organized.exe"
    jarPortable = storeDir + "Get Organized.jar"
    
    releaseNotes = storeDir + ".." + os.sep + "installer" + os.sep + "releasenotes.html"
    license = storeDir + ".." + os.sep + "installer" + os.sep + "license.html"

    finalDistDir = storeDir + ".." + os.sep + ".." + os.sep + ".." + os.sep + ".." + os.sep + ".." + os.sep + ".." + os.sep + ".." + os.sep + "Get Organized" + os.sep
	
    windowsSetupFile = finalDistDir + "Installer" + os.sep + "GetOrganizedSetup.exe"
    osxSetupFile = finalDistDir + "Installer" + os.sep + "GetOrganizedSetup.pkg.zip"
    zipFile = finalDistDir + "Installer" + os.sep + "GetOrganized.zip"
    
    if os.path.exists(finalDistDir) and os.path.exists(storeDir) and os.path.exists(windowsPortable) and os.path.exists(jarPortable) and os.path.exists(windowsSetupFile) and os.path.exists(osxSetupFile) and os.path.exists(zipFile) and os.path.exists(releaseNotes) and os.path.exists(license):
        print ("::ALL NECESSARY FILES IN PLACE::\n")
        print ("Ensure that setup files in place are the LATEST build--this script will not do a build for you, it will only upload the files that already exist.")
        # Grab the current version number we need to push to the server
        verNum = ValidateVersion.getVersionNumber(domainSrc, False)
        
        answer = raw_input ("\nWould you like to update the files and installers on the Get Organized server? ")
        if "yes" in answer.lower() or answer.lower() == "y":
            server(verNum, getOrganizedDir, windowsPortable, jarPortable, windowsSetupFile, osxSetupFile, zipFile, releaseNotes, license)
            
        print ("")
    else:
        print ("The directory structure on your system does not match that required by this script.\nEnsure Get Organized has been built to the latest version and all portable and installation files are in place.")
        print ("")
    
## This is the entry point for the program, which figures off the main method to
# perform operations.
if __name__ == "__main__":
    sys.exit(main(sys.argv))
    
