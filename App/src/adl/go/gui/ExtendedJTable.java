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

import adl.go.resource.Utility;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * An extended JTable with additional functionality, including stripped rows.
 *
 * @author Alex Laird
 */
public class ExtendedJTable extends JTable
{
    /**
     * The alternating row colors.
     */
    private Color rowColors[] = new Color[2];
    /**
     * The reference to the utility.
     */
    private Utility utility;
    /**
     * True when the selection interval is being removed, false otherwise.
     */
    protected boolean removingSelectionInterval = false;
    /**
     * True if stripes should be drawn, false otherwise.
     */
    private boolean drawStripes = false;

    /**
     * Sets the utility pointer.
     *
     * @param utility The utility reference.
     */
    public void setUtility(Utility utility)
    {
        this.utility = utility;
    }

    /**
     * Call this when the data or attributes within the table have changed to
     * update the interface display.
     */
    public void refreshTable()
    {
        invalidate ();
        repaint ();
        getTableHeader ().resizeAndRepaint ();
    }

    /**
     * Retrieves the index relative to the model of this table from finding the
     * element attached to the absolute index (relative to the database) passed
     * in to this method.
     *
     * @param index The absolute index relative to the database structure.
     * @return The index relative to the table.
     */
    public int getSelectableRowFromVectorIndex(int index)
    {
        int newIndex = -1;
        if (index != -1)
        {
            long uniqueID = utility.assignmentsAndEvents.get (index).getUniqueID ();
            for (int i = 0; i < getRowCount (); ++i)
            {
                if (Long.parseLong (getModel ().getValueAt (i, 6).toString ()) == uniqueID)
                {
                    newIndex = i;
                    break;
                }
            }
        }

        return newIndex;
    }

    /**
     * Sets the selected row in the assignments table from a vector index.
     *
     * @param index The vector index.
     */
    public void setSelectedRowFromVectorIndex(int index)
    {
        setSelectedRow (getSelectableRowFromVectorIndex (index));
    }

    /**
     * Retrieves the vector index for the selected row.
     *
     * @param row The row to be used to find.
     * @return The index of the assignment or event in the vector.
     */
    public int getVectorIndexFromSelectedRow(int row)
    {
        if (row != -1)
        {
            return utility.getAssignmentOrEventIndexByID (Long.parseLong (getModel ().getValueAt (row, 6).toString ()));
        }
        else
        {
            return -1;
        }
    }

    /**
     * Retrieves the vector index for the selected row.
     *
     * @return The index of the assignment or event in the vector.
     */
    public int getVectorIndexFromSelectedRow() throws NullPointerException
    {
        if (getSelectedRow () != -1)
        {
            return utility.getAssignmentOrEventIndexByID (Long.parseLong (getModel ().getValueAt (getSelectedRow (), 6).toString ()));
        }
        else
        {
            return -1;
        }
    }

    /**
     * Sets the selected row in the table based on an index.
     *
     * @param index The index of the row to be set.
     */
    public void setSelectedRow(int index)
    {
        if (index != -1)
        {
            getSelectionModel ().setSelectionInterval (index, index);
        }
        else
        {
            removingSelectionInterval = true;
            getSelectionModel ().removeSelectionInterval (getSelectedRow (), getSelectedRow ());
            removingSelectionInterval = false;
        }
        invalidate ();
    }

    /**
     * Sets the selected row in the table based upon a unique ID. The column
     * holding the unique ID is passed in.
     *
     * @param id The id to be selected.
     * @param column The index of the column where the unique ID is stored.
     */
    public void setSelectedRow(long id, int column)
    {
        for (int i = 0; i < getRowCount (); ++i)
        {
            if (Long.parseLong (getModel ().getValueAt (i, column).toString ()) == id)
            {
                setSelectedRow (i);
                break;
            }
        }
    }

    /**
     * Add stripes between cells and behind non-opaque cells.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        if (!(drawStripes = isOpaque ()))
        {
            super.paintComponent (g);
            return;
        }

        // paint background stripes
        updateColors ();
        final Insets insets = getInsets ();
        final int w = getWidth () - insets.left - insets.right;
        final int h = getHeight () - insets.top - insets.bottom;
        final int x = insets.left;
        int y = insets.top;
        int localRowHeight = 16;
        final int nItems = getRowCount ();
        for (int i = 0; i < nItems; i++, y += localRowHeight)
        {
            localRowHeight = getRowHeight (i);
            g.setColor (rowColors[i & 1]);
            g.fillRect (x, y, w, localRowHeight);
        }

        final int nRows = nItems + (insets.top + h - y) / localRowHeight;
        for (int i = nItems; i < nRows; i++, y += localRowHeight)
        {
            g.setColor (rowColors[i & 1]);
            g.fillRect (x, y, w, localRowHeight);
        }
        final int remainder = insets.top + h - y;
        if (remainder > 0)
        {
            g.setColor (rowColors[nRows & 1]);
            g.fillRect (x, y, w, remainder);
        }

        // paint compoent
        setOpaque (false);
        super.paintComponent (g);
        setOpaque (true);
    }

    /**
     * Add background stripes behind rendered cells.
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col)
    {
        Component c = null;
        if (getValueAt (row, col) != null)
        {
            c = super.prepareRenderer (renderer, row, col);
        }
        else
        {
            c = super.prepareRenderer (new DefaultTableCellRenderer (), row, col);
        }

        if (drawStripes && !isCellSelected (row, col))
        {
            c.setBackground (rowColors[row & 1]);
        }
        return c;
    }

    /**
     * Add background stripes behind edited cells.
     */
    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int col)
    {
        final Component c = super.prepareEditor (editor, row, col);
        if (drawStripes && !isCellSelected (row, col))
        {
            c.setBackground (rowColors[row & 1]);
        }
        return c;
    }

    /**
     * Retrieves the cell renderer for the specific cell.
     *
     * @param row The row to retrieve the renderer for.
     * @param column The column to retrieve the renderer for.
     * @return The cell renderer for the specific cell.
     */
    @Override
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        Object value = getValueAt (row, column);
        if (value != null)
        {
            return getDefaultRenderer (value.getClass ());
        }
        return super.getCellRenderer (row, column);
    }

    /**
     * Force the table to fill the viewport's height.
     */
    @Override
    public boolean getScrollableTracksViewportHeight()
    {
        final Component c = getParent ();
        if (!(c instanceof JViewport))
        {
            return false;
        }
        return ((JViewport) c).getHeight () > getPreferredSize ().height;
    }

    /**
     * Updates the colors accordingly for the cell for odd and even.
     */
    private void updateColors()
    {
        try
        {
            rowColors[0] = utility.currentTheme.colorOdd;
            rowColors[1] = utility.currentTheme.colorEven;
        }
        catch (NullPointerException ex)
        {
            rowColors[0] = Color.WHITE;
            rowColors[1] = new Color (237, 240, 242);
        }
    }
}
