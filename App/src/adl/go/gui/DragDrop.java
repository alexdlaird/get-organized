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

import adl.go.types.Event;
import adl.go.types.ListItem;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * The drag and drop class handles the construction and destruction of drag/drop
 * events, but its functionality is limited to assignment and event objects.
 *
 * @author Alex Laird
 */
public class DragDrop implements DragGestureListener, DragSourceListener, DropTargetListener, Transferable
{
    /**
     * A reference to the view panel.
     */
    private ViewPanel viewPanel;
    /**
     * The supported flavors for dragging and dropping.
     */
    private static final DataFlavor[] supportedFlavors =
    {
        null
    };

    /**
     * Set the main supported flavor for dragging and dropping.
     */
    static
    {
        try
        {
            supportedFlavors[0] = new DataFlavor (DataFlavor.javaJVMLocalObjectMimeType);
        }
        catch (Exception ex)
        {
        }
    }
    /**
     * The object being dragged.
     */
    private Object object;
    private int response1 = -1;
    private int response2 = -1;
    private boolean repeat = false;

    /**
     * Construct the drag and drop class with a reference to the view panel.
     *
     * @param viewPanel The reference to the view panel.
     */
    public DragDrop(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
    }

    // Transferable methods.
    @Override
    public Object getTransferData(DataFlavor flavor)
    {
        if (flavor.isMimeTypeEqual (DataFlavor.javaJVMLocalObjectMimeType))
        {
            return object;
        }
        else
        {
            return null;
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return supportedFlavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        return flavor.isMimeTypeEqual (DataFlavor.javaJVMLocalObjectMimeType);
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent ev)
    {
        try
        {
            response1 = viewPanel.checkAssignmentOrEventChanges (viewPanel.domain.currentIndexFromVector);
            response2 = viewPanel.checkRepeatEventChanges (viewPanel.domain.currentIndexFromVector);
            if (response1 != -1 || response2 != -1)
            {
                repeat = true;
            }

            ev.startDrag (null, this, this);
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public void dragDropEnd(DragSourceDropEvent ev)
    {
    }

    @Override
    public void dragEnter(DragSourceDragEvent ev)
    {
    }

    @Override
    public void dragExit(DragSourceEvent ev)
    {
    }

    @Override
    public void dragOver(DragSourceDragEvent ev)
    {
        object = ev.getSource ();
    }

    @Override
    public void dropActionChanged(DragSourceDragEvent ev)
    {
    }

    @Override
    public void dragEnter(DropTargetDragEvent ev)
    {
    }

    @Override
    public void dragExit(DropTargetEvent ev)
    {
    }

    @Override
    public void dragOver(DropTargetDragEvent ev)
    {
        dropTargetDrag (ev);
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent ev)
    {
        dropTargetDrag (ev);
    }

    void dropTargetDrag(DropTargetDragEvent ev)
    {
        ev.acceptDrag (ev.getDropAction ());
    }

    @Override
    public void drop(DropTargetDropEvent ev)
    {
        ev.acceptDrop (ev.getDropAction ());
        try
        {
            if (response1 == -1 && response2 == -1)
            {
                if (!repeat)
                {
                    JPanel panel = (JPanel) ((DropTarget) ev.getSource ()).getComponent ();
                    JPanel targetPanel = (JPanel) ((JViewport) ((JScrollPane) panel.getComponent (1)).getComponent (0)).getComponent (0);
                    Object source = ev.getTransferable ().getTransferData (supportedFlavors[0]);

                    ListItem item = (ListItem) ((DragSourceContext) source).getComponent ().getParent ();

                    Calendar shownCal = viewPanel.miniCalendar.getCalendar ();
                    int shownMonth = shownCal.get (Calendar.MONTH);

                    int dayIndex = viewPanel.getIndexFromDaysArray ((JPanel) ((JScrollPane) ((JViewport) targetPanel.getParent ()).getParent ()).getParent ());
                    viewPanel.assignmentsTable.setSelectedRowFromVectorIndex (viewPanel.domain.utility.getAssignmentOrEventIndexByID (item.getUniqueID ()));

                    if (item instanceof Event)
                    {
                        Calendar cal = viewPanel.eventDateChooser.getCalendar ();

                        int dueMonth = Integer.parseInt (item.getDueDate ().split ("/")[0]);
                        if ((dueMonth == shownMonth - 1 && dueMonth - 1 != shownMonth)
                            || (dueMonth == shownMonth + 11 && dueMonth - 1 != shownMonth))
                        {
                            String day = ((JLabel) panel.getComponent (0)).getText ();
                            int dayNum = Integer.parseInt (day);
                            cal.set (Calendar.DAY_OF_MONTH, dayNum);
                            viewPanel.eventDateChooser.setDate (cal.getTime ());
                        }
                        else if ((dueMonth == shownMonth + 1 && dueMonth - 1 != shownMonth)
                                 || (dueMonth == shownMonth - 11 && dueMonth - 1 != shownMonth))
                        {
                            String day = ((JLabel) panel.getComponent (0)).getText ();
                            int dayNum = Integer.parseInt (day);
                            cal.set (Calendar.DAY_OF_MONTH, dayNum);
                            viewPanel.eventDateChooser.setDate (cal.getTime ());
                        }
                        else
                        {

                            if (cal.get (Calendar.MONTH) == shownMonth - 1 || cal.get (Calendar.MONTH) == shownMonth + 11)
                            {
                                cal.add (Calendar.MONTH, 1);
                            }
                            else if (cal.get (Calendar.MONTH) == shownMonth + 1 || cal.get (Calendar.MONTH) == shownMonth - 11)
                            {
                                cal.add (Calendar.MONTH, -1);
                            }

                            if (dayIndex != -1)
                            {
                                cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
                                viewPanel.eventDateChooser.setDate (cal.getTime ());
                            }
                            else
                            {
                                String day = ((JLabel) panel.getComponent (0)).getText ();
                                int dayNum = Integer.parseInt (day);
                                if (dayNum > 7)
                                {
                                    cal.add (Calendar.MONTH, -1);
                                }
                                else
                                {
                                    cal.add (Calendar.MONTH, 1);
                                }
                                cal.set (Calendar.DAY_OF_MONTH, dayNum);
                                viewPanel.eventDateChooser.setDate (cal.getTime ());
                            }
                        }
                    }
                    else
                    {
                        Calendar cal = viewPanel.dueDateChooser.getCalendar ();

                        int dueMonth = Integer.parseInt (item.getDueDate ().split ("/")[0]);
                        if ((dueMonth == shownMonth - 1 && dueMonth - 1 != shownMonth)
                            || (dueMonth == shownMonth + 11 && dueMonth - 1 != shownMonth))
                        {
                            String day = ((JLabel) panel.getComponent (0)).getText ();
                            int dayNum = Integer.parseInt (day);
                            cal.set (Calendar.DAY_OF_MONTH, dayNum);
                            viewPanel.eventDateChooser.setDate (cal.getTime ());
                        }
                        else if ((dueMonth == shownMonth + 1 && dueMonth - 1 != shownMonth)
                                 || (dueMonth == shownMonth - 11 && dueMonth - 1 != shownMonth))
                        {
                            String day = ((JLabel) panel.getComponent (0)).getText ();
                            int dayNum = Integer.parseInt (day);
                            cal.set (Calendar.DAY_OF_MONTH, dayNum);
                            viewPanel.eventDateChooser.setDate (cal.getTime ());
                        }
                        else
                        {
                            if (cal.get (Calendar.MONTH) == shownMonth - 1 || cal.get (Calendar.MONTH) == shownMonth + 11)
                            {
                                cal.add (Calendar.MONTH, 1);
                            }
                            else if (cal.get (Calendar.MONTH) == shownMonth + 1 || cal.get (Calendar.MONTH) == shownMonth - 11)
                            {
                                cal.add (Calendar.MONTH, -1);
                            }

                            if (dayIndex != -1)
                            {
                                cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
                                viewPanel.dueDateChooser.setDate (cal.getTime ());
                            }
                            else
                            {
                                String day = ((JLabel) panel.getComponent (0)).getText ();
                                int dayNum = Integer.parseInt (day);
                                if (dayNum > 7)
                                {
                                    cal.add (Calendar.MONTH, -1);
                                }
                                else
                                {
                                    cal.add (Calendar.MONTH, 1);
                                }
                                cal.set (Calendar.DAY_OF_MONTH, dayNum);
                                viewPanel.dueDateChooser.setDate (cal.getTime ());
                            }
                        }
                    }

                    viewPanel.scrollToItemOrToday (item);
                }
                repeat = false;
            }
        }
        catch (UnsupportedFlavorException ex)
        {
        }
        catch (IOException ex)
        {
        }

        ev.dropComplete (true);
    }
}
