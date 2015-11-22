package com.vk_random.vk_random.RepCount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk_random.vk_random.ApiCallActivity;
import com.vk_random.vk_random.R;

import java.util.ArrayList;

/**
 * Created by Papin on 26.08.2015.
 */
public class ApiCallActivity2 extends ActionBarActivity {
    private VKRequest myRequest;
    ArrayList<String> id_id2 = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_call);

        processRequestIfRequired();
    }
    public void processRequestIfRequired() {
        VKRequest request = null;
        Log.d("SSSS","PROCESS REQ");
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
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("SSSS", "On RESOTE");
        long requestId = savedInstanceState.getLong("request");
        myRequest = VKRequest.getRegisteredRequest(requestId);
        if (myRequest != null)
        {
            myRequest.unregisterObject();
            myRequest.setRequestListener(mRequestListener);

        }
    }


    protected void setResponseText(String text)
    {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        ParserRepost rep = new ParserRepost();
        String count_= rep.parse(text);
        String[] tmp = count_.split(":");
        tmp = tmp[1].split(",");

        int count = Integer.parseInt(tmp[0]);

        get_id(id);

        statActivity(count, id);
    }

    private void get_id(String id)
    {
        String[] tmp = id.split("_");
        Log.d("ID_WALL", tmp[0] +"  "+ tmp[1]);
        id_id2.add(tmp[0]);
        id_id2.add(tmp[1]);

    }

    private void statActivity(int count,String id)
    {

        Intent i = new Intent(this, ApiCallActivity.class);
        VKRequest request = VKApi.wall().getReposts(VKParameters.from(VKApiConst.POST_ID, id_id2.get(1),
                VKApiConst.OWNER_ID, id_id2.get(0), VKApiConst.COUNT, 1000, VKApiConst.OFFSET, 0));
        i.putExtra("request", request.registerObject());
        startActivity(i);
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


}
