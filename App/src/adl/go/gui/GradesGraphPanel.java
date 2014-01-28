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

import adl.go.resource.LocalUtility;
import adl.go.types.Course;
import adl.go.types.Term;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * The grades panel is shown in the dialog. It is dynamically generated with
 * grade information for the currently selected term each time the UI is
 * updated.
 *
 * @author Alex Laird
 */
public class GradesGraphPanel extends JPanel
{
    /**
     * The left pad.
     */
    private final int LEFT_PAD = 40;
    /**
     * The right pad.
     */
    private final int RIGHT_PAD = 20;
    /**
     * The upper pad.
     */
    private final int UPPER_PAD = 10;
    /**
     * The lower pad.
     */
    private final int LOWER_PAD = 20;
    /**
     * The average pad distance.
     */
    private final int AVG_PAD = 20;
    /**
     * The currently selected term to display grades for.
     */
    private Term currentTerm;
    /**
     * A reference to the utility.
     */
    private LocalUtility utility;
    /**
     * The list of courses containing a list of each courses graded assignments.
     */
    private ArrayList<Object[]> courses = new ArrayList<Object[]> ();

    /**
     * Retrieve the currently displayed term.
     *
     * @return The currently displayed term.
     */
    public Term getTerm()
    {
        return currentTerm;
    }

    public void setUtility(LocalUtility utility)
    {
        this.utility = utility;
    }

    /**
     * Set the currently selected term.
     *
     * @param currentTerm
     */
    public void setTerm(Term currentTerm)
    {
        this.currentTerm = currentTerm;
    }

    /**
     * Rebuild the panel with grade information pertaining to the currently
     * selected term.
     *
     * @param g The graphics for the panel.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent (g);

        if (currentTerm != null)
        {
            // fill the courses list with their respective values for plotting
            courses.clear ();
            for (int i = 0; i < currentTerm.getCourseCount (); ++i)
            {
                Course course = currentTerm.getCourse (i);
                ArrayList<Double> plotPoints = new ArrayList<Double> ();
                ArrayList<Long> dayNum = new ArrayList<Long> ();
                for (int j = 0; j < course.getGradedAssignmentCount (); ++j)
                {
                    int index = j;
                    try
                    {
                        while (index + 1 < course.getGradedAssignmentCount ()
                               && course.getDayNumAtPoint (index) == course.getDayNumAtPoint (index + 1))
                        {
                            ++index;
                        }
                    }
                    catch (ParseException ex)
                    {
                        Domain.LOGGER.add (ex);
                    }

                    plotPoints.add (course.calculateGradeAtPoint (index));

                    try
                    {
                        dayNum.add (course.getDayNumAtPoint (index));
                    }
                    catch (ParseException ex)
                    {
                        dayNum.add ((long) index);
                        Domain.LOGGER.add (ex);
                    }
                }
                courses.add (new Object[]
                        {
                            plotPoints, dayNum
                        });
            }
        }

        // build graphics
        Graphics2D g2 = (Graphics2D) g;

        if (utility != null)
        {
            g2.setFont (utility.currentTheme.fontBold11);
            g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING,
                                 RenderingHints.VALUE_ANTIALIAS_ON);
        }

        // draw the vertical lines
        g2.draw (new Line2D.Double (LEFT_PAD, UPPER_PAD, LEFT_PAD, getHeight () - LOWER_PAD));
        g2.draw (new Line2D.Double (getWidth () - RIGHT_PAD, UPPER_PAD, getWidth () - RIGHT_PAD, getHeight () - LOWER_PAD));

        // draw the horizontal lines
        g2.draw (new Line2D.Double (LEFT_PAD, getHeight () - LOWER_PAD, getWidth () - RIGHT_PAD, getHeight () - LOWER_PAD));
        g2.draw (new Line2D.Double (LEFT_PAD, UPPER_PAD, getWidth () - RIGHT_PAD, UPPER_PAD));

        // draw labels
        Font font = g2.getFont ();
        FontRenderContext frc = g2.getFontRenderContext ();
        LineMetrics lm = font.getLineMetrics ("0", frc);

        // draw blank graph and grid lines
        g2.drawString ("100%", 2, LOWER_PAD - 5);
        g2.drawString ("90%", 10, LOWER_PAD - 5 + (getHeight () - UPPER_PAD - LOWER_PAD) / 10);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10), getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10)));
        g2.setPaint (Color.BLACK);
        g2.drawString ("80%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 2);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 2, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 2));
        g2.setPaint (Color.BLACK);
        g2.drawString ("70%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 3);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 3, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 3));
        g2.setPaint (Color.BLACK);
        g2.drawString ("60%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 4);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 4, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 4));
        g2.setPaint (Color.BLACK);
        g2.drawString ("50%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 5);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 5, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 5));
        g2.setPaint (Color.BLACK);
        g2.drawString ("40%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 6);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 6, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 6));
        g2.setPaint (Color.BLACK);
        g2.drawString ("30%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 7);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 7, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 7));
        g2.setPaint (Color.BLACK);
        g2.drawString ("20%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 8);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 8, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 8));
        g2.setPaint (Color.BLACK);
        g2.drawString ("10%", 10, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 9);
        g2.setPaint (Color.LIGHT_GRAY);
        g2.draw (new Line2D.Double (41, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 9, getWidth () - RIGHT_PAD - 1, LOWER_PAD - 10 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 9));
        g2.setPaint (Color.BLACK);
        g2.drawString ("0%", 18, LOWER_PAD - 5 + ((getHeight () - UPPER_PAD - LOWER_PAD) / 10) * 10);

        if (currentTerm != null)
        {
            float yBase = getHeight () - LOWER_PAD + (LOWER_PAD - lm.getAscent () + lm.getDescent ()) / 2 + lm.getAscent ();
            g2.drawString (currentTerm.getStartDate (), 10, yBase);
            g2.drawString (currentTerm.getEndDate (), getWidth () - (float) font.getStringBounds (currentTerm.getEndDate (), frc).getWidth () - 10, yBase);

            // draw graph lines
            for (int i = 0; i < courses.size (); ++i)
            {
                ArrayList<Double> plotPoints = (ArrayList<Double>) courses.get (i)[0];
                ArrayList<Long> dayNum = (ArrayList<Long>) courses.get (i)[1];

                double xInc;
                try
                {
                    xInc = (double) (getWidth () - 3 * RIGHT_PAD) / currentTerm.getDayCount ();
                }
                catch (ParseException ex)
                {
                    xInc = (double) (getWidth () - 3 * RIGHT_PAD) / (plotPoints.size () - 1);
                    Domain.LOGGER.add (ex);
                }
                double scale = (double) (getHeight () - (UPPER_PAD + LOWER_PAD));
                g2.setPaint (currentTerm.getCourse (i).getColor ());
                // draw graph lines to each assignment point
                for (int j = 0; j < plotPoints.size () - 1; ++j)
                {
                    double x1 = LEFT_PAD + dayNum.get (j) * xInc;
                    double y1 = getHeight () - LOWER_PAD - scale * (plotPoints.get (j) / 100);
                    double x2 = LEFT_PAD + dayNum.get (j + 1) * xInc;
                    double y2 = getHeight () - LOWER_PAD - scale * (plotPoints.get (j + 1) / 100);
                    g2.draw (new Line2D.Double (x1, y1, x2, y2));
                }

                // mark individual assignment grade points
                g2.setPaint (currentTerm.getCourse (i).getColor ());
                for (int j = 0; j < plotPoints.size (); ++j)
                {
                    double x = LEFT_PAD + dayNum.get (j) * xInc;
                    double y = getHeight () - LOWER_PAD - scale * (plotPoints.get (j) / 100);
                    g2.fill (new Ellipse2D.Double (x - 2, y - 2, 4, 4));
                }
            }
        }
    }
}
