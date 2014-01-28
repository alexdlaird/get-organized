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
import adl.go.types.Course;
import adl.go.types.Term;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * The Grades dialog.
 *
 * @author Alex Laird
 */
public class GradesDialog extends EscapeDialog
{
    /**
     * The model for the terms displayed in the combo box in the Grades dialog.
     */
    public DefaultComboBoxModel gradesTermsComboModel = new DefaultComboBoxModel ();
    /**
     * The model for the courses displayed in the combo box in the Grades dialog.
     */
    public DefaultComboBoxModel gradesCoursesComboModel = new DefaultComboBoxModel ();

    /**
     * Construct the Grades dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public GradesDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);
        initComponents ();
    }

    /**
     * Initialize the Grades dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("grades"));
        gradesBottomJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        gradesTopJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowTopBackground1);

        ((TitledBorder) courseSummaryPanelOutter.getBorder ()).setTitleFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradesCoursesComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        gradesCoursesComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradesLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        gradesTermsComboBox.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        gradesTermsComboBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        avgGradeLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        gradesCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        gradesCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gradesJPanel = new javax.swing.JPanel();
        gradesBottomJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        gradesCoursesComboBox = new javax.swing.JComboBox();
        graphPanel = new GradesGraphPanel();
        courseGradesDetails = new javax.swing.JPanel();
        courseGradesScrollPane = new javax.swing.JScrollPane();
        courseGradesListPanel = new javax.swing.JPanel();
        courseSummaryPanelOutter = new javax.swing.JPanel();
        courseSummaryScrollPane = new javax.swing.JScrollPane();
        courseSummaryPanel = new javax.swing.JPanel();
        gradesTopJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        gradesLabel = new javax.swing.JLabel();
        gradesTermsComboBox = new javax.swing.JComboBox();
        avgGradeLabel = new javax.swing.JLabel();
        gradesCloseButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        gradesBottomJPanel.setPreferredSize(new java.awt.Dimension(970, 425));

        gradesCoursesComboBox.setModel(gradesCoursesComboModel);
        gradesCoursesComboBox.setToolTipText(viewPanel.domain.language.getString ("courseDisplayGradesFor"));
        gradesCoursesComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                gradesCoursesComboBoxItemStateChanged(evt);
            }
        });

        graphPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        graphPanel.setOpaque(false);

        javax.swing.GroupLayout graphPanelLayout = new javax.swing.GroupLayout(graphPanel);
        graphPanel.setLayout(graphPanelLayout);
        graphPanelLayout.setHorizontalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );
        graphPanelLayout.setVerticalGroup(
            graphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
        );

        courseGradesDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        courseGradesDetails.setOpaque(false);

        courseGradesScrollPane.setOpaque(false);

        courseGradesListPanel.setOpaque(false);
        courseGradesListPanel.setLayout(new javax.swing.BoxLayout(courseGradesListPanel, javax.swing.BoxLayout.PAGE_AXIS));
        courseGradesScrollPane.setViewportView(courseGradesListPanel);

        javax.swing.GroupLayout courseGradesDetailsLayout = new javax.swing.GroupLayout(courseGradesDetails);
        courseGradesDetails.setLayout(courseGradesDetailsLayout);
        courseGradesDetailsLayout.setHorizontalGroup(
            courseGradesDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(courseGradesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        courseGradesDetailsLayout.setVerticalGroup(
            courseGradesDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(courseGradesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
        );

        courseSummaryPanelOutter.setBorder(javax.swing.BorderFactory.createTitledBorder(viewPanel.domain.language.getString ("courseSummary")));
        courseSummaryPanelOutter.setOpaque(false);

        courseSummaryScrollPane.setBorder(null);
        courseSummaryScrollPane.setOpaque(false);

        courseSummaryPanel.setOpaque(false);
        courseSummaryPanel.setLayout(new java.awt.GridLayout(4, 1));
        courseSummaryScrollPane.setViewportView(courseSummaryPanel);

        javax.swing.GroupLayout courseSummaryPanelOutterLayout = new javax.swing.GroupLayout(courseSummaryPanelOutter);
        courseSummaryPanelOutter.setLayout(courseSummaryPanelOutterLayout);
        courseSummaryPanelOutterLayout.setHorizontalGroup(
            courseSummaryPanelOutterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(courseSummaryScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
        );
        courseSummaryPanelOutterLayout.setVerticalGroup(
            courseSummaryPanelOutterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(courseSummaryScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout gradesBottomJPanelLayout = new javax.swing.GroupLayout(gradesBottomJPanel);
        gradesBottomJPanel.setLayout(gradesBottomJPanelLayout);
        gradesBottomJPanelLayout.setHorizontalGroup(
            gradesBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesBottomJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradesBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(courseSummaryPanelOutter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(gradesCoursesComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, gradesBottomJPanelLayout.createSequentialGroup()
                        .addComponent(courseGradesDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        gradesBottomJPanelLayout.setVerticalGroup(
            gradesBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesBottomJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradesBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(graphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(courseGradesDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gradesCoursesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(courseSummaryPanelOutter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        gradesTopJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gradesTopJPanel.setPreferredSize(new java.awt.Dimension(970, 49));

        gradesLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/grades_mini.png"))); // NOI18N
        gradesLabel.setText(viewPanel.domain.language.getString ("grades"));

        gradesTermsComboBox.setModel(gradesTermsComboModel);
        gradesTermsComboBox.setToolTipText(viewPanel.domain.language.getString ("termDisplayGradesFor"));
        gradesTermsComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                gradesTermsComboBoxItemStateChanged(evt);
            }
        });

        avgGradeLabel.setText("Average grade: <<Term Grade>>");

        gradesCloseButton.setText(viewPanel.domain.language.getString ("close"));
        gradesCloseButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
        gradesCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradesCloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout gradesTopJPanelLayout = new javax.swing.GroupLayout(gradesTopJPanel);
        gradesTopJPanel.setLayout(gradesTopJPanelLayout);
        gradesTopJPanelLayout.setHorizontalGroup(
            gradesTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradesTopJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gradesLabel)
                .addGap(18, 18, 18)
                .addComponent(gradesTermsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(avgGradeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 466, Short.MAX_VALUE)
                .addComponent(gradesCloseButton)
                .addContainerGap())
        );
        gradesTopJPanelLayout.setVerticalGroup(
            gradesTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradesTopJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(gradesTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gradesLabel)
                    .addComponent(gradesTermsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgGradeLabel)
                    .addComponent(gradesCloseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout gradesJPanelLayout = new javax.swing.GroupLayout(gradesJPanel);
        gradesJPanel.setLayout(gradesJPanelLayout);
        gradesJPanelLayout.setHorizontalGroup(
            gradesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradesTopJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
            .addComponent(gradesBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
        );
        gradesJPanelLayout.setVerticalGroup(
            gradesJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradesJPanelLayout.createSequentialGroup()
                .addComponent(gradesTopJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gradesBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradesJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void gradesCoursesComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_gradesCoursesComboBoxItemStateChanged
        if (gradesTermsComboBox.getSelectedIndex () != -1
            && viewPanel.domain.utility.terms.size () > 0
            && viewPanel.domain.utility.terms.get (gradesTermsComboBox.getSelectedIndex ()).getCourseCount () > 0
            && viewPanel.domain.utility.courses.size () > 0
            && gradesTermsComboBox.getSelectedIndex () != -1)
        {
            if (gradesCoursesComboBox.getSelectedIndex () != -1)
            {
                Course course = viewPanel.domain.utility.terms.get (gradesTermsComboBox.getSelectedIndex ()).getCourse (gradesCoursesComboBox.getSelectedIndex ());
                courseSummaryPanel.removeAll ();
                courseSummaryPanel.setLayout (new GridLayout (4, 4));
                for (int i = 0; i < course.getTypeCount (); i += 4)
                {
                    if (i > 4)
                    {
                        GridLayout layout = (GridLayout) courseSummaryPanel.getLayout ();
                        layout.setRows (layout.getRows () + 2);
                    }

                    JPanel rowPanel = new JPanel (new GridLayout (1, 4));
                    rowPanel.setOpaque (false);
                    courseSummaryPanel.add (rowPanel);

                    JLabel first = new JLabel (course.getType (i).getTypeName () + " (" + course.getType (i).getWeight () + ")");
                    first.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
                    rowPanel.add (first);
                    if (i + 1 < course.getTypeCount ())
                    {
                        JLabel second = new JLabel (course.getType (i + 1).getTypeName () + " (" + course.getType (i + 1).getWeight () + ")");
                        second.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
                        rowPanel.add (second);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }
                    if (i + 2 < course.getTypeCount ())
                    {
                        JLabel third = new JLabel (course.getType (i + 2).getTypeName () + " (" + course.getType (i + 2).getWeight () + ")");
                        third.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
                        rowPanel.add (third);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }
                    if (i + 3 < course.getTypeCount ())
                    {
                        JLabel fourth = new JLabel (course.getType (i + 3).getTypeName () + " (" + course.getType (i + 3).getWeight () + ")");
                        fourth.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
                        rowPanel.add (fourth);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }

                    rowPanel = new JPanel (new GridLayout (1, 4));
                    rowPanel.setOpaque (false);
                    courseSummaryPanel.add (rowPanel);

                    double grade = course.getType (i).getGrade () / 100;
                    String gradeString = viewPanel.domain.language.getString ("notApplicableAbbrev");
                    if (grade >= 0)
                    {
                        gradeString = Domain.PERCENT_FORMAT.format (grade);
                    }
                    JLabel fifth = new JLabel ("  " + gradeString);
                    fifth.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                    rowPanel.add (fifth);
                    if (i + 1 < course.getTypeCount ())
                    {
                        grade = course.getType (i + 1).getGrade () / 100;
                        gradeString = viewPanel.domain.language.getString ("notApplicableAbbrev");
                        if (grade >= 0)
                        {
                            gradeString = Domain.PERCENT_FORMAT.format (grade);
                        }
                        JLabel sixth = new JLabel ("  " + gradeString);
                        sixth.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                        rowPanel.add (sixth);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }
                    if (i + 2 < course.getTypeCount ())
                    {
                        grade = course.getType (i + 2).getGrade () / 100;
                        gradeString = viewPanel.domain.language.getString ("notApplicableAbbrev");
                        if (grade >= 0)
                        {
                            gradeString = Domain.PERCENT_FORMAT.format (grade);
                        }
                        JLabel seventh = new JLabel ("  " + gradeString);
                        seventh.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                        rowPanel.add (seventh);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }
                    if (i + 3 < course.getTypeCount ())
                    {
                        grade = course.getType (i + 3).getGrade () / 100;
                        gradeString = viewPanel.domain.language.getString ("notApplicableAbbrev");
                        if (grade >= 0)
                        {
                            gradeString = Domain.PERCENT_FORMAT.format (grade);
                        }
                        JLabel eigth = new JLabel ("  " + gradeString);
                        eigth.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                        rowPanel.add (eigth);
                    }
                    else
                    {
                        rowPanel.add (new JLabel (""));
                    }
                }
                if (course.getTypeCount () == 0)
                {
                    JPanel rowPanel = new JPanel (new GridLayout (1, 4));
                    rowPanel.setOpaque (false);
                    courseSummaryPanel.add (rowPanel);
                    rowPanel = new JPanel (new GridLayout (1, 4));
                    rowPanel.setOpaque (false);
                    courseSummaryPanel.add (rowPanel);
                    JLabel label = new JLabel (viewPanel.domain.language.getString ("noTypesAssocWithCourse"));
                    label.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                    rowPanel.add (label);
                    rowPanel = new JPanel (new GridLayout (1, 4));
                    rowPanel.setOpaque (false);
                    courseSummaryPanel.add (rowPanel);
                    JButton button = new JButton (viewPanel.domain.language.getString ("editGradingScale") + "...");
                    button.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                    button.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
                    final Course passCourse = course;
                    button.addActionListener (new ActionListener ()
                    {
                        @Override
                        public void actionPerformed(ActionEvent evt)
                        {
                            dispose ();
                            viewPanel.goEditTypesFromGrades (passCourse);
                        }
                    });
                    rowPanel.add (button);
                    rowPanel.add (new JLabel (""));
                    rowPanel.add (new JLabel (""));
                    rowPanel.add (new JLabel (""));
                }
                courseSummaryPanel.invalidate ();
                courseSummaryPanel.revalidate ();
                courseSummaryPanel.repaint ();
            }
        }
        else
        {
            courseSummaryPanel.removeAll ();
            courseSummaryPanel.invalidate ();
            courseSummaryPanel.revalidate ();
            courseSummaryPanel.repaint ();
        }
}//GEN-LAST:event_gradesCoursesComboBoxItemStateChanged

    private void gradesTermsComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_gradesTermsComboBoxItemStateChanged
        if (viewPanel.domain.utility.terms.size () > 0 && !viewPanel.gradesLoading)
        {
            graphPanel.setEnabled (true);

            if (gradesTermsComboBox.getSelectedIndex () != -1)
            {
                // load the table
                Term term = viewPanel.domain.utility.terms.get (gradesTermsComboBox.getSelectedIndex ());
                courseGradesListPanel.removeAll ();
                courseSummaryPanel.removeAll ();
                gradesCoursesComboModel.removeAllElements ();
                for (int i = 0; i < term.getCourseCount (); ++i)
                {
                    final Course course = viewPanel.domain.utility.courses.get (viewPanel.domain.utility.getCourseIndex (term.getCourse (i)));
                    gradesCoursesComboModel.addElement (course.getTypeName ());
                    JLabel first = new JLabel (course.getTypeName ());
                    first.addMouseListener (new MouseAdapter ()
                    {
                        @Override
                        public void mouseEntered(MouseEvent evt)
                        {
                            setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
                        }

                        @Override
                        public void mouseExited(MouseEvent evt)
                        {
                            setCursor (Cursor.getDefaultCursor ());
                        }

                        @Override
                        public void mouseReleased(MouseEvent evt)
                        {
                            gradesCoursesComboBox.setSelectedIndex (viewPanel.domain.utility.terms.get (gradesTermsComboBox.getSelectedIndex ()).getCourseIndex (course));
                        }
                    });
                    first.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
                    first.setForeground (course.getColor ());
                    double grade = viewPanel.domain.calculateGradeForCourse (course) / 100;
                    JLabel second = new JLabel ("   " + viewPanel.domain.language.getString ("currentGrade") + ": " + (grade == -0.01 ? viewPanel.domain.language.getString ("notApplicableAbbrev") : Domain.PERCENT_FORMAT.format (grade)));
                    second.addMouseListener (new MouseAdapter ()
                    {
                        @Override
                        public void mouseReleased(MouseEvent evt)
                        {
                            gradesCoursesComboBox.setSelectedIndex (viewPanel.domain.utility.terms.get (gradesTermsComboBox.getSelectedIndex ()).getCourseIndex (course));
                        }
                    });
                    second.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
                    courseGradesListPanel.add (first);
                    courseGradesListPanel.add (second);
                    JLabel space = new JLabel ("__");
                    space.setFont (new Font ("Verdana", Font.PLAIN, 5));
                    courseGradesListPanel.add (space);
                    space.setForeground (courseGradesListPanel.getBackground ());
                }

                // load the graph
                graphPanel.setTerm (term);
                graphPanel.invalidate ();

                if (gradesCoursesComboModel.getSize () == 0)
                {
                    gradesCoursesComboModel.addElement ("-" + viewPanel.domain.language.getString ("none") + "-");
                }
                gradesCoursesComboBox.setSelectedIndex (0);

                double grade = viewPanel.domain.calculateGradeForTerm (term) / 100;
                avgGradeLabel.setText (viewPanel.domain.language.getString ("averageGrade") + ": " + (grade == -0.01 ? viewPanel.domain.language.getString ("notApplicableAbbrev") : Domain.PERCENT_FORMAT.format (grade)));
            }
        }
        else
        {
            avgGradeLabel.setText ("");
            gradesCoursesComboModel.removeAllElements ();
            gradesCoursesComboModel.addElement ("-" + viewPanel.domain.language.getString ("none") + "-");
            gradesCoursesComboBox.setSelectedIndex (0);
            courseGradesListPanel.removeAll ();
            graphPanel.setEnabled (false);
            graphPanel.invalidate ();
        }
}//GEN-LAST:event_gradesTermsComboBoxItemStateChanged

    private void gradesCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradesCloseButtonActionPerformed
        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_gradesCloseButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avgGradeLabel;
    private javax.swing.JPanel courseGradesDetails;
    private javax.swing.JPanel courseGradesListPanel;
    protected javax.swing.JScrollPane courseGradesScrollPane;
    private javax.swing.JPanel courseSummaryPanel;
    private javax.swing.JPanel courseSummaryPanelOutter;
    protected javax.swing.JScrollPane courseSummaryScrollPane;
    protected adl.go.gui.ColoredJPanel gradesBottomJPanel;
    private javax.swing.JButton gradesCloseButton;
    protected javax.swing.JComboBox gradesCoursesComboBox;
    protected javax.swing.JPanel gradesJPanel;
    private javax.swing.JLabel gradesLabel;
    protected javax.swing.JComboBox gradesTermsComboBox;
    protected adl.go.gui.ColoredJPanel gradesTopJPanel;
    public adl.go.gui.GradesGraphPanel graphPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Launches the grades dialog, loading the dialogs term combo box with all
     * available terms.
     */
    protected void goViewGrades()
    {
        if (viewPanel.settingsDialog.isVisible ())
        {
            viewPanel.settingsDialog.closeSettingsDialog ();
        }
        if (viewPanel.termsAndCoursesDialog.isVisible ())
        {
            viewPanel.termsAndCoursesDialog.closeTermsAndCoursesDialog ();
        }

        viewPanel.gradesLoading = true;
        viewPanel.domain.utility.loadGradesTermCombo ();
        viewPanel.gradesLoading = false;

        if (viewPanel.getSelectedTermIndex () == -1)
        {
            if (graphPanel.getTerm () == null)
            {
                gradesTermsComboBox.setSelectedIndex (0);
                gradesTermsComboBoxItemStateChanged (null);
            }
            else
            {
                gradesTermsComboBox.setSelectedIndex (viewPanel.domain.utility.getTermIndex (graphPanel.getTerm ()));
                gradesTermsComboBoxItemStateChanged (null);
            }
        }
        else
        {
            gradesTermsComboBox.setSelectedIndex (viewPanel.getSelectedTermIndex ());
            gradesTermsComboBoxItemStateChanged (null);
        }

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