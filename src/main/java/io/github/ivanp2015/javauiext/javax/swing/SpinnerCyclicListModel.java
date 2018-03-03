/*
Java UI Extensions project.
https://ivanp2015.github.io/javauiext

Copyright (c) 2018, Ivan Pizhenko. All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package io.github.ivanp2015.javauiext.javax.swing;

import java.util.List;
import javax.swing.SpinnerListModel;

/**
 * Cyclic list model for spinner.
 * Wraps at ends.
 * @author Ivan Pizhenko.
 */
public class SpinnerCyclicListModel extends SpinnerListModel {
    /**
     * Initializes new object of class <code>SpinnerCyclicListModel</code>.
     */
    public SpinnerCyclicListModel() {}

    /**
     * Initializes new object of class <code>SpinnerCyclicListModel</code>.
     * @param values List of values.
     */
    public SpinnerCyclicListModel(List<?> values) {
        super(values);
    }

    /**
     * Initializes new object of class <code>SpinnerCyclicListModel</code>.
     * @param values Array of values.
     */
    public SpinnerCyclicListModel(Object[] values) {
        super(values);
    }
    
    /**
     * Returns next value or wraps up to a last value in the list,
     * if values are exhausted.
     * @see SpinnerListModel#getPreviousValue()
     * @return Previous value.
     */
    @Override
    public Object getPreviousValue() {
        final Object v = super.getPreviousValue();
        return v == null ? getList().get(getList().size() - 1): v;
    }

    /**
     * Returns next value or wraps down to a first value in the list, 
     * if values are exhausted.
     * @see SpinnerListModel#getNextValue()
     * @return Next value.
     */
    @Override
    public Object getNextValue() {
        final Object v = super.getNextValue();
        return v == null ? getList().get(0) : v;
    }
}
