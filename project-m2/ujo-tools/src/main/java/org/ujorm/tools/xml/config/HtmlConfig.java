/*
 * Copyright 2018-2012 Pavel Ponec, https://github.com/pponec
 * https://github.com/pponec/ujorm/blob/master/samples/servlet/src/main/java/org/ujorm/ujoservlet/tools/Html.java
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
package org.ujorm.tools.xml.config;

import org.ujorm.tools.xml.config.impl.DefaultHtmlConfig;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Configuraion of HtmlPage
 * @author Pavel Ponec
 */
public interface HtmlConfig extends XmlConfig {

    /** Title is a required element by HTML 5 */
    @Nonnull
    CharSequence getTitle();

    /** CSS links of a HTML page */
    @Nonnull
    CharSequence[] getCssLinks();

    /** Language of a HTML page */
    @Nonnull
    Optional<CharSequence> getLanguage();

    /** Get a content type where a recommended value is {@code "text/html"} */
    @Nonnull
    String getContentType();

    /** Build a real model or a plain writer with a recommended value {@code false} */
    boolean isDocumentObjectModel();
    
    /**
     * Create a new default config
     */
    @Nonnull
    public static DefaultHtmlConfig ofDefault() {
        return new DefaultHtmlConfig();
    }
}