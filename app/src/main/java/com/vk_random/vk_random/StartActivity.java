package com.vk_random.vk_random;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk_random.vk_random.RepCount.wall_test;

/**
 * Created by Papin on 15.08.2015.
 */
public class StartActivity extends ActionBarActivity
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    public static class PlaceholderFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_test, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            view.findViewById(R.id.users_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS,
                            "id,first_name,last_name,sex,bdate,city,country"));
                    //VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_IDS, "91782740"));
                    request.secure = false;
                    request.useSystemLanguage = false;
                    startApiCall(request);
                }
            });

            view.findViewById(R.id.friends_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKRequest request = VKApi.friends().get(VKParameters.from(VKApiConst.FIELDS, "id,first_name,last_name,sex,bdate,city"));
                    startApiCall(request);
                }
            });

            view.findViewById(R.id.messages_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKRequest request = VKApi.messages().get();
                    startApiCall(request);
                }
            });

            view.findViewById(R.id.dialogs_get).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VKRequest request = VKApi.messages().getDialogs();
                    startApiCall(request);
                }
            });


            view.findViewById(R.id.wall_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(getActivity(), wall_test.class);
                    startActivity(i);
                    //VKRequest request = VKApi.wall().getReposts(VKParameters.from(VKApiConst.POST_ID,"2195",VKApiConst.OWNER_ID,"70521140"));
                   // startApiCall(request);

                }
            });



            view.findViewById(R.id.test_validation).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final VKRequest test = new VKRequest("account.testValidation");
                    startApiCall(test);
                }
            });

        }

        private void startApiCall(VKRequest request) {
            Intent i = new Intent(getActivity(), ApiCallActivity.class);
            i.putExtra("request", request.registerObject());
            startActivity(i);
        }

        private void showError(VKError error) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(error.toString())
                    .setPositiveButton("OK", null)
                    .show();

            if (error.httpError != null) {
                Log.w("Test", "Error in request or upload", error.httpError);
            }
        }

    }
}
