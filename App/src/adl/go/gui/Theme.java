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

import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * A theme object contains all coloring and font colors and styles that for a
 * given theme. The current theme object has references within this object for
 * every component that requires coloring.
 *
 * @author Alex Laird
 */
public class Theme
{
    /**
     * The name of the current theme. Theme names must be unique.
     */
    public String name = "Default";
    /**
     * A reference to the view panel.
     */
    private ViewPanel viewPanel;
    /**
     * Default font, plain, 10.
     */
    public Font fontPlain10 = new Font ("Verdana", Font.PLAIN, 10);
    /**
     * Default font, plain, 11.
     */
    public Font fontPlain11 = new Font ("Verdana", Font.PLAIN, 11);
    /**
     * Default font, plain, 12.
     */
    public Font fontPlain12 = new Font ("Verdana", Font.PLAIN, 12);
    /**
     * Default font, bold, 12.
     */
    public Font fontBold12 = new Font ("Verdana", Font.BOLD, 12);
    /**
     * Default font, bold, 14.
     */
    public Font fontBold14 = new Font ("Verdana", Font.BOLD, 14);
    /**
     * Default font, bold, 18.
     */
    public Font fontBold18 = new Font ("Verdana", Font.BOLD, 18);
    /**
     * Default font, italic, 11.
     */
    public Font fontItalic11 = new Font ("Verdana", Font.ITALIC, 11);
    /**
     * Default font, bold, 11.
     */
    public Font fontBold11 = new Font ("Verdana", Font.BOLD, 11);
    public Color colorButtonBackground = new Color (245, 245, 245);
    public Color colorDayInMonthBackground1 = new Color (245, 245, 245);
    public Color colorDayInMonthBackground2 = new Color (215, 215, 215);
    public Color colorDayInMonthText = Color.BLACK;
    public Color colorDayNotInMonthBackground1 = new Color (204, 237, 255);
    public Color colorDayNotInMonthBackground2 = new Color (174, 207, 225);
    public Color colorDayNotInMonthText = new Color (120, 120, 120);
    public Color colorTodayText = new Color (0, 175, 0);
    public Color colorLeftBackground1Panel = new Color (245, 245, 245);
    public Color colorLeftBackground2Panel = new Color (215, 215, 215);
    public Color colorRightBackground1Panel = new Color (245, 245, 245);
    public Color colorRightBackground2Panel = new Color (215, 215, 215);
    public Color colorTopBackground1Panel = new Color (245, 245, 245);
    public Color colorTopBackground2Panel = new Color (215, 215, 215);
    public Color colorBottomBackground1Panel = new Color (245, 245, 245);
    public Color colorBottomBackground2Panel = new Color (215, 215, 215);
    public Color colorLoadingMiddleBackground1Panel = new Color (245, 245, 245);
    public Color colorLoadingMiddleBackground2Panel = new Color (215, 215, 215);
    public Color colorMiddleBackground1Panel = new Color (245, 245, 245);
    public Color colorMiddleBackground2Panel = new Color (215, 215, 215);
    public Color colorMiddleCalendarBackgroundPanel1 = new Color (245, 245, 245);
    public Color colorMiddleCalendarBackgroundPanel2 = new Color (215, 215, 215);
    public Color colorSingleWindowBackground1 = new Color (245, 245, 245);
    public Color colorSingleWindowBackground2 = new Color (215, 215, 215);
    public Color colorDoubleWindowTopBackground1 = new Color (245, 245, 245);
    public Color colorDoubleWindowTopBackground2 = new Color (215, 215, 215);
    public Color colorDoubleWindowBottomBackground1 = new Color (245, 245, 245);
    public Color colorDoubleWindowBottomBackground2 = new Color (245, 245, 245);
    public Color colorMiniCalDayNotSelectedOutsideMonthBackground = new Color (204, 237, 255);
    public Color colorMiniCalDayNotSelectedBackground = new Color (245, 245, 245);
    public Color colorMiniCalDaySelectedBackground = new Color (160, 160, 160);
    public Color colorMiniCalWeekBackground = new Color (210, 228, 238);
    public Color colorMiniCalWeekForeground = new Color (0, 0, 165);
    public Color colorMiniCalSundayForeground = new Color (165, 0, 0);
    public Color colorEven = new Color (237, 240, 242);
    public Color colorOdd = new Color (255, 255, 255);
    public Color colorSelected = new Color (184, 207, 229);
    public Color colorDoneDayInMonth = Color.BLUE;
    public Color colorDoneDayOutsideMonth = new Color (90, 90, 255);
    public Color colorBusyDayInMonth = Color.RED;
    public Color colorBusyDayOutsideMonth = new Color (255, 90, 90);

    /**
     * Construct a new theme with the given name.
     *
     * @param name The name to set for the new theme.
     * @param viewPanel A reference to the view panel.
     */
    public Theme(String name, ViewPanel viewPanel)
    {
        this.name = name;
        this.viewPanel = viewPanel;
    }

    /**
     * Construct a new theme with the given data.
     *
     * @param viewPanel A reference to the view panel.
     * @param data The data to set for the new theme.
     */
    public Theme(ViewPanel viewPanel, String data)
    {
        String[] split = data.split (",");
        name = split[0];
        colorButtonBackground = new Color (Integer.parseInt (split[1].split ("-")[0]), Integer.parseInt (split[1].split ("-")[1]), Integer.parseInt (split[1].split ("-")[2]));
        colorDayInMonthBackground1 = new Color (Integer.parseInt (split[2].split ("-")[0]), Integer.parseInt (split[2].split ("-")[1]), Integer.parseInt (split[2].split ("-")[2]));
        colorDayInMonthBackground2 = new Color (Integer.parseInt (split[3].split ("-")[0]), Integer.parseInt (split[3].split ("-")[1]), Integer.parseInt (split[3].split ("-")[2]));
        colorDayInMonthText = new Color (Integer.parseInt (split[4].split ("-")[0]), Integer.parseInt (split[4].split ("-")[1]), Integer.parseInt (split[4].split ("-")[2]));
        colorDayNotInMonthBackground1 = new Color (Integer.parseInt (split[5].split ("-")[0]), Integer.parseInt (split[5].split ("-")[1]), Integer.parseInt (split[5].split ("-")[2]));
        colorDayNotInMonthBackground2 = new Color (Integer.parseInt (split[6].split ("-")[0]), Integer.parseInt (split[6].split ("-")[1]), Integer.parseInt (split[6].split ("-")[2]));
        colorDayNotInMonthText = new Color (Integer.parseInt (split[7].split ("-")[0]), Integer.parseInt (split[7].split ("-")[1]), Integer.parseInt (split[7].split ("-")[2]));
        colorTodayText = new Color (Integer.parseInt (split[8].split ("-")[0]), Integer.parseInt (split[8].split ("-")[1]), Integer.parseInt (split[8].split ("-")[2]));
        colorLeftBackground1Panel = new Color (Integer.parseInt (split[9].split ("-")[0]), Integer.parseInt (split[9].split ("-")[1]), Integer.parseInt (split[9].split ("-")[2]));
        colorLeftBackground2Panel = new Color (Integer.parseInt (split[10].split ("-")[0]), Integer.parseInt (split[10].split ("-")[1]), Integer.parseInt (split[10].split ("-")[2]));
        colorRightBackground1Panel = new Color (Integer.parseInt (split[11].split ("-")[0]), Integer.parseInt (split[11].split ("-")[1]), Integer.parseInt (split[11].split ("-")[2]));
        colorRightBackground2Panel = new Color (Integer.parseInt (split[12].split ("-")[0]), Integer.parseInt (split[12].split ("-")[1]), Integer.parseInt (split[12].split ("-")[2]));
        colorTopBackground1Panel = new Color (Integer.parseInt (split[13].split ("-")[0]), Integer.parseInt (split[13].split ("-")[1]), Integer.parseInt (split[13].split ("-")[2]));
        colorTopBackground2Panel = new Color (Integer.parseInt (split[14].split ("-")[0]), Integer.parseInt (split[14].split ("-")[1]), Integer.parseInt (split[14].split ("-")[2]));
        colorBottomBackground1Panel = new Color (Integer.parseInt (split[15].split ("-")[0]), Integer.parseInt (split[15].split ("-")[1]), Integer.parseInt (split[15].split ("-")[2]));
        colorBottomBackground2Panel = new Color (Integer.parseInt (split[16].split ("-")[0]), Integer.parseInt (split[16].split ("-")[1]), Integer.parseInt (split[16].split ("-")[2]));
        colorLoadingMiddleBackground1Panel = new Color (Integer.parseInt (split[17].split ("-")[0]), Integer.parseInt (split[17].split ("-")[1]), Integer.parseInt (split[17].split ("-")[2]));
        colorLoadingMiddleBackground2Panel = new Color (Integer.parseInt (split[18].split ("-")[0]), Integer.parseInt (split[18].split ("-")[1]), Integer.parseInt (split[18].split ("-")[2]));
        colorMiddleBackground1Panel = new Color (Integer.parseInt (split[19].split ("-")[0]), Integer.parseInt (split[19].split ("-")[1]), Integer.parseInt (split[19].split ("-")[2]));
        colorMiddleBackground2Panel = new Color (Integer.parseInt (split[20].split ("-")[0]), Integer.parseInt (split[20].split ("-")[1]), Integer.parseInt (split[20].split ("-")[2]));
        colorSingleWindowBackground1 = new Color (Integer.parseInt (split[21].split ("-")[0]), Integer.parseInt (split[21].split ("-")[1]), Integer.parseInt (split[21].split ("-")[2]));
        colorSingleWindowBackground2 = new Color (Integer.parseInt (split[22].split ("-")[0]), Integer.parseInt (split[22].split ("-")[1]), Integer.parseInt (split[22].split ("-")[2]));
        colorDoubleWindowTopBackground1 = new Color (Integer.parseInt (split[23].split ("-")[0]), Integer.parseInt (split[23].split ("-")[1]), Integer.parseInt (split[23].split ("-")[2]));
        colorDoubleWindowTopBackground2 = new Color (Integer.parseInt (split[24].split ("-")[0]), Integer.parseInt (split[24].split ("-")[1]), Integer.parseInt (split[24].split ("-")[2]));
        colorDoubleWindowBottomBackground1 = new Color (Integer.parseInt (split[25].split ("-")[0]), Integer.parseInt (split[25].split ("-")[1]), Integer.parseInt (split[25].split ("-")[2]));
        colorDoubleWindowBottomBackground2 = new Color (Integer.parseInt (split[26].split ("-")[0]), Integer.parseInt (split[26].split ("-")[1]), Integer.parseInt (split[26].split ("-")[2]));

        colorMiniCalDayNotSelectedOutsideMonthBackground = new Color (Integer.parseInt (split[27].split ("-")[0]), Integer.parseInt (split[27].split ("-")[1]), Integer.parseInt (split[27].split ("-")[2]));
        colorMiniCalDayNotSelectedBackground = new Color (Integer.parseInt (split[28].split ("-")[0]), Integer.parseInt (split[28].split ("-")[1]), Integer.parseInt (split[28].split ("-")[2]));
        colorMiniCalDaySelectedBackground = new Color (Integer.parseInt (split[29].split ("-")[0]), Integer.parseInt (split[29].split ("-")[1]), Integer.parseInt (split[29].split ("-")[2]));
        colorMiniCalWeekBackground = new Color (Integer.parseInt (split[30].split ("-")[0]), Integer.parseInt (split[30].split ("-")[1]), Integer.parseInt (split[30].split ("-")[2]));
        colorMiniCalWeekForeground = new Color (Integer.parseInt (split[31].split ("-")[0]), Integer.parseInt (split[31].split ("-")[1]), Integer.parseInt (split[31].split ("-")[2]));
        colorMiniCalSundayForeground = new Color (Integer.parseInt (split[32].split ("-")[0]), Integer.parseInt (split[32].split ("-")[1]), Integer.parseInt (split[32].split ("-")[2]));
        colorEven = new Color (Integer.parseInt (split[33].split ("-")[0]), Integer.parseInt (split[33].split ("-")[1]), Integer.parseInt (split[33].split ("-")[2]));
        colorOdd = new Color (Integer.parseInt (split[34].split ("-")[0]), Integer.parseInt (split[34].split ("-")[1]), Integer.parseInt (split[34].split ("-")[2]));
        colorSelected = new Color (Integer.parseInt (split[35].split ("-")[0]), Integer.parseInt (split[35].split ("-")[1]), Integer.parseInt (split[35].split ("-")[2]));
        colorDoneDayInMonth = new Color (Integer.parseInt (split[36].split ("-")[0]), Integer.parseInt (split[36].split ("-")[1]), Integer.parseInt (split[36].split ("-")[2]));
        colorDoneDayOutsideMonth = new Color (Integer.parseInt (split[37].split ("-")[0]), Integer.parseInt (split[37].split ("-")[1]), Integer.parseInt (split[37].split ("-")[2]));
        colorBusyDayInMonth = new Color (Integer.parseInt (split[38].split ("-")[0]), Integer.parseInt (split[38].split ("-")[1]), Integer.parseInt (split[38].split ("-")[2]));
        colorBusyDayOutsideMonth = new Color (Integer.parseInt (split[39].split ("-")[0]), Integer.parseInt (split[39].split ("-")[1]), Integer.parseInt (split[39].split ("-")[2]));
        colorMiddleCalendarBackgroundPanel1 = new Color (Integer.parseInt (split[40].split ("-")[0]), Integer.parseInt (split[40].split ("-")[1]), Integer.parseInt (split[40].split ("-")[2]));
        colorMiddleCalendarBackgroundPanel2 = new Color (Integer.parseInt (split[41].split ("-")[0]), Integer.parseInt (split[41].split ("-")[1]), Integer.parseInt (split[41].split ("-")[2]));
    }

    /**
     * Set the view panel for the theme.
     *
     * @param viewPanel A reference to the view panel.
     */
    public void setViewPanel(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
    }

    /**
     * Apply this theme to the interface.
     */
    public void apply()
    {
        UIManager.put ("TabbedPane.selected", colorSelected);
        UIManager.put ("TabbedPane.borderColor", colorSelected);
        UIManager.put ("TabbedPane.contentAreaColor", colorSelected);
        UIManager.put ("Menu.background", colorTopBackground1Panel);
        UIManager.put ("Menu.selectionBackground", colorSelected);
        UIManager.put ("MenuBar.background", colorTopBackground1Panel);
        UIManager.put ("PopupMenu.background", colorTopBackground1Panel);
        UIManager.put ("MenuBar.selectionBackground", colorSelected);
        UIManager.put ("MenuItem.background", colorTopBackground1Panel);
        UIManager.put ("MenuItem.selectionBackground", colorSelected);
        UIManager.put ("Separator.background", colorTopBackground1Panel);
        UIManager.put ("Tree.selectionBackground", colorSelected);
        UIManager.put ("ComboBox.selectionBackground", colorSelected);

        viewPanel.gradesDialog.gradesTopJPanel.setBackground (colorDoubleWindowTopBackground1, colorDoubleWindowTopBackground2);
        viewPanel.gradesDialog.gradesBottomJPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.gradesDialog.gradesJPanel.setBackground (colorDoubleWindowBottomBackground1);
        viewPanel.printDialog.printTopJPanel.setBackground (colorDoubleWindowTopBackground1, colorDoubleWindowTopBackground2);
        viewPanel.printDialog.printBottomJPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.printDialog.printPanel.setBackground (colorDoubleWindowBottomBackground1);
        viewPanel.importFromBackupDialog.importTopJPanel.setBackground (colorDoubleWindowTopBackground1, colorDoubleWindowTopBackground2);
        viewPanel.importFromBackupDialog.importBottomJPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.importFromBackupDialog.importFromBackupPanel.setBackground (colorDoubleWindowBottomBackground1);
        viewPanel.settingsDialog.settingsUpperJPanel.setBackground (colorDoubleWindowTopBackground1, colorDoubleWindowTopBackground2);
        viewPanel.termsAndCoursesDialog.termsAndCoursesUpperJPanel.setBackground (colorDoubleWindowTopBackground1, colorDoubleWindowTopBackground2);
        viewPanel.settingsDialog.settingsJPanel.setBackground (colorDoubleWindowBottomBackground1);
        viewPanel.termsAndCoursesDialog.termsAndCoursesJPanel.setBackground (colorDoubleWindowBottomBackground1);
        for (int i = 0; i < viewPanel.settingsDialog.settingsTabbedPane.getTabCount (); ++i)
        {
            viewPanel.settingsDialog.settingsTabbedPane.setBackgroundAt (i, colorDoubleWindowBottomBackground1);
        }
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.termsAndCoursesTabbedPane.getTabCount (); ++i)
        {
            viewPanel.termsAndCoursesDialog.termsAndCoursesTabbedPane.setBackgroundAt (i, colorDoubleWindowBottomBackground1);
        }
        for (int i = 0; i < viewPanel.termsAndCoursesDialog.courseTabbedPane.getTabCount (); ++i)
        {
            viewPanel.termsAndCoursesDialog.courseTabbedPane.setBackgroundAt (i, colorDoubleWindowBottomBackground1);
        }
        viewPanel.settingsDialog.preferencesPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.instructorsDetailsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.typesDetailsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.courseInnerDetailsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.textbooksDetailsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.termsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.termsAndCoursesDialog.coursesPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.settingsDialog.themePanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.settingsDialog.userDetailsPanel.setBackground (colorDoubleWindowBottomBackground1, colorDoubleWindowBottomBackground2);
        viewPanel.aboutDialog.aboutJPanel.setBackground (colorSingleWindowBackground1, colorSingleWindowBackground2);
        viewPanel.updatesJPanel.setBackground (colorSingleWindowBackground1, colorSingleWindowBackground2);
        viewPanel.printingJPanel.setBackground (colorSingleWindowBackground1, colorSingleWindowBackground2);
        viewPanel.repeatDialogPanel.setBackground (colorSingleWindowBackground1, colorSingleWindowBackground2);
        viewPanel.gettingStartedDialog.gettingStartedJPanel.setBackground (colorSingleWindowBackground1, colorSingleWindowBackground2);

        viewPanel.toolBar.setBackground (colorTopBackground1Panel, colorTopBackground2Panel);
        viewPanel.leftPanel.setBackground (colorLeftBackground1Panel, colorLeftBackground2Panel);
        viewPanel.rightPanel.setBackground (colorRightBackground1Panel, colorRightBackground2Panel);
        viewPanel.loadingPanel.setBackground (colorLoadingMiddleBackground1Panel, colorLoadingMiddleBackground2Panel);
        viewPanel.contentPanel.setBackground (colorMiddleBackground1Panel, colorMiddleBackground2Panel);
        viewPanel.statusPanel.setBackground (colorBottomBackground1Panel, colorBottomBackground2Panel);
        viewPanel.listViewPanel.setBackground (colorMiddleCalendarBackgroundPanel1, colorMiddleCalendarBackgroundPanel2);
        viewPanel.weekViewPanel.setBackground (colorMiddleCalendarBackgroundPanel1, colorMiddleCalendarBackgroundPanel2);
        viewPanel.monthViewPanel.setBackground (colorMiddleCalendarBackgroundPanel1, colorMiddleCalendarBackgroundPanel2);
        for (int i = 0; i < viewPanel.middleTabbedPane.getTabCount (); ++i)
        {
            viewPanel.middleTabbedPane.setBackgroundAt (i, colorMiddleCalendarBackgroundPanel1);
        }
        viewPanel.settingsDialog.settingsTabbedPane.updateUI ();
        viewPanel.termsAndCoursesDialog.termsAndCoursesTabbedPane.updateUI ();
        viewPanel.termsAndCoursesDialog.courseTabbedPane.updateUI ();
        viewPanel.middleTabbedPane.updateUI ();
        try
        {
            viewPanel.mainFrame.menuBar.setBackground (colorTopBackground1Panel);
            viewPanel.mainFrame.menuBar.updateUI ();
            for (int i = 0; i < viewPanel.mainFrame.menuBar.getComponentCount (); ++i)
            {
                JMenu menu = (JMenu) viewPanel.mainFrame.menuBar.getComponent (i);
                menu.setBackground (colorTopBackground1Panel);
                menu.updateUI ();
                for (int j = 0; j < menu.getMenuComponentCount (); ++j)
                {
                    if (menu.getMenuComponent (j) instanceof JMenuItem)
                    {
                        ((JMenuItem) menu.getMenuComponent (j)).setBackground (colorTopBackground1Panel);
                        ((JMenuItem) menu.getMenuComponent (j)).updateUI ();
                    }
                    else
                    {
                        ((JSeparator) menu.getMenuComponent (j)).setBackground (colorTopBackground1Panel);
                        ((JSeparator) menu.getMenuComponent (j)).updateUI ();
                    }
                }
            }
        }
        catch (NullPointerException ex)
        {
        }

        viewPanel.miniCalendar.setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.miniCalendar.setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.miniCalendar.setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.miniCalendar.setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.miniCalendar.setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.miniCalendar.setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.miniCalendar.setBackground (colorMiniCalWeekBackground);
        viewPanel.miniCalendar.getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.miniCalendar.setSelectionColor (colorSelected);
        viewPanel.miniCalendar.repaint ();
        viewPanel.miniCalendar.getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.dueDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.dueDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.dueDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.dueDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.dueDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.dueDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.dueDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.dueDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.dueDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.dueDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.dueDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.eventDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.eventDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.eventDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.eventDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.eventDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.eventDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.eventDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.eventDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.eventDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.eventDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.eventDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.repeatEventEndDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.repeatEventEndDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termStartDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.termEndDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseStartDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.courseEndDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labStartDateChooser.getJCalendar ().setSelectionColor (colorSelected);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setUnselectedBackground (colorMiniCalDayNotSelectedBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setUnselectedOutsideMonthBackground (colorMiniCalDayNotSelectedOutsideMonthBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setSelectedBackground (colorMiniCalDaySelectedBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setDecorationBackgroundColor (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setSundayForeground (colorMiniCalSundayForeground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setWeekdayForeground (colorMiniCalWeekForeground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().getDayChooser ().setTodayColor (colorTodayText);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().getMonthChooser ().getComboBoxObj ().updateUI ();
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getPopup ().setBackground (colorMiniCalWeekBackground);
        viewPanel.termsAndCoursesDialog.labEndDateChooser.getJCalendar ().setSelectionColor (colorSelected);

        viewPanel.settingsDialog.settingsCategoriesTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.settingsDialog.settingsCategoriesTable.setSelectionBackground (colorSelected);
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.setSelectionBackground (colorSelected);
        viewPanel.termsAndCoursesDialog.settingsTermsTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.termsAndCoursesDialog.settingsTermsTable.setSelectionBackground (colorSelected);
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.setSelectionBackground (colorSelected);
        viewPanel.termsAndCoursesDialog.settingsTypesTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.termsAndCoursesDialog.settingsTypesTable.setSelectionBackground (colorSelected);
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.setSelectionBackground (colorSelected);
        viewPanel.assignmentsTable.getTableHeader ().setBackground (colorMiddleCalendarBackgroundPanel1);
        viewPanel.assignmentsTable.setSelectionBackground (colorSelected);
        viewPanel.assignmentsTable.updateUI ();
        viewPanel.detailsCourseComboBox.updateUI ();
        viewPanel.detailsTextbookComboBox.updateUI ();
        viewPanel.detailsTypeComboBox.updateUI ();
        viewPanel.categoryComboBox.updateUI ();
        viewPanel.gradesDialog.gradesTermsComboBox.updateUI ();
        viewPanel.gradesDialog.gradesCoursesComboBox.updateUI ();
        viewPanel.settingsDialog.checkForComboBox.updateUI ();
        viewPanel.settingsDialog.colorByComboBox.updateUI ();
        viewPanel.settingsDialog.languageComboBox.updateUI ();
        viewPanel.repeatEventRepeatsComboBox.updateUI ();
        viewPanel.repeatEventRepeatsEveryComboBox.updateUI ();
        viewPanel.settingsDialog.currentThemePrefComboBox.updateUI ();
        viewPanel.settingsDialog.currentThemeComboBox.updateUI ();
        viewPanel.termsAndCoursesDialog.termsComboBox.updateUI ();
        viewPanel.printDialog.contentsComboBox.updateUI ();
        viewPanel.printDialog.nameComboBox.updateUI ();
        viewPanel.printDialog.viewsComboBox.updateUI ();
        viewPanel.printDialog.startMonthComboBox.updateUI ();
        viewPanel.printDialog.endMonthComboBox.updateUI ();
        viewPanel.printDialog.sortingByComboBox.updateUI ();
        viewPanel.printDialog.contentsComboBox.updateUI ();
        viewPanel.printDialog.ascDescComboBox.updateUI ();
        viewPanel.printDialog.sortingByComboBox.updateUI ();
        viewPanel.printDialog.viewsComboBox.updateUI ();
        viewPanel.printDialog.nameComboBox.updateUI ();
        ((DefaultTreeCellRenderer) viewPanel.termTree.getCellRenderer ()).setBackgroundSelectionColor (colorSelected);

        viewPanel.week1Day1.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day2.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day3.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day4.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day5.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day6.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week1Day7.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day1.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day2.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day3.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day4.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day5.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day6.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week2Day7.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day1.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day2.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day3.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day4.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day5.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day6.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week3Day7.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day1.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day2.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day3.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day4.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day5.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day6.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week4Day7.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day1.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day2.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day3.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day4.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day5.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day6.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.week5Day7.setBackground (colorDayInMonthBackground1, colorDayInMonthBackground2);
        viewPanel.extraDayPanel1.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel2.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel3.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel4.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel5.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel6.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        viewPanel.extraDayPanel7.setBackground (colorDayNotInMonthBackground1, colorDayNotInMonthBackground2);
        if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
        {
            viewPanel.loadCalendarView (true);
        }

        viewPanel.refreshBusyDays ();

        for (int i = 0; i < viewPanel.addPopupMenu.getComponentCount (); ++i)
        {
            if (viewPanel.addPopupMenu.getComponent (i) instanceof JMenuItem)
            {
                (viewPanel.addPopupMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JMenuItem) viewPanel.addPopupMenu.getComponent (i)).updateUI ();
            }
            else
            {
                (viewPanel.addPopupMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JSeparator) viewPanel.addPopupMenu.getComponent (i)).updateUI ();
            }
        }
        for (int i = 0; i < viewPanel.termEditMenu.getComponentCount (); ++i)
        {
            if (viewPanel.termEditMenu.getComponent (i) instanceof JMenuItem)
            {
                (viewPanel.termEditMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JMenuItem) viewPanel.termEditMenu.getComponent (i)).updateUI ();
            }
            else
            {
                (viewPanel.termEditMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JSeparator) viewPanel.termEditMenu.getComponent (i)).updateUI ();
            }
        }
        for (int i = 0; i < viewPanel.assignmentsEditMenu.getComponentCount (); ++i)
        {
            if (viewPanel.assignmentsEditMenu.getComponent (i) instanceof JMenuItem)
            {
                (viewPanel.assignmentsEditMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JMenuItem) viewPanel.assignmentsEditMenu.getComponent (i)).updateUI ();
            }
            else
            {
                (viewPanel.assignmentsEditMenu.getComponent (i)).setBackground (colorTopBackground1Panel);
                ((JSeparator) viewPanel.assignmentsEditMenu.getComponent (i)).updateUI ();
            }
        }
    }

    /**
     * The source representation of the theme for saving.
     *
     * @return The string of all theme data for saving to a data file.
     */
    public String out()
    {
        String out = name + ",";
        out += (colorButtonBackground.getRed () + "-" + colorButtonBackground.getGreen () + "-" + colorButtonBackground.getBlue () + ",");
        out += (colorDayInMonthBackground1.getRed () + "-" + colorDayInMonthBackground1.getGreen () + "-" + colorDayInMonthBackground1.getBlue () + ",");
        out += (colorDayInMonthBackground2.getRed () + "-" + colorDayInMonthBackground2.getGreen () + "-" + colorDayInMonthBackground2.getBlue () + ",");
        out += (colorDayInMonthText.getRed () + "-" + colorDayInMonthText.getGreen () + "-" + colorDayInMonthText.getBlue () + ",");
        out += (colorDayNotInMonthBackground1.getRed () + "-" + colorDayNotInMonthBackground1.getGreen () + "-" + colorDayNotInMonthBackground1.getBlue () + ",");
        out += (colorDayNotInMonthBackground2.getRed () + "-" + colorDayNotInMonthBackground2.getGreen () + "-" + colorDayNotInMonthBackground2.getBlue () + ",");
        out += (colorDayNotInMonthText.getRed () + "-" + colorDayNotInMonthText.getGreen () + "-" + colorDayNotInMonthText.getBlue () + ",");
        out += (colorTodayText.getRed () + "-" + colorTodayText.getGreen () + "-" + colorTodayText.getBlue () + ",");
        out += (colorLeftBackground1Panel.getRed () + "-" + colorLeftBackground1Panel.getGreen () + "-" + colorLeftBackground1Panel.getBlue () + ",");
        out += (colorLeftBackground2Panel.getRed () + "-" + colorLeftBackground2Panel.getGreen () + "-" + colorLeftBackground2Panel.getBlue () + ",");
        out += (colorRightBackground1Panel.getRed () + "-" + colorRightBackground1Panel.getGreen () + "-" + colorRightBackground1Panel.getBlue () + ",");
        out += (colorRightBackground2Panel.getRed () + "-" + colorRightBackground2Panel.getGreen () + "-" + colorRightBackground2Panel.getBlue () + ",");
        out += (colorTopBackground1Panel.getRed () + "-" + colorTopBackground1Panel.getGreen () + "-" + colorTopBackground1Panel.getBlue () + ",");
        out += (colorTopBackground2Panel.getRed () + "-" + colorTopBackground2Panel.getGreen () + "-" + colorTopBackground2Panel.getBlue () + ",");
        out += (colorBottomBackground1Panel.getRed () + "-" + colorBottomBackground1Panel.getGreen () + "-" + colorBottomBackground1Panel.getBlue () + ",");
        out += (colorBottomBackground2Panel.getRed () + "-" + colorBottomBackground2Panel.getGreen () + "-" + colorBottomBackground2Panel.getBlue () + ",");
        out += (colorLoadingMiddleBackground1Panel.getRed () + "-" + colorLoadingMiddleBackground1Panel.getGreen () + "-" + colorLoadingMiddleBackground1Panel.getBlue () + ",");
        out += (colorLoadingMiddleBackground2Panel.getRed () + "-" + colorLoadingMiddleBackground2Panel.getGreen () + "-" + colorLoadingMiddleBackground2Panel.getBlue () + ",");
        out += (colorMiddleBackground1Panel.getRed () + "-" + colorMiddleBackground1Panel.getGreen () + "-" + colorMiddleBackground1Panel.getBlue () + ",");
        out += (colorMiddleBackground2Panel.getRed () + "-" + colorMiddleBackground2Panel.getGreen () + "-" + colorMiddleBackground2Panel.getBlue () + ",");
        out += (colorSingleWindowBackground1.getRed () + "-" + colorSingleWindowBackground1.getGreen () + "-" + colorSingleWindowBackground1.getBlue () + ",");
        out += (colorSingleWindowBackground2.getRed () + "-" + colorSingleWindowBackground2.getGreen () + "-" + colorSingleWindowBackground2.getBlue () + ",");
        out += (colorDoubleWindowTopBackground1.getRed () + "-" + colorDoubleWindowTopBackground1.getGreen () + "-" + colorDoubleWindowTopBackground1.getBlue () + ",");
        out += (colorDoubleWindowTopBackground2.getRed () + "-" + colorDoubleWindowTopBackground2.getGreen () + "-" + colorDoubleWindowTopBackground2.getBlue () + ",");
        out += (colorDoubleWindowBottomBackground1.getRed () + "-" + colorDoubleWindowBottomBackground1.getGreen () + "-" + colorDoubleWindowBottomBackground1.getBlue () + ",");
        out += (colorDoubleWindowBottomBackground2.getRed () + "-" + colorDoubleWindowBottomBackground2.getGreen () + "-" + colorDoubleWindowBottomBackground2.getBlue () + ",");
        out += (colorMiniCalDayNotSelectedOutsideMonthBackground.getRed () + "-" + colorMiniCalDayNotSelectedOutsideMonthBackground.getGreen () + "-" + colorMiniCalDayNotSelectedOutsideMonthBackground.getBlue () + ",");
        out += (colorMiniCalDayNotSelectedBackground.getRed () + "-" + colorMiniCalDayNotSelectedBackground.getGreen () + "-" + colorMiniCalDayNotSelectedBackground.getBlue () + ",");
        out += (colorMiniCalDaySelectedBackground.getRed () + "-" + colorMiniCalDaySelectedBackground.getGreen () + "-" + colorMiniCalDaySelectedBackground.getBlue () + ",");
        out += (colorMiniCalWeekBackground.getRed () + "-" + colorMiniCalWeekBackground.getGreen () + "-" + colorMiniCalWeekBackground.getBlue () + ",");
        out += (colorMiniCalWeekForeground.getRed () + "-" + colorMiniCalWeekForeground.getGreen () + "-" + colorMiniCalWeekForeground.getBlue () + ",");
        out += (colorMiniCalSundayForeground.getRed () + "-" + colorMiniCalSundayForeground.getGreen () + "-" + colorMiniCalSundayForeground.getBlue () + ",");
        out += (colorEven.getRed () + "-" + colorEven.getGreen () + "-" + colorEven.getBlue () + ",");
        out += (colorOdd.getRed () + "-" + colorOdd.getGreen () + "-" + colorOdd.getBlue () + ",");
        out += (colorSelected.getRed () + "-" + colorSelected.getGreen () + "-" + colorSelected.getBlue () + ",");
        out += (colorDoneDayInMonth.getRed () + "-" + colorDoneDayInMonth.getGreen () + "-" + colorDoneDayInMonth.getBlue () + ",");
        out += (colorDoneDayOutsideMonth.getRed () + "-" + colorDoneDayOutsideMonth.getGreen () + "-" + colorDoneDayOutsideMonth.getBlue () + ",");
        out += (colorBusyDayInMonth.getRed () + "-" + colorBusyDayInMonth.getGreen () + "-" + colorBusyDayInMonth.getBlue () + ",");
        out += (colorBusyDayOutsideMonth.getRed () + "-" + colorBusyDayOutsideMonth.getGreen () + "-" + colorBusyDayOutsideMonth.getBlue () + ",");
        out += (colorMiddleCalendarBackgroundPanel1.getRed () + "-" + colorMiddleCalendarBackgroundPanel1.getGreen () + "-" + colorMiddleCalendarBackgroundPanel1.getBlue () + ",");
        out += (colorMiddleCalendarBackgroundPanel2.getRed () + "-" + colorMiddleCalendarBackgroundPanel2.getGreen () + "-" + colorMiddleCalendarBackgroundPanel2.getBlue ());

        return out;
    }

    /**
     * Return the string representation of this theme, which is the theme name.
     *
     * @return The name of the theme.
     */
    @Override
    public String toString()
    {
        return name;
    }
}
