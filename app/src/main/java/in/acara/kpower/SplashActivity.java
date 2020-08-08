package in.acara.kpower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String umob,tmob,ucnum,refreshedToken,refreshedTokenNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences = getSharedPreferences("kpower",MODE_PRIVATE);
        umob=sharedPreferences.getString("umob","");
        tmob=sharedPreferences.getString("tmob","");
        ucnum=sharedPreferences.getString("cnum","");
        refreshedToken=sharedPreferences.getString("token","");
        refreshedTokenNew = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(umob.length()>3 && tmob.length()==0) {

                    if(refreshedToken.equalsIgnoreCase(refreshedTokenNew)) {
                        Intent i = new Intent(SplashActivity.this, CActivity.class);
                        startActivity(i);
                        finish();
                    }else
                    {
                        Intent i = new Intent(SplashActivity.this, CActivity.class);
                        startActivity(i);
                        finish();
                    }

                }else if(tmob.length()>3 && umob.length()==0)
                {
                    Intent i = new Intent(SplashActivity.this, CActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);



    }

    public void InstID()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = getString(R.string.api_url)+"instid";
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
                            if(sts.equalsIgnoreCase("00"))
                            {
                                Intent i = new Intent(SplashActivity.this, CActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {

                                Intent i = new Intent(SplashActivity.this, CActivity.class);
                                startActivity(i);
                                finish();

                            }


                        }catch (Exception e){
                            Log.e("asdsadd",e+"d");


                            Toast.makeText(SplashActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
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

                            Log.i("log error", errorString);
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cnum",ucnum);
                params.put("instid",refreshedTokenNew);
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
