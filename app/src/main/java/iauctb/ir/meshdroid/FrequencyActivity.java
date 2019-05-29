package iauctb.ir.meshdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FrequencyActivity extends AppCompatActivity {

    EditText frequencyET;
    TextView startBtn,startDefaultBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency);

        frequencyET = findViewById(R.id.activity_frequency__frequency_et);
        startBtn = findViewById(R.id.activity_frequency__start_btn);
        startDefaultBtn = findViewById(R.id.activity_frequency__start_default_btn);





        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frequencyET.getText()!=null && frequencyET.getText().length()>0){
                    ((MeshDroid)getApplication()).node.reInit(Integer.valueOf(frequencyET.getText().toString()));
                    startActivity(new Intent(FrequencyActivity.this,SearchingActivity.class));
                }
            }
        });

        startDefaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MeshDroid)getApplication()).node.reInit(226600);
                startActivity(new Intent(FrequencyActivity.this,SearchingActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MeshDroid)getApplication()).node.stop();
    }
}
