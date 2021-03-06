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

import com.apple.eawt.Application;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The updater for the Get Organized application.
 *
 * @author Alex Laird
 */
public class GetOrganizedUpdater extends JFrame
{
    public static final String DEFAULT_TITLE = "Get Organized Updater";
    /** The name of the operating system being used.*/
    public static final String OS_NAME = System.getProperty("os.name");
    /** The unformatted path location of the updater.*/
    public static final String UNFORMATTED_PATH = GetOrganizedUpdater.class.getProtectionDomain().getCodeSource().getLocation().toString().replaceAll("%20", " ");
    /** The formatted path for the location of the updater and application.*/
    public static String formattedPath;
    /** A reference to the desktop model (if supported) for launching files and application.*/
    public static Desktop desktop = Desktop.getDesktop();
    /** The option pane which can be customized to have yes/no, ok/cancel, or just ok buttons in it.*/
    public static final JOptionPane OPTION_PANE = new JOptionPane();
    /** The OK button for the option dialog.*/
    public static final JButton OK_OPTION_BUTTON = new JButton("Ok");
    /** The object which contains only the ok button for the option pane.*/
    public static final Object[] OK_CHOICE = new Object[]
    {
        OK_OPTION_BUTTON
    };

    /**
     * Creates new frame for the application.
     */
    public GetOrganizedUpdater()
    {
        if (OS_NAME.toLowerCase().contains("mac"))
        {
            Application macApp = Application.getApplication();
            macApp.setDockIconImage(new ImageIcon(getClass().getResource("/images/go.png")).getImage());
        }

        UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
        UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
        UIManager.put("ProgressBar.foreground", new Color(185, 185, 185));

        formattedPath = UNFORMATTED_PATH.substring(UNFORMATTED_PATH.indexOf(":") + 2, UNFORMATTED_PATH.lastIndexOf("/"));
        if (!OS_NAME.toLowerCase().contains("windows"))
        {
            formattedPath = "/" + formattedPath;
        }

        initComponents();
        initMyComponents();

        setTitle (DEFAULT_TITLE);
    }

    /**
     * 
     */
    private void initMyComponents()
    {
        setLocationRelativeTo(null);
        updatesProgressBar.setMinimum(0);
        updatesProgressBar.setMaximum(100);
        updatesProgressBar.setValue(0);
        updatesProgressBar.setString("");
        statusLabel.setText("Initializing updater ...");
        updatesProgressBar.setStringPainted(true);

        OK_OPTION_BUTTON.setBackground(new Color(246, 245, 245));
        OK_OPTION_BUTTON.setFont(new Font("Verdana", Font.PLAIN, 13));
        OK_OPTION_BUTTON.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                OPTION_PANE.setValue(new Integer(JOptionPane.OK_OPTION));
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        updatesProgressBar = new javax.swing.JProgressBar();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Get Organized Updater");
        setIconImage(new ImageIcon (getClass ().getResource ("/images/go.png")).getImage ());
        setResizable(false);

        updatesProgressBar.setFont(new java.awt.Font("Verdana", 0, 12));
        updatesProgressBar.setStringPainted(true);
        updatesProgressBar.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                updatesProgressBarAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        statusLabel.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        statusLabel.setText("<html>Status Label</html>");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, statusLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, updatesProgressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(updatesProgressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusLabel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updatesProgressBarAncestorAdded(javax.swing.event.AncestorEvent evt)//GEN-FIRST:event_updatesProgressBarAncestorAdded
    {//GEN-HEADEREND:event_updatesProgressBarAncestorAdded
        final JFrame frame = this;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    statusLabel.setText("Connecting to update server ...");
                    updatesProgressBar.setIndeterminate(true);

                    String[] files = new File(formattedPath, "..").list();
                    String fileName = "";
                    for (int i = 0; i < files.length; ++i)
                    {
                        if ((files[i].endsWith(".jar") || files[i].endsWith(".exe"))
                            && files[i].toLowerCase().replaceAll(" ", "").contains("getorganized"))
                        {
                            fileName = files[i];
                        }
                    }

                    double size = -1;
                    try
                    {
                        URL url = new URL("http://updates.alexlaird.com/get-organized/version.txt");
                        URLConnection conn = url.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        // throw away the version number line
                        in.readLine();
                        if (formattedPath.replaceAll(" ", "").toLowerCase().contains("getorganizedportable")
                            || fileName.replaceAll(" ", "").toLowerCase().contains("getorganizedportable"))
                        {
                            String line = in.readLine();
                            String value = line;
                            if (line.contains("="))
                            {
                                String[] split = line.split("=");
                                value = split[1];

                            }
                            size = Double.parseDouble(value);
                        }
                        else if (OS_NAME.toLowerCase().contains("windows"))
                        {
                            in.readLine();
                            String line = in.readLine();
                            String value = line;
                            if (line.contains("="))
                            {
                                String[] split = line.split("=");
                                value = split[1];

                            }
                            size = Double.parseDouble(value);
                        }
                        else if (OS_NAME.toLowerCase().contains("mac"))
                        {
                            in.readLine();
                            in.readLine();
                            String line = in.readLine();
                            String value = line;
                            if (line.contains("="))
                            {
                                String[] split = line.split("=");
                                value = split[1];

                            }
                            size = Double.parseDouble(value);
                        }
                    }
                    catch (Exception ex)
                    {
                        // if any exception occurs, just go get the update
                    }

                    updatesProgressBar.setIndeterminate(false);
                    updatesProgressBar.setString("0%");
                    setTitle (DEFAULT_TITLE + " (0%)");
                    statusLabel.setText("Downloading update ...");

                    // download the latest update over the current Get Organized application
                    File updateFile = null;
                    File releaseNotesFile = null;
                    File licenseFile = null;
                    InputStream in = null;
                    InputStream releaseNotesIn = null;
                    InputStream licenseIn = null;
                    if (formattedPath.replaceAll(" ", "").toLowerCase().contains("getorganizedportable")
                        || fileName.replaceAll(" ", "").toLowerCase().contains("getorganizedportable"))
                    {
                        updateFile = new File(formattedPath, "Update.temp");
                        File oldLicenseFile = new File(formattedPath, "../license.txt");
                        if (oldLicenseFile.exists())
                        {
                            oldLicenseFile.delete();
                        }
                        File oldReleaseNotesFile = new File(formattedPath, "../releasenotes.rtf");
                        if (oldReleaseNotesFile.exists())
                        {
                            oldReleaseNotesFile.delete();
                        }
                        releaseNotesFile = new File(formattedPath, "../releasenotes.html");
                        licenseFile = new File(formattedPath, "../license.html");
                        if (fileName.endsWith(".exe"))
                        {
                            URL url = new URL("http://updates.alexlaird.com/get-organized/GetOrganizedPortable.exe");
                            url.openConnection();
                            in = url.openStream();
                        }
                        else
                        {
                            URL url = new URL("http://updates.alexlaird.com/get-organized/GetOrganizedPortable.jar");
                            url.openConnection();
                            in = url.openStream();
                        }
                        
                        URL url = new URL("http://updates.alexlaird.com/get-organized/releasenotes.html");
                        url.openConnection();
                        releaseNotesIn = url.openStream();
                        url = new URL("http://updates.alexlaird.com/get-organized/license.html");
                        url.openConnection();
                        licenseIn = url.openStream();
                    }
                    else if (OS_NAME.toLowerCase().contains("windows"))
                    {
                        updateFile = new File(formattedPath, "GetOrganizedSetup.exe");
                        URL url = new URL("http://updates.alexlaird.com/get-organized/GetOrganizedSetup.exe");
                        url.openConnection();
                        in = url.openStream();
                    }
                    else if (OS_NAME.toLowerCase().contains("mac"))
                    {
                        updateFile = new File(formattedPath, "GetOrganizedSetup.pkg.zip");
                        URL url = new URL("http://updates.alexlaird.com/get-organized/GetOrganizedSetup.pkg.zip");
                        url.openConnection();
                        in = url.openStream();
                    }

                    if (updateFile != null)
                    {
                        if (updateFile.exists())
                        {
                            updateFile.delete();
                        }

                        FileOutputStream out = new FileOutputStream(updateFile);

                        byte[] buffer = new byte[1024];
                        int bytes = 0;
                        int totalBytes = 0;
                        
                        while ((bytes = in.read (buffer)) > 0)
                        {
                            out.write(buffer, 0, bytes);
                            buffer = new byte[1024];
                            
                            if (size != -1)
                            {
                                int value = (int) ((totalBytes / size) * 100);
                                updatesProgressBar.setValue(value);
                                updatesProgressBar.setString(value + "%");
                                if (value < 100)
                                {
                                    setTitle(DEFAULT_TITLE + " (" + value + "%)");
                                }
                                else
                                {
                                    setTitle(DEFAULT_TITLE + " (100%)");
                                }
                            }
                            
                            totalBytes += bytes;
                        }

                        in.close();
                        out.close();


                        updatesProgressBar.setValue(0);
                        updatesProgressBar.setString("");
                        setTitle (DEFAULT_TITLE);

                        if (releaseNotesFile != null)
                        {
                            updatesProgressBar.setIndeterminate(true);
                            statusLabel.setText ("Downloading latest release notes ...");

                            if (releaseNotesFile.exists())
                            {
                                releaseNotesFile.delete();
                            }

                            out = new FileOutputStream(releaseNotesFile);

                            // download all bytes of the updater
                            bytes = 0;
                            while ((bytes = releaseNotesIn.read (buffer)) > 0)
                            {
                                out.write(buffer, 0, bytes);
                                buffer = new byte[153600];
                            }

                            releaseNotesIn.close();
                            out.close();

                            statusLabel.setText ("Downloading latest license ...");

                            if (licenseFile.exists())
                            {
                                licenseFile.delete();
                            }

                            out = new FileOutputStream(licenseFile);

                            // download all bytes of the updater
                            bytes = 0;
                            while ((bytes = releaseNotesIn.read (buffer)) > 0)
                            {
                                out.write(buffer, 0, bytes);
                                buffer = new byte[153600];
                            }

                            releaseNotesIn.close();
                            out.close();
                        }
                        else
                        {
                            updatesProgressBar.setIndeterminate(true);
                        }

                        if (formattedPath.replaceAll(" ", "").toLowerCase().contains("getorganizedportable")
                            || fileName.replaceAll(" ", "").toLowerCase().contains("getorganizedportable"))
                        {
                            statusLabel.setText("Updating Get Organized ...");
                            File file = new File(formattedPath, "../" + fileName);
                            try
                            {
                                deleteDir (file);

                                // copy the new one in its place
                                copy(updateFile, file);

                                if (file.exists())
                                {
                                    updateFile.delete();
                                }
                                
                                file = new File (file.getCanonicalPath());

                                if (file.getCanonicalPath().endsWith(".exe"))
                                {
                                    if (desktop != null)
                                    {
                                        if (OS_NAME.toLowerCase().contains("windows"))
                                        {
                                            // Since we're on Windows, straggling processes get left sometimes, so kill them
                                            Process p = Runtime.getRuntime().exec("tasklist");
                                            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                            String procLine;
                                            while ((procLine = reader.readLine()) != null)
                                            {
                                                if (procLine.contains("Get Organized Portable.exe"))
                                                {
                                                    Runtime.getRuntime().exec("taskkill /im \"Get Organized Portable.exe\" /f");
                                                }
                                                if (procLine.contains("GetOrganizedPortable.exe"))
                                                {
                                                    Runtime.getRuntime().exec("taskkill /im \"GetOrganizedPortable.exe\" /f");
                                                }
                                                if (procLine.contains("GetOrganized Portable.exe"))
                                                {
                                                    Runtime.getRuntime().exec("taskkill /im \"GetOrganized Portable.exe\" /f");
                                                }
                                            }
                                        }
                                        desktop.open(file);
                                    }
                                    else
                                    {
                                        updatesProgressBar.setIndeterminate(false);
                                        OPTION_PANE.setOptions(OK_CHOICE);
                                        OPTION_PANE.setMessage("Get Organized was successfully updated."
                                                               + file.getCanonicalPath());
                                        OPTION_PANE.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                                        JDialog optionDialog = OPTION_PANE.createDialog(frame, "Updated Successfully");
                                        optionDialog.setVisible(true);
                                    }
                                }
                                else
                                {
                                    Runtime.getRuntime().exec(new String[]
                                            {
                                                "java", "-jar", file.getCanonicalPath()
                                            }, null, null);
                                }
                            }
                            catch (IOException ex)
                            {
                                updatesProgressBar.setIndeterminate(false);
                                OPTION_PANE.setOptions(OK_CHOICE);
                                OPTION_PANE.setMessage("Get Organized was successfully updated."
                                                       + file.getCanonicalPath());
                                OPTION_PANE.setMessageType(JOptionPane.INFORMATION_MESSAGE);
                                JDialog optionDialog = OPTION_PANE.createDialog(frame, "Updated Successfully");
                                optionDialog.setVisible(true);
                            }
                            catch (Exception ex)
                            {
                                updatesProgressBar.setIndeterminate(false);
                                OPTION_PANE.setOptions(OK_CHOICE);
                                OPTION_PANE.setMessage("The update was downloaded but could not be renamed and\n"
                                                       + "moved into place. To apply the update, copy the Get Organized file located at\n"
                                                       + updateFile.getCanonicalPath() + "\n"
                                                       + "and use it to overwrite the existing application at\n"
                                                       + file.getCanonicalPath() + "\n"
                                                       + "More simply, the update can be installed automatically\n"
                                                       + "from alexlaird.com/projects/get-organized.");
                                OPTION_PANE.setMessageType(JOptionPane.ERROR_MESSAGE);
                                JDialog optionDialog = OPTION_PANE.createDialog(frame, "Unable to Update Automatically");
                                optionDialog.setVisible(true);
                            }
                        }
                        else if (OS_NAME.toLowerCase().contains("windows")
                                 || OS_NAME.toLowerCase().contains("mac"))
                        {
                            statusLabel.setText("Launching Get Organized setup ...");
                            if (OS_NAME.toLowerCase().contains("windows"))
                            {
                                // Since we're on Windows, straggling processes get left sometimes, so kill them
                                Process p = Runtime.getRuntime().exec("tasklist");
                                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                String procLine;
                                while ((procLine = reader.readLine()) != null)
                                {
                                    if (procLine.contains("Get Organized.exe"))
                                    {
                                        Runtime.getRuntime().exec("taskkill /im \"Get Organized.exe\" /f");
                                    }
                                    if (procLine.contains("GetOrganized.exe"))
                                    {
                                        Runtime.getRuntime().exec("taskkill /im \"GetOrganized.exe\" /f");
                                    }
                                }
                            }

                            if (desktop != null)
                            {
                                if (OS_NAME.toLowerCase().contains("mac"))
                                {
                                    ZipFile zipFile = new ZipFile(updateFile);
                                    Enumeration entries = zipFile.entries();

                                    while (entries.hasMoreElements())
                                    {
                                        ZipEntry entry = (ZipEntry) entries.nextElement();
                                        if (entry.isDirectory())
                                        {
                                            // This is not robust, just for demonstration purposes.
                                            (new File(formattedPath, entry.getName())).mkdir();
                                            continue;
                                        }

                                        copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(new File(formattedPath, entry.getName()))));
                                    }

                                    zipFile.close();
                                    updateFile.delete();

                                    updateFile = new File(formattedPath, "GetOrganizedSetup.pkg");
                                }
                                desktop.open(updateFile);
                            }
                            else
                            {
                                updatesProgressBar.setIndeterminate(false);
                                OPTION_PANE.setOptions(OK_CHOICE);
                                OPTION_PANE.setMessage("The update was downloaded but the installer could not\n"
                                                       + "be launched. Please launch the installer manually from the\n"
                                                       + "following location:\n"
                                                       + updateFile.getCanonicalPath());
                                OPTION_PANE.setMessageType(JOptionPane.ERROR_MESSAGE);
                                JDialog optionDialog = OPTION_PANE.createDialog(frame, "Unable to Update Automatically");
                                optionDialog.setVisible(true);
                            }
                        }
                    }
                    else
                    {
                        updatesProgressBar.setIndeterminate(false);
                        OPTION_PANE.setOptions(OK_CHOICE);
                        OPTION_PANE.setMessage("The update cannot be downloaded automatically for this\n"
                                               + "system. Please download the update manually from\n"
                                               + "alexlaird.com/projects/get-organized.");
                        OPTION_PANE.setMessageType(JOptionPane.ERROR_MESSAGE);
                        JDialog optionDialog = OPTION_PANE.createDialog(frame, "Unable to Update Automatically");
                        optionDialog.setVisible(true);
                        if (desktop != null)
                        {
                            try
                            {
                                desktop.browse(new URI("http://alexlaird.com/projects/get-organized"));
                            }
                            catch (Exception ex)
                            {
                            }
                        }
                    }

                    System.exit(0);
                }
                catch (Exception ex)
                {
                    updatesProgressBar.setIndeterminate(false);
                    OPTION_PANE.setOptions(OK_CHOICE);
                    OPTION_PANE.setMessage("An unknown error has occured. Try updating the application manually\n"
                                           + "by going to alexlaird.com/projects/get-organized.");
                    OPTION_PANE.setMessageType(JOptionPane.ERROR_MESSAGE);
                    JDialog optionDialog = OPTION_PANE.createDialog(frame, "Unable to Update Automatically");
                    optionDialog.setVisible(true);
                    if (desktop != null)
                    {
                        try
                        {
                            desktop.browse(new URI("http://alexlaird.com/projects/get-organized"));
                        }
                        catch (Exception innerEx)
                        {
                        }
                    }
                    System.exit(0);
                }
            }
        }).start();
    }//GEN-LAST:event_updatesProgressBarAncestorAdded

    /**
     * Remove contents of a directory.
     * 
     * @param dir The directory to be deleted.
     * @return True if the directory was deleted, false otherwise.
     */
    public static boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    /**
     * Copy the given file to the new file.
     *
     * @param fromFile The file to be copied from.
     * @param toFile The file to be copied to.
     * @throws IOException If any exception occurs.
     */
    private void copy(File fromFile, File toFile) throws IOException
    {
        FileInputStream from = new FileInputStream(fromFile);
        FileOutputStream to = new FileOutputStream(toFile);
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = from.read(buffer)) != -1)
        {
            to.write(buffer, 0, bytesRead);
        }

        from.close();
        to.close();
    }

    /**
     * Copy data from the input stream to the output stream.
     *
     * @param in The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException
     */
    public static void copyInputStream(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int len;

        while ((len = in.read(buffer)) >= 0)
        {
            out.write(buffer, 0, len);
        }

        in.close();
        out.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        // define UI characteristics before the applicaiton is instantiated
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (ClassNotFoundException ex)
        {
        }
        catch (InstantiationException ex)
        {
        }
        catch (IllegalAccessException ex)
        {
        }
        catch (UnsupportedLookAndFeelException ex)
        {
        }

        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new GetOrganizedUpdater().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel statusLabel;
    private javax.swing.JProgressBar updatesProgressBar;
    // End of variables declaration//GEN-END:variables
}
