package com.higlowx.mybatis.generator.plugins.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class BeanUtils {
    /**
     * 设置属性
     *
     * @param bean
     * @param name
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setProperty(final Object bean, final String name, final Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = bean.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(bean, value);
    }

    /**
     * 获取属性
     *
     * @param bean
     * @param name
     * @return
     */
    public static Object getProperty(final Object bean, final String name) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = bean.getClass();
        Field field = null;
        //当前类对象中找不到就去父类找
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
     * @param bean
     * @param clazz
     * @param name
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> Object invoke(final Object bean, Class<T> clazz, final String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getDeclaredMethod(name);
        method.setAccessible(true);
        return method.invoke(bean);
    }
}
