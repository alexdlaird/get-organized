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

import adl.go.resource.LocalUtility;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * A generic label type which defines a name for the type and a unique ID. The
 * unique ID passed in must be verified to be unique and not used by any other
 * types in the application--this verification must be done outside of this
 * object since this object is unaware of any other types. The easiest way to do
 * this is to pass the unique ID of System.currentTimeMillis().
 *
 * A ExtendedJPanelForEvent extends a JLabel for simplicity when adding a type
 * to a day panel in Calendar View.
 *
 * @author Alex Laird
 */
public class ExtendedJPanelForEvent extends JPanel
{
    /**
     * The separator character sequence.
     */
    protected final String SEPARATOR = LocalUtility.getSeparator ();
    /**
     * The character marking the end of a line (prior to the line return).
     */
    protected final String END_OF_LINE = LocalUtility.getEndOfLine ();
    /**
     * The character marking a line return.
     */
    protected final String LINE_RETURN = LocalUtility.getLineReturn ();
    /**
     * The selected labels border.
     */
    private final Border selectedBorder = BorderFactory.createMatteBorder (1, 0, 1, 0, Color.DARK_GRAY);
    /**
     * The unselected labels border.
     */
    private final Border unselectedBorder = BorderFactory.createEmptyBorder ();
    /**
     * The label object.
     */
    private final JLabel LABEL = new JLabel ();
    /**
     * The name of the type.
     */
    private String name;
    /**
     * The unique ID of the type.
     */
    private long id;

    /**
     * Constructs a generic label that can be used by an event in the Calendar
     * View.
     *
     * @param name The name of the type.
     * @param id The unique ID of the type.
     */
    public ExtendedJPanelForEvent(String name, long id, LocalUtility utility)
    {
        setItemNameFromConstructor (name);
        this.id = id;
        setBorder (unselectedBorder);

        LABEL.setFont (utility.currentTheme.fontPlain11);
        GroupLayout layout = new GroupLayout (this);
        layout.setHorizontalGroup (
                layout.createSequentialGroup ().addComponent (LABEL));
        layout.setVerticalGroup (
                layout.createSequentialGroup ().addGroup (layout.createParallelGroup (GroupLayout.Alignment.BASELINE).addComponent (LABEL)));
        setLayout (layout);
        setOpaque (false);
        LABEL.setOpaque (false);
    }

    /**
     * Retrieves the label associated with this event.
     *
     * @return The label attached to this event.
     */
    public JLabel getLabel()
    {
        return LABEL;
    }

    /**
     * Retrieve the name of the type.
     *
     * @return The name of the type.
     */
    public String getItemName()
    {
        return name;
    }

    /**
     * Sets the viewable selected state of the checkbox.
     *
     * @param state True shows the assignment as selected, false does not.
     */
    public void showAsSelected(boolean state)
    {
        if (state)
        {
            setBorder (selectedBorder);
        }
        else
        {
            setBorder (unselectedBorder);
        }
    }

    /**
     * The final method for setting the type's name from the constructor.
     *
     * @param name The name of the type.
     */
    public final void setItemNameFromConstructor(String name)
    {
        setItemName (name);
    }

    /**
     * Set the name of the type.
     *
     * @param name The name of the type to be set.
     */
    public void setItemName(String name)
    {
        this.name = name;
        LABEL.setText ("<html>" + name + "</html>");
    }

    /**
     * Retrieve the unique ID of the type.
     *
     * @return The unique ID.
     */
    public long getUniqueID()
    {
        return id;
    }

    /**
     * Warning: this method is implemented only for specific use in forcing a
     * unique ID to a type after creation, necessary during the load from the
     * data files. This method should never be used otherwise, because conflicts
     * may arise. If it is used, ensure that it is used on a guaranteed unique
     * ID such as System.currentTimeMillis().
     *
     * @param id The unique ID to be set.
     */
    public void setUniqueID(long id)
    {
        this.id = id;
    }

    /**
     * The string representation of this event for display.
     *
     * @return The name of the event.
     */
    @Override
    public String toString()
    {
        return name;
    }
}
