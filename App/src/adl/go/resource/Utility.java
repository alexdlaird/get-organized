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

package adl.go.resource;

import adl.go.gui.ComboListItem;
import adl.go.gui.Domain;
import adl.go.gui.Theme;
import adl.go.gui.ViewPanel;
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
import java.awt.Color;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;

/**
 * The generic utility implements all generic data model interaction methods,
 * but for file or server I/O the class is extended to meet those specific
 * needs.
 *
 * @author Alex Laird
 */
public abstract class Utility
{
    /**
     * The token which separates items in the data file.
     */
    protected static final String SEPARATOR = ",";
    /**
     * The token which ends a line of data in the data file.
     */
    protected static final String END_OF_LINE = ";";
    /**
     * The token which separates full objects of data.
     */
    protected static final String LINE_RETURN = "\n";
    /**
     * The list of all preferences.
     */
    public Preferences preferences;
    /**
     * The object of user details.
     */
    public UserDetails userDetails;
    /**
     * The list of all terms.
     */
    public ArrayList<Term> terms = new ArrayList<Term> ();
    /**
     * The list of all courses.
     */
    public ArrayList<Course> courses = new ArrayList<Course> ();
    /**
     * The list of all event years.
     */
    public ArrayList<EventYear> eventYears = new ArrayList<EventYear> ();
    /**
     * The list of all types.
     */
    public ArrayList<AssignmentType> types = new ArrayList<AssignmentType> ();
    /**
     * The list of all instructors.
     */
    public ArrayList<Instructor> instructors = new ArrayList<Instructor> ();
    /**
     * The list of all textbooks.
     */
    public ArrayList<Textbook> textbooks = new ArrayList<Textbook> ();
    /**
     * The list of all assignments and events.
     */
    public ArrayList<ListItem> assignmentsAndEvents = new ArrayList<ListItem> ();
    /**
     * The list of all repeating events.
     */
    public ArrayList<Event> repeatingEvents = new ArrayList<Event> ();
    /**
     * The list of themes available.
     */
    public ArrayList<Theme> themes = new ArrayList<Theme> ();
    /**
     * A reference to the main frame of the application.
     */
    protected ViewPanel viewPanel;
    /**
     * A reference to the domain of the application.
     */
    public Domain domain;
    /**
     * The default preferences for the application.
     */
    protected String defaultSettings;
    /**
     * The currently set theme.
     */
    public Theme currentTheme;
    /**
     * The colors to rotate through.
     */
    private Color[] colorCycle = new Color[]
    {
        new Color (0, 0, 153), new Color (0, 153, 0), new Color (153, 153, 0), new Color (153, 0, 0), new Color (255, 0, 255), new Color (0, 153, 153), new Color (153, 0, 153), new Color (102, 102, 102), new Color (255, 102, 0), new Color (102, 0, 102)
    };
    /**
     * The number of preferences that should be in the preferences file to match
     * the default values.
     */
    protected int prefsCount;

    /**
     * Construct the utility class.
     *
     * @param viewPanel A reference to the view panel.
     */
    public void constructUtility(ViewPanel viewPanel)
    {
        this.viewPanel = viewPanel;
        preferences = new Preferences (viewPanel);
        userDetails = new UserDetails ();

        themes.add (new Theme (null, "Autumn Leaves,245-245-245,245-233-186,238-221-130,0-0-0,139-90-43,139-37-0,120-120-120,0-153-0,227-130-23,212-61-26,227-130-23,212-61-26,245-233-186,212-61-26,205-102-0,205-102-0,139-37-0,139-37-0,205-102-0,205-102-0,245-233-186,217-206-165,245-233-186,212-61-26,245-233-186,245-233-186,139-37-0,238-221-130,227-130-23,212-61-26,0-0-165,165-0-0,255-165-79,245-233-186,184-118-57,0-0-255,75-101-207,255-0-0,255-90-90,212-61-26,139-0-0"));
        themes.add (new Theme (null, "Blue Moon,245-245-245,204-237-255,178-207-255,0-0-0,120-171-235,0-104-139,120-120-120,0-150-0,180-206-245,80-166-194,180-206-245,80-166-194,179-213-245,0-104-139,180-206-245,0-104-139,180-206-245,0-104-139,180-206-245,0-104-139,204-237-255,120-171-235,179-213-245,0-104-139,209-233-245,209-233-245,0-104-139,204-237-255,80-166-194,204-237-255,27-63-139,165-0-0,179-213-245,209-233-245,80-166-194,0-0-255,30-94-255,255-0-0,255-71-83,204-237-255,0-104-139"));
        themes.add (new Theme (null, "Default,245-245-245,204-237-255,174-207-225,0-0-0,255-255-255,235-235-235,120-120-120,0-173-0,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,255-255-255,235-235-235,245-245-245,255-255-255,235-235-235,196-228-245,135-162-176,210-228-238,0-0-165,165-0-0,224-255-255,255-255-255,177-206-222,0-0-255,40-87-255,255-0-0,255-71-83,255-255-255,235-235-235"));
        themes.add (new Theme (null, "Double Bubble,245-245-245,245-200-238,255-172-253,0-0-0,251-184-255,255-110-199,120-120-120,0-166-0,254-231-255,255-146-187,254-231-255,255-146-187,245-217-239,255-110-199,245-245-245,255-146-187,251-231-255,244-191-255,251-231-255,244-191-255,251-231-255,244-191-255,245-217-239,255-106-246,251-231-255,251-231-255,255-110-199,251-184-255,247-60-224,244-191-255,255-23-246,143-34-130,251-231-255,244-191-255,247-151-255,0-0-255,30-94-255,255-0-0,217-10-50,251-231-255,244-191-255"));
        themes.add (new Theme (null, "Marshie Marshmallow,245-245-245,255-255-255,255-255-196,0-0-0,241-226-118,255-224-40,120-120-120,0-175-0,251-185-23,212-173-38,251-185-23,212-173-38,255-255-196,255-224-40,251-185-23,212-173-38,214-169-26,122-69-0,214-169-26,122-69-0,255-255-196,255-224-40,255-255-196,255-224-40,255-255-196,255-255-196,255-224-40,255-255-196,153-102-51,214-169-26,87-58-29,165-0-0,249-255-171,250-255-244,153-102-51,0-0-255,90-90-255,255-0-0,255-90-90,255-255-255,255-255-196"));
        themes.add (new Theme (null, "Minty Kiss,245-245-245,212-245-220,171-245-176,0-0-0,154-255-149,24-181-95,120-120-120,21-189-41,205-245-215,24-181-95,194-245-204,24-181-95,234-245-231,24-181-95,245-245-245,24-181-95,194-245-204,24-181-95,194-245-204,24-181-95,194-245-215,171-245-176,194-245-215,24-181-95,194-245-215,194-245-215,24-181-95,171-245-176,13-102-52,194-245-204,24-181-95,214-100-94,237-240-242,212-245-220,27-204-83,0-0-255,30-94-255,255-0-0,217-58-52,194-245-204,24-181-95"));
        themes.add (new Theme (null, "Ocean Avenue,245-245-245,245-245-245,142-236-245,0-0-0,214-255-244,51-181-159,120-120-120,0-184-0,241-254-255,32-178-170,241-255-254,32-178-170,245-245-245,32-178-170,245-245-245,32-178-170,241-255-254,32-178-170,241-255-254,32-178-170,245-245-245,214-255-244,245-245-245,32-178-170,214-255-244,214-255-244,51-181-159,214-255-244,31-112-99,214-255-244,41-148-130,165-0-0,214-255-244,242-255-247,51-181-159,0-0-255,40-87-255,255-0-0,255-71-83,241-255-254,32-178-170"));
        themes.add (new Theme (null, "Purple Haze,245-245-245,215-192-245,191-149-255,0-0-0,200-154-227,145-44-238,120-120-120,18-161-16,215-198-247,137-104-205,215-198-247,137-104-205,200-184-230,145-44-238,215-198-247,124-29-215,215-198-247,124-29-215,215-198-247,124-29-215,200-184-230,176-170-245,200-184-230,124-29-215,230-229-245,176-170-245,145-44-238,213-198-245,108-24-186,164-106-238,113-0-212,165-0-0,230-229-245,215-198-247,148-33-255,0-0-255,39-54-255,255-0-0,255-71-83,215-198-247,124-29-215"));
        themes.add (new Theme (null, "Spring Fling,245-245-245,255-254-252,255-248-195,0-0-0,221-255-206,186-255-186,120-120-120,0-175-0,186-255-186,131-230-131,186-255-186,131-230-131,245-245-245,186-255-186,245-245-245,255-255-207,245-245-245,255-255-207,255-255-207,255-255-207,245-245-245,221-255-206,245-245-245,131-230-131,221-255-206,221-255-206,137-240-137,255-255-207,255-52-179,210-228-238,255-52-179,0-175-0,186-255-186,255-255-255,255-52-179,0-0-255,90-90-255,255-0-0,255-90-90,215-243-247,215-243-247"));
        themes.add (new Theme (null, "Summer Lovin',245-245-245,184-255-181,114-255-102,0-0-0,255-247-202,255-230-0,120-120-120,0-175-0,215-244-255,35-205-202,215-244-255,35-205-202,215-244-255,35-205-202,215-244-255,85-195-205,184-255-181,184-255-181,171-255-151,171-255-151,184-255-181,184-255-181,215-244-255,35-205-202,184-255-181,184-255-181,255-230-0,114-255-102,35-205-202,255-82-222,0-0-165,165-0-0,239-249-255,180-253-255,255-230-0,0-0-255,90-90-255,255-0-0,255-90-90,255-82-222,255-82-222"));
        themes.add (new Theme (null, "Sundown,245-245-245,255-239-201,251-236-93,0-0-0,255-204-153,255-170-0,120-120-120,0-173-0,255-204-153,255-170-0,255-204-153,255-170-0,243-240-221,251-236-93,255-204-153,255-170-0,255-204-153,255-255-126,255-204-153,255-255-126,243-240-221,204-202-186,243-240-221,251-236-93,243-240-221,243-240-221,255-170-0,255-255-126,176-98-9,255-204-153,255-87-33,165-0-0,251-236-93,255-239-201,255-170-0,0-0-255,90-90-255,255-0-0,255-90-90,255-255-126,255-255-126"));
        themes.add (new Theme (null, "Winter Wonderland,245-245-245,204-237-255,225-225-225,0-0-0,168-187-191,95-159-159,120-120-120,0-173-0,200-227-227,115-177-183,200-227-227,115-177-183,225-225-225,81-135-135,200-227-227,115-177-183,200-227-227,225-225-225,200-227-227,225-225-225,200-227-227,200-227-227,225-225-225,81-135-135,200-227-227,200-227-227,95-159-159,196-228-245,150-90-150,210-228-238,0-0-165,165-0-0,200-227-227,245-245-245,150-90-150,0-0-255,40-87-255,255-0-0,255-71-83,115-177-183,225-225-225"));
    }

    /**
     * Sets the domain reference from the main frame.
     *
     * @param domain The domain reference.
     */
    public void setDomain(Domain domain)
    {
        this.domain = domain;
    }

    /**
     * Retrieves the separator character sequence.
     *
     * @return The separator character sequence.
     */
    public static String getSeparator()
    {
        return SEPARATOR;
    }

    /**
     * Retrieves the end of line character sequence.
     *
     * @return The separator character sequence.
     */
    public static String getEndOfLine()
    {
        return END_OF_LINE;
    }

    /**
     * Retrieves the line return character sequence.
     *
     * @return The line return character sequence.
     */
    public static String getLineReturn()
    {
        return LINE_RETURN;
    }

    /**
     * This method should only be called by a new course or it will get out of
     * sync. It returns the next available unique color in the rotation. After
     * its known colors have been used, it will repeat.
     *
     * @return The next color in the rotation.
     */
    public Color nextColor()
    {
        ++preferences.nextCourseColorIndex;
        if (preferences.nextCourseColorIndex >= colorCycle.length)
        {
            preferences.nextCourseColorIndex = 0;
        }
        return colorCycle[preferences.nextCourseColorIndex];
    }

    /**
     * Retrieves any type matching the given id, if it exists, otherwise returns
     * null.
     *
     * @param id The unique ID to look for.
     * @return The generic object matching the unique ID, if it exists.
     */
    public Object getByID(long id)
    {
        if (getTermByID (id) != null)
        {
            return getTermByID (id);
        }
        else
        {
            if (getCourseByID (id) != null)
            {
                return getCourseByID (id);
            }
            else
            {
                if (getTypeByID (id) != null)
                {
                    return getTypeByID (id);
                }
                else
                {
                    if (getTextbookByID (id) != null)
                    {
                        return getTextbookByID (id);
                    }
                    else
                    {
                        if (getAssignmentOrEventByID (id) != null)
                        {
                            return getAssignmentOrEventByID (id);
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Retrieves a term object for the given id, if it exists, otherwise returns
     * null.
     *
     * @param id The unique ID to look for.
     * @return The term matching the unique ID, if it exists.
     */
    public Term getTermByID(long id)
    {
        for (int i = 0; i < terms.size (); ++i)
        {
            if (terms.get (i).getUniqueID () == id)
            {
                return terms.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieve the index for the given term.
     *
     * @param term The term to get the index for.
     * @return The index of the given term.
     */
    public int getTermIndex(Term term)
    {
        for (int i = 0; i < terms.size (); ++i)
        {
            if (terms.get (i) == term)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retrieves the index of the specified course.
     *
     * @param course The course to find the index for.
     * @return The index of the course.
     */
    public int getCourseIndex(Course course)
    {
        for (int i = 0; i < courses.size (); ++i)
        {
            if (courses.get (i) == course)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retrieves a course object for the given id, if it exists, otherwise
     * returns null.
     *
     * @param id The unique ID to look for.
     * @return The course matching the unique ID, if it exists.
     */
    public Course getCourseByID(long id)
    {
        for (int i = 0; i < courses.size (); ++i)
        {
            if (courses.get (i).getUniqueID () == id)
            {
                return courses.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieves a type object for the given id, if it exists, otherwise returns
     * null.
     *
     * @param id The unique ID to look for.
     * @return The type matching the unique ID, if it exists.
     */
    public AssignmentType getTypeByID(long id)
    {
        for (int i = 0; i < types.size (); ++i)
        {
            if (types.get (i).getUniqueID () == id)
            {
                return types.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieves a textbook object for the given id, if it exists, otherwise
     * returns null.
     *
     * @param id The unique ID to look for.
     * @return The textbook matching the unique ID, if it exists.
     */
    public Textbook getTextbookByID(long id)
    {
        for (int i = 0; i < textbooks.size (); ++i)
        {
            if (textbooks.get (i).getUniqueID () == id)
            {
                return textbooks.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieve the object reference for the given year.
     *
     * @param year The year to look for.
     * @return The EventYear object for the given year.
     */
    public EventYear getEventYear(String year)
    {
        for (int i = 0; i < eventYears.size (); ++i)
        {
            if (eventYears.get (i).getYear ().equals (year))
            {
                return eventYears.get (i);
            }
        }

        EventYear eventYear = new EventYear (year, this);
        eventYears.add (eventYear);
        eventYear.markChanged ();

        return eventYear;
    }

    /**
     * Add the course at the given index to the courses list.
     *
     * @param index The index to add the course to.
     * @param course The course to add.
     */
    public void addToCourseAt(int index, Course course)
    {
        int size = courses.size ();
        courses.add (course);
        if (index < size - 1)
        {
            for (int i = size; i > index + 1; --i)
            {
                //swap (courses, i, i - 1);
            }
        }
    }

    /**
     * Retrieves a category object for the given name, if it exists, otherwise
     * returns null.
     *
     * @param name The name to look for.
     * @return The category object.
     */
    public Category getCategoryByName(String name)
    {
        for (int i = 0; i < preferences.categories.size (); ++i)
        {
            if (preferences.categories.get (i).getName ().equals (name))
            {
                return preferences.categories.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieves a assignment object for the given id, if it exists, otherwise
     * returns null.
     *
     * @param id The unique ID to look for.
     * @return The assignment matching the unique ID, if it exists.
     */
    public ListItem getAssignmentOrEventByID(long id)
    {
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            if (assignmentsAndEvents.get (i).getUniqueID () == id)
            {
                return assignmentsAndEvents.get (i);
            }
        }

        return null;
    }

    /**
     * Retrieves the index of an assignment or event with the specified ID.
     *
     * @param id The unique ID of the assignment.
     * @return The index of the assignment.
     */
    public int getAssignmentOrEventIndexByID(long id)
    {
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            if (assignmentsAndEvents.get (i).getUniqueID () == id)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Retrieves the index of the assignment or event within the
     * assignmentsAndEvents table.
     *
     * @param id The unique ID to look for.
     * @return The index of the assignment or event in the assignmentsAndEvents
     * table.
     */
    public int getTableIndexByID(long id)
    {
        if (id != -1)
        {
            for (int i = 0; i < viewPanel.assignmentsTableModel.getRowCount (); ++i)
            {
                if (Long.parseLong (viewPanel.assignmentsTableModel.getValueAt (i, 6).toString ()) == id)
                {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Retrieves a reference to the term with the specified name if it exists.
     *
     * @param termName The name of the term to retrieve.
     * @return An object for the term, or null if the term was not found.
     */
    public Term getTermByName(String termName)
    {
        for (int i = 0; i < terms.size (); ++i)
        {
            if (terms.get (i).getTypeName ().equals (termName))
            {
                return terms.get (i);
            }
        }

        return null;
    }

    /**
     * Sets all references between terms, courses, textbooks, types, and
     * assignmentsAndEvents, referencing each parent object and adding each
     * child object reference to its parent.
     *
     * It is assumed that, when this method is called, references have never
     * been set. If they have, courses may be added to a term again, etc.
     */
    public void refreshReferences()
    {
        boolean issuesFound = false;

        // refresh term-course references
        for (int i = 0; i < courses.size (); ++i)
        {
            Course course = courses.get (i);

            if (!course.getInstructorName ().equals (""))
            {
                Instructor instructor = new Instructor (course.getInstructorName (), System.currentTimeMillis (), course, this);
                instructor.setInstructorEmail (course.getInstructorEmail ());
                instructor.setInstructorPhone (course.getInstructorPhone ());
                instructor.setOfficeHours (course.getOfficeHours ());
                instructor.setOfficeLocation (course.getOfficeLocation ());

                instructors.add (instructor);
                course.addInstructor (instructor);

                course.setInstructorName ("");
                course.setInstructorEmail ("");
                course.setInstructorPhone ("");
                course.setOfficeHours ("");
                course.setOfficeLocation ("");
            }

            Term term = getTermByID (course.getTermID ());
            if (term == null)
            {
                term = new Term (course.getTermID () + "", course.getTermID (), this);
                terms.add (term);
                if (!issuesFound)
                {
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("termsFileCorrupt"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("termsIssues"));
                    optionDialog.setVisible (true);

                    issuesFound = true;
                }
            }
            course.setTerm (term);
            if (!term.hasCourse (course))
            {
                term.addCourse (course);
            }
        }

        issuesFound = false;

        // refresh course-types references
        for (int i = 0; i < types.size (); ++i)
        {
            AssignmentType type = types.get (i);
            Course course = getCourseByID (type.getCourseID ());
            if (course == null)
            {
                Term term = null;
                if (terms.isEmpty ())
                {
                    long time = System.currentTimeMillis ();
                    term = new Term (time + "", time, this);
                    terms.add (term);
                }
                else
                {
                    term = terms.get (0);
                }
                course = new Course (type.getCourseID () + "", type.getCourseID (), term, this);
                courses.add (course);
                term.add (course);
                if (!issuesFound)
                {
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("coursesFileCorruptTypes"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("coursesIssues"));
                    optionDialog.setVisible (true);

                    issuesFound = true;
                }
            }
            type.setCourse (course);
            if (!course.hasType (type))
            {
                course.addType (type);
            }
        }

        issuesFound = false;

        // refresh course-instructor references
        for (int i = 0; i < instructors.size (); ++i)
        {
            Instructor instructor = instructors.get (i);
            if (instructor.getCourseID () != -1)
            {
                Course course = getCourseByID (instructor.getCourseID ());
                if (course == null)
                {
                    Term term = null;
                    if (terms.isEmpty ())
                    {
                        long time = System.currentTimeMillis ();
                        term = new Term (time + "", time, this);
                        terms.add (term);
                    }
                    else
                    {
                        term = terms.get (0);
                    }
                    course = new Course (instructor.getCourseID () + "", instructor.getCourseID (), term, this);
                    courses.add (course);
                    term.add (course);
                    if (!issuesFound)
                    {
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("coursesFileCorruptInstructors"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("coursesIssues"));
                        optionDialog.setVisible (true);

                        issuesFound = true;
                    }
                }
                instructor.setCourse (course);
                if (!course.hasInstructor (instructor))
                {
                    course.addInstructor (instructor);
                }
            }
        }

        issuesFound = false;

        // refresh course-textbook references
        for (int i = 0; i < textbooks.size (); ++i)
        {
            Textbook textbook = textbooks.get (i);
            Course course = getCourseByID (textbook.getCourseID ());
            if (course == null)
            {
                Term term = null;
                if (terms.isEmpty ())
                {
                    long time = System.currentTimeMillis ();
                    term = new Term (time + "", time, this);
                    terms.add (term);
                }
                else
                {
                    term = terms.get (0);
                }
                course = new Course (textbook.getCourseID () + "", textbook.getCourseID (), term, this);
                courses.add (course);
                term.add (course);
                if (!issuesFound)
                {
                    ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                    ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("coursesFileCorruptTextbooks"));
                    ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                    JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("coursesIssues"));
                    optionDialog.setVisible (true);

                    issuesFound = true;
                }
            }
            textbook.setCourse (course);
            if (!course.hasTextbook (textbook))
            {
                course.addTextbook (textbook);
            }
        }

        issuesFound = false;

        // refresh course-assignment references and assignment-type assignment-textbook references
        // refresh event-category references
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            ListItem item = assignmentsAndEvents.get (i);
            if (item.isAssignment ())
            {
                Assignment assignment = (Assignment) item;
                Course course = getCourseByID (assignment.getCourseID ());
                if (course == null)
                {
                    Term term = null;
                    if (terms.isEmpty ())
                    {
                        long time = System.currentTimeMillis ();
                        term = new Term (time + "", time, this);
                        terms.add (term);
                    }
                    else
                    {
                        term = terms.get (0);
                    }
                    course = new Course (assignment.getCourseID () + "", assignment.getCourseID (), term, this);
                    courses.add (course);
                    term.add (course);
                    if (!issuesFound)
                    {
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("coursesFileCorruptAssignments"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("coursesIssues"));
                        optionDialog.setVisible (true);

                        issuesFound = true;
                    }
                }
                assignment.setCourse (course);
                if (!course.hasAssignment (assignment))
                {
                    course.addAssignment (assignment);
                }

                try
                {
                    assignment.setType (getTypeByID (assignment.getTypeID ()));
                }
                catch (NullPointerException ex)
                {
                    if (!issuesFound)
                    {
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("typesFileCorruptAssignments"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("typesIssues"));
                        optionDialog.setVisible (true);

                        issuesFound = true;
                    }
                }
                try
                {
                    assignment.setTextbook (getTextbookByID (assignment.getTextbookID ()));
                }
                catch (NullPointerException ex)
                {
                    if (!issuesFound)
                    {
                        ViewPanel.OPTION_PANE.setOptions (viewPanel.OK_CHOICE);
                        ViewPanel.OPTION_PANE.setMessage (viewPanel.domain.language.getString ("textbooksFileCorruptAssignments"));
                        ViewPanel.OPTION_PANE.setMessageType (JOptionPane.WARNING_MESSAGE);
                        JDialog optionDialog = ViewPanel.OPTION_PANE.createDialog (viewPanel, viewPanel.domain.language.getString ("textbooksIssues"));
                        optionDialog.setVisible (true);

                        issuesFound = true;
                    }
                }

                if (assignment.needsImmediateSaveNeeded ())
                {
                    assignment.resetNeedsImmediateSaveNeeded ();
                    course.markChanged ();
                }
            }
            else
            {
                Event event = (Event) item;
                Category category = getCategoryByName (event.getCategoryName ());
                if (category == null)
                {
                    category = new Category (event.getCategoryName (), new Color (0, 0, 0));
                    preferences.categories.add (category);
                    viewPanel.settingsDialog.categoryTableModel.addRow (new Object[]
                            {
                                event.getCategoryName (), "0-0-0"
                            });
                    viewPanel.categoryComboModel.addElement (event.getCategoryName ());
                    domain.needsPreferencesSave = true;
                    savePreferences ();
                }
                event.setCategory (category);
                if (!category.hasEvent (event))
                {
                    category.addEvent (event);
                }

                event.setEventYear (getEventYear (event.getDueDate ().split ("/")[2]));
                if (!event.getEventYear ().hasEvent (event))
                {
                    event.getEventYear ().addEvent (event);
                }

                if (event.needsImmediateSaveNeeded ())
                {
                    event.resetNeedsImmediateSaveNeeded ();
                    event.getEventYear ().markChanged ();
                }
            }
        }
    }

    /**
     * Fills the term tree with terms and courses found in their respective
     * vectors.
     */
    public void loadTermTree()
    {
        viewPanel.root.removeAllChildren ();
        for (int i = 0; i < terms.size (); ++i)
        {
            viewPanel.root.add (terms.get (i));
        }
        viewPanel.getDomain ().refreshTermTree ();
        ((DefaultTreeModel) viewPanel.termTree.getModel ()).reload ();
    }

    /**
     * Checks that an assignment or event (at the given index) are within the
     * selected scope in the term tree (a course or term will be selected).
     *
     * @param index The index of the assignment or event.
     * @return True if the assignment or event is within the scope, false
     * otherwise.
     */
    public boolean isWithinFilteredScope(int index)
    {
        boolean is = true;
        ListItem item = assignmentsAndEvents.get (index);
        if (item.isAssignment ())
        {
            Assignment assignment = (Assignment) item;
            int termIndex = viewPanel.getSelectedTermIndex ();
            int courseIndex = viewPanel.getSelectedCourseIndex ();
            // go with the terms scope
            if (courseIndex == -1 && termIndex != -1)
            {
                Term term = terms.get (termIndex);
                if (assignment.getCourse ().getTerm () != term)
                {
                    is = false;
                }
            }
            // go witht he courses scope
            else
            {
                if (courseIndex != -1 && termIndex != -1)
                {
                    Course course = courses.get (courseIndex);
                    if (assignment.getCourse () != course)
                    {
                        is = false;
                    }
                }
            }
        }

        return is;
    }

    /**
     * It is NOT recommended that you use this function. Set a change variable
     * and allow the thread to save automatically.
     */
    public void forceSave()
    {
        domain.workerThread.save ();
    }

    /**
     * Fills the assignmentsAndEvents table in the center panel with the
     * assignmentsAndEvents in the assignmentsAndEvents vector.
     *
     * @param refreshTable True if the UI should be updated, false if only the
     * table model should be updated.
     */
    public void loadAssignmentsTable(boolean refreshTable)
    {
        viewPanel.assignmentsTableModel.setColumnName (1, viewPanel.domain.language.getString ("task"));
        viewPanel.assignmentsTableModel.setColumnName (2, viewPanel.domain.language.getString ("type"));
        viewPanel.assignmentsTableModel.setColumnName (3, viewPanel.domain.language.getString ("course") + viewPanel.domain.language.getString ("category"));
        viewPanel.assignmentsTableModel.setColumnName (4, viewPanel.domain.language.getString ("dueDate"));
        viewPanel.assignmentsTableModel.setColumnName (5, viewPanel.domain.language.getString ("grade"));
        viewPanel.assignmentsTableModel.removeAllRows ();
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            ListItem item = assignmentsAndEvents.get (i);
            if (item.isAssignment () && (preferences.filter1Index == 0 || preferences.filter1Index == 1))
            {
                boolean add = true;
                if (!isWithinFilteredScope (i)
                    || (preferences.filter2Index == 1 && !((Assignment) item).isDone ())
                    || (preferences.filter2Index == 2 && ((Assignment) item).isDone ())
                    || (preferences.filter2Index == 3 && !((Assignment) item).isOverdue ()))
                {
                    add = false;
                }
                if (add)
                {
                    viewPanel.assignmentsTableModel.addRow (((Assignment) item).getRowObject ());
                }
            }
            else
            {
                if (!item.isAssignment () && (preferences.filter1Index == 0 || preferences.filter1Index == 2))
                {
                    boolean add = true;
                    try
                    {
                        if (!isWithinFilteredScope (i)
                            || ((preferences.filter2Index == 1 || preferences.filter2Index == 3) && Domain.DATE_FORMAT.parse (((Event) item).getDueDate ()).after (domain.today))
                            || (preferences.filter2Index == 2 && Domain.DATE_FORMAT.parse (((Event) item).getDueDate ()).before (domain.today)))
                        {
                            add = false;
                        }
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }
                    if (add)
                    {
                        viewPanel.assignmentsTableModel.addRow (((Event) item).getRowObject ());
                    }
                }
            }
        }
        try
        {
            if (refreshTable)
            {
                viewPanel.assignmentsTable.refreshTable ();
            }
        }
        catch (NullPointerException ex)
        {
            Domain.LOGGER.add (ex);
        }
    }

    /**
     * Fills the course combo box in the right details panel.
     */
    public void loadDetailsCourseBox()
    {
        viewPanel.courseComboModel.removeAllElements ();
        Term term = ((Assignment) assignmentsAndEvents.get (domain.getSelectedTableIndex ())).getCourse ().getTerm ();
        for (int i = 0; i < term.getCourseCount (); ++i)
        {
            viewPanel.courseComboModel.addElement (new ComboListItem (term.getCourse (i).getTypeName (), term.getCourse (i).getUniqueID ()));
        }
    }

    /**
     * Fills the textbook combo box in the right details panel.
     */
    public void loadDetailsTextbookBox()
    {
        viewPanel.textbookComboModel.removeAllElements ();
        viewPanel.textbookComboModel.addElement (new ComboListItem ("-" + viewPanel.domain.language.getString ("none") + "-", -1));
        Course course = ((Assignment) assignmentsAndEvents.get (domain.getSelectedTableIndex ())).getCourse ();
        for (int i = 0; i < course.getTextbookCount (); ++i)
        {
            viewPanel.textbookComboModel.addElement (new ComboListItem (course.getTextbook (i).getTypeName (), course.getTextbook (i).getUniqueID ()));
        }
    }

    /**
     * Fills the type combo box in the right details panel.
     */
    public void loadDetailsTypeBox()
    {
        viewPanel.typeComboModel.removeAllElements ();
        viewPanel.typeComboModel.addElement (new ComboListItem ("-" + viewPanel.domain.language.getString ("none") + "-", -1));
        Course course = ((Assignment) assignmentsAndEvents.get (domain.getSelectedTableIndex ())).getCourse ();
        for (int i = 0; i < course.getTypeCount (); ++i)
        {
            viewPanel.typeComboModel.addElement (new ComboListItem (course.getType (i).getTypeName (), course.getType (i).getUniqueID ()));
        }
    }

    /**
     * Fills the terms combo box in the courses tab of the Settings dialog.
     */
    public int loadSettingsTermCombo()
    {
        viewPanel.termsAndCoursesDialog.termComboModel.removeAllElements ();
        for (int i = 0; i < terms.size (); ++i)
        {
            viewPanel.termsAndCoursesDialog.termComboModel.addElement (new ComboListItem (terms.get (i).getTypeName (), terms.get (i).getUniqueID ()));
        }
        return viewPanel.termsAndCoursesDialog.termComboModel.getSize ();
    }

    /**
     * Fills the terms combo box in the Grades dialog.
     */
    public void loadGradesTermCombo()
    {
        viewPanel.gradesDialog.gradesTermsComboModel.removeAllElements ();
        for (int i = 0; i < terms.size (); ++i)
        {
            viewPanel.gradesDialog.gradesTermsComboModel.addElement (new ComboListItem (terms.get (i).getTypeName (), terms.get (i).getUniqueID ()));
        }
        if (viewPanel.gradesDialog.gradesTermsComboModel.getSize () == 0)
        {
            viewPanel.gradesDialog.gradesTermsComboModel.addElement (new ComboListItem ("-" + viewPanel.domain.language.getString ("none") + "-", -1));
            viewPanel.gradesDialog.graphPanel.setTerm (null);
        }
    }

    /**
     * Fills the terms list in the terms tab of the Settings dialog.
     */
    public void loadSettingsTermTable()
    {
        viewPanel.termsAndCoursesDialog.termTableModel.removeAllRows ();
        for (int i = 0; i < terms.size (); ++i)
        {
            viewPanel.termsAndCoursesDialog.termTableModel.addRow (new Object[]
                    {
                        terms.get (i).getTypeName (), terms.get (i).getUniqueID ()
                    });
        }
        viewPanel.termsAndCoursesDialog.settingsTermsTable.refreshTable ();
    }

    /**
     * Fills the courses list in the courses tab of the Settings dialog.
     */
    public void loadSettingsCourseTable()
    {
        viewPanel.termsAndCoursesDialog.courseTableModel.setColumnName (0, viewPanel.domain.language.getString ("course"));
        viewPanel.termsAndCoursesDialog.courseTableModel.setColumnName (1, viewPanel.domain.language.getString ("term"));
        viewPanel.termsAndCoursesDialog.courseTableModel.removeAllRows ();
        for (int i = 0; i < courses.size (); ++i)
        {
            viewPanel.termsAndCoursesDialog.courseTableModel.addRow (new Object[]
                    {
                        courses.get (i).getTypeName (), courses.get (i).getTerm ().getTypeName (), courses.get (i).getUniqueID ()
                    });
        }
        viewPanel.termsAndCoursesDialog.settingsCoursesTable.refreshTable ();
    }

    /**
     * Fills the textbooks list in the courses tab of the Settings dialog with
     * the textbooks from the selected course.
     */
    public void loadSettingsTextbookTable()
    {
        Course course = courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
        viewPanel.termsAndCoursesDialog.textbookTableModel.removeAllRows ();
        for (int i = 0; i < course.getTextbookCount (); ++i)
        {
            viewPanel.termsAndCoursesDialog.textbookTableModel.addRow (new Object[]
                    {
                        course.getTextbook (i).getTypeName (), course.getTextbook (i).getUniqueID ()
                    });
        }
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.refreshTable ();
    }

    /**
     * Fills the types list in the courses tab of the Settings dialog with the
     * types from the selected course.
     */
    public void loadSettingsTypeTable()
    {
        Course course = courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
        viewPanel.termsAndCoursesDialog.typeTableModel.setColumnName (0, viewPanel.domain.language.getString ("name"));
        viewPanel.termsAndCoursesDialog.typeTableModel.setColumnName (1, viewPanel.domain.language.getString ("weight"));
        viewPanel.termsAndCoursesDialog.typeTableModel.removeAllRows ();
        for (int i = 0; i < course.getTypeCount (); ++i)
        {
            viewPanel.termsAndCoursesDialog.typeTableModel.addRow (new Object[]
                    {
                        course.getType (i).getTypeName (), course.getType (i).getWeight (), course.getType (i).getUniqueID ()
                    });
        }
        viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
    }

    /**
     * Fills the instructors list in the courses tab of the Settings dialog with
     * the types from the selected course.
     */
    public void loadSettingsInstructorTable()
    {
        Course course = courses.get (viewPanel.termsAndCoursesDialog.settingsCoursesTable.getSelectedRow ());
        viewPanel.termsAndCoursesDialog.instructorTableModel.setColumnName (0, viewPanel.domain.language.getString ("name"));
        viewPanel.termsAndCoursesDialog.instructorTableModel.setColumnName (1, viewPanel.domain.language.getString ("lecture") + viewPanel.domain.language.getString ("lab"));
        viewPanel.termsAndCoursesDialog.instructorTableModel.removeAllRows ();
        for (int i = 0; i < course.getInstructorCount (); ++i)
        {
            viewPanel.termsAndCoursesDialog.instructorTableModel.addRow (new Object[]
                    {
                        course.getInstructor (i).getTypeName (), course.getInstructor (i).getLectureLab (), course.getInstructor (i).getUniqueID ()
                    });
        }
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.refreshTable ();
    }

    /**
     * Removes all courses attached to the given term.
     *
     * @param term The term to remove all courses from.
     */
    public void removeCoursesAttachedTo(Term term)
    {
        int count = term.getCourseCount ();
        for (int i = 0; i < count; ++i)
        {
            Course course = term.getCourse (0);
            removeAssignmentsAttachedTo (course);
            removeInstructorsAttachedTo (course);
            removeTypesAttachedTo (course);
            removeTextbooksAttachedTo (course);
            courses.remove (course);
            term.removeCourse (course);
            domain.currentCourseIndex = -1;
            for (int j = 0; j < viewPanel.termsAndCoursesDialog.courseTableModel.getRowCount (); ++j)
            {
                if (Long.parseLong (viewPanel.termsAndCoursesDialog.courseTableModel.getValueAt (j, 2).toString ()) == course.getUniqueID ())
                {
                    viewPanel.termsAndCoursesDialog.courseTableModel.removeRow (j);
                    viewPanel.termsAndCoursesDialog.settingsCoursesTable.refreshTable ();
                    break;
                }
            }
        }
    }

    /**
     * Scans through all assignments and removes any attachments to this type
     * from them.
     *
     * @param type The type to remove attachments to.
     */
    public void removeAttachmentsToAssignments(AssignmentType type)
    {
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            ListItem item = assignmentsAndEvents.get (i);
            if (item.isAssignment () && ((Assignment) item).getType () == type)
            {
                ((Assignment) item).setType (null);
            }
        }
    }

    /**
     * Scans through all assignments and removes any attachments to this
     * textbook from them.
     *
     * @param textbook The textbook to remove attachments to.
     */
    public void removeAttachmentsToAssignments(Textbook textbook)
    {
        for (int i = 0; i < assignmentsAndEvents.size (); ++i)
        {
            ListItem item = assignmentsAndEvents.get (i);
            if (item.isAssignment () && ((Assignment) item).getTextbook () == textbook)
            {
                ((Assignment) item).setTextbook (null);
            }
        }
    }

    /**
     * Remove all events from a given category.
     *
     * @param category The category to empty of all events.
     * @param month The month currently displayed.
     * @param year The year currently displayed.
     */
    public void removeEventsAttachedTo(Category category, int month, int year)
    {
        domain.assignmentOrEventLoading.push (true);
        int count = category.getEventCount ();
        for (int i = 0; i < count; ++i)
        {
            Event event = category.getEvent (0);
            int index = getAssignmentOrEventIndexByID (event.getUniqueID ());
            viewPanel.assignmentsTableModel.removeRow (index);
            assignmentsAndEvents.remove (event.getEventYear ().removeEvent (category.removeEvent (event)));
            event.getEventYear ().markChanged ();
            if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
            {
                if (Integer.parseInt (event.getDueDate ().split ("/")[0]) - month - 1 == 0 && Integer.parseInt (event.getDueDate ().split ("/")[2]) - year == 0)
                {
                    viewPanel.daysAssignmentsAndEvents[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1].remove (event);
                    viewPanel.daysAssignmentsAndEvents[Integer.parseInt (event.getDueDate ().split ("/")[1]) - 1].invalidate ();
                }
                else if (Integer.parseInt (event.getDueDate ().split ("/")[0]) - month == 0)
                {
                    try
                    {
                        viewPanel.daysOutsideMonth[viewPanel.lastMonthLast - Integer.parseInt (event.getDueDate ().split ("/")[1])].remove (event);
                        viewPanel.daysOutsideMonth[viewPanel.lastMonthLast - Integer.parseInt (event.getDueDate ().split ("/")[1])].invalidate ();
                    }
                    // if the event was not found in this month or the outter-month panels, ignore
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                    }
                }
                else
                {
                    try
                    {
                        viewPanel.daysOutsideMonth[Integer.parseInt (event.getDueDate ().split ("/")[1]) + viewPanel.lastMonthLast - viewPanel.lastMonthFirst].remove (event);
                        viewPanel.daysOutsideMonth[Integer.parseInt (event.getDueDate ().split ("/")[1]) + viewPanel.lastMonthLast - viewPanel.lastMonthFirst].invalidate ();
                    }
                    // if the event was not found in this month or the outter-month panels, ignore
                    catch (ArrayIndexOutOfBoundsException ex)
                    {
                    }

                }
            }
        }
        viewPanel.assignmentsTable.refreshTable ();
        viewPanel.assignmentsTableRowSelected (null);
        domain.assignmentOrEventLoading.pop ();
    }

    /**
     * Removes all assignmentsAndEvents attached to the given course.
     *
     * @param course The course to remove all assignmentsAndEvents from.
     */
    public void removeAssignmentsAttachedTo(Course course)
    {
        domain.assignmentOrEventLoading.push (true);
        int count = course.getAssignmentCount ();
        for (int i = 0; i < count; ++i)
        {
            Assignment assignment = course.getAssignment (0);
            int index = getAssignmentOrEventIndexByID (assignment.getUniqueID ());
            viewPanel.assignmentsTableModel.removeRow (index);
            assignmentsAndEvents.remove (course.removeAssignment (assignment));
            if (viewPanel.middleTabbedPane.getSelectedIndex () == 1)
            {
                try
                {
                    viewPanel.daysAssignmentsAndEvents[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1].remove (assignment);
                    viewPanel.daysAssignmentsAndEvents[Integer.parseInt (assignment.getDueDate ().split ("/")[1]) - 1].invalidate ();
                }
                // if this exception is caught, it only means the assignment is in a month not displayed, so it should be ignored
                catch (ArrayIndexOutOfBoundsException ex)
                {
                }
            }
        }
        viewPanel.assignmentsTable.refreshTable ();
        viewPanel.assignmentsTableRowSelected (null);
        domain.assignmentOrEventLoading.pop ();
    }

    /**
     * Reverts all events attached to a specific category back to the default
     * event.
     *
     * @param category The category to revert from.
     */
    public void revertEvents(Category category)
    {
        domain.assignmentOrEventLoading.push (true);
        int count = category.getEventCount ();
        for (int i = 0; i < count; ++i)
        {
            Event event = category.getEvent (i);
            event.setCategory (preferences.categories.get (0));
            preferences.categories.get (0).addEvent (event);
            event.getEventYear ().markChanged ();
            viewPanel.refreshAssignmentsRowAt (viewPanel.assignmentsTable.getSelectableRowFromVectorIndex (getAssignmentOrEventIndexByID (event.getUniqueID ())));
            event.refreshText ();
        }
        viewPanel.assignmentsTable.refreshTable ();
        viewPanel.assignmentsTableRowSelected (null);
        domain.assignmentOrEventLoading.pop ();
    }

    /**
     * Removes all types attached to the given course.
     *
     * @param course The course to remove the types from.
     */
    public void removeTypesAttachedTo(Course course)
    {
        domain.typeLoading.push (true);
        int count = course.getTypeCount ();
        for (int i = 0; i < count; ++i)
        {
            AssignmentType type = course.getType (0);
            types.remove (course.removeType (type));
        }
        viewPanel.termsAndCoursesDialog.typeTableModel.removeAllRows ();
        viewPanel.termsAndCoursesDialog.settingsTypesTable.refreshTable ();
        domain.typeLoading.pop ();
    }

    /**
     * Removes all instructors attached to the given course.
     *
     * @param course The course to remove the instructors from.
     */
    public void removeInstructorsAttachedTo(Course course)
    {
        domain.instructorLoading.push (true);
        int count = course.getInstructorCount ();
        for (int i = 0; i < count; ++i)
        {
            Instructor instructor = course.getInstructor (0);
            instructors.remove (course.removeInstructor (instructor));
        }
        viewPanel.termsAndCoursesDialog.instructorTableModel.removeAllRows ();
        viewPanel.termsAndCoursesDialog.settingsInstructorsTable.refreshTable ();
        domain.instructorLoading.pop ();
    }

    /**
     * Removes all textbooks attached to the given course.
     *
     * @param course The course to remove the textbooks from.
     */
    public void removeTextbooksAttachedTo(Course course)
    {
        domain.textbookLoading.push (true);
        int count = course.getTextbookCount ();
        for (int i = 0; i < count; ++i)
        {
            Textbook textbook = course.getTextbook (0);
            textbooks.remove (course.removeTextbook (textbook));
        }
        viewPanel.termsAndCoursesDialog.textbookTableModel.removeAllRows ();
        viewPanel.termsAndCoursesDialog.settingsTextbooksTable.refreshTable ();
        domain.textbookLoading.pop ();
    }

    /**
     * Load all information into the data vector.
     */
    public abstract void load();

    /**
     * Save all information contained in the data vectors.
     */
    public abstract void save();

    /**
     * Load user details into the data vector.
     */
    public abstract void loadUserDetails();

    /**
     * Save user details from the data vector.
     */
    public abstract void saveUserDetails();

    /**
     * Load preferences into the data vector.
     */
    public abstract void loadPreferences();

    /**
     * Save preferences from the data vector.
     */
    public abstract void savePreferences();

    /**
     * Load terms to the data vector.
     */
    public abstract void loadTerms();

    /**
     * Save terms from the data vector.
     */
    public abstract void saveTerms();

    /**
     * Load courses to the data vector.
     */
    public abstract void loadCourses();

    /**
     * Save courses from the data vector.
     */
    public abstract void saveCourses();

    /**
     * Load types to the data vector.
     */
    public abstract void loadTypes();

    /**
     * Save types from the data vector.
     */
    public abstract void saveTypes();

    /**
     * Load instructors to the data vector.
     */
    public abstract void loadInstructors();

    /**
     * Save instructors from the data vector.
     */
    public abstract void saveInstructors();

    /**
     * Load textbooks to the data vector.
     */
    public abstract void loadTextbooks();

    /**
     * Save textbooks from the data vector.
     */
    public abstract void saveTextbooks();

    /**
     * Load assignments to the data vector.
     */
    public abstract void loadAssignments();

    /**
     * Load events to the data vector.
     */
    public abstract void loadEvents();

    /**
     * Load themes to the data vector.
     */
    public abstract void loadThemes(DefaultComboBoxModel model);

    /**
     * Saves assignments from the data vector.
     */
    public abstract void saveAssignments(Course course);

    /**
     * Save events from the data vector.
     */
    public abstract void saveEvents(EventYear eventYear);
}
