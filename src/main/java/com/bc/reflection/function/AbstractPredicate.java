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

import java.util.Objects;

/**
 * For use with java 7 api. Mimics java 8 {@link java.util.function.Predicate}
 * @author Chinomso Bassey Ikwuagwu on Oct 10, 2018 2:23:51 PM
 * @see java.util.function.Predicate
 */
public abstract class AbstractPredicate<T> implements Predicate<T> {

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code false}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ANDed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * AND of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    public AbstractPredicate<T> and(final Predicate<? super T> other) {
        this.requireNonNull(other);
        return new AbstractPredicate<T>() {
            @Override
            public boolean test(T obj) {
                return AbstractPredicate.this.test(obj) && other.test(obj);
            }
        };
    }

    /**
     * Returns a predicate that represents the logical negation of this
     * predicate.
     *
     * @return a predicate that represents the logical negation of this
     * predicate
     */
    public Predicate<T> negate() {
        return new AbstractPredicate<T>() {
            @Override
            public boolean test(T obj) {
                return !AbstractPredicate.this.test(obj);
            }
        };
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another.  When evaluating the composed
     * predicate, if this predicate is {@code true}, then the {@code other}
     * predicate is not evaluated.
     *
     * <p>Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other a predicate that will be logically-ORed with this
     *              predicate
     * @return a composed predicate that represents the short-circuiting logical
     * OR of this predicate and the {@code other} predicate
     * @throws NullPointerException if other is null
     */
    public Predicate<T> or(final Predicate<? super T> other) {
        this.requireNonNull(other);
        return new AbstractPredicate<T>() {
            @Override
            public boolean test(T obj) {
                return AbstractPredicate.this.test(obj) || other.test(obj);
            }
        };
    }

    /**
     * Returns a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}.
     *
     * @param <T> the type of arguments to the predicate
     * @param targetRef the object reference with which to compare for equality,
     *               which may be {@code null}
     * @return a predicate that tests if two arguments are equal according
     * to {@link Objects#equals(Object, Object)}
     */
    static <T> AbstractPredicate<T> isEqual(final Object targetRef) {
        return (null == targetRef)
                ? new AbstractPredicate<T>() {
                    @Override
                    public boolean test(T obj) {
                        return obj == null;
                    }
                }
                : new AbstractPredicate<T>() {
                    @Override
                    public boolean test(T obj) {
                        return targetRef.equals(obj);
                    }
                };
    }

    private <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }
}
