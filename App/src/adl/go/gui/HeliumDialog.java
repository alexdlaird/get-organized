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
import java.awt.Color;
import java.awt.Cursor;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

/**
 * The Helium dialog.
 *
 * @author Alex Laird
 */
public class HeliumDialog extends EscapeDialog
{
    /**
     * Construct the Helium dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public HeliumDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);
        initComponents ();
    }

    /**
     * Initialize the Helium dialog.
     */
    public void init()
    {
        setTitle ("Switch to Helium!");
        heliumJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorSingleWindowBackground1);
        heliumCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        heliumCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        heliumIntroLabel1.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        hyperlinkLabel1.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        heliumIntroLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        heliumIntroLabel2.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        dontShowCheckBox.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        heliumImg.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        heliumIntroLabel.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
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

        heliumJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        closeButtonPanel = new javax.swing.JPanel();
        heliumCloseButton = new javax.swing.JButton();
        dontShowCheckBox = new javax.swing.JCheckBox();
        hyperlinkLabel1 = new javax.swing.JLabel();
        heliumImg = new javax.swing.JLabel();
        heliumLabel = new javax.swing.JLabel();
        heliumIntroLabel = new javax.swing.JLabel();
        heliumImg1 = new javax.swing.JLabel();
        heliumIntroLabel1 = new javax.swing.JLabel();
        heliumIntroLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        closeButtonPanel.setOpaque(false);

        heliumCloseButton.setText("Close");
        heliumCloseButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                heliumCloseButtonActionPerformed(evt);
            }
        });
        closeButtonPanel.add(heliumCloseButton);

        dontShowCheckBox.setText("Don't show this dialog on startup");
        dontShowCheckBox.setOpaque(false);
        dontShowCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                dontShowCheckBoxActionPerformed(evt);
            }
        });
        closeButtonPanel.add(dontShowCheckBox);

        hyperlinkLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        hyperlinkLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/helium.png"))); // NOI18N
        hyperlinkLabel1.setText("<html><a href=\"http://www.heliumedu.com\">Get Organized has been replaced with Helium. Switch today!</a></html>");
        hyperlinkLabel1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                hyperlinkLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                hyperlinkLabel1MouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt)
            {
                hyperlinkLabel1MouseReleased(evt);
            }
        });

        heliumImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/helium_calendar.png"))); // NOI18N

        heliumLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        heliumLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heliumLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/helium_logo.png"))); // NOI18N

        heliumIntroLabel.setText("<html>A brand new school year means it's time for a new take on student success and organization, so we're pleased to announce Helium&#8212;an innovative revamp replacing Get Organized.<br /><br />Helium is your ticket to the top of the class, with every tool you'll need for easier schedule management, more flexible assignment organization, due date notifications, and improved full-scope grade tracking in a sleek, easy-to-use, online format.</html>");
        heliumIntroLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        heliumImg1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adl/go/images/helium_grades.png"))); // NOI18N

        heliumIntroLabel1.setText("<html>Looking to Get Organized? Try Helium Instead!</html>");

        heliumIntroLabel2.setText("<html>Now web-based and mobile-friendly, Helium is Get Organized taken to the next level, with the goal of taking your success as a student to the next level with it.  And don't worry, your entire Get Organized schedule can be easily imported into Helium. Switch today!</html>");

        javax.swing.GroupLayout heliumJPanelLayout = new javax.swing.GroupLayout(heliumJPanel);
        heliumJPanel.setLayout(heliumJPanelLayout);
        heliumJPanelLayout.setHorizontalGroup(
            heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(heliumJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heliumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                    .addComponent(closeButtonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                    .addComponent(hyperlinkLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, heliumJPanelLayout.createSequentialGroup()
                        .addGroup(heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(heliumIntroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(heliumIntroLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(heliumImg1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(heliumImg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(heliumIntroLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        heliumJPanelLayout.setVerticalGroup(
            heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(heliumJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(heliumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(heliumJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(heliumJPanelLayout.createSequentialGroup()
                        .addComponent(heliumIntroLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(heliumIntroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(heliumJPanelLayout.createSequentialGroup()
                        .addComponent(heliumImg, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heliumImg1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heliumIntroLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hyperlinkLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButtonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(heliumJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(heliumJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void heliumCloseButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_heliumCloseButtonActionPerformed
    {//GEN-HEADEREND:event_heliumCloseButtonActionPerformed
        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_heliumCloseButtonActionPerformed

    private void hyperlinkLabel1MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_hyperlinkLabel1MouseReleased
    {//GEN-HEADEREND:event_hyperlinkLabel1MouseReleased
        if (Domain.desktop != null)
        {
            try
            {
                Domain.desktop.browse (new URI ("https://www.heliumedu.com"));
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
}//GEN-LAST:event_hyperlinkLabel1MouseReleased

    private void hyperlinkLabel1MouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_hyperlinkLabel1MouseEntered
    {//GEN-HEADEREND:event_hyperlinkLabel1MouseEntered
        setCursor (Cursor.getPredefinedCursor (Cursor.HAND_CURSOR));
    }//GEN-LAST:event_hyperlinkLabel1MouseEntered

    private void hyperlinkLabel1MouseExited(java.awt.event.MouseEvent evt)//GEN-FIRST:event_hyperlinkLabel1MouseExited
    {//GEN-HEADEREND:event_hyperlinkLabel1MouseExited
        setCursor (Cursor.getDefaultCursor());
    }//GEN-LAST:event_hyperlinkLabel1MouseExited

    private void dontShowCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_dontShowCheckBoxActionPerformed
    {//GEN-HEADEREND:event_dontShowCheckBoxActionPerformed
        viewPanel.domain.utility.preferences.dontShowHelium = dontShowCheckBox.isSelected ();
        viewPanel.domain.needsPreferencesSave = true;
    }//GEN-LAST:event_dontShowCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel closeButtonPanel;
    protected javax.swing.JCheckBox dontShowCheckBox;
    private javax.swing.JButton heliumCloseButton;
    private javax.swing.JLabel heliumImg;
    private javax.swing.JLabel heliumImg1;
    private javax.swing.JLabel heliumIntroLabel;
    private javax.swing.JLabel heliumIntroLabel1;
    private javax.swing.JLabel heliumIntroLabel2;
    protected adl.go.gui.ColoredJPanel heliumJPanel;
    private javax.swing.JLabel heliumLabel;
    private javax.swing.JLabel hyperlinkLabel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Launch the Helium dialog.
     */
    protected void goViewHelium()
    {
        dontShowCheckBox.setSelected (viewPanel.domain.utility.preferences.dontShowGettingStarted);
        
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
