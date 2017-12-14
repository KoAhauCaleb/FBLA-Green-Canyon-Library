package com.GCHS.greencanyonlibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

interface GetBooksResponse {
    void postResult(String result);
}

interface RDSGetBooks {
    @LambdaFunction
    String RDSgetBooks();
}

/**
 * Created by User on 12/8/2017.
 */

public class BooksSQLmanager implements GetBooksResponse{

    private BookCallback call;

    public BooksSQLmanager(BookCallback callback){
        call = callback;
    }

    void getBooks(Context applicationContext){
        GetBooksJSON getBooksAsync = new GetBooksJSON(applicationContext);

        getBooksAsync.delegate = this;

        try {
            getBooksAsync.execute();//Run on new thread because android requires this for network calls,
            // wait for result and store in result
        }catch (Exception e){
            Log.e(e.getMessage(), "BooksSQLmanager");
            e.printStackTrace();
            //return null if failed
        }
    }

    @Override
    public void postResult(String result) {
        call.asyncResult(result);
    }
}

class GetBooksJSON extends AsyncTask<String, Void, String> {

    public GetBooksResponse delegate = null;
    Context applicationContext;

    public GetBooksJSON(Context applicationcontext){
        applicationContext = applicationcontext;
    }

    @Override
    protected String doInBackground(String... strings) {

        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                applicationContext, "us-east-2:d9aac7d6-3d53-4f6e-bb91-08ea99f2c390", Regions.US_EAST_2);

        LambdaInvokerFactory factory = new LambdaInvokerFactory(applicationContext,
                Regions.US_EAST_2, cognitoProvider);

        final RDSGetBooks call = factory.build(RDSGetBooks.class);
        try {
            return call.RDSgetBooks();
        } catch (LambdaFunctionException lfe) {
            Log.e("Tag", "Failed to invoke echo", lfe);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.postResult(s);
    }
}