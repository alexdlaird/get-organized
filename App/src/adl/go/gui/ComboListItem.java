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

package adl.go.gui;

/**
 * A combo list item can be used as an object representation of an object that
 * is placed in a JComboBox. This way the list item can have a name (which is
 * returned in the toString() for display) and also a unique ID that can be
 * associated it for database access.
 *
 * @author Alex Laird
 */
public class ComboListItem
{
    /**
     * The name of the list item.
     */
    String name;
    /**
     * The unique ID of the list item
     */
    long uniqueID;

    /**
     * Constructs the list item with a name and a unique ID.
     *
     * @param name The name of the list item.
     * @param uniqueID The unique ID of the list item.
     */
    public ComboListItem(String name, long uniqueID)
    {
        this.name = name;
        this.uniqueID = uniqueID;
    }

    /**
     * Retrieves the unique ID of the list item.
     *
     * @return The unique ID of the list item.
     */
    public long getUniqueID()
    {
        return uniqueID;
    }

    /**
     * A string representation of the list item.
     *
     * @return The name of the list item.
     */
    @Override
    public String toString()
    {
        return name;
    }
}
