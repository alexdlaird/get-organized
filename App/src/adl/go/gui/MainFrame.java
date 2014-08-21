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

import adl.go.gui.ColoredComponent.GradientStyle;
import adl.go.resource.LocalUtility;
import com.apple.eawt.Application;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

/**
 * The main frame of the stand-alone application.
 *
 * @author Alex Laird
 */
public class MainFrame extends JFrame
{
    /**
     * The minimum window size.
     */
    private final Dimension MINIMUM_SIZE = new Dimension (1000, 540);
    /**
     * The panel that is added to the frame.
     */
    private static ViewPanel viewPanel;

    /**
     * Constructs the frame for the stand-alone application.
     */
    public MainFrame()
    {
        Locale.setDefault (Locale.US);

        if (System.getProperty ("os.name").toLowerCase ().contains ("mac"))
        {
            Application macApp = Application.getApplication ();
            macApp.setDockIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "go_full.png")).getImage ());
        }

        LocalUtility utility = new LocalUtility ();
        UIManager.put ("Button.select", new Color (215, 215, 215));
        UIManager.put ("ToggleButton.select", new Color (215, 215, 215));
        UIManager.put ("TextField.selectionBackground", new Color (192, 192, 192));
        UIManager.put ("TextArea.selectionBackground", new Color (255, 255, 255));
        UIManager.put ("ToolTip.background", new Color (225, 225, 225));
        UIManager.put ("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put ("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put ("ProgressBar.foreground", new Color (185, 185, 185));
        viewPanel = new ViewPanel (this, utility);

        add (viewPanel);
        initComponents ();
        initMyComponents ();
    }

    /**
     * Initializes the frame for the stand-alone application.
     */
    private void initMyComponents()
    {
        setMinimumSize (MINIMUM_SIZE);
        applyLanguageToMenuBar ();
        addWindowListener (new WindowAdapter ()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                viewPanel.quit (true);
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
                if (viewPanel.repeatEventDialog.isVisible ())
                {
                    viewPanel.repeatEventDialog.requestFocus ();
                    viewPanel.repeatEventDialog.toFront ();
                }
                if (viewPanel.aboutDialog.isVisible ())
                {
                    viewPanel.aboutDialog.requestFocus ();
                    viewPanel.aboutDialog.toFront ();
                }
                if (viewPanel.gettingStartedDialog.isVisible ())
                {
                    viewPanel.gettingStartedDialog.requestFocus ();
                    viewPanel.gettingStartedDialog.toFront ();
                }
                if (viewPanel.gradesDialog.isVisible ())
                {
                    viewPanel.gradesDialog.requestFocus ();
                    viewPanel.gradesDialog.toFront ();
                }
                if (viewPanel.settingsDialog.isVisible ())
                {
                    viewPanel.settingsDialog.requestFocus ();
                    viewPanel.settingsDialog.toFront ();
                }
                if (viewPanel.termsAndCoursesDialog.isVisible ())
                {
                    viewPanel.termsAndCoursesDialog.requestFocus ();
                    viewPanel.termsAndCoursesDialog.toFront ();
                }
                if (viewPanel.importFromBackupDialog.isVisible ())
                {
                    viewPanel.importFromBackupDialog.requestFocus ();
                    viewPanel.importFromBackupDialog.toFront ();
                }
                if (viewPanel.updatesDialog.isVisible ())
                {
                    viewPanel.updatesDialog.requestFocus ();
                    viewPanel.updatesDialog.toFront ();
                }
                if (viewPanel.printDialog.isVisible ())
                {
                    viewPanel.printDialog.requestFocus ();
                    viewPanel.printDialog.toFront ();
                }
            }
        });

        addComponentListener (new ComponentListener ()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
                int width = e.getComponent ().getWidth ();
                int height = e.getComponent ().getHeight ();
                if (width < MINIMUM_SIZE.width)
                {
                    width = MINIMUM_SIZE.width;
                }
                if (height < MINIMUM_SIZE.height)
                {
                    height = MINIMUM_SIZE.height;
                }
                if (width > screenSize.width)
                {
                    width = screenSize.width;
                }
                if (height > screenSize.height)
                {
                    height = screenSize.height;
                }
                setSize (new Dimension (width, height));

                refreshOpenMenusAndDialogs ();

                viewPanel.adjustAssignmentTableColumnWidths ();
            }

            @Override
            public void componentMoved(ComponentEvent e)
            {
                refreshOpenMenusAndDialogs ();
            }

            @Override
            public void componentShown(ComponentEvent e)
            {
            }

            @Override
            public void componentHidden(ComponentEvent e)
            {
            }
        });

        setLocationRelativeTo (null);
        setTitle (getDefaultTitle ());
        syncFrame ();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        menuBar = new adl.go.gui.ColoredJMenuBar(GradientStyle.NO_GRADIENT, viewPanel.domain.utility.currentTheme.colorTopBackground1Panel);
        fileMenu = new javax.swing.JMenu();
        backupMenuItem = new javax.swing.JMenuItem();
        fileSeparator2 = new javax.swing.JPopupMenu.Separator();
        importFromBackupMenuItem = new javax.swing.JMenuItem();
        restoreFromBackupMenuItem = new javax.swing.JMenuItem();
        fileSeparator1 = new javax.swing.JPopupMenu.Separator();
        printMenuItem = new javax.swing.JMenuItem();
        fileSeparator3 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        termMenu = new javax.swing.JMenu();
        addTermTopMenuItem = new javax.swing.JMenuItem();
        editTermMenuItem = new javax.swing.JMenuItem();
        removeTermMenuItem = new javax.swing.JMenuItem();
        courseMenu = new javax.swing.JMenu();
        addCourseTopMenuItem = new javax.swing.JMenuItem();
        editCourseMenuItem = new javax.swing.JMenuItem();
        removeCourseMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        courseWebsiteMenuItem = new javax.swing.JMenuItem();
        labWebsiteMenuItem = new javax.swing.JMenuItem();
        courseMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        editInstructorsMenuItem = new javax.swing.JMenuItem();
        editTypesMenuItem = new javax.swing.JMenuItem();
        editTextbooksMenuItem = new javax.swing.JMenuItem();
        assignmentMenu = new javax.swing.JMenu();
        addAssignmentTopMenuItem = new javax.swing.JMenuItem();
        cloneAssignmentMenuItem = new javax.swing.JMenuItem();
        removeAssignmentMenuItem = new javax.swing.JMenuItem();
        eventMenu = new javax.swing.JMenu();
        addEventTopMenuItem = new javax.swing.JMenuItem();
        cloneEventMenuItem = new javax.swing.JMenuItem();
        removeEventMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        editCategoriesMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        viewGradesMenuItem = new javax.swing.JMenuItem();
        toolMenuSeparator2 = new javax.swing.JPopupMenu.Separator();
        termsAndCoursesMenuItem = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        donateMenuItem = new javax.swing.JMenuItem();
        gettingStartedMenuItem = new javax.swing.JMenuItem();
        helpMenuSeparator1 = new javax.swing.JPopupMenu.Separator();
        checkForUpdatesMenuItem = new javax.swing.JMenuItem();
        contactDeveloperMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon (getClass ().getResource ("/adl/go/images/go_full.png")).getImage ());
        getContentPane().setLayout(new java.awt.GridLayout(1, 0, 1, 0));

        fileMenu.setText("File");
        fileMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        fileMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                fileMenuMenuSelected(evt);
            }
        });

        backupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        backupMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        backupMenuItem.setText("Backup...");
        backupMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                backupMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(backupMenuItem);
        fileMenu.add(fileSeparator2);

        importFromBackupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        importFromBackupMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        importFromBackupMenuItem.setText("Import from Backup...");
        importFromBackupMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                importFromBackupMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importFromBackupMenuItem);

        restoreFromBackupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        restoreFromBackupMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        restoreFromBackupMenuItem.setText("Restore from Backup...");
        restoreFromBackupMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                restoreFromBackupMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(restoreFromBackupMenuItem);
        fileMenu.add(fileSeparator1);

        printMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        printMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        printMenuItem.setText("Print...");
        printMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                printMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(printMenuItem);
        fileMenu.add(fileSeparator3);

        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                quitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        termMenu.setText("Term");
        termMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        termMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                termMenuMenuSelected(evt);
            }
        });

        addTermTopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        addTermTopMenuItem.setText("Add...");
        addTermTopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addTermTopMenuItemActionPerformed(evt);
            }
        });
        termMenu.add(addTermTopMenuItem);

        editTermMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editTermMenuItem.setText("Edit...");
        editTermMenuItem.setEnabled(false);
        editTermMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editTermMenuItemActionPerformed(evt);
            }
        });
        termMenu.add(editTermMenuItem);

        removeTermMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        removeTermMenuItem.setText("Remove");
        removeTermMenuItem.setEnabled(false);
        removeTermMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeTermMenuItemActionPerformed(evt);
            }
        });
        termMenu.add(removeTermMenuItem);

        menuBar.add(termMenu);

        courseMenu.setText("Course");
        courseMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        courseMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                courseMenuMenuSelected(evt);
            }
        });

        addCourseTopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addCourseTopMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        addCourseTopMenuItem.setText("Add...");
        addCourseTopMenuItem.setEnabled(false);
        addCourseTopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addCourseTopMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(addCourseTopMenuItem);

        editCourseMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editCourseMenuItem.setText("Edit...");
        editCourseMenuItem.setEnabled(false);
        editCourseMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editCourseMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(editCourseMenuItem);

        removeCourseMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        removeCourseMenuItem.setText("Remove");
        removeCourseMenuItem.setEnabled(false);
        removeCourseMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeCourseMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(removeCourseMenuItem);
        courseMenu.add(jSeparator3);

        courseWebsiteMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        courseWebsiteMenuItem.setText("Course Website");
        courseWebsiteMenuItem.setEnabled(false);
        courseWebsiteMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                courseWebsiteMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(courseWebsiteMenuItem);

        labWebsiteMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        labWebsiteMenuItem.setText("Lab Website");
        labWebsiteMenuItem.setEnabled(false);
        labWebsiteMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                labWebsiteMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(labWebsiteMenuItem);
        courseMenu.add(courseMenuSeparator1);

        editInstructorsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_MASK));
        editInstructorsMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editInstructorsMenuItem.setText("Edit Instructors...");
        editInstructorsMenuItem.setEnabled(false);
        editInstructorsMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editInstructorsMenuItemeditGradingScaleMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(editInstructorsMenuItem);

        editTypesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK));
        editTypesMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editTypesMenuItem.setText("Edit Grading Scale...");
        editTypesMenuItem.setEnabled(false);
        editTypesMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editGradingScaleMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(editTypesMenuItem);

        editTextbooksMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        editTextbooksMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editTextbooksMenuItem.setText("Edit Textbooks...");
        editTextbooksMenuItem.setEnabled(false);
        editTextbooksMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editTextbooksMenuItemActionPerformed(evt);
            }
        });
        courseMenu.add(editTextbooksMenuItem);

        menuBar.add(courseMenu);

        assignmentMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        assignmentMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                assignmentMenuMenuSelected(evt);
            }
        });

        addAssignmentTopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        addAssignmentTopMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        addAssignmentTopMenuItem.setText("Add");
        addAssignmentTopMenuItem.setEnabled(false);
        addAssignmentTopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addAssignmentTopMenuItemActionPerformed(evt);
            }
        });
        assignmentMenu.add(addAssignmentTopMenuItem);

        cloneAssignmentMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        cloneAssignmentMenuItem.setText("Clone");
        cloneAssignmentMenuItem.setEnabled(false);
        cloneAssignmentMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cloneAssignmentMenuItemActionPerformed(evt);
            }
        });
        assignmentMenu.add(cloneAssignmentMenuItem);

        removeAssignmentMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        removeAssignmentMenuItem.setText("Remove");
        removeAssignmentMenuItem.setEnabled(false);
        removeAssignmentMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeAssignmentMenuItemActionPerformed(evt);
            }
        });
        assignmentMenu.add(removeAssignmentMenuItem);

        menuBar.add(assignmentMenu);

        eventMenu.setText("Event");
        eventMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        eventMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                eventMenuMenuSelected(evt);
            }
        });

        addEventTopMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        addEventTopMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        addEventTopMenuItem.setText("Add");
        addEventTopMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                addEventTopMenuItemActionPerformed(evt);
            }
        });
        eventMenu.add(addEventTopMenuItem);

        cloneEventMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        cloneEventMenuItem.setText("Clone");
        cloneEventMenuItem.setEnabled(false);
        cloneEventMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                cloneEventMenuItemActionPerformed(evt);
            }
        });
        eventMenu.add(cloneEventMenuItem);

        removeEventMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        removeEventMenuItem.setText("Remove");
        removeEventMenuItem.setEnabled(false);
        removeEventMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                removeEventMenuItemActionPerformed(evt);
            }
        });
        eventMenu.add(removeEventMenuItem);
        eventMenu.add(jSeparator2);

        editCategoriesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        editCategoriesMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        editCategoriesMenuItem.setText("Edit Categories...");
        editCategoriesMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                editCategoriesMenuItemActionPerformed(evt);
            }
        });
        eventMenu.add(editCategoriesMenuItem);

        menuBar.add(eventMenu);

        toolsMenu.setText("Tools");
        toolsMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        toolsMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                toolsMenuMenuSelected(evt);
            }
        });

        viewGradesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        viewGradesMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        viewGradesMenuItem.setText("Grades");
        viewGradesMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                viewGradesMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(viewGradesMenuItem);
        toolsMenu.add(toolMenuSeparator2);

        termsAndCoursesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PERIOD, java.awt.event.InputEvent.CTRL_MASK));
        termsAndCoursesMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        termsAndCoursesMenuItem.setText("Terms and Courses");
        termsAndCoursesMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                termsAndCoursesMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(termsAndCoursesMenuItem);

        settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_COMMA, java.awt.event.InputEvent.CTRL_MASK));
        settingsMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        settingsMenuItem.setText("Settings");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                settingsMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(settingsMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText("Help");
        helpMenu.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        helpMenu.addMenuListener(new javax.swing.event.MenuListener()
        {
            public void menuCanceled(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt)
            {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt)
            {
                helpMenuMenuSelected(evt);
            }
        });

        helpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        helpMenuItem.setText("Online Help");
        helpMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                helpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpMenuItem);

        donateMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        donateMenuItem.setText("Donate");
        donateMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                donateMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(donateMenuItem);

        gettingStartedMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        gettingStartedMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        gettingStartedMenuItem.setText("Getting Started");
        gettingStartedMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                gettingStartedMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(gettingStartedMenuItem);
        helpMenu.add(helpMenuSeparator1);

        checkForUpdatesMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        checkForUpdatesMenuItem.setText("Check for Updates");
        checkForUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                checkForUpdatesMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(checkForUpdatesMenuItem);

        contactDeveloperMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        contactDeveloperMenuItem.setText("Contact Developer");
        contactDeveloperMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                contactDeveloperMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(contactDeveloperMenuItem);
        helpMenu.add(jSeparator1);

        aboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        aboutMenuItem.setFont(new java.awt.Font("Verdana", 0, 11)); // NOI18N
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_quitMenuItemActionPerformed
    {//GEN-HEADEREND:event_quitMenuItemActionPerformed
        viewPanel.quit (true);
}//GEN-LAST:event_quitMenuItemActionPerformed

    private void addTermTopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addTermTopMenuItemActionPerformed
    {//GEN-HEADEREND:event_addTermTopMenuItemActionPerformed
        viewPanel.goAddTerm ();
}//GEN-LAST:event_addTermTopMenuItemActionPerformed

    private void editTermMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editTermMenuItemActionPerformed
    {//GEN-HEADEREND:event_editTermMenuItemActionPerformed
        viewPanel.goEditTerm ();
}//GEN-LAST:event_editTermMenuItemActionPerformed

    private void removeTermMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeTermMenuItemActionPerformed
    {//GEN-HEADEREND:event_removeTermMenuItemActionPerformed
        viewPanel.goRemoveTerm ();
}//GEN-LAST:event_removeTermMenuItemActionPerformed

    private void termMenuMenuSelected(javax.swing.event.MenuEvent evt)//GEN-FIRST:event_termMenuMenuSelected
    {//GEN-HEADEREND:event_termMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            addTermTopMenuItem.setEnabled (true);

            if (viewPanel.getSelectedTermIndex () != -1)
            {
                editTermMenuItem.setEnabled (true);
                removeTermMenuItem.setEnabled (true);
            }
            else
            {
                editTermMenuItem.setEnabled (false);
                removeTermMenuItem.setEnabled (false);
            }
        }
        else
        {
            for (int i = 0; i < termMenu.getMenuComponentCount (); ++i)
            {
                termMenu.getMenuComponent (i).setEnabled (false);
            }
        }
}//GEN-LAST:event_termMenuMenuSelected

    private void addCourseTopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addCourseTopMenuItemActionPerformed
    {//GEN-HEADEREND:event_addCourseTopMenuItemActionPerformed
        viewPanel.goAddCourse ();
}//GEN-LAST:event_addCourseTopMenuItemActionPerformed

    private void editCourseMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editCourseMenuItemActionPerformed
    {//GEN-HEADEREND:event_editCourseMenuItemActionPerformed
        viewPanel.goEditCourse ();
}//GEN-LAST:event_editCourseMenuItemActionPerformed

    private void removeCourseMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeCourseMenuItemActionPerformed
    {//GEN-HEADEREND:event_removeCourseMenuItemActionPerformed
        viewPanel.goRemoveCourse ();
}//GEN-LAST:event_removeCourseMenuItemActionPerformed

    private void editGradingScaleMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editGradingScaleMenuItemActionPerformed
    {//GEN-HEADEREND:event_editGradingScaleMenuItemActionPerformed
        viewPanel.goEditTypes ();
}//GEN-LAST:event_editGradingScaleMenuItemActionPerformed

    private void editTextbooksMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_editTextbooksMenuItemActionPerformed
    {//GEN-HEADEREND:event_editTextbooksMenuItemActionPerformed
        viewPanel.goEditTextbooks ();
}//GEN-LAST:event_editTextbooksMenuItemActionPerformed

    private void courseMenuMenuSelected(javax.swing.event.MenuEvent evt)//GEN-FIRST:event_courseMenuMenuSelected
    {//GEN-HEADEREND:event_courseMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            if (!viewPanel.domain.utility.terms.isEmpty ())
            {
                addCourseTopMenuItem.setEnabled (true);
            }
            else
            {
                addCourseTopMenuItem.setEnabled (false);
            }
            if (viewPanel.getSelectedCourseIndex () != -1)
            {
                editCourseMenuItem.setEnabled (true);
                removeCourseMenuItem.setEnabled (true);
                editInstructorsMenuItem.setEnabled (true);
                editTypesMenuItem.setEnabled (true);
                editTextbooksMenuItem.setEnabled (true);

                if (!viewPanel.domain.utility.courses.get (viewPanel.getSelectedCourseIndex ()).getCourseWebsite ().equals (""))
                {
                    courseWebsiteMenuItem.setEnabled (true);
                }
                else
                {
                    courseWebsiteMenuItem.setEnabled (false);
                }
                if (viewPanel.domain.utility.courses.get (viewPanel.getSelectedCourseIndex ()).hasLab ()
                    && !viewPanel.domain.utility.courses.get (viewPanel.getSelectedCourseIndex ()).getLabWebsite ().equals (""))
                {
                    labWebsiteMenuItem.setEnabled (true);
                }
                else
                {
                    labWebsiteMenuItem.setEnabled (false);
                }
            }
            else
            {
                editCourseMenuItem.setEnabled (false);
                removeCourseMenuItem.setEnabled (false);
                editInstructorsMenuItem.setEnabled (false);
                editTypesMenuItem.setEnabled (false);
                editTextbooksMenuItem.setEnabled (false);
                labWebsiteMenuItem.setEnabled (false);
                courseWebsiteMenuItem.setEnabled (false);
            }
        }
        else
        {
            for (int i = 0; i < courseMenu.getMenuComponentCount (); ++i)
            {
                courseMenu.getMenuComponent (i).setEnabled (false);
            }
        }
}//GEN-LAST:event_courseMenuMenuSelected

    private void addAssignmentTopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addAssignmentTopMenuItemActionPerformed
    {//GEN-HEADEREND:event_addAssignmentTopMenuItemActionPerformed
        viewPanel.goAddAssignment ();
}//GEN-LAST:event_addAssignmentTopMenuItemActionPerformed

    private void cloneAssignmentMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cloneAssignmentMenuItemActionPerformed
    {//GEN-HEADEREND:event_cloneAssignmentMenuItemActionPerformed
        viewPanel.goCloneAssignment ();
}//GEN-LAST:event_cloneAssignmentMenuItemActionPerformed

    private void removeAssignmentMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeAssignmentMenuItemActionPerformed
    {//GEN-HEADEREND:event_removeAssignmentMenuItemActionPerformed
        viewPanel.goRemoveAssignment ();
}//GEN-LAST:event_removeAssignmentMenuItemActionPerformed

    private void assignmentMenuMenuSelected(javax.swing.event.MenuEvent evt)//GEN-FIRST:event_assignmentMenuMenuSelected
    {//GEN-HEADEREND:event_assignmentMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            if (!viewPanel.domain.utility.courses.isEmpty ())
            {
                addAssignmentTopMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentTopMenuItem.setEnabled (false);
            }
            if (viewPanel.domain.currentIndexFromVector != -1
                && viewPanel.domain.utility.assignmentsAndEvents.get (viewPanel.domain.currentIndexFromVector).isAssignment ())
            {
                cloneAssignmentMenuItem.setEnabled (true);
                removeAssignmentMenuItem.setEnabled (true);
            }
            else
            {
                cloneAssignmentMenuItem.setEnabled (false);
                removeAssignmentMenuItem.setEnabled (false);
            }
        }
        else
        {
            for (int i = 0; i < assignmentMenu.getMenuComponentCount (); ++i)
            {
                assignmentMenu.getMenuComponent (i).setEnabled (false);
            }
        }
}//GEN-LAST:event_assignmentMenuMenuSelected

    private void viewGradesMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_viewGradesMenuItemActionPerformed
    {//GEN-HEADEREND:event_viewGradesMenuItemActionPerformed
        viewPanel.launchingGrades = true;
        viewPanel.gradesDialog.goViewGrades ();
}//GEN-LAST:event_viewGradesMenuItemActionPerformed

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_settingsMenuItemActionPerformed
    {//GEN-HEADEREND:event_settingsMenuItemActionPerformed
        viewPanel.settingsDialog.goViewSettings ();
}//GEN-LAST:event_settingsMenuItemActionPerformed

    private void checkForUpdatesMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_checkForUpdatesMenuItemActionPerformed
    {//GEN-HEADEREND:event_checkForUpdatesMenuItemActionPerformed
        viewPanel.goViewUpdates ();
}//GEN-LAST:event_checkForUpdatesMenuItemActionPerformed

    private void helpMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_helpMenuItemActionPerformed
    {//GEN-HEADEREND:event_helpMenuItemActionPerformed
        viewPanel.goViewHelp ();
}//GEN-LAST:event_helpMenuItemActionPerformed

	private void addEventTopMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_addEventTopMenuItemActionPerformed
	{//GEN-HEADEREND:event_addEventTopMenuItemActionPerformed
            viewPanel.goAddEvent ();
	}//GEN-LAST:event_addEventTopMenuItemActionPerformed

	private void cloneEventMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cloneEventMenuItemActionPerformed
	{//GEN-HEADEREND:event_cloneEventMenuItemActionPerformed
            viewPanel.goCloneEvent ();
	}//GEN-LAST:event_cloneEventMenuItemActionPerformed

	private void removeEventMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removeEventMenuItemActionPerformed
	{//GEN-HEADEREND:event_removeEventMenuItemActionPerformed
            viewPanel.goRemoveEvent (null);
	}//GEN-LAST:event_removeEventMenuItemActionPerformed

	private void eventMenuMenuSelected(javax.swing.event.MenuEvent evt)//GEN-FIRST:event_eventMenuMenuSelected
	{//GEN-HEADEREND:event_eventMenuMenuSelected
            if (!viewPanel.initLoading)
            {
                addEventTopMenuItem.setEnabled (true);
                if (viewPanel.domain.currentIndexFromVector != -1
                    && !viewPanel.domain.utility.assignmentsAndEvents.get (viewPanel.domain.currentIndexFromVector).isAssignment ())
                {
                    cloneEventMenuItem.setEnabled (true);
                    removeEventMenuItem.setEnabled (true);
                }
                else
                {
                    cloneEventMenuItem.setEnabled (false);
                    removeEventMenuItem.setEnabled (false);
                }
            }
            else
            {
                for (int i = 0; i < eventMenu.getMenuComponentCount (); ++i)
                {
                    eventMenu.getMenuComponent (i).setEnabled (false);
                }
            }
	}//GEN-LAST:event_eventMenuMenuSelected

    private void contactDeveloperMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_contactDeveloperMenuItemActionPerformed
    {//GEN-HEADEREND:event_contactDeveloperMenuItemActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("http://alexlaird.com/projects/get-organized/get-organized-contact/"));
            }
            catch (Exception ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            try
            {
                Domain.desktop.mail (new URI ("mailto", "getorganized@alexlaird.com?subject=" + Domain.NAME + " " + Domain.VERSION, null));
            }
            catch (URISyntaxException ex)
            {
                Domain.LOGGER.add (ex);
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
    }//GEN-LAST:event_contactDeveloperMenuItemActionPerformed

    private void gettingStartedMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_gettingStartedMenuItemActionPerformed
    {//GEN-HEADEREND:event_gettingStartedMenuItemActionPerformed
        viewPanel.gettingStartedDialog.showGettingStartedDialog ();
    }//GEN-LAST:event_gettingStartedMenuItemActionPerformed

    private void backupMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_backupMenuItemActionPerformed
    {//GEN-HEADEREND:event_backupMenuItemActionPerformed
        viewPanel.backup ();
    }//GEN-LAST:event_backupMenuItemActionPerformed

    private void restoreFromBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_restoreFromBackupMenuItemActionPerformed
    {//GEN-HEADEREND:event_restoreFromBackupMenuItemActionPerformed
        viewPanel.restoreFromBackup ();
    }//GEN-LAST:event_restoreFromBackupMenuItemActionPerformed

    private void fileMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_fileMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            for (int i = 0; i < fileMenu.getMenuComponentCount (); ++i)
            {
                fileMenu.getMenuComponent (i).setEnabled (true);
            }
        }
        else
        {
            for (int i = 0; i < fileMenu.getMenuComponentCount (); ++i)
            {
                fileMenu.getMenuComponent (i).setEnabled (false);
            }
        }
    }//GEN-LAST:event_fileMenuMenuSelected

    private void toolsMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_toolsMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            for (int i = 0; i < toolsMenu.getMenuComponentCount (); ++i)
            {
                toolsMenu.getMenuComponent (i).setEnabled (true);
            }
        }
        else
        {
            for (int i = 0; i < toolsMenu.getMenuComponentCount (); ++i)
            {
                toolsMenu.getMenuComponent (i).setEnabled (false);
            }
        }
    }//GEN-LAST:event_toolsMenuMenuSelected

    private void helpMenuMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_helpMenuMenuSelected
        if (!viewPanel.initLoading)
        {
            for (int i = 0; i < helpMenu.getMenuComponentCount (); ++i)
            {
                helpMenu.getMenuComponent (i).setEnabled (true);
            }
        }
        else
        {
            for (int i = 0; i < helpMenu.getMenuComponentCount (); ++i)
            {
                helpMenu.getMenuComponent (i).setEnabled (false);
            }
        }
    }//GEN-LAST:event_helpMenuMenuSelected

    private void editInstructorsMenuItemeditGradingScaleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editInstructorsMenuItemeditGradingScaleMenuItemActionPerformed
        viewPanel.goEditInstructors ();
    }//GEN-LAST:event_editInstructorsMenuItemeditGradingScaleMenuItemActionPerformed

    private void editCategoriesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCategoriesMenuItemActionPerformed
        viewPanel.goEditCategories ();
    }//GEN-LAST:event_editCategoriesMenuItemActionPerformed

    private void termsAndCoursesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termsAndCoursesMenuItemActionPerformed
        viewPanel.termsAndCoursesDialog.goViewTermsAndCourses ();
    }//GEN-LAST:event_termsAndCoursesMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        viewPanel.aboutDialog.goViewAbout ();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void labWebsiteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labWebsiteMenuItemActionPerformed
        viewPanel.visitLabWebsite ();
    }//GEN-LAST:event_labWebsiteMenuItemActionPerformed

    private void courseWebsiteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseWebsiteMenuItemActionPerformed
        viewPanel.visitCourseWebsite ();
    }//GEN-LAST:event_courseWebsiteMenuItemActionPerformed

    private void importFromBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importFromBackupMenuItemActionPerformed
        viewPanel.importFromBackup ();
    }//GEN-LAST:event_importFromBackupMenuItemActionPerformed

    private void printMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printMenuItemActionPerformed
        viewPanel.printGetOrganized ();
    }//GEN-LAST:event_printMenuItemActionPerformed

    private void donateMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_donateMenuItemActionPerformed
    {//GEN-HEADEREND:event_donateMenuItemActionPerformed
        viewPanel.goDonate ();
    }//GEN-LAST:event_donateMenuItemActionPerformed

    /**
     * Apply language settings to the menu bar.
     */
    protected final void applyLanguageToMenuBar()
    {
        // menu bar
        fileMenu.setText (viewPanel.domain.language.getString ("file"));
        termMenu.setText (viewPanel.domain.language.getString ("term"));
        courseMenu.setText (viewPanel.domain.language.getString ("course"));
        assignmentMenu.setText (viewPanel.domain.language.getString ("assignment"));
        eventMenu.setText (viewPanel.domain.language.getString ("event"));
        toolsMenu.setText (viewPanel.domain.language.getString ("tools"));
        helpMenu.setText (viewPanel.domain.language.getString ("help"));

        // file menu
        backupMenuItem.setText (viewPanel.domain.language.getString ("backup") + "...");
        importFromBackupMenuItem.setText (viewPanel.domain.language.getString ("importFromBackup") + "...");
        restoreFromBackupMenuItem.setText (viewPanel.domain.language.getString ("restoreFromBackup") + "...");
        printMenuItem.setText (viewPanel.domain.language.getString ("print") + "...");
        quitMenuItem.setText (viewPanel.domain.language.getString ("quit"));
        // term menu
        addTermTopMenuItem.setText (viewPanel.domain.language.getString ("add") + "...");
        editTermMenuItem.setText (viewPanel.domain.language.getString ("edit") + "...");
        removeTermMenuItem.setText (viewPanel.domain.language.getString ("remove"));
        // course menu
        addCourseTopMenuItem.setText (viewPanel.domain.language.getString ("add") + "...");
        editCourseMenuItem.setText (viewPanel.domain.language.getString ("edit") + "...");
        removeCourseMenuItem.setText (viewPanel.domain.language.getString ("remove"));
        courseWebsiteMenuItem.setText (viewPanel.domain.language.getString ("courseWebsite"));
        labWebsiteMenuItem.setText (viewPanel.domain.language.getString ("labWebsite"));
        editInstructorsMenuItem.setText (viewPanel.domain.language.getString ("editInstructors") + "...");
        editTypesMenuItem.setText (viewPanel.domain.language.getString ("editGradingScale") + "...");
        editTextbooksMenuItem.setText (viewPanel.domain.language.getString ("editTextbooks") + "...");
        // assignment menu
        addAssignmentTopMenuItem.setText (viewPanel.domain.language.getString ("add"));
        cloneAssignmentMenuItem.setText (viewPanel.domain.language.getString ("clone"));
        removeAssignmentMenuItem.setText (viewPanel.domain.language.getString ("remove"));
        // event menu
        addEventTopMenuItem.setText (viewPanel.domain.language.getString ("add"));
        cloneEventMenuItem.setText (viewPanel.domain.language.getString ("clone"));
        removeEventMenuItem.setText (viewPanel.domain.language.getString ("remove"));
        editCategoriesMenuItem.setText (viewPanel.domain.language.getString ("editCategories") + "...");
        // tools menu
        viewGradesMenuItem.setText (viewPanel.domain.language.getString ("grades"));
        termsAndCoursesMenuItem.setText (viewPanel.domain.language.getString ("termsAndCourses"));
        settingsMenuItem.setText (viewPanel.domain.language.getString ("settings"));
        // help menu
        helpMenuItem.setText (viewPanel.domain.language.getString ("onlineHelp"));
        donateMenuItem.setText (viewPanel.domain.language.getString ("donate"));
        gettingStartedMenuItem.setText (viewPanel.domain.language.getString ("gettingStarted"));
        checkForUpdatesMenuItem.setText (viewPanel.domain.language.getString ("checkForUpdates"));
        contactDeveloperMenuItem.setText (viewPanel.domain.language.getString ("contactDeveloper"));
        aboutMenuItem.setText (viewPanel.domain.language.getString ("about"));
    }

    /**
     * Synchronize the frame preferences with the user's preferences.
     */
    protected final void syncFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();

        if (viewPanel.domain.utility.preferences.width > 0 && viewPanel.domain.utility.preferences.height > 0)
        {
            if (viewPanel.domain.utility.preferences.width > screenSize.width - 20)
            {
                viewPanel.domain.utility.preferences.width = screenSize.width - 20;
            }
            if (viewPanel.domain.utility.preferences.height > screenSize.height - 20)
            {
                viewPanel.domain.utility.preferences.height = screenSize.height - 20;
            }
        }

        if (viewPanel.domain.utility.preferences.x > 0 && viewPanel.domain.utility.preferences.y > 0)
        {
            if (viewPanel.domain.utility.preferences.x + viewPanel.domain.utility.preferences.width > screenSize.width - 20)
            {
                viewPanel.domain.utility.preferences.x = 10;
            }
            if (viewPanel.domain.utility.preferences.y + viewPanel.domain.utility.preferences.height > screenSize.height - 20)
            {
                viewPanel.domain.utility.preferences.y = 10;
            }
            setBounds (viewPanel.domain.utility.preferences.x, viewPanel.domain.utility.preferences.y,
                       viewPanel.domain.utility.preferences.width, viewPanel.domain.utility.preferences.height);
        }
        else
        {

            setPreferredSize (new Dimension (viewPanel.domain.utility.preferences.width, viewPanel.domain.utility.preferences.height));
            setLocationRelativeTo (null);
        }
    }

    /**
     * Returns the default title for the application.
     *
     * @return The default title for the application.
     */
    private String getDefaultTitle()
    {
        String title = Domain.NAME;
        if (Domain.VERSION.toLowerCase ().contains ("beta") || Domain.VERSION.toLowerCase ().contains ("alpha"))
        {
            title += " " + Domain.VERSION;
        }
        return title;
    }

    /**
     * Refreshes the locations of the open menus and dialogs on the screen.
     */
    private void refreshOpenMenusAndDialogs()
    {
        if (viewPanel.addPopupMenu.isShowing ())
        {
            viewPanel.addPopupMenu.show (viewPanel.addButton, 0, viewPanel.addButton.getHeight ());
        }
        if (viewPanel.repeatEventDialog.isVisible ())
        {
            viewPanel.showRepeatEventDialog ();
        }
        if (viewPanel.gettingStartedDialog.isVisible ())
        {
            viewPanel.gettingStartedDialog.showGettingStartedDialog ();
        }
        if (viewPanel.updatesDialog.isVisible ())
        {
            viewPanel.updatesDialog.pack ();
            viewPanel.updatesDialog.setLocationRelativeTo (this);
            viewPanel.updatesDialog.setVisible (true);
        }
    }

    /**
     * Apply utility fonts to the menu bar.
     */
    protected void applyMenuBarFonts()
    {
        for (int i = 0; i < menuBar.getComponentCount (); ++i)
        {
            JMenu menu = (JMenu) viewPanel.mainFrame.menuBar.getComponent (i);
            menu.setFont (viewPanel.domain.utility.currentTheme.fontPlain11);
            for (int j = 0; j < menu.getMenuComponentCount (); ++j)
            {
                if (menu.getMenuComponent (j) instanceof JMenuItem)
                {
                    ((JMenuItem) menu.getMenuComponent (j)).setFont (viewPanel.domain.utility.currentTheme.fontPlain11);
                }
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    protected javax.swing.JMenuItem addAssignmentTopMenuItem;
    protected javax.swing.JMenuItem addCourseTopMenuItem;
    protected javax.swing.JMenuItem addEventTopMenuItem;
    protected javax.swing.JMenuItem addTermTopMenuItem;
    protected javax.swing.JMenu assignmentMenu;
    private javax.swing.JMenuItem backupMenuItem;
    private javax.swing.JMenuItem checkForUpdatesMenuItem;
    protected javax.swing.JMenuItem cloneAssignmentMenuItem;
    protected javax.swing.JMenuItem cloneEventMenuItem;
    private javax.swing.JMenuItem contactDeveloperMenuItem;
    protected javax.swing.JMenu courseMenu;
    private javax.swing.JPopupMenu.Separator courseMenuSeparator1;
    protected javax.swing.JMenuItem courseWebsiteMenuItem;
    private javax.swing.JMenuItem donateMenuItem;
    private javax.swing.JMenuItem editCategoriesMenuItem;
    protected javax.swing.JMenuItem editCourseMenuItem;
    protected javax.swing.JMenuItem editInstructorsMenuItem;
    protected javax.swing.JMenuItem editTermMenuItem;
    protected javax.swing.JMenuItem editTextbooksMenuItem;
    protected javax.swing.JMenuItem editTypesMenuItem;
    protected javax.swing.JMenu eventMenu;
    protected javax.swing.JMenu fileMenu;
    private javax.swing.JPopupMenu.Separator fileSeparator1;
    private javax.swing.JPopupMenu.Separator fileSeparator2;
    private javax.swing.JPopupMenu.Separator fileSeparator3;
    private javax.swing.JMenuItem gettingStartedMenuItem;
    protected javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem helpMenuItem;
    private javax.swing.JPopupMenu.Separator helpMenuSeparator1;
    private javax.swing.JMenuItem importFromBackupMenuItem;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    protected javax.swing.JMenuItem labWebsiteMenuItem;
    protected adl.go.gui.ColoredJMenuBar menuBar;
    private javax.swing.JMenuItem printMenuItem;
    private javax.swing.JMenuItem quitMenuItem;
    protected javax.swing.JMenuItem removeAssignmentMenuItem;
    protected javax.swing.JMenuItem removeCourseMenuItem;
    protected javax.swing.JMenuItem removeEventMenuItem;
    protected javax.swing.JMenuItem removeTermMenuItem;
    private javax.swing.JMenuItem restoreFromBackupMenuItem;
    private javax.swing.JMenuItem settingsMenuItem;
    protected javax.swing.JMenu termMenu;
    private javax.swing.JMenuItem termsAndCoursesMenuItem;
    private javax.swing.JPopupMenu.Separator toolMenuSeparator2;
    protected javax.swing.JMenu toolsMenu;
    protected javax.swing.JMenuItem viewGradesMenuItem;
    // End of variables declaration//GEN-END:variables
}
