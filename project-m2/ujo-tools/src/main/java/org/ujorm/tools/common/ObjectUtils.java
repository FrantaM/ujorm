/*
 * Copyright 2019-2019 Pavel Ponec
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ujorm.tools.common;

import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Static methods
 * @author Pavel Ponec
 */
public abstract class ObjectUtils {

    private ObjectUtils() {
    }

    /** Run a function in case the value is an instance of the required class.
     *
     * <br>Usage:
     * <pre class="pre">
     *   Object input = "ABC";
     *   int result = ObjectUtils.iof(input, String.class, v -> v.length()).orElse(0);
     *   assertEquals(3, result);
     * </pre>
     */
    public static final <V,R> Optional<R> iof(
            @Nullable final Object value,
            @Nonnull final Class<V> requiredClass,
            @Nonnull final Function<V,R> function) {
        return requiredClass.isInstance(value)
                ? Optional.ofNullable(function.apply((V) value))
                : Optional.empty();
    }

    /** Check the result of a function in case the value have got the same class as the required one.
     *
     * <br>Usage:
     * <pre class="pre">
     *   Object input = "ABC";
     *   int result = ObjectUtils.check(input, String.class, v -> v.length()).orElse(0);
     *   assertEquals(3, result);
     * </pre>
     */
    public static final <V> boolean check(
            @Nullable final Object value,
            @Nonnull final Class<V> requiredClass,
            @Nonnull final Function<V,Boolean> function) {
        return value != null
                && value.getClass() == requiredClass
                && function.apply((V) value);
    }


}