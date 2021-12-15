Pandora Java APP
========

准备工作
=============

Pandora-Java-App项目涉及到 [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 和 [NodeJs](https://nodejs.org/en/download/) ，关于`Java`和`NodeJs`的开发与编译环境要求如下: 

    - Java (>= 11)
    - NodeJs (>=12.13.0 & <13.0.0 || >=14.15.0 & <15.0.0)

你需要执行下面的命令来安装`rush`和`pnpm`进行前端的编译，关于前端的详细文档在 ([./frontend/README.md](/frontend/README.md))。

    $ sudo npm install -g @microsoft/rush@5.54.0
    $ sudo npm install -g pnpm@6.7.1

我们无需手动安装`gradle`工具包，在第一次执行`./gradlew`的命令时，它会自动引导下载`gradle`。


构建与发布
============

1. 替换app名称`{{app_name}}`，执行以下命令将会替换相关文件中的app名称为demo。

   ```bash
   $ ./gradlew changeAppName -Dapp=demo
   ```

2. 打包APP `.zip,.tar.gz`格式的安装包

   ```bash
   $ ./gradlew zip
   ```

   该`zip`命令将会在项目的`archive/build/zip`目录下生成一个`[project]-[version].zip`文件。

   如果你想要打包一个`.tar.gz`格式的安装包，只需要更换`zip`为`tar`即可，命令如下：

   ```bash
   $ ./gradlew tar
   ```

3. 代码的格式化和校验的命令如下，同时建议您安装IDEA的插件`google-java-format`：

   ```bash
   $ ./gradlew googleJavaFormat
   $ ./gradlew verifyGoogleJavaFormat
   ```


目录结构介绍
============

1. **archive：** 打包文件生成目录
2. **config：** app所需配置文件存放目录
3. **gradle：** 全局gradle任务存放目录
4. **java：** 后端代码目录
5. **Webapp：** 前端代码目录

