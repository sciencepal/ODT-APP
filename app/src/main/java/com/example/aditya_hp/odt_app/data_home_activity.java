package com.example.aditya_hp.odt_app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class data_home_activity extends AppCompatActivity{

    Button search_erc;
    Button go;
    EditText erc_box;
    EditText model_box;
    Button logout;
    Button manual;
    Context context = this;
    int num_choices;
    String options[];
    ArrayAdapter<String> dataAdapter;
    String uname;
    String uid;
    int test_type;
    String email;
    TextView name_display;
    TextView email_display;
    TextView erc_selected;
    TextView model_selected;
    TextView test_selected;
    TextView erc1;
    TextView model1;
    TextView test1;
    String ttv_id[];
    String ttv_erc_no[];
    String ttv_model_name[];
    String ttv_test_type[];
    int selected = -1;

    @Override
    public void onResume()
    {
        Intent intent = this.getIntent();
        try {
            uname = intent.getExtras().getString("uname");
            try {
                uid = intent.getExtras().getString("uid");
            } catch (Exception e) {
                uid = "3";
            }
            email = intent.getExtras().getString("email");
            name_display.setText(uname);
            email_display.setText(email);
        }
        catch(Exception e) {
            Toast.makeText(context, " You have been logged out. ", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(context, login_activity.class);
            startActivity(intent1);
        }
        try {
            String p = intent.getExtras().getString("login_page");
        }
        catch(Exception e){}
        super.onResume();
        selected = -1;
        model_selected.setVisibility(View.GONE);
        erc_selected.setVisibility(View.GONE);
        test_selected.setVisibility(View.GONE);
        model1.setVisibility(View.GONE);
        erc1.setVisibility(View.GONE);
        test1.setVisibility(View.GONE);
        go.setVisibility(View.GONE);
        erc_box.setText("");
        model_box.setText("");
        try
        {
            selected = Integer.parseInt(intent.getExtras().getString("selected"));
        }
        catch(Exception e)
        {}
        new spinner_choices().execute("http://117.218.7.217/odt/webservices/odt.php?ajax=getTestVehicles");
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_home_layout);


        search_erc = (Button)(findViewById(R.id.search_erc));
        go = (Button)(findViewById(R.id.go));
        go.setBackgroundColor(Color.GREEN);
        erc_box = (EditText) (findViewById(R.id.erc_box));
        model_box = (EditText)(findViewById(R.id.model_box));
        logout = (Button) findViewById(R.id.logout);
        manual = (Button) findViewById(R.id.manual);
        model_box.setInputType(InputType.TYPE_NULL);
        name_display = (TextView)(findViewById(R.id.name_display));
        email_display = (TextView)(findViewById(R.id.email_display));
        model_selected = (TextView)(findViewById(R.id.model_selected));
        erc_selected = (TextView)(findViewById(R.id.erc_selected));
        test_selected = (TextView)(findViewById(R.id.test_selected));
        model1 = (TextView)(findViewById(R.id.model1));
        erc1 = (TextView)(findViewById(R.id.erc1));
        test1 = (TextView)(findViewById(R.id.test1));
        model_selected.setVisibility(View.GONE);
        erc_selected.setVisibility(View.GONE);
        test_selected.setVisibility(View.GONE);
        model1.setVisibility(View.GONE);
        erc1.setVisibility(View.GONE);
        test1.setVisibility(View.GONE);
        go.setVisibility(View.GONE);

        onResume();
        search_erc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String entered_erc = erc_box.getText().toString();
                if (entered_erc.equals(""))
                {
                    Toast.makeText(context, "Please input ERC Number ", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    String u = "http://117.218.7.217/odt/webservices/odt.php?ajax=getTestVehicles&ercno=";
                    u += entered_erc;
                    new data_home_activity.netconnected().execute(u);
                }
            }
        });

        model_box.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                hideSoftKeyboard(data_home_activity.this);
                new AlertDialog.Builder(context).setTitle("Select Model Number")
                        .setAdapter(dataAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                model_box.setText(options[which].toString());
                                model1.setText("Model Name Selected : ");
                                erc1.setText("ERC No. Selected : ");
                                test1.setText("Test Type : ");
                                erc_selected.setText(ttv_erc_no[which]);
                                model_selected.setText(ttv_model_name[which]);
                                test_selected.setText(ttv_test_type[which]);
                                erc_selected.setVisibility(View.VISIBLE);
                                model_selected.setVisibility(View.VISIBLE);
                                test_selected.setVisibility(View.VISIBLE);
                                model1.setVisibility(View.VISIBLE);
                                erc1.setVisibility(View.VISIBLE);
                                test1.setVisibility(View.VISIBLE);
                                go.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                                selected = which;
                            }
                        }).create().show();
            }
        });


        go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                    if (selected > -1)
                    {
                        Intent intent = new Intent(context, highway_activity.class);
                        if (ttv_test_type[selected].equals("Torture"))
                            intent = new Intent(context, torture_activity.class);
                        else if (ttv_test_type[selected].equals("Offroad"))
                            intent = new Intent(context, offroad_activity.class);
                        intent.putExtra("ttv_id", ttv_id[selected]);
                        intent.putExtra("ttv_erc_no", ttv_erc_no[selected]);
                        intent.putExtra("ttv_model_name", ttv_model_name[selected]);
                        intent.putExtra("ttv_test_type", ttv_test_type[selected]);
                        intent.putExtra("uname", uname);
                        intent.putExtra("uid", uid);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(context, "Please Enter Correct ERC Number", Toast.LENGTH_LONG).show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    try {
                        Intent intent = new Intent(context, login_activity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        });

        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    try {
                        Intent intent = new Intent(context, manual_activity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("uname", uname);
                        intent.putExtra("email", email);
                        intent.putExtra("selected", Integer.toString(selected));
                        intent.putExtra("erc_number", erc_box.getText().toString());
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


        });


    }




    private class netconnected extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... u)
        {
            URL url = null;
            try
            {
                url = new URL(u[0]);
            }
            catch(MalformedURLException e)
            {
                return "E";
            }
            HttpURLConnection conn = null;
            try
            {
                conn = (HttpURLConnection)url.openConnection();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "E";
            }
            try
            {
                InputStreamReader in = new InputStreamReader(url.openStream());
                BufferedReader read = new BufferedReader(in);
                String line = read.readLine();
                return line;
            }
            catch (Exception e)
            {
                return "E";
            }
        }

        protected void onPostExecute(String res)
        {
            if (res == "E") {
                Toast.makeText(context, " Net Not Connected ", Toast.LENGTH_LONG).show();
            }
            else{
                if (res.equals("[]"))
                {
                    Toast.makeText(context, " Incorrect ERC Number ", Toast.LENGTH_LONG).show();
                    erc_box.setTextColor(Color.RED);
                }
                else {
//                    DONE NOT THIS PLEASE
                    res = res.replace("[", "");
                    res = res.replace("]", "");
                    res = res.replace("{", "");
                    res = res.replace("\"", "");
                    res = res.replace("}", "");
                    res = res.replace("&amp;", "&");
                    res = res.replace("&nbsp;", " ");
                    res = res.replace("&copy;", "c");
                    res = res.replace("&reg;", "r");
                    String[] params = res.split(",");
                    for (int i = 0; i < params.length; i++)
                    {
                        String[] temp = params[i].split(":");
                        if (temp[0].equals("ttv_test_type")) {
                            try{
                                test_type = Integer.parseInt(temp[1].substring(2, 3));
                            }
                            catch (Exception e)
                            {
                                test_type = 1;
                            }
                        }
                    }
                    Intent intent = new Intent(context, highway_activity.class);
                    if (test_type == 2)
                        intent = new Intent(context, torture_activity.class);
                    else if (test_type == 3)
                        intent = new Intent(context, offroad_activity.class);
                    params = res.split(",");
                    for (int i = 0; i < params.length; i++)
                    {
                        String[] temp = params[i].split(":");
                        intent.putExtra(temp[0], temp[1]);
                    }
                    intent.putExtra("uname", uname);
                    intent.putExtra("uid", uid);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        }

    }



    private class spinner_choices extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... u)
        {
            URL url = null;
            try
            {
                url = new URL(u[0]);
            }
            catch(MalformedURLException e)
            {
                return "E";
            }
            HttpURLConnection conn = null;
            try
            {
                conn = (HttpURLConnection)url.openConnection();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "E";
            }
            try
            {
                InputStreamReader in = new InputStreamReader(url.openStream());
                BufferedReader read = new BufferedReader(in);
                String line = read.readLine(), line1 = "";
                for (;;line1 = read.readLine()) {
                    if (line.contains("]"))
                        return line;
                    line += line1;
                }
            }
            catch (Exception e)
            {
                return "E";
            }
        }

        protected void onPostExecute(String res)
        {
            if (res == "E") {
                Toast.makeText(context, " Net Not Connected ", Toast.LENGTH_LONG).show();
            }
            else{
                if (res.equals("[]"))
                {
                    Toast.makeText(context, " Incorrect ERC Number ", Toast.LENGTH_LONG).show();
                    erc_box.setTextColor(Color.RED);
                }
                else {
                    res = res.replace("[", "");
                    res = res.replace("]", "");
                    res = res.replace("\"", "");
                    res = res.replace("&amp;", "&");
                    res = res.replace("&nbsp;", " ");
                    res = res.replace("&copy;", "c");
                    res = res.replace("&reg;", "r");
                    res = res.substring(1, res.length() - 1);
                    String[] params = res.split("\\},\\{");
                    try {
                        options = new String[params.length];
                        ttv_id = new String[params.length];
                        ttv_erc_no = new String[params.length];
                        ttv_model_name = new String[params.length];
                        ttv_test_type = new String[params.length];
                        num_choices = params.length;
                        for (int i = 0; i < params.length; i++) {
                            String[] temp = params[i].split(",");
                            for (int j = 0; j < temp.length; j++) {
                                String[] temp1 = temp[j].split(":");
                                if (temp1[0].equals("ttv_id"))
                                    ttv_id[i] = temp1[1];
                                else if (temp1[0].equals("ttv_erc_no"))
                                    ttv_erc_no[i] = temp1[1];
                                else if (temp1[0].equals("ttv_model_name"))
                                    ttv_model_name[i] = temp1[1];
                                else {
                                    String op = temp1[1].substring(2, 3);
                                    if (op.equals("1"))
                                        ttv_test_type[i] = "Highway";
                                    else if (op.equals("2"))
                                        ttv_test_type[i] = "Torture";
                                    else
                                        ttv_test_type[i] = "Offroad";
                                }
                            }
                            options[i] = ttv_erc_no[i] + " - " + ttv_model_name[i] + " - " + ttv_test_type[i];
                        }
                        dataAdapter = new ArrayAdapter<String>(context, R.layout.spin, options);
                    }
                    catch(Exception e){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    if (selected > -1){
                        model_box.setText(options[selected].toString());
                        model1.setText("Model Name Selected : ");
                        erc1.setText("ERC No. Selected : ");
                        test1.setText("Test Type : ");
                        erc_selected.setText(ttv_erc_no[selected]);
                        model_selected.setText(ttv_model_name[selected]);
                        test_selected.setText(ttv_test_type[selected]);
                        erc_selected.setVisibility(View.VISIBLE);
                        model_selected.setVisibility(View.VISIBLE);
                        test_selected.setVisibility(View.VISIBLE);
                        model1.setVisibility(View.VISIBLE);
                        erc1.setVisibility(View.VISIBLE);
                        test1.setVisibility(View.VISIBLE);
                        go.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }
}