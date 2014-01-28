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

package adl.go.gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * A filter that allows extensions to be added (with descriptions) at
 * construction.
 *
 * @author Alex Laird
 */
public class ExtensionFileFilter extends FileFilter
{
    /**
     * Description of the extension filter.
     */
    String description;
    /**
     * Extensions accepted in the filter.
     */
    String extensions[];

    /**
     * Construct the extension filter with only one accepted extension.
     *
     * @param description Description of the extension filter.
     * @param extension Accepted extensions.
     */
    public ExtensionFileFilter(String description, String extension)
    {
        this (description, new String[]
                {
                    extension
                });
    }

    /**
     * Construct the extension filter with an array of accepted extensions.
     *
     * @param description Description of the extension filter.
     * @param extensions Accepted extensions.
     */
    public ExtensionFileFilter(String description, String extensions[])
    {
        if (description == null)
        {
            this.description = extensions[0];
        }
        else
        {
            this.description = description;
        }

        this.extensions = (String[]) extensions.clone ();

        for (int i = 0, n = this.extensions.length; i < n; i++)
        {
            this.extensions[i] = this.extensions[i].toLowerCase ();
        }
    }

    /**
     * Check to see if the specified file is of the accepted extension.
     *
     * @param file The file to be checked.
     * @return True if the file is acceptable, false otherwise.
     */
    @Override
    public boolean accept(File file)
    {
        // always allow directories
        if (file.isDirectory ())
        {
            return true;
        }
        // allow only those extensions found in the accepted extensions array
        else
        {
            String path = file.getAbsolutePath ().toLowerCase ();
            for (int i = 0, n = extensions.length; i < n; i++)
            {
                String extension = extensions[i];
                if ((path.endsWith (extension) && (path.charAt (path.length ()
                                                                - extension.length () - 1)) == '.'))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieve the extension filter description.
     *
     * @return The description of the extension filter.
     */
    @Override
    public String getDescription()
    {
        return description;
    }
}
