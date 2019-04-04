package com.dasinwong.fettler;

import java.lang.reflect.Array;

public class ArrayUtils {

    /**
     * 数组合并
     *
     * @param patchArray 补丁包数组
     * @param oldArray   应用原数组
     */
    public static Object merge(Object patchArray, Object oldArray) {
        int patchLength = Array.getLength(patchArray);
        int oldLength = Array.getLength(oldArray);
        Object newArray = Array.newInstance(patchArray.getClass().getComponentType(), patchLength + oldLength);
        int newLength = Array.getLength(newArray);
        for (int i = 0; i < newLength; ++i) {
            if (i < patchLength) {
                Array.set(newArray, i, Array.get(patchArray, i));
            } else {
                Array.set(newArray, i, Array.get(oldArray, i - patchLength));
            }
        }
        return newArray;
    }
}
