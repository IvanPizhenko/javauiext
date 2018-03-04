/*
 * Java UI Extensions project.
 * https://ivanp2015.github.io/javauiext
 * 
 * Copyright (c) 2018, Ivan Pizhenko. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Ivan Pizhenko designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
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

package io.github.ivanp2015.javauiext.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * JAXB XML file generic serialization code.
 * @author Ivan Pizhenko.
 * @since 0.0.2
 */
public final class JaxbXmlFileSerializer {

    private JaxbXmlFileSerializer() {}

    /**
     * Loads object from specified file.
     * 
     * @param path data file path.
     * @param clazz object class.
     * @return Specified class instance.
     * @throws javax.xml.bind.JAXBException if an error occurred when creating unmarshaler.
     */
    public static Object loadFromXmlFile(String path, Class<?> clazz) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(new File(path));
    }

    /**
     * Saves object to specified file.
     * 
     * @param path data file path.
     * @param object object to save.
     * @throws javax.xml.bind.JAXBException if an error occurred when creating marshaler.
     * @throws java.io.IOException if I/O error happened.
     */
    public static void saveToXmlFile(String path, Object object) throws JAXBException, IOException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
        } catch(PropertyException ex) {
            // ignore this exception, it should not happen.
        }
        try (final FileOutputStream fos = new FileOutputStream(new File(path))) {
            jaxbMarshaller.marshal(object, fos);
        }
    }
}
