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
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * A JPanel that can implement a colored gradient.
 *
 * @author Alex Laird
 */
public class ColoredJPanel extends JPanel
{
    /**
     * The selected gradient style for this component.
     */
    private GradientStyle style = GradientStyle.NO_GRADIENT;
    /**
     * The first color of the gradient.
     */
    private Color color1 = new Color (238, 238, 238);
    /**
     * The color to gradient to.
     */
    private Color color2 = null;

    /**
     * The empty constructor is required for Bean display.
     */
    public ColoredJPanel()
    {
    }

    /**
     * Construct a gradient component with the given style.
     *
     * @param style The style to set for this gradient component.
     * @param color The color for the first color.
     */
    public ColoredJPanel(GradientStyle style, Color color)
    {
        this.style = style;
        color1 = color;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth ();
        int h = getHeight ();

        if (color2 == null)
        {
            int red = color1.getRed ();
            int green = color1.getGreen ();
            int blue = color1.getBlue ();
            color2 = new Color (red - 30, green - 30, blue - 30);
        }

        if (style == style.NO_GRADIENT)
        {
            color2 = color1;
            GradientPaint gp = new GradientPaint (0, 0, color1, 0, h, color2);

            g2d.setPaint (gp);
            g2d.fillRect (0, 0, w, h);
        }
        else if (style == style.HORIZONTAL_GRADIENT_RIGHT)
        {
            GradientPaint gp = new GradientPaint (0, 0, color1, w, 0, color2);

            g2d.setPaint (gp);
            g2d.fillRect (0, 0, w, h);
        }
        else if (style == style.HORIZONTAL_GRADIENT_LEFT)
        {
            GradientPaint gp = new GradientPaint (w, 0, color1, 0, 0, color2);

            g2d.setPaint (gp);
            g2d.fillRect (0, 0, w, h);
        }
        else if (style == style.VERTICAL_GRADIENT_DOWN)
        {
            GradientPaint gp = new GradientPaint (0, 0, color1, 0, h, color2);

            g2d.setPaint (gp);
            g2d.fillRect (0, 0, w, h);
        }
        else if (style == style.VERTICAL_GRADIENT_UP)
        {
            GradientPaint gp = new GradientPaint (0, h, color1, 0, 0, color2);

            g2d.setPaint (gp);
            g2d.fillRect (0, 0, w, h);
        }

        setOpaque (false);
        super.paintComponent (g);
    }

    @Override
    public void setBackground(Color color)
    {
        color1 = color;
        color2 = null;
        repaint ();
    }

    /**
     * Set the first and second colors for the gradient background.
     *
     * @param color1 The first color of the gradient.
     * @param color2 The second color of the gradient.
     */
    public void setBackground(Color color1, Color color2)
    {
        this.color1 = color1;
        this.color2 = color2;
        repaint ();
    }

    @Override
    public Color getBackground()
    {
        return color1;
    }

    /**
     * Retrieve the second background color for the gradient.
     *
     * @return The color for the second background gradient.
     */
    public Color getBackground2()
    {
        return color2;
    }
}
