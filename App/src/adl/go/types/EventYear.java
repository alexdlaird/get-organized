/*
 * Get Organized - Organize your schedule, course events, and grades
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This object contains all events within a specific year.
 *
 * @author Alex Laird
 */
public class EventYear
{
    /**
     * The year of this object.
     */
    private String year;
    /**
     * A list of all events for this course.
     */
    private ArrayList<Event> events = new ArrayList<Event> ();
    /**
     * True if this event year has event changes, false otherwise.
     */
    private boolean eventChanged = false;
    /**
     * The events data file.
     */
    private File eventsFile;
    private File oldEventsFile;
    /**
     * The events data file writer.
     */
    private BufferedWriter outEvent;
    /**
     * The events data file reader.
     */
    private BufferedReader inEvent;

    /**
     * Construct a new event year object with the given year.
     *
     * @param year The year to construct the object for.
     * @param utility A reference to the utility class.
     */
    public EventYear(String year, Utility utility)
    {
        this.year = year;
        if (utility instanceof LocalUtility)
        {
            eventsFile = new File (((LocalUtility) utility).getDataFolder (), "e" + year + ".dat");
            oldEventsFile = new File (((LocalUtility) utility).getAppDataFolder (), "e" + year + ".dat");
        }
    }

    /**
     * Add the given event to this event year.
     *
     * @param event The event to be added.
     */
    public void addEvent(Event event)
    {
        events.add (event);
    }

    /**
     * Check of the given event exists in this event year.
     *
     * @param event The event to check for.
     * @return True if the event is contained in this event year, false
     * otherwise.
     */
    public boolean hasEvent(Event event)
    {
        for (int i = 0; i < events.size (); ++i)
        {
            if (events.get (i) == event)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Remove the given event from this event year.
     *
     * @param event The event to be removed.
     * @return The removed event.
     */
    public Event removeEvent(Event event)
    {
        events.remove (event);
        if (events.isEmpty ())
        {
            markForDeletion (true);
        }
        return event;
    }

    /**
     * Delete the associated assignments file for this course.
     *
     * @param delNew True to delete the new events file, false to delete the
     * old.
     */
    public void markForDeletion(boolean delNew)
    {
        if (delNew)
        {
            eventsFile.delete ();
        }
        else
        {
            if (oldEventsFile.exists ())
            {
                oldEventsFile.delete ();
            }
        }
    }

    /**
     * Retrieve the year string for this event year.
     *
     * @return The year of this event year.
     */
    public String getYear()
    {
        return year;
    }

    /**
     * Events have now been saved, so set changed flag back.
     */
    public void saved()
    {
        eventChanged = false;
    }

    /**
     * Mark events as changed.
     */
    public void markChanged()
    {
        eventChanged = true;
    }

    /**
     * Check if assignment's have been changed within this course.
     *
     * @return True if events have changed, false otherwise.
     */
    public boolean isChanged()
    {
        return eventChanged;
    }

    /**
     * Retrieve the number of events contained within this event year.
     *
     * @return The number of events contained within this event year.
     */
    public int getEventCount()
    {
        return events.size ();
    }

    /**
     * Retrieve the event at the given index.
     *
     * @param i The index to retrieve from.
     * @return The event at the given index.
     */
    public Event getEvent(int i)
    {
        return events.get (i);
    }

    /**
     * Opens the events file writer.
     *
     * @return The newly opened writer.
     */
    public BufferedWriter openWriter() throws IOException
    {
        if (inEvent != null)
        {
            closeReader ();
        }
        if (outEvent != null)
        {
            closeWriter ();
        }
        outEvent = new BufferedWriter (new FileWriter (eventsFile));
        return outEvent;
    }

    /**
     * Create an event file for this event year.
     *
     * @param utility A reference to the utility class.
     */
    private void createEventsFile(LocalUtility utility)
    {
        utility.getDataFolder ().mkdir ();
        try
        {
            outEvent = new BufferedWriter (new FileWriter (eventsFile));

            closeWriter ();
        }
        catch (IOException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Load all events attached to this event years file.
     *
     * @param utility A reference to the utility class.
     * @param useNew False to use old data file path, true to use new.
     */
    public void loadEvents(LocalUtility utility, boolean useNew) throws IOException
    {
        // if the types file does not exist, create it with default values
        if (!eventsFile.exists ())
        {
            createEventsFile (utility);
        }

        // clear all old events and events data and load in terms from the data file
        if (useNew)
        {
            inEvent = openReader (eventsFile);
        }
        else
        {
            inEvent = openReader (oldEventsFile);
        }
        String line = inEvent.readLine ();
        while (line != null)
        {
            if (line.startsWith ("false"))
            {
                Event newEvent = new Event (line, utility);
                utility.assignmentsAndEvents.add (newEvent);
                if (newEvent.getRepeating ().getID () != -1)
                {
                    utility.repeatingEvents.add (newEvent);
                }
            }

            line = inEvent.readLine ();
        }
        closeReader ();
    }

    /**
     * Closes the events file writer.
     */
    public void closeWriter() throws IOException
    {
        if (outEvent != null)
        {
            outEvent.close ();
            outEvent = null;
        }
    }

    /**
     * Opens the events file reader.
     *
     * @param file The file to open a reader for.
     * @return The newly opened reader.
     */
    public BufferedReader openReader(File file) throws IOException
    {
        if (inEvent != null)
        {
            closeReader ();
        }
        if (outEvent != null)
        {
            closeWriter ();
        }
        inEvent = new BufferedReader (new FileReader (file));
        return inEvent;
    }

    /**
     * Closes the events file reader.
     */
    public void closeReader() throws IOException
    {
        if (inEvent != null)
        {
            inEvent.close ();
            inEvent = null;
        }
    }
}
