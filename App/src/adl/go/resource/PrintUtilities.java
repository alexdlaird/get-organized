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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.RepaintManager;

/**
 *
 * @author alexdlaird
 */
public class PrintUtilities implements Printable
{
    /**
     * The component that is to be printed.
     */
    private Component printable;

    /**
     * Display a dialog and print the given component.
     *
     * @param c The component to be printed.
     */
    public static void printComponent(Component c)
    {
        printComponent (c, null);
    }

    /**
     * Print the given component with the given print job.
     *
     * @param c The component to be printed.
     */
    public static void printComponent(Component c, PrinterJob pj)
    {
        PrintUtilities util = new PrintUtilities (c);
        util.print (pj);
    }

    /**
     * Construct a printable version of the component.
     *
     * @param printable The component to be made printable.
     */
    public PrintUtilities(Component printable)
    {
        this.printable = printable;
    }

    /**
     * Print this component.
     */
    public void print(PrinterJob pj)
    {
        if (pj == null)
        {
            pj = PrinterJob.getPrinterJob ();

            pj.setPrintable (this);
            if (pj.printDialog ())
            {
                try
                {
                    pj.print ();
                }
                catch (PrinterException pe)
                {
                    System.out.println ("Error printing: " + pe);
                }
            }
        }
        else
        {
            pj.setPrintable (this);
            try
            {
                pj.print ();
            }
            catch (PrinterException pe)
            {
                System.out.println ("Error printing: " + pe);
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    {
        if (pageIndex > 0)
        {
            return (NO_SUCH_PAGE);
        }
        else
        {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate (pageFormat.getImageableX (), pageFormat.getImageableY ());
            disableDoubleBuffering (printable);
            printable.paint (g2d);
            enableDoubleBuffering (printable);
            return (PAGE_EXISTS);
        }
    }

    /**
     * The speed and quality of printing suffers dramatically if any of the
     * containers have double buffering turned on. So this turns if off
     * globally.
     *
     * @param c The component to disable double buffering for.
     */
    public static void disableDoubleBuffering(Component c)
    {
        RepaintManager currentManager = RepaintManager.currentManager (c);
        currentManager.setDoubleBufferingEnabled (false);
    }

    /**
     * Re-enables double buffering globally.
     *
     * @param c The component to enable double buffering for.
     */
    public static void enableDoubleBuffering(Component c)
    {
        RepaintManager currentManager = RepaintManager.currentManager (c);
        currentManager.setDoubleBufferingEnabled (true);
    }
}
