/*
 * Copyright 2012-2015 Ponec
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
package org.ujorm.tools;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import junit.framework.TestCase;

/**
 * Tests for the class MessageService
 * @author Pavel Ponec
 */
public class MessageServiceTest extends TestCase {

    /** Message Argument */
    private static MessageArg<Integer> ID = new MessageArg<>("ID");
    /** Message Argument */
    private static MessageArg<Date> DATE = new MessageArg<>("DATE");
    /** Message Argument */
    private static MessageArg<String> TEXT = new MessageArg<>("TEXT");
    /** Message Argument */
    private static MessageArg<BigDecimal> NUMBER = new MessageArg<>("NUMBER");

    public MessageServiceTest() {
    }

    public MessageServiceTest(String testName) {
        super(testName);
    }

    /** Demo test. */
    public void testDemo() {
        final MessageService service = new MessageService();
        final MessageArg<String> NAME = new MessageArg<>("NAME");
        String expResult = "The ORM framework Ujorm.";
        String expTemplate = "The ORM framework ${NAME}.";
        String template = service.template("The ORM framework ", NAME, ".");
        Map<String, Object> args = service.map(NAME, "Ujorm");
        String result = service.format(template, args);
        assertEquals(expTemplate, template);
        assertEquals(expResult, result);
    }

    /** Test of map method, of class MessageService. */
    public void testTemplate_1() {
        System.out.println("testTemplate");

        MessageService service = new MessageService();
        String expResult = "A${ID}B";
        String result = service.template("A", ID, "B");
        assertEquals(expResult, result);
    }

    /** Test of map method, of class MessageService. */
    public void testTemplate_2() {
        System.out.println("testTemplate");

        MessageService service = new MessageService();
        String expResult = "${ID}AB";
        String result = service.template(ID, "AB");
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_1a() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Number is 1";
        String template = ("Number is ${ID}");
        Map<String, Object> args = service.map(ID, 1);
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_1b() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Number is 1";
        String template = ("${TEXT} is 1");
        Map<String, Object> args = service.map(TEXT, "Number");
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_2() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Price is 1 CZK";
        String template = service.template("Price is ", ID, " ", TEXT);
        Map<String, Object> args = service.map(ID, 1, TEXT, "CZK");
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_3() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "1 CZK";
        String template = service.template(ID, " ", TEXT);
        Map<String, Object> args = service.map(ID, 1, TEXT, "CZK", "WRONG ARGUMENT");
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_4() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Price is 1 ${TEXT}";
        String template = service.template("Price is ", ID, " ", TEXT);
        Map<String, Object> args = service.map(ID, 1, DATE, new Date());
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_5() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Price is   +123.46 CZK";
        String template = service.template("Price is ${NUMBER,%+9.2f} ${TEXT}");
        Map<String, Object> args = service.map(NUMBER, new BigDecimal("123.456"), TEXT, "CZK");
        String result = service.format(template, args, Locale.ENGLISH);
        assertEquals(expResult, result);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_6() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Date is: 2016-05-04 03:02:01";
        Map<String, Object> args = service.map(DATE, getCalendar().getTime());
        //
        String template1 = "Date is: ${DATE,%tY-%tm-%td %tH:%tM:%tS}";
        String result1 = service.format(template1, args, Locale.ENGLISH);
        assertEquals(expResult, result1);
        //
        String template2 = "Date is: ${DATE,%tF %tT}";
        String result2 = service.format(template2, args, Locale.ENGLISH);
        assertEquals(expResult, result2);
    }

    /** Test of format method, of class MessageService. */
    public void testFormat_7() {
        System.out.println("format");

        MessageService service = new MessageService();
        String expResult = "Date is: 2016-05-04 03:02:01";
        Map<String, Object> args = service.map(DATE, LocalDateTime.parse("2016-05-04T03:02:01"));
        //
        String template1 = "Date is: ${DATE,%tY-%tm-%td %tH:%tM:%tS}";
        String result1 = service.format(template1, args, Locale.ENGLISH);
        assertEquals(expResult, result1);
        //
        String template2 = "Date is: ${DATE,%tF %tT}";
        String result2 = service.format(template2, args, Locale.ENGLISH);
        assertEquals(expResult, result2);
    }

    /** Create a new Calendar for date: 2016-05-04T03:02:01  */
    private Calendar getCalendar() {
        return getCalendar(2016, Calendar.MAY, 4, 3, 2, 1);
    }

    /** Create a new Calendar */
    private Calendar getCalendar(int year, int month, int day, int hour, int minute, int sec) {
        final Calendar result = Calendar.getInstance(Locale.ENGLISH);
        result.set(Calendar.YEAR, year);
        result.set(Calendar.MONTH, month);
        result.set(Calendar.DAY_OF_MONTH, day);
        result.set(Calendar.HOUR_OF_DAY, hour);
        result.set(Calendar.MINUTE, minute);
        result.set(Calendar.SECOND, sec);
        result.set(Calendar.MILLISECOND, 0);
        //
        return result;
    }

}