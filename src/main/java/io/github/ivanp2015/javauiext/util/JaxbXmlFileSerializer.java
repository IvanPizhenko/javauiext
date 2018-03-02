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
 */
public final class JaxbXmlFileSerializer {
    /**
     * Disables class instantiation.
     */
    private JaxbXmlFileSerializer() {}
    
    /**
     * Loads object from specified file.
     * @param path Data file path.
     * @param clazz Object class.
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
     * @param path Data file path.
     * @param object Object to save.
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
