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

package adl.go.types;

import adl.go.gui.Domain;
import adl.go.resource.LocalUtility;
import adl.go.resource.Utility;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * An assignment is owned by a course and can be attached to a type or textbook
 * that is also owned by the same course. An assignment can eventually be marked
 * as completed, and when it is completed its grade can be accessed. The
 * assignment must be attached to a type to properly define its weight in the
 * grading process.
 *
 * @author Alex Laird
 */
public class Assignment extends ExtendedJPanelForAssignment implements ListItem
{
    /**
     * A reference to the utility is needed for coloring.
     */
    private Utility utility;
    /**
     * A reference to the containing course.
     */
    private Course course;
    /**
     * The textbook this assignment uses.
     */
    private Textbook textbook;
    /**
     * The type this assignment is tied to.
     */
    private AssignmentType type;
    /**
     * The due date for the assignment.
     */
    private String dueDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The due hour for the assignment.
     */
    private String hr = "12";
    /**
     * The due minute for the assignment.
     */
    private String min = "00";
    /**
     * The due meridian for the assignment.
     */
    private String m = "PM";
    /**
     * The grade for the assignment.
     */
    private String grade = "";
    /**
     * The comments for the assignment.
     */
    private String comments = "";
    /**
     * The row object for use in the assignmentsAndEvents table.
     */
    private Object[] rowObject = new Object[]
    {
        null, null, null, null, null, null, null
    };
    /**
     * The unique ID of the course this assignment is attached to.
     */
    protected long courseID;
    /**
     * The unique ID for the type of this course.
     */
    protected long typeID;
    /**
     * The unique ID for the textbook of this course.
     */
    protected long textbookID;
    /**
     * The priority of this assignment.
     */
    private int priority = 3;
    /**
     * True if the assignment is completed, false otherwise.
     */
    private boolean isDone = false;
    /**
     * True if an immediate save is needed after application startup.
     */
    private boolean immediateSaveNeeded = false;

    /**
     * Constructs an assignment with a given name, unique ID, and reference to
     * its containing course.
     *
     * @param name The name of the assignment.
     * @param id A unique ID not used by any other type in the application.
     * @param course A reference to the containing course.
     * @param utility A reference to the utility is needed for coloring.
     */
    public Assignment(String name, long id, Course course, LocalUtility utility)
    {
        super (name, id, utility);
        setCourse (course);
        hr = course.getStartTime (0);
        min = course.getStartTime (1);
        m = course.getStartTime (2);
        this.utility = utility;
        refreshRowObject ();
        refreshText ();
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility A reference to the utility is needed for coloring.
     */
    public Assignment(String parse, LocalUtility utility)
    {
        super ("", -1, utility);
        this.utility = utility;
        Scanner scan = new Scanner (parse).useDelimiter ("(?<!\\\\)" + SEPARATOR + "|" + "(?<!\\\\)" + END_OF_LINE);
        // throw away the true saying this is an assignment
        String throwAway = null;
        try
        {
            throwAway = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            if (throwAway != null && (throwAway.equals ("true") || throwAway.equals ("false")))
            {
                setItemName (scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE));
            }
            else
            {
                setItemName (throwAway);
            }
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            setUniqueID (scan.nextLong ());
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            courseID = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            typeID = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            textbookID = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            setIsDone (scan.nextBoolean ());
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            dueDate = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            grade = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            comments = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE).replaceAll ("\\\\<br />", LINE_RETURN);
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            priority = scan.nextInt ();
            if (priority > 5)
            {
                priority = (int) Math.round ((double) priority / 20);
            }
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            hr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        if (hr.contains (":"))
        {
            min = hr.split (":")[1].split (" ")[0];
            m = hr.split (":")[1].split (" ")[1];
            hr = hr.split (":")[0];
            immediateSaveNeeded = true;
        }
        else
        {
            try
            {
                min = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
            try
            {
                m = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
        }
        scan.close ();
    }

    /**
     * Check if the assignment needs an immediate save after startup.
     *
     * @return True if an immediate save is needed, false otherwise.
     */
    public boolean needsImmediateSaveNeeded()
    {
        return immediateSaveNeeded;
    }

    /**
     * Reset the needsImmedateSave flag.
     */
    public void resetNeedsImmediateSaveNeeded()
    {
        immediateSaveNeeded = false;
    }

    /**
     * Retrieves the unique ID of the course this assignment is attached to.
     *
     * @return The unique ID of this assignment's course.
     */
    public long getCourseID()
    {
        return courseID;
    }

    /**
     * Retrieves the unique ID of the type this assignment is attached to.
     *
     * @return The unique ID of this assignment's type.
     */
    public long getTypeID()
    {
        return typeID;
    }

    /**
     * Retrieves the unique ID of the textbook this assignment is attached to.
     *
     * @return The unique ID of this assignmentsAndEvents's textbook.
     */
    public long getTextbookID()
    {
        return textbookID;
    }

    /**
     * Refreshes the text of the component that is shown in Calendar View.
     */
    @Override
    public final void refreshText()
    {
        switch (utility.preferences.colorByIndex)
        {
            // color by due date
            case 0:
            {
                String[] assignmentDate = getDueDate ().split ("/");
                String[] currentDate = Domain.DATE_FORMAT.format (new Date ()).split ("/");
                try
                {
                    if (!isDone () && ((new Date ()).before (Domain.DATE_FORMAT.parse (getDueDate ()))
                                       || (Domain.DATE_FORMAT.format (new Date ()).equals (getDueDate ())))
                        && (Integer.parseInt (assignmentDate[2]) - Integer.parseInt (currentDate[2]) == 0
                            && Integer.parseInt (assignmentDate[0]) - Integer.parseInt (currentDate[0]) == 0))
                    {
                        if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 3)
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[5]);
                        }
                        else if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 2)
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[4]);
                        }
                        else
                        {
                            if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 1)
                            {
                                getLabel ().setForeground (utility.preferences.dueDateColors[3]);
                            }
                            else
                            {
                                if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 0)
                                {
                                    getLabel ().setForeground (utility.preferences.dueDateColors[2]);
                                }
                                else
                                {
                                    getLabel ().setForeground (utility.preferences.dueDateColors[0]);
                                }
                            }
                        }
                        getLabel ().setFont (utility.currentTheme.fontPlain11);
                    }
                    else
                    {
                        if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[1]);
                            getLabel ().setFont (utility.currentTheme.fontItalic11);
                        }
                        else
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[6]);
                            getLabel ().setFont (utility.currentTheme.fontPlain11);
                        }
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }

                break;
            }
            // color by course
            case 1:
            {
                getLabel ().setForeground (getCourse ().getColor ());
                try
                {
                    if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                    {
                        getLabel ().setFont (utility.currentTheme.fontItalic11);
                    }
                    else
                    {
                        getLabel ().setFont (utility.currentTheme.fontPlain11);
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }

                break;
            }
            // color by priority
            case 2:
            {
                try
                {
                    switch (priority)
                    {
                        case 5:
                        {
                            getLabel ().setForeground (utility.preferences.priorityColors[4]);
                            break;
                        }
                        case 4:
                        {
                            getLabel ().setForeground (utility.preferences.priorityColors[3]);
                            break;
                        }
                        case 2:
                        {
                            getLabel ().setForeground (utility.preferences.priorityColors[1]);
                            break;
                        }
                        case 1:
                        {
                            getLabel ().setForeground (utility.preferences.priorityColors[0]);
                            break;
                        }
                        case 3:
                        default:
                        {
                            getLabel ().setForeground (utility.preferences.priorityColors[2]);
                            break;
                        }
                    }

                    if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                    {
                        getLabel ().setFont (utility.currentTheme.fontItalic11);
                    }
                    else
                    {
                        getLabel ().setFont (utility.currentTheme.fontPlain11);
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                break;
            }
        }

        if (isDone)
        {
            getLabel ().setText ("<html><strike>" + getItemName () + "</strike></html>");
        }
        else
        {
            getLabel ().setText ("<html>" + getItemName () + "</html>");
        }
    }

    /**
     * Refreshes the row object with the values stored in this assignment object
     * to ensure they are all correct.
     */
    public final void refreshRowObject()
    {
        rowObject[0] = Boolean.valueOf (isDone);
        String openTags = "<html><b>";
        String closeTags = "</b></html>";
        switch (utility.preferences.colorByIndex)
        {
            // color by due date
            case 0:
            {
                String[] assignmentDate = getDueDate ().split ("/");
                String[] currentDate = Domain.DATE_FORMAT.format (new Date ()).split ("/");
                try
                {
                    if (!isDone () && ((new Date ()).before (Domain.DATE_FORMAT.parse (getDueDate ()))
                                       || (Domain.DATE_FORMAT.format (new Date ()).equals (getDueDate ())))
                        && (Integer.parseInt (assignmentDate[2]) - Integer.parseInt (currentDate[2]) == 0
                            && Integer.parseInt (assignmentDate[0]) - Integer.parseInt (currentDate[0]) == 0))
                    {
                        if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 3)
                        {
                            openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[5].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeTags;
                        }
                        else if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 2)
                        {
                            openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[4].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeTags;
                        }
                        else
                        {
                            if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 1)
                            {
                                openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[3].getRGB () & 0x00FFFFFF) + "\">";
                                closeTags = "</font>" + closeTags;
                            }
                            else
                            {
                                if (Integer.parseInt (assignmentDate[1]) - Integer.parseInt (currentDate[1]) == 0)
                                {
                                    openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[2].getRGB () & 0x00FFFFFF) + "\">";
                                    closeTags = "</font>" + closeTags;
                                }
                                else
                                {
                                    openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[0].getRGB () & 0x00FFFFFF) + "\">";
                                    closeTags = "</font>" + closeTags;
                                }
                            }
                        }
                    }
                    else
                    {
                        if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                        {
                            openTags += "<em><font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[1].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font></em>" + closeTags;
                        }
                        else
                        {
                            openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[6].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeTags;
                        }
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                if (isDone)
                {
                    openTags += "<strike>";
                    closeTags = "</strike>" + closeTags;
                }
                break;
            }
            // color by course
            case 1:
            {
                String openEmTags = "";
                String closeEmTags = "";
                try
                {
                    if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                    {
                        openEmTags += "<em>";
                        closeEmTags += "</em>";
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                String rgb = Integer.toHexString (getCourse ().getColor ().getRGB ());
                openTags += openEmTags + "<font color=\"#" + rgb.substring (2, rgb.length ()) + "\">";
                closeTags = "</font>" + closeEmTags + closeTags;
                if (isDone)
                {
                    openTags += "<strike>";
                    closeTags = "</strike>" + closeTags;
                }
                break;
            }
            // color by priority
            case 2:
            {
                String openEmTags = "";
                String closeEmTags = "";
                try
                {
                    if (!isDone () && (new Date ()).after (Domain.DATE_FORMAT.parse (getDueDate ())))
                    {
                        openEmTags += "<em>";
                        closeEmTags += "</em>";
                    }

                    switch (priority)
                    {
                        case 5:
                        {
                            openTags += openEmTags + "<font color=\"#" + Integer.toHexString (utility.preferences.priorityColors[4].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeEmTags + closeTags;
                            break;
                        }
                        case 4:
                        {
                            openTags += openEmTags + "<font color=\"#" + Integer.toHexString (utility.preferences.priorityColors[3].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeEmTags + closeTags;
                            break;
                        }
                        case 2:
                        {
                            openTags += openEmTags + "<font color=\"#" + Integer.toHexString (utility.preferences.priorityColors[1].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeEmTags + closeTags;
                            break;
                        }
                        case 1:
                        {
                            openTags += openEmTags + "<font color=\"#" + Integer.toHexString (utility.preferences.priorityColors[0].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeEmTags + closeTags;
                            break;
                        }
                        case 3:
                        default:
                        {
                            openTags += openEmTags + "<font color=\"#" + Integer.toHexString (utility.preferences.priorityColors[2].getRGB () & 0x00FFFFFF) + "\">";
                            closeTags = "</font>" + closeEmTags + closeTags;
                            break;
                        }
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                if (isDone)
                {
                    openTags += "<strike>";
                    closeTags = "</strike>" + closeTags;
                }
                break;
            }
        }
        rowObject[1] = openTags + getItemName () + closeTags;
        if (getType () != null)
        {
            rowObject[2] = getType ().getTypeName ();
        }
        else
        {
            rowObject[2] = "";
        }
        rowObject[3] = getCourse ().getTypeName ();
        rowObject[4] = getDueDate ();
        if (isDone ())
        {
            rowObject[5] = getGrade ();
        }
        else
        {
            rowObject[5] = "";
        }
        rowObject[6] = getUniqueID ();
    }

    /**
     * Retrieves the row object for use in the assignmentsAndEvents table.
     *
     * @return The row object.
     */
    @Override
    public Object[] getRowObject()
    {
        refreshRowObject ();
        return rowObject;
    }

    /**
     * Check if the assigment is completed or not.
     *
     * @return True if the assignment is done, false otherwise.
     */
    public boolean isDone()
    {
        return isDone;
    }

    /**
     * Checks if the assignment is overdue or not.
     *
     * @return True if the assignment is overdue, false otherwise.
     */
    public boolean isOverdue()
    {
        boolean overdue = false;

        try
        {
            if (!isDone
                && Domain.DATE_FORMAT.parse (dueDate).before (utility.domain.today))
            {
                overdue = true;
            }
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }

        return overdue;
    }

    /**
     * Returns the due time in h:mm a format.
     *
     * @return The due time string.
     */
    public String getDueTime()
    {
        return hr + ":" + min + " " + m;
    }

    /**
     * Retrieves the due time of the assignment.
     *
     * @param index 0 will return the hr, 1 will return the minute, 2 will
     * return the meridian.
     * @return The hour, minute, or meridian of the assignment.
     */
    public String getDueTime(int index)
    {
        String time = "";

        switch (index)
        {
            case 0:
            {
                time = hr;
                break;
            }
            case 1:
            {
                time = min;
                break;
            }
            case 2:
            {
                time = m;
                break;
            }
        }
        return time;
    }

    /**
     * Sets the due time of the assignment.
     *
     * @param index 0 is hours, 1 is minutes, 2 is meridian
     * @param time The due time to be set.
     */
    public void setDueTime(int index, String time)
    {
        switch (index)
        {
            case 0:
            {
                hr = time;
                break;
            }
            case 1:
            {
                min = time;
                break;
            }
            case 2:
            {
                m = time;
                break;
            }
        }
    }

    /**
     * Set the done state of this assignment to completed or non-completed.
     *
     * @param isDone True if the assignment should be set to done, false if it
     * should be set to not done.
     */
    public final void setIsDone(boolean isDone)
    {
        this.isDone = isDone;
        getCheckBox ().setSelected (isDone);
    }

    /**
     * Retrieve the reference to the containing course.
     *
     * @return The containing course.
     */
    public Course getCourse()
    {
        return course;
    }

    /**
     * Set the reference to the containing course.
     *
     * @param course The containing course.
     */
    public final void setCourse(Course course)
    {
        this.course = course;
    }

    /**
     * Retrieve the due date.
     *
     * @return The due date.
     */
    @Override
    public String getDueDate()
    {
        return dueDate;
    }

    /**
     * Set the due date.
     *
     * @param dueDate The due date to be set.
     */
    public void setDueDate(String dueDate)
    {
        this.dueDate = dueDate;
    }

    /**
     * Retrieve the grade.
     *
     * @return The grade.
     */
    public String getGrade()
    {
        return grade;
    }

    /**
     * Set the grade.
     *
     * @param grade The grade to be set.
     */
    public void setGrade(String grade)
    {
        this.grade = grade;
    }

    /**
     * Retrieves the comments.
     *
     * @return The comments for this assignment.
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * Sets the comments for this assignment.
     *
     * @param comment The comments to be set.
     */
    public void setComments(String comment)
    {
        this.comments = comment;
    }

    /**
     * Retrieves the priority of this assignment.
     *
     * @return The priority of the assignment.
     */
    public int getPriority()
    {
        return priority;
    }

    /**
     * Sets the priority of the assignment.
     *
     * @param priority The priority to be set.
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    /**
     * Retrieves the textbook object for this assignment.
     *
     * @return The textbook for this assignment.
     */
    public Textbook getTextbook()
    {
        return textbook;
    }

    /**
     * Sets the textbook of this assignment to the passed in textbook object.
     *
     * @param textbook The textbook to be set.
     */
    public void setTextbook(Textbook textbook)
    {
        this.textbook = textbook;
    }

    /**
     * Retrieves the type of this assignment.
     *
     * @return The type of this assignment.
     */
    public AssignmentType getType()
    {
        return type;
    }

    /**
     * Sets the type of this assignment to the passed in type object.
     *
     * @param type The type to be set.
     */
    public void setType(AssignmentType type)
    {
        this.type = type;
    }

    /**
     * Always returns true, because this is an assignment type.
     *
     * @return True because this is an assignment.
     */
    @Override
    public boolean isAssignment()
    {
        return true;
    }

    /**
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    @Override
    public String out()
    {
        String typeSeg = "-1";
        if (type != null)
        {
            typeSeg = type.getUniqueID () + "";
        }
        String textbookSeg = "-1";
        if (textbook != null)
        {
            textbookSeg = textbook.getUniqueID () + "";
        }
        return isAssignment () + SEPARATOR
               + getItemName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + getUniqueID () + SEPARATOR
               + getCourse ().getUniqueID () + SEPARATOR
               + typeSeg + SEPARATOR
               + textbookSeg + SEPARATOR
               + isDone + SEPARATOR
               + dueDate + SEPARATOR
               + grade + SEPARATOR
               + comments.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE).replaceAll (LINE_RETURN, "\\\\<br />") + SEPARATOR
               + priority + SEPARATOR
               + hr + SEPARATOR
               + min + SEPARATOR
               + m;
    }
}
