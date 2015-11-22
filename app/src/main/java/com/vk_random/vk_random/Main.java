package com.vk_random.vk_random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Papin on 15.08.2015.
 */
public class Main extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent start = new Intent(this,LoginActivity.class);
        startActivity(start);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
