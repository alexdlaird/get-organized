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

import adl.go.resource.LocalUtility;
import adl.go.resource.WorkerThread;
import adl.go.types.Assignment;
import adl.go.types.AssignmentType;
import adl.go.types.Category;
import adl.go.types.Course;
import adl.go.types.Event;
import adl.go.types.EventYear;
import adl.go.types.Instructor;
import adl.go.types.ListItem;
import adl.go.types.Term;
import adl.go.types.Textbook;
import java.awt.Desktop;
import java.awt.dnd.DnDConstants;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * The domain handles general operations specific to the GUI--it is essentially
 * an abstraction method to get excessive code out of the view's class.
 *
 * @author Alex Laird
 */
public class Domain
{
    /**
     * The name of the application.
     */
    public static final String NAME = "Get Organized";
    /**
     * The version number, which is used in the JFrame title, the About dialog
     * and when checking for updates.
     */
    public static final String VERSION = "1.08";
    /**
     * The current working directory.
     */
    public static final String CWD = System.getProperty ("user.dir");
    /**
     * The user's Domain.HOME directory.
     */
    public static final String HOME = System.getProperty ("user.home", ".");
    /**
     * The location of the source.
     */
    public static final String SOURCE_PATH = Domain.class.getProtectionDomain ().getCodeSource ().getLocation ().toString ().replaceAll ("%20", " ");
    /**
     * The default images folder within the source.
     */
    public static final String IMAGES_FOLDER = "/adl/go/images/";
    /**
     * The name of the operating system being used.
     */
    public static final String OS_NAME = System.getProperty ("os.name");
    /**
     * Represents a patterned regular expression.
     */
    public static final Pattern EMAIL_PATTERN = Pattern.compile ("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    /**
     * Display for comma-separated big numbers.
     */
    public static final NumberFormat NUM_FORMAT = new DecimalFormat ("###,###");
    /**
     * The hour formatter.
     */
    public static final SimpleDateFormat HR_FORMAT = new SimpleDateFormat ("h");
    /**
     * The minute formatter.
     */
    public static final SimpleDateFormat MIN_FORMAT = new SimpleDateFormat ("mm");
    /**
     * The meridian formatter.
     */
    public static final SimpleDateFormat M_FORMAT = new SimpleDateFormat ("a");
    /**
     * The time formatter.
     */
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat ("h:mm a");
    /**
     * The date formatter.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat ("MM/dd/yyyy");
    /**
     * The date formatter excluding day.
     */
    public static final SimpleDateFormat MONTH_YEAR_FORMAT = new SimpleDateFormat ("MM/yyyy");
    /**
     * The date and time formatter.
     */
    public static final SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat ("MM/dd/yyyy h:mm a");
    /**
     * The full time format for debug output.
     */
    public static final SimpleDateFormat DATE_AND_FULL_TIME_FORMAT = new SimpleDateFormat ("MM/dd/yyyy h:mm:ss a");
    /**
     * The number formatted for grade percentages.
     */
    public static final NumberFormat PERCENT_FORMAT = new DecimalFormat ("0.00%");
    /**
     * A reference to the utility, which handles file I/O for the application.
     */
    public LocalUtility utility;
    /**
     * The strings for the current language.
     */
    public ResourceBundle language = ResourceBundle.getBundle ("adl.go.resource.languages.bundle_en");
    /**
     * Today's date.
     */
    public Date today = new Date ();
    /**
     * The error file that keeps track of all errors and their occurrences.
     */
    public File logFile;
    /**
     * The logger captures all errors and saves their messages away for possible
     * bug submission or storage in a file.
     */
    public static ArrayList<Object> LOGGER = new ArrayList<Object> ();
    /**
     * A reference to the desktop model (if supported) for launching files and application.
     */
    public static Desktop desktop;
    /**
     * The load/save thread that is continually running unless explicitly paused
     * or stopped.
     */
    public WorkerThread workerThread;
    /**
     * True if type details are being loaded/unloaded.
     */
    public Stack<Boolean> typeLoading = new Stack<Boolean> ();
    /**
     * True if instructor details are being loaded/unloaded.
     */
    public Stack<Boolean> instructorLoading = new Stack<Boolean> ();
    /**
     * True if textbook details are being loaded/unloaded.
     */
    public Stack<Boolean> textbookLoading = new Stack<Boolean> ();
    /**
     * True if assignment details are being loaded/unloaded.
     */
    public Stack<Boolean> assignmentOrEventLoading = new Stack<Boolean> ();
    /**
     * True if term details are being loaded/unloaded.
     */
    protected Stack<Boolean> termLoading = new Stack<Boolean> ();
    /**
     * True if course details are being loaded/unloaded.
     */
    protected Stack<Boolean> courseLoading = new Stack<Boolean> ();
    /**
     * True when a category is being loaded/unloaded.
     */
    protected Stack<Boolean> categoryLoading = new Stack<Boolean> ();
    /**
     * True if the preferences dialog is being opened, false otherwise.
     */
    protected Stack<Boolean> settingsOpening = new Stack<Boolean> ();
    /**
     * True if the terms and courses dialog is being opened, false otherwise.
     */
    protected Stack<Boolean> termsAndCoursesOpening = new Stack<Boolean> ();
    /**
     * A reference to the main frame of the application.
     */
    private ViewPanel viewPanel;
    /**
     * The tracking variable that gets set when a change is made to the preferences.
     */
    public boolean needsCoursesAndTermsSave = false;
    /**
     * Set true when the program is quitting and needs to save user details.
     */
    public boolean needsUserDetailsSave = false;
    /**
     * Set true when the program is quitting and needs to save preferences.
     */
    public boolean needsPreferencesSave = false;
    /**
     * The boolean to be set when a key is pressed, marking a change and that a
     * save should happen.
     */
    public boolean needsSettingsSaveBool = false;
    /**
     * The currently selected course index in the Settings dialog.
     */
    public int currentCourseIndex = -1;
    /**
     * The currently selected type in the Settings dialog.
     */
    public int currentTypeIndex = -1;
    /**
     * The currently selected instructor in the Settings dialog.
     */
    public int currentInstructorIndex = -1;
    /**
     * The currently selected textbook in the Settings dialog.
     */
    public int currentTextbookIndex = -1;
    /**
     * The currently selected category index.
     */
    public int currentCategoryIndex = -1;
    /**
     * The currently selected assignment's or event's index.
     */
    protected int currentIndexFromVector = -1;
    /**
     * The currently selected term index in the Settings dialog.
     */
    protected int currentTermIndex = -1;
    /**
     * True if an assignment or event is being removed from the
     * assignmentsAndEvents list.
     */
    protected boolean removingAssignmentOrEvent = false;

    /**
     * Constructs the domain for the application's interface.
     *
     * @param viewPanel A reference to the main frame of the application.
     */
    public Domain(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;

        // if the Desktop object is supported, get the reference
        if (Desktop.isDesktopSupported ())
        {
            desktop = Desktop.getDesktop ();
        }

        // instantiate the save thread
        workerThread = new WorkerThread (viewPanel, this);
    }

    /**
     * Pass in the constructed utility.
     *
     * @param utility A reference to the utility.
     */
    protected void setUtility(LocalUtility utility)
    {
        this.utility = utility;
    }

    /**
     * Saves the user's preferences to the preferences file.
     */
    public synchronized void savePreferences()
    {
        utility.preferences.width = -1;
        utility.preferences.height = -1;
        if (viewPanel.mainFrame != null)
        {
            utility.preferences.width = viewPanel.mainFrame.getWidth ();
            utility.preferences.height = viewPanel.mainFrame.getHeight ();
        }
        utility.preferences.x = -1;
        utility.preferences.y = -1;
        if (viewPanel.mainFrame != null)
        {
            utility.preferences.x = viewPanel.mainFrame.getX ();
            utility.preferences.y = viewPanel.mainFrame.getY ();
        }

        utility.savePreferences ();

        needsPreferencesSave = false;
    }

    /**
     * Saves the user details to the data file.
     */
    public synchronized void saveUserDetails()
    {
        utility.saveUserDetails ();

        needsUserDetailsSave = false;
    }

    /**
     * Updates the displayed values in the term tree (since they may be changed
     * from the Settings dialog), then saves all current data in the vectors to
     * the data files. This method also ensures that the term tree is fully
     * expanded.
     */
    public synchronized void saveSettings()
    {
        int[] selection = viewPanel.termTree.getSelectionRows ();

        viewPanel.root.removeAllChildren ();
        // add the terms to the term tree (course nodes are already attached
        // to their terms)
        for (int i = 0; i < utility.terms.size (); ++i)
        {
            viewPanel.root.add (utility.terms.get (i));
        }
        refreshTermTree ();

        viewPanel.termTree.setSelectionRows (selection);

        utility.saveTerms ();
        utility.saveCourses ();
        utility.saveTypes ();
        utility.saveInstructors ();
        utility.saveTextbooks ();
        viewPanel.expandTermTree (new TreeExpansionEvent (this, null));
    }

    /**
     * Saves changes made to the currently selected assignment to the data file.
     *
     * @param course
     */
    public synchronized void saveAssignments(Course course)
    {
        utility.saveAssignments (course);
        course.saved ();
    }

    /**
     * Saves changes made to the currently selected event to the data file.
     */
    public synchronized void saveEvents(EventYear eventYear)
    {
        utility.saveEvents (eventYear);
        eventYear.saved ();
    }

    /**
     * Retrieves the index (relative to the assignmentsAndEvents table) of the
     * currently selected assignment or event.
     *
     * @return The index of the currently selected assignment or event from the
     * assignmentsAndEvents table.
     */
    public int getSelectedTableIndex()
    {
        return currentIndexFromVector;
    }

    /**
     * Adds a new, empty term to the list of terms in the Settings dialog. This
     * method also launches the Settings dialog (if not already open) to allow
     * the user to specify additional attributes to the term. The term is not
     * actually added to the term tree until the user saves the changes made in
     * the Settings dialog.
     */
    protected synchronized void addTerm()
    {
        if (currentTermIndex != -1)
        {
            viewPanel.termsAndCoursesDialog.checkTermsAndCoursesChanges (viewPanel.termsAndCoursesDialog.settingsTermsTable.getSelectedRow (), 0);
        }
        termLoading.push (true);

        int index = 0;
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.termTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (viewPanel.termsAndCoursesDialog.termTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newTerm")))
                {
                    ++index;
                }
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.termTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newTerm") + " " + index))
                {
                    ++index;
                }
            }
        }
        String termName = language.getString ("newTerm");
        if (index != 0)
        {
            termName += " " + index;
        }
        Term newTerm = new Term (termName, System.currentTimeMillis (), utility);
        utility.terms.add (newTerm);
        viewPanel.root.add (newTerm);
        viewPanel.termsAndCoursesDialog.termTableModel.addRow (new Object[]
                {
                    termName, newTerm.getUniqueID ()
                });
        viewPanel.termsAndCoursesDialog.settingsTermsTable.refreshTable ();
        viewPanel.termsAndCoursesDialog.settingsTermsTable.setSelectedRow (newTerm.getUniqueID (), 1);
        viewPanel.termsAndCoursesDialog.settingsTermsTable.scrollRectToVisible (viewPanel.termsAndCoursesDialog.settingsTermsTable.getCellRect (viewPanel.termsAndCoursesDialog.termTableModel.getRowCount () - 1, 0, false));
        viewPanel.termsAndCoursesDialog.removeTermButton.setEnabled (true);
        refreshTermTree ();
        viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (newTerm.getPath ()));
        viewPanel.enableCourseButtons ();
        viewPanel.termsAndCoursesDialog.showTermDetails ();

        needsCoursesAndTermsSave = true;

        termLoading.pop ();
    }

    /**
     * Adds a new, empty course to the list of course in the Settings dialog.
     * This course has a reference to the currently selected term in the term
     * tree. This method also launches the Settings dialog (if not already open)
     * to allow the user to specify additional attributes to the course. The
     * course is not actually added to the term tree until the user saves the
     * changes made in the Settings dialog.
     */
    protected synchronized void addCourse()
    {
        if (currentCourseIndex != -1)
        {
            viewPanel.termsAndCoursesDialog.checkTermsAndCoursesChanges (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow (), 1);
        }
        courseLoading.push (true);

        viewPanel.termsAndCoursesDialog.termsComboBox.setEnabled (true);
        utility.loadSettingsTermCombo ();
        int termIndex = viewPanel.getSelectedTermIndex ();
        if (termIndex == -1)
        {
            viewPanel.findTermWithin ();
            termIndex = viewPanel.getSelectedTermIndex ();
        }
        Term term = utility.terms.get (termIndex);

        int index = 0;
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.courseTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (viewPanel.termsAndCoursesDialog.courseTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newCourse")))
                {
                    ++index;
                }
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.courseTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newCourse") + " " + index))
                {
                    ++index;
                }
            }
        }
        String courseName = language.getString ("newCourse");
        if (index != 0)
        {
            courseName += " " + index;
        }
        Course newCourse = new Course (courseName, System.currentTimeMillis (), term, utility);
        int courseIndex = findCourseIndex (newCourse);
        utility.courses.add (newCourse);
        term.addCourse (newCourse);
        ((ExtendedSettingsTableModel) viewPanel.termsAndCoursesDialog.courseTableModel).insertRowAt (courseIndex, new Object[]
                {
                    courseName, newCourse.getTerm ().getTypeName (), newCourse.getUniqueID ()
                });
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.refreshTable ();
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (newCourse.getUniqueID (), 2);
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.scrollRectToVisible (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getCellRect (viewPanel.termsAndCoursesDialog.courseTableModel.getRowCount () - 1, 0, false));
        viewPanel.termsAndCoursesDialog.removeCourseButton.setEnabled (true);
        viewPanel.expandTermTree (new TreeExpansionEvent (this, null));
        refreshTermTree ();
        viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (newCourse.getPath ()));
        viewPanel.termsAndCoursesDialog.courseScrollPane.scrollRectToVisible (viewPanel.termsAndCoursesDialog.courseNameTextField.getBounds ());

        viewPanel.enableAssignmentButtons ();
        viewPanel.termsAndCoursesDialog.showCourseDetails ();
        currentTypeIndex = -1;
        currentTextbookIndex = -1;
        currentInstructorIndex = -1;
        needsCoursesAndTermsSave = true;

        courseLoading.pop ();
    }

    /**
     * Retrieve the index in the courses list where this course should be added
     * relative to its parent term.
     *
     * @param course The course to add.
     * @return The index for where to add it.
     */
    protected int findCourseIndex(Course course)
    {
        for (int i = utility.courses.size () - 1; i >= 0; --i)
        {
            if (course != utility.courses.get (i)
                && utility.courses.get (i).getTerm () == course.getTerm ())
            {
                return i;
            }
        }
        return utility.courses.size ();
    }

    /**
     * Adds a new, empty textbook to the list of textbooks in the Settings
     * dialog. This textbook has a reference to the currently selected course in
     * the term tree. This method also launches the Settings dialog (if not
     * already open) to allow the user to specify additional attributes to the
     * textbook.
     */
    protected synchronized void addTextbook()
    {
        if (currentTextbookIndex != -1)
        {
            viewPanel.termsAndCoursesDialog.checkTermsAndCoursesChanges (currentTextbookIndex, 3);
        }
        textbookLoading.push (true);

        int courseIndex = viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ();
        Course course = utility.courses.get (courseIndex);

        int index = 0;
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.textbookTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (viewPanel.termsAndCoursesDialog.textbookTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newTextbook")))
                {
                    ++index;
                }
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.textbookTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newTextbook") + " " + index))
                {
                    ++index;
                }
            }
        }
        String textbookName = language.getString ("newTextbook");
        if (index != 0)
        {
            textbookName += " " + index;
        }
        Textbook newTextbook = new Textbook (textbookName, System.currentTimeMillis (), course, utility);
        utility.textbooks.add (newTextbook);
        course.addTextbook (newTextbook);
        viewPanel.termsAndCoursesDialog.textbookTableModel.addRow (new Object[]
                {
                    textbookName, newTextbook.getUniqueID ()
                });
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.refreshTable ();
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.setSelectedRow (newTextbook.getUniqueID (), 1);
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.scrollRectToVisible (viewPanel.termsAndCoursesDialog.settingsTextbooksTable.getCellRect (viewPanel.termsAndCoursesDialog.textbookTableModel.getRowCount () - 1, 0, false));
        viewPanel.termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (3);
        viewPanel.termsAndCoursesDialog.removeTextbookButton.setEnabled (true);
        needsCoursesAndTermsSave = true;

        viewPanel.termsAndCoursesDialog.showTextbookDetails ();

        textbookLoading.pop ();
    }

    /**
     * Adds a new, empty type to the list of types in the Settings dialog. This
     * type has a reference to the currently selected course in the term tree.
     * This method also launches the Settings dialog (if not already open) to
     * allow the user to specify additional attributes to the type.
     */
    protected synchronized void addType()
    {
        if (currentTypeIndex != -1)
        {
            viewPanel.termsAndCoursesDialog.checkTermsAndCoursesChanges (currentTypeIndex, 2);
        }
        typeLoading.push (true);

        int courseIndex = viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ();
        Course course = utility.courses.get (courseIndex);

        int index = 0;
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.typeTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (viewPanel.termsAndCoursesDialog.typeTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newType")))
                {
                    ++index;
                }
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.typeTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newType") + " " + index))
                {
                    ++index;
                }
            }
        }
        String typeName = language.getString ("newType");
        if (index != 0)
        {
            typeName += " " + index;
        }
        AssignmentType newType = new AssignmentType (typeName, System.currentTimeMillis (), course, utility);
        utility.types.add (newType);
        course.addType (newType);
        viewPanel.termsAndCoursesDialog.typeTableModel.addRow (new Object[]
                {
                    typeName, "", newType.getUniqueID ()
                });
        viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
        viewPanel.termsAndCoursesDialog.setSettingsTypesTableSelection (newType.getUniqueID ());
        viewPanel.termsAndCoursesDialog.settingsTypesTable.scrollRectToVisible (viewPanel.termsAndCoursesDialog.settingsTypesTable.getCellRect (viewPanel.termsAndCoursesDialog.typeTableModel.getRowCount () - 1, 0, false));
        viewPanel.termsAndCoursesDialog.removeTypeButton.setEnabled (true);
        viewPanel.termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (2);
        needsCoursesAndTermsSave = true;

        viewPanel.termsAndCoursesDialog.showTypeDetails ();

        typeLoading.pop ();
    }

    /**
     * Adds a new instructor to the Settings dialog.
     */
    protected synchronized void addInstructor()
    {
        if (currentInstructorIndex != -1)
        {
            viewPanel.termsAndCoursesDialog.checkTermsAndCoursesChanges (currentInstructorIndex, 4);
        }
        instructorLoading.push (true);

        int courseIndex = viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ();
        Course course = utility.courses.get (courseIndex);

        int index = 0;
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.instructorTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (viewPanel.termsAndCoursesDialog.instructorTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newInstructor")))
                {
                    ++index;
                }
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.instructorTableModel.getValueAt (i, 0).toString ().equals (language.getString ("newInstructor") + " " + index))
                {
                    ++index;
                }
            }
        }
        String instructorName = language.getString ("newInstructor");
        if (index != 0)
        {
            instructorName += " " + index;
        }
        Instructor newInstructor = new Instructor (instructorName, System.currentTimeMillis (), course, utility);
        utility.instructors.add (newInstructor);
        course.addInstructor (newInstructor);
        viewPanel.termsAndCoursesDialog.instructorTableModel.addRow (new Object[]
                {
                    instructorName, newInstructor.getLectureLab (), newInstructor.getUniqueID ()
                });
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.refreshTable ();
        viewPanel.termsAndCoursesDialog.setSettingsInstructorsTableSelection (newInstructor.getUniqueID ());
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.scrollRectToVisible (viewPanel.termsAndCoursesDialog.settingsInstructorsTable.getCellRect (viewPanel.termsAndCoursesDialog.instructorTableModel.getRowCount () - 1, 0, false));
        viewPanel.termsAndCoursesDialog.removeInstructorButton.setEnabled (true);
        viewPanel.termsAndCoursesDialog.courseTabbedPane.setSelectedIndex (1);
        needsCoursesAndTermsSave = true;

        viewPanel.termsAndCoursesDialog.showInstructorDetails ();

        instructorLoading.pop ();
    }

    /**
     * Adds a new, empty event to the list of events/assignmentsAndEvents in the
     * middle of the main window. Also makes the Event Details Panel fully
     * visible so the user may edit details relating to the assignment.
     */
    protected void addEvent(Event event)
    {
        if (currentIndexFromVector != -1)
        {
            viewPanel.checkAssignmentOrEventChanges (currentIndexFromVector);
            viewPanel.checkRepeatEventChanges (currentIndexFromVector);
        }
        assignmentOrEventLoading.push (true);

        boolean silent = true;
        if (event == null)
        {
            event = new Event (language.getString ("newEvent"), System.currentTimeMillis (), utility, utility.getEventYear (viewPanel.miniCalendar.getYearChooser ().getYear () + ""));
            silent = false;
        }
        event.getEventYear ().addEvent (event);
        utility.preferences.categories.get (0).addEvent (event);
        if (!silent && viewPanel.middleTabbedPane.getSelectedIndex () == 1 && viewPanel.selectedDayPanel != null)
        {
            String month = viewPanel.miniCalendar.getMonthChooser ().getMonth () + 1 + "";
            if (month.length () == 1)
            {
                month = "0" + month;
            }
            String day = viewPanel.getIndexFromDaysArray (viewPanel.selectedDayPanel) + 1 + "";
            if (day.length () == 1)
            {
                day = "0" + day;
            }
            int year = viewPanel.miniCalendar.getYearChooser ().getYear ();

            String newDate = month + "/" + day + "/" + year;
            event.setDate (newDate, utility);
        }
        if (utility.preferences.filter1Index != 1)
        {
            viewPanel.assignmentsTableModel.addRow (event.getRowObject ());
        }
        utility.assignmentsAndEvents.add (event);
        if (utility.preferences.filter1Index != 1)
        {
            viewPanel.assignmentsTable.setSelectedRow (viewPanel.assignmentsTableModel.getRowCount () - 1);
            viewPanel.cloneButton.setEnabled (true);
            viewPanel.removeButton.setEnabled (true);
        }

        event.getEventYear ().markChanged ();
        viewPanel.filter (true);

        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            if (utility.preferences.filter1Index != 1)
            {
                viewPanel.daysAssignmentsAndEvents[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1].add (event);
                viewPanel.shownEvents.add (event);
            }

            if (!silent)
            {
                event.getLabel ().addMouseListener (new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased(MouseEvent evt)
                    {
                        viewPanel.eventMouseReleased (evt);
                    }
                });
            }
            viewPanel.DRAG_SOURCE.createDefaultDragGestureRecognizer (event.getLabel (), DnDConstants.ACTION_MOVE, viewPanel.DND_LISTENER);
            event.refreshText ();

            if (utility.preferences.filter1Index != 1)
            {
                viewPanel.assignmentsTable.setSelectedRowFromVectorIndex (utility.getAssignmentOrEventIndexByID (event.getUniqueID ()));
                if (viewPanel.selectedDayPanel != null)
                {
                    viewPanel.selectedDayPanel.setBorder (viewPanel.UNSELECTED_DAY_BORDER);
                }
                viewPanel.selectedDayPanel = viewPanel.days[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1];
                viewPanel.selectedDayPanel.setBorder (viewPanel.SELECTED_DAY_BORDER);
            }

            viewPanel.refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
        }
        viewPanel.refreshBusyDays ();

        if (viewPanel.assignmentsTableModel.getRowCount () == 1)
        {
            viewPanel.assignmentsTableRowSelected (null);
        }
        viewPanel.adjustAssignmentTableColumnWidths ();

        assignmentOrEventLoading.pop ();
    }

    /**
     * Adds a new, empty assignment to the list of assignmentsAndEvents in the
     * middle of the main window. Also makes the Assignment Details panel fully
     * visible so the user may edit details relating to the assignment.
     */
    protected void addAssignment()
    {
        if (currentIndexFromVector != -1)
        {
            viewPanel.checkAssignmentOrEventChanges (currentIndexFromVector);
            viewPanel.checkRepeatEventChanges (currentIndexFromVector);
        }
        assignmentOrEventLoading.push (true);

        int courseIndex = 0;
        boolean absolute = false;
        if (viewPanel.getSelectedCourseIndexFrom (viewPanel.getSelectedTermIndex ()) != -1)
        {
            courseIndex = viewPanel.getSelectedCourseIndexFrom (viewPanel.getSelectedTermIndex ());
        }
        else
        {
            for (int i = 0; i < utility.courses.size (); ++i)
            {
                Course course = utility.courses.get (i);
                Date date = viewPanel.miniCalendar.getDate ();

                try
                {
                    if (date.after (Domain.DATE_FORMAT.parse (course.getStartDate ()))
                        && date.before (Domain.DATE_FORMAT.parse (course.getEndDate ())))
                    {
                        absolute = true;
                        courseIndex = i;
                        break;
                    }
                }
                catch (ParseException ex)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
        int termIndex = 0;
        if (viewPanel.getSelectedTermIndex () != -1)
        {
            termIndex = viewPanel.getSelectedTermIndex ();
        }
        Course course = null;
        if (absolute)
        {
            course = utility.courses.get (courseIndex);
        }
        else
        {
            course = utility.terms.get (termIndex).getCourse (courseIndex);
        }
        Assignment assignment = new Assignment (language.getString ("newAssignment"), System.currentTimeMillis (), course, utility);
        course.addAssignment (assignment);
        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1 && viewPanel.selectedDayPanel != null)
        {
            String month = viewPanel.miniCalendar.getMonthChooser ().getMonth () + 1 + "";
            if (month.length () == 1)
            {
                month = "0" + month;
            }
            String day = viewPanel.getIndexFromDaysArray (viewPanel.selectedDayPanel) + 1 + "";
            if (day.length () == 1)
            {
                day = "0" + day;
            }
            int year = viewPanel.miniCalendar.getYearChooser ().getYear ();

            String newDate = month + "/" + day + "/" + year;
            assignment.setDueDate (newDate);
        }
        if (utility.preferences.filter1Index != 2)
        {
            viewPanel.assignmentsTableModel.addRow (assignment.getRowObject ());
        }
        utility.assignmentsAndEvents.add (assignment);
        if (utility.preferences.filter1Index != 2)
        {
            viewPanel.assignmentsTable.setSelectedRow (viewPanel.assignmentsTableModel.getRowCount () - 1);
            viewPanel.cloneButton.setEnabled (true);
            viewPanel.removeButton.setEnabled (true);
        }

        course.markChanged ();
        viewPanel.filter (true);

        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            if (utility.preferences.filter1Index != 2)
            {
                viewPanel.daysAssignmentsAndEvents[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1].add (assignment);
                viewPanel.shownAssignments.add (assignment);
            }

            assignment.getLabel ().addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    viewPanel.assignmentMousePressed (evt);
                }
            });
            assignment.getCheckBox ().addItemListener (new ItemListener ()
            {
                @Override
                public void itemStateChanged(ItemEvent evt)
                {
                    viewPanel.assignmentItemStateChanged (evt);
                }
            });
            viewPanel.DRAG_SOURCE.createDefaultDragGestureRecognizer (assignment.getLabel (), DnDConstants.ACTION_MOVE, viewPanel.DND_LISTENER);
            assignment.refreshText ();

            if (utility.preferences.filter1Index != 2)
            {
                viewPanel.assignmentsTable.setSelectedRowFromVectorIndex (utility.getAssignmentOrEventIndexByID (assignment.getUniqueID ()));
                if (viewPanel.selectedDayPanel != null)
                {
                    viewPanel.selectedDayPanel.setBorder (viewPanel.UNSELECTED_DAY_BORDER);
                }
                viewPanel.selectedDayPanel = viewPanel.days[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1];
                viewPanel.selectedDayPanel.setBorder (viewPanel.SELECTED_DAY_BORDER);
            }

            viewPanel.refreshDayInCalendar (Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1);
        }
        viewPanel.refreshBusyDays ();

        if (viewPanel.assignmentsTableModel.getRowCount () == 1)
        {
            viewPanel.assignmentsTableRowSelected (null);
        }
        viewPanel.adjustAssignmentTableColumnWidths ();

        assignmentOrEventLoading.pop ();
    }

    /**
     * Create a cloned object of the given toClone object, with the cloned
     * object only differing in date by the given date.
     *
     * @param toClone The event to clone.
     * @param utility A reference to the applications utility.
     * @param date The date for the newly cloned event.
     * @param effectYearAndCat True if the event should be added to the
     * year/category upon creation, false if the event should just be created
     * and returned
     * @return The cloned object.
     */
    protected Event createCloneObject(Event toClone, LocalUtility utility, Date date, boolean effectYearAndCat)
    {
        long uniqueID = System.currentTimeMillis ();
        while (utility.getByID (uniqueID) != null)
        {
            uniqueID = (long) (uniqueID * Math.random ());
        }
        Event event = new Event (toClone.getItemName (), uniqueID, utility, toClone.getEventYear ());
        if (effectYearAndCat)
        {
            toClone.getCategory ().addEvent (event);
            event.getEventYear ().addEvent (event);
            event.getEventYear ().markChanged ();
        }
        event.setCategory (toClone.getCategory ());
        event.setDate (Domain.DATE_FORMAT.format (date), utility);
        event.setDescription (toClone.getDescription ());
        event.setStartTime (0, toClone.getStartTime (0));
        event.setStartTime (1, toClone.getStartTime (1));
        event.setStartTime (2, toClone.getStartTime (2));
        event.setEndTime (0, toClone.getEndTime (0));
        event.setEndTime (1, toClone.getEndTime (1));
        event.setEndTime (2, toClone.getEndTime (2));
        event.setIsAllDay (toClone.isAllDay ());
        event.setEventLocation (toClone.getEventLocation ());
        event.setRepeating (toClone.getRepeating ().toString ());
        event.getRepeating ().setID (toClone.getRepeating ().getID ());

        return event;
    }

    /**
     * Duplicates the selected event, making a second (yet unique) instance of
     * it in the events list and in the assignmentsAndEvents table.
     *
     * @param utility
     */
    protected void cloneEvent(LocalUtility utility)
    {
        if (currentIndexFromVector != -1)
        {
            viewPanel.checkAssignmentOrEventChanges (currentIndexFromVector);
            viewPanel.checkRepeatEventChanges (currentIndexFromVector);
        }
        assignmentOrEventLoading.push (true);

        Event toClone = (Event) utility.assignmentsAndEvents.get (currentIndexFromVector);
        try
        {
            Event event = createCloneObject (toClone, utility, Domain.DATE_FORMAT.parse (toClone.getDueDate ()), true);
            event.getRepeating ().setID (-1);

            if (utility.preferences.filter1Index != 1)
            {
                viewPanel.assignmentsTableModel.addRow (event.getRowObject ());
            }
            utility.assignmentsAndEvents.add (event);

            viewPanel.assignmentsTable.setSelectedRow (viewPanel.assignmentsTableModel.getRowCount () - 1);
            viewPanel.filter (true);

            if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
            {
                if (utility.preferences.filter1Index != 1)
                {
                    viewPanel.daysAssignmentsAndEvents[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1].add (event);
                    viewPanel.shownEvents.add (event);
                }

                event.getLabel ().addMouseListener (new MouseAdapter ()
                {
                    @Override
                    public void mouseReleased(MouseEvent evt)
                    {
                        viewPanel.eventMouseReleased (evt);
                    }
                });
                viewPanel.DRAG_SOURCE.createDefaultDragGestureRecognizer (event.getLabel (), DnDConstants.ACTION_MOVE, viewPanel.DND_LISTENER);
                event.refreshText ();

                viewPanel.refreshDayInCalendar (Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1);
            }
            viewPanel.refreshBusyDays ();
        }
        catch (ParseException ex)
        {
            Domain.LOGGER.add (ex);
        }

        assignmentOrEventLoading.pop ();
    }

    /**
     * Duplicates the selected assignment, making a second (yet unique) instance
     * of it in the assignmentsAndEvents list, to the course, and in the
     * assignmentsAndEvents table.
     */
    protected void cloneAssignment()
    {
        if (currentIndexFromVector != -1)
        {
            viewPanel.checkAssignmentOrEventChanges (currentIndexFromVector);
            viewPanel.checkRepeatEventChanges (currentIndexFromVector);
        }
        assignmentOrEventLoading.push (true);

        Assignment toClone = (Assignment) utility.assignmentsAndEvents.get (currentIndexFromVector);
        Assignment assignment = new Assignment (toClone.getItemName (), System.currentTimeMillis (), toClone.getCourse (), utility);
        toClone.getCourse ().addAssignment (assignment);
        assignment.setDueDate (toClone.getDueDate ());
        assignment.setDueTime (0, toClone.getDueTime (0));
        assignment.setDueTime (1, toClone.getDueTime (1));
        assignment.setDueTime (2, toClone.getDueTime (2));
        assignment.setComments (toClone.getComments ());
        assignment.setGrade (toClone.getGrade ());
        assignment.setIsDone (toClone.isDone ());
        assignment.setPriority (toClone.getPriority ());
        assignment.setTextbook (toClone.getTextbook ());
        assignment.setType (toClone.getType ());
        if (utility.preferences.filter1Index != 1)
        {
            viewPanel.assignmentsTableModel.addRow (assignment.getRowObject ());
        }
        utility.assignmentsAndEvents.add (assignment);

        viewPanel.assignmentsTable.setSelectedRow (viewPanel.assignmentsTableModel.getRowCount () - 1);
        assignment.getCourse ().markChanged ();
        viewPanel.filter (true);

        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            if (utility.preferences.filter1Index != 1)
            {
                viewPanel.daysAssignmentsAndEvents[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1].add (assignment);
                viewPanel.shownAssignments.add (assignment);
            }

            assignment.getLabel ().addMouseListener (new MouseAdapter ()
            {
                @Override
                public void mouseReleased(MouseEvent evt)
                {
                    viewPanel.assignmentMousePressed (evt);
                }
            });
            assignment.getCheckBox ().addItemListener (new ItemListener ()
            {
                @Override
                public void itemStateChanged(ItemEvent evt)
                {
                    viewPanel.assignmentItemStateChanged (evt);
                }
            });
            viewPanel.DRAG_SOURCE.createDefaultDragGestureRecognizer (assignment.getLabel (), DnDConstants.ACTION_MOVE, viewPanel.DND_LISTENER);
            assignment.refreshText ();

            viewPanel.refreshDayInCalendar (Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1);
        }
        viewPanel.refreshBusyDays ();

        assignmentOrEventLoading.pop ();
    }

    /**
     * Set the name of the category.
     *
     * @param index The index of the category to set the name for.
     */
    protected void setCategoryName(int index)
    {
        if (categoryLoading.empty () && needsPreferencesSave && !viewPanel.initLoading)
        {
            Category category = utility.preferences.categories.get (index);
            if (utility.getCategoryByName (viewPanel.settingsDialog.categoryNameTextField.getText ()) == null
                || utility.getCategoryByName (viewPanel.settingsDialog.categoryNameTextField.getText ()) == category)
            {
                category.setName (viewPanel.settingsDialog.categoryNameTextField.getText ());
                viewPanel.settingsDialog.categoryTableModel.setValueAt (category.getName (), index, 0);
                viewPanel.settingsDialog.settingsCategoriesTable.refreshTable ();
            }
            else
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("categoryDuplicateName"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.settingsDialog, language.getString ("duplicateName"));
                optionDialog.setVisible (true);
                viewPanel.settingsDialog.categoryNameTextField.setText (category.getName ());
            }
        }
    }

    /**
     * Refreshes the items in the categories combo box.
     */
    public void refreshCategoryComboModel()
    {
        assignmentOrEventLoading.push (true);

        viewPanel.categoryComboModel.removeAllElements ();
        for (int i = 0; i < utility.preferences.categories.size (); ++i)
        {
            viewPanel.categoryComboModel.addElement (utility.preferences.categories.get (i).getName ());
        }
        viewPanel.categoryComboBox.invalidate ();

        if (currentIndexFromVector != -1)
        {
            ListItem item = utility.assignmentsAndEvents.get (currentIndexFromVector);
            if (!item.isAssignment ())
            {
                viewPanel.categoryComboBox.setSelectedItem (((Event) item).getCategory ().getName ());
            }
        }
        assignmentOrEventLoading.pop ();
    }

    /**
     * Sets the name of the specified term with the value found in the term name
     * text field of the Settings dialog.
     *
     * @param index The index of the term.
     */
    protected void setTermName(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && termLoading.empty ())
        {
            Term term = utility.terms.get (index);
            String text = viewPanel.termsAndCoursesDialog.termNameTextField.getText ();
            if (text.replaceAll (" ", "").equals (""))
            {
                termLoading.push (true);
                viewPanel.termsAndCoursesDialog.termNameTextField.setText (term.getTypeName ());
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("invalidTermNameText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTermName"));
                optionDialog.setVisible (true);
                viewPanel.termsAndCoursesDialog.termNameTextField.requestFocus ();
                viewPanel.termsAndCoursesDialog.termNameTextField.selectAll ();
                termLoading.pop ();
            }
            else
            {
                if (!text.equals ("-" + language.getString ("none") + "-"))
                {
                    term.setTypeName (text);
                    refreshTermTree ();
                    viewPanel.termsAndCoursesDialog.termTableModel.setValueAt (term.getTypeName (), index, 0);
                    viewPanel.termsAndCoursesDialog.settingsTermsTable.refreshTable ();
                }
                else
                {
                    termLoading.push (true);
                    viewPanel.termsAndCoursesDialog.termNameTextField.setText (term.getTypeName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("termNameAlreadyExists"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTermName"));
                    optionDialog.setVisible (true);
                    viewPanel.termsAndCoursesDialog.termNameTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.termNameTextField.selectAll ();
                    termLoading.pop ();
                }
            }
        }
    }

    /**
     * Refresh the term tree after an item has been changed.
     */
    public void refreshTermTree()
    {
        TreePath node = viewPanel.termTree.getSelectionPath ();
        viewPanel.termTree.invalidate ();
        ((DefaultTreeModel) viewPanel.termTree.getModel ()).reload ();
        viewPanel.expandTermTree (new TreeExpansionEvent (this, null));
        viewPanel.termTree.setSelectionPath (node);
    }

    /**
     * Sets the name of the specified course with the value found in the course
     * name text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseName(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            String text = viewPanel.termsAndCoursesDialog.courseNameTextField.getText ();
            if (text.replaceAll (" ", "").equals (""))
            {
                courseLoading.push (true);
                viewPanel.termsAndCoursesDialog.courseNameTextField.setText (course.getTypeName ());
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("invalidCourseNameText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidCourseName"));
                optionDialog.setVisible (true);
                viewPanel.termsAndCoursesDialog.courseNameTextField.requestFocus ();
                viewPanel.termsAndCoursesDialog.courseNameTextField.selectAll ();
                courseLoading.pop ();
            }
            else
            {
                if (!text.equals ("-" + language.getString ("none") + "-"))
                {
                    course.setTypeName (viewPanel.termsAndCoursesDialog.courseNameTextField.getText ());
                    refreshTermTree ();
                    viewPanel.termsAndCoursesDialog.courseTableModel.setValueAt (course.getTypeName (), index, 0);
                    viewPanel.termsAndCoursesDialog.settingsCoursesTable.refreshTable ();
                }
                else
                {
                    courseLoading.push (true);
                    viewPanel.termsAndCoursesDialog.courseNameTextField.setText (course.getTypeName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("courseNameAlreadyExists"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidCourseName"));
                    optionDialog.setVisible (true);
                    viewPanel.termsAndCoursesDialog.courseNameTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.courseNameTextField.selectAll ();
                    courseLoading.pop ();
                }
            }
        }
    }

    /**
     * Sets the name of the specified type with the value found in the type name
     * text field of the Settings dialog.
     *
     * @param index The index of the type.
     */
    protected void setTypeName(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && typeLoading.empty ())
        {
            AssignmentType type = utility.courses.get (currentCourseIndex).getType (index);
            String text = viewPanel.termsAndCoursesDialog.typeNameTextField.getText ();
            if (text.replaceAll (" ", "").equals (""))
            {
                typeLoading.push (true);
                viewPanel.termsAndCoursesDialog.typeNameTextField.setText (type.getTypeName ());
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("invalidTypeNameText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTypeName"));
                optionDialog.setVisible (true);
                viewPanel.termsAndCoursesDialog.typeNameTextField.requestFocus ();
                viewPanel.termsAndCoursesDialog.typeNameTextField.selectAll ();
                typeLoading.pop ();
            }
            else
            {
                if (!text.equals ("-" + language.getString ("none") + "-"))
                {
                    type.setTypeName (text);
                    viewPanel.termsAndCoursesDialog.typeTableModel.setValueAt (type.getTypeName (), index, 0);
                    viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
                }
                else
                {
                    typeLoading.push (true);
                    viewPanel.termsAndCoursesDialog.typeNameTextField.setText (type.getTypeName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("typeNameAlreadyExists"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTypeName"));
                    optionDialog.setVisible (true);
                    viewPanel.termsAndCoursesDialog.typeNameTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.typeNameTextField.selectAll ();
                    typeLoading.pop ();
                }
            }
        }
    }

    /**
     * Sets the name of the specified textbook with the value found in the
     * textbook name text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookName(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            String text = viewPanel.termsAndCoursesDialog.textbookNameTextField.getText ();
            if (text.replaceAll (" ", "").equals (""))
            {
                textbookLoading.push (true);
                viewPanel.termsAndCoursesDialog.textbookNameTextField.setText (textbook.getTypeName ());
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("invalidTextbookNameText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTextbookName"));
                optionDialog.setVisible (true);
                viewPanel.termsAndCoursesDialog.textbookNameTextField.requestFocus ();
                viewPanel.termsAndCoursesDialog.textbookNameTextField.selectAll ();
                textbookLoading.pop ();
            }
            else
            {
                if (!text.equals ("-" + language.getString ("none") + "-"))
                {
                    textbook.setTypeName (viewPanel.termsAndCoursesDialog.textbookNameTextField.getText ());
                    viewPanel.termsAndCoursesDialog.textbookTableModel.setValueAt (textbook.getTypeName (), index, 0);
                    viewPanel.termsAndCoursesDialog.settingsTextbooksTable.refreshTable ();
                }
                else
                {
                    textbookLoading.push (true);
                    viewPanel.termsAndCoursesDialog.textbookNameTextField.setText (textbook.getTypeName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("textbookNameAlreadyExists"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidTextbookName"));
                    optionDialog.setVisible (true);
                    viewPanel.termsAndCoursesDialog.textbookNameTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.textbookNameTextField.selectAll ();
                    textbookLoading.pop ();
                }
            }
        }
    }

    /**
     * Sets the name of the specified assignment with the value found in the
     * assignment name text field of the details right-hand side panel.
     *
     * @param index The index of the assignment.
     */
    protected int setAssignmentName(int index)
    {
        int response = -1;
        try
        {
            if (index != -1
                && ((Assignment) utility.assignmentsAndEvents.get (index)).getCourse ().isChanged ()
                && assignmentOrEventLoading.empty ()
                && viewPanel.assignmentContentPanel.isVisible ())
            {
                Assignment assignment = (Assignment) utility.assignmentsAndEvents.get (index);
                String text = viewPanel.assignmentNameTextField.getText ();
                if (text.replaceAll (" ", "").equals (""))
                {
                    response = 1;
                    assignmentOrEventLoading.push (true);
                    viewPanel.assignmentNameTextField.setText (assignment.getItemName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("assignmentLeftEmpty"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, language.getString ("invalidAssignmentName"));
                    optionDialog.setVisible (true);
                    if (viewPanel.dontReselectName.empty ())
                    {
                        viewPanel.assignmentNameTextField.requestFocus ();
                        viewPanel.assignmentNameTextField.selectAll ();
                    }
                    assignmentOrEventLoading.pop ();
                }
                else
                {
                    assignment.setItemName (viewPanel.assignmentNameTextField.getText ());
                    assignment.refreshText ();
                    viewPanel.refreshAssignmentsRowAt (index);
                    assignment.getCourse ().markChanged ();
                    viewPanel.filter (false);

                    if (utility.preferences.sortIndex == 1)
                    {
                        viewPanel.scrollToItemOrToday (assignment);
                    }
                }
            }
        }
        catch (ClassCastException ex)
        {
        }
        return response;
    }

    /**
     * Sets the name of the specified event with the value found in the event
     * name text field of the details right-hand side panel.
     *
     * @param index The index of the event.
     */
    protected int setEventName(int index)
    {
        int response = -1;
        try
        {
            if (index != -1
                && ((Event) utility.assignmentsAndEvents.get (index)).getEventYear ().isChanged ()
                && assignmentOrEventLoading.empty ()
                && viewPanel.eventContentPanel.isVisible ())
            {
                Event event = (Event) utility.assignmentsAndEvents.get (index);
                String text = viewPanel.eventNameTextField.getText ();
                if (text.replaceAll (" ", "").equals (""))
                {
                    response = 1;
                    assignmentOrEventLoading.push (true);
                    viewPanel.eventNameTextField.setText (event.getItemName ());
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (language.getString ("eventLeftEmpty"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, language.getString ("invalidEventName"));
                    optionDialog.setVisible (true);
                    if (viewPanel.dontReselectName.empty ())
                    {
                        viewPanel.eventNameTextField.requestFocus ();
                        viewPanel.eventNameTextField.selectAll ();
                    }
                    assignmentOrEventLoading.pop ();
                }
                else
                {
                    if (!text.equals (event.getItemName ()))
                    {
                        viewPanel.eventChanges.push (true);
                    }

                    event.setItemName (viewPanel.eventNameTextField.getText ());
                    viewPanel.refreshAssignmentsRowAt (index);
                    event.refreshText ();

                    event.getEventYear ().markChanged ();
                    viewPanel.filter (false);

                    if (utility.preferences.sortIndex == 1)
                    {
                        viewPanel.scrollToItemOrToday (event);
                    }
                }
            }
        }
        catch (ClassCastException ex)
        {
        }
        return response;
    }

    /**
     * Sets the weight of the specified type with the value found in the type
     * weight text field of the Settings dialog.
     *
     * @param index The index of the type.
     */
    protected void setTypeWeight(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && typeLoading.empty ())
        {
            AssignmentType type = utility.courses.get (currentCourseIndex).getType (index);
            if (verifyWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText (), type) != null && !verifyWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText (), type).equals ("-1"))
            {
                viewPanel.termsAndCoursesDialog.weightTextField.setText (verifyWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText (), type));
                type.setWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText ());
                setTotalWeightLabel (currentCourseIndex);
                viewPanel.termsAndCoursesDialog.typeTableModel.setValueAt (type.getWeight (), index, 1);
                viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.weightTextField.getText ().replaceAll (" ", "").equals (""))
                {
                    type.setWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText ());
                    setTotalWeightLabel (currentCourseIndex);
                    viewPanel.termsAndCoursesDialog.typeTableModel.setValueAt (type.getWeight (), index, 1);
                    viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
                }
                else
                {
                    if (verifyWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText (), type) == null)
                    {
                        viewPanel.termsAndCoursesDialog.weightTextField.setText (type.getWeight ());
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (language.getString ("enterValidPercentageWeight"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidWeight"));
                        optionDialog.setVisible (true);
                    }
                    else if (verifyWeight (viewPanel.termsAndCoursesDialog.weightTextField.getText (), type).equals ("-1"))
                    {
                        viewPanel.termsAndCoursesDialog.weightTextField.setText (type.getWeight ());
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (language.getString ("typesMustAddToHundred"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidWeight"));
                        optionDialog.setVisible (true);
                    }
                    else
                    {
                        viewPanel.termsAndCoursesDialog.weightTextField.setText (type.getWeight ());
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (language.getString ("typeWeightMustBeValidPercent"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidWeight"));
                        optionDialog.setVisible (true);
                    }
                    viewPanel.termsAndCoursesDialog.weightTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.weightTextField.selectAll ();
                }
            }
        }
    }

    /**
     * Set overall type weight for currently displayed course.
     *
     * @param index Current course index.
     */
    public void setTotalWeightLabel(int index)
    {
        ArrayList<Double> weights = new ArrayList<Double> ();
        Course course = utility.courses.get (index);
        for (int i = 0; i < course.getTypeCount (); ++i)
        {
            AssignmentType type = course.getType (i);
            if (!type.getWeight ().equals (""))
            {
                if (weights.isEmpty ())
                {
                    weights.add (Double.parseDouble (type.getWeight ().replaceAll ("%", "")));
                }
                else
                {
                    weights.add (Double.parseDouble (type.getWeight ().replaceAll ("%", "")) + weights.get (weights.size () - 1));
                }
            }
        }
        if (weights.isEmpty ())
        {
            viewPanel.termsAndCoursesDialog.typesListLabel.setText (language.getString ("gradingScale"));
        }
        else
        {
            double weight = weights.get (weights.size () - 1);
            if (weight > 100)
            {
                weight = 100;
            }
            viewPanel.termsAndCoursesDialog.typesListLabel.setText (language.getString ("gradingScale") + " (" + language.getString ("total") + ": " + Domain.PERCENT_FORMAT.format (weight / 100) + ")");
        }
    }

    /**
     * Verifies a given weight to ensure that it is a valid numerical form for a
     * type's weight to be. If so, a formatted weight is returned, otherwise
     * null is returned.
     *
     * @param weight The unformatted weight to be checked.
     * @param type The type assigned being reference by this course.
     */
    private String verifyWeight(String weight, AssignmentType type)
    {
        try
        {
            String newWeight = parseToPercent (weight);
            if (newWeight != null)
            {
                double percent = Double.parseDouble (newWeight.replaceAll ("%", "")) / 100;
                if (1 >= (type.getCourse ().getTotalWeightsLess (type)) + percent)
                {
                    newWeight = Domain.PERCENT_FORMAT.format (percent);
                }
                else
                {
                    newWeight = "-1";
                }

                if (!newWeight.equals ("-1") && newWeight.split ("\\.")[1].equals ("00%"))
                {
                    newWeight = newWeight.split ("\\.")[0] + "%";
                }
            }

            return newWeight;
        }
        catch (NumberFormatException ex)
        {
            return "-1";
        }
    }

    /**
     * Sets the author of the specified textbook with the value found in the
     * textbook author text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookAuthor(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            textbook.setAuthor (viewPanel.termsAndCoursesDialog.authorTextField.getText ());
        }
    }

    /**
     * Sets the publisher of the specified textbook with the value found in the
     * textbook publisher text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookPublisher(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            textbook.setPublisher (viewPanel.termsAndCoursesDialog.publisherTextField.getText ());
        }
    }

    /**
     * Sets the ISBN of the specified textbook with the value found in the
     * textbook ISBN text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookISBN(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            textbook.setISBN (viewPanel.termsAndCoursesDialog.isbnTextField.getText ());

            if (!textbook.getISBN ().replaceAll (" ", "").equals (""))
            {
                viewPanel.termsAndCoursesDialog.searchGoogleButton.setEnabled (true);
            }
            else
            {
                viewPanel.termsAndCoursesDialog.searchGoogleButton.setEnabled (false);
            }
        }
    }

    /**
     * Sets the textbook source of the specified textbook with the value found
     * in the textbook source text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookSource(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            if (!viewPanel.termsAndCoursesDialog.textbookSourceTextField.getText ().equals ("")
                && !viewPanel.termsAndCoursesDialog.textbookSourceTextField.getText ().startsWith ("http://"))
            {
                viewPanel.termsAndCoursesDialog.textbookSourceTextField.setText ("http://" + viewPanel.termsAndCoursesDialog.textbookSourceTextField.getText ());
            }
            textbook.setSource (viewPanel.termsAndCoursesDialog.textbookSourceTextField.getText ());
        }
    }

    /**
     * Sets the price of the specified textbook with the value found in the
     * textbook price text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookPrice(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            textbook.setPrice (viewPanel.termsAndCoursesDialog.purchasePriceTextField.getText ());
        }
    }

    /**
     * Sets the contact email of the specified textbook with the value found in
     * the textbook contact email text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookContactEmail(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Matcher matcher = Domain.EMAIL_PATTERN.matcher (viewPanel.termsAndCoursesDialog.contactEmailTextField.getText ());
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            if (matcher.matches ())
            {
                textbook.setContactEmail (viewPanel.termsAndCoursesDialog.contactEmailTextField.getText ());
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.contactEmailTextField.getText ().replaceAll (" ", "").equals (""))
                {
                    textbook.setContactEmail (viewPanel.termsAndCoursesDialog.contactEmailTextField.getText ());
                }
                else
                {
                    viewPanel.termsAndCoursesDialog.contactEmailTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.contactEmailTextField.selectAll ();
                }
            }
        }
    }

    /**
     * Sets the condition of the specified textbook with the value found in the
     * textbook condition text field of the Settings dialog.
     *
     * @param index The index of the textbook.
     */
    protected void setTextbookCondition(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && textbookLoading.empty ())
        {
            Textbook textbook = utility.courses.get (currentCourseIndex).getTextbook (index);
            textbook.setCondition (viewPanel.termsAndCoursesDialog.conditionTextField.getText ());
        }
    }

    /**
     * Sets the grade of the specified assignment with the value found in the
     * assignment grade text field of the details right-hand side panel.
     *
     * @param index The index of the assignment.
     */
    protected void setAssignmentGrade(int index)
    {
        try
        {
            if (index != -1
                && ((Assignment) utility.assignmentsAndEvents.get (index)).getCourse ().isChanged ()
                && assignmentOrEventLoading.empty ())
            {
                Assignment assignment = (Assignment) utility.assignmentsAndEvents.get (currentIndexFromVector);
                if (parseToPercent (viewPanel.gradeTextField.getText ()) != null)
                {
                    viewPanel.gradeTextField.setText (parseToPercent (viewPanel.gradeTextField.getText ()));
                    assignment.setGrade (viewPanel.gradeTextField.getText ());
                    viewPanel.refreshAssignmentsRowAt (currentIndexFromVector);
                    assignment.getCourse ().markChanged ();
                    viewPanel.filter (true);

                    if (utility.preferences.sortIndex == 5)
                    {
                        viewPanel.scrollToItemOrToday (assignment);
                    }
                }
                else
                {
                    if (viewPanel.gradeTextField.getText ().replaceAll (" ", "").equals (""))
                    {
                        assignment.setGrade (viewPanel.gradeTextField.getText ());
                        viewPanel.refreshAssignmentsRowAt (currentIndexFromVector);
                        assignment.getCourse ().markChanged ();
                        viewPanel.filter (true);

                        if (utility.preferences.sortIndex == 5)
                        {
                            viewPanel.scrollToItemOrToday (assignment);
                        }
                    }
                    else
                    {
                        assignmentOrEventLoading.push (true);
                        viewPanel.gradeTextField.setText ("");
                        assignmentOrEventLoading.pop ();
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (language.getString ("assnMustEnterValidGrade"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, language.getString ("invalidGrade"));
                        optionDialog.setVisible (true);
                        viewPanel.gradeTextField.requestFocus ();
                        viewPanel.gradeTextField.selectAll ();
                    }
                }
            }
        }
        catch (ClassCastException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Sets the location of the specified event with the value found in the
     * event location text field of the details right-hand side panel.
     *
     * @param index The index of the event.
     */
    protected void setEventLocation(int index)
    {
        try
        {
            if (index != -1
                && ((Event) utility.assignmentsAndEvents.get (index)).getEventYear ().isChanged ()
                && assignmentOrEventLoading.empty ())
            {
                viewPanel.eventChanges.push (true);

                Event event = (Event) utility.assignmentsAndEvents.get (currentIndexFromVector);

                event.setEventLocation (viewPanel.locationTextField.getText ());

                if (!event.getEventLocation ().replaceAll (" ", "").equals (""))
                {
                    viewPanel.googleMapsButton.setEnabled (true);
                }
                else
                {
                    viewPanel.googleMapsButton.setEnabled (false);
                }
            }
        }
        catch (ClassCastException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Parses a grade into a valid String and returns it. If the grade could not
     * be parsed, null is returned.
     *
     * @param string The string to be parsed.
     * @return The parsed grade, or null if it could not be parsed.
     */
    protected String parseToPercent(String string)
    {
        String newString = null;

        string = string.replaceAll (" ", "");
        try
        {
            double value = Double.parseDouble (string);
            if (value != 0)
            {
                newString = Domain.PERCENT_FORMAT.format (value / 100);
            }
            else
            {
                newString = "0%";
            }
        }
        catch (NumberFormatException ex)
        {
            // already a percentage?
            if (string.endsWith ("%"))
            {
                try
                {
                    double value = Double.parseDouble (string.replaceAll ("%", ""));
                    newString = Domain.PERCENT_FORMAT.format (value / 100);
                }
                catch (NumberFormatException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
            // proper division?
            else
            {
                if (string.contains ("/"))
                {
                    String[] split = string.split ("/");
                    if (split.length == 2)
                    {
                        try
                        {
                            double first = Double.parseDouble (split[0]);
                            if (first != 0)
                            {
                                double second = Double.parseDouble (split[1]);
                                double value = first / second;
                                newString = Domain.PERCENT_FORMAT.format (value);
                            }
                            else
                            {
                                newString = "0%";
                            }
                        }
                        catch (NumberFormatException innerEx)
                        {
                            Domain.LOGGER.add (ex);
                        }
                    }
                }
                // worded division?
                else
                {
                    if (string.contains ("of"))
                    {
                        String[] split = string.split ("of");
                        if (split.length == 2)
                        {
                            if (split[0].contains ("out"))
                            {
                                split[0] = split[0].substring (0, split[0].indexOf ("out"));
                                split[0] = split[0].replaceAll (" ", "");
                            }

                            try
                            {
                                double first = Double.parseDouble (split[0]);
                                if (first != 0)
                                {
                                    double second = Double.parseDouble (split[1]);
                                    double value = first / second;
                                    newString = Domain.PERCENT_FORMAT.format (value);
                                }
                                else
                                {
                                    newString = "0%";
                                }
                            }
                            catch (NumberFormatException innerEx)
                            {
                                Domain.LOGGER.add (ex);
                            }
                        }
                    }
                }
            }
        }

        if (newString != null && !newString.equals ("0%") && newString.split ("\\.")[1].equals ("00%"))
        {
            newString = newString.split ("\\.")[0] + "%";
        }

        return newString;
    }

    /**
     * Sets the start time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseStartHr(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labEndHrChooser.setValue (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMinChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (index);
            String time = Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ());
            course.setLabStartTime (0, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the start time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseStartMin(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labEndHrChooser.setValue (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMinChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (index);
            String time = Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ());
            course.setLabStartTime (1, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the start time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseStartM(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labEndHrChooser.setValue (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMinChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labEndMChooser.setValue (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (index);
            String time = Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ());
            course.setLabStartTime (2, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseEndHr(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labStartHrChooser.setValue (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMinChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ());
            course.setLabEndTime (0, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseEndMin(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labStartHrChooser.setValue (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMinChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ());
            course.setLabEndTime (1, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time for the lab of the currently selected course.
     *
     * @param index The currently selected course.
     */
    protected void setLabCourseEndM(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labStartMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.labStartHrChooser.setValue (viewPanel.termsAndCoursesDialog.labEndHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMinChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.labStartMChooser.setValue (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.labEndMChooser.getValue ());
            course.setLabEndTime (2, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the start time of the specified course with the value found in the
     * course start time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseStartHr(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.endHrChooser.setValue (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMinChooser.setValue (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMChooser.setValue (viewPanel.termsAndCoursesDialog.startMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            Course course = utility.courses.get (index);
            String time = Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ());
            course.setStartTime (0, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the start time of the specified course with the value found in the
     * course start time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseStartMin(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.endHrChooser.setValue (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMinChooser.setValue (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMChooser.setValue (viewPanel.termsAndCoursesDialog.startMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (index);
            String time = Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ());
            course.setStartTime (1, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the start time of the specified course with the value found in the
     * course start time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseStartM(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.endHrChooser.setValue (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMinChooser.setValue (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.endMChooser.setValue (viewPanel.termsAndCoursesDialog.startMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (index);
            String time = Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ());
            course.setStartTime (2, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time of the specified course with the value found in the
     * course end time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseEndHr(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.startHrChooser.setValue (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMinChooser.setValue (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMChooser.setValue (viewPanel.termsAndCoursesDialog.endMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ());
            course.setEndTime (0, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time of the specified course with the value found in the
     * course end time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseEndMin(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.startHrChooser.setValue (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMinChooser.setValue (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMChooser.setValue (viewPanel.termsAndCoursesDialog.endMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ());
            course.setEndTime (1, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the end time of the specified course with the value found in the
     * course end time spinner in the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseEndM(int index)
    {
        if (index != -1 && courseLoading.empty ())
        {
            try
            {
                Date startDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.startHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.startMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.startMChooser.getValue ()));
                Date endDate = Domain.TIME_FORMAT.parse (Domain.HR_FORMAT.format (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ()) + ":" + Domain.MIN_FORMAT.format (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ()) + " " + Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ()));
                if (startDate.after (endDate))
                {
                    viewPanel.termsAndCoursesDialog.startHrChooser.setValue (viewPanel.termsAndCoursesDialog.endHrChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMinChooser.setValue (viewPanel.termsAndCoursesDialog.endMinChooser.getValue ());
                    viewPanel.termsAndCoursesDialog.startMChooser.setValue (viewPanel.termsAndCoursesDialog.endMChooser.getValue ());
                }
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            Course course = utility.courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
            String time = Domain.M_FORMAT.format (viewPanel.termsAndCoursesDialog.endMChooser.getValue ());
            course.setEndTime (2, time);
            needsCoursesAndTermsSave = true;
        }
    }

    /**
     * Sets the comments of the specified assignment with the value found in the
     * assignment comments text area of the details right-hand side panel.
     *
     * @param index The index of the assignment.
     */
    protected void setAssignmentComments(int index)
    {
        try
        {
            if (index != -1
                && ((Assignment) utility.assignmentsAndEvents.get (index)).getCourse ().isChanged ()
                && assignmentOrEventLoading.empty ())
            {
                Assignment assignment = (Assignment) utility.assignmentsAndEvents.get (currentIndexFromVector);
                assignment.setComments (viewPanel.commentsTextArea.getText ());
            }
        }
        catch (ClassCastException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Sets the description of the specified event with the value found in the
     * event comments text area of the details right-hand side panel.
     *
     * @param index The index of the event.
     */
    protected void setEventDescription(int index)
    {
        try
        {
            if (index != -1
                && ((Event) utility.assignmentsAndEvents.get (index)).getEventYear ().isChanged ()
                && assignmentOrEventLoading.empty ())
            {
                viewPanel.eventChanges.push (true);

                Event event = (Event) utility.assignmentsAndEvents.get (currentIndexFromVector);

                ViewPanel.OPTION_PANE.setValue (null);

                event.setDescription (viewPanel.descriptionTextArea.getText ());
            }
        }
        catch (ClassCastException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Sets the name of the instructor for the specified course with the value
     * found in the instructor name text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setInstructorName(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && instructorLoading.empty ())
        {
            Instructor instructor = utility.courses.get (currentCourseIndex).getInstructor (index);
            String text = viewPanel.termsAndCoursesDialog.instructorNameTextField.getText ();
            if (text.replaceAll (" ", "").equals (""))
            {
                instructorLoading.push (true);
                viewPanel.termsAndCoursesDialog.instructorNameTextField.setText (instructor.getTypeName ());
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (language.getString ("instructorNameLeftEmpty"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel.termsAndCoursesDialog, language.getString ("invalidInstructorName"));
                optionDialog.setVisible (true);
                viewPanel.termsAndCoursesDialog.instructorNameTextField.requestFocus ();
                viewPanel.termsAndCoursesDialog.instructorNameTextField.selectAll ();
                instructorLoading.pop ();
            }
            else
            {
                instructor.setTypeName (viewPanel.termsAndCoursesDialog.instructorNameTextField.getText ());
                viewPanel.termsAndCoursesDialog.instructorTableModel.setValueAt (instructor.getTypeName (), index, 0);
                viewPanel.termsAndCoursesDialog.settingsInstructorsTable.refreshTable ();
            }
        }
    }

    /**
     * Sets the lab room location of the specified course with the value found
     * in the lab room location text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setLabRoomLocation(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            course.setLabRoomLocation (viewPanel.termsAndCoursesDialog.labRoomTextField.getText ());
        }
    }

    /**
     * Sets the room location of the specified course with the value found in
     * the room location text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setRoomLocation(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            course.setRoomLocation (viewPanel.termsAndCoursesDialog.roomTextField.getText ());
        }
    }

    /**
     * Sets the course number of the specified course with the value found in
     * the course number text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseNumber(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            course.setCourseNumber (viewPanel.termsAndCoursesDialog.courseNumberTextField.getText ());
        }
    }

    /**
     * Sets the course website of the specified course with the value found in
     * the course website text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setCourseWebsite(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            if (!viewPanel.termsAndCoursesDialog.courseWebsiteTextField.getText ().equals ("")
                && !viewPanel.termsAndCoursesDialog.courseWebsiteTextField.getText ().startsWith ("http://"))
            {
                viewPanel.termsAndCoursesDialog.courseWebsiteTextField.setText ("http://" + viewPanel.termsAndCoursesDialog.courseWebsiteTextField.getText ());
            }
            course.setCourseWebsite (viewPanel.termsAndCoursesDialog.courseWebsiteTextField.getText ());
        }
    }

    /**
     * Sets the lab website of the specified course with the value found in the
     * lab website text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setLabWebsite(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            if (!viewPanel.termsAndCoursesDialog.labWebsiteTextField.getText ().equals ("")
                && !viewPanel.termsAndCoursesDialog.labWebsiteTextField.getText ().startsWith ("http://"))
            {
                viewPanel.termsAndCoursesDialog.labWebsiteTextField.setText ("http://" + viewPanel.termsAndCoursesDialog.labWebsiteTextField.getText ());
            }
            course.setLabWebsite (viewPanel.termsAndCoursesDialog.labWebsiteTextField.getText ());
        }
    }

    /**
     * Sets the room location of the specified course with the value found in
     * the room location text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setLabNumber(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && courseLoading.empty ())
        {
            Course course = utility.courses.get (index);
            course.setLabNumber (viewPanel.termsAndCoursesDialog.labNumberTextField.getText ());
        }
    }

    /**
     * Sets the email of the instructor for the specified course with the value
     * found in the instructor email text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setInstructorEmail(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && instructorLoading.empty ())
        {
            Matcher matcher = Domain.EMAIL_PATTERN.matcher (viewPanel.termsAndCoursesDialog.instructorEmailTextField.getText ());
            Instructor instructor = utility.courses.get (currentCourseIndex).getInstructor (index);
            if (matcher.matches ())
            {
                instructor.setInstructorEmail (viewPanel.termsAndCoursesDialog.instructorEmailTextField.getText ());
                viewPanel.checkInstructorButtonState ();
            }
            else
            {
                if (viewPanel.termsAndCoursesDialog.instructorEmailTextField.getText ().replaceAll (" ", "").equals (""))
                {
                    instructor.setInstructorEmail (viewPanel.termsAndCoursesDialog.instructorEmailTextField.getText ());
                    viewPanel.checkInstructorButtonState ();
                }
                else
                {
                    viewPanel.termsAndCoursesDialog.instructorEmailTextField.requestFocus ();
                    viewPanel.termsAndCoursesDialog.instructorEmailTextField.selectAll ();
                }
            }
        }
    }

    /**
     * Sets the office hours of the instructor for the specified course with the
     * value found in the office hours text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setOfficeHours(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && instructorLoading.empty ())
        {
            Instructor instructor = utility.courses.get (currentCourseIndex).getInstructor (index);
            instructor.setOfficeHours (viewPanel.termsAndCoursesDialog.officeHoursTextField.getText ());
        }
    }

    /**
     * Sets the phone of the instructor for the specified course with the value
     * found in the phone text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setInstructorPhone(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && instructorLoading.empty ())
        {
            Instructor instructor = utility.courses.get (currentCourseIndex).getInstructor (index);
            instructor.setInstructorPhone (viewPanel.termsAndCoursesDialog.phoneTextField.getText ());
            viewPanel.checkInstructorButtonState ();
        }
    }

    /**
     * Sets the office location of the instructor for the specified course with
     * the value found in the office location text field of the Settings dialog.
     *
     * @param index The index of the course.
     */
    protected void setOfficeLocation(int index)
    {
        if (index != -1 && needsCoursesAndTermsSave && instructorLoading.empty ())
        {
            Instructor instructor = utility.courses.get (currentCourseIndex).getInstructor (index);
            instructor.setOfficeLocation (viewPanel.termsAndCoursesDialog.officeLocationTextField.getText ());
        }
    }

    /**
     * This method sorts the assignmentsAndEvents list through bubbling every
     * assignment up in the model, synchronizing the model with the
     * assignmentsAndEvents vector, and saving the data. Since the models
     * bubbling methods are called, the assignmentsAndEvents will sort by
     * whatever column was last selected for sorting.
     */
    protected void sortAssignmentsList()
    {
        assignmentOrEventLoading.push (true);

        long selectedID = -1;
        if (viewPanel.assignmentsTable.getSelectedRow () != -1)
        {
            try
            {
                selectedID = Long.parseLong (viewPanel.assignmentsTableModel.getValueAt (viewPanel.assignmentsTable.getSelectedRow (), 6).toString ());
            }
            catch (NullPointerException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }

        boolean sortAscending = viewPanel.assignmentsTableModel.isSortAscending ();
        boolean swapped = true;
        switch (viewPanel.assignmentsTableModel.getColumnSorting ())
        {
            // sort by "done" state
            case 0:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        ListItem firstItem = utility.assignmentsAndEvents.get (i);
                        ListItem secondItem = utility.assignmentsAndEvents.get (i + 1);
                        int first = -1;
                        int second = -1;
                        if (firstItem.isAssignment ())
                        {
                            if (((Assignment) firstItem).isDone ())
                            {
                                first = 0;
                            }
                            else
                            {
                                first = 1;
                            }
                        }
                        else
                        {
                            first = 2;
                        }
                        if (secondItem.isAssignment ())
                        {
                            if (((Assignment) secondItem).isDone ())
                            {
                                second = 0;
                            }
                            else
                            {
                                second = 1;
                            }
                        }
                        else
                        {
                            second = 2;
                        }

                        if ((sortAscending && first < second)
                            || (!sortAscending && first > second))
                        {
                            swap (i, i + 1);
                            viewPanel.assignmentsTableModel.swap (i, i + 1);
                            swapped = true;
                        }
                    }
                }
                break;
            }
            // sort by task name
            case 1:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        String first = utility.assignmentsAndEvents.get (i).getItemName ();
                        if (first == null)
                        {
                            first = "";
                        }
                        String second = utility.assignmentsAndEvents.get (i + 1).getItemName ();
                        if (second == null)
                        {
                            second = "";
                        }
                        if ((sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) < 0) || (!sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) > 0))
                        {
                            swap (i, i + 1);
                            viewPanel.assignmentsTableModel.swap (i, i + 1);
                            swapped = true;
                        }
                    }
                }
                break;
            }
            // sort by type name
            case 2:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        String first = "";
                        String second = "";
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i);
                            if (item.isAssignment ())
                            {
                                if (((Assignment) item).getType () != null)
                                {
                                    first = ((Assignment) item).getType ().getTypeName ();
                                }
                            }
                            else
                            {
                                first = ((Event) item).getRowObject ()[2].toString ();
                            }
                        }
                        catch (NullPointerException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i + 1);
                            if (item.isAssignment ())
                            {
                                if (((Assignment) item).getType () != null)
                                {
                                    second = ((Assignment) item).getType ().getTypeName ();
                                }
                            }
                            else
                            {
                                second = ((Event) item).getRowObject ()[2].toString ();
                            }
                        }
                        catch (NullPointerException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        if ((sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) < 0) || (!sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) > 0))
                        {
                            swap (i, i + 1);
                            viewPanel.assignmentsTableModel.swap (i, i + 1);
                            swapped = true;
                        }
                    }
                }
                break;
            }
            // sort by course name
            case 3:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        String first = "";
                        String second = "";
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i);
                            if (item.isAssignment ())
                            {
                                first = ((Assignment) item).getCourse ().getTypeName ();
                            }
                        }
                        catch (NullPointerException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i + 1);
                            if (item.isAssignment ())
                            {
                                second = ((Assignment) item).getCourse ().getTypeName ();
                            }
                        }
                        catch (NullPointerException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        if ((sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) < 0) || (!sortAscending && second.toLowerCase ().compareTo (first.toLowerCase ()) > 0))
                        {
                            swap (i, i + 1);
                            viewPanel.assignmentsTableModel.swap (i, i + 1);
                            swapped = true;
                        }
                    }
                }
                break;
            }
            // sort by due date
            case 4:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        try
                        {
                            ListItem first = utility.assignmentsAndEvents.get (i);
                            ListItem second = utility.assignmentsAndEvents.get (i + 1);
                            Date firstDate = null;
                            Date secondDate = null;
                            if (first.isAssignment ())
                            {
                                firstDate = Domain.DATE_AND_TIME_FORMAT.parse (first.getDueDate () + " " + ((Assignment) first).getDueTime ());
                            }
                            else
                            {
                                String startTime = "12:00 AM";
                                if (!((Event) first).isAllDay ())
                                {
                                    startTime = ((Event) first).getStartTime (0) + ":" + ((Event) first).getStartTime (1) + " " + ((Event) first).getStartTime (2);
                                }
                                firstDate = Domain.DATE_AND_TIME_FORMAT.parse (first.getDueDate () + " " + startTime);
                            }
                            if (second.isAssignment ())
                            {
                                secondDate = Domain.DATE_AND_TIME_FORMAT.parse (second.getDueDate () + " " + ((Assignment) second).getDueTime ());
                            }
                            else
                            {
                                String startTime = "12:00 AM";
                                if (!((Event) second).isAllDay ())
                                {
                                    startTime = ((Event) second).getStartTime (0) + ":" + ((Event) second).getStartTime (1) + " " + ((Event) second).getStartTime (2);
                                }
                                secondDate = Domain.DATE_AND_TIME_FORMAT.parse (second.getDueDate () + " " + startTime);
                            }

                            if ((sortAscending && firstDate.after (secondDate)) || (!sortAscending && firstDate.before (secondDate))
                                || (firstDate.compareTo (secondDate) == 0 && sortAscending && first.isAssignment () && !second.isAssignment ())
                                || (firstDate.compareTo (secondDate) == 0 && !sortAscending && !first.isAssignment () && second.isAssignment ()))
                            {
                                swap (i, i + 1);
                                viewPanel.assignmentsTableModel.swap (i, i + 1);
                                swapped = true;
                            }
                        }
                        catch (ParseException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                    }
                }
                break;
            }
            // sort by grade
            case 5:
            {
                while (swapped)
                {
                    swapped = false;
                    for (int i = 0; i < utility.assignmentsAndEvents.size () - 1; ++i)
                    {
                        double first = -1;
                        double second = -1;
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i);
                            if (item.isAssignment ())
                            {
                                if (!((Assignment) item).getGrade ().equals (""))
                                {
                                    first = Double.parseDouble (((Assignment) item).getGrade ().replaceAll ("%", ""));
                                }
                            }
                            else
                            {
                                first = -2;
                            }
                        }
                        catch (NumberFormatException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        try
                        {
                            ListItem item = utility.assignmentsAndEvents.get (i + 1);
                            if (item.isAssignment ())
                            {
                                if (!((Assignment) item).getGrade ().equals (""))
                                {
                                    second = Double.parseDouble (((Assignment) item).getGrade ().replaceAll ("%", ""));
                                }
                            }
                            else
                            {
                                second = -2;
                            }
                        }
                        catch (NumberFormatException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                        if ((sortAscending && first > second) || (!sortAscending && first < second))
                        {
                            swap (i, i + 1);
                            viewPanel.assignmentsTableModel.swap (i, i + 1);
                            swapped = true;
                        }
                    }
                }
                break;
            }
        }
        viewPanel.assignmentsTable.refreshTable ();
        if (selectedID != -1)
        {
            viewPanel.ignoreTableSelection = true;
            viewPanel.assignmentsTable.setSelectedRowFromVectorIndex (utility.getAssignmentOrEventIndexByID (selectedID));
            viewPanel.ignoreTableSelection = false;
        }

        assignmentOrEventLoading.pop ();
    }

    /**
     * Swaps to locations in the assignmentsAndEvents vector.
     *
     * @param first The first swap index.
     * @param second The second swap index.
     */
    private void swap(int first, int second)
    {
        ListItem temp = utility.assignmentsAndEvents.get (second);
        utility.assignmentsAndEvents.set (second, utility.assignmentsAndEvents.get (first));
        utility.assignmentsAndEvents.set (first, temp);
    }

    /**
     * Refreshes all displayed information in the assignmentsAndEvents list.
     */
    protected void refreshAssignmentsList()
    {
        for (int i = 0; i < viewPanel.assignmentsTableModel.getRowCount (); ++i)
        {
            for (int j = 0; j < viewPanel.assignmentsTableModel.getColumnCount (); ++j)
            {
                viewPanel.assignmentsTableModel.setRow (utility.assignmentsAndEvents.get (i).getRowObject (), i);
            }
        }
        try
        {
            viewPanel.assignmentsTable.refreshTable ();
        }
        catch (NullPointerException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Refreshes all shown assignmentsAndEvents and events in Calendar View.
     */
    protected void refreshShownAssignmentsAndEvents()
    {
        for (int i = 0; i < viewPanel.shownAssignments.size (); ++i)
        {
            viewPanel.shownAssignments.get (i).refreshText ();
        }
        for (int i = 0; i < viewPanel.shownEvents.size (); ++i)
        {
            viewPanel.shownEvents.get (i).refreshText ();
        }
    }

    /**
     * Calculates the grade for the given term.
     *
     * @param term The term to calculate the grade for.
     * @return The current grade for the given term.
     */
    protected double calculateGradeForTerm(Term term)
    {
        try
        {
            double total = 0;
            int courseCount = 0;
            for (int i = 0; i < term.getCourseCount (); ++i)
            {
                double grade = calculateGradeForCourse (term.getCourse (i));
                if (grade != -1)
                {
                    total += grade;
                    ++courseCount;
                }
            }
            if (courseCount == 0)
            {
                return -1;
            }
            return total / courseCount;
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }

    /**
     * Calculates the grade for the given course.
     *
     * @param course The course to calculate the grade for.
     * @return The current grade for the given course.
     */
    protected double calculateGradeForCourse(Course course)
    {
        try
        {
            course.resetTypeGrades ();
            course.resetGradesList ();
            for (int i = 0; i < course.getAssignmentCount (); ++i)
            {
                Assignment assignment = course.getAssignment (i);
                if (assignment.isDone () && !assignment.getGrade ().replaceAll (" ", "").equals (""))
                {
                    if (assignment.getType () != null && !assignment.getType ().getWeight ().replaceAll (" ", "").equals (""))
                    {
                        assignment.getType ().addGrade (Double.parseDouble (assignment.getGrade ().replaceAll ("%", "")));
                        try
                        {
                            course.addGrade (Double.parseDouble (assignment.getGrade ().replaceAll ("%", "")), assignment.getType (), Domain.DATE_AND_TIME_FORMAT.parse (assignment.getDueDate () + " " + assignment.getDueTime (0) + ":" + assignment.getDueTime (1) + " " + assignment.getDueTime (2)).getTime ());
                        }
                        catch (ParseException ex)
                        {
                            Domain.LOGGER.add (ex);
                        }
                    }
                }
            }
            course.sortGradesListByTime ();

            double overallGrade = course.getTypeGrades () / course.getTypePercentComplete ();
            if (overallGrade >= 0)
            {
                return overallGrade;
            }
            else
            {
                return -1;
            }
        }
        catch (NumberFormatException ex)
        {
            return -1;
        }
    }

    /**
     * Enable or disable the indeterminate state of the progress bar. Must be
     * set to false if you wish to set values manually.
     *
     * @param progressBar The progress bar to set the state/value for.
     * @param indeterminate True if the progress bar should be set
     * indeterminate, false otherwise.
     * @param string The string to paint in the progress bar.
     * @param stringPainted True if the string should be painted, false
     * otherwise.
     * @param value The value to set the progress bar to. -1 if no value should
     * be set. This will only set it indeterminate is set to false.
     */
    public void setProgressState(final JProgressBar progressBar, final boolean indeterminate, final String string, final boolean stringPainted, final int value)
    {
        SwingUtilities.invokeLater (new Runnable ()
        {
            @Override
            public void run()
            {
                progressBar.setIndeterminate (indeterminate);
                progressBar.setStringPainted (stringPainted);
                progressBar.setString (string);
                if (!indeterminate && value != -1)
                {
                    progressBar.setValue (value);
                }
            }
        });
    }

    /**
     * The minimum value a progress can be. Minimum has been set to 0 by
     * default, so only use this accessor if you want to change that value.
     *
     * @param progressBar The progress bar to set the state/value for.
     * @param minValue The minimum value of the progress bar.
     */
    public void setProgressMinValue(final JProgressBar progressBar, int minValue)
    {
        progressBar.setMinimum (minValue);
    }

    /**
     * The maximum value a progress can be. Maximum has been set to 100 by
     * default, so only use this accessor if you want to change that value.
     *
     * @param progressBar The progress bar to set the state/value for.
     * @param maxValue The maximum value of the progress bar.
     */
    public void setProgressMaxValue(JProgressBar progressBar, int maxValue)
    {
        progressBar.setMaximum (maxValue);
    }
}
