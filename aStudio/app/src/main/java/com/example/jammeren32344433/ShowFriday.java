package com.example.jammeren32344433;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.util.Log;


public class ShowFriday extends AppCompatActivity {
    private String ipApi;
    private Button[] buttons = new Button[6];
    private static final String TAG = "11111111";  // Тэг для логов
//    public String selectedGroup = getIntent().getStringExtra("selected_group");

    private OkHttpClient client = new OkHttpClient();
    private String weekNumber;
    public String day_global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_friday);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        globalvariable app = (globalvariable) getApplication();
        ipApi = app.getIpApi();
        String selectedGroup = getIntent().getStringExtra("selected_group");
        buttons[0] = findViewById(R.id.button_monday);
        buttons[1] = findViewById(R.id.button_tuesday);
        buttons[2] = findViewById(R.id.button_wednesday);
        buttons[3] = findViewById(R.id.button_thursday);
        buttons[4] = findViewById(R.id.button_friday);
        buttons[5] = findViewById(R.id.button_saturday);

        Calendar calendar = Calendar.getInstance();
        day_global = getDayOfWeek(calendar);
        highlightCurrentDayButton(day_global);

        TextView textView = findViewById(R.id.textView2);
        textView.setText(selectedGroup);

        Spinner spinner = findViewById(R.id.spinner2);
        fetchWeeksData(spinner);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String[] parts = selectedItem.split(" ");
                weekNumber = parts[1];
                fetchSchedule(selectedGroup, weekNumber, day_global);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private  void  clear(){
        int[] paraNamesIds = {R.id.para1_name, R.id.para2_name, R.id.para3_name, R.id.para4_name, R.id.para5_name, R.id.para6_name, R.id.para7_name, R.id.para8_name};
        int[] paraPrepodsIds = {R.id.para1_prepod, R.id.para2_prepod, R.id.para3_prepod, R.id.para4_prepod, R.id.para5_prepod, R.id.para6_prepod, R.id.para7_prepod};
        int[] paraMestosIds = {R.id.para1_mesto, R.id.para2_mesto, R.id.para3_mesto, R.id.para4_mesto, R.id.para5_mesto, R.id.para6_mesto, R.id.para7_mesto};

        // Устанавливаем все TextView на "None" перед обновлением
        for (int i = 0; i < paraNamesIds.length; i++) {
            TextView nameView = findViewById(paraNamesIds[i]);
            nameView.setText("None");
        }

        for (int i = 0; i < paraPrepodsIds.length; i++) {
            TextView prepodView = findViewById(paraPrepodsIds[i]);
            prepodView.setText("None");
        }

        for (int i = 0; i < paraMestosIds.length; i++) {
            TextView mestoView = findViewById(paraMestosIds[i]);
            mestoView.setText("None");
        }
        LinearLayout para1 = findViewById(R.id.para1);
        LinearLayout para2 = findViewById(R.id.para2);
        LinearLayout para3 = findViewById(R.id.para3);
        LinearLayout para4 = findViewById(R.id.para4);
        LinearLayout para5 = findViewById(R.id.para5);
        LinearLayout para6 = findViewById(R.id.para6);
        LinearLayout para7 = findViewById(R.id.para7);
        LinearLayout para8 = findViewById(R.id.para8);
        ShowFriday.this.runOnUiThread(() -> {
            para1.setVisibility(View.GONE);
            para2.setVisibility(View.GONE);
            para3.setVisibility(View.GONE);
            para4.setVisibility(View.GONE);
            para5.setVisibility(View.GONE);
            para6.setVisibility(View.GONE);
            para7.setVisibility(View.GONE);
            para8.setVisibility(View.GONE);
        });

    }
    private void fetchSchedule(String group, String week, String day) {
        String url = "http://"+ipApi+"/schedule";

        // Создаем JSON-объект с параметрами
        String json = "{"
                + "\"group\":\"" + group + "\","
                + "\"week\":\"" + week + "\","
                + "\"day\":\"" + day + "\","
                + "\"param\":\"128888888888888883102974714623864hf2109643gb107462d7163cb5bv71205689712365912b5v6512365576b21786375678v16b2567167856187265861274865vbb1v265v1623756b146587126375b691v26587v1b2875v752871v2h990385bv918265v9b1209856v12896b35962b1vks41209k5871v0b29364265097617831237649128765782169875621b7569v2\""
                + "}";

        // Определяем тип контента как JSON
        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        // Создаем тело запроса с JSON
        RequestBody body = RequestBody.create(json, JSON);

        // Создаем POST запрос
        Request request = new Request.Builder()
                .url(url)
                .post(body) // Указываем, что это POST запрос с JSON
                .build();

        // Отправляем запрос
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
//                Log.i(TAG, "11111111");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.i(TAG, "222222");
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
//                    Log.i(TAG, responseData);
                    runOnUiThread(() -> {
                        try {
                            JSONArray scheduleArray = new JSONArray(responseData);
                            updateSchedule(scheduleArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private String getDayOfWeek(Calendar calendar) {
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        boolean after1715 = (calendar.get(Calendar.HOUR_OF_DAY) > 17) ||
                (calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) > 15);

        switch (currentDay) {
            case Calendar.MONDAY: return after1715 ? "вт" : "пн";
            case Calendar.TUESDAY: return after1715 ? "ср" : "вт";
            case Calendar.WEDNESDAY: return after1715 ? "чт" : "ср";
            case Calendar.THURSDAY: return after1715 ? "пт" : "чт";
            case Calendar.FRIDAY: return after1715 ? "сб" : "пт";
            case Calendar.SATURDAY: return "сб";
            default: return "пн";
        }
    }
    private void updateSchedule(JSONArray scheduleArray) {
        try {
            // Массивы ID для элементов TextView
            int[] paraNamesIds = {R.id.para1_name, R.id.para2_name, R.id.para3_name, R.id.para4_name, R.id.para5_name, R.id.para6_name, R.id.para7_name, R.id.para8_name};
            int[] paraPrepodsIds = {R.id.para1_prepod, R.id.para2_prepod, R.id.para3_prepod, R.id.para4_prepod, R.id.para5_prepod, R.id.para6_prepod, R.id.para7_prepod};
            int[] paraMestosIds = {R.id.para1_mesto, R.id.para2_mesto, R.id.para3_mesto, R.id.para4_mesto, R.id.para5_mesto, R.id.para6_mesto, R.id.para7_mesto};
            LinearLayout para1 = findViewById(R.id.para1);
            LinearLayout para2 = findViewById(R.id.para2);
            LinearLayout para3 = findViewById(R.id.para3);
            LinearLayout para4 = findViewById(R.id.para4);
            LinearLayout para5 = findViewById(R.id.para5);
            LinearLayout para6 = findViewById(R.id.para6);
            LinearLayout para7 = findViewById(R.id.para7);
            LinearLayout para8 = findViewById(R.id.para8);

            // Обновляем расписание из JSON
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject scheduleObject = scheduleArray.getJSONObject(i);
                String time = scheduleObject.getString("time");
                String subject = scheduleObject.getString("subject");
                String teacher = scheduleObject.getString("teacher");
                String type = scheduleObject.getString("type");
                JSONArray rooms = scheduleObject.getJSONArray("rooms");
//                Log.i(TAG, type);
                // Преобразуем массив rooms в строку через запятую
                StringBuilder roomsString = new StringBuilder();
                for (int j = 0; j < rooms.length(); j++) {
                    if (j > 0) {
                        roomsString.append(", ");
                    }
                    roomsString.append(rooms.getString(j));
                }

                // В зависимости от времени обновляем соответствующую пару
                if (time.equals("08:30-10:00")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para1.setVisibility(View.VISIBLE);
                    });
                    TextView para1Name = findViewById(paraNamesIds[0]);
                    TextView para1Prepod = findViewById(paraPrepodsIds[0]);
                    TextView para1Mesto = findViewById(paraMestosIds[0]);

                    para1Name.setText(subject);
                    para1Prepod.setText(teacher);
                    para1Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("10:15-11:45")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para2.setVisibility(View.VISIBLE);
                    });
                    TextView para2Name = findViewById(paraNamesIds[1]);
                    TextView para2Prepod = findViewById(paraPrepodsIds[1]);
                    TextView para2Mesto = findViewById(paraMestosIds[1]);

                    para2Name.setText(subject);
                    para2Prepod.setText(teacher);
                    para2Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("12:00-13:30")) {

                    ShowFriday.this.runOnUiThread(() -> {
                        para3.setVisibility(View.VISIBLE);
                    });

                    TextView para3Name = findViewById(paraNamesIds[2]);
                    TextView para3Prepod = findViewById(paraPrepodsIds[2]);
                    TextView para3Mesto = findViewById(paraMestosIds[2]);

                    para3Name.setText(subject);
                    para3Prepod.setText(teacher);
                    para3Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("14:00-15:30")) {
                    if (subject != null) {
                        ShowFriday.this.runOnUiThread(() -> {
                            para4.setVisibility(View.VISIBLE);
                        });
                    }
                    TextView para4Name = findViewById(paraNamesIds[3]);
                    TextView para4Prepod = findViewById(paraPrepodsIds[3]);
                    TextView para4Mesto = findViewById(paraMestosIds[3]);

                    para4Name.setText(subject);
                    para4Prepod.setText(teacher);
                    para4Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("15:45-17:15")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para5.setVisibility(View.VISIBLE);
                    });
                    TextView para5Name = findViewById(paraNamesIds[4]);
                    TextView para5Prepod = findViewById(paraPrepodsIds[4]);
                    TextView para5Mesto = findViewById(paraMestosIds[4]);

                    para5Name.setText(subject);
                    para5Prepod.setText(teacher);
                    para5Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("17:30-19:00")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para6.setVisibility(View.VISIBLE);
                    });
                    TextView para6Name = findViewById(paraNamesIds[5]);
                    TextView para6Prepod = findViewById(paraPrepodsIds[5]);
                    TextView para6Mesto = findViewById(paraMestosIds[5]);

                    para6Name.setText(subject);
                    para6Prepod.setText(teacher);
                    para6Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("19:15-20:45")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para7.setVisibility(View.VISIBLE);
                    });
                    TextView para7Name = findViewById(paraNamesIds[6]);
                    TextView para7Prepod = findViewById(paraPrepodsIds[6]);
                    TextView para7Mesto = findViewById(paraMestosIds[6]);

                    para7Name.setText(subject);
                    para7Prepod.setText(teacher);
                    para7Mesto.setText(roomsString.toString()+"        "+ type);
                } if (time.equals("21:00-22:30")) {
                    ShowFriday.this.runOnUiThread(() -> {
                        para8.setVisibility(View.VISIBLE);
                    });
                    TextView para8Name = findViewById(paraNamesIds[7]);
                    TextView para8Prepod = findViewById(paraPrepodsIds[7]);
                    TextView para8Mesto = findViewById(paraMestosIds[7]);

                    para8Name.setText(subject);
                    para8Prepod.setText(teacher);
                    para8Mesto.setText(roomsString.toString()+"        "+ type);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onButtonClick(View view) {
        resetButtonColors();
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.grey2));
        day_global = getButtonDay(view.getId());
        String selectedGroup = getIntent().getStringExtra("selected_group");
        clear();
        fetchSchedule(selectedGroup, weekNumber, day_global);
    }
    public void onClickGroup(View view) {
        Intent intent = new Intent(ShowFriday.this, MainActivity.class);
        intent.putExtra("auto", "none");
        startActivity(intent);
    }
    private void resetButtonColors() {
        for (Button button : buttons) {
            button.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private String getButtonDay(int buttonId) {
        if (buttonId == R.id.button_monday) {
            return "пн";
        } else if (buttonId == R.id.button_tuesday) {
            return "вт";
        } else if (buttonId == R.id.button_wednesday) {
            return "ср";
        } else if (buttonId == R.id.button_thursday) {
            return "чт";
        } else if (buttonId == R.id.button_friday) {
            return "пт";
        } else if (buttonId == R.id.button_saturday) {
            return "сб";
        } else {
            return "";
        }
    }

    private void fetchWeeksData(Spinner spinner) {
        Request request = new Request.Builder()
                .url("http://"+ipApi+"/week")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonResponse = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            updateSpinnerWithWeeks(spinner, jsonResponse);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }
    private void updateSpinnerWithWeeks(Spinner spinner, String jsonResponse) throws JSONException, ParseException {
        JSONArray weeksArray = new JSONArray(jsonResponse);
        String[] weekStrings = new String[weeksArray.length()];
        int currentWeekIndex = -1;

        // Формат для дат
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = new Date();  // Текущая дата

        // Перебираем недели и формируем массив для Spinner
        for (int i = 0; i < weeksArray.length(); i++) {
            JSONObject weekObject = weeksArray.getJSONObject(i);
            int weekNum = weekObject.getInt("num");
            String dateStartStr = weekObject.getString("date_start");
            String dateEndStr = weekObject.getString("date_end");

            Date dateStart = sdf.parse(dateStartStr);
            Date dateEnd = sdf.parse(dateEndStr);

            // Формируем строку для Spinner
            weekStrings[i] = "Неделя " + weekNum + " (" + dateStartStr + " - " + dateEndStr + ")";

            // Проверяем, попадает ли текущая дата в диапазон недели
            if (today.after(dateStart) && today.before(dateEnd)) {
                currentWeekIndex = i;  // Актуальная неделя
            }
        }

        // Обновляем адаптер Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weekStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Если найдена актуальная неделя, устанавливаем её как выбранную
        if (currentWeekIndex != -1) {
            spinner.setSelection(currentWeekIndex);
            weekNumber = String.valueOf(currentWeekIndex);
        }
    }
    private void highlightCurrentDayButton(String currentDay) {
        int color = ContextCompat.getColor(this, R.color.grey2);  // Цвет для покраски

        switch (currentDay) {
            case "пн":
                buttons[0].setBackgroundColor(color);
                break;
            case "вт":
                buttons[1].setBackgroundColor(color);
                break;
            case "ср":
                buttons[2].setBackgroundColor(color);
                break;
            case "чт":
                buttons[3].setBackgroundColor(color);
                break;
            case "пт":
                buttons[4].setBackgroundColor(color);
                break;
            case "сб":
                buttons[5].setBackgroundColor(color);
                break;
        }
    }

    public void onClickPrepod(View view) {
        if (view instanceof TextView) {
            String selectedGroup = getIntent().getStringExtra("selected_group");
            TextView textView = (TextView) view;
            String selectedPrepod = textView.getText().toString();

            Log.i(TAG, "Text: " + selectedPrepod);

            if (selectedPrepod != null && !selectedPrepod.trim().isEmpty()) {
                Intent intent = new Intent(ShowFriday.this, ShowSaturday.class);
                intent.putExtra("selected_prepod", selectedPrepod);
                intent.putExtra("selected_group", selectedGroup);
                startActivity(intent);
                finish();
            } else {
                Log.i(TAG, "No teacher selected, cannot proceed");
            }
        } else {
            Log.i(TAG, "The clicked view is not a TextView");
        }
    }


}
