#!/usr/bin/env python

##
# Get Organized - Organize your schedule, course assignments, and grades
# Copyright (c) 2012 Alex Laird
# getorganized@alexlaird.com
# alexlaird.com
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
import os, sys

## Scans through all *.java files in Get Organized, finds references to the language
# resource bundle, and ensures all keys are found in the resource bundle. Also scans
# through the resource bundle to ensure there are no duplicate keys.
#
# @author Alex Laird
# @date 02/18/11
#
# @file VerifyResource.py
# @version 1.1

## The relative path to directories
REL_SRC = ".." + os.sep + "src" + os.sep
REL_BUNDLE_DIR = ".." + os.sep + "src" + os.sep + "adl" + os.sep + "go" + os.sep + "resource" + os.sep + "languages" + os.sep

##
# Walk from the top directory into each sub directory, attaining a list of
# files and directories.
#
# @param top The current directory to recurse through.
def walk(top):
    # Retrieve a list of everything in this folder
    names = os.listdir (top)

    dirs, nonDirs = [], []
    for name in names:
        # Store the directory or file in its respective list
        if os.path.isdir (os.path.join(top, name)):
            if name != ".svn" and name != "images":
                dirs.append (name)
        else:
            if name.endswith (".java"):
                nonDirs.append (name)
            
    for name in dirs:
        if name != ".svn" and name != "images":
            path = os.path.join (top, name)
            if not os.path.islink (path):
                # Recursively enter this subdirectory
                for x in walk (path):
                    yield x

    yield top, dirs, nonDirs

##
# Find the given key in the resource bundle.
#
# @param fileObj The current file being scanned.
# @param lineNumber The line number this key is on in the scanned file.
# @param srcDir The source directory.
# @param bundleDir The resource bundle directory.
def findInResource(fileObj, lineNumber, key, bundleDir):
    foundInAll = True
    # Attain the list of bundles
    bundles = os.listdir (bundleDir)
    # Remove unwanted files or directories from the list
    try:
        bundles.remove (".svn")
    except:
        pass
    
    for bundle in bundles:
        # Open a stream to the file
        fileStream = open (os.path.join (bundleDir, bundle), "r")

        found = False
        # Scan through each line of the file looking for a reference to the resource bundle
        for line in fileStream:
            if line.startswith (key):
                found = True
                break

        # Close the file stream
        fileStream.close ()
        
        if not found:
            foundInAll = False
            print ("::MISSING KEY::\nSource file: " + fileObj + "\nLine: " + str (lineNumber) + "\nBundle: " + bundle + "\nKey: " + key + "\n")

    return foundInAll

##
# Scan through the given source file to identify references to the resource bundle,
# and ensure that key is in the resource bundle.
# 
# @param path The path to the current source file being scanned.
# @param fileObj The source file being scanned.
# @param bundleDir The resource bundle directory.
def scanSrcFile(path, fileObj, bundleDir):
    missing = False
    # Open a stream to the file
    fileStream = open (os.path.join (path, fileObj), "r")

    # Scan through each line of the file looking for a reference to the resource bundle
    lineNumber = 0
    notFound = False
    for line in fileStream:
        lineNumber += 1
        key = None
        if line.replace (" ", "").find ("language.getString(") != -1:
            string = "language.getString("
            start = line.replace (" ", "").find (string) + len (string) + 1
            end = line.replace (" ", "").find ("\"", start)
            key = line.replace (" ", "")[start:end]
        elif line.replace (" ", "").find ("getLanguage().getString(") != -1:
            string = "getLanguage().getString("
            start = line.replace (" ", "").find (string) + len (string) + 1
            end = line.replace (" ", "").find ("\"", start)
            key = line.replace (" ", "")[start:end]
        
        if key != None:
            if not findInResource (fileObj, lineNumber, key, bundleDir):
                missing = True

    # Close the file stream
    fileStream.close ()
    
    return missing

##
# Verify the source code.
#
# @param srcDir The source directory.
# @param bundleDir The resource bundle directory.
def verifyGetOrganizedCode(srcDir, bundleDir):
    success = True
    print ("::Verify Get Organized Source::")
    print ("Source root: " + srcDir)
    
    # Iterates through all source files
    for root, dirs, files in walk (srcDir):
        for fileObj in files:
            missing = scanSrcFile (root, fileObj, bundleDir)
            if missing:
                success = False
    
    return success

##
# Scan the bundle from the given line number and lower to find the given key.
#
# @param bundleDir The resource bundle directory.
# @param bundle The current resource bundle.
# @param key The key to be found in the bundle.
# @param lineNumber The line number to start searching at.
def findKeyInBundle(bundleDir, bundle, key, lowestLine):
    fileStream = open (os.path.join (bundleDir, bundle), "r")
    
    lineNumber = 0
    for line in fileStream:
        lineNumber += 1
        if lineNumber > lowestLine and line.find ("=") != -1:
            foundKey = line.split ("=")[0]
            if key == foundKey:
                return True
    
    fileStream.close ();
    
    return False

##
# Verify the resource bundle has no duplicate keys.
#
# @param bundleDir The resource bundle directory.
def verifyResourceBundle(bundleDir):
    success = True
    print ("::Verify Resource Bundle::")
    print ("Bundle root: " + bundleDir)
    
    # Attain the list of bundles
    bundles = os.listdir (bundleDir)
    # Remove unwanted files or directories from the list
    try:
        bundles.remove (".svn")
    except:
        pass
    
    print ("Bundles found: " + str (bundles))
    
    for bundle in bundles:
        # Open a stream to the file
        fileStream = open (os.path.join (bundleDir, bundle), "r")

        # Scan through each line of the file looking for a reference to the resource bundle
        lineNumber = 0
        for line in fileStream:
            lineNumber += 1
            if line.find ("=") != -1:
                key = line.split ("=")[0]
                found = findKeyInBundle (bundleDir, bundle, key, lineNumber)
                if found:
                    success = False
                    print ("::DUPLICATE KEY::\nBundle: " + bundle + "\nKey: " + key + "\n")

        # Close the file stream
        fileStream.close ()
    
    return success

## Calls respective helper methods to complete overall task of ensuring the
# resource bundle for Get Organized is valid.
#
# @param args The command-line arguments.
def main(args):
    # get the absolute path of the Get Organized directory directory
    getOrganizedDir = os.path.abspath(args[0])[:os.path.abspath(args[0]).rfind(os.sep)] + os.sep
    srcDir = getOrganizedDir + REL_SRC
    bundleDir = getOrganizedDir + REL_BUNDLE_DIR
    if os.path.exists (getOrganizedDir) and os.path.exists (srcDir) and os.path.exists (bundleDir):
        exitCode = 0
        
        # Verify all Get Organized resource bundle calls against the resource bundle keys available
        success = verifyGetOrganizedCode (srcDir, bundleDir)
        if success:
            print ("::NO MISSING RESOURCES::")
        else:
            exitCode = 1
            
        print ("")
        
        # Verify the resouce bundle to ensure no duplicate entries
        success = verifyResourceBundle (bundleDir)
        if success:
            print ("::NO DUPLICATE KEYS::")
        else:
            exitCode = 1

        print ("")
    else:
        print ("The directory structure on your system does not match that required by this script.")
        
    return exitCode

## This is the entry point for the program, which figures off the main method to
# perform operations.
if __name__ == "__main__":
    sys.exit(main(sys.argv))
    
