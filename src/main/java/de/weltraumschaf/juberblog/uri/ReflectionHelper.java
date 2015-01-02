/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package de.weltraumschaf.juberblog.uri;

import de.weltraumschaf.commons.guava.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods for Java reflection.
 *
 * @author Paul Sandoz (paul.sandoz at oracle.com)
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 */
final class ReflectionHelper {

    private static final Logger LOGGER = Logger.getLogger(ReflectionHelper.class.getName());
    private static final PrivilegedAction NoOpPrivilegedACTION = new PrivilegedAction() {
        @Override
        public Object run() {
            return null;
        }
    };

    /**
     * Get the declaring class of an accessible object. Supported are {@link Method},
     * {@link Field} and {@link Constructor} accessible object types.
     *
     * @param ao an accessible object.
     * @return the declaring class of an accessible object.
     * @throws IllegalArgumentException in case the type of the accessible object
     *                                  is not supported.
     */
    public static Class<?> getDeclaringClass(AccessibleObject ao) {
        if (ao instanceof Method) {
            return ((Method) ao).getDeclaringClass();
        } else if (ao instanceof Field) {
            return ((Field) ao).getDeclaringClass();
        } else if (ao instanceof Constructor) {
            return ((Constructor) ao).getDeclaringClass();
        } else {
            throw new IllegalArgumentException("Unsupported accessible object type: " + ao.getClass().getName());
        }
    }

    /**
     * Create a string representation of an object.
     * <p/>
     * Returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character {@code '&#64;'}, and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <pre>
     * o.getClass().getName() + '@' + Integer.toHexString(o.hashCode())
     * </pre>
     *
     * @param o the object.
     * @return the string representation of the object.
     */
    public static String objectToString(Object o) {
        if (o == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName()).
                append('@').append(Integer.toHexString(o.hashCode()));
        return sb.toString();
    }

    /**
     * Create a string representation of a method and an instance whose
     * class implements the method.
     * <p/>
     * Returns a string consisting of the name of the class of which the object
     * is an instance, the at-sign character {@code '&#64;'},
     * the unsigned hexadecimal representation of the hash code of the
     * object, the character {@code '.'}, the name of the method,
     * the character {@code '('}, the list of method parameters, and
     * the character {@code ')'}. In other words, those method returns a
     * string equal to the value of:
     * <pre>
     * o.getClass().getName() + '@' + Integer.toHexString(o.hashCode()) +
     *         '.' + m.getName() + '(' + &lt;parameters&gt; + ')'.</pre>
     *
     * @param o the object whose class implements {@code m}.
     * @param m the method.
     * @return the string representation of the method and instance.
     */
    public static String methodInstanceToString(Object o, Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName()).
                append('@').append(Integer.toHexString(o.hashCode())).
                append('.').append(m.getName()).append('(');

        Class[] params = m.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            sb.append(getTypeName(params[i]));
            if (i < (params.length - 1)) {
                sb.append(",");
            }
        }

        sb.append(')');

        return sb.toString();
    }

    /**
     * Get the Java type or array name.
     *
     * If the class is representing an array, the {@code "[]"} suffix will be added
     * to the name of the type for each dimension of an array.
     *
     * @param type Java type (can represent an array).
     * @return Java type or array name.
     */
    private static String getTypeName(Class<?> type) {
        if (type.isArray()) {
            Class<?> cl = type;
            int dimensions = 0;
            while (cl.isArray()) {
                dimensions++;
                cl = cl.getComponentType();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(cl.getName());
            for (int i = 0; i < dimensions; i++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return type.getName();
    }

    /**
     * Get privileged action to obtain Class from given class name.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     * <p/>
     * The context class loader will be utilized if accessible and non-null.
     * Otherwise the defining class loader of this class will
     * be utilized.
     *
     * @param <T>  class type.
     * @param name class name.
     * @return privileged action to obtain desired Class.
     *         The action could return {@code null} if the class cannot be found.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static <T> PrivilegedAction<Class<T>> classForNamePA(String name) {
        return classForNamePA(name, getContextClassLoader());
    }

    /**
     * Get privileged action to obtain Class from given class name.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     * <p/>
     * @param <T>  class type.
     * @param name class name.
     * @param cl   class loader to use, if {@code null} then the defining class loader
     *             of this class will be utilized.
     * @return privileged action to obtain desired Class.
     *             The action could return {@code null} if the class cannot be found.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    @SuppressWarnings("unchecked")
    public static <T> PrivilegedAction<Class<T>> classForNamePA(final String name, final ClassLoader cl) {
        return new PrivilegedAction<Class<T>>() {
            @Override
            public Class<T> run() {
                if (cl != null) {
                    try {
                        return (Class<T>) Class.forName(name, false, cl);
                    } catch (ClassNotFoundException ex) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE,
                                    "Unable to load class " + name + " using the supplied class loader "
                                            + cl.getClass().getName() + ".", ex);
                        }
                    }
                }
                try {
                    return (Class<T>) Class.forName(name);
                } catch (ClassNotFoundException ex) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Unable to load class " + name + " using the current class loader.", ex);
                    }
                }

                return null;
            }
        };
    }

    /**
     * Get privileged action to obtain class loader for given class.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param clazz class for which to get class loader.
     * @return privileged action to obtain class loader for the {@code clazz} class.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<ClassLoader> getClassLoaderPA(final Class<?> clazz) {
        return new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        };
    }

    /**
     * Get privileged action to obtain fields declared on given class.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param clazz class for which to get the declared fields.
     * @return privileged action to obtain fields declared on the {@code clazz} class.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<Field[]> getDeclaredFieldsPA(final Class<?> clazz) {
        return new PrivilegedAction<Field[]>() {
            @Override
            public Field[] run() {
                return clazz.getDeclaredFields();
            }
        };
    }

    /**
     * Get privileged action to obtain methods declared on given class.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param clazz class for which to get the declared methods.
     * @return privileged action to obtain methods declared on the {@code clazz} class.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<Method[]> getDeclaredMethodsPA(final Class<?> clazz) {
        return new PrivilegedAction<Method[]>() {
            @Override
            public Method[] run() {
                return clazz.getDeclaredMethods();
            }
        };
    }

    /**
     * Get privileged exception action to obtain Class from given class name.
     * If run using security manager, the returned privileged exception action
     * must be invoked within a doPrivileged block.
     * <p/>
     * The actual context class loader will be utilized if accessible and non-null.
     * Otherwise the defining class loader of the calling class will be utilized.
     *
     * @param <T>  class type.
     * @param name class name.
     * @return privileged exception action to obtain the Class.
     *         The action could throw {@link ClassNotFoundException} or return {@code null} if the class cannot be found.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedExceptionAction)
     */
    public static <T> PrivilegedExceptionAction<Class<T>> classForNameWithExceptionPEA(final String name) throws ClassNotFoundException {
        return classForNameWithExceptionPEA(name, getContextClassLoader());
    }

    /**
     * Get privileged exception action to obtain Class from given class name.
     * If run using security manager, the returned privileged exception action
     * must be invoked within a doPrivileged block.
     *
     * @param <T>  class type.
     * @param name class name.
     * @param cl   class loader to use, if {@code null} then the defining class loader
     *             of the calling class will be utilized.
     * @return privileged exception action to obtain the Class.
     *         The action throws {@link ClassNotFoundException}
     *         or returns {@code null} if the class cannot be found.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedExceptionAction)
     */
    @SuppressWarnings("unchecked")
    public static <T> PrivilegedExceptionAction<Class<T>> classForNameWithExceptionPEA(final String name, final ClassLoader cl) throws ClassNotFoundException {
            return new PrivilegedExceptionAction<Class<T>>() {
                @Override
                public Class<T> run() throws ClassNotFoundException {
                    if (cl != null) {
                        try {
                            return (Class<T>) Class.forName(name, false, cl);
                        } catch (ClassNotFoundException ex) {
                            // ignored on purpose
                        }
                    }
                    return (Class<T>) Class.forName(name);
                }
            };
    }

    /**
     * Get privileged action to obtain context class loader.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @return privileged action to obtain the actual context class loader.
     *         The action could return {@code null} if context class loader has not been set.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<ClassLoader> getContextClassLoaderPA() {
        return new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        };
    }

    /**
     * Get the context class loader.
     *
     * @return the context class loader, otherwise {@code null} if not set.
     */
    private static ClassLoader getContextClassLoader() {
        return AccessController.doPrivileged(getContextClassLoaderPA());
    }

    /**
     * Get privileged action to set the actual context class loader.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param classLoader context class loader to be set.
     * @return privileged action to set context class loader.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction setContextClassLoaderPA(final ClassLoader classLoader) {
        return new PrivilegedAction() {
            @Override
            public Object run() {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;
            }
        };
    }

    /**
     * Get privileged action to set a method to be accessible.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param m method to be set as accessible.
     * @return privileged action to set the method to be accessible.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction setAccessibleMethodPA(final Method m) {
        if (Modifier.isPublic(m.getModifiers())) {
            return NoOpPrivilegedACTION;
        }

        return new PrivilegedAction<Object>() {

            @Override
            public Object run() {
                if (!m.isAccessible()) {
                    m.setAccessible(true);
                }
                return m;
            }
        };
    }


    /**
     * Check if the given type is a primitive type.
     *
     * @param type type to be checked.
     */
    public static boolean isPrimitive(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return c.isPrimitive();
        }
        return false;
    }

    /**
     * Get the type arguments for a parameterized type.
     * <p/>
     * In case the type is not a {@link ParameterizedType parameterized type},
     * the method returns {@code null}.
     *
     * @param type parameterized type.
     * @return type arguments for a parameterized type, or {@code null} in case the input type is
     *         not a parameterized type.
     */
    public static Type[] getTypeArguments(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }

        return ((ParameterizedType) type).getActualTypeArguments();
    }

    /**
     * Get a type argument at particular index for a parameterized type.
     * <p/>
     * In case the type is not a {@link ParameterizedType parameterized type},
     * the method returns {@code null}.
     *
     * @param type  parameterized type.
     * @param index type parameter index.
     * @return type argument for a parameterized type at a given index, or {@code null}
     *         in case the input type is not a parameterized type.
     */
    public static Type getTypeArgument(Type type, int index) {
        if (type instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType) type;
            return fix(p.getActualTypeArguments()[index]);
        }

        return null;
    }

    /**
     * JDK 5.0 has a bug of creating {@link GenericArrayType} where it shouldn't.
     * fix that manually to work around the problem.
     * <p/>
     * See bug 6202725.
     */
    private static Type fix(Type t) {
        if (!(t instanceof GenericArrayType))
            return t;

        GenericArrayType gat = (GenericArrayType) t;
        if (gat.getGenericComponentType() instanceof Class) {
            Class c = (Class) gat.getGenericComponentType();
            return Array.newInstance(c, 0).getClass();
        }

        return t;
    }

    /**
     * Checks if the type is an array type.
     *
     * @param type type to check.
     * @return {@code true} in case the type is an array type, {@code false} otherwise.
     */
    public static boolean isArray(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return c.isArray();
        }
        return type instanceof GenericArrayType;
    }

    /**
     * Checks if the type is an array of a given component type.
     *
     * @param type          type to check.
     * @param componentType array component type.
     * @return {@code true} in case the type is an array type of a given component type,
     *         {@code false} otherwise.
     */
    public static boolean isArrayOfType(Type type, Class<?> componentType) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return c.isArray() && c != byte[].class;
        }
        if (type instanceof GenericArrayType) {
            final Type arrayComponentType = ((GenericArrayType) type).getGenericComponentType();
            return arrayComponentType == componentType;
        }
        return false;
    }

    /**
     * Gets the component type of the array.
     *
     * @param type must be an array.
     * @return array component type.
     * @throws IllegalArgumentException in case the type is not an array type.
     */
    public static Type getArrayComponentType(Type type) {
        if (type instanceof Class) {
            Class c = (Class) type;
            return c.getComponentType();
        }
        if (type instanceof GenericArrayType)
            return ((GenericArrayType) type).getGenericComponentType();

        throw new IllegalArgumentException();
    }

    /**
     * Get Array class of component type.
     *
     * @param c the component class of the array
     * @return the array class.
     */
    public static Class<?> getArrayForComponentType(Class<?> c) {
        try {
            Object o = Array.newInstance(c, 0);
            return o.getClass();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Get privileged action to obtain the static valueOf(String ) method.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param c The class to obtain the method.
     * @return privileged action to get the method.
     *         The action could return {@code null} if the method is not present.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    @SuppressWarnings("unchecked")
    public static PrivilegedAction<Method> getValueOfStringMethodPA(final Class<?> c) {
        return new PrivilegedAction<Method>() {
            @Override
            public Method run() {
                try {
                    final Method m = c.getDeclaredMethod("valueOf", String.class);
                    if (!Modifier.isStatic(m.getModifiers()) && m.getReturnType() == c) {
                        return null;
                    }
                    return m;
                } catch (NoSuchMethodException nsme) {
                    return null;
                }
            }
        };
    }

    /**
     * Get privileged action to get the static fromString(String ) method.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param c class for which to get the method.
     * @return privileged action to obtain the method.
     *         The action could return {@code null} if the method is not present.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    @SuppressWarnings("unchecked")
    public static PrivilegedAction<Method> getFromStringStringMethodPA(final Class<?> c) {
        return new PrivilegedAction<Method>() {
            @Override
            public Method run() {
                try {
                    final Method m = c.getDeclaredMethod("fromString", String.class);
                    if (!Modifier.isStatic(m.getModifiers()) && m.getReturnType() == c) {
                        return null;
                    }
                    return m;
                } catch (NoSuchMethodException nsme) {
                    return null;
                }
            }
        };
    }

    /**
     * Get privileged action to obtain constructor that has a single parameter of String.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     *
     * @param c The class for which to obtain the constructor.
     * @return privileged action to obtain the constructor.
     *         The action could return {@code null} if the constructor is not present.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<Constructor> getStringConstructorPA(final Class<?> c) {
        return new PrivilegedAction<Constructor>() {
            @Override
            public Constructor run() {
                try {
                    return c.getConstructor(String.class);
                } catch (SecurityException e) {
                    throw e;
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }

    /**
     * Returns collection of all annotation types attached to a given annotated element that have the provided meta
     * annotation attached.
     *
     * @param annotatedElement annotated element.
     * @param metaAnnotation   meta annotation attached to the annotation types we are looking for (if null, annotation
     *                         types of all attached annotations will be returned).
     * @return list of annotation types with a given meta annotation
     */
    public static Collection<Class<? extends Annotation>> getAnnotationTypes(
            AnnotatedElement annotatedElement,
            Class<? extends Annotation> metaAnnotation
    ) {
        Set<Class<? extends Annotation>> result = Sets.newIdentityHashSet();
        for (Annotation a : annotatedElement.getAnnotations()) {
            Class<? extends Annotation> aType = a.annotationType();
            if (metaAnnotation == null || aType.getAnnotation(metaAnnotation) != null) {
                result.add(aType);
            }
        }
        return result;
    }

    /**
     * Determine whether a given method is {@code getter}.
     *
     * @param method method to be examined.
     * @return {@code true} if the method is {@code getter}, {@code false} otherwise.
     */
    public static boolean isGetter(final Method method) {
        if (method.getParameterTypes().length == 0
                && Modifier.isPublic(method.getModifiers())) {
            final String methodName = method.getName();

            if (methodName.startsWith("get")) {
                return !void.class.equals(method.getReturnType());
            } else if (methodName.startsWith("is")) {
                return boolean.class.equals(method.getReturnType()) || Boolean.class.equals(method.getReturnType());
            }
        }
        return false;
    }

    /**
     * Create a {@link javax.ws.rs.core.GenericType generic type} information for a given
     * Java {@code instance}.
     * <p>
     * If the supplied instance is an instance of {@link javax.ws.rs.core.GenericEntity}, the generic type
     * information will be computed using the {@link javax.ws.rs.core.GenericEntity#getType()}
     * information. Otherwise the {@code instance.getClass()} will be used.
     * </p>
     *
     *
     * @param instance Java instance for which the {@code GenericType} description should be created.
     * @return {@code GenericType} describing the Java {@code instance}.
     */
    public static GenericType genericTypeFor(Object instance) {
        GenericType genericType;
        if (instance instanceof GenericEntity) {
            genericType = new GenericType(((GenericEntity) instance).getType());
        } else {
            genericType = (instance == null) ? null : new GenericType(instance.getClass());
        }
        return genericType;
    }

    /**
     * Determine whether a given method is {@code setter}.
     *
     * @param method method to be examined.
     * @return {@code true} if the method is {@code setter}, {@code false} otherwise.
     */
    public static boolean isSetter(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && void.class.equals(method.getReturnType())
                && method.getParameterTypes().length == 1
                && method.getName().startsWith("set");
    }

    /**
     * Determine property (field) name from given getter/setter method.
     *
     * @param method method to be examined.
     * @return property (field) name.
     */
    public static String getPropertyName(final Method method) {
        if (!isGetter(method) && !isSetter(method)) {
            throw new IllegalArgumentException("Method is neither getter nor setter.");
        }

        final String methodName = method.getName();
        final int offset = methodName.startsWith("is") ? 2 : 3;

        final char[] chars = methodName.toCharArray();
        chars[offset] = Character.toLowerCase(chars[offset]);

        return new String(chars, offset, chars.length - offset);
    }

    /**
     * A tuple consisting of a concrete class and a declaring class that declares a
     * generic interface type.
     */
    public static class DeclaringClassInterfacePair {

        /**
         * Concrete class.
         */
        public final Class<?> concreteClass;
        /**
         * Declaring class.
         */
        public final Class<?> declaringClass;
        /**
         * Generic interface type.
         */
        public final Type genericInterface;

        private DeclaringClassInterfacePair(Class<?> concreteClass, Class<?> declaringClass, Type genericInteface) {
            this.concreteClass = concreteClass;
            this.declaringClass = declaringClass;
            this.genericInterface = genericInteface;
        }
    }

    /**
     * Get the parameterized class arguments for a declaring class that
     * declares a generic interface type.
     *
     * @param p the declaring class
     * @return the parameterized class arguments, or null if the generic
     *         interface type is not a parameterized type.
     */
    public static Class[] getParameterizedClassArguments(DeclaringClassInterfacePair p) {
        if (p.genericInterface instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) p.genericInterface;
            Type[] as = pt.getActualTypeArguments();
            Class[] cas = new Class[as.length];

            for (int i = 0; i < as.length; i++) {
                Type a = as[i];
                if (a instanceof Class) {
                    cas[i] = (Class) a;
                } else if (a instanceof ParameterizedType) {
                    pt = (ParameterizedType) a;
                    cas[i] = (Class) pt.getRawType();
                } else if (a instanceof TypeVariable) {
                    final TypeVariable tv = (TypeVariable) a;
                    ClassTypePair ctp = resolveTypeVariable(p.concreteClass, p.declaringClass, tv);
                    cas[i] = (ctp != null) ? ctp.rawClass() : (Class<?>) (tv.getBounds()[0]);
                } else if (a instanceof GenericArrayType) {
                    final GenericArrayType gat = (GenericArrayType) a;
                    Type t = gat.getGenericComponentType();
                    if (t instanceof Class) {
                        cas[i] = getArrayForComponentType((Class<?>) t);
                    }
                }
            }
            return cas;
        } else {
            return null;
        }
    }

    /**
     * Get the parameterized type arguments for a declaring class that
     * declares a generic interface type.
     *
     * @param p the declaring class
     * @return the parameterized type arguments, or null if the generic
     *         interface type is not a parameterized type.
     */
    public static Type[] getParameterizedTypeArguments(DeclaringClassInterfacePair p) {
        if (p.genericInterface instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) p.genericInterface;
            Type[] as = pt.getActualTypeArguments();
            Type[] ras = new Type[as.length];

            for (int i = 0; i < as.length; i++) {
                Type a = as[i];
                if (a instanceof Class) {
                    ras[i] = a;
                } else if (a instanceof ParameterizedType) {
                    ras[i] = a;
                } else if (a instanceof TypeVariable) {
                    ClassTypePair ctp = resolveTypeVariable(p.concreteClass, p.declaringClass, (TypeVariable) a);
                    if (ctp == null) {
                        throw new IllegalArgumentException(
                                String.format("Unable to resolve generic type value of %s for an instance of %s."
                                        , p.genericInterface, p.concreteClass));
                    }
                    ras[i] = ctp.type();
                }
            }
            return ras;
        } else {
            return null;
        }
    }

    /**
     * Find the declaring class that implements or extends an interface.
     *
     * @param concrete the concrete class than directly or indirectly
     *                 implements or extends an interface class.
     * @param iface    the interface class.
     * @return the tuple of the declaring class and the generic interface
     *         type.
     */
    public static DeclaringClassInterfacePair getClass(Class<?> concrete, Class<?> iface) {
        return getClass(concrete, iface, concrete);
    }

    private static DeclaringClassInterfacePair getClass(Class<?> concrete, Class<?> iface, Class<?> c) {
        Type[] gis = c.getGenericInterfaces();
        DeclaringClassInterfacePair p = getType(concrete, iface, c, gis);
        if (p != null) {
            return p;
        }

        c = c.getSuperclass();
        if (c == null || c == Object.class) {
            return null;
        }

        return getClass(concrete, iface, c);
    }

    private static DeclaringClassInterfacePair getType(Class<?> concrete, Class<?> iface, Class<?> c, Type[] ts) {
        for (Type t : ts) {
            DeclaringClassInterfacePair p = getType(concrete, iface, c, t);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    private static DeclaringClassInterfacePair getType(Class<?> concrete, Class<?> iface, Class<?> c, Type t) {
        if (t instanceof Class) {
            if (t == iface) {
                return new DeclaringClassInterfacePair(concrete, c, t);
            } else {
                return getClass(concrete, iface, (Class<?>) t);
            }
        } else if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            if (pt.getRawType() == iface) {
                return new DeclaringClassInterfacePair(concrete, c, t);
            } else {
                return getClass(concrete, iface, (Class<?>) pt.getRawType());
            }
        }
        return null;
    }

    /**
     * Resolve generic type parameter(s) of a raw class and it's generic type
     * based on the class that declares the generic type parameter(s) to be resolved
     * and a concrete implementation of the declaring class.
     *
     * @param concreteClass       concrete implementation of the declaring class.
     * @param declaringClass      class declaring the generic type parameter(s) to be
     *                            resolved.
     * @param rawResolvedType     raw class of the generic type to be resolved.
     * @param genericResolvedType generic type information of th type to be resolved.
     * @return a pair of class and the generic type values with the the resolved
     *         generic parameter types.
     */
    public static ClassTypePair resolveGenericType(final Class concreteClass, final Class declaringClass,
                                                   final Class rawResolvedType, final Type genericResolvedType) {
        if (genericResolvedType instanceof TypeVariable) {
            ClassTypePair ct = resolveTypeVariable(
                    concreteClass,
                    declaringClass,
                    (TypeVariable) genericResolvedType);

            if (ct != null) {
                return ct;
            }
        } else if (genericResolvedType instanceof ParameterizedType) {
            final ParameterizedType pt = (ParameterizedType) genericResolvedType;
            final Type[] ptts = pt.getActualTypeArguments();
            boolean modified = false;
            for (int i = 0; i < ptts.length; i++) {
                ClassTypePair ct =
                        resolveGenericType(concreteClass, declaringClass, (Class) pt.getRawType(), ptts[i]);
                if (ct.type() != ptts[i]) {
                    ptts[i] = ct.type();
                    modified = true;
                }
            }
            if (modified) {
                ParameterizedType rpt = new ParameterizedType() {

                    @Override
                    public Type[] getActualTypeArguments() {
                        return ptts.clone();
                    }

                    @Override
                    public Type getRawType() {
                        return pt.getRawType();
                    }

                    @Override
                    public Type getOwnerType() {
                        return pt.getOwnerType();
                    }
                };
                return ClassTypePair.of((Class<?>) pt.getRawType(), rpt);
            }
        } else if (genericResolvedType instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) genericResolvedType;
            final ClassTypePair ct =
                    resolveGenericType(concreteClass, declaringClass, null, gat.getGenericComponentType());
            if (gat.getGenericComponentType() != ct.type()) {
                try {
                    Class ac = ReflectionHelper.getArrayForComponentType(ct.rawClass());
                    return ClassTypePair.of(ac);
                } catch (Exception e) {
                    LOGGER.log(Level.FINEST, "", e);
                }
            }
        }

        return ClassTypePair.of(rawResolvedType, genericResolvedType);
    }

    /**
     * Given a type variable resolve the Java class of that variable.
     *
     * @param c  the concrete class from which all type variables are resolved.
     * @param dc the declaring class where the type variable was defined.
     * @param tv the type variable.
     * @return the resolved Java class and type, otherwise null if the type variable
     *         could not be resolved.
     */
    public static ClassTypePair resolveTypeVariable(Class<?> c, Class<?> dc, TypeVariable tv) {
        return resolveTypeVariable(c, dc, tv, new HashMap<TypeVariable, Type>());
    }

    private static ClassTypePair resolveTypeVariable(Class<?> c, Class<?> dc, TypeVariable tv,
                                                     Map<TypeVariable, Type> map) {
        Type[] gis = c.getGenericInterfaces();
        for (Type gi : gis) {
            if (gi instanceof ParameterizedType) {
                // process pt of interface
                ParameterizedType pt = (ParameterizedType) gi;
                ClassTypePair ctp = resolveTypeVariable(pt, (Class<?>) pt.getRawType(), dc, tv, map);
                if (ctp != null) {
                    return ctp;
                }
            }
        }

        Type gsc = c.getGenericSuperclass();
        if (gsc instanceof ParameterizedType) {
            // process pt of class
            ParameterizedType pt = (ParameterizedType) gsc;
            return resolveTypeVariable(pt, c.getSuperclass(), dc, tv, map);
        } else if (gsc instanceof Class) {
            return resolveTypeVariable(c.getSuperclass(), dc, tv, map);
        }
        return null;
    }

    private static ClassTypePair resolveTypeVariable(ParameterizedType pt, Class<?> c, Class<?> dc, TypeVariable tv,
                                                     Map<TypeVariable, Type> map) {
        Type[] typeArguments = pt.getActualTypeArguments();

        TypeVariable[] typeParameters = c.getTypeParameters();

        Map<TypeVariable, Type> subMap = new HashMap<TypeVariable, Type>();
        for (int i = 0; i < typeArguments.length; i++) {
            // Substitute a type variable with the Java class
            final Type typeArgument = typeArguments[i];
            if (typeArgument instanceof TypeVariable) {
                Type t = map.get(typeArgument);
                subMap.put(typeParameters[i], t);
            } else {
                subMap.put(typeParameters[i], typeArgument);
            }
        }

        if (c == dc) {
            Type t = subMap.get(tv);
            if (t instanceof Class) {
                return ClassTypePair.of((Class) t);
            } else if (t instanceof GenericArrayType) {
                GenericArrayType gat = (GenericArrayType) t;
                t = gat.getGenericComponentType();
                if (t instanceof Class) {
                    c = (Class<?>) t;
                    try {
                        return ClassTypePair.of(getArrayForComponentType(c));
                    } catch (Exception ignored) {
                        // ignored
                    }
                    return null;
                } else if (t instanceof ParameterizedType) {
                    Type rt = ((ParameterizedType) t).getRawType();
                    if (rt instanceof Class) {
                        c = (Class<?>) rt;
                    } else {
                        return null;
                    }
                    try {
                        return ClassTypePair.of(getArrayForComponentType(c), gat);
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    return null;
                }
            } else if (t instanceof ParameterizedType) {
                pt = (ParameterizedType) t;
                if (pt.getRawType() instanceof Class) {
                    return ClassTypePair.of((Class<?>) pt.getRawType(), pt);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return resolveTypeVariable(c, dc, tv, subMap);
        }
    }

    /**
     * Get privileged action to find a method on a class given an existing method.
     * If run using security manager, the returned privileged action
     * must be invoked within a doPrivileged block.
     * <p/>
     * If there exists a public method on the class that has the same name
     * and parameters as the existing method then that public method is
     * returned from the action.
     * <p/>
     * Otherwise, if there exists a public method on the class that has
     * the same name and the same number of parameters as the existing method,
     * and each generic parameter type, in order, of the public method is equal
     * to the generic parameter type, in the same order, of the existing method
     * or is an instance of {@link TypeVariable} then that public method is
     * returned from the action.
     *
     * @param c the class to search for a public method
     * @param m the method to find
     * @return privileged action to return public method found.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<Method> findMethodOnClassPA(final Class<?> c, final Method m) {
            return new PrivilegedAction<Method>() {
                @Override
                public Method run() {
                    try {
                        return c.getMethod(m.getName(), m.getParameterTypes());
                    } catch (NoSuchMethodException nsme) {
                            for (final Method _m : c.getMethods()) {
                                if (_m.getName().equals(m.getName())
                                        && _m.getParameterTypes().length == m.getParameterTypes().length) {
                                    if (compareParameterTypes(m.getGenericParameterTypes(),
                                            _m.getGenericParameterTypes())) {
                                        return _m;
                                    }
                            }
                        }
                        return null;
                    }
                }
            };
    }

    /**
     * Get privileged action to return an array containing {@link Method} objects reflecting all
     * the public <em>member</em> methods of the supplied class or interface
     * object, including those declared by the class or interface and those
     * inherited from superclasses and superinterfaces.
     *
     * Array classes return all the (public) member methods
     * inherited from the {@code Object} class.  The elements in the array
     * returned are not sorted and are not in any particular order.  This
     * method returns action providing an array of length 0 if this {@code Class} object
     * represents a class or interface that has no public member methods, or if
     * this {@code Class} object represents a primitive type or void.
     *
     * <p>
     * The class initialization method {@code <clinit>} is not
     * included in the returned array. If the class declares multiple public
     * member methods with the same parameter types, they are all included in
     * the returned array.
     * </p>
     * <p>
     * See <em>The Java Language Specification</em>, sections 8.2 and 8.4.
     * </p>
     *
     * @param c class for which the methods should be returned.
     * @return privileged action to obtain an array of {@code Method} objects representing the
     *         public methods of the class.
     *
     * @see AccessController#doPrivileged(java.security.PrivilegedAction)
     */
    public static PrivilegedAction<Method[]> getMethodsPA(final Class<?> c) {
        return new PrivilegedAction<Method[]>() {
            @Override
            public Method[] run() {
                return c.getMethods();
            }
        };
    }

    private static Method[] _getMethods(Class<?> clazz) {
        return AccessController.doPrivileged(getMethodsPA(clazz));
    }

    /**
     * Find a {@link Method method} that overrides the given {@code method} on the given {@link Class class}.
     *
     * @param clazz  class to find overriding method on.
     * @param method an abstract method to find implementing method for.
     * @return method that overrides the given method or the given method itself if a better alternative cannot be found.
     */
    public static Method findOverridingMethodOnClass(final Class<?> clazz, final Method method) {
        for (final Method _method : _getMethods(clazz)) {
            if (!_method.isBridge()
                    && !Modifier.isAbstract(_method.getModifiers())
                    && _method.getName().equals(method.getName())
                    && _method.getParameterTypes().length == method.getParameterTypes().length) {

                if (compareParameterTypes(_method.getGenericParameterTypes(), method.getGenericParameterTypes())) {
                    return _method;
                }
            }
        }

        if (method.isBridge() || Modifier.isAbstract(method.getModifiers())) {
            LOGGER.log(
                    Level.INFO,
                    String.format("overriding.method.cannot.be.foundMethod that overrides {0} cannot be found on class {1}."
                            , method, clazz));
        }

        return method;
    }

    /**
     * Compare generic parameter types of two methods.
     *
     * @param ts  generic parameter types of the first method.
     * @param _ts generic parameter types of the second method.
     * @return {@code true} if the given types are understood to be equal, {@code false} otherwise.
     * @see #compareParameterTypes(java.lang.reflect.Type, java.lang.reflect.Type)
     */
    private static boolean compareParameterTypes(Type[] ts, Type[] _ts) {
        for (int i = 0; i < ts.length; i++) {
            if (!ts[i].equals(_ts[i])) {
                if (!compareParameterTypes(ts[i], _ts[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Compare respective generic parameter types of two methods.
     *
     * @param ts  generic parameter type of the first method.
     * @param _ts generic parameter type of the second method.
     * @return {@code true} if the given types are understood to be equal, {@code false} otherwise.
     */
    @SuppressWarnings("unchecked")
    private static boolean compareParameterTypes(final Type ts, final Type _ts) {
        if (ts instanceof Class) {
            final Class<?> clazz = (Class<?>) ts;

            if (_ts instanceof Class) {
                return ((Class) _ts).isAssignableFrom(clazz);
            } else if (_ts instanceof TypeVariable) {
                return checkTypeBounds(clazz, ((TypeVariable) _ts).getBounds());
            }
        }
        return _ts instanceof TypeVariable;
    }

    @SuppressWarnings("unchecked")
    private static boolean checkTypeBounds(final Class type, final Type[] bounds) {
        for (final Type bound : bounds) {
            if (bound instanceof Class) {
                if (!((Class) bound).isAssignableFrom(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    private final static Class<?> bundleReferenceClass = AccessController.doPrivileged(classForNamePA("org.osgi.framework.BundleReference", null));

    /**
     * Prevents instantiation.
     */
    private ReflectionHelper() {
    }
}