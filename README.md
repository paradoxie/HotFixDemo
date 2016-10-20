# HotFixDemo
keywords: 热修复  RocooFix实践

## 相关配置

### 1.项目的build.gradle：

	dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
        classpath 'com.dodola:rocoofix:1.2.6'//添加
    }

### 2.module的build.gradle：

	apply plugin: 'com.android.application'
	apply plugin: 'com.dodola.rocoofix'//添加
	repositories {
    	flatDir {
        	dirs 'libs'
    	}
	}

**配置签名**，因为一般都是**在发布情况下**才需要热修复嘛，所以以下操作全都是针对于release版本操作。另外，注意看下面的注释部分

	signingConfigs {
        test {
            keyAlias 'shoyu'
            keyPassword 'admin22'
            storeFile file('doc/test.jks')
            storePassword 'admin22'
        }
    }

	defaultConfig {
			...
        minSdkVersion 15 //这里最低版本支持到API 15
			...
        versionCode 1 //这个值涉及到补丁版本发布
			...
    }

	buildTypes {
        release {
            minifyEnabled true // 注意：一定要开启混淆
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.test
        }
    }

    dependencies {
    	...
    	compile 'com.dodola:rocoo:1.1'
    	compile(name: 'AndroidQuery-release', ext: 'aar')
    }

    rocoo_fix {
    	includePackage = ['cf/paradoxie/hotfixdemo']
    	excludeClass = ['MyApplicaton.class', 'HotFixManger.class', 'BasePermissionActivity.class']
	//    preVersionPath = '1'//制作补丁版本时开启
	//    scanref = true //制作补丁版本事开启
   		enable = true
	}


**文件配置**

> 1.libs文件夹下：AndroidQuery-release.aar、commons-codec-1.10.jar 两个文件复制到自己的libs下，编译。
>
> 2.doc文件夹下：jks签名文件，这个自己配置都行，随便放哪，上面storeFile路径对就行。
>
> 3.在main/assets文件夹下：patch.jar、rocoo.dex，示例。MyApplicaton中有注释说明。
>
> 4.最后是java代码：主要是hotfix和util两个包下面的内容。注意导包。
> 5.MyApplicaton在manifest.xml中的

**混淆配置**

> 注意看自己的包名

好像就差不多了吧。
最终的编译效果会在app路径下生成rocoofix文件夹，如下图：

![1](http://odpd0x6u7.bkt.clouddn.com/1.png)

## 使用


**首先部署一个release版本到手机上**

点击右侧gradle projects,选择app/Tasks/install/installRelease,将会部署发布版到设备上。如图：

![2](http://odpd0x6u7.bkt.clouddn.com/2.png)

**根据demo中的设定，运行点击按钮会是下图的反应，嗯，这是一个有bug的版本，修复之前的状态**

![3](http://odpd0x6u7.bkt.clouddn.com/3.png)

**然后去修复相关bug，此Demo里就是HelloHack类下的showHello方法的修改，修改之后bug后，修改module的build.gradle相关配置，主要就三个地方**

	1.versionCode 2版本号修改
	2.rocoo_fix中，preVersionPath = '1' 取消注释，开启补丁制作
	3.取消scanref = true注释

编译后app路径下生成rocoofix下，如图：

![4](http://odpd0x6u7.bkt.clouddn.com/4.png)

**最后制作补丁包：点击右侧gradle projects,选择app/Tasks/build/assembleRelease,如图：**

![5](http://odpd0x6u7.bkt.clouddn.com/5.png)

**完事后会在rocoofix/version2/release/下生成patch.jar,如图：**

![6](http://odpd0x6u7.bkt.clouddn.com/6.png)

**最后将patch.jar放置到图3所示的目录下，当然这个目录可以自己指定，最后部署后的运行效果：**

![7](http://odpd0x6u7.bkt.clouddn.com/7.png)


> 注：须重启才能完成修复。


## 说点儿

热修复听起来不错，但是也只能作为备用，在产品上线后的无奈之举，因为谁也不知道发布后会出现什么情况，热修复就相当于补考，但是补考这种事，还是不要参与最好。另外，热修复肯定不适合大量的代码修改，因为很难预料会出现什么状况.

RocooFix框架解决了Nuwa因为Gradle1.40 里Transform API无法打包的情况，现在兼容Gradle1.3-Gradle2.1.0版本，基于QQ空间终端开发团队的技术文章实现，更多功能实现和局限性参看[RocooFix](https://github.com/dodola/RocooFix)



---

> 本文作者：paradoxie

> 个人主页：[谢盒盒的小黑屋，不止说技术](http://www.paradoxie.cf/)

> 简书地址：[简书主页，专注技术类](http://www.jianshu.com/users/05f39939cbf3/latest_articles)

> github地址：[paradoxie](https://github.com/paradoxie)

> 转载请注明出处，蟹蟹!

> -------我的梦想真的是做一条咸鱼！
