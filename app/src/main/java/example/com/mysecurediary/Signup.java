package example.com.mysecurediary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    public static ArrayList<UserModel> userArr = new ArrayList<>();
    private EditText username;
    private EditText password;
    private EditText passwordConfirm;
    private String user;
    private String pass;
    private String passConfirm;
    private byte[] salt1;
    String exists = "false";
    String cancel = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirm);
    }

    public void goDashboard(View v){
        user = username.getText().toString();
        pass = password.getText().toString();
        passConfirm = passwordConfirm.getText().toString();

        if(user.equals("")){
            username.setError("Username field is empty!");
            cancel = "true";
        }
        else if(pass.equals("")){
            password.setError("Password is required!");
            cancel = "true";
        }
        else if(passConfirm.equals("")){
            passwordConfirm.setError("Please confirm your password!");
            cancel = "true";
        }
        else if(!(pass.equals(passConfirm))){
            passwordConfirm.setError("Passwords do not match!");
            cancel = "true";
        }

        if(!cancel.equals("true")) {
            for (int i = 0; i < userArr.size(); i++) {
                if (user.equals(userArr.get(i).getUsername())) {
                    exists = "true";
                }
            }

            if (!exists.equals("true")) {
                HashData hd = new HashData();
                GenerateSalt gs = new GenerateSalt();

                String passHash = "";

                salt1 = gs.getSalt();
                String salt = salt1.toString();

                passHash = hd.SHA_512Hash(pass, salt1.toString());

                new AsyncSignUp().execute(user, passHash, salt);
                Intent homePage = new Intent(this, Dashboard.class);
                startActivity(homePage);
                Dashboard.username = user;
                //finish();
            } else {
                username.setError("Username exists!");
            }
        }
        cancel = "false";

    }


    private class AsyncSignUp extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.1.6/Connect/createUser.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", params[0])
                        .appendQueryParameter("password", params[1])
                        .appendQueryParameter("salt", params[2]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }
    }
}
