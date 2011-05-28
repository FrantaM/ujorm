/*
 *  Copyright 2007-2010 Pavel Ponec
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


package org.ujoframework.extensions;

/**
 * The method {@link #exportToString()} replaces the original {@link ValueTextable#toString()}.
 * @author Ponec
 * @see ValueTextable#toString()
 */
public interface ValueExportable extends ValueTextable {

    /** Export the value as String.
     * The method replaces the original {@link ValueTextable#toString()}.
     */
    public String exportToString();

}
