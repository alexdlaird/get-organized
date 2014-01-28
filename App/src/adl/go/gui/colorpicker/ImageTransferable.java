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
/*
 * @(#)ColorPicker.java  1.0  2008-03-01
 *
 * Copyright (c) 2008 Jeremy Wood
 * E-mail: mickleness@gmail.com
 * All rights reserved.
 *
 * The copyright of this software is owned by Jeremy Wood.
 * You may not use, copy or modify this software, except in
 * accordance with the license agreement you entered into with
 * Jeremy Wood. For details see accompanying license terms.
 */

package adl.go.gui.colorpicker;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class ImageTransferable implements Transferable
{
    Image img;

    public ImageTransferable(Image i)
    {
        img = i;
    }

    @Override
    public Object getTransferData(DataFlavor f)
            throws UnsupportedFlavorException, IOException
    {
        if (f.equals (DataFlavor.imageFlavor) == false)
        {
            throw new UnsupportedFlavorException (f);
        }
        return img;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]
                {
                    DataFlavor.imageFlavor
                };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return (flavor.equals (DataFlavor.imageFlavor));
    }
}
