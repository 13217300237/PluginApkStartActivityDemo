package study.hank.com.plugin_lib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * 插件apk的管理类
 * <p>
 * loadPluginApk(String path);
 * init(Context context);
 * getPackageInfo();
 * getDexClassLoader();
 * getResources();
 */
public class PluginManager {

    //*****应该是单例模式，因为一个宿主app只需要一个插件管理器对象即可*****
    private PluginManager() {
    }

    private volatile static PluginManager instance;//volatile 保证每一次取的instance对象都是最新的

    public static PluginManager getInstance() {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager();
                }
            }
        }
        return instance;
    }

    private Context mContext;//上下文

    private PackageInfo packageInfo;//包信息
    private DexClassLoader dexClassLoader;//类加载器
    private Resources resources;//资源包

    public void init(Context context) {
        mContext = context.getApplicationContext();//要用application 因为这是单例，直接用Activity对象作为上下文会导致内存泄漏
    }

    /**
     * 从插件apk中读出我们所需要的信息
     *
     * @param apkPath
     */
    public void loadPluginApk(String apkPath) {
        //先拿到包信息
        packageInfo = mContext.getPackageManager().getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);//只拿Activity
        if (packageInfo == null)
            throw new RuntimeException("插件加载失败");//如果apkPath是传的错的，那就拿不到包信息了，下面的代码也就不用执行

        //类加载器，DexClassLoader专门负责外部dex的类
        File outFile = mContext.getDir("odex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(apkPath, outFile.getAbsolutePath(), null, mContext.getClassLoader());

        //创建AssetManager，然后创建Resources
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.invoke(assetManager, apkPath);
            resources = new Resources(assetManager,
                    mContext.getResources().getDisplayMetrics(),
                    mContext.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //把这3个玩意公开出去
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public Resources getResources() {
        return resources;
    }

    /**
     * 既然无论是宿主启动插件的Activity，还是插件内部的跳转都要使用ProxyActivity作为代理，
     * 何不写一个公共方法以供调用呢？
     *
     * @param context
     * @param realActivityClassName
     */
    public void gotoActivity(Context context, String realActivityClassName) {
        Intent intent = new Intent(context, ProxyActivity.class);
        intent.putExtra(PluginApkConst.TAG_CLASS_NAME, realActivityClassName);
        context.startActivity(intent);
    }
}
