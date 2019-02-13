package study.hank.com.plugin_lib;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * 代理Activity
 * 作用：接收来自宿主的跳转意图，并且拿到其中的参数
 * 这里只能继承Activity，而不是AppCompatActivity，否则会报“空指针”
 * 原因是，AppCompatActivity会调用上下文，你问为啥？不知道啊，问谷歌大佬
 */
public class ProxyActivity extends Activity {

    private String realActivityName;//既然我只是个代理，那么自然有真正的Activity

    private IPlugin iPlugin;

    // ````````由ProxyActivity代为管理真正Activity的生命周期```````````````````````````````
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realActivityName = getIntent().getStringExtra(PluginApkConst.TAG_CLASS_NAME);//宿主，将真正的跳转意图，放在了这个参数className中，
        //拿到realActivityName，接下来的工作，自然就是展示出真正的Activity
        try {// 原则，反射创建RealActivity对象，但是，去拿这个它的class，只能用dexClassLoader
            Class<?> realActivityClz = PluginManager.getInstance().getDexClassLoader().loadClass(realActivityName);
            Object obj = realActivityClz.newInstance();
            if (obj instanceof IPlugin) {//所有的插件Activity，都必须是IPlugin的实现类
                iPlugin = (IPlugin) obj;
                Bundle bd = new Bundle();
                bd.putInt(PluginApkConst.TAG_FROM, IPlugin.FROM_EXTERNAL);
                iPlugin.attach(this);
                iPlugin.onCreate(bd);//反射创建的插件Activity的生命周期函数不会被执行，那么，就由ProxyActivity代为执行
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        iPlugin.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        iPlugin.onResume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        iPlugin.onRestart();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        iPlugin.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        iPlugin.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        iPlugin.onDestroy();
        super.onDestroy();
    }

    //然后，下面2个方法必须重写,因为插件中使用的是外部的类，和资源，所以必须用对应的DexClassLoader，
    @Override
    public ClassLoader getClassLoader() {
        ClassLoader classLoader = PluginManager.getInstance().getDexClassLoader();
        return classLoader != null ? classLoader : super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        Resources resources = PluginManager.getInstance().getResources();
        return resources != null ? resources : super.getResources();
    }
}
