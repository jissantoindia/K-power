package in.acara.kpower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RegtymActivity extends AppCompatActivity {

    LinearLayout rBtn,lBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regtym);
        getSupportActionBar().hide();

        rBtn= findViewById(R.id.trbtn);
        lBtn= findViewById(R.id.tlbtn);

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegtymActivity.this, UregActivity.class);
                startActivity(i);

            }
        });

        lBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegtymActivity.this, UlogActivity.class);
                startActivity(i);

            }
        });
    }
}
