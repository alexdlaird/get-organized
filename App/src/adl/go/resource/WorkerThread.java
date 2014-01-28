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

package adl.go.resource;

import adl.go.gui.Domain;
import adl.go.gui.ViewPanel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * The thread which continually checks if changes have been made and need to be
 * saved.
 *
 * @author Alex Laird
 */
public class WorkerThread extends Thread
{
    /**
     * The main frame of the application.
     */
    private ViewPanel viewPanel;
    /**
     * The domain for the main frame.
     */
    private Domain domain;
    /**
     * If the thread is already in a save operation, a second save operation may
     * not be called on it until the first finishes.
     */
    public boolean saving = false;
    /**
     * Save, if save is needed, every thirty seconds.
     */
    private final long delay = 500;
    /**
     * The next time (system clock in ms) that a save will be attempted.
     */
    private long nextSave = 0;
    /**
     * Check if the thread should quit.
     */
    private boolean wantToQuit = false;
    /**
     * True if a save is allowed, false otherwise.
     */
    private boolean allowSave = false;

    /**
     * Constructs the load/save thread with a reference to the main frame and a
     * reference to the local utility object.
     *
     * @param viewPanel A reference to the main frame of the application.
     * @param domain The domain for the main frame.
     */
    public WorkerThread(ViewPanel viewPanel, Domain domain)
    {
        this.viewPanel = viewPanel;
        this.domain = domain;
    }

    /**
     * Sets the quit state of the thread to true, so it will not execute its
     * actions after each delay. It can be set back to running by calling run()
     * at any time.
     */
    public void stopRunning()
    {
        wantToQuit = true;
    }

    /**
     * Sets the quit state of the thread to false, so it will start execution
     * again.
     *
     * @param allowSave Set the state of the allow save flag.
     */
    public void setAllowSave(boolean allowSave)
    {
        this.allowSave = allowSave;
    }

    /**
     * Starts the save thread and checks every delay interval to see if changes
     * have been made and settings should be saved to the file.
     */
    @Override
    public void run()
    {
        while (!wantToQuit)
        {
            try
            {
                sleep (delay);
            }
            catch (InterruptedException ex)
            {
                Domain.LOGGER.add (ex);
            }

            // Grab the time to see if this thread needs to execute an action
            long currTime = System.currentTimeMillis ();

            if (allowSave && nextSave <= currTime)
            {
                save ();

                nextSave = currTime + 500;
            }
        }
    }

    /**
     * Calls the respective save methods if changes have been made.
     */
    protected synchronized void save()
    {
        if (!viewPanel.initLoading)
        {
            String prevDate = Domain.DATE_FORMAT.format (domain.today);
            if (domain.needsPreferencesSave)
            {
                domain.savePreferences ();
                try
                {
                    domain.today = Domain.DATE_AND_TIME_FORMAT.parse (Domain.DATE_AND_TIME_FORMAT.format (new Date ()));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                domain.needsPreferencesSave = false;
            }
            if (domain.needsUserDetailsSave)
            {
                domain.saveUserDetails ();
                try
                {
                    domain.today = Domain.DATE_AND_TIME_FORMAT.parse (Domain.DATE_AND_TIME_FORMAT.format (new Date ()));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                domain.needsUserDetailsSave = false;
            }
            if (domain.needsCoursesAndTermsSave)
            {
                domain.saveSettings ();
                try
                {
                    domain.today = Domain.DATE_FORMAT.parse (Domain.DATE_FORMAT.format (new Date ()));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                domain.needsCoursesAndTermsSave = false;
            }
            for (int i = 0; i < domain.utility.courses.size (); ++i)
            {
                if (domain.utility.courses.get (i).isChanged ())
                {
                    domain.saveAssignments (domain.utility.courses.get (i));
                    try
                    {
                        domain.today = Domain.DATE_FORMAT.parse (Domain.DATE_FORMAT.format (new Date ()));
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }
                }
            }
            for (int i = 0; i < domain.utility.eventYears.size (); ++i)
            {
                if (domain.utility.eventYears.get (i).isChanged ())
                {
                    domain.saveEvents (domain.utility.eventYears.get (i));
                    try
                    {
                        domain.today = Domain.DATE_FORMAT.parse (Domain.DATE_FORMAT.format (new Date ()));
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }
                }
            }
            if (!prevDate.equals (Domain.DATE_FORMAT.format (domain.today)))
            {
                viewPanel.refindToday ();
            }

            // save anything that is stored in the logger, then clear it
            if (Domain.LOGGER.size () > 0)
            {
                try
                {
                    BufferedWriter out = new BufferedWriter (new FileWriter (domain.logFile, true));
                    Date date = new Date ();
                    out.write ("------------------------------------\n");
                    out.write ("Date: " + Domain.DATE_AND_FULL_TIME_FORMAT.format (date) + "\n");

                    for (int i = 0; i < Domain.LOGGER.size (); ++i)
                    {
                        Exception ex = (Exception) Domain.LOGGER.get (i);
                        out.write ("Error: " + ex.getClass () + "\n");
                        out.write ("Message: " + ex.getMessage () + "\n--\nTrace:\n");
                        Object[] trace = ex.getStackTrace ();
                        for (int j = 0; j < trace.length; ++j)
                        {
                            out.write ("  " + trace[j].toString () + "\n");
                        }
                        out.write ("--\n\n");
                        out.write ("----\n");
                    }

                    out.write ("------------------------------------\n\n\n");
                    out.flush ();

                    Domain.LOGGER.clear ();
                }
                catch (IOException ex)
                {
                }
            }
        }
    }
}
