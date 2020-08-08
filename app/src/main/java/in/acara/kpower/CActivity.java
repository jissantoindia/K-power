package in.acara.kpower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String uName,uMobile,uCnum,uPnum,uAddress,uPtyp;
    TextView Dcnum,Dname,Daddress,Dpost,Dptype,Dcsts,Dcstsmsg;
    SwipeRefreshLayout pullToRefresh;
    ImageView Signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        getSupportActionBar().hide();

        pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        Dcnum = findViewById(R.id.dcnum);
        Dname = findViewById(R.id.dname);
        Daddress = findViewById(R.id.daddress);
        Dpost = findViewById(R.id.dpost);
        Dptype = findViewById(R.id.dptyp);
        Dcsts = findViewById(R.id.csts);
        Dcstsmsg = findViewById(R.id.msgbox);
        Signout = findViewById(R.id.signout);
        Dcstsmsg.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("kpower",MODE_PRIVATE);
        uName=sharedPreferences.getString("uname","");
        uMobile=sharedPreferences.getString("umob","");
        uCnum=sharedPreferences.getString("cnum","");
        uPnum=sharedPreferences.getString("pnum","");
        uAddress=sharedPreferences.getString("address","");
        uPtyp=sharedPreferences.getString("ptyp","");

        Dcnum.setText("#"+uCnum);
        Dname.setText(""+uName);
        Daddress.setText(""+uAddress);
        Dpost.setText("Post ID: "+ "TCR/266");
        Dptype.setText("Connection type: "+"Single "+" Phase");
        CstsCheckAPI();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {





            @Override
            public void onRefresh() {
                Dcsts.setText("Checking Status...");
                CstsCheckAPI();
                pullToRefresh.setRefreshing(false);
            }
        });

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getSharedPreferences("kpower",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("umob", "");
                editor.putString("upass", "");
                editor.putString("uname", "");
                editor.putString("cnum", "");
                editor.putString("address", "");
                editor.putString("pnum", "");
                editor.putString("token", "");
                editor.commit();

                Intent i = new Intent(CActivity.this, SplashActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    void OpenBrowser(String Url)
    {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(Url));
        startActivity(i);
    }



    public void BillNow(View view)
    {
        OpenBrowser("https://wss.kseb.in/selfservices/quickpay");
    }
    public void ChatNow(View view)
    {
        OpenBrowser("https://tawk.to/chat/5f2eb1a4ed9d9d2627093567/default?$_tawk_sk=5f2ebcc8d7dcb155f93c8f4e&$_tawk_tk=99de3e6191005ed71e28ebac1badbc02&v=689");
    }

    public void CallNow(View view)
    {
        OpenBrowser("tel:+917034199928");
    }

    public void CstsCheckAPI()
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String URL = getString(R.string.api_url)+"ace-api.php?uid=83";
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

                                Dcsts.setText(" Supply Available");
                                Dcsts.setBackgroundResource(R.drawable.success);
                                Dcstsmsg.setVisibility(View.GONE);

                            }
                            else {

                                Dcstsmsg.setVisibility(View.VISIBLE);
                                Dcsts.setText(" Supply Not Available");
                                Dcsts.setBackgroundResource(R.drawable.danger);
                                Dcstsmsg.setText(msg);
                                Dcstsmsg.setBackgroundResource(R.drawable.dangermsg);

                            }


                        }catch (Exception e){
                            Log.e("asdsadd",e+"d");


                            Toast.makeText(CActivity.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("action","userStatus");
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
