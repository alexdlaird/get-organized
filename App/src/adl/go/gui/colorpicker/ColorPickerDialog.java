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

import adl.go.gui.EscapeDialog;
import adl.go.gui.ViewPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * This wraps a
 * <code>ColorPicker</code> in a simple dialog with "OK" and "Cancel" options.
 * <P>(This object is used by the static calls in
 * <code>ColorPicker</code> to show a dialog.)
 *
 */
public class ColorPickerDialog extends EscapeDialog
{
    private static final long serialVersionUID = 1L;
    ColorPicker cp;
    int alpha;
    JButton ok = new JButton ("Ok");
    JButton cancel = new JButton ("Cancel");
    Color returnValue = null;
    ActionListener buttonListener = new ActionListener ()
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Object src = e.getSource ();
            if (src == ok)
            {
                returnValue = cp.getColor ();
            }
            setVisible (false);
        }
    };

    public ColorPickerDialog(Frame owner, Color color, boolean includeOpacity, ViewPanel viewPanel)
    {
        super (owner);
        setMainPanel (viewPanel);
        initialize (owner, color, includeOpacity);
    }

    public ColorPickerDialog(Dialog owner, Color color, boolean includeOpacity, ViewPanel viewPanel)
    {
        super (owner);
        setMainPanel (viewPanel);
        initialize (owner, color, includeOpacity);
    }

    private void initialize(Component owner, Color color, boolean includeOpacity)
    {
        cp = new ColorPicker (true, includeOpacity);
        setModal (true);
        setResizable (false);
        getContentPane ().setLayout (new GridBagLayout ());
        GridBagConstraints c = new GridBagConstraints ();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets (10, 10, 10, 10);
        getContentPane ().add (cp, c);
        c.gridy++;
        c.gridwidth = 1;
        getContentPane ().add (new JPanel (), c);
        c.gridx++;
        c.weightx = 0;
        getContentPane ().add (cancel, c);
        c.gridx++;
        c.weightx = 0;
        getContentPane ().add (ok, c);
        cp.setRGB (color.getRed (), color.getGreen (), color.getBlue ());
        cp.setOpacity (((float) color.getAlpha ()) / 255f);
        alpha = color.getAlpha ();

        ok.setFont (new Font ("Verdana", Font.PLAIN, 13));
        ok.setBackground (new Color (246, 245, 245));
        cancel.setFont (new Font ("Verdana", Font.PLAIN, 13));
        cancel.setBackground (new Color (246, 245, 245));

        pack ();
        setLocationRelativeTo (owner);

        ok.addActionListener (buttonListener);
        cancel.addActionListener (buttonListener);

        getRootPane ().setDefaultButton (ok);
    }

    /**
     * @return the color committed when the user clicked 'OK'. Note this
     * returns <code>null</code> if the user canceled this dialog, or exited via
     * the close decoration.
     */
    public Color getColor()
    {
        return returnValue;
    }
}
