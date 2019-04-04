package com.dasinwong.fettler;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashSet;

import dalvik.system.DexClassLoader;

public class Fettler {

    private static final String TAG = "Fettler";
    private HashSet<File> fixDexSet;
    private Context context;
    private FixListener listener;

    private Fettler(Context context) {
        fixDexSet = new HashSet<>();
        this.context = context;
    }

    /**
     * 构造Fettler对象，初始化成员变量
     */
    public static Fettler with(Context context) {
        return new Fettler(context);
    }

    /**
     * 初始化，在application的attachBaseContext()调用
     */
    public static void init(Context context) {
        with(context).start();
    }

    /**
     * 清理磁盘缓存的dex文件（慎用，会导致需要修复的dex文件失效）
     */
    public static void clear(Context context) {
        with(context).clear();
    }

    /**
     * 添加补丁包
     */
    public Fettler add(String dexPath) {
        File dexFile = new File(dexPath);
        File targetFile = new File(context.getDir(Constants.TEMP_FOLDER, Context.MODE_PRIVATE) + File.separator + dexFile.getName());
        if (targetFile.exists()) targetFile.delete();
        FileUtils.copy(dexFile, targetFile);
        Log.i(TAG, "===== 成功添加 " + targetFile.getName() + " =====");
        return this;
    }

    /**
     * 添加补丁包
     */
    public Fettler add(File dexFile) {
        File targetFile = new File(context.getDir(Constants.TEMP_FOLDER, Context.MODE_PRIVATE) + File.separator + dexFile.getName());
        if (targetFile.exists()) targetFile.delete();
        FileUtils.copy(dexFile, targetFile);
        Log.i(TAG, "===== 成功添加 " + targetFile.getName() + " =====");
        return this;
    }

    /**
     * 添加监听
     */
    public Fettler listen(FixListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 热修复
     */
    public void start() {
        Log.i(TAG, "===== 开始修复 =====");
        fixDexSet.clear(); //清理集合
        File dexDir = context.getDir(Constants.TEMP_FOLDER, Context.MODE_PRIVATE);
        //遍历所有dex文件添加到集合
        for (File dex : dexDir.listFiles()) {
            String fileName = dex.getName();
            //非主dex文件
            if (fileName.endsWith(Constants.DEX_SUFFIX) && !fileName.equals(Constants.MAIN_DEX_NAME))
                fixDexSet.add(dex);
        }
        //开始插桩修复
        createDexClassLoader(dexDir);
    }

    /**
     * 创建自有类加载器生成dexElements对象
     */
    private void createDexClassLoader(File dexDir) {
        //创建临时解压目录
        File tempDir = new File(dexDir.getAbsolutePath() + File.separator + Constants.TEMP_UNZIP_FOLDER);
        if (!tempDir.exists()) tempDir.mkdirs();
        //遍历dex集合进行插桩修复
        for (File dex : fixDexSet) {
            Log.i(TAG, "===== 正在修复 " + dex.getName() + " =====");
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), tempDir.getAbsolutePath(), null, context.getClassLoader());
            hotFix(classLoader);
        }
        if (listener != null) listener.onComplete();
        Log.i(TAG, "===== 修复完成 =====");
    }

    /**
     * 插桩修复
     */
    private void hotFix(DexClassLoader loader) {
        //获取自有类加载器中的dexElements对象
        Object patchElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(loader));
        //获取系统类加载器中的dexElements对象
        Object oldElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(context.getClassLoader()));
        //合并dexElements数组
        Object newElements = ArrayUtils.merge(patchElements, oldElements);
        //获取系统类加载器中的pathList对象
        Object pathList = ReflectUtils.getPathList(context.getClassLoader());
        //将合并后的数组赋值给系统的类加载器pathList对象的dexElements属性
        ReflectUtils.setField(pathList, pathList.getClass(), Constants.DEX_ELEMENTS, newElements);
    }

    /**
     * 清理磁盘缓存的dex文件（慎用，会导致需要修复的dex文件失效）
     */
    public void clear() {
        fixDexSet.clear();
        String dexDir = context.getDir(Constants.TEMP_FOLDER, Context.MODE_PRIVATE).getAbsolutePath();
        File tempFile = new File(dexDir + File.separator + Constants.TEMP_UNZIP_FOLDER);
        for (File dex : tempFile.listFiles()) {
            if (dex.exists()) dex.delete();
        }
        File dexFile = new File(dexDir);
        for (File dex : dexFile.listFiles()) {
            if (dex.exists()) dex.delete();
        }
        Log.i(TAG, "===== 清理完成 =====");
    }
}
