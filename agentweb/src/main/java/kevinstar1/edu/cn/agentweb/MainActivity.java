package kevinstar1.edu.cn.agentweb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        final Button btnJsBridge = findViewById(R.id.btn_jsbridge);
        btnJsBridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "敬请期待", Toast.LENGTH_SHORT).show();

            }
        });
        Button btnCesium = findViewById(R.id.btn_three_model);
        btnCesium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CesiumActivity.class);
                startActivity(intent);
            }
        });

        Button btnBabylon = findViewById(R.id.btn_babylon);
        btnBabylon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BabylonActivity.class);
                startActivity(intent);
            }
        });

        Button btnThreeJS = findViewById(R.id.btn_three);
        btnThreeJS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ThreeJSActivity.class);
                startActivity(intent);
            }
        });
    }
}
