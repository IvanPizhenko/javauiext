/*
 * Java UI Extensions project.
 * https://ivanp2015.github.io/javauiext
 * 
 * Copyright (c) 2018, Ivan Pizhenko. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.   designates this
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

import java.awt.Font;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for XML serialization of the {@link java.awt.Font}.
 * 
 * @see <a href="https://stackoverflow.com/a/33056815/1540501">Stackoverflow answer.</a>
 * @see XmlAdapter
 * @see javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter
 * 
 * @author Ivan Pizhenko
 * @since 0.0.2
 */
public class FontXmlAdapter extends XmlAdapter<FontXmlAdapter.FontData, Font> {

    /**
     * See {@link XmlAdapter#unmarshal(java.lang.Object)} for more details.
     *
     * @param v Object to un-marshal.
     * @return Un-marshaled object.
     * @throws Exception if there's an error during the conversion.
     */
    @Override
    public Font unmarshal(FontData v) throws Exception {
        return v.toFont();
    }

    /**
     * See {@link XmlAdapter#marshal(java.lang.Object)} for more details.
     *
     * @param v Object to marshal.
     * @return Marshaled object.
     * @throws Exception if there's an error during the conversion.
     */
    @Override
    public FontData marshal(Font v) throws Exception {
        return new FontData(v);
    }

    /**
     * XML serialization helper type for {@link java.awt.Font}.
     */
    @XmlType(name="Font", propOrder = {}, namespace="https://ivanp2015.github.io/javauiext/java/awt")
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class FontData {

        @XmlAttribute(name="family", required=true)
        private String family;
        
        @XmlAttribute(name="bold", required=false)
        private boolean bold;

        @XmlAttribute(name="italic", required=false)
        private boolean italic;

        @XmlAttribute(name="size", required=true)
        private int size;

        /**
         * Constructs new object of class {@code FontData}.
         */
        public FontData() {
        }

        /**
         * Constructs new object of class {@code FontData}.
         *
         * @param font the source font.
         */
        public FontData(Font font) {
            this.family = font.getFamily();
            this.bold = font.isBold();
            this.italic = font.isItalic();
            this.size = font.getSize();
        }

        /**
         * Converts this object to {@link java.awt.Font} object.
         *
         * @return New font object.
         */
        public Font toFont() {
            int style = 0;
            if (bold) {
                style |= Font.BOLD;
            }
            if (italic) {
                style |= Font.ITALIC;
            }
            return new Font(family, style, size);
        }
    }
}
