/*
 * Copyright 2013, Pavel Ponec
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
package org.ujorm.hotels.gui.customer;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;
import org.ujorm.hotels.entity.Customer;
import org.ujorm.wicket.component.dialog.EntityDialogPanel;

/**
 * Customer Editor
 * @author Pavel Ponec
 */
public class CustomerEditor extends EntityDialogPanel<Customer> {
    private static final long serialVersionUID = 0L;

    public CustomerEditor(ModalWindow modalWindow, IModel<Customer> model) {
        super(modalWindow, model);

        // Editable fields:
        fields.add(Customer.LOGIN);
        fields.add(Customer.TITLE);
        fields.add(Customer.FIRSTNAME);
        fields.add(Customer.SURENAME);
        fields.add(Customer.EMAIL);
        fields.add(Customer.ADMIN);
        fields.add(Customer.ACTIVE);

        // Modify attribute(s):
        fields.getField(Customer.LOGIN).setEnabled(false);
        fields.getField(Customer.TITLE).setEnabled(false);
    }

}