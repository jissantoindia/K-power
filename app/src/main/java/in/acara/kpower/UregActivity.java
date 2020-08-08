package in.acara.kpower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UregActivity extends AppCompatActivity {

    EditText rUname,rUcnum,rUmob,rUptyp,rUpass;
    Button regButton;
    String reguname,regcnum,regumob,regptyp,regupass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ureg);
        getSupportActionBar().hide();

        regButton = findViewById(R.id.regbutton);
        rUname = findViewById(R.id.runame);
        rUcnum = findViewById(R.id.rucnum);
        rUmob = findViewById(R.id.rumob);
        rUptyp = findViewById(R.id.ruptyp);
        rUpass = findViewById(R.id.rupass);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reguname =  rUname.getText().toString();
                regcnum =  rUcnum.getText().toString();
                regumob =  rUmob.getText().toString();
                regptyp =  rUptyp.getText().toString();
                regupass =  rUpass.getText().toString();


                if(reguname.length()>3 && regcnum.length()>3 && regumob.length()>3 && regptyp.length()>0 && regupass.length()>3)
                {

                }
                else
                {
                    Toast.makeText(UregActivity.this, "Oops! Some fields are Empty...", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
