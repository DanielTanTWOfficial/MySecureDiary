package example.com.mysecurediary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private ArrayList<UserModel> userArr = new ArrayList<>();
    private EditText username;
    private EditText password;
    private String user;
    private String pass;
    private byte[] salt1;
    private String cancel = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Login.JSONTask().execute("http://192.168.1.6/Connect/userInfo.php");

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.passwordConfirm);
    }

    public void goDashboard(View v){
        user = username.getText().toString();
        pass = password.getText().toString();

        if(user.equals("")){
            username.setError("Username field is empty!");
            cancel = "true";
        }
        else if(pass.equals("")){
            password.setError("Password is required!");
            cancel = "true";
        }

        if(!cancel.equals("true")) {
            for (int i = 0; i < userArr.size(); i++) {

                String passHash = hashPass(pass, userArr.get(i).getSalt());

                if (user.equals(userArr.get(i).getUsername()) && passHash.equals(userArr.get(i).getPassword())) {

                    Intent homePage = new Intent(this, Dashboard.class);
                    startActivity(homePage);
                    Dashboard.username = user;
                    //finish();
                }
            }
        }
        cancel = "false";
    }

    public void goPassReset(View v){
        user = username.getText().toString();
        pass = password.getText().toString();

        if(user.equals("")){
            username.setError("Username field is empty!");
            cancel = "true";
        }
        else if(pass.equals("")){
            password.setError("Password is required!");
            cancel = "true";
        }

        if(!cancel.equals("true")) {
            for (int i = 0; i < userArr.size(); i++) {
                if (user.equals(userArr.get(i).getUsername())) {
                    String passHash = hashPass(pass, userArr.get(i).getSalt());

                    if(passHash.equals(userArr.get(i).getPassword())) {
                        Intent passPage = new Intent(this, PasswordReset.class);
                        startActivity(passPage);
                        PasswordReset.username = user;
                        //finish();
                    }
                }
            }
        }
    }

    public void goSignUp(View v){
        Signup.userArr = userArr;
        Intent signPage = new Intent(this, Signup.class);
        startActivity(signPage);
    }

    public String hashPass(String pass, String salt){
        HashData hd = new HashData();

        String passHash = "";

        passHash = hd.SHA_512Hash(pass, salt);

        return passHash;
    }

    public class JSONTask extends AsyncTask<String, String, ArrayList<UserModel>> {
        @Override
        protected ArrayList<UserModel> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObj = new JSONObject(finalJson);
                JSONArray parentArray = parentObj.getJSONArray("users");

                StringBuffer finalBufferedData = new StringBuffer();

                for(int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObj = parentArray.getJSONObject(i);

                    UserModel fm = new UserModel(finalObj.getString("username"), finalObj.getString("password"), finalObj.getString("salt"));
                    userArr.add(fm);
                }

                return userArr;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<UserModel> result) {
            super.onPostExecute(result);

            if(!userArr.isEmpty()) {
                userArr = result;
            }
        }
    }
}
