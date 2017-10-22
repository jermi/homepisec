package org.homepisec.android.homepisecapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.homepisec.android.homepisecapp.control.rest.client.ApiClient;
import org.homepisec.android.homepisecapp.control.rest.client.ApiException;
import org.homepisec.android.homepisecapp.control.rest.client.api.ReadingsControllerApi;
import org.homepisec.android.homepisecapp.control.rest.client.model.DeviceEvent;
import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getReadings();
            }
        }).start();
    }

    private void getReadings() {
        try {
            ApiClient apiClient = new ApiClient();
            apiClient.setDatetimeFormat(new SimpleDateFormat("SSSSSSSSSSSSS", Locale.getDefault()));
            apiClient.setBasePath("http://192.168.100.201:8080");
            ReadingsControllerApi readingsControllerApi = new ReadingsControllerApi(apiClient);
            List<DeviceEvent> readingsUsingGET = readingsControllerApi.getReadingsUsingGET();
            Log.i("MAIN", readingsUsingGET.toString());
        } catch (ApiException e) {
            Log.e("MAIN", e.getMessage(), e);
        }
    }

    public void onButtonClick(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
