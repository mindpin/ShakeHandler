Android ShakeHandler
============
摇一摇组件。

摇一摇之后，进行post操作。可以设置url、cookie、以及POST传递参数，并对结果进行回调。

## 如何引用此组件：
### 安装
```
git clone https://github.com/mindpin/ShakeHandler
cd ShakeHandler
mvn clean install
```

### maven引用
在maven项目，pom.xml添加以下依赖引用：

```
<dependency>
<groupId>com.mindpin.android.shakehandler</groupId>
<artifactId>shakehandler</artifactId>
<version>0.1.0-SNAPSHOT</version>
<type>apklib</type>
</dependency>
```

### android权限设置
AndroidManifest.xml 添加如下权限
```
<uses-permission android:name="android.permission.VIBRATE"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

## 使用说明
请参考示例

## 依赖库
* [seismic][1]

[1]: https://github.com/square/seismic
