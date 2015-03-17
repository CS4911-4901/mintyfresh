package edu.gatech.cs4911.mintyfresh.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.test.InstrumentationTestCase;
import android.view.Menu;
import android.view.MenuItem;

import edu.gatech.cs4911.mintyfresh.R;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class ImageCacheTest extends InstrumentationTestCase {
    DBHandler handler;
    Activity activity;
    ImageCache cache;
    FloorplanMeta testMeta;

    public void setUp() throws Exception {
        super.setUp();

        handler = new DBHandler(STEAKSCORP_READ_ONLY);
        activity = new ImageCacheTestActivity();
        testMeta = new FloorplanMeta("CUL", 1);
    }

    public void tearDown() throws Exception {
    }

    public void testInstantiation() throws Exception {
        cache = new ImageCache(handler, activity.getApplicationContext());
        assertNotNull(cache);
    }

    public void testUpdate() throws Exception {
        cache.update();

        assertTrue(cache.size() > 0);
        assertTrue(cache.contains(testMeta));
    }

    private class ImageCacheTestActivity extends ActionBarActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home_screen);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
    }
}