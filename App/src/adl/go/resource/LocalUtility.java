/*
 * Get Organized - Organize your schedule, course assignments, and grades
 * Copyright © 2012 Alex Laird
 * getorganized@alexlaird.com
 * alexlaird.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package adl.go.resource;

import adl.go.gui.Domain;
import adl.go.gui.ViewPanel;
import adl.go.types.Assignment;
import adl.go.types.AssignmentType;
import adl.go.types.Category;
import adl.go.types.Course;
import adl.go.types.Event;
import adl.go.types.EventYear;
import adl.go.types.Instructor;
import adl.go.types.ListItem;
import adl.go.types.Term;
import adl.go.types.Textbook;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 * This utility class contains generic methods that interact with the offline
 * version of the application, most of which pertain to file operations.
 *
 * @author Alex Laird
 */
public class LocalUtility extends Utility
{
    /**
     * The top-level data folder.
     */
    private File dataFolder;
    /**
     * The object which is instantiated to read from a file.
     */
    private BufferedReader in;
    /**
     * The object which is instantiated to write to a file.
     */
    private BufferedWriter out;
    /**
     * The old application data folder.
     */
    private File oldAppDataFolder;
    /**
     * The user details data file.
     */
    private File userDetailsFile;
    private File oldUserDetailsFile;
    /**
     * The preferences data file.
     */
    private File preferencesFile;
    private File oldPreferencesFile;
    /**
     * The terms data file.
     */
    private File termsFile;
    private File oldTermsFile;
    /**
     * The courses data file.
     */
    private File coursesFile;
    private File oldCoursesFile;
    /**
     * The types data file.
     */
    private File typesFile;
    private File oldTypesFile;
    /**
     * The types data file.
     */
    private File instructorsFile;
    private File oldInstructorsFile;
    /**
     * The textbooks data file.
     */
    private File textbooksFile;
    private File oldTextbooksFile;

    /**
     * Construct the local utility.
     */
    public LocalUtility()
    {
        final String applicationData = System.getenv ("APPDATA");
        if (applicationData != null && !LocalUtility.class.getProtectionDomain ().getCodeSource ().getLocation ().toString ().replaceAll ("%20", " ").toLowerCase ().replaceAll (" ", "").contains ("getorganizedportable"))
        {
            oldAppDataFolder = new File (applicationData, "." + Domain.NAME + '/');
        }
    }

    /**
     * Construct utility data.
     *
     * @param viewPanel A reference to the view panel.
     */
    public void constructLocalUtility(ViewPanel viewPanel)
    {
        constructUtility (viewPanel);

        // create the application data folder directly in the folder with the app if it's portable
        if (Domain.SOURCE_PATH.toLowerCase ().replaceAll (" ", "").contains ("getorganizedportable"))
        {
            String path = Domain.SOURCE_PATH.substring (Domain.SOURCE_PATH.indexOf (":") + 2, Domain.SOURCE_PATH.lastIndexOf ("/"));
            if (!Domain.OS_NAME.toLowerCase ().contains ("windows"))
            {
                path = "/" + path;
            }
            dataFolder = new File (path, "." + "lib/");
        }
        // create the application data folder in the user's HOME directory
        else
        {
            if (Domain.OS_NAME.toLowerCase ().contains ("linux")
                || Domain.OS_NAME.toLowerCase ().contains ("solaris"))
            {
                dataFolder = new File (Domain.HOME, "." + Domain.NAME + "/");
            }
            else if (Domain.OS_NAME.toLowerCase ().contains ("windows"))
            {
                dataFolder = new File (Domain.HOME, "." + Domain.NAME + "/");
            }
            else if (Domain.OS_NAME.toLowerCase ().contains ("mac"))
            {
                dataFolder = new File (Domain.HOME, "Library/Application Support/" + Domain.NAME + "/");
            }
            else
            {
                dataFolder = new File (System.getProperty ("user.dir"), "." + "lib/");
            }
        }

        // point to the data files within the data folder
        userDetailsFile = new File (dataFolder, "user.dat");
        preferencesFile = new File (dataFolder, "preferences.dat");
        termsFile = new File (dataFolder, "terms.dat");
        coursesFile = new File (dataFolder, "courses.dat");
        typesFile = new File (dataFolder, "types.dat");
        instructorsFile = new File (dataFolder, "instructors.dat");
        textbooksFile = new File (dataFolder, "textbooks.dat");

        // point to old data files that may still exist
        if (oldAppDataFolder != null)
        {
            oldUserDetailsFile = new File (oldAppDataFolder, "user.dat");
            oldPreferencesFile = new File (oldAppDataFolder, "preferences.dat");
            oldTermsFile = new File (oldAppDataFolder, "terms.dat");
            oldCoursesFile = new File (oldAppDataFolder, "courses.dat");
            oldTypesFile = new File (oldAppDataFolder, "types.dat");
            oldInstructorsFile = new File (oldAppDataFolder, "instructors.dat");
            oldTextbooksFile = new File (oldAppDataFolder, "textbooks.dat");
        }
    }

    /**
     * Retrieves the data folder.
     *
     * @return The file that is the data folder.
     */
    public File getDataFolder()
    {
        return dataFolder;
    }

    /**
     * Retrieves the old application data folder.
     *
     * @return The file that is the old app data folder.
     */
    public File getAppDataFolder()
    {
        return oldAppDataFolder;
    }

    /**
     * Retrieve the index for the given item in the course list.
     *
     * @param terms The list of terms to search.
     * @param id The ID of the term to look for.
     * @return The index of the given term from the given list.
     */
    public int getCourseIndex(ArrayList<Course> terms, long id)
    {
        for (int i = 0; i < courses.size (); ++i)
        {
            if (terms.get (i).getUniqueID () == id)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Imports data from the given file to the current data set.
     *
     * @param in The reader to start reading from the file at the correct point.
     * @param tempTerms The list of terms to import.
     * @param tempCourses The list of courses IDs to import.
     * @return True if import succeeded, false otherwise.
     */
    public boolean importFromBackup(BufferedReader in, ArrayList<Term> tempTerms, ArrayList<Course> tempCourses) throws IOException
    {
        ArrayList<Textbook> tempTextbooks = new ArrayList<Textbook> ();
        ArrayList<AssignmentType> tempTypes = new ArrayList<AssignmentType> ();
        ArrayList<Instructor> tempInstructors = new ArrayList<Instructor> ();
        ArrayList<ListItem> tempAssignmentsAndEvents = new ArrayList<ListItem> ();
        ArrayList<Event> tempRepeatingEvents = new ArrayList<Event> ();

        // import the rest of the data attached to the given courses
        // throw out title line
        in.readLine ();
        // read all types
        String line = in.readLine ();
        while (!line.equals (""))
        {
            Textbook textbook = new Textbook (line, this);

            for (int i = 0; i < tempCourses.size (); ++i)
            {
                if (tempCourses.get (i).getUniqueID () == textbook.getCourseID ())
                {
                    tempTextbooks.add (textbook);
                    break;
                }
            }

            line = in.readLine ();
        }

        // throw out title line
        in.readLine ();
        // read all types
        line = in.readLine ();
        while (!line.equals (""))
        {
            AssignmentType type = new AssignmentType (line, this);

            for (int i = 0; i < tempCourses.size (); ++i)
            {
                if (tempCourses.get (i).getUniqueID () == type.getCourseID ())
                {
                    tempTypes.add (type);
                    break;
                }
            }

            line = in.readLine ();
        }

        // throw out title line
        in.readLine ();
        // read all assignments and events
        line = in.readLine ();
        while (!line.equals (""))
        {
            if (line.startsWith ("true"))
            {
                Assignment assignment = new Assignment (line, this);

                for (int i = 0; i < tempCourses.size (); ++i)
                {
                    if (tempCourses.get (i).getUniqueID () == assignment.getCourseID ())
                    {
                        tempAssignmentsAndEvents.add (assignment);
                        break;
                    }
                }
            }

            line = in.readLine ();
        }

        try
        {
            // throw out title line
            in.readLine ();
            // read all instructors
            line = in.readLine ();
            while (!line.equals (""))
            {
                Instructor instructor = new Instructor (line, this);

                for (int i = 0; i < tempCourses.size (); ++i)
                {
                    if (tempCourses.get (i).getUniqueID () == instructor.getCourseID ())
                    {
                        tempInstructors.add (instructor);
                        break;
                    }
                }

                line = in.readLine ();
            }
        }
        catch (NullPointerException ex)
        {
        }

        // add the temporary arrays to the actual data arrays
        for (int i = 0; i < tempTerms.size (); ++i)
        {
            if (getByID (tempTerms.get (i).getUniqueID ()) == null)
            {
                terms.add (tempTerms.get (i));
            }
        }
        for (int i = 0; i < tempCourses.size (); ++i)
        {
            courses.add (tempCourses.get (i));
        }
        for (int i = 0; i < tempTextbooks.size (); ++i)
        {
            textbooks.add (tempTextbooks.get (i));
        }
        for (int i = 0; i < tempTypes.size (); ++i)
        {
            types.add (tempTypes.get (i));
        }
        for (int i = 0; i < tempInstructors.size (); ++i)
        {
            instructors.add (tempInstructors.get (i));
        }
        for (int i = 0; i < eventYears.size (); ++i)
        {
            eventYears.get (i).markForDeletion (true);
        }
        for (int i = 0; i < tempAssignmentsAndEvents.size (); ++i)
        {
            assignmentsAndEvents.add (tempAssignmentsAndEvents.get (i));
        }
        for (int i = 0; i < tempRepeatingEvents.size (); ++i)
        {
            repeatingEvents.add (tempRepeatingEvents.get (i));
        }

        // resolve reference issues
        refreshReferences ();

        return true;
    }

    /**
     * Restores all data from a specified data file into the program.
     *
     * @param file The file to import from.
     * @return True if restore succeeded, false otherwise.
     */
    public boolean restoreFromBackup(File file)
    {
        // create temporaray lists in case an error occurs during import, than old data
        // is not lost
        Preferences tempPreferences = new Preferences (viewPanel);
        UserDetails tempUserDetails = new UserDetails ();
        ArrayList<Term> tempTerms = new ArrayList<Term> ();
        ArrayList<Course> tempCourses = new ArrayList<Course> ();
        ArrayList<Textbook> tempTextbooks = new ArrayList<Textbook> ();
        ArrayList<Instructor> tempInstructors = new ArrayList<Instructor> ();
        ArrayList<AssignmentType> tempTypes = new ArrayList<AssignmentType> ();
        ArrayList<ListItem> tempAssignmentsAndEvents = new ArrayList<ListItem> ();
        ArrayList<Event> tempRepeatingEvents = new ArrayList<Event> ();

        // add everything from the backup file to the data vectors
        try
        {
            in = new BufferedReader (new FileReader (file));

            // throw out title line
            in.readLine ();
            // read preferences line
            String prefString = in.readLine ();
            // throw out title line
            in.readLine ();
            tempPreferences.setWithString (prefString, in.readLine (), viewPanel);

            // throw out blank line
            in.readLine ();
            // throw out title line
            in.readLine ();
            // read all terms
            String line = in.readLine ();
            while (!line.equals (""))
            {
                tempTerms.add (new Term (line, this));

                line = in.readLine ();
            }

            // throw out title line
            in.readLine ();
            // read all courses
            line = in.readLine ();
            while (!line.equals (""))
            {
                tempCourses.add (new Course (line, this));

                line = in.readLine ();
            }

            // throw out title line
            in.readLine ();
            // read all types
            line = in.readLine ();
            while (!line.equals (""))
            {
                tempTextbooks.add (new Textbook (line, this));

                line = in.readLine ();
            }

            // throw out title line
            in.readLine ();
            // read all types
            line = in.readLine ();
            while (!line.equals (""))
            {
                tempTypes.add (new AssignmentType (line, this));

                line = in.readLine ();
            }

            // throw out title line
            in.readLine ();
            // read all assignments and events
            line = in.readLine ();
            while (!line.equals (""))
            {
                if (line.startsWith ("true"))
                {
                    tempAssignmentsAndEvents.add (new Assignment (line, this));
                }
                else
                {
                    Event newEvent = new Event (line, this);
                    tempAssignmentsAndEvents.add (newEvent);
                    if (newEvent.getRepeating ().getID () != -1)
                    {
                        tempRepeatingEvents.add (newEvent);
                    }
                }

                line = in.readLine ();
            }

            try
            {
                // throw out title line
                in.readLine ();
                // read all instructors
                line = in.readLine ();
                while (!line.equals (""))
                {
                    tempInstructors.add (new Instructor (line, this));

                    line = in.readLine ();
                }

                // throw out title line
                in.readLine ();
                tempUserDetails.setWithString (in.readLine ());
            }
            catch (NullPointerException ex)
            {
            }
        }
        catch (Exception ex)
        {
            return false;
        }

        terms = tempTerms;
        for (int i = 0; i < courses.size (); ++i)
        {
            courses.get (i).markForDeletion ();
        }
        courses = tempCourses;
        textbooks = tempTextbooks;
        types = tempTypes;
        instructors = tempInstructors;
        for (int i = 0; i < eventYears.size (); ++i)
        {
            eventYears.get (i).markForDeletion (true);
        }
        eventYears.clear ();
        assignmentsAndEvents = tempAssignmentsAndEvents;
        repeatingEvents = tempRepeatingEvents;
        preferences = tempPreferences;
        userDetails = tempUserDetails;

        refreshReferences ();

        viewPanel.settingsDialog.categoryTableModel.removeAllRows ();
        if (preferences.categories.size () > 0)
        {
            // parse each color line (name, color) and add it to the data vector as well as the UI model
            for (int i = 0; i < preferences.categories.size (); ++i)
            {
                viewPanel.settingsDialog.categoryTableModel.addRow (new Object[]
                        {
                            preferences.categories.get (i).getName (), preferences.categories.get (i).getColorString ()
                        });
                viewPanel.categoryComboModel.addElement (preferences.categories.get (i).getName ());
            }
        }
        else
        {
            // if no categories existed, add the default category, which always exists
            preferences.categories.add (new Category (viewPanel.domain.language.getString ("default"), Color.BLACK));
            viewPanel.settingsDialog.categoryTableModel.addRow (new Object[]
                    {
                        viewPanel.domain.language.getString ("default"), "0-0-0"
                    });
            viewPanel.categoryComboModel.addElement (viewPanel.domain.language.getString ("default"));
        }

        domain.currentCategoryIndex = -1;
        viewPanel.settingsDialog.settingsCategoriesTable.setSelectedRow (0);

        return true;
    }

    /**
     * Write all user data and preferences to a backup file.
     *
     * @param file The backup file to be written to.
     */
    public void writeBackupFile(File file)
    {
        try
        {
            out = new BufferedWriter (new FileWriter (file));

            // write preferences to the backup file
            out.write ("--PREFERENCES--" + LINE_RETURN);
            out.write (preferences.out () + LINE_RETURN);

            // write categories to the backup file
            out.write ("--CATEGORIES--" + LINE_RETURN);
            for (int i = 0; i < preferences.categories.size (); ++i)
            {
                out.write (preferences.categories.get (i).getName () + SEPARATOR);
                out.write (preferences.categories.get (i).getFormattedColor ());

                if (i < preferences.categories.size () - 1)
                {
                    out.write (SEPARATOR);
                }
            }
            out.write (LINE_RETURN + LINE_RETURN);

            // write terms to the backup file
            out.write ("--TERMS--" + LINE_RETURN);
            for (int i = 0; i < terms.size (); ++i)
            {
                out.write (terms.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write courses to the backup file
            out.write ("--COURSES--" + LINE_RETURN);
            for (int i = 0; i < courses.size (); ++i)
            {
                out.write (courses.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write textbooks to the backup file
            out.write ("--TEXTBOOKS--" + LINE_RETURN);
            for (int i = 0; i < textbooks.size (); ++i)
            {
                out.write (textbooks.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write types to the backup file
            out.write ("--TYPES--" + LINE_RETURN);
            for (int i = 0; i < types.size (); ++i)
            {
                out.write (types.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write assignments and events to the backup file
            out.write ("--ASSIGNMENTS AND EVENTS--" + LINE_RETURN);
            for (int i = 0; i < assignmentsAndEvents.size (); ++i)
            {
                out.write (assignmentsAndEvents.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write instructors to the backup file
            out.write ("--INSTRUCTORS--" + LINE_RETURN);
            for (int i = 0; i < instructors.size (); ++i)
            {
                out.write (instructors.get (i).out () + END_OF_LINE + LINE_RETURN);
            }
            out.write (LINE_RETURN);

            // write user details to the backup file
            out.write ("--USER DETAILS--" + LINE_RETURN);
            out.write (userDetails.out () + LINE_RETURN);

            out.write (LINE_RETURN);

            out.flush ();

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the preferences file does not exist, this will create a default
     * preferences file that can be written to.
     */
    private void createPreferencesFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (preferencesFile));

            out.write (preferences.out () + "\n" + "Default,0-0-0");
            out.flush ();

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the user details file does not exist, this will create an empty user
     * details file that can be written to.
     */
    private void createUserDetailsFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (userDetailsFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the terms file does not exist, this will create an empty terms file
     * that can be written to.
     */
    private void createTermsFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (termsFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the courses file does not exist, this will create an empty courses
     * file that can be written to.
     */
    private void createCoursesFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (coursesFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the types file does not exist, this will create an empty types file
     * that can be written to.
     */
    private void createTypesFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (typesFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the instructors file does not exist, this will create an empty
     * instructors file that can be written to.
     */
    private void createInstructorsFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (instructorsFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * If the textbooks file does not exist, this will create an empty textbooks
     * file that can be written to.
     */
    private void createTextbooksFile()
    {
        dataFolder.mkdir ();
        try
        {
            out = new BufferedWriter (new FileWriter (textbooksFile));

            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Performs a load operation, filling all the data vectors and preferences
     * vectors with all data found in the data files.
     */
    @Override
    public void load()
    {
        loadTerms ();
        loadCourses ();
        loadTypes ();
        loadInstructors ();
        loadTextbooks ();
        loadAssignments ();
        loadEvents ();

        // if we have moved data files from the an old location, remove the old data folder at this point and mark for saving to the
        // new data files
        if (oldAppDataFolder != null && oldAppDataFolder.exists ())
        {
            oldAppDataFolder.delete ();
            for (int i = 0; i < courses.size (); ++i)
            {
                courses.get (i).markChanged ();
            }
            for (int i = 0; i < eventYears.size (); ++i)
            {
                eventYears.get (i).markChanged ();
            }
            domain.needsCoursesAndTermsSave = true;
            domain.needsPreferencesSave = true;
        }

        refreshReferences ();
    }

    /**
     * Performs a save operation, pushing all data in the data vectors and
     * preferences vectors into the data files.
     */
    @Override
    public void save()
    {
        saveTerms ();
        saveCourses ();
        saveTypes ();
        saveInstructors ();
        saveTextbooks ();
    }

    /**
     * Loads the preferences vector with values from the data file.
     */
    @Override
    public void loadPreferences()
    {
        // if the preferences file does not exist, create it with default values
        if (!preferencesFile.exists ())
        {
            createPreferencesFile ();
        }

        try
        {
            if (oldPreferencesFile != null && oldPreferencesFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldPreferencesFile));
            }
            else
            {
                in = new BufferedReader (new FileReader (preferencesFile));
            }

            String line = in.readLine ();
            // parse the line of preferences
            if (line != null)
            {
                // clear all old preferences and load all new preferences into the vector
                String[] split = line.split (",");
                try
                {
                    preferences.width = Integer.parseInt (split[0]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                catch (NumberFormatException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.height = Integer.parseInt (split[1]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.x = Integer.parseInt (split[2]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.y = Integer.parseInt (split[3]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.nextCourseColorIndex = Integer.parseInt (split[4]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.colorByIndex = Integer.parseInt (split[5]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.middleTabbedPaneIndex = Integer.parseInt (split[6]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.filter1Index = Integer.parseInt (split[7]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.filter2Index = Integer.parseInt (split[8]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.sortIndex = Integer.parseInt (split[9]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.rmAlert = Boolean.valueOf (split[10]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.autoUpdate = Boolean.valueOf (split[11]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.sortAscending = Boolean.valueOf (split[12]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.dontShowGettingStarted = Boolean.valueOf (split[13]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.updateCheckIndex = Integer.parseInt (split[14]);
                }
                catch (ArrayIndexOutOfBoundsException innerEx)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[15].split ("-");
                    preferences.priorityColors[0] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[16].split ("-");
                    preferences.priorityColors[1] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[17].split ("-");
                    preferences.priorityColors[2] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[18].split ("-");
                    preferences.priorityColors[3] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[19].split ("-");
                    preferences.priorityColors[4] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[20].split ("-");
                    preferences.dueDateColors[0] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[21].split ("-");
                    preferences.dueDateColors[1] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[22].split ("-");
                    preferences.dueDateColors[2] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[23].split ("-");
                    preferences.dueDateColors[3] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[24].split ("-");
                    preferences.dueDateColors[4] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[25].split ("-");
                    preferences.dueDateColors[5] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.currentTheme = split[26];
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    preferences.language = split[27];
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
                try
                {
                    String[] color = split[28].split ("-");
                    preferences.dueDateColors[6] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsPreferencesSave = true;
                }
            }

            // find the current theme and select it
            for (int i = 0; i < themes.size (); ++i)
            {
                viewPanel.settingsDialog.themesModel.addElement (themes.get (i));
                viewPanel.settingsDialog.themesPrefModel.addElement (themes.get (i));
                if (themes.get (i).name.equals (preferences.currentTheme))
                {
                    currentTheme = themes.get (i);
                }
            }
            if (currentTheme == null)
            {
                currentTheme = themes.get (0);
            }

            // clear all old categories and load all category lines
            line = in.readLine ();
            viewPanel.settingsDialog.categoryTableModel.removeAllRows ();
            preferences.categories.clear ();
            if (line != null)
            {
                String[] split = line.split (",");
                // parse each color line (name, color) and add it to the data vector as well as the UI model
                for (int i = 0; i < split.length; i += 2)
                {
                    String[] colorSplit = split[i + 1].split ("-");
                    Color color = new Color (Integer.parseInt (colorSplit[0]), Integer.parseInt (colorSplit[1]), Integer.parseInt (colorSplit[2]));
                    preferences.categories.add (new Category (split[i], color));
                    viewPanel.settingsDialog.categoryTableModel.addRow (new Object[]
                            {
                                split[i], split[i + 1]
                            });
                    viewPanel.categoryComboModel.addElement (split[i]);
                }
            }
            else
            {
                // if no categories existed, add the default category, which always exists
                preferences.categories.add (new Category (viewPanel.domain.language.getString ("default"), Color.BLACK));
                viewPanel.settingsDialog.categoryTableModel.addRow (new Object[]
                        {
                            viewPanel.domain.language.getString ("default"), "0-0-0"
                        });
                viewPanel.categoryComboModel.addElement (viewPanel.domain.language.getString ("default"));
                domain.needsPreferencesSave = true;
            }
            in.close ();

            if (domain.needsPreferencesSave)
            {
                savePreferences ();
            }
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's preferences file was old, remove it so future saves are done to the new location
        if (oldPreferencesFile != null && oldPreferencesFile.exists ())
        {
            oldPreferencesFile.delete ();
        }
    }

    /**
     * Saves all preferences data in the vector to the data file.
     */
    @Override
    public void savePreferences()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (preferencesFile));

            // write the preferences vector to a single line
            out.write (preferences.out ());

            out.write (LINE_RETURN);

            // write categories each to their own line
            for (int i = 0; i < preferences.categories.size (); ++i)
            {
                out.write (preferences.categories.get (i).getName () + SEPARATOR);
                out.write (preferences.categories.get (i).getFormattedColor ());

                if (i < preferences.categories.size () - 1)
                {
                    out.write (SEPARATOR);
                }
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the user details object with values from the data file.
     */
    @Override
    public void loadUserDetails()
    {
        // if the terms file does not exist, create it with default values
        if (!userDetailsFile.exists ())
        {
            createUserDetailsFile ();
        }

        try
        {
            if (oldUserDetailsFile != null && oldUserDetailsFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldUserDetailsFile));
            }
            else
            {
                in = new BufferedReader (new FileReader (userDetailsFile));
            }

            String line = in.readLine ();
            // parse the line of preferences
            if (line != null)
            {
                // clear all old preferences and load all new preferences into the vector
                String[] split = line.split (",");
                try
                {
                    userDetails.studentName = split[0].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.school = split[1].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.idNumber = split[2].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.boxNumber = split[3].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.majors = split[4].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.concentrations = split[5].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.minors = split[6].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.advisorName = split[7].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.advisorEmail = split[8].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.advisorPhone = split[9].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.advisorOfficeHours = split[10].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.advisorOfficeLocation = split[11].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
                try
                {
                    userDetails.email = split[12].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    domain.needsUserDetailsSave = true;
                }
            }
            in.close ();

            if (domain.needsUserDetailsSave)
            {
                saveUserDetails ();
            }
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's details file was old, remove it so future saves are done to the new location
        if (oldUserDetailsFile != null && oldUserDetailsFile.exists ())
        {
            oldUserDetailsFile.delete ();
        }
    }

    /**
     * Saves all preferences data in the vector to the data file.
     */
    @Override
    public void saveUserDetails()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (userDetailsFile));

            // write the preferences vector to a single line
            out.write (userDetails.out ());

            out.write (LINE_RETURN);

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the term vector with values from the data file.
     */
    @Override
    public void loadTerms()
    {
        // if the terms file does not exist, create it with default values
        if (!termsFile.exists ())
        {
            createTermsFile ();
        }

        try
        {
            // clear all old term data and load in terms from the data file
            terms.clear ();

            if (oldTermsFile != null && oldTermsFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldTermsFile));
                String line = in.readLine ();
                while (line != null)
                {
                    terms.add (new Term (line, this));

                    line = in.readLine ();
                }
                in.close ();
            }

            in = new BufferedReader (new FileReader (termsFile));
            String line = in.readLine ();
            while (line != null)
            {
                terms.add (new Term (line, this));

                line = in.readLine ();
            }
            in.close ();
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's terms file was old, remove it so future saves are done to the new location
        if (oldTermsFile != null && oldTermsFile.exists ())
        {
            oldTermsFile.delete ();
        }
    }

    /**
     * Saves all term data in the vector to the data file.
     */
    @Override
    public void saveTerms()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (termsFile));

            // write each term to a its own line
            for (int i = 0; i < terms.size (); ++i)
            {
                out.write (terms.get (i).out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the course vector with values from the data file.
     */
    @Override
    public void loadCourses()
    {
        // if the courses file does not exist, create it with default values
        if (!coursesFile.exists ())
        {
            createCoursesFile ();
        }

        try
        {
            // clear all old course data and load in terms from the data file
            courses.clear ();

            if (oldCoursesFile != null && oldCoursesFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldCoursesFile));
                String line = in.readLine ();
                while (line != null)
                {
                    courses.add (new Course (line, this));

                    line = in.readLine ();
                }
                in.close ();
            }

            in = new BufferedReader (new FileReader (coursesFile));
            String line = in.readLine ();
            while (line != null)
            {
                courses.add (new Course (line, this));

                line = in.readLine ();
            }
            in.close ();
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's courses file was old, remove it so future saves are done to the new location
        if (oldCoursesFile != null && oldCoursesFile.exists ())
        {
            oldCoursesFile.delete ();
        }
    }

    /**
     * Saves all course data in the vector to the data file.
     */
    @Override
    public void saveCourses()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (coursesFile));

            // write each course to its own line
            for (int i = 0; i < courses.size (); ++i)
            {
                out.write (courses.get (i).out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the type vector with values from the data file.
     */
    @Override
    public void loadTypes()
    {
        // if the types file does not exist, create it with default values
        if (!typesFile.exists ())
        {
            createTypesFile ();
        }

        try
        {
            // clear all old type data and load in terms from the data file
            types.clear ();

            if (oldTypesFile != null && oldTypesFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldTypesFile));
                String line = in.readLine ();
                while (line != null)
                {
                    types.add (new AssignmentType (line, this));

                    line = in.readLine ();
                }
                in.close ();
            }

            in = new BufferedReader (new FileReader (typesFile));
            String line = in.readLine ();
            while (line != null)
            {
                types.add (new AssignmentType (line, this));

                line = in.readLine ();
            }
            in.close ();
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's types file was old, remove it so future saves are done to the new location
        if (oldTypesFile != null && oldTypesFile.exists ())
        {
            oldTypesFile.delete ();
        }
    }

    /**
     * Loads the instructors vector with values from the data file.
     */
    @Override
    public void loadInstructors()
    {
        // if the types file does not exist, create it with default values
        if (!instructorsFile.exists ())
        {
            createInstructorsFile ();
        }

        try
        {
            // clear all old type data and load in terms from the data file
            instructors.clear ();

            if (oldInstructorsFile != null && oldInstructorsFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldInstructorsFile));
                String line = in.readLine ();
                while (line != null)
                {
                    instructors.add (new Instructor (line, this));

                    line = in.readLine ();
                }
                in.close ();
            }

            in = new BufferedReader (new FileReader (instructorsFile));
            String line = in.readLine ();
            while (line != null)
            {
                instructors.add (new Instructor (line, this));

                line = in.readLine ();
            }
            in.close ();
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's instructors file was old, remove it so future saves are done to the new location
        if (oldInstructorsFile != null && oldInstructorsFile.exists ())
        {
            oldInstructorsFile.delete ();
        }
    }

    /**
     * Saves all type data in the vector to the data file.
     */
    @Override
    public void saveTypes()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (typesFile));

            // write each type to its own line
            for (int i = 0; i < types.size (); ++i)
            {
                out.write (types.get (i).out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Saves all type data in the vector to the data file.
     */
    @Override
    public void saveInstructors()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (instructorsFile));

            // write each instructor to its own line
            for (int i = 0; i < instructors.size (); ++i)
            {
                out.write (instructors.get (i).out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the textbook vector with values from the data file.
     */
    @Override
    public void loadTextbooks()
    {
        // if the textbooks file does not exist, create it with default values
        if (!textbooksFile.exists ())
        {
            createTextbooksFile ();
        }

        try
        {
            // clear all old textbook data and load in terms from the data file
            textbooks.clear ();

            if (oldTextbooksFile != null && oldTextbooksFile.exists ())
            {
                in = new BufferedReader (new FileReader (oldTextbooksFile));
                String line = in.readLine ();
                while (line != null)
                {
                    textbooks.add (new Textbook (line, this));

                    line = in.readLine ();
                }
                in.close ();
            }

            in = new BufferedReader (new FileReader (textbooksFile));
            String line = in.readLine ();
            while (line != null)
            {
                textbooks.add (new Textbook (line, this));

                line = in.readLine ();
            }
            in.close ();
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }

        // if the user's textbooks file was old, remove it so future saves are done to the new location
        if (oldTextbooksFile != null && oldTextbooksFile.exists ())
        {
            oldTextbooksFile.delete ();
        }
    }

    /**
     * Saves all textbook data in the vector to the data file.
     */
    @Override
    public void saveTextbooks()
    {
        try
        {
            out = new BufferedWriter (new FileWriter (textbooksFile));

            // write each textbook to its own line
            for (int i = 0; i < textbooks.size (); ++i)
            {
                out.write (textbooks.get (i).out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            out.close ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the assignment vector with values from the data file.
     */
    @Override
    public void loadAssignments()
    {
        try
        {
            // clear all old assignments and events data and load in terms from the data file
            assignmentsAndEvents.clear ();

            for (int i = 0; i < courses.size (); ++i)
            {
                // this method takes care of assignments from old data files as well
                courses.get (i).loadAssignments (this);
            }
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Loads the events and events vector with values from the data file.
     */
    @Override
    public void loadEvents()
    {
        try
        {
            if (oldAppDataFolder != null && oldAppDataFolder.list () != null)
            {
                for (int i = 0; i < oldAppDataFolder.list ().length; ++i)
                {
                    if (oldAppDataFolder.list ()[i].startsWith ("e") && oldAppDataFolder.list ()[i].endsWith (".dat"))
                    {
                        EventYear eventYear = new EventYear (oldAppDataFolder.list ()[i].substring (1, oldAppDataFolder.list ()[i].length () - 4), this);
                        eventYears.add (eventYear);
                        eventYear.loadEvents (this, false);
                        eventYear.markChanged ();
                        eventYear.markForDeletion (false);
                    }
                }
            }

            if (dataFolder.list () != null)
            {
                for (int i = 0; i < dataFolder.list ().length; ++i)
                {
                    if (dataFolder.list ()[i].startsWith ("e") && dataFolder.list ()[i].endsWith (".dat"))
                    {
                        EventYear eventYear = getEventYear (dataFolder.list ()[i].substring (1, dataFolder.list ()[i].length () - 4));
                        eventYears.add (eventYear);
                        eventYear.loadEvents (this, true);
                    }
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Load themes into the given combo model.
     *
     * @param model The model to load themes into.
     */
    @Override
    public void loadThemes(DefaultComboBoxModel model)
    {
        model.removeAllElements ();
        for (int i = 0; i < themes.size (); ++i)
        {
            model.addElement (themes.get (i));
        }
    }

    /**
     * Saves all assignments data in the vector to the data file.
     */
    @Override
    public void saveAssignments(Course course)
    {
        try
        {
            out = course.openWriter ();

            // write each assignment and event to its own line
            for (int i = 0; i < course.getAssignmentCount (); ++i)
            {
                Assignment assignment = course.getAssignment (i);
                out.write (assignment.out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            course.closeWriter ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Saves all events data in the vector to the data file.
     */
    @Override
    public void saveEvents(EventYear eventYear)
    {
        try
        {
            out = eventYear.openWriter ();

            for (int i = 0; i < eventYear.getEventCount (); ++i)
            {
                Event event = eventYear.getEvent (i);
                out.write (event.out () + END_OF_LINE + LINE_RETURN);
            }

            out.flush ();
            eventYear.closeWriter ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }
}
