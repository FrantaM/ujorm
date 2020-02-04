package org.ujorm2.core;

/**
 * This context provides instances of the meta model including direct keys
 * @author Pavel Ponec
 */
public class UjoContext {

    /** Default context of Ujorm */
    public static final UjoContext DEFAULT_CONTEXT = new UjoContext();

//    public <U, V> Key<U, V> createKey(Class<U> domainClass, Class<V> valueClass,  Setter setter, Getter getter) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public <U, V> Object createEntity(Class<U> domainClass, Class<V> valueClass,  Setter setter, Getter getter) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    /** Get cached meta-model */
//    public <T, M extends MetaInterface<T>> M getMetaModel(Class<T> domain, Class<M> meta) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }


    public static UjoContext of() {
        return DEFAULT_CONTEXT;
    }

}
