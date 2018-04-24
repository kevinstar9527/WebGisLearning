package kevinstar1.edu.cn.agentweb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

public class CesiumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llContainer = findViewById(R.id.container);
       AgentWeb mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llContainer,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb().go("http://172.27.35.1:8088/");
        //mAgentWeb.getJsInterfaceHolder().addJavaObject("android",this);
    }
}
