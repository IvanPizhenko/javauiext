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

import java.awt.Font;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for XML serialization of the {@link  java.awt.Font}. See this
 * <a href="https://stackoverflow.com/a/33056815/1540501">SO answer</a>
 * for more details about the idea.
 * @author Ivan Pizhenko
 */
public class FontXmlAdapter extends XmlAdapter<FontXmlAdapter.FontValueType, Font> {

    /**
     * See {@link XmlAdapter#unmarshal(java.lang.Object)} for more details.
     * @param v Object to un-marshal.
     * @return Un-marshaled object.
     * @throws Exception 
     */
    @Override
    public Font unmarshal(FontValueType v) throws Exception {
        return v.toFont();
    }

    /**
     * See {@link XmlAdapter#marshal(java.lang.Object)} for more details.
     * @param v Object to marshal.
     * @return Marshaled object.
     * @throws Exception 
     */
    @Override
    public FontValueType marshal(Font v) throws Exception {
        return new FontValueType(v);
    }

    /**
     * XML serialization proxy type for {@link java.awt.Font}.
     */
    @XmlType(name="Font", propOrder = {}, namespace="https://ivanp2015.github.io/javauiext/java/awt")
    @XmlAccessorType(XmlAccessType.NONE)
    public static final class FontValueType {

        /**
         * Font family.
         */
        @XmlAttribute(name="family", required=true)
        private String family;
        
        /**
         * Font style: bold.
         */
        @XmlAttribute(name="bold", required=false)
        private boolean bold;

        /**
         * Font style: italic.
         */
        @XmlAttribute(name="italic", required=false)
        private boolean italic;
        
        /**
         * Font size.
         */
        @XmlAttribute(name="size", required=true)
        private int size;

        /**
         * Initializes new object of class <code>FontValueType</code>.
         */
        public FontValueType() {
        }
        
        /**
         * Initializes new object of class <code>FontValueType</code>.
         * @param font The font.
         */
        public FontValueType(Font font) {
            this.family = font.getFamily();
            this.bold = font.isBold();
            this.italic = font.isItalic();
            this.size = font.getSize();
        }
        
        /**
         * Converts to font object.
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
