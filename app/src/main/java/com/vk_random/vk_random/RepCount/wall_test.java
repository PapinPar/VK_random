package com.vk_random.vk_random.RepCount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk_random.vk_random.ApiCallActivity;
import com.vk_random.vk_random.R;

import java.util.ArrayList;

public class wall_test extends ActionBarActivity implements View.OnClickListener {

    ArrayList<String> id_id2 = new ArrayList<>();
    TextView wall_id;
    private VKRequest myRequest;
    int count;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_test);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.GoRandom:
                wall_id = (TextView)findViewById(R.id.post_wall2);
                String url;
                url = wall_id.getText().toString();
                get_id(url);
                break;
        }

    }

        private void get_id(String url)
        {
            String[] tmp = url.split("wall");
            tmp = tmp[1].split("%");
            tmp = tmp[0].split("_");
            id_id2.add(tmp[0]);
            id_id2.add(tmp[1]);
            String s = id_id2.get(0) + "_"+ id_id2.get(1);
            VKRequest request2 = VKApi.wall().getById(VKParameters.from(VKApiConst.
                    POSTS, s, VKApiConst.EXTENDED, 1));
            processRequestIfRequired(request2.registerObject());
        }



    private void processRequestIfRequired(long s) {
        VKRequest request = null;
        Log.d("SSSS", "PROCESS REQ");
        long requestId = s;
        request = VKRequest.getRegisteredRequest(requestId);
        if (request != null)
            request.unregisterObject();
        myRequest = request;
        request.executeWithListener(mRequestListener);
    }

    protected void setResponseText(String text)
    {
        ParserRepost rep = new ParserRepost();
        String count_= rep.parse(text);
        String[] tmp = count_.split(":");
        tmp = tmp[1].split(",");

        count = Integer.parseInt(tmp[0]);
        VKRequest request3 = VKApi.wall().getReposts(VKParameters.from(
                VKApiConst.OWNER_ID,id_id2.get(0),
                VKApiConst.POST_ID,id_id2.get(1),
                VKApiConst.OFFSET,0,
                VKApiConst.COUNT,1000));
        Log.d("AHAH", String.valueOf(count));
       start(request3);
    }

    private void start(VKRequest request3) {
        Intent intent = new Intent(this, ApiCallActivity.class);
        intent.putExtra("request",request3.registerObject());
        startActivity(intent);
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
}
