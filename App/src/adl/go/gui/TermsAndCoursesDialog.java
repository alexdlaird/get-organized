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

import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;
import adl.go.gui.ColoredComponent.GradientStyle;
import adl.go.gui.colorpicker.ColorPicker;
import adl.go.types.AssignmentType;
import adl.go.types.Course;
import adl.go.types.Instructor;
import adl.go.types.Term;
import adl.go.types.Textbook;

/**
 * The Terms and Courses dialog.
 *
 * @author Alex Laird
 */
public class TermsAndCoursesDialog extends EscapeDialog
{
    /**
     * The model for the terms displayed in the combo box in the Terms and
     * Courses dialog.
     */
    public DefaultComboBoxModel termComboModel = new DefaultComboBoxModel ();
    /**
     * The model for the terms displayed in the table in the Terms and Courses
     * dialog.
     */
    public ExtendedSettingsTableModel termTableModel;
    /**
     * The model for the courses displayed in the table in the Terms and Courses
     * dialog.
     */
    public ExtendedSettingsTableModel courseTableModel;
    /**
     * The model for the textbooks displayed in the table in the Terms and
     * Courses dialog.
     */
    public ExtendedSettingsTableModel textbookTableModel;
    /**
     * The model for the types displayed in the table in the Terms and Courses
     * dialog.
     */
    public ExtendedSettingsTableModel typeTableModel;
    /**
     * The model for the instructors displayed in the table in the Terms and
     * Courses dialog.
     */
    public ExtendedSettingsTableModel instructorTableModel;

    /**
     * Construct the Terms and Courses dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public TermsAndCoursesDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);

        termTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    ""
                }, viewPanel);
        courseTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    "Course", "Term"
                }, viewPanel);
        textbookTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    ""
                }, viewPanel);
        typeTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    "Name", "Weight"
                }, viewPanel);
        instructorTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    "Name", "Lecture/Lab"
                }, viewPanel);

        initComponents ();

        settingsTermsTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsTermsTableRowSelected (evt);
            }
        });
        settingsCoursesTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsCoursesTableRowSelected (evt);
            }
        });
        settingsTypesTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsTypesTableRowSelected (evt);
            }
        });
        settingsInstructorsTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsInstructorsTableRowSelected (evt);
            }
        });
        settingsTextbooksTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsTextbooksTableRowSelected (evt);
            }
        });

        termDetailsPanel.setVisible (false);
        courseTabbedPane.setVisible (false);
        addCourseButton.setEnabled (false);
    }

    /**
     * Initialize the Terms and Courses dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("termsAndCourses"));
        termsAndCoursesUpperJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowTopBackground1);
        termsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        coursesPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        courseInnerDetailsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        instructorsDetailsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        typesDetailsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        textbooksDetailsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);

        termsAndCoursesLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        termsAndCoursesCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        termsAndCoursesCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termsAndCoursesTabbedPane.setFont (viewPanel.domain.utility.currentTheme.fontPlain11);
        noTermsTermLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        termDetailsNoLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        termStartDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        termEndDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        termStartDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termEndDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        addCourseToTermButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addCourseToTermButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addTermButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addTermButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        removeTermButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeTermButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termsListLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        moveTermDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTermDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveTermUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTermUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsTermsTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseDetailsNoLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        noTermsYetLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseTabbedPane.setFont (viewPanel.domain.utility.currentTheme.fontPlain11);
        sunToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        sunToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        satToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        monToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        tueToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        wedToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        thuToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        friToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        courseDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseColorLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        satToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseStartDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        roomLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        onlineCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        creditsSpinner.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        daysLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        courseEndDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        monToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        termsComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseStartDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        courseNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        tueToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        assignedToTermLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        thuToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseEndDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        friToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        roomTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseTimeLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        creditsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        wedToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseHasLabCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        labSatToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labFriToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labThuToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labSunToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labMonToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labTueToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labWedToggleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labSunToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labSatToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labMonToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labTueToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labWedToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labThuToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labFriToggleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        labDaysLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labEndDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labEndDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labStartDateChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labStartDateLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labTimeLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labOnlineCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labRoomTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labRoomLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labCreditsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labCreditsSpinner.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        startHrChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        startMinChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        startMChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        endMinChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        endHrChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        endMChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labStartHrChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labStartMinChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labStartMChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labEndMinChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labEndHrChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labEndMChooser.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseColon1.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseColon2.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseColon4.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseColon3.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        courseNumberLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        courseNumberTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labNumberLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labNumberTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        instructorsListLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        addInstructorButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addInstructorButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        removeInstructorButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeInstructorButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveInstructorUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveInstructorUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveInstructorDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveInstructorDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsInstructorsTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        instructorDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        instructorNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        instructorEmailTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        instructorEmailLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        phoneLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        phoneTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        officeHoursTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        officeHoursLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        officeLocationTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        officeLocationLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        emailInstructorButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        emailInstructorButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        lectureRadioButton.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labRadioButton.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        bothRadioButton.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        typesListLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        typeDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        typeNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        typeWeightLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        weightTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addTypeButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addTypeButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        removeTypeButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeTypeButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveTypeUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTypeUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveTypeDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTypeDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsTypesTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        textbooksListLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        textbookNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        textbookDetailsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        isbnLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        isbnTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        authorLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        authorTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        publisherLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        publisherTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        onlineSourceLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        textbookSourceTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        purchasePrice.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        conditionLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        purchasePriceTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        conditionTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        visitTextbookSourceButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        visitTextbookSourceButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        contactEmailLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        contactEmailTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        orderedCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        receivedCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        contactSendEmailButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        contactSendEmailButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        searchGoogleButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        searchGoogleButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addTextbookButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addTextbookButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        removeTextbookButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeTextbookButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveTextbookUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTextbookUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsTextbooksTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveTextbookDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveTextbookDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseListLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        removeCourseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeCourseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addCourseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addCourseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveCourseUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveCourseUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveCourseDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveCourseDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsCoursesTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        courseWebsiteLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        courseWebsiteTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        visitCourseWebsiteButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        visitCourseWebsiteButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        labWebsiteLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        labWebsiteTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        visitLabWebsiteButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        visitLabWebsiteButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        instructorButtonGroup = new javax.swing.ButtonGroup();
        termsAndCoursesJPanel = new javax.swing.JPanel();
        termsAndCoursesUpperJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        termsAndCoursesLabel = new javax.swing.JLabel();
        termsAndCoursesCloseButton = new javax.swing.JButton();
        termsAndCoursesTabbedPane = new javax.swing.JTabbedPane();
        termsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        termDetailsCardPanel = new javax.swing.JPanel();
        noTermDetailsPanel = new javax.swing.JPanel();
        noTermsTermLabel = new javax.swing.JLabel();
        termDetailsNoLabel = new javax.swing.JLabel();
        termDetailsPanel = new javax.swing.JPanel();
        termStartDateLabel = new javax.swing.JLabel();
        termEndDateLabel = new javax.swing.JLabel();
        termStartDateChooser = new com.toedter.calendar.JDateChooser();
        termEndDateChooser = new com.toedter.calendar.JDateChooser();
        termDetailsLabel = new javax.swing.JLabel();
        addCourseToTermButton = new javax.swing.JButton();
        termNameTextField = new javax.swing.JTextField();
        termsListPanel = new javax.swing.JPanel();
        addTermButton = new javax.swing.JButton();
        removeTermButton = new javax.swing.JButton();
        termsListLabel = new javax.swing.JLabel();
        moveTermDownButton = new javax.swing.JButton();
        moveTermUpButton = new javax.swing.JButton();
        settingsTermsScrollPane = new javax.swing.JScrollPane();
        settingsTermsTable = new adl.go.gui.ExtendedJTable();
        coursesPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        courseDetailsCardPanel = new javax.swing.JPanel();
        noCourseDetailsPanel = new javax.swing.JPanel();
        courseDetailsNoLabel = new javax.swing.JLabel();
        noTermsYetLabel = new javax.swing.JLabel();
        courseTabbedPane = new javax.swing.JTabbedPane();
        courseScrollPane = new javax.swing.JScrollPane();
        courseInnerDetailsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        sunToggleButton = new javax.swing.JToggleButton();
        courseDetailsLabel = new javax.swing.JLabel();
        courseColorLabel = new javax.swing.JLabel();
        satToggleButton = new javax.swing.JToggleButton();
        courseStartDateChooser = new com.toedter.calendar.JDateChooser();
        roomLabel = new javax.swing.JLabel();
        onlineCheckBox = new javax.swing.JCheckBox();
        creditsSpinner = new javax.swing.JSpinner();
        daysLabel = new javax.swing.JLabel();
        courseEndDateLabel = new javax.swing.JLabel();
        monToggleButton = new javax.swing.JToggleButton();
        termsComboBox = new javax.swing.JComboBox();
        courseStartDateLabel = new javax.swing.JLabel();
        courseNameTextField = new javax.swing.JTextField();
        tueToggleButton = new javax.swing.JToggleButton();
        assignedToTermLabel = new javax.swing.JLabel();
        thuToggleButton = new javax.swing.JToggleButton();
        courseEndDateChooser = new com.toedter.calendar.JDateChooser();
        friToggleButton = new javax.swing.JToggleButton();
        roomTextField = new javax.swing.JTextField();
        courseTimeLabel = new javax.swing.JLabel();
        creditsLabel = new javax.swing.JLabel();
        wedToggleButton = new javax.swing.JToggleButton();
        courseHasLabCheckBox = new javax.swing.JCheckBox();
        labDetailsLabel = new javax.swing.JLabel();
        labSatToggleButton = new javax.swing.JToggleButton();
        labFriToggleButton = new javax.swing.JToggleButton();
        labThuToggleButton = new javax.swing.JToggleButton();
        labSunToggleButton = new javax.swing.JToggleButton();
        labMonToggleButton = new javax.swing.JToggleButton();
        labTueToggleButton = new javax.swing.JToggleButton();
        labWedToggleButton = new javax.swing.JToggleButton();
        labDaysLabel = new javax.swing.JLabel();
        labEndDateLabel = new javax.swing.JLabel();
        labEndDateChooser = new com.toedter.calendar.JDateChooser();
        labStartDateChooser = new com.toedter.calendar.JDateChooser();
        labStartDateLabel = new javax.swing.JLabel();
        labTimeLabel = new javax.swing.JLabel();
        labOnlineCheckBox = new javax.swing.JCheckBox();
        labRoomTextField = new javax.swing.JTextField();
        labRoomLabel = new javax.swing.JLabel();
        labCreditsLabel = new javax.swing.JLabel();
        labCreditsSpinner = new javax.swing.JSpinner();
        startHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        startMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        startMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        endMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        endHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        endMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labStartHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labStartMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labStartMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labEndMinChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labEndHrChooser = new com.toedter.calendar.JSpinnerDateEditor();
        labEndMChooser = new com.toedter.calendar.JSpinnerDateEditor();
        courseColon1 = new javax.swing.JLabel();
        courseColon2 = new javax.swing.JLabel();
        courseColon4 = new javax.swing.JLabel();
        courseColon3 = new javax.swing.JLabel();
        courseColorPanel = new javax.swing.JPanel();
        courseNumberLabel = new javax.swing.JLabel();
        courseNumberTextField = new javax.swing.JTextField();
        labNumberLabel = new javax.swing.JLabel();
        labNumberTextField = new javax.swing.JTextField();
        courseWebsiteLabel = new javax.swing.JLabel();
        courseWebsiteTextField = new javax.swing.JTextField();
        visitCourseWebsiteButton = new javax.swing.JButton();
        labWebsiteLabel = new javax.swing.JLabel();
        labWebsiteTextField = new javax.swing.JTextField();
        visitLabWebsiteButton = new javax.swing.JButton();
        instructorsScrollPane = new javax.swing.JScrollPane();
        instructorsDetailsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        instructorsListLabel = new javax.swing.JLabel();
        addInstructorButton = new javax.swing.JButton();
        removeInstructorButton = new javax.swing.JButton();
        moveInstructorUpButton = new javax.swing.JButton();
        moveInstructorDownButton = new javax.swing.JButton();
        settingsTypesScrollPane1 = new javax.swing.JScrollPane();
        settingsInstructorsTable = new adl.go.gui.ExtendedJTable();
        instructorDetailsPanel = new javax.swing.JPanel();
        instructorDetailsLabel = new javax.swing.JLabel();
        instructorNameTextField = new javax.swing.JTextField();
        instructorEmailTextField = new javax.swing.JTextField();
        instructorEmailLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        officeHoursTextField = new javax.swing.JTextField();
        officeHoursLabel = new javax.swing.JLabel();
        officeLocationTextField = new javax.swing.JTextField();
        officeLocationLabel = new javax.swing.JLabel();
        emailInstructorButton = new javax.swing.JButton();
        lectureRadioButton = new javax.swing.JRadioButton();
        labRadioButton = new javax.swing.JRadioButton();
        bothRadioButton = new javax.swing.JRadioButton();
        typesDetailsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        typesListLabel = new javax.swing.JLabel();
        typeDetailsPanel = new javax.swing.JPanel();
        typeDetailsLabel = new javax.swing.JLabel();
        typeNameTextField = new javax.swing.JTextField();
        typeWeightLabel = new javax.swing.JLabel();
        weightTextField = new javax.swing.JTextField();
        addTypeButton = new javax.swing.JButton();
        removeTypeButton = new javax.swing.JButton();
        moveTypeUpButton = new javax.swing.JButton();
        moveTypeDownButton = new javax.swing.JButton();
        settingsTypesScrollPane = new javax.swing.JScrollPane();
        settingsTypesTable = new ExtendedJTable();
        textbooksScrollPane = new javax.swing.JScrollPane();
        textbooksDetailsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        textbooksListLabel = new javax.swing.JLabel();
        textbookDetailsPanel = new javax.swing.JPanel();
        textbookNameTextField = new javax.swing.JTextField();
        textbookDetailsLabel = new javax.swing.JLabel();
        isbnLabel = new javax.swing.JLabel();
        isbnTextField = new javax.swing.JTextField();
        authorLabel = new javax.swing.JLabel();
        authorTextField = new javax.swing.JTextField();
        publisherLabel = new javax.swing.JLabel();
        publisherTextField = new javax.swing.JTextField();
        onlineSourceLabel = new javax.swing.JLabel();
        textbookSourceTextField = new javax.swing.JTextField();
        purchasePrice = new javax.swing.JLabel();
        conditionLabel = new javax.swing.JLabel();
        purchasePriceTextField = new javax.swing.JTextField();
        conditionTextField = new javax.swing.JTextField();
        visitTextbookSourceButton = new javax.swing.JButton();
        contactEmailLabel = new javax.swing.JLabel();
        contactEmailTextField = new javax.swing.JTextField();
        orderedCheckBox = new javax.swing.JCheckBox();
        receivedCheckBox = new javax.swing.JCheckBox();
        contactSendEmailButton = new javax.swing.JButton();
        searchGoogleButton = new javax.swing.JButton();
        addTextbookButton = new javax.swing.JButton();
        removeTextbookButton = new javax.swing.JButton();
        moveTextbookUpButton = new javax.swing.JButton();
        settingsTextbooksScrollPane = new javax.swing.JScrollPane();
        settingsTextbooksTable = new adl.go.gui.ExtendedJTable();
        moveTextbookDownButton = new javax.swing.JButton();
        courseListPanel = new javax.swing.JPanel();
        courseListLabel = new javax.swing.JLabel();
        removeCourseButton = new javax.swing.JButton();
        addCourseButton = new javax.swing.JButton();
        moveCourseUpButton = new javax.swing.JButton();
        moveCourseDownButton = new javax.swing.JButton();
        settingsCoursesScrollPane = new javax.swing.JScrollPane();
        settingsCoursesTable = new adl.go.gui.ExtendedJTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        termsAndCoursesUpperJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        termsAndCoursesUpperJPanel.setPreferredSize(new java.awt.Dimension(970, 49));

        termsAndCoursesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/terms_courses_mini.png"))); // NOI18N
        termsAndCoursesLabel.setText(viewPanel.domain.language.getString ("termsAndCourses"));

        termsAndCoursesCloseButton.setText(viewPanel.domain.language.getString ("close"));
        termsAndCoursesCloseButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
        termsAndCoursesCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termsAndCoursesCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout termsAndCoursesUpperJPanelLayout = new javax.swing.GroupLayout(termsAndCoursesUpperJPanel);
        termsAndCoursesUpperJPanel.setLayout(termsAndCoursesUpperJPanelLayout);
        termsAndCoursesUpperJPanelLayout.setHorizontalGroup(
            termsAndCoursesUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termsAndCoursesUpperJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(termsAndCoursesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 761, Short.MAX_VALUE)
                .addComponent(termsAndCoursesCloseButton)
                .addContainerGap())
        );
        termsAndCoursesUpperJPanelLayout.setVerticalGroup(
            termsAndCoursesUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termsAndCoursesUpperJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(termsAndCoursesUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(termsAndCoursesLabel)
                    .addComponent(termsAndCoursesCloseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        termsAndCoursesTabbedPane.setPreferredSize(new java.awt.Dimension(970, 546));
        termsAndCoursesTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                termsAndCoursesTabbedPaneStateChanged(evt);
            }
        });

        termsPanel.setLayout(new java.awt.GridLayout(1, 2));

        termDetailsCardPanel.setOpaque(false);
        termDetailsCardPanel.setLayout(new java.awt.CardLayout());

        noTermDetailsPanel.setOpaque(false);

        noTermsTermLabel.setText("<html>" + viewPanel.domain.language.getString ("noTermsAdded") + "</html>");

        termDetailsNoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        termDetailsNoLabel.setText(viewPanel.domain.language.getString ("termDetails"));
        termDetailsNoLabel.setMinimumSize(new java.awt.Dimension(324, 14));

        javax.swing.GroupLayout noTermDetailsPanelLayout = new javax.swing.GroupLayout(noTermDetailsPanel);
        noTermDetailsPanel.setLayout(noTermDetailsPanelLayout);
        noTermDetailsPanelLayout.setHorizontalGroup(
            noTermDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noTermDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(noTermDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(termDetailsNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(noTermsTermLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                .addContainerGap())
        );
        noTermDetailsPanelLayout.setVerticalGroup(
            noTermDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noTermDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(termDetailsNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noTermsTermLabel)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        termDetailsCardPanel.add(noTermDetailsPanel, "card3");

        termDetailsPanel.setOpaque(false);

        termStartDateLabel.setText(viewPanel.domain.language.getString ("startDate") + ":");

        termEndDateLabel.setText(viewPanel.domain.language.getString ("endDate") + ":");

        termStartDateChooser.setToolTipText(viewPanel.domain.language.getString ("termStartDateToolTip"));
        termStartDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                termStartDateChooserPropertyChange(evt);
            }
        });

        termEndDateChooser.setToolTipText(viewPanel.domain.language.getString ("termEndDateToolTip"));
        termEndDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                termEndDateChooserPropertyChange(evt);
            }
        });

        termDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        termDetailsLabel.setText(viewPanel.domain.language.getString ("termDetails"));
        termDetailsLabel.setMinimumSize(new java.awt.Dimension(324, 14));

        addCourseToTermButton.setText(viewPanel.domain.language.getString ("addCourseToTerm"));
        addCourseToTermButton.setToolTipText(viewPanel.domain.language.getString ("addCourseToTermToolTip"));
        addCourseToTermButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseToTermButtonActionPerformed(evt);
            }
        });

        termNameTextField.setToolTipText(viewPanel.domain.language.getString ("termNameToolTip"));
        termNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                termNameTextFieldActionPerformed(evt);
            }
        });
        termNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                termNameTextFieldtextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                termNameTextFieldFocusLost(evt);
            }
        });
        termNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                termNameTextFieldsettingsChangesMade(evt);
            }
        });

        javax.swing.GroupLayout termDetailsPanelLayout = new javax.swing.GroupLayout(termDetailsPanel);
        termDetailsPanel.setLayout(termDetailsPanelLayout);
        termDetailsPanelLayout.setHorizontalGroup(
            termDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(termDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(termDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(termNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(termDetailsPanelLayout.createSequentialGroup()
                        .addComponent(termStartDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(termStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(termDetailsPanelLayout.createSequentialGroup()
                        .addComponent(termEndDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(termEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addCourseToTermButton))
                .addContainerGap())
        );
        termDetailsPanelLayout.setVerticalGroup(
            termDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(termDetailsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(termNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(termDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(termStartDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(termStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(termDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(termEndDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(termEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addCourseToTermButton)
                .addContainerGap(243, Short.MAX_VALUE))
        );

        termDetailsCardPanel.add(termDetailsPanel, "card2");

        termsPanel.add(termDetailsCardPanel);

        termsListPanel.setOpaque(false);

        addTermButton.setText(viewPanel.domain.language.getString ("add"));
        addTermButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addTermButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTermButtonActionPerformed(evt);
            }
        });

        removeTermButton.setText(viewPanel.domain.language.getString ("remove"));
        removeTermButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeTermButton.setEnabled(false);
        removeTermButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTermButtonActionPerformed(evt);
            }
        });

        termsListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        termsListLabel.setText(viewPanel.domain.language.getString ("termsList"));

        moveTermDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveTermDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveTermDownButton.setEnabled(false);
        moveTermDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTermDownButtonActionPerformed(evt);
            }
        });

        moveTermUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveTermUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveTermUpButton.setEnabled(false);
        moveTermUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTermUpButtonActionPerformed(evt);
            }
        });

        settingsTermsTable.setModel(termTableModel);
        settingsTermsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsTermsScrollPane.setViewportView(settingsTermsTable);

        javax.swing.GroupLayout termsListPanelLayout = new javax.swing.GroupLayout(termsListPanel);
        termsListPanel.setLayout(termsListPanelLayout);
        termsListPanelLayout.setHorizontalGroup(
            termsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termsListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(termsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(termsListPanelLayout.createSequentialGroup()
                        .addComponent(moveTermUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(moveTermDownButton))
                    .addComponent(termsListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(settingsTermsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(termsListPanelLayout.createSequentialGroup()
                        .addComponent(addTermButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(removeTermButton)))
                .addContainerGap())
        );
        termsListPanelLayout.setVerticalGroup(
            termsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termsListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(termsListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTermsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(termsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTermButton)
                    .addComponent(removeTermButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(termsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveTermUpButton)
                    .addComponent(moveTermDownButton))
                .addGap(671, 671, 671))
        );

        termsPanel.add(termsListPanel);

        termsAndCoursesTabbedPane.addTab("Terms", termsPanel);

        coursesPanel.setLayout(new java.awt.GridLayout(1, 2));

        courseDetailsCardPanel.setOpaque(false);
        courseDetailsCardPanel.setLayout(new java.awt.CardLayout());

        noCourseDetailsPanel.setOpaque(false);

        courseDetailsNoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        courseDetailsNoLabel.setText(viewPanel.domain.language.getString ("courseDetails"));
        courseDetailsNoLabel.setMinimumSize(new java.awt.Dimension(324, 14));

        noTermsYetLabel.setText("<html>" + viewPanel.domain.language.getString ("noTermsCourseTab") + "</html>");

        javax.swing.GroupLayout noCourseDetailsPanelLayout = new javax.swing.GroupLayout(noCourseDetailsPanel);
        noCourseDetailsPanel.setLayout(noCourseDetailsPanelLayout);
        noCourseDetailsPanelLayout.setHorizontalGroup(
            noCourseDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noCourseDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(noCourseDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseDetailsNoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addComponent(noTermsYetLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE))
                .addContainerGap())
        );
        noCourseDetailsPanelLayout.setVerticalGroup(
            noCourseDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noCourseDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(courseDetailsNoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(noTermsYetLabel)
                .addContainerGap(354, Short.MAX_VALUE))
        );

        courseDetailsCardPanel.add(noCourseDetailsPanel, "card3");

        courseScrollPane.setBackground(coursesPanel.getBackground ());
        courseScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        courseInnerDetailsPanel.setPreferredSize(new java.awt.Dimension(450, 800));

        sunToggleButton.setText(viewPanel.domain.language.getString ("sun"));
        sunToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sunToggleButtonActionPerformed(evt);
            }
        });
        sunToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        courseDetailsLabel.setText(viewPanel.domain.language.getString ("courseDetails"));

        courseColorLabel.setText(viewPanel.domain.language.getString ("color") + ":");

        satToggleButton.setText(viewPanel.domain.language.getString ("sat"));
        satToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                satToggleButtonActionPerformed(evt);
            }
        });
        satToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseStartDateChooser.setToolTipText(viewPanel.domain.language.getString ("courseStartDateToolTip"));
        courseStartDateChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });
        courseStartDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                courseStartDateChooserPropertyChange(evt);
            }
        });

        roomLabel.setText(viewPanel.domain.language.getString ("room") + ":");

        onlineCheckBox.setText(viewPanel.domain.language.getString ("online"));
        onlineCheckBox.setToolTipText(viewPanel.domain.language.getString ("courseOnlineToolTip"));
        onlineCheckBox.setOpaque(false);
        onlineCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineCheckBoxActionPerformed(evt);
            }
        });
        onlineCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        creditsSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));
        creditsSpinner.setToolTipText(viewPanel.domain.language.getString ("courseCreditsToolTip"));
        creditsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                creditsSpinnerStateChanged(evt);
            }
        });
        creditsSpinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        daysLabel.setText(viewPanel.domain.language.getString ("classDays") + ":");

        courseEndDateLabel.setText(viewPanel.domain.language.getString ("endDate") + ":");

        monToggleButton.setText(viewPanel.domain.language.getString ("mon"));
        monToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monToggleButtonActionPerformed(evt);
            }
        });
        monToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        termsComboBox.setBackground(new java.awt.Color(246, 245, 245));
        termsComboBox.setModel(termComboModel);
        termsComboBox.setToolTipText(viewPanel.domain.language.getString ("inTermToolTip"));
        termsComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                termsComboBoxItemStateChanged(evt);
            }
        });
        termsComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseStartDateLabel.setText(viewPanel.domain.language.getString ("startDate") + ":");

        courseNameTextField.setToolTipText("The name of the course");
        courseNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseNameTextFieldActionPerformed(evt);
            }
        });
        courseNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                courseNameTextFieldFocusLost(evt);
            }
        });
        courseNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                courseNameTextFieldsettingsChangesMade(evt);
            }
        });

        tueToggleButton.setText(viewPanel.domain.language.getString ("tue"));
        tueToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tueToggleButtonActionPerformed(evt);
            }
        });
        tueToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        assignedToTermLabel.setText(viewPanel.domain.language.getString ("inTerm") + ":");

        thuToggleButton.setText(viewPanel.domain.language.getString ("thu"));
        thuToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thuToggleButtonActionPerformed(evt);
            }
        });
        thuToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseEndDateChooser.setToolTipText(viewPanel.domain.language.getString ("courseEndDateToolTip"));
        courseEndDateChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });
        courseEndDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                courseEndDateChooserPropertyChange(evt);
            }
        });

        friToggleButton.setText(viewPanel.domain.language.getString ("fri"));
        friToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                friToggleButtonActionPerformed(evt);
            }
        });
        friToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        roomTextField.setToolTipText(viewPanel.domain.language.getString ("courseRoomToolTip"));
        roomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomTextFieldActionPerformed(evt);
            }
        });
        roomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                roomTextFieldFocusLost(evt);
            }
        });
        roomTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                roomTextFieldsettingsChangesMade(evt);
            }
        });

        courseTimeLabel.setText(viewPanel.domain.language.getString ("time") + ":");

        creditsLabel.setText(viewPanel.domain.language.getString ("credits") + ":");

        wedToggleButton.setText(viewPanel.domain.language.getString ("wed"));
        wedToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wedToggleButtonActionPerformed(evt);
            }
        });
        wedToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseHasLabCheckBox.setText(viewPanel.domain.language.getString ("courseHasLab"));
        courseHasLabCheckBox.setOpaque(false);
        courseHasLabCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseHasLabCheckBoxActionPerformed(evt);
            }
        });
        courseHasLabCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labDetailsLabel.setText(viewPanel.domain.language.getString ("labDetails"));
        labDetailsLabel.setEnabled(false);

        labSatToggleButton.setText(viewPanel.domain.language.getString ("sat"));
        labSatToggleButton.setEnabled(false);
        labSatToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labSatToggleButtonActionPerformed(evt);
            }
        });
        labSatToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labFriToggleButton.setText(viewPanel.domain.language.getString ("fri"));
        labFriToggleButton.setEnabled(false);
        labFriToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labFriToggleButtonActionPerformed(evt);
            }
        });
        labFriToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labThuToggleButton.setText(viewPanel.domain.language.getString ("thu"));
        labThuToggleButton.setEnabled(false);
        labThuToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labThuToggleButtonActionPerformed(evt);
            }
        });
        labThuToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labSunToggleButton.setText(viewPanel.domain.language.getString ("sun"));
        labSunToggleButton.setEnabled(false);
        labSunToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labSunToggleButtonActionPerformed(evt);
            }
        });
        labSunToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labMonToggleButton.setText(viewPanel.domain.language.getString ("mon"));
        labMonToggleButton.setEnabled(false);
        labMonToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labMonToggleButtonActionPerformed(evt);
            }
        });
        labMonToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labTueToggleButton.setText(viewPanel.domain.language.getString ("tue"));
        labTueToggleButton.setEnabled(false);
        labTueToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labTueToggleButtonActionPerformed(evt);
            }
        });
        labTueToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labWedToggleButton.setText(viewPanel.domain.language.getString ("wed"));
        labWedToggleButton.setEnabled(false);
        labWedToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labWedToggleButtonActionPerformed(evt);
            }
        });
        labWedToggleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labDaysLabel.setText(viewPanel.domain.language.getString ("labDays") + ":");
        labDaysLabel.setEnabled(false);

        labEndDateLabel.setText(viewPanel.domain.language.getString ("endDate") + ":");
        labEndDateLabel.setEnabled(false);

        labEndDateChooser.setToolTipText(viewPanel.domain.language.getString ("labEndDateToolTip"));
        labEndDateChooser.setEnabled(false);
        labEndDateChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });
        labEndDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                labEndDateChooserPropertyChange(evt);
            }
        });

        labStartDateChooser.setToolTipText(viewPanel.domain.language.getString ("labStartDateToolTip"));
        labStartDateChooser.setEnabled(false);
        labStartDateChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });
        labStartDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                labStartDateChooserPropertyChange(evt);
            }
        });

        labStartDateLabel.setText(viewPanel.domain.language.getString ("startDate") + ":");
        labStartDateLabel.setEnabled(false);

        labTimeLabel.setText(viewPanel.domain.language.getString ("time") + ":");
        labTimeLabel.setEnabled(false);

        labOnlineCheckBox.setText(viewPanel.domain.language.getString ("online"));
        labOnlineCheckBox.setToolTipText(viewPanel.domain.language.getString ("labOnlineToolTip"));
        labOnlineCheckBox.setEnabled(false);
        labOnlineCheckBox.setOpaque(false);
        labOnlineCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labOnlineCheckBoxActionPerformed(evt);
            }
        });
        labOnlineCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labRoomTextField.setToolTipText(viewPanel.domain.language.getString ("labRoomToolTip"));
        labRoomTextField.setEnabled(false);
        labRoomTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labRoomTextFieldActionPerformed(evt);
            }
        });
        labRoomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                labRoomTextFieldFocusLost(evt);
            }
        });
        labRoomTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labRoomTextFieldsettingsChangesMade(evt);
            }
        });

        labRoomLabel.setText(viewPanel.domain.language.getString ("room") + ":");
        labRoomLabel.setEnabled(false);

        labCreditsLabel.setText(viewPanel.domain.language.getString ("credits") + ":");
        labCreditsLabel.setEnabled(false);

        labCreditsSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), Double.valueOf(0.0d), null, Double.valueOf(0.5d)));
        labCreditsSpinner.setToolTipText(viewPanel.domain.language.getString ("labCreditsToolTip"));
        labCreditsSpinner.setEnabled(false);
        labCreditsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labCreditsSpinnerStateChanged(evt);
            }
        });
        labCreditsSpinner.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        startHrChooser.setToolTipText(viewPanel.domain.language.getString ("startHrToolTip"));
        startHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(startHrChooser, "h"));
        startHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startHrChooserStateChanged(evt);
            }
        });
        startHrChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        startMinChooser.setToolTipText(viewPanel.domain.language.getString ("startMinToolTip"));
        startMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(startMinChooser, "mm"));
        startMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startMinChooserStateChanged(evt);
            }
        });
        startMinChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        startMChooser.setEditor(new javax.swing.JSpinner.DateEditor(startMChooser, "a"));
        startMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                startMChooserStateChanged(evt);
            }
        });
        startMChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        endMinChooser.setToolTipText(viewPanel.domain.language.getString ("endMinToolTip"));
        endMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(endMinChooser, "mm"));
        endMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endMinChooserStateChanged(evt);
            }
        });
        endMinChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        endHrChooser.setToolTipText(viewPanel.domain.language.getString ("endHrToolTip"));
        endHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(endHrChooser, "h"));
        endHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endHrChooserStateChanged(evt);
            }
        });
        endHrChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        endMChooser.setEditor(new javax.swing.JSpinner.DateEditor(endMChooser, "a"));
        endMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                endMChooserStateChanged(evt);
            }
        });
        endMChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labStartHrChooser.setToolTipText(viewPanel.domain.language.getString ("startHrToolTip"));
        labStartHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(labStartHrChooser, "h"));
        labStartHrChooser.setEnabled(false);
        labStartHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labStartHrChooserStateChanged(evt);
            }
        });
        labStartHrChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labStartMinChooser.setToolTipText(viewPanel.domain.language.getString ("startMinToolTip"));
        labStartMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(labStartMinChooser, "mm"));
        labStartMinChooser.setEnabled(false);
        labStartMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labStartMinChooserStateChanged(evt);
            }
        });
        labStartMinChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labStartMChooser.setEditor(new javax.swing.JSpinner.DateEditor(labStartMChooser, "a"));
        labStartMChooser.setEnabled(false);
        labStartMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labStartMChooserStateChanged(evt);
            }
        });
        labStartMChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });
        labStartMChooser.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                labStartMChooserKeyReleased(evt);
            }
        });

        labEndMinChooser.setToolTipText(viewPanel.domain.language.getString ("endMinToolTip"));
        labEndMinChooser.setEditor(new javax.swing.JSpinner.DateEditor(labEndMinChooser, "mm"));
        labEndMinChooser.setEnabled(false);
        labEndMinChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labEndMinChooserStateChanged(evt);
            }
        });
        labEndMinChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labEndHrChooser.setToolTipText(viewPanel.domain.language.getString ("endHrToolTip"));
        labEndHrChooser.setEditor(new javax.swing.JSpinner.DateEditor(labEndHrChooser, "h"));
        labEndHrChooser.setEnabled(false);
        labEndHrChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labEndHrChooserStateChanged(evt);
            }
        });
        labEndHrChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        labEndMChooser.setEditor(new javax.swing.JSpinner.DateEditor(labEndMChooser, "a"));
        labEndMChooser.setEnabled(false);
        labEndMChooser.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                labEndMChooserStateChanged(evt);
            }
        });
        labEndMChooser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesComponentFocusGained(evt);
            }
        });

        courseColon1.setText(":");

        courseColon2.setText(":");

        courseColon4.setText(":");

        courseColon3.setText(":");

        courseColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        courseColorPanel.setToolTipText(viewPanel.domain.language.getString ("clickToChangeColorToolTip"));
        courseColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        courseColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                courseColorPanelcolorPanelMouseEnteredTermsAndCourses(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                courseColorPanelcolorPanelMouseExitedTermsAndCourses(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                courseColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout courseColorPanelLayout = new javax.swing.GroupLayout(courseColorPanel);
        courseColorPanel.setLayout(courseColorPanelLayout);
        courseColorPanelLayout.setHorizontalGroup(
            courseColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        courseColorPanelLayout.setVerticalGroup(
            courseColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        courseNumberLabel.setText(viewPanel.domain.language.getString ("courseNumber") + ":");

        courseNumberTextField.setToolTipText(viewPanel.domain.language.getString ("courseNumberToolTip"));
        courseNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseNumberTextFieldActionPerformed(evt);
            }
        });
        courseNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                courseNumberTextFieldFocusLost(evt);
            }
        });
        courseNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                courseNumberTextFieldsettingsChangesMade(evt);
            }
        });

        labNumberLabel.setText(viewPanel.domain.language.getString ("labNumber") + ":");
        labNumberLabel.setEnabled(false);

        labNumberTextField.setToolTipText(viewPanel.domain.language.getString ("labNumberToolTip"));
        labNumberTextField.setEnabled(false);
        labNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labNumberTextFieldActionPerformed(evt);
            }
        });
        labNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                labNumberTextFieldFocusLost(evt);
            }
        });
        labNumberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                labNumberTextFieldsettingsChangesMade(evt);
            }
        });

        courseWebsiteLabel.setText(viewPanel.domain.language.getString ("courseWebsiteNonMenu") + ":");

        courseWebsiteTextField.setToolTipText(viewPanel.domain.language.getString ("courseWebsiteToolTip"));
        courseWebsiteTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseWebsiteTextFieldActionPerformed(evt);
            }
        });
        courseWebsiteTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                courseWebsiteTextFieldFocusLost(evt);
            }
        });
        courseWebsiteTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                courseNumberTextFieldsettingsChangesMade(evt);
            }
        });

        visitCourseWebsiteButton.setText(viewPanel.domain.language.getString ("visit"));
        visitCourseWebsiteButton.setToolTipText(viewPanel.domain.language.getString ("visitCourseWebsiteToolTip"));
        visitCourseWebsiteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitCourseWebsiteButtonActionPerformed(evt);
            }
        });

        labWebsiteLabel.setText(viewPanel.domain.language.getString ("labWebsiteNonMenu") + ":");
        labWebsiteLabel.setEnabled(false);

        labWebsiteTextField.setToolTipText(viewPanel.domain.language.getString ("labWebsiteToolTip"));
        labWebsiteTextField.setEnabled(false);
        labWebsiteTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labWebsiteTextFieldActionPerformed(evt);
            }
        });
        labWebsiteTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                coursesTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                labWebsiteTextFieldFocusLost(evt);
            }
        });
        labWebsiteTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                courseNumberTextFieldsettingsChangesMade(evt);
            }
        });

        visitLabWebsiteButton.setText(viewPanel.domain.language.getString ("visit"));
        visitLabWebsiteButton.setToolTipText(viewPanel.domain.language.getString ("visitLabWebsiteToolTip"));
        visitLabWebsiteButton.setEnabled(false);
        visitLabWebsiteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitLabWebsiteButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout courseInnerDetailsPanelLayout = new javax.swing.GroupLayout(courseInnerDetailsPanel);
        courseInnerDetailsPanel.setLayout(courseInnerDetailsPanelLayout);
        courseInnerDetailsPanelLayout.setHorizontalGroup(
            courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labRoomLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labRoomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labCreditsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labCreditsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labEndHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labStartHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseColon3)
                            .addComponent(courseColon4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(labEndMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labEndMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(labStartMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labStartMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labOnlineCheckBox))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(creditsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creditsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(courseColorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(courseColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(courseTimeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseColon1)
                            .addComponent(courseColon2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(startMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(onlineCheckBox))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(roomLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(courseEndDateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(courseEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(courseStartDateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(courseStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(daysLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                        .addComponent(sunToggleButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(monToggleButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tueToggleButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(wedToggleButton))
                                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                        .addGap(38, 38, 38)
                                        .addComponent(thuToggleButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(friToggleButton)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(satToggleButton)))))
                        .addGap(23, 23, 23))
                    .addComponent(labDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addComponent(courseHasLabCheckBox)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(courseDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(assignedToTermLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(termsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(courseNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(courseNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(courseWebsiteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(courseWebsiteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(visitCourseWebsiteButton))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labWebsiteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labWebsiteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(visitLabWebsiteButton))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labEndDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labStartDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(labDaysLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(labThuToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labFriToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labSatToggleButton))
                            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                                .addComponent(labSunToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labMonToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labTueToggleButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labWedToggleButton)))))
                .addContainerGap())
        );
        courseInnerDetailsPanelLayout.setVerticalGroup(
            courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(courseDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(courseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(assignedToTermLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(termsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseNumberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(courseNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(courseWebsiteLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(courseWebsiteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(visitCourseWebsiteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(creditsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(creditsSpinner)
                        .addComponent(courseColorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                    .addComponent(courseColorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(roomLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(roomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(courseTimeLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(onlineCheckBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, courseInnerDetailsPanelLayout.createSequentialGroup()
                            .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(startHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(courseColon1)
                                .addComponent(startMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(startMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(endHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(courseColon2)
                                .addComponent(endMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(endMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(courseStartDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(courseStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(courseEndDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(courseEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sunToggleButton)
                            .addComponent(monToggleButton)
                            .addComponent(tueToggleButton)
                            .addComponent(wedToggleButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(thuToggleButton)
                            .addComponent(friToggleButton)
                            .addComponent(satToggleButton)))
                    .addComponent(daysLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(courseHasLabCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labNumberLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(labNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labWebsiteLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(labWebsiteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(visitLabWebsiteButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labCreditsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labCreditsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labRoomLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labRoomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labOnlineCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labStartHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labStartMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labStartMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseColon3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labEndHrChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labEndMinChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labEndMChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(courseColon4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labStartDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labStartDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labEndDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labEndDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(courseInnerDetailsPanelLayout.createSequentialGroup()
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labSunToggleButton)
                            .addComponent(labMonToggleButton)
                            .addComponent(labTueToggleButton)
                            .addComponent(labWedToggleButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(courseInnerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labThuToggleButton)
                            .addComponent(labFriToggleButton)
                            .addComponent(labSatToggleButton)))
                    .addComponent(labDaysLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        courseScrollPane.setViewportView(courseInnerDetailsPanel);

        courseTabbedPane.addTab("Course", courseScrollPane);

        instructorsScrollPane.setBackground(coursesPanel.getBackground ());
        instructorsScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        instructorsDetailsPanel.setBackground(coursesPanel.getBackground ());
        instructorsDetailsPanel.setPreferredSize(new java.awt.Dimension(450, 400));

        instructorsListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instructorsListLabel.setText(viewPanel.domain.language.getString ("instructorsList"));

        addInstructorButton.setText(viewPanel.domain.language.getString ("add"));
        addInstructorButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addInstructorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInstructorButtonActionPerformed(evt);
            }
        });

        removeInstructorButton.setText(viewPanel.domain.language.getString ("remove"));
        removeInstructorButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeInstructorButton.setEnabled(false);
        removeInstructorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeInstructorButtonActionPerformed(evt);
            }
        });

        moveInstructorUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveInstructorUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveInstructorUpButton.setEnabled(false);
        moveInstructorUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveInstructorUpButtonActionPerformed(evt);
            }
        });

        moveInstructorDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveInstructorDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveInstructorDownButton.setEnabled(false);
        moveInstructorDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveInstructorDownButtonActionPerformed(evt);
            }
        });

        settingsInstructorsTable.setModel(instructorTableModel);
        settingsInstructorsTable.setMaximumSize(new java.awt.Dimension(300, 64));
        settingsInstructorsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsTypesScrollPane1.setViewportView(settingsInstructorsTable);

        instructorDetailsPanel.setOpaque(false);

        instructorDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        instructorDetailsLabel.setText(viewPanel.domain.language.getString ("instructorDetails"));

        instructorNameTextField.setToolTipText(viewPanel.domain.language.getString ("instructorNameToolTip"));
        instructorNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instructorNameTextFieldActionPerformed(evt);
            }
        });
        instructorNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                instructorNameTextFieldFocusLost(evt);
            }
        });
        instructorNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                instructorNameTextFieldsettingsChangesMade(evt);
            }
        });

        instructorEmailTextField.setToolTipText(viewPanel.domain.language.getString ("emailAddressInstructorToolTip"));
        instructorEmailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instructorEmailTextFieldActionPerformed(evt);
            }
        });
        instructorEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                instructorEmailTextFieldFocusLost(evt);
            }
        });
        instructorEmailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                instructorEmailTextFieldsettingsChangesMade(evt);
            }
        });

        instructorEmailLabel.setText(viewPanel.domain.language.getString ("email") + ":");

        phoneLabel.setText(viewPanel.domain.language.getString ("phone") + ":");

        phoneTextField.setToolTipText(viewPanel.domain.language.getString ("instructorPhoneNumberToolTip"));
        phoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneTextFieldActionPerformed(evt);
            }
        });
        phoneTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                phoneTextFieldFocusLost(evt);
            }
        });
        phoneTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                phoneTextFieldsettingsChangesMade(evt);
            }
        });

        officeHoursTextField.setToolTipText(viewPanel.domain.language.getString ("officeHoursInstructorToolTip"));
        officeHoursTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                officeHoursTextFieldActionPerformed(evt);
            }
        });
        officeHoursTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                officeHoursTextFieldFocusLost(evt);
            }
        });
        officeHoursTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                officeHoursTextFieldsettingsChangesMade(evt);
            }
        });

        officeHoursLabel.setText(viewPanel.domain.language.getString ("officeHours") + ":");

        officeLocationTextField.setToolTipText(viewPanel.domain.language.getString ("officeLocationInstructorToolTip"));
        officeLocationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                officeLocationTextFieldActionPerformed(evt);
            }
        });
        officeLocationTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                officeLocationTextFieldFocusLost(evt);
            }
        });
        officeLocationTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                officeLocationTextFieldsettingsChangesMade(evt);
            }
        });

        officeLocationLabel.setText(viewPanel.domain.language.getString ("officeLocation") + ":");

        emailInstructorButton.setText(viewPanel.domain.language.getString ("sendEmail"));
        emailInstructorButton.setToolTipText(viewPanel.domain.language.getString ("emailInstructorButtonToolTip"));
        emailInstructorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailInstructorButtonActionPerformed(evt);
            }
        });
        emailInstructorButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsComponentFocusGained(evt);
            }
        });

        instructorButtonGroup.add(lectureRadioButton);
        lectureRadioButton.setText(viewPanel.domain.language.getString ("lecture"));
        lectureRadioButton.setOpaque(false);
        lectureRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lectureRadioButtonActionPerformed(evt);
            }
        });
        lectureRadioButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsComponentFocusGained(evt);
            }
        });

        instructorButtonGroup.add(labRadioButton);
        labRadioButton.setText(viewPanel.domain.language.getString ("lab"));
        labRadioButton.setOpaque(false);
        labRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labRadioButtonActionPerformed(evt);
            }
        });
        labRadioButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsComponentFocusGained(evt);
            }
        });

        instructorButtonGroup.add(bothRadioButton);
        bothRadioButton.setText(viewPanel.domain.language.getString ("both"));
        bothRadioButton.setOpaque(false);
        bothRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bothRadioButtonActionPerformed(evt);
            }
        });
        bothRadioButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                instructorsComponentFocusGained(evt);
            }
        });

        javax.swing.GroupLayout instructorDetailsPanelLayout = new javax.swing.GroupLayout(instructorDetailsPanel);
        instructorDetailsPanel.setLayout(instructorDetailsPanelLayout);
        instructorDetailsPanelLayout.setHorizontalGroup(
            instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(instructorDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
            .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lectureRadioButton)
                .addGap(18, 18, 18)
                .addComponent(labRadioButton)
                .addGap(18, 18, 18)
                .addComponent(bothRadioButton)
                .addContainerGap(123, Short.MAX_VALUE))
            .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                        .addComponent(instructorEmailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instructorEmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailInstructorButton))
                    .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                        .addComponent(phoneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                        .addComponent(officeHoursLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(officeHoursTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                        .addComponent(officeLocationLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(officeLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
            .addComponent(instructorNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );
        instructorDetailsPanelLayout.setVerticalGroup(
            instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instructorDetailsPanelLayout.createSequentialGroup()
                .addComponent(instructorDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructorNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lectureRadioButton)
                    .addComponent(labRadioButton)
                    .addComponent(bothRadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(instructorEmailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emailInstructorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(instructorEmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(phoneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(officeHoursLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(officeHoursTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(instructorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(officeLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(officeLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout instructorsDetailsPanelLayout = new javax.swing.GroupLayout(instructorsDetailsPanel);
        instructorsDetailsPanel.setLayout(instructorsDetailsPanelLayout);
        instructorsDetailsPanelLayout.setHorizontalGroup(
            instructorsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instructorsDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(instructorsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(instructorDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsTypesScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addGroup(instructorsDetailsPanelLayout.createSequentialGroup()
                        .addComponent(addInstructorButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(removeInstructorButton))
                    .addGroup(instructorsDetailsPanelLayout.createSequentialGroup()
                        .addComponent(moveInstructorUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(moveInstructorDownButton))
                    .addComponent(instructorsListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addContainerGap())
        );
        instructorsDetailsPanelLayout.setVerticalGroup(
            instructorsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(instructorsDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(instructorsListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTypesScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(instructorsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addInstructorButton)
                    .addComponent(removeInstructorButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(instructorsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveInstructorUpButton)
                    .addComponent(moveInstructorDownButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructorDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        instructorsScrollPane.setViewportView(instructorsDetailsPanel);

        courseTabbedPane.addTab("Instructors", instructorsScrollPane);

        typesDetailsPanel.setBackground(coursesPanel.getBackground ());

        typesListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        typesListLabel.setText(viewPanel.domain.language.getString ("gradingScale"));

        typeDetailsPanel.setOpaque(false);

        typeDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        typeDetailsLabel.setText(viewPanel.domain.language.getString ("typeDetails"));

        typeNameTextField.setToolTipText(viewPanel.domain.language.getString ("typeNameToolTip"));
        typeNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typeNameTextFieldActionPerformed(evt);
            }
        });
        typeNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                typeNameTextFieldtextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                typeNameTextFieldFocusLost(evt);
            }
        });
        typeNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                typeNameTextFieldsettingsChangesMade(evt);
            }
        });

        typeWeightLabel.setText(viewPanel.domain.language.getString ("weight") + ":");

        weightTextField.setToolTipText(viewPanel.domain.language.getString ("weightToolTip"));
        weightTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weightTextFieldActionPerformed(evt);
            }
        });
        weightTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weightTextFieldtextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weightTextFieldFocusLost(evt);
            }
        });
        weightTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                weightTextFieldsettingsChangesMade(evt);
            }
        });

        javax.swing.GroupLayout typeDetailsPanelLayout = new javax.swing.GroupLayout(typeDetailsPanel);
        typeDetailsPanel.setLayout(typeDetailsPanelLayout);
        typeDetailsPanelLayout.setHorizontalGroup(
            typeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(typeNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
            .addComponent(typeDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
            .addGroup(typeDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(typeWeightLabel)
                .addGap(10, 10, 10)
                .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(255, Short.MAX_VALUE))
        );
        typeDetailsPanelLayout.setVerticalGroup(
            typeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typeDetailsPanelLayout.createSequentialGroup()
                .addComponent(typeDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(typeDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(typeWeightLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(weightTextField))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        addTypeButton.setText(viewPanel.domain.language.getString ("add"));
        addTypeButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addTypeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTypeButtonActionPerformed(evt);
            }
        });

        removeTypeButton.setText(viewPanel.domain.language.getString ("remove"));
        removeTypeButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeTypeButton.setEnabled(false);
        removeTypeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTypeButtonActionPerformed(evt);
            }
        });

        moveTypeUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveTypeUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveTypeUpButton.setEnabled(false);
        moveTypeUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTypeUpButtonActionPerformed(evt);
            }
        });

        moveTypeDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveTypeDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveTypeDownButton.setEnabled(false);
        moveTypeDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTypeDownButtonActionPerformed(evt);
            }
        });

        settingsTypesTable.setModel(typeTableModel);
        settingsTypesTable.setMaximumSize(new java.awt.Dimension(300, 64));
        settingsTypesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsTypesScrollPane.setViewportView(settingsTypesTable);

        javax.swing.GroupLayout typesDetailsPanelLayout = new javax.swing.GroupLayout(typesDetailsPanel);
        typesDetailsPanel.setLayout(typesDetailsPanelLayout);
        typesDetailsPanelLayout.setHorizontalGroup(
            typesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typesDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(typesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsTypesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                    .addComponent(typeDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(typesDetailsPanelLayout.createSequentialGroup()
                        .addComponent(addTypeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 259, Short.MAX_VALUE)
                        .addComponent(removeTypeButton))
                    .addGroup(typesDetailsPanelLayout.createSequentialGroup()
                        .addComponent(moveTypeUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 259, Short.MAX_VALUE)
                        .addComponent(moveTypeDownButton))
                    .addComponent(typesListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE))
                .addContainerGap())
        );
        typesDetailsPanelLayout.setVerticalGroup(
            typesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(typesDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(typesListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTypesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTypeButton)
                    .addComponent(removeTypeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(typesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveTypeUpButton)
                    .addComponent(moveTypeDownButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(typeDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        courseTabbedPane.addTab("Grading Scale", typesDetailsPanel);

        textbooksScrollPane.setBackground(coursesPanel.getBackground ());
        textbooksScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        textbooksDetailsPanel.setPreferredSize(new java.awt.Dimension(450, 495));

        textbooksListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textbooksListLabel.setText(viewPanel.domain.language.getString ("textbooksList"));

        textbookDetailsPanel.setOpaque(false);

        textbookNameTextField.setToolTipText(viewPanel.domain.language.getString ("textbookNameToolTip"));
        textbookNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textbookNameTextFieldActionPerformed(evt);
            }
        });
        textbookNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textbookNameTextFieldFocusLost(evt);
            }
        });
        textbookNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textbookNameTextFieldsettingsChangesMade(evt);
            }
        });

        textbookDetailsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textbookDetailsLabel.setText(viewPanel.domain.language.getString ("textbookDetails"));

        isbnLabel.setText(viewPanel.domain.language.getString ("isbn") + ":");

        isbnTextField.setToolTipText(viewPanel.domain.language.getString ("isbnToolTip"));
        isbnTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isbnTextFieldActionPerformed(evt);
            }
        });
        isbnTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                isbnTextFieldFocusLost(evt);
            }
        });
        isbnTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                isbnTextFieldsettingsChangesMade(evt);
            }
        });

        authorLabel.setText(viewPanel.domain.language.getString ("authors") + ":");

        authorTextField.setToolTipText(viewPanel.domain.language.getString ("authorsNameToolTip"));
        authorTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authorTextFieldActionPerformed(evt);
            }
        });
        authorTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                authorTextFieldFocusLost(evt);
            }
        });
        authorTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                authorTextFieldsettingsChangesMade(evt);
            }
        });

        publisherLabel.setText(viewPanel.domain.language.getString ("publisher") + ":");

        publisherTextField.setToolTipText(viewPanel.domain.language.getString ("publisherToolTip"));
        publisherTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publisherTextFieldActionPerformed(evt);
            }
        });
        publisherTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                publisherTextFieldFocusLost(evt);
            }
        });
        publisherTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                publisherTextFieldsettingsChangesMade(evt);
            }
        });

        onlineSourceLabel.setText(viewPanel.domain.language.getString ("website") + ":");

        textbookSourceTextField.setToolTipText(viewPanel.domain.language.getString ("textbookWebsiteToolTip"));
        textbookSourceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textbookSourceTextFieldActionPerformed(evt);
            }
        });
        textbookSourceTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textbookSourceTextFieldFocusLost(evt);
            }
        });
        textbookSourceTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textbookSourceTextFieldsettingsChangesMade(evt);
            }
        });

        purchasePrice.setText(viewPanel.domain.language.getString ("purchasePrice") + ":");

        conditionLabel.setText(viewPanel.domain.language.getString ("condition") + ":");

        purchasePriceTextField.setToolTipText(viewPanel.domain.language.getString ("purchasePriceToolTip"));
        purchasePriceTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasePriceTextFieldActionPerformed(evt);
            }
        });
        purchasePriceTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                purchasePriceTextFieldFocusLost(evt);
            }
        });
        purchasePriceTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                purchasePriceTextFieldsettingsChangesMade(evt);
            }
        });

        conditionTextField.setToolTipText(viewPanel.domain.language.getString ("conditionTextbookToolTip"));
        conditionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conditionTextFieldActionPerformed(evt);
            }
        });
        conditionTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                conditionTextFieldFocusLost(evt);
            }
        });
        conditionTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                conditionTextFieldsettingsChangesMade(evt);
            }
        });

        visitTextbookSourceButton.setText(viewPanel.domain.language.getString ("visit"));
        visitTextbookSourceButton.setToolTipText(viewPanel.domain.language.getString ("visitWebsiteToolTip"));
        visitTextbookSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visitTextbookSourceButtonActionPerformed(evt);
            }
        });
        visitTextbookSourceButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksComponentFocusGained(evt);
            }
        });

        contactEmailLabel.setText(viewPanel.domain.language.getString ("contactEmail") + ":");

        contactEmailTextField.setToolTipText(viewPanel.domain.language.getString ("contactEmailTextbookToolTip"));
        contactEmailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactEmailTextFieldActionPerformed(evt);
            }
        });
        contactEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                contactEmailTextFieldFocusLost(evt);
            }
        });
        contactEmailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contactEmailTextFieldsettingsChangesMade(evt);
            }
        });

        orderedCheckBox.setText(viewPanel.domain.language.getString ("ordered"));
        orderedCheckBox.setToolTipText(viewPanel.domain.language.getString ("orderedToolTip"));
        orderedCheckBox.setOpaque(false);
        orderedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderedCheckBoxActionPerformed(evt);
            }
        });
        orderedCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksComponentFocusGained(evt);
            }
        });

        receivedCheckBox.setText(viewPanel.domain.language.getString ("received"));
        receivedCheckBox.setToolTipText(viewPanel.domain.language.getString ("receivedToolTip"));
        receivedCheckBox.setOpaque(false);
        receivedCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                receivedCheckBoxActionPerformed(evt);
            }
        });
        receivedCheckBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksComponentFocusGained(evt);
            }
        });

        contactSendEmailButton.setText(viewPanel.domain.language.getString ("sendEmail"));
        contactSendEmailButton.setToolTipText(viewPanel.domain.language.getString ("sendEmailTextbookToolTip"));
        contactSendEmailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contactSendEmailButtonActionPerformed(evt);
            }
        });
        contactSendEmailButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksComponentFocusGained(evt);
            }
        });

        searchGoogleButton.setText(viewPanel.domain.language.getString ("searchGoogle"));
        searchGoogleButton.setToolTipText(viewPanel.domain.language.getString ("searchGoogleToolTip"));
        searchGoogleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchGoogleButtonActionPerformed(evt);
            }
        });
        searchGoogleButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textbooksComponentFocusGained(evt);
            }
        });

        javax.swing.GroupLayout textbookDetailsPanelLayout = new javax.swing.GroupLayout(textbookDetailsPanel);
        textbookDetailsPanel.setLayout(textbookDetailsPanelLayout);
        textbookDetailsPanelLayout.setHorizontalGroup(
            textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(isbnLabel)
                        .addGap(10, 10, 10)
                        .addComponent(isbnTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchGoogleButton))
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(authorLabel)
                        .addGap(10, 10, 10)
                        .addComponent(authorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(publisherLabel)
                        .addGap(10, 10, 10)
                        .addComponent(publisherTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(textbookNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
            .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(orderedCheckBox)
                .addGap(18, 18, 18)
                .addComponent(receivedCheckBox)
                .addContainerGap(232, Short.MAX_VALUE))
            .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(onlineSourceLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textbookSourceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(visitTextbookSourceButton)
                .addContainerGap())
            .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(contactEmailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contactEmailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contactSendEmailButton))
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(purchasePrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchasePriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                        .addComponent(conditionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(conditionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
            .addComponent(textbookDetailsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
        );
        textbookDetailsPanelLayout.setVerticalGroup(
            textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textbookDetailsPanelLayout.createSequentialGroup()
                .addComponent(textbookDetailsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textbookNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(authorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(authorTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(isbnLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchGoogleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(isbnTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(publisherLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(publisherTextField))
                .addGap(18, 18, 18)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderedCheckBox)
                    .addComponent(receivedCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(onlineSourceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(visitTextbookSourceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textbookSourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(contactEmailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactSendEmailButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contactEmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purchasePrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchasePriceTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbookDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(conditionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(conditionTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addTextbookButton.setText(viewPanel.domain.language.getString ("add"));
        addTextbookButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addTextbookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTextbookButtonActionPerformed(evt);
            }
        });

        removeTextbookButton.setText(viewPanel.domain.language.getString ("remove"));
        removeTextbookButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeTextbookButton.setEnabled(false);
        removeTextbookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeTextbookButtonActionPerformed(evt);
            }
        });

        moveTextbookUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveTextbookUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveTextbookUpButton.setEnabled(false);
        moveTextbookUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTextbookUpButtonActionPerformed(evt);
            }
        });

        settingsTextbooksTable.setModel(textbookTableModel);
        settingsTextbooksTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsTextbooksScrollPane.setViewportView(settingsTextbooksTable);

        moveTextbookDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveTextbookDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveTextbookDownButton.setEnabled(false);
        moveTextbookDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveTextbookDownButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout textbooksDetailsPanelLayout = new javax.swing.GroupLayout(textbooksDetailsPanel);
        textbooksDetailsPanel.setLayout(textbooksDetailsPanelLayout);
        textbooksDetailsPanelLayout.setHorizontalGroup(
            textbooksDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textbooksDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(textbooksDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textbookDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(textbooksDetailsPanelLayout.createSequentialGroup()
                        .addComponent(addTextbookButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(removeTextbookButton))
                    .addGroup(textbooksDetailsPanelLayout.createSequentialGroup()
                        .addComponent(moveTextbookUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 240, Short.MAX_VALUE)
                        .addComponent(moveTextbookDownButton))
                    .addComponent(settingsTextbooksScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(textbooksListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addContainerGap())
        );
        textbooksDetailsPanelLayout.setVerticalGroup(
            textbooksDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(textbooksDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textbooksListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTextbooksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbooksDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addTextbookButton)
                    .addComponent(removeTextbookButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(textbooksDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveTextbookUpButton)
                    .addComponent(moveTextbookDownButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textbookDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        textbooksScrollPane.setViewportView(textbooksDetailsPanel);

        courseTabbedPane.addTab("Textbooks", textbooksScrollPane);

        courseDetailsCardPanel.add(courseTabbedPane, "card2");

        coursesPanel.add(courseDetailsCardPanel);

        courseListPanel.setOpaque(false);

        courseListLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        courseListLabel.setText(viewPanel.domain.language.getString ("coursesList"));

        removeCourseButton.setText(viewPanel.domain.language.getString ("remove"));
        removeCourseButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeCourseButton.setEnabled(false);
        removeCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCourseButtonActionPerformed(evt);
            }
        });

        addCourseButton.setText(viewPanel.domain.language.getString ("add"));
        addCourseButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addCourseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCourseButtonActionPerformed(evt);
            }
        });

        moveCourseUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveCourseUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveCourseUpButton.setEnabled(false);
        moveCourseUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveCourseUpButtonActionPerformed(evt);
            }
        });

        moveCourseDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveCourseDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveCourseDownButton.setEnabled(false);
        moveCourseDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveCourseDownButtonActionPerformed(evt);
            }
        });

        settingsCoursesTable.setModel(courseTableModel);
        settingsCoursesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        settingsCoursesScrollPane.setViewportView(settingsCoursesTable);

        javax.swing.GroupLayout courseListPanelLayout = new javax.swing.GroupLayout(courseListPanel);
        courseListPanel.setLayout(courseListPanelLayout);
        courseListPanelLayout.setHorizontalGroup(
            courseListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(courseListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(courseListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(settingsCoursesScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(courseListPanelLayout.createSequentialGroup()
                        .addComponent(moveCourseUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(moveCourseDownButton))
                    .addComponent(courseListLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 462, Short.MAX_VALUE)
                    .addGroup(courseListPanelLayout.createSequentialGroup()
                        .addComponent(addCourseButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 264, Short.MAX_VALUE)
                        .addComponent(removeCourseButton)))
                .addContainerGap())
        );
        courseListPanelLayout.setVerticalGroup(
            courseListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(courseListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(courseListLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsCoursesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCourseButton)
                    .addComponent(removeCourseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(courseListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveCourseUpButton)
                    .addComponent(moveCourseDownButton))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        coursesPanel.add(courseListPanel);

        termsAndCoursesTabbedPane.addTab("Courses", coursesPanel);

        javax.swing.GroupLayout termsAndCoursesJPanelLayout = new javax.swing.GroupLayout(termsAndCoursesJPanel);
        termsAndCoursesJPanel.setLayout(termsAndCoursesJPanelLayout);
        termsAndCoursesJPanelLayout.setHorizontalGroup(
            termsAndCoursesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(termsAndCoursesUpperJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(termsAndCoursesTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        termsAndCoursesJPanelLayout.setVerticalGroup(
            termsAndCoursesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(termsAndCoursesJPanelLayout.createSequentialGroup()
                .addComponent(termsAndCoursesUpperJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(termsAndCoursesTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(termsAndCoursesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(termsAndCoursesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void termsAndCoursesCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termsAndCoursesCloseButtonActionPerformed
        closeTermsAndCoursesDialog ();
}//GEN-LAST:event_termsAndCoursesCloseButtonActionPerformed

    private void termStartDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_termStartDateChooserPropertyChange
        if (settingsTermsTable.getSelectedRow () != -1 && viewPanel.domain.termLoading.empty ())
        {
            if (termStartDateChooser.getDate ().after (termEndDateChooser.getDate ()))
            {
                termEndDateChooser.setDate (termStartDateChooser.getDate ());
            }
            Term term = viewPanel.domain.utility.terms.get (settingsTermsTable.getSelectedRow ());
            try
            {
                String startDate = Domain.DATE_FORMAT.format (termStartDateChooser.getDate ());
                term.setStartDate (startDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    termStartDateChooser.setDate (Domain.DATE_FORMAT.parse (term.getStartDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_termStartDateChooserPropertyChange

    private void termEndDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_termEndDateChooserPropertyChange
        if (settingsTermsTable.getSelectedRow () != -1 && viewPanel.domain.termLoading.empty ())
        {
            if (termStartDateChooser.getDate ().after (termEndDateChooser.getDate ()))
            {
                termStartDateChooser.setDate (termEndDateChooser.getDate ());
            }
            Term term = viewPanel.domain.utility.terms.get (settingsTermsTable.getSelectedRow ());
            try
            {
                String endDate = Domain.DATE_FORMAT.format (termEndDateChooser.getDate ());
                term.setEndDate (endDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    termEndDateChooser.setDate (Domain.DATE_FORMAT.parse (term.getEndDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_termEndDateChooserPropertyChange

    private void addCourseToTermButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseToTermButtonActionPerformed
        viewPanel.termTree.setSelectionPath (new TreePath (viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex).getPath ()));
        termsAndCoursesTabbedPane.setSelectedIndex (1);
        addCourseButtonActionPerformed (null);
}//GEN-LAST:event_addCourseToTermButtonActionPerformed

    private void termNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_termNameTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTermName (settingsTermsTable.getSelectedRow ());
        if (evt != null)
        {
            termNameTextField.requestFocus ();
            termNameTextField.selectAll ();
        }
}//GEN-LAST:event_termNameTextFieldActionPerformed

    private void termNameTextFieldtextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termNameTextFieldtextFieldFocusGained
        ((JTextField) evt.getComponent ()).selectAll ();
}//GEN-LAST:event_termNameTextFieldtextFieldFocusGained

    private void termNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_termNameTextFieldFocusLost
        termNameTextFieldActionPerformed (null);
}//GEN-LAST:event_termNameTextFieldFocusLost

    private void termNameTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_termNameTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_termNameTextFieldsettingsChangesMade

    private void addTermButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTermButtonActionPerformed
        viewPanel.domain.addTerm ();
}//GEN-LAST:event_addTermButtonActionPerformed

    private void removeTermButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTermButtonActionPerformed
        if (settingsTermsTable.getSelectedRow () != -1 || evt == null)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeTermText"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
            JDialog optionDialog = null;
            if (evt != null)
            {
                optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeTerm"));
            }
            else
            {
                optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("removeTerm"));
            }
            optionDialog.setVisible (true);
            if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                int index = settingsTermsTable.getSelectedRow ();
                if (evt == null)
                {
                    index = viewPanel.getSelectedTermIndex ();
                }
                Term term = viewPanel.domain.utility.terms.get (index);
                viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getPath ()));
                for (int i = 0; i < term.getCourseCount (); ++i)
                {
                    term.getCourse (i).markForDeletion ();
                }
                viewPanel.domain.utility.removeCoursesAttachedTo (term);
                termTableModel.removeRow (index);
                settingsTermsTable.refreshTable ();
                viewPanel.root.remove (term);
                viewPanel.domain.utility.terms.remove (index);
                viewPanel.domain.needsCoursesAndTermsSave = true;
                viewPanel.domain.refreshTermTree ();

                if (viewPanel.domain.utility.terms.isEmpty ())
                {
                    viewPanel.disableCourseButtons ();
                    viewPanel.disableAssignmentButtons ();
                    settingsCoursesTable.setSelectedRow (-1);
                    viewPanel.domain.currentCourseIndex = -1;
                    settingsCoursesTableRowSelected (null);
                }
                if (evt != null)
                {
                    updateSettingsCourseInformation ();
                }
                if (index == viewPanel.domain.utility.terms.size ())
                {
                    --index;
                }
                viewPanel.domain.currentTermIndex = index;
                viewPanel.domain.termLoading.push (true);
                settingsTermsTable.setSelectedRow (index);
                settingsTermsTableRowSelected (null);
                if (index != -1)
                {
                    viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (viewPanel.domain.utility.terms.get (index).getPath ()));
                }
                viewPanel.refreshBusyDays ();
                viewPanel.domain.currentCourseIndex = -1;
                viewPanel.domain.termLoading.pop ();
            }
        }
}//GEN-LAST:event_removeTermButtonActionPerformed

    private void moveTermDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTermDownButtonActionPerformed
        viewPanel.domain.termLoading.push (true);
        if (settingsTermsTable.getSelectedRow () != -1 && settingsTermsTable.getSelectedRow () < termTableModel.getRowCount () - 1)
        {
            viewPanel.swap (termTableModel, 0, settingsTermsTable.getSelectedRow (), settingsTermsTable.getSelectedRow () + 1);
        }
        settingsTermsTable.setSelectedRow (settingsTermsTable.getSelectedRow () + 1);
        viewPanel.domain.termLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTermDownButtonActionPerformed

    private void moveTermUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTermUpButtonActionPerformed
        viewPanel.domain.termLoading.push (true);
        if (settingsTermsTable.getSelectedRow () != -1 && settingsTermsTable.getSelectedRow () > 0)
        {
            viewPanel.swap (termTableModel, 0, settingsTermsTable.getSelectedRow (), settingsTermsTable.getSelectedRow () - 1);
        }
        settingsTermsTable.setSelectedRow (settingsTermsTable.getSelectedRow () - 1);
        viewPanel.domain.termLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTermUpButtonActionPerformed

    private void sunToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sunToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (0, sunToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_sunToggleButtonActionPerformed

    private void satToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_satToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (6, satToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_satToggleButtonActionPerformed

    private void courseStartDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_courseStartDateChooserPropertyChange
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            if (courseStartDateChooser.getDate ().after (courseEndDateChooser.getDate ()))
            {
                courseEndDateChooser.setDate (courseStartDateChooser.getDate ());
            }
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            try
            {
                String startDate = Domain.DATE_FORMAT.format (courseStartDateChooser.getDate ());
                course.setStartDate (startDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    courseStartDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getStartDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_courseStartDateChooserPropertyChange

    private void onlineCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineCheckBoxActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setIsOnline (onlineCheckBox.isSelected ());
            if (course.isOnline ())
            {
                courseTimeLabel.setEnabled (false);
                startHrChooser.setEnabled (false);
                startMinChooser.setEnabled (false);
                startMChooser.setEnabled (false);
                endHrChooser.setEnabled (false);
                endMinChooser.setEnabled (false);
                endMChooser.setEnabled (false);
                courseColon1.setEnabled (false);
                courseColon2.setEnabled (false);

                roomLabel.setEnabled (false);
                roomTextField.setEnabled (false);

                daysLabel.setEnabled (false);
                sunToggleButton.setEnabled (false);
                monToggleButton.setEnabled (false);
                tueToggleButton.setEnabled (false);
                wedToggleButton.setEnabled (false);
                thuToggleButton.setEnabled (false);
                friToggleButton.setEnabled (false);
                satToggleButton.setEnabled (false);
            }
            else
            {
                courseTimeLabel.setEnabled (true);
                startHrChooser.setEnabled (true);
                startMinChooser.setEnabled (true);
                startMChooser.setEnabled (true);
                endHrChooser.setEnabled (true);
                endMinChooser.setEnabled (true);
                endMChooser.setEnabled (true);
                courseColon1.setEnabled (true);
                courseColon2.setEnabled (true);

                roomLabel.setEnabled (true);
                roomTextField.setEnabled (true);

                daysLabel.setEnabled (true);
                sunToggleButton.setEnabled (true);
                monToggleButton.setEnabled (true);
                tueToggleButton.setEnabled (true);
                wedToggleButton.setEnabled (true);
                thuToggleButton.setEnabled (true);
                friToggleButton.setEnabled (true);
                satToggleButton.setEnabled (true);
            }
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_onlineCheckBoxActionPerformed

    private void creditsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_creditsSpinnerStateChanged
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setCredits (creditsSpinner.getValue ().toString ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_creditsSpinnerStateChanged

    private void monToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (1, monToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_monToggleButtonActionPerformed

    private void termsComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_termsComboBoxItemStateChanged
        if (termsComboBox.getSelectedIndex () != -1 && settingsCoursesTable.getSelectedRow () != -1
            && viewPanel.domain.courseLoading.empty () && viewPanel.domain.termsAndCoursesOpening.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.getTerm ().removeCourse (course);
            course.setTerm (viewPanel.domain.utility.terms.get (termsComboBox.getSelectedIndex ()));
            course.getTerm ().addCourse (course);

            viewPanel.domain.courseLoading.push (true);
            courseTableModel.removeRow (settingsCoursesTable.getSelectedRow ());
            viewPanel.domain.utility.courses.remove (course);
            int courseIndex = viewPanel.domain.findCourseIndex (course);
            viewPanel.domain.utility.courses.add (course);
            ((ExtendedSettingsTableModel) courseTableModel).insertRowAt (courseIndex, new Object[]
                    {
                        course.getTypeName (), course.getTerm ().getTypeName (), course.getUniqueID ()
                    });
            settingsCoursesTable.setSelectedRow (course.getUniqueID (), 2);
            viewPanel.domain.courseLoading.pop ();

            settingsCoursesTable.refreshTable ();
            viewPanel.domain.refreshTermTree ();
            viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (course.getPath ()));
            viewPanel.expandTermTree (new TreeExpansionEvent (this, null));
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_termsComboBoxItemStateChanged

    private void courseNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseNameTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setCourseName (settingsCoursesTable.getSelectedRow ());
        if (evt != null)
        {
            courseNameTextField.requestFocus ();
            courseNameTextField.selectAll ();
        }
}//GEN-LAST:event_courseNameTextFieldActionPerformed

    private void courseNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_courseNameTextFieldFocusLost
        courseNameTextFieldActionPerformed (null);
}//GEN-LAST:event_courseNameTextFieldFocusLost

    private void courseNameTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseNameTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_courseNameTextFieldsettingsChangesMade

    private void tueToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tueToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (2, tueToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_tueToggleButtonActionPerformed

    private void thuToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thuToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (4, thuToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_thuToggleButtonActionPerformed

    private void courseEndDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_courseEndDateChooserPropertyChange
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            if (courseStartDateChooser.getDate ().after (courseEndDateChooser.getDate ()))
            {
                courseStartDateChooser.setDate (courseEndDateChooser.getDate ());
            }
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            try
            {
                String endDate = Domain.DATE_FORMAT.format (courseEndDateChooser.getDate ());
                course.setEndDate (endDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    courseEndDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getEndDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_courseEndDateChooserPropertyChange

    private void friToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_friToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (5, friToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_friToggleButtonActionPerformed

    private void roomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setRoomLocation (settingsCoursesTable.getSelectedRow ());
        if (evt != null)
        {
            roomTextField.requestFocus ();
            roomTextField.selectAll ();
        }
}//GEN-LAST:event_roomTextFieldActionPerformed

    private void roomTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_roomTextFieldFocusLost
        roomTextFieldActionPerformed (null);
}//GEN-LAST:event_roomTextFieldFocusLost

    private void roomTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roomTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_roomTextFieldsettingsChangesMade

    private void wedToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wedToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setOnDay (3, wedToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_wedToggleButtonActionPerformed

    private void courseHasLabCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseHasLabCheckBoxActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setHasLab (courseHasLabCheckBox.isSelected ());
            updateLabUI (course);
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_courseHasLabCheckBoxActionPerformed

    private void labSatToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labSatToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (6, labSatToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labSatToggleButtonActionPerformed

    private void labFriToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labFriToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (5, labFriToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labFriToggleButtonActionPerformed

    private void labThuToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labThuToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (4, labThuToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labThuToggleButtonActionPerformed

    private void labSunToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labSunToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (0, labSunToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labSunToggleButtonActionPerformed

    private void labMonToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labMonToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (1, labMonToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labMonToggleButtonActionPerformed

    private void labTueToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labTueToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (2, labTueToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labTueToggleButtonActionPerformed

    private void labWedToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labWedToggleButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabOnDay (3, labWedToggleButton.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labWedToggleButtonActionPerformed

    private void labEndDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_labEndDateChooserPropertyChange
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            if (labStartDateChooser.getDate ().after (labEndDateChooser.getDate ()))
            {
                labStartDateChooser.setDate (labEndDateChooser.getDate ());
            }
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            try
            {
                String endDate = Domain.DATE_FORMAT.format (labEndDateChooser.getDate ());
                course.setLabEndDate (endDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    labEndDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getLabEndDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_labEndDateChooserPropertyChange

    private void labStartDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_labStartDateChooserPropertyChange
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            if (labStartDateChooser.getDate ().after (labEndDateChooser.getDate ()))
            {
                labEndDateChooser.setDate (labStartDateChooser.getDate ());
            }
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            try
            {
                String startDate = Domain.DATE_FORMAT.format (labStartDateChooser.getDate ());
                course.setLabStartDate (startDate);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            catch (NullPointerException ex)
            {
                try
                {
                    labStartDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getLabStartDate ()));
                }
                catch (ParseException innerEx)
                {
                    Domain.LOGGER.add (ex);
                }
            }
        }
}//GEN-LAST:event_labStartDateChooserPropertyChange

    private void labOnlineCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labOnlineCheckBoxActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabIsOnline (labOnlineCheckBox.isSelected ());
            updateLabUI (course);
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labOnlineCheckBoxActionPerformed

    private void labRoomTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labRoomTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setLabRoomLocation (settingsCoursesTable.getSelectedRow ());
        if (evt != null)
        {
            roomTextField.requestFocus ();
            roomTextField.selectAll ();
        }
}//GEN-LAST:event_labRoomTextFieldActionPerformed

    private void labRoomTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labRoomTextFieldFocusLost
        labRoomTextFieldActionPerformed (null);
}//GEN-LAST:event_labRoomTextFieldFocusLost

    private void labRoomTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labRoomTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_labRoomTextFieldsettingsChangesMade

    private void labCreditsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labCreditsSpinnerStateChanged
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            course.setLabCredits (labCreditsSpinner.getValue ().toString ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labCreditsSpinnerStateChanged

    private void startHrChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startHrChooserStateChanged
        viewPanel.domain.setCourseStartHr (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_startHrChooserStateChanged

    private void startMinChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startMinChooserStateChanged
        viewPanel.domain.setCourseStartMin (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_startMinChooserStateChanged

    private void startMChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_startMChooserStateChanged
        viewPanel.domain.setCourseStartM (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_startMChooserStateChanged

    private void endMinChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endMinChooserStateChanged
        viewPanel.domain.setCourseEndMin (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_endMinChooserStateChanged

    private void endHrChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endHrChooserStateChanged
        viewPanel.domain.setCourseEndHr (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_endHrChooserStateChanged

    private void endMChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_endMChooserStateChanged
        viewPanel.domain.setCourseEndM (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_endMChooserStateChanged

    private void labStartHrChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labStartHrChooserStateChanged
        viewPanel.domain.setLabCourseStartHr (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labStartHrChooserStateChanged

    private void labStartMinChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labStartMinChooserStateChanged
        viewPanel.domain.setLabCourseStartMin (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labStartMinChooserStateChanged

    private void labStartMChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labStartMChooserStateChanged
        viewPanel.domain.setLabCourseStartM (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labStartMChooserStateChanged

    private void labStartMChooserKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labStartMChooserKeyReleased
        viewPanel.domain.setLabCourseStartM (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labStartMChooserKeyReleased

    private void labEndMinChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labEndMinChooserStateChanged
        viewPanel.domain.setLabCourseEndMin (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labEndMinChooserStateChanged

    private void labEndHrChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labEndHrChooserStateChanged
        viewPanel.domain.setLabCourseEndHr (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labEndHrChooserStateChanged

    private void labEndMChooserStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_labEndMChooserStateChanged
        viewPanel.domain.setLabCourseEndM (settingsCoursesTable.getSelectedRow ());
}//GEN-LAST:event_labEndMChooserStateChanged

    private void courseColorPanelcolorPanelMouseEnteredTermsAndCourses(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseColorPanelcolorPanelMouseEnteredTermsAndCourses
        setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
}//GEN-LAST:event_courseColorPanelcolorPanelMouseEnteredTermsAndCourses

    private void courseColorPanelcolorPanelMouseExitedTermsAndCourses(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseColorPanelcolorPanelMouseExitedTermsAndCourses
        setCursor (Cursor.getDefaultCursor ());
}//GEN-LAST:event_courseColorPanelcolorPanelMouseExitedTermsAndCourses

    private void courseColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_courseColorPanelMouseReleased
        if (settingsCoursesTable.getSelectedRow () != -1 && viewPanel.domain.courseLoading.empty ())
        {
            Color color = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getColor ();
            Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
            if (newColor != null)
            {
                viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).setColor (newColor);
                courseColorPanel.setBackground (newColor);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
        }
}//GEN-LAST:event_courseColorPanelMouseReleased

    private void courseNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseNumberTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setCourseNumber (settingsCoursesTable.getSelectedRow ());
        if (evt != null)
        {
            courseNumberTextField.requestFocus ();
            courseNumberTextField.selectAll ();
        }
}//GEN-LAST:event_courseNumberTextFieldActionPerformed

    private void courseNumberTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_courseNumberTextFieldFocusLost
        courseNumberTextFieldActionPerformed (null);
}//GEN-LAST:event_courseNumberTextFieldFocusLost

    private void courseNumberTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_courseNumberTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
            else if (evt.getSource () == courseWebsiteTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseWebsiteTextField.setText (course.getCourseWebsite ());
            }
            else if (evt.getSource () == labWebsiteTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labWebsiteTextField.setText (course.getLabWebsite ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_courseNumberTextFieldsettingsChangesMade

    private void labNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labNumberTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setLabNumber (settingsCoursesTable.getSelectedRow ());
        if (evt != null)
        {
            labNumberTextField.requestFocus ();
            labNumberTextField.selectAll ();
        }
}//GEN-LAST:event_labNumberTextFieldActionPerformed

    private void labNumberTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labNumberTextFieldFocusLost
        courseNumberTextFieldActionPerformed (null);
}//GEN-LAST:event_labNumberTextFieldFocusLost

    private void labNumberTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labNumberTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_labNumberTextFieldsettingsChangesMade

    private void addInstructorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInstructorButtonActionPerformed
        viewPanel.domain.addInstructor ();
}//GEN-LAST:event_addInstructorButtonActionPerformed

    private void removeInstructorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeInstructorButtonActionPerformed
        if (viewPanel.domain.currentInstructorIndex != -1)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeInstructorText"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeInstructor"));
            optionDialog.setVisible (true);
            if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                int index = settingsInstructorsTable.getSelectedRow ();
                Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
                instructorTableModel.removeRow (index);
                settingsInstructorsTable.refreshTable ();
                viewPanel.domain.utility.instructors.remove (course.removeInstructor (course.getInstructor (index)));
                viewPanel.domain.needsCoursesAndTermsSave = true;

                if (viewPanel.domain.utility.instructors.isEmpty ())
                {
                    removeInstructorButton.setEnabled (false);
                    moveInstructorUpButton.setEnabled (false);
                    moveInstructorDownButton.setEnabled (false);
                }

                if (index == instructorTableModel.getRowCount ())
                {
                    --index;
                }
                viewPanel.domain.currentInstructorIndex = index;
                viewPanel.domain.instructorLoading.push (true);
                settingsInstructorsTable.setSelectedRow (index);
                settingsInstructorsTableRowSelected (null);
                viewPanel.domain.instructorLoading.pop ();
            }
        }
}//GEN-LAST:event_removeInstructorButtonActionPerformed

    private void moveInstructorUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveInstructorUpButtonActionPerformed
        viewPanel.domain.instructorLoading.push (true);
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.currentInstructorIndex > 0)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapInstructors (viewPanel.domain.currentInstructorIndex, viewPanel.domain.currentInstructorIndex - 1);
            viewPanel.swap (instructorTableModel, 4, viewPanel.domain.currentInstructorIndex, viewPanel.domain.currentInstructorIndex - 1);
        }
        settingsInstructorsTable.setSelectedRow (viewPanel.domain.currentInstructorIndex - 1);
        viewPanel.domain.instructorLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveInstructorUpButtonActionPerformed

    private void moveInstructorDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveInstructorDownButtonActionPerformed
        viewPanel.domain.instructorLoading.push (true);
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.currentInstructorIndex < instructorTableModel.getRowCount () - 1)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapInstructors (viewPanel.domain.currentInstructorIndex, viewPanel.domain.currentInstructorIndex + 1);
            viewPanel.swap (instructorTableModel, 4, viewPanel.domain.currentInstructorIndex, viewPanel.domain.currentInstructorIndex + 1);
        }
        settingsInstructorsTable.setSelectedRow (viewPanel.domain.currentInstructorIndex + 1);
        viewPanel.domain.instructorLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveInstructorDownButtonActionPerformed

    private void instructorNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instructorNameTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setInstructorName (viewPanel.domain.currentInstructorIndex);
        if (evt != null)
        {
            instructorNameTextField.requestFocus ();
            instructorNameTextField.selectAll ();
        }
}//GEN-LAST:event_instructorNameTextFieldActionPerformed

    private void instructorNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_instructorNameTextFieldFocusLost
        instructorNameTextFieldActionPerformed (null);
}//GEN-LAST:event_instructorNameTextFieldFocusLost

    private void instructorNameTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_instructorNameTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_instructorNameTextFieldsettingsChangesMade

    private void instructorEmailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instructorEmailTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setInstructorEmail (viewPanel.domain.currentInstructorIndex);
        if (evt != null)
        {
            instructorEmailTextField.requestFocus ();
            instructorEmailTextField.selectAll ();
        }
        if (instructorEmailTextField.getText ().equals (""))
        {
            emailInstructorButton.setEnabled (false);
        }
        else
        {
            emailInstructorButton.setEnabled (true);
        }
}//GEN-LAST:event_instructorEmailTextFieldActionPerformed

    private void instructorEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_instructorEmailTextFieldFocusLost
        instructorEmailTextFieldActionPerformed (null);
}//GEN-LAST:event_instructorEmailTextFieldFocusLost

    private void instructorEmailTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_instructorEmailTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_instructorEmailTextFieldsettingsChangesMade

    private void phoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setInstructorPhone (viewPanel.domain.currentInstructorIndex);
        if (evt != null)
        {
            phoneTextField.requestFocus ();
            phoneTextField.selectAll ();
        }
}//GEN-LAST:event_phoneTextFieldActionPerformed

    private void phoneTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneTextFieldFocusLost
        phoneTextFieldActionPerformed (null);
}//GEN-LAST:event_phoneTextFieldFocusLost

    private void phoneTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_phoneTextFieldsettingsChangesMade

    private void officeHoursTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_officeHoursTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setOfficeHours (viewPanel.domain.currentInstructorIndex);
        if (evt != null)
        {
            officeHoursTextField.requestFocus ();
            officeHoursTextField.selectAll ();
        }
}//GEN-LAST:event_officeHoursTextFieldActionPerformed

    private void officeHoursTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_officeHoursTextFieldFocusLost
        officeHoursTextFieldActionPerformed (null);
}//GEN-LAST:event_officeHoursTextFieldFocusLost

    private void officeHoursTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_officeHoursTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_officeHoursTextFieldsettingsChangesMade

    private void officeLocationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_officeLocationTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setOfficeLocation (viewPanel.domain.currentInstructorIndex);
        if (evt != null)
        {
            officeLocationTextField.requestFocus ();
            officeLocationTextField.selectAll ();
        }
}//GEN-LAST:event_officeLocationTextFieldActionPerformed

    private void officeLocationTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_officeLocationTextFieldFocusLost
        officeLocationTextFieldActionPerformed (null);
}//GEN-LAST:event_officeLocationTextFieldFocusLost

    private void officeLocationTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_officeLocationTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_officeLocationTextFieldsettingsChangesMade

    private void emailInstructorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailInstructorButtonActionPerformed
        Matcher matcher = Domain.EMAIL_PATTERN.matcher (instructorEmailTextField.getText ());
        if (Domain.desktop != null)
        {
            if (matcher.matches ())
            {
                try
                {
                    Domain.desktop.mail (new URI ("mailto", instructorEmailTextField.getText () + "?subject=" + viewPanel.domain.language.getString ("questionAbout") + " " + viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTypeName ().replaceAll ("&", " and ") + " " + viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getCourseNumber ().replaceAll ("&", " and "), null));
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
                instructorEmailTextField.requestFocus ();
                instructorEmailTextField.selectAll ();
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("noSendEmailClientNotFound"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToSend"));
            optionDialog.setVisible (true);
        }
}//GEN-LAST:event_emailInstructorButtonActionPerformed

    private void lectureRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lectureRadioButtonActionPerformed
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.instructorLoading.empty ())
        {
            Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
            instructor.setLectureLab (viewPanel.domain.language.getString ("lecture"));
            instructorTableModel.setValueAt (viewPanel.domain.language.getString ("lecture"), viewPanel.domain.currentInstructorIndex, 1);
            settingsInstructorsTable.refreshTable ();
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_lectureRadioButtonActionPerformed

    private void labRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labRadioButtonActionPerformed
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.instructorLoading.empty ())
        {
            Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
            instructor.setLectureLab (viewPanel.domain.language.getString ("lab"));
            instructorTableModel.setValueAt (viewPanel.domain.language.getString ("lab"), viewPanel.domain.currentInstructorIndex, 1);
            settingsInstructorsTable.refreshTable ();
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_labRadioButtonActionPerformed

    private void bothRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bothRadioButtonActionPerformed
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.instructorLoading.empty ())
        {
            Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
            instructor.setLectureLab (viewPanel.domain.language.getString ("both"));
            instructorTableModel.setValueAt (viewPanel.domain.language.getString ("both"), viewPanel.domain.currentInstructorIndex, 1);
            settingsInstructorsTable.refreshTable ();
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_bothRadioButtonActionPerformed

    private void typeNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typeNameTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTypeName (viewPanel.domain.currentTypeIndex);
        if (evt != null)
        {
            typeNameTextField.requestFocus ();
            typeNameTextField.selectAll ();
        }
}//GEN-LAST:event_typeNameTextFieldActionPerformed

    private void typeNameTextFieldtextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_typeNameTextFieldtextFieldFocusGained
        ((JTextField) evt.getComponent ()).selectAll ();
}//GEN-LAST:event_typeNameTextFieldtextFieldFocusGained

    private void typeNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_typeNameTextFieldFocusLost
        typeNameTextFieldActionPerformed (null);
}//GEN-LAST:event_typeNameTextFieldFocusLost

    private void typeNameTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_typeNameTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_typeNameTextFieldsettingsChangesMade

    private void weightTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weightTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTypeWeight (viewPanel.domain.currentTypeIndex);
        if (evt != null)
        {
            weightTextField.requestFocus ();
            weightTextField.selectAll ();
        }
}//GEN-LAST:event_weightTextFieldActionPerformed

    private void weightTextFieldtextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTextFieldtextFieldFocusGained
        ((JTextField) evt.getComponent ()).selectAll ();
}//GEN-LAST:event_weightTextFieldtextFieldFocusGained

    private void weightTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weightTextFieldFocusLost
        weightTextFieldActionPerformed (null);
}//GEN-LAST:event_weightTextFieldFocusLost

    private void weightTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_weightTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_weightTextFieldsettingsChangesMade

    private void addTypeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTypeButtonActionPerformed
        viewPanel.domain.addType ();
}//GEN-LAST:event_addTypeButtonActionPerformed

    private void removeTypeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTypeButtonActionPerformed
        if (viewPanel.domain.currentTypeIndex != -1)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeTypeText"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeType"));
            optionDialog.setVisible (true);
            if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                int index = settingsTypesTable.getSelectedRow ();
                Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
                typeTableModel.removeRow (index);
                settingsTypesTable.refreshTable ();
                viewPanel.domain.utility.removeAttachmentsToAssignments (course.getType (index));
                viewPanel.domain.utility.types.remove (course.removeType (course.getType (index)));
                viewPanel.domain.needsCoursesAndTermsSave = true;

                if (viewPanel.domain.utility.types.isEmpty ())
                {
                    removeTypeButton.setEnabled (false);
                    moveTypeUpButton.setEnabled (false);
                    moveTypeDownButton.setEnabled (false);
                    viewPanel.domain.setTotalWeightLabel (settingsCoursesTable.getSelectedRow ());
                }

                if (index == typeTableModel.getRowCount ())
                {
                    --index;
                }
                viewPanel.domain.currentTypeIndex = index;
                viewPanel.domain.typeLoading.push (true);
                settingsTypesTable.setSelectedRow (index);
                settingsTypesTableRowSelected (null);
                viewPanel.domain.typeLoading.pop ();
            }
        }
}//GEN-LAST:event_removeTypeButtonActionPerformed

    private void moveTypeUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTypeUpButtonActionPerformed
        viewPanel.domain.typeLoading.push (true);
        if (viewPanel.domain.currentTypeIndex != -1 && viewPanel.domain.currentTypeIndex > 0)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapTypes (viewPanel.domain.currentTypeIndex, viewPanel.domain.currentTypeIndex - 1);
            viewPanel.swap (typeTableModel, 2, viewPanel.domain.currentTypeIndex, viewPanel.domain.currentTypeIndex - 1);
        }
        settingsTypesTable.setSelectedRow (viewPanel.domain.currentTypeIndex - 1);
        viewPanel.domain.typeLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTypeUpButtonActionPerformed

    private void moveTypeDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTypeDownButtonActionPerformed
        viewPanel.domain.typeLoading.push (true);
        if (viewPanel.domain.currentTypeIndex != -1 && viewPanel.domain.currentTypeIndex < typeTableModel.getRowCount () - 1)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapTypes (viewPanel.domain.currentTypeIndex, viewPanel.domain.currentTypeIndex + 1);
            viewPanel.swap (typeTableModel, 2, viewPanel.domain.currentTypeIndex, viewPanel.domain.currentTypeIndex + 1);
        }
        settingsTypesTable.setSelectedRow (viewPanel.domain.currentTypeIndex + 1);
        viewPanel.domain.typeLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTypeDownButtonActionPerformed

    private void textbookNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textbookNameTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookName (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            textbookNameTextField.requestFocus ();
            textbookNameTextField.selectAll ();
        }
}//GEN-LAST:event_textbookNameTextFieldActionPerformed

    private void textbookNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textbookNameTextFieldFocusLost
        textbookNameTextFieldActionPerformed (null);
}//GEN-LAST:event_textbookNameTextFieldFocusLost

    private void textbookNameTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textbookNameTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_textbookNameTextFieldsettingsChangesMade

    private void isbnTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookISBN (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            isbnTextField.requestFocus ();
            isbnTextField.selectAll ();
        }
}//GEN-LAST:event_isbnTextFieldActionPerformed

    private void isbnTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_isbnTextFieldFocusLost
        isbnTextFieldActionPerformed (null);
}//GEN-LAST:event_isbnTextFieldFocusLost

    private void isbnTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_isbnTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_isbnTextFieldsettingsChangesMade

    private void authorTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authorTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookAuthor (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            textbookNameTextField.requestFocus ();
            textbookNameTextField.selectAll ();
        }
}//GEN-LAST:event_authorTextFieldActionPerformed

    private void authorTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_authorTextFieldFocusLost
        authorTextFieldActionPerformed (null);
}//GEN-LAST:event_authorTextFieldFocusLost

    private void authorTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_authorTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_authorTextFieldsettingsChangesMade

    private void publisherTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publisherTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookPublisher (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            publisherTextField.requestFocus ();
            publisherTextField.selectAll ();
        }
}//GEN-LAST:event_publisherTextFieldActionPerformed

    private void publisherTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_publisherTextFieldFocusLost
        publisherTextFieldActionPerformed (null);
}//GEN-LAST:event_publisherTextFieldFocusLost

    private void publisherTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_publisherTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_publisherTextFieldsettingsChangesMade

    private void textbookSourceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textbookSourceTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookSource (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            textbookSourceTextField.requestFocus ();
            textbookSourceTextField.selectAll ();
        }
        if (textbookSourceTextField.getText ().replaceAll (" ", "").equals (""))
        {
            visitTextbookSourceButton.setEnabled (false);
        }
        else
        {
            visitTextbookSourceButton.setEnabled (true);
        }
}//GEN-LAST:event_textbookSourceTextFieldActionPerformed

    private void textbookSourceTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textbookSourceTextFieldFocusLost
        textbookSourceTextFieldActionPerformed (null);
}//GEN-LAST:event_textbookSourceTextFieldFocusLost

    private void textbookSourceTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textbookSourceTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_textbookSourceTextFieldsettingsChangesMade

    private void purchasePriceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasePriceTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookPrice (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            purchasePriceTextField.requestFocus ();
            purchasePriceTextField.selectAll ();
        }
}//GEN-LAST:event_purchasePriceTextFieldActionPerformed

    private void purchasePriceTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_purchasePriceTextFieldFocusLost
        purchasePriceTextFieldActionPerformed (null);
}//GEN-LAST:event_purchasePriceTextFieldFocusLost

    private void purchasePriceTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchasePriceTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_purchasePriceTextFieldsettingsChangesMade

    private void conditionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conditionTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookCondition (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            conditionTextField.requestFocus ();
            conditionTextField.selectAll ();
        }
}//GEN-LAST:event_conditionTextFieldActionPerformed

    private void conditionTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_conditionTextFieldFocusLost
        conditionTextFieldActionPerformed (null);
}//GEN-LAST:event_conditionTextFieldFocusLost

    private void conditionTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_conditionTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_conditionTextFieldsettingsChangesMade

    private void visitTextbookSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitTextbookSourceButtonActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI (textbookSourceTextField.getText ()));
            }
            catch (URISyntaxException ex)
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("textbookURLNoLaunch"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("invalidURL"));
                optionDialog.setVisible (true);
                textbookSourceTextField.requestFocus ();
                textbookSourceTextField.selectAll ();
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
}//GEN-LAST:event_visitTextbookSourceButtonActionPerformed

    private void contactEmailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactEmailTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setTextbookContactEmail (viewPanel.domain.currentTextbookIndex);
        if (evt != null)
        {
            contactEmailTextField.requestFocus ();
            contactEmailTextField.selectAll ();
        }
        if (contactEmailTextField.getText ().equals (""))
        {
            contactSendEmailButton.setEnabled (false);
        }
        else
        {
            contactSendEmailButton.setEnabled (true);
        }
}//GEN-LAST:event_contactEmailTextFieldActionPerformed

    private void contactEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactEmailTextFieldFocusLost
        contactEmailTextFieldActionPerformed (null);
}//GEN-LAST:event_contactEmailTextFieldFocusLost

    private void contactEmailTextFieldsettingsChangesMade(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactEmailTextFieldsettingsChangesMade
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            if (evt.getSource () == termNameTextField)
            {
                Term term = viewPanel.domain.utility.terms.get (viewPanel.domain.currentTermIndex);
                termNameTextField.setText (term.getTypeName ());
            }
            else if (evt.getSource () == courseNameTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNameTextField.setText (course.getTypeName ());
            }
            else if (evt.getSource () == roomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                roomTextField.setText (course.getRoomLocation ());
            }
            else if (evt.getSource () == labRoomTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labRoomTextField.setText (course.getLabRoomLocation ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == typeNameTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                typeNameTextField.setText (type.getTypeName ());
            }
            else if (evt.getSource () == weightTextField)
            {
                AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (viewPanel.domain.currentTypeIndex);
                weightTextField.setText (type.getWeight ());
            }
            else if (evt.getSource () == textbookNameTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookNameTextField.setText (textbook.getTypeName ());
            }
            else if (evt.getSource () == authorTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                authorTextField.setText (textbook.getAuthor ());
            }
            else if (evt.getSource () == isbnTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                isbnTextField.setText (textbook.getISBN ());
            }
            else if (evt.getSource () == publisherTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                publisherTextField.setText (textbook.getPublisher ());
            }
            else if (evt.getSource () == textbookSourceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                textbookSourceTextField.setText (textbook.getSource ());
            }
            else if (evt.getSource () == contactEmailTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                contactEmailTextField.setText (textbook.getContactEmail ());
            }
            else if (evt.getSource () == purchasePriceTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                purchasePriceTextField.setText (textbook.getPrice ());
            }
            else if (evt.getSource () == conditionTextField)
            {
                Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
                conditionTextField.setText (textbook.getCondition ());
            }
            else if (evt.getSource () == instructorNameTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorNameTextField.setText (instructor.getTypeName ());
            }
            else if (evt.getSource () == instructorEmailTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                instructorEmailTextField.setText (instructor.getInstructorEmail ());
            }
            else if (evt.getSource () == phoneTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                phoneTextField.setText (instructor.getInstructorPhone ());
            }
            else if (evt.getSource () == officeHoursTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeHoursTextField.setText (instructor.getOfficeHours ());
            }
            else if (evt.getSource () == officeLocationTextField)
            {
                Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (viewPanel.domain.currentInstructorIndex);
                officeLocationTextField.setText (instructor.getOfficeLocation ());
            }
            else if (evt.getSource () == courseNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                courseNumberTextField.setText (course.getCourseNumber ());
            }
            else if (evt.getSource () == labNumberTextField)
            {
                Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
                labNumberTextField.setText (course.getLabNumber ());
            }
        }
        viewPanel.domain.needsSettingsSaveBool = true;
}//GEN-LAST:event_contactEmailTextFieldsettingsChangesMade

    private void orderedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderedCheckBoxActionPerformed
        if (viewPanel.domain.currentTextbookIndex != -1 && viewPanel.domain.textbookLoading.empty ())
        {
            Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
            textbook.setIsOrdered (orderedCheckBox.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_orderedCheckBoxActionPerformed

    private void receivedCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_receivedCheckBoxActionPerformed
        if (viewPanel.domain.currentTextbookIndex != -1 && viewPanel.domain.textbookLoading.empty ())
        {
            Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex);
            textbook.setIsReceived (receivedCheckBox.isSelected ());
            viewPanel.domain.needsCoursesAndTermsSave = true;
        }
}//GEN-LAST:event_receivedCheckBoxActionPerformed

    private void contactSendEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contactSendEmailButtonActionPerformed
        Matcher matcher = Domain.EMAIL_PATTERN.matcher (contactEmailTextField.getText ());
        if (Domain.desktop != null)
        {
            if (matcher.matches ())
            {
                try
                {
                    Domain.desktop.mail (new URI ("mailto", contactEmailTextField.getText () + "?subject=" + viewPanel.domain.language.getString ("questionAbout") + " " + viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (viewPanel.domain.currentTextbookIndex).getTypeName ().replaceAll ("&", ""), null));
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
                contactEmailTextField.requestFocus ();
                contactEmailTextField.selectAll ();
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("noSendEmailClientNotFound"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToSend"));
            optionDialog.setVisible (true);
        }
}//GEN-LAST:event_contactSendEmailButtonActionPerformed

    private void searchGoogleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchGoogleButtonActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("http://www.google.com/search?q=isbn%20" + isbnTextField.getText ()));
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
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
}//GEN-LAST:event_searchGoogleButtonActionPerformed

    private void addTextbookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTextbookButtonActionPerformed
        viewPanel.domain.addTextbook ();
}//GEN-LAST:event_addTextbookButtonActionPerformed

    private void removeTextbookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeTextbookButtonActionPerformed
        if (viewPanel.domain.currentTextbookIndex != -1)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeTextbookText"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeTextbook"));
            optionDialog.setVisible (true);
            if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                int index = settingsTextbooksTable.getSelectedRow ();
                Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
                textbookTableModel.removeRow (index);
                settingsTextbooksTable.refreshTable ();
                viewPanel.domain.utility.removeAttachmentsToAssignments (course.getTextbook (index));
                viewPanel.domain.utility.textbooks.remove (course.removeTextbook (course.getTextbook (index)));
                viewPanel.domain.needsCoursesAndTermsSave = true;

                if (viewPanel.domain.utility.textbooks.isEmpty ())
                {
                    removeTextbookButton.setEnabled (false);
                    moveTextbookUpButton.setEnabled (false);
                    moveTextbookDownButton.setEnabled (false);
                }

                if (index == textbookTableModel.getRowCount ())
                {
                    --index;
                }
                viewPanel.domain.currentTextbookIndex = index;
                viewPanel.domain.textbookLoading.push (true);
                settingsTextbooksTable.setSelectedRow (index);
                settingsTextbooksTableRowSelected (null);
                if (index == -1)
                {
                    settingsTextbooksTableRowSelected (null);
                }
                viewPanel.domain.textbookLoading.pop ();
            }
        }
}//GEN-LAST:event_removeTextbookButtonActionPerformed

    private void moveTextbookUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTextbookUpButtonActionPerformed
        viewPanel.domain.textbookLoading.push (true);
        if (viewPanel.domain.currentTextbookIndex != -1 && viewPanel.domain.currentTextbookIndex > 0)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapTextbooks (viewPanel.domain.currentTextbookIndex, viewPanel.domain.currentTextbookIndex - 1);
            viewPanel.swap (textbookTableModel, 3, viewPanel.domain.currentTextbookIndex, viewPanel.domain.currentTextbookIndex - 1);
        }
        settingsTextbooksTable.setSelectedRow (viewPanel.domain.currentTextbookIndex - 1);
        viewPanel.domain.textbookLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTextbookUpButtonActionPerformed

    private void moveTextbookDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveTextbookDownButtonActionPerformed
        viewPanel.domain.textbookLoading.push (true);
        if (viewPanel.domain.currentTextbookIndex != -1 && viewPanel.domain.currentTextbookIndex < textbookTableModel.getRowCount () - 1)
        {
            Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex);
            course.swapTextbooks (viewPanel.domain.currentTextbookIndex, viewPanel.domain.currentTextbookIndex + 1);
            viewPanel.swap (textbookTableModel, 3, viewPanel.domain.currentTextbookIndex, viewPanel.domain.currentTextbookIndex + 1);
        }
        settingsTextbooksTable.setSelectedRow (viewPanel.domain.currentTextbookIndex + 1);
        viewPanel.domain.textbookLoading.pop ();
        viewPanel.domain.needsCoursesAndTermsSave = true;
}//GEN-LAST:event_moveTextbookDownButtonActionPerformed

    private void removeCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCourseButtonActionPerformed
        if (settingsCoursesTable.getSelectedRow () != -1)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeCourseText"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
            JDialog optionDialog = null;
            if (evt != null)
            {
                optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeCourse"));
            }
            else
            {
                optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("removeCourse"));
            }
            optionDialog.setVisible (true);
            if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
            {
                int index = settingsCoursesTable.getSelectedRow ();
                if (evt == null)
                {
                    index = viewPanel.getSelectedCourseIndex ();
                }
                Course course = viewPanel.domain.utility.courses.get (index);
                viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (course.getPath ()));
                Term term = course.getTerm ();
                viewPanel.domain.utility.removeAssignmentsAttachedTo (course);
                viewPanel.domain.utility.removeTypesAttachedTo (course);
                viewPanel.domain.utility.removeInstructorsAttachedTo (course);
                viewPanel.domain.utility.removeTextbooksAttachedTo (course);
                if (evt != null)
                {
                    courseTableModel.removeRow (index);
                    settingsCoursesTable.refreshTable ();
                }
                viewPanel.domain.utility.courses.remove (term.removeCourse (course));
                viewPanel.domain.refreshTermTree ();
                viewPanel.domain.needsCoursesAndTermsSave = true;
                course.markForDeletion ();

                if (viewPanel.domain.utility.courses.isEmpty ())
                {
                    viewPanel.disableAssignmentButtons ();
                }

                if (index == viewPanel.domain.utility.courses.size ())
                {
                    --index;
                }
                viewPanel.domain.currentCourseIndex = index;
                viewPanel.domain.courseLoading.push (true);
                if (evt != null)
                {
                    settingsCoursesTable.setSelectedRow (index);
                    viewPanel.domain.currentTypeIndex = -1;
                    settingsInstructorsTable.setSelectedRow (index);
                    viewPanel.domain.currentInstructorIndex = -1;
                    settingsTypesTable.setSelectedRow (-1);
                    viewPanel.domain.currentTextbookIndex = -1;
                    settingsTextbooksTable.setSelectedRow (-1);
                    settingsCoursesTableRowSelected (null);
                }
                if (index != -1)
                {
                    viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (course.getPath ()));
                }
                else
                {
                    viewPanel.termTree.getSelectionModel ().setSelectionPath (new TreePath (term.getPath ()));
                }
                viewPanel.refreshBusyDays ();
                viewPanel.domain.courseLoading.pop ();
            }
        }
}//GEN-LAST:event_removeCourseButtonActionPerformed

    private void addCourseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCourseButtonActionPerformed
        viewPanel.domain.addCourse ();
}//GEN-LAST:event_addCourseButtonActionPerformed

    private void moveCourseUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveCourseUpButtonActionPerformed
        viewPanel.domain.courseLoading.push (true);
        if (settingsCoursesTable.getSelectedRow () != -1 && settingsCoursesTable.getSelectedRow () > 0)
        {
            Term term1 = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getTerm ();
            Term term2 = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow () - 1).getTerm ();
            if (term1 == term2)
            {
                term1.swapCourses (term1.getCourseIndex (viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ())), term1.getCourseIndex (viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow () - 1)));
                viewPanel.domain.refreshTermTree ();
                viewPanel.swap (courseTableModel, 1, settingsCoursesTable.getSelectedRow (), settingsCoursesTable.getSelectedRow () - 1);
                settingsCoursesTable.setSelectedRow (settingsCoursesTable.getSelectedRow () - 1);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            else
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("courseTermMismatchUpText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("courseTermMismatch"));
                optionDialog.setVisible (true);
            }
        }
        viewPanel.domain.courseLoading.pop ();
}//GEN-LAST:event_moveCourseUpButtonActionPerformed

    private void moveCourseDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveCourseDownButtonActionPerformed
        viewPanel.domain.courseLoading.push (true);
        if (settingsCoursesTable.getSelectedRow () != -1 && settingsCoursesTable.getSelectedRow () < courseTableModel.getRowCount () - 1)
        {
            Term term1 = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getTerm ();
            Term term2 = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow () + 1).getTerm ();
            if (term1 == term2)
            {
                term1.swapCourses (term1.getCourseIndex (viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ())), term1.getCourseIndex (viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow () + 1)));
                viewPanel.domain.refreshTermTree ();
                viewPanel.swap (courseTableModel, 1, settingsCoursesTable.getSelectedRow (), settingsCoursesTable.getSelectedRow () + 1);
                settingsCoursesTable.setSelectedRow (settingsCoursesTable.getSelectedRow () + 1);
                viewPanel.domain.needsCoursesAndTermsSave = true;
            }
            else
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("courseTermMismatchDownText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("courseTermMismatch"));
                optionDialog.setVisible (true);
            }
        }
        viewPanel.domain.courseLoading.pop ();
}//GEN-LAST:event_moveCourseDownButtonActionPerformed

    private void termsAndCoursesTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_termsAndCoursesTabbedPaneStateChanged
        if (!viewPanel.initLoading)
        {
            if (viewPanel.domain.currentCourseIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentCourseIndex, 1);
            }
            if (viewPanel.domain.currentTermIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentTermIndex, 0);
            }

            // fill the terms list
            if (termsAndCoursesTabbedPane.getSelectedIndex () == 0)
            {
                viewPanel.domain.utility.loadSettingsTermTable ();
                viewPanel.domain.termLoading.push (true);
                settingsTermsTable.setSelectedRow (viewPanel.domain.currentTermIndex);
                viewPanel.domain.termLoading.pop ();

                if (settingsTermsTable.getSelectedRow () == -1
                    && settingsTermsTable.getRowCount () > 0)
                {
                    settingsTermsTable.setSelectedRow (0);
                    settingsTermsTableRowSelected (null);
                }
            }
            // fill the courses list and the terms combo
            else if (termsAndCoursesTabbedPane.getSelectedIndex () == 1)
            {
                updateSettingsCourseInformation ();
            }
        }
}//GEN-LAST:event_termsAndCoursesTabbedPaneStateChanged

    private void coursesComponentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coursesComponentFocusGained
        courseInnerDetailsPanel.scrollRectToVisible (evt.getComponent ().getBounds ());
    }//GEN-LAST:event_coursesComponentFocusGained

    private void textbooksComponentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textbooksComponentFocusGained
        textbookDetailsPanel.scrollRectToVisible (evt.getComponent ().getBounds ());
    }//GEN-LAST:event_textbooksComponentFocusGained

    private void instructorsComponentFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_instructorsComponentFocusGained
        instructorsDetailsPanel.scrollRectToVisible (evt.getComponent ().getBounds ());
    }//GEN-LAST:event_instructorsComponentFocusGained

    private void textbooksTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textbooksTextFieldFocusGained
        textbooksComponentFocusGained (evt);
        ((javax.swing.JTextField) evt.getComponent ()).selectAll ();
    }//GEN-LAST:event_textbooksTextFieldFocusGained

    private void instructorsTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_instructorsTextFieldFocusGained
        textbooksComponentFocusGained (evt);
        ((javax.swing.JTextField) evt.getComponent ()).selectAll ();
    }//GEN-LAST:event_instructorsTextFieldFocusGained

    private void coursesTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_coursesTextFieldFocusGained
        coursesComponentFocusGained (evt);
        ((JTextField) evt.getComponent ()).selectAll ();
    }//GEN-LAST:event_coursesTextFieldFocusGained

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeTermsAndCoursesDialog ();
    }//GEN-LAST:event_formWindowClosing

    private void courseWebsiteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseWebsiteTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setCourseWebsite (viewPanel.domain.currentCourseIndex);
        if (evt != null)
        {
            courseWebsiteTextField.requestFocus ();
            courseWebsiteTextField.selectAll ();
        }
        if (courseWebsiteTextField.getText ().replaceAll (" ", "").equals (""))
        {
            visitCourseWebsiteButton.setEnabled (false);
        }
        else
        {
            visitCourseWebsiteButton.setEnabled (true);
        }
    }//GEN-LAST:event_courseWebsiteTextFieldActionPerformed

    private void courseWebsiteTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_courseWebsiteTextFieldFocusLost
        courseWebsiteTextFieldActionPerformed (null);
    }//GEN-LAST:event_courseWebsiteTextFieldFocusLost

    private void labWebsiteTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labWebsiteTextFieldActionPerformed
        if (viewPanel.domain.needsSettingsSaveBool)
        {
            viewPanel.domain.needsCoursesAndTermsSave = true;
            viewPanel.domain.needsSettingsSaveBool = false;
        }
        viewPanel.domain.setLabWebsite (viewPanel.domain.currentCourseIndex);
        if (evt != null)
        {
            labWebsiteTextField.requestFocus ();
            labWebsiteTextField.selectAll ();
        }
        if (labWebsiteTextField.getText ().replaceAll (" ", "").equals (""))
        {
            visitLabWebsiteButton.setEnabled (false);
        }
        else
        {
            visitLabWebsiteButton.setEnabled (true);
        }
    }//GEN-LAST:event_labWebsiteTextFieldActionPerformed

    private void labWebsiteTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_labWebsiteTextFieldFocusLost
        labWebsiteTextFieldActionPerformed (null);
    }//GEN-LAST:event_labWebsiteTextFieldFocusLost

    private void visitCourseWebsiteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitCourseWebsiteButtonActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI (courseWebsiteTextField.getText ()));
            }
            catch (URISyntaxException ex)
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("invalidCourseUrlText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("invalidURL"));
                optionDialog.setVisible (true);
                courseWebsiteTextField.requestFocus ();
                courseWebsiteTextField.selectAll ();
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
    }//GEN-LAST:event_visitCourseWebsiteButtonActionPerformed

    private void visitLabWebsiteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_visitLabWebsiteButtonActionPerformed
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI (labWebsiteTextField.getText ()));
            }
            catch (URISyntaxException ex)
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("invalidCourseLabUrlText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("invalidURL"));
                optionDialog.setVisible (true);
                labWebsiteTextField.requestFocus ();
                labWebsiteTextField.selectAll ();
            }
            catch (IOException ex)
            {
                Domain.LOGGER.add (ex);
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("browserCouldntLaunch"));
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("unableToVisit"));
            optionDialog.setVisible (true);
        }
    }//GEN-LAST:event_visitLabWebsiteButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCourseButton;
    private javax.swing.JButton addCourseToTermButton;
    private javax.swing.JButton addInstructorButton;
    private javax.swing.JButton addTermButton;
    private javax.swing.JButton addTextbookButton;
    private javax.swing.JButton addTypeButton;
    private javax.swing.JLabel assignedToTermLabel;
    private javax.swing.JLabel authorLabel;
    protected javax.swing.JTextField authorTextField;
    protected javax.swing.JRadioButton bothRadioButton;
    private javax.swing.JLabel conditionLabel;
    protected javax.swing.JTextField conditionTextField;
    private javax.swing.JLabel contactEmailLabel;
    protected javax.swing.JTextField contactEmailTextField;
    private javax.swing.JButton contactSendEmailButton;
    private javax.swing.JLabel courseColon1;
    private javax.swing.JLabel courseColon2;
    private javax.swing.JLabel courseColon3;
    private javax.swing.JLabel courseColon4;
    private javax.swing.JLabel courseColorLabel;
    private javax.swing.JPanel courseColorPanel;
    private javax.swing.JPanel courseDetailsCardPanel;
    private javax.swing.JLabel courseDetailsLabel;
    private javax.swing.JLabel courseDetailsNoLabel;
    protected com.toedter.calendar.JDateChooser courseEndDateChooser;
    private javax.swing.JLabel courseEndDateLabel;
    protected javax.swing.JCheckBox courseHasLabCheckBox;
    protected adl.go.gui.ColoredJPanel courseInnerDetailsPanel;
    private javax.swing.JLabel courseListLabel;
    private javax.swing.JPanel courseListPanel;
    protected javax.swing.JTextField courseNameTextField;
    private javax.swing.JLabel courseNumberLabel;
    protected javax.swing.JTextField courseNumberTextField;
    protected javax.swing.JScrollPane courseScrollPane;
    protected com.toedter.calendar.JDateChooser courseStartDateChooser;
    private javax.swing.JLabel courseStartDateLabel;
    protected javax.swing.JTabbedPane courseTabbedPane;
    private javax.swing.JLabel courseTimeLabel;
    private javax.swing.JLabel courseWebsiteLabel;
    protected javax.swing.JTextField courseWebsiteTextField;
    protected adl.go.gui.ColoredJPanel coursesPanel;
    private javax.swing.JLabel creditsLabel;
    private javax.swing.JSpinner creditsSpinner;
    private javax.swing.JLabel daysLabel;
    private javax.swing.JButton emailInstructorButton;
    protected com.toedter.calendar.JSpinnerDateEditor endHrChooser;
    protected com.toedter.calendar.JSpinnerDateEditor endMChooser;
    protected com.toedter.calendar.JSpinnerDateEditor endMinChooser;
    private javax.swing.JToggleButton friToggleButton;
    private javax.swing.ButtonGroup instructorButtonGroup;
    private javax.swing.JLabel instructorDetailsLabel;
    private javax.swing.JPanel instructorDetailsPanel;
    private javax.swing.JLabel instructorEmailLabel;
    protected javax.swing.JTextField instructorEmailTextField;
    protected javax.swing.JTextField instructorNameTextField;
    protected adl.go.gui.ColoredJPanel instructorsDetailsPanel;
    protected javax.swing.JLabel instructorsListLabel;
    private javax.swing.JScrollPane instructorsScrollPane;
    private javax.swing.JLabel isbnLabel;
    protected javax.swing.JTextField isbnTextField;
    private javax.swing.JLabel labCreditsLabel;
    private javax.swing.JSpinner labCreditsSpinner;
    private javax.swing.JLabel labDaysLabel;
    private javax.swing.JLabel labDetailsLabel;
    protected com.toedter.calendar.JDateChooser labEndDateChooser;
    private javax.swing.JLabel labEndDateLabel;
    protected com.toedter.calendar.JSpinnerDateEditor labEndHrChooser;
    protected com.toedter.calendar.JSpinnerDateEditor labEndMChooser;
    protected com.toedter.calendar.JSpinnerDateEditor labEndMinChooser;
    private javax.swing.JToggleButton labFriToggleButton;
    private javax.swing.JToggleButton labMonToggleButton;
    private javax.swing.JLabel labNumberLabel;
    protected javax.swing.JTextField labNumberTextField;
    protected javax.swing.JCheckBox labOnlineCheckBox;
    private javax.swing.JRadioButton labRadioButton;
    private javax.swing.JLabel labRoomLabel;
    protected javax.swing.JTextField labRoomTextField;
    private javax.swing.JToggleButton labSatToggleButton;
    protected com.toedter.calendar.JDateChooser labStartDateChooser;
    private javax.swing.JLabel labStartDateLabel;
    protected com.toedter.calendar.JSpinnerDateEditor labStartHrChooser;
    protected com.toedter.calendar.JSpinnerDateEditor labStartMChooser;
    protected com.toedter.calendar.JSpinnerDateEditor labStartMinChooser;
    private javax.swing.JToggleButton labSunToggleButton;
    private javax.swing.JToggleButton labThuToggleButton;
    private javax.swing.JLabel labTimeLabel;
    private javax.swing.JToggleButton labTueToggleButton;
    private javax.swing.JLabel labWebsiteLabel;
    protected javax.swing.JTextField labWebsiteTextField;
    private javax.swing.JToggleButton labWedToggleButton;
    private javax.swing.JRadioButton lectureRadioButton;
    private javax.swing.JToggleButton monToggleButton;
    private javax.swing.JButton moveCourseDownButton;
    private javax.swing.JButton moveCourseUpButton;
    private javax.swing.JButton moveInstructorDownButton;
    private javax.swing.JButton moveInstructorUpButton;
    private javax.swing.JButton moveTermDownButton;
    private javax.swing.JButton moveTermUpButton;
    private javax.swing.JButton moveTextbookDownButton;
    private javax.swing.JButton moveTextbookUpButton;
    private javax.swing.JButton moveTypeDownButton;
    private javax.swing.JButton moveTypeUpButton;
    private javax.swing.JPanel noCourseDetailsPanel;
    private javax.swing.JPanel noTermDetailsPanel;
    private javax.swing.JLabel noTermsTermLabel;
    private javax.swing.JLabel noTermsYetLabel;
    private javax.swing.JLabel officeHoursLabel;
    protected javax.swing.JTextField officeHoursTextField;
    private javax.swing.JLabel officeLocationLabel;
    protected javax.swing.JTextField officeLocationTextField;
    protected javax.swing.JCheckBox onlineCheckBox;
    private javax.swing.JLabel onlineSourceLabel;
    private javax.swing.JCheckBox orderedCheckBox;
    private javax.swing.JLabel phoneLabel;
    protected javax.swing.JTextField phoneTextField;
    private javax.swing.JLabel publisherLabel;
    protected javax.swing.JTextField publisherTextField;
    private javax.swing.JLabel purchasePrice;
    protected javax.swing.JTextField purchasePriceTextField;
    private javax.swing.JCheckBox receivedCheckBox;
    protected javax.swing.JButton removeCourseButton;
    protected javax.swing.JButton removeInstructorButton;
    protected javax.swing.JButton removeTermButton;
    protected javax.swing.JButton removeTextbookButton;
    protected javax.swing.JButton removeTypeButton;
    private javax.swing.JLabel roomLabel;
    protected javax.swing.JTextField roomTextField;
    private javax.swing.JToggleButton satToggleButton;
    protected javax.swing.JButton searchGoogleButton;
    private javax.swing.JScrollPane settingsCoursesScrollPane;
    public adl.go.gui.ExtendedJTable settingsCoursesTable;
    public adl.go.gui.ExtendedJTable settingsInstructorsTable;
    private javax.swing.JScrollPane settingsTermsScrollPane;
    public adl.go.gui.ExtendedJTable settingsTermsTable;
    private javax.swing.JScrollPane settingsTextbooksScrollPane;
    public adl.go.gui.ExtendedJTable settingsTextbooksTable;
    private javax.swing.JScrollPane settingsTypesScrollPane;
    private javax.swing.JScrollPane settingsTypesScrollPane1;
    public adl.go.gui.ExtendedJTable settingsTypesTable;
    protected com.toedter.calendar.JSpinnerDateEditor startHrChooser;
    protected com.toedter.calendar.JSpinnerDateEditor startMChooser;
    protected com.toedter.calendar.JSpinnerDateEditor startMinChooser;
    private javax.swing.JToggleButton sunToggleButton;
    private javax.swing.JPanel termDetailsCardPanel;
    private javax.swing.JLabel termDetailsLabel;
    private javax.swing.JLabel termDetailsNoLabel;
    private javax.swing.JPanel termDetailsPanel;
    protected com.toedter.calendar.JDateChooser termEndDateChooser;
    private javax.swing.JLabel termEndDateLabel;
    protected javax.swing.JTextField termNameTextField;
    protected com.toedter.calendar.JDateChooser termStartDateChooser;
    private javax.swing.JLabel termStartDateLabel;
    private javax.swing.JButton termsAndCoursesCloseButton;
    protected javax.swing.JPanel termsAndCoursesJPanel;
    private javax.swing.JLabel termsAndCoursesLabel;
    protected javax.swing.JTabbedPane termsAndCoursesTabbedPane;
    protected adl.go.gui.ColoredJPanel termsAndCoursesUpperJPanel;
    protected javax.swing.JComboBox termsComboBox;
    private javax.swing.JLabel termsListLabel;
    private javax.swing.JPanel termsListPanel;
    protected adl.go.gui.ColoredJPanel termsPanel;
    private javax.swing.JLabel textbookDetailsLabel;
    private javax.swing.JPanel textbookDetailsPanel;
    protected javax.swing.JTextField textbookNameTextField;
    protected javax.swing.JTextField textbookSourceTextField;
    protected adl.go.gui.ColoredJPanel textbooksDetailsPanel;
    private javax.swing.JLabel textbooksListLabel;
    protected javax.swing.JScrollPane textbooksScrollPane;
    private javax.swing.JToggleButton thuToggleButton;
    private javax.swing.JToggleButton tueToggleButton;
    private javax.swing.JLabel typeDetailsLabel;
    private javax.swing.JPanel typeDetailsPanel;
    protected javax.swing.JTextField typeNameTextField;
    private javax.swing.JLabel typeWeightLabel;
    protected adl.go.gui.ColoredJPanel typesDetailsPanel;
    protected javax.swing.JLabel typesListLabel;
    private javax.swing.JButton visitCourseWebsiteButton;
    private javax.swing.JButton visitLabWebsiteButton;
    private javax.swing.JButton visitTextbookSourceButton;
    private javax.swing.JToggleButton wedToggleButton;
    protected javax.swing.JTextField weightTextField;
    // End of variables declaration//GEN-END:variables

    /**
     * Launch the terms and courses dialog.
     */
    protected void goViewTermsAndCourses()
    {
        if (viewPanel.settingsDialog.isVisible ())
        {
            viewPanel.settingsDialog.closeSettingsDialog ();
        }
        if (viewPanel.gradesDialog.isVisible ())
        {
            viewPanel.gradesDialog.setVisible (false);
        }
        viewPanel.closeRepeatEventDialog ();

        if (!isVisible ())
        {
            pack ();
            setLocationRelativeTo (viewPanel);
            setVisible (true);

            viewPanel.assignmentsTable.setSelectedRow (-1);
            viewPanel.assignmentsTableRowSelected (null);
            viewPanel.termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));

            termsAndCoursesTabbedPane.setSelectedIndex (0);
            termsAndCoursesTabbedPaneStateChanged (null);
            courseTabbedPane.setSelectedIndex (0);
            settingsTermsTableRowSelected (null);
            settingsCoursesTableRowSelected (null);
            settingsTypesTableRowSelected (null);
            settingsInstructorsTableRowSelected (null);
            settingsTextbooksTableRowSelected (null);
        }
        else
        {
            requestFocus ();
        }
    }

    /**
     * Performs a close operation on the terms and courses dialog, checking for
     * unsaved changes first.
     */
    public void closeTermsAndCoursesDialog()
    {
        dispose ();
        viewPanel.requestFocus ();

        if (termsAndCoursesTabbedPane.getSelectedIndex () == 0)
        {
            checkTermsAndCoursesChanges (settingsTermsTable.getSelectedRow (), 0);
        }
        else
        {
            if (termsAndCoursesTabbedPane.getSelectedIndex () == 1)
            {
                checkTermsAndCoursesChanges (settingsCoursesTable.getSelectedRow (), 1);
            }
        }
        viewPanel.domain.refreshAssignmentsList ();
        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            viewPanel.domain.refreshShownAssignmentsAndEvents ();
        }

        viewPanel.termTreeNodeSelected (new TreeSelectionEvent (this, null, true, null, null));
    }

    /**
     * Update enabled states of course lab details.
     *
     * @param course The course to update the UI for.
     */
    protected void updateLabUI(Course course)
    {
        if (course.hasLab ())
        {
            labDetailsLabel.setEnabled (true);
            labNumberLabel.setEnabled (true);
            labNumberTextField.setEnabled (true);
            labCreditsLabel.setEnabled (true);
            if (labWebsiteTextField.getText ().equals (""))
            {
                visitLabWebsiteButton.setEnabled (false);
            }
            else
            {
                visitLabWebsiteButton.setEnabled (true);
            }
            labWebsiteLabel.setEnabled (true);
            labWebsiteTextField.setEnabled (true);
            labCreditsSpinner.setEnabled (true);
            labOnlineCheckBox.setEnabled (true);

            labStartDateLabel.setEnabled (true);
            labStartDateChooser.setEnabled (true);
            labEndDateLabel.setEnabled (true);
            labEndDateChooser.setEnabled (true);
        }
        else
        {
            labDetailsLabel.setEnabled (false);
            labNumberLabel.setEnabled (false);
            labNumberTextField.setEnabled (false);
            visitLabWebsiteButton.setEnabled (false);
            labWebsiteLabel.setEnabled (false);
            labWebsiteTextField.setEnabled (false);
            labCreditsLabel.setEnabled (false);
            labCreditsSpinner.setEnabled (false);
            labOnlineCheckBox.setEnabled (false);

            labStartDateLabel.setEnabled (false);
            labStartDateChooser.setEnabled (false);
            labEndDateLabel.setEnabled (false);
            labEndDateChooser.setEnabled (false);

            labTimeLabel.setEnabled (false);
            labStartHrChooser.setEnabled (false);
            labStartMinChooser.setEnabled (false);
            labStartMChooser.setEnabled (false);
            labEndHrChooser.setEnabled (false);
            labEndMinChooser.setEnabled (false);
            labEndMChooser.setEnabled (false);
            courseColon3.setEnabled (false);
            courseColon4.setEnabled (false);

            labRoomLabel.setEnabled (false);
            labRoomTextField.setEnabled (false);

            labDaysLabel.setEnabled (false);
            labSunToggleButton.setEnabled (false);
            labMonToggleButton.setEnabled (false);
            labTueToggleButton.setEnabled (false);
            labWedToggleButton.setEnabled (false);
            labThuToggleButton.setEnabled (false);
            labFriToggleButton.setEnabled (false);
            labSatToggleButton.setEnabled (false);
        }
        if (course.hasLab () && course.labIsOnline ())
        {
            labTimeLabel.setEnabled (false);
            labStartHrChooser.setEnabled (false);
            labStartMinChooser.setEnabled (false);
            labStartMChooser.setEnabled (false);
            labEndHrChooser.setEnabled (false);
            labEndMinChooser.setEnabled (false);
            labEndMChooser.setEnabled (false);
            courseColon3.setEnabled (false);
            courseColon4.setEnabled (false);

            labRoomLabel.setEnabled (false);
            labRoomTextField.setEnabled (false);

            labDaysLabel.setEnabled (false);
            labSunToggleButton.setEnabled (false);
            labMonToggleButton.setEnabled (false);
            labTueToggleButton.setEnabled (false);
            labWedToggleButton.setEnabled (false);
            labThuToggleButton.setEnabled (false);
            labFriToggleButton.setEnabled (false);
            labSatToggleButton.setEnabled (false);
        }
        else if (course.hasLab () && !course.labIsOnline ())
        {
            labTimeLabel.setEnabled (true);
            labStartHrChooser.setEnabled (true);
            labStartMinChooser.setEnabled (true);
            labStartMChooser.setEnabled (true);
            labEndHrChooser.setEnabled (true);
            labEndMinChooser.setEnabled (true);
            labEndMChooser.setEnabled (true);
            courseColon3.setEnabled (true);
            courseColon4.setEnabled (true);

            labRoomLabel.setEnabled (true);
            labRoomTextField.setEnabled (true);

            labDaysLabel.setEnabled (true);
            labSunToggleButton.setEnabled (true);
            labMonToggleButton.setEnabled (true);
            labTueToggleButton.setEnabled (true);
            labWedToggleButton.setEnabled (true);
            labThuToggleButton.setEnabled (true);
            labFriToggleButton.setEnabled (true);
            labSatToggleButton.setEnabled (true);
        }
    }

    /**
     * The event that occurs when an item is selected in the terms list.
     *
     * @param evt The event trigger object.
     */
    protected void settingsTermsTableRowSelected(ListSelectionEvent evt)
    {
        if (settingsTermsTable.getSelectedRow () != -1)
        {
            if (viewPanel.domain.currentTermIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentTermIndex, 0);
            }
            viewPanel.domain.currentTermIndex = settingsTermsTable.getSelectedRow ();
            removeTermButton.setEnabled (true);

            if (settingsTermsTable.getSelectedRow () > 0)
            {
                moveTermUpButton.setEnabled (true);
            }
            else
            {
                moveTermUpButton.setEnabled (false);
            }
            if (settingsTermsTable.getSelectedRow () < termTableModel.getRowCount () - 1)
            {
                moveTermDownButton.setEnabled (true);
            }
            else
            {
                moveTermDownButton.setEnabled (false);
            }

            settingsTermsTable.scrollRectToVisible (settingsTermsTable.getCellRect (viewPanel.domain.currentTermIndex, 0, false));
            showTermDetails ();
        }
        else
        {
            removeTermButton.setEnabled (false);
            moveTermUpButton.setEnabled (false);
            moveTermDownButton.setEnabled (false);
            clearTermDetails ();
        }
    }

    /**
     * Finds the type with the specified id and selects it.
     *
     * @param id The id to be set for selection in the types table.
     */
    protected void setSettingsTypesTableSelection(long id)
    {
        for (int i = 0; i < typeTableModel.getRowCount (); ++i)
        {
            if (Long.parseLong (typeTableModel.getValueAt (i, 2).toString ()) == id)
            {
                settingsTypesTable.getSelectionModel ().setSelectionInterval (i, i);
                break;
            }
        }
    }

    /**
     * Finds the instructor with the specified id and selects it.
     *
     * @param id The id to be set for selection in the instructors table.
     */
    protected void setSettingsInstructorsTableSelection(long id)
    {
        for (int i = 0; i < instructorTableModel.getRowCount (); ++i)
        {
            if (Long.parseLong (instructorTableModel.getValueAt (i, 2).toString ()) == id)
            {
                settingsInstructorsTable.getSelectionModel ().setSelectionInterval (i, i);
                break;
            }
        }
    }

    /**
     * The event that occurs when an item is selected in the courses list.
     *
     * @param e The event trigger object.
     */
    protected void settingsCoursesTableRowSelected(ListSelectionEvent e)
    {
        if (settingsCoursesTable.getSelectedRow () != -1)
        {
            if (viewPanel.domain.currentCourseIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentCourseIndex, 1);
                if (viewPanel.domain.currentInstructorIndex != -1 && courseTabbedPane.getSelectedIndex () == 1)
                {
                    checkTermsAndCoursesChanges (viewPanel.domain.currentInstructorIndex, 4);
                }
                if (viewPanel.domain.currentTypeIndex != -1 && courseTabbedPane.getSelectedIndex () == 2)
                {
                    checkTermsAndCoursesChanges (viewPanel.domain.currentTypeIndex, 2);
                }
                if (viewPanel.domain.currentTextbookIndex != -1 && courseTabbedPane.getSelectedIndex () == 3)
                {
                    checkTermsAndCoursesChanges (viewPanel.domain.currentTextbookIndex, 3);
                }
                viewPanel.domain.currentTypeIndex = -1;
                settingsTypesTable.setSelectedRow (-1);
                viewPanel.domain.currentInstructorIndex = -1;
                settingsInstructorsTable.setSelectedRow (-1);
                viewPanel.domain.currentTextbookIndex = -1;
                settingsTextbooksTable.setSelectedRow (-1);
            }
            viewPanel.domain.currentCourseIndex = settingsCoursesTable.getSelectedRow ();
            courseTabbedPane.setSelectedIndex (0);
            removeCourseButton.setEnabled (true);

            if (settingsCoursesTable.getSelectedRow () > 0)
            {
                moveCourseUpButton.setEnabled (true);
            }
            else
            {
                moveCourseUpButton.setEnabled (false);
            }
            if (settingsCoursesTable.getSelectedRow () < courseTableModel.getRowCount () - 1)
            {
                moveCourseDownButton.setEnabled (true);
            }
            else
            {
                moveCourseDownButton.setEnabled (false);
            }

            settingsCoursesTable.scrollRectToVisible (settingsCoursesTable.getCellRect (viewPanel.domain.currentCourseIndex, 0, false));
            showCourseDetails ();
        }
        else
        {
            removeTypeButton.setEnabled (false);
            moveTypeUpButton.setEnabled (false);
            moveTypeDownButton.setEnabled (false);
            removeTextbookButton.setEnabled (false);
            moveTextbookUpButton.setEnabled (false);
            moveTextbookDownButton.setEnabled (false);
            removeCourseButton.setEnabled (false);
            moveCourseUpButton.setEnabled (false);
            moveCourseDownButton.setEnabled (false);
            clearCourseDetails ();
        }
    }

    /**
     * The event that occurs when an item is selected in the type list.
     *
     * @param e The event trigger object.
     */
    protected void settingsTypesTableRowSelected(ListSelectionEvent e)
    {
        if (settingsTypesTable.getSelectedRow () != -1)
        {
            if (viewPanel.domain.currentTypeIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentTypeIndex, 2);
            }
            viewPanel.domain.currentTypeIndex = settingsTypesTable.getSelectedRow ();
            removeTypeButton.setEnabled (true);

            if (viewPanel.domain.currentTypeIndex > 0)
            {
                moveTypeUpButton.setEnabled (true);
            }
            else
            {
                moveTypeUpButton.setEnabled (false);
            }
            if (viewPanel.domain.currentTypeIndex < typeTableModel.getRowCount () - 1)
            {
                moveTypeDownButton.setEnabled (true);
            }
            else
            {
                moveTypeDownButton.setEnabled (false);
            }

            settingsTypesTable.scrollRectToVisible (settingsTypesTable.getCellRect (viewPanel.domain.currentTypeIndex, 0, false));
            showTypeDetails ();
        }
        else
        {
            removeTypeButton.setEnabled (false);
            moveTypeUpButton.setEnabled (false);
            moveTypeDownButton.setEnabled (false);
            viewPanel.domain.currentTypeIndex = -1;
            clearTypeDetails ();
        }
    }

    /**
     * The event that occurs when an item is selected in the instructors list.
     *
     * @param e The event trigger object.
     */
    protected void settingsInstructorsTableRowSelected(ListSelectionEvent e)
    {
        if (settingsInstructorsTable.getSelectedRow () != -1)
        {
            if (viewPanel.domain.currentInstructorIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentInstructorIndex, 4);
            }
            viewPanel.domain.currentInstructorIndex = settingsInstructorsTable.getSelectedRow ();
            removeInstructorButton.setEnabled (true);

            if (viewPanel.domain.currentInstructorIndex > 0)
            {
                moveInstructorUpButton.setEnabled (true);
            }
            else
            {
                moveInstructorUpButton.setEnabled (false);
            }
            if (viewPanel.domain.currentInstructorIndex < instructorTableModel.getRowCount () - 1)
            {
                moveInstructorDownButton.setEnabled (true);
            }
            else
            {
                moveInstructorDownButton.setEnabled (false);
            }

            settingsInstructorsTable.scrollRectToVisible (settingsInstructorsTable.getCellRect (viewPanel.domain.currentInstructorIndex, 0, false));
            showInstructorDetails ();
        }
        else
        {
            removeInstructorButton.setEnabled (false);
            moveInstructorUpButton.setEnabled (false);
            moveInstructorDownButton.setEnabled (false);
            viewPanel.domain.currentInstructorIndex = -1;
            clearInstructorDetails ();
        }
    }

    /**
     * The event that occurs when an item is selected in the textbooks list.
     *
     * @param e The event trigger object.
     */
    protected void settingsTextbooksTableRowSelected(ListSelectionEvent e)
    {
        if (settingsTextbooksTable.getSelectedRow () != -1)
        {
            if (viewPanel.domain.currentTextbookIndex != -1)
            {
                checkTermsAndCoursesChanges (viewPanel.domain.currentTextbookIndex, 3);
            }
            viewPanel.domain.currentTextbookIndex = settingsTextbooksTable.getSelectedRow ();
            removeTextbookButton.setEnabled (true);

            if (viewPanel.domain.currentTextbookIndex > 0)
            {
                moveTextbookUpButton.setEnabled (true);
            }
            else
            {
                moveTextbookUpButton.setEnabled (false);
            }
            if (viewPanel.domain.currentTextbookIndex < textbookTableModel.getRowCount () - 1)
            {
                moveTextbookDownButton.setEnabled (true);
            }
            else
            {
                moveTextbookDownButton.setEnabled (false);
            }

            settingsTextbooksTable.scrollRectToVisible (settingsTextbooksTable.getCellRect (viewPanel.domain.currentTextbookIndex, 0, false));
            showTextbookDetails ();
        }
        else
        {
            removeTextbookButton.setEnabled (false);
            moveTextbookUpButton.setEnabled (false);
            moveTextbookDownButton.setEnabled (false);
            viewPanel.domain.currentTextbookIndex = -1;
            showTextbookDetails ();
        }
    }

    /**
     * Checks if any details in preferences text fields are different than the
     * values stored in that object's definitions. If they are, it updates the
     * object's definitions with those values so they can be saved.
     *
     * @param index The selected index
     * @param typeIndex 0 is for terms, 1 is for courses, 2 is for types, 3 is
     * for textbooks, 4 is for instructors
     */
    public synchronized void checkTermsAndCoursesChanges(int index, int typeIndex)
    {
        if (index != -1)
        {
            switch (typeIndex)
            {
                // terms list
                case 0:
                {
                    Term term = viewPanel.domain.utility.terms.get (index);
                    if (!termNameTextField.getText ().equals (term.getTypeName ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTermName (index);
                    }
                    break;
                }
                // courses list
                case 1:
                {
                    Course course = viewPanel.domain.utility.courses.get (index);
                    if (!courseNameTextField.getText ().equals (course.getTypeName ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setCourseName (index);
                    }
                    if (!roomTextField.getText ().equals (course.getRoomLocation ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setRoomLocation (index);
                    }
                    if (!labRoomTextField.getText ().equals (course.getLabRoomLocation ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setLabRoomLocation (index);
                    }
                    if (!courseNumberTextField.getText ().equals (course.getCourseNumber ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setCourseNumber (index);
                    }
                    if (!labNumberTextField.getText ().equals (course.getLabNumber ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setLabNumber (index);
                    }
                    if (!courseWebsiteTextField.getText ().equals (course.getCourseWebsite ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setCourseWebsite (index);
                    }
                    if (!labWebsiteTextField.getText ().equals (course.getLabWebsite ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setLabWebsite (index);
                    }
                    break;
                }
                // types list
                case 2:
                {
                    AssignmentType type = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getType (index);
                    if (!typeNameTextField.getText ().equals (type.getTypeName ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTypeName (index);
                    }
                    if (!weightTextField.getText ().equals (type.getWeight ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTypeWeight (index);
                    }
                    break;
                }
                // textbooks list
                case 3:
                {
                    Textbook textbook = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getTextbook (index);
                    if (!textbookNameTextField.getText ().equals (textbook.getTypeName ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookName (index);
                    }
                    if (!authorTextField.getText ().equals (textbook.getAuthor ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookAuthor (index);
                    }
                    if (!publisherTextField.getText ().equals (textbook.getPublisher ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookPublisher (index);
                    }
                    if (!isbnTextField.getText ().equals (textbook.getISBN ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookISBN (index);
                    }
                    if (!textbookSourceTextField.getText ().equals (textbook.getSource ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookSource (index);
                    }
                    if (!purchasePriceTextField.getText ().equals (textbook.getPrice ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookPrice (index);
                    }
                    if (!conditionTextField.getText ().equals (textbook.getCondition ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookCondition (index);
                    }
                    if (!contactEmailTextField.getText ().equals (textbook.getContactEmail ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setTextbookContactEmail (index);
                    }
                    break;
                }
                case 4:
                {
                    Instructor instructor = viewPanel.domain.utility.courses.get (viewPanel.domain.currentCourseIndex).getInstructor (index);
                    if (!instructorNameTextField.getText ().equals (instructor.getTypeName ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setInstructorName (index);
                    }
                    if (!instructorEmailTextField.getText ().equals (instructor.getInstructorEmail ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setInstructorEmail (index);
                    }
                    if (!officeHoursTextField.getText ().equals (instructor.getOfficeHours ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setOfficeHours (index);
                    }
                    if (!officeLocationTextField.getText ().equals (instructor.getOfficeLocation ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setOfficeLocation (index);
                    }
                    if (!phoneTextField.getText ().equals (instructor.getInstructorPhone ()))
                    {
                        viewPanel.domain.needsCoursesAndTermsSave = true;
                        viewPanel.domain.setInstructorPhone (index);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Displays the details for the selected term.
     */
    protected void showTermDetails()
    {
        if (settingsTermsTable.getSelectedRow () != -1 && settingsTermsTable.getSelectedRow () < termTableModel.getRowCount ())
        {
            viewPanel.domain.termLoading.push (true);

            termDetailsPanel.setVisible (true);

            Term term = viewPanel.domain.utility.terms.get (settingsTermsTable.getSelectedRow ());
            try
            {
                termStartDateChooser.setDate (Domain.DATE_FORMAT.parse (term.getStartDate ()));
                termEndDateChooser.setDate (Domain.DATE_FORMAT.parse (term.getEndDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            termNameTextField.setText (term.getTypeName ());
            termNameTextField.requestFocus ();
            termNameTextField.selectAll ();

            termDetailsPanel.setVisible (true);
            noTermDetailsPanel.setVisible (false);

            viewPanel.domain.termLoading.pop ();
        }
        else
        {
            clearTermDetails ();
        }
    }

    /**
     * Clears details of a deselected or removed term.
     */
    protected void clearTermDetails()
    {
        viewPanel.domain.termLoading.push (true);

        termDetailsPanel.setVisible (false);

        termNameTextField.setText ("");
        termStartDateChooser.setDate (viewPanel.domain.today);
        termEndDateChooser.setDate (viewPanel.domain.today);

        noTermDetailsPanel.setVisible (true);
        termDetailsPanel.setVisible (false);

        viewPanel.domain.termLoading.pop ();
    }

    /**
     * Shows the details of the selected course.
     */
    protected void showCourseDetails()
    {
        if (settingsCoursesTable.getSelectedRow () != -1 && settingsCoursesTable.getSelectedRow () < courseTableModel.getRowCount ())
        {
            viewPanel.domain.courseLoading.push (true);

            viewPanel.domain.typeLoading.push (true);
            viewPanel.domain.textbookLoading.push (true);
            viewPanel.domain.utility.loadSettingsTermCombo ();
            viewPanel.domain.utility.loadSettingsTypeTable ();
            viewPanel.domain.utility.loadSettingsInstructorTable ();
            viewPanel.domain.utility.loadSettingsTextbookTable ();
            clearTypeDetails ();
            viewPanel.domain.currentTypeIndex = -1;
            clearTextbookDetails ();
            viewPanel.domain.currentTextbookIndex = -1;

            courseTabbedPane.setVisible (true);

            Course course = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ());
            creditsSpinner.setValue (Double.parseDouble (course.getCredits ()));
            courseColorPanel.setBackground (course.getColor ());
            roomTextField.setText (course.getRoomLocation ());
            try
            {
                startHrChooser.setValue (Domain.HR_FORMAT.parse (course.getStartTime (0)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                startMinChooser.setValue (Domain.MIN_FORMAT.parse (course.getStartTime (1)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                startMChooser.setValue (Domain.M_FORMAT.parse (course.getStartTime (2)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                endHrChooser.setValue (Domain.HR_FORMAT.parse (course.getEndTime (0)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                endMinChooser.setValue (Domain.MIN_FORMAT.parse (course.getEndTime (1)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                endMChooser.setValue (Domain.M_FORMAT.parse (course.getEndTime (2)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            try
            {
                courseStartDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getStartDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                courseEndDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getEndDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            onlineCheckBox.setSelected (course.isOnline ());
            if (course.isOnline ())
            {
                courseTimeLabel.setEnabled (false);
                startHrChooser.setEnabled (false);
                startMinChooser.setEnabled (false);
                startMChooser.setEnabled (false);
                endHrChooser.setEnabled (false);
                endMinChooser.setEnabled (false);
                endMChooser.setEnabled (false);
                courseColon1.setEnabled (false);
                courseColon2.setEnabled (false);

                roomLabel.setEnabled (false);
                roomTextField.setEnabled (false);

                daysLabel.setEnabled (false);
                sunToggleButton.setEnabled (false);
                monToggleButton.setEnabled (false);
                tueToggleButton.setEnabled (false);
                wedToggleButton.setEnabled (false);
                thuToggleButton.setEnabled (false);
                friToggleButton.setEnabled (false);
                satToggleButton.setEnabled (false);
            }
            else
            {
                courseTimeLabel.setEnabled (true);
                startHrChooser.setEnabled (true);
                startMinChooser.setEnabled (true);
                startMChooser.setEnabled (true);
                endHrChooser.setEnabled (true);
                endMinChooser.setEnabled (true);
                endMChooser.setEnabled (true);
                courseColon1.setEnabled (true);
                courseColon2.setEnabled (true);

                roomLabel.setEnabled (true);
                roomTextField.setEnabled (true);

                daysLabel.setEnabled (true);
                sunToggleButton.setEnabled (true);
                monToggleButton.setEnabled (true);
                tueToggleButton.setEnabled (true);
                wedToggleButton.setEnabled (true);
                thuToggleButton.setEnabled (true);
                friToggleButton.setEnabled (true);
                satToggleButton.setEnabled (true);
            }
            viewPanel.setComboBoxSelection (termsComboBox, course.getTerm ().getUniqueID ());
            courseWebsiteTextField.setText (course.getCourseWebsite ());
            sunToggleButton.setSelected (course.isOnDay (0));
            monToggleButton.setSelected (course.isOnDay (1));
            tueToggleButton.setSelected (course.isOnDay (2));
            wedToggleButton.setSelected (course.isOnDay (3));
            thuToggleButton.setSelected (course.isOnDay (4));
            friToggleButton.setSelected (course.isOnDay (5));
            satToggleButton.setSelected (course.isOnDay (6));

            try
            {
                labStartHrChooser.setValue (Domain.HR_FORMAT.parse (course.getLabStartTime (0)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labStartMinChooser.setValue (Domain.MIN_FORMAT.parse (course.getLabStartTime (1)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labStartMChooser.setValue (Domain.M_FORMAT.parse (course.getLabStartTime (2)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labEndHrChooser.setValue (Domain.HR_FORMAT.parse (course.getLabEndTime (0)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labEndMinChooser.setValue (Domain.MIN_FORMAT.parse (course.getLabEndTime (1)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labEndMChooser.setValue (Domain.M_FORMAT.parse (course.getLabEndTime (2)));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labStartDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getLabStartDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }
            try
            {
                labEndDateChooser.setDate (Domain.DATE_FORMAT.parse (course.getLabEndDate ()));
            }
            catch (ParseException ex)
            {
                Domain.LOGGER.add (ex);
            }

            courseHasLabCheckBox.setSelected (course.hasLab ());
            labOnlineCheckBox.setSelected (course.labIsOnline ());
            updateLabUI (course);

            labWebsiteTextField.setText (course.getLabWebsite ());
            labSunToggleButton.setSelected (course.isLabOnDay (0));
            labMonToggleButton.setSelected (course.isLabOnDay (1));
            labTueToggleButton.setSelected (course.isLabOnDay (2));
            labWedToggleButton.setSelected (course.isLabOnDay (3));
            labThuToggleButton.setSelected (course.isLabOnDay (4));
            labFriToggleButton.setSelected (course.isLabOnDay (5));
            labSatToggleButton.setSelected (course.isLabOnDay (6));

            courseNumberTextField.setText (course.getCourseNumber ());
            labNumberTextField.setText (course.getLabNumber ());
            labRoomTextField.setText (course.getLabRoomLocation ());
            labCreditsSpinner.setValue (Double.parseDouble (course.getLabCredits ()));

            addInstructorButton.setEnabled (true);
            addTypeButton.setEnabled (true);
            addTextbookButton.setEnabled (true);
            courseNameTextField.setText (course.getTypeName ());

            viewPanel.domain.setTotalWeightLabel (settingsCoursesTable.getSelectedRow ());

            courseNameTextField.requestFocus ();
            courseNameTextField.selectAll ();

            courseTabbedPane.setVisible (true);
            noCourseDetailsPanel.setVisible (false);

            viewPanel.domain.typeLoading.pop ();
            viewPanel.domain.textbookLoading.pop ();
            viewPanel.domain.courseLoading.pop ();
        }
        else
        {
            clearCourseDetails ();
        }
    }

    /**
     * Clears details after a course is deselected or removed.
     */
    protected void clearCourseDetails()
    {
        viewPanel.domain.courseLoading.push (true);

        courseTabbedPane.setVisible (false);

        courseNameTextField.setText ("");
        roomTextField.setText ("");
        courseNumberTextField.setText ("");
        labNumberTextField.setText ("");
        labRoomTextField.setText ("");
        courseWebsiteTextField.setText ("");
        labWebsiteTextField.setText ("");
        courseStartDateChooser.setDate (viewPanel.domain.today);
        courseEndDateChooser.setDate (viewPanel.domain.today);
        sunToggleButton.setSelected (false);
        monToggleButton.setSelected (false);
        tueToggleButton.setSelected (false);
        wedToggleButton.setSelected (false);
        thuToggleButton.setSelected (false);
        friToggleButton.setSelected (false);
        satToggleButton.setSelected (false);
        labSunToggleButton.setSelected (false);
        labMonToggleButton.setSelected (false);
        labTueToggleButton.setSelected (false);
        labWedToggleButton.setSelected (false);
        labThuToggleButton.setSelected (false);
        labFriToggleButton.setSelected (false);
        labSatToggleButton.setSelected (false);
        termsComboBox.removeAllItems ();
        courseTabbedPane.setSelectedIndex (0);
        typeTableModel.removeAllRows ();
        instructorTableModel.removeAllRows ();
        textbookTableModel.removeAllRows ();

        clearInstructorDetails ();
        clearTypeDetails ();
        clearTextbookDetails ();

        noCourseDetailsPanel.setVisible (true);
        courseTabbedPane.setVisible (false);

        if (viewPanel.domain.utility.terms.isEmpty ())
        {
            noTermsYetLabel.setText ("<html>" + viewPanel.domain.language.getString ("noTermsCourseTab") + "</html>");
        }
        else
        {
            noTermsYetLabel.setText ("<html>" + viewPanel.domain.language.getString ("noCoursesAdded") + "</html>");
        }

        viewPanel.domain.courseLoading.pop ();
    }

    /**
     * Show the details of the selected type.
     */
    protected void showTypeDetails()
    {
        if (viewPanel.domain.currentTypeIndex != -1 && viewPanel.domain.currentTypeIndex < typeTableModel.getRowCount ())
        {
            viewPanel.domain.typeLoading.push (true);

            viewPanel.enableAllComponents (typeDetailsPanel);

            AssignmentType type = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getType (viewPanel.domain.currentTypeIndex);
            weightTextField.setText (type.getWeight ());
            typeNameTextField.setText (type.getTypeName ());
            typeNameTextField.requestFocus ();
            typeNameTextField.selectAll ();

            viewPanel.domain.typeLoading.pop ();
        }
        else
        {
            clearTypeDetails ();
        }
    }

    /**
     * Show the details of the selected instructor.
     */
    protected void showInstructorDetails()
    {
        if (viewPanel.domain.currentInstructorIndex != -1 && viewPanel.domain.currentInstructorIndex < instructorTableModel.getRowCount ())
        {
            viewPanel.domain.instructorLoading.push (true);

            viewPanel.enableAllComponents (instructorDetailsPanel);

            Instructor instructor = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getInstructor (viewPanel.domain.currentInstructorIndex);
            instructorNameTextField.setText (instructor.getTypeName ());
            instructorEmailTextField.setText (instructor.getInstructorEmail ());
            if (instructor.getInstructorEmail ().equals (""))
            {
                emailInstructorButton.setEnabled (false);
            }
            else
            {
                emailInstructorButton.setEnabled (true);
            }
            phoneTextField.setText (instructor.getInstructorPhone ());
            officeLocationTextField.setText (instructor.getOfficeLocation ());
            officeHoursTextField.setText (instructor.getOfficeHours ());

            if (instructor.getLectureLab ().equals (viewPanel.domain.language.getString ("lecture")))
            {
                lectureRadioButton.setSelected (true);
            }
            else if (instructor.getLectureLab ().equals (viewPanel.domain.language.getString ("lab")))
            {
                labRadioButton.setSelected (true);
            }
            else if (instructor.getLectureLab ().equals (viewPanel.domain.language.getString ("both")))
            {
                bothRadioButton.setSelected (true);
            }

            instructorNameTextField.requestFocus ();
            instructorNameTextField.selectAll ();

            viewPanel.domain.instructorLoading.pop ();
        }
        else
        {
            clearInstructorDetails ();
        }
    }

    /**
     * Clears details after a instructor is deselected or removed.
     */
    protected void clearInstructorDetails()
    {
        viewPanel.domain.instructorLoading.push (true);

        viewPanel.disableAllComponents (instructorDetailsPanel);

        instructorNameTextField.setText ("");
        instructorEmailTextField.setText ("");
        phoneTextField.setText ("");
        officeHoursTextField.setText ("");
        officeLocationTextField.setText ("");
        lectureRadioButton.setSelected (true);

        viewPanel.domain.instructorLoading.pop ();
    }

    /**
     * Clears details after a type is deselected or removed.
     */
    protected void clearTypeDetails()
    {
        viewPanel.domain.typeLoading.push (true);

        viewPanel.disableAllComponents (typeDetailsPanel);

        typeNameTextField.setText ("");
        weightTextField.setText ("");

        viewPanel.domain.typeLoading.pop ();
    }

    /**
     * Show the details of the selected textbook.
     */
    protected void showTextbookDetails()
    {
        if (viewPanel.domain.currentTextbookIndex != -1 && viewPanel.domain.currentTextbookIndex < textbookTableModel.getRowCount ())
        {
            viewPanel.domain.textbookLoading.push (true);

            viewPanel.enableAllComponents (textbookDetailsPanel);

            Textbook textbook = viewPanel.domain.utility.courses.get (settingsCoursesTable.getSelectedRow ()).getTextbook (viewPanel.domain.currentTextbookIndex);
            authorTextField.setText (textbook.getAuthor ());
            publisherTextField.setText (textbook.getPublisher ());
            isbnTextField.setText (textbook.getISBN ());
            if (textbook.getISBN ().equals (""))
            {
                searchGoogleButton.setEnabled (false);
            }
            else
            {
                searchGoogleButton.setEnabled (true);
            }
            textbookSourceTextField.setText (textbook.getSource ());
            orderedCheckBox.setSelected (textbook.isOrdered ());
            receivedCheckBox.setSelected (textbook.isReceived ());
            contactEmailTextField.setText (textbook.getContactEmail ());
            if (textbook.getContactEmail ().equals (""))
            {
                contactSendEmailButton.setEnabled (false);
            }
            else
            {
                contactSendEmailButton.setEnabled (true);
            }
            if (textbook.getSource ().replaceAll (" ", "").equals (""))
            {
                visitTextbookSourceButton.setEnabled (false);
            }
            else
            {
                visitTextbookSourceButton.setEnabled (true);
            }
            purchasePriceTextField.setText (textbook.getPrice ());
            conditionTextField.setText (textbook.getCondition ());
            textbookNameTextField.setText (textbook.getTypeName ());
            textbookNameTextField.requestFocus ();
            textbookNameTextField.selectAll ();

            viewPanel.domain.textbookLoading.pop ();
        }
        else
        {
            clearTextbookDetails ();
        }
    }

    /**
     * Clears details after a textbook is deselected or removed.
     */
    protected void clearTextbookDetails()
    {
        viewPanel.domain.textbookLoading.push (true);

        viewPanel.disableAllComponents (textbookDetailsPanel);

        textbookNameTextField.setText ("");
        authorTextField.setText ("");
        publisherTextField.setText ("");
        isbnTextField.setText ("");
        textbookSourceTextField.setText ("");
        contactEmailTextField.setText ("");
        orderedCheckBox.setSelected (false);
        receivedCheckBox.setSelected (false);
        purchasePriceTextField.setText ("");
        conditionTextField.setText ("");

        viewPanel.domain.textbookLoading.pop ();
    }

    /**
     * Update the courses table and the displayed course information in the
     * settings window.
     */
    private void updateSettingsCourseInformation()
    {
        viewPanel.domain.utility.loadSettingsCourseTable ();
        viewPanel.domain.courseLoading.push (true);
        int count = viewPanel.domain.utility.loadSettingsTermCombo ();
        settingsCoursesTable.setSelectedRow (viewPanel.domain.currentCourseIndex);
        showCourseDetails ();
        viewPanel.domain.currentTypeIndex = -1;
        viewPanel.domain.currentTextbookIndex = -1;
        if (count > 0)
        {
            addCourseButton.setEnabled (true);
        }
        else
        {
            addCourseButton.setEnabled (false);
        }
        viewPanel.domain.courseLoading.pop ();

        if (settingsCoursesTable.getSelectedRow () == -1
            && settingsCoursesTable.getRowCount () > 0)
        {
            settingsCoursesTable.setSelectedRow (0);
            settingsCoursesTableRowSelected (null);
        }
    }

    /**
     * Fire a remove course action.
     */
    protected void removeTermButtonActionPerformed()
    {
        removeTermButtonActionPerformed (null);
    }

    /**
     * Fire a remove course action.
     */
    protected void removeCourseButtonActionPerformed()
    {
        removeCourseButtonActionPerformed (null);
    }

    /**
     * Apply the language for this dialog.
     *
     * @param language The language resource to be applied.
     */
    public void applyLanguage(ResourceBundle language)
    {
        settingsCoursesTable.getColumnModel ().getColumn (0).setHeaderValue (language.getString ("course"));
        settingsCoursesTable.getColumnModel ().getColumn (1).setHeaderValue (language.getString ("term"));
        settingsCoursesTable.getTableHeader ().resizeAndRepaint ();
        settingsTypesTable.getColumnModel ().getColumn (0).setHeaderValue (language.getString ("name"));
        settingsTypesTable.getColumnModel ().getColumn (1).setHeaderValue (language.getString ("weight"));
        settingsTypesTable.getTableHeader ().resizeAndRepaint ();
        settingsInstructorsTable.getColumnModel ().getColumn (0).setHeaderValue (language.getString ("name"));
        settingsInstructorsTable.getColumnModel ().getColumn (1).setHeaderValue (language.getString ("lecture") + "/" + language.getString ("lab"));
        settingsInstructorsTable.getTableHeader ().resizeAndRepaint ();

        ((JTextFieldDateEditor) termStartDateChooser.getDateEditor ()).setToolTipText (language.getString ("termStartDateChooserToolTip"));
        ((JTextFieldDateEditor) termEndDateChooser.getDateEditor ()).setToolTipText (language.getString ("termEndDateChooserToolTip"));
        ((JTextFieldDateEditor) courseStartDateChooser.getDateEditor ()).setToolTipText (language.getString ("courseStartDateChooserToolTip"));
        ((JTextFieldDateEditor) courseEndDateChooser.getDateEditor ()).setToolTipText (language.getString ("courseEndDateChooserToolTip"));
        ((JTextFieldDateEditor) labStartDateChooser.getDateEditor ()).setToolTipText (language.getString ("labStartDateChooserToolTip"));
        ((JTextFieldDateEditor) labEndDateChooser.getDateEditor ()).setToolTipText (language.getString ("labEndDateChooserToolTip"));
        termStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        termStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });
        termEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        termEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });
        courseStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        courseStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });
        courseEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        courseEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });
        labStartDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        labStartDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });
        labEndDateChooser.getJCalendar ().getMonthChooser ().setMonthNames (new String[]
                {
                    language.getString ("january"), language.getString ("february"), language.getString ("march"), language.getString ("april"), language.getString ("may"), language.getString ("june"), language.getString ("july"), language.getString ("august"), language.getString ("september"), language.getString ("october"), language.getString ("november"), language.getString ("december")
                });
        labEndDateChooser.getJCalendar ().getDayChooser ().setDayNames (new String[]
                {
                    language.getString ("sun"), language.getString ("mon"), language.getString ("tue"), language.getString ("wed"), language.getString ("thu"), language.getString ("fri"), language.getString ("sat")
                });

        bothRadioButton.setText (language.getString ("both"));
        bothRadioButton.setToolTipText (language.getString ("bothInstructorToolTip"));
    }
}
