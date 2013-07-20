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

import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.ujorm.core.KeyRing;
import org.ujorm.hotels.entity.Customer;
import org.ujorm.hotels.gui.customer.action.CustActionPanel;
import org.ujorm.hotels.gui.hotel.action.Toolbar;
import org.ujorm.hotels.services.DbService;
import org.ujorm.wicket.UjoEvent;
import org.ujorm.wicket.component.dialog.MessageDialogPanel;
import org.ujorm.wicket.component.grid.KeyColumn;
import org.ujorm.wicket.component.grid.UjoDataProvider;
import static org.ujorm.wicket.CommonActions.*;
import static org.ujorm.wicket.component.grid.UjoDataProvider.*;

/**
 * Customer Panel
 * @author Pavel Ponec
 */
public class CustomerTable extends Panel {

    @SpringBean(name="dbService") DbService dbService;
    private CustomerEditor editDialog;
    private MessageDialogPanel removeDialog;

    public CustomerTable(String id) {
        super(id);

        UjoDataProvider<Customer> columns
                = UjoDataProvider.of(Customer.ACTIVE.whereEq(true));
        columns.addColumn(Customer.LOGIN);
        columns.addColumn(Customer.TITLE);
        columns.addColumn(Customer.FIRSTNAME);
        columns.addColumn(Customer.SURENAME);
        columns.addColumn(Customer.EMAIL);
        columns.addColumn(Customer.ADMIN);
        columns.addColumn(Customer.ACTIVE);
        columns.addColumn(newActionColumn());
        columns.setSort(Customer.LOGIN);
        add(columns.createDataTable(10));

        // Dialogs:
        add((editDialog = createEditDialog("editDialog", 700, 390)).getModalWindow());
        add((removeDialog = createMessageDialog("removeDialog", 290, 160)).getModalWindow());
    }

    /** Manage events */
    @Override
    public void onEvent(IEvent<?> argEvent) {
        if (argEvent.getPayload() instanceof UjoEvent) {
            final UjoEvent<Customer> event = (UjoEvent<Customer>) argEvent.getPayload();

            if (event.isAction(UPDATE)) {
                if (event.showDialog()) {
                    editDialog.show(event, new ResourceModel("dialog.edit.title"));
                } else {
                    dbService.updateCustomer(event.getDomain());
                    reloadTable(event);
                }
            }
            else if (event.isAction(DELETE)) {
                if (event.showDialog()) {
                    removeDialog.setMessage(new Model("Do you want to remove selected Customer really?"));
                    removeDialog.show(event
                            , new ResourceModel("dialog.delete.title")
                            , "delete");
                } else {
                    dbService.deleteCustomer(event.getDomain());
                    reloadTable(event);
                }
            }
            else if (event.isAction(Toolbar.FILTER_ACTION)) {
                reloadTable(event);
            }
        }
    }


    /** Nabídka akcí: */
    private AbstractColumn<Customer, KeyRing<Customer>> newActionColumn() {
        return new KeyColumn<Customer, Integer>(KeyRing.of(Customer.ID), null, null) {
            @Override
            public void populateItem(Item item, String componentId, IModel model) {
                final Customer customer = (Customer) model.getObject();
                final CustActionPanel panel = new CustActionPanel(componentId, customer);
                item.add(panel);
            }
        };
    }

    /** Create the editor dialog */
    private CustomerEditor createEditDialog(String componentId, int width, int height) {
        IModel<Customer> model = Model.of(new Customer());
        final ModalWindow modalWindow = new ModalWindow(componentId, model);
        modalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);

        final CustomerEditor result = new CustomerEditor(modalWindow, model);
        modalWindow.setInitialWidth(width);
        modalWindow.setInitialHeight(height);
        modalWindow.setTitle(new ResourceModel("dialog.edit.title"));
        //modalWindow.setCookieName("modal-dialog");

        return result;
    }

    /** Create the editor dialog */
    private MessageDialogPanel createMessageDialog(String componentId, int width, int height) {
        IModel<String> model = Model.of("");
        final ModalWindow modalWindow = new ModalWindow(componentId, model);
        modalWindow.setCssClassName(ModalWindow.CSS_CLASS_BLUE);

        final MessageDialogPanel result = new MessageDialogPanel(modalWindow, model);
        modalWindow.setInitialWidth(width);
        modalWindow.setInitialHeight(height);
        //modalWindow.setCookieName("modal-dialog");

        return result;
    }

    /** Reload the data table */
    private void reloadTable(UjoEvent event) {
        event.getTarget().add(get(DEFAULT_DATATABLE_ID));
    }

}