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

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * A button that can be used in the top toolbar of the application.
 *
 * @author Alex Laird
 */
public class ToolbarButton extends JLabel
{
    /**
     * The default image icon.
     */
    private ImageIcon standard;
    /**
     * The hovered image icon.
     */
    private ImageIcon hover;
    /**
     * The depressed image icon.
     */
    private ImageIcon depressed;
    /**
     * The selected state of the button.
     */
    private boolean isSelected = false;

    /**
     * Constructs a new toolbar button with the give image icon as the default
     * image.
     *
     * @param imageIcon The default image icon.
     */
    public ToolbarButton(ImageIcon imageIcon)
    {
        super ();
        setIcon (imageIcon);
        standard = imageIcon;
        hover = new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "" + imageIcon.toString ().substring (imageIcon.toString ().lastIndexOf ("/") + 1, imageIcon.toString ().lastIndexOf (".")) + "_hover.png"));
        depressed = new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "" + imageIcon.toString ().substring (imageIcon.toString ().lastIndexOf ("/") + 1, imageIcon.toString ().lastIndexOf (".")) + "_depressed.png"));
    }

    /**
     * Sets the selected state of the button.
     *
     * @param state True to set depressed, false otherwise.
     */
    public void setSelected(boolean state)
    {
        isSelected = state;
        setDepressed (state);
    }

    /**
     * Returns the selected state of the button.
     *
     * @return The selected state of the button.
     */
    public boolean isSelected()
    {
        return isSelected;
    }

    /**
     * Sets the hover state of the button.
     *
     * @param state True to set hover, false otherwise.
     */
    public void setHover(boolean state)
    {
        if (state)
        {
            setIcon (hover);
        }
        else
        {
            setIcon (standard);
        }
    }

    /**
     * Sets the depressed state of the button.
     *
     * @param state True to set depressed, false otherwise.
     */
    public void setDepressed(boolean state)
    {
        if (state)
        {
            setIcon (depressed);
        }
        else
        {
            setIcon (standard);
        }
    }
}
