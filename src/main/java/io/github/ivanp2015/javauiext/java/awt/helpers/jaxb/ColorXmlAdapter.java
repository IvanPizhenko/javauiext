/*
Java UI Extensions project. https://ivanp2015.github.io/javauiext

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

package io.github.ivanp2015.javauiext.java.awt.helpers.jaxb;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for XML serialization of the {@link  java.awt.Color}.
 * See this <a href="https://stackoverflow.com/a/33056815/1540501">SO answer</a>
 * for more details about the idea.
 * @author Ivan Pizhenko
 */
public class ColorXmlAdapter extends XmlAdapter<ColorXmlAdapter.ColorValueType, Color> {

    /**
     * See {@link XmlAdapter#unmarshal(java.lang.Object)} for more details.
     * @param v Object to un-marshal.
     * @return Un-marshaled object.
     * @throws Exception 
     */
    @Override
    public Color unmarshal(ColorValueType v) throws Exception {
        return v.toColor();
    }

    /**
     * See {@link XmlAdapter#marshal(java.lang.Object)} for more details.
     * @param v Object to marshal.
     * @return Marshaled object.
     * @throws Exception 
     */
    @Override
    public ColorValueType marshal(Color v) throws Exception {
        return new ColorValueType(v);
    }

    /**
     * XML serialization proxy type for {@link java.awt.Color}.
     */
    @XmlType(name="RGBColor", propOrder = {}, namespace="https://ivanp2015.github.io/javauiext/java/awt")
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class ColorValueType {

        /**
         * Red value.
         */
        @XmlAttribute(name = "r")
        private int red;
        
        /**
         * Blue value
         */
        @XmlAttribute(name = "b")
        private int green;
        
        /**
         * Green value.
         */
        @XmlAttribute(name = "b")
        private int blue;

        /**
         * Initializes new object of class <code>ColorValueType</code>.
         */
        public ColorValueType() {
        }
        
        /**
         * Initializes new object of class <code>ColorValueType</code>.
         * @param color Source color.
         */
        public ColorValueType(Color color) {
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
        }
        
        /**
         * Converts to color object.
         * @return New color object.
         */
        public Color toColor() {
            return new Color(red, green, blue);
        }
    }
}
