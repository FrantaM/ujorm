/*
 *  Copyright 2013-2014 Pavel Ponec
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
package org.ujorm.wicket.component.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.OddEvenItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;
import org.ujorm.Key;
import org.ujorm.KeyList;
import org.ujorm.Ujo;
import org.ujorm.core.KeyRing;
import org.ujorm.criterion.Criterion;
import org.ujorm.wicket.CssAppender;
import org.ujorm.wicket.component.toolbar.InsertToolbar;

/**
 * <p>This class called <strong>UjoDataProvider</strong> is an database
 * Wicket DataProvider. For a customization you can use a your own {@link IColumn} implementations
 * or you can owerwrite selected methods of this provider.
 * </p><p>
 * The implementation generates two database requests per a one rendering,
 * the first one get size and the second one get paged data. You can owerwrite the two data methods:
 * {@link #iterator(long, long) iterator()} and the {@link #size() size()}
 * for more optimization.
 * </p><p>
 * The current class uses a {@link WicketApplication} implementation, which must
 * implement the interface OrmHandlerProvider for an ORM support. See the example:
 * </p>
 * <h4>See the simple sample:</h4>
 * <pre class="pre"> {@code
 *  Criterion<Hotel> allActiveHotels = Hotel.ACTIVE.whereEq(true);
 *  UjoDataProvider<Hotel> dataProvider = UjoDataProvider.of(allActiveHotels);
 *
 *  dataProvider.addColumn(Hotel.NAME);
 *  dataProvider.addColumn(Hotel.CITY.add(City.NAME)); // An example of relations
 *  dataProvider.addColumn(Hotel.STREET);
 *  dataProvider.addColumn(Hotel.PRICE);
 *  dataProvider.addColumn(KeyColumn.of(Hotel.CURRENCY, SORTING_OFF));
 *  dataProvider.addColumn(Hotel.STARS);
 *  dataProvider.addColumn(Hotel.PHONE);
 *  dataProvider.setSort(Hotel.NAME);
 *
 *  panel.add(dataProvider.createDataTable("datatable", 10));
 * }
 * </pre>
 * @author Pavel Ponec
 */
public abstract class AbstractDataProvider<U extends Ujo> extends SortableDataProvider<U, Object> {
    private static final long serialVersionUID = 1L;
    /** Default Datatable ID have got value {@code "datatable"}. */
    public static final String DEFAULT_DATATABLE_ID = "datatable";
    /** Default CSS style for a SELECTED row */
    protected static final String DEFAULT_CSS_SELECTED = "selected";
    /** Data size */
    protected Long size;
    /** Data criterion model for filtering the data resource */
    protected IModel<Criterion<U>> filter;
    /** Data criterion model for select data rows */
    protected IModel<Criterion<U>> selected;
    /** Visible table columns */
    private List<IColumn<U, ?>> columns = new ArrayList<IColumn<U, ?>>();
    /** Default column sorting for the method {@link #addColumn(org.ujorm.Key) }
     * where the feature is enabled by default
     */
    private boolean defaultColumnSorting = true;

    /** Constructor
     * @param criterion Condition to a database query
     */
    public AbstractDataProvider(@Nonnull IModel<Criterion<U>> criterion) {
        this(criterion, null);
    }

    /** Constructor
     * @param filter Model of a condition to a database query
     * @param defaultSort Default sorting can be assigned optionally
     */
    public AbstractDataProvider
            ( @Nonnull IModel<Criterion<U>> filter
            , @Nullable Key<U,?> defaultSort) {
        this.filter = Args.notNull(filter, "The filter is required");

        if (defaultSort == null) {
            KeyRing<U> keys = KeyRing.of((Class<U>)filter.getObject().getDomain());
            defaultSort =  keys.getFirstKey();
        }
        setSort(defaultSort);
    }

    /**
     * Sets the current sort state and assign the BaseClass
     *
     * @param key
     * sort key
     * @param order
     * sort order
     */
    final public void setSort(Key<U, ?> key) {
        super.setSort((KeyRing)KeyRing.of(key), key.isAscending()
                ? SortOrder.ASCENDING
                : SortOrder.DESCENDING);
    }

    /** Vrací klíč pro řazení */
    public Key<U,?> getSortKey() {
        final SortParam<Object> sort = getSort();
        if (sort != null) {
            final Object key = getSort().getProperty();
            return key instanceof KeyRing
            ? ((KeyRing<U>)key).getFirstKey().descending(!sort.isAscending())
            : null ;
        } else {
            return null;
        }
    }

    /** Build a JDBC ResultSet allways.
     * Overwrite the method for an optimization.<br>
     */

    public abstract Iterator<U> iterator(long first, long count);

    /** Method calculate the size using special SQL requst.
     * Overwrite the method for an optimization.<br>
     * Original documentation: {@inheritDoc}
     */
    public abstract long size();

    /** Commit and close transaction */
    @Override
    public abstract void detach();

    /** Get a domann class */
    public Class<U> getDomainClass() {
        return (Class<U>) filter.getObject().getDomain();
    }

    /** Create a model */
    @Override
    public IModel<U> model(U object) {
        return new Model((Serializable)object);
    }

    /** Add table column */
    public boolean add(IColumn<U, ?> column) {
        return columns.add(column);
    }

    /** Add table columns according to column type including CSS class */
    public void add(KeyList<? super U> columns) {
        for (Key t : columns) {
            add(t);
        }
    }

    /** Add table column according to column type including CSS class */
    public <V> boolean add(Key<U,V> column, CssAppender cssClass) {
        final boolean result = add(column);
        ((KeyColumn)columns.get(columns.size()-1)).setCssClass(cssClass.getCssClass());
        return result;
    }

    /** Add table column according to column type */
    public <V> boolean add(Key<U,V> column) {
        if (column.isTypeOf(Boolean.class)) {
            return add(KeyColumnBoolean.of(column, isSortingEnabled(column)));
        }
        if (column.isTypeOf(Number.class)) {
            return add(KeyColumn.of(column, isSortingEnabled(column), "number"));
        }
        else {
            return add(KeyColumn.of(column, isSortingEnabled(column), null));
        }
    }

    /** Returns a CSS style for SELECTED row.
     * The default value is {@link #DEFAULT_CSS_SELECTED} */
    protected String getCssSelected() {
        return DEFAULT_CSS_SELECTED;
    }

    /** The sorting is enabled for a persistent Ujorm Key by default
     * @see #isDefaultColumnSorting()
     */
    protected boolean isSortingEnabled(final Key<U, ?> column) throws IllegalArgumentException {
        return defaultColumnSorting;
    }

    /** Create AJAX-based DataTable with a {@link #DEFAULT_DATATABLE_ID} */
    public final <S> DataTable<U,S> createDataTable(final int rowsPerPage) {
        return createDataTable(DEFAULT_DATATABLE_ID, rowsPerPage);
    }

    /** Create AJAX-based DataTable */
    public final <S> DataTable<U,S> createDataTable(final String id, final int rowsPerPage) {
        return createDataTable(id, rowsPerPage, false);
    }

    /** Create AJAX-based DataTable */
    public final <S> DataTable<U,S> createDataTable(final int rowsPerPage, boolean insertToolbar) {
        return createDataTable(DEFAULT_DATATABLE_ID, rowsPerPage, insertToolbar);
    }

    /** Create AJAX-based DataTable
     * @param id Component ID
     * @param rowsPerPage Row count per the one page
     * @param insertToolbar Append a generic toolbar for an insert action.
     * @return Create AJAX-based DataTable
     */
    public <S> DataTable<U,S> createDataTable(final String id, final int rowsPerPage, boolean insertToolbar) {
        final DataTable<U,S> result = new DataTable<U,S>(id, (List)columns, this, rowsPerPage) {
            @Override protected Item<U> newRowItem
                    ( final String id
                    , final int index
                    , final IModel<U> model) {
                final Item<U> result = new OddEvenItem<U>(id, index, model);

                // Mark a selected rows:
                if (selected != null) {
                    final Criterion<U> crn = selected.getObject();
                    if (crn!=null && crn.evaluate(model.getObject())) {
                       result.add(new CssAppender(getCssSelected()));
                    }
                }
                return result;
            }
        };

        result.addTopToolbar(new AjaxNavigationToolbar(result));
        result.addTopToolbar(new HeadersToolbar(result, this));
        result.addBottomToolbar(new NoRecordsToolbar(result));
        result.setOutputMarkupId(true);

        if (insertToolbar) {
            result.addBottomToolbar(new InsertToolbar(result, getDomainClass()));
        }

        return result;
    }

    // ============= SETERS / GETTERS =============

    /**
     * Default column sorting for the method {@link #addColumn(org.ujorm.Key) }
     * where the feature is enabled by default
     * @return the defaultColumnSorting
     */
    public final boolean isDefaultColumnSorting() {
        return defaultColumnSorting;
    }

    /**
     * Default column sorting for the method {@link #addColumn(org.ujorm.Key) }
     * where the feature is enabled by default
     * @param defaultColumnSorting the defaultColumnSorting to set
     */
    public void setDefaultColumnSorting(boolean defaultColumnSorting) {
        this.defaultColumnSorting = defaultColumnSorting;
    }

    /** Transient tableOf columns */
    public List<IColumn<U, ?>> getColumns() {
        return columns;
    }

    /** Assign a CSS class to a KeyColumn, if exists */
    public void setCssClass(final Key<U, ?> key, final String cssClass) {
        for (IColumn<U, ?> iColumn : columns) {
            if (iColumn instanceof KeyColumn
            && ((KeyColumn) iColumn).getKey().equals(key)) {
               ((KeyColumn) iColumn).setCssClass(cssClass);
               break;
            }
        }
    }

    /**
     * Data criterion model for select data rows
     * @return the selected
     */
    @Nullable
    public IModel<Criterion<U>> getSelected() {
        return selected;
    }

    /**
     * Data criterion model for select data rows
     * @param selected the selected to set
     */
    public void setSelected(@Nullable IModel<Criterion<U>> selected) {
        this.selected = selected;
    }

    /**
     * Data criterion model for select data rows
     * @param selected the selected to set
     */
    public void setSelected(@Nonnull Criterion<U> selected) {
        setSelected(Model.of(selected));
    }

    // --------- CRUD support ---------

    /** Insert row to the data source.
     * The method is not implemetned by default.
     * @param row Insert one table row
     */
    public boolean insertRow(U row) {
        throw new UnsupportedOperationException();
    }

    /** Delete rows from the data source
     * The method is not implemetned by default.
     * @param deleteCondition Remove all row with a condition.
     */
    public long deleteRow(Criterion<U> deleteCondition) {
        throw new UnsupportedOperationException();
    }

    /** Update all rows with a codition using the row
     * The method is not implemetned by default.
     * @param updateCondition Update condition
     * @param updatedRow Updated row
     */
    public long updateRow(Criterion<U> updateCondition, U updatedRow) {
        throw new UnsupportedOperationException();
 }

}