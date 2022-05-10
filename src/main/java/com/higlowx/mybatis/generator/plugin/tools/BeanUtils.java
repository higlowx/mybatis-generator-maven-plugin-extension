/*
 * Copyright 2022 Higlowx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.higlowx.mybatis.generator.plugin.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * BeanUtils
 *
 * @author Dylan.Li
 */
public class BeanUtils {

    /**
     * Set property
     *
     * @param bean  bean
     * @param name  name
     * @param value value
     */
    public static void setProperty(final Object bean, final String name, final Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(bean, value);
    }

    /**
     * Get property
     *
     * @param bean bean
     * @param name name
     * @return property
     */
    public static Object getProperty(final Object bean, final String name) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = bean.getClass();
        Field field = null;

        do {
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        } while (clazz != null && field == null);

        if (field == null) {
            throw new NoSuchFieldException(name);
        }

        field.setAccessible(true);

        return field.get(bean);
    }

    /**
     * 执行无参方法
     *
     * @param bean  bean
     * @param clazz clazz
     * @param name  name
     * @return invoke result
     */
    public static <T> Object invoke(final Object bean, Class<T> clazz, final String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(name);
        method.setAccessible(true);
        return method.invoke(bean);
    }
}
