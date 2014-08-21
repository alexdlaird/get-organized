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

/**
 * This class contains all user detail information
 *
 * @author Alex Laird
 */
public class UserDetails
{
    /**
     * The separator character sequence.
     */
    protected final String SEPARATOR = LocalUtility.getSeparator ();
    /**
     * The end of line character sequence.
     */
    protected final String END_OF_LINE = LocalUtility.getEndOfLine ();
    /**
     * The name of the user.
     */
    protected String studentName = "";
    /**
     * The email address of the student.
     */
    protected String email = "";
    /**
     * The user's school name.
     */
    protected String school = "";
    /**
     * The student's ID number.
     */
    protected String idNumber = "";
    /**
     * The student's box number.
     */
    protected String boxNumber = "";
    /**
     * The student's major(s).
     */
    protected String majors = "";
    /**
     * The student's concentration(s).
     */
    protected String concentrations = "";
    /**
     * The student's minor(s).
     */
    protected String minors = "";
    /**
     * The student's advisor's name.
     */
    protected String advisorName = "";
    /**
     * The student's advisor's email address.
     */
    protected String advisorEmail = "";
    /**
     * The student's advisor's phone number.
     */
    protected String advisorPhone = "";
    /**
     * The student's advisor's office hours.
     */
    protected String advisorOfficeHours = "";
    /**
     * The student's advisor's office location.
     */
    protected String advisorOfficeLocation = "";

    /**
     * Set all user detail values with the given string.
     *
     * @param userDetailsString The string to set preferences with.
     */
    public void setWithString(String userDetailsString)
    {
        String[] split = userDetailsString.split (",");
        try
        {
            studentName = split[0].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            school = split[1].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            idNumber = split[2].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            boxNumber = split[3].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            majors = split[4].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            concentrations = split[5].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            minors = split[6].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            advisorName = split[7].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            advisorEmail = split[8].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            advisorPhone = split[9].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            advisorOfficeHours = split[10].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            advisorOfficeLocation = split[11].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
        try
        {
            email = split[12].replaceAll ("\\\\" + SEPARATOR, SEPARATOR).replaceAll ("\\\\" + END_OF_LINE, END_OF_LINE);
        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
        }
    }

    public String getStudentName()
    {
        return studentName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getSchool()
    {
        return school;
    }

    public String getIdNumber()
    {
        return idNumber;
    }

    public String getBoxNumber()
    {
        return boxNumber;
    }

    public String getMajors()
    {
        return majors;
    }

    public String getConcentrations()
    {
        return concentrations;
    }

    public String getMinors()
    {
        return minors;
    }

    public String getAdvisorName()
    {
        return advisorName;
    }

    public String getAdvisorEmail()
    {
        return advisorEmail;
    }

    public String getAdvisorPhone()
    {
        return advisorPhone;
    }

    public String getAdvisorOfficeHours()
    {
        return advisorOfficeHours;
    }

    public String getAdvisorsOfficeLocation()
    {
        return advisorOfficeLocation;
    }

    public void setStudentName(String studentName)
    {
        this.studentName = studentName;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setSchool(String school)
    {
        this.school = school;
    }

    public void setIdNumber(String idNumber)
    {
        this.idNumber = idNumber;
    }

    public void setBoxNumber(String boxNumber)
    {
        this.boxNumber = boxNumber;
    }

    public void setMajors(String majors)
    {
        this.majors = majors;
    }

    public void setConcentrations(String concentrations)
    {
        this.concentrations = concentrations;
    }

    public void setMinors(String minors)
    {
        this.minors = minors;
    }

    public void setAdvisorName(String advisorName)
    {
        this.advisorName = advisorName;
    }

    public void setAdvisorEmail(String advisorEmail)
    {
        this.advisorEmail = advisorEmail;
    }

    public void setAdvisorPhone(String advisorPhone)
    {
        this.advisorPhone = advisorPhone;
    }

    public void setAdvisorOfficeHours(String advisorOfficeHours)
    {
        this.advisorOfficeHours = advisorOfficeHours;
    }

    public void setAdvisorOfficeLocation(String advisorOfficeLocation)
    {
        this.advisorOfficeLocation = advisorOfficeLocation;
    }

    /**
     * Returns a string of all components in this object that is formatted that
     * the file reader/writer will cooperate with it.
     *
     * @return The formatted output string.
     */
    public String out()
    {
        return studentName.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + school.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + idNumber.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + boxNumber.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + majors.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + concentrations.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + minors.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + advisorName.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + advisorEmail.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + advisorPhone.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + advisorOfficeHours.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + advisorOfficeLocation.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE) + SEPARATOR
               + email.replaceAll (SEPARATOR, "\\\\" + SEPARATOR).replaceAll (END_OF_LINE, "\\\\" + END_OF_LINE);
    }
}
