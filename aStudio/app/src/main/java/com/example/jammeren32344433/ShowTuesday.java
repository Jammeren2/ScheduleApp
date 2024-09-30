package com.example.jammeren32344433;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;

public class ShowTuesday extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_tuesday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onButtonClick(View view) {
        int buttonId = view.getId();
        Intent intent = null;

        if (buttonId == R.id.button_monday) {
            intent = new Intent(this, ShowMonday.class);
        } else if (buttonId == R.id.button_tuesday) {
            intent = new Intent(this, ShowTuesday.class);
        }else if (buttonId == R.id.button_wednesday) {
            intent = new Intent(this, ShowWednesday.class);
        }else if (buttonId == R.id.button_thursday) {
            intent = new Intent(this, ShowThursday.class);
        }else if (buttonId == R.id.button_friday) {
            intent = new Intent(this, ShowFriday.class);
        }else if (buttonId == R.id.button_saturday) {
            intent = new Intent(this, ShowSaturday.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}