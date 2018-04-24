package kevinstar1.edu.cn.agentweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;

public class AgentWebActivity extends AppCompatActivity {

    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llContainer = findViewById(R.id.container);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llContainer,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .createAgentWeb().go("file:///android_asset/"+"openlayer.html");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",this);

        

    }
}
