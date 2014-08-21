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

import adl.go.gui.Domain;
import adl.go.gui.MainFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The launcher for Get Organized.
 *
 * @author Alex Laird
 */
public class GetOrganized
{
    /**
     * The method responsible for constructing the visual frame and maintaining
     * the thread as long as the frame is open.
     *
     * @param args The command-line arguments.
     */
    public static void main(String args[])
    {
        // throw any uncaught exceptions to the logger
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler ()
        {
            @Override
            public void uncaughtException(Thread t, Throwable e)
            {
                Domain.LOGGER.add (e);
            }
        });

        // define UI characteristics before the applicaiton is instantiated
        try
        {
            UIManager.setLookAndFeel (UIManager.getCrossPlatformLookAndFeelClassName ());
        }
        catch (ClassNotFoundException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (InstantiationException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (IllegalAccessException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (UnsupportedLookAndFeelException ex)
        {
            Domain.LOGGER.add (ex);
        }

        EventQueue.invokeLater (new Runnable ()
        {
            @Override
            public void run()
            {
                try
                {
                    new MainFrame ().setVisible (true);
                }
                catch (Exception ex)
                {
                    UIManager.put ("OptionPane.font", new Font ("Verdana", Font.PLAIN, 12));
                    UIManager.put ("OptionPane.messageFont", new Font ("Verdana", Font.PLAIN, 12));
                    UIManager.put ("OptionPane.buttonFont", new Font ("Verdana", Font.PLAIN, 12));

                    /**
                     * The option pane which can be customized to have yes/no,
                     * ok/cancel, or just ok buttons in it.
                     */
                    final JOptionPane optionPane = new JOptionPane ();
                    JButton okButton = new JButton ("Ok");
                    okButton.setBackground (new Color (245, 245, 245));
                    okButton.setFont (new Font ("Verdana", Font.PLAIN, 12));
                    okButton.addActionListener (new ActionListener ()
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            optionPane.setValue (new Integer (JOptionPane.OK_OPTION));
                        }
                    });

                    System.out.println ("Error: " + ex.getClass ());
                    System.out.println ("Message: " + ex.getMessage () + "\n--\nTrace:");
                    Object[] trace = ex.getStackTrace ();
                    for (int j = 0; j < trace.length; ++j)
                    {
                        System.out.println ("  " + trace[j].toString ());
                    }
                    System.out.println ();

                    optionPane.setOptions (new Object[]
                            {
                                okButton
                            });
                    optionPane.setMessage ("A fatal error occured while launching Get Organized. Your course data\n"
                                           + "should be safe.  Visit alexlaird.com to see if an update\n"
                                           + "is available to resolve this issue, otherwise contact the developer.");
                    optionPane.setMessageType (JOptionPane.ERROR_MESSAGE);
                    JDialog optionDialog = optionPane.createDialog ("Fatal Error");
                    optionDialog.setVisible (true);

                    System.exit (1);
                }
            }
        });
    }
}
