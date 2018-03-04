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

package io.github.ivanp2015.javauiext.java.awt.helpers.jaxb;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for XML serialization of the {@link java.awt.Color}.
 *
 * @see <a href="https://stackoverflow.com/a/33056815/1540501">Stackoverflow answer.</a>
 * @see XmlAdapter
 * @see javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
 *
 * @author Ivan Pizhenko
 * @since 0.0.2
 */
public class ColorXmlAdapter extends XmlAdapter<ColorXmlAdapter.RGBColorData, Color> {

    /**
     * See {@link XmlAdapter#unmarshal(java.lang.Object)} for more details.
     *
     * @param v Object to un-marshal.
     * @return Un-marshaled object.
     * @throws Exception if there's an error during the conversion.
     */
    @Override
    public Color unmarshal(RGBColorData v) throws Exception {
        return v.toColor();
    }

    /**
     * See {@link XmlAdapter#marshal(java.lang.Object)} for more details.
     *
     * @param v Object to marshal.
     * @return Marshaled object.
     * @throws Exception if there's an error during the conversion.
     */
    @Override
    public RGBColorData marshal(Color v) throws Exception {
        return new RGBColorData(v);
    }

    /**
     * XML serialization helper type for {@link java.awt.Color}.
     */
    @XmlType(name="RGBColor", propOrder = {}, namespace="https://ivanp2015.github.io/javauiext/java/awt")
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class RGBColorData {

        @XmlAttribute(name = "r")
        private int red;

        @XmlAttribute(name = "g")
        private int green;

        @XmlAttribute(name = "b")
        private int blue;

        /**
         * Constructs new object of class {@code RGBColorData}.
         */
        public RGBColorData() {
        }

        /**
         * Constructs new object of class {@code RGBColorData}.
         *
         * @param color Source color.
         */
        public RGBColorData(Color color) {
            this.red = color.getRed();
            this.green = color.getGreen();
            this.blue = color.getBlue();
        }

        /**
         * Converts this object to {@link java.awt.Color} object.
         *
         * @return New color object.
         */
        public Color toColor() {
            return new Color(red, green, blue);
        }
    }
}
