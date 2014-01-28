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

/**
 * The coloring constants for a colored component.
 *
 * @author Alex Laird
 */
public interface ColoredComponent
{
    /**
     * Available gradient styles for this component.
     */
    public enum GradientStyle
    {
        NO_GRADIENT,
        HORIZONTAL_GRADIENT_RIGHT,
        HORIZONTAL_GRADIENT_LEFT,
        VERTICAL_GRADIENT_DOWN,
        VERTICAL_GRADIENT_UP
    }
}
