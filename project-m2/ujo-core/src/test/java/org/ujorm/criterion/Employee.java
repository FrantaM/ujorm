/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ujorm.criterion;

import org.ujorm.Key;
import org.ujorm.implementation.map.MapUjoExt;
import org.ujorm.extensions.PathProperty;

/**
 *
 * @author Pavel Ponec
 */
public final class Employee extends MapUjoExt<Employee> {

    public static final Key<Employee, String> NAME = newKey("Name");
    public static final Key<Employee, Boolean> MALE = newKey("Male", false);
    public static final Key<Employee, Double> CASH = newKey("Cash", 0d);
    public static final Key<Employee, Employee> MOTHER = newKey("Mother");

    static { init(Employee.class); }

    public Employee() {

    }

    public Employee(String name) {
        init();
        NAME.setValue(this, name);
    }

    public Employee(double cash) {
        init();
        CASH.setValue(this, cash);
    }


    public void init() {
        set(NAME, "Jack");
        set(MOTHER, new Employee());
        set(MOTHER, NAME, "Jane");
        set(MOTHER, CASH, 200d);

        String name = get(MOTHER, NAME);
        double cash = get(MOTHER, CASH);

        System.out.println(name + " " + cash);
    }

    public void init2() {
        set(NAME, "Jack").set(CASH, 50d);
        set(MOTHER, new Employee());
        get(MOTHER).set(NAME, "Jackie").set(CASH, 10D);

        String name = get(MOTHER, NAME);
        double cash = get(MOTHER, CASH);

        System.out.println(name + " " + cash);
    }

    public void init3() {
        set(NAME, "Jack").set(CASH, 50D);
        set(MOTHER, new Employee());
        set(MOTHER, MOTHER, new Employee());
        set(MOTHER, MOTHER, CASH, 20D);
        set(MOTHER, MOTHER, MOTHER, new Employee());
        get(MOTHER, MOTHER, MOTHER).set(NAME, "Jack").set(CASH, 10D);
        get(MOTHER, MOTHER, MOTHER).set(NAME, "Jack").set(CASH, 1.1);

        String name = get(PathProperty.newInstance(MOTHER, MOTHER, MOTHER, NAME));
        Double cash = get(PathProperty.newInstance(MOTHER, MOTHER, MOTHER, CASH));

        String name2 = get(MOTHER, MOTHER, NAME);
        Double cash2 = get(MOTHER, MOTHER, CASH);

        System.out.println(name + " " + cash);
    }
}