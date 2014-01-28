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

import adl.go.gui.ColoredComponent.GradientStyle;
import adl.go.gui.colorpicker.ColorPicker;
import adl.go.types.Category;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;

/**
 * The Settings dialog.
 *
 * @author Alex Laird
 */
public class SettingsDialog extends EscapeDialog
{
    /**
     * The model for the categories displayed in the table in the Settings dialog.
     */
    public ExtendedSettingsTableModel categoryTableModel;
    /**
     * The model for the themes displayed in the combo box in the Themes
     * settings panel.
     */
    public DefaultComboBoxModel themesModel = new DefaultComboBoxModel ();
    /**
     * The model for the themes displayed in the combo box in the Themes
     * settings panel.
     */
    public DefaultComboBoxModel themesPrefModel = new DefaultComboBoxModel ();

    /**
     * Construct the Settings dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public SettingsDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);

        categoryTableModel = new ExtendedSettingsTableModel (new String[]
                {
                    ""
                }, viewPanel);

        initComponents ();

        // add the listener for when the value of the categories table is changed
        settingsCategoriesTable.getSelectionModel ().addListSelectionListener (new ListSelectionListener ()
        {
            @Override
            public void valueChanged(ListSelectionEvent evt)
            {
                settingsCategoriesTableRowSelected (evt);
            }
        });

        settingsTabbedPane.remove (themePanel);
    }

    /**
     * Initialize the Settings dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("settings"));
        settingsUpperJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowTopBackground1);
        preferencesPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        themePanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        userDetailsPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        ((TitledBorder) categoriesPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ((TitledBorder) dueDateColoringPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ((TitledBorder) priorityColoringPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        settingsCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        settingsCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        currentThemePrefLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        currentThemePrefComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        checkForComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        colorByComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        languageComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        currentThemePrefComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        settingsTabbedPane.setFont (viewPanel.domain.utility.currentTheme.fontPlain11);
        settingsCategoriesTable.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addCategoryButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        addCategoryButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveCategoryUpButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveCategoryUpButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        moveCategoryDownButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        moveCategoryDownButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        removeCategoryButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        removeCategoryButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        eventColorLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        emptyCategory.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        emptyCategory.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        categoryNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ddColor6Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor5Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor4Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor3Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor2Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor1Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        ddColor0Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        defaultsDueDateButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        defaultsDueDateButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        pColor5Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        pColor4Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        pColor3Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        pColor2Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        pColor1Label.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        defaultsPriorityButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        defaultsPriorityButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        colorByComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        colorByLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        showRmAssnWarningCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        checkForComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        checkForLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        autoUpdateCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        languageLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        languageComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        currentThemeComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        currentThemeComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        currentThemeLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        jLabel30.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel33.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel34.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel31.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel32.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel35.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel27.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel29.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel28.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel23.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        jLabel21.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        jLabel52.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel38.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel36.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel51.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel50.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel49.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel24.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        jLabel46.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel45.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel48.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel42.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel41.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel44.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel43.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel39.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel22.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        jLabel47.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel37.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        jLabel25.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        jLabel40.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        newThemeButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        newThemeButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        themeNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        saveThemeButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        saveThemeButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        applyThemeButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        applyThemeButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ((TitledBorder) advisorDetailsPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontBold12);
        ((TitledBorder) studentDetailsPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontBold12);
        advisorOfficeLocationTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        advisorOfficeLocationLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        emailLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        emailTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        advisorOfficeHoursTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        advisorOfficeHoursLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        advisorPhoneTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        advisorPhoneLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        advisorEmailLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        advisorEmailTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        emailAdvisorButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        emailAdvisorButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        advisorNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        studentNameTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        schoolLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        studentSchoolTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        idNumberTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        idNumberLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        boxNumberLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        boxNumberTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        majorsTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        majorsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        concentrationsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
        concentrationsTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        minorsTextField.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        minorsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold11);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settingsJPanel = new javax.swing.JPanel();
        settingsUpperJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        settingsLabel = new javax.swing.JLabel();
        settingsCloseButton = new javax.swing.JButton();
        currentThemePrefLabel = new javax.swing.JLabel();
        currentThemePrefComboBox = new javax.swing.JComboBox();
        settingsTabbedPane = new javax.swing.JTabbedPane();
        preferencesPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        categoriesPanel = new javax.swing.JPanel();
        categoriesScrollPane = new javax.swing.JScrollPane();
        settingsCategoriesTable = new ExtendedJTable();
        addCategoryButton = new javax.swing.JButton();
        moveCategoryUpButton = new javax.swing.JButton();
        moveCategoryDownButton = new javax.swing.JButton();
        removeCategoryButton = new javax.swing.JButton();
        eventColorLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        eventColorPanel = new javax.swing.JPanel();
        emptyCategory = new javax.swing.JButton();
        categoryNameTextField = new javax.swing.JTextField();
        preferencesScrollPane = new javax.swing.JScrollPane();
        preferencesScrollablePanel = new javax.swing.JPanel();
        dueDateColoringPanel = new javax.swing.JPanel();
        ddColor6Label = new javax.swing.JLabel();
        ddColor6Panel = new javax.swing.JPanel();
        ddColor5Panel = new javax.swing.JPanel();
        ddColor5Label = new javax.swing.JLabel();
        ddColor4Panel = new javax.swing.JPanel();
        ddColor4Label = new javax.swing.JLabel();
        ddColor3Panel = new javax.swing.JPanel();
        ddColor3Label = new javax.swing.JLabel();
        ddColor2Label = new javax.swing.JLabel();
        ddColor2Panel = new javax.swing.JPanel();
        ddColor1Label = new javax.swing.JLabel();
        ddColor1Panel = new javax.swing.JPanel();
        ddColor0Label = new javax.swing.JLabel();
        ddColor0Panel = new javax.swing.JPanel();
        defaultsDueDateButton = new javax.swing.JButton();
        priorityColoringPanel = new javax.swing.JPanel();
        pColor5Label = new javax.swing.JLabel();
        pColor5Panel = new javax.swing.JPanel();
        pColor4Panel = new javax.swing.JPanel();
        pColor4Label = new javax.swing.JLabel();
        pColor3Panel = new javax.swing.JPanel();
        pColor3Label = new javax.swing.JLabel();
        pColor2Panel = new javax.swing.JPanel();
        pColor2Label = new javax.swing.JLabel();
        pColor1Label = new javax.swing.JLabel();
        pColor1Panel = new javax.swing.JPanel();
        defaultsPriorityButton = new javax.swing.JButton();
        colorByComboBox = new javax.swing.JComboBox();
        colorByLabel = new javax.swing.JLabel();
        showRmAssnWarningCheckBox = new javax.swing.JCheckBox();
        checkForComboBox = new javax.swing.JComboBox();
        checkForLabel = new javax.swing.JLabel();
        autoUpdateCheckBox = new javax.swing.JCheckBox();
        languageLabel = new javax.swing.JLabel();
        languageComboBox = new javax.swing.JComboBox();
        userDetailsPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        advisorDetailsPanel = new javax.swing.JPanel();
        advisorOfficeLocationTextField = new javax.swing.JTextField();
        advisorOfficeLocationLabel = new javax.swing.JLabel();
        advisorOfficeHoursTextField = new javax.swing.JTextField();
        advisorOfficeHoursLabel = new javax.swing.JLabel();
        advisorPhoneTextField = new javax.swing.JTextField();
        advisorPhoneLabel = new javax.swing.JLabel();
        advisorEmailLabel = new javax.swing.JLabel();
        advisorEmailTextField = new javax.swing.JTextField();
        emailAdvisorButton = new javax.swing.JButton();
        advisorNameTextField = new javax.swing.JTextField();
        studentDetailsPanel = new javax.swing.JPanel();
        studentNameTextField = new javax.swing.JTextField();
        schoolLabel = new javax.swing.JLabel();
        studentSchoolTextField = new javax.swing.JTextField();
        idNumberTextField = new javax.swing.JTextField();
        idNumberLabel = new javax.swing.JLabel();
        boxNumberLabel = new javax.swing.JLabel();
        boxNumberTextField = new javax.swing.JTextField();
        majorsTextField = new javax.swing.JTextField();
        majorsLabel = new javax.swing.JLabel();
        concentrationsLabel = new javax.swing.JLabel();
        concentrationsTextField = new javax.swing.JTextField();
        minorsTextField = new javax.swing.JTextField();
        minorsLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        themePanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        currentThemeComboBox = new javax.swing.JComboBox();
        currentThemeLabel = new javax.swing.JLabel();
        themeScrollPane = new javax.swing.JScrollPane();
        themeScrollablePanel = new javax.swing.JPanel();
        themeLeftPanel = new javax.swing.JPanel();
        themeSingleWindowColorPanel2 = new javax.swing.JPanel();
        themeDoubleWindowTopColorPanel2 = new javax.swing.JPanel();
        themeDoubleWindowBottomColorPanel2 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        themeMiddleLoadingColorPanel = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        themeDoubleWindowBottomColorPanel = new javax.swing.JPanel();
        themeDoubleWindowTopColorPanel = new javax.swing.JPanel();
        themeSingleWindowColorPanel = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        themeMiddleColorPanel = new javax.swing.JPanel();
        themeTopColorPanel = new javax.swing.JPanel();
        themeRightColorPanel = new javax.swing.JPanel();
        themeLeftColorPanel = new javax.swing.JPanel();
        themeBottomColorPanel = new javax.swing.JPanel();
        themeBottomColorPanel2 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        themeMiddleColorPanel2 = new javax.swing.JPanel();
        themeRightColorPanel2 = new javax.swing.JPanel();
        themeTopColorPanel2 = new javax.swing.JPanel();
        themeLeftColorPanel2 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        themeMiddleLoadingColorPanel2 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        themeMiddleCalendarColorPanel = new javax.swing.JPanel();
        themeMiddleCalendarColorPanel2 = new javax.swing.JPanel();
        themeMiddlePanel = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        themeDayInMonthColorPanel = new javax.swing.JPanel();
        themeDayInMonthColorPanel2 = new javax.swing.JPanel();
        themeTodayColorPanel = new javax.swing.JPanel();
        themeDayOutsideMonthColorPanel = new javax.swing.JPanel();
        themeDayOutsideMonthColorPanel2 = new javax.swing.JPanel();
        themeMiniDayInMonthColorPanel = new javax.swing.JPanel();
        themeMiniDayOutsideMonthColorPanel = new javax.swing.JPanel();
        themeMiniDaySelectedColorPanel = new javax.swing.JPanel();
        themeMiniWeekNamesBackgroundColorPanel = new javax.swing.JPanel();
        themeMiniWeekNamesForegroundColorPanel = new javax.swing.JPanel();
        themeMiniSundayColorPanel = new javax.swing.JPanel();
        themeMiniBusyDayInMonthColorPanel = new javax.swing.JPanel();
        themeMiniCompletedDayInMonthColorPanel = new javax.swing.JPanel();
        themeMiniBusyDayOutsideMonthColorPanel = new javax.swing.JPanel();
        themeMiniCompletedDayOutsideMonthColorPanel = new javax.swing.JPanel();
        themeRightPanel = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        themeEvenRowColorPanel = new javax.swing.JPanel();
        themeOddRowColorPanel = new javax.swing.JPanel();
        themeSelectedRowColorPanel = new javax.swing.JPanel();
        newThemeButton = new javax.swing.JButton();
        themeNameTextField = new javax.swing.JTextField();
        saveThemeButton = new javax.swing.JButton();
        applyThemeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        settingsUpperJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        settingsUpperJPanel.setPreferredSize(new java.awt.Dimension(970, 49));

        settingsLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/settings_mini.png"))); // NOI18N
        settingsLabel.setText(viewPanel.domain.language.getString ("settings"));

        settingsCloseButton.setText(viewPanel.domain.language.getString ("close"));
        settingsCloseButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
        settingsCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsCloseButtonActionPerformed(evt);
            }
        });

        currentThemePrefLabel.setText(viewPanel.domain.language.getString ("currentTheme") + ":");

        currentThemePrefComboBox.setModel(themesPrefModel);
        currentThemePrefComboBox.setToolTipText(viewPanel.domain.language.getString ("currentThemeToolTip"));
        currentThemePrefComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentThemePrefComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsUpperJPanelLayout = new javax.swing.GroupLayout(settingsUpperJPanel);
        settingsUpperJPanel.setLayout(settingsUpperJPanelLayout);
        settingsUpperJPanelLayout.setHorizontalGroup(
            settingsUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsUpperJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsLabel)
                .addGap(18, 18, 18)
                .addComponent(currentThemePrefLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentThemePrefComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 643, Short.MAX_VALUE)
                .addComponent(settingsCloseButton)
                .addContainerGap())
        );
        settingsUpperJPanelLayout.setVerticalGroup(
            settingsUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsUpperJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsUpperJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(settingsLabel)
                    .addComponent(settingsCloseButton)
                    .addComponent(currentThemePrefLabel)
                    .addComponent(currentThemePrefComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.setPreferredSize(new java.awt.Dimension(970, 546));

        categoriesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("eventCategories")));
        categoriesPanel.setOpaque(false);

        settingsCategoriesTable.setModel(categoryTableModel);
        settingsCategoriesTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        categoriesScrollPane.setViewportView(settingsCategoriesTable);

        addCategoryButton.setText(viewPanel.domain.language.getString ("add"));
        addCategoryButton.setToolTipText(viewPanel.domain.language.getString ("addToolTip"));
        addCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryButtonActionPerformed(evt);
            }
        });

        moveCategoryUpButton.setText(viewPanel.domain.language.getString ("moveUp"));
        moveCategoryUpButton.setToolTipText(viewPanel.domain.language.getString ("moveUpToolTip"));
        moveCategoryUpButton.setEnabled(false);
        moveCategoryUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveCategoryUpButtonActionPerformed(evt);
            }
        });

        moveCategoryDownButton.setText(viewPanel.domain.language.getString ("moveDown"));
        moveCategoryDownButton.setToolTipText(viewPanel.domain.language.getString ("moveDownToolTip"));
        moveCategoryDownButton.setEnabled(false);
        moveCategoryDownButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveCategoryDownButtonActionPerformed(evt);
            }
        });

        removeCategoryButton.setText(viewPanel.domain.language.getString ("remove"));
        removeCategoryButton.setToolTipText(viewPanel.domain.language.getString ("removeToolTip"));
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCategoryButtonActionPerformed(evt);
            }
        });

        eventColorLabel.setText(viewPanel.domain.language.getString ("color") + ":");

        jLabel2.setText(viewPanel.domain.language.getString ("name") + ":");

        eventColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        eventColorPanel.setToolTipText(viewPanel.domain.language.getString ("clickToChangeColorToolTip"));
        eventColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        eventColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                eventColorPanelMouseReleased(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eventColorPanelcolorPanelMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eventColorPanelcolorPanelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout eventColorPanelLayout = new javax.swing.GroupLayout(eventColorPanel);
        eventColorPanel.setLayout(eventColorPanelLayout);
        eventColorPanelLayout.setHorizontalGroup(
            eventColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        eventColorPanelLayout.setVerticalGroup(
            eventColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        emptyCategory.setText(viewPanel.domain.language.getString ("emptyCategory"));
        emptyCategory.setToolTipText(viewPanel.domain.language.getString ("emptyCategoryToolTip"));
        emptyCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emptyCategoryActionPerformed(evt);
            }
        });

        categoryNameTextField.setToolTipText("The name of the event category");
        categoryNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryNameTextFieldActionPerformed(evt);
            }
        });
        categoryNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                categoryNameTextFieldtextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                categoryNameTextFieldFocusLost(evt);
            }
        });
        categoryNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                categoryNameTextFieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout categoriesPanelLayout = new javax.swing.GroupLayout(categoriesPanel);
        categoriesPanel.setLayout(categoriesPanelLayout);
        categoriesPanelLayout.setHorizontalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoriesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addComponent(moveCategoryUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                        .addComponent(moveCategoryDownButton))
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addComponent(addCategoryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                        .addComponent(removeCategoryButton))
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(categoryNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                    .addGroup(categoriesPanelLayout.createSequentialGroup()
                        .addComponent(eventColorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(eventColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                        .addComponent(emptyCategory)))
                .addContainerGap())
        );
        categoriesPanelLayout.setVerticalGroup(
            categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriesPanelLayout.createSequentialGroup()
                .addComponent(categoriesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addCategoryButton)
                    .addComponent(removeCategoryButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(moveCategoryUpButton)
                    .addComponent(moveCategoryDownButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categoryNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(categoriesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emptyCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(eventColorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(eventColorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        preferencesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        preferencesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        preferencesScrollPane.setOpaque(false);

        preferencesScrollablePanel.setOpaque(false);

        dueDateColoringPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("dueDateColoring")));
        dueDateColoringPanel.setOpaque(false);

        ddColor6Label.setText(viewPanel.domain.language.getString ("dueInThreeDays") + ":");

        ddColor6Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor6Panel.setToolTipText("Click to change color");
        ddColor6Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor6Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor6PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor6PanelLayout = new javax.swing.GroupLayout(ddColor6Panel);
        ddColor6Panel.setLayout(ddColor6PanelLayout);
        ddColor6PanelLayout.setHorizontalGroup(
            ddColor6PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor6PanelLayout.setVerticalGroup(
            ddColor6PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor5Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor5Panel.setToolTipText("Click to change color");
        ddColor5Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor5Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor5PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor5PanelLayout = new javax.swing.GroupLayout(ddColor5Panel);
        ddColor5Panel.setLayout(ddColor5PanelLayout);
        ddColor5PanelLayout.setHorizontalGroup(
            ddColor5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor5PanelLayout.setVerticalGroup(
            ddColor5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor5Label.setText(viewPanel.domain.language.getString ("dueInTwoDays") + ":");

        ddColor4Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor4Panel.setToolTipText("Click to change color");
        ddColor4Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor4Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor4PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor4PanelLayout = new javax.swing.GroupLayout(ddColor4Panel);
        ddColor4Panel.setLayout(ddColor4PanelLayout);
        ddColor4PanelLayout.setHorizontalGroup(
            ddColor4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor4PanelLayout.setVerticalGroup(
            ddColor4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor4Label.setText(viewPanel.domain.language.getString ("dueTomorrow") + ":");

        ddColor3Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor3Panel.setToolTipText("Click to change color");
        ddColor3Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor3Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor3PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor3PanelLayout = new javax.swing.GroupLayout(ddColor3Panel);
        ddColor3Panel.setLayout(ddColor3PanelLayout);
        ddColor3PanelLayout.setHorizontalGroup(
            ddColor3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor3PanelLayout.setVerticalGroup(
            ddColor3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor3Label.setText(viewPanel.domain.language.getString ("dueToday") + ":");

        ddColor2Label.setText(viewPanel.domain.language.getString ("overdue") + ":");

        ddColor2Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor2Panel.setToolTipText("Click to change color");
        ddColor2Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor2Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor2PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor2PanelLayout = new javax.swing.GroupLayout(ddColor2Panel);
        ddColor2Panel.setLayout(ddColor2PanelLayout);
        ddColor2PanelLayout.setHorizontalGroup(
            ddColor2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor2PanelLayout.setVerticalGroup(
            ddColor2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor1Label.setText(viewPanel.domain.language.getString ("notYetDue") + ":");

        ddColor1Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor1Panel.setToolTipText("Click to change color");
        ddColor1Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor1Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor1PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor1PanelLayout = new javax.swing.GroupLayout(ddColor1Panel);
        ddColor1Panel.setLayout(ddColor1PanelLayout);
        ddColor1PanelLayout.setHorizontalGroup(
            ddColor1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor1PanelLayout.setVerticalGroup(
            ddColor1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ddColor0Label.setText(viewPanel.domain.language.getString ("completed") + ":");

        ddColor0Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ddColor0Panel.setToolTipText("Click to change color");
        ddColor0Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        ddColor0Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ddColor0PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout ddColor0PanelLayout = new javax.swing.GroupLayout(ddColor0Panel);
        ddColor0Panel.setLayout(ddColor0PanelLayout);
        ddColor0PanelLayout.setHorizontalGroup(
            ddColor0PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        ddColor0PanelLayout.setVerticalGroup(
            ddColor0PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        defaultsDueDateButton.setText(viewPanel.domain.language.getString ("defaults"));
        defaultsDueDateButton.setToolTipText("Restore default due date coloring");
        defaultsDueDateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultsDueDateButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dueDateColoringPanelLayout = new javax.swing.GroupLayout(dueDateColoringPanel);
        dueDateColoringPanel.setLayout(dueDateColoringPanelLayout);
        dueDateColoringPanelLayout.setHorizontalGroup(
            dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor4Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor4Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor5Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor5Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor6Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor6Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor3Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor3Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                        .addComponent(ddColor0Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddColor0Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(defaultsDueDateButton)))
        );
        dueDateColoringPanelLayout.setVerticalGroup(
            dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dueDateColoringPanelLayout.createSequentialGroup()
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor6Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor6Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor5Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor5Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor4Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor4Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor3Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor3Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor2Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor2Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor1Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor1Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dueDateColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ddColor0Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(ddColor0Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defaultsDueDateButton))
        );

        priorityColoringPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("priorityColoring")));
        priorityColoringPanel.setOpaque(false);

        pColor5Label.setText("5/5:");

        pColor5Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pColor5Panel.setToolTipText("Click to change color");
        pColor5Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        pColor5Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pColor5PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pColor5PanelLayout = new javax.swing.GroupLayout(pColor5Panel);
        pColor5Panel.setLayout(pColor5PanelLayout);
        pColor5PanelLayout.setHorizontalGroup(
            pColor5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        pColor5PanelLayout.setVerticalGroup(
            pColor5PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pColor4Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pColor4Panel.setToolTipText("Click to change color");
        pColor4Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        pColor4Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pColor4PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pColor4PanelLayout = new javax.swing.GroupLayout(pColor4Panel);
        pColor4Panel.setLayout(pColor4PanelLayout);
        pColor4PanelLayout.setHorizontalGroup(
            pColor4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        pColor4PanelLayout.setVerticalGroup(
            pColor4PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pColor4Label.setText("4/5:");

        pColor3Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pColor3Panel.setToolTipText("Click to change color");
        pColor3Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        pColor3Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pColor3PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pColor3PanelLayout = new javax.swing.GroupLayout(pColor3Panel);
        pColor3Panel.setLayout(pColor3PanelLayout);
        pColor3PanelLayout.setHorizontalGroup(
            pColor3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        pColor3PanelLayout.setVerticalGroup(
            pColor3PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pColor3Label.setText("3/5:");

        pColor2Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pColor2Panel.setToolTipText("Click to change color");
        pColor2Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        pColor2Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pColor2PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pColor2PanelLayout = new javax.swing.GroupLayout(pColor2Panel);
        pColor2Panel.setLayout(pColor2PanelLayout);
        pColor2PanelLayout.setHorizontalGroup(
            pColor2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        pColor2PanelLayout.setVerticalGroup(
            pColor2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        pColor2Label.setText("2/5:");

        pColor1Label.setText("1/5:");

        pColor1Panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pColor1Panel.setToolTipText("Click to change color");
        pColor1Panel.setPreferredSize(new java.awt.Dimension(25, 25));
        pColor1Panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pColor1PanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout pColor1PanelLayout = new javax.swing.GroupLayout(pColor1Panel);
        pColor1Panel.setLayout(pColor1PanelLayout);
        pColor1PanelLayout.setHorizontalGroup(
            pColor1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        pColor1PanelLayout.setVerticalGroup(
            pColor1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        defaultsPriorityButton.setText(viewPanel.domain.language.getString ("defaults"));
        defaultsPriorityButton.setToolTipText("Restore default priority coloring");
        defaultsPriorityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultsPriorityButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout priorityColoringPanelLayout = new javax.swing.GroupLayout(priorityColoringPanel);
        priorityColoringPanel.setLayout(priorityColoringPanelLayout);
        priorityColoringPanelLayout.setHorizontalGroup(
            priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                        .addComponent(pColor3Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pColor3Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                        .addComponent(pColor4Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pColor4Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                        .addComponent(pColor5Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pColor5Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                        .addComponent(pColor2Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pColor2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                        .addComponent(pColor1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pColor1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(60, 60, 60))
            .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(defaultsPriorityButton))
        );
        priorityColoringPanelLayout.setVerticalGroup(
            priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(priorityColoringPanelLayout.createSequentialGroup()
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pColor5Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(pColor5Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pColor4Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(pColor4Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pColor3Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(pColor3Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pColor2Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(pColor2Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(priorityColoringPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pColor1Panel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                    .addComponent(pColor1Label, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(defaultsPriorityButton))
        );

        colorByComboBox.setModel(new DefaultComboBoxModel(new String[] { viewPanel.domain.language.getString ("dueDate"), viewPanel.domain.language.getString ("course"), viewPanel.domain.language.getString ("priority") }));
        colorByComboBox.setToolTipText(viewPanel.domain.language.getString ("colorAssnByToolTip"));
        colorByComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                colorByComboBoxActionPerformed(evt);
            }
        });

        colorByLabel.setText(viewPanel.domain.language.getString ("colorAssnBy") + ":");

        showRmAssnWarningCheckBox.setSelected(true);
        showRmAssnWarningCheckBox.setText(viewPanel.domain.language.getString ("showDeleteWarnings"));
        showRmAssnWarningCheckBox.setOpaque(false);
        showRmAssnWarningCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showRmAssnWarningCheckBoxActionPerformed(evt);
            }
        });

        checkForComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Stable only", "Beta and stable", "Alpha, beta, and stable" }));
        checkForComboBox.setToolTipText(viewPanel.domain.language.getString ("checkForToolTip"));
        checkForComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkForComboBoxActionPerformed(evt);
            }
        });

        checkForLabel.setText(viewPanel.domain.language.getString ("checkFor") + ":");

        autoUpdateCheckBox.setSelected(true);
        autoUpdateCheckBox.setText(viewPanel.domain.language.getString ("automaticallyCheckForUpdates"));
        autoUpdateCheckBox.setOpaque(false);
        autoUpdateCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoUpdateCheckBoxActionPerformed(evt);
            }
        });

        languageLabel.setText(viewPanel.domain.language.getString ("language") + ":");

        languageComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "English", "Español" }));
        languageComboBox.setToolTipText(viewPanel.domain.language.getString ("languageToolTip"));
        languageComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout preferencesScrollablePanelLayout = new javax.swing.GroupLayout(preferencesScrollablePanel);
        preferencesScrollablePanel.setLayout(preferencesScrollablePanelLayout);
        preferencesScrollablePanelLayout.setHorizontalGroup(
            preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(checkForLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkForComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(autoUpdateCheckBox)
                    .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                        .addComponent(colorByLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(colorByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(showRmAssnWarningCheckBox)
                    .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                        .addComponent(languageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(languageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                        .addComponent(priorityColoringPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dueDateColoringPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(382, Short.MAX_VALUE))
        );
        preferencesScrollablePanelLayout.setVerticalGroup(
            preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferencesScrollablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(autoUpdateCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(checkForLabel)
                    .addComponent(checkForComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showRmAssnWarningCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(colorByLabel)
                    .addComponent(colorByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(languageLabel)
                    .addComponent(languageComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(preferencesScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priorityColoringPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dueDateColoringPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        preferencesScrollPane.setViewportView(preferencesScrollablePanel);

        javax.swing.GroupLayout preferencesPanelLayout = new javax.swing.GroupLayout(preferencesPanel);
        preferencesPanel.setLayout(preferencesPanelLayout);
        preferencesPanelLayout.setHorizontalGroup(
            preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, preferencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(preferencesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(categoriesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        preferencesPanelLayout.setVerticalGroup(
            preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(preferencesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(preferencesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoriesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(preferencesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE))
                .addContainerGap())
        );

        settingsTabbedPane.addTab(viewPanel.domain.language.getString ("preferences"), preferencesPanel);

        advisorDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("advisorDetails")));
        advisorDetailsPanel.setOpaque(false);

        advisorOfficeLocationTextField.setToolTipText(viewPanel.domain.language.getString ("advisorOfficeLocationToolTip"));
        advisorOfficeLocationTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advisorOfficeLocationTextFieldActionPerformed(evt);
            }
        });
        advisorOfficeLocationTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advisorOfficeLocationTextFieldFocusLost(evt);
            }
        });

        advisorOfficeLocationLabel.setText(viewPanel.domain.language.getString ("officeLocation") + ":");

        advisorOfficeHoursTextField.setToolTipText(viewPanel.domain.language.getString ("advisorOfficeHoursToolTip"));
        advisorOfficeHoursTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advisorOfficeHoursTextFieldActionPerformed(evt);
            }
        });
        advisorOfficeHoursTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advisorOfficeHoursTextFieldFocusLost(evt);
            }
        });

        advisorOfficeHoursLabel.setText(viewPanel.domain.language.getString ("officeHours") + ":");

        advisorPhoneTextField.setToolTipText(viewPanel.domain.language.getString ("advisorPhoneToolTip"));
        advisorPhoneTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advisorPhoneTextFieldActionPerformed(evt);
            }
        });
        advisorPhoneTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advisorPhoneTextFieldFocusLost(evt);
            }
        });

        advisorPhoneLabel.setText(viewPanel.domain.language.getString ("phone") + ":");

        advisorEmailLabel.setText(viewPanel.domain.language.getString ("email") + ":");

        advisorEmailTextField.setToolTipText(viewPanel.domain.language.getString ("advisorEmailToolTip"));
        advisorEmailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advisorEmailTextFieldActionPerformed(evt);
            }
        });
        advisorEmailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advisorEmailTextFieldFocusLost(evt);
            }
        });

        emailAdvisorButton.setText(viewPanel.domain.language.getString ("sendEmail"));
        emailAdvisorButton.setToolTipText("Send an email to the instructor");
        emailAdvisorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailAdvisorButtonActionPerformed(evt);
            }
        });

        advisorNameTextField.setToolTipText(viewPanel.domain.language.getString ("advisorNameToolTip"));
        advisorNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advisorNameTextFieldActionPerformed(evt);
            }
        });
        advisorNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advisorNameTextFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout advisorDetailsPanelLayout = new javax.swing.GroupLayout(advisorDetailsPanel);
        advisorDetailsPanel.setLayout(advisorDetailsPanelLayout);
        advisorDetailsPanelLayout.setHorizontalGroup(
            advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, advisorDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(advisorNameTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                        .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                                .addComponent(advisorEmailLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(advisorEmailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                            .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                                .addComponent(advisorPhoneLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(advisorPhoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                                .addComponent(advisorOfficeHoursLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(advisorOfficeHoursTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                                .addComponent(advisorOfficeLocationLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(advisorOfficeLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailAdvisorButton)))
                .addContainerGap())
        );
        advisorDetailsPanelLayout.setVerticalGroup(
            advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advisorDetailsPanelLayout.createSequentialGroup()
                .addComponent(advisorNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(advisorEmailLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(advisorEmailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(emailAdvisorButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(advisorPhoneLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(advisorPhoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(advisorOfficeHoursLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(advisorOfficeHoursTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advisorDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(advisorOfficeLocationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(advisorOfficeLocationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        studentDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("studentDetails")));
        studentDetailsPanel.setOpaque(false);

        studentNameTextField.setToolTipText(viewPanel.domain.language.getString ("studentNameToolTip"));
        studentNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentNameTextFieldActionPerformed(evt);
            }
        });
        studentNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                studentNameTextFieldFocusLost(evt);
            }
        });

        schoolLabel.setText(viewPanel.domain.language.getString ("school") + ":");

        studentSchoolTextField.setToolTipText(viewPanel.domain.language.getString ("schoolToolTip"));
        studentSchoolTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSchoolTextFieldActionPerformed(evt);
            }
        });
        studentSchoolTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                studentSchoolTextFieldFocusLost(evt);
            }
        });

        idNumberTextField.setToolTipText(viewPanel.domain.language.getString ("idNumberToolTip"));
        idNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idNumberTextFieldActionPerformed(evt);
            }
        });
        idNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                idNumberTextFieldFocusLost(evt);
            }
        });

        idNumberLabel.setText(viewPanel.domain.language.getString ("idNumber") + ":");

        boxNumberLabel.setText(viewPanel.domain.language.getString ("boxNumber") + ":");

        boxNumberTextField.setToolTipText(viewPanel.domain.language.getString ("boxNumberToolTip"));
        boxNumberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxNumberTextFieldActionPerformed(evt);
            }
        });
        boxNumberTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                boxNumberTextFieldFocusLost(evt);
            }
        });

        majorsTextField.setToolTipText(viewPanel.domain.language.getString ("majorsToolTip"));
        majorsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                majorsTextFieldActionPerformed(evt);
            }
        });
        majorsTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                majorsTextFieldFocusLost(evt);
            }
        });

        majorsLabel.setText(viewPanel.domain.language.getString ("majors") + ":");

        concentrationsLabel.setText(viewPanel.domain.language.getString ("concentrations") + ":");

        concentrationsTextField.setToolTipText(viewPanel.domain.language.getString ("concentrationsToolTip"));
        concentrationsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                concentrationsTextFieldActionPerformed(evt);
            }
        });
        concentrationsTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                concentrationsTextFieldFocusLost(evt);
            }
        });

        minorsTextField.setToolTipText(viewPanel.domain.language.getString ("minorsToolTip"));
        minorsTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                minorsTextFieldActionPerformed(evt);
            }
        });
        minorsTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                minorsTextFieldFocusLost(evt);
            }
        });

        minorsLabel.setText(viewPanel.domain.language.getString ("minors") + ":");

        emailLabel.setText(viewPanel.domain.language.getString ("email") + ":");

        emailTextField.setToolTipText(viewPanel.domain.language.getString ("schoolToolTip"));
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        emailTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                settingsTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailTextFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout studentDetailsPanelLayout = new javax.swing.GroupLayout(studentDetailsPanel);
        studentDetailsPanel.setLayout(studentDetailsPanelLayout);
        studentDetailsPanelLayout.setHorizontalGroup(
            studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(emailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(schoolLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentSchoolTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(idNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(idNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(boxNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(boxNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(concentrationsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(concentrationsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(majorsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(majorsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                    .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                        .addComponent(minorsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minorsTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)))
                .addContainerGap())
        );
        studentDetailsPanelLayout.setVerticalGroup(
            studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentDetailsPanelLayout.createSequentialGroup()
                .addComponent(studentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailLabel)
                    .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(schoolLabel)
                    .addComponent(studentSchoolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idNumberLabel)
                    .addComponent(idNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxNumberLabel)
                    .addComponent(boxNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(majorsLabel)
                    .addComponent(majorsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(concentrationsLabel)
                    .addComponent(concentrationsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(minorsLabel)
                    .addComponent(minorsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout userDetailsPanelLayout = new javax.swing.GroupLayout(userDetailsPanel);
        userDetailsPanel.setLayout(userDetailsPanelLayout);
        userDetailsPanelLayout.setHorizontalGroup(
            userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(advisorDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        userDetailsPanelLayout.setVerticalGroup(
            userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(userDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(advisorDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(97, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab(viewPanel.domain.language.getString ("userDetails"), userDetailsPanel);

        currentThemeComboBox.setModel(themesModel);
        currentThemeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                currentThemeComboBoxItemStateChanged(evt);
            }
        });

        currentThemeLabel.setText("Current theme:");

        themeScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        themeScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        themeScrollPane.setOpaque(false);

        themeScrollablePanel.setOpaque(false);

        themeLeftPanel.setOpaque(false);

        themeSingleWindowColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeSingleWindowColorPanel2.setToolTipText("Click to change color");
        themeSingleWindowColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeSingleWindowColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeSingleWindowColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeSingleWindowColorPanel2Layout = new javax.swing.GroupLayout(themeSingleWindowColorPanel2);
        themeSingleWindowColorPanel2.setLayout(themeSingleWindowColorPanel2Layout);
        themeSingleWindowColorPanel2Layout.setHorizontalGroup(
            themeSingleWindowColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeSingleWindowColorPanel2Layout.setVerticalGroup(
            themeSingleWindowColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDoubleWindowTopColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDoubleWindowTopColorPanel2.setToolTipText("Click to change color");
        themeDoubleWindowTopColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDoubleWindowTopColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDoubleWindowTopColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDoubleWindowTopColorPanel2Layout = new javax.swing.GroupLayout(themeDoubleWindowTopColorPanel2);
        themeDoubleWindowTopColorPanel2.setLayout(themeDoubleWindowTopColorPanel2Layout);
        themeDoubleWindowTopColorPanel2Layout.setHorizontalGroup(
            themeDoubleWindowTopColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDoubleWindowTopColorPanel2Layout.setVerticalGroup(
            themeDoubleWindowTopColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDoubleWindowBottomColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDoubleWindowBottomColorPanel2.setToolTipText("Click to change color");
        themeDoubleWindowBottomColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDoubleWindowBottomColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDoubleWindowBottomColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDoubleWindowBottomColorPanel2Layout = new javax.swing.GroupLayout(themeDoubleWindowBottomColorPanel2);
        themeDoubleWindowBottomColorPanel2.setLayout(themeDoubleWindowBottomColorPanel2Layout);
        themeDoubleWindowBottomColorPanel2Layout.setHorizontalGroup(
            themeDoubleWindowBottomColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDoubleWindowBottomColorPanel2Layout.setVerticalGroup(
            themeDoubleWindowBottomColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel30.setText("Bottom panel:");

        themeMiddleLoadingColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleLoadingColorPanel.setToolTipText("Click to change color");
        themeMiddleLoadingColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleLoadingColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleLoadingColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleLoadingColorPanelLayout = new javax.swing.GroupLayout(themeMiddleLoadingColorPanel);
        themeMiddleLoadingColorPanel.setLayout(themeMiddleLoadingColorPanelLayout);
        themeMiddleLoadingColorPanelLayout.setHorizontalGroup(
            themeMiddleLoadingColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleLoadingColorPanelLayout.setVerticalGroup(
            themeMiddleLoadingColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel33.setText("Single-section window:");

        jLabel34.setText("Double-section top:");

        jLabel31.setText("Middle \"Loading\" panel:");

        jLabel32.setText("Middle panel:");

        themeDoubleWindowBottomColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDoubleWindowBottomColorPanel.setToolTipText("Click to change color");
        themeDoubleWindowBottomColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDoubleWindowBottomColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDoubleWindowBottomColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDoubleWindowBottomColorPanelLayout = new javax.swing.GroupLayout(themeDoubleWindowBottomColorPanel);
        themeDoubleWindowBottomColorPanel.setLayout(themeDoubleWindowBottomColorPanelLayout);
        themeDoubleWindowBottomColorPanelLayout.setHorizontalGroup(
            themeDoubleWindowBottomColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDoubleWindowBottomColorPanelLayout.setVerticalGroup(
            themeDoubleWindowBottomColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDoubleWindowTopColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDoubleWindowTopColorPanel.setToolTipText("Click to change color");
        themeDoubleWindowTopColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDoubleWindowTopColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDoubleWindowTopColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDoubleWindowTopColorPanelLayout = new javax.swing.GroupLayout(themeDoubleWindowTopColorPanel);
        themeDoubleWindowTopColorPanel.setLayout(themeDoubleWindowTopColorPanelLayout);
        themeDoubleWindowTopColorPanelLayout.setHorizontalGroup(
            themeDoubleWindowTopColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDoubleWindowTopColorPanelLayout.setVerticalGroup(
            themeDoubleWindowTopColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeSingleWindowColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeSingleWindowColorPanel.setToolTipText("Click to change color");
        themeSingleWindowColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeSingleWindowColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeSingleWindowColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeSingleWindowColorPanelLayout = new javax.swing.GroupLayout(themeSingleWindowColorPanel);
        themeSingleWindowColorPanel.setLayout(themeSingleWindowColorPanelLayout);
        themeSingleWindowColorPanelLayout.setHorizontalGroup(
            themeSingleWindowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeSingleWindowColorPanelLayout.setVerticalGroup(
            themeSingleWindowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel35.setText("Double-section bottom:");

        themeMiddleColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleColorPanel.setToolTipText("Click to change color");
        themeMiddleColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleColorPanelLayout = new javax.swing.GroupLayout(themeMiddleColorPanel);
        themeMiddleColorPanel.setLayout(themeMiddleColorPanelLayout);
        themeMiddleColorPanelLayout.setHorizontalGroup(
            themeMiddleColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleColorPanelLayout.setVerticalGroup(
            themeMiddleColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeTopColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeTopColorPanel.setToolTipText("Click to change color");
        themeTopColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeTopColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeTopColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeTopColorPanelLayout = new javax.swing.GroupLayout(themeTopColorPanel);
        themeTopColorPanel.setLayout(themeTopColorPanelLayout);
        themeTopColorPanelLayout.setHorizontalGroup(
            themeTopColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeTopColorPanelLayout.setVerticalGroup(
            themeTopColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeRightColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeRightColorPanel.setToolTipText("Click to change color");
        themeRightColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeRightColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeRightColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeRightColorPanelLayout = new javax.swing.GroupLayout(themeRightColorPanel);
        themeRightColorPanel.setLayout(themeRightColorPanelLayout);
        themeRightColorPanelLayout.setHorizontalGroup(
            themeRightColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeRightColorPanelLayout.setVerticalGroup(
            themeRightColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeLeftColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeLeftColorPanel.setToolTipText("Click to change color");
        themeLeftColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeLeftColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeLeftColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeLeftColorPanelLayout = new javax.swing.GroupLayout(themeLeftColorPanel);
        themeLeftColorPanel.setLayout(themeLeftColorPanelLayout);
        themeLeftColorPanelLayout.setHorizontalGroup(
            themeLeftColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeLeftColorPanelLayout.setVerticalGroup(
            themeLeftColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeBottomColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeBottomColorPanel.setToolTipText("Click to change color");
        themeBottomColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeBottomColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeBottomColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeBottomColorPanelLayout = new javax.swing.GroupLayout(themeBottomColorPanel);
        themeBottomColorPanel.setLayout(themeBottomColorPanelLayout);
        themeBottomColorPanelLayout.setHorizontalGroup(
            themeBottomColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeBottomColorPanelLayout.setVerticalGroup(
            themeBottomColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeBottomColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeBottomColorPanel2.setToolTipText("Click to change color");
        themeBottomColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeBottomColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeBottomColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeBottomColorPanel2Layout = new javax.swing.GroupLayout(themeBottomColorPanel2);
        themeBottomColorPanel2.setLayout(themeBottomColorPanel2Layout);
        themeBottomColorPanel2Layout.setHorizontalGroup(
            themeBottomColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeBottomColorPanel2Layout.setVerticalGroup(
            themeBottomColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel27.setText("Left panel:");

        themeMiddleColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleColorPanel2.setToolTipText("Click to change color");
        themeMiddleColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleColorPanel2Layout = new javax.swing.GroupLayout(themeMiddleColorPanel2);
        themeMiddleColorPanel2.setLayout(themeMiddleColorPanel2Layout);
        themeMiddleColorPanel2Layout.setHorizontalGroup(
            themeMiddleColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleColorPanel2Layout.setVerticalGroup(
            themeMiddleColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeRightColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeRightColorPanel2.setToolTipText("Click to change color");
        themeRightColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeRightColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeRightColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeRightColorPanel2Layout = new javax.swing.GroupLayout(themeRightColorPanel2);
        themeRightColorPanel2.setLayout(themeRightColorPanel2Layout);
        themeRightColorPanel2Layout.setHorizontalGroup(
            themeRightColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeRightColorPanel2Layout.setVerticalGroup(
            themeRightColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeTopColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeTopColorPanel2.setToolTipText("Click to change color");
        themeTopColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeTopColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeTopColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeTopColorPanel2Layout = new javax.swing.GroupLayout(themeTopColorPanel2);
        themeTopColorPanel2.setLayout(themeTopColorPanel2Layout);
        themeTopColorPanel2Layout.setHorizontalGroup(
            themeTopColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeTopColorPanel2Layout.setVerticalGroup(
            themeTopColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeLeftColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeLeftColorPanel2.setToolTipText("Click to change color");
        themeLeftColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeLeftColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeLeftColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeLeftColorPanel2Layout = new javax.swing.GroupLayout(themeLeftColorPanel2);
        themeLeftColorPanel2.setLayout(themeLeftColorPanel2Layout);
        themeLeftColorPanel2Layout.setHorizontalGroup(
            themeLeftColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeLeftColorPanel2Layout.setVerticalGroup(
            themeLeftColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel29.setText("Top panel:");

        jLabel28.setText("Right panel:");

        themeMiddleLoadingColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleLoadingColorPanel2.setToolTipText("Click to change color");
        themeMiddleLoadingColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleLoadingColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleLoadingColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleLoadingColorPanel2Layout = new javax.swing.GroupLayout(themeMiddleLoadingColorPanel2);
        themeMiddleLoadingColorPanel2.setLayout(themeMiddleLoadingColorPanel2Layout);
        themeMiddleLoadingColorPanel2Layout.setHorizontalGroup(
            themeMiddleLoadingColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleLoadingColorPanel2Layout.setVerticalGroup(
            themeMiddleLoadingColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel23.setText("Window Coloring");

        jLabel21.setText("Application Coloring");

        jLabel52.setText("Middle calendar panel:");

        themeMiddleCalendarColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleCalendarColorPanel.setToolTipText("Click to change color");
        themeMiddleCalendarColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleCalendarColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleCalendarColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleCalendarColorPanelLayout = new javax.swing.GroupLayout(themeMiddleCalendarColorPanel);
        themeMiddleCalendarColorPanel.setLayout(themeMiddleCalendarColorPanelLayout);
        themeMiddleCalendarColorPanelLayout.setHorizontalGroup(
            themeMiddleCalendarColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleCalendarColorPanelLayout.setVerticalGroup(
            themeMiddleCalendarColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiddleCalendarColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiddleCalendarColorPanel2.setToolTipText("Click to change color");
        themeMiddleCalendarColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiddleCalendarColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiddleCalendarColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiddleCalendarColorPanel2Layout = new javax.swing.GroupLayout(themeMiddleCalendarColorPanel2);
        themeMiddleCalendarColorPanel2.setLayout(themeMiddleCalendarColorPanel2Layout);
        themeMiddleCalendarColorPanel2Layout.setHorizontalGroup(
            themeMiddleCalendarColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiddleCalendarColorPanel2Layout.setVerticalGroup(
            themeMiddleCalendarColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout themeLeftPanelLayout = new javax.swing.GroupLayout(themeLeftPanel);
        themeLeftPanel.setLayout(themeLeftPanelLayout);
        themeLeftPanelLayout.setHorizontalGroup(
            themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeTopColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeTopColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeLeftColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeLeftColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeRightColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeRightColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeBottomColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeBottomColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleLoadingColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleLoadingColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleCalendarColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiddleCalendarColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23)
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeSingleWindowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeSingleWindowColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDoubleWindowTopColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDoubleWindowTopColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeLeftPanelLayout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDoubleWindowBottomColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDoubleWindowBottomColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        themeLeftPanelLayout.setVerticalGroup(
            themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeLeftPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addGap(18, 18, 18)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeTopColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeTopColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeLeftColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeLeftColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeRightColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeRightColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeBottomColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeBottomColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeMiddleLoadingColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeMiddleLoadingColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeMiddleColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeMiddleColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeMiddleCalendarColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeMiddleCalendarColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52))
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themeSingleWindowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(themeSingleWindowColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(themeDoubleWindowTopColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeDoubleWindowTopColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeLeftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themeDoubleWindowBottomColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeDoubleWindowBottomColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addContainerGap(245, Short.MAX_VALUE))
        );

        themeMiddlePanel.setOpaque(false);

        jLabel38.setText("Today:");

        jLabel36.setText("Day in month:");

        jLabel51.setText("outside month:");

        jLabel50.setText("outside month:");

        jLabel49.setText("outside month:");

        jLabel24.setText("Mini Calendar Coloring");

        jLabel46.setText("Completed day in month:");

        jLabel45.setText("Busy day in month:");

        jLabel48.setText("outside month:");

        jLabel42.setText("Week names background:");

        jLabel41.setText("Day selected:");

        jLabel44.setText("Sunday:");

        jLabel43.setText("foreground:");

        jLabel39.setText("Day in month:");

        jLabel22.setText("Calendar Coloring");

        themeDayInMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDayInMonthColorPanel.setToolTipText("Click to change color");
        themeDayInMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDayInMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDayInMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDayInMonthColorPanelLayout = new javax.swing.GroupLayout(themeDayInMonthColorPanel);
        themeDayInMonthColorPanel.setLayout(themeDayInMonthColorPanelLayout);
        themeDayInMonthColorPanelLayout.setHorizontalGroup(
            themeDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDayInMonthColorPanelLayout.setVerticalGroup(
            themeDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDayInMonthColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDayInMonthColorPanel2.setToolTipText("Click to change color");
        themeDayInMonthColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDayInMonthColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDayInMonthColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDayInMonthColorPanel2Layout = new javax.swing.GroupLayout(themeDayInMonthColorPanel2);
        themeDayInMonthColorPanel2.setLayout(themeDayInMonthColorPanel2Layout);
        themeDayInMonthColorPanel2Layout.setHorizontalGroup(
            themeDayInMonthColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDayInMonthColorPanel2Layout.setVerticalGroup(
            themeDayInMonthColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeTodayColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeTodayColorPanel.setToolTipText("Click to change color");
        themeTodayColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeTodayColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeTodayColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeTodayColorPanelLayout = new javax.swing.GroupLayout(themeTodayColorPanel);
        themeTodayColorPanel.setLayout(themeTodayColorPanelLayout);
        themeTodayColorPanelLayout.setHorizontalGroup(
            themeTodayColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeTodayColorPanelLayout.setVerticalGroup(
            themeTodayColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDayOutsideMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDayOutsideMonthColorPanel.setToolTipText("Click to change color");
        themeDayOutsideMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDayOutsideMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDayOutsideMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDayOutsideMonthColorPanelLayout = new javax.swing.GroupLayout(themeDayOutsideMonthColorPanel);
        themeDayOutsideMonthColorPanel.setLayout(themeDayOutsideMonthColorPanelLayout);
        themeDayOutsideMonthColorPanelLayout.setHorizontalGroup(
            themeDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDayOutsideMonthColorPanelLayout.setVerticalGroup(
            themeDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeDayOutsideMonthColorPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeDayOutsideMonthColorPanel2.setToolTipText("Click to change color");
        themeDayOutsideMonthColorPanel2.setPreferredSize(new java.awt.Dimension(25, 25));
        themeDayOutsideMonthColorPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeDayOutsideMonthColorPanel2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeDayOutsideMonthColorPanel2Layout = new javax.swing.GroupLayout(themeDayOutsideMonthColorPanel2);
        themeDayOutsideMonthColorPanel2.setLayout(themeDayOutsideMonthColorPanel2Layout);
        themeDayOutsideMonthColorPanel2Layout.setHorizontalGroup(
            themeDayOutsideMonthColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeDayOutsideMonthColorPanel2Layout.setVerticalGroup(
            themeDayOutsideMonthColorPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniDayInMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniDayInMonthColorPanel.setToolTipText("Click to change color");
        themeMiniDayInMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniDayInMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniDayInMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniDayInMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniDayInMonthColorPanel);
        themeMiniDayInMonthColorPanel.setLayout(themeMiniDayInMonthColorPanelLayout);
        themeMiniDayInMonthColorPanelLayout.setHorizontalGroup(
            themeMiniDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniDayInMonthColorPanelLayout.setVerticalGroup(
            themeMiniDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniDayOutsideMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniDayOutsideMonthColorPanel.setToolTipText("Click to change color");
        themeMiniDayOutsideMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniDayOutsideMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniDayOutsideMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniDayOutsideMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniDayOutsideMonthColorPanel);
        themeMiniDayOutsideMonthColorPanel.setLayout(themeMiniDayOutsideMonthColorPanelLayout);
        themeMiniDayOutsideMonthColorPanelLayout.setHorizontalGroup(
            themeMiniDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniDayOutsideMonthColorPanelLayout.setVerticalGroup(
            themeMiniDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniDaySelectedColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniDaySelectedColorPanel.setToolTipText("Click to change color");
        themeMiniDaySelectedColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniDaySelectedColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniDaySelectedColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniDaySelectedColorPanelLayout = new javax.swing.GroupLayout(themeMiniDaySelectedColorPanel);
        themeMiniDaySelectedColorPanel.setLayout(themeMiniDaySelectedColorPanelLayout);
        themeMiniDaySelectedColorPanelLayout.setHorizontalGroup(
            themeMiniDaySelectedColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniDaySelectedColorPanelLayout.setVerticalGroup(
            themeMiniDaySelectedColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniWeekNamesBackgroundColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniWeekNamesBackgroundColorPanel.setToolTipText("Click to change color");
        themeMiniWeekNamesBackgroundColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniWeekNamesBackgroundColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniWeekNamesBackgroundColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniWeekNamesBackgroundColorPanelLayout = new javax.swing.GroupLayout(themeMiniWeekNamesBackgroundColorPanel);
        themeMiniWeekNamesBackgroundColorPanel.setLayout(themeMiniWeekNamesBackgroundColorPanelLayout);
        themeMiniWeekNamesBackgroundColorPanelLayout.setHorizontalGroup(
            themeMiniWeekNamesBackgroundColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniWeekNamesBackgroundColorPanelLayout.setVerticalGroup(
            themeMiniWeekNamesBackgroundColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniWeekNamesForegroundColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniWeekNamesForegroundColorPanel.setToolTipText("Click to change color");
        themeMiniWeekNamesForegroundColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniWeekNamesForegroundColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniWeekNamesForegroundColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniWeekNamesForegroundColorPanelLayout = new javax.swing.GroupLayout(themeMiniWeekNamesForegroundColorPanel);
        themeMiniWeekNamesForegroundColorPanel.setLayout(themeMiniWeekNamesForegroundColorPanelLayout);
        themeMiniWeekNamesForegroundColorPanelLayout.setHorizontalGroup(
            themeMiniWeekNamesForegroundColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniWeekNamesForegroundColorPanelLayout.setVerticalGroup(
            themeMiniWeekNamesForegroundColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniSundayColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniSundayColorPanel.setToolTipText("Click to change color");
        themeMiniSundayColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniSundayColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniSundayColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniSundayColorPanelLayout = new javax.swing.GroupLayout(themeMiniSundayColorPanel);
        themeMiniSundayColorPanel.setLayout(themeMiniSundayColorPanelLayout);
        themeMiniSundayColorPanelLayout.setHorizontalGroup(
            themeMiniSundayColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniSundayColorPanelLayout.setVerticalGroup(
            themeMiniSundayColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniBusyDayInMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniBusyDayInMonthColorPanel.setToolTipText("Click to change color");
        themeMiniBusyDayInMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniBusyDayInMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniBusyDayInMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniBusyDayInMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniBusyDayInMonthColorPanel);
        themeMiniBusyDayInMonthColorPanel.setLayout(themeMiniBusyDayInMonthColorPanelLayout);
        themeMiniBusyDayInMonthColorPanelLayout.setHorizontalGroup(
            themeMiniBusyDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniBusyDayInMonthColorPanelLayout.setVerticalGroup(
            themeMiniBusyDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniCompletedDayInMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniCompletedDayInMonthColorPanel.setToolTipText("Click to change color");
        themeMiniCompletedDayInMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniCompletedDayInMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniCompletedDayInMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniCompletedDayInMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniCompletedDayInMonthColorPanel);
        themeMiniCompletedDayInMonthColorPanel.setLayout(themeMiniCompletedDayInMonthColorPanelLayout);
        themeMiniCompletedDayInMonthColorPanelLayout.setHorizontalGroup(
            themeMiniCompletedDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniCompletedDayInMonthColorPanelLayout.setVerticalGroup(
            themeMiniCompletedDayInMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniBusyDayOutsideMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniBusyDayOutsideMonthColorPanel.setToolTipText("Click to change color");
        themeMiniBusyDayOutsideMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniBusyDayOutsideMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniBusyDayOutsideMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniBusyDayOutsideMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniBusyDayOutsideMonthColorPanel);
        themeMiniBusyDayOutsideMonthColorPanel.setLayout(themeMiniBusyDayOutsideMonthColorPanelLayout);
        themeMiniBusyDayOutsideMonthColorPanelLayout.setHorizontalGroup(
            themeMiniBusyDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniBusyDayOutsideMonthColorPanelLayout.setVerticalGroup(
            themeMiniBusyDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeMiniCompletedDayOutsideMonthColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeMiniCompletedDayOutsideMonthColorPanel.setToolTipText("Click to change color");
        themeMiniCompletedDayOutsideMonthColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeMiniCompletedDayOutsideMonthColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeMiniCompletedDayOutsideMonthColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeMiniCompletedDayOutsideMonthColorPanelLayout = new javax.swing.GroupLayout(themeMiniCompletedDayOutsideMonthColorPanel);
        themeMiniCompletedDayOutsideMonthColorPanel.setLayout(themeMiniCompletedDayOutsideMonthColorPanelLayout);
        themeMiniCompletedDayOutsideMonthColorPanelLayout.setHorizontalGroup(
            themeMiniCompletedDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeMiniCompletedDayOutsideMonthColorPanelLayout.setVerticalGroup(
            themeMiniCompletedDayOutsideMonthColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout themeMiddlePanelLayout = new javax.swing.GroupLayout(themeMiddlePanel);
        themeMiddlePanel.setLayout(themeMiddlePanelLayout);
        themeMiddlePanelLayout.setHorizontalGroup(
            themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDayInMonthColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeDayOutsideMonthColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeTodayColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel24)
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniDaySelectedColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniSundayColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniWeekNamesBackgroundColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniWeekNamesForegroundColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniCompletedDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel49)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniCompletedDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniBusyDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeMiniBusyDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        themeMiddlePanelLayout.setVerticalGroup(
            themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addGap(18, 18, 18)
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(themeDayInMonthColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(themeDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38)
                            .addComponent(themeTodayColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24))
                    .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(themeDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(themeDayOutsideMonthColorPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(themeMiniDayOutsideMonthColorPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel39)
                    .addComponent(themeMiniDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel41)
                            .addComponent(themeMiniDaySelectedColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42))
                    .addComponent(themeMiniWeekNamesBackgroundColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43)
                    .addComponent(themeMiniWeekNamesForegroundColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel48)
                    .addComponent(themeMiniBusyDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(themeMiddlePanelLayout.createSequentialGroup()
                        .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addComponent(themeMiniSundayColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45))
                    .addComponent(themeMiniBusyDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeMiddlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel46)
                    .addComponent(themeMiniCompletedDayInMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49)
                    .addComponent(themeMiniCompletedDayOutsideMonthColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(285, Short.MAX_VALUE))
        );

        themeRightPanel.setOpaque(false);

        jLabel47.setText("Selected row:");

        jLabel37.setText("Even row:");

        jLabel25.setText("Table and Tree Coloring");

        jLabel40.setText("Odd row:");

        themeEvenRowColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeEvenRowColorPanel.setToolTipText("Click to change color");
        themeEvenRowColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeEvenRowColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeEvenRowColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeEvenRowColorPanelLayout = new javax.swing.GroupLayout(themeEvenRowColorPanel);
        themeEvenRowColorPanel.setLayout(themeEvenRowColorPanelLayout);
        themeEvenRowColorPanelLayout.setHorizontalGroup(
            themeEvenRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeEvenRowColorPanelLayout.setVerticalGroup(
            themeEvenRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeOddRowColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeOddRowColorPanel.setToolTipText("Click to change color");
        themeOddRowColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeOddRowColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeOddRowColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeOddRowColorPanelLayout = new javax.swing.GroupLayout(themeOddRowColorPanel);
        themeOddRowColorPanel.setLayout(themeOddRowColorPanelLayout);
        themeOddRowColorPanelLayout.setHorizontalGroup(
            themeOddRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeOddRowColorPanelLayout.setVerticalGroup(
            themeOddRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        themeSelectedRowColorPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        themeSelectedRowColorPanel.setToolTipText("Click to change color");
        themeSelectedRowColorPanel.setPreferredSize(new java.awt.Dimension(25, 25));
        themeSelectedRowColorPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                colorPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                colorPanelMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                themeSelectedRowColorPanelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout themeSelectedRowColorPanelLayout = new javax.swing.GroupLayout(themeSelectedRowColorPanel);
        themeSelectedRowColorPanel.setLayout(themeSelectedRowColorPanelLayout);
        themeSelectedRowColorPanelLayout.setHorizontalGroup(
            themeSelectedRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );
        themeSelectedRowColorPanelLayout.setVerticalGroup(
            themeSelectedRowColorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout themeRightPanelLayout = new javax.swing.GroupLayout(themeRightPanel);
        themeRightPanel.setLayout(themeRightPanelLayout);
        themeRightPanelLayout.setHorizontalGroup(
            themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addGroup(themeRightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeEvenRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeRightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeOddRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(themeRightPanelLayout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeSelectedRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        themeRightPanelLayout.setVerticalGroup(
            themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeRightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themeEvenRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themeOddRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(themeRightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(themeSelectedRowColorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(436, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout themeScrollablePanelLayout = new javax.swing.GroupLayout(themeScrollablePanel);
        themeScrollablePanel.setLayout(themeScrollablePanelLayout);
        themeScrollablePanelLayout.setHorizontalGroup(
            themeScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themeScrollablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(themeLeftPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(themeMiddlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(themeRightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(357, Short.MAX_VALUE))
        );
        themeScrollablePanelLayout.setVerticalGroup(
            themeScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, themeScrollablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themeScrollablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(themeRightPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(themeMiddlePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(themeLeftPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        themeScrollPane.setViewportView(themeScrollablePanel);

        newThemeButton.setText("New");
        newThemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newThemeButtonActionPerformed(evt);
            }
        });

        themeNameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                themeNameTextFieldActionPerformed(evt);
            }
        });
        themeNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                themeNameTextFieldFocusLost(evt);
            }
        });

        saveThemeButton.setText("Save");
        saveThemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveThemeButtonActionPerformed(evt);
            }
        });

        applyThemeButton.setText("Apply");
        applyThemeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyThemeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout themePanelLayout = new javax.swing.GroupLayout(themePanel);
        themePanel.setLayout(themePanelLayout);
        themePanelLayout.setHorizontalGroup(
            themePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(themeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                    .addGroup(themePanelLayout.createSequentialGroup()
                        .addComponent(currentThemeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentThemeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(themeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newThemeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveThemeButton)
                        .addGap(18, 18, 18)
                        .addComponent(applyThemeButton)))
                .addContainerGap())
        );
        themePanelLayout.setVerticalGroup(
            themePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(themePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(themePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentThemeLabel)
                    .addComponent(currentThemeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newThemeButton)
                    .addComponent(saveThemeButton)
                    .addComponent(applyThemeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(themeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addContainerGap())
        );

        settingsTabbedPane.addTab("Themes", themePanel);

        javax.swing.GroupLayout settingsJPanelLayout = new javax.swing.GroupLayout(settingsJPanel);
        settingsJPanel.setLayout(settingsJPanelLayout);
        settingsJPanelLayout.setHorizontalGroup(
            settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(settingsUpperJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(settingsTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        settingsJPanelLayout.setVerticalGroup(
            settingsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsJPanelLayout.createSequentialGroup()
                .addComponent(settingsUpperJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(settingsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(settingsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(settingsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void settingsCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsCloseButtonActionPerformed
        closeSettingsDialog ();
}//GEN-LAST:event_settingsCloseButtonActionPerformed

    private void currentThemePrefComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentThemePrefComboBoxActionPerformed
        if (!viewPanel.initLoading)
        {
            Theme theme = viewPanel.domain.utility.themes.get (currentThemePrefComboBox.getSelectedIndex ());
            viewPanel.domain.utility.currentTheme = theme;
            viewPanel.domain.utility.currentTheme.apply ();
            currentThemeComboBox.setSelectedItem (viewPanel.domain.utility.currentTheme);

            viewPanel.domain.utility.preferences.currentTheme = viewPanel.domain.utility.currentTheme.name;
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_currentThemePrefComboBoxActionPerformed

    private void addCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryButtonActionPerformed
        int index = 0;
        for (int i = 0; i < categoryTableModel.getRowCount (); ++i)
        {
            if (index == 0)
            {
                if (categoryTableModel.getValueAt (i, 0).toString ().equals (viewPanel.domain.language.getString ("newCategory")))
                {
                    ++index;
                }
            }
            else
            {
                if (categoryTableModel.getValueAt (i, 0).toString ().equals (viewPanel.domain.language.getString ("newCategory") + " " + index))
                {
                    ++index;
                }
            }
        }
        String categoryName = viewPanel.domain.language.getString ("newCategory");
        if (index != 0)
        {
            categoryName += " " + index;
        }
        Category newCategory = new Category (categoryName, Color.BLACK);
        viewPanel.domain.utility.preferences.categories.add (newCategory);
        categoryTableModel.addRow (new Object[]
                {
                    categoryName, -1
                });
        settingsCategoriesTable.refreshTable ();
        settingsCategoriesTable.setSelectedRow (categoryTableModel.getRowCount () - 1);
        settingsCategoriesTable.scrollRectToVisible (settingsCategoriesTable.getCellRect (categoryTableModel.getRowCount () - 1, 0, false));
}//GEN-LAST:event_addCategoryButtonActionPerformed

    private void moveCategoryUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveCategoryUpButtonActionPerformed
        viewPanel.domain.categoryLoading.push (true);
        if (viewPanel.domain.currentCategoryIndex != -1 && viewPanel.domain.currentCategoryIndex > 1)
        {
            Object temp = categoryTableModel.getValueAt (viewPanel.domain.currentCategoryIndex, 0);
            categoryTableModel.setValueAt (categoryTableModel.getValueAt (viewPanel.domain.currentCategoryIndex - 1, 0), viewPanel.domain.currentCategoryIndex, 0);
            categoryTableModel.setValueAt (temp, viewPanel.domain.currentCategoryIndex - 1, 0);

            Category tempType = viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex);
            viewPanel.domain.utility.preferences.categories.set (viewPanel.domain.currentCategoryIndex, viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex - 1));
            viewPanel.domain.utility.preferences.categories.set (viewPanel.domain.currentCategoryIndex - 1, tempType);
        }
        settingsCategoriesTable.setSelectedRow (settingsCategoriesTable.getSelectedRow () - 1);
        viewPanel.domain.categoryLoading.pop ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_moveCategoryUpButtonActionPerformed

    private void moveCategoryDownButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveCategoryDownButtonActionPerformed
        viewPanel.domain.categoryLoading.push (true);
        if (viewPanel.domain.currentCategoryIndex != -1 && viewPanel.domain.currentCategoryIndex < categoryTableModel.getRowCount () - 1)
        {
            Object temp = categoryTableModel.getValueAt (viewPanel.domain.currentCategoryIndex, 0);
            categoryTableModel.setValueAt (categoryTableModel.getValueAt (viewPanel.domain.currentCategoryIndex + 1, 0), viewPanel.domain.currentCategoryIndex, 0);
            categoryTableModel.setValueAt (temp, viewPanel.domain.currentCategoryIndex + 1, 0);

            Category tempType = viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex);
            viewPanel.domain.utility.preferences.categories.set (viewPanel.domain.currentCategoryIndex, viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex + 1));
            viewPanel.domain.utility.preferences.categories.set (viewPanel.domain.currentCategoryIndex + 1, tempType);
        }
        settingsCategoriesTable.setSelectedRow (settingsCategoriesTable.getSelectedRow () + 1);
        viewPanel.domain.categoryLoading.pop ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_moveCategoryDownButtonActionPerformed

    private void removeCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCategoryButtonActionPerformed
        ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("removeCategoryText"));
        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("removeCategory"));
        optionDialog.setVisible (true);
        if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
        {
            viewPanel.domain.categoryLoading.push (true);

            int index = viewPanel.domain.currentCategoryIndex;
            Category category = viewPanel.domain.utility.preferences.categories.get (index);
            categoryTableModel.removeRow (index);
            settingsCategoriesTable.refreshTable ();
            viewPanel.domain.utility.revertEvents (category);
            viewPanel.domain.utility.preferences.categories.remove (category);

            if (index == categoryTableModel.getRowCount ())
            {
                --index;
            }
            viewPanel.domain.currentCategoryIndex = index;
            settingsCategoriesTable.setSelectedRow (index);
            settingsCategoriesTableRowSelected (null);

            viewPanel.domain.categoryLoading.pop ();
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_removeCategoryButtonActionPerformed

    private void eventColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eventColorPanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.categories.get (settingsCategoriesTable.getSelectedRow ()).getColor ();
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.categories.get (settingsCategoriesTable.getSelectedRow ()).setColor (newColor);
            eventColorPanel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_eventColorPanelMouseReleased

    private void eventColorPanelcolorPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eventColorPanelcolorPanelMouseExited
        setCursor (Cursor.getDefaultCursor ());
}//GEN-LAST:event_eventColorPanelcolorPanelMouseExited

    private void eventColorPanelcolorPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eventColorPanelcolorPanelMouseEntered
        setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
}//GEN-LAST:event_eventColorPanelcolorPanelMouseEntered

    private void emptyCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emptyCategoryActionPerformed
        ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("emptyCategoryText"));
        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("emptyCategory"));
        optionDialog.setVisible (true);
        if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
        {
            viewPanel.domain.utility.removeEventsAttachedTo (viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex), viewPanel.miniCalendar.getMonthChooser ().getMonth (), viewPanel.miniCalendar.getYearChooser ().getYear ());
            viewPanel.refreshBusyDays ();
        }
}//GEN-LAST:event_emptyCategoryActionPerformed

    private void categoryNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryNameTextFieldActionPerformed
        viewPanel.domain.setCategoryName (viewPanel.domain.currentCategoryIndex);
        if (evt != null)
        {
            categoryNameTextField.requestFocus ();
            categoryNameTextField.selectAll ();
        }
}//GEN-LAST:event_categoryNameTextFieldActionPerformed

    private void categoryNameTextFieldtextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_categoryNameTextFieldtextFieldFocusGained
        ((JTextField) evt.getComponent ()).selectAll ();
}//GEN-LAST:event_categoryNameTextFieldtextFieldFocusGained

    private void categoryNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_categoryNameTextFieldFocusLost
        categoryNameTextFieldActionPerformed (null);
}//GEN-LAST:event_categoryNameTextFieldFocusLost

    private void categoryNameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categoryNameTextFieldKeyPressed
        if (evt.getKeyCode () == KeyEvent.VK_ESCAPE)
        {
            Category cat = viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex);
            categoryNameTextField.setText (cat.getName ());
        }
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_categoryNameTextFieldKeyPressed

    private void ddColor6PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor6PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[5];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[5] = newColor;
            ddColor6Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor6PanelMouseReleased

    private void ddColor5PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor5PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[4];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[4] = newColor;
            ddColor5Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor5PanelMouseReleased

    private void ddColor4PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor4PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[3];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[3] = newColor;
            ddColor4Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor4PanelMouseReleased

    private void ddColor3PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor3PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[2];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[2] = newColor;
            ddColor3Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor3PanelMouseReleased

    private void ddColor2PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor2PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[1];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[1] = newColor;
            ddColor2Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor2PanelMouseReleased

    private void ddColor1PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ddColor1PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[0];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[0] = newColor;
            ddColor1Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_ddColor1PanelMouseReleased

    private void defaultsDueDateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultsDueDateButtonActionPerformed
        ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("restoreColoringText"));
        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("restoreColoring"));
        optionDialog.setVisible (true);
        if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
        {
            viewPanel.domain.utility.preferences.defaultDueDateColors ();

            ddColor6Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[5]);
            ddColor5Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[4]);
            ddColor4Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[3]);
            ddColor3Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[2]);
            ddColor2Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[1]);
            ddColor1Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[0]);
        }
}//GEN-LAST:event_defaultsDueDateButtonActionPerformed

    private void pColor5PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pColor5PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.priorityColors[4];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.priorityColors[4] = newColor;
            pColor5Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_pColor5PanelMouseReleased

    private void pColor4PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pColor4PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.priorityColors[3];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.priorityColors[3] = newColor;
            pColor4Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_pColor4PanelMouseReleased

    private void pColor3PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pColor3PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.priorityColors[2];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.priorityColors[2] = newColor;
            pColor3Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_pColor3PanelMouseReleased

    private void pColor2PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pColor2PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.priorityColors[1];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.priorityColors[1] = newColor;
            pColor2Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_pColor2PanelMouseReleased

    private void pColor1PanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pColor1PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.priorityColors[0];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.priorityColors[0] = newColor;
            pColor1Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
}//GEN-LAST:event_pColor1PanelMouseReleased

    private void defaultsPriorityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultsPriorityButtonActionPerformed
        ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CHOICES);
        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("restoreColoringText"));
        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("restoreColoring"));
        optionDialog.setVisible (true);
        if (ViewPanel.OPTION_PANE.getValue () != null && Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
        {
            viewPanel.domain.utility.preferences.defaultPriorityColors ();

            pColor5Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[4]);
            pColor4Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[3]);
            pColor3Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[2]);
            pColor2Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[1]);
            pColor1Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[0]);
        }
}//GEN-LAST:event_defaultsPriorityButtonActionPerformed

    private void colorByComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_colorByComboBoxActionPerformed
        viewPanel.domain.utility.preferences.colorByIndex = colorByComboBox.getSelectedIndex ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_colorByComboBoxActionPerformed

    private void showRmAssnWarningCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showRmAssnWarningCheckBoxActionPerformed
        viewPanel.domain.utility.preferences.rmAlert = showRmAssnWarningCheckBox.isSelected ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_showRmAssnWarningCheckBoxActionPerformed

    private void checkForComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkForComboBoxActionPerformed
        viewPanel.domain.utility.preferences.updateCheckIndex = checkForComboBox.getSelectedIndex ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_checkForComboBoxActionPerformed

    private void autoUpdateCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoUpdateCheckBoxActionPerformed
        viewPanel.domain.utility.preferences.autoUpdate = autoUpdateCheckBox.isSelected ();
        checkForLabel.setEnabled (autoUpdateCheckBox.isSelected ());
        checkForComboBox.setEnabled (autoUpdateCheckBox.isSelected ());
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_autoUpdateCheckBoxActionPerformed

    private void languageComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageComboBoxActionPerformed
        if (!viewPanel.initLoading)
        {
            viewPanel.domain.utility.preferences.language = languageComboBox.getSelectedItem ().toString ();
            viewPanel.domain.needsPreferencesSave = true;
            viewPanel.loadLanguageResource (viewPanel.domain.utility.preferences.language);
            viewPanel.applyLanguage ();
        }
}//GEN-LAST:event_languageComboBoxActionPerformed

    private void currentThemeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_currentThemeComboBoxItemStateChanged
        if (!viewPanel.initLoading)
        {
            if (currentThemeComboBox.getSelectedIndex () != -1)
            {
                Theme selectedTheme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
                themeNameTextField.setText (selectedTheme.name);

                themeBottomColorPanel.setBackground (selectedTheme.colorBottomBackground1Panel);
                themeBottomColorPanel2.setBackground (selectedTheme.colorBottomBackground2Panel);
                themeDayInMonthColorPanel.setBackground (selectedTheme.colorDayInMonthBackground1);
                themeDayInMonthColorPanel2.setBackground (selectedTheme.colorDayInMonthBackground2);
                themeDayOutsideMonthColorPanel.setBackground (selectedTheme.colorDayNotInMonthBackground1);
                themeDayOutsideMonthColorPanel2.setBackground (selectedTheme.colorDayNotInMonthBackground2);
                themeDoubleWindowBottomColorPanel.setBackground (selectedTheme.colorDoubleWindowBottomBackground1);
                themeDoubleWindowBottomColorPanel2.setBackground (selectedTheme.colorDoubleWindowBottomBackground2);
                themeDoubleWindowTopColorPanel.setBackground (selectedTheme.colorDoubleWindowTopBackground1);
                themeDoubleWindowTopColorPanel2.setBackground (selectedTheme.colorDoubleWindowTopBackground2);
                themeEvenRowColorPanel.setBackground (selectedTheme.colorEven);
                themeLeftColorPanel.setBackground (selectedTheme.colorLeftBackground1Panel);
                themeLeftColorPanel2.setBackground (selectedTheme.colorLeftBackground2Panel);
                themeMiddleColorPanel.setBackground (selectedTheme.colorMiddleBackground1Panel);
                themeMiddleColorPanel2.setBackground (selectedTheme.colorMiddleBackground2Panel);
                themeMiddleLoadingColorPanel.setBackground (selectedTheme.colorLoadingMiddleBackground1Panel);
                themeMiddleLoadingColorPanel2.setBackground (selectedTheme.colorLoadingMiddleBackground2Panel);
                themeMiniBusyDayInMonthColorPanel.setBackground (selectedTheme.colorBusyDayInMonth);
                themeMiniBusyDayOutsideMonthColorPanel.setBackground (selectedTheme.colorBusyDayOutsideMonth);
                themeMiniCompletedDayInMonthColorPanel.setBackground (selectedTheme.colorDoneDayInMonth);
                themeMiniCompletedDayOutsideMonthColorPanel.setBackground (selectedTheme.colorDoneDayOutsideMonth);
                themeMiniDayInMonthColorPanel.setBackground (selectedTheme.colorMiniCalDayNotSelectedBackground);
                themeMiniDayOutsideMonthColorPanel.setBackground (selectedTheme.colorMiniCalDayNotSelectedOutsideMonthBackground);
                themeMiniDaySelectedColorPanel.setBackground (selectedTheme.colorMiniCalDaySelectedBackground);
                themeMiniSundayColorPanel.setBackground (selectedTheme.colorMiniCalSundayForeground);
                themeMiniWeekNamesBackgroundColorPanel.setBackground (selectedTheme.colorMiniCalWeekBackground);
                themeMiniWeekNamesForegroundColorPanel.setBackground (selectedTheme.colorMiniCalWeekForeground);
                themeOddRowColorPanel.setBackground (selectedTheme.colorOdd);
                themeRightColorPanel.setBackground (selectedTheme.colorRightBackground1Panel);
                themeRightColorPanel2.setBackground (selectedTheme.colorRightBackground2Panel);
                themeSelectedRowColorPanel.setBackground (selectedTheme.colorSelected);
                themeSingleWindowColorPanel.setBackground (selectedTheme.colorSingleWindowBackground1);
                themeSingleWindowColorPanel2.setBackground (selectedTheme.colorSingleWindowBackground2);
                themeTodayColorPanel.setBackground (selectedTheme.colorTodayText);
                themeTopColorPanel.setBackground (selectedTheme.colorTopBackground1Panel);
                themeTopColorPanel2.setBackground (selectedTheme.colorTopBackground2Panel);
                themeMiddleCalendarColorPanel.setBackground (selectedTheme.colorMiddleCalendarBackgroundPanel1);
                themeMiddleCalendarColorPanel2.setBackground (selectedTheme.colorMiddleCalendarBackgroundPanel2);
            }
        }
}//GEN-LAST:event_currentThemeComboBoxItemStateChanged

    private void themeSingleWindowColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeSingleWindowColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeSingleWindowColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeSingleWindowColorPanel2.setBackground (newColor);
            theme.colorSingleWindowBackground2 = newColor;
        }
}//GEN-LAST:event_themeSingleWindowColorPanel2MouseReleased

    private void themeDoubleWindowTopColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDoubleWindowTopColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDoubleWindowTopColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDoubleWindowTopColorPanel2.setBackground (newColor);
            theme.colorDoubleWindowTopBackground2 = newColor;
        }
}//GEN-LAST:event_themeDoubleWindowTopColorPanel2MouseReleased

    private void themeDoubleWindowBottomColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDoubleWindowBottomColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDoubleWindowBottomColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDoubleWindowBottomColorPanel2.setBackground (newColor);
            theme.colorDoubleWindowBottomBackground2 = newColor;
        }
}//GEN-LAST:event_themeDoubleWindowBottomColorPanel2MouseReleased

    private void themeMiddleLoadingColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleLoadingColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiddleLoadingColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleLoadingColorPanel.setBackground (newColor);
            theme.colorLoadingMiddleBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeMiddleLoadingColorPanelMouseReleased

    private void themeDoubleWindowBottomColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDoubleWindowBottomColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDoubleWindowBottomColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDoubleWindowBottomColorPanel.setBackground (newColor);
            theme.colorDoubleWindowBottomBackground1 = newColor;
        }
}//GEN-LAST:event_themeDoubleWindowBottomColorPanelMouseReleased

    private void themeDoubleWindowTopColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDoubleWindowTopColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDoubleWindowTopColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDoubleWindowTopColorPanel.setBackground (newColor);
            theme.colorDoubleWindowTopBackground1 = newColor;
        }
}//GEN-LAST:event_themeDoubleWindowTopColorPanelMouseReleased

    private void themeSingleWindowColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeSingleWindowColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeSingleWindowColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeSingleWindowColorPanel.setBackground (newColor);
            theme.colorSingleWindowBackground1 = newColor;
        }
}//GEN-LAST:event_themeSingleWindowColorPanelMouseReleased

    private void themeMiddleColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiddleColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleColorPanel.setBackground (newColor);
            theme.colorMiddleBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeMiddleColorPanelMouseReleased

    private void themeTopColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeTopColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeTopColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeTopColorPanel.setBackground (newColor);
            theme.colorTopBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeTopColorPanelMouseReleased

    private void themeRightColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeRightColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeRightColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeRightColorPanel.setBackground (newColor);
            theme.colorRightBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeRightColorPanelMouseReleased

    private void themeLeftColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeLeftColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeLeftColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeLeftColorPanel.setBackground (newColor);
            theme.colorLeftBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeLeftColorPanelMouseReleased

    private void themeBottomColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeBottomColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeBottomColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeBottomColorPanel.setBackground (newColor);
            theme.colorBottomBackground1Panel = newColor;
        }
}//GEN-LAST:event_themeBottomColorPanelMouseReleased

    private void themeBottomColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeBottomColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeBottomColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeBottomColorPanel2.setBackground (newColor);
            theme.colorBottomBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeBottomColorPanel2MouseReleased

    private void themeMiddleColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiddleColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleColorPanel2.setBackground (newColor);
            theme.colorMiddleBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeMiddleColorPanel2MouseReleased

    private void themeRightColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeRightColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeRightColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeRightColorPanel2.setBackground (newColor);
            theme.colorRightBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeRightColorPanel2MouseReleased

    private void themeTopColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeTopColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeTopColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeTopColorPanel2.setBackground (newColor);
            theme.colorTopBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeTopColorPanel2MouseReleased

    private void themeLeftColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeLeftColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeLeftColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeLeftColorPanel2.setBackground (newColor);
            theme.colorLeftBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeLeftColorPanel2MouseReleased

    private void themeMiddleLoadingColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleLoadingColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiddleLoadingColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleLoadingColorPanel2.setBackground (newColor);
            theme.colorLoadingMiddleBackground2Panel = newColor;
        }
}//GEN-LAST:event_themeMiddleLoadingColorPanel2MouseReleased

    private void themeMiddleCalendarColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleCalendarColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemePrefComboBox.getSelectedIndex ());
        Color color = themeMiddleCalendarColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleCalendarColorPanel.setBackground (newColor);
            theme.colorMiddleCalendarBackgroundPanel1 = newColor;
        }
}//GEN-LAST:event_themeMiddleCalendarColorPanelMouseReleased

    private void themeMiddleCalendarColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiddleCalendarColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemePrefComboBox.getSelectedIndex ());
        Color color = themeMiddleCalendarColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiddleCalendarColorPanel2.setBackground (newColor);
            theme.colorMiddleCalendarBackgroundPanel2 = newColor;
        }
}//GEN-LAST:event_themeMiddleCalendarColorPanel2MouseReleased

    private void themeDayInMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDayInMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDayInMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDayInMonthColorPanel.setBackground (newColor);
            theme.colorDayInMonthBackground1 = newColor;
        }
}//GEN-LAST:event_themeDayInMonthColorPanelMouseReleased

    private void themeDayInMonthColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDayInMonthColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDayInMonthColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDayInMonthColorPanel2.setBackground (newColor);
            theme.colorDayInMonthBackground2 = newColor;
        }
}//GEN-LAST:event_themeDayInMonthColorPanel2MouseReleased

    private void themeTodayColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeTodayColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeTodayColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeTodayColorPanel.setBackground (newColor);
            theme.colorTodayText = newColor;
        }
}//GEN-LAST:event_themeTodayColorPanelMouseReleased

    private void themeDayOutsideMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDayOutsideMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDayOutsideMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDayOutsideMonthColorPanel.setBackground (newColor);
            theme.colorDayNotInMonthBackground1 = newColor;
        }
}//GEN-LAST:event_themeDayOutsideMonthColorPanelMouseReleased

    private void themeDayOutsideMonthColorPanel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeDayOutsideMonthColorPanel2MouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeDayOutsideMonthColorPanel2.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeDayOutsideMonthColorPanel2.setBackground (newColor);
            theme.colorDayNotInMonthBackground2 = newColor;
        }
}//GEN-LAST:event_themeDayOutsideMonthColorPanel2MouseReleased

    private void themeMiniDayInMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniDayInMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniDayInMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniDayInMonthColorPanel.setBackground (newColor);
            theme.colorMiniCalDayNotSelectedBackground = newColor;
        }
}//GEN-LAST:event_themeMiniDayInMonthColorPanelMouseReleased

    private void themeMiniDayOutsideMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniDayOutsideMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniDayOutsideMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniDayOutsideMonthColorPanel.setBackground (newColor);
            theme.colorMiniCalDayNotSelectedOutsideMonthBackground = newColor;
        }
}//GEN-LAST:event_themeMiniDayOutsideMonthColorPanelMouseReleased

    private void themeMiniDaySelectedColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniDaySelectedColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniDaySelectedColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniDaySelectedColorPanel.setBackground (newColor);
            theme.colorMiniCalDaySelectedBackground = newColor;
        }
}//GEN-LAST:event_themeMiniDaySelectedColorPanelMouseReleased

    private void themeMiniWeekNamesBackgroundColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniWeekNamesBackgroundColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniWeekNamesBackgroundColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniWeekNamesBackgroundColorPanel.setBackground (newColor);
            theme.colorMiniCalWeekBackground = newColor;
        }
}//GEN-LAST:event_themeMiniWeekNamesBackgroundColorPanelMouseReleased

    private void themeMiniWeekNamesForegroundColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniWeekNamesForegroundColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniWeekNamesForegroundColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniWeekNamesForegroundColorPanel.setBackground (newColor);
            theme.colorMiniCalWeekForeground = newColor;
        }
}//GEN-LAST:event_themeMiniWeekNamesForegroundColorPanelMouseReleased

    private void themeMiniSundayColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniSundayColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniSundayColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniSundayColorPanel.setBackground (newColor);
            theme.colorMiniCalSundayForeground = newColor;
        }
}//GEN-LAST:event_themeMiniSundayColorPanelMouseReleased

    private void themeMiniBusyDayInMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniBusyDayInMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniBusyDayInMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniBusyDayInMonthColorPanel.setBackground (newColor);
            theme.colorBusyDayInMonth = newColor;
        }
}//GEN-LAST:event_themeMiniBusyDayInMonthColorPanelMouseReleased

    private void themeMiniCompletedDayInMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniCompletedDayInMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniCompletedDayInMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniCompletedDayInMonthColorPanel.setBackground (newColor);
            theme.colorDoneDayInMonth = newColor;
        }
}//GEN-LAST:event_themeMiniCompletedDayInMonthColorPanelMouseReleased

    private void themeMiniBusyDayOutsideMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniBusyDayOutsideMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniBusyDayOutsideMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniBusyDayOutsideMonthColorPanel.setBackground (newColor);
            theme.colorBusyDayOutsideMonth = newColor;
        }
}//GEN-LAST:event_themeMiniBusyDayOutsideMonthColorPanelMouseReleased

    private void themeMiniCompletedDayOutsideMonthColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeMiniCompletedDayOutsideMonthColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeMiniCompletedDayOutsideMonthColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeMiniCompletedDayOutsideMonthColorPanel.setBackground (newColor);
            theme.colorDoneDayOutsideMonth = newColor;
        }
}//GEN-LAST:event_themeMiniCompletedDayOutsideMonthColorPanelMouseReleased

    private void themeEvenRowColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeEvenRowColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeEvenRowColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeEvenRowColorPanel.setBackground (newColor);
            theme.colorEven = newColor;
        }
}//GEN-LAST:event_themeEvenRowColorPanelMouseReleased

    private void themeOddRowColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeOddRowColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeOddRowColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeOddRowColorPanel.setBackground (newColor);
            theme.colorOdd = newColor;
        }
}//GEN-LAST:event_themeOddRowColorPanelMouseReleased

    private void themeSelectedRowColorPanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_themeSelectedRowColorPanelMouseReleased
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
        Color color = themeSelectedRowColorPanel.getBackground ();
        Color newColor = ColorPicker.showDialog (this, "Select Color", color, false, viewPanel);
        if (newColor != null)
        {
            themeSelectedRowColorPanel.setBackground (newColor);
            theme.colorSelected = newColor;
        }
}//GEN-LAST:event_themeSelectedRowColorPanelMouseReleased

    private void newThemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newThemeButtonActionPerformed
        if (!viewPanel.initLoading)
        {
            int index = 0;
            for (int i = 0; i < viewPanel.domain.utility.themes.size (); ++i)
            {
                if (index == 0)
                {
                    if (viewPanel.domain.utility.themes.get (i).name.equals ("New Theme"))
                    {
                        ++index;
                    }
                }
                else
                {
                    if (viewPanel.domain.utility.themes.get (i).name.equals ("New Theme " + index))
                    {
                        ++index;
                    }
                }
            }
            String themeName = "New Theme";
            if (index != 0)
            {
                themeName += " " + index;
            }
            Theme theme = new Theme (themeName, viewPanel);
            viewPanel.domain.utility.currentTheme = theme;
            viewPanel.domain.utility.themes.add (theme);
            themesModel.addElement (theme);
            themesPrefModel.addElement (theme);
            currentThemeComboBox.invalidate ();
            currentThemePrefComboBox.invalidate ();
            currentThemeComboBox.setSelectedIndex (viewPanel.domain.utility.themes.size () - 1);
        }
}//GEN-LAST:event_newThemeButtonActionPerformed

    private void themeNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_themeNameTextFieldActionPerformed
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());

        boolean found = false;
        for (int i = 0; i < viewPanel.domain.utility.themes.size (); ++i)
        {
            if (viewPanel.domain.utility.themes.get (i).name.equals (themeNameTextField.getText ()))
            {
                themeNameTextField.setText (theme.name);
                found = true;
            }
        }

        if (!found)
        {
            theme.name = themeNameTextField.getText ();
        }

        currentThemeComboBox.invalidate ();
        currentThemePrefComboBox.invalidate ();
}//GEN-LAST:event_themeNameTextFieldActionPerformed

    private void themeNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_themeNameTextFieldFocusLost
        themeNameTextFieldActionPerformed (null);
}//GEN-LAST:event_themeNameTextFieldFocusLost

    private void saveThemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveThemeButtonActionPerformed
        Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());

        try
        {
            viewPanel.fileChooser.setDialogType (JFileChooser.SAVE_DIALOG);
            viewPanel.fileChooser.setApproveButtonText ("Save");
            viewPanel.fileChooser.setApproveButtonToolTipText ("Save Theme");
            viewPanel.fileChooser.setDialogTitle ("Save Theme");
            viewPanel.fileChooser.setSelectedFile (new File (theme.name + ".theme"));
            int response = viewPanel.fileChooser.showSaveDialog (this);
            while (response == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = viewPanel.fileChooser.getSelectedFile ();
                // ensure an extension is on the file
                if (selectedFile.getName ().indexOf (".") == -1)
                {
                    selectedFile = new File (viewPanel.fileChooser.getSelectedFile ().toString () + ".theme");
                }
                if (selectedFile.isFile ())
                {
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.YES_NO_CANCEL_CHOICES);
                    ViewPanel.OPTION_PANE.setMessage ("The specified filename already exists in the current folder.\nWould you like to overwrite the old file?");
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.QUESTION_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, "Overwrite File");
                    optionDialog.setVisible (true);
                    if (Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.YES_OPTION)
                    {
                        BufferedWriter out = new BufferedWriter (new FileWriter (selectedFile));
                        out.write (theme.out ());
                        out.flush ();
                        out.close ();
                        break;
                    }
                    else if (Integer.parseInt (ViewPanel.OPTION_PANE.getValue ().toString ()) == JOptionPane.NO_OPTION)
                    {
                        viewPanel.fileChooser.setSelectedFile (selectedFile);
                        response = viewPanel.fileChooser.showSaveDialog (this);
                        continue;
                    }
                    else
                    {
                        break;
                    }
                }
                else
                {
                    BufferedWriter out = new BufferedWriter (new FileWriter (selectedFile));
                    out.write (theme.out ());
                    out.flush ();
                    out.close ();
                    break;
                }
            }
        }
        catch (IOException ex)
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage ("Error writing to the file.");
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.ERROR_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, "Unknown Error");
            optionDialog.setVisible (true);
        }
}//GEN-LAST:event_saveThemeButtonActionPerformed

    private void applyThemeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyThemeButtonActionPerformed
        if (!viewPanel.initLoading)
        {
            Theme theme = viewPanel.domain.utility.themes.get (currentThemeComboBox.getSelectedIndex ());
            viewPanel.domain.utility.currentTheme = theme;
            viewPanel.domain.utility.currentTheme.apply ();
            currentThemePrefComboBox.setSelectedItem (viewPanel.domain.utility.currentTheme);
        }
}//GEN-LAST:event_applyThemeButtonActionPerformed

    private void colorPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanelMouseEntered
        setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
    }//GEN-LAST:event_colorPanelMouseEntered

    private void colorPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorPanelMouseExited
        setCursor (Cursor.getDefaultCursor ());
    }//GEN-LAST:event_colorPanelMouseExited

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeSettingsDialog ();
    }//GEN-LAST:event_formWindowClosing

    private void advisorNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advisorNameTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setAdvisorName (advisorNameTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                advisorNameTextField.requestFocus ();
                advisorNameTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_advisorNameTextFieldActionPerformed

    private void advisorEmailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advisorEmailTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            Matcher matcher = Domain.EMAIL_PATTERN.matcher (advisorEmailTextField.getText ());
            if (matcher.matches ())
            {
                viewPanel.domain.utility.userDetails.setAdvisorEmail (advisorEmailTextField.getText ());
                viewPanel.domain.needsUserDetailsSave = true;
            }
            else
            {
                if (advisorEmailTextField.getText ().replaceAll (" ", "").equals (""))
                {
                    viewPanel.domain.utility.userDetails.setAdvisorEmail (advisorEmailTextField.getText ());
                    viewPanel.domain.needsUserDetailsSave = true;
                }
                else
                {
                    advisorEmailTextField.requestFocus ();
                    advisorEmailTextField.selectAll ();
                }
            }
        }
        if (evt != null)
        {
            advisorEmailTextField.requestFocus ();
            advisorEmailTextField.selectAll ();
        }
        if (advisorEmailTextField.getText ().equals (""))
        {
            emailAdvisorButton.setEnabled (false);
        }
        else
        {
            emailAdvisorButton.setEnabled (true);
        }
    }//GEN-LAST:event_advisorEmailTextFieldActionPerformed

    private void advisorPhoneTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advisorPhoneTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setAdvisorPhone (advisorPhoneTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                advisorPhoneTextField.requestFocus ();
                advisorPhoneTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_advisorPhoneTextFieldActionPerformed

    private void advisorOfficeHoursTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advisorOfficeHoursTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setAdvisorOfficeHours (advisorOfficeHoursTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                advisorOfficeHoursTextField.requestFocus ();
                advisorOfficeHoursTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_advisorOfficeHoursTextFieldActionPerformed

    private void advisorOfficeLocationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advisorOfficeLocationTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setAdvisorOfficeLocation (advisorOfficeLocationTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                advisorOfficeLocationTextField.requestFocus ();
                advisorOfficeLocationTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_advisorOfficeLocationTextFieldActionPerformed

    private void studentNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentNameTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setStudentName (studentNameTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                studentNameTextField.requestFocus ();
                studentNameTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_studentNameTextFieldActionPerformed

    private void studentSchoolTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSchoolTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setSchool (studentSchoolTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                studentSchoolTextField.requestFocus ();
                studentSchoolTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_studentSchoolTextFieldActionPerformed

    private void idNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idNumberTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setIdNumber (idNumberTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                idNumberTextField.requestFocus ();
                idNumberTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_idNumberTextFieldActionPerformed

    private void boxNumberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxNumberTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setBoxNumber (boxNumberTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                boxNumberTextField.requestFocus ();
                boxNumberTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_boxNumberTextFieldActionPerformed

    private void majorsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_majorsTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setMajors (majorsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                majorsTextField.requestFocus ();
                majorsTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_majorsTextFieldActionPerformed

    private void concentrationsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_concentrationsTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setConcentrations (concentrationsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                concentrationsTextField.requestFocus ();
                concentrationsTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_concentrationsTextFieldActionPerformed

    private void minorsTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_minorsTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            viewPanel.domain.utility.userDetails.setMinors (minorsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
            if (evt != null)
            {
                minorsTextField.requestFocus ();
                minorsTextField.selectAll ();
            }
        }
    }//GEN-LAST:event_minorsTextFieldActionPerformed

    private void settingsTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_settingsTextFieldFocusGained
        ((JTextField) evt.getSource ()).selectAll ();
    }//GEN-LAST:event_settingsTextFieldFocusGained

    private void studentNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_studentNameTextFieldFocusLost
        studentNameTextFieldActionPerformed (null);
    }//GEN-LAST:event_studentNameTextFieldFocusLost

    private void studentSchoolTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_studentSchoolTextFieldFocusLost
        studentSchoolTextFieldActionPerformed (null);
    }//GEN-LAST:event_studentSchoolTextFieldFocusLost

    private void idNumberTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_idNumberTextFieldFocusLost
        idNumberTextFieldActionPerformed (null);
    }//GEN-LAST:event_idNumberTextFieldFocusLost

    private void boxNumberTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_boxNumberTextFieldFocusLost
        boxNumberTextFieldActionPerformed (null);
    }//GEN-LAST:event_boxNumberTextFieldFocusLost

    private void majorsTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_majorsTextFieldFocusLost
        majorsTextFieldActionPerformed (null);
    }//GEN-LAST:event_majorsTextFieldFocusLost

    private void concentrationsTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_concentrationsTextFieldFocusLost
        concentrationsTextFieldActionPerformed (null);
    }//GEN-LAST:event_concentrationsTextFieldFocusLost

    private void minorsTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_minorsTextFieldFocusLost
        minorsTextFieldActionPerformed (null);
    }//GEN-LAST:event_minorsTextFieldFocusLost

    private void advisorNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advisorNameTextFieldFocusLost
        advisorNameTextFieldActionPerformed (null);
    }//GEN-LAST:event_advisorNameTextFieldFocusLost

    private void advisorEmailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advisorEmailTextFieldFocusLost
        advisorEmailTextFieldActionPerformed (null);
    }//GEN-LAST:event_advisorEmailTextFieldFocusLost

    private void advisorPhoneTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advisorPhoneTextFieldFocusLost
        advisorPhoneTextFieldActionPerformed (null);
    }//GEN-LAST:event_advisorPhoneTextFieldFocusLost

    private void advisorOfficeHoursTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advisorOfficeHoursTextFieldFocusLost
        advisorOfficeHoursTextFieldActionPerformed (null);
    }//GEN-LAST:event_advisorOfficeHoursTextFieldFocusLost

    private void advisorOfficeLocationTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advisorOfficeLocationTextFieldFocusLost
        advisorOfficeLocationTextFieldActionPerformed (null);
    }//GEN-LAST:event_advisorOfficeLocationTextFieldFocusLost

    private void emailAdvisorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailAdvisorButtonActionPerformed
        Matcher matcher = Domain.EMAIL_PATTERN.matcher (advisorEmailTextField.getText ());
        if (Domain.desktop != null)
        {
            if (matcher.matches ())
            {
                try
                {
                    Domain.desktop.mail (new URI ("mailto", advisorEmailTextField.getText (), null));
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
                advisorEmailTextField.requestFocus ();
                advisorEmailTextField.selectAll ();
            }
        }
        else
        {
            ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
            ViewPanel.OPTION_PANE.setMessage ("An email client could not be launched. You will have to send one manually.");
            ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
            JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, "Unable to Send");
            optionDialog.setVisible (true);
        }
    }//GEN-LAST:event_emailAdvisorButtonActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        if (!viewPanel.initLoading && viewPanel.domain.settingsOpening.empty ())
        {
            Matcher matcher = Domain.EMAIL_PATTERN.matcher (emailTextField.getText ());
            if (matcher.matches ())
            {
                viewPanel.domain.utility.userDetails.setEmail (emailTextField.getText ());
                viewPanel.domain.needsUserDetailsSave = true;
            }
            else
            {
                if (emailTextField.getText ().replaceAll (" ", "").equals (""))
                {
                    viewPanel.domain.utility.userDetails.setEmail (emailTextField.getText ());
                    viewPanel.domain.needsUserDetailsSave = true;
                }
                else
                {
                    emailTextField.requestFocus ();
                    emailTextField.selectAll ();
                }
            }
        }
        if (evt != null)
        {
            emailTextField.requestFocus ();
            emailTextField.selectAll ();
        }
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void emailTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailTextFieldFocusLost
        emailTextFieldActionPerformed (null);
    }//GEN-LAST:event_emailTextFieldFocusLost

    private void ddColor0PanelMouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_ddColor0PanelMouseReleased
    {//GEN-HEADEREND:event_ddColor0PanelMouseReleased
        Color color = viewPanel.domain.utility.preferences.dueDateColors[6];
        Color newColor = ColorPicker.showDialog (this, viewPanel.domain.language.getString ("selectColor"), color, false, viewPanel);
        if (newColor != null)
        {
            viewPanel.domain.utility.preferences.dueDateColors[6] = newColor;
            ddColor0Panel.setBackground (newColor);
            viewPanel.domain.needsPreferencesSave = true;
        }
    }//GEN-LAST:event_ddColor0PanelMouseReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addCategoryButton;
    private javax.swing.JPanel advisorDetailsPanel;
    private javax.swing.JLabel advisorEmailLabel;
    protected javax.swing.JTextField advisorEmailTextField;
    protected javax.swing.JTextField advisorNameTextField;
    private javax.swing.JLabel advisorOfficeHoursLabel;
    protected javax.swing.JTextField advisorOfficeHoursTextField;
    private javax.swing.JLabel advisorOfficeLocationLabel;
    protected javax.swing.JTextField advisorOfficeLocationTextField;
    private javax.swing.JLabel advisorPhoneLabel;
    protected javax.swing.JTextField advisorPhoneTextField;
    private javax.swing.JButton applyThemeButton;
    private javax.swing.JCheckBox autoUpdateCheckBox;
    private javax.swing.JLabel boxNumberLabel;
    private javax.swing.JTextField boxNumberTextField;
    private javax.swing.JPanel categoriesPanel;
    private javax.swing.JScrollPane categoriesScrollPane;
    protected javax.swing.JTextField categoryNameTextField;
    public javax.swing.JComboBox checkForComboBox;
    private javax.swing.JLabel checkForLabel;
    public javax.swing.JComboBox colorByComboBox;
    private javax.swing.JLabel colorByLabel;
    private javax.swing.JLabel concentrationsLabel;
    private javax.swing.JTextField concentrationsTextField;
    public javax.swing.JComboBox currentThemeComboBox;
    private javax.swing.JLabel currentThemeLabel;
    public javax.swing.JComboBox currentThemePrefComboBox;
    private javax.swing.JLabel currentThemePrefLabel;
    private javax.swing.JLabel ddColor0Label;
    private javax.swing.JPanel ddColor0Panel;
    private javax.swing.JLabel ddColor1Label;
    private javax.swing.JPanel ddColor1Panel;
    private javax.swing.JLabel ddColor2Label;
    private javax.swing.JPanel ddColor2Panel;
    private javax.swing.JLabel ddColor3Label;
    private javax.swing.JPanel ddColor3Panel;
    private javax.swing.JLabel ddColor4Label;
    private javax.swing.JPanel ddColor4Panel;
    private javax.swing.JLabel ddColor5Label;
    private javax.swing.JPanel ddColor5Panel;
    private javax.swing.JLabel ddColor6Label;
    private javax.swing.JPanel ddColor6Panel;
    private javax.swing.JButton defaultsDueDateButton;
    private javax.swing.JButton defaultsPriorityButton;
    private javax.swing.JPanel dueDateColoringPanel;
    private javax.swing.JButton emailAdvisorButton;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JButton emptyCategory;
    private javax.swing.JLabel eventColorLabel;
    private javax.swing.JPanel eventColorPanel;
    private javax.swing.JLabel idNumberLabel;
    private javax.swing.JTextField idNumberTextField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    public javax.swing.JComboBox languageComboBox;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JLabel majorsLabel;
    private javax.swing.JTextField majorsTextField;
    private javax.swing.JLabel minorsLabel;
    private javax.swing.JTextField minorsTextField;
    private javax.swing.JButton moveCategoryDownButton;
    private javax.swing.JButton moveCategoryUpButton;
    private javax.swing.JButton newThemeButton;
    private javax.swing.JLabel pColor1Label;
    private javax.swing.JPanel pColor1Panel;
    private javax.swing.JLabel pColor2Label;
    private javax.swing.JPanel pColor2Panel;
    private javax.swing.JLabel pColor3Label;
    private javax.swing.JPanel pColor3Panel;
    private javax.swing.JLabel pColor4Label;
    private javax.swing.JPanel pColor4Panel;
    private javax.swing.JLabel pColor5Label;
    private javax.swing.JPanel pColor5Panel;
    protected adl.go.gui.ColoredJPanel preferencesPanel;
    protected javax.swing.JScrollPane preferencesScrollPane;
    private javax.swing.JPanel preferencesScrollablePanel;
    private javax.swing.JPanel priorityColoringPanel;
    protected javax.swing.JButton removeCategoryButton;
    private javax.swing.JButton saveThemeButton;
    private javax.swing.JLabel schoolLabel;
    public adl.go.gui.ExtendedJTable settingsCategoriesTable;
    private javax.swing.JButton settingsCloseButton;
    protected javax.swing.JPanel settingsJPanel;
    private javax.swing.JLabel settingsLabel;
    protected javax.swing.JTabbedPane settingsTabbedPane;
    protected adl.go.gui.ColoredJPanel settingsUpperJPanel;
    private javax.swing.JCheckBox showRmAssnWarningCheckBox;
    private javax.swing.JPanel studentDetailsPanel;
    protected javax.swing.JTextField studentNameTextField;
    private javax.swing.JTextField studentSchoolTextField;
    private javax.swing.JPanel themeBottomColorPanel;
    private javax.swing.JPanel themeBottomColorPanel2;
    private javax.swing.JPanel themeDayInMonthColorPanel;
    private javax.swing.JPanel themeDayInMonthColorPanel2;
    private javax.swing.JPanel themeDayOutsideMonthColorPanel;
    private javax.swing.JPanel themeDayOutsideMonthColorPanel2;
    private javax.swing.JPanel themeDoubleWindowBottomColorPanel;
    private javax.swing.JPanel themeDoubleWindowBottomColorPanel2;
    private javax.swing.JPanel themeDoubleWindowTopColorPanel;
    private javax.swing.JPanel themeDoubleWindowTopColorPanel2;
    private javax.swing.JPanel themeEvenRowColorPanel;
    private javax.swing.JPanel themeLeftColorPanel;
    private javax.swing.JPanel themeLeftColorPanel2;
    private javax.swing.JPanel themeLeftPanel;
    private javax.swing.JPanel themeMiddleCalendarColorPanel;
    private javax.swing.JPanel themeMiddleCalendarColorPanel2;
    private javax.swing.JPanel themeMiddleColorPanel;
    private javax.swing.JPanel themeMiddleColorPanel2;
    private javax.swing.JPanel themeMiddleLoadingColorPanel;
    private javax.swing.JPanel themeMiddleLoadingColorPanel2;
    private javax.swing.JPanel themeMiddlePanel;
    private javax.swing.JPanel themeMiniBusyDayInMonthColorPanel;
    private javax.swing.JPanel themeMiniBusyDayOutsideMonthColorPanel;
    private javax.swing.JPanel themeMiniCompletedDayInMonthColorPanel;
    private javax.swing.JPanel themeMiniCompletedDayOutsideMonthColorPanel;
    private javax.swing.JPanel themeMiniDayInMonthColorPanel;
    private javax.swing.JPanel themeMiniDayOutsideMonthColorPanel;
    private javax.swing.JPanel themeMiniDaySelectedColorPanel;
    private javax.swing.JPanel themeMiniSundayColorPanel;
    private javax.swing.JPanel themeMiniWeekNamesBackgroundColorPanel;
    private javax.swing.JPanel themeMiniWeekNamesForegroundColorPanel;
    private javax.swing.JTextField themeNameTextField;
    private javax.swing.JPanel themeOddRowColorPanel;
    protected adl.go.gui.ColoredJPanel themePanel;
    private javax.swing.JPanel themeRightColorPanel;
    private javax.swing.JPanel themeRightColorPanel2;
    private javax.swing.JPanel themeRightPanel;
    protected javax.swing.JScrollPane themeScrollPane;
    private javax.swing.JPanel themeScrollablePanel;
    private javax.swing.JPanel themeSelectedRowColorPanel;
    private javax.swing.JPanel themeSingleWindowColorPanel;
    private javax.swing.JPanel themeSingleWindowColorPanel2;
    private javax.swing.JPanel themeTodayColorPanel;
    private javax.swing.JPanel themeTopColorPanel;
    private javax.swing.JPanel themeTopColorPanel2;
    protected adl.go.gui.ColoredJPanel userDetailsPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Check for changes to the user details before closing the Settings dialog.
     */
    public synchronized void checkUserDetailsChanges()
    {
        if (!studentNameTextField.getText ().equals (viewPanel.domain.utility.userDetails.getStudentName ()))
        {
            viewPanel.domain.utility.userDetails.setStudentName (studentNameTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!emailTextField.getText ().equals (viewPanel.domain.utility.userDetails.getEmail ()))
        {
            viewPanel.domain.utility.userDetails.setEmail (emailTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!advisorOfficeLocationTextField.getText ().equals (viewPanel.domain.utility.userDetails.getAdvisorsOfficeLocation ()))
        {
            viewPanel.domain.utility.userDetails.setAdvisorOfficeLocation (advisorOfficeLocationTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!advisorOfficeHoursTextField.getText ().equals (viewPanel.domain.utility.userDetails.getAdvisorOfficeHours ()))
        {
            viewPanel.domain.utility.userDetails.setAdvisorOfficeHours (advisorOfficeHoursTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!advisorPhoneTextField.getText ().equals (viewPanel.domain.utility.userDetails.getAdvisorPhone ()))
        {
            viewPanel.domain.utility.userDetails.setAdvisorPhone (advisorPhoneTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!advisorEmailTextField.getText ().equals (viewPanel.domain.utility.userDetails.getAdvisorEmail ()))
        {
            viewPanel.domain.utility.userDetails.setAdvisorEmail (advisorEmailTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!advisorNameTextField.getText ().equals (viewPanel.domain.utility.userDetails.getAdvisorName ()))
        {
            viewPanel.domain.utility.userDetails.setAdvisorName (advisorNameTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!studentSchoolTextField.getText ().equals (viewPanel.domain.utility.userDetails.getSchool ()))
        {
            viewPanel.domain.utility.userDetails.setSchool (studentSchoolTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!idNumberTextField.getText ().equals (viewPanel.domain.utility.userDetails.getIdNumber ()))
        {
            viewPanel.domain.utility.userDetails.setIdNumber (idNumberTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!boxNumberTextField.getText ().equals (viewPanel.domain.utility.userDetails.getBoxNumber ()))
        {
            viewPanel.domain.utility.userDetails.setBoxNumber (boxNumberTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!majorsTextField.getText ().equals (viewPanel.domain.utility.userDetails.getMajors ()))
        {
            viewPanel.domain.utility.userDetails.setMajors (majorsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!concentrationsTextField.getText ().equals (viewPanel.domain.utility.userDetails.getConcentrations ()))
        {
            viewPanel.domain.utility.userDetails.setConcentrations (concentrationsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
        if (!minorsTextField.getText ().equals (viewPanel.domain.utility.userDetails.getMinors ()))
        {
            viewPanel.domain.utility.userDetails.setMinors (minorsTextField.getText ());
            viewPanel.domain.needsUserDetailsSave = true;
        }
    }

    /**
     * Performs a close operation on the preferences dialog, checking for
     * unsaved changes first.
     */
    public void closeSettingsDialog()
    {
        dispose ();
        viewPanel.requestFocus ();

        if (settingsTabbedPane.getSelectedIndex () == 1)
        {
            checkUserDetailsChanges ();
        }
        viewPanel.domain.refreshAssignmentsList ();
        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            viewPanel.domain.refreshShownAssignmentsAndEvents ();
        }

        viewPanel.termTreeNodeSelected (new TreeSelectionEvent (this, null, false, null, null));
    }

    /**
     * Launches the Settings dialog.
     */
    protected void goViewSettings()
    {
        if (viewPanel.gradesDialog.isVisible ())
        {
            viewPanel.gradesDialog.setVisible (false);
        }
        if (viewPanel.termsAndCoursesDialog.isVisible ())
        {
            viewPanel.termsAndCoursesDialog.closeTermsAndCoursesDialog ();
        }
        viewPanel.closeRepeatEventDialog ();

        if (!isVisible ())
        {
            viewPanel.domain.settingsOpening.push (true);

            pack ();
            setLocationRelativeTo (viewPanel);
            setVisible (true);

            viewPanel.assignmentsTable.setSelectedRow (-1);
            viewPanel.assignmentsTableRowSelected (null);
            viewPanel.termTreeNodeSelected (new TreeSelectionEvent (this, null, false, null, null));

            showRmAssnWarningCheckBox.setSelected (viewPanel.domain.utility.preferences.rmAlert);
            autoUpdateCheckBox.setSelected (viewPanel.domain.utility.preferences.autoUpdate);
            checkForLabel.setEnabled (autoUpdateCheckBox.isSelected ());
            checkForComboBox.setEnabled (autoUpdateCheckBox.isSelected ());
            checkForComboBox.setSelectedIndex (viewPanel.domain.utility.preferences.updateCheckIndex);
            viewPanel.initLoading = true;
            currentThemePrefComboBox.setSelectedItem (viewPanel.domain.utility.currentTheme);
            viewPanel.initLoading = false;
            currentThemeComboBox.setSelectedItem (viewPanel.domain.utility.currentTheme);
            colorByComboBox.setSelectedIndex (viewPanel.domain.utility.preferences.colorByIndex);
            pColor5Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[4]);
            pColor4Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[3]);
            pColor3Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[2]);
            pColor2Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[1]);
            pColor1Panel.setBackground (viewPanel.domain.utility.preferences.priorityColors[0]);
            ddColor6Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[5]);
            ddColor5Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[4]);
            ddColor4Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[3]);
            ddColor3Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[2]);
            ddColor2Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[1]);
            ddColor1Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[0]);
            ddColor0Panel.setBackground (viewPanel.domain.utility.preferences.dueDateColors[6]);
            if (viewPanel.domain.currentCategoryIndex == -1)
            {
                settingsCategoriesTable.setSelectedRow (0);
            }
            settingsTabbedPane.setSelectedIndex (0);
            settingsCategoriesTableRowSelected (null);

            advisorOfficeLocationTextField.setText (viewPanel.domain.utility.userDetails.getAdvisorsOfficeLocation ());
            advisorOfficeHoursTextField.setText (viewPanel.domain.utility.userDetails.getAdvisorOfficeHours ());
            advisorPhoneTextField.setText (viewPanel.domain.utility.userDetails.getAdvisorPhone ());
            advisorEmailTextField.setText (viewPanel.domain.utility.userDetails.getAdvisorEmail ());
            if (advisorEmailTextField.getText ().equals (""))
            {
                emailAdvisorButton.setEnabled (false);
            }
            else
            {
                emailAdvisorButton.setEnabled (true);
            }
            advisorNameTextField.setText (viewPanel.domain.utility.userDetails.getAdvisorName ());
            studentNameTextField.setText (viewPanel.domain.utility.userDetails.getStudentName ());
            emailTextField.setText (viewPanel.domain.utility.userDetails.getEmail ());
            studentSchoolTextField.setText (viewPanel.domain.utility.userDetails.getSchool ());
            idNumberTextField.setText (viewPanel.domain.utility.userDetails.getIdNumber ());
            boxNumberTextField.setText (viewPanel.domain.utility.userDetails.getBoxNumber ());
            majorsTextField.setText (viewPanel.domain.utility.userDetails.getMajors ());
            concentrationsTextField.setText (viewPanel.domain.utility.userDetails.getConcentrations ());
            minorsTextField.setText (viewPanel.domain.utility.userDetails.getMinors ());

            viewPanel.domain.settingsOpening.pop ();
        }
        else
        {
            requestFocus ();
        }
    }

    /**
     * The event that occurs when an item is selected from the event categories
     * table.
     *
     * @param evt The triggering event.
     */
    public void settingsCategoriesTableRowSelected(ListSelectionEvent evt)
    {
        if (viewPanel.domain.currentCategoryIndex != -1)
        {
            Category category = viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex);
            if (!categoryNameTextField.getText ().equals (category.getName ()))
            {
                viewPanel.domain.setCategoryName (viewPanel.domain.currentCategoryIndex);
            }
        }

        viewPanel.domain.currentCategoryIndex = settingsCategoriesTable.getSelectedRow ();

        if (viewPanel.domain.currentCategoryIndex != -1)
        {
            viewPanel.domain.categoryLoading.push (true);

            Category category = viewPanel.domain.utility.preferences.categories.get (viewPanel.domain.currentCategoryIndex);
            categoryNameTextField.setText (category.getName ());
            eventColorPanel.setBackground (category.getColor ());

            viewPanel.domain.categoryLoading.pop ();
        }

        if (viewPanel.domain.currentCategoryIndex != -1 && viewPanel.domain.currentCategoryIndex > 0)
        {
            removeCategoryButton.setEnabled (true);
        }
        else
        {
            removeCategoryButton.setEnabled (false);
        }

        if (viewPanel.domain.currentCategoryIndex > 1)
        {
            moveCategoryUpButton.setEnabled (true);
        }
        else
        {
            moveCategoryUpButton.setEnabled (false);
        }

        if (viewPanel.domain.currentCategoryIndex < categoryTableModel.getRowCount () - 1 && viewPanel.domain.currentCategoryIndex > 0)
        {
            moveCategoryDownButton.setEnabled (true);
        }
        else
        {
            moveCategoryDownButton.setEnabled (false);
        }

        categoryNameTextField.requestFocus ();
        categoryNameTextField.selectAll ();
    }

    /**
     * Fire a currentThemeComboBox item changed event.
     */
    protected void currentThemeComboBoxItemStateChanged()
    {
        currentThemeComboBoxItemStateChanged (null);
    }

    /**
     * Apply the language for this dialog.
     *
     * @param language The language resource to be applied.
     */
    public void applyLanguage(ResourceBundle language)
    {
    }
}
