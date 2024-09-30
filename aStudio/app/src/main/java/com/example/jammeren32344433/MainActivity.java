package com.example.jammeren32344433;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.widget.Switch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "11111111";  // Тэг для логов
    private String ipApi;
    private boolean isFirstSelection = true;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String SELECTED_GROUP_KEY = "selected_group";
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        globalvariable app = (globalvariable) getApplication();
        ipApi = app.getIpApi();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedGroup = sharedPreferences.getString(SELECTED_GROUP_KEY, null);
        String auto = getIntent().getStringExtra("auto");

//        savedGroup = null;
        if (savedGroup != null) {
            if (auto == null) {
                //Log.i(TAG, savedGroup);
                Intent intent = new Intent(this, ShowFriday.class);
                intent.putExtra("selected_group", savedGroup);
                startActivity(intent);
                finish();
            }
            else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SELECTED_GROUP_KEY, null);
                editor.apply();
            }
        }

        setContentView(R.layout.activity_main);

        Switch rememberSwitch = findViewById(R.id.switch1);
        rememberSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Логика при включении/выключении Switch
//            Log.i(TAG, "1");
            if (isChecked) {
//                Log.i(TAG, "2");

                String selectedGroup = sharedPreferences.getString(SELECTED_GROUP_KEY, null);
                if (selectedGroup != null) {
//                    Log.i(TAG, "3");
//                    Log.i(TAG, selectedGroup);
                    Intent intent = new Intent(MainActivity.this, ShowFriday.class);
                    intent.putExtra("selected_group", selectedGroup);
                    startActivity(intent);
                    finish();
                }
            }
        });

        showGroups();
    }

//    public void onButtonAuto(View view) {
//        Intent intent = new Intent(this, ShowMonday.class);
//        startActivity(intent);
//    }

    public void showGroups() {
        ImageView noInternetImage = findViewById(R.id.noInternetImage);

        TextView view = findViewById(R.id.content);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("http://"+ipApi+"/group").build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    JSONArray array = new JSONArray(json);
                    ArrayList<String> groups = new ArrayList<>();

                    Spinner spinner = findViewById(R.id.spinner);
                    groups.add("Выбор группы");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String text = object.getString("name");
                        groups.add(text);
                    }
                    MainActivity.this.runOnUiThread(() -> {
                        noInternetImage.setVisibility(View.INVISIBLE);
                    });

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, groups);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    MainActivity.this.runOnUiThread(() -> {
                        spinner.setAdapter(adapter);

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (isFirstSelection) {
                                    isFirstSelection = false;
                                } else {

                                    String selectedGroup = groups.get(position);
//                                    Log.i(TAG, "211"+selectedGroup);
                                    Switch rememberSwitch = findViewById(R.id.switch1);
                                    if (rememberSwitch.isChecked()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(SELECTED_GROUP_KEY, selectedGroup);
                                        editor.apply();
                                    }

                                    Intent intent = new Intent(MainActivity.this, ShowFriday.class);
                                    intent.putExtra("selected_group", selectedGroup);
//                                    Log.i(TAG, selectedGroup);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                            }
                        });
                    });

                } catch (JSONException e) {
                    view.post(() -> view.append(e.getMessage()));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                view.post(() -> view.append(e.getMessage()));
                Log.i(TAG, "111111111111");

//                ImageView noInternetImage = findViewById(R.id.noInternetImage);
                MainActivity.this.runOnUiThread(() -> {
                    noInternetImage.setVisibility(View.VISIBLE);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptEx) {
                    interruptEx.printStackTrace();
                }
                showGroups();
            }
        });
    }
}
