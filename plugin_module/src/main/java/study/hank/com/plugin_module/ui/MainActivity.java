package study.hank.com.plugin_module.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import study.hank.com.plugin_lib.PluginBaseActivity;
import study.hank.com.plugin_module.R;

/**
 * 插件的第一个Activity
 */
public class MainActivity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(proxy, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
