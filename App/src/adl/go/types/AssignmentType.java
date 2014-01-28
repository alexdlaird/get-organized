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
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A type is tied directly to a course and can be tired indirectly to individual
 * assignmentsAndEvents within that course. It is necessary for that
 * assignment/course to be included when grades are viewed.
 *
 * @author Alex Laird
 */
public class AssignmentType extends ExtendedTreeNode
{
    /**
     * The course this type is attached to.
     */
    private Course course;
    /**
     * The weight of this type in the course.
     */
    private String weight = "";
    /**
     * The grade for this type.
     */
    private double grade = -1;
    /**
     * The number of assignments reporting a grade for this type.
     */
    private int gradeCount = 0;
    /**
     * The unique ID of the course this type is attached to.
     */
    protected long courseID;

    /**
     * Constructs a new type.
     *
     * @param name The name of the type.
     * @param id The unique ID of the type.
     * @param course The course this type is attached to.
     * @param utility The reference to the utility.
     */
    public AssignmentType(String name, long id, Course course, LocalUtility utility)
    {
        super (name, id, utility);
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
    public AssignmentType(String parse, LocalUtility utility)
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
            weight = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        scan.close ();
    }

    /**
     * Retrieves the unique ID of the course this type is attached to.
     *
     * @return The unique ID of this type's course.
     */
    public long getCourseID()
    {
        return courseID;
    }

    /**
     * Retrieves the course this type is attached to.
     *
     * @return The course this type is attached to.
     */
    public Course getCourse()
    {
        return course;
    }

    /**
     * Sets the course this type is attached to.
     *
     * @param course The course to set for this type.
     */
    public final void setCourse(Course course)
    {
        this.course = course;
    }

    /**
     * Sets the weight of this type.
     *
     * @param weight The weight to be set.
     */
    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    /**
     * Retrieves the weight of this course.
     *
     * @return The weight of this course.
     */
    public String getWeight()
    {
        return weight;
    }

    /**
     * Retrieve the grade for this type.
     *
     * @return The grade for this type.
     */
    public double getGrade()
    {
        return grade;
    }

    /**
     * Add the given grade to this type weight's grade.
     *
     * @param grade The new grade to add to the current weight.
     */
    public void addGrade(double grade)
    {
        if (grade == -1)
        {
            grade = 0;
        }
        ++gradeCount;
        if (gradeCount == 1)
        {
            this.grade = grade;
        }
        else
        {
            this.grade = ((this.grade + grade) / 2);
        }
    }

    /**
     * Reset the types cumulative grade.
     */
    public void resetGrade()
    {
        grade = -1;
        gradeCount = 0;
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
               + weight.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE);
    }
}
