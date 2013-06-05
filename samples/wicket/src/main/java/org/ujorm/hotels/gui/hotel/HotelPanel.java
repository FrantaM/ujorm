/*
 ** Copyright 2013, Pavel Ponec
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
package org.ujorm.hotels.gui.hotel;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.ujorm.hotels.domains.Hotel;
import org.ujorm.wicket.component.gridView.KeyColumn;

/**
 *
 * @author Pavel Ponec
 */
public class HotelPanel extends Panel {

    //@SpringBean private UjormTransactionManager manager;

    public HotelPanel(String id) {
        super(id);

        final List<IColumn> columns = new ArrayList<IColumn>();
        columns.add(KeyColumn.of(Hotel.ID));
        columns.add(KeyColumn.of(Hotel.NAME));
        columns.add(KeyColumn.of(Hotel.CITY));
        columns.add(KeyColumn.of(Hotel.PRICE));
        columns.add(KeyColumn.of(Hotel.CURRENCY));
        columns.add(KeyColumn.of(Hotel.STARS));
        columns.add(KeyColumn.of(Hotel.PHONE));

        // add(new DefaultDataTable("datatable", columns, new HotelProvider(), 20));
    }

}