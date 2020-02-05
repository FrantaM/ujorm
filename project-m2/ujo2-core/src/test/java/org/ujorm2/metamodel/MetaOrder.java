package org.ujorm2.metamodel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import org.ujorm2.Key;
import org.ujorm2.core.AbstractDomainModel;
import org.ujorm2.core.KeyFactory;
import org.ujorm2.core.KeyFactoryProvider;
import org.ujorm2.core.UjoContext;
import org.ujorm2.doman.Order;
import org.ujorm2.doman.Order.State;

/**
 * TODO: A helper class generated by a Maven module.
 * @author Pavel Ponec
 */
public class MetaOrder<D> extends AbstractDomainModel<D, Order> {

    /** All direct keys */
    static final class DirectKeys<T extends Order> implements KeyFactoryProvider<T> {

        final KeyFactory<T> keyFactory = new KeyFactory(Order.class);

        final Key<T, Integer> id = keyFactory.newKey(
                (d) -> d.getId(),
                (d, v) -> d.setId(v));

        final Key<T, State> state = keyFactory.newKey(
                (d) -> d.getState(),
                (d, v) -> d.setState(v));

        final Key<T, BigDecimal> totalPrice = keyFactory.newKey(
                (d) -> d.getTotalPrice(),
                (d, v) -> d.setTotalPrice(v));

        final MetaUser<T> user = keyFactory.newRelation(
                (d) -> d.getUser(),
                (d, v) -> d.setUser(v));

        final Key<T, String> note = keyFactory.newKey(
                (d) -> d.getNote(),
                (d, v) -> d.setNote(v));

        final Key<T, LocalDateTime> created = keyFactory.newKey(
                (d) -> d.getCreated(),
                (d, v) -> d.setCreated(v));

        @Override
        public KeyFactory<T> getKeyFactory() {
            return keyFactory;
        }
    };

    public MetaOrder() {
        super(new DirectKeys());
    }

    public MetaOrder(@Nonnull Key<D,?> keyPrefix) {
        super(keyPrefix);
    }

    @Override
    public D createDomain() {
        return (D) new Order();
    }

    /** Provider of an instance of DirectKeys */
    private DirectKeys key() {
        return (DirectKeys) directKeys;
    }

    // --- KEY PROVIDERS ---

    public Key<D, Integer> id() {
        return getKey(key().id);
    }

    public Key<D, State> state() {
        return getKey(key().state);
    }

    public  Key<D, BigDecimal> totalPrice() {
        return getKey(key().totalPrice);
    }

    public MetaUser<D> user() {
        return (MetaUser) getKey(key().user);
    }

    public Key<D, String> note() {
        return getKey(key().note);
    }

    public Key<D, LocalDateTime> created() {
        return getKey(key().created);
    }

    public static final MetaOrder<Order> of(@Nonnull UjoContext context) {
        return new MetaOrder<>();
    }

}