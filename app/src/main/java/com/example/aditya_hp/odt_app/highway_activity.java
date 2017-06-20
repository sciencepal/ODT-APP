package com.example.aditya_hp.odt_app;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class highway_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView date;
    EditText odo_start;
    EditText odo_end;
    TextView kilo;
    EditText fuel;
    EditText urea;
    EditText odoatfuel;
    EditText remarks;
    EditText issuelog;
    Button update;
    Button cancel;
    Button add_issue;
    Context context = this;
    DatePickerDialog dpd;
    LinearLayout container;
    String issue_list[];
    String uname;
    String uid;
    String ttv_id;
    String ttv_test_type;
    String ttv_erc_no;
    String ttv_model_name;
    String email;
    TextView name_display;
    TextView email_display;
    TextView highway;
    TextView model_name;
    Application appa = this.getApplication();
    boolean done = true;


    @Override
    public void onResume()
    {
        try {
            Intent intent = this.getIntent();
            uname = intent.getExtras().getString("uname");
            try {
                uid = intent.getExtras().getString("uid");
            } catch (Exception e) {
                uid = "3";
            }
            email = intent.getExtras().getString("email");
            ttv_id = intent.getExtras().getString("ttv_id");
            ttv_erc_no = intent.getExtras().getString("ttv_erc_no");
            ttv_model_name = intent.getExtras().getString("ttv_model_name");
            ttv_test_type = intent.getExtras().getString("ttv_test_type");
            name_display.setText(uname);
            email_display.setText(email);
            model_name.setText(ttv_model_name);
        }
        catch(Exception e) {
            Toast.makeText(context, " You have been logged out. ", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, login_activity.class);
            startActivity(intent);
        }
        super.onResume();
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highway_layout);

        date = (TextView) findViewById(R.id.date);
        odo_start = (EditText)findViewById(R.id.odo_start);
        odo_end = (EditText)findViewById(R.id.odo_end);
        fuel = (EditText)findViewById(R.id.fuel);
        urea = (EditText)findViewById(R.id.urea);
        odoatfuel = (EditText)findViewById(R.id.odoatfuel);
        remarks = (EditText)findViewById(R.id.remarks);
        issuelog = (EditText)findViewById(R.id.issuelog);
        kilo = (TextView) findViewById(R.id.kilo);
        add_issue = (Button)findViewById(R.id.add_issue);
        container = (LinearLayout) findViewById(R.id.container);
        name_display = (TextView) findViewById(R.id.name_display);
        email_display = (TextView) findViewById(R.id.email_display);
        highway = (TextView) findViewById(R.id.highway);
        model_name = (TextView) findViewById(R.id.model_name);
        LayoutTransition transition = new LayoutTransition();
        container.setLayoutTransition(transition);




        onResume();
        highway.setText("Highway Testing of ERC No. " + ttv_erc_no);
        Calendar cal;
        int year = 2017;
        int month = 4;
        int dayOfMonth = 27;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cal = Calendar.getInstance();
            year = cal.getInstance().get(Calendar.YEAR);
            month = cal.getInstance().get(Calendar.MONTH);
            dayOfMonth = cal.getInstance().get(Calendar.DATE);
        }
        dpd = new DatePickerDialog(context, highway_activity.this, year, month, dayOfMonth);
        String calen = Integer.toString(dayOfMonth) + "-" + Integer.toString(month+1) + "-" + Integer.toString(year);
        date.setText(calen);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(highway_activity.this);
                dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpd.show();
            }
        });

        kilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    double km1 = Double.parseDouble(odo_start.getText().toString());
                    double km2 = Double.parseDouble(odo_end.getText().toString());
                    kilo.setText(Double.toString(km2 - km1));
                }
                catch(Exception e)
                {
                    String s = "Please Enter proper values of start Km and end Km";
                    Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                }
            }
        });


        add_issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.row, null);
                final TextView new_issue = (TextView)addView.findViewById(R.id.new_issue);
                String log = issuelog.getText().toString();
                if (log.equals(""))
                    Toast.makeText(context, "Please Write Previous Issue Log Before Adding New Issue", Toast.LENGTH_LONG).show();
                else {
                    new_issue.setText(log);
                    Button remove = (Button) addView.findViewById(R.id.remove);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) addView.getParent()).removeView(addView);
                        }
                    });
                    container.addView(addView, 0);
                    issuelog.setText("");
                }
            }
        });

        update = (Button)findViewById(R.id.update);
        update.setBackgroundColor(Color.GREEN);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean total_done = true;
                try{
                    double x = Double.parseDouble(odo_start.getText().toString());
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Incorrect Data Format !")
                            .setMessage("Odometer Start must have decimal value").show();
                    return;
                }
                try{
                    double x = Double.parseDouble(odo_end.getText().toString());
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Incorrect Data Format !")
                            .setMessage("Odometer End must have decimal value").show();
                    return;
                }
                double km1 = Double.parseDouble(odo_start.getText().toString());
                double km2 = Double.parseDouble(odo_end.getText().toString());
                kilo.setText(Double.toString(km2 - km1));
                try{
                    double x = Double.parseDouble(fuel.getText().toString());
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Incorrect Data Format !")
                            .setMessage("Fuel must have decimal value").show();
                    return;
                }
                try{
                    double x = Double.parseDouble(urea.getText().toString());
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Incorrect Data Format !")
                            .setMessage("Urea must have decimal value").show();
                    return;
                }
                try{
                    double x = Double.parseDouble(odoatfuel.getText().toString());
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Incorrect Data Format !")
                            .setMessage("Odometer@Fuel must have decimal value").show();
                    return;
                }
                String url = "http://117.218.7.217/odt/webservices/odt.php?ajax=saveDailyData";
                String[] date_format = (date.getText().toString()).split("-");
                if (date_format[0].length() == 1)
                    date_format[0] = "0" + date_format[0];
                if (date_format[1].length() == 1)
                    date_format[1] = "0" + date_format[1];
                try
                {
                    url += "&";
                    url += URLEncoder.encode("testID", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(ttv_id, "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("testDT", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(date_format[2] + "/" + date_format[1] + "/" + date_format[0], "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("userID", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(uid, "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("remarks", "UTF-8");
                    url += "=";
                    String res = remarks.getText().toString();
                    url += URLEncoder.encode(res, "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("odmstart", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(odo_start.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("odmend", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(odo_end.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("odmfuel", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(odoatfuel.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("km", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(kilo.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("fuel", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(fuel.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("uera", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode(urea.getText().toString(), "UTF-8");
                    url += "&";
                    url += URLEncoder.encode("testtype", "UTF-8");
                    url += "=";
                    url += URLEncoder.encode("TT1", "UTF-8");
                    new netconnected().execute(url);
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Error in Insertion !")
                            .setMessage(e.getMessage()).show();
                }
                total_done = total_done && done;
                String url1 = "http://117.218.7.217/odt/webservices/odt.php?ajax=saveDailyIssue";
                try {
                    url1 += "&";
                    url1 += URLEncoder.encode("testID", "UTF-8");
                    url1 += "=";
                    url1 += URLEncoder.encode(ttv_id, "UTF-8");
                    url1 += "&";
                    url1 += URLEncoder.encode("testDT", "UTF-8");
                    url1 += "=";
                    url1 += URLEncoder.encode(date_format[2] + "/" + date_format[1] + "/" + date_format[0], "UTF-8");
                    url1 += "&";
                    url1 += URLEncoder.encode("userID", "UTF-8");
                    url1 += "=";
                    url1 += URLEncoder.encode(uid, "UTF-8");
                    int num_issues = container.getChildCount();
                    url1 += "&";
                    url1 += URLEncoder.encode("remarks", "UTF-8");
                    url1 += "=";
                    for (int i = 0; i < num_issues; i++) {
                        String temp = url1;
                        View issue = container.getChildAt(i);
                        TextView issue_text = (TextView) (issue.findViewById(R.id.new_issue));
                        String issue_list = issue_text.getText().toString();
                        temp += issue_list;
                        url1 += "&";
                        url1 += URLEncoder.encode("srlno", "UTF-8");
                        url1 += "=";
                        url1 += Integer.toString(i+1);
                        temp += "&";
                        temp += URLEncoder.encode("testtype", "UTF-8");
                        temp += "=";
                        temp += URLEncoder.encode("TT1", "UTF-8");
                        new netconnected1().execute(url1);
                        total_done = total_done && done;
                    }
                    if (total_done)
                    {
                        Toast.makeText(context, " Data Successfully Updated ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, data_home_activity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("uname", uname);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, " Data Not Updated. Please Check Network Connection ", Toast.LENGTH_LONG).show();
                    }
                }
                catch(Exception e)
                {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Error in Insertion !")
                            .setMessage(e.getMessage()).show();
                }

            }
        });


        cancel = (Button)findViewById(R.id.cancel);
        cancel.setBackgroundColor(Color.RED);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, data_home_activity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("uname", uname);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String calen = Integer.toString(dayOfMonth) + "-" + Integer.toString(month+1) + "-" + Integer.toString(year);
        date.setText(calen);
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
            if (res.equals("E")) {
                done = false;
            }
            else{
                done = true;
            }
        }

    }

    private class netconnected1 extends AsyncTask<String, Void, String> {
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
            if (res.equals("E")) {
                done = false;
            }
            else{
                done = true;
            }
        }

    }

    String convert(String res)
    {
        res = res.replace("\"", "&quot;");
        res = res.replace("&", "&amp;");
        res = res.replace(" ", "&nbsp;");
        res = res.replace("<", "&lt;");
        res = res.replace(">", "&gt;");
        res = res.replace("\'", "&#39;");
        return res;
    }

}



