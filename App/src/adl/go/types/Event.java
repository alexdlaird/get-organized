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
 * An event has no owner and simply requires a start date, end date, and event
 * details.
 *
 * @author Alex Laird
 */
public class Event extends ExtendedJPanelForEvent implements ListItem
{
    /**
     * The reference to the utility for coloring.
     */
    private Utility utility;
    /**
     * The object for the year to which this event belongs.
     */
    private EventYear eventYear;
    /**
     * The unique name of the category.
     */
    private String categoryName;
    /**
     * The category of the event.
     */
    private Category category;
    /**
     * The date of the event.
     */
    private String date = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The start hour of the event.
     */
    private String startHr = "12";
    /**
     * The start minute of the event.
     */
    private String startMin = "00";
    /**
     * The start meridian of the event.
     */
    private String startM = "PM";
    /**
     * The end hour of the event.
     */
    private String endHr = "12";
    /**
     * The end minute of the event.
     */
    private String endMin = "00";
    /**
     * The end meridian of the event.
     */
    private String endM = "PM";
    /**
     * The location of the event.
     */
    private String location = "";
    /**
     * The description for the event.
     */
    private String description = "";
    /**
     * The string for the repetition of the event.
     */
    private Repeating repeating = new Repeating ();
    /**
     * The row object for use in the assignmentsAndEvents table.
     */
    private Object[] rowObject = new Object[]
    {
        null, null, null, null, null, null, null
    };
    /**
     * True if the event is all day, false otherwise.
     */
    private boolean isAllDay = false;
    /**
     * True if an immediate save is needed after application startup.
     */
    private boolean immediateSaveNeeded = false;

    /**
     * Constructs an event with a given name, unique ID, and reference to its
     * containing course.
     *
     * @param name The name of the event.
     * @param id A unique ID not used by any other type in the application.
     * @param utility A reference to the utility is needed for coloring.
     */
    public Event(String name, long id, LocalUtility utility, EventYear eventYear)
    {
        super (name, id, utility);
        this.utility = utility;
        this.eventYear = eventYear;
        category = utility.preferences.categories.get (0);
        refreshRowObject ();
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility A reference to the utility is needed for coloring.
     */
    public Event(String parse, LocalUtility utility)
    {
        super ("", -1, utility);
        this.utility = utility;
        Scanner scan = new Scanner (parse).useDelimiter ("(?<!\\\\)" + SEPARATOR + "|" + "(?<!\\\\)" + END_OF_LINE);
        // throw away the false saying this is an event
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
            date = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            startHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            endHr = scan.next ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            isAllDay = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            location = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            description = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE).replaceAll ("\\\\<br />", LINE_RETURN);
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            categoryName = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            repeating.id = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }
        try
        {
            setRepeating (scan.next ());
        }
        catch (NoSuchElementException ex)
        {
            immediateSaveNeeded = true;
        }

        if (startHr.contains (":"))
        {
            startMin = startHr.split (":")[1].split (" ")[0];
            startM = startHr.split (":")[1].split (" ")[1];
            startHr = startHr.split (":")[0];
            immediateSaveNeeded = true;
        }
        else
        {
            try
            {
                startMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
            try
            {
                startM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
        }
        if (endHr.contains (":"))
        {
            endMin = endHr.split (":")[1].split (" ")[0];
            endM = endHr.split (":")[1].split (" ")[1];
            endHr = endHr.split (":")[0];
            immediateSaveNeeded = true;
        }
        else
        {
            try
            {
                endMin = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
            try
            {
                endM = scan.next ();
            }
            catch (NoSuchElementException ex)
            {
                immediateSaveNeeded = true;
            }
        }

        scan.close ();
    }

    /**
     * Retrieve the object reference to this event's event year.
     *
     * @return The event year of this event.
     */
    public EventYear getEventYear()
    {
        return eventYear;
    }

    /**
     * Set the event year object for this event.
     *
     * @param eventYear The event year to be set.
     */
    public void setEventYear(EventYear eventYear)
    {
        this.eventYear = eventYear;
    }

    /**
     * Check if the event needs an immediate save after startup.
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
     * Refreshes the text of the component that is shown in Calendar View.
     */
    @Override
    public void refreshText()
    {
        switch (utility.preferences.colorByIndex)
        {
            // color by due date
            case 0:
            {
                String[] eventDate = getDueDate ().split ("/");
                String[] currentDate = Domain.DATE_FORMAT.format (new Date ()).split ("/");
                try
                {
                    if (((new Date ()).before (Domain.DATE_FORMAT.parse (getDueDate ()))
                         || (Domain.DATE_FORMAT.format (new Date ()).equals (getDueDate ())))
                        && (Integer.parseInt (eventDate[2]) - Integer.parseInt (currentDate[2]) == 0
                            && Integer.parseInt (eventDate[0]) - Integer.parseInt (currentDate[0]) == 0))
                    {
                        if (Integer.parseInt (eventDate[1]) - Integer.parseInt (currentDate[1]) == 3)
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[5]);
                        }
                        else if (Integer.parseInt (eventDate[1]) - Integer.parseInt (currentDate[1]) == 2)
                        {
                            getLabel ().setForeground (utility.preferences.dueDateColors[4]);
                        }
                        else
                        {
                            if (Integer.parseInt (eventDate[1]) - Integer.parseInt (currentDate[1]) == 1)
                            {
                                getLabel ().setForeground (utility.preferences.dueDateColors[3]);
                            }
                            else
                            {
                                if (Integer.parseInt (eventDate[1]) - Integer.parseInt (currentDate[1]) == 0)
                                {
                                    getLabel ().setForeground (utility.preferences.dueDateColors[2]);
                                }
                                else
                                {
                                    getLabel ().setForeground (utility.preferences.dueDateColors[0]);
                                }
                            }
                        }
                    }
                    else
                    {
                        getLabel ().setForeground (utility.preferences.dueDateColors[0]);
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }

                break;
            }
            // color by category
            case 1:
            {
                getLabel ().setForeground (getCategory ().getColor ());
                break;
            }
            // color by priority
            case 2:
            {
                getLabel ().setForeground (getCategory ().getColor ());
                break;
            }
        }

        setItemName (getItemName ());
    }

    /**
     * Retrieves the category this event is attached to.
     *
     * @return The category this assignment is attached to.
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * Retrieves the name of the category this event is attached to.
     *
     * @return The name of the category this assignment is attached to.
     */
    public String getCategoryName()
    {
        if (categoryName == null)
        {
            categoryName = "Default";
        }
        return categoryName;
    }

    /**
     * Sets the category for this event.
     *
     * @param category The category to be set.
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }

    /**
     * Refreshes the row object with the values stored in this event object to
     * ensure they are all correct.
     */
    public final void refreshRowObject()
    {
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
                    if (((new Date ()).before (Domain.DATE_FORMAT.parse (getDueDate ()))
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
                        openTags += "<font color=\"#" + Integer.toHexString (utility.preferences.dueDateColors[0].getRGB () & 0x00FFFFFF) + "\">";
                        closeTags = "</font>" + closeTags;
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }

                break;
            }
            // color by category
            case 1:
            {
                String rgb = Integer.toHexString (getCategory ().getColor ().getRGB ());
                openTags += "<font color=\"#" + rgb.substring (2, rgb.length ()) + "\">";
                closeTags = "</font>" + closeTags;
                break;
            }
            // color by priority
            case 2:
            {
                String rgb = Integer.toHexString (getCategory ().getColor ().getRGB ());
                openTags += "<font color=\"#" + rgb.substring (2, rgb.length ()) + "\">";
                closeTags = "</font>" + closeTags;
                break;
            }
        }
        if (!isAllDay ())
        {
            rowObject[1] = openTags + "(" + startHr + ":" + startMin + " " + startM + ") " + getItemName () + closeTags;
        }
        else
        {
            rowObject[1] = openTags + getItemName () + closeTags;
        }
        rowObject[2] = "Event";
        rowObject[3] = getCategoryName ();
        rowObject[4] = getDueDate ();
        rowObject[5] = "---";
        rowObject[6] = getUniqueID ();
    }

    /**
     * Set the name of the event with the given name.
     *
     * @param name The name to set the event with.
     */
    @Override
    public final void setItemName(String name)
    {
        super.setItemName (name);
        if (!isAllDay ())
        {
            getLabel ().setText ("<html>(" + startHr + ":" + startMin + " " + startM + ") " + name + "</html>");
        }
        else
        {
            getLabel ().setText ("<html>" + name + "</html>");
        }
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
     * Retrieve the date.
     *
     * @return The date.
     */
    @Override
    public String getDueDate()
    {
        return date;
    }

    /**
     * Retrieves if the event is all day or not.
     *
     * @return True if the event is all day, false otherwise.
     */
    public boolean isAllDay()
    {
        return isAllDay;
    }

    /**
     * Sets the all day state of the event
     *
     * @param isAllDay True if the event should be all day, false otherwise.
     */
    public void setIsAllDay(boolean isAllDay)
    {
        this.isAllDay = isAllDay;
    }

    /**
     * Set the repeating object for the event with the given string.
     *
     * @param repeatingString The repeating string for the event.
     */
    public final void setRepeating(String repeatingString)
    {
        if (!repeatingString.equals (""))
        {
            String[] split = repeatingString.split ("-");
            repeating.repeatsIndex = Integer.parseInt (split[0]);
            repeating.repeatsEveryIndex = Integer.parseInt (split[1]);
            repeating.sunday = Boolean.valueOf (split[2]);
            repeating.monday = Boolean.valueOf (split[3]);
            repeating.tuesday = Boolean.valueOf (split[4]);
            repeating.wednesday = Boolean.valueOf (split[5]);
            repeating.thursday = Boolean.valueOf (split[6]);
            repeating.friday = Boolean.valueOf (split[7]);
            repeating.saturday = Boolean.valueOf (split[8]);
            repeating.startDate = split[9];
            repeating.endDate = split[10];
        }
    }

    /**
     * Retrieve the repeating object for the event.
     *
     * @return The repeating object for the event.
     */
    public Repeating getRepeating()
    {
        return repeating;
    }

    /**
     * Set the date.
     *
     * @param date The date to be set.
     */
    public void setDate(String date, LocalUtility utility)
    {
        this.date = date;
        setEventYear (utility.getEventYear (date.split ("/")[2]));
    }

    /**
     * Sets the start time of the event.
     *
     * @param index 0 will return the hr, 1 will return the minute, 2 will
     * return the meridian.
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
        refreshText ();
    }

    /**
     * Retrieves the start time of the event.
     *
     * @param index 0 will return the hr, 1 will return the minute, 2 will
     * return the meridian.
     * @return The start time of the event.
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
     * Sets the end time of the event.
     *
     * @param index 0 will return the hr, 1 will return the minute, 2 will
     * return the meridian.
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
     * Retrieves the end time of the event.
     *
     * @param index 0 will return the hr, 1 will return the minute, 2 will
     * return the meridian.
     * @return The end time of the event.
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
     * Retrieves the event location.
     *
     * @return The location of the event.
     */
    public String getEventLocation()
    {
        return location;
    }

    /**
     * Sets the event location.
     *
     * @param location The location of the event to be set.
     */
    public void setEventLocation(String location)
    {
        this.location = location;
    }

    /**
     * Retrieves the description.
     *
     * @return The description of the event.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description for this event.
     *
     * @param description The description to be set.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Always returns false, because an event is not an assignment.
     *
     * @return False because this is an event, not an assignment.
     */
    @Override
    public boolean isAssignment()
    {
        return false;
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
        return isAssignment () + SEPARATOR
               + getItemName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + getUniqueID () + SEPARATOR
               + date + SEPARATOR
               + startHr + SEPARATOR
               + endHr + SEPARATOR
               + isAllDay + SEPARATOR
               + location.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + description.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE).replaceAll (LINE_RETURN, "\\\\<br />") + SEPARATOR
               + getCategory ().getName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + repeating.id + SEPARATOR
               + repeating + SEPARATOR
               + startMin + SEPARATOR
               + startM + SEPARATOR
               + endMin + SEPARATOR
               + endM;
    }
}
