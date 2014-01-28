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

import adl.go.resource.LocalUtility;
import adl.go.resource.Utility;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * An instructor is attached to a course.
 *
 * @author Alex Laird
 */
public class Instructor extends ExtendedTreeNode
{
    /**
     * The course this instructor is attached to.
     */
    private Course course;
    /**
     * The unique ID of the course this instructor is attached to.
     */
    protected long courseID = -1;
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
     * The office location of the instructor.
     */
    private String instructorPhone = "";
    /**
     * Whether the instructor teaches the lecture or the lab.
     */
    private String lectureLab = "Lecture";

    /**
     * Constructs a new instructor.
     *
     * @param name The name of the instructor.
     * @param id The unique ID of the instructor.
     * @param course The course this instructor is attached to.
     * @param utility The reference to the utility.
     */
    public Instructor(String name, long id, Course course, Utility utility)
    {
        super (name, id, (LocalUtility) utility);
        setCourse (course);
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility The reference to the utility.
     */
    public Instructor(String parse, LocalUtility utility)
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
            courseID = scan.nextLong ();
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
            instructorPhone = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            lectureLab = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        scan.close ();
    }

    /**
     * Retrieves the unique ID of the course this instructor is attached to.
     *
     * @return The unique ID of this instructor's course.
     */
    public long getCourseID()
    {
        return courseID;
    }

    /**
     * Retrieves the course this instructor is attached to.
     *
     * @return The course this instructor is attached to.
     */
    public Course getCourse()
    {
        return course;
    }

    /**
     * Sets the course this instructor is attached to.
     *
     * @param course The course to set for this instructor.
     */
    public final void setCourse(Course course)
    {
        this.course = course;
    }

    /**
     *
     * @return
     */
    public String getLectureLab()
    {
        return lectureLab;
    }

    /**
     *
     * @param lectureLab
     */
    public void setLectureLab(String lectureLab)
    {
        this.lectureLab = lectureLab;
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
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    public String out()
    {
        return getTypeName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + getUniqueID () + SEPARATOR + getCourse ().getUniqueID () + SEPARATOR
               + instructorEmail.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + officeHours.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + officeLocation.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + instructorPhone.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + lectureLab;
    }
}
