package com.dasinwong.fettler;

import java.lang.reflect.Field;

public class ReflectUtils {

    /**
     * 获取某个属性对象
     *
     * @param obj   该属性所属类的对象
     * @param clazz 该属性的所属类
     * @param field 属性名
     */
    private static Object getField(Object obj, Class<?> clazz, String field) {
        try {
            Field declaredField = clazz.getDeclaredField(field);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给某个属性赋值
     *
     * @param obj   该属性所属类的对象
     * @param clazz 该属性的所属类
     * @param field 属性名
     * @param value 值
     */
    public static void setField(Object obj, Class<?> clazz, String field, Object value) {
        try {
            Field declaredField = clazz.getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取BaseDexClassLoader对象中的pathList对象
     *
     * @param baseDexClassLoader baseDexClassLoader对象
     */
    public static Object getPathList(Object baseDexClassLoader) {
        try {
            return getField(baseDexClassLoader, Class.forName(Constants.BASE_DEX_CLASS_LOADER), Constants.PATH_LIST);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取PathList对象中的dexElements对象
     *
     * @param pathList pathList对象
     */
    public static Object getDexElements(Object pathList) {
        return getField(pathList, pathList.getClass(), Constants.DEX_ELEMENTS);
    }
}
