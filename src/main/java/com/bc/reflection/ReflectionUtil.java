/*
 * Copyright 2017 NUROX Ltd.
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

import com.bc.reflection.function.MethodIsGetter;
import com.bc.reflection.function.MethodIsSetter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.bc.reflection.function.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 25, 2017 8:51:02 PM
 */
public class ReflectionUtil {
    
    public ReflectionUtil() { }
    
    public Object newInstanceForCollectionType(Class type) {
        try{
            return this.newInstance(type);
        }catch(RuntimeException ignored) {
            return this.newInstance(this.getClassForCollectionType(type));
        }
    }
    
    public Class getClassForCollectionType(Class type) {
        if(Set.class.isAssignableFrom(type)) {
            return LinkedHashSet.class;
        }else if(Collection.class.isAssignableFrom(type)) {
            return ArrayList.class;
        }else if(Map.class.isAssignableFrom(type)) {
            return LinkedHashMap.class;
        }else{
            throw new UnsupportedOperationException();
        }
    }
    
    public <T> T newInstance(Class<T> entityType) {
        try{
            return entityType.getConstructor().newInstance();
        }catch(NoSuchMethodException | SecurityException | InstantiationException | 
                IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Type [] getGenericReturnTypeArguments(Method method) {
        final Type genericReturnType = method.getGenericReturnType();
        return ((ParameterizedType)genericReturnType).getActualTypeArguments();
    }

    public List<Type []> getGenericParameterTypeArguments(Method method) {
        final Type [] genericParameterTypes = method.getGenericParameterTypes();
        final List<Type[]> output = new ArrayList(genericParameterTypes.length);
        for(Type t : genericParameterTypes) {
            if(t instanceof ParameterizedType) {
                final Type [] actual = ((ParameterizedType)t).getActualTypeArguments();
                output.add(actual);
            }
        }
        return output;
    }
    
    public Type [] getGenericTypeArguments(Field field) {
        final Type genericReturnType = field.getGenericType();
        if(genericReturnType instanceof ParameterizedType) {
            return ((ParameterizedType)genericReturnType).getActualTypeArguments();
        }else{
            return new Type[0];
        }
    }

    public Object getValue(Object object, String name) {
        
        return this.getValue(object.getClass(), object, object.getClass().getDeclaredMethods(), name);
    }

    public Object getValue(Class aClass, 
            Object object, Method [] methods, String name) {
        
        final Method method = getMethod(false, methods, name);

        if(method == null) {
            throw new IllegalArgumentException("Could not find matching method for: "+name+" in class: "+aClass);
        }
        
        try{
            
            return method.invoke(object);
            
        }catch(Exception e) {
            
            StringBuilder builder = new StringBuilder("Error getting entity value.");
            builder.append(" Entity: ").append(object);
            builder.append(", Method: ").append(method==null?null:method.getName());
            builder.append(", Column: ").append(name);

            throw new UnsupportedOperationException(builder.toString(), e);
        }
    }

    public void setValue(Object object, String name, Object value) {
        
        this.setValue(object.getClass(), object, object.getClass().getDeclaredMethods(), name, value);
    }
    
    public void setValue(Class aClass, 
            Object object, Method [] methods, 
            String name, Object value) {
        
        final Method method = getMethod(true, methods, name);
        if(method == null) {
            throw new IllegalArgumentException("Could not find matching method for: "+name+" in class: "+aClass);
        }
        
        try{
            
            method.invoke(object, value);
            
        }catch(Exception e) {
            
            StringBuilder builder = new StringBuilder("Error setting entity value.");
            builder.append(" Object: ").append(object);
            builder.append(", Method: ").append(method==null?null:method.getName());
            builder.append(", Name: ").append(name);
            builder.append(", Value: ").append(value);
            builder.append(", Value type: ").append(value==null?null:value.getClass());
            builder.append(", Expected type: ").append(method==null?null:method.getParameterTypes()[0]);

            throw new UnsupportedOperationException(builder.toString(), e);
        }
    }
    
    public Method getMethodAlphaNumeric(boolean setter, Method [] methods, String columnName) {
        return this.getMethod(setter, methods, this.removeAll(columnName, '_').toString());
    }

    /**
     * Methods of the {@link java.lang.Object} class are not considered
     * @param setter boolean, if true only setter methods are considered
     * @param methods The array of methods within which to search for a 
     * method matching the specified column name.
     * @param columnName The column name for which a method with a matching
     * name is to be returned.
     * @return A method whose name matches the input columnName or null if none was found
     */
    public Method getMethod(boolean setter, Method [] methods, String columnName) {
        
        final Predicate<Method> methodTest = setter ? new MethodIsSetter() : new MethodIsGetter();
        
        return this.getMethod(methodTest, methods, columnName);
    }
    
    /**
     * Methods of the {@link java.lang.Object} class are not considered
     * @param methodTest Predicate<java.lang.Method> test to filter which methods to consider
     * @param methods The array of methods within which to search for a 
     * method matching the specified column name.
     * @param columnName The column name for which a method with a matching
     * name is to be returned.
     * @return A method whose name matches the input columnName or null if none was found
     */
    public Method getMethod(Predicate<Method> methodTest, Method [] methods, String columnName) {
        
        Method output = null;
        
        final String lhs = this.capitalize(columnName);
        
        for(Method method : methods) {
            
            if(method.getDeclaringClass() == Object.class) {
                continue;
            }
            
            if(!methodTest.test(method)) {
                continue;
            }
            
            final String methodName = method.getName();
            
            final String rhs = methodName.substring(this.firstIndexOfUpperCaseChar(methodName));

            if(lhs.equals(rhs)) {
                output = method;
                break;
            }
        }

        return output;
    }

    /**
     * Methods of the {@link java.lang.Object} class are not considered
     * @param setter boolean, if true only setter methods are considered
     * @param method Method. The method for which a column with a name 
     * matching the method name will be returned/
     * @param outputIfNone
     * @return A column whose name matches the input Method name, or null if
     * no such column could be inferred.
     */
    public String getName(boolean setter, Method method, String outputIfNone) {
        final StringBuilder builder = new StringBuilder();
        this.appendName(setter, method, builder);
        return builder.length() == 0 ? outputIfNone : builder.toString();
    }
    
    public void appendName(boolean setter, Method method, StringBuilder buff) {

        final Predicate<Method> methodTest = setter ? new MethodIsSetter() : new MethodIsGetter();
        
        final String methodName = method.getName();
        
        if(method.getDeclaringClass() != Object.class && methodTest.test(method)) {
        
            final String prefix;
            if(setter) {
                prefix = "set";
            }else{
                prefix = methodName.startsWith("is") ? "is" : "get";
            }

            final int prefixLen = prefix.length();
            final int len = methodName.length();

            boolean doneFirst = false;
            for(int i=0; i<len; i++) {

                if(i < prefixLen) {
                    continue;
                }

                char ch = methodName.charAt(i);

                if(!doneFirst) {
                    doneFirst = true;
                    ch = Character.toLowerCase(ch);
                }

                buff.append(ch);
            }
        }
    }
    
    public int firstIndexOfUpperCaseChar(String input) {
        int output = -1;
        for(int i=0; i<input.length(); i++) {
            final char ch = input.charAt(i);
            if(Character.isUpperCase(ch)) {
                output = i;
                break;
            }
        }
        return output;
    }
    
    public String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public String unCapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public StringBuilder removeAll(String input, char toRemove) {
        StringBuilder builder = new StringBuilder(input.length());
        for(int i=0; i<input.length(); i++) {
            char ch = input.charAt(i);
            if(ch == toRemove) {
                continue;
            }
            builder.append(ch);
        }
        return builder;
    }
}
