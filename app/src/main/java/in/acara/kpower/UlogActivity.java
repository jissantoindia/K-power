package in.acara.kpower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UlogActivity extends AppCompatActivity {

    String logumob,logupass,refreshedToken,instID;
    EditText lUmob,lUpass;
    Button logButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulog);
        getSupportActionBar().hide();

        logButton = findViewById(R.id.logbutton);
        lUmob = findViewById(R.id.lumob);
        lUpass = findViewById(R.id.lupass);

        sharedPreferences = getSharedPreferences("kpower",MODE_PRIVATE);
        refreshedToken=sharedPreferences.getString("token","");

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logumob =  lUmob.getText().toString();
                logupass =  lUpass.getText().toString();

                if(logumob.length()>3 && logupass.length()>3)
                {
                    logButton.setEnabled(false);
                    logButton.setText("Please Wait...");
                    LoginAPI();
                }
                else
                {
                    Intent i = new Intent(UlogActivity.this, CActivity.class);
                    startActivity(i);
                }

            }
        });

    }
//
//    private void playVideoAtSelection() {
//        if (!(player == null)) {
//            player.loadVideo(LIVE_ID);
//        }
//    }

    public void LoginAPI()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = getString(R.string.api_url)+"ace-api.php";
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        Log.i("VOLLEYES", response);

                        try {
                            JSONObject jsn=new JSONObject(response);
                            String msg = jsn.getString("msg");
                            String sts = jsn.getString("sts");
                            if(sts.equalsIgnoreCase("01"))
                            {

                                String name = jsn.getString("fname");
                                String cnum = jsn.getString("uc");
                                String address = jsn.getString("address");
                                String pnum = jsn.getString("email");
//                                String ptyp = dta.getString("cnum");
//                                String instID = dta.getString("instid");

                                sharedPreferences = getSharedPreferences("kpower",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("umob", logumob);
                                editor.putString("upass", logupass);
                                editor.putString("uname", name);
                                editor.putString("cnum", cnum);
                                editor.putString("address", address);
                                editor.commit();

                                    Intent i = new Intent(UlogActivity.this, CActivity.class);
                                    startActivity(i);
                                    finish();

                            }
                            else {

                                logButton.setEnabled(true);
                                logButton.setText("Login Now");
                                Toast.makeText(UlogActivity.this, msg, Toast.LENGTH_SHORT).show();

                            }


                        }catch (Exception e){
                            Log.e("asdsadd",e+"d");

                            logButton.setEnabled(true);
                            logButton.setText("Login Now");
                            Toast.makeText(UlogActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if(response != null && response.data != null){
                            String errorString = new String(response.data);
                            logButton.setEnabled(true);
                            logButton.setText("Try Again");
                            Log.i("log error", errorString);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("moboremail",logumob);
                params.put("password",logupass);
                params.put("action","login");
                Log.i("loginp ", params.toString());


                return params;
            }

        };


        // Add the realibility on the connection.
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));

        // Start the request immediately
        queue.add(request);

    }

}
