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
import os, sys, datetime

## Retrieves the current version number from Domain.java, updates any other
# files that need to be aware of the latest version number, and returns the
# version number to the caller of this script.
#
# @author Alex Laird
# @date 03/27/11
#
# @file ValidateVersion.py
# @version 1.1

## The relative path to the directories
REL_SRC = ".." + os.sep + "src" + os.sep
REL_INSTALLER = ".." + os.sep + "installer" + os.sep

# The array of month names
MONTHS = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]

##
# Retrieve the version number from the file containing versioning information.
#
# @param srcFile The file containing the version information.
# @param silent True if output should not be displayed, False otherwise.
def getVersionNumber(srcFile, silent):
    # Open a stream to the file
    fileStream = open (srcFile, "r")
    verNum = ""
    
    for line in fileStream:
        if line.find ("final String VERSION =") != -1:
            section = line.split("=")[1]
            verNum = section[2:len(section) - 3]
    if not silent:
        print ("Retrieved version from " + srcFile + ": " + verNum)

    # Close the file stream
    fileStream.close ()
    
    return verNum

##
# Update the version number in launcher.xml.
#
# @param launcherSrc The launcher.xml file.
# @param verNum The latest version number.
# @param silent True if output should not be displayed, False otherwise.
def updateLauncher(launcherSrc, verNum, silent):
    # Open a stream to the file
    fileStream = open (launcherSrc, "r")
    updatedFileStream = open (launcherSrc + "temp", "w")
    
    changeMade = False
    for line in fileStream:
        newLine = line
        updatedFileStream.write(newLine)

    # Close the file stream and rename updated file
    fileStream.close ()
    updatedFileStream.close ()
    if changeMade:
        os.remove(launcherSrc)
        os.rename(launcherSrc + "temp", launcherSrc)
    else:
        os.remove(launcherSrc + "temp")
    
    return

##
# Update the version number in the .iss file.
#
# @param windowsSrc The .iss file.
# @param verNum The latest version number.
# @param silent True if output should not be displayed, False otherwise.
def updateWindows(windowsSrc, verNum, silent):
    # Open a stream to the file
    fileStream = open (windowsSrc, "r")
    updatedFileStream = open (windowsSrc + "temp", "w")
    
    changeMade = False
    for line in fileStream:
        newLine = line
        if line.find ("AppVersion=") != -1:
            oldVer = line.split("=")[1]
            oldVer = oldVer[:len(oldVer) - 1]
            if oldVer != verNum:
                if not silent:
                    print ("Updated Windows setup script AppVersion from " + oldVer + " to " + verNum)
                changeMade = True
                newLine = line.replace(oldVer, verNum)
        elif line.find ("AppVerName=") != -1:
            value = line.split("=")[1].split(" ")
            oldVer = value[2]
            i = 3
            while i < len(value):
               oldVer += (" " + value[i])
               i += 1
            oldVer = oldVer[:len(oldVer) - 1]
            if oldVer != verNum:
                if not silent:
                    print ("Updated Windows setup script AppVerName from " + oldVer + " to " + verNum)
                changeMade = True
                newLine = line.replace(oldVer, verNum)
            
        updatedFileStream.write(newLine)

    # Close the file stream and rename updated file
    fileStream.close ()
    updatedFileStream.close ()
    if changeMade:
        os.remove(windowsSrc)
        os.rename(windowsSrc + "temp", windowsSrc)
    else:
        os.remove(windowsSrc + "temp")
    
    return

##
# Update the version number in OSX.packproj.
#
# @param osxSrc The OSX.packproj file.
# @param verNum The latest version number.
# @param silent True if output should not be displayed, False otherwise.
def updateOSX(osxSrc, verNum, silent):
    # Open a stream to the file
    fileStream = open (osxSrc, "r")
    updatedFileStream = open (osxSrc + "temp", "w")
    
    changeMade = False
    nextIFP = False
    nextID = False
    nextShortVer = False
    for line in fileStream:
        newLine = line
        if line.find ("IFPkgDescriptionVersion") != -1:
            nextIFP = True
            updatedFileStream.write(newLine)
            continue
        elif line.find("CFBundleIdentifier") != -1:
            nextID = True
            updatedFileStream.write(newLine)
            continue
        elif line.find("CFBundleShortVersionString") != -1:
            nextShortVer = True
            updatedFileStream.write(newLine)
            continue
        
        if nextIFP:
            nextIFP = False
            oldVer = line.strip()[8:len(line.strip()) - 9]
            if oldVer != verNum:
                if not silent:
                    print ("Updated OS X setup script IFPkgDescriptionVersion from " + oldVer + " to " + verNum)
                changeMade = True
                newLine = line.replace(oldVer, verNum)
        elif nextID:
            nextID = False
            oldVer = line.strip()[15:len(line.strip()) - 9].replace (" ", "")
            if oldVer.replace (" ", "") != verNum.replace (" ", ""):
                if not silent:
                    print ("Updated OS X setup script CFBundleIdentifier from " + oldVer + " to " + verNum.replace (" ", ""))
                changeMade = True
                newLine = line.replace(oldVer, verNum.replace(" ", ""))
        elif nextShortVer:
            nextShortVer = False
            oldVer = line.strip()[8:len(line.strip()) - 9]
            if oldVer != verNum:
                if not silent:
                    print ("Updated OS X setup script CFBundleShortVersionString from " + oldVer + " to " + verNum)
                changeMade = True
                newLine = line.replace(oldVer, verNum)
            
        updatedFileStream.write(newLine)

    # Close the file stream and rename updated file
    fileStream.close ()
    updatedFileStream.close ()
    if changeMade:
        os.remove(osxSrc)
        os.rename(osxSrc + "temp", osxSrc)
    else:
        os.remove(osxSrc + "temp")
    
    return

##
# Update the version number in releasenotes.html.
#
# @param releaseNotesSrc The releasenotes.html file.
# @param verNum The latest version number.
# @param silent True if output should not be displayed, False otherwise.
def updateReleaseNotes(releaseNotesSrc, verNum, silent):
    # Open a stream to the file
    fileStream = open (releaseNotesSrc, "r")
    updatedFileStream = open (releaseNotesSrc + "temp", "w")
    
    changeMade = False
    for line in fileStream:
        newLine = line
        if line.find ("<title>") != -1 and line.find ("Get Organized") != -1:
            oldVer = line.strip()[21:len(line.strip()) - 24]
            if oldVer != verNum:
                if not silent:
                    print ("Updated release notes version from " + oldVer + " to " + verNum)
                changeMade = True
                newLine = line.replace(oldVer, verNum)
        elif line.find ("'font-size:14pt;'>Get Organized") != -1:
            dashIndex = line.strip().rfind("-")
            oldVer = line.strip()[50:dashIndex - 1]
            newLine = line.replace(oldVer, verNum)
            # Replace the old date with the current date
            dashIndex = newLine.rfind("-") + 2
            endIndex = newLine.rfind("</span>")
            oldDate = newLine[dashIndex:endIndex]
            now = datetime.datetime.now()
            newDate = MONTHS[now.month - 1] + " " + str(now.day) + ", " + str(now.year)
            if oldDate != newDate:
                if not silent:
                    print ("Updated release notes date from " + oldDate + " to " + newDate)
                changeMade = True
                newLine = newLine.replace(oldDate, newDate)
            
        updatedFileStream.write(newLine)

    # Close the file stream and rename updated file
    fileStream.close ()
    updatedFileStream.close ()
    if changeMade:
        os.remove(releaseNotesSrc)
        os.rename(releaseNotesSrc + "temp", releaseNotesSrc)
    else:
        os.remove(releaseNotesSrc + "temp")
    
    return

## Calls respective helper methods to complete overall task of ensuring the
# version numbers are validate where needed.
#
# @param args The command-line arguments.
def main(args):
    silent = 0
    if len(args) < 2:
        print ("No arguments found.\nPass this script a 0 to update all version numbers and return the ones digit.\nPass this script a 1 to simply return the tenths digit of the version number.\nPass this script a 2 to simply return the hundreths digit of the version number.")
        return
    elif len(args) == 3:
        if args[2].lower() == "true" or args[2] == "1":
            silent = True
        else:
            silent = False
    
    # Get the absolute path of the Get Organized directory directory
    getOrganizedDir = os.path.abspath(args[0])[:os.path.abspath(args[0]).rfind(os.sep)] + os.sep
    # Setup paths to source files
    domainSrc = getOrganizedDir + REL_SRC + "adl" + os.sep + "go" + os.sep + "gui" + os.sep + "Domain.java"
    launcherSrc = getOrganizedDir + REL_INSTALLER + "launcher.xml"
    windowsSrc = getOrganizedDir + REL_INSTALLER + "Windows.iss"
    osxSrc = getOrganizedDir + REL_INSTALLER + "OSX.packproj"
    releaseNotesSrc = getOrganizedDir + REL_INSTALLER + "releasenotes.html"
    # Ensure all source files actually exist
    if os.path.exists (domainSrc) and os.path.exists (launcherSrc) and os.path.exists (windowsSrc) and os.path.exists (osxSrc) and os.path.exists (releaseNotesSrc):
        if args[1] == "0":
            if not silent:
                print ("::VALIDATE VERSION::")
            verNum = getVersionNumber(domainSrc, silent)
            updateLauncher(launcherSrc, verNum, silent)
            updateWindows(windowsSrc, verNum, silent)
            updateOSX(osxSrc, verNum, silent)
            updateReleaseNotes(releaseNotesSrc, verNum, silent)
        elif args[1] == "1":
            verNum = getVersionNumber(domainSrc, silent)
            return verNum[:verNum.find(".")]
        elif args[1] == "2":
            verNum = getVersionNumber(domainSrc, silent)
            return verNum[verNum.find(".") + 1:verNum.find(".") + 2]
        elif args[1] == "3":
            verNum = getVersionNumber(domainSrc, silent)
            return verNum[verNum.find(".") + 2:]
        else:
            print ("The argument specified is not a valid selection.")
    else:
        print ("The directory structure on your system does not match that required by this script.")

## This is the entry point for the program, which figures off the main method to
# perform operations.
if __name__ == "__main__":
    sys.exit(main(sys.argv))
    
