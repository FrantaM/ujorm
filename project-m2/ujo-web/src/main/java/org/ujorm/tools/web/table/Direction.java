/*
 * Copyright 2021-2021 Pavel Ponec, https://github.com/pponec
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
package org.ujorm.tools.web.table;

/**
 * Sort direction
 * @author Pavel Ponec
 */
public enum Direction {
    
    DOWN,
    UP,
    BOTH;

    /** Safe equals */
    public boolean safeEquals(Direction direction) {
        return equals(direction);
    }
    
    /** Turn the direction */
    public Direction switchIt() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case BOTH:
                return this;
            default:
                throw new IllegalStateException("Illegal direction: " + this);
        }
    } 
    
    public static final Direction of(Boolean ascending) {
        if (ascending == null) {
            return BOTH;
        }
        return ascending ? DOWN : UP;
    }
}
