package example.com.mysecurediary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class PreviousEntries extends AppCompatActivity {
    private ArrayList<EntryModel> entryArr = new ArrayList<>();
    private EntryModel[] listEntries;
    private ArrayAdapter<EntryModel> adapter;
    private Context context = this;
    public static String username;
    private ListView prevList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_entries);

        new PreviousEntries.JSONTask().execute("http://192.168.1.6/Connect/diaryEntry.php");
    }


    public class JSONTask extends AsyncTask<String, String, ArrayList<EntryModel>> {
        @Override
        protected ArrayList<EntryModel> doInBackground(String... params) {
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
                JSONArray parentArray = parentObj.getJSONArray("entries");

                StringBuffer finalBufferedData = new StringBuffer();

                for(int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObj = parentArray.getJSONObject(i);

                    EntryModel fm = new EntryModel(finalObj.getString("user"), finalObj.getString("datee"), finalObj.getString("content"));

                    if(username.equals(finalObj.getString("user"))) {
                            entryArr.add(fm);
                    }
                }

                return entryArr;

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
        protected void onPostExecute(ArrayList<EntryModel> result) {
            super.onPostExecute(result);

            if(!result.isEmpty()) {

                prevList = (ListView) findViewById(R.id.prevList);
                listEntries = new EntryModel[result.size()];

                for (int i = 0; i < result.size(); i++) {
                    listEntries[i] = result.get(i);
                }

                adapter = new ArrayAdapter<>(context, R.layout.list_text, listEntries);

                prevList.setAdapter(adapter);
            }
        }
    }
}
