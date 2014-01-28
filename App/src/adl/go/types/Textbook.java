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

package adl.go.types;

import adl.go.resource.LocalUtility;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Textbooks are tied directly to a course and indirectly to individual
 * assignmentsAndEvents within that course.
 *
 * @author Alex Laird
 */
public class Textbook extends ExtendedTreeNode
{
    /**
     * The unique ID of the course this textbook is attached to.
     */
    protected long courseID;
    /**
     * A reference to the containing course.
     */
    private Course course;
    /**
     * The author of this textbook.
     */
    private String author = "";
    /**
     * The publisher of this textbook.
     */
    private String publisher = "";
    /**
     * The ISBN number of this textbook.
     */
    private String isbn = "";
    /**
     * The online source (should be a web address) the textbook can be found at.
     */
    private String source = "";
    /**
     * The price of the textbook.
     */
    private String price = "";
    /**
     * The condition of the textbook.
     */
    private String condition = "";
    /**
     * The contact email for this textbook.
     */
    private String email = "";
    /**
     * The received state of the textbook.
     */
    private boolean isReceived = false;
    /**
     * The ordered state of the textbook.
     */
    private boolean isOrdered = false;

    /**
     * Constructs a textbook with a given name, unique ID, and containing
     * course.
     *
     * @param name The name of the textbook.
     * @param id The unique ID of the textbook.
     * @param course The containing course.
     * @param utility The reference to the utility resource.
     */
    public Textbook(String name, long id, Course course, LocalUtility utility)
    {
        super (name, id, utility);
        setCourse (course);
    }

    /**
     * Parses a single input string into every attribute's initial state for
     * this object--this is specifically used by the loading methods from the
     * data file.
     *
     * @param parse The string of all data to be used for initialization.
     * @param utility The reference to the utility resource.
     */
    public Textbook(String parse, LocalUtility utility)
    {
        super ("", -1, utility);
        Scanner scan = new Scanner (parse).useDelimiter ("(?<!\\\\)" + SEPARATOR + "|" + "(?<!\\\\)" + END_OF_LINE);
        try
        {
            setTypeName (scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE));
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            setUniqueID (scan.nextLong ());
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            courseID = scan.nextLong ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            author = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            publisher = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            isbn = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            source = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            price = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            condition = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            email = scan.next ().replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            isOrdered = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        try
        {
            isReceived = scan.nextBoolean ();
        }
        catch (NoSuchElementException ex)
        {
            utility.domain.needsCoursesAndTermsSave = true;
        }
        scan.close ();
    }

    /**
     * Retrieves the unique ID of the course this textbook is attached to.
     *
     * @return The unique ID of this textbook's course.
     */
    public long getCourseID()
    {
        return courseID;
    }

    /**
     * Retreives the containing course.
     *
     * @return The containing course.
     */
    public Course getCourse()
    {
        return course;
    }

    /**
     * Sets the containing course.
     *
     * @param course The containing course to be set to.
     */
    public final void setCourse(Course course)
    {
        this.course = course;
    }

    /**
     * Retrieves the author for this textbook.
     *
     * @return The author for this textbook.
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Sets the author of this textbook.
     *
     * @param author The author to be set.
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * Retrieves the publisher for this textbook.
     *
     * @return The publisher for this textbook.
     */
    public String getPublisher()
    {
        return publisher;
    }

    /**
     * Sets the publisher of this textbook.
     *
     * @param publisher The publisher to be set.
     */
    public void setPublisher(String publisher)
    {
        this.publisher = publisher;
    }

    /**
     * Retrieves the ISBN for this textbook.
     *
     * @return The ISBN for this textbook.
     */
    public String getISBN()
    {
        return isbn;
    }

    /**
     * Sets the ISBN of this textbook.
     *
     * @param isbn The ISBN to be set.
     */
    public void setISBN(String isbn)
    {
        this.isbn = isbn;
    }

    /**
     * Retrieves the source for this textbook.
     *
     * @return The source for this textbook.
     */
    public String getSource()
    {
        return source;
    }

    /**
     * Sets the source of this textbook.
     *
     * @param source The source to be set.
     */
    public void setSource(String source)
    {
        this.source = source;
    }

    /**
     * Retrieves the price for this textbook.
     *
     * @return The price for this textbook.
     */
    public String getPrice()
    {
        return price;
    }

    /**
     * Sets the ordered state of the textbook.
     *
     * @param isOrdered The ordered state of the textbook.
     */
    public void setIsOrdered(boolean isOrdered)
    {
        this.isOrdered = isOrdered;
    }

    /**
     * Retrieves the ordered state of the textbook.
     *
     * @return The ordered state of the textbook.
     */
    public boolean isOrdered()
    {
        return isOrdered;
    }

    /**
     * Sets the received state of the textbook.
     *
     * @param isReceived The received state of the textbook.
     */
    public void setIsReceived(boolean isReceived)
    {
        this.isReceived = isReceived;
    }

    /**
     * Retrieves the received state of the textbook.
     *
     * @return The received state of the textbook.
     */
    public boolean isReceived()
    {
        return isReceived;
    }

    /**
     * Sets the price of this textbook.
     *
     * @param price The price to be set.
     */
    public void setPrice(String price)
    {
        this.price = price;
    }

    /**
     * Sets the contact email of this textbook.
     *
     * @param email The contact email to be set.
     */
    public void setContactEmail(String email)
    {
        this.email = email;
    }

    /**
     * Retrieves the contact email for this textbook.
     *
     * @return The contact email for this textbook.
     */
    public String getContactEmail()
    {
        return email;
    }

    /**
     * Retrieves the condition for this textbook.
     *
     * @return The condition for this textbook.
     */
    public String getCondition()
    {
        return condition;
    }

    /**
     * Sets the condition of this textbook.
     *
     * @param condition The condition to be set.
     */
    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    /**
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    public String out()
    {
        return getTypeName ().replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + getUniqueID () + SEPARATOR
               + getCourse ().getUniqueID () + SEPARATOR
               + author.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + publisher.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + isbn.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + source.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + price.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + condition.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + email.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + isOrdered + SEPARATOR
               + isReceived;
    }
}
