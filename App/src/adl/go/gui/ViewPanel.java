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
import adl.go.resource.Updater;
import adl.go.types.Assignment;
import adl.go.types.AssignmentType;
import adl.go.types.Course;
import adl.go.types.Event;
import adl.go.types.Instructor;
import adl.go.types.ListItem;
import adl.go.types.Repeating;
import adl.go.types.Term;
import adl.go.types.Textbook;
import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.AppEvent.PreferencesEvent;
import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.Application;
import com.apple.eawt.PreferencesHandler;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Keep your Course schedule and grades organized with Get Organized, a simple
 * and efficient academic planner. Specify Assignment details, keep track of
 * up-and-coming and overdue schoolwork, monitor grades, and more. View your
 * schedule as a collective list or in a familiar calendar format.
 *
 * Get Organized is a free, digital planner that eliminates the hassle and cost
 * of a physical planner. See the list of Key Features below that will help the
 * punctual to stay on top of their schedule and the procrastinator to stay on
 * track.
 *
 * The view of this application handles all UI-specific interactions between the
 * user and the domain of the application, which should contain direct
 * interaction with the back-end.
 *
 * @author Alex Laird
 */
public class ViewPanel extends JPanel
{
    /**
     * The option pane which can be customized to have yes/no, ok/cancel, or
     * just ok buttons in it.
     */
    public static final JOptionPane OPTION_PANE = new JOptionPane ();
    /**
     * The "Only this instance" button for repeat events.
     */
    public final Integer ONLY_THIS_INSTANCE_OPTION = new Integer (30);
    /**
     * The "All following" button for repeat events.
     */
    public final Integer ALL_FOLLOWING_OPTION = new Integer (31);
    /**
     * The "All in series" button for repeat events.
     */
    public final Integer ALL_IN_SERIES_OPTION = new Integer (32);
    /**
     * The yes button for the option dialog.
     */
    public final JButton YES_OPTION_BUTTON = new JButton ("Yes");
    /**
     * The no button for the option dialog.
     */
    public final JButton NO_OPTION_BUTTON = new JButton ("No");
    /**
     * The OK button for the option dialog.
     */
    public final JButton OK_OPTION_BUTTON = new JButton ("Ok");
    /**
     * The I understand button for the option dialog.
     */
    public final JButton I_UNDERSTAND_OPTION_BUTTON = new JButton ("I understand");
    /**
     * The cancel button for the option dialog.
     */
    public final JButton CANCEL_OPTION_BUTTON = new JButton ("Cancel");
    /**
     * The "Only this instance" button for repeating events.
     */
    public final JButton ONLY_THIS_INSTANCE_BUTTON = new JButton ("Only this instance");
    /**
     * The "All in series" button for repeating events.
     */
    public final JButton ALL_IN_SERIES_BUTTON = new JButton ("All in series");
    /**
     * The "All following" button for repeating events.
     */
    public final JButton ALL_FOLLOWING_BUTTON = new JButton ("This and all following");
    /**
     * The "All following" button for repeating events.
     */
    public final JButton REPLACE_OPTION_BUTTON = new JButton ("Replace");
    /**
     * The object which contains the merge, replace, and cancel buttons.
     */
    public final Object[] REPLACE_CANCEL_CHOICES = new Object[]
    {
        REPLACE_OPTION_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The object which contains the yes and no buttons for the option pane.
     */
    public final Object[] YES_NO_CHOICES = new Object[]
    {
        YES_OPTION_BUTTON, NO_OPTION_BUTTON
    };
    /**
     * The object which contains the yes, no, and cancel buttons.
     */
    public final Object[] YES_NO_CANCEL_CHOICES = new Object[]
    {
        YES_OPTION_BUTTON, NO_OPTION_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The object which contains the ok and cancel buttons for the option pane.
     */
    public final Object[] OK_CANCEL_CHOICES = new Object[]
    {
        OK_OPTION_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The object which contains only the ok button for the option pane.
     */
    public final Object[] OK_CHOICE = new Object[]
    {
        OK_OPTION_BUTTON
    };
    /**
     * The object which contains four buttons for presenting with a repeat event dialog.
     */
    public final Object[] REPEATING_CHOICE = new Object[]
    {
        ONLY_THIS_INSTANCE_BUTTON, ALL_IN_SERIES_BUTTON, ALL_FOLLOWING_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The object which contains three buttons for removing repeating events.
     */
    public final Object[] REMOVE_REPEATING_CHOICES = new Object[]
    {
        ONLY_THIS_INSTANCE_BUTTON, ALL_IN_SERIES_BUTTON, ALL_FOLLOWING_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The object which contains two buttons for updating repeating events.
     */
    public final Object[] UPDATE_REPEATING_CHOICES = new Object[]
    {
        ALL_IN_SERIES_BUTTON, ALL_FOLLOWING_BUTTON, CANCEL_OPTION_BUTTON
    };
    /**
     * The grey color.
     */
    public final Color GREY = Color.LIGHT_GRAY;
    /**
     * The black color.
     */
    public final Color BLACK = Color.BLACK;
    /**
     * The unselected border for a day of a month.
     */
    public final Border UNSELECTED_DAY_BORDER = BorderFactory.createEtchedBorder ();
    /**
     * The selected border for a day of the month.
     */
    public final Border SELECTED_DAY_BORDER = BorderFactory.createLoweredBevelBorder ();
    /**
     * The unit to increment scroll bars.
     */
    public final int SCROLL_INCREMENT = 16;
    /**
     * The source object for draggable assignments and events.
     */
    public final DragSource DRAG_SOURCE = new DragSource ();
    /**
     * The drag-and-drop listener for assignments and events.
     */
    public final DragDrop DND_LISTENER = new DragDrop (this);
    /**
     * The main frame of a stand-alone application.
     */
    public MainFrame mainFrame;
    /**
     * The root node which terms (and ultimately courses) are added to.
     */
    public DefaultMutableTreeNode root = new DefaultMutableTreeNode ("root");
    /**
     * The model for the left-hand term tree.
     */
    public DefaultTreeModel termsModel = new DefaultTreeModel (root);
    /**
     * The model for the middle assignments and events table.
     */
    public ExtendedTableModel assignmentsTableModel = new ExtendedTableModel ();
    /**
     * The model for the courses displayed in the combo box in the Assignment
     * Details panel.
     */
    public DefaultComboBoxModel courseComboModel = new DefaultComboBoxModel ();
    /**
     * The model for the textbooks displayed in the combo box in the Assignment
     * Details panel.
     */
    public DefaultComboBoxModel textbookComboModel = new DefaultComboBoxModel ();
    /**
     * The model for the types displayed in the combo box in the Assignment
     * Details panel.
     */
    public DefaultComboBoxModel typeComboModel = new DefaultComboBoxModel ();
    /**
     * The model for the categories displayed in the combo box in the Event
     * Details panel.
     */
    public DefaultComboBoxModel categoryComboModel = new DefaultComboBoxModel ();
    /**
     * The add button in the toolbar.
     */
    public ToolbarButton addButton;
    /**
     * The clone button in the toolbar.
     */
    public ToolbarButton cloneButton;
    /**
     * The remove button in the toolbar.
     */
    public ToolbarButton removeButton;
    /**
     * The ask button in the toolbar.
     */
    public ToolbarButton askInstructorButton;
    /**
     * The grades button in the toolbar.
     */
    public ToolbarButton viewGradesButton;
    /**
     * The preferences button in the toolbar.
     */
    public ToolbarButton settingsButton;
    /**
     * The terms and courses button in the toolbar.
     */
    public ToolbarButton termsAndCoursesButton;
    /**
     * The array that holds references to the panels holding the assignments and
     * events for each day of the month shown in Calendar View.
     */
    public JPanel[] daysAssignmentsAndEvents;
    /**
     * The array that holds references to panels shown in month view but outside
     * the currently shown month
     */
    public JPanel[] daysOutsideMonth;
    /**
     * The domain object reference performs generic actions specific to the GUI.
     */
    public Domain domain = new Domain (this);
    /**
     * The list of currently shown assignmentsAndEvents in Calendar View.
     */
    protected ArrayList<Assignment> shownAssignments = new ArrayList<Assignment> ();
    /**
     * The list of currently shown events in Calendar View.
     */
    protected ArrayList<Event> shownEvents = new ArrayList<Event> ();
    /**
     * The JPanel of the currently selected day in Calendar View.
     */
    protected JPanel selectedDayPanel = null;
    /**
     * The array that holds references to the panels that hold day elements in
     * the Calendar View.
     */
    protected ColoredJPanel[] days;
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel1 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events for this day.
     */
    private JPanel extraDay1 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll1 = new JScrollPane (extraDay1);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel2 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay2 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll2 = new JScrollPane (extraDay2);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel3 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay3 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll3 = new JScrollPane (extraDay3);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel4 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay4 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll4 = new JScrollPane (extraDay4);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel5 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay5 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll5 = new JScrollPane (extraDay5);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel6 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay6 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll6 = new JScrollPane (extraDay6);
    /**
     * An extra panel containing assignments and events in Calendar View.
     */
    protected ColoredJPanel extraDayPanel7 = new adl.go.gui.ColoredJPanel (GradientStyle.VERTICAL_GRADIENT_DOWN, new Color (255, 255, 255));
    /**
     * An extra panel containing the label for the day number and the panel that
     * will contain assignments and events.
     */
    private JPanel extraDay7 = new JPanel ();
    /**
     * The extra scroll panel around the panel containing the assignments and
     * events for this day.
     */
    private JScrollPane extraDayScroll7 = new JScrollPane (extraDay7);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel1 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel2 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel3 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel4 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel5 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel6 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * An extra day label
     */
    private JLabel extraDayLabel7 = new JLabel ("#", SwingConstants.RIGHT);
    /**
     * True if the mouse is dragging in the assignments table, false otherwise.
     */
    private boolean mouseDraggingInTable = false;
    /**
     * True if the table selection should be ignored, false otherwise.
     */
    public boolean ignoreTableSelection = false;
    /**
     * True if the application is running initialization, false otherwise.
     */
    public boolean initLoading = true;
    /**
     * True if the grades dialog is launching, false otherwise.
     */
    protected boolean launchingGrades = false;
    /**
     * True while the application goes through its quitting process, false otherwise.
     */
    protected boolean quitting = false;
    /**
     * If contains no elements, name text fields should be reselected.
     */
    protected Stack<Boolean> dontReselectName = new Stack<Boolean> ();
    /**
     * Forces the state change view to load.
     */
    private boolean forceViewLoad = false;
    /**
     * True if a tree selection should be ignored, false otherwise.
     */
    private boolean ignoreTreeSelection = false;
    /**
     * True when event changes have been made (but selection has not been
     * terminated), false otherwise.
     */
    protected Stack<Boolean> eventChanges = new Stack<Boolean> ();
    /**
     * True when repeating event changes have been made (but selection has not
     * been terminated), false otherwise.
     */
    protected boolean repeatEventChanges = false;
    /**
     * True when Grades dialog is launching, false otherwise.
     */
    protected boolean gradesLoading = false;
    /**
     * The index of the first day in the next month.
     */
    public int lastMonthFirst = -1;
    /**
     * The number of days in the previous month.
     */
    public int lastMonthLast = -1;
    /**
     * The last good directory the user selected from.
     */
    protected String lastGoodFile;
    /**
     * The end date for the currently selected event.
     */
    private Date repeatingEndDate;
    /**
     * The currently selected event (before any changes are made).
     */
    private Event tempEvent = null;
    /**
     * A reference to the about dialog.
     */
    protected AboutDialog aboutDialog = new AboutDialog (this);
    /**
     * A reference to the getting started dialog.
     */
    protected GettingStartedDialog gettingStartedDialog = new GettingStartedDialog (this);
    /**
     * A reference to the Helium dialog.
     */
    protected HeliumDialog heliumDialog = new HeliumDialog (this);
    /**
     * A reference to the import from backup dialog.
     */
    protected ImportFromBackupDialog importFromBackupDialog = new ImportFromBackupDialog (this);
    /**
     * A reference to the print dialog.
     */
    protected PrintDialog printDialog = new PrintDialog (this);
    /**
     * A reference to the settings dialog.
     */
    public SettingsDialog settingsDialog = new SettingsDialog (this);
    /**
     * A reference to the terms and courses dialog.
     */
    public TermsAndCoursesDialog termsAndCoursesDialog = new TermsAndCoursesDialog (this);
    /**
     * A reference to the grades dialog.
     */
    public GradesDialog gradesDialog = new GradesDialog (this);

    ////////////////////////////////////////////////////////////////////////////
    // Object Construction
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Creates new form MainFrame for a stand-alone application.
     *
     * @param mainFrame A reference to the main frame.
     * @param utility A reference to the utility.
     */
    public ViewPanel(MainFrame mainFrame, LocalUtility utility)
    {
        this.mainFrame = mainFrame;
        domain.setUtility (utility);
        init ();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Calls initialization functions for both the applet and frame-based
     * applications.
     */
    private void init()
    {
        setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));

        domain.utility.constructLocalUtility (this);
        domain.utility.setDomain (domain);
        domain.utility.loadPreferences ();
        domain.utility.loadUserDetails ();

        loadLanguageResource (domain.utility.preferences.language);
        YES_OPTION_BUTTON.setText (domain.language.getString ("yes"));
        NO_OPTION_BUTTON.setText (domain.language.getString ("no"));
        OK_OPTION_BUTTON.setText (domain.language.getString ("ok"));
        I_UNDERSTAND_OPTION_BUTTON.setText (domain.language.getString ("iUnderstand"));
        CANCEL_OPTION_BUTTON.setText (domain.language.getString ("cancel"));
        ONLY_THIS_INSTANCE_BUTTON.setText (domain.language.getString ("onlyThisInstance"));
        ALL_IN_SERIES_BUTTON.setText (domain.language.getString ("allInSeries"));
        ALL_FOLLOWING_BUTTON.setText (domain.language.getString ("allFollowing"));
        REPLACE_OPTION_BUTTON.setText (domain.language.getString ("replace"));
        initComponents ();

        middleTabbedPane.remove (weekViewPanel);

        miniCalendar.getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        miniCalendar.getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        dueDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        dueDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        dueDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        eventDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        eventDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        eventDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        repeatEventEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        repeatEventEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        repeatEventEndDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.termStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.termStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.termEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.termEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.labStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.labStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setWeekOfYearVisible (false);
        termsAndCoursesDialog.labEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        termsAndCoursesDialog.labEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setWeekOfYearVisible (false);

        settingsDialog.languageComboBox.setSelectedItem (domain.utility.preferences.language);

        // initialize the assignments and events details area
        blankContentPanel.setVisible (true);
        userDetailsContentPanel.setVisible (false);
        noUserDetailsPanel.setVisible (false);
        termContentPanel.setVisible (false);
        courseContentPanel.setVisible (false);
        assignmentContentPanel.setVisible (false);
        eventContentPanel.setVisible (false);
        ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("details"));

        addButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "add_button.png")));
        cloneButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "clone_button.png")));
        removeButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "remove_button.png")));
        askInstructorButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "ask_button.png")));
        termsAndCoursesButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "terms_courses_button.png")));
        viewGradesButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "grades_button.png")));
        settingsButton = new ToolbarButton (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "settings_button.png")));

        aboutDialog.init ();
        gettingStartedDialog.init ();
        heliumDialog.init ();
        gradesDialog.init ();
        printDialog.init ();
        importFromBackupDialog.init ();
        settingsDialog.init ();
        termsAndCoursesDialog.init ();

        addButton.setEnabled (false);
        cloneButton.setEnabled (false);
        removeButton.setEnabled (false);
        askInstructorButton.setEnabled (false);
        viewGradesButton.setEnabled (false);
        settingsButton.setEnabled (false);
        termsAndCoursesButton.setEnabled (false);

        toolBar.add (addButton);
        toolBar.addSeparator ();
        toolBar.add (cloneButton);
        toolBar.addSeparator ();
        toolBar.add (removeButton);
        toolBar.addSeparator (new Dimension (50, 10));
        toolBar.add (askInstructorButton);
        toolBar.addSeparator (new Dimension (50, 10));
        toolBar.add (viewGradesButton);
        toolBar.addSeparator (new Dimension (50, 10));
        toolBar.add (termsAndCoursesButton);
        toolBar.addSeparator ();
        toolBar.add (settingsButton);

        UIManager.put ("OptionPane.font", domain.utility.currentTheme.fontPlain12);
        UIManager.put ("OptionPane.messageFont", domain.utility.currentTheme.fontPlain12);
        UIManager.put ("OptionPane.buttonFont", domain.utility.currentTheme.fontPlain12);
        setFontForComponents (fileChooser.getComponents ());

        for (int i = 0; i < domain.utility.themes.size (); ++i)
        {
            domain.utility.themes.get (i).setViewPanel (this);
        }
        domain.utility.currentTheme.apply ();
        domain.logFile = new File (domain.utility.getDataFolder (), "log.dat");

        dayScroll1.getViewport ().setOpaque (false);
        dayScroll2.getViewport ().setOpaque (false);
        dayScroll3.getViewport ().setOpaque (false);
        dayScroll4.getViewport ().setOpaque (false);
        dayScroll5.getViewport ().setOpaque (false);
        dayScroll6.getViewport ().setOpaque (false);
        dayScroll7.getViewport ().setOpaque (false);
        dayScroll8.getViewport ().setOpaque (false);
        dayScroll9.getViewport ().setOpaque (false);
        dayScroll10.getViewport ().setOpaque (false);
        dayScroll11.getViewport ().setOpaque (false);
        dayScroll12.getViewport ().setOpaque (false);
        dayScroll13.getViewport ().setOpaque (false);
        dayScroll14.getViewport ().setOpaque (false);
        dayScroll15.getViewport ().setOpaque (false);
        dayScroll16.getViewport ().setOpaque (false);
        dayScroll17.getViewport ().setOpaque (false);
        dayScroll18.getViewport ().setOpaque (false);
        dayScroll19.getViewport ().setOpaque (false);
        dayScroll20.getViewport ().setOpaque (false);
        dayScroll21.getViewport ().setOpaque (false);
        dayScroll22.getViewport ().setOpaque (false);
        dayScroll23.getViewport ().setOpaque (false);
        dayScroll24.getViewport ().setOpaque (false);
        dayScroll25.getViewport ().setOpaque (false);
        dayScroll26.getViewport ().setOpaque (false);
        dayScroll27.getViewport ().setOpaque (false);
        dayScroll28.getViewport ().setOpaque (false);
        dayScroll29.getViewport ().setOpaque (false);
        dayScroll30.getViewport ().setOpaque (false);
        dayScroll31.getViewport ().setOpaque (false);
        dayScroll32.getViewport ().setOpaque (false);
        dayScroll33.getViewport ().setOpaque (false);
        dayScroll34.getViewport ().setOpaque (false);
        dayScroll35.getViewport ().setOpaque (false);
        termsAndCoursesDialog.courseScrollPane.getViewport ().setOpaque (false);
        termsAndCoursesDialog.textbooksScrollPane.getViewport ().setOpaque (false);
        settingsDialog.themeScrollPane.getViewport ().setOpaque (false);
        gradesDialog.courseSummaryScrollPane.getViewport ().setOpaque (false);
        gradesDialog.courseGradesScrollPane.getViewport ().setOpaque (false);

        extraDay1.setOpaque (false);
        extraDayScroll1.setOpaque (false);
        extraDayScroll1.getViewport ().setOpaque (false);
        extraDayLabel1.setOpaque (false);
        extraDay2.setOpaque (false);
        extraDayScroll2.setOpaque (false);
        extraDayScroll2.getViewport ().setOpaque (false);
        extraDayLabel2.setOpaque (false);
        extraDay3.setOpaque (false);
        extraDayScroll3.setOpaque (false);
        extraDayScroll3.getViewport ().setOpaque (false);
        extraDayLabel3.setOpaque (false);
        extraDay4.setOpaque (false);
        extraDayScroll4.setOpaque (false);
        extraDayScroll4.getViewport ().setOpaque (false);
        extraDayLabel4.setOpaque (false);
        extraDay5.setOpaque (false);
        extraDayScroll5.setOpaque (false);
        extraDayScroll5.getViewport ().setOpaque (false);
        extraDayLabel5.setOpaque (false);
        extraDay6.setOpaque (false);
        extraDayScroll6.setOpaque (false);
        extraDayScroll6.getViewport ().setOpaque (false);
        extraDayLabel6.setOpaque (false);
        extraDay7.setOpaque (false);
        extraDayScroll7.setOpaque (false);
        extraDayScroll7.getViewport ().setOpaque (false);
        extraDayLabel7.setOpaque (false);

        gradesDialog.courseGradesScrollPane.getViewport ().setOpaque (false);
        gradesDialog.courseSummaryScrollPane.getViewport ().setOpaque (false);
        settingsDialog.preferencesScrollPane.getViewport ().setOpaque (false);
        termScrollPane.getViewport ().setOpaque (false);
        termsAndCoursesDialog.courseScrollPane.getViewport ().setOpaque (false);
        termsAndCoursesDialog.textbooksScrollPane.getViewport ().setOpaque (false);

        addButton.setToolTipText (domain.language.getString ("addButtonToolTip"));
        addButton.setFont (domain.utility.currentTheme.fontPlain11);
        addButton.setText (domain.language.getString ("add"));
        addButton.setIconTextGap (-3);
        addButton.setHorizontalTextPosition (JLabel.CENTER);
        addButton.setVerticalTextPosition (JLabel.BOTTOM);
        addButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (addButton.isEnabled () && !initLoading)
                {
                    addButton.setSelected (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                if (addButton.isEnabled () && !initLoading)
                {
                    addButton.setSelected (true);
                    addPopupMenu.show (addButton, 0, addButton.getHeight ());
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (addButton.isEnabled () && !addButton.isSelected () && !initLoading)
                {
                    addButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                if (!addPopupMenu.isShowing ())
                {
                    addButton.setHover (false);
                }
            }
        });

        cloneButton.setToolTipText (domain.language.getString ("cloneButtonToolTip"));
        cloneButton.setFont (domain.utility.currentTheme.fontPlain11);
        cloneButton.setText (domain.language.getString ("clone"));
        cloneButton.setIconTextGap (-3);
        cloneButton.setHorizontalTextPosition (JLabel.CENTER);
        cloneButton.setVerticalTextPosition (JLabel.BOTTOM);
        cloneButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (cloneButton.isEnabled () && !initLoading)
                {
                    cloneButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                cloneButton.setDepressed (false);

                if (cloneButton.isEnabled () && !initLoading)
                {
                    if (domain.utility.assignmentsAndEvents.get (assignmentsTable.getVectorIndexFromSelectedRow ()).isAssignment ())
                    {
                        goCloneAssignment ();
                    }
                    else
                    {
                        goCloneEvent ();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (cloneButton.isEnabled ())
                {
                    cloneButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                cloneButton.setHover (false);
            }
        });

        removeButton.setToolTipText (domain.language.getString ("removeButtonToolTip"));
        removeButton.setFont (domain.utility.currentTheme.fontPlain11);
        removeButton.setText (domain.language.getString ("remove"));
        removeButton.setIconTextGap (-3);
        removeButton.setHorizontalTextPosition (JLabel.CENTER);
        removeButton.setVerticalTextPosition (JLabel.BOTTOM);
        removeButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (removeButton.isEnabled () && !initLoading)
                {
                    removeButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                removeButton.setDepressed (false);

                if (removeButton.isEnabled () && !initLoading)
                {
                    ListItem item = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                    if (item.isAssignment ())
                    {
                        goRemoveAssignment ();
                    }
                    else
                    {
                        goRemoveEvent (null);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (removeButton.isEnabled () && !initLoading)
                {
                    removeButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                removeButton.setHover (false);
            }
        });

        askInstructorButton.setToolTipText (domain.language.getString ("askInstructorButtonToolTip"));
        askInstructorButton.setFont (domain.utility.currentTheme.fontPlain11);
        askInstructorButton.setText (domain.language.getString ("ask"));
        askInstructorButton.setIconTextGap (-3);
        askInstructorButton.setHorizontalTextPosition (JLabel.CENTER);
        askInstructorButton.setVerticalTextPosition (JLabel.BOTTOM);
        askInstructorButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (askInstructorButton.isEnabled () && !initLoading)
                {
                    askInstructorButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                askInstructorButton.setDepressed (false);

                if (askInstructorButton.isEnabled () && !initLoading)
                {
                    goAskInstructor ();
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (askInstructorButton.isEnabled () && !initLoading)
                {
                    askInstructorButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                askInstructorButton.setHover (false);
            }
        });

        viewGradesButton.setToolTipText (domain.language.getString ("viewGradesButtonToolTip"));
        viewGradesButton.setFont (domain.utility.currentTheme.fontPlain11);
        viewGradesButton.setText (domain.language.getString ("grades"));
        viewGradesButton.setIconTextGap (-3);
        viewGradesButton.setHorizontalTextPosition (JLabel.CENTER);
        viewGradesButton.setVerticalTextPosition (JLabel.BOTTOM);
        viewGradesButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (viewGradesButton.isEnabled () && !initLoading)
                {
                    viewGradesButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                viewGradesButton.setDepressed (false);

                if (viewGradesButton.isEnabled () && !initLoading)
                {
                    launchingGrades = true;
                    gradesDialog.goViewGrades ();
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (viewGradesButton.isEnabled () && !initLoading)
                {
                    viewGradesButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                viewGradesButton.setHover (false);
            }
        });

        settingsButton.setToolTipText (domain.language.getString ("settingsButtonToolTip"));
        settingsButton.setFont (domain.utility.currentTheme.fontPlain11);
        settingsButton.setText (domain.language.getString ("settings"));
        settingsButton.setIconTextGap (-3);
        settingsButton.setHorizontalTextPosition (JLabel.CENTER);
        settingsButton.setVerticalTextPosition (JLabel.BOTTOM);
        settingsButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (settingsButton.isEnabled () && !initLoading)
                {
                    settingsButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                settingsButton.setDepressed (false);

                if (settingsButton.isEnabled () && !initLoading)
                {
                    settingsDialog.goViewSettings ();
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (settingsButton.isEnabled () && !initLoading)
                {
                    settingsButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                settingsButton.setHover (false);
            }
        });

        termsAndCoursesButton.setToolTipText (domain.language.getString ("termsAndCoursesButtonToolTip"));
        termsAndCoursesButton.setFont (domain.utility.currentTheme.fontPlain11);
        termsAndCoursesButton.setText (domain.language.getString ("courses"));
        termsAndCoursesButton.setIconTextGap (-3);
        termsAndCoursesButton.setHorizontalTextPosition (JLabel.CENTER);
        termsAndCoursesButton.setVerticalTextPosition (JLabel.BOTTOM);
        termsAndCoursesButton.addMouseListener (new MouseAdapter ()
        {
            @Override
            public void mousePressed(MouseEvent evt)
            {
                if (termsAndCoursesButton.isEnabled () && !initLoading)
                {
                    termsAndCoursesButton.setDepressed (true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt)
            {
                termsAndCoursesButton.setDepressed (false);

                if (termsAndCoursesButton.isEnabled () && !initLoading)
                {
                    termsAndCoursesDialog.goViewTermsAndCourses ();
                }
            }

            @Override
            public void mouseEntered(MouseEvent evt)
            {
                if (termsAndCoursesButton.isEnabled () && !initLoading)
                {
                    termsAndCoursesButton.setHover (true);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt)
            {
                termsAndCoursesButton.setHover (false);
            }
        });

        miniCalendar.setDate (domain.today);
    }

    /**
     * Custom initialization of specific components is done here.
     */
    private void initMyComponents()
    {
        try
        {
            if (Domain.OS_NAME.toLowerCase ().contains ("mac"))
            {
                Application macApp = Application.getApplication ();
                macApp.setPreferencesHandler (new PreferencesHandler ()
                {
                    @Override
                    public void handlePreferences(PreferencesEvent evt)
                    {
                        settingsDialog.goViewSettings ();
                    }
                });
                macApp.setQuitHandler (new QuitHandler ()
                {
                    @Override
                    public void handleQuitRequestWith(QuitEvent evt, QuitResponse rsp)
                    {
                        quit (true);
                    }
                });
                macApp.setAboutHandler (new AboutHandler ()
                {
                    @Override
                    public void handleAbout(AboutEvent evt)
                    {
                        aboutDialog.goViewAbout ();
                    }
                });
            }

            try
            {
                lastGoodFile = new File (Domain.HOME, "Get Organized " + domain.language.getString ("backup") + ".gbak").getCanonicalPath ();
            }
            catch (IOException ex)
            {
            }

            try
            {
                domain.today = Domain.DATE_FORMAT.parse (Domain.DATE_FORMAT.format (new Date ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            gradesDialog.graphPanel.setUtility (domain.utility);
            printDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "printer.png")).getImage ());
            gradesDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "grades_button.png")).getImage ());
            settingsDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "settings_button.png")).getImage ());
            termsAndCoursesDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "terms_courses_button.png")).getImage ());
            gettingStartedDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "go.png")).getImage ());
            heliumDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "helium.png")).getImage ());
            aboutDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "go.png")).getImage ());
            updatesDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "go.png")).getImage ());
            importFromBackupDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "go.png")).getImage ());
            printDialog.setIconImage (new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "printer.png")).getImage ());

            // remap shortcut keys to system defaults
            Toolkit.getDefaultToolkit ().addAWTEventListener (new AWTEventListener ()
            {
                @Override
                public void eventDispatched(AWTEvent event)
                {
                    KeyEvent kev = (KeyEvent) event;
                    if (kev.getID () == KeyEvent.KEY_PRESSED || kev.getID () == KeyEvent.KEY_RELEASED || kev.getID () == KeyEvent.KEY_PRESSED)
                    {
                        if ((kev.getModifiersEx () & KeyEvent.META_DOWN_MASK) != 0 && !((kev.getModifiersEx () & KeyEvent.CTRL_DOWN_MASK) != 0))
                        {
                            kev.consume ();
                            KeyEvent fake = new KeyEvent (kev.getComponent (), kev.getID (), kev.getWhen (), (kev.getModifiersEx () & ~KeyEvent.META_DOWN_MASK) | KeyEvent.CTRL_DOWN_MASK, kev.getKeyCode (), kev.getKeyChar ());
                            Toolkit.getDefaultToolkit ().getSystemEventQueue ().postEvent (fake);
                        }
                    }
                }
            }, KeyEvent.KEY_EVENT_MASK);

            buildDropTargets ();

            extraDayPanel1.setLayout (new BorderLayout ());
            extraDayPanel2.setLayout (new BorderLayout ());
            extraDayPanel3.setLayout (new BorderLayout ());
            extraDayPanel4.setLayout (new BorderLayout ());
            extraDayPanel5.setLayout (new BorderLayout ());
            extraDayPanel6.setLayout (new BorderLayout ());
            extraDayPanel7.setLayout (new BorderLayout ());

            // set the main panel of all dialogs to allow escaping
            ((EscapeDialog) updatesDialog).setMainPanel (this);
            ((EscapeDialog) repeatEventDialog).setMainPanel (this);

            rightPanel.invalidate ();
            gradeLabel.setVisible (false);
            gradeTextField.setVisible (false);

            // initialize the assignemnts table
            adjustAssignmentTableColumnWidths ();
            assignmentsTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
            {
                @Override
                public void valueChanged(ListSelectionEvent evt)
                {
                    assignmentsTableRowSelected (evt);
                }
            });
            assignmentsTable.getTableHeader ().setFont (domain.utility.currentTheme.fontPlain12);
            assignmentsTable.getTableHeader ().addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    assignmentsTableHeaderSelected (evt, -1);
                }
            });

            termsAndCoursesDialog.settingsTypesTable.getColumnModel ().getColumn (1).setMinWidth (70);
            termsAndCoursesDialog.settingsTypesTable.getColumnModel ().getColumn (1).setMaxWidth (70);
            termsAndCoursesDialog.settingsTypesTable.getTableHeader ().setFont (domain.utility.currentTheme.fontPlain12);
            termsAndCoursesDialog.settingsInstructorsTable.getColumnModel ().getColumn (1).setMinWidth (120);
            termsAndCoursesDialog.settingsInstructorsTable.getColumnModel ().getColumn (1).setMaxWidth (120);
            termsAndCoursesDialog.settingsInstructorsTable.getTableHeader ().setFont (domain.utility.currentTheme.fontPlain12);

            // initialize the term tree
            termTree.getSelectionModel ().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
            termTree.getSelectionModel ().addTreeSelectionListener (new TreeSelectionListener ()
            {
                @Override
                public void valueChanged(TreeSelectionEvent evt)
                {
                    if (!ignoreTreeSelection)
                    {
                        termTreeNodeSelected (evt);
                    }
                }
            });
            termTree.addTreeExpansionListener (new TreeExpansionListener ()
            {
                @Override
                public void treeExpanded(TreeExpansionEvent evt)
                {
                }

                @Override
                public void treeCollapsed(TreeExpansionEvent evt)
                {
                    expandTermTree (evt);
                }
            });

            extraDayScroll1.setBorder (null);
            extraDayScroll1.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll1.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll1.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll2.setBorder (null);
            extraDayScroll2.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll2.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll2.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll3.setBorder (null);
            extraDayScroll3.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll3.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll3.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll4.setBorder (null);
            extraDayScroll4.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll4.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll4.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll5.setBorder (null);
            extraDayScroll5.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll5.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll5.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll6.setBorder (null);
            extraDayScroll6.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll6.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll6.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });
            extraDayScroll7.setBorder (null);
            extraDayScroll7.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            extraDayScroll7.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            extraDayScroll7.addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    scrollPaneMouseReleased (evt);
                }
            });

            extraDayPanel1.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay1.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel2.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay2.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel3.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay3.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel4.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay4.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel5.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay5.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel6.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay6.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDayPanel7.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);
            extraDay7.setBackground (domain.utility.currentTheme.colorDayInMonthBackground1);

            extraDayLabel1.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel1.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel1.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel2.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel2.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel2.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel3.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel3.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel3.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel4.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel4.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel4.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel5.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel5.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel5.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel6.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel6.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel6.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayLabel7.setVerticalAlignment (SwingConstants.TOP);
            extraDayLabel7.setFont (domain.utility.currentTheme.fontPlain12);
            extraDayLabel7.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            extraDayPanel1.add (extraDayLabel1, BorderLayout.NORTH);
            extraDay1.setLayout (new BoxLayout (extraDay1, BoxLayout.PAGE_AXIS));
            extraDayPanel1.add (extraDayScroll1, BorderLayout.CENTER);
            extraDayPanel2.add (extraDayLabel2, BorderLayout.NORTH);
            extraDay2.setLayout (new BoxLayout (extraDay2, BoxLayout.PAGE_AXIS));
            extraDayPanel2.add (extraDayScroll2, BorderLayout.CENTER);
            extraDayPanel3.add (extraDayLabel3, BorderLayout.NORTH);
            extraDay3.setLayout (new BoxLayout (extraDay3, BoxLayout.PAGE_AXIS));
            extraDayPanel3.add (extraDayScroll3, BorderLayout.CENTER);
            extraDayPanel4.add (extraDayLabel4, BorderLayout.NORTH);
            extraDay4.setLayout (new BoxLayout (extraDay4, BoxLayout.PAGE_AXIS));
            extraDayPanel4.add (extraDayScroll4, BorderLayout.CENTER);
            extraDayPanel5.add (extraDayLabel5, BorderLayout.NORTH);
            extraDay5.setLayout (new BoxLayout (extraDay5, BoxLayout.PAGE_AXIS));
            extraDayPanel5.add (extraDayScroll5, BorderLayout.CENTER);
            extraDayPanel6.add (extraDayLabel6, BorderLayout.NORTH);
            extraDay6.setLayout (new BoxLayout (extraDay6, BoxLayout.PAGE_AXIS));
            extraDayPanel6.add (extraDayScroll6, BorderLayout.CENTER);
            extraDayPanel7.add (extraDayLabel7, BorderLayout.NORTH);
            extraDay7.setLayout (new BoxLayout (extraDay7, BoxLayout.PAGE_AXIS));
            extraDayPanel7.add (extraDayScroll7, BorderLayout.CENTER);

            // give the extended table and the table's model references to the domain.utility
            assignmentsTable.setUtility (domain.utility);
            assignmentsTableModel.setUtility (domain.utility);
            settingsDialog.settingsCategoriesTable.setUtility (domain.utility);
            termsAndCoursesDialog.settingsCoursesTable.setUtility (domain.utility);
            termsAndCoursesDialog.settingsTermsTable.setUtility (domain.utility);
            termsAndCoursesDialog.settingsTypesTable.setUtility (domain.utility);
            termsAndCoursesDialog.settingsInstructorsTable.setUtility (domain.utility);
            termsAndCoursesDialog.settingsTextbooksTable.setUtility (domain.utility);

            YES_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            YES_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            YES_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.YES_OPTION));
                }
            });
            NO_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            NO_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            NO_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.NO_OPTION));
                }
            });
            REPLACE_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            REPLACE_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            REPLACE_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.YES_OPTION));
                }
            });
            I_UNDERSTAND_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            I_UNDERSTAND_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            I_UNDERSTAND_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.OK_OPTION));
                }
            });
            OK_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            OK_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            OK_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.OK_OPTION));
                }
            });
            CANCEL_OPTION_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            CANCEL_OPTION_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            CANCEL_OPTION_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (JOptionPane.CANCEL_OPTION));
                }
            });
            ALL_FOLLOWING_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            ALL_FOLLOWING_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            ALL_FOLLOWING_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (ALL_FOLLOWING_OPTION));
                }
            });
            ALL_IN_SERIES_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            ALL_IN_SERIES_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            ALL_IN_SERIES_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (ALL_IN_SERIES_OPTION));
                }
            });
            ONLY_THIS_INSTANCE_BUTTON.setBackground (domain.utility.currentTheme.colorButtonBackground);
            ONLY_THIS_INSTANCE_BUTTON.setFont (domain.utility.currentTheme.fontPlain12);
            ONLY_THIS_INSTANCE_BUTTON.addActionListener (new ActionListener ()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OPTION_PANE.setValue (new Integer (ONLY_THIS_INSTANCE_OPTION));
                }
            });

            // set the look and feel of the JFileChooser
            JPanel access = (JPanel) ((JPanel) fileChooser.getComponent (0)).getComponent (0);
            ((JButton) access.getComponent (0)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JButton) access.getComponent (2)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JButton) access.getComponent (4)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JToggleButton) access.getComponent (6)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JToggleButton) access.getComponent (7)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            access = (JPanel) ((JPanel) fileChooser.getComponent (3)).getComponent (3);
            ((JButton) access.getComponent (0)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JButton) access.getComponent (0)).setFont (domain.utility.currentTheme.fontPlain12);
            ((JButton) access.getComponent (1)).setBackground (domain.utility.currentTheme.colorButtonBackground);
            ((JButton) access.getComponent (1)).setFont (domain.utility.currentTheme.fontPlain12);
            ((JButton) access.getComponent (1)).setToolTipText (domain.language.getString ("cancelBackupToolTip"));

            ((JTextFieldDateEditor) dueDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("dueDateChooserToolTip"));
            ((JTextFieldDateEditor) eventDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("eventDateChooserToolTip"));
            ((JTextFieldDateEditor) repeatEventEndDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("repeatEventEndDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.termStartDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("termStartDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.termEndDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("termEndDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.courseStartDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("courseStartDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.courseEndDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("courseEndDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.labStartDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("labStartDateChooserToolTip"));
            ((JTextFieldDateEditor) termsAndCoursesDialog.labEndDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("labEndDateChooserToolTip"));

            if (!domain.utility.preferences.dontShowGettingStarted)
            {
                gettingStartedDialog.showGettingStartedDialog ();
            }
            if (!domain.utility.preferences.dontShowHelium)
            {
                heliumDialog.goViewHelium ();
            }
        }
        catch (Exception ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Build drop targets for all panels.
     */
    protected void buildDropTargets()
    {
        week1Day1.setDropTarget (new DropTarget (week1Day1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week1Day2.setDropTarget (new DropTarget (week1Day2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week1Day3.setDropTarget (new DropTarget (week1Day3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week1Day4.setDropTarget (new DropTarget (week1Day4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week1Day5.setDropTarget (new DropTarget (week1Day5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week1Day6.setDropTarget (new DropTarget (week1Day6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day7.setDropTarget (new DropTarget (week1Day7, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day1.setDropTarget (new DropTarget (week2Day1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day2.setDropTarget (new DropTarget (week2Day2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day3.setDropTarget (new DropTarget (week2Day3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day4.setDropTarget (new DropTarget (week2Day4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day5.setDropTarget (new DropTarget (week2Day5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day6.setDropTarget (new DropTarget (week2Day6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week2Day7.setDropTarget (new DropTarget (week2Day7, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day1.setDropTarget (new DropTarget (week3Day1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day2.setDropTarget (new DropTarget (week3Day2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day3.setDropTarget (new DropTarget (week3Day3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day4.setDropTarget (new DropTarget (week3Day4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day5.setDropTarget (new DropTarget (week3Day5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day6.setDropTarget (new DropTarget (week3Day6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week3Day7.setDropTarget (new DropTarget (week3Day7, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day1.setDropTarget (new DropTarget (week4Day1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day2.setDropTarget (new DropTarget (week4Day2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day3.setDropTarget (new DropTarget (week4Day3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day4.setDropTarget (new DropTarget (week4Day4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day5.setDropTarget (new DropTarget (week4Day5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day6.setDropTarget (new DropTarget (week4Day6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week4Day7.setDropTarget (new DropTarget (week4Day7, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day1.setDropTarget (new DropTarget (week5Day1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day2.setDropTarget (new DropTarget (week5Day2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day3.setDropTarget (new DropTarget (week5Day3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day4.setDropTarget (new DropTarget (week5Day4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day5.setDropTarget (new DropTarget (week5Day5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day6.setDropTarget (new DropTarget (week5Day6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        week5Day7.setDropTarget (new DropTarget (week5Day7, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel1.setDropTarget (new DropTarget (extraDayPanel1, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel2.setDropTarget (new DropTarget (extraDayPanel2, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel3.setDropTarget (new DropTarget (extraDayPanel3, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel4.setDropTarget (new DropTarget (extraDayPanel4, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel5.setDropTarget (new DropTarget (extraDayPanel5, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel6.setDropTarget (new DropTarget (extraDayPanel6, DnDConstants.ACTION_MOVE, DND_LISTENER));
        extraDayPanel7.setDropTarget (new DropTarget (extraDayPanel7, DnDConstants.ACTION_MOVE, DND_LISTENER));
    }

    /**
     * Triggered when a new table header is selected to sort the assignments and
     * events list by.
     *
     * @param evt The mouse click event, if it exists.
     */
    public void assignmentsTableHeaderSelected(MouseEvent evt, int colIndex)
    {
        // get the selected column, and filter (sort) by thatthat column
        assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).setHeaderValue (assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).getHeaderValue ().toString ().replaceAll ("\\<html\\>|\\<b\\>|\\</html\\>|\\</b\\>", ""));
        if (evt != null)
        {
            assignmentsTableModel.setColumnSorting (assignmentsTable.getTableHeader ().columnAtPoint (evt.getPoint ()));
        }
        else
        {
            assignmentsTableModel.setColumnSorting (colIndex);
        }
        assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).setHeaderValue ("<html><b>" + assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).getHeaderValue () + "</b></html>");
        assignmentsTable.getTableHeader ().resizeAndRepaint ();
        filter (true);

        // set the preferences sorting model to the selected column
        domain.utility.preferences.sortIndex = assignmentsTableModel.getColumnSorting ();
        domain.utility.preferences.sortAscending = assignmentsTableModel.isSortAscending ();
        domain.needsPreferencesSave = true;
    }

    /**
     * Initializes the states of the add buttons depending on whether terms
     * and/or courses exist yet.
     */
    protected void initButtons()
    {
        if (domain.utility.terms.size () > 0)
        {
            enableCourseButtons ();
        }
        if (domain.utility.courses.size () > 0)
        {
            enableAssignmentButtons ();
        }
    }

    /**
     * Iterates through any component in the given component and specifies the
     * font and size that it should be, maintaining the style of the font.
     *
     * @param comp The list of components to iterate through (recursively).
     */
    private void setFontForComponents(Component[] comp)
    {
        for (int i = 0; i < comp.length; ++i)
        {
            if (comp[i] instanceof Container)
            {
                setFontForComponents (((Container) comp[i]).getComponents ());
            }
            try
            {
                comp[i].setFont (new Font ("Verdana", comp[i].getFont ().getStyle (), 11));
            }
            catch (Exception ex)
            {
                // do nothing if a setFont function was not found
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Interface-Generated Implementation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addPopupMenu = new javax.swing.JPopupMenu();
        addTermMenuItem = new javax.swing.JMenuItem();
        addCourseMenuItem = new javax.swing.JMenuItem();
        addAssignmentMenuItem = new javax.swing.JMenuItem();
        addPopupSeparator = new javax.swing.JPopupMenu.Separator();
        addEventMenuItem = new javax.swing.JMenuItem();
        updatesDialog = new adl.go.gui.EscapeDialog();
        updatesJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorSingleWindowBackground1);
        updatesProgressBar = new javax.swing.JProgressBar();
        updatesCloseButton = new javax.swing.JButton();
        termEditMenu = new javax.swing.JPopupMenu();
        addTermEditMenuItem = new javax.swing.JMenuItem();
        editTermEditMenuItem = new javax.swing.JMenuItem();
        removeTermEditMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        addCourseEditMenuItem = new javax.swing.JMenuItem();
        editCourseEditMenuItem = new javax.swing.JMenuItem();
        removeCourseEditMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        courseWebsiteMenuItem = new javax.swing.JMenuItem();
        labWebsiteMenuItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        editInstructorsEditMenuItem = new javax.swing.JMenuItem();
        editTypesEditMenuItem = new javax.swing.JMenuItem();
        editTextbooksEditMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        addAssignmentTermEditMenuItem = new javax.swing.JMenuItem();
        assignmentsEditMenu = new javax.swing.JPopupMenu();
        addAssignmentEditMenuItem = new javax.swing.JMenuItem();
        addEventEditMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        cloneEditMenuItem = new javax.swing.JMenuItem();
        removeEditMenuItem = new javax.swing.JMenuItem();
        filter1ButtonGroup = new javax.swing.ButtonGroup();
        filter2ButtonGroup = new javax.swing.ButtonGroup();
        repeatEventDialog = new adl.go.gui.EscapeDialog();
        repeatDialogPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorSingleWindowBackground1);
        repeatEventDoneButton = new javax.swing.JButton();
        repeatsLabel = new javax.swing.JLabel();
        repeatsEveryLabel = new javax.swing.JLabel();
        repeatsOnLabel = new javax.swing.JLabel();
        repeatsEndingLabel = new javax.swing.JLabel();
        repeatEventRepeatsComboBox = new javax.swing.JComboBox();
        repeatEventRepeatsEveryComboBox = new javax.swing.JComboBox();
        repeatEventEndDateChooser = new com.toedter.calendar.JDateChooser();
        reSunCheckBox = new javax.swing.JCheckBox();
        reMonCheckBox = new javax.swing.JCheckBox();
        reTueCheckBox = new javax.swing.JCheckBox();
        reWedCheckBox = new javax.swing.JCheckBox();
        reThuCheckBox = new javax.swing.JCheckBox();
        reFriCheckBox = new javax.swing.JCheckBox();
        reSatCheckBox = new javax.swing.JCheckBox();
        everyDescriptionLabel = new javax.swing.JLabel();
        fileChooser = new javax.swing.JFileChooser();
        askPopupMenu = new javax.swing.JPopupMenu();
        printingDialog = new adl.go.gui.EscapeDialog();
        printingJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorSingleWindowBackground1);
        printingJLabel = new javax.swing.JLabel();
        toolBar = new adl.go.gui.ColoredJToolBar(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorTopBackground1Panel);
        leftPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorLeftBackground1Panel);
        termScrollPane = new javax.swing.JScrollPane();
        termTree = new javax.swing.JTree();
        miniCalendar = new com.toedter.calendar.JCalendar();
        middlePanel = new javax.swing.JPanel();
        loadingPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorLoadingMiddleBackground1Panel);
        loadingLabel = new javax.swing.JLabel();
        contentPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorMiddleBackground1Panel);
        upperPanelForFilters = new javax.swing.JPanel();
        bothFilterRadioButton = new javax.swing.JRadioButton();
        assignmentsFilterRadioButton = new javax.swing.JRadioButton();
        eventsFilterRadioButton = new javax.swing.JRadioButton();
        jSeparator6 = new javax.swing.JSeparator();
        allFilterRadioButton = new javax.swing.JRadioButton();
        overdueFilterRadioButton = new javax.swing.JRadioButton();
        doneFilterRadioButton = new javax.swing.JRadioButton();
        notDoneFilterRadioButton = new javax.swing.JRadioButton();
        middlePanelForTabs = new javax.swing.JPanel();
        middleTabbedPane = new javax.swing.JTabbedPane();
        listViewPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorMiddleBackground1Panel);
        assignmentsListScrollPane = new javax.swing.JScrollPane();
        assignmentsTable = new adl.go.gui.ExtendedJTable();
        weekViewPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorMiddleCalendarBackgroundPanel1);
        monthViewPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorMiddleCalendarBackgroundPanel1);
        monthAndYearPanel = new javax.swing.JPanel();
        currentMonthLabel = new javax.swing.JLabel();
        currentYearLabel = new javax.swing.JLabel();
        prevMonthButton = new javax.swing.JLabel();
        todayButton = new javax.swing.JLabel();
        nextMonthButton = new javax.swing.JLabel();
        daysOfWeekPanel = new javax.swing.JPanel();
        sundayLabel = new javax.swing.JLabel();
        mondayLabel = new javax.swing.JLabel();
        tuesdayLabel = new javax.swing.JLabel();
        wednesdayLabel = new javax.swing.JLabel();
        thursdayLabel = new javax.swing.JLabel();
        fridayLabel = new javax.swing.JLabel();
        saturdayLabel = new javax.swing.JLabel();
        monthDaysPanel = new javax.swing.JPanel();
        week1Day1 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel1 = new javax.swing.JLabel();
        dayScroll1 = new javax.swing.JScrollPane();
        day1 = new javax.swing.JPanel();
        week1Day2 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel2 = new javax.swing.JLabel();
        dayScroll2 = new javax.swing.JScrollPane();
        day2 = new javax.swing.JPanel();
        week1Day3 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel3 = new javax.swing.JLabel();
        dayScroll3 = new javax.swing.JScrollPane();
        day3 = new javax.swing.JPanel();
        week1Day4 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel4 = new javax.swing.JLabel();
        dayScroll4 = new javax.swing.JScrollPane();
        day4 = new javax.swing.JPanel();
        week1Day5 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel5 = new javax.swing.JLabel();
        dayScroll5 = new javax.swing.JScrollPane();
        day5 = new javax.swing.JPanel();
        week1Day6 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel6 = new javax.swing.JLabel();
        dayScroll6 = new javax.swing.JScrollPane();
        day6 = new javax.swing.JPanel();
        week1Day7 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel7 = new javax.swing.JLabel();
        dayScroll7 = new javax.swing.JScrollPane();
        day7 = new javax.swing.JPanel();
        week2Day1 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel8 = new javax.swing.JLabel();
        dayScroll8 = new javax.swing.JScrollPane();
        day8 = new javax.swing.JPanel();
        week2Day2 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel9 = new javax.swing.JLabel();
        dayScroll9 = new javax.swing.JScrollPane();
        day9 = new javax.swing.JPanel();
        week2Day3 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel10 = new javax.swing.JLabel();
        dayScroll10 = new javax.swing.JScrollPane();
        day10 = new javax.swing.JPanel();
        week2Day4 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel11 = new javax.swing.JLabel();
        dayScroll11 = new javax.swing.JScrollPane();
        day11 = new javax.swing.JPanel();
        week2Day5 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel12 = new javax.swing.JLabel();
        dayScroll12 = new javax.swing.JScrollPane();
        day12 = new javax.swing.JPanel();
        week2Day6 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel13 = new javax.swing.JLabel();
        dayScroll13 = new javax.swing.JScrollPane();
        day13 = new javax.swing.JPanel();
        week2Day7 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel14 = new javax.swing.JLabel();
        dayScroll14 = new javax.swing.JScrollPane();
        day14 = new javax.swing.JPanel();
        week3Day1 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel15 = new javax.swing.JLabel();
        dayScroll15 = new javax.swing.JScrollPane();
        day15 = new javax.swing.JPanel();
        week3Day2 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel16 = new javax.swing.JLabel();
        dayScroll16 = new javax.swing.JScrollPane();
        day16 = new javax.swing.JPanel();
        week3Day3 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel17 = new javax.swing.JLabel();
        dayScroll17 = new javax.swing.JScrollPane();
        day17 = new javax.swing.JPanel();
        week3Day4 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel18 = new javax.swing.JLabel();
        dayScroll18 = new javax.swing.JScrollPane();
        day18 = new javax.swing.JPanel();
        week3Day5 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel19 = new javax.swing.JLabel();
        dayScroll19 = new javax.swing.JScrollPane();
        day19 = new javax.swing.JPanel();
        week3Day6 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel20 = new javax.swing.JLabel();
        dayScroll20 = new javax.swing.JScrollPane();
        day20 = new javax.swing.JPanel();
        week3Day7 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel21 = new javax.swing.JLabel();
        dayScroll21 = new javax.swing.JScrollPane();
        day21 = new javax.swing.JPanel();
        week4Day1 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel22 = new javax.swing.JLabel();
        dayScroll22 = new javax.swing.JScrollPane();
        day22 = new javax.swing.JPanel();
        week4Day2 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel23 = new javax.swing.JLabel();
        dayScroll23 = new javax.swing.JScrollPane();
        day23 = new javax.swing.JPanel();
        week4Day3 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel24 = new javax.swing.JLabel();
        dayScroll24 = new javax.swing.JScrollPane();
        day24 = new javax.swing.JPanel();
        week4Day4 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel25 = new javax.swing.JLabel();
        dayScroll25 = new javax.swing.JScrollPane();
        day25 = new javax.swing.JPanel();
        week4Day5 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel26 = new javax.swing.JLabel();
        dayScroll26 = new javax.swing.JScrollPane();
        day26 = new javax.swing.JPanel();
        week4Day6 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel27 = new javax.swing.JLabel();
        dayScroll27 = new javax.swing.JScrollPane();
        day27 = new javax.swing.JPanel();
        week4Day7 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel28 = new javax.swing.JLabel();
        dayScroll28 = new javax.swing.JScrollPane();
        day28 = new javax.swing.JPanel();
        week5Day1 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel29 = new javax.swing.JLabel();
        dayScroll29 = new javax.swing.JScrollPane();
        day29 = new javax.swing.JPanel();
        week5Day2 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel30 = new javax.swing.JLabel();
        dayScroll30 = new javax.swing.JScrollPane();
        day30 = new javax.swing.JPanel();
        week5Day3 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel31 = new javax.swing.JLabel();
        dayScroll31 = new javax.swing.JScrollPane();
        day31 = new javax.swing.JPanel();
        week5Day4 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel32 = new javax.swing.JLabel();
        dayScroll32 = new javax.swing.JScrollPane();
        day32 = new javax.swing.JPanel();
        week5Day5 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel33 = new javax.swing.JLabel();
        dayScroll33 = new javax.swing.JScrollPane();
        day33 = new javax.swing.JPanel();
        week5Day6 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel34 = new javax.swing.JLabel();
        dayScroll34 = new javax.swing.JScrollPane();
        day34 = new javax.swing.JPanel();
        week5Day7 = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorDayInMonthBackground1);
        dayLabel35 = new javax.swing.JLabel();
        dayScroll35 = new javax.swing.JScrollPane();
        day35 = new javax.swing.JPanel();
        rightPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, domain.utility.currentTheme.colorRightBackground1Panel);
        blankContentPanel = new javax.swing.JPanel();
        noUserDetailsPanel = new javax.swing.JPanel();
        noStudentAdvisorDetails = new javax.swing.JLabel();
        editUserDetailsButton = new javax.swing.JButton();
        userDetailsContentPanel = new javax.swing.JPanel();
        userNameDetailsLabel = new javax.swing.JLabel();
        schoolDetailsLabel = new javax.swing.JLabel();
        idNumberDetailsLabel = new javax.swing.JLabel();
        boxNumberDetailsLabel = new javax.swing.JLabel();
        advisorDetailsLabel = new javax.swing.JLabel();
        officeHoursDetailsLabel = new javax.swing.JLabel();
        officeLocationDetailsLabel = new javax.swing.JLabel();
        contactAdvisorButton = new javax.swing.JButton();
        termContentPanel = new javax.swing.JPanel();
        termNameDetailsLabel = new javax.swing.JLabel();
        termCoursesDetailsLabel = new javax.swing.JLabel();
        termAvgGradeDetailsLabel = new javax.swing.JLabel();
        termTextbooksDetailsLabel = new javax.swing.JLabel();
        termStartDateDetailsLabel = new javax.swing.JLabel();
        termEndDateDetailsLabel = new javax.swing.JLabel();
        termTypesDetailsLabel = new javax.swing.JLabel();
        termTotalAssignmentsDetailsLabel = new javax.swing.JLabel();
        termUnfinishedDetailsLabel = new javax.swing.JLabel();
        termCreditsDetailsLabel = new javax.swing.JLabel();
        courseContentPanel = new javax.swing.JPanel();
        courseNameDetailsLabel = new javax.swing.JLabel();
        courseStartDateDetailsLabel = new javax.swing.JLabel();
        courseEndDateDetailsLabel = new javax.swing.JLabel();
        courseStartTimeDetailsLabel = new javax.swing.JLabel();
        courseEndTimeDetailsLabel = new javax.swing.JLabel();
        courseCreditsDetailsLabel = new javax.swing.JLabel();
        courseRoomDetailsLabel = new javax.swing.JLabel();
        courseTotalAssignmentsDetailsLabel = new javax.swing.JLabel();
        courseUnfinishedDetailsLabel = new javax.swing.JLabel();
        courseCurrentGradeDetailsLabel = new javax.swing.JLabel();
        courseTypesDetailsLabel = new javax.swing.JLabel();
        courseTextbooksDetailsLabel = new javax.swing.JLabel();
        courseDaysDetailsLabel = new javax.swing.JLabel();
        assignmentContentPanel = new javax.swing.JPanel();
        assignmentNameTextField = new javax.swing.JTextField();
        assignmentsSeparator1 = new javax.swing.JSeparator();
        courseLabel = new javax.swing.JLabel();
        textbookLabel = new javax.swing.JLabel();
        assignmentsSeparator2 = new javax.swing.JSeparator();
        typeLabel = new javax.swing.JLabel();
        priorityLabel = new javax.swing.JLabel();
        detailsTypeComboBox = new javax.swing.JComboBox();
        detailsCourseComboBox = new javax.swing.JComboBox();
        detailsTextbookComboBox = new javax.swing.JComboBox();
        gradeLabel = new javax.swing.JLabel();
        gradeTextField = new javax.swing.JTextField();
        assignmentsSeparator3 = new javax.swing.JSeparator();
        completedCheckBox = new javax.swing.JCheckBox();
        commentsLabel = new javax.swing.JLabel();
        commentsScrollPane = new javax.swing.JScrollPane();
        commentsTextArea = new javax.swing.JTextArea();
        prioritySlider = new javax.swing.JSlider();
        dueDateChooser = new com.toedter.calendar.JDateChooser();
        dueHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        dueMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        dueMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        assnColon = new javax.swing.JLabel();
        eventContentPanel = new javax.swing.JPanel();
        eventNameTextField = new javax.swing.JTextField();
        eventDateChooser = new com.toedter.calendar.JDateChooser();
        commentsScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        eventsSeparator2 = new javax.swing.JSeparator();
        allDayEventCheckBox = new javax.swing.JCheckBox();
        locationLabel = new javax.swing.JLabel();
        locationTextField = new javax.swing.JTextField();
        categoryComboBox = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        eventRepeatButton = new javax.swing.JButton();
        eventStartHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventEndHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventStartMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventStartMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventEndMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventEndMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        eventColon1 = new javax.swing.JLabel();
        eventColon2 = new javax.swing.JLabel();
        googleMapsButton = new javax.swing.JButton();
        eventsSeparator1 = new javax.swing.JSeparator();
        statusPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_UP, domain.utility.currentTheme.colorBottomBackground1Panel);
        progressBar = new javax.swing.JProgressBar();

        addPopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                addPopupMenuPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        addTermMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addTermMenuItem.setText(domain.language.getString ("addTerm") + "...");
        addTermMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTermMenuItemActionPerformed(evt);
            }
        });
        addPopupMenu.add(addTermMenuItem);

        addCourseMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addCourseMenuItem.setText(domain.language.getString ("addCourse") + "...");
        addCourseMenuItem.setEnabled(false);
        addCourseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseMenuItemActionPerformed(evt);
            }
        });
        addPopupMenu.add(addCourseMenuItem);

        addAssignmentMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addAssignmentMenuItem.setText(domain.language.getString ("addAssignment"));
        addAssignmentMenuItem.setEnabled(false);
        addAssignmentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentMenuItemActionPerformed(evt);
            }
        });
        addPopupMenu.add(addAssignmentMenuItem);
        addPopupMenu.add(addPopupSeparator);

        addEventMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addEventMenuItem.setText(domain.language.getString ("addEvent"));
        addEventMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEventMenuItemActionPerformed(evt);
            }
        });
        addPopupMenu.add(addEventMenuItem);

        updatesDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        updatesDialog.setTitle(domain.language.getString ("checkForUpdates"));
        updatesDialog.setAlwaysOnTop(true);
        updatesDialog.setName("updatesDialog"); // NOI18N
        updatesDialog.setResizable(false);

        updatesProgressBar.setFont(domain.utility.currentTheme.fontPlain12);
        updatesProgressBar.setOpaque(false);
        updatesProgressBar.setStringPainted(true);

        updatesCloseButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
        updatesCloseButton.setFont(domain.utility.currentTheme.fontPlain12);
        updatesCloseButton.setText(domain.language.getString ("close"));
        updatesCloseButton.setToolTipText(domain.language.getString ("closeToolTip"));
        updatesCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatesCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updatesJPanelLayout = new javax.swing.GroupLayout(updatesJPanel);
        updatesJPanel.setLayout(updatesJPanelLayout);
        updatesJPanelLayout.setHorizontalGroup(
            updatesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updatesProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(updatesCloseButton)
                .addContainerGap())
        );
        updatesJPanelLayout.setVerticalGroup(
            updatesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatesJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updatesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updatesProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updatesCloseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout updatesDialogLayout = new javax.swing.GroupLayout(updatesDialog.getContentPane());
        updatesDialog.getContentPane().setLayout(updatesDialogLayout);
        updatesDialogLayout.setHorizontalGroup(
            updatesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updatesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        updatesDialogLayout.setVerticalGroup(
            updatesDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(updatesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        addTermEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addTermEditMenuItem.setText(domain.language.getString ("addTerm") + "...");
        addTermEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTermMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(addTermEditMenuItem);

        editTermEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        editTermEditMenuItem.setText(domain.language.getString ("editTerm") + "...");
        editTermEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTermEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(editTermEditMenuItem);

        removeTermEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        removeTermEditMenuItem.setText(domain.language.getString ("removeTerm"));
        removeTermEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTermEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(removeTermEditMenuItem);
        termEditMenu.add(jSeparator1);

        addCourseEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addCourseEditMenuItem.setText(domain.language.getString ("addCourse") + "...");
        addCourseEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(addCourseEditMenuItem);

        editCourseEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        editCourseEditMenuItem.setText(domain.language.getString ("editCourse") + "...");
        editCourseEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCourseEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(editCourseEditMenuItem);

        removeCourseEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        removeCourseEditMenuItem.setText(domain.language.getString ("removeCourse"));
        removeCourseEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCourseEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(removeCourseEditMenuItem);
        termEditMenu.add(jSeparator2);

        courseWebsiteMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        courseWebsiteMenuItem.setText(domain.language.getString ("courseWebsite"));
        courseWebsiteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(courseWebsiteMenuItem);

        labWebsiteMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        labWebsiteMenuItem.setText(domain.language.getString ("labWebsite"));
        labWebsiteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(labWebsiteMenuItem);
        termEditMenu.add(jSeparator7);

        editInstructorsEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        editInstructorsEditMenuItem.setText(domain.language.getString ("editInstructors") + "...");
        editInstructorsEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editInstructorsEditMenuItemeditGradingScaleEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(editInstructorsEditMenuItem);

        editTypesEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        editTypesEditMenuItem.setText(domain.language.getString ("editGradingScale") + "...");
        editTypesEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editGradingScaleEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(editTypesEditMenuItem);

        editTextbooksEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        editTextbooksEditMenuItem.setText(domain.language.getString ("editTextbooks") + "...");
        editTextbooksEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editTextbooksEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(editTextbooksEditMenuItem);
        termEditMenu.add(jSeparator3);

        addAssignmentTermEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addAssignmentTermEditMenuItem.setText(domain.language.getString ("addAssignment"));
        addAssignmentTermEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentTermEditMenuItemActionPerformed(evt);
            }
        });
        termEditMenu.add(addAssignmentTermEditMenuItem);

        addAssignmentEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addAssignmentEditMenuItem.setText(domain.language.getString ("addAssignment"));
        addAssignmentEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentMenuItemActionPerformed(evt);
            }
        });
        assignmentsEditMenu.add(addAssignmentEditMenuItem);

        addEventEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        addEventEditMenuItem.setText(domain.language.getString ("addEvent"));
        addEventEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEventEditMenuItemActionPerformed(evt);
            }
        });
        assignmentsEditMenu.add(addEventEditMenuItem);
        assignmentsEditMenu.add(jSeparator5);

        cloneEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        cloneEditMenuItem.setText(domain.language.getString ("clone"));
        cloneEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cloneEditMenuItemActionPerformed(evt);
            }
        });
        assignmentsEditMenu.add(cloneEditMenuItem);

        removeEditMenuItem.setFont(domain.utility.currentTheme.fontPlain11);
        removeEditMenuItem.setText(domain.language.getString ("remove"));
        removeEditMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEditMenuItemActionPerformed(evt);
            }
        });
        assignmentsEditMenu.add(removeEditMenuItem);

        repeatEventDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        repeatEventDialog.setName("repeatEventDialog"); // NOI18N
        repeatEventDialog.setResizable(false);
        repeatEventDialog.setUndecorated(true);

        repeatDialogPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        repeatEventDoneButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
        repeatEventDoneButton.setFont(domain.utility.currentTheme.fontPlain12);
        repeatEventDoneButton.setText(domain.language.getString ("done"));
        repeatEventDoneButton.setToolTipText(domain.language.getString ("doneEditingRepetitionToolTip"));
        repeatEventDoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatEventDoneButtonActionPerformed(evt);
            }
        });

        repeatsLabel.setFont(domain.utility.currentTheme.fontBold11);
        repeatsLabel.setText(domain.language.getString ("repeats") + ":");

        repeatsEveryLabel.setFont(domain.utility.currentTheme.fontBold11);
        repeatsEveryLabel.setText(domain.language.getString ("every") + ":");

        repeatsOnLabel.setFont(domain.utility.currentTheme.fontBold11);
        repeatsOnLabel.setText(domain.language.getString ("on") + ":");

        repeatsEndingLabel.setFont(domain.utility.currentTheme.fontBold11);
        repeatsEndingLabel.setText(domain.language.getString ("ending") + ":");

        repeatEventRepeatsComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
        repeatEventRepeatsComboBox.setFont(domain.utility.currentTheme.fontPlain12);
        repeatEventRepeatsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {domain.language.getString ("never"), domain.language.getString ("daily"), domain.language.getString ("weekly"), domain.language.getString ("monthly"), domain.language.getString ("yearly")}));
        repeatEventRepeatsComboBox.setToolTipText(domain.language.getString ("howTheEventRepeatsToolTip"));
        repeatEventRepeatsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatEventRepeatsComboBoxActionPerformed(evt);
            }
        });

        repeatEventRepeatsEveryComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
        repeatEventRepeatsEveryComboBox.setFont(domain.utility.currentTheme.fontPlain12);
        repeatEventRepeatsEveryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        repeatEventRepeatsEveryComboBox.setToolTipText(domain.language.getString ("theIntervalForRepeatingToolTip"));
        repeatEventRepeatsEveryComboBox.setEnabled(false);
        repeatEventRepeatsEveryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatEventRepeatsEveryComboBoxActionPerformed(evt);
            }
        });

        repeatEventEndDateChooser.setToolTipText(domain.language.getString ("endDateForTheRepetitionToolTip"));
        repeatEventEndDateChooser.setEnabled(false);
        repeatEventEndDateChooser.setFont(domain.utility.currentTheme.fontPlain12);
        repeatEventEndDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                repeatEventEndDateChooserPropertyChange(evt);
            }
        });

        reSunCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reSunCheckBox.setText(domain.language.getString ("su"));
        reSunCheckBox.setEnabled(false);
        reSunCheckBox.setOpaque(false);
        reSunCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reMonCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reMonCheckBox.setText(domain.language.getString ("m"));
        reMonCheckBox.setEnabled(false);
        reMonCheckBox.setOpaque(false);
        reMonCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reTueCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reTueCheckBox.setText(domain.language.getString ("t"));
        reTueCheckBox.setEnabled(false);
        reTueCheckBox.setOpaque(false);
        reTueCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reWedCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reWedCheckBox.setText(domain.language.getString ("w"));
        reWedCheckBox.setEnabled(false);
        reWedCheckBox.setOpaque(false);
        reWedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reThuCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reThuCheckBox.setText(domain.language.getString ("th"));
        reThuCheckBox.setEnabled(false);
        reThuCheckBox.setOpaque(false);
        reThuCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reFriCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reFriCheckBox.setText(domain.language.getString ("f"));
        reFriCheckBox.setEnabled(false);
        reFriCheckBox.setOpaque(false);
        reFriCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        reSatCheckBox.setFont(domain.utility.currentTheme.fontPlain11);
        reSatCheckBox.setText(domain.language.getString ("sa"));
        reSatCheckBox.setEnabled(false);
        reSatCheckBox.setOpaque(false);
        reSatCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatDayCheckBoxActionPerformed(evt);
            }
        });

        everyDescriptionLabel.setFont(domain.utility.currentTheme.fontBold11);
        everyDescriptionLabel.setText("<<Desc>>");

        javax.swing.GroupLayout repeatDialogPanelLayout = new javax.swing.GroupLayout(repeatDialogPanel);
        repeatDialogPanel.setLayout(repeatDialogPanelLayout);
        repeatDialogPanelLayout.setHorizontalGroup(
            repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                        .addComponent(repeatsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repeatEventRepeatsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                        .addComponent(repeatsEveryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repeatEventRepeatsEveryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(everyDescriptionLabel))
                    .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                        .addComponent(repeatsOnLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                                .addComponent(reThuCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reFriCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reSatCheckBox))
                            .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                                .addComponent(reSunCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reMonCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reTueCheckBox)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reWedCheckBox))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, repeatDialogPanelLayout.createSequentialGroup()
                        .addComponent(repeatsEndingLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repeatEventEndDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
                    .addComponent(repeatEventDoneButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        repeatDialogPanelLayout.setVerticalGroup(
            repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(repeatDialogPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatsLabel)
                    .addComponent(repeatEventRepeatsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatsEveryLabel)
                    .addComponent(repeatEventRepeatsEveryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(everyDescriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatsOnLabel)
                    .addComponent(reSunCheckBox)
                    .addComponent(reMonCheckBox)
                    .addComponent(reTueCheckBox)
                    .addComponent(reWedCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(reThuCheckBox)
                    .addComponent(reFriCheckBox)
                    .addComponent(reSatCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(repeatDialogPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(repeatsEndingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(repeatEventEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repeatEventDoneButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout repeatEventDialogLayout = new javax.swing.GroupLayout(repeatEventDialog.getContentPane());
        repeatEventDialog.getContentPane().setLayout(repeatEventDialogLayout);
        repeatEventDialogLayout.setHorizontalGroup(
            repeatEventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(repeatDialogPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        repeatEventDialogLayout.setVerticalGroup(
            repeatEventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(repeatDialogPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        fileChooser.setFileFilter(new ExtensionFileFilter (domain.language.getString ("getOrganizedBackupFiles") + " (.gbak)", new String[] {"GBAK"}));

        askPopupMenu.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                askPopupMenuPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        printingDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        printingDialog.setTitle(domain.language.getString ("printing"));
        printingDialog.setAlwaysOnTop(true);
        printingDialog.setName("printingDialog"); // NOI18N
        printingDialog.setResizable(false);

        printingJLabel.setFont(domain.utility.currentTheme.fontBold12);
        printingJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "printer.png"))); // NOI18N
        printingJLabel.setText(domain.language.getString ("getOrganizedPrinting"));

        javax.swing.GroupLayout printingJPanelLayout = new javax.swing.GroupLayout(printingJPanel);
        printingJPanel.setLayout(printingJPanelLayout);
        printingJPanelLayout.setHorizontalGroup(
            printingJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printingJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printingJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                .addContainerGap())
        );
        printingJPanelLayout.setVerticalGroup(
            printingJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printingJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printingJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout printingDialogLayout = new javax.swing.GroupLayout(printingDialog.getContentPane());
        printingDialog.getContentPane().setLayout(printingDialogLayout);
        printingDialogLayout.setHorizontalGroup(
            printingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printingJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        printingDialogLayout.setVerticalGroup(
            printingDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printingJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setName("viewPanel"); // NOI18N
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                formAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        setLayout(new java.awt.BorderLayout());

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N
        toolBar.setPreferredSize(new java.awt.Dimension(1414, 53));
        add(toolBar, java.awt.BorderLayout.NORTH);

        leftPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        leftPanel.setName("leftPanel"); // NOI18N
        leftPanel.setPreferredSize(new java.awt.Dimension(227, 570));

        termScrollPane.setName("termScrollPane"); // NOI18N
        termScrollPane.setOpaque(false);

        termTree.setBackground(new java.awt.Color(238, 238, 238));
        termTree.setFont(domain.utility.currentTheme.fontPlain12);
        termTree.setModel(termsModel);
        termTree.setCellRenderer(new DefaultTreeCellRenderer ()
            {
                @Override
                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
                {
                    super.getTreeCellRendererComponent (tree, value, sel, expanded, leaf, row, hasFocus);

                    if (value instanceof Term || value instanceof Course)
                    {
                        setIcon ((Icon) new ImageIcon (getClass ().getResource (Domain.IMAGES_FOLDER + "terms_courses_mini.png")));
                    }

                    return this;
                }

                @Override
                public Color getBackground()
                {
                    return null;
                }

                @Override
                public Color getBackgroundNonSelectionColor()
                {
                    return null;
                }
            });
            termTree.setName("termTree"); // NOI18N
            termTree.setOpaque(false);
            termTree.setRootVisible(false);
            termTree.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    termTreeMouseReleased(evt);
                }
            });
            termScrollPane.setViewportView(termTree);

            miniCalendar.setFont(domain.utility.currentTheme.fontPlain11);
            miniCalendar.setOpaque(true);
            miniCalendar.setWeekOfYearVisible(false);
            miniCalendar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    miniCalendarPropertyChange(evt);
                }
            });

            javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
            leftPanel.setLayout(leftPanelLayout);
            leftPanelLayout.setHorizontalGroup(
                leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(miniCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 223, Short.MAX_VALUE)
                .addComponent(termScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
            );
            leftPanelLayout.setVerticalGroup(
                leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                    .addComponent(termScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(miniCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            add(leftPanel, java.awt.BorderLayout.WEST);

            middlePanel.setName("middlePanel"); // NOI18N
            middlePanel.setLayout(new java.awt.CardLayout());

            loadingPanel.setLayout(new java.awt.BorderLayout());

            loadingLabel.setFont(domain.utility.currentTheme.fontBold18);
            loadingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            loadingLabel.setText(domain.language.getString ("loading") + "...");
            loadingPanel.add(loadingLabel, java.awt.BorderLayout.CENTER);

            middlePanel.add(loadingPanel, "loadingCard");

            contentPanel.setOpaque(false);
            contentPanel.setLayout(new java.awt.BorderLayout());

            upperPanelForFilters.setOpaque(false);
            upperPanelForFilters.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

            filter1ButtonGroup.add(bothFilterRadioButton);
            bothFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            bothFilterRadioButton.setSelected(true);
            bothFilterRadioButton.setText(domain.language.getString ("both"));
            bothFilterRadioButton.setToolTipText(domain.language.getString ("bothToolTip"));
            bothFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            bothFilterRadioButton.setOpaque(false);
            bothFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter1ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(bothFilterRadioButton);

            filter1ButtonGroup.add(assignmentsFilterRadioButton);
            assignmentsFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            assignmentsFilterRadioButton.setText(domain.language.getString ("assignments"));
            assignmentsFilterRadioButton.setToolTipText(domain.language.getString ("assignmentsToolTip"));
            assignmentsFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            assignmentsFilterRadioButton.setOpaque(false);
            assignmentsFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter1ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(assignmentsFilterRadioButton);

            filter1ButtonGroup.add(eventsFilterRadioButton);
            eventsFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            eventsFilterRadioButton.setText(domain.language.getString ("events"));
            eventsFilterRadioButton.setToolTipText(domain.language.getString ("eventsToolTip"));
            eventsFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            eventsFilterRadioButton.setOpaque(false);
            eventsFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter1ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(eventsFilterRadioButton);

            jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);
            jSeparator6.setPreferredSize(new java.awt.Dimension(4, 20));
            upperPanelForFilters.add(jSeparator6);

            filter2ButtonGroup.add(allFilterRadioButton);
            allFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            allFilterRadioButton.setSelected(true);
            allFilterRadioButton.setText(domain.language.getString ("all"));
            allFilterRadioButton.setToolTipText(domain.language.getString ("allToolTip"));
            allFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            allFilterRadioButton.setOpaque(false);
            allFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter2ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(allFilterRadioButton);

            filter2ButtonGroup.add(overdueFilterRadioButton);
            overdueFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            overdueFilterRadioButton.setText(domain.language.getString ("overdue"));
            overdueFilterRadioButton.setToolTipText(domain.language.getString ("overdueToolTip"));
            overdueFilterRadioButton.setOpaque(false);
            overdueFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter2ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(overdueFilterRadioButton);

            filter2ButtonGroup.add(doneFilterRadioButton);
            doneFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            doneFilterRadioButton.setText(domain.language.getString ("done"));
            doneFilterRadioButton.setToolTipText(domain.language.getString ("doneToolTip"));
            doneFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            doneFilterRadioButton.setOpaque(false);
            doneFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter2ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(doneFilterRadioButton);

            filter2ButtonGroup.add(notDoneFilterRadioButton);
            notDoneFilterRadioButton.setFont(domain.utility.currentTheme.fontPlain11);
            notDoneFilterRadioButton.setText(domain.language.getString ("notDone"));
            notDoneFilterRadioButton.setToolTipText(domain.language.getString ("notDoneToolTip"));
            notDoneFilterRadioButton.setMargin(new java.awt.Insets(0, 1, 0, 1));
            notDoneFilterRadioButton.setOpaque(false);
            notDoneFilterRadioButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    filter2ButtonActionPerformed(evt);
                }
            });
            upperPanelForFilters.add(notDoneFilterRadioButton);

            contentPanel.add(upperPanelForFilters, java.awt.BorderLayout.NORTH);

            middlePanelForTabs.setOpaque(false);
            middlePanelForTabs.setLayout(new java.awt.GridLayout(1, 0));

            middleTabbedPane.setFont(domain.utility.currentTheme.fontPlain11);
            middleTabbedPane.setName("middleTabbedPane"); // NOI18N
            middleTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    middleTabbedPaneStateChanged(evt);
                }
            });

            listViewPanel.setName("listViewPanel"); // NOI18N

            assignmentsTable.setFont(domain.utility.currentTheme.fontPlain11);
            assignmentsTable.setModel(assignmentsTableModel);
            assignmentsTable.setGridColor(new java.awt.Color(200, 200, 200));
            assignmentsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            assignmentsTable.getTableHeader().setReorderingAllowed(false);
            assignmentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    assignmentsTableMousePressed(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    assignmentsTableMouseReleased(evt);
                }
            });
            assignmentsListScrollPane.setViewportView(assignmentsTable);

            javax.swing.GroupLayout listViewPanelLayout = new javax.swing.GroupLayout(listViewPanel);
            listViewPanel.setLayout(listViewPanelLayout);
            listViewPanelLayout.setHorizontalGroup(
                listViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(assignmentsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
            );
            listViewPanelLayout.setVerticalGroup(
                listViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(assignmentsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
            );

            middleTabbedPane.addTab(domain.language.getString ("listView"), listViewPanel);

            javax.swing.GroupLayout weekViewPanelLayout = new javax.swing.GroupLayout(weekViewPanel);
            weekViewPanel.setLayout(weekViewPanelLayout);
            weekViewPanelLayout.setHorizontalGroup(
                weekViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 880, Short.MAX_VALUE)
            );
            weekViewPanelLayout.setVerticalGroup(
                weekViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 595, Short.MAX_VALUE)
            );

            middleTabbedPane.addTab(domain.language.getString ("weekView"), weekViewPanel);

            monthAndYearPanel.setOpaque(false);

            currentMonthLabel.setFont(domain.utility.currentTheme.fontBold12);
            currentMonthLabel.setText("Month");

            currentYearLabel.setFont(domain.utility.currentTheme.fontBold12);
            currentYearLabel.setText("Year");

            prevMonthButton.setFont(domain.utility.currentTheme.fontBold12);
            prevMonthButton.setText("<< |");
            prevMonthButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    prevMonthButtonMouseReleased(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    prevMonthButtonMouseExited(evt);
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    prevMonthButtonMouseEntered(evt);
                }
            });

            todayButton.setFont(domain.utility.currentTheme.fontBold12);
            todayButton.setText(domain.language.getString ("today"));
            todayButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    todayButtonMouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    todayButtonMouseExited(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    todayButtonMouseReleased(evt);
                }
            });

            nextMonthButton.setFont(domain.utility.currentTheme.fontBold12);
            nextMonthButton.setText("| >>");
            nextMonthButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    nextMonthButtonMouseEntered(evt);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    nextMonthButtonMouseExited(evt);
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    nextMonthButtonMouseReleased(evt);
                }
            });

            javax.swing.GroupLayout monthAndYearPanelLayout = new javax.swing.GroupLayout(monthAndYearPanel);
            monthAndYearPanel.setLayout(monthAndYearPanelLayout);
            monthAndYearPanelLayout.setHorizontalGroup(
                monthAndYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(monthAndYearPanelLayout.createSequentialGroup()
                    .addComponent(currentMonthLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(currentYearLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 688, Short.MAX_VALUE)
                    .addComponent(prevMonthButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(todayButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(nextMonthButton)
                    .addContainerGap())
            );
            monthAndYearPanelLayout.setVerticalGroup(
                monthAndYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(monthAndYearPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentMonthLabel)
                    .addComponent(currentYearLabel)
                    .addComponent(prevMonthButton)
                    .addComponent(todayButton)
                    .addComponent(nextMonthButton))
            );

            daysOfWeekPanel.setOpaque(false);
            daysOfWeekPanel.setLayout(new java.awt.GridLayout(1, 7));

            sundayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            sundayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            sundayLabel.setText(domain.language.getString ("sunday"));
            daysOfWeekPanel.add(sundayLabel);

            mondayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            mondayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            mondayLabel.setText(domain.language.getString ("monday"));
            daysOfWeekPanel.add(mondayLabel);

            tuesdayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            tuesdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            tuesdayLabel.setText(domain.language.getString ("tuesday"));
            daysOfWeekPanel.add(tuesdayLabel);

            wednesdayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            wednesdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            wednesdayLabel.setText(domain.language.getString ("wednesday"));
            daysOfWeekPanel.add(wednesdayLabel);

            thursdayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            thursdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            thursdayLabel.setText(domain.language.getString ("thursday"));
            daysOfWeekPanel.add(thursdayLabel);

            fridayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            fridayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            fridayLabel.setText(domain.language.getString ("friday"));
            daysOfWeekPanel.add(fridayLabel);

            saturdayLabel.setFont(domain.utility.currentTheme.fontPlain12);
            saturdayLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            saturdayLabel.setText(domain.language.getString ("saturday"));
            daysOfWeekPanel.add(saturdayLabel);

            monthDaysPanel.setOpaque(false);
            monthDaysPanel.setLayout(new java.awt.GridLayout(5, 7));

            week1Day1.setBorder(UNSELECTED_DAY_BORDER);
            week1Day1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day1.setLayout(new java.awt.BorderLayout());

            dayLabel1.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel1.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel1.setText("#");
            dayLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day1.add(dayLabel1, java.awt.BorderLayout.NORTH);

            dayScroll1.setBorder(null);
            dayScroll1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll1.setOpaque(false);
            dayScroll1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day1.setOpaque(false);
            day1.setLayout(new javax.swing.BoxLayout(day1, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll1.setViewportView(day1);

            week1Day1.add(dayScroll1, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day1);

            week1Day2.setBorder(UNSELECTED_DAY_BORDER);
            week1Day2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day2.setLayout(new java.awt.BorderLayout());

            dayLabel2.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel2.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel2.setText("#");
            dayLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day2.add(dayLabel2, java.awt.BorderLayout.NORTH);

            dayScroll2.setBorder(null);
            dayScroll2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll2.setOpaque(false);
            dayScroll2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day2.setOpaque(false);
            day2.setLayout(new javax.swing.BoxLayout(day2, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll2.setViewportView(day2);

            week1Day2.add(dayScroll2, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day2);

            week1Day3.setBorder(UNSELECTED_DAY_BORDER);
            week1Day3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day3.setLayout(new java.awt.BorderLayout());

            dayLabel3.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel3.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel3.setText("#");
            dayLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day3.add(dayLabel3, java.awt.BorderLayout.NORTH);

            dayScroll3.setBorder(null);
            dayScroll3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll3.setOpaque(false);
            dayScroll3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day3.setOpaque(false);
            day3.setLayout(new javax.swing.BoxLayout(day3, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll3.setViewportView(day3);

            week1Day3.add(dayScroll3, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day3);

            week1Day4.setBorder(UNSELECTED_DAY_BORDER);
            week1Day4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day4.setLayout(new java.awt.BorderLayout());

            dayLabel4.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel4.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel4.setText("#");
            dayLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day4.add(dayLabel4, java.awt.BorderLayout.NORTH);

            dayScroll4.setBorder(null);
            dayScroll4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll4.setOpaque(false);
            dayScroll4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day4.setOpaque(false);
            day4.setLayout(new javax.swing.BoxLayout(day4, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll4.setViewportView(day4);

            week1Day4.add(dayScroll4, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day4);

            week1Day5.setBorder(UNSELECTED_DAY_BORDER);
            week1Day5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day5.setLayout(new java.awt.BorderLayout());

            dayLabel5.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel5.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel5.setText("#");
            dayLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day5.add(dayLabel5, java.awt.BorderLayout.NORTH);

            dayScroll5.setBorder(null);
            dayScroll5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll5.setOpaque(false);
            dayScroll5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day5.setOpaque(false);
            day5.setLayout(new javax.swing.BoxLayout(day5, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll5.setViewportView(day5);

            week1Day5.add(dayScroll5, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day5);

            week1Day6.setBorder(UNSELECTED_DAY_BORDER);
            week1Day6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day6.setLayout(new java.awt.BorderLayout());

            dayLabel6.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel6.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel6.setText("#");
            dayLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day6.add(dayLabel6, java.awt.BorderLayout.NORTH);

            dayScroll6.setBorder(null);
            dayScroll6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll6.setOpaque(false);
            dayScroll6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day6.setOpaque(false);
            day6.setLayout(new javax.swing.BoxLayout(day6, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll6.setViewportView(day6);

            week1Day6.add(dayScroll6, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day6);

            week1Day7.setBorder(UNSELECTED_DAY_BORDER);
            week1Day7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week1Day7.setLayout(new java.awt.BorderLayout());

            dayLabel7.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel7.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel7.setText("#");
            dayLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week1Day7.add(dayLabel7, java.awt.BorderLayout.NORTH);

            dayScroll7.setBorder(null);
            dayScroll7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll7.setOpaque(false);
            dayScroll7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day7.setOpaque(false);
            day7.setLayout(new javax.swing.BoxLayout(day7, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll7.setViewportView(day7);

            week1Day7.add(dayScroll7, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week1Day7);

            week2Day1.setBorder(UNSELECTED_DAY_BORDER);
            week2Day1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day1.setLayout(new java.awt.BorderLayout());

            dayLabel8.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel8.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel8.setText("#");
            dayLabel8.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day1.add(dayLabel8, java.awt.BorderLayout.NORTH);

            dayScroll8.setBorder(null);
            dayScroll8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll8.setOpaque(false);
            dayScroll8.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day8.setOpaque(false);
            day8.setLayout(new javax.swing.BoxLayout(day8, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll8.setViewportView(day8);

            week2Day1.add(dayScroll8, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day1);

            week2Day2.setBorder(UNSELECTED_DAY_BORDER);
            week2Day2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day2.setLayout(new java.awt.BorderLayout());

            dayLabel9.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel9.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel9.setText("#");
            dayLabel9.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day2.add(dayLabel9, java.awt.BorderLayout.NORTH);

            dayScroll9.setBorder(null);
            dayScroll9.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll9.setOpaque(false);
            dayScroll9.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day9.setOpaque(false);
            day9.setLayout(new javax.swing.BoxLayout(day9, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll9.setViewportView(day9);

            week2Day2.add(dayScroll9, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day2);

            week2Day3.setBorder(UNSELECTED_DAY_BORDER);
            week2Day3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day3.setLayout(new java.awt.BorderLayout());

            dayLabel10.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel10.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel10.setText("#");
            dayLabel10.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day3.add(dayLabel10, java.awt.BorderLayout.NORTH);

            dayScroll10.setBorder(null);
            dayScroll10.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll10.setOpaque(false);
            dayScroll10.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day10.setOpaque(false);
            day10.setLayout(new javax.swing.BoxLayout(day10, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll10.setViewportView(day10);

            week2Day3.add(dayScroll10, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day3);

            week2Day4.setBorder(UNSELECTED_DAY_BORDER);
            week2Day4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day4.setLayout(new java.awt.BorderLayout());

            dayLabel11.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel11.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel11.setText("#");
            dayLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day4.add(dayLabel11, java.awt.BorderLayout.NORTH);

            dayScroll11.setBorder(null);
            dayScroll11.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll11.setOpaque(false);
            dayScroll11.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day11.setOpaque(false);
            day11.setLayout(new javax.swing.BoxLayout(day11, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll11.setViewportView(day11);

            week2Day4.add(dayScroll11, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day4);

            week2Day5.setBorder(UNSELECTED_DAY_BORDER);
            week2Day5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day5.setLayout(new java.awt.BorderLayout());

            dayLabel12.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel12.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel12.setText("#");
            dayLabel12.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day5.add(dayLabel12, java.awt.BorderLayout.NORTH);

            dayScroll12.setBorder(null);
            dayScroll12.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll12.setOpaque(false);
            dayScroll12.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day12.setOpaque(false);
            day12.setLayout(new javax.swing.BoxLayout(day12, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll12.setViewportView(day12);

            week2Day5.add(dayScroll12, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day5);

            week2Day6.setBorder(UNSELECTED_DAY_BORDER);
            week2Day6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day6.setLayout(new java.awt.BorderLayout());

            dayLabel13.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel13.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel13.setText("#");
            dayLabel13.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day6.add(dayLabel13, java.awt.BorderLayout.NORTH);

            dayScroll13.setBorder(null);
            dayScroll13.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll13.setOpaque(false);
            dayScroll13.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day13.setOpaque(false);
            day13.setLayout(new javax.swing.BoxLayout(day13, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll13.setViewportView(day13);

            week2Day6.add(dayScroll13, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day6);

            week2Day7.setBorder(UNSELECTED_DAY_BORDER);
            week2Day7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week2Day7.setLayout(new java.awt.BorderLayout());

            dayLabel14.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel14.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel14.setText("#");
            dayLabel14.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week2Day7.add(dayLabel14, java.awt.BorderLayout.NORTH);

            dayScroll14.setBorder(null);
            dayScroll14.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll14.setOpaque(false);
            dayScroll14.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day14.setOpaque(false);
            day14.setLayout(new javax.swing.BoxLayout(day14, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll14.setViewportView(day14);

            week2Day7.add(dayScroll14, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week2Day7);

            week3Day1.setBorder(UNSELECTED_DAY_BORDER);
            week3Day1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day1.setLayout(new java.awt.BorderLayout());

            dayLabel15.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel15.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel15.setText("#");
            dayLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day1.add(dayLabel15, java.awt.BorderLayout.NORTH);

            dayScroll15.setBorder(null);
            dayScroll15.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll15.setOpaque(false);
            dayScroll15.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day15.setOpaque(false);
            day15.setLayout(new javax.swing.BoxLayout(day15, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll15.setViewportView(day15);

            week3Day1.add(dayScroll15, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day1);

            week3Day2.setBorder(UNSELECTED_DAY_BORDER);
            week3Day2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day2.setLayout(new java.awt.BorderLayout());

            dayLabel16.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel16.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel16.setText("#");
            dayLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day2.add(dayLabel16, java.awt.BorderLayout.NORTH);

            dayScroll16.setBorder(null);
            dayScroll16.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll16.setOpaque(false);
            dayScroll16.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day16.setOpaque(false);
            day16.setLayout(new javax.swing.BoxLayout(day16, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll16.setViewportView(day16);

            week3Day2.add(dayScroll16, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day2);

            week3Day3.setBorder(UNSELECTED_DAY_BORDER);
            week3Day3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day3.setLayout(new java.awt.BorderLayout());

            dayLabel17.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel17.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel17.setText("#");
            dayLabel17.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day3.add(dayLabel17, java.awt.BorderLayout.NORTH);

            dayScroll17.setBorder(null);
            dayScroll17.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll17.setOpaque(false);
            dayScroll17.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day17.setOpaque(false);
            day17.setLayout(new javax.swing.BoxLayout(day17, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll17.setViewportView(day17);

            week3Day3.add(dayScroll17, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day3);

            week3Day4.setBorder(UNSELECTED_DAY_BORDER);
            week3Day4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day4.setLayout(new java.awt.BorderLayout());

            dayLabel18.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel18.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel18.setText("#");
            dayLabel18.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day4.add(dayLabel18, java.awt.BorderLayout.NORTH);

            dayScroll18.setBorder(null);
            dayScroll18.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll18.setOpaque(false);
            dayScroll18.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day18.setOpaque(false);
            day18.setLayout(new javax.swing.BoxLayout(day18, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll18.setViewportView(day18);

            week3Day4.add(dayScroll18, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day4);

            week3Day5.setBorder(UNSELECTED_DAY_BORDER);
            week3Day5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day5.setLayout(new java.awt.BorderLayout());

            dayLabel19.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel19.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel19.setText("#");
            dayLabel19.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day5.add(dayLabel19, java.awt.BorderLayout.NORTH);

            dayScroll19.setBorder(null);
            dayScroll19.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll19.setOpaque(false);
            dayScroll19.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day19.setOpaque(false);
            day19.setLayout(new javax.swing.BoxLayout(day19, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll19.setViewportView(day19);

            week3Day5.add(dayScroll19, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day5);

            week3Day6.setBorder(UNSELECTED_DAY_BORDER);
            week3Day6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day6.setLayout(new java.awt.BorderLayout());

            dayLabel20.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel20.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel20.setText("#");
            dayLabel20.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day6.add(dayLabel20, java.awt.BorderLayout.NORTH);

            dayScroll20.setBorder(null);
            dayScroll20.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll20.setOpaque(false);
            dayScroll20.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day20.setOpaque(false);
            day20.setLayout(new javax.swing.BoxLayout(day20, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll20.setViewportView(day20);

            week3Day6.add(dayScroll20, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day6);

            week3Day7.setBorder(UNSELECTED_DAY_BORDER);
            week3Day7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week3Day7.setLayout(new java.awt.BorderLayout());

            dayLabel21.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel21.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel21.setText("#");
            dayLabel21.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week3Day7.add(dayLabel21, java.awt.BorderLayout.NORTH);

            dayScroll21.setBorder(null);
            dayScroll21.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll21.setOpaque(false);
            dayScroll21.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day21.setOpaque(false);
            day21.setLayout(new javax.swing.BoxLayout(day21, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll21.setViewportView(day21);

            week3Day7.add(dayScroll21, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week3Day7);

            week4Day1.setBorder(UNSELECTED_DAY_BORDER);
            week4Day1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day1.setLayout(new java.awt.BorderLayout());

            dayLabel22.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel22.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel22.setText("#");
            dayLabel22.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day1.add(dayLabel22, java.awt.BorderLayout.NORTH);

            dayScroll22.setBorder(null);
            dayScroll22.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll22.setOpaque(false);
            dayScroll22.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day22.setOpaque(false);
            day22.setLayout(new javax.swing.BoxLayout(day22, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll22.setViewportView(day22);

            week4Day1.add(dayScroll22, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day1);

            week4Day2.setBorder(UNSELECTED_DAY_BORDER);
            week4Day2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day2.setLayout(new java.awt.BorderLayout());

            dayLabel23.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel23.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel23.setText("#");
            dayLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day2.add(dayLabel23, java.awt.BorderLayout.NORTH);

            dayScroll23.setBorder(null);
            dayScroll23.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll23.setOpaque(false);
            dayScroll23.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day23.setOpaque(false);
            day23.setLayout(new javax.swing.BoxLayout(day23, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll23.setViewportView(day23);

            week4Day2.add(dayScroll23, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day2);

            week4Day3.setBorder(UNSELECTED_DAY_BORDER);
            week4Day3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day3.setLayout(new java.awt.BorderLayout());

            dayLabel24.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel24.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel24.setText("#");
            dayLabel24.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day3.add(dayLabel24, java.awt.BorderLayout.NORTH);

            dayScroll24.setBorder(null);
            dayScroll24.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll24.setOpaque(false);
            dayScroll24.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day24.setOpaque(false);
            day24.setLayout(new javax.swing.BoxLayout(day24, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll24.setViewportView(day24);

            week4Day3.add(dayScroll24, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day3);

            week4Day4.setBorder(UNSELECTED_DAY_BORDER);
            week4Day4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day4.setLayout(new java.awt.BorderLayout());

            dayLabel25.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel25.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel25.setText("#");
            dayLabel25.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day4.add(dayLabel25, java.awt.BorderLayout.NORTH);

            dayScroll25.setBorder(null);
            dayScroll25.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll25.setOpaque(false);
            dayScroll25.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day25.setOpaque(false);
            day25.setLayout(new javax.swing.BoxLayout(day25, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll25.setViewportView(day25);

            week4Day4.add(dayScroll25, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day4);

            week4Day5.setBorder(UNSELECTED_DAY_BORDER);
            week4Day5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day5.setLayout(new java.awt.BorderLayout());

            dayLabel26.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel26.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel26.setText("#");
            dayLabel26.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day5.add(dayLabel26, java.awt.BorderLayout.NORTH);

            dayScroll26.setBorder(null);
            dayScroll26.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll26.setOpaque(false);
            dayScroll26.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day26.setOpaque(false);
            day26.setLayout(new javax.swing.BoxLayout(day26, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll26.setViewportView(day26);

            week4Day5.add(dayScroll26, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day5);

            week4Day6.setBorder(UNSELECTED_DAY_BORDER);
            week4Day6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day6.setLayout(new java.awt.BorderLayout());

            dayLabel27.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel27.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel27.setText("#");
            dayLabel27.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day6.add(dayLabel27, java.awt.BorderLayout.NORTH);

            dayScroll27.setBorder(null);
            dayScroll27.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll27.setOpaque(false);
            dayScroll27.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day27.setOpaque(false);
            day27.setLayout(new javax.swing.BoxLayout(day27, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll27.setViewportView(day27);

            week4Day6.add(dayScroll27, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day6);

            week4Day7.setBorder(UNSELECTED_DAY_BORDER);
            week4Day7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week4Day7.setLayout(new java.awt.BorderLayout());

            dayLabel28.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel28.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel28.setText("#");
            dayLabel28.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week4Day7.add(dayLabel28, java.awt.BorderLayout.NORTH);

            dayScroll28.setBorder(null);
            dayScroll28.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll28.setOpaque(false);
            dayScroll28.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day28.setOpaque(false);
            day28.setLayout(new javax.swing.BoxLayout(day28, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll28.setViewportView(day28);

            week4Day7.add(dayScroll28, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week4Day7);

            week5Day1.setBorder(UNSELECTED_DAY_BORDER);
            week5Day1.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day1.setLayout(new java.awt.BorderLayout());

            dayLabel29.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel29.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel29.setText("#");
            dayLabel29.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day1.add(dayLabel29, java.awt.BorderLayout.NORTH);

            dayScroll29.setBorder(null);
            dayScroll29.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll29.setOpaque(false);
            dayScroll29.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day29.setOpaque(false);
            day29.setLayout(new javax.swing.BoxLayout(day29, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll29.setViewportView(day29);

            week5Day1.add(dayScroll29, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day1);

            week5Day2.setBorder(UNSELECTED_DAY_BORDER);
            week5Day2.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day2.setLayout(new java.awt.BorderLayout());

            dayLabel30.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel30.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel30.setText("#");
            dayLabel30.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day2.add(dayLabel30, java.awt.BorderLayout.NORTH);

            dayScroll30.setBorder(null);
            dayScroll30.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll30.setOpaque(false);
            dayScroll30.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day30.setOpaque(false);
            day30.setLayout(new javax.swing.BoxLayout(day30, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll30.setViewportView(day30);

            week5Day2.add(dayScroll30, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day2);

            week5Day3.setBorder(UNSELECTED_DAY_BORDER);
            week5Day3.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day3.setLayout(new java.awt.BorderLayout());

            dayLabel31.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel31.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel31.setText("#");
            dayLabel31.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day3.add(dayLabel31, java.awt.BorderLayout.NORTH);

            dayScroll31.setBorder(null);
            dayScroll31.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll31.setOpaque(false);
            dayScroll31.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day31.setOpaque(false);
            day31.setLayout(new javax.swing.BoxLayout(day31, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll31.setViewportView(day31);

            week5Day3.add(dayScroll31, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day3);

            week5Day4.setBorder(UNSELECTED_DAY_BORDER);
            week5Day4.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day4.setLayout(new java.awt.BorderLayout());

            dayLabel32.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel32.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel32.setText("#");
            dayLabel32.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day4.add(dayLabel32, java.awt.BorderLayout.NORTH);

            dayScroll32.setBorder(null);
            dayScroll32.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll32.setOpaque(false);
            dayScroll32.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day32.setOpaque(false);
            day32.setLayout(new javax.swing.BoxLayout(day32, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll32.setViewportView(day32);

            week5Day4.add(dayScroll32, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day4);

            week5Day5.setBorder(UNSELECTED_DAY_BORDER);
            week5Day5.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day5.setLayout(new java.awt.BorderLayout());

            dayLabel33.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel33.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel33.setText("#");
            dayLabel33.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day5.add(dayLabel33, java.awt.BorderLayout.NORTH);

            dayScroll33.setBorder(null);
            dayScroll33.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll33.setOpaque(false);
            dayScroll33.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day33.setOpaque(false);
            day33.setLayout(new javax.swing.BoxLayout(day33, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll33.setViewportView(day33);

            week5Day5.add(dayScroll33, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day5);

            week5Day6.setBorder(UNSELECTED_DAY_BORDER);
            week5Day6.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day6.setLayout(new java.awt.BorderLayout());

            dayLabel34.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel34.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel34.setText("#");
            dayLabel34.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day6.add(dayLabel34, java.awt.BorderLayout.NORTH);

            dayScroll34.setBorder(null);
            dayScroll34.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll34.setOpaque(false);
            dayScroll34.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day34.setOpaque(false);
            day34.setLayout(new javax.swing.BoxLayout(day34, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll34.setViewportView(day34);

            week5Day6.add(dayScroll34, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day6);

            week5Day7.setBorder(UNSELECTED_DAY_BORDER);
            week5Day7.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    dayPanelMouseReleased(evt);
                }
            });
            week5Day7.setLayout(new java.awt.BorderLayout());

            dayLabel35.setFont(domain.utility.currentTheme.fontPlain12);
            dayLabel35.setForeground(domain.utility.currentTheme.colorDayInMonthText);
            dayLabel35.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            dayLabel35.setText("#");
            dayLabel35.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            week5Day7.add(dayLabel35, java.awt.BorderLayout.NORTH);

            dayScroll35.setBorder(null);
            dayScroll35.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dayScroll35.setOpaque(false);
            dayScroll35.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    scrollPaneMouseReleased(evt);
                }
            });

            day35.setOpaque(false);
            day35.setLayout(new javax.swing.BoxLayout(day35, javax.swing.BoxLayout.PAGE_AXIS));
            dayScroll35.setViewportView(day35);

            week5Day7.add(dayScroll35, java.awt.BorderLayout.CENTER);

            monthDaysPanel.add(week5Day7);

            javax.swing.GroupLayout monthViewPanelLayout = new javax.swing.GroupLayout(monthViewPanel);
            monthViewPanel.setLayout(monthViewPanelLayout);
            monthViewPanelLayout.setHorizontalGroup(
                monthViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(monthAndYearPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(daysOfWeekPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
                .addComponent(monthDaysPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
            );
            monthViewPanelLayout.setVerticalGroup(
                monthViewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(monthViewPanelLayout.createSequentialGroup()
                    .addComponent(monthAndYearPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(daysOfWeekPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(monthDaysPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE))
            );

            middleTabbedPane.addTab(domain.language.getString ("monthView"), monthViewPanel);

            middlePanelForTabs.add(middleTabbedPane);

            contentPanel.add(middlePanelForTabs, java.awt.BorderLayout.CENTER);

            middlePanel.add(contentPanel, "card4");

            add(middlePanel, java.awt.BorderLayout.CENTER);

            rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, domain.language.getString ("userDetails"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, domain.utility.currentTheme.fontBold12));
            rightPanel.setName("rightPanel"); // NOI18N
            rightPanel.setPreferredSize(new java.awt.Dimension(237, 570));
            rightPanel.setLayout(new java.awt.CardLayout());

            blankContentPanel.setOpaque(false);

            javax.swing.GroupLayout blankContentPanelLayout = new javax.swing.GroupLayout(blankContentPanel);
            blankContentPanel.setLayout(blankContentPanelLayout);
            blankContentPanelLayout.setHorizontalGroup(
                blankContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 221, Short.MAX_VALUE)
            );
            blankContentPanelLayout.setVerticalGroup(
                blankContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 616, Short.MAX_VALUE)
            );

            rightPanel.add(blankContentPanel, "card7");

            noUserDetailsPanel.setOpaque(false);

            noStudentAdvisorDetails.setFont(domain.utility.currentTheme.fontBold12);
            noStudentAdvisorDetails.setText("<html>" + domain.language.getString ("noUserDetailsString") + "</html>");

            editUserDetailsButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
            editUserDetailsButton.setFont(domain.utility.currentTheme.fontPlain12);
            editUserDetailsButton.setText(domain.language.getString ("editUserDetails"));
            editUserDetailsButton.setToolTipText(domain.language.getString ("editUserDetailsToolTip"));
            editUserDetailsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    editUserDetailsButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout noUserDetailsPanelLayout = new javax.swing.GroupLayout(noUserDetailsPanel);
            noUserDetailsPanel.setLayout(noUserDetailsPanelLayout);
            noUserDetailsPanelLayout.setHorizontalGroup(
                noUserDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(noUserDetailsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(noUserDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(noStudentAdvisorDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(editUserDetailsButton))
                    .addContainerGap())
            );
            noUserDetailsPanelLayout.setVerticalGroup(
                noUserDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(noUserDetailsPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(noStudentAdvisorDetails)
                    .addGap(18, 18, 18)
                    .addComponent(editUserDetailsButton)
                    .addContainerGap(550, Short.MAX_VALUE))
            );

            rightPanel.add(noUserDetailsPanel, "card8");

            userDetailsContentPanel.setOpaque(false);

            userNameDetailsLabel.setFont(domain.utility.currentTheme.fontBold12);
            userNameDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            userNameDetailsLabel.setText("<<User Name>>");

            schoolDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            schoolDetailsLabel.setText("School:  <<text>>");

            idNumberDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            idNumberDetailsLabel.setText("ID number:  <<text>>");

            boxNumberDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            boxNumberDetailsLabel.setText("Box number:  <<text>>");

            advisorDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            advisorDetailsLabel.setText("Advisor:  <<text>>");

            officeHoursDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            officeHoursDetailsLabel.setText("Office hours: <<text>>");

            officeLocationDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            officeLocationDetailsLabel.setText("Office location: <<text>>");

            contactAdvisorButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
            contactAdvisorButton.setFont(domain.utility.currentTheme.fontPlain12);
            contactAdvisorButton.setText(domain.language.getString ("contactAdvisor"));
            contactAdvisorButton.setToolTipText(domain.language.getString ("contactAdvisorToolTip"));
            contactAdvisorButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    contactAdvisorButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout userDetailsContentPanelLayout = new javax.swing.GroupLayout(userDetailsContentPanel);
            userDetailsContentPanel.setLayout(userDetailsContentPanelLayout);
            userDetailsContentPanelLayout.setHorizontalGroup(
                userDetailsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(userDetailsContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(userDetailsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(userNameDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(schoolDetailsLabel)
                        .addComponent(idNumberDetailsLabel)
                        .addComponent(boxNumberDetailsLabel)
                        .addComponent(advisorDetailsLabel)
                        .addComponent(officeHoursDetailsLabel)
                        .addComponent(officeLocationDetailsLabel)
                        .addComponent(contactAdvisorButton))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            userDetailsContentPanelLayout.setVerticalGroup(
                userDetailsContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(userDetailsContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(userNameDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(schoolDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(idNumberDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(boxNumberDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(advisorDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(officeHoursDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(officeLocationDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(contactAdvisorButton)
                    .addContainerGap(406, Short.MAX_VALUE))
            );

            rightPanel.add(userDetailsContentPanel, "card4");

            termContentPanel.setOpaque(false);

            termNameDetailsLabel.setFont(domain.utility.currentTheme.fontBold12);
            termNameDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            termNameDetailsLabel.setText("<<Term Name>>");

            termCoursesDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termCoursesDetailsLabel.setText("Courses: <<count>>");

            termAvgGradeDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termAvgGradeDetailsLabel.setText("Average grade: <<grade>>");

            termTextbooksDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termTextbooksDetailsLabel.setText("Textbooks: <<count>>");

            termStartDateDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termStartDateDetailsLabel.setText("Start date: <<date>>");

            termEndDateDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termEndDateDetailsLabel.setText("End date: <<date>>");

            termTypesDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termTypesDetailsLabel.setText("Types: <<count>>");

            termTotalAssignmentsDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termTotalAssignmentsDetailsLabel.setText("Total assignments: <<count>>");

            termUnfinishedDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termUnfinishedDetailsLabel.setText("    Unfinished: <<count>>");

            termCreditsDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            termCreditsDetailsLabel.setText("Credits: <<count>>");

            javax.swing.GroupLayout termContentPanelLayout = new javax.swing.GroupLayout(termContentPanel);
            termContentPanel.setLayout(termContentPanelLayout);
            termContentPanelLayout.setHorizontalGroup(
                termContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(termContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(termContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(termNameDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(termStartDateDetailsLabel)
                        .addComponent(termEndDateDetailsLabel)
                        .addComponent(termCoursesDetailsLabel)
                        .addComponent(termCreditsDetailsLabel)
                        .addComponent(termTotalAssignmentsDetailsLabel)
                        .addComponent(termAvgGradeDetailsLabel)
                        .addComponent(termTypesDetailsLabel)
                        .addComponent(termTextbooksDetailsLabel)
                        .addComponent(termUnfinishedDetailsLabel))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            termContentPanelLayout.setVerticalGroup(
                termContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(termContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(termNameDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(termStartDateDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(termEndDateDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(termCoursesDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(termCreditsDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(termTotalAssignmentsDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(termUnfinishedDetailsLabel)
                    .addGap(17, 17, 17)
                    .addComponent(termAvgGradeDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(termTypesDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(termTextbooksDetailsLabel)
                    .addContainerGap(352, Short.MAX_VALUE))
            );

            rightPanel.add(termContentPanel, "card6");

            courseContentPanel.setOpaque(false);

            courseNameDetailsLabel.setFont(domain.utility.currentTheme.fontBold12);
            courseNameDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            courseNameDetailsLabel.setText("<<Course Name>>");

            courseStartDateDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseStartDateDetailsLabel.setText("Start date: <<date>>");

            courseEndDateDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseEndDateDetailsLabel.setText("End date: <<date>>");

            courseStartTimeDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseStartTimeDetailsLabel.setText("Start time: <<time>>");

            courseEndTimeDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseEndTimeDetailsLabel.setText("End time: <<time>>");

            courseCreditsDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseCreditsDetailsLabel.setText("Credits: <<num>>");

            courseRoomDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseRoomDetailsLabel.setText("Room: <<num>>");

            courseTotalAssignmentsDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseTotalAssignmentsDetailsLabel.setText("Total assignments: <<count>>");

            courseUnfinishedDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseUnfinishedDetailsLabel.setText("    Unfinished: <<count>>");

            courseCurrentGradeDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseCurrentGradeDetailsLabel.setText("Current grade: <<grade>>");

            courseTypesDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseTypesDetailsLabel.setText("Types: <<count>>");

            courseTextbooksDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseTextbooksDetailsLabel.setText("Textbooks: <<count>>");

            courseDaysDetailsLabel.setFont(domain.utility.currentTheme.fontPlain12);
            courseDaysDetailsLabel.setText("Day(s): <<days>>");
            courseDaysDetailsLabel.setPreferredSize(new java.awt.Dimension(106, 32));

            javax.swing.GroupLayout courseContentPanelLayout = new javax.swing.GroupLayout(courseContentPanel);
            courseContentPanel.setLayout(courseContentPanelLayout);
            courseContentPanelLayout.setHorizontalGroup(
                courseContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(courseContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(courseContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(courseContentPanelLayout.createSequentialGroup()
                            .addGroup(courseContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(courseNameDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(courseCreditsDetailsLabel)
                                .addComponent(courseRoomDetailsLabel)
                                .addComponent(courseStartDateDetailsLabel)
                                .addComponent(courseEndDateDetailsLabel)
                                .addComponent(courseStartTimeDetailsLabel)
                                .addComponent(courseEndTimeDetailsLabel))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(courseContentPanelLayout.createSequentialGroup()
                            .addComponent(courseDaysDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                            .addGap(13, 13, 13))
                        .addGroup(courseContentPanelLayout.createSequentialGroup()
                            .addGroup(courseContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(courseTotalAssignmentsDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(courseCurrentGradeDetailsLabel)
                                .addComponent(courseTypesDetailsLabel)
                                .addComponent(courseTextbooksDetailsLabel)
                                .addComponent(courseUnfinishedDetailsLabel))
                            .addContainerGap(59, Short.MAX_VALUE))))
            );
            courseContentPanelLayout.setVerticalGroup(
                courseContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(courseContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(courseNameDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(courseCreditsDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseRoomDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(courseStartDateDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseEndDateDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(courseStartTimeDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseEndTimeDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseDaysDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseTotalAssignmentsDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseUnfinishedDetailsLabel)
                    .addGap(17, 17, 17)
                    .addComponent(courseCurrentGradeDetailsLabel)
                    .addGap(18, 18, 18)
                    .addComponent(courseTypesDetailsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(courseTextbooksDetailsLabel)
                    .addContainerGap(274, Short.MAX_VALUE))
            );

            rightPanel.add(courseContentPanel, "card5");

            assignmentContentPanel.setOpaque(false);

            assignmentNameTextField.setFont(domain.utility.currentTheme.fontPlain12);
            assignmentNameTextField.setToolTipText(domain.language.getString ("assignmentNameToolTip"));
            assignmentNameTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    assignmentNameTextFieldActionPerformed(evt);
                }
            });
            assignmentNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textFieldFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    assignmentNameTextFieldFocusLost(evt);
                }
            });
            assignmentNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textFieldKeyPressed(evt);
                }
            });

            courseLabel.setFont(domain.utility.currentTheme.fontBold11);
            courseLabel.setText(domain.language.getString ("course") + ":");

            textbookLabel.setFont(domain.utility.currentTheme.fontBold11);
            textbookLabel.setText(domain.language.getString ("textbook") + ":");

            typeLabel.setFont(domain.utility.currentTheme.fontBold11);
            typeLabel.setText(domain.language.getString ("type") + ":");

            priorityLabel.setFont(domain.utility.currentTheme.fontBold11);
            priorityLabel.setText(domain.language.getString ("priority") + ":");

            detailsTypeComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
            detailsTypeComboBox.setFont(domain.utility.currentTheme.fontPlain12);
            detailsTypeComboBox.setModel(typeComboModel);
            detailsTypeComboBox.setToolTipText(domain.language.getString ("assnTypeToolTip"));
            detailsTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    detailsTypeComboBoxItemStateChanged(evt);
                }
            });

            detailsCourseComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
            detailsCourseComboBox.setFont(domain.utility.currentTheme.fontPlain12);
            detailsCourseComboBox.setModel(courseComboModel);
            detailsCourseComboBox.setToolTipText(domain.language.getString ("assnCourseToolTip"));
            detailsCourseComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    detailsCourseComboBoxItemStateChanged(evt);
                }
            });

            detailsTextbookComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
            detailsTextbookComboBox.setFont(domain.utility.currentTheme.fontPlain12);
            detailsTextbookComboBox.setModel(textbookComboModel);
            detailsTextbookComboBox.setToolTipText(domain.language.getString ("assnTextbookToolTip"));
            detailsTextbookComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    detailsTextbookComboBoxItemStateChanged(evt);
                }
            });

            gradeLabel.setFont(domain.utility.currentTheme.fontBold11);
            gradeLabel.setText(domain.language.getString ("grade") + ":");

            gradeTextField.setFont(domain.utility.currentTheme.fontPlain12);
            gradeTextField.setToolTipText(domain.language.getString ("assnGradeToolTip"));
            gradeTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    gradeTextFieldActionPerformed(evt);
                }
            });
            gradeTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textFieldFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    gradeTextFieldFocusLost(evt);
                }
            });
            gradeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textFieldKeyPressed(evt);
                }
            });

            completedCheckBox.setFont(domain.utility.currentTheme.fontBold11);
            completedCheckBox.setText(domain.language.getString ("completed"));
            completedCheckBox.setToolTipText(domain.language.getString ("assnCompletedToolTip"));
            completedCheckBox.setOpaque(false);
            completedCheckBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    completedCheckBoxActionPerformed(evt);
                }
            });

            commentsLabel.setFont(domain.utility.currentTheme.fontBold11);
            commentsLabel.setText(domain.language.getString ("comments") + ":");

            commentsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            commentsTextArea.setColumns(17);
            commentsTextArea.setFont(domain.utility.currentTheme.fontPlain12);
            commentsTextArea.setLineWrap(true);
            commentsTextArea.setRows(4);
            commentsTextArea.setTabSize(4);
            commentsTextArea.setToolTipText(domain.language.getString ("assnCommentsToolTip"));
            commentsTextArea.setWrapStyleWord(true);
            commentsTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    commentsTextAreaFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    commentsTextAreaFocusLost(evt);
                }
            });
            commentsTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    commentsTextAreaKeyPressed(evt);
                }
            });
            commentsScrollPane.setViewportView(commentsTextArea);

            prioritySlider.setFont(new java.awt.Font("Verdana", 0, 11));
            prioritySlider.setMajorTickSpacing(1);
            prioritySlider.setMaximum(5);
            prioritySlider.setMinimum(1);
            prioritySlider.setMinorTickSpacing(1);
            prioritySlider.setSnapToTicks(true);
            prioritySlider.setToolTipText(domain.language.getString ("assnPriorityToolTip"));
            prioritySlider.setOpaque(false);
            prioritySlider.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    prioritySliderStateChanged(evt);
                }
            });

            dueDateChooser.setToolTipText(domain.language.getString ("assignmentDueDateToolTip"));
            dueDateChooser.setFont(domain.utility.currentTheme.fontPlain12);
            dueDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    dueDateChooserPropertyChange(evt);
                }
            });

            dueHrChooser.setToolTipText(domain.language.getString ("assnHrDueToolTip"));
            dueHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(dueHrChooser, "h"));
            dueHrChooser.setFont(domain.utility.currentTheme.fontPlain12);
            dueHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    dueHrChooserStateChanged(evt);
                }
            });

            dueMinChooser.setToolTipText(domain.language.getString ("assnMinDueToolTip"));
            dueMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(dueMinChooser, "mm"));
            dueMinChooser.setFont(domain.utility.currentTheme.fontPlain12);
            dueMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    dueMinChooserStateChanged(evt);
                }
            });

            dueMChooser.setEditor(new javax.swing.JSpinner.DateEditor(dueMChooser, "a"));
            dueMChooser.setFont(domain.utility.currentTheme.fontPlain12);
            dueMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    dueMChooserStateChanged(evt);
                }
            });

            assnColon.setFont(domain.utility.currentTheme.fontBold12);
            assnColon.setText(":");

            javax.swing.GroupLayout assignmentContentPanelLayout = new javax.swing.GroupLayout(assignmentContentPanel);
            assignmentContentPanel.setLayout(assignmentContentPanelLayout);
            assignmentContentPanelLayout.setHorizontalGroup(
                assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, assignmentContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(commentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(assignmentsSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(completedCheckBox)
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(courseLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(detailsCourseComboBox, 0, 131, Short.MAX_VALUE))
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(textbookLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(detailsTextbookComboBox, 0, 131, Short.MAX_VALUE))
                        .addComponent(assignmentsSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(assignmentsSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(priorityLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(prioritySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(typeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(detailsTypeComboBox, 0, 131, Short.MAX_VALUE))
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(gradeLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(gradeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
                        .addComponent(commentsLabel)
                        .addComponent(dueDateChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(assignmentNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                            .addComponent(dueHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(assnColon)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dueMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dueMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );
            assignmentContentPanelLayout.setVerticalGroup(
                assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(assignmentContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(assignmentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(dueDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dueHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(assnColon)
                        .addComponent(dueMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dueMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addComponent(assignmentsSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(courseLabel)
                        .addComponent(detailsCourseComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(textbookLabel)
                        .addComponent(detailsTextbookComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(assignmentsSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(typeLabel)
                        .addComponent(detailsTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(priorityLabel)
                        .addComponent(prioritySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(completedCheckBox)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(assignmentContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(gradeLabel)
                        .addComponent(gradeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(assignmentsSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(commentsLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(commentsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addContainerGap())
            );

            rightPanel.add(assignmentContentPanel, "card2");

            eventContentPanel.setOpaque(false);

            eventNameTextField.setFont(domain.utility.currentTheme.fontPlain12);
            eventNameTextField.setToolTipText(domain.language.getString ("eventNameToolTip"));
            eventNameTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eventNameTextFieldActionPerformed(evt);
                }
            });
            eventNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textFieldFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    eventNameTextFieldFocusLost(evt);
                }
            });
            eventNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textFieldKeyPressed(evt);
                }
            });

            eventDateChooser.setToolTipText(domain.language.getString ("eventDueDateToolTip"));
            eventDateChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                public void propertyChange(java.beans.PropertyChangeEvent evt) {
                    eventDateChooserPropertyChange(evt);
                }
            });

            commentsScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            descriptionTextArea.setColumns(17);
            descriptionTextArea.setFont(domain.utility.currentTheme.fontPlain12);
            descriptionTextArea.setLineWrap(true);
            descriptionTextArea.setRows(4);
            descriptionTextArea.setTabSize(4);
            descriptionTextArea.setToolTipText(domain.language.getString ("eventDescToolTip"));
            descriptionTextArea.setWrapStyleWord(true);
            descriptionTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    commentsTextAreaFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    descriptionTextAreaFocusLost(evt);
                }
            });
            descriptionTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    descriptionTextAreaKeyPressed(evt);
                }
            });
            commentsScrollPane1.setViewportView(descriptionTextArea);

            descriptionLabel.setFont(domain.utility.currentTheme.fontBold11);
            descriptionLabel.setText(domain.language.getString ("description") + ":");

            allDayEventCheckBox.setFont(domain.utility.currentTheme.fontBold11);
            allDayEventCheckBox.setText(domain.language.getString ("allDay"));
            allDayEventCheckBox.setToolTipText(domain.language.getString ("allDayToolTip"));
            allDayEventCheckBox.setOpaque(false);
            allDayEventCheckBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    allDayEventCheckBoxActionPerformed(evt);
                }
            });

            locationLabel.setFont(domain.utility.currentTheme.fontBold11);
            locationLabel.setText(domain.language.getString ("location") + ":");

            locationTextField.setFont(domain.utility.currentTheme.fontPlain12);
            locationTextField.setToolTipText(domain.language.getString ("eventLocationToolTip"));
            locationTextField.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    locationTextFieldActionPerformed(evt);
                }
            });
            locationTextField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textFieldFocusGained(evt);
                }
                public void focusLost(java.awt.event.FocusEvent evt) {
                    locationTextFieldFocusLost(evt);
                }
            });
            locationTextField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    textFieldKeyPressed(evt);
                }
            });

            categoryComboBox.setBackground(domain.utility.currentTheme.colorButtonBackground);
            categoryComboBox.setFont(domain.utility.currentTheme.fontPlain12);
            categoryComboBox.setModel(categoryComboModel);
            categoryComboBox.setToolTipText(domain.language.getString ("eventCategoryToolTip"));
            categoryComboBox.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    categoryComboBoxItemStateChanged(evt);
                }
            });

            jLabel3.setFont(domain.utility.currentTheme.fontBold11);
            jLabel3.setText(domain.language.getString ("category") + ":");

            eventRepeatButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
            eventRepeatButton.setFont(domain.utility.currentTheme.fontPlain12);
            eventRepeatButton.setText(domain.language.getString ("repeat"));
            eventRepeatButton.setToolTipText(domain.language.getString ("repeatButtonToolTip"));
            eventRepeatButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eventRepeatButtonActionPerformed(evt);
                }
            });

            eventStartHrChooser.setModel(new javax.swing.SpinnerDateModel());
            eventStartHrChooser.setToolTipText(domain.language.getString ("startHrToolTip"));
            eventStartHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventStartHrChooser, "h"));
            eventStartHrChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventStartHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventStartHrChooserStateChanged(evt);
                }
            });

            eventEndHrChooser.setToolTipText(domain.language.getString ("endHrToolTip"));
            eventEndHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventEndHrChooser, "h"));
            eventEndHrChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventEndHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventEndHrChooserStateChanged(evt);
                }
            });

            eventStartMinChooser.setToolTipText(domain.language.getString ("startMinToolTip"));
            eventStartMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventStartMinChooser, "mm"));
            eventStartMinChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventStartMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventStartMinChooserStateChanged(evt);
                }
            });

            eventStartMChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventStartMChooser, "a"));
            eventStartMChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventStartMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventStartMChooserStateChanged(evt);
                }
            });

            eventEndMinChooser.setToolTipText(domain.language.getString ("endMinToolTip"));
            eventEndMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventEndMinChooser, "mm"));
            eventEndMinChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventEndMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventEndMinChooserStateChanged(evt);
                }
            });

            eventEndMChooser.setEditor(new javax.swing.JSpinner.DateEditor(eventEndMChooser, "a"));
            eventEndMChooser.setFont(domain.utility.currentTheme.fontPlain12);
            eventEndMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent evt) {
                    eventEndMChooserStateChanged(evt);
                }
            });

            eventColon1.setFont(domain.utility.currentTheme.fontBold12);
            eventColon1.setText(":");

            eventColon2.setFont(domain.utility.currentTheme.fontBold12);
            eventColon2.setText(":");

            googleMapsButton.setBackground(domain.utility.currentTheme.colorButtonBackground);
            googleMapsButton.setFont(domain.utility.currentTheme.fontPlain12);
            googleMapsButton.setText(domain.language.getString ("googleMaps"));
            googleMapsButton.setToolTipText(domain.language.getString ("eventLocationButtonToolTip"));
            googleMapsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    googleMapsButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout eventContentPanelLayout = new javax.swing.GroupLayout(eventContentPanel);
            eventContentPanel.setLayout(eventContentPanelLayout);
            eventContentPanelLayout.setHorizontalGroup(
                eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(eventContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(eventsSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addGroup(eventContentPanelLayout.createSequentialGroup()
                            .addComponent(allDayEventCheckBox, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                            .addComponent(eventRepeatButton))
                        .addGroup(eventContentPanelLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(categoryComboBox, 0, 131, Short.MAX_VALUE))
                        .addGroup(eventContentPanelLayout.createSequentialGroup()
                            .addComponent(locationLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                            .addComponent(googleMapsButton))
                        .addComponent(descriptionLabel)
                        .addGroup(eventContentPanelLayout.createSequentialGroup()
                            .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(eventStartHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(eventEndHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(eventContentPanelLayout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eventColon2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eventEndMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eventEndMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, eventContentPanelLayout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(eventColon1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eventStartMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(eventStartMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(eventDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(eventNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(commentsScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(locationTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)
                        .addComponent(eventsSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE))
                    .addContainerGap())
            );
            eventContentPanelLayout.setVerticalGroup(
                eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(eventContentPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(eventNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(eventDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(eventStartHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eventColon1)
                        .addComponent(eventStartMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eventStartMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(eventEndHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eventColon2)
                        .addComponent(eventEndMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eventEndMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(allDayEventCheckBox)
                        .addComponent(eventRepeatButton))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(eventsSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGap(18, 18, 18)
                    .addGroup(eventContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(locationLabel)
                        .addComponent(googleMapsButton))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(eventsSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(descriptionLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(commentsScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addContainerGap())
            );

            rightPanel.add(eventContentPanel, "card3");

            add(rightPanel, java.awt.BorderLayout.EAST);

            statusPanel.setName("statusPanel"); // NOI18N
            statusPanel.setPreferredSize(new java.awt.Dimension(1414, 27));
            statusPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 2));

            progressBar.setFont(domain.utility.currentTheme.fontPlain10);
            progressBar.setIndeterminate(true);
            progressBar.setName("progressBar"); // NOI18N
            progressBar.setOpaque(false);
            progressBar.setPreferredSize(new java.awt.Dimension(150, 23));
            progressBar.setString(domain.language.getString ("loading") + "...");
            progressBar.setStringPainted(true);
            statusPanel.add(progressBar);

            add(statusPanel, java.awt.BorderLayout.SOUTH);
        }// </editor-fold>//GEN-END:initComponents

    private void addTermMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTermMenuItemActionPerformed
        termsAndCoursesDialog.goViewTermsAndCourses ();
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (0);
        domain.addTerm ();
    }//GEN-LAST:event_addTermMenuItemActionPerformed

    private void addCourseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseMenuItemActionPerformed
        termsAndCoursesDialog.goViewTermsAndCourses ();
        domain.termsAndCoursesOpening.push (true);
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
        domain.termsAndCoursesOpening.pop ();
        domain.addCourse ();
    }//GEN-LAST:event_addCourseMenuItemActionPerformed

    private void addAssignmentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentMenuItemActionPerformed
        domain.addAssignment ();
    }//GEN-LAST:event_addAssignmentMenuItemActionPerformed

    private void assignmentNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignmentNameTextFieldActionPerformed
        domain.setAssignmentName (domain.currentIndexFromVector);
        if (evt != null && dontReselectName.empty ())
        {
            assignmentNameTextField.requestFocus ();
            assignmentNameTextField.selectAll ();
        }
    }//GEN-LAST:event_assignmentNameTextFieldActionPerformed

    private void assignmentNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_assignmentNameTextFieldFocusLost
        assignmentNameTextFieldActionPerformed (null);
    }//GEN-LAST:event_assignmentNameTextFieldFocusLost

    protected void completedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completedCheckBoxActionPerformed
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            if (item.isAssignment ())
            {
                Assignment assignment = (Assignment) item;
                assignment.setIsDone (completedCheckBox.isSelected ());
                refreshAssignmentsRowAt (domain.currentIndexFromVector);
                assignment.refreshText ();
                assignment.getCourse ().markChanged ();
                filter (true);
                refreshBusyDays ();
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    if (selectedDayPanel != null)
                    {
                        selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
                    }
                    selectedDayPanel = days[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1];
                    selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
                }

                if (domain.utility.preferences.sortIndex == 0)
                {
                    scrollToItemOrToday (assignment);
                }
            }
        }

        if (completedCheckBox.isSelected () && !gradeLabel.isVisible ())
        {
            gradeLabel.setVisible (true);
            gradeTextField.setVisible (true);
            gradeTextField.requestFocus ();
        }
        else
        {
            if (!completedCheckBox.isSelected () && gradeLabel.isVisible ())
            {
                gradeLabel.setVisible (false);
                gradeTextField.setVisible (false);
                commentsTextArea.requestFocus ();
            }
        }
    }//GEN-LAST:event_completedCheckBoxActionPerformed

    private void gradeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeTextFieldActionPerformed
        domain.setAssignmentGrade (domain.currentIndexFromVector);
        if (evt != null)
        {
            gradeTextField.requestFocus ();
            gradeTextField.selectAll ();
        }
    }//GEN-LAST:event_gradeTextFieldActionPerformed

    private void gradeTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_gradeTextFieldFocusLost
        if (assignmentContentPanel.isVisible ())
        {
            gradeTextFieldActionPerformed (null);
        }
    }//GEN-LAST:event_gradeTextFieldFocusLost

    private void commentsTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentsTextAreaFocusLost
        if (assignmentContentPanel.isVisible ())
        {
            domain.setAssignmentComments (domain.currentIndexFromVector);
        }
    }//GEN-LAST:event_commentsTextAreaFocusLost

    private synchronized void commentsTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commentsTextAreaKeyPressed
        ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ().markChanged ();
    }//GEN-LAST:event_commentsTextAreaKeyPressed

    private synchronized void prioritySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_prioritySliderStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            assignment.setPriority (prioritySlider.getValue ());
            assignment.refreshText ();
            refreshAssignmentsRowAt (domain.currentIndexFromVector);
            assignment.getCourse ().markChanged ();
        }
    }//GEN-LAST:event_prioritySliderStateChanged

    private void detailsCourseComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailsCourseComboBoxItemStateChanged
        if (detailsCourseComboBox.getSelectedIndex () != -1 && domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            Course oldCourse = assignment.getCourse ();
            oldCourse.markChanged ();
            oldCourse.removeAssignment (assignment);
            assignment.setCourse (assignment.getCourse ().getTerm ().getCourse (detailsCourseComboBox.getSelectedIndex ()));
            assignment.getCourse ().addAssignment (assignment);
            assignment.refreshText ();
            refreshAssignmentsRowAt (domain.currentIndexFromVector);
            if ((assignment.getDueTime (0) + assignment.getDueTime (1) + assignment.getDueTime (2)).equals ((oldCourse.getStartTime (0) + oldCourse.getStartTime (1) + oldCourse.getStartTime (2))))
            {
                try
                {
                    dueHrChooser.setValue (Domain.HR_FORMAT.parse (assignment.getCourse ().getStartTime (0)));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                try
                {
                    dueMinChooser.setValue (Domain.MIN_FORMAT.parse (assignment.getCourse ().getStartTime (1)));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                try
                {
                    dueMChooser.setValue (Domain.M_FORMAT.parse (assignment.getCourse ().getStartTime (2)));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
            domain.utility.loadDetailsTextbookBox ();
            domain.utility.loadDetailsTypeBox ();
            assignment.getCourse ().markChanged ();
            if (getSelectedCourseIndex () != -1)
            {
                // setting the selection path calls filters and also saves
                termTree.setSelectionPath (new TreePath (assignment.getCourse ().getPath ()));
            }
            else
            {
                filter (true);
            }

            assignmentsTable.setSelectedRow (assignment.getUniqueID (), 6);

            if (domain.utility.preferences.sortIndex == 3)
            {
                scrollToItemOrToday ((ListItem) assignment);
            }
        }
    }//GEN-LAST:event_detailsCourseComboBoxItemStateChanged

    private synchronized void detailsTextbookComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailsTextbookComboBoxItemStateChanged
        if (detailsTextbookComboBox.getSelectedIndex () != -1 && detailsTextbookComboBox.getSelectedIndex () != 0
            && domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            assignment.setTextbook (assignment.getCourse ().getTextbook (detailsTextbookComboBox.getSelectedIndex () - 1));
            assignment.getCourse ().markChanged ();
        }
        else
        {
            if (domain.assignmentOrEventLoading.empty ())
            {
                ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).setTextbook (null);
                ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ().markChanged ();
            }
        }
    }//GEN-LAST:event_detailsTextbookComboBoxItemStateChanged

    private synchronized void detailsTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_detailsTypeComboBoxItemStateChanged
        if (detailsTypeComboBox.getSelectedIndex () != -1 && detailsTypeComboBox.getSelectedIndex () != 0
            && domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            assignment.setType (assignment.getCourse ().getType (detailsTypeComboBox.getSelectedIndex () - 1));
            refreshAssignmentsRowAt (domain.currentIndexFromVector);
            assignment.getCourse ().markChanged ();
            filter (true);

            if (domain.utility.preferences.sortIndex == 2)
            {
                scrollToItemOrToday ((ListItem) assignment);
            }
        }
        else
        {
            if (domain.assignmentOrEventLoading.empty ())
            {
                ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).setType (null);
                refreshAssignmentsRowAt (domain.currentIndexFromVector);
                ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ().markChanged ();
            }
        }
    }//GEN-LAST:event_detailsTypeComboBoxItemStateChanged

    private void textFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textFieldFocusGained
        ((JTextField) evt.getComponent ()).selectAll ();
    }//GEN-LAST:event_textFieldFocusGained

    private void commentsTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commentsTextAreaFocusGained
        ((JTextArea) evt.getComponent ()).selectAll ();
    }//GEN-LAST:event_commentsTextAreaFocusGained

    private void dueDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dueDateChooserPropertyChange
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
        {
            checkAssignmentOrEventChanges (domain.currentIndexFromVector);
            domain.assignmentOrEventLoading.push (true);
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            try
            {
                String dueDate = Domain.DATE_FORMAT.format (dueDateChooser.getDate ());
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    assignment.getParent ().remove (assignment);
                }
                assignment.setDueDate (dueDate);

                Calendar cal = Calendar.getInstance ();
                cal.setTime (dueDateChooser.getDate ());

                assignment.refreshText ();
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    try
                    {
                        daysAssignmentsAndEvents[Integer.parseInt (dueDate.split ("/")[1]) - 1].add (assignment);
                    }
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                    }
                }
                // adjust the Calendar View to the new month and year to display
                miniCalendar.setDate (Domain.DATE_FORMAT.parse (assignment.getDueDate ()));
                refreshAssignmentsRowAt (domain.currentIndexFromVector);
                assignment.getCourse ().markChanged ();
                filter (true);
                refreshBusyDays ();

                if (domain.utility.preferences.sortIndex == 4)
                {
                    scrollToItemOrToday ((ListItem) assignment);
                }
            }
            catch (NullPointerException ex)
            {
                try
                {
                    dueDateChooser.setDate (Domain.DATE_FORMAT.parse (assignment.getDueDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            domain.assignmentOrEventLoading.pop ();
        }
    }//GEN-LAST:event_dueDateChooserPropertyChange

    private void miniCalendarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_miniCalendarPropertyChange
        if ((evt.getPropertyName ().contains ("calendar") || evt.getPropertyName ().contains ("date")) && !initLoading && !quitting && domain.assignmentOrEventLoading.empty ())
        {
            String[] oldDateString;
            String[] newDateString;
            if (evt.getPropertyName ().contains ("calendar"))
            {
                oldDateString = Domain.DATE_FORMAT.format (((GregorianCalendar) evt.getOldValue ()).getTime ()).split ("/");
                newDateString = Domain.DATE_FORMAT.format (((GregorianCalendar) evt.getNewValue ()).getTime ()).split ("/");
            }
            else
            {
                oldDateString = Domain.DATE_FORMAT.format (((Date) evt.getOldValue ()).getTime ()).split ("/");
                newDateString = Domain.DATE_FORMAT.format (((Date) evt.getNewValue ()).getTime ()).split ("/");
            }
            String oldMonth = oldDateString[0];
            String newMonth = newDateString[0];
            String oldYear = oldDateString[2];
            String newYear = newDateString[2];

            if (middleTabbedPane.getSelectedIndex () == 0)
            {
                if (oldMonth.equals (newMonth) && oldYear.equals (newYear))
                {
                    String selectedDate = Domain.DATE_FORMAT.format (miniCalendar.getDate ());
                    for (int i = 0; i < domain.utility.assignmentsAndEvents.size (); ++i)
                    {
                        if (selectedDate.equals (domain.utility.assignmentsAndEvents.get (i).getDueDate ()))
                        {
                            assignmentsTable.setSelectedRow (i);
                        }
                    }
                }
            }
            else
            {
                if (!oldMonth.equals (newMonth) || !oldYear.equals (newYear))
                {
                    loadCalendarView (true);
                }
                String[] split = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                if (selectedDayPanel != null)
                {
                    selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
                }
                selectedDayPanel = days[Integer.parseInt (split[1]) - 1];
                selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
            }

            // if the new month is not within the current selected term, switch the selected term
            // so proper assignments will be shown
            try
            {
                if (getSelectedTermIndex () != -1)
                {
                    Date newDate;
                    if (evt.getPropertyName ().contains ("calendar"))
                    {
                        newDate = Domain.MONTH_YEAR_FORMAT.parse (Domain.MONTH_YEAR_FORMAT.format (((GregorianCalendar) evt.getNewValue ()).getTime ()));
                    }
                    else
                    {
                        newDate = Domain.MONTH_YEAR_FORMAT.parse (Domain.MONTH_YEAR_FORMAT.format (((Date) evt.getNewValue ()).getTime ()));
                    }
                    Term currentTerm = domain.utility.terms.get (getSelectedTermIndex ());
                    Date startDate = Domain.MONTH_YEAR_FORMAT.parse (currentTerm.getStartDate ().split ("/")[0] + "/" + currentTerm.getStartDate ().split ("/")[2]);
                    Date endDate = Domain.MONTH_YEAR_FORMAT.parse (currentTerm.getEndDate ().split ("/")[0] + "/" + currentTerm.getEndDate ().split ("/")[2]);
                    if (startDate.after (newDate) || endDate.before (newDate))
                    {
                        smartSelectCurrentTerm (newDate);
                    }
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        refreshBusyDays ();
    }//GEN-LAST:event_miniCalendarPropertyChange

    private void editTermEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTermEditMenuItemActionPerformed
        goEditTerm ();
    }//GEN-LAST:event_editTermEditMenuItemActionPerformed

    private void removeTermEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTermEditMenuItemActionPerformed
        goRemoveTerm ();
    }//GEN-LAST:event_removeTermEditMenuItemActionPerformed

    private void editCourseEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCourseEditMenuItemActionPerformed
        goEditCourse ();
    }//GEN-LAST:event_editCourseEditMenuItemActionPerformed

    private void removeCourseEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCourseEditMenuItemActionPerformed
        goRemoveCourse ();
    }//GEN-LAST:event_removeCourseEditMenuItemActionPerformed

    private void removeEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeEditMenuItemActionPerformed
        if (domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).isAssignment ())
        {
            goRemoveAssignment ();
        }
        else
        {
            goRemoveEvent (null);
        }
    }//GEN-LAST:event_removeEditMenuItemActionPerformed

	private synchronized void middleTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_middleTabbedPaneStateChanged
            if (!initLoading || forceViewLoad)
            {
                if (middleTabbedPane.getSelectedIndex () == 0)
                {
                    int index = assignmentsTable.getSelectedRow ();
                    domain.assignmentOrEventLoading.push (true);
                    domain.utility.loadAssignmentsTable (true);
                    assignmentsTable.setSelectedRow (index);
                    domain.assignmentOrEventLoading.pop ();
                }
                else
                {
                    loadCalendarView (true);
                }

                domain.utility.preferences.middleTabbedPaneIndex = middleTabbedPane.getSelectedIndex ();
                domain.needsPreferencesSave = true;
            }
	}//GEN-LAST:event_middleTabbedPaneStateChanged

	private void addAssignmentTermEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentTermEditMenuItemActionPerformed
            goAddAssignment ();
	}//GEN-LAST:event_addAssignmentTermEditMenuItemActionPerformed

    private void formAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_formAncestorAdded
        Thread load = new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                try
                {
                    initMyComponents ();

                    // start the worker thread and request a load
                    domain.workerThread.start ();
                    domain.utility.load ();

                    domain.utility.loadTermTree ();
                    domain.utility.loadAssignmentsTable (true);

                    findTermWithin ();

                    syncWithPreferences (false);
                    filter (true);

                    assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).setHeaderValue ("<html><b>" + assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).getHeaderValue () + "</b></html>");

                    expandTermTree (new TreeExpansionEvent (this, null));
                    refreshBusyDays ();
                    initButtons ();
                    scrollToItemOrToday (null);

                    contentPanel.setVisible (true);
                    loadingPanel.setVisible (false);

                    domain.setProgressState (progressBar, false, "", false, -1);

                    initLoading = false;
                    Theme tempCurrent = domain.utility.currentTheme;
                    settingsDialog.currentThemeComboBoxItemStateChanged ();
                    domain.utility.currentTheme = tempCurrent;
                    addButton.setEnabled (true);
                    viewGradesButton.setEnabled (true);
                    settingsButton.setEnabled (true);
                    termsAndCoursesButton.setEnabled (true);
                    domain.workerThread.setAllowSave (true);

                    termTree.invalidate ();
                    termTree.revalidate ();
                    termTree.repaint ();
                }
                catch (Exception ex)
                {
                    Domain.LOGGER.add (ex);
                }
                finally
                {
                    setCursor (Cursor.getDefaultCursor ());
                }
            }
        });
        load.start ();
    }//GEN-LAST:event_formAncestorAdded

    private void dayPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dayPanelMouseReleased
        checkAssignmentOrEventChanges (domain.currentIndexFromVector);
        checkRepeatEventChanges (domain.currentIndexFromVector);

        if (selectedDayPanel != evt.getSource () && getIndexFromDaysArray ((JPanel) evt.getSource ()) != -1)
        {
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = (JPanel) evt.getSource ();
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
            int dayIndex = getIndexFromDaysArray ((JPanel) evt.getSource ());
            Calendar cal = miniCalendar.getCalendar ();
            cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
            miniCalendar.setDate (cal.getTime ());
            refreshBusyDays ();
        }
        else if (selectedDayPanel != evt.getSource ())
        {
            String day = ((JLabel) ((JPanel) evt.getSource ()).getComponent (0)).getText ();
            int dayNum = Integer.parseInt (day);
            Calendar cal = miniCalendar.getCalendar ();
            if (dayNum > 7)
            {
                cal.add (Calendar.MONTH, -1);
            }
            else
            {
                cal.add (Calendar.MONTH, 1);
            }
            cal.set (Calendar.DAY_OF_MONTH, dayNum);
            miniCalendar.setDate (cal.getTime ());
            loadCalendarView (false);
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = days[dayNum - 1];
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        }

        if ((evt.getButton () == 2 || evt.getButton () == 3) && getIndexFromDaysArray ((JPanel) evt.getSource ()) != -1)
        {
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentEditMenuItem.setEnabled (false);
            }
            cloneEditMenuItem.setEnabled (false);
            removeEditMenuItem.setEnabled (false);
            assignmentsEditMenu.show (evt.getComponent (), evt.getPoint ().x, evt.getPoint ().y);
        }
    }//GEN-LAST:event_dayPanelMouseReleased

    private void scrollPaneMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollPaneMouseReleased
        checkAssignmentOrEventChanges (domain.currentIndexFromVector);
        checkRepeatEventChanges (domain.currentIndexFromVector);

        if (selectedDayPanel != (JPanel) ((JScrollPane) evt.getSource ()).getParent () && getIndexFromDaysArray ((JPanel) ((JScrollPane) evt.getSource ()).getParent ()) != -1)
        {
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = (JPanel) ((JScrollPane) evt.getSource ()).getParent ();
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
            int dayIndex = getIndexFromDaysArray ((JPanel) ((JScrollPane) evt.getSource ()).getParent ());
            Calendar cal = miniCalendar.getCalendar ();
            cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
            miniCalendar.setDate (cal.getTime ());
            refreshBusyDays ();
        }
        else if (selectedDayPanel != (JPanel) ((JScrollPane) evt.getSource ()).getParent ())
        {
            String day = ((JLabel) ((JPanel) ((JScrollPane) evt.getSource ()).getParent ()).getComponent (0)).getText ();
            int dayNum = Integer.parseInt (day);
            Calendar cal = miniCalendar.getCalendar ();
            if (dayNum > 7)
            {
                cal.add (Calendar.MONTH, -1);
            }
            else
            {
                cal.add (Calendar.MONTH, 1);
            }
            cal.set (Calendar.DAY_OF_MONTH, dayNum);
            miniCalendar.setDate (cal.getTime ());
            loadCalendarView (false);
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = days[dayNum - 1];
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        }

        if ((evt.getButton () == 2 || evt.getButton () == 3) && getIndexFromDaysArray ((JPanel) ((JScrollPane) evt.getSource ()).getParent ()) != -1)
        {
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentEditMenuItem.setEnabled (false);
            }
            cloneEditMenuItem.setEnabled (false);
            removeEditMenuItem.setEnabled (false);
            assignmentsEditMenu.show (evt.getComponent (), evt.getPoint ().x, evt.getPoint ().y);
        }
    }//GEN-LAST:event_scrollPaneMouseReleased

    private void assignmentsTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignmentsTableMouseReleased
        mouseDraggingInTable = false;

        if (evt.getButton () == 1
            && domain.currentIndexFromVector != -1
            && assignmentsTable.columnAtPoint (evt.getPoint ()) != 0
            && assignmentsTable.rowAtPoint (evt.getPoint ()) != -1)
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (assignmentsTable.getVectorIndexFromSelectedRow (assignmentsTable.rowAtPoint (evt.getPoint ())));
            if (dontReselectName.empty ())
            {
                if (item.isAssignment ())
                {
                    assignmentNameTextField.requestFocus ();
                }
                else
                {
                    eventNameTextField.requestFocus ();
                }
            }
        }
        else
        {
            if (evt.getButton () == 1
                && assignmentsTable.columnAtPoint (evt.getPoint ()) == 0
                && assignmentsTable.rowAtPoint (evt.getPoint ()) != -1)
            {
                assignmentsTable.setSelectedRow (assignmentsTable.rowAtPoint (evt.getPoint ()));
                assignmentsTableRowSelected (null);
                ListItem item = domain.utility.assignmentsAndEvents.get (assignmentsTable.getVectorIndexFromSelectedRow (assignmentsTable.rowAtPoint (evt.getPoint ())));
                if (item.isAssignment ())
                {
                    completedCheckBox.setSelected (!((Assignment) item).isDone ());
                    completedCheckBoxActionPerformed (null);
                }
            }
        }
        if (evt.getButton () == 2 || evt.getButton () == 3)
        {
            checkAssignmentOrEventChanges (domain.currentIndexFromVector);
            checkRepeatEventChanges (domain.currentIndexFromVector);

            assignmentsTable.setSelectedRow (assignmentsTable.rowAtPoint (evt.getPoint ()));
            showAssignmentAndEventEditMenu (evt);
        }
    }//GEN-LAST:event_assignmentsTableMouseReleased

    private void termTreeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_termTreeMouseReleased
        TreePath path = termTree.getPathForLocation ((int) Math.round (evt.getPoint ().x), (int) Math.round (evt.getPoint ().y));
        termTree.getSelectionModel ().setSelectionPath (path);
        if (evt.getButton () == 1 && evt.getClickCount () == 2)
        {
            if (getSelectedCourseIndexFrom (getSelectedTermIndex ()) != -1)
            {
                goEditCourse ();
            }
            else
            {
                if (getSelectedTermIndex () != -1)
                {
                    goEditTerm ();
                }
            }
        }
        else
        {
            if (evt.getButton () == 2 || evt.getButton () == 3)
            {
                showTermEditMenu (evt);
            }
        }
    }//GEN-LAST:event_termTreeMouseReleased

    private void addPopupMenuPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_addPopupMenuPopupMenuWillBecomeInvisible
        addButton.setSelected (false);
    }//GEN-LAST:event_addPopupMenuPopupMenuWillBecomeInvisible

    private void updatesCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatesCloseButtonActionPerformed
        updatesDialog.dispose ();
        setCursor (Cursor.getDefaultCursor ());
        requestFocus ();
    }//GEN-LAST:event_updatesCloseButtonActionPerformed

    private void editGradingScaleEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editGradingScaleEditMenuItemActionPerformed
        goEditTypes ();
    }//GEN-LAST:event_editGradingScaleEditMenuItemActionPerformed

    private void editTextbooksEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editTextbooksEditMenuItemActionPerformed
        goEditTextbooks ();
    }//GEN-LAST:event_editTextbooksEditMenuItemActionPerformed

    private void nextMonthButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMonthButtonMouseReleased
        Calendar cal = Calendar.getInstance ();
        cal.setTime (miniCalendar.getDate ());
        cal.add (Calendar.MONTH, 1);
        miniCalendar.setDate (cal.getTime ());
        loadCalendarView (false);
    }//GEN-LAST:event_nextMonthButtonMouseReleased

    private void prevMonthButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prevMonthButtonMouseReleased
        Calendar cal = Calendar.getInstance ();
        cal.setTime (miniCalendar.getDate ());
        cal.add (Calendar.MONTH, -1);
        miniCalendar.setDate (cal.getTime ());
        loadCalendarView (false);
    }//GEN-LAST:event_prevMonthButtonMouseReleased

    private void todayButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_todayButtonMouseReleased
        String[] todaySplit = Domain.DATE_FORMAT.format (domain.today).split ("/");
        miniCalendar.getYearChooser ().setYear (Integer.parseInt (todaySplit[2]));
        miniCalendar.getMonthChooser ().setMonth (Integer.parseInt (todaySplit[0]) - 1);
        miniCalendar.getDayChooser ().setDay (Integer.parseInt (todaySplit[1]));
        loadCalendarView (false);

        if (selectedDayPanel != null)
        {
            selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
        }
        selectedDayPanel = days[Integer.parseInt (Domain.DATE_FORMAT.format (domain.today).split ("/")[1]) - 1];
        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);

        scrollToItemOrToday (null);
    }//GEN-LAST:event_todayButtonMouseReleased

    private void addEventMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEventMenuItemActionPerformed
        domain.addEvent (null);
    }//GEN-LAST:event_addEventMenuItemActionPerformed

    private void todayButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_todayButtonMouseEntered
        todayButton.setForeground (Color.GRAY);
    }//GEN-LAST:event_todayButtonMouseEntered

    private void prevMonthButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prevMonthButtonMouseEntered
        prevMonthButton.setForeground (Color.GRAY);
    }//GEN-LAST:event_prevMonthButtonMouseEntered

    private void nextMonthButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMonthButtonMouseEntered
        nextMonthButton.setForeground (Color.GRAY);
    }//GEN-LAST:event_nextMonthButtonMouseEntered

    private void nextMonthButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextMonthButtonMouseExited
        nextMonthButton.setForeground (Color.BLACK);
    }//GEN-LAST:event_nextMonthButtonMouseExited

    private void todayButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_todayButtonMouseExited
        todayButton.setForeground (Color.BLACK);
    }//GEN-LAST:event_todayButtonMouseExited

    private void prevMonthButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prevMonthButtonMouseExited
        prevMonthButton.setForeground (Color.BLACK);
    }//GEN-LAST:event_prevMonthButtonMouseExited

    private void eventNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventNameTextFieldActionPerformed
        domain.setEventName (domain.currentIndexFromVector);
        if (evt != null && dontReselectName.empty ())
        {
            eventNameTextField.requestFocus ();
            eventNameTextField.selectAll ();
        }
    }//GEN-LAST:event_eventNameTextFieldActionPerformed

    private void eventNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eventNameTextFieldFocusLost
        eventNameTextFieldActionPerformed (null);
    }//GEN-LAST:event_eventNameTextFieldFocusLost

    private void eventDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_eventDateChooserPropertyChange
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
        {
            eventChanges.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            Event localTempEvent = null;
            try
            {
                localTempEvent = domain.createCloneObject (event, domain.utility, Domain.DATE_FORMAT.parse (event.getDueDate ()), false);
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            boolean wasRepeating = false;
            // remove the attachment to repetition
            if (event.getRepeating ().getID () != -1)
            {
                wasRepeating = true;
                event.getRepeating ().setID (-1);
                domain.utility.repeatingEvents.remove (event);
            }

            try
            {
                String dueDate = Domain.DATE_FORMAT.format (eventDateChooser.getDate ());
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    event.getParent ().remove (event);
                }
                event.getEventYear ().markChanged ();
                event.getEventYear ().removeEvent (event);
                event.setDate (dueDate, domain.utility);
                Calendar cal = Calendar.getInstance ();
                cal.setTime (eventDateChooser.getDate ());
                event.getEventYear ().addEvent (event);
                event.refreshText ();
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    try
                    {
                        daysAssignmentsAndEvents[Integer.parseInt (dueDate.split ("/")[1]) - 1].add (event);
                    }
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                    }
                }
                miniCalendar.setDate (eventDateChooser.getDate ());
                refreshAssignmentsRowAt (domain.currentIndexFromVector);
                event.getEventYear ().markChanged ();
                filter (true);
                refreshBusyDays ();

                if (domain.utility.preferences.sortIndex == 4)
                {
                    scrollToItemOrToday ((ListItem) event);
                }
            }
            catch (NullPointerException ex)
            {
                try
                {
                    domain.assignmentOrEventLoading.push (true);
                    eventDateChooser.setDate (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                    domain.assignmentOrEventLoading.pop ();
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }

            OPTION_PANE.setValue (null);

            if (wasRepeating)
            {
                ViewPanel.OPTION_PANE.setOptions (YES_NO_CHOICES);
                ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("editRepeatingEventText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("editRepeatingEvent"));
                optionDialog.setVisible (true);
            }

            if (OPTION_PANE.getValue () == null || (OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION))
            {
                try
                {
                    domain.assignmentOrEventLoading.push (true);
                    eventDateChooser.setDate (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                    domain.assignmentOrEventLoading.pop ();
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
            else
            {
                try
                {
                    domain.assignmentOrEventLoading.push (true);
                    eventDateChooser.setDate (Domain.DATE_FORMAT.parse (localTempEvent.getDueDate ()));
                    domain.assignmentOrEventLoading.pop ();

                    try
                    {
                        event.getRepeating ().setID (localTempEvent.getRepeating ().getID ());
                        domain.utility.repeatingEvents.add (event);

                        String dueDate = localTempEvent.getDueDate ();
                        if (middleTabbedPane.getSelectedIndex () == 1)
                        {
                            event.getParent ().remove (event);
                        }
                        event.getEventYear ().markChanged ();
                        event.getEventYear ().removeEvent (event);
                        event.setDate (dueDate, domain.utility);
                        Calendar cal = Calendar.getInstance ();
                        cal.setTime (eventDateChooser.getDate ());
                        event.getEventYear ().addEvent (event);
                        event.refreshText ();
                        if (middleTabbedPane.getSelectedIndex () == 1)
                        {
                            try
                            {
                                daysAssignmentsAndEvents[Integer.parseInt (dueDate.split ("/")[1]) - 1].add (event);
                            }
                            catch (ArrayIndexOutOfBoundsException ex)
                            {
                            }
                        }
                        miniCalendar.setDate (eventDateChooser.getDate ());
                        refreshAssignmentsRowAt (domain.currentIndexFromVector);
                        event.getEventYear ().markChanged ();
                        refreshBusyDays ();

                        if (domain.utility.preferences.sortIndex == 4)
                        {
                            scrollToItemOrToday ((ListItem) event);
                        }
                    }
                    catch (NullPointerException ex)
                    {
                        try
                        {
                            domain.assignmentOrEventLoading.push (true);
                            eventDateChooser.setDate (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                            domain.assignmentOrEventLoading.pop ();
                        }
                        catch (ParseException innerEx)
                        {
                            Domain.LOGGER.add (ex);
                        }
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
    }//GEN-LAST:event_eventDateChooserPropertyChange

    private void descriptionTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextAreaFocusLost
        if (eventContentPanel.isVisible ())
        {
            domain.setEventDescription (domain.currentIndexFromVector);
        }
    }//GEN-LAST:event_descriptionTextAreaFocusLost

    private synchronized void descriptionTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descriptionTextAreaKeyPressed
        ((Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getEventYear ().markChanged ();
    }//GEN-LAST:event_descriptionTextAreaKeyPressed

    private void filter1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filter1ButtonActionPerformed
        if (!initLoading)
        {
            assignmentsTable.setSelectedRow (-1);
            assignmentsTableRowSelected (null);
            if (bothFilterRadioButton.isSelected ())
            {
                domain.utility.preferences.filter1Index = 0;
            }
            else
            {
                if (assignmentsFilterRadioButton.isSelected ())
                {
                    domain.utility.preferences.filter1Index = 1;
                }
                else
                {
                    domain.utility.preferences.filter1Index = 2;
                }
            }
            domain.needsPreferencesSave = true;
            filter (true);
        }
    }//GEN-LAST:event_filter1ButtonActionPerformed

    private void filter2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filter2ButtonActionPerformed
        if (!initLoading)
        {
            assignmentsTable.setSelectedRow (-1);
            assignmentsTableRowSelected (null);
            if (allFilterRadioButton.isSelected ())
            {
                domain.utility.preferences.filter2Index = 0;
            }
            else
            {
                if (doneFilterRadioButton.isSelected ())
                {
                    domain.utility.preferences.filter2Index = 1;
                }
                else
                {
                    if (notDoneFilterRadioButton.isSelected ())
                    {
                        domain.utility.preferences.filter2Index = 2;
                    }
                    else
                    {
                        domain.utility.preferences.filter2Index = 3;
                    }
                }
            }
            domain.needsPreferencesSave = true;
            filter (true);
        }
    }//GEN-LAST:event_filter2ButtonActionPerformed

    private void allDayEventCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allDayEventCheckBoxActionPerformed
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            domain.assignmentOrEventLoading.push (true);

            ListItem item = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            if (!item.isAssignment ())
            {
                Event event = (Event) item;

                setEventIsAllDay (event);
            }

            domain.assignmentOrEventLoading.pop ();
        }

        if (allDayEventCheckBox.isSelected ())
        {
            eventStartHrChooser.setEnabled (false);
            eventStartMinChooser.setEnabled (false);
            eventStartMChooser.setEnabled (false);
            eventEndHrChooser.setEnabled (false);
            eventEndMinChooser.setEnabled (false);
            eventEndMChooser.setEnabled (false);
            eventColon1.setEnabled (false);
            eventColon2.setEnabled (false);
        }
        else
        {
            eventStartHrChooser.setEnabled (true);
            eventStartMinChooser.setEnabled (true);
            eventStartMChooser.setEnabled (true);
            eventEndHrChooser.setEnabled (true);
            eventEndMinChooser.setEnabled (true);
            eventEndMChooser.setEnabled (true);
            eventColon1.setEnabled (true);
            eventColon2.setEnabled (true);
        }
    }//GEN-LAST:event_allDayEventCheckBoxActionPerformed

    private synchronized void textFieldKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_textFieldKeyPressed
    {//GEN-HEADEREND:event_textFieldKeyPressed
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == assignmentNameTextField)
            {
                Assignment assn = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                assignmentNameTextField.setText (assn.getItemName ());
                assn.getCourse ().markChanged ();
            }
            else if (evt.getSource () == gradeTextField)
            {
                Assignment assn = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                gradeTextField.setText (assn.getItemName ());
                assn.getCourse ().markChanged ();
            }
            else if (evt.getSource () == eventNameTextField)
            {
                Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                eventNameTextField.setText (event.getItemName ());
                event.getEventYear ().markChanged ();
            }
            else if (evt.getSource () == locationTextField)
            {
                Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                locationTextField.setText (event.getItemName ());
                event.getEventYear ().markChanged ();
            }
        }
        else
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            if (item.isAssignment ())
            {
                ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ().markChanged ();
            }
            else
            {
                ((Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getEventYear ().markChanged ();
            }
        }
    }//GEN-LAST:event_textFieldKeyPressed

    private void locationTextFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_locationTextFieldActionPerformed
    {//GEN-HEADEREND:event_locationTextFieldActionPerformed
        domain.setEventLocation (domain.currentIndexFromVector);
        if (evt != null)
        {
            gradeTextField.requestFocus ();
            gradeTextField.selectAll ();
        }
    }//GEN-LAST:event_locationTextFieldActionPerformed

    private void locationTextFieldFocusLost(java.awt.event.FocusEvent evt)//GEN-FIRST:event_locationTextFieldFocusLost
    {//GEN-HEADEREND:event_locationTextFieldFocusLost
        if (eventContentPanel.isVisible ())
        {
            locationTextFieldActionPerformed (null);
        }
    }//GEN-LAST:event_locationTextFieldFocusLost

    private synchronized void categoryComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_categoryComboBoxItemStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !initLoading)
        {
            eventChanges.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            setEventCategory (event);
            event.getEventYear ().markChanged ();
        }
    }//GEN-LAST:event_categoryComboBoxItemStateChanged

    private void cloneEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cloneEditMenuItemActionPerformed
        if (domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).isAssignment ())
        {
            goCloneAssignment ();
        }
        else
        {
            goCloneEvent ();
        }
    }//GEN-LAST:event_cloneEditMenuItemActionPerformed

    private void eventRepeatButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventRepeatButtonActionPerformed
        showRepeatEventDialog ();
    }//GEN-LAST:event_eventRepeatButtonActionPerformed

    private void repeatEventDoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatEventDoneButtonActionPerformed
        closeRepeatEventDialog ();
    }//GEN-LAST:event_repeatEventDoneButtonActionPerformed

    private void repeatEventRepeatsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatEventRepeatsComboBoxActionPerformed
        repeatEventChanges = true;

        int index = repeatEventRepeatsComboBox.getSelectedIndex ();
        int count = repeatEventRepeatsEveryComboBox.getSelectedIndex () + 1;

        if (index == 1)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("days"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("day"));
            }
        }

        // no repetition
        if (index == 0)
        {
            everyDescriptionLabel.setText ("");
            repeatEventRepeatsEveryComboBox.setEnabled (false);
            reSunCheckBox.setEnabled (false);
            reMonCheckBox.setEnabled (false);
            reTueCheckBox.setEnabled (false);
            reWedCheckBox.setEnabled (false);
            reThuCheckBox.setEnabled (false);
            reFriCheckBox.setEnabled (false);
            reSatCheckBox.setEnabled (false);
            repeatEventEndDateChooser.setEnabled (false);
        }
        // repeat weekly
        else if (index == 2)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("weeks"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("week"));
            }
            repeatEventRepeatsEveryComboBox.setEnabled (true);
            reSunCheckBox.setEnabled (true);
            reMonCheckBox.setEnabled (true);
            reTueCheckBox.setEnabled (true);
            reWedCheckBox.setEnabled (true);
            reThuCheckBox.setEnabled (true);
            reFriCheckBox.setEnabled (true);
            reSatCheckBox.setEnabled (true);
            repeatEventEndDateChooser.setEnabled (true);

            // grab the current day and set it
            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            Calendar cal = Calendar.getInstance ();
            try
            {
                cal.setTime (Domain.DATE_FORMAT.parse (event.getDueDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            switch (cal.get (Calendar.DAY_OF_WEEK))
            {
                case 1:
                {
                    reSunCheckBox.setSelected (true);
                    break;
                }
                case 2:
                {
                    reMonCheckBox.setSelected (true);
                    break;
                }
                case 3:
                {
                    reTueCheckBox.setSelected (true);
                    break;
                }
                case 4:
                {
                    reWedCheckBox.setSelected (true);
                    break;
                }
                case 5:
                {
                    reThuCheckBox.setSelected (true);
                    break;
                }
                case 6:
                {
                    reFriCheckBox.setSelected (true);
                    break;
                }
                case 7:
                {
                    reSatCheckBox.setSelected (true);
                    break;
                }
            }
        }
        // repeat monthly or yearly
        else
        {
            if (index == 3)
            {
                if (count > 1)
                {
                    everyDescriptionLabel.setText (domain.language.getString ("months"));
                }
                else
                {
                    everyDescriptionLabel.setText (domain.language.getString ("month"));
                }
            }
            else if (index == 4)
            {
                if (count > 1)
                {
                    everyDescriptionLabel.setText (domain.language.getString ("years"));
                }
                else
                {
                    everyDescriptionLabel.setText (domain.language.getString ("year"));
                }
            }
            repeatEventRepeatsEveryComboBox.setEnabled (true);
            reSunCheckBox.setEnabled (false);
            reMonCheckBox.setEnabled (false);
            reTueCheckBox.setEnabled (false);
            reWedCheckBox.setEnabled (false);
            reThuCheckBox.setEnabled (false);
            reFriCheckBox.setEnabled (false);
            reSatCheckBox.setEnabled (false);
            repeatEventEndDateChooser.setEnabled (true);
        }
    }//GEN-LAST:event_repeatEventRepeatsComboBoxActionPerformed

    private void repeatEventEndDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_repeatEventEndDateChooserPropertyChange
        if (repeatEventEndDateChooser.getDate () != null)
        {
            if (!evt.getPropertyName ().equals ("ancestor"))
            {
                repeatEventChanges = true;
            }

            if (repeatEventEndDateChooser.getDate ().before (eventDateChooser.getDate ()))
            {
                repeatEventEndDateChooser.setDate (eventDateChooser.getDate ());
            }
            repeatingEndDate = repeatEventEndDateChooser.getDate ();
        }
    }//GEN-LAST:event_repeatEventEndDateChooserPropertyChange

    private void assignmentsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignmentsTableMousePressed
        mouseDraggingInTable = true;
    }//GEN-LAST:event_assignmentsTableMousePressed

    private synchronized void dueHrChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dueHrChooserStateChanged
    {//GEN-HEADEREND:event_dueHrChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            String time = Domain.HR_FORMAT.format (dueHrChooser.getValue ());
            assignment.setDueTime (0, time);
            assignment.getCourse ().markChanged ();
        }
    }//GEN-LAST:event_dueHrChooserStateChanged

    private synchronized void dueMinChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dueMinChooserStateChanged
    {//GEN-HEADEREND:event_dueMinChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            String time = Domain.MIN_FORMAT.format (dueMinChooser.getValue ());
            assignment.setDueTime (1, time);
            assignment.getCourse ().markChanged ();
        }
    }//GEN-LAST:event_dueMinChooserStateChanged

    private synchronized void dueMChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_dueMChooserStateChanged
    {//GEN-HEADEREND:event_dueMChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
        {
            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
            String dueTime = Domain.M_FORMAT.format (dueMChooser.getValue ());
            assignment.setDueTime (2, dueTime);
            assignment.getCourse ().markChanged ();
        }
    }//GEN-LAST:event_dueMChooserStateChanged

    private void eventStartHrChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventStartHrChooserStateChanged
    {//GEN-HEADEREND:event_eventStartHrChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventEndHrChooser.setValue (eventStartHrChooser.getValue ());
                    eventEndMinChooser.setValue (eventStartMinChooser.getValue ());
                    eventEndMChooser.setValue (eventStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventStartHr (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventStartHrChooserStateChanged

    private void eventEndHrChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventEndHrChooserStateChanged
    {//GEN-HEADEREND:event_eventEndHrChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventStartHrChooser.setValue (eventEndHrChooser.getValue ());
                    eventStartMinChooser.setValue (eventEndMinChooser.getValue ());
                    eventStartMChooser.setValue (eventEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventEndHr (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventEndHrChooserStateChanged

    private void eventStartMinChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventStartMinChooserStateChanged
    {//GEN-HEADEREND:event_eventStartMinChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventEndHrChooser.setValue (eventStartHrChooser.getValue ());
                    eventEndMinChooser.setValue (eventStartMinChooser.getValue ());
                    eventEndMChooser.setValue (eventStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventStartMin (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventStartMinChooserStateChanged

    private void eventStartMChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventStartMChooserStateChanged
    {//GEN-HEADEREND:event_eventStartMChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventEndHrChooser.setValue (eventStartHrChooser.getValue ());
                    eventEndMinChooser.setValue (eventStartMinChooser.getValue ());
                    eventEndMChooser.setValue (eventStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventStartM (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventStartMChooserStateChanged

    private void eventEndMinChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventEndMinChooserStateChanged
    {//GEN-HEADEREND:event_eventEndMinChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventStartHrChooser.setValue (eventEndHrChooser.getValue ());
                    eventStartMinChooser.setValue (eventEndMinChooser.getValue ());
                    eventStartMChooser.setValue (eventEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventEndMin (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventEndMinChooserStateChanged

    private void eventEndMChooserStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_eventEndMChooserStateChanged
    {//GEN-HEADEREND:event_eventEndMChooserStateChanged
        if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty ())
        {
            eventChanges.push (true);

            dontReselectName.push (true);

            Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);

            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    eventStartHrChooser.setValue (eventEndHrChooser.getValue ());
                    eventStartMinChooser.setValue (eventEndMinChooser.getValue ());
                    eventStartMChooser.setValue (eventEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            setEventEndM (event);

            dontReselectName.pop ();
        }
    }//GEN-LAST:event_eventEndMChooserStateChanged

    private void googleMapsButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_googleMapsButtonActionPerformed
    {//GEN-HEADEREND:event_googleMapsButtonActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("http://maps.google.com/maps?daddr=" + locationTextField.getText ().replaceAll (" ", "+").replaceAll ("\\.", "%2E").replaceAll ("\\\"", "%22")));
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
            catch (URISyntaxException ex)
            {
                Domain.LOGGER.add (ex);
            }

        }
        else
        {
            OPTION_PANE.setOptions (OK_CHOICE);
            OPTION_PANE.setMessage (domain.language.getString ("browserCouldntLaunch"));
            OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
    }//GEN-LAST:event_googleMapsButtonActionPerformed

    private void repeatEventRepeatsEveryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatEventRepeatsEveryComboBoxActionPerformed
        repeatEventChanges = true;

        int index = repeatEventRepeatsComboBox.getSelectedIndex ();
        int count = repeatEventRepeatsEveryComboBox.getSelectedIndex () + 1;
        if (index == 1)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("days"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("day"));
            }
        }
        if (index == 2)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("weeks"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("week"));
            }
        }
        else if (index == 3)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("months"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("month"));
            }
        }
        else if (index == 4)
        {
            if (count > 1)
            {
                everyDescriptionLabel.setText (domain.language.getString ("years"));
            }
            else
            {
                everyDescriptionLabel.setText (domain.language.getString ("year"));
            }
        }
    }//GEN-LAST:event_repeatEventRepeatsEveryComboBoxActionPerformed

    private void repeatDayCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatDayCheckBoxActionPerformed
        repeatEventChanges = true;
    }//GEN-LAST:event_repeatDayCheckBoxActionPerformed

    private void askPopupMenuPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_askPopupMenuPopupMenuWillBecomeInvisible
        askInstructorButton.setSelected (false);
    }//GEN-LAST:event_askPopupMenuPopupMenuWillBecomeInvisible

    private void editInstructorsEditMenuItemeditGradingScaleEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editInstructorsEditMenuItemeditGradingScaleEditMenuItemActionPerformed
        goEditInstructors ();
    }//GEN-LAST:event_editInstructorsEditMenuItemeditGradingScaleEditMenuItemActionPerformed

    private void labWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed
        visitLabWebsite ();
    }//GEN-LAST:event_labWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed

    private void courseWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed
        visitCourseWebsite ();
    }//GEN-LAST:event_courseWebsiteMenuItemeditGradingScaleEditMenuItemActionPerformed

    private void contactAdvisorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactAdvisorButtonActionPerformed
        settingsDialog.goViewSettings ();
        settingsDialog.settingsTabbedPane.setSelectedIndex (1);
        settingsDialog.advisorNameTextField.requestFocus ();
        settingsDialog.advisorNameTextField.selectAll ();
    }//GEN-LAST:event_contactAdvisorButtonActionPerformed

    private void editUserDetailsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUserDetailsButtonActionPerformed
        settingsDialog.goViewSettings ();
        settingsDialog.settingsTabbedPane.setSelectedIndex (1);
        settingsDialog.studentNameTextField.requestFocus ();
        settingsDialog.studentNameTextField.selectAll ();
    }//GEN-LAST:event_editUserDetailsButtonActionPerformed

    private void addEventEditMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEventEditMenuItemActionPerformed
        domain.addEvent (null);
    }//GEN-LAST:event_addEventEditMenuItemActionPerformed

    ////////////////////////////////////////////////////////////////////////////
    // Custom Implementation
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Load the resource bundle for the given domain.language.
     *
     * @param language The language to load the resource for.
     */
    protected void loadLanguageResource(String languageString)
    {
        try
        {
            if (languageString.equals ("English"))
            {
                domain.language = ResourceBundle.getBundle ("adl.go.resource.languages.bundle_en");
                dueDateChooser.getDateEditor ().setEnabled (true);
                eventDateChooser.getDateEditor ().setEnabled (true);
                repeatEventEndDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.termStartDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.termEndDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.courseStartDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.courseEndDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.labStartDateChooser.getDateEditor ().setEnabled (true);
                termsAndCoursesDialog.labEndDateChooser.getDateEditor ().setEnabled (true);
            }
            else
            {
                dueDateChooser.getDateEditor ().setEnabled (false);
                eventDateChooser.getDateEditor ().setEnabled (false);
                repeatEventEndDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.termStartDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.termEndDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.courseStartDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.courseEndDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.labStartDateChooser.getDateEditor ().setEnabled (false);
                termsAndCoursesDialog.labEndDateChooser.getDateEditor ().setEnabled (false);
                if (languageString.equals ("Español"))
                {
                    domain.language = ResourceBundle.getBundle ("adl.go.resource.languages.bundle_es");
                }
            }
        }
        catch (NullPointerException ex)
        {
        }
    }

    /**
     * Initialize and write to backup file.
     *
     * @param file The file to write to.
     */
    private void backupThread(final File file)
    {
        new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                initLoading = true;
                domain.utility.writeBackupFile (file);

                updatesDialog.dispose ();
                updatesCloseButton.setVisible (true);
                updatesDialog.setTitle (domain.language.getString ("checkForUpdates"));
                initLoading = false;
                requestFocus ();
            }
        }).start ();
    }

    /**
     * Close the open windows.
     */
    protected void closeOpenWindows()
    {
        // close any open windows
        if (aboutDialog.isVisible ())
        {
            aboutDialog.dispose ();
        }
        if (repeatEventDialog.isVisible ())
        {
            repeatEventDialog.dispose ();
        }
        if (gettingStartedDialog.isVisible ())
        {
            gettingStartedDialog.dispose ();
        }
        if (heliumDialog.isVisible())
        {
            heliumDialog.dispose();
        }
        if (gradesDialog.isVisible ())
        {
            gradesDialog.dispose ();
        }
        if (printDialog.isVisible ())
        {
            printDialog.dispose ();
        }
        if (importFromBackupDialog.isVisible ())
        {
            importFromBackupDialog.dispose ();
        }
        if (settingsDialog.isVisible ())
        {
            settingsDialog.closeSettingsDialog ();
        }
        if (termsAndCoursesDialog.isVisible ())
        {
            termsAndCoursesDialog.closeTermsAndCoursesDialog ();
        }
    }

    /**
     * Allow the user to backup all preferences and course information into a
     * single, accessible backup file.
     */
    protected void backup()
    {
        closeOpenWindows ();

        fileChooser.setDialogType (JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText (domain.language.getString ("backup"));
        fileChooser.setApproveButtonToolTipText (domain.language.getString ("backupGetOrganized"));
        fileChooser.setDialogTitle (domain.language.getString ("backupGetOrganized"));
        File selectedFile = new File (lastGoodFile);
        fileChooser.setSelectedFile (selectedFile);
        int response = fileChooser.showSaveDialog (this);
        while (response == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = fileChooser.getSelectedFile ();
            try
            {
                lastGoodFile = selectedFile.getCanonicalPath ();
            }
            catch (IOException ex)
            {
            }
            // ensure an extension is on the file
            if (selectedFile.getName ().indexOf (".") == -1)
            {
                selectedFile = new File (fileChooser.getSelectedFile ().toString () + ".gbak");
            }
            // ensure the file is a valid backup file
            if (!selectedFile.toString ().endsWith (".gbak"))
            {
                OPTION_PANE.setOptions (OK_CHOICE);
                OPTION_PANE.setMessage (domain.language.getString ("mustBeValidExtension") + " (.gbak).");
                OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("invalidExtension"));
                optionDialog.setVisible (true);
                fileChooser.setSelectedFile (new File (fileChooser.getSelectedFile ().toString ().substring (0, fileChooser.getSelectedFile ().toString ().lastIndexOf (".")) + ".gbak"));
                response = fileChooser.showSaveDialog (this);
                continue;
            }
            if (selectedFile.isFile ())
            {
                OPTION_PANE.setOptions (YES_NO_CANCEL_CHOICES);
                OPTION_PANE.setMessage (domain.language.getString ("filenameAlreadyExists"));
                OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("overwriteFile"));
                optionDialog.setVisible (true);
                if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
                {
                    domain.setProgressState (updatesProgressBar, true, domain.language.getString ("backingUpGetOrganized") + " ...", true, -1);
                    updatesCloseButton.setVisible (false);
                    updatesDialog.setTitle (domain.language.getString ("backingUp"));
                    updatesDialog.pack ();
                    updatesDialog.setLocationRelativeTo (this);
                    updatesDialog.setVisible (true);

                    backupThread (selectedFile);

                    break;
                }
                else if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.NO_OPTION)
                {
                    fileChooser.setSelectedFile (selectedFile);
                    response = fileChooser.showSaveDialog (this);
                    continue;
                }
                else
                {
                    break;
                }
            }
            else
            {
                domain.setProgressState (updatesProgressBar, true, domain.language.getString ("backingUpGetOrganized") + " ...", true, -1);
                updatesCloseButton.setVisible (false);
                updatesDialog.setTitle (domain.language.getString ("backingUp"));
                updatesDialog.pack ();
                updatesDialog.setLocationRelativeTo (this);
                updatesDialog.setVisible (true);

                backupThread (selectedFile);

                break;
            }
        }
    }

    /**
     * Refind domain.today's date.
     */
    public void refindToday()
    {
        miniCalendar.getDayChooser ().drawDays ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            loadCalendarView (true);
        }
    }

    /**
     * Import all data from a given backup file.
     */
    protected void restoreFromBackup()
    {
        closeOpenWindows ();

        fileChooser.setDialogType (JFileChooser.OPEN_DIALOG);
        fileChooser.setApproveButtonText (domain.language.getString ("restore"));
        fileChooser.setApproveButtonToolTipText (domain.language.getString ("restoreToBackupToolTip"));
        fileChooser.setDialogTitle (domain.language.getString ("restoreToBackup"));
        File selectedFile = new File (lastGoodFile);
        fileChooser.setSelectedFile (selectedFile);
        int response = fileChooser.showOpenDialog (this);
        while (response == JFileChooser.APPROVE_OPTION)
        {
            final File permFile = fileChooser.getSelectedFile ();
            if (!permFile.isFile () || !selectedFile.toString ().endsWith (".gbak"))
            {
                OPTION_PANE.setOptions (OK_CHOICE);
                OPTION_PANE.setMessage (domain.language.getString ("mustBeValidExtension") + " (.gbak).");
                OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("invalidExtension"));
                optionDialog.setVisible (true);
                fileChooser.setSelectedFile (new File (fileChooser.getSelectedFile ().toString ().substring (0, fileChooser.getSelectedFile ().toString ().lastIndexOf (".")) + ".gbak"));
                response = fileChooser.showOpenDialog (this);
                continue;
            }
            try
            {
                lastGoodFile = permFile.getCanonicalPath ();
            }
            catch (IOException ex)
            {
            }
            OPTION_PANE.setOptions (REPLACE_CANCEL_CHOICES);
            OPTION_PANE.setMessage (domain.language.getString ("permanentlyEraseCurrentData"));
            OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("overwriteExistingData"));
            optionDialog.setVisible (true);
            if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                loadingPanel.setVisible (true);
                contentPanel.setVisible (false);

                setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
                domain.setProgressState (progressBar, true, domain.language.getString ("restoringGetOrganized"), true, -1);
                loadingLabel.setText (domain.language.getString ("restoringGetOrganized") + " ...");

                miniCalendar.setDate (domain.today);

                final ViewPanel viewPanel = this;
                new Thread (new Runnable ()
                {
                    @Override
                    public void run()
                    {
                        initLoading = true;
                        ignoreTableSelection = true;

                        assignmentsTable.setSelectedRow (-1);
                        assignmentsTableRowSelected (null);
                        termsAndCoursesDialog.settingsTermsTable.setSelectedRow (-1);
                        termsAndCoursesDialog.settingsTermsTableRowSelected (null);
                        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (-1);
                        termsAndCoursesDialog.settingsCoursesTableRowSelected (null);
                        domain.currentCourseIndex = -1;
                        domain.currentTermIndex = -1;
                        domain.currentTextbookIndex = -1;
                        domain.currentTypeIndex = -1;
                        domain.currentInstructorIndex = -1;
                        eventChanges.clear ();
                        repeatEventChanges = false;

                        settingsDialog.settingsTabbedPane.setSelectedIndex (0);
                        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (0);
                        domain.workerThread.setAllowSave (false);

                        boolean success = false;
                        boolean fatal = false;
                        try
                        {
                            success = domain.utility.restoreFromBackup (permFile);
                        }
                        catch (Exception ex)
                        {
                            success = false;
                            fatal = true;
                        }

                        domain.setProgressState (progressBar, true, domain.language.getString ("loading") + "...", true, -1);
                        loadingLabel.setText (domain.language.getString ("loading") + "...");
                        requestFocus ();

                        if (!success && !fatal)
                        {
                            setCursor (Cursor.getDefaultCursor ());

                            contentPanel.setVisible (true);
                            loadingPanel.setVisible (false);

                            OPTION_PANE.setOptions (OK_CHOICE);
                            OPTION_PANE.setMessage (domain.language.getString ("invalidRestoreFile"));
                            OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                            JDialog innerOptionDialog = OPTION_PANE.createDialog (viewPanel, domain.language.getString ("noChangesMade"));
                            innerOptionDialog.setVisible (true);

                            initLoading = false;
                            domain.workerThread.setAllowSave (true);
                        }
                        else if (success && !fatal)
                        {
                            assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).setHeaderValue (assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).getHeaderValue ().toString ().replaceAll ("\\<html\\>|\\<b\\>|\\</html\\>|\\</b\\>", ""));
                            termTree.getSelectionModel ().setSelectionPath (null);
                            domain.utility.loadTermTree ();
                            domain.utility.loadAssignmentsTable (true);
                            findTermWithin ();

                            syncWithPreferences (true);
                            filter (false);

                            assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).setHeaderValue ("<html><b>" + assignmentsTable.getColumnModel ().getColumn (assignmentsTableModel.getColumnSorting ()).getHeaderValue () + "</b></html>");

                            expandTermTree (new TreeExpansionEvent (this, null));
                            refreshBusyDays ();
                            initButtons ();
                            scrollToItemOrToday (null);

                            if (!domain.utility.preferences.autoUpdate)
                            {
                                domain.setProgressState (progressBar, false, "", false, -1);
                            }

                            setCursor (Cursor.getDefaultCursor ());

                            contentPanel.setVisible (true);
                            loadingPanel.setVisible (false);

                            initLoading = false;
                            ignoreTableSelection = false;
                            for (int i = 0; i < domain.utility.courses.size (); ++i)
                            {
                                domain.utility.courses.get (i).markChanged ();
                            }
                            for (int i = 0; i < domain.utility.eventYears.size (); ++i)
                            {
                                domain.utility.eventYears.get (i).markChanged ();
                            }
                            domain.needsCoursesAndTermsSave = true;
                            domain.needsPreferencesSave = true;
                            domain.needsSettingsSaveBool = true;

                            domain.utility.currentTheme.apply ();
                            domain.workerThread.setAllowSave (true);

                            termTree.invalidate ();
                            termTree.revalidate ();
                            termTree.repaint ();
                        }
                        else
                        {
                            setCursor (Cursor.getDefaultCursor ());

                            contentPanel.setVisible (true);
                            loadingPanel.setVisible (false);

                            initLoading = false;
                            ignoreTableSelection = false;
                            domain.setProgressState (updatesProgressBar, false, "", false, -1);

                            domain.workerThread.setAllowSave (true);

                            OPTION_PANE.setOptions (OK_CHOICE);
                            OPTION_PANE.setMessage (domain.language.getString ("aFatalErrorHasOccurred"));
                            OPTION_PANE.setMessageType (JOptionPane.ERROR_MESSAGE);
                            JDialog innerOptionDialog = OPTION_PANE.createDialog (viewPanel, domain.language.getString ("fatalError"));
                            innerOptionDialog.setVisible (true);

                            quit (true);
                        }
                    }
                }).start ();
            }
            break;
        }
    }

    /**
     * Set the given event to all day or not, depending on state of checkbox.
     *
     * @param event The event to set as all day.
     */
    private void setEventIsAllDay(Event event)
    {
        eventChanges.push (true);

        event.setIsAllDay (allDayEventCheckBox.isSelected ());
        refreshAssignmentsRowAt (domain.currentIndexFromVector);
        event.refreshText ();
        event.getEventYear ().markChanged ();
        filter (true);
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = days[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1];
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        }
    }

    /**
     * Set the category of the given event.
     *
     * @param event The event to set the category for.
     */
    private void setEventCategory(Event event)
    {
        event.getCategory ().removeEvent (event);
        event.setCategory (domain.utility.preferences.categories.get (categoryComboBox.getSelectedIndex ()));
        event.getCategory ().addEvent (event);
        refreshAssignmentsRowAt (domain.currentIndexFromVector);
        event.refreshText ();
        if (domain.utility.preferences.sortIndex == 3)
        {
            scrollToItemOrToday (event);
        }
    }

    /**
     * Set the start time of the given event.
     *
     * @param event The event to set the start time for.
     */
    private synchronized void setEventStartHr(Event event)
    {
        event.setStartTime (0, Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Set the start time of the given event.
     *
     * @param event The event to set the start time for.
     */
    private synchronized void setEventStartMin(Event event)
    {
        event.setStartTime (1, Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Set the start time of the given event.
     *
     * @param event The event to set the start time for.
     */
    private synchronized void setEventStartM(Event event)
    {
        event.setStartTime (2, Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Set the end time of the given event.
     *
     * @param event The event to set the end time for.
     */
    private synchronized void setEventEndHr(Event event)
    {
        event.setEndTime (0, Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Set the end time of the given event.
     *
     * @param event The event to set the end time for.
     */
    private synchronized void setEventEndMin(Event event)
    {
        event.setEndTime (1, Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Set the end time of the given event.
     *
     * @param event The event to set the end time for.
     */
    private synchronized void setEventEndM(Event event)
    {
        event.setEndTime (2, Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
        refreshAssignmentsRowAt (domain.currentIndexFromVector);

        event.getEventYear ().markChanged ();
        if (middleTabbedPane.getSelectedIndex () == 1)
        {
            refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
    }

    /**
     * Select the term in the term tree that the given date is contained within.
     *
     * @param date The date that is to be matched within a term.
     */
    private void smartSelectCurrentTerm(Date date)
    {
        try
        {
            for (int i = 0; i < domain.utility.terms.size (); ++i)
            {
                Term term = domain.utility.terms.get (i);
                if ((Domain.MONTH_YEAR_FORMAT.parse (term.getStartDate ().split ("/")[0] + "/" + term.getStartDate ().split ("/")[2])).compareTo (date) <= 0
                    && (Domain.MONTH_YEAR_FORMAT.parse (term.getEndDate ().split ("/")[0] + "/" + term.getEndDate ().split ("/")[2])).compareTo (date) >= 0)
                {
                    termTree.setSelectionPath (new TreePath (term.getPath ()));
                    break;
                }
            }
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Checks if the current date is within any of the state to end dates of all
     * terms and sets that term as selected if it is.
     */
    public void findTermWithin()
    {
        for (int i = 0; i < domain.utility.terms.size (); ++i)
        {
            Term term = domain.utility.terms.get (i);
            try
            {
                Calendar cal1 = Calendar.getInstance ();
                Calendar cal2 = Calendar.getInstance ();
                cal1.setTime (Domain.DATE_FORMAT.parse (term.getStartDate ()));
                cal1.add (Calendar.DAY_OF_YEAR, -1);
                cal2.setTime (Domain.DATE_FORMAT.parse (term.getEndDate ()));
                cal2.add (Calendar.DAY_OF_YEAR, 1);
                if (domain.today.after (cal1.getTime ())
                    && domain.today.before (cal2.getTime ()))
                {
                    TreePath path = new TreePath (term.getPath ());
                    termTree.getSelectionModel ().setSelectionPath (path);
                    break;
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        if (termTree.getSelectionModel ().getSelectionPath () == null && domain.utility.terms.size () > 0)
        {
            termTree.getSelectionModel ().setSelectionPath (new TreePath (domain.utility.terms.get (0).getPath ()));
        }
        termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
    }

    /**
     * Performs a close operation on the repeating event dialog, checking for
     * unsaved changes first.
     */
    public void closeRepeatEventDialog()
    {
        if (repeatEventDialog.isVisible ())
        {
            repeatEventDialog.dispose ();
            requestFocus ();

            if (domain.currentIndexFromVector != -1 && domain.assignmentOrEventLoading.empty () && !quitting)
            {
                Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                String repeatingString = "";

                if (repeatEventRepeatsComboBox.getSelectedIndex () != 0)
                {
                    boolean alreadyRepeating = true;
                    String oldRepeating = event.getRepeating ().toString ();
                    String dueDate = event.getDueDate ();
                    // assign a unique ID to the repeating event if it has not already been given one
                    if (event.getRepeating ().getID () == -1)
                    {
                        alreadyRepeating = false;
                        oldRepeating = null;
                        event.getRepeating ().setID (System.currentTimeMillis ());
                    }
                    else
                    {
                        dueDate = event.getRepeating ().getStartDate ();
                    }
                    repeatingString = repeatEventRepeatsComboBox.getSelectedIndex () + "-"
                                      + repeatEventRepeatsEveryComboBox.getSelectedIndex () + "-"
                                      + reSunCheckBox.isSelected () + "-"
                                      + reMonCheckBox.isSelected () + "-"
                                      + reTueCheckBox.isSelected () + "-"
                                      + reWedCheckBox.isSelected () + "-"
                                      + reThuCheckBox.isSelected () + "-"
                                      + reFriCheckBox.isSelected () + "-"
                                      + reSatCheckBox.isSelected () + "-"
                                      + dueDate + "-"
                                      + Domain.DATE_FORMAT.format (repeatingEndDate);

                    event.setRepeating (repeatingString);
                    if (!alreadyRepeating)
                    {
                        domain.utility.repeatingEvents.add (event);
                        createRepeatingInstances (event);
                    }
                    else
                    {
                        if (repeatEventChanges)
                        {
                            updateRepeatingInstaces (event, oldRepeating);
                        }
                    }

                    eventChanges.clear ();
                }
                else if (event.getRepeating ().getID () != -1)
                {
                    ViewPanel.OPTION_PANE.setOptions (YES_NO_CHOICES);
                    ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("removeRepeatingEventsText"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("removeRepeatingEvents"));
                    optionDialog.setVisible (true);

                    if (OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
                    {
                        destroyRepeatingInstances (event);

                        event.getRepeating ().setID (-1);
                        domain.utility.repeatingEvents.remove (event);
                        event.getEventYear ().markChanged ();
                    }

                    eventChanges.clear ();
                }
                event.getEventYear ().markChanged ();
            }

            repeatingEndDate = null;
        }
    }

    /**
     * Clones the given event with its repetition attached to it.
     *
     * @param toClone The event to clone repetitively.
     * @param inMonth The month which is currently shown in Calendar View.
     * @param date The date to set for the newly created event (since it's
     * cloned in repetition, the date is the only part that changes).
     */
    private void cloneEventWithRepeat(Event toClone, boolean inMonth, Date date)
    {
        domain.assignmentOrEventLoading.push (true);

        Event event = domain.createCloneObject (toClone, domain.utility, date, true);

        if (domain.utility.preferences.filter1Index != 1)
        {
            assignmentsTableModel.addRow (event.getRowObject ());
        }
        domain.utility.assignmentsAndEvents.add (event);
        domain.utility.repeatingEvents.add (event);

        if (inMonth)
        {
            if (middleTabbedPane.getSelectedIndex () == 1)
            {
                if (domain.utility.preferences.filter1Index != 1)
                {
                    daysAssignmentsAndEvents[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1].add (event);
                    shownEvents.add (event);
                }

                event.getLabel ().addMouseListener (new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased(MouseEvent evt)
                    {
                        eventMouseReleased (evt);
                    }
                });
                DRAG_SOURCE.createDefaultDragGestureRecognizer (event.getLabel (), DnDConstants.ACTION_MOVE, DND_LISTENER);
                event.refreshText ();

                refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
            }
            refreshBusyDays ();
        }

        domain.assignmentOrEventLoading.pop ();
    }

    /**
     * Updates all repeating instances of the currently selected event.
     *
     * @param event The original event in the series of repetition.
     * @param oldRepeating If only updating following, this will be attached to
     * preceding repeating instances
     */
    private void updateRepeatingInstaces(Event event, String oldRepeating)
    {
        OPTION_PANE.setOptions (UPDATE_REPEATING_CHOICES);
        OPTION_PANE.setMessage (domain.language.getString ("modifiedRepetitionText"));
        OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
        JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("modifiedRepetitionText"));
        optionDialog.setVisible (true);

        if (OPTION_PANE.getValue () != null)
        {
            if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == ALL_IN_SERIES_OPTION)
            {
                goRemoveEvent (event);

                event.setDate (event.getRepeating ().getStartDate (), domain.utility);
                event.getEventYear ().markChanged ();
                domain.addEvent (event);
                domain.utility.repeatingEvents.add (event);

                createRepeatingInstances (event);
            }
            else if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == ALL_FOLLOWING_OPTION)
            {
                try
                {
                    goRemoveEvent (event);

                    String preDate = event.getDueDate ();
                    String postDate = preDate;
                    String postEndDate = event.getRepeating ().getEndDate ();

                    Date newDate = Domain.DATE_FORMAT.parse (preDate);
                    Calendar newCalendar = Calendar.getInstance ();
                    newCalendar.setTime (newDate);
                    newCalendar.add (Calendar.DATE, -1);
                    newDate = newCalendar.getTime ();
                    preDate = Domain.DATE_FORMAT.format (newDate);

                    Event preEvent = event;
                    Event postEvent = domain.createCloneObject (preEvent, domain.utility, Domain.DATE_FORMAT.parse (preEvent.getDueDate ()), true);
                    if (oldRepeating != null)
                    {
                        preEvent.setRepeating (oldRepeating);
                    }
                    preEvent.setDate (event.getRepeating ().getStartDate (), domain.utility);
                    long uniqueID = System.currentTimeMillis ();
                    while (domain.utility.getByID (uniqueID) != null)
                    {
                        uniqueID = (long) (uniqueID * Math.random ());
                    }
                    preEvent.getRepeating ().setID (uniqueID);
                    preEvent.getRepeating ().setEndDate (preDate);
                    preEvent.setDate (preEvent.getRepeating ().getStartDate (), domain.utility);
                    preEvent.getEventYear ().markChanged ();

                    postEvent.setDate (postDate, domain.utility);
                    postEvent.getRepeating ().setStartDate (postDate);
                    postEvent.getRepeating ().setEndDate (postEndDate);
                    postEvent.getEventYear ().markChanged ();

                    domain.addEvent (preEvent);
                    domain.utility.repeatingEvents.add (preEvent);
                    createRepeatingInstances (preEvent);

                    domain.addEvent (postEvent);
                    domain.utility.repeatingEvents.add (postEvent);
                    createRepeatingInstances (postEvent);
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
            else
            {
                if (tempEvent != null)
                {
                    event.setItemName (tempEvent.getItemName ());
                    event.setCategory (tempEvent.getCategory ());
                    event.setDate (tempEvent.getDueDate (), domain.utility);
                    event.setDescription (tempEvent.getDescription ());
                    event.setStartTime (0, tempEvent.getStartTime (0));
                    event.setStartTime (1, tempEvent.getStartTime (1));
                    event.setStartTime (2, tempEvent.getStartTime (2));
                    event.setEndTime (0, tempEvent.getEndTime (0));
                    event.setEndTime (1, tempEvent.getEndTime (1));
                    event.setEndTime (2, tempEvent.getEndTime (2));
                    event.setIsAllDay (tempEvent.isAllDay ());
                    event.setEventLocation (tempEvent.getEventLocation ());
                    event.setRepeating (tempEvent.getRepeating ().toString ());
                    event.getRepeating ().setID (tempEvent.getRepeating ().getID ());
                    event.getEventYear ().markChanged ();

                    event.refreshRowObject ();
                    event.refreshText ();

                    eventChanges.pop ();
                    repeatEventChanges = false;
                }
            }
        }
        else
        {
            if (tempEvent != null)
            {
                event.setItemName (tempEvent.getItemName ());
                event.setCategory (tempEvent.getCategory ());
                event.setDate (tempEvent.getDueDate (), domain.utility);
                event.setDescription (tempEvent.getDescription ());
                event.setStartTime (0, tempEvent.getStartTime (0));
                event.setStartTime (1, tempEvent.getStartTime (1));
                event.setStartTime (2, tempEvent.getStartTime (2));
                event.setEndTime (0, tempEvent.getEndTime (0));
                event.setEndTime (1, tempEvent.getEndTime (1));
                event.setEndTime (2, tempEvent.getEndTime (2));
                event.setIsAllDay (tempEvent.isAllDay ());
                event.setEventLocation (tempEvent.getEventLocation ());
                event.setRepeating (tempEvent.getRepeating ().toString ());
                event.getRepeating ().setID (tempEvent.getRepeating ().getID ());
                event.getEventYear ().markChanged ();

                event.refreshRowObject ();
                event.refreshText ();

                eventChanges.pop ();
                repeatEventChanges = false;
            }
        }
    }

    /**
     * Create separate instances for each event in a repeating series.
     *
     * @param event The event to create repeating instances of.
     */
    private void createRepeatingInstances(Event tempEvent)
    {
        final Event event = tempEvent;
        final Repeating repeat = event.getRepeating ();

        setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));

        Thread thread = new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                Calendar cal = Calendar.getInstance ();
                try
                {
                    int monthDifCount = 0;
                    int yearDifCount = 0;
                    Date startDate = Domain.DATE_FORMAT.parse (repeat.getStartDate ());
                    cal.setTime (startDate);
                    cal.add (Calendar.DAY_OF_WEEK, 1);
                    int curMonth = cal.get (Calendar.MONTH);
                    int curYear = cal.get (Calendar.YEAR);
                    Date endDate = Domain.DATE_FORMAT.parse (repeat.getEndDate ());

                    while (cal.getTime ().compareTo (endDate) <= 0)
                    {
                        if (cal.get (Calendar.MONTH) != curMonth)
                        {
                            ++monthDifCount;
                            curMonth = cal.get (Calendar.MONTH);
                        }
                        if (cal.get (Calendar.YEAR) != curYear)
                        {
                            ++yearDifCount;
                            curYear = cal.get (Calendar.YEAR);
                        }

                        // repeating daily
                        if (event.getRepeating ().getRepeatsIndex () == 1)
                        {
                            long dayDiff = (cal.getTime ().getTime () - startDate.getTime ()) / (1000 * 60 * 60 * 24);
                            if (dayDiff % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                            {
                                String[] firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                                String[] secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                                cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                            }
                        }
                        // repeating weekly
                        else if (event.getRepeating ().getRepeatsIndex () == 2)
                        {
                            int dayOfWeek = cal.get (Calendar.DAY_OF_WEEK);
                            Repeating repeat = event.getRepeating ();
                            switch (dayOfWeek)
                            {
                                case 1:
                                {
                                    if (repeat.getSunday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 2:
                                {
                                    if (repeat.getMonday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 3:
                                {
                                    if (repeat.getTuesday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 4:
                                {
                                    if (repeat.getWednesday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 5:
                                {
                                    if (repeat.getThursday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 6:
                                {
                                    if (repeat.getFriday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                                case 7:
                                {
                                    if (repeat.getSaturday ())
                                    {
                                        repeatCheckAndAdd (event, startDate, cal);
                                    }
                                    break;
                                }
                            }
                        }
                        // repeating monthly
                        else if (event.getRepeating ().getRepeatsIndex () == 3)
                        {
                            String[] firstSplit = Domain.DATE_FORMAT.format (startDate).split ("/");
                            String[] secondSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                            if (firstSplit[1].equals (secondSplit[1])
                                && monthDifCount % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                            {
                                firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                                secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                                cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                            }
                        }
                        // repeating annually
                        else if (event.getRepeating ().getRepeatsIndex () == 4)
                        {
                            String[] firstSplit = Domain.DATE_FORMAT.format (startDate).split ("/");
                            String[] secondSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                            if (firstSplit[1].equals (secondSplit[1])
                                && yearDifCount % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                            {
                                firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                                secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                                cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                            }
                        }

                        cal.add (Calendar.DAY_OF_WEEK, 1);
                    }

                    event.getEventYear ().markChanged ();
                    filter (true);
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        });
        thread.start ();
        try
        {
            thread.join ();
        }
        catch (InterruptedException ex)
        {
            Domain.LOGGER.add (ex);
        }

        setCursor (Cursor.getDefaultCursor ());
    }

    /**
     * Check that the given event falls on a day that the current repetition
     * desires, then add it.
     *
     * @param event The event to be checked.
     * @param startDate The start date of the repetiton.
     * @param cal The repetition's calendar reference.
     */
    public void repeatCheckAndAdd(Event event, Date startDate, Calendar cal)
    {
        long dayDiff = (cal.getTime ().getTime () - startDate.getTime ()) / (1000 * 60 * 60 * 24);
        switch (event.getRepeating ().getRepeatsIndex ())
        {
            // weekly repeating
            case 2:
            {
                if ((dayDiff / 7) % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                {
                    String[] firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                    String[] secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                    cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                }
                break;
            }
            // monthly repeating
            case 3:
            {
                if ((dayDiff / 30) % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                {
                    String[] firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                    String[] secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                    cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                }
                break;
            }
            // yearly repeating
            case 4:
            {
                if ((dayDiff / 365) % (event.getRepeating ().getRepeatsEveryIndex () + 1) == 0)
                {
                    String[] firstSplit = Domain.DATE_FORMAT.format (cal.getTime ()).split ("/");
                    String[] secondSplit = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                    cloneEventWithRepeat (event, firstSplit[1].equals (secondSplit[1]) && firstSplit[2].equals (secondSplit[2]), cal.getTime ());
                }
                break;
            }
        }
    }

    /**
     * Destroy all instances of a repeating event (except the given one).
     *
     * @param event The event to destroy repeating instances of.
     */
    private void destroyRepeatingInstances(final Event event)
    {
        setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));

        Thread thread = new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                assignmentsTable.removingSelectionInterval = true;

                for (int i = domain.utility.repeatingEvents.size () - 1; i >= 0; --i)
                {
                    Event repEvent = domain.utility.repeatingEvents.get (i);
                    if (repEvent != event
                        && repEvent.getRepeating ().getID () == event.getRepeating ().getID ())
                    {
                        if (middleTabbedPane.getSelectedIndex () == 1)
                        {
                            JPanel parent = (JPanel) repEvent.getParent ();
                            if (parent != null)
                            {
                                parent.remove (repEvent);
                                parent.invalidate ();
                            }
                            shownEvents.remove (repEvent);
                        }
                        domain.utility.assignmentsAndEvents.remove (repEvent);
                        domain.utility.repeatingEvents.remove (repEvent);
                        repEvent.getCategory ().removeEvent (repEvent);
                        repEvent.getEventYear ().removeEvent (repEvent);
                        repEvent.getEventYear ().markChanged ();

                        assignmentsTableModel.removeRow (domain.utility.getAssignmentOrEventIndexByID (repEvent.getUniqueID ()));
                    }
                }

                assignmentsTable.removingSelectionInterval = false;
            }
        });
        thread.start ();
        try
        {
            thread.join ();
        }
        catch (InterruptedException ex)
        {
            Domain.LOGGER.add (ex);
        }

        setCursor (Cursor.getDefaultCursor ());
    }

    /**
     * Display the repeat event dialog for the currently selected event.
     */
    protected void showRepeatEventDialog()
    {
        Event event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
        try
        {
            String nextMonth = Integer.parseInt (event.getDueDate ().substring (0, 2)) + 1 + "";
            if (nextMonth.length () == 1)
            {
                nextMonth = "0" + nextMonth;
            }
            String endDate = nextMonth + event.getDueDate ().substring (2, event.getDueDate ().length ());

            if (event.getRepeating ().getID () != -1)
            {
                String[] parsed = event.getRepeating ().toString ().split ("-");

                endDate = parsed[10];

                repeatEventRepeatsComboBox.setSelectedIndex (Integer.parseInt (parsed[0]));
                repeatEventRepeatsEveryComboBox.setSelectedIndex (Integer.parseInt (parsed[1]));
                reSunCheckBox.setSelected (Boolean.valueOf (parsed[2]));
                reMonCheckBox.setSelected (Boolean.valueOf (parsed[3]));
                reTueCheckBox.setSelected (Boolean.valueOf (parsed[4]));
                reWedCheckBox.setSelected (Boolean.valueOf (parsed[5]));
                reThuCheckBox.setSelected (Boolean.valueOf (parsed[6]));
                reFriCheckBox.setSelected (Boolean.valueOf (parsed[7]));
                reSatCheckBox.setSelected (Boolean.valueOf (parsed[8]));
            }
            else
            {
                repeatEventRepeatsComboBox.setSelectedIndex (0);
                repeatEventRepeatsEveryComboBox.setSelectedIndex (0);
                reSunCheckBox.setSelected (false);
                reMonCheckBox.setSelected (false);
                reTueCheckBox.setSelected (false);
                reWedCheckBox.setSelected (false);
                reThuCheckBox.setSelected (false);
                reFriCheckBox.setSelected (false);
                reSatCheckBox.setSelected (false);

                // grab the current day and set it
                Calendar cal = Calendar.getInstance ();
                try
                {
                    cal.setTime (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
                switch (cal.get (Calendar.DAY_OF_WEEK))
                {
                    case 1:
                    {
                        reSunCheckBox.setSelected (true);
                        break;
                    }
                    case 2:
                    {
                        reMonCheckBox.setSelected (true);
                        break;
                    }
                    case 3:
                    {
                        reTueCheckBox.setSelected (true);
                        break;
                    }
                    case 4:
                    {
                        reWedCheckBox.setSelected (true);
                        break;
                    }
                    case 5:
                    {
                        reThuCheckBox.setSelected (true);
                        break;
                    }
                    case 6:
                    {
                        reFriCheckBox.setSelected (true);
                        break;
                    }
                    case 7:
                    {
                        reSatCheckBox.setSelected (true);
                        break;
                    }
                }
            }

            repeatEventEndDateChooser.setDate (Domain.DATE_FORMAT.parse (endDate));

            repeatEventChanges = false;
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }

        repeatEventDialog.pack ();
        repeatEventDialog.setSize (230, repeatEventDialog.getHeight ());
        repeatEventDialog.setLocation (eventRepeatButton.getLocationOnScreen ().x - (repeatEventDialog.getWidth () - eventRepeatButton.getWidth ()),
                                       eventRepeatButton.getLocationOnScreen ().y + 20);
        repeatEventDialog.setVisible (true);
    }

    /**
     * Swaps the first and second element in the given list.
     *
     * @param model The list to swap the elements in.
     * @param index 0 for terms, 1 for courses, 2 for types, 3 for textbooks, 4
     * for instructors
     * @param first The first element location.
     * @param second The second element location.
     */
    public void swap(ExtendedSettingsTableModel model, int index, int first, int second)
    {
        Object temp1 = model.getValueAt (first, 0);
        Object temp2 = model.getValueAt (first, 1);
        Object temp3 = model.getValueAt (first, 2);
        model.setValueAt (model.getValueAt (second, 0), first, 0);
        model.setValueAt (model.getValueAt (second, 1), first, 1);
        try
        {
            model.setValueAt (model.getValueAt (second, 2), first, 2);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        model.setValueAt (temp1, second, 0);
        model.setValueAt (temp2, second, 1);
        try
        {
            model.setValueAt (temp3, second, 2);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }

        switch (index)
        {
            case 0:
            {
                root.insert ((Term) root.getChildAt (second), first);
                domain.refreshTermTree ();
                Term tempType = domain.utility.terms.get (first);
                domain.utility.terms.set (first, domain.utility.terms.get (second));
                domain.utility.terms.set (second, tempType);
                break;
            }
            case 1:
            {
                Course tempType = domain.utility.courses.get (first);
                domain.utility.courses.set (first, domain.utility.courses.get (second));
                domain.utility.courses.set (second, tempType);
                break;
            }
            case 2:
            {
                AssignmentType tempType = domain.utility.types.get (first);
                domain.utility.types.set (first, domain.utility.types.get (second));
                domain.utility.types.set (second, tempType);
                break;
            }
            case 3:
            {
                Textbook tempType = domain.utility.textbooks.get (first);
                domain.utility.textbooks.set (first, domain.utility.textbooks.get (second));
                domain.utility.textbooks.set (second, tempType);
                break;
            }
            case 4:
            {
                Instructor tempType = domain.utility.instructors.get (first);
                domain.utility.instructors.set (first, domain.utility.instructors.get (second));
                domain.utility.instructors.set (second, tempType);
                break;
            }
        }
    }

    /**
     * Retrieve a reference to the domain class.
     */
    public Domain getDomain()
    {
        return domain;
    }

    /**
     * Refreshes the days marked as busy on the mini calendar.
     */
    public void refreshBusyDays()
    {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (miniCalendar.getDate ());
        int year = cal.get (Calendar.YEAR);
        int month = cal.get (Calendar.MONTH) + 1;

        int dueCount = 0;

        miniCalendar.getDayChooser ().revertAllDayForegrounds ();

        for (int i = 0; i < domain.utility.assignmentsAndEvents.size (); ++i)
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (i);
            try
            {
                boolean isDone = true;
                Date dueDate = Domain.DATE_FORMAT.parse (item.getDueDate ());

                if (item.isAssignment () && !((Assignment) item).isDone () && dueDate.compareTo (domain.today) == 0)
                {
                    ++dueCount;
                }

                if (item.isAssignment ())
                {
                    isDone = ((Assignment) item).isDone ();
                }
                else
                {
                    Event event = (Event) item;
                    dueDate = Domain.DATE_AND_TIME_FORMAT.parse (event.getDueDate () + " " + event.getStartTime (0) + ":" + event.getStartTime (1) + " " + event.getStartTime (2));
                }

                if ((dueDate.compareTo (domain.today) < 0 && item.isAssignment () && !isDone) || !isDone || (dueDate.compareTo (domain.today) >= 0 && !item.isAssignment ()))
                {
                    String[] split = item.getDueDate ().split ("/");
                    int thisYear = Integer.parseInt (split[2]);
                    int day = Integer.parseInt (split[1]);
                    int thisMonth = Integer.parseInt (split[0]);
                    if (thisYear - year == 0 && thisMonth - month == 0)
                    {
                        miniCalendar.getDayChooser ().setDayForeground (day, domain.utility.currentTheme.colorBusyDayInMonth);
                    }
                    else
                    {
                        Calendar calBef = Calendar.getInstance ();
                        calBef.setTime (miniCalendar.getDate ());
                        calBef.add (Calendar.MONTH, -1);
                        calBef.set (Calendar.DAY_OF_MONTH, lastMonthFirst);
                        calBef.add (Calendar.DAY_OF_MONTH, -1);
                        calBef.set (Calendar.HOUR, 12);
                        calBef.set (Calendar.MINUTE, 00);
                        calBef.set (Calendar.AM_PM, Calendar.PM);
                        calBef.add (Calendar.MINUTE, -1);

                        Calendar monthDate = Calendar.getInstance ();
                        monthDate.setTime (miniCalendar.getDate ());
                        monthDate.set (Calendar.DAY_OF_MONTH, 1);
                        monthDate.add (Calendar.DAY_OF_MONTH, -1);
                        monthDate.set (Calendar.HOUR, 12);
                        monthDate.set (Calendar.MINUTE, 00);
                        monthDate.set (Calendar.AM_PM, Calendar.PM);

                        Calendar calAft = null;
                        try
                        {
                            buildDaysOutsideMonth ();
                            int finalDay = Integer.parseInt (((JLabel) ((JPanel) ((JScrollPane) ((JViewport) daysOutsideMonth[daysOutsideMonth.length - 1].getParent ()).getParent ()).getParent ()).getComponent (0)).getText ());
                            if (finalDay < 7)
                            {
                                calAft = Calendar.getInstance ();
                                calAft.setTime (miniCalendar.getDate ());
                                calAft.add (Calendar.MONTH, 1);
                                calAft.set (Calendar.DAY_OF_MONTH, finalDay);
                                calAft.set (Calendar.HOUR, 12);
                                calAft.set (Calendar.MINUTE, 00);
                                calAft.set (Calendar.AM_PM, Calendar.PM);
                            }
                        }
                        catch (NullPointerException ex)
                        {
                        }
                        if (dueDate.after (calBef.getTime ()) && dueDate.before (monthDate.getTime ()))
                        {
                            miniCalendar.getDayChooser ().setDayForeground (lastMonthFirst - Integer.parseInt (item.getDueDate ().split ("/")[1]) - 1, domain.utility.currentTheme.colorBusyDayOutsideMonth);
                        }
                        else if (calAft != null)
                        {
                            monthDate.set (Calendar.DAY_OF_MONTH, monthDate.getActualMaximum (Calendar.DATE));
                            if (dueDate.after (monthDate.getTime ()) && dueDate.before (calAft.getTime ()))
                            {
                                miniCalendar.getDayChooser ().setDayForeground (days.length + Integer.parseInt (item.getDueDate ().split ("/")[1]), domain.utility.currentTheme.colorBusyDayOutsideMonth);
                            }
                        }
                    }
                }
                else
                {
                    if (dueDate.compareTo (domain.today) >= 0 && isDone && dayIsDone (dueDate))
                    {
                        String[] split = item.getDueDate ().split ("/");
                        int thisYear = Integer.parseInt (split[2]);
                        int day = Integer.parseInt (split[1]);
                        int thisMonth = Integer.parseInt (split[0]);
                        if (thisYear - year == 0 && thisMonth - month == 0)
                        {
                            miniCalendar.getDayChooser ().setDayForeground (day, domain.utility.currentTheme.colorDoneDayInMonth);
                        }
                        else
                        {
                            Calendar calBef = Calendar.getInstance ();
                            calBef.setTime (miniCalendar.getDate ());
                            calBef.add (Calendar.MONTH, -1);
                            calBef.set (Calendar.DAY_OF_MONTH, lastMonthFirst);
                            calBef.add (Calendar.DAY_OF_MONTH, -1);
                            calBef.set (Calendar.HOUR, 12);
                            calBef.set (Calendar.MINUTE, 00);
                            calBef.set (Calendar.AM_PM, Calendar.PM);
                            calBef.add (Calendar.MINUTE, -1);

                            Calendar monthDate = Calendar.getInstance ();
                            monthDate.setTime (miniCalendar.getDate ());
                            monthDate.set (Calendar.DAY_OF_MONTH, 1);
                            monthDate.add (Calendar.DAY_OF_MONTH, -1);
                            monthDate.set (Calendar.HOUR, 12);
                            monthDate.set (Calendar.MINUTE, 00);
                            monthDate.set (Calendar.AM_PM, Calendar.PM);

                            buildDaysOutsideMonth ();
                            Calendar calAft = null;
                            int finalDay = Integer.parseInt (((JLabel) ((JPanel) ((JScrollPane) ((JViewport) daysOutsideMonth[daysOutsideMonth.length - 1].getParent ()).getParent ()).getParent ()).getComponent (0)).getText ());
                            if (finalDay < 7)
                            {
                                calAft = Calendar.getInstance ();
                                calAft.setTime (miniCalendar.getDate ());
                                calAft.add (Calendar.MONTH, 1);
                                calAft.set (Calendar.DAY_OF_MONTH, finalDay);
                                calAft.set (Calendar.HOUR, 12);
                                calAft.set (Calendar.MINUTE, 00);
                                calAft.set (Calendar.AM_PM, Calendar.PM);
                            }
                            if (dueDate.after (calBef.getTime ()) && dueDate.before (monthDate.getTime ()))
                            {
                                miniCalendar.getDayChooser ().setDayForeground (lastMonthFirst - Integer.parseInt (item.getDueDate ().split ("/")[1]) - 1, domain.utility.currentTheme.colorDoneDayOutsideMonth);
                            }
                            else if (calAft != null)
                            {
                                monthDate.set (Calendar.DAY_OF_MONTH, monthDate.getActualMaximum (Calendar.DATE));
                                if (dueDate.after (monthDate.getTime ()) && dueDate.before (calAft.getTime ()))
                                {
                                    miniCalendar.getDayChooser ().setDayForeground (days.length + Integer.parseInt (item.getDueDate ().split ("/")[1]), domain.utility.currentTheme.colorDoneDayOutsideMonth);
                                }
                            }
                        }
                    }
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        if (System.getProperty ("os.name").toLowerCase ().contains ("mac"))
        {
            Application macApp = Application.getApplication ();
            if (dueCount > 0)
            {
                macApp.setDockIconBadge (dueCount + "");
            }
            else
            {
                macApp.setDockIconBadge ("");
            }
        }
    }

    /**
     * Checks that all assignments on this day are done. Used to turn that
     * particular day to a blue color if the day is in the future. If only some
     * of the assignments are done, the day is still shown as red.
     *
     * @param dueDate The date to check for all assignments complete on.
     * @return True if all assignments are done in this day, false if any are
     * left incomplete.
     */
    private boolean dayIsDone(Date dueDate)
    {
        boolean isDone = true;
        for (int i = 0; i < domain.utility.assignmentsAndEvents.size (); ++i)
        {
            try
            {
                ListItem item = domain.utility.assignmentsAndEvents.get (i);
                Date curDate = Domain.DATE_FORMAT.parse (item.getDueDate ());
                if (dueDate.compareTo (curDate) == 0 && item.isAssignment () && !((Assignment) item).isDone ())
                {
                    isDone = false;
                    break;
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        return isDone;
    }

    /**
     * Refreshes assignments and events shown in a given day.
     *
     * @param index The index of the day to be refreshed.
     */
    public void refreshDayInCalendar(int index)
    {
        ArrayList<ListItem> items = new ArrayList<ListItem> ();
        for (int i = 0; i < daysAssignmentsAndEvents[index].getComponentCount (); ++i)
        {
            items.add ((ListItem) daysAssignmentsAndEvents[index].getComponent (i));
        }

        // first add all day events
        for (int i = 0; i < items.size (); ++i)
        {
            ListItem item = items.get (i);
            if (!item.isAssignment () && ((Event) item).isAllDay ())
            {
                daysAssignmentsAndEvents[index].add ((Event) item);
            }
        }

        // then add assignments
        for (int i = 0; i < items.size (); ++i)
        {
            ListItem item = items.get (i);
            if (item.isAssignment ())
            {
                daysAssignmentsAndEvents[index].add ((Assignment) item);
            }
        }

        // finally add timed events
        ArrayList<Event> timedEvents = new ArrayList<Event> ();
        for (int i = 0; i < items.size (); ++i)
        {
            ListItem item = items.get (i);
            if (!item.isAssignment () && !((Event) item).isAllDay ())
            {
                timedEvents.add ((Event) item);
            }
        }

        sortEventVectorByTime (timedEvents);

        for (int i = 0; i < timedEvents.size (); ++i)
        {
            daysAssignmentsAndEvents[index].add (timedEvents.get (i));
        }
    }

    /**
     * Sorts a vector of events by time.
     *
     * @param timedEvents
     */
    private void sortEventVectorByTime(ArrayList<Event> timedEvents)
    {
        boolean swapped = true;
        while (swapped)
        {
            swapped = false;
            for (int i = 0; i < timedEvents.size () - 1; ++i)
            {
                try
                {
                    Date firstDate = Domain.DATE_AND_TIME_FORMAT.parse (timedEvents.get (i).getDueDate () + " " + timedEvents.get (i).getStartTime (0) + ":" + timedEvents.get (i).getStartTime (1) + " " + timedEvents.get (i).getStartTime (2));
                    Date secondDate = Domain.DATE_AND_TIME_FORMAT.parse (timedEvents.get (i + 1).getDueDate () + " " + timedEvents.get (i + 1).getStartTime (0) + ":" + timedEvents.get (i + 1).getStartTime (1) + " " + timedEvents.get (i + 1).getStartTime (2));
                    if (firstDate.after (secondDate))
                    {
                        Event temp = timedEvents.get (i);
                        timedEvents.set (i, timedEvents.get (i + 1));
                        timedEvents.set (i + 1, temp);
                        swapped = true;
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
    }

    /**
     * Check if the currently selected repeat event has had changes.
     *
     * @param index The current index for the event.
     */
    protected int checkRepeatEventChanges(int index)
    {
        int response = -1;
        if (index != -1 && !mouseDraggingInTable && !domain.utility.assignmentsAndEvents.get (index).isAssignment ())
        {
            if (!eventChanges.isEmpty () && ((Event) domain.utility.assignmentsAndEvents.get (index)).getRepeating ().getID () != -1)
            {
                response = 1;
                OPTION_PANE.setOptions (REMOVE_REPEATING_CHOICES);
                OPTION_PANE.setMessage (domain.language.getString ("modifiedRepeatingEventText"));
                OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("modifiedRepeatingEvent"));
                optionDialog.setVisible (true);

                if (OPTION_PANE.getValue () != null)
                {
                    Event event = (Event) domain.utility.assignmentsAndEvents.get (index);
                    if (Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == ONLY_THIS_INSTANCE_OPTION)
                    {
                        // remove the attachment to repetition
                        event.getRepeating ().setID (-1);
                        domain.utility.repeatingEvents.remove (event);

                        // update this instance of the event
                        event.setItemName (eventNameTextField.getText ());
                        event.setStartTime (0, Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()));
                        event.setStartTime (1, Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()));
                        event.setStartTime (2, Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                        event.setEndTime (0, Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()));
                        event.setEndTime (1, Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()));
                        event.setEndTime (2, Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                        event.setIsAllDay (allDayEventCheckBox.isSelected ());
                        event.setCategory (domain.utility.preferences.categories.get (categoryComboBox.getSelectedIndex ()));
                        event.setEventLocation (locationTextField.getText ());
                        event.setDescription (descriptionTextArea.getText ());
                        event.getEventYear ().markChanged ();

                        refreshAssignmentsRowAt (index);
                        event.refreshText ();
                    }
                    else if (Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == ALL_IN_SERIES_OPTION)
                    {
                        // scan through all repeating events, update ones with matching repeating IDs
                        for (int i = 0; i < domain.utility.repeatingEvents.size (); ++i)
                        {
                            Event repEvent = domain.utility.repeatingEvents.get (i);
                            if (repEvent.getRepeating ().getID () == event.getRepeating ().getID ())
                            {
                                // update this instance of the event
                                repEvent.setItemName (eventNameTextField.getText ());
                                repEvent.setStartTime (0, Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()));
                                repEvent.setStartTime (1, Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()));
                                repEvent.setStartTime (2, Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                                repEvent.setEndTime (0, Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()));
                                repEvent.setEndTime (1, Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()));
                                repEvent.setEndTime (2, Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                                repEvent.setIsAllDay (allDayEventCheckBox.isSelected ());
                                repEvent.setCategory (domain.utility.preferences.categories.get (categoryComboBox.getSelectedIndex ()));
                                repEvent.setEventLocation (locationTextField.getText ());
                                repEvent.setDescription (descriptionTextArea.getText ());
                                repEvent.getEventYear ().markChanged ();

                                refreshAssignmentsRowAt (domain.utility.getAssignmentOrEventIndexByID (repEvent.getUniqueID ()));
                                repEvent.refreshText ();
                            }
                        }
                    }
                    else if (Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == ALL_FOLLOWING_OPTION)
                    {
                        long newID = System.currentTimeMillis ();
                        long oldID = event.getRepeating ().getID ();

                        // scan through all following repeating events, update ones with matching repeating IDs
                        for (int i = 0; i < domain.utility.repeatingEvents.size (); ++i)
                        {
                            Event repEvent = domain.utility.repeatingEvents.get (i);
                            try
                            {
                                if (repEvent.getRepeating ().getID () == oldID
                                    && Domain.DATE_FORMAT.parse (event.getDueDate ()).compareTo (Domain.DATE_FORMAT.parse (repEvent.getDueDate ())) <= 0)
                                {
                                    // update this instance of the event
                                    repEvent.setItemName (eventNameTextField.getText ());
                                    repEvent.setStartTime (0, Domain.HR_FORMAT.format (eventStartHrChooser.getValue ()));
                                    repEvent.setStartTime (1, Domain.MIN_FORMAT.format (eventStartMinChooser.getValue ()));
                                    repEvent.setStartTime (2, Domain.M_FORMAT.format (eventStartMChooser.getValue ()));
                                    repEvent.setEndTime (0, Domain.HR_FORMAT.format (eventEndHrChooser.getValue ()));
                                    repEvent.setEndTime (1, Domain.MIN_FORMAT.format (eventEndMinChooser.getValue ()));
                                    repEvent.setEndTime (2, Domain.M_FORMAT.format (eventEndMChooser.getValue ()));
                                    repEvent.setIsAllDay (allDayEventCheckBox.isSelected ());
                                    repEvent.setCategory (domain.utility.preferences.categories.get (categoryComboBox.getSelectedIndex ()));
                                    repEvent.setEventLocation (locationTextField.getText ());
                                    repEvent.setDescription (descriptionTextArea.getText ());
                                    repEvent.getEventYear ().markChanged ();

                                    refreshAssignmentsRowAt (domain.utility.getAssignmentOrEventIndexByID (repEvent.getUniqueID ()));
                                    repEvent.refreshText ();

                                    // since this is now a separate repetition instance, create it with the new repeating ID
                                    repEvent.getRepeating ().setID (newID);
                                }
                            }
                            catch (ParseException ex)
                            {
                                Domain.LOGGER.add (ex);
                            }
                        }
                    }
                    else
                    {
                        if (tempEvent != null)
                        {
                            event.setItemName (tempEvent.getItemName ());
                            event.setCategory (tempEvent.getCategory ());
                            event.setDate (tempEvent.getDueDate (), domain.utility);
                            event.setDescription (tempEvent.getDescription ());
                            event.setStartTime (0, tempEvent.getStartTime (0));
                            event.setStartTime (1, tempEvent.getStartTime (1));
                            event.setStartTime (2, tempEvent.getStartTime (2));
                            event.setEndTime (0, tempEvent.getEndTime (0));
                            event.setEndTime (1, tempEvent.getEndTime (1));
                            event.setEndTime (2, tempEvent.getEndTime (2));
                            event.setIsAllDay (tempEvent.isAllDay ());
                            event.setEventLocation (tempEvent.getEventLocation ());
                            event.setRepeating (tempEvent.getRepeating ().toString ());
                            event.getRepeating ().setID (tempEvent.getRepeating ().getID ());
                            event.getEventYear ().markChanged ();

                            event.refreshRowObject ();
                            event.refreshText ();
                            repeatEventChanges = false;
                        }
                    }
                }
                else
                {
                    if (tempEvent != null)
                    {
                        Event event = (Event) domain.utility.assignmentsAndEvents.get (index);

                        event.setItemName (tempEvent.getItemName ());
                        event.setCategory (tempEvent.getCategory ());
                        event.setDate (tempEvent.getDueDate (), domain.utility);
                        event.setDescription (tempEvent.getDescription ());
                        event.setStartTime (0, tempEvent.getStartTime (0));
                        event.setStartTime (1, tempEvent.getStartTime (1));
                        event.setStartTime (2, tempEvent.getStartTime (2));
                        event.setEndTime (0, tempEvent.getEndTime (0));
                        event.setEndTime (1, tempEvent.getEndTime (1));
                        event.setEndTime (2, tempEvent.getEndTime (2));
                        event.setIsAllDay (tempEvent.isAllDay ());
                        event.setEventLocation (tempEvent.getEventLocation ());
                        event.setRepeating (tempEvent.getRepeating ().toString ());
                        event.getRepeating ().setID (tempEvent.getRepeating ().getID ());
                        event.getEventYear ().markChanged ();

                        event.refreshRowObject ();
                        event.refreshText ();

                        repeatEventChanges = false;
                    }
                }
            }

            eventChanges.clear ();
        }

        return response;
    }

    /**
     * The event that occurs when an item is selected in the assignments and
     * events list.
     *
     * @param e The event trigger object.
     */
    public void assignmentsTableRowSelected(ListSelectionEvent e)
    {
        if (!assignmentsTable.removingSelectionInterval && !mouseDraggingInTable && !ignoreTableSelection)
        {
            try
            {
                if (domain.currentIndexFromVector != -1 && assignmentsTable.getVectorIndexFromSelectedRow () != domain.currentIndexFromVector
                    && domain.currentIndexFromVector < domain.utility.assignmentsAndEvents.size () && !domain.removingAssignmentOrEvent)
                {
                    checkAssignmentOrEventChanges (domain.currentIndexFromVector);
                    checkRepeatEventChanges (domain.currentIndexFromVector);
                    domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).showAsSelected (false);
                }
                eventChanges.clear ();
                repeatEventChanges = false;

                if (domain.utility.assignmentsAndEvents.size () > 0 && assignmentsTable.getSelectedRow () != -1)
                {
                    domain.currentIndexFromVector = assignmentsTable.getVectorIndexFromSelectedRow ();
                    domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).showAsSelected (true);
                }
                else
                {
                    domain.currentIndexFromVector = -1;
                }
            }
            catch (NullPointerException ex)
            {
                domain.currentIndexFromVector = -1;
            }

            if (domain.currentIndexFromVector != -1)
            {
                ListItem item = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                String[] oldDate = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");

                if (item.isAssignment ())
                {
                    showAssignmentDetails (domain.currentIndexFromVector);
                }
                else
                {
                    showEventDetails (domain.currentIndexFromVector);
                    try
                    {
                        tempEvent = domain.createCloneObject ((Event) item, domain.utility, Domain.DATE_FORMAT.parse (item.getDueDate ()), false);
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }
                }
                cloneButton.setEnabled (true);
                removeButton.setEnabled (true);
                checkInstructorButtonState ();

                String[] newDate = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                if (middleTabbedPane.getSelectedIndex () == 1
                    && !oldDate[0].equals (newDate[0]))
                {
                    loadCalendarView (true);
                }
            }
            else
            {
                mainDetailsDisplay ();
            }
        }
        else if (assignmentsTable.removingSelectionInterval)
        {
            try
            {
                domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).showAsSelected (false);
            }
            catch (Exception ex)
            {
            }
            domain.currentIndexFromVector = -1;
            cloneButton.setEnabled (false);
            removeButton.setEnabled (false);
            checkInstructorButtonState ();
            mainDetailsDisplay ();
        }
    }

    /**
     * Determine which main details panel to display when a node is selected.
     */
    public void mainDetailsDisplay()
    {
        if (getSelectedCourseIndex () == -1 && getSelectedTermIndex () != -1)
        {
            showMainTermDetails ();
        }
        else if (getSelectedCourseIndex () != -1)
        {
            showMainCourseDetails ();
        }
        else
        {
            clearMainDetailsPanel ();
        }
    }

    /**
     * The event that occurs when an item is selected in the term tree.
     *
     * @param e The triggering event object.
     */
    protected void termTreeNodeSelected(TreeSelectionEvent e)
    {
        assignmentsTable.setSelectedRow (-1);
        assignmentsTableRowSelected (new ListSelectionEvent (this, -1, -1, false));
        if (!initLoading)
        {
            filter (true);
        }

        checkInstructorButtonState ();
        mainDetailsDisplay ();
    }

    /**
     * Adjust all columns (except the Assignment column) to their specified
     * width, then calculates the remaining space and fills it with the
     * Assignment column.
     */
    protected void adjustAssignmentTableColumnWidths()
    {
        assignmentsTable.getColumnModel ().getColumn (0).setMinWidth (20);
        assignmentsTable.getColumnModel ().getColumn (0).setMaxWidth (20);
        assignmentsTable.getColumnModel ().getColumn (2).setMinWidth (90);
        assignmentsTable.getColumnModel ().getColumn (2).setMaxWidth (100);
        assignmentsTable.getColumnModel ().getColumn (4).setMinWidth (80);
        assignmentsTable.getColumnModel ().getColumn (4).setMaxWidth (80);
        assignmentsTable.getColumnModel ().getColumn (5).setMinWidth (50);
        assignmentsTable.getColumnModel ().getColumn (5).setMaxWidth (50);
    }

    /**
     * Checks the states of the selected email attribute of the selected
     * assignment and/or course to see if it exists; if a professor's email does
     * exist, the button to email them becomes available.
     */
    protected void checkInstructorButtonState()
    {
        try
        {
            Course course = null;
            if (domain.currentIndexFromVector != -1 && domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).isAssignment ())
            {
                course = ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ();
            }
            else if (domain.currentIndexFromVector == -1 && getSelectedCourseIndex () != -1)
            {
                course = domain.utility.courses.get (getSelectedCourseIndex ());
            }
            if (course != null)
            {
                boolean found = false;
                for (int i = 0; i < course.getInstructorCount (); ++i)
                {
                    if (!course.getInstructor (i).getInstructorEmail ().equals ("") || !course.getInstructor (i).getInstructorPhone ().equals (""))
                    {
                        found = true;
                        break;
                    }
                }
                if (found)
                {
                    askInstructorButton.setEnabled (true);
                }
                else
                {
                    askInstructorButton.setEnabled (false);
                }
            }
            else
            {
                askInstructorButton.setEnabled (false);
            }
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            Domain.LOGGER.add (ex);
        }
        catch (NullPointerException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Checks if any of the details in the assignment detail text fields are
     * different than the values in this assignmentsAndEvents stored object.
     *
     * @param index The index of the assignment to be checked.
     */
    public synchronized int checkAssignmentOrEventChanges(int index)
    {
        int response = -1;
        if (index != -1 && !mouseDraggingInTable)
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (index);
            if (item.isAssignment ())
            {
                Assignment assignment = (Assignment) item;
                if (!assignmentNameTextField.getText ().equals (assignment.getItemName ()))
                {
                    assignment.getCourse ().markChanged ();
                    response = domain.setAssignmentName (index);
                }
                if (!gradeTextField.getText ().equals (assignment.getGrade ()))
                {
                    assignment.getCourse ().markChanged ();
                    domain.setAssignmentGrade (index);
                }
                if (!commentsTextArea.getText ().equals (assignment.getComments ()))
                {
                    assignment.getCourse ().markChanged ();
                    domain.setAssignmentComments (index);
                }
            }
            else
            {
                Event event = (Event) item;
                if (!eventNameTextField.getText ().equals (event.getItemName ()))
                {
                    event.getEventYear ().markChanged ();
                    response = domain.setEventName (index);
                }
                if (!locationTextField.getText ().equals (event.getEventLocation ()))
                {
                    event.getEventYear ().markChanged ();
                    domain.setEventLocation (index);
                }
                if (!descriptionTextArea.getText ().equals (event.getDescription ()))
                {
                    event.getEventYear ().markChanged ();
                    domain.setEventDescription (index);
                }
            }
        }
        return response;
    }

    /**
     * Refreshes an entire row of the assignmentsAndEvents table after a value
     * has been changed in the assignment object.
     *
     * @param row The row to be updated.
     */
    public void refreshAssignmentsRowAt(int row)
    {
        if (row != -1)
        {
            Object[] rowObject = domain.utility.assignmentsAndEvents.get (row).getRowObject ();
            int index = assignmentsTable.getSelectableRowFromVectorIndex (row);

            if (index != -1)
            {
                for (int i = 0; i < assignmentsTableModel.getColumnCount (); ++i)
                {
                    assignmentsTableModel.setValueAt (rowObject[i], index, i);
                }
            }
        }
    }

    /**
     * Scroll the assignments list to the element given or to domain.today's
     * date.
     *
     * @param item The item to scroll to, which is null if scrolling to
     * domain.today.
     */
    public void scrollToItemOrToday(ListItem item)
    {
        long id = -1;
        if (item == null)
        {
            // only scroll if the assignments and events are sorted by due date
            if (domain.utility.preferences.sortIndex == 4)
            {
                for (int i = 0; i < domain.utility.assignmentsAndEvents.size (); ++i)
                {
                    try
                    {
                        if (!Domain.DATE_FORMAT.parse (domain.utility.assignmentsAndEvents.get (i).getDueDate ()).before (domain.today))
                        {
                            id = domain.utility.assignmentsAndEvents.get (i).getUniqueID ();
                            break;
                        }
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }
                }
            }
        }
        else
        {
            id = item.getUniqueID ();
        }

        int index = domain.utility.getTableIndexByID (id);
        if (id != -1 && index != -1)
        {
            assignmentsTable.scrollRectToVisible (assignmentsTable.getCellRect (index, 0, false));
        }
    }

    /**
     * Disables all child components in a given component.
     *
     * @param component The components whose children will all be disabled.
     */
    protected void disableAllComponents(JComponent component)
    {
        for (int i = 0; i < component.getComponents ().length; ++i)
        {
            component.getComponent (i).setEnabled (false);
        }
    }

    /**
     * Sets the selection for the given combo box to the element with the given
     * id.
     *
     * @param box The combo box to set selection for.
     * @param id The unique ID to set selected.
     */
    public void setComboBoxSelection(JComboBox box, long id)
    {
        for (int i = 0; i < box.getItemCount (); ++i)
        {
            if (((ComboListItem) box.getModel ().getElementAt (i)).getUniqueID () == id)
            {
                box.setSelectedIndex (i);
                break;
            }
        }
    }

    /**
     * Enables all child components in a given component.
     *
     * @param component The components whose children will all be enabled.
     */
    protected void enableAllComponents(JComponent component)
    {
        for (int i = 0; i < component.getComponents ().length; ++i)
        {
            component.getComponent (i).setEnabled (true);
        }
    }

    /**
     * Show summary of the currently selected course in the main window.
     */
    private void showMainCourseDetails()
    {
        Course course = domain.utility.courses.get (getSelectedCourseIndex ());
        courseNameDetailsLabel.setText (course.getTypeName ());
        courseNameDetailsLabel.setForeground (course.getColor ());
        courseStartDateDetailsLabel.setText (domain.language.getString ("startDate") + ": " + course.getStartDate ());
        courseStartDateDetailsLabel.setSize (courseStartDateDetailsLabel.getPreferredSize ());
        courseEndDateDetailsLabel.setText (domain.language.getString ("endDate") + ": " + course.getEndDate ());
        courseEndDateDetailsLabel.setSize (courseEndDateDetailsLabel.getPreferredSize ());

        if (!course.isOnline ())
        {
            courseStartTimeDetailsLabel.setText (domain.language.getString ("startTime") + ": " + course.getStartTime (0) + ":" + course.getStartTime (1) + " " + course.getStartTime (2));
            courseStartTimeDetailsLabel.setSize (courseStartTimeDetailsLabel.getPreferredSize ());
            courseEndTimeDetailsLabel.setText (domain.language.getString ("endTime") + ": " + course.getEndTime (0) + ":" + course.getEndTime (1) + " " + course.getEndTime (2));
            courseEndTimeDetailsLabel.setSize (courseEndTimeDetailsLabel.getPreferredSize ());
        }
        else
        {
            courseStartTimeDetailsLabel.setText (domain.language.getString ("onlineCourse"));
            courseStartTimeDetailsLabel.setSize (courseStartTimeDetailsLabel.getPreferredSize ());
            courseEndTimeDetailsLabel.setText ("");
        }

        courseCreditsDetailsLabel.setText (domain.language.getString ("credits") + ": " + (Double.parseDouble (course.getCredits ()) + Double.parseDouble (course.getLabCredits ())));
        courseCreditsDetailsLabel.setSize (courseCreditsDetailsLabel.getPreferredSize ());
        if (course.isOnline ())
        {
            courseRoomDetailsLabel.setText ("");
        }
        else
        {
            if (!course.getRoomLocation ().replaceAll (" ", "").equals (""))
            {
                courseRoomDetailsLabel.setText (domain.language.getString ("room") + ": " + course.getRoomLocation ());
            }
            else
            {
                courseRoomDetailsLabel.setText (domain.language.getString ("room") + ": " + domain.language.getString ("notApplicableAbbrev"));
            }
            courseRoomDetailsLabel.setSize (courseRoomDetailsLabel.getPreferredSize ());
        }

        String listedDays = course.getDaysString (domain.language);
        if (!course.isOnline () && listedDays.length () > 0)
        {
            String daysString = domain.language.getString ("day");
            if (listedDays.split (",").length > 1)
            {
                daysString = domain.language.getString ("days");
            }
            courseDaysDetailsLabel.setText ("<html>" + daysString + ": " + listedDays + "</html>");
            courseDaysDetailsLabel.setSize (courseDaysDetailsLabel.getPreferredSize ());
        }
        else
        {
            courseDaysDetailsLabel.setText ("");
        }

        courseTotalAssignmentsDetailsLabel.setText (domain.language.getString ("totalAssignments") + ": " + Domain.NUM_FORMAT.format (course.getAssignmentCount ()));
        courseTotalAssignmentsDetailsLabel.setSize (courseTotalAssignmentsDetailsLabel.getPreferredSize ());
        courseUnfinishedDetailsLabel.setText ("    " + domain.language.getString ("unfinished") + ": " + Domain.NUM_FORMAT.format (course.getUnfinishedAssignmentCount ()));
        courseUnfinishedDetailsLabel.setSize (courseUnfinishedDetailsLabel.getPreferredSize ());
        double grade = domain.calculateGradeForCourse (course) / 100;
        courseCurrentGradeDetailsLabel.setText (domain.language.getString ("averageGrade") + ": " + (grade == -0.01 ? domain.language.getString ("notApplicableAbbrev") : Domain.PERCENT_FORMAT.format (grade)));
        courseCurrentGradeDetailsLabel.setSize (courseCurrentGradeDetailsLabel.getPreferredSize ());
        courseTypesDetailsLabel.setText (domain.language.getString ("types") + ": " + course.getTypeCount ());
        courseTypesDetailsLabel.setSize (courseTypesDetailsLabel.getPreferredSize ());
        courseTextbooksDetailsLabel.setText (domain.language.getString ("textbooks") + ": " + course.getTextbookCount ());
        courseTextbooksDetailsLabel.setSize (courseTextbooksDetailsLabel.getPreferredSize ());

        courseContentPanel.setVisible (true);
        userDetailsContentPanel.setVisible (false);
        noUserDetailsPanel.setVisible (false);
        blankContentPanel.setVisible (false);
        termContentPanel.setVisible (false);
        assignmentContentPanel.setVisible (false);
        eventContentPanel.setVisible (false);
        ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("courseDetails"));
        rightPanel.invalidate ();
    }

    /**
     * Show summary of the currently selected term in the main window.
     */
    private void showMainTermDetails()
    {
        Term term = domain.utility.terms.get (getSelectedTermIndex ());
        termNameDetailsLabel.setText (term.getTypeName ());
        termStartDateDetailsLabel.setText (domain.language.getString ("startDate") + ": " + term.getStartDate ());
        termStartDateDetailsLabel.setSize (termStartDateDetailsLabel.getPreferredSize ());
        termEndDateDetailsLabel.setText (domain.language.getString ("endDate") + ": " + term.getEndDate ());
        termEndDateDetailsLabel.setSize (termEndDateDetailsLabel.getPreferredSize ());
        termCoursesDetailsLabel.setText (domain.language.getString ("courses") + ": " + term.getCourseCount ());
        termCoursesDetailsLabel.setSize (termCoursesDetailsLabel.getPreferredSize ());
        termCreditsDetailsLabel.setText (domain.language.getString ("credits") + ": " + term.getCreditCount ());
        termCreditsDetailsLabel.setSize (termCreditsDetailsLabel.getPreferredSize ());
        termTotalAssignmentsDetailsLabel.setText (domain.language.getString ("totalAssignments") + ": " + Domain.NUM_FORMAT.format (term.getAssignmentCount ()));
        termTotalAssignmentsDetailsLabel.setSize (termTotalAssignmentsDetailsLabel.getPreferredSize ());
        termUnfinishedDetailsLabel.setText ("    " + domain.language.getString ("unfinished") + ": " + Domain.NUM_FORMAT.format (term.getUnfinishedAssignmentCount ()));
        termUnfinishedDetailsLabel.setSize (termUnfinishedDetailsLabel.getPreferredSize ());
        double grade = domain.calculateGradeForTerm (term) / 100;
        termAvgGradeDetailsLabel.setText (domain.language.getString ("averageGrade") + ": " + (grade == -0.01 ? domain.language.getString ("notApplicableAbbrev") : Domain.PERCENT_FORMAT.format (grade)));
        termAvgGradeDetailsLabel.setSize (termAvgGradeDetailsLabel.getPreferredSize ());
        termTypesDetailsLabel.setText (domain.language.getString ("types") + ": " + term.getTypesCount ());
        termTypesDetailsLabel.setSize (termTypesDetailsLabel.getPreferredSize ());
        termTextbooksDetailsLabel.setText (domain.language.getString ("textbooks") + ": " + term.getTextbooksCount ());
        termTextbooksDetailsLabel.setSize (termTextbooksDetailsLabel.getPreferredSize ());

        termContentPanel.setVisible (true);
        courseContentPanel.setVisible (false);
        userDetailsContentPanel.setVisible (false);
        noUserDetailsPanel.setVisible (false);
        blankContentPanel.setVisible (false);
        assignmentContentPanel.setVisible (false);
        eventContentPanel.setVisible (false);
        ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("termDetails"));
        rightPanel.invalidate ();
    }

    /**
     * Ensure there is at least one user detail specified.
     */
    private boolean emptyStudentDetails()
    {
        if (domain.utility.userDetails.getSchool ().equals ("")
            && domain.utility.userDetails.getIdNumber ().equals ("")
            && domain.utility.userDetails.getBoxNumber ().equals ("")
            && domain.utility.userDetails.getAdvisorName ().equals ("")
            && domain.utility.userDetails.getAdvisorOfficeHours ().equals ("")
            && domain.utility.userDetails.getAdvisorsOfficeLocation ().equals ("")
            && domain.utility.userDetails.getAdvisorEmail ().equals ("")
            && domain.utility.userDetails.getAdvisorPhone ().equals ("")
            && domain.utility.userDetails.getEmail ().equals (""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Show summary of user details in the main window.
     */
    private void clearMainDetailsPanel()
    {
        closeRepeatEventDialog ();

        assignmentsTable.setSelectedRow (-1);

        if (emptyStudentDetails ())
        {


            noUserDetailsPanel.setVisible (true);
            blankContentPanel.setVisible (false);
            userDetailsContentPanel.setVisible (false);
            termContentPanel.setVisible (false);
            courseContentPanel.setVisible (false);
            assignmentContentPanel.setVisible (false);
            eventContentPanel.setVisible (false);
        }
        else
        {
            userNameDetailsLabel.setText (domain.utility.userDetails.getStudentName ());
            if (!domain.utility.userDetails.getSchool ().equals (""))
            {
                schoolDetailsLabel.setText (domain.language.getString ("school") + ": " + domain.utility.userDetails.getSchool ());
                schoolDetailsLabel.setSize (schoolDetailsLabel.getPreferredSize ());
            }
            else
            {
                schoolDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getIdNumber ().equals (""))
            {
                idNumberDetailsLabel.setText (domain.language.getString ("idNumber") + ": " + domain.utility.userDetails.getIdNumber ());
                idNumberDetailsLabel.setSize (idNumberDetailsLabel.getPreferredSize ());
            }
            else
            {
                idNumberDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getBoxNumber ().equals (""))
            {
                boxNumberDetailsLabel.setText (domain.language.getString ("boxNumber") + ": " + domain.utility.userDetails.getBoxNumber ());
                boxNumberDetailsLabel.setSize (boxNumberDetailsLabel.getPreferredSize ());
            }
            else
            {
                boxNumberDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getAdvisorName ().equals (""))
            {
                advisorDetailsLabel.setText (domain.language.getString ("advisor") + ": " + domain.utility.userDetails.getAdvisorName ());
                advisorDetailsLabel.setSize (advisorDetailsLabel.getPreferredSize ());
            }
            else
            {
                advisorDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getAdvisorOfficeHours ().equals (""))
            {
                officeHoursDetailsLabel.setText (domain.language.getString ("officeHours") + ": " + domain.utility.userDetails.getAdvisorOfficeHours ());
                officeHoursDetailsLabel.setSize (officeHoursDetailsLabel.getPreferredSize ());
            }
            else
            {
                officeHoursDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getAdvisorsOfficeLocation ().equals (""))
            {
                officeLocationDetailsLabel.setText (domain.language.getString ("officeLocation") + ": " + domain.utility.userDetails.getAdvisorsOfficeLocation ());
                officeLocationDetailsLabel.setSize (officeLocationDetailsLabel.getPreferredSize ());
            }
            else
            {
                officeLocationDetailsLabel.setText ("");
            }
            if (!domain.utility.userDetails.getAdvisorEmail ().equals ("")
                || !domain.utility.userDetails.getAdvisorPhone ().equals (""))
            {
                contactAdvisorButton.setVisible (true);
            }
            else
            {
                contactAdvisorButton.setVisible (false);
            }

            userDetailsContentPanel.setVisible (true);
            noUserDetailsPanel.setVisible (false);
            blankContentPanel.setVisible (false);
            termContentPanel.setVisible (false);
            courseContentPanel.setVisible (false);
            assignmentContentPanel.setVisible (false);
            eventContentPanel.setVisible (false);
        }
        ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("userDetails"));
        rightPanel.invalidate ();
        cloneButton.setEnabled (false);
        removeButton.setEnabled (false);
        checkInstructorButtonState ();
    }

    /**
     * Displays the details for the currently selected assignment in the
     * Assignment Details panel of the main area.
     *
     * @param index The index of the assignment selected
     */
    private void showAssignmentDetails(int index)
    {
        if (domain.currentIndexFromVector != -1)
        {
            domain.assignmentOrEventLoading.push (true);

            closeRepeatEventDialog ();

            domain.utility.loadDetailsCourseBox ();
            domain.utility.loadDetailsTextbookBox ();
            domain.utility.loadDetailsTypeBox ();

            Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (index);
            try
            {
                dueDateChooser.setDate (Domain.DATE_FORMAT.parse (assignment.getDueDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                dueHrChooser.setValue (Domain.HR_FORMAT.parse (assignment.getDueTime (0)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                dueMinChooser.setValue (Domain.MIN_FORMAT.parse (assignment.getDueTime (1)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                dueMChooser.setValue (Domain.M_FORMAT.parse (assignment.getDueTime (2)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            setComboBoxSelection (detailsCourseComboBox, assignment.getCourse ().getUniqueID ());
            if (assignment.getTextbook () != null)
            {
                setComboBoxSelection (detailsTextbookComboBox, assignment.getTextbook ().getUniqueID ());
            }
            if (assignment.getType () != null)
            {
                setComboBoxSelection (detailsTypeComboBox, assignment.getType ().getUniqueID ());
            }
            prioritySlider.setValue (assignment.getPriority ());
            completedCheckBox.setSelected (assignment.isDone ());
            completedCheckBoxActionPerformed (null);
            gradeTextField.setText (assignment.getGrade ());
            commentsTextArea.setText (assignment.getComments ());
            assignmentContentPanel.setVisible (true);
            termContentPanel.setVisible (false);
            courseContentPanel.setVisible (false);
            eventContentPanel.setVisible (false);
            userDetailsContentPanel.setVisible (false);
            noUserDetailsPanel.setVisible (false);
            blankContentPanel.setVisible (false);
            ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("assignmentDetails"));
            rightPanel.invalidate ();

            if (middleTabbedPane.getSelectedIndex () == 1 && assignment.getParent () != null)
            {
                if (selectedDayPanel != null)
                {
                    selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
                }
                selectedDayPanel = (JPanel) assignment.getParent ().getParent ().getParent ().getParent ();
                selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
            }
            try
            {
                miniCalendar.setDate (Domain.DATE_FORMAT.parse (assignment.getDueDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            refreshBusyDays ();

            assignmentNameTextField.setText (assignment.getItemName ());
            if (dontReselectName.empty ())
            {
                assignmentNameTextField.requestFocus ();
                assignmentNameTextField.selectAll ();
            }

            domain.assignmentOrEventLoading.pop ();
        }
    }

    /**
     * Displays the details for the currently selected assignment in the
     * Assignment Details panel of the main area.
     *
     * @param index The index of the event selected.
     */
    private void showEventDetails(int index)
    {
        domain.assignmentOrEventLoading.push (true);

        closeRepeatEventDialog ();

        domain.refreshCategoryComboModel ();

        Event event = (Event) domain.utility.assignmentsAndEvents.get (index);
        try
        {
            eventDateChooser.setDate (Domain.DATE_FORMAT.parse (event.getDueDate ()));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventStartHrChooser.setValue (Domain.HR_FORMAT.parse (event.getStartTime (0)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventStartMinChooser.setValue (Domain.MIN_FORMAT.parse (event.getStartTime (1)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventStartMChooser.setValue (Domain.M_FORMAT.parse (event.getStartTime (2)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventEndHrChooser.setValue (Domain.HR_FORMAT.parse (event.getEndTime (0)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventEndMinChooser.setValue (Domain.MIN_FORMAT.parse (event.getEndTime (1)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        try
        {
            eventEndMChooser.setValue (Domain.M_FORMAT.parse (event.getEndTime (2)));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        allDayEventCheckBox.setSelected (event.isAllDay ());
        allDayEventCheckBoxActionPerformed (null);
        locationTextField.setText (event.getEventLocation ());
        if (!event.getEventLocation ().replaceAll (" ", "").equals (""))
        {
            googleMapsButton.setEnabled (true);
        }
        else
        {
            googleMapsButton.setEnabled (false);
        }

        descriptionTextArea.setText (event.getDescription ());
        categoryComboBox.setSelectedItem (event.getCategory ().getName ());

        eventContentPanel.setVisible (true);
        termContentPanel.setVisible (false);
        courseContentPanel.setVisible (false);
        assignmentContentPanel.setVisible (false);
        userDetailsContentPanel.setVisible (false);
        noUserDetailsPanel.setVisible (false);
        blankContentPanel.setVisible (false);
        ((TitledBorder) rightPanel.getBorder ()).setTitle (domain.language.getString ("eventDetails"));
        rightPanel.invalidate ();

        if (middleTabbedPane.getSelectedIndex () == 1 && event.getParent () != null)
        {
            if (selectedDayPanel != null)
            {
                selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            }
            selectedDayPanel = (JPanel) event.getParent ().getParent ().getParent ().getParent ();
            selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        }
        try
        {
            miniCalendar.setDate (Domain.DATE_FORMAT.parse (event.getDueDate ()));
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        refreshBusyDays ();

        eventNameTextField.setText (event.getItemName ());
        if (dontReselectName.empty ())
        {
            eventNameTextField.requestFocus ();
            eventNameTextField.selectAll ();
        }

        domain.assignmentOrEventLoading.pop ();
    }

    /**
     * Displays the edit menu with buttons enabled/disabled appropriately.
     *
     * @param evt The causing event.
     */
    public void showTermEditMenu(MouseEvent evt)
    {
        if (!initLoading)
        {
            addTermEditMenuItem.setEnabled (true);
            if (termTree.getRowForLocation (evt.getX (), evt.getY ()) != -1)
            {
                if (termTree.getPathForLocation (evt.getX (), evt.getY ()).getParentPath ().toString ().equals ("[root]"))
                {
                    editTermEditMenuItem.setEnabled (true);
                    removeTermEditMenuItem.setEnabled (true);
                    editInstructorsEditMenuItem.setEnabled (false);
                    editTypesEditMenuItem.setEnabled (false);
                    editTextbooksEditMenuItem.setEnabled (false);
                    editCourseEditMenuItem.setEnabled (false);
                    removeCourseEditMenuItem.setEnabled (false);
                    courseWebsiteMenuItem.setEnabled (false);
                    labWebsiteMenuItem.setEnabled (false);
                }
                else
                {
                    editTermEditMenuItem.setEnabled (true);
                    removeTermEditMenuItem.setEnabled (true);
                    editInstructorsEditMenuItem.setEnabled (true);
                    editTypesEditMenuItem.setEnabled (true);
                    editTextbooksEditMenuItem.setEnabled (true);
                    editCourseEditMenuItem.setEnabled (true);
                    removeCourseEditMenuItem.setEnabled (true);
                    if (!domain.utility.courses.get (getSelectedCourseIndex ()).getCourseWebsite ().equals (""))
                    {
                        courseWebsiteMenuItem.setEnabled (true);
                    }
                    else
                    {
                        courseWebsiteMenuItem.setEnabled (false);
                    }
                    if (domain.utility.courses.get (getSelectedCourseIndex ()).hasLab ()
                        && domain.utility.courses.get (getSelectedCourseIndex ()).getLabWebsite ().equals (""))
                    {
                        labWebsiteMenuItem.setEnabled (true);
                    }
                    else
                    {
                        labWebsiteMenuItem.setEnabled (false);
                    }
                }
            }
            else
            {
                editTermEditMenuItem.setEnabled (false);
                removeTermEditMenuItem.setEnabled (false);
                editCourseEditMenuItem.setEnabled (false);
                editInstructorsEditMenuItem.setEnabled (false);
                editTypesEditMenuItem.setEnabled (false);
                editTextbooksEditMenuItem.setEnabled (false);
                removeCourseEditMenuItem.setEnabled (false);
                courseWebsiteMenuItem.setEnabled (false);
                labWebsiteMenuItem.setEnabled (false);
            }
            if (domain.utility.terms.size () > 0)
            {
                addCourseEditMenuItem.setEnabled (true);
            }
            else
            {
                addCourseEditMenuItem.setEnabled (false);
            }
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentTermEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentTermEditMenuItem.setEnabled (false);
            }
        }
        else
        {
            addTermEditMenuItem.setEnabled (false);
            addCourseEditMenuItem.setEnabled (false);
            addAssignmentTermEditMenuItem.setEnabled (false);
            editTermEditMenuItem.setEnabled (false);
            removeTermEditMenuItem.setEnabled (false);
            editInstructorsEditMenuItem.setEnabled (false);
            editTypesEditMenuItem.setEnabled (false);
            editTextbooksEditMenuItem.setEnabled (false);
            editCourseEditMenuItem.setEnabled (false);
            removeCourseEditMenuItem.setEnabled (false);
            courseWebsiteMenuItem.setEnabled (false);
            labWebsiteMenuItem.setEnabled (false);
        }
        termEditMenu.setLocation (evt.getLocationOnScreen ());
        termEditMenu.show (termTree, evt.getPoint ().x, evt.getPoint ().y);
    }

    /**
     * Shows the assignment edit menu with buttons enabled/disabled
     * appropriately.
     *
     * @param evt The causing event.
     */
    public void showAssignmentAndEventEditMenu(MouseEvent evt)
    {
        if (!initLoading)
        {
            addEventEditMenuItem.setEnabled (true);
            int index = assignmentsTable.getVectorIndexFromSelectedRow (assignmentsTable.rowAtPoint (evt.getPoint ()));
            if (index != -1)
            {
                cloneEditMenuItem.setEnabled (true);
                removeEditMenuItem.setEnabled (true);
            }
            else
            {
                cloneEditMenuItem.setEnabled (false);
                removeEditMenuItem.setEnabled (false);
            }
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentEditMenuItem.setEnabled (false);
            }
        }
        else
        {
            addAssignmentEditMenuItem.setEnabled (false);
            addEventEditMenuItem.setEnabled (false);
            removeEditMenuItem.setEnabled (false);
            cloneEditMenuItem.setEnabled (false);
        }
        assignmentsEditMenu.show (evt.getComponent (), evt.getPoint ().x, evt.getPoint ().y);
    }

    /**
     * Expands all possible nodes in the term tree.
     *
     * @param e The firing event.
     */
    protected void expandTermTree(TreeExpansionEvent e)
    {
        for (int row = 0; row < termTree.getRowCount (); ++row)
        {
            termTree.expandRow (row);
        }
    }

    /**
     * Enables buttons relating to course adding.
     */
    protected void enableCourseButtons()
    {
        addCourseMenuItem.setEnabled (true);
        mainFrame.addCourseTopMenuItem.setEnabled (true);
    }

    /**
     * Disables buttons relating to course adding.
     */
    protected void disableCourseButtons()
    {
        addCourseMenuItem.setEnabled (false);
        mainFrame.addCourseTopMenuItem.setEnabled (false);
    }

    /**
     * Enables buttons relating to assignment adding.
     */
    protected void enableAssignmentButtons()
    {
        addAssignmentMenuItem.setEnabled (true);
        mainFrame.addAssignmentTopMenuItem.setEnabled (true);
    }

    /**
     * Disables buttons relating to assignment adding.
     */
    protected void disableAssignmentButtons()
    {
        addAssignmentMenuItem.setEnabled (false);
        mainFrame.addAssignmentTopMenuItem.setEnabled (false);
    }

    /**
     * Retrieves the current selected course from the term tree.
     *
     * @return The currently selected course in the term tree.
     */
    public int getSelectedCourseIndex()
    {
        try
        {
            if (termTree.getSelectionPath ().getParentPath ().toString ().equals ("[root]"))
            {
                return -1;
            }
        }
        catch (NullPointerException ex)
        {
            return -1;
        }

        try
        {
            if (termTree.getSelectionRows () != null)
            {
                String[] path = termTree.getSelectionPath ().toString ().split (", ");
                if (path[0].startsWith ("["))
                {
                    path[0] = path[0].substring (1, path[0].length ());
                }
                if (path[path.length - 1].endsWith ("]"))
                {
                    path[path.length - 1] = path[path.length - 1].substring (0, path[path.length - 1].length () - 1);
                }
                for (int i = 0; i < domain.utility.courses.size (); ++i)
                {
                    TreeNode[] nodes = domain.utility.courses.get (i).getPath ();
                    boolean match = true;
                    for (int j = 0; j < nodes.length; ++j)
                    {
                        try
                        {
                            if (!path[j].equals (nodes[j].toString ()))
                            {
                                match = false;
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ex)
                        {
                            match = false;
                            break;
                        }
                    }
                    if (match)
                    {
                        return i;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            return -1;
        }
        return -1;
    }

    /**
     * Retrieves the selected course index relative to the selected term index.
     *
     * @param termIndex The term to be indexed from for the course.
     * @return The index value of the course relative to the term.
     */
    public int getSelectedCourseIndexFrom(int termIndex)
    {
        int index = -1;
        if (termTree.getSelectionRows () != null)
        {
            for (int i = 0; i <= termTree.getSelectionRows ()[0]; ++i)
            {
                if (termTree.getPathForRow (i).getParentPath ().toString ().equals ((new TreePath (domain.utility.terms.get (termIndex).getPath ())).toString ()))
                {
                    ++index;
                }
            }
        }
        return index;
    }

    /**
     * Construct the daysOutsideMonth array if it is null.
     */
    private void buildDaysOutsideMonth()
    {
        if (daysOutsideMonth == null)
        {
            constructCalendarForMonth ();
        }
    }

    /**
     * Constructs the visual calendar for the current month, filling the array
     * of JPanel days as it goes.
     *
     * @return The first day of the month.
     */
    private int[] constructCalendarForMonth()
    {
        Date date = miniCalendar.getDate ();
        Calendar cal = Calendar.getInstance ();
        cal.setTime (date);

        int lastDate = cal.getActualMaximum (Calendar.DATE);
        cal.set (Calendar.DAY_OF_MONTH, lastDate);
        int lastDay = cal.get (Calendar.DAY_OF_MONTH);
        daysAssignmentsAndEvents = new JPanel[lastDay];
        days = new ColoredJPanel[lastDay];

        int numWeeks = cal.get (Calendar.WEEK_OF_MONTH);
        if (numWeeks > 5)
        {
            monthDaysPanel.setLayout (new GridLayout (6, 7));
            monthDaysPanel.add (extraDayPanel1);
            monthDaysPanel.add (extraDayPanel2);
            monthDaysPanel.add (extraDayPanel3);
            monthDaysPanel.add (extraDayPanel4);
            monthDaysPanel.add (extraDayPanel5);
            monthDaysPanel.add (extraDayPanel6);
            monthDaysPanel.add (extraDayPanel7);
        }

        cal.set (Calendar.DAY_OF_MONTH, 1);
        int firstDay = cal.get (Calendar.DAY_OF_WEEK);
        int dayNumber = 1;

        date = miniCalendar.getDate ();
        Calendar backCal = Calendar.getInstance ();
        backCal.setTime (date);
        backCal.add (Calendar.MONTH, -1);
        int maxDay = backCal.getActualMaximum (Calendar.DATE);
        int tempLastMonthLast = maxDay;
        backCal.add (Calendar.MONTH, 1);
        backCal.set (Calendar.DAY_OF_MONTH, backCal.getActualMaximum (Calendar.DATE));
        int lastDayOfWeek = backCal.get (Calendar.DAY_OF_WEEK);
        daysOutsideMonth = new JPanel[firstDay - 1 + 7 - lastDayOfWeek];

        int outsideMonthIndex = 0;
        for (int i = firstDay - 2; i >= 0; --i)
        {
            days[i] = (ColoredJPanel) monthDaysPanel.getComponent (i);
            JLabel label = ((JLabel) ((JPanel) monthDaysPanel.getComponent (i)).getComponent (0));
            label.setForeground (domain.utility.currentTheme.colorDayNotInMonthText);
            label.setText (maxDay + "");
            days[i].setBorder (UNSELECTED_DAY_BORDER);
            days[i].setBackground (domain.utility.currentTheme.colorDayNotInMonthBackground1, domain.utility.currentTheme.colorDayNotInMonthBackground2);
            daysOutsideMonth[outsideMonthIndex] = (JPanel) ((JViewport) ((JScrollPane) ((JPanel) monthDaysPanel.getComponent (i)).getComponent (1)).getComponent (0)).getComponent (0);
            ++outsideMonthIndex;
            --maxDay;
        }
        int terminal = lastDay + 1;
        for (int i = dayNumber - 1 + firstDay - 1; dayNumber <= lastDay; ++i, ++dayNumber)
        {
            days[dayNumber - 1] = (ColoredJPanel) monthDaysPanel.getComponent (i);
            days[dayNumber - 1].setBorder (UNSELECTED_DAY_BORDER);
            JLabel label = ((JLabel) ((JPanel) monthDaysPanel.getComponent (i)).getComponent (0));
            label.setForeground (domain.utility.currentTheme.colorDayInMonthText);
            label.setText (dayNumber + "");
            days[dayNumber - 1].setBackground (domain.utility.currentTheme.colorDayInMonthBackground1, domain.utility.currentTheme.colorDayInMonthBackground2);
            daysAssignmentsAndEvents[dayNumber - 1] = (JPanel) ((JViewport) ((JScrollPane) ((JPanel) monthDaysPanel.getComponent (i)).getComponent (1)).getComponent (0)).getComponent (0);
            terminal = i;
        }
        int nextTerminal = terminal;
        for (int i = terminal + 1, j = 1; i < monthDaysPanel.getComponentCount (); ++i, ++j)
        {
            ColoredJPanel monthDayPanel = (ColoredJPanel) monthDaysPanel.getComponent (i);
            monthDayPanel.setBorder (UNSELECTED_DAY_BORDER);
            monthDayPanel.setBackground (domain.utility.currentTheme.colorDayNotInMonthBackground1, domain.utility.currentTheme.colorDayNotInMonthBackground2);
            JLabel label = ((JLabel) monthDayPanel.getComponent (0));
            label.setForeground (domain.utility.currentTheme.colorDayNotInMonthText);
            label.setText (j + "");
            daysOutsideMonth[outsideMonthIndex] = (JPanel) ((JViewport) ((JScrollPane) ((JPanel) monthDaysPanel.getComponent (i)).getComponent (1)).getComponent (0)).getComponent (0);
            ++outsideMonthIndex;
            nextTerminal = j;
        }

        return new int[]
                {
                    firstDay, maxDay + 1, tempLastMonthLast, nextTerminal
                };
    }

    /**
     * Resets the calendar month view to a default, blank template.
     */
    private void resetCalendarMonth()
    {
        extraDay1.removeAll ();
        extraDay1.invalidate ();
        monthDaysPanel.remove (extraDayPanel1);
        extraDay2.removeAll ();
        extraDay2.invalidate ();
        monthDaysPanel.remove (extraDayPanel2);
        extraDay3.removeAll ();
        extraDay3.invalidate ();
        monthDaysPanel.remove (extraDayPanel3);
        extraDay4.removeAll ();
        extraDay4.invalidate ();
        monthDaysPanel.remove (extraDayPanel4);
        extraDay5.removeAll ();
        extraDay5.invalidate ();
        monthDaysPanel.remove (extraDayPanel5);
        extraDay6.removeAll ();
        extraDay6.invalidate ();
        monthDaysPanel.remove (extraDayPanel6);
        extraDay7.removeAll ();
        extraDay7.invalidate ();
        monthDaysPanel.remove (extraDayPanel7);
        monthDaysPanel.setLayout (new GridLayout (5, 7));

        // set all month labels to their default color
        for (int i = 0; i < monthDaysPanel.getComponentCount (); ++i)
        {
            ((JPanel) monthDaysPanel.getComponent (i)).getComponent (0).setForeground (domain.utility.currentTheme.colorDayInMonthText);
        }

        // remove all listeners from assignments and events from day panels
        for (int i = 0; i < shownAssignments.size (); ++i)
        {
            try
            {
                JPanel parent = (JPanel) shownAssignments.get (i).getParent ();
                // if the parent is null, the assignment has already been removed
                if (parent != null)
                {
                    parent.remove (shownAssignments.get (i));
                    parent.invalidate ();
                }
            }
            catch (NullPointerException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        // remove all listeners from assignments and events  from day panels
        for (int i = 0; i < shownEvents.size (); ++i)
        {
            try
            {
                JPanel parent = (JPanel) shownEvents.get (i).getParent ();
                // if the parent is null, the event has already been removed
                if (parent != null)
                {
                    parent.remove (shownEvents.get (i));
                    parent.invalidate ();
                }
            }
            catch (NullPointerException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
    }

    /**
     * Ensure the current assignment is in the filter view, then add it to shown
     * assignments.
     *
     * @param i The index of the assignment.
     * @param assignment A reference to the assignment.
     */
    private void checkAndAddAssignment(int i, Assignment assignment)
    {
        boolean add = true;
        if (!domain.utility.isWithinFilteredScope (i)
            || domain.utility.preferences.filter1Index == 2
            || (domain.utility.preferences.filter2Index == 1 && !assignment.isDone ())
            || (domain.utility.preferences.filter2Index == 2 && assignment.isDone ())
            || (domain.utility.preferences.filter2Index == 3 && !assignment.isOverdue ()))
        {
            add = false;
        }
        if (add)
        {
            shownAssignments.add (assignment);

            if (assignment.getLabel ().getMouseListeners ().length == 0)
            {
                assignment.getLabel ().addMouseListener (new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased(MouseEvent evt)
                    {
                        assignmentMousePressed (evt);
                    }
                });
            }
            if (assignment.getCheckBox ().getItemListeners ().length == 0)
            {
                assignment.getCheckBox ().addItemListener (new ItemListener ()
                {
                    @Override
                    public void itemStateChanged(ItemEvent evt)
                    {
                        assignmentItemStateChanged (evt);
                    }
                });
            }
            DRAG_SOURCE.createDefaultDragGestureRecognizer (assignment.getLabel (), DnDConstants.ACTION_MOVE, DND_LISTENER);
        }
    }

    /**
     * Ensure the current event is in the filter view, then add it to shown
     * assignments.
     *
     * @param i The index of the assignment.
     * @param assignment A reference to the assignment.
     */
    private void checkAndAddEvent(int i, Event event)
    {
        boolean add = true;
        try
        {
            if (!domain.utility.isWithinFilteredScope (i)
                || domain.utility.preferences.filter1Index == 1
                || ((domain.utility.preferences.filter2Index == 1 || domain.utility.preferences.filter2Index == 3) && Domain.DATE_FORMAT.parse (event.getDueDate ()).after (domain.today))
                || (domain.utility.preferences.filter2Index == 2 && Domain.DATE_FORMAT.parse (event.getDueDate ()).before (domain.today)))
            {
                add = false;
            }
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }
        if (add)
        {
            shownEvents.add (event);

            if (event.getMouseListeners ().length == 0)
            {
                event.getLabel ().addMouseListener (new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased(MouseEvent evt)
                    {
                        eventMouseReleased (evt);
                    }
                });
            }
            DRAG_SOURCE.createDefaultDragGestureRecognizer (event.getLabel (), DnDConstants.ACTION_MOVE, DND_LISTENER);
        }
    }

    /**
     * Displays details for the current selected month in the month view panel.
     *
     * @param reselect True if the current assignment or event's panel should be
     * reselected, false otherwise
     * @return The index of the first day of the month.
     */
    protected int loadCalendarView(boolean reselect)
    {
        Calendar cal = Calendar.getInstance ();
        cal.setTime (miniCalendar.getDate ());
        int month = cal.get (Calendar.MONTH);
        int year = cal.get (Calendar.YEAR);
        currentMonthLabel.setText (miniCalendar.getMonthChooser ().getComboBoxObj ().getItemAt (month).toString ());
        currentYearLabel.setText (year + "");
        resetCalendarMonth ();
        shownAssignments.clear ();
        shownEvents.clear ();

        int[] results = constructCalendarForMonth ();
        int firstDay = results[0];
        lastMonthFirst = results[1];
        lastMonthLast = results[2];
        int nextMonthLast = results[3];

        String[] dateSplit = Domain.DATE_FORMAT.format (domain.today).split ("/");
        if (Integer.parseInt (dateSplit[0]) - month - 1 == 0 && Integer.parseInt (dateSplit[2]) - year == 0)
        {
            JLabel label = (JLabel) days[Integer.parseInt (dateSplit[1]) - 1].getComponent (0);
            label.setForeground (domain.utility.currentTheme.colorTodayText);
            label.setText ("<html><b>" + label.getText () + "</b></html>");
        }

        for (int i = 0; i < domain.utility.assignmentsAndEvents.size (); ++i)
        {
            ListItem item = domain.utility.assignmentsAndEvents.get (i);
            item.refreshText ();
            String[] split = item.getDueDate ().split ("/");
            try
            {
                Calendar cal2 = Calendar.getInstance ();
                cal2.setTime (Domain.DATE_FORMAT.parse (item.getDueDate ()));
                int yearDiff = cal2.get (Calendar.YEAR) - miniCalendar.getCalendar ().get (Calendar.YEAR);
                int diff = miniCalendar.getCalendar ().get (Calendar.MONTH) - cal2.get (Calendar.MONTH) - yearDiff * 12;
                if (diff == 0
                    || (diff == -1 && Integer.parseInt (split[1]) <= nextMonthLast)
                    || (diff == 1 && Integer.parseInt (split[1]) >= lastMonthFirst))
                {
                    if (item.isAssignment ())
                    {
                        checkAndAddAssignment (i, (Assignment) item);
                    }
                    else
                    {
                        checkAndAddEvent (i, (Event) item);
                    }
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        // first add all day events
        for (int i = 0; i < shownEvents.size (); ++i)
        {
            Event event = shownEvents.get (i);
            String[] split = event.getDueDate ().split ("/");
            try
            {
                Calendar cal2 = Calendar.getInstance ();
                cal2.setTime (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                int yearDiff = cal2.get (Calendar.YEAR) - miniCalendar.getCalendar ().get (Calendar.YEAR);
                int diff = miniCalendar.getCalendar ().get (Calendar.MONTH) - cal2.get (Calendar.MONTH) - yearDiff * 12;
                if (event.isAllDay ())
                {
                    if (diff == 0)
                    {
                        daysAssignmentsAndEvents[Integer.parseInt (split[1]) - 1].add (event);
                    }
                    else if (diff == 1)
                    {
                        daysOutsideMonth[lastMonthLast - Integer.parseInt (split[1])].add (event);
                    }
                    else if (diff == -1)
                    {
                        daysOutsideMonth[Integer.parseInt (split[1]) + lastMonthLast - lastMonthFirst].add (event);
                    }
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
                shownEvents.remove (event);
            }
        }

        // then add assignments
        for (int i = 0; i < shownAssignments.size (); ++i)
        {
            Assignment assignment = shownAssignments.get (i);
            String[] split = assignment.getDueDate ().split ("/");
            try
            {
                Calendar cal2 = Calendar.getInstance ();
                cal2.setTime (Domain.DATE_FORMAT.parse (assignment.getDueDate ()));
                int yearDiff = cal2.get (Calendar.YEAR) - miniCalendar.getCalendar ().get (Calendar.YEAR);
                int diff = miniCalendar.getCalendar ().get (Calendar.MONTH) - cal2.get (Calendar.MONTH) - yearDiff * 12;
                if (diff == 0)
                {
                    daysAssignmentsAndEvents[Integer.parseInt (split[1]) - 1].add (assignment);
                }
                else if (diff == 1)
                {
                    daysOutsideMonth[lastMonthLast - Integer.parseInt (split[1])].add (assignment);
                }
                else if (diff == -1)
                {
                    daysOutsideMonth[Integer.parseInt (split[1]) + lastMonthLast - lastMonthFirst].add (assignment);
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
                shownAssignments.remove (assignment);
            }
        }

        // then add timed events
        ArrayList<Event> timedEvents = new ArrayList<Event> ();
        for (int i = 0; i < shownEvents.size (); ++i)
        {
            Event event = shownEvents.get (i);
            if (!event.isAllDay ())
            {
                timedEvents.add (event);
            }
        }

        sortEventVectorByTime (timedEvents);

        for (int i = 0; i < timedEvents.size (); ++i)
        {
            Event event = timedEvents.get (i);
            String[] split = event.getDueDate ().split ("/");
            try
            {
                Calendar cal2 = Calendar.getInstance ();
                cal2.setTime (Domain.DATE_FORMAT.parse (event.getDueDate ()));
                int yearDiff = cal2.get (Calendar.YEAR) - miniCalendar.getCalendar ().get (Calendar.YEAR);
                int diff = miniCalendar.getCalendar ().get (Calendar.MONTH) - cal2.get (Calendar.MONTH) - yearDiff * 12;
                if (diff == 0)
                {
                    daysAssignmentsAndEvents[Integer.parseInt (split[1]) - 1].add (event);
                }
                else if (diff == 1)
                {
                    daysOutsideMonth[lastMonthLast - Integer.parseInt (split[1])].add (event);
                }
                else if (diff == -1)
                {
                    daysOutsideMonth[Integer.parseInt (split[1]) + lastMonthLast - lastMonthFirst].add (event);
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            catch (ArrayIndexOutOfBoundsException ex)
            {
                timedEvents.remove (event);
            }
        }

        if (selectedDayPanel != null)
        {
            selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
        }
        selectedDayPanel = days[miniCalendar.getCalendar ().get (Calendar.DAY_OF_MONTH) - 1];
        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);

        if (reselect)
        {
            if (domain.currentIndexFromVector != -1)
            {
                if (selectedDayPanel != null)
                {
                    selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
                }
                String[] split = domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).getDueDate ().split ("/");
                String[] calDate = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                if (split != null)
                {
                    if (Integer.parseInt (split[0]) - Integer.parseInt (calDate[0]) == 0
                        && Integer.parseInt (split[2]) - Integer.parseInt (calDate[2]) == 0)
                    {
                        selectedDayPanel = days[Integer.parseInt (split[1]) - 1];
                        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
                        miniCalendar.getDayChooser ().setDay (Integer.parseInt (split[1]));
                    }
                    else
                    {
                        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
                    }
                }
            }
            else
            {
                String[] split = Domain.DATE_FORMAT.format (domain.today).split ("/");
                String[] calDate = Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/");
                if (Integer.parseInt (split[0]) - Integer.parseInt (calDate[0]) == 0
                    && Integer.parseInt (split[2]) - Integer.parseInt (calDate[2]) == 0)
                {
                    selectedDayPanel = days[Integer.parseInt (Domain.DATE_FORMAT.format (miniCalendar.getDate ()).split ("/")[1]) - 1];
                    selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
                }
            }
        }

        for (int i = 0; i < daysAssignmentsAndEvents.length; ++i)
        {
            daysAssignmentsAndEvents[i].invalidate ();
        }
        for (int i = 0; i < daysOutsideMonth.length; ++i)
        {
            daysOutsideMonth[i].invalidate ();
        }

        refreshBusyDays ();

        return firstDay;
    }

    /**
     * Action when an assignment item is clicked in the Calendar View.
     *
     * @param evt The mouse click event.
     */
    protected void assignmentMousePressed(MouseEvent evt)
    {
        if (selectedDayPanel != null)
        {
            selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
        }
        int index = domain.utility.getAssignmentOrEventIndexByID (((Assignment) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ());
        if (domain.currentIndexFromVector != index)
        {
            assignmentsTable.setSelectedRowFromVectorIndex (domain.utility.getAssignmentOrEventIndexByID (((Assignment) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ()));
        }
        else
        {
            try
            {
                int shownMonth = miniCalendar.getCalendar ().get (Calendar.MONTH) + 1;
                int assnMonth = Integer.parseInt (((Assignment) ((JLabel) evt.getSource ()).getParent ()).getDueDate ().split ("/")[0]);
                if (shownMonth != assnMonth)
                {
                    miniCalendar.setDate (Domain.DATE_FORMAT.parse (((Assignment) ((JLabel) evt.getSource ()).getParent ()).getDueDate ()));
                    loadCalendarView (true);
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        selectedDayPanel = (JPanel) ((Assignment) domain.utility.getAssignmentOrEventByID (((Assignment) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ())).getParent ().getParent ().getParent ().getParent ();
        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        int dayIndex = getIndexFromDaysArray (selectedDayPanel);
        Calendar cal = miniCalendar.getCalendar ();
        cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
        miniCalendar.setDate (cal.getTime ());
        refreshBusyDays ();

        if (evt.getButton () == 2 || evt.getButton () == 3)
        {
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentEditMenuItem.setEnabled (false);
            }
            cloneEditMenuItem.setEnabled (true);
            removeEditMenuItem.setEnabled (true);
            assignmentsEditMenu.show (evt.getComponent (), evt.getPoint ().x, evt.getPoint ().y);
        }

        scrollToItemOrToday ((ListItem) ((JLabel) evt.getSource ()).getParent ());
    }

    /**
     * Action when the assignmentsAndEvents item state is changed in the
     * Calendar View.
     *
     * @param evt The mouse click event.
     */
    protected void assignmentItemStateChanged(ItemEvent evt)
    {
        if (!initLoading && !quitting && !domain.removingAssignmentOrEvent && domain.assignmentOrEventLoading.empty ())
        {
            Assignment assignment = (Assignment) ((JCheckBox) evt.getSource ()).getParent ();
            domain.assignmentOrEventLoading.push (true);
            assignmentsTable.setSelectedRowFromVectorIndex (domain.utility.getAssignmentOrEventIndexByID (((Assignment) ((JCheckBox) evt.getSource ()).getParent ()).getUniqueID ()));
            completedCheckBox.setSelected (assignment.getCheckBox ().isSelected ());
            domain.assignmentOrEventLoading.pop ();
            completedCheckBoxActionPerformed (null);
        }
    }

    /**
     * Action when an event is clicked in the Calendar View.
     *
     * @param evt The mouse click event.
     */
    protected void eventMouseReleased(MouseEvent evt)
    {
        if (selectedDayPanel != null)
        {
            selectedDayPanel.setBorder (UNSELECTED_DAY_BORDER);
        }
        int index = domain.utility.getAssignmentOrEventIndexByID (((Event) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ());
        if (domain.currentIndexFromVector != index)
        {
            assignmentsTable.setSelectedRowFromVectorIndex (domain.utility.getAssignmentOrEventIndexByID (((Event) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ()));
        }
        else
        {
            try
            {
                int shownMonth = miniCalendar.getCalendar ().get (Calendar.MONTH) + 1;
                int assnMonth = Integer.parseInt (((Event) ((JLabel) evt.getSource ()).getParent ()).getDueDate ().split ("/")[0]);
                if (shownMonth != assnMonth)
                {
                    miniCalendar.setDate (Domain.DATE_FORMAT.parse (((Event) ((JLabel) evt.getSource ()).getParent ()).getDueDate ()));
                    loadCalendarView (true);
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        selectedDayPanel = (JPanel) ((Event) domain.utility.getAssignmentOrEventByID (((Event) ((JLabel) evt.getSource ()).getParent ()).getUniqueID ())).getParent ().getParent ().getParent ().getParent ();
        selectedDayPanel.setBorder (SELECTED_DAY_BORDER);
        int dayIndex = getIndexFromDaysArray (selectedDayPanel);
        Calendar cal = miniCalendar.getCalendar ();
        cal.set (Calendar.DAY_OF_MONTH, dayIndex + 1);
        miniCalendar.setDate (cal.getTime ());
        refreshBusyDays ();

        if (evt.getButton () == 2 || evt.getButton () == 3)
        {
            if (domain.utility.courses.size () > 0
                && ((getSelectedCourseIndex () == -1 && getSelectedTermIndex () == -1)
                    || (getSelectedTermIndex () != -1 && domain.utility.terms.get (getSelectedTermIndex ()).getCourseCount () > 0)))
            {
                addAssignmentEditMenuItem.setEnabled (true);
            }
            else
            {
                addAssignmentEditMenuItem.setEnabled (false);
            }
            cloneEditMenuItem.setEnabled (true);
            removeEditMenuItem.setEnabled (true);
            assignmentsEditMenu.show (evt.getComponent (), evt.getPoint ().x, evt.getPoint ().y);
        }
        else
        {
            if (!initLoading && !quitting && !domain.removingAssignmentOrEvent && domain.assignmentOrEventLoading.empty ())
            {
                Event event = (Event) ((JLabel) evt.getSource ()).getParent ();
                domain.assignmentOrEventLoading.push (true);
                assignmentsTable.setSelectedRowFromVectorIndex (domain.utility.getAssignmentOrEventIndexByID (event.getUniqueID ()));
                domain.assignmentOrEventLoading.pop ();
            }
        }

        scrollToItemOrToday ((ListItem) ((JLabel) evt.getSource ()).getParent ());
    }

    /**
     * Searches to see if the current days array contains the specified panel
     * and returns the index of that panel, if found.
     *
     * @param panel The panel to search for.
     * @return The index of the panel. Returns -1 if not found.
     */
    protected int getIndexFromDaysArray(JPanel panel)
    {
        for (int i = 0; i < days.length; ++i)
        {
            if (days[i] == panel)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retrieves the currently selected (directly or indirectly selected through
     * a course) term from the term tree.
     *
     * @return The currently indirectly or directly selected term in the term
     * tree.
     */
    public int getSelectedTermIndex()
    {
        try
        {
            if (termTree.getSelectionRows () != null)
            {
                String[] path = termTree.getSelectionPath ().toString ().split (", ");
                if (path[0].startsWith ("["))
                {
                    path[0] = path[0].substring (1, path[0].length ());
                }
                if (path[path.length - 1].endsWith ("]"))
                {
                    path[path.length - 1] = path[path.length - 1].substring (0, path[path.length - 1].length () - 1);
                }
                for (int i = 0; i < domain.utility.terms.size (); ++i)
                {
                    TreeNode[] nodes = domain.utility.terms.get (i).getPath ();
                    boolean match = true;
                    for (int j = 0; j < nodes.length; ++j)
                    {
                        try
                        {
                            if (!path[j].equals (nodes[j].toString ()))
                            {
                                match = false;
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException ex)
                        {
                            match = false;
                            break;
                        }
                    }
                    if (match)
                    {
                        return i;
                    }
                }
            }
        }
        catch (Exception ex)
        {
            return -1;
        }
        return -1;
    }

    /**
     * Helper method that simply routes to the local add assignment action.
     */
    protected void goAddAssignment()
    {
        addAssignmentMenuItemActionPerformed (null);
    }

    /**
     * Helper method that simply routes to the local add event action.
     */
    protected void goAddEvent()
    {
        addEventMenuItemActionPerformed (null);
    }

    /**
     * Routes immediately to cloning an event.
     */
    protected void goCloneEvent()
    {
        domain.cloneEvent (domain.utility);
    }

    /**
     * Routes immediately to cloning an assignment.
     */
    protected void goCloneAssignment()
    {
        domain.cloneAssignment ();
    }

    /**
     * Removes the currently selected event.
     */
    protected synchronized void goRemoveEvent(Event event)
    {
        if (domain.currentIndexFromVector != -1 || event != null)
        {
            JCheckBox notAgain = null;
            OPTION_PANE.setValue (null);
            if (domain.utility.preferences.rmAlert)
            {
                notAgain = new JCheckBox (domain.language.getString ("dontAskMeAgain"));
                notAgain.setFont (domain.utility.currentTheme.fontPlain12);
                OPTION_PANE.setOptions (YES_NO_CHOICES);
                OPTION_PANE.setMessage (new Object[]
                        {
                            domain.language.getString ("removeEventText"), notAgain
                        });
                OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("removeEvent"));
                optionDialog.setVisible (true);
            }

            if ((OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION) || notAgain == null)
            {
                boolean noRemove = false;

                domain.removingAssignmentOrEvent = true;
                if (notAgain != null)
                {
                    domain.utility.preferences.rmAlert = !notAgain.isSelected ();
                }

                int index = domain.currentIndexFromVector;
                boolean silent = true;
                if (event == null)
                {
                    event = (Event) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector);
                    silent = false;
                }
                int dayIndex = -1;
                int eventIndex = -1;

                if (event.getRepeating ().getID () != -1)
                {
                    domain.utility.repeatingEvents.remove (event);

                    boolean removeAll = false;
                    boolean removeFuture = false;

                    if (!silent)
                    {
                        OPTION_PANE.setOptions (REMOVE_REPEATING_CHOICES);
                        OPTION_PANE.setMessage (domain.language.getString ("removeRepeatingEventText"));
                        OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                        JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("removeRepeatingEvent"));
                        optionDialog.setVisible (true);
                    }
                    else
                    {
                        removeAll = true;
                        removeFuture = true;
                    }

                    if (OPTION_PANE.getValue () != null)
                    {
                        if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == ALL_IN_SERIES_OPTION)
                        {
                            removeAll = true;
                        }
                        else if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == ALL_FOLLOWING_OPTION)
                        {
                            removeFuture = true;
                        }
                        else if (Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.CANCEL_OPTION)
                        {
                            noRemove = true;
                        }
                    }

                    if (!noRemove && (removeAll || removeFuture))
                    {
                        // remove all instances of this event
                        for (int i = domain.utility.repeatingEvents.size () - 1; i >= 0; --i)
                        {
                            Event repEvent = domain.utility.repeatingEvents.get (i);
                            if (repEvent.getRepeating ().getID () == event.getRepeating ().getID ())
                            {
                                try
                                {
                                    if (removeAll
                                        || (removeFuture && Domain.DATE_FORMAT.parse (repEvent.getDueDate ()).after (Domain.DATE_FORMAT.parse (event.getDueDate ()))))
                                    {
                                        if (middleTabbedPane.getSelectedIndex () == 1)
                                        {
                                            JPanel parent = (JPanel) repEvent.getParent ();
                                            if (parent != null)
                                            {
                                                parent.remove (repEvent);
                                                parent.invalidate ();
                                            }
                                            shownEvents.remove (repEvent);
                                        }
                                        assignmentsTableModel.removeRow (domain.utility.getAssignmentOrEventIndexByID (repEvent.getUniqueID ()));
                                        domain.utility.assignmentsAndEvents.remove (repEvent);
                                        domain.utility.repeatingEvents.remove (repEvent);
                                        repEvent.getCategory ().removeEvent (repEvent);
                                        repEvent.getEventYear ().removeEvent (repEvent);
                                        repEvent.getEventYear ().markChanged ();
                                    }
                                }
                                catch (ParseException ex)
                                {
                                    Domain.LOGGER.add (ex);
                                }
                            }
                        }
                    }

                    if (!noRemove)
                    {
                        if (middleTabbedPane.getSelectedIndex () == 1)
                        {
                            dayIndex = Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1;
                            for (int i = 0; i < daysAssignmentsAndEvents[dayIndex].getComponentCount (); ++i)
                            {
                                if (event == daysAssignmentsAndEvents[dayIndex].getComponent (i))
                                {
                                    eventIndex = i;
                                }
                            }
                            daysAssignmentsAndEvents[dayIndex].remove (event);
                            daysAssignmentsAndEvents[dayIndex].invalidate ();
                            shownEvents.remove (event);
                        }
                        assignmentsTableModel.removeRow (domain.utility.getAssignmentOrEventIndexByID (event.getUniqueID ()));
                        domain.utility.assignmentsAndEvents.remove (event.getEventYear ().removeEvent (event));
                        event.getCategory ().removeEvent (event);
                        event.getEventYear ().removeEvent (event);
                        event.getEventYear ().markChanged ();
                    }

                    loadCalendarView (false);
                }
                else
                {
                    if (middleTabbedPane.getSelectedIndex () == 1)
                    {
                        dayIndex = Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1;
                        for (int i = 0; i < daysAssignmentsAndEvents[dayIndex].getComponentCount (); ++i)
                        {
                            if (event == daysAssignmentsAndEvents[dayIndex].getComponent (i))
                            {
                                eventIndex = i;
                            }
                        }
                        daysAssignmentsAndEvents[dayIndex].remove (event);
                        daysAssignmentsAndEvents[dayIndex].invalidate ();
                        shownEvents.remove (event);
                    }

                    assignmentsTableModel.removeRow (domain.utility.getAssignmentOrEventIndexByID (event.getUniqueID ()));
                    domain.utility.assignmentsAndEvents.remove (event);
                    event.getCategory ().removeEvent (event);
                    event.getEventYear ().removeEvent (event);
                }
                assignmentsTable.refreshTable ();

                if (domain.utility.assignmentsAndEvents.isEmpty ())
                {
                    termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
                }

                while (index > assignmentsTableModel.getRowCount () - 1)
                {
                    --index;
                }
                domain.currentIndexFromVector = index;
                if (dayIndex != -1)
                {
                    if (daysAssignmentsAndEvents[dayIndex].getComponentCount () > 1)
                    {
                        if (eventIndex < daysAssignmentsAndEvents[dayIndex].getComponentCount ())
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (eventIndex)).getUniqueID ());
                        }
                        else
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (daysAssignmentsAndEvents[dayIndex].getComponentCount () - 1)).getUniqueID ());
                        }
                    }
                    else
                    {
                        if (daysAssignmentsAndEvents[dayIndex].getComponentCount () == 1)
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (0)).getUniqueID ());
                        }
                        else
                        {
                            index = -1;
                        }
                    }
                }

                assignmentsTable.setSelectedRowFromVectorIndex (index);
                assignmentsTableRowSelected (null);

                checkInstructorButtonState ();

                domain.removingAssignmentOrEvent = false;

                event.getEventYear ().markChanged ();

                refreshBusyDays ();
            }
        }
    }

    /**
     * Removes the currently selected assignment.
     */
    protected synchronized void goRemoveAssignment()
    {
        if (domain.currentIndexFromVector != -1)
        {
            JCheckBox notAgain = null;
            OPTION_PANE.setValue (null);
            if (domain.utility.preferences.rmAlert)
            {
                notAgain = new JCheckBox (domain.language.getString ("dontAskMeAgain"));
                notAgain.setFont (domain.utility.currentTheme.fontPlain12);
                OPTION_PANE.setOptions (YES_NO_CHOICES);
                OPTION_PANE.setMessage (new Object[]
                        {
                            domain.language.getString ("removeAssignmentText"), notAgain
                        });
                OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("removeAssignment"));
                optionDialog.setVisible (true);
            }
            if ((OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION) || notAgain == null)
            {
                domain.removingAssignmentOrEvent = true;

                if (notAgain != null)
                {
                    domain.utility.preferences.rmAlert = !notAgain.isSelected ();
                }

                int index = domain.currentIndexFromVector;
                Assignment assignment = (Assignment) domain.utility.assignmentsAndEvents.get (index);
                int dayIndex = -1;
                int assignmentIndex = -1;
                if (middleTabbedPane.getSelectedIndex () == 1)
                {
                    dayIndex = Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1;
                    for (int i = 0; i < daysAssignmentsAndEvents[dayIndex].getComponentCount (); ++i)
                    {
                        if (assignment == daysAssignmentsAndEvents[dayIndex].getComponent (i))
                        {
                            assignmentIndex = i;
                        }
                    }
                    daysAssignmentsAndEvents[dayIndex].remove (assignment);
                    daysAssignmentsAndEvents[dayIndex].invalidate ();
                    shownAssignments.remove (assignment);
                }
                assignmentsTableModel.removeRow (assignmentsTable.getSelectableRowFromVectorIndex (index));
                domain.utility.assignmentsAndEvents.remove (assignment.getCourse ().removeAssignment (assignment));
                assignmentsTable.refreshTable ();

                if (domain.utility.assignmentsAndEvents.isEmpty ())
                {
                    termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
                }

                try
                {
                    assignmentsTable.getSelectableRowFromVectorIndex (index);
                }
                catch (IndexOutOfBoundsException ex)
                {
                    --index;
                }
                domain.currentIndexFromVector = index;
                if (dayIndex != -1)
                {
                    if (daysAssignmentsAndEvents[dayIndex].getComponentCount () > 1)
                    {
                        if (assignmentIndex < daysAssignmentsAndEvents[dayIndex].getComponentCount ())
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (assignmentIndex)).getUniqueID ());
                        }
                        else
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (daysAssignmentsAndEvents[dayIndex].getComponentCount () - 1)).getUniqueID ());
                        }
                    }
                    else
                    {
                        if (daysAssignmentsAndEvents[dayIndex].getComponentCount () == 1)
                        {
                            index = domain.utility.getAssignmentOrEventIndexByID (((ListItem) daysAssignmentsAndEvents[dayIndex].getComponent (0)).getUniqueID ());
                        }
                        else
                        {
                            index = -1;
                        }
                    }
                }

                assignmentsTable.setSelectedRowFromVectorIndex (index);
                assignmentsTableRowSelected (null);

                checkInstructorButtonState ();

                domain.removingAssignmentOrEvent = false;

                assignment.getCourse ().markChanged ();

                refreshBusyDays ();
            }
        }
    }

    /**
     * Prompt the user for print details and run print.
     */
    protected void printGetOrganized()
    {
        printDialog.goViewPrint ();
    }

    /**
     * Import data from the given backup file.
     */
    protected void importFromBackup()
    {
        importFromBackupDialog.goViewImportFromBackup ();
    }

    /**
     * Asks the instructor, launching an email dialog if an email is specified
     * and the Settings dialog if a phone number is specified.
     */
    protected void goAskInstructor()
    {
        Course course = null;
        if (domain.currentIndexFromVector != -1 && domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).isAssignment ())
        {
            course = ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getCourse ();
        }
        else
        {
            course = domain.utility.courses.get (getSelectedCourseIndex ());
        }
        if (course != null)
        {
            askPopupMenu.removeAll ();
            for (int i = 0; i < course.getInstructorCount (); ++i)
            {
                final Course finalCourse = course;
                final Instructor instructor = course.getInstructor (i);
                final String email = course.getInstructor (i).getTypeName ();
                if (!instructor.getInstructorEmail ().equals (""))
                {
                    String lectureLab = instructor.getLectureLab ();
                    if (lectureLab.equals (domain.language.getString ("both")))
                    {
                        lectureLab = domain.language.getString ("lecture") + "/" + domain.language.getString ("lab");
                    }
                    JMenuItem menuItem = new JMenuItem (domain.language.getString ("email") + " " + instructor.getTypeName () + " (" + lectureLab + ")");
                    menuItem.setFont (domain.utility.currentTheme.fontPlain11);
                    final String title;
                    if (domain.currentIndexFromVector != -1 && domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector).isAssignment ())
                    {
                        title = ((Assignment) domain.utility.assignmentsAndEvents.get (domain.currentIndexFromVector)).getItemName () + " " + domain.language.getString ("assignment");
                    }
                    else
                    {
                        title = course.getTypeName () + " " + course.getCourseNumber ();
                    }
                    menuItem.addActionListener (new ActionListener ()
                    {
                        @Override
                        public void actionPerformed(ActionEvent evt)
                        {
                            if (Domain.desktop != null)
                            {
                                try
                                {
                                    Domain.desktop.mail (new URI ("mailto", email + "?subject=" + domain.language.getString ("questionAbout") + " " + title.replaceAll ("&", " and "), null));
                                }
                                catch (Exception ex)
                                {
                                    termsAndCoursesDialog.goViewTermsAndCourses ();
                                    domain.termsAndCoursesOpening.push (true);
                                    termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
                                    domain.courseLoading.push (true);
                                    termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (finalCourse.getUniqueID (), 2);
                                    domain.instructorLoading.push (true);
                                    termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (1);
                                    termsAndCoursesDialog.settingsInstructorsTable.setSelectedRow (instructor.getUniqueID (), 2);
                                    domain.instructorLoading.pop ();
                                    domain.courseLoading.pop ();
                                    domain.termsAndCoursesOpening.pop ();
                                    termsAndCoursesDialog.instructorEmailTextField.requestFocus ();
                                    termsAndCoursesDialog.instructorEmailTextField.selectAll ();
                                }
                            }
                            else
                            {
                                termsAndCoursesDialog.goViewTermsAndCourses ();
                                domain.termsAndCoursesOpening.push (true);
                                termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
                                domain.courseLoading.push (true);
                                termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (finalCourse.getUniqueID (), 2);
                                domain.instructorLoading.push (true);
                                termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (1);
                                termsAndCoursesDialog.settingsInstructorsTable.setSelectedRow (instructor.getUniqueID (), 2);
                                domain.instructorLoading.pop ();
                                domain.courseLoading.pop ();
                                domain.termsAndCoursesOpening.pop ();
                                termsAndCoursesDialog.instructorEmailTextField.requestFocus ();
                                termsAndCoursesDialog.instructorEmailTextField.selectAll ();
                            }
                        }
                    });
                    askPopupMenu.add (menuItem);
                }
                if (!instructor.getInstructorPhone ().equals (""))
                {
                    String lectureLab = instructor.getLectureLab ();
                    if (lectureLab.equals (domain.language.getString ("both")))
                    {
                        lectureLab = domain.language.getString ("lecture") + "/" + domain.language.getString ("lab");
                    }
                    JMenuItem menuItem = new JMenuItem (domain.language.getString ("call") + " " + instructor.getTypeName () + " (" + lectureLab + ")");
                    menuItem.setFont (domain.utility.currentTheme.fontPlain11);
                    menuItem.addActionListener (new ActionListener ()
                    {
                        @Override
                        public void actionPerformed(ActionEvent evt)
                        {
                            termsAndCoursesDialog.goViewTermsAndCourses ();
                            domain.termsAndCoursesOpening.push (true);
                            termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
                            domain.courseLoading.push (true);
                            termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (finalCourse.getUniqueID (), 2);
                            domain.instructorLoading.push (true);
                            termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (1);
                            termsAndCoursesDialog.settingsInstructorsTable.setSelectedRow (instructor.getUniqueID (), 2);
                            domain.instructorLoading.pop ();
                            domain.courseLoading.pop ();
                            domain.termsAndCoursesOpening.pop ();
                            termsAndCoursesDialog.phoneTextField.requestFocus ();
                            termsAndCoursesDialog.phoneTextField.selectAll ();
                        }
                    });
                    askPopupMenu.add (menuItem);
                }
            }
            askPopupMenu.show (askInstructorButton, 0, askInstructorButton.getHeight ());
        }
    }

    /**
     * Launch the Updates dialog.
     */
    protected void goViewUpdates()
    {
        domain.setProgressState (updatesProgressBar, true, domain.language.getString ("checkingForUpdates") + "...", true, -1);
        updatesCloseButton.setText (domain.language.getString ("cancel"));
        updatesCloseButton.setToolTipText (domain.language.getString ("cancelUpdateToolTip"));
        updatesDialog.pack ();
        updatesDialog.setLocationRelativeTo (this);
        updatesDialog.setVisible (true);

        new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                try
                {
                    setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
                    Object[] status = Updater.checkForUpdates (Domain.VERSION, domain.utility.preferences.updateCheckIndex);
                    setCursor (Cursor.getDefaultCursor ());
                    if (Integer.parseInt (status[0].toString ()) == 1)
                    {
                        domain.setProgressState (updatesProgressBar, false, "", false, -1);

                        String extraMessage = "";
                        if (status[2] != null)
                        {
                            extraMessage = domain.language.getString ("priority") + ": " + status[2].toString ().split ("=")[1] + "\n";
                        }
                        OPTION_PANE.setOptions (OK_CANCEL_CHOICES);
                        OPTION_PANE.setMessage (domain.language.getString ("localVersion") + ": " + Domain.VERSION + "\n" + domain.language.getString ("serverVersion") + ": " + status[1] + "\n" + extraMessage + "\n" + domain.language.getString ("downloadLatestQuestion"));
                        OPTION_PANE.setMessageType (JOptionPane.INFORMATION_MESSAGE);
                        JDialog optionDialog = OPTION_PANE.createDialog (updatesDialog, domain.language.getString ("updateAvailable"));
                        optionDialog.setVisible (true);
                        if (OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.OK_OPTION)
                        {
                            downloadUpdate ();
                        }
                        else
                        {
                            updatesProgressBar.setString (domain.language.getString ("notUpdated"));
                            updatesCloseButton.setText (domain.language.getString ("close"));
                            updatesCloseButton.setToolTipText (domain.language.getString ("closeToolTip"));
                        }
                    }
                    else
                    {
                        if (Integer.parseInt (status[0].toString ()) == 0)
                        {
                            domain.setProgressState (updatesProgressBar, false, domain.language.getString ("noUpdatesAvailable") + "...", true, -1);
                            updatesCloseButton.setText (domain.language.getString ("close"));
                            updatesCloseButton.setToolTipText (domain.language.getString ("closeToolTip"));
                        }
                        else
                        {
                            if (Integer.parseInt (status[0].toString ()) == -1)
                            {
                                domain.setProgressState (updatesProgressBar, false, domain.language.getString ("serverConnectionFailed") + "...", true, -1);
                                updatesCloseButton.setText (domain.language.getString ("close"));
                                updatesCloseButton.setToolTipText (domain.language.getString ("closeToolTip"));
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    Domain.LOGGER.add (ex);
                }
                finally
                {
                    setCursor (Cursor.getDefaultCursor ());
                }
            }
        }).start ();
    }

    /**
     * Launch the online help content.
     */
    protected void goViewHelp()
    {
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("http://alexlaird.com/projects/get-organized/support"));
            }
            catch (Exception ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            OPTION_PANE.setOptions (OK_CHOICE);
            OPTION_PANE.setMessage (domain.language.getString ("helpContentOnlineText"));
            OPTION_PANE.setMessageType (JOptionPane.INFORMATION_MESSAGE);
            JDialog optionDialog = OPTION_PANE.createDialog (updatesDialog, domain.language.getString ("helpContentOnline"));
            optionDialog.setVisible (true);
        }
    }
    
    /**
     * Launch the online donation page.
     */
    protected void goDonate()
    {
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("http://alexlaird.com/projects/get-organized/donate"));
            }
            catch (Exception ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            OPTION_PANE.setOptions (OK_CHOICE);
            OPTION_PANE.setMessage (domain.language.getString ("donateOnlineText"));
            OPTION_PANE.setMessageType (JOptionPane.INFORMATION_MESSAGE);
            JDialog optionDialog = OPTION_PANE.createDialog (updatesDialog, domain.language.getString ("donateOnline"));
            optionDialog.setVisible (true);
        }
    }

    /**
     * Routes to the local add term action.
     */
    protected void goAddTerm()
    {
        addTermMenuItemActionPerformed (null);
    }

    /**
     * Edits types for the currently selected course.
     */
    protected void goEditTypes()
    {
        editCourseEditMenuItemActionPerformed (null);

        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (2);
        if (termsAndCoursesDialog.typeTableModel.getRowCount () > 0)
        {
            termsAndCoursesDialog.settingsTypesTable.getSelectionModel ().setSelectionInterval (0, 0);
        }
    }

    /**
     * Edits types for the currently selected course.
     */
    protected void goEditTypesFromGrades(Course course)
    {
        termsAndCoursesDialog.goViewTermsAndCourses ();
        domain.termsAndCoursesOpening.push (true);
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
        domain.termsAndCoursesOpening.pop ();

        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (course.getUniqueID (), 2);
        termsAndCoursesDialog.settingsInstructorsTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTypesTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTextbooksTable.setSelectedRow (-1);
        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (0);
        termsAndCoursesDialog.showCourseDetails ();

        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (2);
        if (termsAndCoursesDialog.typeTableModel.getRowCount () > 0)
        {
            termsAndCoursesDialog.settingsTypesTable.getSelectionModel ().setSelectionInterval (0, 0);
        }
    }

    /**
     * Edits types for the currently selected course.
     */
    protected void goEditInstructors()
    {
        editCourseEditMenuItemActionPerformed (null);

        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (1);
        if (termsAndCoursesDialog.instructorTableModel.getRowCount () > 0)
        {
            termsAndCoursesDialog.settingsInstructorsTable.getSelectionModel ().setSelectionInterval (0, 0);
        }
    }

    /**
     * Edit categories for events.
     */
    protected void goEditCategories()
    {
        settingsDialog.goViewSettings ();
    }

    /**
     * Edits the textbooks for the currently selected course.
     */
    protected void goEditTextbooks()
    {
        editCourseEditMenuItemActionPerformed (null);

        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (3);
        if (termsAndCoursesDialog.textbookTableModel.getRowCount () > 0)
        {
            termsAndCoursesDialog.settingsTextbooksTable.setSelectedRow (0);
        }
    }

    /**
     * Edits the currently selected term.
     */
    protected void goEditTerm()
    {
        termsAndCoursesDialog.goViewTermsAndCourses ();
        Term term = domain.utility.terms.get (getSelectedTermIndex ());
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (0);
        termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getPath ()));

        termsAndCoursesDialog.settingsTermsTable.setSelectedRow (getSelectedTermIndex ());
        termsAndCoursesDialog.showTermDetails ();
    }

    /**
     * Removes the currently selected term from either right-click on the term
     * or the Terms menu button being clicked.
     */
    protected void goRemoveTerm()
    {
        Term term = domain.utility.terms.get (getSelectedTermIndex ());
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (0);
        termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getPath ()));

        termsAndCoursesDialog.settingsTermsTable.setSelectedRow (getSelectedTermIndex ());
        termsAndCoursesDialog.showTermDetails ();
        termsAndCoursesDialog.removeTermButtonActionPerformed ();

        assignmentsTable.setSelectedRow (-1);
        assignmentsTableRowSelected (null);
        termsAndCoursesDialog.settingsTermsTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTermsTableRowSelected (null);
        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsCoursesTableRowSelected (null);
        domain.currentCourseIndex = -1;
        domain.currentTermIndex = -1;
        domain.currentTextbookIndex = -1;
        domain.currentTypeIndex = -1;

        termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
    }

    /**
     * Routes to the local add course action.
     */
    protected void goAddCourse()
    {
        addCourseMenuItemActionPerformed (null);
    }

    /**
     * Edits the currently selected course.
     */
    protected void goEditCourse()
    {
        termsAndCoursesDialog.goViewTermsAndCourses ();
        Course course = domain.utility.terms.get (getSelectedTermIndex ()).getCourse (getSelectedCourseIndexFrom (getSelectedTermIndex ()));
        domain.termsAndCoursesOpening.push (true);
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
        domain.termsAndCoursesOpening.pop ();
        termTree.getSelectionModel ().setSelectionPath (new TreePath (course.getPath ()));

        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (course.getUniqueID (), 2);
        termsAndCoursesDialog.settingsInstructorsTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTypesTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTextbooksTable.setSelectedRow (-1);
        termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (0);
        termsAndCoursesDialog.showCourseDetails ();
    }

    /**
     * Removes the currently selected course.
     */
    protected void goRemoveCourse()
    {
        Course course = domain.utility.courses.get (getSelectedCourseIndex ());
        Term term = course.getTerm ();
        int courseIndex = term.getCourseIndex (course);
        domain.termsAndCoursesOpening.push (true);
        termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (1);
        domain.termsAndCoursesOpening.pop ();
        termTree.getSelectionModel ().setSelectionPath (new TreePath (course.getPath ()));

        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (getSelectedCourseIndex ());
        termsAndCoursesDialog.showCourseDetails ();
        termsAndCoursesDialog.removeCourseButtonActionPerformed ();

        assignmentsTable.setSelectedRow (-1);
        assignmentsTableRowSelected (null);
        termsAndCoursesDialog.settingsTermsTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsTermsTableRowSelected (null);
        termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (-1);
        termsAndCoursesDialog.settingsCoursesTableRowSelected (null);
        domain.currentCourseIndex = -1;
        domain.currentTermIndex = -1;
        domain.currentTextbookIndex = -1;
        domain.currentTypeIndex = -1;

        if (term.getCourseCount () > 0)
        {
            if (courseIndex == term.getCourseCount ())
            {
                --courseIndex;
            }
            termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getCourse (courseIndex).getPath ()));
        }
        else
        {
            termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getPath ()));
        }
        termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
    }

    /**
     * Filter the assignments list according to preferences and course
     * selections. This function ends up sorting the assignments list AND saving
     * to the data file.
     *
     * @param reselect True if assignments table row should be selected again
     * after filter, false otherwise.
     */
    public void filter(final boolean reselect)
    {
        try
        {
            Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR);

            if (loadingPanel.isVisible ())
            {
                loadingLabel.setText (domain.language.getString ("filtering") + "...");
            }
            domain.setProgressState (progressBar, true, domain.language.getString ("filtering") + "...", true, -1);

            domain.assignmentOrEventLoading.push (true);

            long selectedID = -1;
            if (assignmentsTable.getSelectedRow () != -1)
            {
                selectedID = Long.parseLong (assignmentsTableModel.getValueAt (assignmentsTable.getSelectedRow (), 6).toString ());
            }

            int filter1 = domain.utility.preferences.filter1Index;
            int filter2 = domain.utility.preferences.filter2Index;
            domain.utility.preferences.filter1Index = 0;
            domain.utility.preferences.filter2Index = 0;
            int[] selection = termTree.getSelectionRows ();

            ignoreTreeSelection = true;
            termTree.removeSelectionPath (termTree.getSelectionPath ());
            ignoreTreeSelection = false;

            domain.utility.loadAssignmentsTable (false);
            domain.sortAssignmentsList ();
            if (middleTabbedPane.getSelectedIndex () == 1)
            {
                loadCalendarView (reselect);
            }

            domain.utility.preferences.filter1Index = filter1;
            domain.utility.preferences.filter2Index = filter2;
            ignoreTreeSelection = true;
            termTree.setSelectionRows (selection);
            ignoreTreeSelection = false;
            domain.utility.loadAssignmentsTable (true);
            if (middleTabbedPane.getSelectedIndex () == 1)
            {
                loadCalendarView (reselect);
            }

            if (selectedID != -1)
            {
                assignmentsTable.setSelectedRow (selectedID, 6);
                domain.currentIndexFromVector = assignmentsTable.getVectorIndexFromSelectedRow ();
                assignmentsTableRowSelected (null);
                scrollToItemOrToday (domain.utility.getAssignmentOrEventByID (selectedID));
            }
            else
            {
                domain.currentIndexFromVector = assignmentsTable.getVectorIndexFromSelectedRow ();
            }

            if (loadingPanel.isVisible ())
            {
                loadingLabel.setText (domain.language.getString ("loading") + "...");
                domain.setProgressState (progressBar, true, domain.language.getString ("loading") + "...", true, -1);
            }

            domain.assignmentOrEventLoading.pop ();
        }
        catch (Exception ex)
        {
            Domain.LOGGER.add (ex);
        }
        finally
        {
            setCursor (Cursor.getDefaultCursor ());
            domain.setProgressState (progressBar, false, "", false, -1);
        }
    }

    /**
     * Downloads the update that has been shown to be available.
     */
    private void downloadUpdate()
    {
        domain.setProgressState (updatesProgressBar, true, domain.language.getString ("downloadingUpdater") + "...", true, -1);

        File updaterFile = Updater.getUpdater (domain.utility.getDataFolder ());
        if (updaterFile != null)
        {
            updatesProgressBar.setString (domain.language.getString ("launchingUpdater") + " ...");
            updatesDialog.setEnabled (false);

            // launch the returned file to complete the update
            try
            {
                Runtime.getRuntime ().exec (new String[]
                        {
                            "java", "-jar", updaterFile.getCanonicalPath ()
                        }, null, null);
                quit (true);
            }
            catch (IOException ex)
            {
                try
                {
                    OPTION_PANE.setOptions (OK_CHOICE);
                    OPTION_PANE.setMessage (domain.language.getString ("unableToLaunchUpdaterString") + " " + updaterFile.getCanonicalPath ());
                    OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = OPTION_PANE.createDialog (this, domain.language.getString ("unableToLaunchUpdater"));
                    optionDialog.setVisible (true);
                }
                catch (IOException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
        else
        {
            domain.setProgressState (updatesProgressBar, false, domain.language.getString ("updateFailed") + "...", true, -1);
            updatesCloseButton.setText (domain.language.getString ("close"));
            updatesCloseButton.setToolTipText (domain.language.getString ("closeToolTip"));
        }
    }

    /**
     * Loads preferences pertaining to how the application should load visually.
     *
     * @param syncFrame True if the frame should also be synced, false otherwise
     */
    protected void syncWithPreferences(boolean syncFrame)
    {
        switch (domain.utility.preferences.filter1Index)
        {
            case 0:
            {
                bothFilterRadioButton.setSelected (true);
                break;
            }
            case 1:
            {
                assignmentsFilterRadioButton.setSelected (true);
                break;
            }
            case 2:
            {
                eventsFilterRadioButton.setSelected (true);
                break;
            }
        }
        switch (domain.utility.preferences.filter2Index)
        {
            case 0:
            {
                allFilterRadioButton.setSelected (true);
                break;
            }
            case 1:
            {
                doneFilterRadioButton.setSelected (true);
                break;
            }
            case 2:
            {
                notDoneFilterRadioButton.setSelected (true);
                break;
            }
            case 3:
            {
                overdueFilterRadioButton.setSelected (true);
                break;
            }
        }

        if (syncFrame)
        {
            mainFrame.syncFrame ();
        }
        mainFrame.applyMenuBarFonts ();

        middleTabbedPane.setSelectedIndex (domain.utility.preferences.middleTabbedPaneIndex);
        forceViewLoad = true;
        middleTabbedPaneStateChanged (null);
        forceViewLoad = false;

        assignmentsTableModel.setColumnSorting (domain.utility.preferences.sortIndex);
        assignmentsTableModel.setSortAscending (domain.utility.preferences.sortAscending);

        gettingStartedDialog.dontShowCheckBox.setSelected (domain.utility.preferences.dontShowGettingStarted);
        heliumDialog.dontShowCheckBox.setSelected(domain.utility.preferences.dontShowHelium);

        final ViewPanel viewPanel = this;
        new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                // check for updates if auto update is enabled
                if (domain.utility.preferences.autoUpdate)
                {
                    Object[] status = Updater.checkForUpdates (Domain.VERSION, domain.utility.preferences.updateCheckIndex);
                    if (Integer.parseInt (status[0].toString ()) == 1)
                    {
                        domain.setProgressState (updatesProgressBar, false, "", false, -1);

                        String extraMessage = "";
                        if (status[2] != null)
                        {
                            extraMessage = domain.language.getString ("priority") + ": " + status[2].toString ().split ("=")[1] + "\n";
                        }
                        OPTION_PANE.setOptions (OK_CANCEL_CHOICES);
                        OPTION_PANE.setMessage (domain.language.getString ("localVersion") + ": " + Domain.VERSION + "\n" + domain.language.getString ("serverVersion") + ": " + status[1] + "\n" + extraMessage + "\n" + domain.language.getString ("downloadLatestQuestion"));
                        OPTION_PANE.setMessageType (JOptionPane.INFORMATION_MESSAGE);
                        JDialog optionDialog = OPTION_PANE.createDialog (updatesDialog, domain.language.getString ("updateAvailable"));
                        optionDialog.setVisible (true);
                        if (OPTION_PANE.getValue () != null && Integer.parseInt (OPTION_PANE.getValue ().toString ()) == JOptionPane.OK_OPTION)
                        {
                            updatesDialog.pack ();
                            updatesDialog.setLocationRelativeTo (viewPanel);
                            updatesDialog.setVisible (true);

                            downloadUpdate ();
                        }
                        else
                        {
                            domain.setProgressState (updatesProgressBar, false, domain.language.getString ("notUpdated") + "...", true, -1);
                            updatesCloseButton.setText (domain.language.getString ("close"));
                            updatesCloseButton.setToolTipText (domain.language.getString ("closeToolTip"));
                        }
                    }
                }
            }
        }).start ();
    }

    /**
     * Visit the website for the current course.
     */
    protected void visitCourseWebsite()
    {
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI (domain.utility.courses.get (getSelectedCourseIndex ()).getCourseWebsite ()));
            }
            catch (URISyntaxException ex)
            {
                ViewPanel.OPTION_PANE.setOptions (OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("invalidCourseUrlText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("invalidURL"));
                optionDialog.setVisible (true);
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
    }

    /**
     * Visit the website for the current course's lab.
     */
    protected void visitLabWebsite()
    {
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI (domain.utility.courses.get (getSelectedCourseIndex ()).getLabWebsite ()));
            }
            catch (URISyntaxException ex)
            {
                ViewPanel.OPTION_PANE.setOptions (OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("invalidCourseLabUrlText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("invalidURL"));
                optionDialog.setVisible (true);
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
    }

    /**
     * Apply language settings to all labels for the current resource bundle.
     */
    protected void applyLanguage()
    {
        // menu bar
        mainFrame.applyLanguageToMenuBar ();
        // dialog titles
        aboutDialog.setTitle (domain.language.getString ("about"));
        importFromBackupDialog.setTitle (domain.language.getString ("importFromBackup"));
        printDialog.setTitle (domain.language.getString ("print"));
        settingsDialog.setTitle (domain.language.getString ("settings"));
        termsAndCoursesDialog.setTitle (domain.language.getString ("termsAndCourses"));
        gradesDialog.setTitle (domain.language.getString ("grades"));
        gettingStartedDialog.setTitle (domain.language.getString ("gettingStarted"));
        updatesDialog.setTitle (domain.language.getString ("checkForUpdates"));
        heliumDialog.setTitle("Switch to Helium!");

        // file chooser
        fileChooser.resetChoosableFileFilters ();
        fileChooser.setFileFilter (new ExtensionFileFilter (domain.language.getString ("getOrganizedBackupFiles") + " (.gbak)", new String[]
                {
                    "GBAK"
                }));

        // dialog contents
        aboutDialog.applyLanguage (domain.language);
        gettingStartedDialog.applyLanguage (domain.language);
        heliumDialog.applyLanguage(domain.language);
        importFromBackupDialog.applyLanguage (domain.language);
        printDialog.applyLanguage (domain.language);
        settingsDialog.applyLanguage (domain.language);
        termsAndCoursesDialog.applyLanguage (domain.language);
        gradesDialog.applyLanguage (domain.language);

        // constant buttons
        YES_OPTION_BUTTON.setText (domain.language.getString ("yes"));
        NO_OPTION_BUTTON.setText (domain.language.getString ("no"));
        OK_OPTION_BUTTON.setText (domain.language.getString ("ok"));
        I_UNDERSTAND_OPTION_BUTTON.setText (domain.language.getString ("iUnderstand"));
        CANCEL_OPTION_BUTTON.setText (domain.language.getString ("cancel"));
        ONLY_THIS_INSTANCE_BUTTON.setText (domain.language.getString ("onlyThisInstance"));
        ALL_IN_SERIES_BUTTON.setText (domain.language.getString ("allInSeries"));
        ALL_FOLLOWING_BUTTON.setText (domain.language.getString ("allFollowing"));
        REPLACE_OPTION_BUTTON.setText (domain.language.getString ("replace"));

        // toolbar
        addButton.setText (domain.language.getString ("add"));
        addButton.setToolTipText (domain.language.getString ("addButtonToolTip"));
        cloneButton.setText (domain.language.getString ("clone"));
        cloneButton.setToolTipText (domain.language.getString ("cloneButtonToolTip"));
        removeButton.setText (domain.language.getString ("remove"));
        removeButton.setToolTipText (domain.language.getString ("removeButtonToolTip"));
        askInstructorButton.setText (domain.language.getString ("ask"));
        askInstructorButton.setToolTipText (domain.language.getString ("askInstructorButtonToolTip"));
        viewGradesButton.setText (domain.language.getString ("grades"));
        viewGradesButton.setToolTipText (domain.language.getString ("viewGradesButtonToolTip"));
        termsAndCoursesButton.setText (domain.language.getString ("courses"));
        termsAndCoursesButton.setToolTipText (domain.language.getString ("termsAndCoursesButtonToolTip"));
        settingsButton.setText (domain.language.getString ("settings"));
        settingsButton.setToolTipText (domain.language.getString ("settingsButtonToolTip"));

        // calendars
        ((JTextFieldDateEditor) dueDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("dueDateChooserToolTip"));
        ((JTextFieldDateEditor) eventDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("eventDateChooserToolTip"));
        ((JTextFieldDateEditor) repeatEventEndDateChooser.getDateEditor ()).setToolTipText (domain.language.getString ("repeatEventEndDateChooserToolTip"));
        miniCalendar.getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        miniCalendar.getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        dueDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        dueDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        eventDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        eventDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });
        repeatEventEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    domain.language.getString ("january"), domain.language.getString ("february"), domain.language.getString ("march"), domain.language.getString ("april"), domain.language.getString ("may"), domain.language.getString ("june"), domain.language.getString ("july"), domain.language.getString ("august"), domain.language.getString ("september"), domain.language.getString ("october"), domain.language.getString ("november"), domain.language.getString ("december")
                });
        repeatEventEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    domain.language.getString ("sun"), domain.language.getString ("mon"), domain.language.getString ("tue"), domain.language.getString ("wed"), domain.language.getString ("thu"), domain.language.getString ("fri"), domain.language.getString ("sat")
                });

        // left panel

        // assignment details in right panel

        // event details in right panel

        // term details in right panel

        // course details in right panel

        // user details in right panel

        // filter panel
        assignmentsFilterRadioButton.setText (domain.language.getString ("assignments"));
        eventsFilterRadioButton.setText (domain.language.getString ("events"));
        allFilterRadioButton.setText (domain.language.getString ("all"));
        overdueFilterRadioButton.setText (domain.language.getString ("overdue"));
        doneFilterRadioButton.setText (domain.language.getString ("done"));
        notDoneFilterRadioButton.setText (domain.language.getString ("notDone"));
        assignmentsFilterRadioButton.setToolTipText (domain.language.getString ("assignmentsToolTip"));
        eventsFilterRadioButton.setToolTipText (domain.language.getString ("eventsToolTip"));
        allFilterRadioButton.setToolTipText (domain.language.getString ("allToolTip"));
        overdueFilterRadioButton.setToolTipText (domain.language.getString ("overdueToolTip"));
        doneFilterRadioButton.setToolTipText (domain.language.getString ("doneToolTip"));
        notDoneFilterRadioButton.setToolTipText (domain.language.getString ("notDoneToolTip"));
        // middle loading panel
        loadingLabel.setText (domain.language.getString ("loading") + "...");
        // middle list panel
        assignmentsTable.getColumnModel ().getColumn (1).setHeaderValue (domain.language.getString ("task"));
        assignmentsTable.getColumnModel ().getColumn (2).setHeaderValue (domain.language.getString ("type"));
        assignmentsTable.getColumnModel ().getColumn (3).setHeaderValue (domain.language.getString ("course") + "/" + domain.language.getString ("category"));
        assignmentsTable.getColumnModel ().getColumn (4).setHeaderValue (domain.language.getString ("dueDate"));
        assignmentsTable.getColumnModel ().getColumn (5).setHeaderValue (domain.language.getString ("grade"));
        assignmentsTable.getTableHeader ().resizeAndRepaint ();
        // middle calendar panel
        middleTabbedPane.setTitleAt (0, domain.language.getString ("listView"));
        middleTabbedPane.setTitleAt (1, domain.language.getString ("monthView"));
        sundayLabel.setText (domain.language.getString ("sunday"));
        mondayLabel.setText (domain.language.getString ("monday"));
        tuesdayLabel.setText (domain.language.getString ("tuesday"));
        wednesdayLabel.setText (domain.language.getString ("wednesday"));
        thursdayLabel.setText (domain.language.getString ("thursday"));
        fridayLabel.setText (domain.language.getString ("friday"));
        saturdayLabel.setText (domain.language.getString ("saturday"));
        todayButton.setText (domain.language.getString ("today"));
    }

    /**
     * Stores any errors found in the logger to the log data file, disposes of
     * the application framework, and terminates the application.
     *
     * @param forceQuit True if System.exit should be called, false otherwise.
     */
    protected void quit(boolean forceQuit)
    {
        mainFrame.setVisible (false);
        if (!initLoading)
        {
            quitting = true;

            if (domain.currentIndexFromVector != -1)
            {
                checkAssignmentOrEventChanges (domain.currentIndexFromVector);
                checkRepeatEventChanges (domain.currentIndexFromVector);
            }

            domain.workerThread.stopRunning ();
            domain.needsPreferencesSave = true;
            domain.utility.forceSave ();

            quitting = false;
        }
        if (forceQuit)
        {
            System.exit (0);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addAssignmentEditMenuItem;
    private javax.swing.JMenuItem addAssignmentMenuItem;
    private javax.swing.JMenuItem addAssignmentTermEditMenuItem;
    private javax.swing.JMenuItem addCourseEditMenuItem;
    private javax.swing.JMenuItem addCourseMenuItem;
    private javax.swing.JMenuItem addEventEditMenuItem;
    private javax.swing.JMenuItem addEventMenuItem;
    protected javax.swing.JPopupMenu addPopupMenu;
    private javax.swing.JPopupMenu.Separator addPopupSeparator;
    private javax.swing.JMenuItem addTermEditMenuItem;
    private javax.swing.JMenuItem addTermMenuItem;
    private javax.swing.JLabel advisorDetailsLabel;
    private javax.swing.JCheckBox allDayEventCheckBox;
    private javax.swing.JRadioButton allFilterRadioButton;
    protected javax.swing.JPopupMenu askPopupMenu;
    protected javax.swing.JPanel assignmentContentPanel;
    protected javax.swing.JTextField assignmentNameTextField;
    protected javax.swing.JPopupMenu assignmentsEditMenu;
    private javax.swing.JRadioButton assignmentsFilterRadioButton;
    private javax.swing.JScrollPane assignmentsListScrollPane;
    private javax.swing.JSeparator assignmentsSeparator1;
    private javax.swing.JSeparator assignmentsSeparator2;
    private javax.swing.JSeparator assignmentsSeparator3;
    public adl.go.gui.ExtendedJTable assignmentsTable;
    private javax.swing.JLabel assnColon;
    private javax.swing.JPanel blankContentPanel;
    private javax.swing.JRadioButton bothFilterRadioButton;
    private javax.swing.JLabel boxNumberDetailsLabel;
    protected javax.swing.JComboBox categoryComboBox;
    private javax.swing.JMenuItem cloneEditMenuItem;
    private javax.swing.JLabel commentsLabel;
    private javax.swing.JScrollPane commentsScrollPane;
    private javax.swing.JScrollPane commentsScrollPane1;
    protected javax.swing.JTextArea commentsTextArea;
    protected javax.swing.JCheckBox completedCheckBox;
    private javax.swing.JButton contactAdvisorButton;
    protected adl.go.gui.ColoredJPanel contentPanel;
    private javax.swing.JPanel courseContentPanel;
    private javax.swing.JLabel courseCreditsDetailsLabel;
    private javax.swing.JLabel courseCurrentGradeDetailsLabel;
    private javax.swing.JLabel courseDaysDetailsLabel;
    private javax.swing.JLabel courseEndDateDetailsLabel;
    private javax.swing.JLabel courseEndTimeDetailsLabel;
    private javax.swing.JLabel courseLabel;
    private javax.swing.JLabel courseNameDetailsLabel;
    private javax.swing.JLabel courseRoomDetailsLabel;
    private javax.swing.JLabel courseStartDateDetailsLabel;
    private javax.swing.JLabel courseStartTimeDetailsLabel;
    private javax.swing.JLabel courseTextbooksDetailsLabel;
    private javax.swing.JLabel courseTotalAssignmentsDetailsLabel;
    private javax.swing.JLabel courseTypesDetailsLabel;
    private javax.swing.JLabel courseUnfinishedDetailsLabel;
    private javax.swing.JMenuItem courseWebsiteMenuItem;
    private javax.swing.JLabel currentMonthLabel;
    private javax.swing.JLabel currentYearLabel;
    private javax.swing.JPanel day1;
    private javax.swing.JPanel day10;
    private javax.swing.JPanel day11;
    private javax.swing.JPanel day12;
    private javax.swing.JPanel day13;
    private javax.swing.JPanel day14;
    private javax.swing.JPanel day15;
    private javax.swing.JPanel day16;
    private javax.swing.JPanel day17;
    private javax.swing.JPanel day18;
    private javax.swing.JPanel day19;
    private javax.swing.JPanel day2;
    private javax.swing.JPanel day20;
    private javax.swing.JPanel day21;
    private javax.swing.JPanel day22;
    private javax.swing.JPanel day23;
    private javax.swing.JPanel day24;
    private javax.swing.JPanel day25;
    private javax.swing.JPanel day26;
    private javax.swing.JPanel day27;
    private javax.swing.JPanel day28;
    private javax.swing.JPanel day29;
    private javax.swing.JPanel day3;
    private javax.swing.JPanel day30;
    private javax.swing.JPanel day31;
    private javax.swing.JPanel day32;
    private javax.swing.JPanel day33;
    private javax.swing.JPanel day34;
    private javax.swing.JPanel day35;
    private javax.swing.JPanel day4;
    private javax.swing.JPanel day5;
    private javax.swing.JPanel day6;
    private javax.swing.JPanel day7;
    private javax.swing.JPanel day8;
    private javax.swing.JPanel day9;
    private javax.swing.JLabel dayLabel1;
    private javax.swing.JLabel dayLabel10;
    private javax.swing.JLabel dayLabel11;
    private javax.swing.JLabel dayLabel12;
    private javax.swing.JLabel dayLabel13;
    private javax.swing.JLabel dayLabel14;
    private javax.swing.JLabel dayLabel15;
    private javax.swing.JLabel dayLabel16;
    private javax.swing.JLabel dayLabel17;
    private javax.swing.JLabel dayLabel18;
    private javax.swing.JLabel dayLabel19;
    private javax.swing.JLabel dayLabel2;
    private javax.swing.JLabel dayLabel20;
    private javax.swing.JLabel dayLabel21;
    private javax.swing.JLabel dayLabel22;
    private javax.swing.JLabel dayLabel23;
    private javax.swing.JLabel dayLabel24;
    private javax.swing.JLabel dayLabel25;
    private javax.swing.JLabel dayLabel26;
    private javax.swing.JLabel dayLabel27;
    private javax.swing.JLabel dayLabel28;
    private javax.swing.JLabel dayLabel29;
    private javax.swing.JLabel dayLabel3;
    private javax.swing.JLabel dayLabel30;
    private javax.swing.JLabel dayLabel31;
    private javax.swing.JLabel dayLabel32;
    private javax.swing.JLabel dayLabel33;
    private javax.swing.JLabel dayLabel34;
    private javax.swing.JLabel dayLabel35;
    private javax.swing.JLabel dayLabel4;
    private javax.swing.JLabel dayLabel5;
    private javax.swing.JLabel dayLabel6;
    private javax.swing.JLabel dayLabel7;
    private javax.swing.JLabel dayLabel8;
    private javax.swing.JLabel dayLabel9;
    private javax.swing.JScrollPane dayScroll1;
    private javax.swing.JScrollPane dayScroll10;
    private javax.swing.JScrollPane dayScroll11;
    private javax.swing.JScrollPane dayScroll12;
    private javax.swing.JScrollPane dayScroll13;
    private javax.swing.JScrollPane dayScroll14;
    private javax.swing.JScrollPane dayScroll15;
    private javax.swing.JScrollPane dayScroll16;
    private javax.swing.JScrollPane dayScroll17;
    private javax.swing.JScrollPane dayScroll18;
    private javax.swing.JScrollPane dayScroll19;
    private javax.swing.JScrollPane dayScroll2;
    private javax.swing.JScrollPane dayScroll20;
    private javax.swing.JScrollPane dayScroll21;
    private javax.swing.JScrollPane dayScroll22;
    private javax.swing.JScrollPane dayScroll23;
    private javax.swing.JScrollPane dayScroll24;
    private javax.swing.JScrollPane dayScroll25;
    private javax.swing.JScrollPane dayScroll26;
    private javax.swing.JScrollPane dayScroll27;
    private javax.swing.JScrollPane dayScroll28;
    private javax.swing.JScrollPane dayScroll29;
    private javax.swing.JScrollPane dayScroll3;
    private javax.swing.JScrollPane dayScroll30;
    private javax.swing.JScrollPane dayScroll31;
    private javax.swing.JScrollPane dayScroll32;
    private javax.swing.JScrollPane dayScroll33;
    private javax.swing.JScrollPane dayScroll34;
    private javax.swing.JScrollPane dayScroll35;
    private javax.swing.JScrollPane dayScroll4;
    private javax.swing.JScrollPane dayScroll5;
    private javax.swing.JScrollPane dayScroll6;
    private javax.swing.JScrollPane dayScroll7;
    private javax.swing.JScrollPane dayScroll8;
    private javax.swing.JScrollPane dayScroll9;
    protected javax.swing.JPanel daysOfWeekPanel;
    private javax.swing.JLabel descriptionLabel;
    protected javax.swing.JTextArea descriptionTextArea;
    protected javax.swing.JComboBox detailsCourseComboBox;
    protected javax.swing.JComboBox detailsTextbookComboBox;
    protected javax.swing.JComboBox detailsTypeComboBox;
    private javax.swing.JRadioButton doneFilterRadioButton;
    protected com.toedter.calendar.JDateChooser dueDateChooser;
    private com.toedter.calendar.JSpinnerDateEditor dueHrChooser;
    private com.toedter.calendar.JSpinnerDateEditor dueMChooser;
    private com.toedter.calendar.JSpinnerDateEditor dueMinChooser;
    private javax.swing.JMenuItem editCourseEditMenuItem;
    private javax.swing.JMenuItem editInstructorsEditMenuItem;
    private javax.swing.JMenuItem editTermEditMenuItem;
    private javax.swing.JMenuItem editTextbooksEditMenuItem;
    private javax.swing.JMenuItem editTypesEditMenuItem;
    private javax.swing.JButton editUserDetailsButton;
    private javax.swing.JLabel eventColon1;
    private javax.swing.JLabel eventColon2;
    protected javax.swing.JPanel eventContentPanel;
    protected com.toedter.calendar.JDateChooser eventDateChooser;
    private com.toedter.calendar.JSpinnerDateEditor eventEndHrChooser;
    private com.toedter.calendar.JSpinnerDateEditor eventEndMChooser;
    private com.toedter.calendar.JSpinnerDateEditor eventEndMinChooser;
    protected javax.swing.JTextField eventNameTextField;
    private javax.swing.JButton eventRepeatButton;
    private com.toedter.calendar.JSpinnerDateEditor eventStartHrChooser;
    private com.toedter.calendar.JSpinnerDateEditor eventStartMChooser;
    private com.toedter.calendar.JSpinnerDateEditor eventStartMinChooser;
    private javax.swing.JRadioButton eventsFilterRadioButton;
    private javax.swing.JSeparator eventsSeparator1;
    private javax.swing.JSeparator eventsSeparator2;
    private javax.swing.JLabel everyDescriptionLabel;
    protected javax.swing.JFileChooser fileChooser;
    private javax.swing.ButtonGroup filter1ButtonGroup;
    private javax.swing.ButtonGroup filter2ButtonGroup;
    private javax.swing.JLabel fridayLabel;
    protected javax.swing.JButton googleMapsButton;
    private javax.swing.JLabel gradeLabel;
    protected javax.swing.JTextField gradeTextField;
    private javax.swing.JLabel idNumberDetailsLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JMenuItem labWebsiteMenuItem;
    protected adl.go.gui.ColoredJPanel leftPanel;
    protected adl.go.gui.ColoredJPanel listViewPanel;
    protected javax.swing.JLabel loadingLabel;
    protected adl.go.gui.ColoredJPanel loadingPanel;
    private javax.swing.JLabel locationLabel;
    protected javax.swing.JTextField locationTextField;
    private javax.swing.JPanel middlePanel;
    private javax.swing.JPanel middlePanelForTabs;
    public javax.swing.JTabbedPane middleTabbedPane;
    protected com.toedter.calendar.JCalendar miniCalendar;
    private javax.swing.JLabel mondayLabel;
    protected javax.swing.JPanel monthAndYearPanel;
    protected javax.swing.JPanel monthDaysPanel;
    protected adl.go.gui.ColoredJPanel monthViewPanel;
    private javax.swing.JLabel nextMonthButton;
    private javax.swing.JLabel noStudentAdvisorDetails;
    private javax.swing.JPanel noUserDetailsPanel;
    private javax.swing.JRadioButton notDoneFilterRadioButton;
    private javax.swing.JLabel officeHoursDetailsLabel;
    private javax.swing.JLabel officeLocationDetailsLabel;
    private javax.swing.JRadioButton overdueFilterRadioButton;
    private javax.swing.JLabel prevMonthButton;
    protected javax.swing.JDialog printingDialog;
    private javax.swing.JLabel printingJLabel;
    protected adl.go.gui.ColoredJPanel printingJPanel;
    private javax.swing.JLabel priorityLabel;
    private javax.swing.JSlider prioritySlider;
    public javax.swing.JProgressBar progressBar;
    private javax.swing.JCheckBox reFriCheckBox;
    private javax.swing.JCheckBox reMonCheckBox;
    private javax.swing.JCheckBox reSatCheckBox;
    private javax.swing.JCheckBox reSunCheckBox;
    private javax.swing.JCheckBox reThuCheckBox;
    private javax.swing.JCheckBox reTueCheckBox;
    private javax.swing.JCheckBox reWedCheckBox;
    private javax.swing.JMenuItem removeCourseEditMenuItem;
    protected javax.swing.JMenuItem removeEditMenuItem;
    private javax.swing.JMenuItem removeTermEditMenuItem;
    protected adl.go.gui.ColoredJPanel repeatDialogPanel;
    protected javax.swing.JDialog repeatEventDialog;
    private javax.swing.JButton repeatEventDoneButton;
    protected com.toedter.calendar.JDateChooser repeatEventEndDateChooser;
    protected javax.swing.JComboBox repeatEventRepeatsComboBox;
    protected javax.swing.JComboBox repeatEventRepeatsEveryComboBox;
    private javax.swing.JLabel repeatsEndingLabel;
    private javax.swing.JLabel repeatsEveryLabel;
    private javax.swing.JLabel repeatsLabel;
    private javax.swing.JLabel repeatsOnLabel;
    protected adl.go.gui.ColoredJPanel rightPanel;
    private javax.swing.JLabel saturdayLabel;
    private javax.swing.JLabel schoolDetailsLabel;
    protected adl.go.gui.ColoredJPanel statusPanel;
    private javax.swing.JLabel sundayLabel;
    private javax.swing.JLabel termAvgGradeDetailsLabel;
    private javax.swing.JPanel termContentPanel;
    private javax.swing.JLabel termCoursesDetailsLabel;
    private javax.swing.JLabel termCreditsDetailsLabel;
    protected javax.swing.JPopupMenu termEditMenu;
    private javax.swing.JLabel termEndDateDetailsLabel;
    private javax.swing.JLabel termNameDetailsLabel;
    private javax.swing.JScrollPane termScrollPane;
    private javax.swing.JLabel termStartDateDetailsLabel;
    private javax.swing.JLabel termTextbooksDetailsLabel;
    private javax.swing.JLabel termTotalAssignmentsDetailsLabel;
    public javax.swing.JTree termTree;
    private javax.swing.JLabel termTypesDetailsLabel;
    private javax.swing.JLabel termUnfinishedDetailsLabel;
    private javax.swing.JLabel textbookLabel;
    private javax.swing.JLabel thursdayLabel;
    private javax.swing.JLabel todayButton;
    protected adl.go.gui.ColoredJToolBar toolBar;
    private javax.swing.JLabel tuesdayLabel;
    private javax.swing.JLabel typeLabel;
    protected javax.swing.JButton updatesCloseButton;
    protected javax.swing.JDialog updatesDialog;
    protected adl.go.gui.ColoredJPanel updatesJPanel;
    public javax.swing.JProgressBar updatesProgressBar;
    private javax.swing.JPanel upperPanelForFilters;
    private javax.swing.JPanel userDetailsContentPanel;
    private javax.swing.JLabel userNameDetailsLabel;
    private javax.swing.JLabel wednesdayLabel;
    protected adl.go.gui.ColoredJPanel week1Day1;
    protected adl.go.gui.ColoredJPanel week1Day2;
    protected adl.go.gui.ColoredJPanel week1Day3;
    protected adl.go.gui.ColoredJPanel week1Day4;
    protected adl.go.gui.ColoredJPanel week1Day5;
    protected adl.go.gui.ColoredJPanel week1Day6;
    protected adl.go.gui.ColoredJPanel week1Day7;
    protected adl.go.gui.ColoredJPanel week2Day1;
    protected adl.go.gui.ColoredJPanel week2Day2;
    protected adl.go.gui.ColoredJPanel week2Day3;
    protected adl.go.gui.ColoredJPanel week2Day4;
    protected adl.go.gui.ColoredJPanel week2Day5;
    protected adl.go.gui.ColoredJPanel week2Day6;
    protected adl.go.gui.ColoredJPanel week2Day7;
    protected adl.go.gui.ColoredJPanel week3Day1;
    protected adl.go.gui.ColoredJPanel week3Day2;
    protected adl.go.gui.ColoredJPanel week3Day3;
    protected adl.go.gui.ColoredJPanel week3Day4;
    protected adl.go.gui.ColoredJPanel week3Day5;
    protected adl.go.gui.ColoredJPanel week3Day6;
    protected adl.go.gui.ColoredJPanel week3Day7;
    protected adl.go.gui.ColoredJPanel week4Day1;
    protected adl.go.gui.ColoredJPanel week4Day2;
    protected adl.go.gui.ColoredJPanel week4Day3;
    protected adl.go.gui.ColoredJPanel week4Day4;
    protected adl.go.gui.ColoredJPanel week4Day5;
    protected adl.go.gui.ColoredJPanel week4Day6;
    protected adl.go.gui.ColoredJPanel week4Day7;
    protected adl.go.gui.ColoredJPanel week5Day1;
    protected adl.go.gui.ColoredJPanel week5Day2;
    protected adl.go.gui.ColoredJPanel week5Day3;
    protected adl.go.gui.ColoredJPanel week5Day4;
    protected adl.go.gui.ColoredJPanel week5Day5;
    protected adl.go.gui.ColoredJPanel week5Day6;
    protected adl.go.gui.ColoredJPanel week5Day7;
    protected adl.go.gui.ColoredJPanel weekViewPanel;
    // End of variables declaration//GEN-END:variables
}
