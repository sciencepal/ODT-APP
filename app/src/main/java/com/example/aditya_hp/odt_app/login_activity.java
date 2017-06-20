package com.example.aditya_hp.odt_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class login_activity extends AppCompatActivity {


    Button login;
    EditText userid, pwd;
    Context context = login_activity.this;
    Bundle data = new Bundle();

    @Override
    public void onResume()
    {
        super.onResume();
        userid.setText("");
        pwd.setText("");
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
        setContentView(R.layout.login_layout);

        login = (Button)findViewById(R.id.login);
        userid = (EditText)findViewById(R.id.userid);
        pwd = (EditText)findViewById(R.id.pwd);
        onResume();



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection())
                {
                    String uid, pas;
                    uid = userid.getText().toString();
                    pas = pwd.getText().toString();
                    if (uid.equals("") || pas.equals("")) {
                        Toast.makeText(context, "Enter User ID and Password", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String u = "http://117.218.7.217/odt/webservices/login.php?";
                    u += "userid=";
                    u += uid;
                    u += "&pwd=";
                    u += pas;
                    new netconnected().execute(u);
                }
            }




            public boolean checkConnection(){
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
                else
                {
                    Toast.makeText(context, " Net Not Connected ", Toast.LENGTH_LONG).show();
                    return false;
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
            else {
                if (res.contains("Invalid Login Credential"))
                {
                    Toast.makeText(context, " Incorrect User ID or Password ", Toast.LENGTH_LONG).show();
                    pwd.setText("");
                    userid.setTextColor(Color.RED);
                }
                else {
                    res = res.replace("{", "");
                    res = res.replace("\"", "");
                    res = res.replace("}", "");
                    String[] params = res.split(",");
                    String uname="";
                    Intent intent = new Intent(context, data_home_activity.class);
                    for (int i = 1; i < params.length; i++)
                    {
                        String[] temp = params[i].split(":");
                        intent.putExtra(temp[0], temp[1]);
                        if (temp[0].equals("uname"))
                            uname = temp[1];
                    }
                    intent.putExtra("login_page", "Yes");
                    Toast.makeText(context, "Welcome, " + uname, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        }

    }

}
