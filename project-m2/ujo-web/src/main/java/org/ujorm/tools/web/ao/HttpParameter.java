/*
 * Copyright 2020-2020 Pavel Ponec, https://github.com/pponec
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
package org.ujorm.tools.web.ao;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;


/**
 * An interface for bulding HTML parameters by an Enumerator.
 *
 * <h3>Usage</h3>
 * <pre class="pre">
 * {
 *    String value = Param.text(httpServletRequest, "my default value");
 * }
 * enum Param implements HttpParam {
 *     REGEXP,
 *     TEXT;
 *     @Override public String toString() {
 *         return name().toLowerCase();
 *     }
 * }
 * </pre>
 *
 * @author Pavel Ponec
 */
public interface HttpParameter extends CharSequence {

    @Override
    default int length() {
        return toString().length();
    }

    @Override
    default char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }


    default String value(@Nonnull final HttpServletRequest request) {
        return value(request, null);
    }

    default String value(@Nonnull final HttpServletRequest request, @Nonnull final String defaultValue) {
        final String result = request.getParameter(toString());
        return result != null ? result : defaultValue;
    }

}