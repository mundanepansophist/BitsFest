package test.tejeshwar.bitsfest.dota;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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


public class RegisterParticipant extends AppCompatActivity {
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    boolean scanned=false;
    private static int reg,accom;
    private static CheckBox freereg,freeaccom;
    private static EditText name,college,phone,email,scancode;
    private static ImageButton qrscan;
    private static FloatingActionButton register;
    private static String register_name,register_college,register_phone,register_email,register_scancode;

    String sname,sphone,semail,scollege;
    boolean sreg,saccom;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutCollege,inputLayoutPhone,inputLayoutScancode;

    @Override
    public void onCreate(final Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_registerparticipant);
        name= (EditText) findViewById(R.id.register_name);
        college= (EditText) findViewById(R.id.register_college);
        phone= (EditText) findViewById(R.id.register_phone);
        email= (EditText) findViewById(R.id.register_email);
        scancode= (EditText) findViewById(R.id.register_scancode);

        qrscan= (ImageButton) findViewById(R.id.qr_scan);
        register= (FloatingActionButton) findViewById(R.id.register_details);

        freeaccom= (CheckBox) findViewById(R.id.free_accom);
        freereg= (CheckBox) findViewById(R.id.free_reg);

        inputLayoutCollege= (TextInputLayout) findViewById(R.id.inputcollege);
        inputLayoutName= (TextInputLayout) findViewById(R.id.inputname);
        inputLayoutPhone= (TextInputLayout) findViewById(R.id.inputphone);
        inputLayoutEmail= (TextInputLayout) findViewById(R.id.inputemail);
        inputLayoutScancode= (TextInputLayout) findViewById(R.id.inputscancode);

        final SharedPreferences[] prefs = {getPreferences(MODE_PRIVATE)};
        if(prefs[0] !=null) {
            sname = prefs[0].getString("name", null);
            sphone = prefs[0].getString("phone", null);
            semail = prefs[0].getString("email", null);
            scollege = prefs[0].getString("college", null);
            sreg= prefs[0].getBoolean("reg",false);
            saccom= prefs[0].getBoolean("accom",false);

            name.setText(sname);
            phone.setText(sphone);
            email.setText(semail);
            college.setText(scollege);

            if(sreg){
                freereg.setChecked(true);
            }

            if (saccom){
                freeaccom.setChecked(true);
            }
        }

        register_scancode = getIntent().getStringExtra("scancode");
        if(register_scancode!=null) {
            scancode.setText(register_scancode);
            scanned=true;
        }


        qrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterParticipant.this,FullScannerActivity.class);
                /*Bundle saveInstanceState=new Bundle();
                saveInstanceState.putString("name",name.getText().toString());
                saveInstanceState.putString("phone",phone.getText().toString());
                saveInstanceState.putString("email",email.getText().toString());
                saveInstanceState.putString("college",college.getText().toString());
                saveInstanceState.putInt("reg",reg);
                saveInstanceState.putInt("accom",accom);
                intent.putExtra("bundle",saveInstanceState);*/

                SharedPreferences.Editor editor=getPreferences(MODE_PRIVATE).edit();
                editor.putString("name",name.getText().toString());
                editor.putString("phone",phone.getText().toString());
                editor.putString("email",email.getText().toString());
                editor.putString("college",college.getText().toString());
                editor.putBoolean("reg",freereg.isChecked());
                editor.putBoolean("accom",freeaccom.isChecked());
                editor.apply();

                startActivity(intent);
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_name = name.getText().toString();
                register_college = college.getText().toString();
                register_phone = phone.getText().toString();
                register_email= email.getText().toString();

                if(!scanned) {
                    register_scancode = scancode.getText().toString();
                }

                if(freereg.isChecked()){
                    reg=1;
                }else{
                    reg=0;
                }

                if (freeaccom.isChecked()){
                    accom=1;
                }
                else {
                    accom = 0;
                }

                if((register_college.isEmpty())){/*
                    inputLayoutCollege.setErrorEnabled(true);
                    inputLayoutCollege.setError("Invalid College Name");*/
                    college.setError("Invalid College Name");
                }

                if((register_name.isEmpty()) ){
                    Log.e("error edit text","hi");
                    /*
                    inputLayoutName.setErrorEnabled(true);
                    inputLayoutName.setError("Invalid Name");*/
                    name.setError("Invalid Name");
                }

                if((register_phone.isEmpty())||register_phone.length()!=10){
                    /*inputLayoutPhone.setErrorEnabled(true);
                    inputLayoutPhone.setError("Invalid Phone Number");*/
                    phone.setError("Invalid Phone Number");
                }

                if((register_scancode.isEmpty())){
                    /*inputLayoutScancode.setErrorEnabled(true);
                    inputLayoutScancode.setError("Invalid ScanCode");*/
                    scancode.setError("Invalid ScanCode");
                }

                if (!(Patterns.EMAIL_ADDRESS.matcher(register_email).matches())){
                    /*inputLayoutEmail.setErrorEnabled(true);
                    inputLayoutEmail.setError("Invalid Email Address");*/
                    email.setError("Invalid Email Address");
                }

                if((register_college!=null) && (register_phone.length() == 10) && (register_name != null)&& (register_scancode != null) && (Patterns.EMAIL_ADDRESS.matcher(register_email).matches())){

                    int success=sendDataServer(register_name, register_college, register_phone, register_email, register_scancode, accom, reg);
                    Log.e("success",success+"");
                    if(success==1) {
                        name.setText("");
                        college.setText("");
                        phone.setText("");
                        email.setText("");
                        scancode.setText("");
                        freeaccom.setChecked(false);
                        freereg.setChecked(false);
                        scanned = false;
                        prefs[0] = null;
                    }
                }
                else {
                    Toast.makeText(RegisterParticipant.this,"Invalid Data given",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    final String url="http://192.168.0.103:3000/login";

    int registration;
    public int sendDataServer(final String name, final String college, final String phone, final String email, final String scancode, final int accom, final int reg){
        final int success[] = new int[1];
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                try {
                    JSONObject object=new JSONObject(response);
                    registration=object.getInt("registration");
                    if(registration==1) {
                        Toast.makeText(RegisterParticipant.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        Log.e("json server bheja ", object.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                success[0]=1;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterParticipant.this,"make sure you are connected to campus intranet",Toast.LENGTH_SHORT).show();
                Log.e("volley error",error.toString());
                success[0] =0;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("tag","register");
                params.put("name",name);
                params.put("college",college);
                params.put("phone",phone);
                params.put("email",email);
                params.put("code",scancode);
                params.put("accom", String.valueOf(accom));
                params.put("reg", String.valueOf(reg));
                Log.e("sent data",params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance().getRequestQueue().add(request);
        Log.e("inner success",success[0]+"");
        return success[0];
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        super.onSaveInstanceState(saveInstanceState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /*sname=savedInstanceState.getString("name");
        sphone=savedInstanceState.getString("phone");
        semail=savedInstanceState.getString("email");
        scollege=savedInstanceState.getString("college");
        name.setText(sname);
        phone.setText(sphone);
        email.setText(semail);
        college.setText(scollege);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*
        getPreferences(MODE_PRIVATE).edit().remove("name").apply();
        getPreferences(MODE_PRIVATE).edit().remove("college").apply();
        getPreferences(MODE_PRIVATE).edit().remove("phone").apply();
        getPreferences(MODE_PRIVATE).edit().remove("email").apply();
        getPreferences(MODE_PRIVATE).edit().remove("reg").apply();
        getPreferences(MODE_PRIVATE).edit().remove("accom").apply();
 */
    }
}