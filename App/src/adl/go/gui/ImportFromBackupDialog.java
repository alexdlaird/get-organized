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
import adl.go.types.Course;
import adl.go.types.Term;
import ca.ansir.swing.tristate.TriState;
import ca.ansir.swing.tristate.TriStateTreeCellRenderer;
import ca.ansir.swing.tristate.TriStateTreeHandler;
import ca.ansir.swing.tristate.TriStateTreeNode;
import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * The Import from Backup dialog.
 *
 * @author Alex Laird
 */
public class ImportFromBackupDialog extends EscapeDialog
{
    /**
     * The root node of the import tree.
     */
    DefaultMutableTreeNode root = new TriStateTreeNode ("root");
    /**
     * The list of terms to import.
     */
    ArrayList<Term> terms = new ArrayList<Term> ();
    /**
     * The list of courses to import.
     */
    ArrayList<Course> courses = new ArrayList<Course> ();
    /**
     * The file to import from.
     */
    private File file;
    /**
     * The buffered reader to read from the file.
     */
    private BufferedReader in;
    /**
     * True if success, false otherwise.
     */
    private boolean success = false;
    /**
     * True if a fatal error occurred, false otherwise.
     */
    private boolean fatal = false;
    /**
     * True if cancel was pressed, false otherwise.
     */
    protected boolean cancel = false;

    /**
     * Construct the Import dialog.
     *
     * @param viewPanel A reference to the view panel.
     */
    public ImportFromBackupDialog(ViewPanel viewPanel)
    {
        super (viewPanel.mainFrame);
        setMainPanel (viewPanel);
        initComponents ();
    }

    /**
     * Initialize the Import dialog.
     */
    public void init()
    {
        setTitle (viewPanel.domain.language.getString ("importFromBackup"));
        importFromBackupPanel.setBackground (viewPanel.domain.utility.currentTheme.colorSingleWindowBackground1);
        importButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        importCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        importBottomJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowBottomBackground1);
        importTopJPanel.setBackground (viewPanel.domain.utility.currentTheme.colorDoubleWindowTopBackground1);

        importCloseButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        importCloseButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        importButton.setBackground (viewPanel.domain.utility.currentTheme.colorButtonBackground);
        importButton.setFont (viewPanel.domain.utility.currentTheme.fontPlain12);
        importFromBackupLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold14);
        importSummaryLabel.setFont (viewPanel.domain.utility.currentTheme.fontBold12);
        availableImportTree.setFont (viewPanel.domain.utility.currentTheme.fontBold14);

        // Construct the tree so it can have tri-state checkboxes
        availableImportScrollPane.getViewport ().setOpaque (false);
        TriStateTreeHandler triStateTreeHandler = new TriStateTreeHandler (availableImportTree);
        availableImportTree.setShowsRootHandles (true);

        availableImportTree.addTreeExpansionListener (new TreeExpansionListener ()
        {
            @Override
            public void treeExpanded(TreeExpansionEvent evt)
            {
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent evt)
            {
                expandImportTree (evt);
            }
        });
    }

    /**
     * Expands all possible nodes in the term tree.
     *
     * @param e The firing event.
     */
    private void expandImportTree(TreeExpansionEvent e)
    {
        for (int row = 0; row < availableImportTree.getRowCount (); ++row)
        {
            availableImportTree.expandRow (row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings ("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importFromBackupPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        importBottomJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.NO_GRADIENT, Color.WHITE);
        importSummaryLabel = new javax.swing.JLabel();
        availableImportScrollPane = new javax.swing.JScrollPane();
        availableImportTree = new javax.swing.JTree(root);
        importTopJPanel = new adl.go.gui.ColoredJPanel(GradientStyle.VERTICAL_GRADIENT_DOWN, Color.WHITE);
        importCloseButton = new javax.swing.JButton();
        importButton = new javax.swing.JButton();
        importFromBackupLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);

        importSummaryLabel.setText(viewPanel.domain.language.getString ("nothingToImportText"));

        availableImportScrollPane.setOpaque(false);

        availableImportTree.setCellRenderer(new TriStateTreeCellRenderer ()
            {
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
            availableImportTree.setOpaque(false);
            availableImportTree.setRootVisible(false);
            availableImportScrollPane.setViewportView(availableImportTree);

            javax.swing.GroupLayout importBottomJPanelLayout = new javax.swing.GroupLayout(importBottomJPanel);
            importBottomJPanel.setLayout(importBottomJPanelLayout);
            importBottomJPanelLayout.setHorizontalGroup(
                importBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, importBottomJPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(importBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(availableImportScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addComponent(importSummaryLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                    .addContainerGap())
            );
            importBottomJPanelLayout.setVerticalGroup(
                importBottomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, importBottomJPanelLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addComponent(importSummaryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(availableImportScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addGap(12, 12, 12))
            );

            importTopJPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            importCloseButton.setText(viewPanel.domain.language.getString ("close"));
            importCloseButton.setToolTipText(viewPanel.domain.language.getString ("closeToolTip"));
            importCloseButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    importCloseButtonActionPerformed(evt);
                }
            });

            importButton.setText(viewPanel.domain.language.getString ("import"));
            importButton.setToolTipText(viewPanel.domain.language.getString ("importButtonToolTip"));
            importButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    importButtonActionPerformed(evt);
                }
            });

            importFromBackupLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(Domain.IMAGES_FOLDER + "go.png"))); // NOI18N
            importFromBackupLabel.setText(viewPanel.domain.language.getString ("importFromBackup"));

            javax.swing.GroupLayout importTopJPanelLayout = new javax.swing.GroupLayout(importTopJPanel);
            importTopJPanel.setLayout(importTopJPanelLayout);
            importTopJPanelLayout.setHorizontalGroup(
                importTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(importTopJPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(importFromBackupLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(importButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importCloseButton)
                    .addContainerGap())
            );
            importTopJPanelLayout.setVerticalGroup(
                importTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(importTopJPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(importTopJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(importFromBackupLabel)
                        .addComponent(importButton)
                        .addComponent(importCloseButton))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout importFromBackupPanelLayout = new javax.swing.GroupLayout(importFromBackupPanel);
            importFromBackupPanel.setLayout(importFromBackupPanelLayout);
            importFromBackupPanelLayout.setHorizontalGroup(
                importFromBackupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(importTopJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(importBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            importFromBackupPanelLayout.setVerticalGroup(
                importFromBackupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(importFromBackupPanelLayout.createSequentialGroup()
                    .addComponent(importTopJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(importBottomJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(importFromBackupPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(importFromBackupPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void importCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCloseButtonActionPerformed
        cancel = true;
        try
        {
            in.close ();
        }
        catch (IOException ex)
        {
        }
        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_importCloseButtonActionPerformed

    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        cancel = false;
        try
        {
            viewPanel.loadingPanel.setVisible (true);
            viewPanel.contentPanel.setVisible (false);

            setCursor (Cursor.getPredefinedCursor (Cursor.WAIT_CURSOR));
            viewPanel.domain.setProgressState (viewPanel.progressBar, true, viewPanel.domain.language.getString ("importingToGetOrganized"), true, -1);
            viewPanel.loadingLabel.setText (viewPanel.domain.language.getString ("importingToGetOrganized") + " ...");

            viewPanel.initLoading = true;
            viewPanel.ignoreTableSelection = true;

            viewPanel.assignmentsTable.setSelectedRow (-1);
            viewPanel.assignmentsTableRowSelected (null);
            viewPanel.termsAndCoursesDialog.settingsTermsTable.setSelectedRow (-1);
            viewPanel.termsAndCoursesDialog.settingsTermsTableRowSelected (null);
            viewPanel.termsAndCoursesDialog.settingsCoursesTable.setSelectedRow (-1);
            viewPanel.termsAndCoursesDialog.settingsCoursesTableRowSelected (null);
            viewPanel.domain.currentCourseIndex = -1;
            viewPanel.domain.currentTermIndex = -1;
            viewPanel.domain.currentTextbookIndex = -1;
            viewPanel.domain.currentTypeIndex = -1;
            viewPanel.domain.currentInstructorIndex = -1;
            viewPanel.eventChanges.clear ();
            viewPanel.repeatEventChanges = false;

            viewPanel.settingsDialog.settingsTabbedPane.setSelectedIndex (0);
            viewPanel.termsAndCoursesDialog.termsAndCoursesTabbedPane.setSelectedIndex (0);
            viewPanel.domain.workerThread.setAllowSave (false);

            int courseIndex = 0;
            int termIndex = 0;
            for (int i = 0; i < root.getChildCount (); ++i)
            {
                TriStateTreeNode termNode = (TriStateTreeNode) root.getChildAt (i);
                if (termNode.getState () == TriState.UNSELECTED)
                {
                    terms.remove (termIndex);
                    for (int j = 0; j < termNode.getChildCount (); ++j)
                    {
                        courses.remove (courseIndex);
                    }
                }
                else if (termNode.getState () == TriState.MIXED)
                {
                    ++termIndex;
                    for (int j = 0; j < termNode.getChildCount (); ++j)
                    {
                        TriStateTreeNode courseNode = (TriStateTreeNode) termNode.getChildAt (j);
                        if (courseNode.getState () == TriState.SELECTED)
                        {
                            ++courseIndex;
                        }
                        else
                        {
                            courses.remove (courseIndex);
                        }
                    }
                }
                else
                {
                    ++termIndex;
                    courseIndex += termNode.getChildCount ();
                }
            }
            // remove duplicates (if the term or course already exists in the user's schedule)
            for (int i = 0; i < courses.size (); ++i)
            {
                if (viewPanel.domain.utility.getByID (courses.get (i).getUniqueID ()) != null)
                {
                    courses.remove (i);
                    --i;
                }
            }
            for (int i = 0; i < terms.size (); ++i)
            {
                if (viewPanel.domain.utility.getByID (terms.get (i).getUniqueID ()) != null)
                {
                    if (courses.indexOf (terms.get (i)) != -1)
                    {
                        terms.remove (i);
                        --i;
                    }
                }
            }

            if (terms.size () > 0 && courses.size () > 0)
            {
                success = viewPanel.domain.utility.importFromBackup (in, terms, courses);

                if (success)
                {
                }
            }
            else
            {
                cancel = true;
            }
        }
        catch (Exception ex)
        {
            success = false;
            fatal = true;
        }
        finally
        {
            setCursor (Cursor.getDefaultCursor ());
            try
            {
                in.close ();
            }
            catch (IOException ex)
            {
            }
        }

        dispose ();
        viewPanel.requestFocus ();
}//GEN-LAST:event_importButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane availableImportScrollPane;
    private javax.swing.JTree availableImportTree;
    protected adl.go.gui.ColoredJPanel importBottomJPanel;
    private javax.swing.JButton importButton;
    private javax.swing.JButton importCloseButton;
    private javax.swing.JLabel importFromBackupLabel;
    protected adl.go.gui.ColoredJPanel importFromBackupPanel;
    private javax.swing.JLabel importSummaryLabel;
    protected adl.go.gui.ColoredJPanel importTopJPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Parse the input file and add terms and courses to the import dialog.
     *
     * @param file The input file to parse.
     */
    private void parseInputFile(File file) throws FileNotFoundException, IOException
    {
        // clear out old terms and courses
        root.removeAllChildren ();

        in = new BufferedReader (new FileReader (file));

        // throw out title line
        in.readLine ();
        // throw out preferences line
        in.readLine ();
        // throw out title line
        in.readLine ();
        // throw out categories line
        in.readLine ();

        // throw out blank line
        in.readLine ();
        // throw out title line
        in.readLine ();
        // read all terms
        String line = in.readLine ();
        terms.clear ();
        while (!line.equals (""))
        {
            Term term = new Term (line, viewPanel.domain.utility);
            terms.add (term);
            TriStateTreeNode termNode = new TriStateTreeNode (term.getTypeName ());
            root.add (termNode);

            line = in.readLine ();
        }

        // throw out title line
        in.readLine ();
        // read all courses
        line = in.readLine ();
        courses.clear ();
        while (!line.equals (""))
        {
            Course course = new Course (line, viewPanel.domain.utility);
            courses.add (course);

            // retrieve the index of the node to add this to
            int index = -1;
            for (int i = 0; i < terms.size (); ++i)
            {
                if (terms.get (i).getUniqueID () == course.getTermID ())
                {
                    index = i;
                }
            }

            //int index = viewPanel.domain.utility.getTermIndex (terms, course.getTermID());
            TriStateTreeNode courseNode = new TriStateTreeNode (course.getTypeName ());
            TriStateTreeNode termNode = (TriStateTreeNode) root.getChildAt (index);
            termNode.add (courseNode);

            line = in.readLine ();
        }

        // refresh the shown elements
        ((DefaultTreeModel) availableImportTree.getModel ()).reload ();

        expandImportTree (new TreeExpansionEvent (this, null));

        availableImportTree.setCellRenderer (new TriStateTreeCellRenderer ()
        {
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
    }

    /**
     * Launch the dialog.
     */
    protected void goViewImportFromBackup()
    {
        viewPanel.closeOpenWindows ();
        viewPanel.checkAssignmentOrEventChanges (viewPanel.domain.currentIndexFromVector);
        viewPanel.checkRepeatEventChanges (viewPanel.domain.currentIndexFromVector);

        cancel = false;

        viewPanel.fileChooser.setDialogType (JFileChooser.OPEN_DIALOG);
        viewPanel.fileChooser.setApproveButtonText (viewPanel.domain.language.getString ("import"));
        viewPanel.fileChooser.setApproveButtonToolTipText (viewPanel.domain.language.getString ("importFromBackupToolip"));
        viewPanel.fileChooser.setDialogTitle (viewPanel.domain.language.getString ("importFromBackup"));
        File selectedFile = new File (viewPanel.lastGoodFile);
        viewPanel.fileChooser.setSelectedFile (selectedFile);
        int response = viewPanel.fileChooser.showOpenDialog (viewPanel);

        // prompt the user to select a backup file to import from
        while (response == JFileChooser.APPROVE_OPTION)
        {
            file = viewPanel.fileChooser.getSelectedFile ();
            if (!file.isFile () || !selectedFile.toString ().endsWith (".gbak"))
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("mustBeValidExtension") + " (.gbak).");
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("invalidExtension"));
                optionDialog.setVisible (true);
                viewPanel.fileChooser.setSelectedFile (new File (viewPanel.fileChooser.getSelectedFile ().toString ().substring (0, viewPanel.fileChooser.getSelectedFile ().toString ().lastIndexOf (".")) + ".gbak"));
                response = viewPanel.fileChooser.showOpenDialog (this);
                continue;
            }
            try
            {
                viewPanel.lastGoodFile = file.getCanonicalPath ();
            }
            catch (IOException ex)
            {
            }

            try
            {
                parseInputFile (file);
            }
            catch (FileNotFoundException ex)
            {
            }
            catch (IOException ex)
            {
                cancel = true;
            }

            if (root.getChildCount () == 0)
            {
                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("nothingToImportText"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("nothingToImportTitle"));
                optionDialog.setVisible (true);
                return;
            }
            else
            {
                importSummaryLabel.setText ("<html>" + viewPanel.domain.language.getString ("importSummaryText") + "</html>");
                importButton.setEnabled (true);
            }

            pack ();
            setLocationRelativeTo (viewPanel);
            setVisible (true);

            viewPanel.domain.setProgressState (viewPanel.progressBar, false, "", false, -1);
            viewPanel.loadingLabel.setText (viewPanel.domain.language.getString ("loading") + "...");

            if (cancel)
            {
                setCursor (Cursor.getDefaultCursor ());
                viewPanel.contentPanel.setVisible (true);
                viewPanel.loadingPanel.setVisible (false);

                viewPanel.initLoading = false;
                viewPanel.domain.workerThread.setAllowSave (true);
            }
            else if (!success && !fatal)
            {
                setCursor (Cursor.getDefaultCursor ());

                viewPanel.contentPanel.setVisible (true);
                viewPanel.loadingPanel.setVisible (false);

                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("invalidRestoreFile"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                JDialog innerOptionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("noChangesMade"));
                innerOptionDialog.setVisible (true);

                viewPanel.initLoading = false;
                viewPanel.domain.workerThread.setAllowSave (true);
            }
            else if (success && !fatal)
            {
                viewPanel.termTree.getSelectionModel ().setSelectionPath (null);
                viewPanel.domain.utility.loadTermTree ();
                viewPanel.domain.utility.loadAssignmentsTable (true);
                viewPanel.findTermWithin ();

                viewPanel.filter (true);

                viewPanel.expandTermTree (new TreeExpansionEvent (this, null));
                viewPanel.refreshBusyDays ();
                viewPanel.initButtons ();
                viewPanel.scrollToItemOrToday (null);

                viewPanel.contentPanel.setVisible (true);
                viewPanel.loadingPanel.setVisible (false);

                setCursor (Cursor.getDefaultCursor ());

                viewPanel.initLoading = false;
                viewPanel.ignoreTableSelection = false;
                for (int i = 0; i < viewPanel.domain.utility.courses.size (); ++i)
                {
                    viewPanel.domain.utility.courses.get (i).markChanged ();
                }
                for (int i = 0; i < viewPanel.domain.utility.eventYears.size (); ++i)
                {
                    viewPanel.domain.utility.eventYears.get (i).markChanged ();
                }
                viewPanel.domain.needsCoursesAndTermsSave = true;
                viewPanel.domain.needsPreferencesSave = true;
                viewPanel.domain.needsSettingsSaveBool = true;

                viewPanel.domain.workerThread.setAllowSave (true);
            }
            else
            {
                setCursor (Cursor.getDefaultCursor ());

                viewPanel.contentPanel.setVisible (true);
                viewPanel.loadingPanel.setVisible (false);

                viewPanel.initLoading = false;
                viewPanel.ignoreTableSelection = false;
                viewPanel.domain.setProgressState (viewPanel.progressBar, false, "", false, -1);

                viewPanel.domain.workerThread.setAllowSave (true);

                ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("aFatalErrorHasOccurred"));
                ViewPanel.OPTION_PANE.setMessageType (JOptionPane.ERROR_MESSAGE);
                JDialog innerOptionDialog = ViewPanel.OPTION_PANE.createDialog (this, viewPanel.domain.language.getString ("fatalError"));
                innerOptionDialog.setVisible (true);

                viewPanel.quit (true);
            }

            break;
        }
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
