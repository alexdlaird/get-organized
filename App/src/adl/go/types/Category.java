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

import java.awt.Color;
import java.util.ArrayList;

/**
 * A category is used soley for the purpose of coloring events so they may
 * better be organized.
 *
 * @author Alex Laird
 */
public class Category
{
    /**
     * The list of events attached to this category.
     */
    private ArrayList<Event> events = new ArrayList<Event> ();
    /**
     * The name of the category.
     */
    private String name;
    /**
     * The color the category will be shown as.
     */
    private Color color = Color.BLACK;

    /**
     * Constructs a new category with the given name and color.
     *
     * @param name The name of the category.
     * @param color The color of the category.
     */
    public Category(String name, Color color)
    {
        this.name = name;
        this.color = color;
    }

    /**
     * Retrieves the event at the given index attached to this category.
     *
     * @param index The index of the event to retrieve.
     * @return The event at the given index within this category.
     */
    public Event getEvent(int index)
    {
        return events.get (index);
    }

    /**
     * Retrieves the event count for this category.
     *
     * @return The number of events attached to this category.
     */
    public int getEventCount()
    {
        return events.size ();
    }

    /**
     * Checks if an event is attached to this category.
     *
     * @param event The event to be searched for.
     * @return True if the event is attached to this category, false otherwise.
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
     * Adds the given event to this category.
     *
     * @param event The event to be added to this category.
     */
    public void addEvent(Event event)
    {
        events.add (event);
    }

    /**
     * Removes the given event from this category.
     *
     * @param event The event to be removed.
     * @return The removed event.
     */
    public Event removeEvent(Event event)
    {
        events.remove (event);
        return event;
    }

    /**
     * Retrieves the name of the category.
     *
     * @return The name of the category.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name The name to be set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Retrieves the formatted color string.
     *
     * @return The formatted color string.
     */
    public String getColorString()
    {
        return color.getRed () + "-" + color.getGreen () + "-" + color.getBlue ();
    }

    /**
     * Retrieves the color of the category.
     *
     * @return The color of the category.
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * Retrieves a string representation of the color for this category.
     *
     * @return The string representation of the color for this category.
     */
    public String getFormattedColor()
    {
        return color.getRed () + "-" + color.getGreen () + "-" + color.getBlue ();
    }

    /**
     * Sets the color for this category.
     *
     * @param color The color to be set for this category.
     */
    public void setColor(Color color)
    {
        this.color = color;
    }
}
