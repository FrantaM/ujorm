/*
 * Copyright 2019-2019 Pavel Ponec,
 * https://github.com/pponec/ujorm/blob/master/project-m2/ujo-tools/src/main/java/org/ujorm/tools/XmlModel.java
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

package org.ujorm.tools.web;

import java.util.LinkedHashMap;
import org.junit.*;
import org.ujorm.tools.xml.config.impl.DefaultHtmlConfig;
import org.ujorm.tools.xml.config.HtmlConfig;
import static org.junit.Assert.assertEquals;

/**
 * @author Pavel Ponec
 */
public class ElementTest {

    /**
     * Test of addSelect method, of class Element.
     */
    @Test
    public void testAddSelect_3args() {
        System.out.println("addSelect");
        DefaultHtmlConfig config = HtmlConfig.ofDefault();
        HtmlElement resInstance = createHtmlPage(config);

        String result = resInstance.toString();
        String expectedResult = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\"/>\n"
                + "<title>Demo</title></head>\n"
                + "<body>\n"
                + "<select name=\"selectName\">\n"
                + "<option value=\"1\">one</option>\n"
                + "<option value=\"2\" selected=\"selected\">two</option>\n"
                + "<option value=\"3\">three</option>"
                + "</select>"
                + "</body>"
                + "</html>";
        assertEquals(expectedResult, result);

        // --- DOM model ---

        config.setDom(true);
        HtmlElement domInstance = createHtmlPage(config);
        result = domInstance.toString();
        assertEquals(expectedResult, result);
    }

    /** Create HTML page */
    private HtmlElement createHtmlPage(HtmlConfig config) throws IllegalStateException {
        HtmlElement result = HtmlElement.of(config);
        try (HtmlElement instance = result) {
            Element body = instance.getBody();

            Object value = 2;
            LinkedHashMap options = new LinkedHashMap<>();
            options.put(1, "one");
            options.put(value, "two");
            options.put(3, "three");
            body.addSelect().setName("selectName")
                    .addSelectOptions(value, options, "mySelect");
        }
        return result;
    }

    /**
     * Test of addSelect method, of class Element.
     */
    @Test
    public void testAddFieldset() {
        System.out.println("addFieldset");
        CharSequence[] cssClasses = null;
        HtmlElement resInstance = HtmlElement.of(null);
        try (HtmlElement instance = resInstance) {
            Element body = instance.getBody();
            body.addFieldset("MyTitle", "myCssClass").addText("Lorem ipsum ...");
        }

        String result = resInstance.toString();
        String expectedResult = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\"/>\n"
                + "<title>Demo</title></head>\n"
                + "<body>\n"
                + "<fieldset class=\"myCssClass\">\n"
                + "<legend>MyTitle</legend>"
                + "Lorem ipsum ..."
                + "</fieldset>"
                + "</body>"
                + "</html>";
        assertEquals(expectedResult, result);
    }
}