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
import java.awt.Color;
import java.util.ResourceBundle;

/**
 * The Getting Started Dialog.
 *
 * @author Alex Laird
 */
public class GettingStartedDialog extends EscapeDialog
{
    /**
     * Construct the Getting Started dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public GettingStartedDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);
        initComponents ();
    }

    /**
     * Initialize the Getting Started dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("gettingStarted"));
        gettingStartedJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorSingleWindowBackground1);
        welcomeLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        aTACFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addingTermsAndCoursesLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        wFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        addingTermsAndCoursesContLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        gSATFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradingScaleAndTextbooksLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        aTACCFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradingScaleAndTextbooksContLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        gSATCFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        assignmentsAndEventsLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        aEFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        viewsAndFiltersLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        vFFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        sortingLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        sFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradesGettingStartedLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        gFirstLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        closeGettingStartedButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        closeGettingStartedButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        nextGettingStartedButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        nextGettingStartedButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        prevGettingStartedButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        prevGettingStartedButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        dontShowCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gettingStartedJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        jSeparator8 = new javax.swing.JSeparator();
        getOrganizedLabel = new javax.swing.JLabel();
        gettingStartedPanel = new javax.swing.JPanel();
        firstCard = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        aTACFirstLabel = new javax.swing.JLabel();
        addingTermsAndCoursesLabel = new javax.swing.JLabel();
        wFirstLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        secondCard = new javax.swing.JPanel();
        addingTermsAndCoursesContLabel = new javax.swing.JLabel();
        gSATFirstLabel = new javax.swing.JLabel();
        gradingScaleAndTextbooksLabel = new javax.swing.JLabel();
        aTACCFirstLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        thirdCard = new javax.swing.JPanel();
        gradingScaleAndTextbooksContLabel = new javax.swing.JLabel();
        gSATCFirstLabel = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        fourthCard = new javax.swing.JPanel();
        assignmentsAndEventsLabel = new javax.swing.JLabel();
        aEFirstLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        fifthCard = new javax.swing.JPanel();
        viewsAndFiltersLabel = new javax.swing.JLabel();
        vFFirstLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        sixthCard = new javax.swing.JPanel();
        sortingLabel = new javax.swing.JLabel();
        sFirstLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        seventhCard = new javax.swing.JPanel();
        gradesGettingStartedLabel = new javax.swing.JLabel();
        gFirstLabel = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        aboutPanelLowerPanel = new javax.swing.JPanel();
        closeGettingStartedButton = new javax.swing.JButton();
        nextGettingStartedButton = new javax.swing.JButton();
        prevGettingStartedButton = new javax.swing.JButton();
        dontShowCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        gettingStartedJPanel.setPreferredSize(new java.awt.Dimension(562, 523));

        getOrganizedLabel.setFont(new java.awt.Font("Verdana", 1, 16));
        getOrganizedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getOrganizedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "logo.png"))); // NOI18N

        gettingStartedPanel.setOpaque(false);
        gettingStartedPanel.setPreferredSize(new java.awt.Dimension(559, 0));
        gettingStartedPanel.setLayout(new java.awt.CardLayout());

        firstCard.setOpaque(false);
        firstCard.setLayout(null);

        welcomeLabel.setText(viewPanel.domain.language.getString ("welcome"));
        firstCard.add(welcomeLabel);
        welcomeLabel.setBounds(20, 0, 230, 14);

        aTACFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("first2") + "</html>");
        aTACFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        firstCard.add(aTACFirstLabel);
        aTACFirstLabel.setBounds(20, 180, 240, 120);

        addingTermsAndCoursesLabel.setText(viewPanel.domain.language.getString ("addingTermsAndCourses"));
        firstCard.add(addingTermsAndCoursesLabel);
        addingTermsAndCoursesLabel.setBounds(20, 150, 240, 20);

        wFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("first1") + "</html>");
        wFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        firstCard.add(wFirstLabel);
        wFirstLabel.setBounds(20, 26, 520, 110);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "add_term_2.png"))); // NOI18N
        firstCard.add(jLabel4);
        jLabel4.setBounds(340, 230, 200, 110);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "add_term_1.png"))); // NOI18N
        firstCard.add(jLabel1);
        jLabel1.setBounds(270, 120, 150, 180);

        gettingStartedPanel.add(firstCard, "card2");

        secondCard.setOpaque(false);
        secondCard.setLayout(null);

        addingTermsAndCoursesContLabel.setText(viewPanel.domain.language.getString ("addingTermsAndCoursesCont"));
        secondCard.add(addingTermsAndCoursesContLabel);
        addingTermsAndCoursesContLabel.setBounds(10, 0, 360, 20);

        gSATFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("second2") + "</html>");
        gSATFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        secondCard.add(gSATFirstLabel);
        gSATFirstLabel.setBounds(320, 210, 230, 130);

        gradingScaleAndTextbooksLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradingScaleAndTextbooksLabel.setText(viewPanel.domain.language.getString ("gradingScaleAndTextbooks"));
        secondCard.add(gradingScaleAndTextbooksLabel);
        gradingScaleAndTextbooksLabel.setBounds(238, 175, 310, 30);

        aTACCFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("second1") + "</html>");
        aTACCFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        secondCard.add(aTACCFirstLabel);
        aTACCFirstLabel.setBounds(10, 25, 330, 140);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "add_course_1.png"))); // NOI18N
        secondCard.add(jLabel5);
        jLabel5.setBounds(350, 10, 192, 160);

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "add_course_2.png"))); // NOI18N
        secondCard.add(jLabel6);
        jLabel6.setBounds(10, 160, 170, 170);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "weight_1.png"))); // NOI18N
        secondCard.add(jLabel7);
        jLabel7.setBounds(170, 200, 140, 131);

        gettingStartedPanel.add(secondCard, "card3");

        thirdCard.setOpaque(false);
        thirdCard.setLayout(null);

        gradingScaleAndTextbooksContLabel.setText(viewPanel.domain.language.getString ("gradingScaleAndTextbooksCont"));
        thirdCard.add(gradingScaleAndTextbooksContLabel);
        gradingScaleAndTextbooksContLabel.setBounds(20, 0, 521, 20);

        gSATCFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("third1") + "</html>");
        gSATCFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        thirdCard.add(gSATCFirstLabel);
        gSATCFirstLabel.setBounds(20, 30, 280, 300);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "weight_2.png"))); // NOI18N
        thirdCard.add(jLabel8);
        jLabel8.setBounds(330, 0, 150, 100);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "weight_3.png"))); // NOI18N
        thirdCard.add(jLabel9);
        jLabel9.setBounds(325, 80, 194, 180);

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "grading.png"))); // NOI18N
        thirdCard.add(jLabel16);
        jLabel16.setBounds(310, 250, 230, 80);

        gettingStartedPanel.add(thirdCard, "card4");

        fourthCard.setOpaque(false);
        fourthCard.setLayout(null);

        assignmentsAndEventsLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        assignmentsAndEventsLabel.setText(viewPanel.domain.language.getString ("assignmentsAndEvents"));
        fourthCard.add(assignmentsAndEventsLabel);
        assignmentsAndEventsLabel.setBounds(260, 0, 280, 20);

        aEFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("fourth1") + "</html>");
        aEFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fourthCard.add(aEFirstLabel);
        aEFirstLabel.setBounds(261, 26, 290, 310);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "add_assignment.png"))); // NOI18N
        fourthCard.add(jLabel10);
        jLabel10.setBounds(10, 120, 130, 150);

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "clone_event.png"))); // NOI18N
        jLabel17.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        fourthCard.add(jLabel17);
        jLabel17.setBounds(100, 220, 164, 111);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "assignment_details.png"))); // NOI18N
        jLabel12.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        fourthCard.add(jLabel12);
        jLabel12.setBounds(40, 10, 220, 166);

        gettingStartedPanel.add(fourthCard, "card5");

        fifthCard.setOpaque(false);
        fifthCard.setLayout(null);

        viewsAndFiltersLabel.setText(viewPanel.domain.language.getString ("viewsAndFilters"));
        fifthCard.add(viewsAndFiltersLabel);
        viewsAndFiltersLabel.setBounds(20, 0, 521, 14);

        vFFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("fifth1") + "</html>");
        vFFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        fifthCard.add(vFFirstLabel);
        vFFirstLabel.setBounds(20, 26, 330, 320);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "list_calendar_view.png"))); // NOI18N
        fifthCard.add(jLabel13);
        jLabel13.setBounds(350, 20, 200, 80);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "filters.png"))); // NOI18N
        fifthCard.add(jLabel14);
        jLabel14.setBounds(330, 130, 220, 50);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "mini_calendar.png"))); // NOI18N
        fifthCard.add(jLabel15);
        jLabel15.setBounds(360, 180, 190, 150);

        gettingStartedPanel.add(fifthCard, "card6");

        sixthCard.setOpaque(false);
        sixthCard.setLayout(null);

        sortingLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sortingLabel.setText(viewPanel.domain.language.getString ("sortingInListView"));
        sixthCard.add(sortingLabel);
        sortingLabel.setBounds(250, 0, 290, 20);

        sFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("sixth1") + "</html>");
        sFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        sixthCard.add(sFirstLabel);
        sFirstLabel.setBounds(250, 30, 290, 300);

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "sorting2.png"))); // NOI18N
        sixthCard.add(jLabel11);
        jLabel11.setBounds(10, 190, 220, 130);

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "sorting.png"))); // NOI18N
        sixthCard.add(jLabel18);
        jLabel18.setBounds(30, 20, 200, 160);

        gettingStartedPanel.add(sixthCard, "card7");

        seventhCard.setOpaque(false);
        seventhCard.setLayout(null);

        gradesGettingStartedLabel.setText(viewPanel.domain.language.getString ("gradeBook"));
        seventhCard.add(gradesGettingStartedLabel);
        gradesGettingStartedLabel.setBounds(20, 0, 290, 14);

        gFirstLabel.setText("<html>" + viewPanel.domain.language.getString ("seventh1") + "</html>");
        gFirstLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        seventhCard.add(gFirstLabel);
        gFirstLabel.setBounds(20, 30, 310, 300);

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "grades2.png"))); // NOI18N
        seventhCard.add(jLabel19);
        jLabel19.setBounds(350, 20, 190, 180);

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "grades1.png"))); // NOI18N
        seventhCard.add(jLabel20);
        jLabel20.setBounds(329, 190, 221, 110);

        gettingStartedPanel.add(seventhCard, "card7");

        aboutPanelLowerPanel.setOpaque(false);

        closeGettingStartedButton.setText(viewPanel.domain.language.getString ("close"));
        closeGettingStartedButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
        closeGettingStartedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeGettingStartedButtonActionPerformed(evt);
            }
        });

        nextGettingStartedButton.setText(viewPanel.domain.language.getString ("next"));
        nextGettingStartedButton.setToolTipText(viewPanel.domain.language.getString ("nextGettingStartedToolTip"));
        nextGettingStartedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextGettingStartedButtonActionPerformed(evt);
            }
        });

        prevGettingStartedButton.setText(viewPanel.domain.language.getString ("previous"));
        prevGettingStartedButton.setToolTipText(viewPanel.domain.language.getString ("previousGettingStartedToolTip"));
        prevGettingStartedButton.setEnabled(false);
        prevGettingStartedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevGettingStartedButtonActionPerformed(evt);
            }
        });

        dontShowCheckBox.setText(viewPanel.domain.language.getString ("dontShowOnStartup"));
        dontShowCheckBox.setOpaque(false);
        dontShowCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dontShowCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aboutPanelLowerPanelLayout = new javax.swing.GroupLayout(aboutPanelLowerPanel);
        aboutPanelLowerPanel.setLayout(aboutPanelLowerPanelLayout);
        aboutPanelLowerPanelLayout.setHorizontalGroup(
            aboutPanelLowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLowerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutPanelLowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aboutPanelLowerPanelLayout.createSequentialGroup()
                        .addComponent(dontShowCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addComponent(prevGettingStartedButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextGettingStartedButton))
                    .addComponent(closeGettingStartedButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        aboutPanelLowerPanelLayout.setVerticalGroup(
            aboutPanelLowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aboutPanelLowerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeGettingStartedButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(aboutPanelLowerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextGettingStartedButton)
                    .addComponent(prevGettingStartedButton)
                    .addComponent(dontShowCheckBox))
                .addContainerGap())
        );

        javax.swing.GroupLayout gettingStartedJPanelLayout = new javax.swing.GroupLayout(gettingStartedJPanel);
        gettingStartedJPanel.setLayout(gettingStartedJPanelLayout);
        gettingStartedJPanelLayout.setHorizontalGroup(
            gettingStartedJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gettingStartedJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gettingStartedJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                    .addComponent(gettingStartedPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, gettingStartedJPanelLayout.createSequentialGroup()
                        .addComponent(getOrganizedLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aboutPanelLowerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gettingStartedJPanelLayout.setVerticalGroup(
            gettingStartedJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gettingStartedJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gettingStartedJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(aboutPanelLowerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(getOrganizedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gettingStartedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gettingStartedJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gettingStartedJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeGettingStartedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_closeGettingStartedButtonActionPerformed
    {//GEN-HEADEREND:event_closeGettingStartedButtonActionPerformed
        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_closeGettingStartedButtonActionPerformed

    private void nextGettingStartedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_nextGettingStartedButtonActionPerformed
    {//GEN-HEADEREND:event_nextGettingStartedButtonActionPerformed
        if (firstCard.isVisible ())
        {
            secondCard.setVisible (true);
            firstCard.setVisible (false);
            prevGettingStartedButton.setEnabled (true);
        }
        else if (secondCard.isVisible ())
        {
            thirdCard.setVisible (true);
            secondCard.setVisible (false);
        }
        else if (thirdCard.isVisible ())
        {
            fourthCard.setVisible (true);
            thirdCard.setVisible (false);
        }
        else if (fourthCard.isVisible ())
        {
            fifthCard.setVisible (true);
            fourthCard.setVisible (false);
        }
        else if (fifthCard.isVisible ())
        {
            sixthCard.setVisible (true);
            fifthCard.setVisible (false);
        }
        else if (sixthCard.isVisible ())
        {
            seventhCard.setVisible (true);
            sixthCard.setVisible (false);
            nextGettingStartedButton.setEnabled (false);
        }
}//GEN-LAST:event_nextGettingStartedButtonActionPerformed

    private void prevGettingStartedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_prevGettingStartedButtonActionPerformed
    {//GEN-HEADEREND:event_prevGettingStartedButtonActionPerformed
        if (secondCard.isVisible ())
        {
            firstCard.setVisible (true);
            secondCard.setVisible (false);
            prevGettingStartedButton.setEnabled (false);
        }
        else if (thirdCard.isVisible ())
        {
            secondCard.setVisible (true);
            thirdCard.setVisible (false);
        }
        else if (fourthCard.isVisible ())
        {
            thirdCard.setVisible (true);
            fourthCard.setVisible (false);
        }
        else if (fifthCard.isVisible ())
        {
            fourthCard.setVisible (true);
            fifthCard.setVisible (false);
        }
        else if (sixthCard.isVisible ())
        {
            fifthCard.setVisible (true);
            sixthCard.setVisible (false);
        }
        else if (seventhCard.isVisible ())
        {
            sixthCard.setVisible (true);
            seventhCard.setVisible (false);
            nextGettingStartedButton.setEnabled (true);
        }
}//GEN-LAST:event_prevGettingStartedButtonActionPerformed

    private void dontShowCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dontShowCheckBoxActionPerformed
    {//GEN-HEADEREND:event_dontShowCheckBoxActionPerformed
        viewPanel.domain.utility.preferences.dontShowGettingStarted = dontShowCheckBox.isSelected ();
        viewPanel.domain.needsPreferencesSave = true;
}//GEN-LAST:event_dontShowCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel aEFirstLabel;
    private javax.swing.JLabel aTACCFirstLabel;
    private javax.swing.JLabel aTACFirstLabel;
    private javax.swing.JPanel aboutPanelLowerPanel;
    private javax.swing.JLabel addingTermsAndCoursesContLabel;
    private javax.swing.JLabel addingTermsAndCoursesLabel;
    private javax.swing.JLabel assignmentsAndEventsLabel;
    private javax.swing.JButton closeGettingStartedButton;
    protected javax.swing.JCheckBox dontShowCheckBox;
    private javax.swing.JPanel fifthCard;
    private javax.swing.JPanel firstCard;
    private javax.swing.JPanel fourthCard;
    private javax.swing.JLabel gFirstLabel;
    private javax.swing.JLabel gSATCFirstLabel;
    private javax.swing.JLabel gSATFirstLabel;
    private javax.swing.JLabel getOrganizedLabel;
    protected adl.go.gui.ColoredJPanel gettingStartedJPanel;
    private javax.swing.JPanel gettingStartedPanel;
    private javax.swing.JLabel gradesGettingStartedLabel;
    private javax.swing.JLabel gradingScaleAndTextbooksContLabel;
    private javax.swing.JLabel gradingScaleAndTextbooksLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JButton nextGettingStartedButton;
    private javax.swing.JButton prevGettingStartedButton;
    private javax.swing.JLabel sFirstLabel;
    private javax.swing.JPanel secondCard;
    private javax.swing.JPanel seventhCard;
    private javax.swing.JPanel sixthCard;
    private javax.swing.JLabel sortingLabel;
    private javax.swing.JPanel thirdCard;
    private javax.swing.JLabel vFFirstLabel;
    private javax.swing.JLabel viewsAndFiltersLabel;
    private javax.swing.JLabel wFirstLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Construct the Getting Started dialog to show the first page properly.
     */
    protected void showGettingStartedDialog()
    {
        dontShowCheckBox.setSelected (viewPanel.domain.utility.preferences.dontShowGettingStarted);

        firstCard.setVisible (true);
        secondCard.setVisible (false);
        thirdCard.setVisible (false);
        fourthCard.setVisible (false);
        fifthCard.setVisible (false);
        sixthCard.setVisible (false);
        seventhCard.setVisible (false);

        prevGettingStartedButton.setEnabled (false);
        nextGettingStartedButton.setEnabled (true);

        pack ();
        setLocationRelativeTo (viewPanel);
        setVisible (true);

        nextGettingStartedButton.requestFocus ();
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