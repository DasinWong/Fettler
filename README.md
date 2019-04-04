# Fettler
Android 热修复框架 （基于类加载机制的代码修复）
- 支持 Android 5.0 以上设备
- 运行时修复，应用无需重启
- 版本更新时要注意dex修复包的清理
## 如何接入
Project层级下的build.gradle文件
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Module层级下的build.gradle文件
```
dependencies {
    ...
    implementation 'com.github.dasinwong:Fettler:1.0'
}
```
## 类及其方法介绍
### Fettler
热修复核心类

| 方法 | 描述 |
| :-------------: | :-------------: |
| init | 初始化，程序启动时调用 |
| with | 创建Fettler对象的静态方法 |
| add | 添加dex补丁文件（可连续添加多个） |
| listen | 添加修复监听（选用） |
| start | 开始修复 |
| clear | 清理dex补丁文件（版本更新时） |
### FixListener
修复监听接口

| 方法 | 描述 |
| :-------------: | :-------------: |
| onComplete | 修复完成时回调 |
## 使用方法
#### 1.程序启动时进行初始化
```
public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //初始化Fettler
        Fettler.init(context);
    }
}
```
#### 2.执行修复
```
//获取补丁包文件（正常情况下应该是通过网络下载到本地）
File dexFile = new File(Environment.getExternalStorageDirectory(), "XXX.dex");

//添加dex文件和监听，开始修复
Fettler.with(this).add(dexFile).listen(new FixListener() {
    @Override
        public void onComplete() {
        Toast.makeText(XXX.this, "修复完成", Toast.LENGTH_SHORT).show();
    }
}).start();
```
#### 3.清理修复包
在版本更新时清理
```
//直接调用静态方法清理
Fettler.clear(this);

//创建Fettler对象清理
Fettler.with(this).clear();
```
## 需要权限
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```
