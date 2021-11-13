# [HDFS_web](https://github.com/dddddkio/hdfs_web)

Hadoop简单web文件管理系统，包含创建文件夹、下载、上传、删除功能

---------------------------------

**快速开始**

- 1. 本机部署hadoop环境，启动HDFS
- 2. Eclipse/IDEA导入maven工程，运行DemoApplication主类
- 3. 浏览器（建议为firefox）打开index.html

--------------

**技术栈**

Spring Boot

Maven

VUE

AXIOS

Hadoop

BootStrap

------------------------

**问题点分析**

1. 本地服务端与web端在不同端口，常出现跨域问题，需要在API类加入

   ```java
   @CrossOrigin
   ```

2. 粗心导致跨域问题，而原因在于使用POST类型访问GetMapping注解的API

   ```java
   @GetMappding("hdfs")
   ```

   ```java
   @RequestMappding("hdfs")
   ```

3. 上传文件实现思路 

   web端multipart/form-data上传图片 --> server端接收文件并转存至本地缓存文件夹  -->  本地缓存文件上传至hadoop服务端

4. 面包屑导航实现思路

   使用数组，记录当前目录的情况

5. 实时更新当前路径

   使用全局变量nowPath并进行实时更新

6. Chrome浏览器（Webkit内核）的安全策略决定了file协议访问的应用无法使用XMLHttpRequest对象。尝试了一下Firefox浏览器，没有发现此问题。

-------------

**页面预览**

![](https://github.com/dddddkio/hdfs_web/blob/main/预览图片.png)

[]: https://github.com/dddddkio/hdfs_web/blob/main/演示视频.mp4	"视频预览"

