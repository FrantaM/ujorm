/*
 * Copyright 2018-2018 Pavel Ponec,
 * https://github.com/pponec/ujorm/blob/master/project-m2/ujo-tools/src/main/java/org/ujorm/tools/XmlWriter.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ujorm.tools.dom;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.ujorm.tools.Check;
import org.ujorm.tools.dom.AbstractElement.WriterTool;
import static org.ujorm.tools.dom.AbstractElement.WriterTool.*;

/**
 * If you need special formatting, overwrite responsible methods.
 * @see XmlElement
 * @since 1.88
 * @author Pavel Ponec
 */
public class XmlWriter {

    /** Writer */
    @Nonnull
    protected final Appendable out;

    /** An element offset is enabled */
    protected final boolean offsetEnabled;

    /** An offset space */
    @Nullable
    protected final String offsetSpace;

    /** Default constructor a zero offset */
    public XmlWriter() {
        this(new CharArrayWriter(512));
    }

    /** Writer constructor with a zero offset */
    public XmlWriter(@Nonnull final Appendable out) {
        this(out, "");
    }

    /**
     * A writer constructor
     * @param out A writer
     * @param offsetSpace String for a one level offset.
     */
    public XmlWriter(@Nonnull final Appendable out, @Nullable final String offsetSpace) {
        this.out = out;
        this.offsetEnabled = Check.hasLength(offsetSpace);
        this.offsetSpace = offsetSpace;
    }

    /** Render the XML code without header */
    @Nonnull
    public final XmlWriter write(final int level, @Nonnull final XmlElement element) throws IOException {
        return write(level, element.name, element.attributes, element.children, element);
    }

    /** Render the XML code without header
     * @param level Element nesting level.
     * @param name Name of element
     * @param attributes Attributes of the element
     * @param children Childern of the element including {@code null} items
     * @param element Original element
     * @return This
     */
    @Nonnull
    protected XmlWriter write(final int level
            , @Nonnull final CharSequence name
            , @Nullable final Map<String, Object> attributes
            , @Nullable final List<Object> children
            , @Nonnull final XmlElement element) throws IOException {
        out.append(XML_LT);
        out.append(name);

        if (Check.hasLength(attributes)) {
            assert attributes != null; // For static analyzer only
            for (String key : attributes.keySet()) {
                out.append(CHAR_SPACE);
                out.append(key);
                out.append('=');
                out.append(XML_2QUOT);
                writeValue(attributes.get(key), element, key);
                out.append(XML_2QUOT);
            }
        }
        if (Check.hasLength(children)) {
            assert children != null; // For static analyzer only
            out.append(XML_GT);
            boolean writeNewLine = true;
            for (Object child : children) {
                if (child instanceof XmlElement) {
                    if (writeNewLine) {
                        writeNewLine(level);
                    } else {
                        writeNewLine = true;
                    }
                    write(level + 1, (XmlElement) child);
                } else if (child instanceof XmlElement.RawEnvelope) {
                    writeRawValue(((XmlElement.RawEnvelope) child).get(), element);
                    writeNewLine = false;
                } else {
                    writeValue(child, element, null);
                    writeNewLine = false;
                }
            }
            out.append(XML_LT);
            out.append(FORWARD_SLASH);
            out.append(name);
        } else {
            out.append(FORWARD_SLASH);
        }
        out.append(XML_GT);
        return this;
    }

    /** Write a new line with an offset by the current level */
    protected void writeNewLine(final int level) throws IOException {
        out.append(CHAR_NEW_LINE);
        if (offsetEnabled) {
            for (int i = level - 1; i >= 0; i--) {
                out.append(offsetSpace);
            }
        }
    }

    /** Write escaped value to the output
     * @param value A value to write
     * @param element The element
     * @param attribute A name of the XML attribute of {@code null} value for a XML text.
     */
    protected void writeValue(@Nullable final Object value, @Nonnull final XmlElement element, final @Nullable String attribute) throws IOException {
        final CharSequence text = value instanceof CharSequence ? (CharSequence) value : String.valueOf(value);
        WriterTool.write(text, out);
    }

    /**
     * Write the content of an envelope
     * @param rawValue A raw value to print
     * @param element An original element
     */
    protected void writeRawValue(@Nonnull final Object rawValue, @Nonnull final XmlElement element) throws IOException {
        out.append(rawValue.toString());
    }

    @Override @Nonnull
    public String toString() {
        final String result = out.toString();
        return result != null
             ? result
             : String.valueOf(result);
    }
}

