package com.example.colortiles;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView twDark, twBright;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        twDark = findViewById(R.id.twDark);
        twBright = findViewById(R.id.twBright);
    }


    public void setTextViews(int darkCount) {
        twDark.setText(darkCount + " красных");
        twBright.setText((16 - darkCount) + " розовых");
        if (darkCount == 16 || darkCount == 0)
            Toast.makeText(this, "Вы выиграли", Toast.LENGTH_SHORT).show();
    }
}