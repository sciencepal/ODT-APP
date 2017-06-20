package com.example.aditya_hp.odt_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class manual_activity extends AppCompatActivity {

    Button back;
    Context context = this;
    Intent intent1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_layout);

        intent1 = this.getIntent();

        back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    try {
                        Intent intent = new Intent(context, data_home_activity.class);
                        intent.putExtra("uid", intent1.getExtras().getString("uid"));
                        intent.putExtra("uname", intent1.getExtras().getString("uname"));
                        intent.putExtra("email", intent1.getExtras().getString("email"));
                        intent.putExtra("selected", intent1.getExtras().getString("selected"));
                        intent.putExtra("erc_number", intent1.getExtras().getString("erc_number"));
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }
}