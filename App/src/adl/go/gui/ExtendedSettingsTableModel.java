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

import javax.swing.table.AbstractTableModel;

/**
 * The table model used for the list tables in the Settings dialog.
 *
 * @author Alex Laird
 */
public class ExtendedSettingsTableModel extends AbstractTableModel
{
    /**
     * A reference to the view panel.
     */
    private ViewPanel viewPanel;
    /**
     * The column names for this table.
     */
    private String[] columnNames =
    {
    };
    /**
     * The data array keeps track of rows in this table.
     */
    private Object[][] data =
    {
    };

    /**
     * Constructs a new model with the given column names.
     *
     * @param columnNames The array of columns names.
     * @param viewPanel A reference to the view panel.
     */
    public ExtendedSettingsTableModel(String[] columnNames, ViewPanel viewPanel)
    {
        this.columnNames = columnNames;
        this.viewPanel = viewPanel;
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
     * Add the row at the given index to the courses list.
     *
     * @param index The index to add the row to.
     * @param course The row to add.
     */
    public void insertRowAt(int index, Object[] row)
    {
        int rowCount = getRowCount ();
        addRow (row);
        if (index < rowCount - 1)
        {
            for (int i = rowCount; i > index + 1; --i)
            {
                viewPanel.swap (viewPanel.termsAndCoursesDialog.courseTableModel, 1, i, i - 1);
            }
        }
    }

    /**
     * Removes the specified row from the table.
     *
     * @param index The index to be removed from the table.
     */
    public void removeRow(int index)
    {
        if (data.length > 0)
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
     * Retrieves the column name for the specified column index.
     *
     * @param col The column index.
     * @return The name of the specified column.
     */
    @Override
    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    /**
     * Retrieves the value at the specified row and column location in the data
     * model.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The value at the row, column location in the data model.
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
     * Always returns false so all cells are not editable.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The editable state of the cell.
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /**
     * Sets the value at the specified location in the data model.
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
}
