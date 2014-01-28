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

/**
 * A list item is either an assignment or an event. By default, it is assumed
 * the list item is an assignment unless otherwise specified. One can call
 * isAssignment(), which will return true if the list item is an assignment or
 * false if the list item is an event.
 *
 * @author Alex Laird
 */
public interface ListItem
{
    /**
     * Returns true if it is an assignment, false if it is an event.
     *
     * @return True if it is an assignment, false if it is an event.
     */
    public boolean isAssignment();

    /**
     * Retrieves the type name for the assignment or event.
     *
     * @return The name of the assignment or event.
     */
    public String getItemName();

    /**
     * Retrieves the unique ID for the assignment or event.
     *
     * @return The unique ID of the assignment or event.
     */
    public long getUniqueID();

    /**
     * Sets the unique ID for the assignment or event.
     *
     * @param id The unique ID of the assignment or event to be written.
     */
    public void setUniqueID(long id);

    /**
     * Retrieves the due date (or start date) or the assignment or event.
     *
     * @return The due date or start date of the assignment or event.
     */
    public String getDueDate();

    /**
     * Retrieves the table row object for this item.
     *
     * @return The table row object for this item.
     */
    public Object[] getRowObject();

    /**
     * Set the state of the item, whether it is shown as selected or not in the
     * UI.
     *
     * @param state The state to be set for the UI selection.
     */
    public void showAsSelected(boolean state);

    /**
     * Refreshes the text of the component that is shown in Calendar View.
     */
    public void refreshText();

    /**
     * Returns the output string for the data file.
     *
     * @return The output string for the data file.
     */
    public String out();
}
