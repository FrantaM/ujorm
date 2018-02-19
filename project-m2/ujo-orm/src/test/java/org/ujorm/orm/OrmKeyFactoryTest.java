/*
 *  Copyright 2018 Pavel Ponec
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
package org.ujorm.orm;

import org.junit.Test;
import org.ujorm.orm.bo.XCustomerExtended;
import static junit.framework.TestCase.assertEquals;

/**
 * A test of the OrmKeyFactory class.
 * @author Pavel Ponec
 */
public class OrmKeyFactoryTest {

    /** Test of createKey method, of class OrmKeyFactory. */
    @Test
    public void testCreateKey() {
        System.out.println("createKey");

        final Long iBase = 1L;
        final Long idExte = 2L;
        final XCustomerExtended cust = new XCustomerExtended();

        assertEquals(0, XCustomerExtended.ID.getIndex());
        assertEquals(6, XCustomerExtended.ID_EXTENDED.getIndex());

        assertEquals("id", XCustomerExtended.ID.getName());
        assertEquals("idExtended", XCustomerExtended.ID_EXTENDED.getName());

        cust.setId(iBase);
        cust.setIdExtended(idExte);

        assertEquals(iBase, cust.getId());
        assertEquals(idExte, cust.getIdExtended());
    }
}