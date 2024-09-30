package com.example.jammeren32344433;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;
import java.util.Calendar;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import androidx.core.content.ContextCompat;
import android.content.Context;


public class ShowMonday extends AppCompatActivity {

    private TextView[] paraNames = new TextView[5];
    private TextView[] paraPrepods = new TextView[5];
    private TextView[] paraMestos = new TextView[5];
    private Button[] buttons = new Button[6];

    private static final String DATABASE_NAME = "data.db";
    private static final String DATABASE_PATH = "/databases/";
    private static final String ASSETS_DB_PATH = "databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_monday);
//        initTextAndButtons();

//        Calendar calendar = Calendar.getInstance();
//        String dayOfWeek = getDayOfWeek(calendar);
//        printData(dayOfWeek);
//        highlightButton(dayOfWeek);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        copyDatabaseFromAssets();
    }

//    private void initTextAndButtons() {
//        int[] paraNamesIds = {R.id.para1_name, R.id.para2_name, R.id.para3_name, R.id.para4_name, R.id.para5_name};
//        int[] paraPrepodsIds = {R.id.para1_prepod, R.id.para2_prepod, R.id.para3_prepod, R.id.para4_prepod, R.id.para5_prepod};
//        int[] paraMestosIds = {R.id.para1_mesto, R.id.para2_mesto, R.id.para3_mesto, R.id.para4_mesto, R.id.para5_mesto};
//        int[] buttonIds = {R.id.button_monday, R.id.button_tuesday, R.id.button_wednesday, R.id.button_thursday, R.id.button_friday, R.id.button_saturday};
//
//        for (int i = 0; i < 5; i++) {
//            paraNames[i] = findViewById(paraNamesIds[i]);
//            paraPrepods[i] = findViewById(paraPrepodsIds[i]);
//            paraMestos[i] = findViewById(paraMestosIds[i]);
//            paraNames[i].setText("Нету");
//            paraPrepods[i].setText("Нету");
//            paraMestos[i].setText("Нету");
//        }
//
//        for (int i = 0; i < buttons.length; i++) {
//            buttons[i] = findViewById(buttonIds[i]);
//        }
//    }
//
//    private void printData(String dayOfWeek) {
//        initTextAndButtons();
//        SQLiteDatabase database = openDatabase();
//        if (database == null) return;
//
//        String query = "SELECT para_number, vrema_pari, chet_week_class, ne4et_week_class, chet_prepod_name, " +
//                "ne4et_prepod_name, chet_korpus, ne4et_korpus, chet_kabinet, ne4et_kabinet FROM schedule WHERE day_of_week = ?";
//        Cursor cursor = database.rawQuery(query, new String[]{dayOfWeek});
//
//        if (cursor.moveToFirst()) {
//            do {
//                int paraNumber = cursor.getInt(0) - 1;
//                String classText = formatText(cursor.getString(2), cursor.getString(3));
//                String prepodText = formatText(cursor.getString(4), cursor.getString(5));
//                String mestoText = formatText(cursor.getString(6), cursor.getString(7)) + ", " + formatText(cursor.getString(8), cursor.getString(9));
//
//                if (paraNumber >= 0 && paraNumber < 5) {
//                    paraNames[paraNumber].setText(classText);
//                    paraPrepods[paraNumber].setText(prepodText);
//                    paraMestos[paraNumber].setText(mestoText);
//                }
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        database.close();
//    }
//
//    private String formatText(String chet, String nechet) {
//        return chet.equals(nechet) ? chet : chet + " / " + nechet;
//    }
//
//    public void onButtonClick(View view) {
//        resetButtonColors();
//        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.grey2));
//
//        String dayOfWeek = getButtonDay(view.getId());
//        if (!dayOfWeek.isEmpty()) {
//            printData(dayOfWeek);
//        }
//    }
//
//    private String getButtonDay(int buttonId) {
//        if (buttonId == R.id.button_monday) {
//            return "Понедельник";
//        } else if (buttonId == R.id.button_tuesday) {
//            return "Вторник";
//        } else if (buttonId == R.id.button_wednesday) {
//            return "Среда";
//        } else if (buttonId == R.id.button_thursday) {
//            return "Четверг";
//        } else if (buttonId == R.id.button_friday) {
//            return "Пятница";
//        } else if (buttonId == R.id.button_saturday) {
//            return "Суббота";
//        } else {
//            return "";
//        }
//    }
//
//    private void resetButtonColors() {
//        for (Button button : buttons) {
//            button.setBackgroundColor(Color.TRANSPARENT);
//        }
//    }
//
//    private void copyDatabaseFromAssets() {
//        File dbFile = getDatabasePath(DATABASE_NAME);
//        if (!dbFile.exists()) {
//            try (InputStream inputStream = getAssets().open(ASSETS_DB_PATH + DATABASE_NAME);
//                 OutputStream outputStream = new FileOutputStream(dbFile)) {
//
//                byte[] buffer = new byte[1024];
//                int length;
//                while ((length = inputStream.read(buffer)) > 0) {
//                    outputStream.write(buffer, 0, length);
//                }
//
//                outputStream.flush();
//                Log.d("DatabaseCopy", "База данных успешно скопирована.");
//            } catch (Exception e) {
//                Log.e("DatabaseCopyError", "Ошибка копирования базы данных: " + e.getMessage());
//            }
//        }
//    }
//
//    private SQLiteDatabase openDatabase() {
//        File dbFile = getDatabasePath(DATABASE_NAME);
//        if (dbFile.exists()) {
//            try {
//                return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
//            } catch (SQLiteException e) {
//                Log.e("DatabaseOpenError", "Ошибка открытия базы данных: " + e.getMessage());
//            }
//        }
//        Log.e("DatabaseOpenError", "Файл базы данных не найден.");
//        return null;
//    }
//
//    private String getDayOfWeek(Calendar calendar) {
//        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
//        boolean after1715 = (calendar.get(Calendar.HOUR_OF_DAY) > 17) ||
//                (calendar.get(Calendar.HOUR_OF_DAY) == 17 && calendar.get(Calendar.MINUTE) > 15);
//
//        switch (currentDay) {
//            case Calendar.MONDAY: return after1715 ? "Вторник" : "Понедельник";
//            case Calendar.TUESDAY: return after1715 ? "Среда" : "Вторник";
//            case Calendar.WEDNESDAY: return after1715 ? "Четверг" : "Среда";
//            case Calendar.THURSDAY: return after1715 ? "Пятница" : "Четверг";
//            case Calendar.FRIDAY: return after1715 ? "Суббота" : "Пятница";
//            case Calendar.SATURDAY: return "Суббота";
//            default: return "Понедельник";
//        }
//    }
//
//    private int getColorFromResources(Context context, int colorResId) {
//        return ContextCompat.getColor(context, colorResId);
//    }
//
//    private void highlightButton(String dayOfWeek) {
//        resetButtonColors();
//
//        // Получаем цвет из ресурсов
//        int highlightColor = getColorFromResources(buttons[0].getContext(), R.color.grey2);
//
//        switch (dayOfWeek) {
//            case "Понедельник": buttons[0].setBackgroundColor(highlightColor); break;
//            case "Вторник": buttons[1].setBackgroundColor(highlightColor); break;
//            case "Среда": buttons[2].setBackgroundColor(highlightColor); break;
//            case "Четверг": buttons[3].setBackgroundColor(highlightColor); break;
//            case "Пятница": buttons[4].setBackgroundColor(highlightColor); break;
//            case "Суббота": buttons[5].setBackgroundColor(highlightColor); break;
//        }
//    }
}
