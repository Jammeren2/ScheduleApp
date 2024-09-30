package com.example.jammeren32344433;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;
import android.widget.LinearLayout;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class ShowSaturday extends AppCompatActivity {
    private static final String TAG = "11111111";  // Тэг для логов
    private String ipApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_saturday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        globalvariable app = (globalvariable) getApplication();
        ipApi = app.getIpApi();
        String selectedPrepod = getIntent().getStringExtra("selected_prepod");
        TextView textView = findViewById(R.id.textView3);
        textView.setText(selectedPrepod);
        get_load_img();



    }
    public void load_info(String mail, String phone, String address) {
        TextView textView4 = findViewById(R.id.textView4);
        TextView textView5 = findViewById(R.id.textView5);
        TextView textView6 = findViewById(R.id.textView6);

        if (!mail.isEmpty()) {
            ShowSaturday.this.runOnUiThread(() -> {
                textView4.setVisibility(View.VISIBLE);
                textView4.setText(mail);
            });
            textView4.setOnClickListener(view -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822"); // MIME тип для email
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail}); // Email-адрес

                // Устанавливаем пакет для Gmail
                intent.setPackage("com.google.android.gm");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {}
            });
        }

        if (!phone.isEmpty()) {
            ShowSaturday.this.runOnUiThread(() -> {
                textView5.setVisibility(View.VISIBLE);
                textView5.setText(phone);
            });
        }

        if (!address.isEmpty()) {
            ShowSaturday.this.runOnUiThread(() -> {
                textView6.setVisibility(View.VISIBLE);
                textView6.setText(address);
            });
        }
    }


    public void back (View view) {
        String selectedgroup = getIntent().getStringExtra("selected_group");
        Intent intent = new Intent(ShowSaturday.this, ShowFriday.class);
        intent.putExtra("selected_group", selectedgroup);
        startActivity(intent);
        finish();
    } // вернутся назад
    public void get_load_img() {
        String selectedPrepod = getIntent().getStringExtra("selected_prepod");
        ImageView imageView3 = findViewById(R.id.imageView3);

        // Создаем клиент OkHttp
        OkHttpClient client = new OkHttpClient();

        // Формируем URL запроса
        String url = "http://"+ipApi+"/prepod?fio=" + selectedPrepod;

        // Создаем запрос
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Выполняем запрос асинхронно
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Обрабатываем ошибку
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Получаем тело ответа
                    String responseData = response.body().string();

                    try {
                        // Преобразуем ответ в JSON
                        JSONArray jsonArray = new JSONArray(responseData);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        String teacher_mail = jsonObject.getString("teacher_mail");
                        String teacher_phone = jsonObject.getString("teacher_phone");
                        String teacher_adress = jsonObject.getString("teacher_adress");
                        load_info(teacher_mail, teacher_phone, teacher_adress);

                        // Получаем URL изображения
                        final String imageUrl = jsonObject.getString("teacher_img");
                        if (imageUrl != "null") {
                            Log.i(TAG, imageUrl);
                            // Загружаем изображение в UI потоке
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Picasso.get().load(imageUrl).into(imageView3);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }   // загрузка фото
}