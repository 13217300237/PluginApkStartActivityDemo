package study.hank.com.plugin_lib.read;

/**
 * 必读
 */
public class ReadMe {

    /**
     * 此module，是插件化开发的框架层代码
     *
     * 负责 整合所有插件相关的类（含抽象类），接口 .
     *
     *  现在来写思路：
     *
     *  技术基础：一个Activity的启动，需要完成3个步骤：
     *  1.类的加载
     *  2.资源文件的加载
     *  3.生命周期的管理
     *  所以在宿主启动插件Activity时，
     *  插件Activity内部的 类，资源 必须能够被宿主所使用，它的生命周期也必须交由宿主的代理Activity类去管理
     *  类，和资源，分别由 DexClassLoader和Resources，AssetManager来负责.
     *  生命周期，则是由ProxyActivity extends AppCompatActivity来代为管理.
     *
     *  所以，plugin_lib 中，至少应该有3个类:
     *
     *  1.ProxyActivity.java
     *      plugin_lib被宿主module引用之后，宿主可以直接通过跳转到ProxyActivity的方式来跳转到真正的插件Activity
     *      （ProxyActivity只能继承android.app.Activity ,而不能继承 android.support.v7.app.AppCompatActivity
     *      这是由于AppCompatActivity会调用context上下文，而导致空指针，更细节的原因，暂不明）

     *  2.PluginManager.java
     *      插件本身是一个apk文件，那么怎么去使用到这个apk呢？使用的逻辑全部集中在这里。
     *      包括：
     *      1）演示demo比较方便的方式，是将apk文件放在宿主的assets目录内，
     *      然后用文件流，将它写到app的缓存目录内(data/data/包名/自定义目录名)，并且取得插件apk文件的目录
     *      2）宿主要使用插件内的classes类，res资源，
     *         则需要用到DexClassLoader，Resources，AssetManager,PackageInfo,PackageManager这些类.
     *         ~ DexClassLoader的作用，是读取外部的dex文件，并且可以从中拿到想要的类，再通过反射来构建类.
     *         ~ Resources,AssetManager 是app的资源大管家，同样，可以通过hook AssetManager的addAssetPath方法，来创建只属于插件资源的Resources管家.
     *         ~ PackageInfo,PackageManager 是包的信息管理，使用他们，可以拿到插件apk内部的所有 Activity.
     *       3)另外，考虑到插件中可能不只有一个Activity，所以，创建一个共同父类让他们来继承，可以少些很多重复代码.
     *          那就再多一个 PluginBaseActivity.java (它可以继承AppCompatActivity)
     *
     *
     */
}
