package org.ujorm2.metamodel;

import java.time.LocalDateTime;
import javax.annotation.Nonnull;
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
    protected static final class DirectKeys<D> implements KeyFactoryProvider {

        final KeyFactory<Anonymous> keyFactory = new KeyFactory(Anonymous.class);

        final Key<Anonymous, Integer> id = keyFactory.newKey(
                (d) -> d.getId(),
                (d, v) -> d.setId(v));

        final Key<Anonymous, Short> pin = keyFactory.newKey(
                (d) -> d.getPin(),
                (d, v) -> d.setPin(v));

        final Key<Anonymous, LocalDateTime> created = keyFactory.newKey(
                (d) -> d.getCreated(),
                (d, v) -> d.setCreated(v));

        final MetaAnonymous<Anonymous> parent = keyFactory.newRelation(
                (d) -> d.getParent(),
                (d, v) -> d.setParent(v));

        @Override
        public KeyFactory getKeyFactory() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };

    public MetaAnonymous() {
        super(new DirectKeys());
    }

    public MetaAnonymous(@Nonnull Key<D,?> keyPrefix) {
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

    public Key<D, Short> pin() {
        return getKey(key().pin);
    }

    public Key<D, LocalDateTime> created() {
        return getKey(key().created);
    }

    public MetaAnonymous<D> parent() {
        return (MetaAnonymous) getKey(key().parent);
    }

    public static final MetaAnonymous<Anonymous> of(@Nullable UjoContext context) {
        return new MetaAnonymous<>();
    }

}
