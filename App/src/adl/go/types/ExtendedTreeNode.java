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
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * A generic node type which defines a name for the object and a unique ID. The
 * unique ID passed in must be verified to be unique and not used by any other
 * types in the application--this verification must be done outside of this
 * object since this object is unaware of any other types. The easiest way to do
 * this is to pass the unique ID of System.currentTimeMillis().
 *
 * A ExtendedTreeNode extends a DefaultMutableTreeNode for simplicity when
 * referencing the term's courses.
 *
 * @author Alex Laird
 */
public class ExtendedTreeNode extends DefaultMutableTreeNode
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
     * The name of the type.
     */
    private String name;
    /**
     * The unique ID of the type.
     */
    private long id;
    /**
     * A reference to the utility.
     */
    private LocalUtility utility;

    /**
     * Constructs a tree node that can be extended by a term or course.
     *
     * @param name The name of the type.
     * @param id The unique ID of the type.
     * @param utility The reference to the utility.
     */
    public ExtendedTreeNode(String name, long id, LocalUtility utility)
    {
        this.name = name;
        this.id = id;
        this.utility = utility;
    }

    /**
     * Retrieve the name of the type.
     *
     * @return The name of the type.
     */
    public String getTypeName()
    {
        return name;
    }

    /**
     * Set the name of the type.
     *
     * @param name The name of the type to be set.
     */
    public void setTypeName(String name)
    {
        this.name = name;
        setUserObject (name);
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
     * The string representation of the tree node to be used for display.
     *
     * @return The name of the tree node.
     */
    @Override
    public String toString()
    {
        return name;
    }
}
