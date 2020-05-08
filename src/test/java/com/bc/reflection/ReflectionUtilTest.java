/*
 * Copyright 2018 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bc.reflection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Josh
 */
public class ReflectionUtilTest {
    
    public ReflectionUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testNewInstanceForCollectionType() {
        System.out.println("newInstanceForCollectionType");
        this.testNewInstanceForCollectionType(List.class);
        this.testNewInstanceForCollectionType(Set.class);
        this.testNewInstanceForCollectionType(Collection.class);
        this.testNewInstanceForCollectionType(Map.class);
    }
    public void testNewInstanceForCollectionType(Class type) {
        final ReflectionUtil instance = new ReflectionUtil();
        final Object result = instance.newInstanceForCollectionType(type);
        System.out.println("Type: " + type + ", instance type: " + result.getClass() + ", instance: " + result);
        assertTrue(type.isAssignableFrom(result.getClass()));
    }

    /**
     * Test of newInstance method, of class ReflectionUtil.
     */
    @Test
    public void testNewInstance() {
        System.out.println("newInstance");
        final ReflectionUtil instance = new ReflectionUtil();
        final Object result = instance.newInstance(Person.class);
        assertEquals(Person.class, result.getClass());
    }

    /**
     * Test of getMethod method, of class ReflectionUtil.
     */
    @Test
    public void testGetMethod() throws Exception {
        System.out.println("getMethod");
        final boolean setter = false;
        final Method[] methods = Person.class.getMethods();
        final String columnName = "alive";
        final ReflectionUtil instance = new ReflectionUtil();
        final Method expResult = Person.class.getMethod("isAlive");
        System.out.println("Expected: " + expResult);
        final Method result = instance.getMethod(setter, methods, columnName);
        System.out.println("   Found: " + result);
        assertEquals(expResult, result);
    }
}

/**
    @Test
    public void testGetGenericReturnTypeArguments() {
        System.out.println("getGenericReturnTypeArguments");
        Method method = null;
        ReflectionUtil instance = new ReflectionUtil();
        Type[] expResult = null;
        Type[] result = instance.getGenericReturnTypeArguments(method);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetGenericParameterTypeArguments() {
        System.out.println("getGenericParameterTypeArguments");
        Method method = null;
        ReflectionUtil instance = new ReflectionUtil();
        List expResult = null;
        List result = instance.getGenericParameterTypeArguments(method);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetGenericTypeArguments() {
        System.out.println("getGenericTypeArguments");
        Field method = null;
        ReflectionUtil instance = new ReflectionUtil();
        Type[] expResult = null;
        Type[] result = instance.getGenericTypeArguments(method);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValue_Object_String() {
        System.out.println("getValue");
        Object object = null;
        String name = "";
        ReflectionUtil instance = new ReflectionUtil();
        Object expResult = null;
        Object result = instance.getValue(object, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetValue_4args() {
        System.out.println("getValue");
        Class aClass = null;
        Object object = null;
        Method[] methods = null;
        String name = "";
        ReflectionUtil instance = new ReflectionUtil();
        Object expResult = null;
        Object result = instance.getValue(aClass, object, methods, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetValue_3args() {
        System.out.println("setValue");
        Object object = null;
        String name = "";
        Object value = null;
        ReflectionUtil instance = new ReflectionUtil();
        instance.setValue(object, name, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetValue_5args() {
        System.out.println("setValue");
        Class aClass = null;
        Object object = null;
        Method[] methods = null;
        String name = "";
        Object value = null;
        ReflectionUtil instance = new ReflectionUtil();
        instance.setValue(aClass, object, methods, name, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFirstIndexOfUpperCaseChar() {
        System.out.println("firstIndexOfUpperCaseChar");
        String input = "";
        ReflectionUtil instance = new ReflectionUtil();
        int expResult = 0;
        int result = instance.firstIndexOfUpperCaseChar(input);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testCapitalize() {
        System.out.println("capitalize");
        String name = "";
        ReflectionUtil instance = new ReflectionUtil();
        String expResult = "";
        String result = instance.capitalize(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testUnCapitalize() {
        System.out.println("unCapitalize");
        String name = "";
        ReflectionUtil instance = new ReflectionUtil();
        String expResult = "";
        String result = instance.unCapitalize(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testRemoveAll() {
        System.out.println("removeAll");
        String input = "";
        char toRemove = ' ';
        ReflectionUtil instance = new ReflectionUtil();
        StringBuilder expResult = null;
        StringBuilder result = instance.removeAll(input, toRemove);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/