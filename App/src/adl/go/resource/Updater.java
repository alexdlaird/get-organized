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

package adl.go.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * The updater class connects to the update server and checks for available
 * updates to the application; it also is able to download the updater if an
 * update is available. All methods are static so they can be accessed anytime
 * from anywhere.
 *
 * @author Alex Laird
 */
public class Updater
{
    /**
     * Checks the current version of the application against the latest version
     * on the server and returns true if a newer update is available.
     *
     * @param currentString The current version of the application that will be
     * compared to the latest version on the server.
     * @param checkIndex 0 is stable only, 1 is beta and stable, 2 is any new
     * release
     * @return Details about available updates, if there are any.
     */
    public static Object[] checkForUpdates(String currentString, int checkIndex)
    {
        String priority = null;
        String message = null;
        try
        {
            // establish a connection with the update server
            URL url = new URL ("http://updates.alexlaird.com/get-organized/version.txt");
            URLConnection conn = url.openConnection ();
            BufferedReader in = new BufferedReader (new InputStreamReader (conn.getInputStream ()));
            // grab the server version information
            String line = in.readLine ();
            // throw out the three version sizes
            try
            {
                in.readLine ();
                in.readLine ();
                in.readLine ();
                priority = in.readLine ();
            }
            catch (Exception ex)
            {
            }
            if (line != null && line.split ("=").length > 0)
            {
                String serverString = line.split ("=")[1];

                // parse the line from the server into appropriate version information
                double currentVersion = Double.parseDouble (currentString.split (" ")[0]);
                String currentSuffix;
                try
                {
                    currentSuffix = currentString.split (" ")[1];
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    currentSuffix = null;
                }
                double serverVersion = Double.parseDouble (serverString.split (" ")[0]);
                String serverSuffix;
                try
                {
                    serverSuffix = serverString.split (" ")[1];
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    serverSuffix = null;
                }

                // compare the local version of the software to the server version
                if (serverVersion > currentVersion
                    || (serverVersion == currentVersion && currentSuffix != null && currentSuffix.toLowerCase ().equals ("beta") && serverSuffix == null)
                    || (serverVersion == currentVersion && currentSuffix != null && currentSuffix.toLowerCase ().equals ("alpha") && serverSuffix != null && serverSuffix.toLowerCase ().equals ("beta"))
                    || (serverVersion == currentVersion && currentSuffix != null && currentSuffix.toLowerCase ().equals ("alpha") && serverSuffix == null))
                {
                    if ((checkIndex == 0 && serverSuffix == null)
                        || (checkIndex == 1 && (serverSuffix == null || serverSuffix.toLowerCase ().equals ("beta")))
                        || (checkIndex == 2 && (serverSuffix == null || serverSuffix.toLowerCase ().equals ("beta") || serverSuffix.toLowerCase ().equals ("alpha"))))
                    {
                        // an update is available, so return 1 (for true) and the update information
                        if (serverSuffix == null)
                        {
                            serverSuffix = "";
                        }
                        return new Object[]
                                {
                                    1, serverVersion + " " + serverSuffix, priority
                                };
                    }
                    else
                    {
                        // an update was found, but it did not meet the user's criteria
                        return new Object[]
                                {
                                    0, null, priority
                                };
                    }
                }
                else
                {
                    // no update was available on the server
                    return new Object[]
                            {
                                0, null, priority
                            };
                }
            }
            else
            {
                // the line read from the server was null, so connection essentially failed
                return new Object[]
                        {
                            -1, null, priority
                        };
            }
        }
        catch (MalformedURLException ex)
        {
            // server connection failed
            return new Object[]
                    {
                        -1, null, priority
                    };
        }
        catch (IOException ex)
        {
            // an unknown error occured
            return new Object[]
                    {
                        -1, null, priority
                    };
        }
        catch (NumberFormatException ex)
        {
            // parsing the server version errored
            return new Object[]
                    {
                        -1, null, priority
                    };
        }
    }

    /**
     * checkForUpdates() should be called before getUpdater() to ensure an
     * update is available. This update will retrieve the Updater executable,
     * which can then be launched to run the update.
     *
     * @return A reference to the Updater.jar file, if it was downloaded
     * properly.
     */
    public static File getUpdater(File dataFolder)
    {
        try
        {
            URL url = new URL ("http://updates.alexlaird.com/get-organized/Updater.jar");
            url.openConnection ();
            InputStream out = url.openStream ();

            File file = new File (dataFolder, "Updater.jar");
            FileOutputStream in = new FileOutputStream (file);
            byte[] buffer = new byte[153600];
            int bytes = 0;

            while ((bytes = out.read (buffer)) > 0)
            {
                in.write (buffer, 0, bytes);
                buffer = new byte[153600];
            }

            in.close ();
            out.close ();

            return file;
        }
        catch (MalformedURLException ex)
        {
            return null;
        }
        catch (IOException ex)
        {
            // an unknown error occured
            return null;
        }
    }
}
