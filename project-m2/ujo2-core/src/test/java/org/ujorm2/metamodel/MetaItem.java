package org.ujorm2.metamodel;

import java.math.BigDecimal;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.ujorm2.Key;
import org.ujorm2.core.AbstractDomainModel;
import org.ujorm2.core.KeyFactory;
import org.ujorm2.core.KeyFactoryProvider;
import org.ujorm2.doman.Item;

/**
 * TODO: A helper class generated by a Maven module.
 * @author Pavel Ponec
 * @param <D> Domain
 */
    public class MetaItem<D> extends AbstractDomainModel<D, Item> {

    /** All direct keys */
    protected static final class DirectKeys<D> implements KeyFactoryProvider {

        final KeyFactory<Item> keyFactory = new KeyFactory(Item.class);

        final Key<Item, Integer> id = keyFactory.newKey(
                (d) -> d.getId(),
                (d, v) -> d.setId(v));

        final Key<Item, String> note = keyFactory.newKey(
                (d) -> d.getNote(),
                (d, v) -> d.setNote(v));

        final Key<Item, BigDecimal> price = keyFactory.newKey(
                (d) -> d.getPrice(),
                (d, v) -> d.setPrice(v));

        final MetaOrder<Item> order = keyFactory.newRelation(
                (d) -> d.getOrder(),
                (d, v) -> d.setOrder(v));

        final Key<Item, Boolean> descending = keyFactory.newKey(
                (d) -> d.getDescending(),
                (d, v) -> d.setDescending(v));

        final Key<Item, Integer> codePoints = keyFactory.newKey(
                (d) -> d.getCodePoints(),
                (d, v) -> d.setCodePoints(v));

        @Override
        public KeyFactory getKeyFactory() {
            return keyFactory;
        }
    };

    public MetaItem() {
        super(new DirectKeys());
    }

    public MetaItem(@Nonnull Key<D,?> keyPrefix) {
        super(keyPrefix);
    }

    @Override
    public D createDomain() {
        return (D) new Item();
    }

    /** Provider of an instance of DirectKeys */
    private DirectKeys key() {
        return (DirectKeys) directKeys;
    }

    // --- KEY PROVIDERS ---

    public Key<D, Integer> id() {
        return getKey(key().id);
    }

    public Key<D, String> note() {
        return getKey(key().note);
    }

    public Key<D, BigDecimal> price() {
        return getKey(key().price);
    }

    public MetaOrder<D> order() {
        return (MetaOrder) getKey(key().order);
    }

    public Key<D, Boolean> descending$() {
        return getKey(key().descending);
    }

    public Key<D, Integer> codePoints$() {
        return getKey(key().codePoints);
    }

    // ---- Helper method

    public static final MetaItem<Item> of(@Nullable KeyFactory context) {
        return new MetaItem<>();
    }

}
