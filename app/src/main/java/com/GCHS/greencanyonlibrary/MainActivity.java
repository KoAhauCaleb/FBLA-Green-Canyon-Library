package com.GCHS.greencanyonlibrary;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

interface LoginCallback{
    void userResult(UserInfo user);
}

public class MainActivity extends AppCompatActivity implements LoginCallback{
    private final String prefs = "Prefs";
    private String codeContent;
    private TextView EnterID;
    private ProgressBar LoggingIn;
    private UsersSQLmanager userManager = new UsersSQLmanager(this);
    private View v = null;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences(prefs, Context.MODE_PRIVATE);

        boolean li = sp.getBoolean("logged_in", false);

        if(li == true){
            start();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnterID = findViewById(R.id.EnterID);
        LoggingIn = findViewById(R.id.loginProgress);
    }

    private void start() {
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
        finish();
    }


    public void scanNow(View view){
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            }
            else {
                scan();
            }
        }
        else{
            scan();
        }
    }

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
    }

    public void scan(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Vol+ turns on flash, Vol- turns off flash");
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);//Use back camera
        integrator.initiateScan();
    }


    public void loginNow(View view){

        v = view;
        LoggingIn.setVisibility(View.VISIBLE);

        String ID = EnterID.getText().toString();
        userManager.Login(ID, this.getApplicationContext());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            codeContent = scanningResult.getContents();

            // display it on screen
            EnterID.setText(codeContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"Scan unsuccessful, please manually enter ID. ", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scan();
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void userResult(UserInfo user) {
        String ID = EnterID.getText().toString();

        LoggingIn.setVisibility(View.GONE);

        SharedPreferences.Editor editor = sp.edit();

        try {
            Toast toast = Toast.makeText(getApplicationContext(),"Hello " + user.getFirstName(), Toast.LENGTH_LONG);
            toast.show();
            editor.putBoolean("logged_in", true);
            editor.putString("ID", ID);
            editor.putString("fn",user.getFirstName());
            editor.putString("ln",user.getLastName());
            editor.putBoolean("teach", user.getTeacher());
            editor.commit();
            start();
        }catch(Exception e) {
            Snackbar.make(v, "Somthing went wrong", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
