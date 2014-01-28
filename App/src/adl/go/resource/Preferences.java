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

import adl.go.gui.ViewPanel;
import adl.go.types.Category;
import java.awt.Color;
import java.util.ArrayList;

/**
 * This class contains all user preference information regarding how the
 * application and the user interact.
 *
 * @author Alex Laird
 */
public class Preferences
{
    /**
     * The separator character sequence.
     */
    protected final String SEPARATOR = LocalUtility.getSeparator ();
    /**
     * The width of the frame.
     */
    public int width;
    /**
     * The height of the frame.
     */
    public int height;
    /**
     * The x-coordinate of the frame.
     */
    public int x = -1;
    /**
     * The y-coordinate of the frame.
     */
    public int y = -1;
    /**
     * The index for the next course color to select.
     */
    public int nextCourseColorIndex = 0;
    /**
     * The index for how the assignments are colored.
     */
    public int colorByIndex = 1;
    /**
     * The index for the default selected middle tabbed pane.
     */
    public int middleTabbedPaneIndex = 0;
    /**
     * The first filter index.
     */
    public int filter1Index = 0;
    /**
     * The second filter index.
     */
    public int filter2Index = 0;
    /**
     * The column index to sort assignments and events by.
     */
    public int sortIndex = 4;
    /**
     * The default index for update checking.
     */
    public int updateCheckIndex = 0;
    /**
     * True if the removal alert should be posted on remove, false otherwise.
     */
    public boolean rmAlert = true;
    /**
     * True if auto update is enabled, false otherwise.
     */
    public boolean autoUpdate = true;
    /**
     * True if assignments and events are sorted in ascending order, false otherwise.
     */
    public boolean sortAscending = true;
    /**
     * True if the getting started dialog should not be shown, false if it
     * should otherwise.
     */
    public boolean dontShowGettingStarted = false;
    /**
     * The list of colors for each priority.
     */
    public Color[] priorityColors = new Color[5];
    /**
     * The list of colors for each due date.
     */
    public Color[] dueDateColors = new Color[7];
    /**
     * The currently displayed theme.
     */
    public String currentTheme = "Default";
    /**
     * The preferred language for the user.
     */
    public String language = "English";
    /**
     * The list of categories.
     */
    public ArrayList<Category> categories = new ArrayList<Category> ();

    /**
     * Construct the default preferences.
     *
     * @param viewPanel A reference to the main view panel.
     */
    public Preferences(ViewPanel viewPanel)
    {
        width = viewPanel.getWidth ();
        height = viewPanel.getHeight ();
        categories.add (new Category ("Default", Color.BLACK));
        priorityColors[0] = new Color (30, 0, 0);
        priorityColors[1] = new Color (90, 0, 0);
        priorityColors[2] = new Color (131, 0, 0);
        priorityColors[3] = new Color (206, 0, 0);
        priorityColors[4] = new Color (255, 0, 0);
        dueDateColors[0] = Color.BLACK;
        dueDateColors[1] = new Color (238, 0, 0);
        dueDateColors[2] = new Color (238, 0, 0);
        dueDateColors[3] = new Color (205, 0, 0);
        dueDateColors[4] = new Color (128, 0, 0);
        dueDateColors[5] = new Color (96, 0, 0);
        dueDateColors[6] = Color.BLUE;
    }

    /**
     * Set all preferences values with the given string.
     *
     * @param preferencesString The string to set preferences with.
     * @param categoriesString The string to set the categories with.
     */
    public void setWithString(String preferencesString, String categoriesString, ViewPanel viewPanel)
    {
        categories.clear ();

        String[] split = preferencesString.split (",");
        try
        {
            width = Integer.parseInt (split[0]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            height = Integer.parseInt (split[1]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            x = Integer.parseInt (split[2]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            y = Integer.parseInt (split[3]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            nextCourseColorIndex = Integer.parseInt (split[4]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            colorByIndex = Integer.parseInt (split[5]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            middleTabbedPaneIndex = Integer.parseInt (split[6]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            filter1Index = Integer.parseInt (split[7]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            filter2Index = Integer.parseInt (split[8]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            sortIndex = Integer.parseInt (split[9]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            rmAlert = Boolean.valueOf (split[10]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            autoUpdate = Boolean.valueOf (split[11]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            sortAscending = Boolean.valueOf (split[12]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            dontShowGettingStarted = Boolean.valueOf (split[13]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            updateCheckIndex = Integer.parseInt (split[14]);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[15].split ("-");
            priorityColors[0] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[16].split ("-");
            priorityColors[1] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[17].split ("-");
            priorityColors[2] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[18].split ("-");
            priorityColors[3] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[19].split ("-");
            priorityColors[4] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[20].split ("-");
            dueDateColors[0] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[21].split ("-");
            dueDateColors[1] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[22].split ("-");
            dueDateColors[2] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[23].split ("-");
            dueDateColors[3] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[24].split ("-");
            dueDateColors[4] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[25].split ("-");
            dueDateColors[5] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            currentTheme = split[26];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            language = split[27];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            String[] color = split[28].split ("-");
            dueDateColors[6] = new Color (Integer.parseInt (color[0]), Integer.parseInt (color[1]), Integer.parseInt (color[2]));
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }

        // find the current theme and select it
        for (int i = 0; i < viewPanel.domain.utility.themes.size (); ++i)
        {
            viewPanel.settingsDialog.themesModel.addElement (viewPanel.domain.utility.themes.get (i));
            viewPanel.settingsDialog.themesPrefModel.addElement (viewPanel.domain.utility.themes.get (i));
            if (viewPanel.domain.utility.themes.get (i).name.equals (currentTheme))
            {
                viewPanel.domain.utility.currentTheme = viewPanel.domain.utility.themes.get (i);
            }
        }
        if (currentTheme == null)
        {
            viewPanel.domain.utility.currentTheme = viewPanel.domain.utility.themes.get (0);
        }

        // read all categories
        split = categoriesString.split (",");
        // parse each color line (name, color) and add it to the data vector as well as the UI model
        for (int i = 0; i < split.length; i += 2)
        {
            // ignore this entry if it is the default
            String[] colorSplit = split[i + 1].split ("-");
            Color color = new Color (Integer.parseInt (colorSplit[0]), Integer.parseInt (colorSplit[1]), Integer.parseInt (colorSplit[2]));
            categories.add (new Category (split[i], color));
        }
    }

    /**
     * Restores all preferences to the defaults, which includes clearing all
     * categories except "Default."
     */
    public void restoreDefaults()
    {
        categories.clear ();
        categories.add (new Category ("Default", Color.BLACK));
        x = -1;
        y = -1;
        nextCourseColorIndex = 0;
        colorByIndex = 1;
        middleTabbedPaneIndex = 0;
        filter1Index = 0;
        filter2Index = 0;
        sortIndex = 4;
        updateCheckIndex = 1;
        rmAlert = true;
        autoUpdate = true;
        sortAscending = true;
        dontShowGettingStarted = false;
        currentTheme = "Default";
        language = "English";
        defaultPriorityColors ();
        defaultDueDateColors ();
    }

    /**
     * Reset the default priority colors.
     */
    public void defaultPriorityColors()
    {
        priorityColors[0] = new Color (30, 0, 0);
        priorityColors[1] = new Color (90, 0, 0);
        priorityColors[2] = new Color (131, 0, 0);
        priorityColors[3] = new Color (206, 0, 0);
        priorityColors[4] = new Color (255, 0, 0);
    }

    /**
     * Reset the default due date colors.
     */
    public void defaultDueDateColors()
    {
        dueDateColors[0] = Color.BLACK;
        dueDateColors[1] = new Color (238, 0, 0);
        dueDateColors[2] = new Color (238, 0, 0);
        dueDateColors[3] = new Color (205, 0, 0);
        dueDateColors[4] = new Color (128, 0, 0);
        dueDateColors[5] = new Color (96, 0, 0);
        dueDateColors[6] = Color.BLUE;
    }

    /**
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    public String out()
    {
        return width + SEPARATOR
               + height + SEPARATOR
               + x + SEPARATOR
               + y + SEPARATOR
               + nextCourseColorIndex + SEPARATOR
               + colorByIndex + SEPARATOR
               + middleTabbedPaneIndex + SEPARATOR
               + filter1Index + SEPARATOR
               + filter2Index + SEPARATOR
               + sortIndex + SEPARATOR
               + rmAlert + SEPARATOR
               + autoUpdate + SEPARATOR
               + sortAscending + SEPARATOR
               + dontShowGettingStarted + SEPARATOR
               + updateCheckIndex + SEPARATOR
               + priorityColors[0].getRed () + "-" + priorityColors[0].getGreen () + "-" + priorityColors[0].getBlue () + SEPARATOR
               + priorityColors[1].getRed () + "-" + priorityColors[1].getGreen () + "-" + priorityColors[1].getBlue () + SEPARATOR
               + priorityColors[2].getRed () + "-" + priorityColors[2].getGreen () + "-" + priorityColors[2].getBlue () + SEPARATOR
               + priorityColors[3].getRed () + "-" + priorityColors[3].getGreen () + "-" + priorityColors[3].getBlue () + SEPARATOR
               + priorityColors[4].getRed () + "-" + priorityColors[4].getGreen () + "-" + priorityColors[4].getBlue () + SEPARATOR
               + dueDateColors[0].getRed () + "-" + dueDateColors[0].getGreen () + "-" + dueDateColors[0].getBlue () + SEPARATOR
               + dueDateColors[1].getRed () + "-" + dueDateColors[1].getGreen () + "-" + dueDateColors[1].getBlue () + SEPARATOR
               + dueDateColors[2].getRed () + "-" + dueDateColors[2].getGreen () + "-" + dueDateColors[2].getBlue () + SEPARATOR
               + dueDateColors[3].getRed () + "-" + dueDateColors[3].getGreen () + "-" + dueDateColors[3].getBlue () + SEPARATOR
               + dueDateColors[4].getRed () + "-" + dueDateColors[4].getGreen () + "-" + dueDateColors[4].getBlue () + SEPARATOR
               + dueDateColors[5].getRed () + "-" + dueDateColors[5].getGreen () + "-" + dueDateColors[5].getBlue () + SEPARATOR
               + currentTheme + SEPARATOR
               + language + SEPARATOR
               + dueDateColors[6].getRed () + "-" + dueDateColors[6].getGreen () + "-" + dueDateColors[6].getBlue ();
    }
}
