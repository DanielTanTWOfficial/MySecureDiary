package example.com.mysecurediary;

import android.annotation.TargetApi;
import android.content.Intent;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private TextView Date;
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Date = (TextView) findViewById(R.id.Date);

        Thread t = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy\nhh:mm:ss a");
                                String dateString = sdf.format(date);
                                Date.setText(dateString);
                            }
                        });
                    }
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void makeEntry(View v){
        NewEntry.username = username;
        Intent EntryPage = new Intent(this, NewEntry.class);
        startActivity(EntryPage);
    }

    public void pastEntries(View v){
        Intent prevEntryPage = new Intent(this, PreviousEntries.class);
        startActivity(prevEntryPage);
        PreviousEntries.username = this.username;
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

}
