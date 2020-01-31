package org.ujorm.metamodel;

import java.math.BigDecimal;
import javax.annotation.Nullable;
import org.ujorm.Key;
import org.ujorm.core.AbstractKey;
import org.ujorm.core.MetaInterface;
import org.ujorm.core.UjoContext;
import org.ujorm.doman.*;

/**
 * TODO: A helper class generated by a Maven module.
 * @author Pavel Ponec
 */
public class MetaItem<T> extends AbstractKey<T, Item> implements MetaInterface<T> {

    public MetaItem(Class<T> domainClass, UjoContext context) {
        super(domainClass, context);
    }

    public Key<T, Integer> id() {
        return null;
    }

    public Key<T, String> note() {
        return null;
    }

    public Key<T, BigDecimal> price() {
        return null;
    }

    public MetaOrder<T> order() {
        return null;
    }

    public Key<T, Boolean> descending$() {
        return null;
    }

    public Key<T, Integer> codePoints$() {
        return null;
    }

    // ---- Helper method

    @Override
    public T newDomain() {
        return (T) new Item();
    }

    public static final MetaItem<Item> of(@Nullable UjoContext context) {
        return context.getMetaModel(Item.class, MetaItem.class);
    }

    public static final MetaItem<Item> of() {
        return of(UjoContext.of());
    }


}
