package com.GCHS.greencanyonlibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

interface GetUserResponse {
    void postResult(String result);
}

interface AppRDS {
    @LambdaFunction
    String appRDS(String request);
}

/**
 * Created by User on 11/11/2017.
 */

public class UsersSQLmanager implements GetUserResponse{

    private LoginCallback call;

    public UsersSQLmanager(LoginCallback callback){
        call = callback;
    }

    void Login(final String ID, Context applicationContext){
        GetUserJSON att = new GetUserJSON(applicationContext);

        att.delegate = this;

        try {
            att.execute(ID);//Run on new thread because android requires this for network calls,
                                           // wait for result and store in result
        }catch (Exception e){
            Log.e(e.getMessage(), "UsersSQLmanager - Line 32");
        }
    }

    @Override
    public void postResult(String result) {
        UserInfo user = null;
        JsonObject userJson = null;
        try {
            userJson = new Gson().fromJson(result, JsonObject.class);
        }catch(Exception e){
            Log.e(e.getMessage(), "UsersSQLmanager - Line 44");
        }
        boolean teach = false;

        try {
            if(userJson.get("teacher").getAsInt() == 1){
                teach = true;
            }
            user = new UserInfo(userJson.get("firstname").getAsString(), userJson.get("lastname").getAsString(), teach);
        }catch(Exception e){
            Log.e(e.getMessage(), "UsersSQLmanager - Line 54");
        }
        call.userResult(user);
    }
}

class GetUserJSON extends AsyncTask<String, Void, String> {

    GetUserResponse delegate = null;
    private Context applicationContext;

    GetUserJSON(Context context){
        applicationContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
            applicationContext, "us-east-2:d9aac7d6-3d53-4f6e-bb91-08ea99f2c390", Regions.US_EAST_2);

        // Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
        LambdaInvokerFactory factory = new LambdaInvokerFactory(applicationContext,
                Regions.US_EAST_2, cognitoProvider);

        // Create the Lambda proxy object with a default Json data binder.
        // You can provide your own data binder by implementing
        // LambdaDataBinder.
        final AppRDS call = factory.build(AppRDS.class);
        // invoke "echo" method. In case it fails, it will throw a
        // LambdaFunctionException.
        try {
            return call.appRDS(strings[0]);
        } catch (LambdaFunctionException lfe) {
            Log.e("Tag", "Failed to invoke echo", lfe);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.postResult(result);
    }
}