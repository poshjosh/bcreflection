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

package com.bc.reflection.function;

import java.lang.reflect.Method;
import java.util.function.Predicate;

/**
 * @author Chinomso Bassey Ikwuagwu on Oct 10, 2018 3:09:54 PM
 */
public class MethodHasReturnType implements Predicate<Method> {

    private final Class returnType;
    
    public MethodHasReturnType(Class returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean test(Method method) {
        return this.returnType.isAssignableFrom(method.getReturnType());
    }
}
