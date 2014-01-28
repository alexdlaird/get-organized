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
import java.util.Date;

/**
 * This object contains information pertaining to the repetition of an event.
 *
 * @author Alex Laird
 */
public class Repeating
{
    /**
     * The unique ID for the repeating event.
     */
    protected long id = -1;
    /**
     * The index for how it repeats.
     */
    protected int repeatsIndex = 0;
    /**
     * The index for how often it repeats.
     */
    protected int repeatsEveryIndex = 0;
    /**
     * Repeating on Sunday.
     */
    protected boolean sunday = false;
    /**
     * Repeating on Monday.
     */
    protected boolean monday = false;
    /**
     * Repeating on Tuesday.
     */
    protected boolean tuesday = false;
    /**
     * Repeating on Wednesday.
     */
    protected boolean wednesday = false;
    /**
     * Repeating on Thursday.
     */
    protected boolean thursday = false;
    /**
     * Repeating on Friday.
     */
    protected boolean friday = false;
    /**
     * Repeating on Saturday.
     */
    protected boolean saturday = false;
    /**
     * The start date for repeating.
     */
    protected String startDate = Domain.DATE_FORMAT.format (new Date ());
    /**
     * The end date for repeating.
     */
    protected String endDate = Domain.DATE_FORMAT.format (new Date ());

    /**
     * Retrieve the unique ID for the repetition of the event.
     *
     * @return The ID for the repetition of the event.
     */
    public long getID()
    {
        return id;
    }

    /**
     * Set the unique ID for the repetition of the event.
     *
     * @param id The ID to be set for the event.
     */
    public void setID(long id)
    {
        this.id = id;
    }

    /**
     * Retrieve the repeating index for the repetition.
     *
     * @return The repeating index of the repetition.
     */
    public int getRepeatsIndex()
    {
        return repeatsIndex;
    }

    /**
     * Retrieve the repeats every index for the repetition.
     *
     * @return The repeats every index of the repetition.
     */
    public int getRepeatsEveryIndex()
    {
        return repeatsEveryIndex;
    }

    /**
     * Retrieve the Sunday repetition.
     *
     * @return The Sunday repetition.
     */
    public boolean getSunday()
    {
        return sunday;
    }

    /**
     * Retrieve the Monday repetition.
     *
     * @return The Monday repetition.
     */
    public boolean getMonday()
    {
        return monday;
    }

    /**
     * Retrieve the Tuesday repetition.
     *
     * @return The Tuesday repetition.
     */
    public boolean getTuesday()
    {
        return tuesday;
    }

    /**
     * Retrieve the Wednesday repetition.
     *
     * @return The Wednesday repetition.
     */
    public boolean getWednesday()
    {
        return wednesday;
    }

    /**
     * Retrieve the Thursday repetition.
     *
     * @return The Thursday repetition.
     */
    public boolean getThursday()
    {
        return thursday;
    }

    /**
     * Retrieve the Friday repetition.
     *
     * @return The Friday repetition.
     */
    public boolean getFriday()
    {
        return friday;
    }

    /**
     * Retrieve the Saturday repetition.
     *
     * @return The Saturday repetition.
     */
    public boolean getSaturday()
    {
        return saturday;
    }

    /**
     * Retrieve the start date of the repetition.
     *
     * @return The start date of the repetition.
     */
    public String getStartDate()
    {
        return startDate;
    }

    /**
     *
     * @param startDate
     */
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    /**
     * Retrieve the end date of the repetition.
     *
     * @return The end date of the repetition.
     */
    public String getEndDate()
    {
        return endDate;
    }

    /**
     *
     * @param endDate
     */
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    /**
     * A string representation of the repetition of the event. Does not include
     * the unique ID.
     *
     * @return The string of the repetition of the event.
     */
    @Override
    public String toString()
    {
        return repeatsIndex + "-"
               + repeatsEveryIndex + "-"
               + sunday + "-"
               + monday + "-"
               + tuesday + "-"
               + wednesday + "-"
               + thursday + "-"
               + friday + "-"
               + saturday + "-"
               + startDate + "-"
               + endDate;
    }
}
