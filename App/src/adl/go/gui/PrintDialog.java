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
import adl.go.resource.PrintUtilities;
import adl.go.types.Course;
import adl.go.types.ExtendedTreeNode;
import adl.go.types.Term;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.SheetCollate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable.PrintMode;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;

/**
 * The Import from Backup dialog.
 *
 * @author Alex Laird
 */
public class PrintDialog extends EscapeDialog
{
    /**
     * The model for available printers.
     */
    private DefaultComboBoxModel printersModel = new DefaultComboBoxModel ();
    /**
     * The model for available views.
     */
    private DefaultComboBoxModel viewsModel = new DefaultComboBoxModel ();
    /**
     * The model for available sorting modes.
     */
    private DefaultComboBoxModel sortingModel = new DefaultComboBoxModel ();
    /**
     * The model for available terms and courses.
     */
    private DefaultComboBoxModel termsAndCoursesModel = new DefaultComboBoxModel ();
    /**
     * The model for available sorting means.
     */
    private DefaultComboBoxModel ascDescModel = new DefaultComboBoxModel ();
    /**
     * The model for the month names model.
     */
    private DefaultComboBoxModel monthNamesModel1 = new DefaultComboBoxModel ();
    /**
     * The model for the month names model.
     */
    private DefaultComboBoxModel monthNamesModel2 = new DefaultComboBoxModel ();
    /**
     * True while components are initializing, false otherwise.
     */
    private boolean initLoading = true;
    /**
     * The current printer selected.
     */
    private PrintService curPrinter = null;

    /**
     * Construct the Print dialog.
     */
    public PrintDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);
        initComponents ();
    }

    /**
     * Initialize the Print dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("print"));
        viewsModel.addElement (viewPanel.domain.language.getString ("listView"));
        viewsModel.addElement (viewPanel.domain.language.getString ("calendarView"));
        sortingModel.addElement (viewPanel.domain.language.getString ("done"));
        sortingModel.addElement (viewPanel.domain.language.getString ("task"));
        sortingModel.addElement (viewPanel.domain.language.getString ("type"));
        sortingModel.addElement (viewPanel.domain.language.getString ("course") + "/" + viewPanel.domain.language.getString ("category"));
        sortingModel.addElement (viewPanel.domain.language.getString ("dueDate"));
        sortingModel.addElement (viewPanel.domain.language.getString ("grade"));
        ascDescModel.addElement (viewPanel.domain.language.getString ("ascending"));
        ascDescModel.addElement (viewPanel.domain.language.getString ("descending"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("january"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("february"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("march"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("april"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("may"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("june"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("july"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("august"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("september"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("october"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("november"));
        monthNamesModel1.addElement (viewPanel.domain.language.getString ("december"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("january"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("february"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("march"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("april"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("may"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("june"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("july"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("august"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("september"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("october"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("november"));
        monthNamesModel2.addElement (viewPanel.domain.language.getString ("december"));
        printPanel.setBackground (viewPanel.domain.utility.currentTheme.colorSingleWindowBackground1);
        printButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        printCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        printBottomJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        printTopJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowTopBackground1);

        printCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        printCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        printButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        printButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        printLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        nameLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        statusLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        typeLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        infoLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        nameComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        propertiesButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        statusResponseLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        typeResponseLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        infoResponseLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        numCopiesLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        collateCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        numCopiesSpinner.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        viewLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        viewsComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        betweenLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        sortingByLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        sortingByComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        contentsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        contentsComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        startMonthComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        endMonthComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        startYearSpinner.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        endYearSpinner.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ascDescComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        contentsComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        nameComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        viewsComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        startMonthComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        endMonthComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        startYearSpinner.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        endYearSpinner.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        sortingByComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        contentsComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        ascDescComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        ((TitledBorder) printServicePanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ((TitledBorder) printDetailsPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        ((TitledBorder) copiesPanel.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);

        initLoading = false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        printPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        printBottomJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        printServicePanel = new javax.swing.JPanel();
        leftPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        infoLabel = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        nameComboBox = new javax.swing.JComboBox();
        propertiesButton = new javax.swing.JButton();
        statusResponseLabel = new javax.swing.JLabel();
        blankLabel1 = new javax.swing.JLabel();
        typeResponseLabel = new javax.swing.JLabel();
        blankLabel2 = new javax.swing.JLabel();
        infoResponseLabel = new javax.swing.JLabel();
        blankLabel3 = new javax.swing.JLabel();
        copiesPanel = new javax.swing.JPanel();
        numCopiesLabel = new javax.swing.JLabel();
        numCopiesSpinner = new javax.swing.JSpinner();
        collateCheckBox = new javax.swing.JCheckBox();
        printDetailsPanel = new javax.swing.JPanel();
        viewLabel = new javax.swing.JLabel();
        viewsComboBox = new javax.swing.JComboBox();
        calendarPanel = new javax.swing.JPanel();
        betweenLabel = new javax.swing.JLabel();
        listPanel = new javax.swing.JPanel();
        sortingByLabel = new javax.swing.JLabel();
        sortingByComboBox = new javax.swing.JComboBox();
        ascDescComboBox = new javax.swing.JComboBox();
        startMonthComboBox = new javax.swing.JComboBox();
        startYearSpinner = new javax.swing.JSpinner();
        endMonthComboBox = new javax.swing.JComboBox();
        endYearSpinner = new javax.swing.JSpinner();
        contentsLabel = new javax.swing.JLabel();
        contentsComboBox = new javax.swing.JComboBox();
        printTopJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        printCloseButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        printLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        printServicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("printService")));
        printServicePanel.setOpaque(false);

        leftPanel.setOpaque(false);
        leftPanel.setLayout(new java.awt.GridLayout(4, 1));

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nameLabel.setText(viewPanel.domain.language.getString ("name") + ": ");
        leftPanel.add(nameLabel);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        statusLabel.setText(viewPanel.domain.language.getString ("status") + ": ");
        leftPanel.add(statusLabel);

        typeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        typeLabel.setText(viewPanel.domain.language.getString ("type") + ": ");
        leftPanel.add(typeLabel);

        infoLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        infoLabel.setText(viewPanel.domain.language.getString ("info") + ": ");
        leftPanel.add(infoLabel);

        rightPanel.setOpaque(false);
        rightPanel.setLayout(new java.awt.GridLayout(4, 2, 10, 0));

        nameComboBox.setModel(printersModel);
        nameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameComboBoxActionPerformed(evt);
            }
        });
        rightPanel.add(nameComboBox);

        propertiesButton.setText(viewPanel.domain.language.getString ("properties"));
        propertiesButton.setEnabled(false);
        rightPanel.add(propertiesButton);
        rightPanel.add(statusResponseLabel);
        rightPanel.add(blankLabel1);
        rightPanel.add(typeResponseLabel);
        rightPanel.add(blankLabel2);
        rightPanel.add(infoResponseLabel);
        rightPanel.add(blankLabel3);

        javax.swing.GroupLayout printServicePanelLayout = new javax.swing.GroupLayout(printServicePanel);
        printServicePanel.setLayout(printServicePanelLayout);
        printServicePanelLayout.setHorizontalGroup(
            printServicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printServicePanelLayout.createSequentialGroup()
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
        );
        printServicePanelLayout.setVerticalGroup(
            printServicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
            .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
        );

        copiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("copies")));
        copiesPanel.setOpaque(false);

        numCopiesLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        numCopiesLabel.setText(viewPanel.domain.language.getString("numberOfCopies") + ": ");

        numCopiesSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));

        collateCheckBox.setText(viewPanel.domain.language.getString("collate"));
        collateCheckBox.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout copiesPanelLayout = new javax.swing.GroupLayout(copiesPanel);
        copiesPanel.setLayout(copiesPanelLayout);
        copiesPanelLayout.setHorizontalGroup(
            copiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, copiesPanelLayout.createSequentialGroup()
                .addGroup(copiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(collateCheckBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(numCopiesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numCopiesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        copiesPanelLayout.setVerticalGroup(
            copiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(copiesPanelLayout.createSequentialGroup()
                .addGroup(copiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numCopiesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numCopiesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collateCheckBox)
                .addContainerGap())
        );

        printDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("printDetails")));
        printDetailsPanel.setOpaque(false);

        viewLabel.setText(viewPanel.domain.language.getString ("view") + ": ");

        viewsComboBox.setModel(viewsModel);
        viewsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewsComboBoxActionPerformed(evt);
            }
        });

        calendarPanel.setOpaque(false);

        betweenLabel.setText(viewPanel.domain.language.getString ("between") + ": ");

        listPanel.setOpaque(false);

        sortingByLabel.setText(viewPanel.domain.language.getString ("sortingBy") + ": ");

        sortingByComboBox.setModel(sortingModel);
        sortingByComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortingByComboBoxActionPerformed(evt);
            }
        });

        ascDescComboBox.setModel(ascDescModel);
        ascDescComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ascDescComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout listPanelLayout = new javax.swing.GroupLayout(listPanel);
        listPanel.setLayout(listPanelLayout);
        listPanelLayout.setHorizontalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, listPanelLayout.createSequentialGroup()
                .addComponent(sortingByLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ascDescComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 155, Short.MAX_VALUE)
                    .addComponent(sortingByComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 155, Short.MAX_VALUE)))
        );
        listPanelLayout.setVerticalGroup(
            listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(listPanelLayout.createSequentialGroup()
                .addGroup(listPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sortingByLabel)
                    .addComponent(sortingByComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ascDescComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        startMonthComboBox.setModel(monthNamesModel1);

        startYearSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        startYearSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(startYearSpinner, "#"));

        endMonthComboBox.setModel(monthNamesModel2);

        endYearSpinner.setModel(new javax.swing.SpinnerNumberModel());
        endYearSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(endYearSpinner, "#"));

        javax.swing.GroupLayout calendarPanelLayout = new javax.swing.GroupLayout(calendarPanel);
        calendarPanel.setLayout(calendarPanelLayout);
        calendarPanelLayout.setHorizontalGroup(
            calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calendarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(calendarPanelLayout.createSequentialGroup()
                        .addComponent(betweenLabel)
                        .addGap(58, 58, 58))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, calendarPanelLayout.createSequentialGroup()
                        .addComponent(startMonthComboBox, 0, 154, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(startYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(calendarPanelLayout.createSequentialGroup()
                        .addComponent(endMonthComboBox, 0, 154, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(endYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        calendarPanelLayout.setVerticalGroup(
            calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(calendarPanelLayout.createSequentialGroup()
                .addComponent(betweenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(calendarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endMonthComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endYearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        contentsLabel.setText(viewPanel.domain.language.getString ("content") + ": ");

        contentsComboBox.setModel(termsAndCoursesModel);
        contentsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout printDetailsPanelLayout = new javax.swing.GroupLayout(printDetailsPanel);
        printDetailsPanel.setLayout(printDetailsPanelLayout);
        printDetailsPanelLayout.setHorizontalGroup(
            printDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calendarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printDetailsPanelLayout.createSequentialGroup()
                        .addComponent(viewLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewsComboBox, 0, 165, Short.MAX_VALUE))
                    .addGroup(printDetailsPanelLayout.createSequentialGroup()
                        .addComponent(contentsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contentsComboBox, 0, 165, Short.MAX_VALUE)))
                .addContainerGap())
        );
        printDetailsPanelLayout.setVerticalGroup(
            printDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printDetailsPanelLayout.createSequentialGroup()
                .addGroup(printDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(viewLabel)
                    .addComponent(viewsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(calendarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contentsLabel)
                    .addComponent(contentsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout printBottomJPanelLayout = new javax.swing.GroupLayout(printBottomJPanel);
        printBottomJPanel.setLayout(printBottomJPanelLayout);
        printBottomJPanelLayout.setHorizontalGroup(
            printBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printBottomJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(printServicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printBottomJPanelLayout.createSequentialGroup()
                        .addComponent(printDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(copiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        printBottomJPanelLayout.setVerticalGroup(
            printBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printBottomJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printServicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(copiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        printTopJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        printCloseButton.setText(viewPanel.domain.language.getString ("close"));
        printCloseButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
        printCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printCloseButtonActionPerformed(evt);
            }
        });

        printButton.setText(viewPanel.domain.language.getString ("print"));
        printButton.setToolTipText(viewPanel.domain.language.getString ("printButtonToolTip"));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        printLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/printer.png"))); // NOI18N
        printLabel.setText(viewPanel.domain.language.getString ("print"));

        javax.swing.GroupLayout printTopJPanelLayout = new javax.swing.GroupLayout(printTopJPanel);
        printTopJPanel.setLayout(printTopJPanelLayout);
        printTopJPanelLayout.setHorizontalGroup(
            printTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printTopJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addComponent(printCloseButton)
                .addContainerGap())
        );
        printTopJPanelLayout.setVerticalGroup(
            printTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printTopJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(printLabel)
                    .addComponent(printButton)
                    .addComponent(printCloseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout printPanelLayout = new javax.swing.GroupLayout(printPanel);
        printPanel.setLayout(printPanelLayout);
        printPanelLayout.setHorizontalGroup(
            printPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printTopJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(printBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        printPanelLayout.setVerticalGroup(
            printPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printPanelLayout.createSequentialGroup()
                .addComponent(printTopJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(printPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void printCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printCloseButtonActionPerformed
        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_printCloseButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // close the current print dialog
        dispose ();
        viewPanel.printingDialog.setLocationRelativeTo (viewPanel);
        viewPanel.printingDialog.setVisible (true);
        viewPanel.printingDialog.requestFocus ();

        new Thread (new Runnable ()
        {
            @Override
            public void run()
            {
                try
                {
                    String strDate = MessageFormat.format ("{0,date,short} {0,time,short}", new Date ());
                    String headerString = "Get Organized - " + strDate;

                    String name = "Get Organized";
                    if (!viewPanel.domain.utility.userDetails.getStudentName ().equals (""))
                    {
                        name = viewPanel.domain.utility.userDetails.getStudentName () + " - " + name;
                        headerString = viewPanel.domain.utility.userDetails.getStudentName () + " - " + headerString;
                    }

                    MessageFormat header = new MessageFormat (headerString);
                    MessageFormat footer = new MessageFormat ("- Page {0} -");

                    if (viewsComboBox.getSelectedIndex () == 0)
                    {
                        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet ();
                        aset.add (new JobName (name, Locale.getDefault ()));
                        aset.add (new Copies (Integer.parseInt (numCopiesSpinner.getValue ().toString ())));
                        if (collateCheckBox.isSelected ())
                        {
                            aset.add (SheetCollate.COLLATED);
                        }
                        else
                        {
                            aset.add (SheetCollate.UNCOLLATED);
                        }
                        viewPanel.assignmentsTable.print (PrintMode.FIT_WIDTH, header, footer, false, aset, false, curPrinter);
                    }
                    else
                    {
                        Dimension oldDim = viewPanel.mainFrame.getSize ();
                        Date oldDate = viewPanel.miniCalendar.getDate ();
                        viewPanel.mainFrame.setSize (1092, 900);
                        PrinterJob pj = PrinterJob.getPrinterJob ();
                        pj.setCopies (Integer.parseInt (numCopiesSpinner.getValue ().toString ()));
                        pj.setPrintService (curPrinter);
                        pj.setJobName (name);
                        Calendar currDate = Calendar.getInstance ();
                        currDate.set (Calendar.DAY_OF_MONTH, 1);
                        currDate.set (Calendar.MONTH, startMonthComboBox.getSelectedIndex ());
                        currDate.set (Calendar.YEAR, Integer.parseInt (startYearSpinner.getValue ().toString ()));
                        Calendar endDate = Calendar.getInstance ();
                        endDate.set (Calendar.MONTH, endMonthComboBox.getSelectedIndex ());
                        endDate.set (Calendar.YEAR, Integer.parseInt (endYearSpinner.getValue ().toString ()));
                        endDate.set (Calendar.DAY_OF_MONTH, endDate.getActualMaximum (Calendar.DAY_OF_MONTH));

                        while (currDate.before (endDate))
                        {
                            viewPanel.miniCalendar.setDate (currDate.getTime ());
                            viewPanel.loadCalendarView (false);
                            PrintUtilities.printComponent (viewPanel.monthViewPanel, pj);
                            currDate.add (Calendar.MONTH, 1);
                        }
                        viewPanel.miniCalendar.setDate (oldDate);
                        viewPanel.loadCalendarView (true);
                        viewPanel.mainFrame.setSize (oldDim);
                    }

                    viewPanel.printingDialog.dispose ();
                }
                catch (PrinterException ex)
                {
                }
            }
        }).start ();
    }//GEN-LAST:event_printButtonActionPerformed

    private void viewsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewsComboBoxActionPerformed
        if (viewsComboBox.getSelectedIndex () == 0)
        {
            for (int i = 0; i < calendarPanel.getComponentCount (); ++i)
            {
                calendarPanel.getComponent (i).setEnabled (false);
            }
            for (int i = 0; i < listPanel.getComponentCount (); ++i)
            {
                listPanel.getComponent (i).setEnabled (true);
            }
        }
        else
        {
            for (int i = 0; i < calendarPanel.getComponentCount (); ++i)
            {
                calendarPanel.getComponent (i).setEnabled (true);
            }
            for (int i = 0; i < listPanel.getComponentCount (); ++i)
            {
                listPanel.getComponent (i).setEnabled (false);
            }
        }
        if (!initLoading)
        {
            viewPanel.middleTabbedPane.setSelectedIndex (viewsComboBox.getSelectedIndex ());
        }
    }//GEN-LAST:event_viewsComboBoxActionPerformed

    private void sortingByComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortingByComboBoxActionPerformed
        if (!initLoading)
        {
            if (sortingByComboBox.getSelectedIndex () != viewPanel.assignmentsTableModel.getColumnSorting ())
            {
                initLoading = true;
                ascDescComboBox.setSelectedIndex (0);
                initLoading = false;
            }
            viewPanel.assignmentsTableHeaderSelected (null, sortingByComboBox.getSelectedIndex ());
        }
    }//GEN-LAST:event_sortingByComboBoxActionPerformed

    private void contentsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentsComboBoxActionPerformed
        if (!initLoading)
        {
            Object obj = contentsComboBox.getSelectedItem ();
            if (obj instanceof ExtendedTreeNode)
            {
                viewPanel.termTree.setSelectionPath (new TreePath (((ExtendedTreeNode) obj).getPath ()));
            }
            else
            {
                viewPanel.termTree.setSelectionPath (null);
            }
        }
    }//GEN-LAST:event_contentsComboBoxActionPerformed

    private void ascDescComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ascDescComboBoxActionPerformed
        if (!initLoading)
        {
            if ((viewPanel.domain.utility.preferences.sortAscending && ascDescComboBox.getSelectedIndex () != 0)
                || (!viewPanel.domain.utility.preferences.sortAscending && ascDescComboBox.getSelectedIndex () != 1))
            {
                viewPanel.assignmentsTable.getColumnModel ().getColumn (viewPanel.assignmentsTableModel.getColumnSorting ()).setHeaderValue (viewPanel.assignmentsTable.getColumnModel ().getColumn (viewPanel.assignmentsTableModel.getColumnSorting ()).getHeaderValue ().toString ().replaceAll ("\\<html\\>|\\<b\\>|\\</html\\>|\\</b\\>", ""));
                viewPanel.assignmentsTableModel.setColumnSorting (sortingByComboBox.getSelectedIndex ());
                viewPanel.assignmentsTable.getColumnModel ().getColumn (viewPanel.assignmentsTableModel.getColumnSorting ()).setHeaderValue ("<html><b>" + viewPanel.assignmentsTable.getColumnModel ().getColumn (viewPanel.assignmentsTableModel.getColumnSorting ()).getHeaderValue () + "</b></html>");
                viewPanel.filter (true);

                viewPanel.domain.utility.preferences.sortIndex = viewPanel.assignmentsTableModel.getColumnSorting ();
                viewPanel.domain.utility.preferences.sortAscending = viewPanel.assignmentsTableModel.isSortAscending ();
                viewPanel.domain.needsPreferencesSave = true;
            }
        }
    }//GEN-LAST:event_ascDescComboBoxActionPerformed

    private void nameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameComboBoxActionPerformed
        if (!initLoading)
        {
            for (PrintService printer : PrintServiceLookup.lookupPrintServices (null, null))
            {
                if (printer.hashCode () == ((PrinterItem) nameComboBox.getSelectedItem ()).code)
                {
                    AttributeSet att = printer.getAttributes ();
                    for (Attribute a : att.toArray ())
                    {
                        // Change to the new printer
                        curPrinter = printer;

                        String attributeName;
                        String attributeValue;
                        attributeName = a.getName ();
                        attributeValue = att.get (a.getClass ()).toString ();
                        if (attributeName.equals ("printer-is-accepting-jobs"))
                        {
                            if (attributeValue.equals ("accepting-jobs"))
                            {
                                statusResponseLabel.setText ("Online");
                            }
                            else
                            {
                                statusResponseLabel.setText ("Offline");
                            }
                        }
                        else if (attributeName.equals ("color-supported"))
                        {
                            if (attributeValue.equals ("supported"))
                            {
                                infoResponseLabel.setText ("Color support");
                            }
                            else
                            {
                                infoResponseLabel.setText ("No color support");
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_nameComboBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JComboBox ascDescComboBox;
    private javax.swing.JLabel betweenLabel;
    private javax.swing.JLabel blankLabel1;
    private javax.swing.JLabel blankLabel2;
    private javax.swing.JLabel blankLabel3;
    private javax.swing.JPanel calendarPanel;
    private javax.swing.JCheckBox collateCheckBox;
    protected javax.swing.JComboBox contentsComboBox;
    private javax.swing.JLabel contentsLabel;
    private javax.swing.JPanel copiesPanel;
    protected javax.swing.JComboBox endMonthComboBox;
    private javax.swing.JSpinner endYearSpinner;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel infoResponseLabel;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel listPanel;
    protected javax.swing.JComboBox nameComboBox;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel numCopiesLabel;
    private javax.swing.JSpinner numCopiesSpinner;
    protected adl.go.gui.ColoredJPanel printBottomJPanel;
    private javax.swing.JButton printButton;
    private javax.swing.JButton printCloseButton;
    private javax.swing.JPanel printDetailsPanel;
    private javax.swing.JLabel printLabel;
    protected adl.go.gui.ColoredJPanel printPanel;
    private javax.swing.JPanel printServicePanel;
    protected adl.go.gui.ColoredJPanel printTopJPanel;
    private javax.swing.JButton propertiesButton;
    private javax.swing.JPanel rightPanel;
    protected javax.swing.JComboBox sortingByComboBox;
    private javax.swing.JLabel sortingByLabel;
    protected javax.swing.JComboBox startMonthComboBox;
    private javax.swing.JSpinner startYearSpinner;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel statusResponseLabel;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JLabel typeResponseLabel;
    private javax.swing.JLabel viewLabel;
    protected javax.swing.JComboBox viewsComboBox;
    // End of variables declaration//GEN-END:variables

    /**
     * Launch the dialog.
     */
    protected void goViewPrint()
    {
        initLoading = true;

        viewPanel.closeOpenWindows ();
        printersModel.removeAllElements ();
        int defaultIndex = -1;
        int curIndex = 0;
        for (PrintService printer : PrintServiceLookup.lookupPrintServices (null, null))
        {
            if (printer == PrintServiceLookup.lookupDefaultPrintService ())
            {
                curPrinter = printer;
                defaultIndex = curIndex;
            }
            printersModel.addElement (new PrinterItem (printer.getName (), printer.hashCode ()));
            ++curIndex;
        }
        if (defaultIndex != -1)
        {
            nameComboBox.setSelectedIndex (defaultIndex);
        }
        else
        {
            if (PrintServiceLookup.lookupPrintServices (null, null).length > 0)
            {
                curPrinter = PrintServiceLookup.lookupPrintServices (null, null)[0];
            }
        }

        startMonthComboBox.setSelectedIndex (viewPanel.miniCalendar.getMonthChooser ().getMonth ());
        endMonthComboBox.setSelectedIndex (viewPanel.miniCalendar.getMonthChooser ().getMonth ());
        startYearSpinner.setValue (viewPanel.miniCalendar.getYearChooser ().getYear ());
        endYearSpinner.setValue (viewPanel.miniCalendar.getYearChooser ().getYear ());

        viewsComboBox.setSelectedIndex (viewPanel.middleTabbedPane.getSelectedIndex ());
        viewsComboBoxActionPerformed (null);
        sortingByComboBox.setSelectedIndex (viewPanel.domain.utility.preferences.sortIndex);
        ascDescComboBox.setSelectedIndex ((viewPanel.domain.utility.preferences.sortAscending) ? 0 : 1);

        // fill the content combo with terms and courses
        termsAndCoursesModel.removeAllElements ();
        termsAndCoursesModel.addElement ("-" + viewPanel.domain.language.getString ("all") + "-");
        for (int i = 0; i < viewPanel.domain.utility.terms.size (); ++i)
        {
            Term term = viewPanel.domain.utility.terms.get (i);
            termsAndCoursesModel.addElement (term);
            for (int j = 0; j < term.getCourseCount (); ++j)
            {
                Course course = term.getCourse (j);
                termsAndCoursesModel.addElement (course);
            }
        }

        initLoading = false;
        nameComboBoxActionPerformed (null);

        if (viewPanel.getSelectedCourseIndex () != -1)
        {
            contentsComboBox.setSelectedItem (viewPanel.domain.utility.courses.get (viewPanel.getSelectedCourseIndex ()));
        }
        else if (viewPanel.getSelectedTermIndex () != -1)
        {
            contentsComboBox.setSelectedItem (viewPanel.domain.utility.terms.get (viewPanel.getSelectedTermIndex ()));
        }
        else
        {
            contentsComboBox.setSelectedIndex (0);
        }

        viewPanel.printingDialog.pack ();
        pack ();
        setLocationRelativeTo (viewPanel);
        setVisible (true);
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

class PrinterItem
{
    protected String name;
    protected int code;

    PrinterItem(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
