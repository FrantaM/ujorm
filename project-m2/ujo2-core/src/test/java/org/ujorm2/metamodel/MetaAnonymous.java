package org.ujorm2.metamodel;

import java.time.LocalDateTime;
import javax.annotation.Nullable;
import org.ujorm2.Key;
import org.ujorm2.core.AbstractDomainModel;
import org.ujorm2.core.KeyFactory;
import org.ujorm2.core.KeyFactoryProvider;
import org.ujorm2.core.UjoContext;
import org.ujorm2.doman.Anonymous;
import org.ujorm2.doman.Item;

/**
 * TODO: A helper class generated by a Maven module.
 * @author Pavel Ponec
 */
public class MetaAnonymous<D> extends AbstractDomainModel<D, Anonymous> {

    /** All direct keys */
    static final class DirectKeys<T extends Anonymous> implements KeyFactoryProvider<T> {

        final KeyFactory<T> keyFactory = new KeyFactory(Anonymous.class);

        final Key<T, Integer> id = keyFactory.newKey(
                (d) -> d.getId(),
                (d, v) -> d.setId(v));

        final Key<T, Short> pin = keyFactory.newKey(
                (d) -> d.getPin(),
                (d, v) -> d.setPin(v));

        final Key<T, LocalDateTime> created = keyFactory.newKey(
                (d) -> d.getCreated(),
                (d, v) -> d.setCreated(v));

        final MetaAnonymous<T> parent = keyFactory.newRelation(
                (d) -> d.getParent(),
                (d, v) -> d.setParent(v));

        @Override
        public KeyFactory<T> getKeyFactory() {
            return keyFactory;
        }
    };

    public MetaAnonymous() {
        super(new DirectKeys());
    }

    public MetaAnonymous(KeyFactoryProvider keyFactoryProvider, Key<D, ?> keyPrefix) {
        super(keyFactoryProvider, keyPrefix);
    }

    @Override
    public <A> AbstractDomainModel<A, Anonymous> prefix(Key<A, D> key) {
        return new MetaAnonymous(keys(), key);
    }

    @Override
    public D createDomain() {
        return (D) new Item();
    }

    /** Provider of an instance of DirectKeys */
    @Override
    protected final DirectKeys keys() {
        return (DirectKeys) directKeys;
    }

    // --- KEY PROVIDERS ---

    public Key<D, Integer> id() {
        return getKey(keys().id);
    }

    public Key<D, Short> pin() {
        return getKey(keys().pin);
    }

    public Key<D, LocalDateTime> created() {
        return getKey(keys().created);
    }

    public MetaAnonymous<D> parent() {
        return (MetaAnonymous) getKey(keys().parent);
    }

    public static final MetaAnonymous<Anonymous> of(@Nullable UjoContext context) {
        return new MetaAnonymous<>();
    }

}
