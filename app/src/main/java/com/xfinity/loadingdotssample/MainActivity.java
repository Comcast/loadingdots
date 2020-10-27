package com.xfinity.loadingdotssample;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.xfinity.loadingdots.LoadingDots;

public class MainActivity extends AppCompatActivity {
    private LoadingDots loadingDots;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingDots = (LoadingDots) findViewById(R.id.loading_dots);
        View visibilityToggle = findViewById(R.id.visibility_toggle);
        visibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadingDots.getVisibility() == View.GONE) {
                    loadingDots.setVisibility(View.VISIBLE);
                } else {
                    loadingDots.setVisibility(View.GONE);
                }
            }
        });
    }
}

