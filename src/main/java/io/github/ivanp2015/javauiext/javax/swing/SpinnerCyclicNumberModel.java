/*
 * Java UI Extensions project.
 * https://ivanp2015.github.io/javauiext
 * 
 * Copyright (c) 2018, Ivan Pizhenko. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Ivan Pizhenko designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Ivan Pizhenko in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package io.github.ivanp2015.javauiext.javax.swing;

import javax.swing.SpinnerNumberModel;

/**
 * Cyclic number model for {@link javax.swing.JSpinner}. Wraps at both ends.
 *
 * @see javax.swing.JSpinner
 * @see javax.swing.SpinnerModel
 * @see javax.swing.AbstractSpinnerModel
 * @see javax.swing.SpinnerListModel
 * @see javax.swing.SpinnerNumberModel
 * @see javax.swing.SpinnerDateModel
 *
 * @author Ivan Pizhenko
 * @since 0.0.1
 */
public class SpinnerCyclicNumberModel extends SpinnerNumberModel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs new object of class {@code CyclicSpinnerNumberModel}.
     * See more details at {@link javax.swing.SpinnerNumberModel#SpinnerNumberModel()}.
     *
     * @see javax.swing.SpinnerNumberModel#SpinnerNumberModel()
     */
    public SpinnerCyclicNumberModel() {}

    /**
     * Constructs new object of class {@code CyclicSpinnerNumberModel}.
     * See more details at {@link javax.swing.SpinnerNumberModel#SpinnerNumberModel(double, double, double, double)}.
     *
     * @param value initial value.
     * @param minimum minimum value.
     * @param maximum maximum value.
     * @param stepSize value step.
     * @see javax.swing.SpinnerNumberModel#SpinnerNumberModel(double, double, double, double)
     */
    public SpinnerCyclicNumberModel(double value, double minimum, double maximum,
            double stepSize) {
        super(value, minimum, maximum, stepSize);
    }

    /**
     * Constructs new object of class {@code CyclicSpinnerNumberModel}.
     * See more details at {@link javax.swing.SpinnerNumberModel#SpinnerNumberModel(int, int, int, int)}.
     * 
     * @param value initial value.
     * @param minimum minimum value.
     * @param maximum maximum value.
     * @param stepSize value step.
     * @see javax.swing.SpinnerNumberModel#SpinnerNumberModel(int, int, int, int)
     */
    public SpinnerCyclicNumberModel(int value, int minimum, int maximum, int stepSize) {
        super(value, minimum, maximum, stepSize);
    }

    /**
     * Constructs new object of class {@code CyclicSpinnerNumberModel}.
     * See more details at {@link javax.swing.SpinnerNumberModel#SpinnerNumberModel(Number, Comparable, Comparable, Number)}.
     *
     * @param value initial value.
     * @param minimum minimum value.
     * @param maximum maximum value.
     * @param stepSize value step.
     * @see javax.swing.SpinnerNumberModel#SpinnerNumberModel(Number, Comparable, Comparable, Number)
     */
    public SpinnerCyclicNumberModel(Number value, Comparable<?> minimum, Comparable<?> maximum,
            Number stepSize) {
        super(value, minimum, maximum, stepSize);
    }

    /**
     * Returns next value or wraps to a maximum value, if values are exhausted.
     *
     * @return next value.
     * @see SpinnerNumberModel#getPreviousValue()
     */
    @Override
    public Object getPreviousValue() {
        final Object v = super.getPreviousValue();
        return v == null ? getMaximum(): v;
    }

    /**
     * Returns next value or wraps to a minimum value, if values are exhausted.
     *
     * @return next value.
     * @see SpinnerNumberModel#getNextValue()
     */
    @Override
    public Object getNextValue() {
        final Object v = super.getNextValue();
        return v == null ? getMinimum() : v;
    }
}
