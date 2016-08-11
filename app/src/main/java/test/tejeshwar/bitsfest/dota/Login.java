package test.tejeshwar.bitsfest.dota;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import test.tejeshwar.bitsfest.R;
import test.tejeshwar.bitsfest.app.VolleySingleton;


public class Login extends AppCompatActivity {

    final String url="http://192.168.0.103:3000";
    EditText password,id;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password= (EditText) findViewById(R.id.password_edittext);
        id= (EditText) findViewById(R.id.loginid_edittext);
        login= (Button) findViewById(R.id.validate_user_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginid=id.getText().toString();
                String passcode=password.getText().toString();
                int s=validateLogin(loginid,passcode);
            }
        });

    }
    int success=0;
    private int validateLogin(final String loginid, final String passcode) {

        if (loginid.isEmpty()) {
            id.setError("Login ID cannot be empty");
        }
        if (passcode.isEmpty()) {
            password.setError("Password cannot be empty");
        }

        if ( (!loginid.isEmpty())&&(!passcode.isEmpty())) {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("dosh login server",response);
                    resolveResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volley error",error+"");
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tag", "dosh_login");
                    params.put("team_id", loginid);
                    params.put("password", passcode);
                    return params;
                }
            };
            VolleySingleton.getInstance().getRequestQueue().add(request);
        }
        return 0;
    }

    private void resolveResponse(String response) {
        try {
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.has("error")){

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
