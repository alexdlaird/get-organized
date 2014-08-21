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

package adl.go.gui;

import adl.go.resource.Utility;
import javax.swing.table.AbstractTableModel;

/**
 * The table model used for the assignments table, which allows JComponents to
 * be displayed and used in the table--this allows Done column to allow editing
 * of the JCheckBox that is placed there.
 *
 * @author Alex Laird
 */
public class ExtendedTableModel extends AbstractTableModel
{
    /**
     * The column names for this table.
     */
    private String[] columnNames =
    {
        "",
        "Task",
        "Type",
        "Course/Category",
        "Due Date",
        "Grade"
    };
    /**
     * The data array keeps track of rows in this table.
     */
    private Object[][] data =
    {
    };
    /**
     * The reference to the utility.
     */
    private Utility utility;
    /**
     * The column index which will be sorted if a sort is done.
     */
    private int columnSorting = 4;
    /**
     * True if the sort should be ascending, false if descending.
     */
    private boolean sortAscending = true;

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
     * Checks if a column is sortable.
     *
     * @param index The index of the column.
     * @return True if the column is sortable, false otherwise.
     */
    public boolean isSortableColumn(int index)
    {
        return true;
    }

    /**
     * Sets the column that will be sorted on a sort().
     *
     * @param index The column sorting index to be set.
     */
    public void setColumnSorting(int index)
    {
        if (index == columnSorting && sortAscending)
        {
            sortAscending = false;
        }
        else
        {
            if (isSortableColumn (index))
            {
                columnSorting = index;
                sortAscending = true;
            }
        }
    }

    /**
     * Retrieves the sorting direction of the model--ascending or descending.
     *
     * @return True if sorting ascending, false otherwise.
     */
    public boolean isSortAscending()
    {
        return sortAscending;
    }

    /**
     * Set the sorting ascending state of the model.
     *
     * @param sortAscending The sort ascending state to be set.
     */
    public void setSortAscending(boolean sortAscending)
    {
        this.sortAscending = sortAscending;
    }

    /**
     * Retrieves the index of the column that will be sorted on a sort().
     *
     * @return The index of the sorting column.
     */
    public int getColumnSorting()
    {
        return columnSorting;
    }

    /**
     * Set the column at the given index with the given string.
     *
     * @param col The column index to set.
     * @param name The string to set with.
     */
    public void setColumnName(int col, String name)
    {
        columnNames[col] = name;
    }

    /**
     * Adds a row to the table filled with data from the passed in array.
     *
     * @param row The row to be placed in the table.
     */
    public void addRow(Object[] row)
    {
        // create a new data array with one more row and fill it with the old data
        Object[][] newData = new Object[data.length + 1][columnNames.length + 1];
        for (int i = 0; i < data.length; ++i)
        {
            for (int j = 0; j < columnNames.length + 1; ++j)
            {
                newData[i][j] = data[i][j];
            }
        }

        // fill the new row
        for (int i = 0; i < columnNames.length + 1; ++i)
        {
            newData[newData.length - 1][i] = row[i];
        }
        data = newData;
        fireTableRowsUpdated (data.length, data.length);
    }

    /**
     * Removes the specified row from the table.
     *
     * @param index The index to be removed from the table.
     */
    public void removeRow(int index)
    {
        // create a new data array with one more row and fill it with the old data
        Object[][] newData = new Object[data.length - 1][columnNames.length + 1];
        for (int i = 0; i < newData.length; ++i)
        {
            for (int j = 0; j < columnNames.length + 1; ++j)
            {
                int refIndex = i;
                if (i >= index)
                {
                    refIndex += 1;
                }
                newData[i][j] = data[refIndex][j];
            }
        }

        data = newData;
        fireTableRowsUpdated (data.length, data.length);
    }

    /**
     * Removes all rows from the table.
     */
    public void removeAllRows()
    {
        data = new Object[][]
        {
        };
    }

    /**
     * Retrieves the column count.
     *
     * @return The column count.
     */
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    /**
     * Retrieves the row count.
     *
     * @return The row count.
     */
    @Override
    public int getRowCount()
    {
        return data.length;
    }

    /**
     * Retrieves the name of the column at the given index.
     *
     * @param col The column index.
     * @return The name of the column.
     */
    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    /**
     * Retrieves the value at the specified row and column index in the data
     * model.
     *
     * @param row The row index.
     * @param col The colum index.
     * @return The value at that location in the data model.
     */
    @Override
    public Object getValueAt(int row, int col)
    {
        try
        {
            return data[row][col];
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            return null;
        }
    }

    /**
     * If the column is the first column and the list item type is an
     * assignment, the cell is editable, otherwise it is not.
     *
     * @param row The row index.
     * @param col The column index.
     * @return True if the cell is editable, false otherwise.
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 0 && utility.assignmentsAndEvents.get (row).isAssignment ())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets the value at the specified row, column location in the data model.
     *
     * @param value The value to be set.
     * @param row The row index.
     * @param col The column index.
     */
    @Override
    public void setValueAt(Object value, int row, int col)
    {
        data[row][col] = value;
        fireTableCellUpdated (row, col);
    }

    /**
     * Set the row object.
     *
     * @param rowObject The row object to be set.
     * @param row The index of the row.
     */
    public void setRow(Object[] rowObject, int row)
    {
        data[row] = rowObject;
    }

    /**
     * Swaps to rows with each other.
     *
     * @param first The first swap index.
     * @param second The second swap index.
     */
    public void swap(int first, int second)
    {
        Object[] temp = data[second];
        data[second] = data[first];
        data[first] = temp;
        fireTableRowsUpdated (first, first);
        fireTableRowsUpdated (second, second);
    }
}
