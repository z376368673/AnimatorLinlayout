package sh.hao.com.animatorlinlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnimaLinlayout animaLinlayout = findViewById(R.id.line);
        animaLinlayout.addAnima(this);
    }
}
