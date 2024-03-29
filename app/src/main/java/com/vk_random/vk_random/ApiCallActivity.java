package com.vk_random.vk_random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk_random.vk_random.GetRandomUser.Parser;

import java.util.ArrayList;

/**
 * Created by Papin on 15.08.2015.
 */
public class ApiCallActivity extends ActionBarActivity {
    private VKRequest myRequest;
    private VKRequest RepCount;

    private static final String FRAGMENT_TAG = "response_view";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_call);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), FRAGMENT_TAG)
                    .commit();
        }

        processRequestIfRequired();
    }
    private PlaceholderFragment getFragment() {
        return (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }
    private void processRequestIfRequired() {
        VKRequest request = null;

        if (getIntent() != null && getIntent().getExtras() != null && getIntent().hasExtra("request")) {
            long requestId = getIntent().getExtras().getLong("request");
            request = VKRequest.getRegisteredRequest(requestId);
            if (request != null)
                request.unregisterObject();
        }

        if (request == null) return;
        myRequest = request;
        request.executeWithListener(mRequestListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("response", getFragment().textView.getText());
        if (myRequest != null) {
            outState.putLong("request", myRequest.registerObject());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence response = savedInstanceState.getCharSequence("response");
        if (response != null) {
            getFragment().textView.setText(response);
        }

        long requestId = savedInstanceState.getLong("request");
        myRequest = VKRequest.getRegisteredRequest(requestId);
        if (myRequest != null && RepCount!=null)
        {
            myRequest.unregisterObject();
            myRequest.setRequestListener(mRequestListener);
        }
    }


    protected void setResponseText(String text)
    {
      ArrayList<String> finish = new ArrayList<String>();
        Parser parser = new Parser();
        finish= parser.parse(text);
        String print = "";
        for(int s =0;s<finish.size();s++)
        {
            print = print+"http:/vk.com/id" + finish.get(s) + "\n";
            Log.d("SAD", String.valueOf(finish.get(s)));
        }
        print = print.replace("\"","");
        PlaceholderFragment fragment = getFragment();
        if (fragment != null && fragment.textView != null) {
            fragment.textView.setText(print);
        }
    }



    VKRequest.VKRequestListener mRequestListener = new VKRequest.VKRequestListener()
    {
        public void onComplete(VKResponse response)
        {
            setResponseText(response.json.toString());
        }

        @Override
        public void onError(VKError error)
        {
            setResponseText(error.toString());
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded,
                               long bytesTotal)
        {        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
        {
            getFragment().textView.append(
                    String.format("Attempt %d/%d failed\n", attemptNumber, totalAttempts));
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        myRequest.cancel();
        Log.d(VKSdk.SDK_TAG, "On destroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        public TextView textView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View v = inflater.inflate(R.layout.fragment_api_call, container, false);
            textView = (TextView) v.findViewById(R.id.response);
            return v;
        }
    }
}
