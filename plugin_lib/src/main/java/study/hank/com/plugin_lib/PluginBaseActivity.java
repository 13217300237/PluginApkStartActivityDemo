package study.hank.com.plugin_lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * 插件Activity的基类，插件中的所有Activity，都要继承它
 */
public abstract class PluginBaseActivity extends AppCompatActivity implements IPlugin {

    private final String TAG = "PluginBaseActivityTag";
    protected Activity proxy;//上下文

    //这里基本上都在重写原本Activity的函数，因为 要兼容“插件单独测试” 和 "集成到宿主整体测试",所以要进行情况区分
    private int from = IPlugin.FROM_INTERNAL;//默认是“插件单独测试”

    @Override
    public void attach(Activity proxyActivity) {
        proxy = proxyActivity;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        if (saveInstanceState != null)
            from = saveInstanceState.getInt(PluginApkConst.TAG_FROM);

        if (from == IPlugin.FROM_INTERNAL) {
            super.onCreate(saveInstanceState);
            proxy = this;//如果是从内部跳转，那就将上下文定为自己
        }
    }

    @Override
    public void onStart() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onStart();
        } else {
            Log.d(TAG, "宿主启动：onStart()");
        }
    }

    @Override
    public void onResume() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onResume();
        } else {
            Log.d(TAG, "宿主启动：onResume()");
        }
    }

    @Override
    public void onRestart() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onRestart();
        } else {
            Log.d(TAG, "宿主启动：onRestart()");
        }
    }

    @Override
    public void onPause() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onPause();
        } else {
            Log.d(TAG, "宿主启动：onPause()");
        }
    }

    @Override
    public void onStop() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onStop();
        } else {
            Log.d(TAG, "宿主启动：onStop()");
        }
    }

    @Override
    public void onDestroy() {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onDestroy();
        } else {
            Log.d(TAG, "宿主启动：onDestroy()");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (from == IPlugin.FROM_INTERNAL) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "宿主启动：onActivityResult()");
        }
    }

    //下面是几个生命周期之外的重写函数
    @Override
    public void setContentView(int layoutResID) {//设置contentView分情况
        if (from == IPlugin.FROM_INTERNAL) {
            super.setContentView(layoutResID);
        } else {
            proxy.setContentView(layoutResID);
        }
    }

    @Override
    public View findViewById(int id) {
        if (from == FROM_INTERNAL) {
            return super.findViewById(id);
        } else {
            return proxy.findViewById(id);
        }
    }

    @Override
    public void startActivity(Intent intent) {//同理
        if (from == IPlugin.FROM_INTERNAL) {
            super.startActivity(intent);//原intent只能用于插件单独运行时
        } else {
            // 如果是集成模式下，插件内的跳转，控制权 仍然是在宿主上下文里面，所以--!
            // 先跳到代理Activity，由代理Activity展示真正的Activity内容
            PluginManager.getInstance().gotoActivity(proxy, intent.getComponent().getClassName());
        }
    }

}
