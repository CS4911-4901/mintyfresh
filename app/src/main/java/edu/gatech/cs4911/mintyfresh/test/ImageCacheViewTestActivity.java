package edu.gatech.cs4911.mintyfresh.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVG;

import edu.gatech.cs4911.mintyfresh.R;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class ImageCacheViewTestActivity extends ActionBarActivity {
    DBHandler handler;
    ImageCache cache;
    FloorplanMeta testMeta;
    ImageView testImageViewOutput;
    LinearLayout layout;
    SVGImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cache_view_test);
        testMeta = new FloorplanMeta("CUL", 1);
        testImageViewOutput = (ImageView) findViewById(R.id.imageCacheViewTestImage);
        layout = new LinearLayout(this);
        imageView = new SVGImageView(this);

        try {
            // Moment of truth!
            handler = new NetIoTask().execute("").get();
            cache = new ImageCache(handler, getApplicationContext());
            new CacheUpdaterTask().execute(cache);

            SVG result = new ImageUpdaterTask().execute("CUL_1.svg").get();
            imageView.setSVG(result);
            layout.addView(imageView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            setContentView(layout);

        } catch (Exception e) {
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_cache_view_test, menu);
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

    private class ImageUpdaterTask extends AsyncTask<String, Void, SVG> {
        protected SVG doInBackground(String... filename) {
            try {
                SVG result = SVG.getFromInputStream(getApplicationContext()
                        .openFileInput(filename[0]));

                return result;

            } catch (Exception e) {
                return null;
            }
        }
    }
}
