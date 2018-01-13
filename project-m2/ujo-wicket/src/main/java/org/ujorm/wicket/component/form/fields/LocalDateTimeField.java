/*
 *  Copyright 2017 Pavel Ponec
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.ujorm.wicket.component.form.fields;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.ujorm.Key;
import org.ujorm.Ujo;
import org.ujorm.tools.Check;
import org.ujorm.wicket.component.tools.DateTimes;

/**
 * Local datetime field with a Label including a feedback message.
 * @author Pavel Ponec
 */
public class LocalDateTimeField<T> extends LocalDateField<T> {
    private static final long serialVersionUID = 2017_06_05L;
    /** Default CSS class have got value {@code datepicker} */
    public static final String CSS_DATEPICKER = "datePickerComponent";

    public <U extends Ujo> LocalDateTimeField(Key<U,T> key) {
        super(key.getName(), key, null);
    }

    public <U extends Ujo> LocalDateTimeField(String componentId, Key<U,T> key, String cssClass) {
        super(componentId, key, cssClass);
    }

    /** Returns localizadDate pattern */
    @Override
    protected String getDatePattern() {
        final String key = DateTimes.LOCALE_DATETIME_FORMAT_KEY;
        return getLocalizer().getString(key, this, DateTimes.getDefaultPattern(key));
    }

    /** A LocalDateTime converter factory */
    @Override
    protected IConverter<?> createDateConverter() {
        return new IConverter<LocalDateTime>() {
            @Override public LocalDateTime convertToObject(@Nullable final String localDate, @Nullable final Locale locale) throws ConversionException {
                return Check.hasLength(localDate) 
                     ? LocalDateTime.parse(localDate, getFormatter(locale))
                     : null;
            }

            @Override public String convertToString(@Nullable final LocalDateTime localDate, @Nullable final Locale locale) {
                return localDate != null 
                      ? localDate.format(getFormatter(locale))
                      : "";
            }

            @Nonnull private DateTimeFormatter getFormatter(@Nullable final Locale locale) {
                return DateTimeFormatter.ofPattern(getDatePattern(), locale);
            }
        };
    }
}
