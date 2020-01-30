/*
 *  Copyright 2007-2020 Pavel Ponec
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

package org.ujorm.core;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.ujorm.CompositeKey;
import org.ujorm.Key;
import org.ujorm.ListKey;
import org.ujorm.Validator;
import org.ujorm.core.annot.PackagePrivate;
import org.ujorm.criterion.Criterion;
import org.ujorm.criterion.Operator;
import org.ujorm.criterion.ProxyValue;
import org.ujorm.criterion.ValueCriterion;
import org.ujorm.tools.Assert;
import org.ujorm.validator.ValidationException;

/**
 * TODO: Rename to {@code KeyImpl}
 * The main implementation of the interface {@link Key}.
 * @see AbstractUjo
 * @author Pavel Ponec
 */
@Immutable
public class Property<U,VALUE> implements Key<U,VALUE> {

    /** Property Separator character */
    public static final char PROPERTY_SEPARATOR = '.';
    /** Undefined index value */
    public static final Integer UNDEFINED_INDEX = null;

    /** Property name */
    private String name;
    /** Property index, there are exist three index ranges:
     * <ul>
     *     <li>index == UNDEFINED_INDEX
     *     : an undefine index or a signal for auto-index action</li>
     *     <li>index &lt; UNDEFINED_INDEX
     *      : the discontinuous and ascending series of numbers that is generated using a special method</li>
     *     <li>index &gt; UNDEFINED_INDEX
     *     : the continuous and ascending series of numbers usable as a pointer to an array. This is a final state</li>
     * </ul>
     */
    private int index;
    /** Property type (class) */
    private Class<VALUE> type;
    /** Domain type type (class) */
    private Class<U> domainType;
    /** Property default value */
    private VALUE defaultValue;
    /** Input Validator */
    private Validator<VALUE> validator;
    /** Lock all properties after initialization */
    private boolean lock;


    /** A key sequencer for an index attribute
     * @see #_nextSequence()
     */
    private static int _sequencer = Integer.MIN_VALUE;

    /** Returns a next key index by a synchronized method.
     * The D key indexed by this method may not be in continuous series
     * however numbers have the <strong>upward direction</strong> always.
     */
    protected static synchronized int _nextRawSequence() {
        return _sequencer++;
    }

    /** POJO writer */
    private final BiConsumer<U,VALUE> writer;

    /** POJO reader */
    private final Function<U,VALUE> reader;

    /** Protected constructor */
    protected Property(
            @Nonnull final Function<U,VALUE> reader,
            @Nonnull final BiConsumer<U,VALUE> writer,
            @Nullable final Integer index
    ) {
        this.reader = reader;
        this.writer = writer;
        this.index = index != UNDEFINED_INDEX
                ? index
                : _nextRawSequence();
    }

    /** Lock the Property */
    protected void lock() {
        this.lock = true;
    }
    /** Check an internal log and throw an {@code IllegalStateException} if the object is locked. */
    protected final void checkLock() throws IllegalStateException {
        if (this.lock) {
            throw new IllegalStateException("The key is already initialized: " + this);
        }
    }

    /** The Name must not contain any dot character */
    private void setName(@Nonnull final String name) throws IllegalArgumentException{
        if (name==null) {
            return;
        }
        Assert.hasLength(name, "Key name of the '{}' must not be empty", domainType);

        Assert.isFalse(isPropertySeparatorDisabled()
                && name.indexOf(PROPERTY_SEPARATOR) > 0
            , "Key name '{}' must not contain a dot character '{}'."
            , name
            , PROPERTY_SEPARATOR);

        this.name = name;
    }

    /** Method returns the {@code true} in case the {@link #PROPERTY_SEPARATOR}
     * character is disabled in a key name.
     * The method can be overriden.
     * The {@code true} is a default value.
     */
    protected boolean isPropertySeparatorDisabled() {
        return true;
    }

    /** Is the key Locked? */
    @PackagePrivate final boolean isLock() {
        return lock;
    }

    /** Check validity of keys */
    protected void checkValidity() throws IllegalArgumentException {
        Assert.notNull(name, "Name must not be null for key index: #{}", index);
        Assert.notNull(type, "Type must not be null in the {}", this);
        Assert.notNull(domainType, "domainType", name);
        Assert.isTrue(defaultValue == null || type.isInstance(defaultValue)
                , "Default value have not properly type in the ", this);
    }

    /** Name of Property */
    @Override
    public final String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public final String getFullName() {
        return domainType != null
             ? domainType.getSimpleName() + '.' + name
             : name ;
    }

    /** Type of Property */
    @Override
    public final Class<VALUE> getValueClass() {
        return type;
    }

    /** Type of Property */
    @Override
    public final Class<U> getDomainClass() {
        return domainType;
    }

    /** Index of Property */
    @Override
    public final int getIndex() {
        return index;
    }

    /**
     * It is a basic method for setting an appropriate type safe value to an MapUjo object.
     * <br>For the setting value is used internally a method
     *     {@link AbstractUjo#writeValue(org.ujorm.Key, java.lang.Object) }
     * @see AbstractUjo#writeValue(org.ujorm.Key, java.lang.Object)
     */
    @Override
    public void setValue(final VALUE value, @Nonnull final U ujo) throws ValidationException {
        writer.accept(ujo, value);
    }

    /**
     * A shortcut for the method {@link #of(org.ujorm.Ujo)}.
     * @see #of(Ujo)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final VALUE getValue(@Nonnull final U ujo) {
        return reader.apply(ujo);
    }

    /**
     * It is a basic method for getting an appropriate type safe value from an Ujo object.
     * <br>For the getting value is used internally a method
     *     {@link AbstractUjo#readValue(org.ujorm.Key)} .
     * <br>Note: this method replaces the value of <strong>null</strong> for default
     * @param ujo If a NULL parameter is used then an exception NullPointerException is throwed.
     * @return Returns a type safe value from the ujo object.
     * @see AbstractUjo#readValue(Key)
     */
    @SuppressWarnings("unchecked")
    @Override
    public VALUE of(@Nonnull final U ujo) {
                throw new UnsupportedOperationException("TODO");
    }

    /** Returns a Default key value. The value replace the {@code null} value in the method Ujo.readValue(...).
     * If the default value is not modified, returns the {@code null}.
     */
    @Override
    public VALUE getDefaultValue() {
        return defaultValue;
    }

    /** Assign a Default value. The default value may be modified after locking - at your own risk.
     * <br>WARNING: the change of the default value modifies all values in all instances with the null value of the current key!
     */
    @SuppressWarnings("unchecked")
    public <PROPERTY extends Property> PROPERTY writeDefault(final VALUE value) {
        defaultValue = value;
        if (lock) checkValidity();
        return (PROPERTY) this;
    }

    /** Indicates whether a parameter value of the ujo "equal to" this default value. */
    @Override
    public boolean isDefault(@Nonnull final U ujo) {
        VALUE value = of(ujo);
        final boolean result
        =  value==defaultValue
        || (defaultValue!=null && defaultValue.equals(value))
        ;
        return result;
    }

    /**
     * If the key is the direct key of the related UJO class then method returns the TRUE value.
     * The return value false means, that key is type of {@link CompositeKey}.
     * <br>
     * Note: The composite keys are excluded from from function Ujo.readProperties() by default
     * and these keys should not be sent to methods Ujo.writeValue() and Ujo.readValue().
     * @see CompositeKey
     * @since 0.81
     * @deprecated use rather a negation of the method {@link #isComposite() }
     */
    @Deprecated
    @Override
    public final boolean isDirect() {
        return ! isComposite();
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isComposite() {
        return false;
    }

    /** A flag for a direction of sorting. This method returns true always.
     * @since 0.85
     * @see org.ujorm.core.UjoComparator
     */
    @Override
    public boolean isAscending() {
        return true;
    }

    /** Create a new instance of the <strong>indirect</strong> key with a descending direction of order.
     * @since 1.21
     * @see #isAscending()
     * @see org.ujorm.core.UjoComparator
     */
    @Override
    public Key<U, VALUE> descending(final boolean descending) {
            throw new UnsupportedOperationException("TODO");
    }

    /** Get the ujorm key validator or return the {@code null} value if no validator was assigned */
    @Override
    public Validator<VALUE> getValidator() {
        return validator;
    }

    /** Create new composite (indirect) instance.
     * @since 0.92
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> CompositeKey<U, T> join(@Nonnull final Key<? super VALUE, T> key) {
            throw new UnsupportedOperationException("TODO");

    }

    /** Create new composite (indirect) instance for an object type of ListKey.
     * @since 0.92
     */
    @Override
    public <T> ListKey<U, T> join(@Nonnull final ListKey<? super VALUE, T> key) {
            throw new UnsupportedOperationException("TODO");

    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> CompositeKey<U, T> join(@Nonnull final Key<? super VALUE, T> key, final String alias) {
            throw new UnsupportedOperationException("TODO");
    }

    /** Copy a value from the first UJO object to second one. A null value is not replaced by the default. */
    @Override
    public void copy(@Nonnull final  U from, @Nonnull final U to) {
            throw new UnsupportedOperationException("TODO");

    }

    /** Returns true if the key type is a type or subtype of the parameter class. */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isTypeOf(@Nonnull final Class type) {
        return type.isAssignableFrom(this.type);
    }

    /** Returns true if the domain type is a type or subtype of the parameter class. */
    @SuppressWarnings("unchecked")
    @Override
    public boolean isDomainOf(@Nonnull final Class type) {
        return type.isAssignableFrom(this.domainType);
    }

    /**
     * Returns true, if the key value equals to a parameter value. The key value can be null.
     *
     * @param ujo A basic Ujo.
     * @param value Null value is supported.
     * @return Accordance
     */
    @Override
    public boolean equals(@Nonnull final U ujo, @Nullable final VALUE value) {
        final Object myValue = of(ujo);
        if (myValue==value) { return true; }

        final boolean result
        =  myValue!=null
        && value  !=null
        && myValue.equals(value)
        ;
        return result;
    }

    /**
     * Returns {@code true}, if the key is the same like the checked argument.
     * The key value can be {@code null}.
     * @param key An another key.
     */
    @Override
    public boolean equals(@Nullable final Object key) {
        return this == key;
    }

    /**
     * Returns true, if the key name equals to the parameter value.
     */
    @Override
    public boolean equalsName(final CharSequence name) {
        return name!=null && name.toString().equals(this.name);
    }

    /** Returns a native hash
     * @see java.lang.System#identityHashCode(java.lang.Object)
     */
    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    /** Compare to another Key object by the index and name of the key.
     * @since 1.20
     */
    @Override
    public int compareTo(@Nonnull final Key p) {
        return index<p.getIndex() ? -1
             : index>p.getIndex() ?  1
             : name.compareTo(p.getName())
             ;
    }

    /** A char from Name */
    @Override
    public char charAt(final int index) {
        return name.charAt(index);
    }

    /** Length of the Name */
    @Override
    public int length() {
        return name.length();
    }

    /** Sub sequence from the Name */
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return name.subSequence(start, end);
    }

    /** Returns a name of Property */
    @Override
    public final String toString() {
        return name;
    }

    /** Returns the full name of the Key including a simple domain class.
     * <br>Example: Person.id
     * @deprecated Use the method {@link #getFullName()} rather.
     */
    @Deprecated
    @Override
    public final String toStringFull() {
        return getFullName();
    }

    /**
     * Returns the full name of the Key including all attributes.
     * <br>Example: Person.id {index=0, ascending=false, ...}
     * @param extended arguments false calls the method {@link #getFullName()} only.
     * @return the full name of the Key including all attributes.
     */
    @Override
    public String toStringFull(boolean extended) {
        return  extended
                ? getFullName() + Property.printAttributes(this)
                : getFullName() ;
    }

    /** Print  */
    @PackagePrivate static String printAttributes(@Nonnull final Key key) {
        return " {index=" + key.getIndex()
            + ", ascending=" + key.isAscending()
            + ", composite=" + key.isComposite()
            + ", default=" + key.getDefaultValue()
            + ", validator=" + (key.getValidator()!=null ? key.getValidator().getClass().getSimpleName() : null)
            + ", type=" + key.getValueClass()
            + ", domainType=" + key.getDomainClass()
            + ", class=" + key.getClass().getName()
            + "}" ;
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forCriterion(@Nonnull final Operator operator, @Nullable final VALUE value) {
        return Criterion.forCriton(this, operator, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forCriterion(@Nonnull final Operator operator, @Nullable final ProxyValue<VALUE> proxyValue) {
        return Criterion.forCriton(this, operator, proxyValue);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forCriterion(@Nonnull final Operator operator, Key<?, VALUE> value) {
        return Criterion.forCriton(this, operator, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forEq(@Nullable final VALUE value) {
        return Criterion.forCriton(this, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forEq(@Nonnull final Key<U, VALUE> value) {
        return Criterion.forCrn(this, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forEq(@Nonnull final ProxyValue<VALUE> proxyValue) {
        return Criterion.forCriton(this, Operator.EQ, proxyValue);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forIn(@Nonnull final Collection<VALUE> list) {
        return Criterion.forIn(this, list);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNotIn(@Nonnull final Collection<VALUE> list) {
        return Criterion.forNotIn(this, list);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forIn(@Nonnull final VALUE... list) {
        return Criterion.forIn(this, list);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNotIn(@Nonnull final VALUE... list) {
        return Criterion.forNotIn(this, list);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNull() {
        return Criterion.forNull(this);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNotNull() {
        return Criterion.forNotNull(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Criterion<U> forLength() {
        final Criterion<U> result = forNotNull()
            .and(Criterion.forCriton(this, Operator.NOT_EQ, (VALUE) getEmptyValue()))
                ;
        return result;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Criterion<U> forEmpty() {
        final Criterion<U> result = forNull()
                .or(ValueCriterion.forCriton(this, Operator.EQ, (VALUE) getEmptyValue()));
        return result;
    }

    /** Returns an empty value */
    @Nullable
    private Object getEmptyValue() {
        if (CharSequence.class.isAssignableFrom(type)) {
            return "";
        }
        if (type.isArray()) {
            return Array.newInstance(type, 0);
        }
        if (List.class.isAssignableFrom(type)) {
            return Collections.EMPTY_LIST;
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNeq(@Nullable final VALUE value) {
        return Criterion.forCriton(this, Operator.NOT_EQ, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forGt(@Nullable final VALUE value) {
        return Criterion.forCriton(this, Operator.GT, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forGe(@Nullable final VALUE value) {
        return Criterion.forCriton(this, Operator.GE, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forLt(@Nullable final VALUE value) {
        return Criterion.forCriton(this, Operator.LT, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forLe(@Nullable final VALUE value) {
        return Criterion.forCriton(this, Operator.LE, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forSql(String sqlCondition) {
        return Criterion.forSql(this, sqlCondition);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forSql(String sqlCondition, VALUE value) {
        return Criterion.forSql(this, sqlCondition, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forSqlUnchecked(@Nonnull final String sqlCondition, @Nullable final Object value) {
        return Criterion.forSqlUnchecked(this, sqlCondition, value);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forAll() {
        return Criterion.forAll(this);
    }

    /** {@inheritDoc} */
    @Override
    public Criterion<U> forNone() {
        return Criterion.forNone(this);
    }

    // --------- STATIC METHODS -------------------

    /** Returns a new instance of key where the default value is null.
     * The method assigns a next key index.
     * @hidden
     */
    public static <D,VALUE> Property<D,VALUE> of(String name, Class<VALUE> type, VALUE value, Integer index, boolean lock) {
        return of(name, type, value, index, (Validator) null, lock);
    }

    /** Returns a new instance of key where the default value is null.
     * The method assigns a next key index.
     * @hidden
     */
    public static <D,VALUE> Property<D,VALUE> of(String name, Class<VALUE> type, VALUE value, Integer index, Validator validator, boolean lock) {
            throw new UnsupportedOperationException("TODO");

    }


    /** Returns a new instance of key where the default value is null.
     * The method assigns a next key index.
     * @hidden
     */
    public static <D,VALUE> Property<D,VALUE> of(String name, Class<VALUE> type, Class<D> domainType, int index) {
        throw new UnsupportedOperationException("TODO");

    }

    /** Returns a new instance of key where the default value is null.
     * The method assigns a next key index.
     * @hidden
     */
    public static <D,VALUE> Property<D,VALUE> of(String name, Class<VALUE> type) {
        final Class<D> domainType = null;
        return of(name, type, domainType, Property.UNDEFINED_INDEX);
    }

    /** Returns a new instance of key where the default value is null.
     * The method assigns a next key index.
     * @hidden
     */
    public static <D,VALUE> Property<D,VALUE> of(String name, Class<VALUE> type, Class<D> domainType) {
        return of(name, type, domainType, Property.UNDEFINED_INDEX);
    }

    /** A Property Factory where a key type is related from from default value.
     * Method assigns a next key index.
     * @hidden
     */
    public static <D, VALUE> Property<D, VALUE> of(String name, VALUE value, int index) {
            throw new UnsupportedOperationException("TODO");

    }

    /** A Property Factory where a key type is related from from default value.
     * Method assigns a next key index.
     * @hidden
     */
    public static <D, VALUE> Property<D, VALUE> of(String name, VALUE value) {
         return of(name, value, UNDEFINED_INDEX);
    }


    /** A Property Factory where a key type is related from from default value.
     * Method assigns a next key index.
     * @hidden
     */
    @SuppressWarnings("unchecked")
    public static <D, VALUE> Property<D, VALUE> of(final Key<D,VALUE> p, int index) {
         return of(p.getName(), p.getValueClass(), p.getDefaultValue(), index, true);
    }


    /** A Property Factory where a key type is related from from default value.
     * <br>Warning: Method does not lock the key so you must call AbstractUjo.init(..) method after initialization!
     * @hidden
     */
    @SuppressWarnings("unchecked")
    public static <D, VALUE> Key<D, VALUE> of(final Key<D,VALUE> p) {
         return of(p.getName(), p.getValueClass(), (VALUE) p.getDefaultValue(), UNDEFINED_INDEX, false);
    }


}
