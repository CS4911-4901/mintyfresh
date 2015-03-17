package edu.gatech.cs4911.mintyfresh.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.SQLException;

import edu.gatech.cs4911.mintyfresh.R;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class ImageCacheViewTestActivity extends ActionBarActivity {
    DBHandler handler;
    ImageCache cache;
    FloorplanMeta testMeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testMeta = new FloorplanMeta("CUL", 1);

        try {
            // Moment of truth!
            handler = new NetIoTask().execute("").get();
            cache = new ImageCache(handler, getApplicationContext());

            String successState = new CacheUpdaterTask().execute(cache).get();
            System.console(); // BREAK
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NetIoTask extends AsyncTask<String, Void, DBHandler> {
        protected DBHandler doInBackground(String... handler) {
            try {
                return new DBHandler(STEAKSCORP_READ_ONLY);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private class CacheUpdaterTask extends AsyncTask<ImageCache, Void, String> {
        protected String doInBackground(ImageCache... cache) {
            try {
                cache[0].update();
            } catch (Exception e) {
                return e.toString();
            } return "SUCCESS";
        }
    }
}
