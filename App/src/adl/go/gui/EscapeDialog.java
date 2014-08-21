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

import adl.go.gui.colorpicker.ColorPickerDialog;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * Creates a standard JDialog with the extension that closes when the escape key
 * is pressed.
 *
 * @author Alex Laird
 */
public class EscapeDialog extends JDialog
{
    /**
     * A reference to the main panel of the application.
     */
    protected ViewPanel viewPanel;

    /**
     * Constructs a standard Escape Dialog.
     */
    public EscapeDialog()
    {
        super ();
    }

    /**
     * Constructs an Escape Dialog with a dialog as its parent.
     *
     * @param parent The dialog to be the parent.
     */
    public EscapeDialog(JDialog parent)
    {
        super (parent);
    }

    /**
     * Constructs an Escape Dialog with a frame as its parent.
     *
     * @param parent The dialog to be the parent.
     */
    public EscapeDialog(Frame parent)
    {
        super (parent);
    }

    /**
     * Constructs an Escape Dialog with a JFrame as its parent.
     *
     * @param parent The dialog to be the parent.
     */
    public EscapeDialog(JFrame parent)
    {
        super (parent);
    }

    /**
     * Constructs an Escape Dialog with a AWT dialog as its parent.
     *
     * @param parent The dialog to be the parent.
     */
    public EscapeDialog(Dialog parent)
    {
        super (parent);
    }

    /**
     * Sets a reference to the main panel of the escape dialog.
     *
     * @param viewPanel The main panel of the escape dialog.
     */
    public void setMainPanel(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
    }

    /**
     * Overrides the root pane method to create it in the same way but with the
     * added functionality that supports an escape key listener. This allows the
     * dialog to perform some exit operations when escape is pressed prior to
     * disposing the dialog, and this also allows the escape key to act the same
     * way as pressing the close button.
     *
     * @return The root pane.
     */
    @Override
    protected JRootPane createRootPane()
    {
        Action actionListener = new AbstractAction ()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                if (viewPanel.settingsDialog.isVisible () && !(((JRootPane) actionEvent.getSource ()).getParent () instanceof ColorPickerDialog))
                {
                    viewPanel.settingsDialog.closeSettingsDialog ();
                }
                else if (viewPanel.termsAndCoursesDialog.isVisible () && !(((JRootPane) actionEvent.getSource ()).getParent () instanceof ColorPickerDialog))
                {
                    viewPanel.termsAndCoursesDialog.closeTermsAndCoursesDialog ();
                }
                else if (viewPanel.repeatEventDialog.isVisible ())
                {
                    viewPanel.closeRepeatEventDialog ();
                }
                else if (viewPanel.importFromBackupDialog.isVisible ())
                {
                    viewPanel.importFromBackupDialog.cancel = true;
                    ((JDialog) rootPane.getParent ()).dispose ();
                }
                else
                {
                    ((JDialog) rootPane.getParent ()).dispose ();
                }
                if (!(((JRootPane) actionEvent.getSource ()).getParent () instanceof ColorPickerDialog))
                {
                    viewPanel.setEnabled (true);
                    viewPanel.requestFocus ();
                }
            }
        };
        rootPane = new JRootPane ();
        KeyStroke stroke = KeyStroke.getKeyStroke ("ESCAPE");
        InputMap inputMap = rootPane.getInputMap (JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put (stroke, "ESCAPE");
        rootPane.getActionMap ().put ("ESCAPE", actionListener);

        return rootPane;
    }
}
