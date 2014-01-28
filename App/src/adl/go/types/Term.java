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
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A term spans a specific amount of time and contains references to all courses
 * that are within that are created within it. Assignments are then indirectly
 * added to terms through a course. Terms are necessary to create courses,
 * assignmentsAndEvents, or any other object within this application.
 * Additionally, terms are what grades are broken down under in the grades
 * dialog.
 *
 * @author Alex Laird
 */
public class Term extends ExtendedTreeNode
{
    /**
     * The start date of the term.
     */
    private String startDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The end date of the term.
     */
    private String endDate = Domain.DATE_FORMAT.format (new Date ());

    /**
     * Constructs a term with a name and a unique ID.
     *
     * @param name The name of the term.
     * @param id The unique ID of the term.
     * @param utility The reference to the utility.
     */
    public Term(String name, long id, Utility utility)
    {
        super (name, id, (LocalUtility) utility);
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility The reference to the utility.
     */
    public Term(String parse, LocalUtility utility)
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
            setUniqueID (scan.nextLong ());
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            setStartDate (scan.next ());
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            setEndDate (scan.next ());
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        scan.close ();
    }

    /**
     * Retrieve the number of days in the entire term.
     *
     * @return The number of days in the term.
     */
    public long getDayCount() throws ParseException
    {
        return ((Domain.DATE_FORMAT.parse (getEndDate ()).getTime () - Domain.DATE_FORMAT.parse (getStartDate ()).getTime ()) / (24 * 60 * 60 * 1000));
    }

    /**
     * Adds a course to the term.
     *
     * @param course A course to be added to the the term.
     */
    public void addCourse(Course course)
    {
        // add to the tree
        add (course);
    }

    /**
     * Removes the specified course from the term.
     *
     * @param course Removes this course from the term.
     * @return The removed course.
     */
    public Course removeCourse(Course course)
    {
        remove (course);
        return course;
    }

    /**
     * Retrieves the course at the specified index.
     *
     * @param i The index of the course to select.
     * @return The course at the given index.
     */
    public Course getCourse(int i)
    {
        return (Course) getChildAt (i);
    }

    /**
     *
     * @param course
     * @return
     */
    public int getCourseIndex(Course course)
    {
        for (int i = 0; i < getChildCount (); ++i)
        {
            if (getChildAt (i) == course)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * True if this term contains the specified course.
     *
     * @param course The course to check for.
     * @return True if it is in this term, false otherwise.
     */
    public boolean hasCourse(Course course)
    {
        for (int i = 0; i < getChildCount (); ++i)
        {
            if (getChildAt (i) == course)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a reference to the course with the specified name if it is
     * contained in this term.
     *
     * @param courseName The name of the course to retrieve.
     * @return An object for the course, or null if the course was not found.
     */
    public Course getCourseByName(String courseName)
    {
        for (int i = 0; i < getChildCount (); ++i)
        {
            if (((Course) getChildAt (i)).getTypeName ().equals (courseName))
            {
                return (Course) getChildAt (i);
            }
        }

        return null;
    }

    /**
     * Retrieves the total number of lecture and lab hours for this course.
     *
     * @return The total number of lecture and lab hours for this course.
     */
    public double getCreditCount()
    {
        double total = 0;
        for (int i = 0; i < getCourseCount (); ++i)
        {
            total += Double.parseDouble (getCourse (i).getCredits ());
            if (getCourse (i).hasLab ())
            {
                total += Double.parseDouble (getCourse (i).getLabCredits ());
            }
        }
        return total;
    }

    /**
     * Returns the number of courses in this term.
     *
     * @return The number of courses in this term.
     */
    public int getCourseCount()
    {
        return getChildCount ();
    }

    /**
     * Retrieves the number of unfinished assignments in this term.
     *
     * @return The number of unfinished assignments in this term.
     */
    public int getUnfinishedAssignmentCount()
    {
        int total = 0;
        for (int i = 0; i < getCourseCount (); ++i)
        {
            total += getCourse (i).getUnfinishedAssignmentCount ();
        }
        return total;
    }

    /**
     * Retrieves the number of assignments in this term.
     *
     * @return The number of assignments in this term.
     */
    public int getAssignmentCount()
    {
        int total = 0;
        for (int i = 0; i < getCourseCount (); ++i)
        {
            total += getCourse (i).getAssignmentCount ();
        }
        return total;
    }

    /**
     * Retrieves the number of textbooks in this term.
     *
     * @return The number of textbooks in this term.
     */
    public int getTextbooksCount()
    {
        int total = 0;
        for (int i = 0; i < getCourseCount (); ++i)
        {
            total += getCourse (i).getTextbookCount ();
        }
        return total;
    }

    /**
     * Retrieves the number of types in this term.
     *
     * @return The number of types in this term.
     */
    public int getTypesCount()
    {
        int total = 0;
        for (int i = 0; i < getCourseCount (); ++i)
        {
            total += getCourse (i).getTypeCount ();
        }
        return total;
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
     * Sets the end date for the term.
     *
     * @param endDate The end date for the term to be set.
     */
    public final void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * Swap the two courses at the given indeces.
     *
     * @param first The first course to swap with the second.
     * @param second The second course to swap with the first.
     */
    public void swapCourses(int first, int second)
    {
        insert ((Course) getChildAt (second), first);
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
               + endDate;
    }
}
