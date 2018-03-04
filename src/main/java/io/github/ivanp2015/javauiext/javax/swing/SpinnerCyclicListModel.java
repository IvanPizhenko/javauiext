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

import java.util.List;
import javax.swing.SpinnerListModel;

/**
 * Cyclic list model for {@link javax.swing.JSpinner}. Wraps at both ends.
 * 
 * @see javax.swing.JSpinner
 * @see javax.swing.SpinnerModel
 * @see javax.swing.AbstractSpinnerModel
 * @see javax.swing.SpinnerListModel
 * @see javax.swing.SpinnerNumberModel
 * @see javax.swing.SpinnerDateModel
 * 
 * @author Ivan Pizhenko.
 * @since 0.0.1
 */
public class SpinnerCyclicListModel extends SpinnerListModel {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs new object of class {@code SpinnerCyclicListModel}.
     * See {@link SpinnerListModel#SpinnerListModel()} for more details.
     * 
     * @see SpinnerListModel#SpinnerListModel()
     */
    public SpinnerCyclicListModel() {}

    /**
     * Constructs new object of class {@code SpinnerCyclicListModel}.
     * See {@link SpinnerListModel#SpinnerListModel(List)} for more details.
     * 
     * @param values list of values.
     * @see SpinnerListModel#SpinnerListModel(List)
     */
    public SpinnerCyclicListModel(List<?> values) {
        super(values);
    }

    /**
     * Constructs new object of class {@code SpinnerCyclicListModel}.
     * See {@link SpinnerListModel#SpinnerListModel(Object[])} for more details.
     *
     * @param values array of values.
     * @see SpinnerListModel#SpinnerListModel(Object[])
     */
    public SpinnerCyclicListModel(Object[] values) {
        super(values);
    }

    /**
     * Returns previous value or wraps to a last value in the list,
     * if values are exhausted.
     *
     * @return Previous value.
     * @see SpinnerListModel#getPreviousValue()
     */
    @Override
    public Object getPreviousValue() {
        final Object v = super.getPreviousValue();
        return v == null ? getList().get(getList().size() - 1): v;
    }

    /**
     * Returns next value or wraps to a first value in the list, 
     * if values are exhausted.
     *
     * @return Next value.
     * @see SpinnerListModel#getNextValue()
     */
    @Override
    public Object getNextValue() {
        final Object v = super.getNextValue();
        return v == null ? getList().get(0) : v;
    }
}
