/*
 * Get Organized - Organize your schedule, course assignments, and grades
 * Copyright © 2012 Alex Laird
 * getorganized@alexlaird.name
 * alexlaird.name
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

package adl.go.types;

import adl.go.gui.Domain;
import adl.go.resource.LocalUtility;
import adl.go.resource.Utility;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * A course may contain textbooks, assignmentsAndEvents, and assignment types
 * and has a term as its parent. A course is necessary to attach an assignment
 * to.
 *
 * @author Alex Laird
 */
public class Course extends ExtendedTreeNode
{
    /**
     * A reference to the containing term.
     */
    private Term term;
    /**
     * A list of all assignments for this course.
     */
    private ArrayList<Assignment> assignments = new ArrayList<Assignment> ();
    /**
     * A list of all textbooks for this course.
     */
    private ArrayList<Textbook> textbooks = new ArrayList<Textbook> ();
    /**
     * A list of all types for this course.
     */
    private ArrayList<AssignmentType> types = new ArrayList<AssignmentType> ();
    /**
     * A list of all instructors for this course.
     */
    private ArrayList<Instructor> instructors = new ArrayList<Instructor> ();
    /**
     * The start date of the term.
     */
    private String startDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The end date of the term.
     */
    private String endDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The room this course is in.
     */
    private String roomLocation = "";
    /**
     * The name of the instructor for this course.
     */
    private String instructorName = "";
    /**
     * The instructor's email for this course.
     */
    private String instructorEmail = "";
    /**
     * The office hours of the instructor.
     */
    private String officeHours = "";
    /**
     * The office location of the instructor.
     */
    private String officeLocation = "";
    /**
     * The number of credits the course is.
     */
    private String credits = "0";
    /**
     * The start hour of the course.
     */
    private String startHr = "12";
    /**
     * The start minute of the course.
     */
    private String startMin = "00";
    /**
     * The start meridian of the course.
     */
    private String startM = "PM";
    /**
     * The end hour of the course.
     */
    private String endHr = "12";
    /**
     * The end minute of the course.
     */
    private String endMin = "00";
    /**
     * The end meridian of the course.
     */
    private String endM = "PM";
    /**
     * The phone number of the instructor
     */
    private String instructorPhone = "";
    /**
     * The color used to represent assignments attached to the course.
     */
    private Color color = Color.BLACK;
    /**
     * True if the course has a lab with separate meeting time.
     */
    private boolean hasLab = false;
    /**
     * True if the course's lab is online.
     */
    private boolean labIsOnline = false;
    /**
     * The room location of the course's lab.
     */
    private String labRoomLocation = "";
    /**
     * The start date of the course's lab.
     */
    private String labStartDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The end date of the course's lab.
     */
    private String labEndDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The start hour of the course's lab.
     */
    private String labStartHr = "12";
    /**
     * The start minute of the course's lab.
     */
    private String labStartMin = "00";
    /**
     * The start meridian of the course's lab.
     */
    private String labStartM = "PM";
    /**
     * The end hour of the course's lab.
     */
    private String labEndHr = "12";
    /**
     * The end minute of the course's lab.
     */
    private String labEndMin = "00";
    /**
     * The end meridian of the course's lab.
     */
    private String labEndM = "PM";
    /**
     * The number of credits the lab is.
     */
    private String labCredits = "0";
    /**
     * The course number of this course.
     */
    private String courseNumber = "";
    /**
     * The lab number of this course.
     */
    private String labNumber = "";
    /**
     * The website for this course.
     */
    private String courseWebsite = "";
    /**
     * The website for the lab of this course.
     */
    private String labWebsite = "";
    /**
     * The unique ID of the term this course is attached to.
     */
    protected long termID;
    /**
     * The days of the week this class is on.
     */
    private boolean[] daysOfWeek = new boolean[]
    {
        false, false, false, false, false, false, false
    };
    /**
     * The days of the week the lab is on.
     */
    private boolean[] labDaysOfWeek = new boolean[]
    {
        false, false, false, false, false, false, false
    };
    /**
     * True if a course is online, false otherwise.
     */
    private boolean isOnline = false;
    /**
     * The list of grades list due date times, which corresponds directly to the
     * grades list.
     */
    private ArrayList<Long> gradesListTimes = new ArrayList<Long> ();
    /**
     * The list of grades, which corresponds directly to the list of grades due
     * date times.
     */
    private ArrayList<Object[]> gradesList = new ArrayList<Object[]> ();
    /**
     * True if this course has assignment changes, false otherwise.
     */
    private boolean assignmentChanged = false;
    /**
     * The assignments data file.
     */
    private File assignmentsFile;
    private File oldAssignmentsFile;
    /**
     * The assignments data file writer.
     */
    private BufferedWriter outAssignment;
    /**
     * The assignments data file reader.
     */
    private BufferedReader inAssignment;

    /**
     * Constructs a course with a name, unique ID, and containing term.
     *
     * @param name The name of the course.
     * @param id The unique ID of the course.
     * @param term The containing term of the course.
     * @param utility The reference to the utility resource.
     */
    public Course(String name, long id, Term term, Utility utility)
    {
        super (name, id, (LocalUtility) utility);
        assignmentsFile = new File (((LocalUtility) utility).getDataFolder (), "a" + id + ".dat");
        if (((LocalUtility) utility).getAppDataFolder () != null)
        {
            oldAssignmentsFile = new File (((LocalUtility) utility).getAppDataFolder (), "a" + id + ".dat");
        }
        setTerm (term);
        // tie the start and end dates of the course and lab to that of the parent term
        setStartDate (term.getStartDate ());
        setEndDate (term.getEndDate ());
        setLabStartDate (term.getStartDate ());
        setLabEndDate (term.getEndDate ());
        setColor (utility.nextColor ());
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility The reference to the utility resource.
     */
    public Course(String parse, LocalUtility utility)
    {
        super ("", -1, utility);
        Scanner scan = new Scanner (parse).useDelimiter ("(?<!\\\\)" + SEPARATOR + "|" + "(?<!\\\\)" + END_OF_LINE);
        try
        {
            setTypeName (scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE));
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            long tempId = scan.nextLong ();
            setUniqueID (tempId);
            assignmentsFile = new File (utility.getDataFolder (), "a" + tempId + ".dat");
            if (((LocalUtility) utility).getAppDataFolder () != null)
            {
                oldAssignmentsFile = new File (utility.getAppDataFolder (), "a" + tempId + ".dat");
            }
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            startDate = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            endDate = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            termID = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[0] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[1] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[2] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[3] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[4] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[5] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            daysOfWeek[6] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            roomLocation = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            instructorName = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            instructorEmail = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            officeHours = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            officeLocation = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            credits = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            startHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            endHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            instructorPhone = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            isOnline = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            String[] colorSplit = scan.next ().split ("-");
            color = new Color (Integer.parseInt (colorSplit[0]), Integer.parseInt (colorSplit[1]), Integer.parseInt (colorSplit[2]));
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        catch (NumberFormatException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            hasLab = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labIsOnline = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labRoomLocation = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labStartDate = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labEndDate = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labStartHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labEndHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labCredits = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }

        if (startHr.contains (":"))
        {
            startMin = startHr.split (":")[1].split (" ")[0];
            startM = startHr.split (":")[1].split (" ")[1];
            startHr = startHr.split (":")[0];
            utility.domain.needsCoursesAndTermsSave = true;
        }
        else
        {
            try
            {
                startMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
            try
            {
                startM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
        }
        if (endHr.contains (":"))
        {
            endMin = endHr.split (":")[1].split (" ")[0];
            endM = endHr.split (":")[1].split (" ")[1];
            endHr = endHr.split (":")[0];
            utility.domain.needsCoursesAndTermsSave = true;
        }
        else
        {
            try
            {
                endMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
            try
            {
                endM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
        }
        if (labStartHr.contains (":"))
        {
            labStartMin = labStartHr.split (":")[1].split (" ")[0];
            labStartM = labStartHr.split (":")[1].split (" ")[1];
            labStartHr = labStartHr.split (":")[0];
            utility.domain.needsCoursesAndTermsSave = true;
        }
        else
        {
            try
            {
                labStartMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
            try
            {
                labStartM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
        }
        if (labEndHr.contains (":"))
        {
            labEndMin = labEndHr.split (":")[1].split (" ")[0];
            labEndM = labEndHr.split (":")[1].split (" ")[1];
            labEndHr = labEndHr.split (":")[0];
            utility.domain.needsCoursesAndTermsSave = true;
        }
        else
        {
            try
            {
                labEndMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
            try
            {
                labEndM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                utility.domain.needsCoursesAndTermsSave = true;
            }
        }
        try
        {
            labDaysOfWeek[0] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[1] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[2] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[3] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[4] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[5] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labDaysOfWeek[6] = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            courseNumber = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labNumber = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            courseWebsite = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            labWebsite = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }

        scan.close ();
    }

    /**
     * Retrieves the unique ID of the term this course is attached to.
     *
     * @return The unique ID of this courses term.
     */
    public long getTermID()
    {
        return termID;
    }

    /**
     * Assignments have now been saved, so set changed flag back.
     */
    public void saved()
    {
        assignmentChanged = false;
    }

    /**
     * Mark assignments as changed.
     */
    public void markChanged()
    {
        assignmentChanged = true;
    }

    /**
     * Delete the associated assignments file for this course.
     */
    public void markForDeletion()
    {
        assignmentsFile.delete ();
    }

    /**
     * Check if assignment's have been changed within this course.
     *
     * @return True if assignments have changed, false otherwise.
     */
    public boolean isChanged()
    {
        return assignmentChanged;
    }

    /**
     * Opens the assignments file writer.
     *
     * @return The newly opened writer.
     */
    public BufferedWriter openWriter() throws IOException
    {
        if (inAssignment != null)
        {
            closeReader ();
        }
        if (outAssignment != null)
        {
            closeWriter ();
        }
        outAssignment = new BufferedWriter (new FileWriter (assignmentsFile));
        return outAssignment;
    }

    /**
     * Create an assignments file for the given course.
     *
     * @param utility A reference to the utility class.
     */
    private void createAssignmentsFile(LocalUtility utility)
    {
        utility.getDataFolder ().mkdir ();
        try
        {
            outAssignment = new BufferedWriter (new FileWriter (assignmentsFile));

            closeWriter ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Load the assignment's in this course's assignment data file.
     *
     * @param utility A reference to the utility class.
     */
    public void loadAssignments(LocalUtility utility) throws IOException
    {
        // if the types file does not exist, create it with default values
        if (!assignmentsFile.exists ())
        {
            createAssignmentsFile (utility);
        }

        // clear all old assignments and events data and load in terms from the data file
        if (oldAssignmentsFile != null && oldAssignmentsFile.exists ())
        {
            inAssignment = openReader (oldAssignmentsFile);
            String line = inAssignment.readLine ();
            while (line != null)
            {
                if (line.startsWith ("true"))
                {
                    utility.assignmentsAndEvents.add (new Assignment (line, utility));
                }

                line = inAssignment.readLine ();
            }
            closeReader ();
            oldAssignmentsFile.delete ();
        }

        inAssignment = openReader (assignmentsFile);
        String line = inAssignment.readLine ();
        while (line != null)
        {
            if (line.startsWith ("true"))
            {
                utility.assignmentsAndEvents.add (new Assignment (line, utility));
            }

            line = inAssignment.readLine ();
        }
        closeReader ();
    }

    /**
     * Closes the assignments file writer.
     */
    public void closeWriter() throws IOException
    {
        if (outAssignment != null)
        {
            outAssignment.close ();
            outAssignment = null;
        }
    }

    /**
     * Opens the assignments file reader.
     *
     * @return The newly opened reader.
     */
    public BufferedReader openReader(File file) throws IOException
    {
        if (inAssignment != null)
        {
            closeReader ();
        }
        if (outAssignment != null)
        {
            closeWriter ();
        }
        inAssignment = new BufferedReader (new FileReader (file));
        return inAssignment;
    }

    /**
     * Closes the assignments file reader.
     */
    public void closeReader() throws IOException
    {
        if (inAssignment != null)
        {
            inAssignment.close ();
            inAssignment = null;
        }
    }

    /**
     * Adds the given assignment to the course.
     *
     * @param assignment The assignment to be added
     */
    public void addAssignment(Assignment assignment)
    {
        assignments.add (assignment);
    }

    /**
     * Removes the specified assignment from the course.
     *
     * @param assignment The assignment to be removed.
     * @return The removed assignment.
     */
    public Assignment removeAssignment(Assignment assignment)
    {
        assignments.remove (assignment);
        return assignment;
    }

    /**
     * Retrieves the assignment at the specified index.
     *
     * @param index The index of the assignment.
     * @return The assignment at the specified index.
     */
    public Assignment getAssignment(int index)
    {
        return assignments.get (index);
    }

    /**
     * Returns the index of the assignment in this course.
     *
     * @return The index of the assignment.
     */
    public int getLocalAssignmentIndex(Assignment assignment)
    {
        for (int i = 0; i < assignments.size (); ++i)
        {
            if (assignments.get (i) == assignment)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retrieve the sum of all weights attached to this course added together.
     *
     * @return The added type weight grades for this course.
     */
    public double getTypeGrades()
    {
        ArrayList<AssignmentType> usedTypes = new ArrayList<AssignmentType> ();

        for (int i = 0; i < assignments.size (); ++i)
        {
            if (assignments.get (i).isDone () && !assignments.get (i).getGrade ().replaceAll (" ", "").equals ("") && assignments.get (i).getType () != null)
            {
                if (!usedTypes.contains (assignments.get (i).getType ()))
                {
                    usedTypes.add (assignments.get (i).getType ());
                }
            }
        }

        double sum = 0;
        for (int i = 0; i < usedTypes.size (); ++i)
        {
            sum += (usedTypes.get (i).getGrade () * (Double.parseDouble (usedTypes.get (i).getWeight ().replaceAll ("%", ""))));
        }
        return sum;
    }

    /**
     * Reset the sorted list of grades.
     */
    public void resetGradesList()
    {
        gradesList.clear ();
        gradesListTimes.clear ();
    }

    /**
     * Add the given grade and its information to the list of grades.
     *
     * @param grade
     * @param type
     * @param time
     */
    public void addGrade(double grade, AssignmentType type, long time)
    {
        gradesList.add (new Object[]
                {
                    grade, type
                });
        gradesListTimes.add (time);
    }

    /**
     * Sort the list of grades by due date.
     */
    public void sortGradesListByTime()
    {
        boolean swapped = true;
        while (swapped)
        {
            swapped = false;
            for (int i = 0; i < gradesListTimes.size () - 1; ++i)
            {
                if (gradesListTimes.get (i) > gradesListTimes.get (i + 1))
                {
                    long tempL = gradesListTimes.get (i);
                    gradesListTimes.set (i, gradesListTimes.get (i + 1));
                    gradesListTimes.set (i + 1, tempL);
                    Object[] tempD = gradesList.get (i);
                    gradesList.set (i, gradesList.get (i + 1));
                    gradesList.set (i + 1, tempD);
                    swapped = true;
                }
            }
        }
    }

    /**
     * Get the day count from the beginning of the term this course is within
     * for the given assignment index.
     *
     * @param j The assignment index to get the day for.
     * @return The number of days in the given assignment is from the beginning
     * of this course's term.
     */
    public long getDayNumAtPoint(int j) throws ParseException
    {
        return (gradesListTimes.get (j) - Domain.DATE_FORMAT.parse (getStartDate ()).getTime ()) / (24 * 60 * 60 * 1000);
    }

    /**
     * Calculate the overall grade for the course up to the given point.
     *
     * @param j The furthest point to calculate a grade for.
     * @return The grade up to the given point.
     */
    public double calculateGradeAtPoint(int j)
    {
        double overallWeight = 0;
        ArrayList<Object[]> typesAndAvg = new ArrayList<Object[]> ();
        for (int i = 0; i <= j; ++i)
        {
            double grade = (Double) gradesList.get (i)[0];
            AssignmentType type = (AssignmentType) gradesList.get (i)[1];
            double weight = Double.parseDouble (type.getWeight ().replaceAll ("%", ""));
            Object[] foundType = null;
            for (int k = 0; k < typesAndAvg.size (); ++k)
            {
                if (typesAndAvg.get (k)[0] == type)
                {
                    foundType = typesAndAvg.get (k);
                }
            }
            if (foundType == null)
            {
                typesAndAvg.add (new Object[]
                        {
                            type, grade
                        });
                overallWeight += weight;
            }
            else
            {
                double avgGrade = (Double) foundType[1];
                avgGrade = (avgGrade + grade) / 2;
                foundType[1] = avgGrade;
            }
        }

        double sum = 0;
        for (int i = 0; i < typesAndAvg.size (); ++i)
        {
            sum += (((Double) typesAndAvg.get (i)[1]) * ((Double.parseDouble (((AssignmentType) typesAndAvg.get (i)[0]).getWeight ().replaceAll ("%", "")))));
        }
        return sum / overallWeight;
    }

    /**
     * Reset all grades for each type associated with this course.
     */
    public void resetTypeGrades()
    {
        for (int i = 0; i < getTypeCount (); ++i)
        {
            getType (i).resetGrade ();
        }
    }

    /**
     * Retrieves the percentage value of all types that have at least one
     * completed assignment attached to them.
     *
     * @return The sum percentage of type weights that have a completed
     * assignment attached to them.
     */
    public double getTypePercentComplete()
    {
        ArrayList<AssignmentType> usedTypes = new ArrayList<AssignmentType> ();
        for (int i = 0; i < assignments.size (); ++i)
        {
            if (assignments.get (i).isDone () && !assignments.get (i).getGrade ().replaceAll (" ", "").equals ("") && assignments.get (i).getType () != null)
            {
                if (!usedTypes.contains (assignments.get (i).getType ()))
                {
                    usedTypes.add (assignments.get (i).getType ());
                }
            }
        }

        double sum = 0;
        for (int i = 0; i < usedTypes.size (); ++i)
        {
            if (!usedTypes.get (i).getWeight ().replaceAll (" ", "").equals (""))
            {
                sum += Double.parseDouble (usedTypes.get (i).getWeight ().replaceAll ("%", ""));
            }
        }

        return sum;
    }

    /**
     * Retrieves the number of unfinished assignments attached to this course.
     *
     * @return The number of assignments for this course.
     */
    public int getUnfinishedAssignmentCount()
    {
        int total = 0;
        for (int i = 0; i < getAssignmentCount (); ++i)
        {
            if (getAssignment (i).isDone ())
            {
                ++total;
            }
        }
        return getAssignmentCount () - total;
    }

    /**
     * Retrieves the number of assignments attached to this course.
     *
     * @return The number of assignments for this course.
     */
    public int getAssignmentCount()
    {
        return assignments.size ();
    }

    /**
     * Retrieves the number of graded assignments attached to this course.
     *
     * @return The number of graded assignments attached to this course.
     */
    public int getGradedAssignmentCount()
    {
        return gradesList.size ();
    }

    /**
     * Checks if the specified assignment is contained in this course.
     *
     * @return The assignment to be checked for.
     */
    public boolean hasAssignment(Assignment assignment)
    {
        for (int i = 0; i < assignments.size (); ++i)
        {
            if (assignments.get (i) == assignment)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a list of all assignmentsAndEvents matching the name specified.
     *
     * @param assignmentName The name of the assignment to retrieve.
     * @return A list of all assignment objects matching the name contained in
     * this course.
     */
    public Assignment[] getAssignmentByName(String assignmentName)
    {
        ArrayList<Assignment> assignment = new ArrayList<Assignment> ();

        for (int i = 0; i < getAssignmentCount (); ++i)
        {
            if (getAssignment (i).getItemName ().equals (assignmentName))
            {
                assignment.add (getAssignment (i));
            }
        }

        return assignment.toArray (new Assignment[assignment.size ()]);
    }

    /**
     * Adds the given textbook to the course.
     *
     * @param textbook The textbook to be added
     */
    public void addTextbook(Textbook textbook)
    {
        textbooks.add (textbook);
    }

    /**
     * Removes the specified textbook from the course.
     *
     * @param textbook The textbook to be removed.
     * @return The removed textbook.
     */
    public Textbook removeTextbook(Textbook textbook)
    {
        textbooks.remove (textbook);
        return textbook;
    }

    /**
     * Retrieves the number of instructors attached to this course.
     *
     * @return The number of instructors for this course.
     */
    public int getInstructorCount()
    {
        return instructors.size ();
    }

    /**
     * Retrieves the number of textbooks attached to this course.
     *
     * @return The number of textbooks for this course.
     */
    public int getTextbookCount()
    {
        return textbooks.size ();
    }

    /**
     * Retrieves the textbook at the specified index.
     *
     * @param i The index to retrieve from.
     * @return The specified textbook.
     */
    public Textbook getTextbook(int i)
    {
        return textbooks.get (i);
    }

    /**
     * Checks if the specified textbook is contained in this course.
     *
     * @return The textbook to be checked for.
     */
    public boolean hasTextbook(Textbook textbook)
    {
        for (int i = 0; i < textbooks.size (); ++i)
        {
            if (textbooks.get (i) == textbook)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a reference to the textbook with the specified name if it is
     * contained in this course.
     *
     * @param textbookName The name of the textbook to retrieve.
     * @return An object for the textbook, or null if the course was not found.
     */
    public Textbook getTextbookByName(String textbookName)
    {
        for (int i = 0; i < textbooks.size (); ++i)
        {
            if (getTextbook (i).getTypeName ().equals (textbookName))
            {
                return getTextbook (i);
            }
        }

        return null;
    }

    /**
     * Adds the given type to the course.
     *
     * @param type The type to be added
     */
    public void addType(AssignmentType type)
    {
        types.add (type);
    }

    /**
     * Adds the given type to the course.
     *
     * @param instructor The instructor to be added
     */
    public void addInstructor(Instructor instructor)
    {
        instructors.add (instructor);
    }

    /**
     * Removes the specified type from the course.
     *
     * @param type The type to be removed.
     * @return The removed type.
     */
    public AssignmentType removeType(AssignmentType type)
    {
        types.remove (type);
        return type;
    }

    /**
     * Removes the specified type from the course.
     *
     * @param type The type to be removed.
     * @return The removed type.
     */
    public Instructor removeInstructor(Instructor instructor)
    {
        instructors.remove (instructor);
        return instructor;
    }

    /**
     * Retrieves the number of textbooks attached to this course.
     *
     * @return The number of textbooks for this course.
     */
    public int getTypeCount()
    {
        return types.size ();
    }

    /**
     * Retrieves the instructor at the specified index.
     *
     * @param i The index to retrieve from.
     * @return The specified instructor.
     */
    public Instructor getInstructor(int i)
    {
        return instructors.get (i);
    }

    /**
     * Retrieves the type at the specified index.
     *
     * @param i The index to retrieve from.
     * @return The specified type.
     */
    public AssignmentType getType(int i)
    {
        return types.get (i);
    }

    /**
     * Retrieves the total of all type weights for this course (which will be
     * less than or equal to one and returned as some decimal less than or equal
     * to one).
     *
     * @param type The type to not add into the calculation since it is being
     * changed.
     * @return The total of all type weights for this course.
     */
    public double getTotalWeightsLess(AssignmentType type)
    {
        double total = 0;

        for (int i = 0; i < types.size (); ++i)
        {
            if (!types.get (i).getWeight ().equals ("") && types.get (i) != type)
            {
                total += Double.parseDouble (types.get (i).getWeight ().replaceAll ("%", ""));
            }
        }

        return total / 100;
    }

    /**
     * Checks if the specified type is contained in this course.
     *
     * @return The type to be checked for.
     */
    public boolean hasType(AssignmentType type)
    {
        for (int i = 0; i < types.size (); ++i)
        {
            if (types.get (i) == type)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the specified instructor is contained in this course.
     *
     * @return The instructor to be checked for.
     */
    public boolean hasInstructor(Instructor instructor)
    {
        for (int i = 0; i < instructors.size (); ++i)
        {
            if (instructors.get (i) == instructor)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a reference to the type with the specified name if it is
     * contained in this course.
     *
     * @param typeName The name of the type to retrieve.
     * @return An object for the type, or null if the course was not found.
     */
    public AssignmentType getTypeByName(String typeName)
    {
        for (int i = 0; i < types.size (); ++i)
        {
            if (getType (i).getTypeName ().equals (typeName))
            {
                return getType (i);
            }
        }

        return null;
    }

    /**
     * Retrieves the containing term of the course.
     *
     * @return The containing term of the course.
     */
    public Term getTerm()
    {
        return term;
    }

    /**
     * Sets the containing term.
     *
     * @param term The containing term to be set.
     */
    public final void setTerm(Term term)
    {
        this.term = term;
    }

    /**
     * Retrieves the start date for the term.
     *
     * @return The start date for the term.
     */
    public String getStartDate()
    {
        return startDate;
    }

    /**
     * Sets the start date for the term.
     *
     * @param startDate The start date for the term to be set.
     */
    public final void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Retrieves the end date for the term.
     *
     * @return The end date for the term.
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     * Sets the end date for the course.
     *
     * @param endDate The end date for the course to be set.
     */
    public final void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Sets the start date for the lab.
     *
     * @param labStartDate The start date for the lab to be set.
     */
    public final void setLabStartDate(String labStartDate)
    {
        this.labStartDate = labStartDate;
    }

    /**
     * Retrieves the start date for the lab.
     *
     * @return The start date for the lab.
     */
    public String getLabStartDate()
    {
        return labStartDate;
    }

    /**
     * Sets the end date for the lab.
     *
     * @param labEndDate The end date to be set for the lab.
     */
    public final void setLabEndDate(String labEndDate)
    {
        this.labEndDate = labEndDate;
    }

    /**
     * Retrieves the end date of the lab.
     *
     * @return The end date of the lab.
     */
    public String getLabEndDate()
    {
        return labEndDate;
    }

    /**
     * Returns true or false if this class is set to be on that day of the week.
     * The days of the week are indexed from 0 to 6, where 0 is Sunday and 6 is
     * Saturday.
     *
     * @param i The index of the day.
     * @return True if the course is on that day, false otherwise.
     */
    public boolean isOnDay(int i)
    {
        return daysOfWeek[i];
    }

    /**
     * Returns true or false if this lab is set to be on that day of the week.
     * The days of the week are indexed from 0 to 6, where 0 is Sunday and 6 is
     * Saturday.
     *
     * @param i The index of the day.
     * @return True if the course is on that day, false otherwise.
     */
    public boolean isLabOnDay(int i)
    {
        return labDaysOfWeek[i];
    }

    /**
     * Sets the state for the day of the week for this course.
     *
     * @param i The index of the day.
     * @param state The state to set that day to.
     */
    public void setOnDay(int i, boolean state)
    {
        daysOfWeek[i] = state;
    }

    /**
     * Sets the state of the day of the week for this lab.
     *
     * @param i The index of the day.
     * @param state The state to set that day to.
     */
    public void setLabOnDay(int i, boolean state)
    {
        labDaysOfWeek[i] = state;
    }

    /**
     * Retrieves a comma separated list of lab days the course meets on.
     *
     * @return A string showing the lab days the course meets on.
     */
    public String getLabDaysString(ResourceBundle language)
    {
        String days = "";
        if (labDaysOfWeek[0])
        {
            days += language.getString ("sun") + ", ";
        }
        if (labDaysOfWeek[1])
        {
            days += language.getString ("mon") + ", ";
        }
        if (labDaysOfWeek[2])
        {
            days += language.getString ("tue") + ", ";
        }
        if (labDaysOfWeek[3])
        {
            days += language.getString ("wed") + ", ";
        }
        if (labDaysOfWeek[4])
        {
            days += language.getString ("thu") + ", ";
        }
        if (labDaysOfWeek[5])
        {
            days += language.getString ("fri") + ", ";
        }
        if (labDaysOfWeek[6])
        {
            days += language.getString ("sat");
        }
        if (days.trim ().endsWith (","))
        {
            days = days.trim ().substring (0, days.lastIndexOf (","));
        }

        return days;
    }

    /**
     * Retrieves a comma separated list of days the course meets on.
     *
     * @return A string showing the days the course meets on.
     */
    public String getDaysString(ResourceBundle language)
    {
        String days = "";
        if (daysOfWeek[0])
        {
            days += language.getString ("sun") + ", ";
        }
        if (daysOfWeek[1])
        {
            days += language.getString ("mon") + ", ";
        }
        if (daysOfWeek[2])
        {
            days += language.getString ("tue") + ", ";
        }
        if (daysOfWeek[3])
        {
            days += language.getString ("wed") + ", ";
        }
        if (daysOfWeek[4])
        {
            days += language.getString ("thu") + ", ";
        }
        if (daysOfWeek[5])
        {
            days += language.getString ("fri") + ", ";
        }
        if (daysOfWeek[6])
        {
            days += language.getString ("sat");
        }
        if (days.trim ().endsWith (","))
        {
            days = days.trim ().substring (0, days.lastIndexOf (","));
        }

        return days;
    }

    /**
     * Retrieves the room location of this course.
     *
     * @return The room location of this course.
     */
    public String getRoomLocation()
    {
        return roomLocation;
    }

    /**
     * Sets the room location of this course.
     *
     * @param roomLocation The room location to be set.
     */
    public void setRoomLocation(String roomLocation)
    {
        this.roomLocation = roomLocation;
    }

    /**
     * Sets the course number of this course.
     *
     * @param courseNumber The course number to be set.
     */
    public void setCourseNumber(String courseNumber)
    {
        this.courseNumber = courseNumber;
    }

    /**
     * Sets the course website for this course.
     *
     * @param courseWebsite The course website to be set.
     */
    public void setCourseWebsite(String courseWebsite)
    {
        this.courseWebsite = courseWebsite;
    }

    /**
     * Sets the lab website for this course.
     *
     * @param courseWebsite The lab website to be set.
     */
    public void setLabWebsite(String labWebsite)
    {
        this.labWebsite = labWebsite;
    }

    /**
     * Sets the lab number of this course.
     *
     * @param courseNumber The lab number to be set.
     */
    public void setLabNumber(String labNumber)
    {
        this.labNumber = labNumber;
    }

    /**
     * Retrieves the course number of this course.
     */
    public String getCourseNumber()
    {
        return courseNumber;
    }

    /**
     * Retrieves the lab number of this course.
     */
    public String getLabNumber()
    {
        return labNumber;
    }

    /**
     * Retrieves the course website of this course.
     */
    public String getCourseWebsite()
    {
        return courseWebsite;
    }

    /**
     * Retrieves the course website of this course.
     */
    public String getLabWebsite()
    {
        return labWebsite;
    }

    /**
     * Retrieves the lab room location of this lab.
     *
     * @return The lab room location of this course.
     */
    public String getLabRoomLocation()
    {
        return labRoomLocation;
    }

    /**
     * Sets the lab room location of this course.
     *
     * @param labRoomLocation The lab room location to be set.
     */
    public void setLabRoomLocation(String labRoomLocation)
    {
        this.labRoomLocation = labRoomLocation;
    }

    /**
     * Retrieves the instructor's name for this course.
     *
     * @return The instructor's name for this course.
     */
    public String getInstructorName()
    {
        return instructorName;
    }

    /**
     * Sets the instructor's name for this course.
     *
     * @param instructorName The instructor's name to be set.
     */
    public void setInstructorName(String instructorName)
    {
        this.instructorName = instructorName;
    }

    /**
     * Retrieves the instructor's email for this course.
     *
     * @return The instructor's email for this course.
     */
    public String getInstructorEmail()
    {
        return instructorEmail;
    }

    /**
     * Sets the instructor's email for this course.
     *
     * @param instructorEmail The instructor's email to be set.
     */
    public void setInstructorEmail(String instructorEmail)
    {
        this.instructorEmail = instructorEmail;
    }

    /**
     * Retrieves the instructor's phone for this course.
     *
     * @return The instructor's phone for this course.
     */
    public String getInstructorPhone()
    {
        return instructorPhone;
    }

    /**
     * Sets the instructor's phone for this course.
     *
     * @param instructorPhone The instructor's phone to be set.
     */
    public void setInstructorPhone(String instructorPhone)
    {
        this.instructorPhone = instructorPhone;
    }

    /**
     * Retrieves the office location of the instructor for this course.
     *
     * @return The office location of the instructor.
     */
    public String getOfficeLocation()
    {
        return officeLocation;
    }

    /**
     * Sets the office location of the instructor for this course.
     *
     * @param officeLocation The office location to be set.
     */
    public void setOfficeLocation(String officeLocation)
    {
        this.officeLocation = officeLocation;
    }

    /**
     * Retrieves the office hours of the instructor for this course.
     *
     * @return The office hours of the instructor.
     */
    public String getOfficeHours()
    {
        return officeHours;
    }

    /**
     * Sets the office hours of the instructor for this course.
     *
     * @param officeHours Sets the office hours for this instructor.
     */
    public void setOfficeHours(String officeHours)
    {
        this.officeHours = officeHours;
    }

    /**
     * Retrieves the start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @return The start time of the course.
     */
    public String getStartTime(int index)
    {
        String time = "";
        switch (index)
        {
            case 0:
            {
                time = startHr;
                break;
            }
            case 1:
            {
                time = startMin;
                break;
            }
            case 2:
            {
                time = startM;
                break;
            }
        }
        return time;
    }

    /**
     * Sets the start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @param time The start time to be set.
     */
    public final void setStartTime(int index, String time)
    {
        switch (index)
        {
            case 0:
            {
                startHr = time;
                break;
            }
            case 1:
            {
                startMin = time;
                break;
            }
            case 2:
            {
                startM = time;
                break;
            }
        }
    }

    /**
     * Retrieves the lab's start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @return The lab's start time of the course.
     */
    public String getLabStartTime(int index)
    {
        String time = "";
        switch (index)
        {
            case 0:
            {
                time = labStartHr;
                break;
            }
            case 1:
            {
                time = labStartMin;
                break;
            }
            case 2:
            {
                time = labStartM;
                break;
            }
        }
        return time;
    }

    /**
     * Sets the lab's start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @param time The lab start time to be set.
     */
    public final void setLabStartTime(int index, String time)
    {
        switch (index)
        {
            case 0:
            {
                labStartHr = time;
                break;
            }
            case 1:
            {
                labStartMin = time;
                break;
            }
            case 2:
            {
                labStartM = time;
                break;
            }
        }
    }

    /**
     * Retrieves the end time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @return The end time of the course.
     */
    public String getEndTime(int index)
    {
        String time = "";
        switch (index)
        {
            case 0:
            {
                time = endHr;
                break;
            }
            case 1:
            {
                time = endMin;
                break;
            }
            case 2:
            {
                time = endM;
                break;
            }
        }
        return time;
    }

    /**
     * Sets the end time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @param time The end time to be set.
     */
    public final void setEndTime(int index, String time)
    {
        switch (index)
        {
            case 0:
            {
                endHr = time;
                break;
            }
            case 1:
            {
                endMin = time;
                break;
            }
            case 2:
            {
                endM = time;
                break;
            }
        }
    }

    /**
     * Retrieves the lab's start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @return The lab's start time of the course.
     */
    public String getLabEndTime(int index)
    {
        String time = "";
        switch (index)
        {
            case 0:
            {
                time = labEndHr;
                break;
            }
            case 1:
            {
                time = labEndMin;
                break;
            }
            case 2:
            {
                time = labEndM;
                break;
            }
        }
        return time;
    }

    /**
     * Sets the lab's start time of the course.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @param time The lab start time to be set.
     */
    public final void setLabEndTime(int index, String time)
    {
        switch (index)
        {
            case 0:
            {
                labEndHr = time;
                break;
            }
            case 1:
            {
                labEndMin = time;
                break;
            }
            case 2:
            {
                labEndM = time;
                break;
            }
        }
    }

    /**
     * Retrieves the credits of the course.
     *
     * @return The credits of the course.
     */
    public String getCredits()
    {
        return credits;
    }

    /**
     * Sets the credits of the course.
     *
     * @param credits The credits to be set.
     */
    public void setCredits(String credits)
    {
        this.credits = credits;
    }

    /**
     * Retrieves the lab credits of the course.
     *
     * @return The lab credits of the course.
     */
    public String getLabCredits()
    {
        return labCredits;
    }

    /**
     * Sets the lab credits of the course.
     *
     * @param labCredits The lab credits to be set.
     */
    public void setLabCredits(String labCredits)
    {
        this.labCredits = labCredits;
    }

    /**
     * Checks if the course is an online course or not.
     *
     * @return True if the course is online, false otherwise.
     */
    public boolean isOnline()
    {
        return isOnline;
    }

    /**
     * Sets the online state of the course.
     *
     * @param state True if the course is online, false otherwise.
     */
    public void setIsOnline(boolean state)
    {
        isOnline = state;
    }

    /**
     * Checks if the course has a lab or not.
     *
     * @return True if the course has a lab, false otherwise.
     */
    public boolean hasLab()
    {
        return hasLab;
    }

    /**
     * Sets whether the course has a lab or not.
     *
     * @param state True if the course has a lab, false otherwise.
     */
    public void setHasLab(boolean state)
    {
        hasLab = state;
    }

    /**
     * Sets whether the lab is online or not.
     *
     * @return True if the lab is online, false otherwise.
     */
    public boolean labIsOnline()
    {
        return labIsOnline;
    }

    /**
     * Retrieves whether the lab is online or not.
     *
     * @param state True if the lab is online, false otherwise.
     */
    public void setLabIsOnline(boolean state)
    {
        labIsOnline = state;
    }

    /**
     * Sets the color for this course.
     *
     * @param color The color to be set for this course.
     */
    public final void setColor(Color color)
    {
        this.color = color;
    }

    /**
     * Retrieves the color for this course.
     *
     * @return The color for this course.
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Swaps two types within the course at the two given indeces.
     *
     * @param first The first index to swap.
     * @param second The second index to swap
     */
    public void swapTypes(int first, int second)
    {
        AssignmentType temp = types.get (first);
        types.set (first, types.get (second));
        types.set (second, temp);
    }

    /**
     * Swaps two instructors within the course at the two given indeces.
     *
     * @param first The first index to swap.
     * @param second The second index to swap
     */
    public void swapInstructors(int first, int second)
    {
        Instructor temp = instructors.get (first);
        instructors.set (first, instructors.get (second));
        instructors.set (second, temp);
    }

    /**
     * Swaps two textbooks within the course at the two given indeces.
     *
     * @param first The first index to swap.
     * @param second The second index to swap
     */
    public void swapTextbooks(int first, int second)
    {
        Textbook temp = textbooks.get (first);
        textbooks.set (first, textbooks.get (second));
        textbooks.set (second, temp);
    }

    /**
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    public String out()
    {
        return getTypeName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + getUniqueID () + SEPARATOR
               + startDate + SEPARATOR
               + endDate + SEPARATOR
               + getTerm ().getUniqueID () + SEPARATOR
               + daysOfWeek[0] + SEPARATOR
               + daysOfWeek[1] + SEPARATOR
               + daysOfWeek[2] + SEPARATOR
               + daysOfWeek[3] + SEPARATOR
               + daysOfWeek[4] + SEPARATOR
               + daysOfWeek[5] + SEPARATOR
               + daysOfWeek[6] + SEPARATOR
               + roomLocation.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + instructorName.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + instructorEmail.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + officeHours.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + officeLocation.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + credits + SEPARATOR
               + startHr + SEPARATOR
               + endHr + SEPARATOR
               + instructorPhone.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + isOnline + SEPARATOR
               + color.getRed () + "-" + color.getGreen () + "-" + color.getBlue () + SEPARATOR
               + hasLab + SEPARATOR
               + labIsOnline + SEPARATOR
               + labRoomLocation.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + labStartDate + SEPARATOR
               + labEndDate + SEPARATOR
               + labStartHr + SEPARATOR
               + labEndHr + SEPARATOR
               + labCredits + SEPARATOR
               + startMin + SEPARATOR
               + startM + SEPARATOR
               + endMin + SEPARATOR
               + endM + SEPARATOR
               + labStartMin + SEPARATOR
               + labStartM + SEPARATOR
               + labEndMin + SEPARATOR
               + labEndM + SEPARATOR
               + labDaysOfWeek[0] + SEPARATOR
               + labDaysOfWeek[1] + SEPARATOR
               + labDaysOfWeek[2] + SEPARATOR
               + labDaysOfWeek[3] + SEPARATOR
               + labDaysOfWeek[4] + SEPARATOR
               + labDaysOfWeek[5] + SEPARATOR
               + labDaysOfWeek[6] + SEPARATOR
               + courseNumber.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + labNumber.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + courseWebsite.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + labWebsite.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE);
    }
}
