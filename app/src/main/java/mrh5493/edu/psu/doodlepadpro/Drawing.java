package mrh5493.edu.psu.doodlepadpro;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

import mrh5493.edu.psu.database.DoodleContentProvider;
import mrh5493.edu.psu.database.DoodleContract;

public class Drawing extends AppCompatActivity {

    String Title;
    String Description;
    Boolean save = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Title = intent.getStringExtra("title");
        Description = intent.getStringExtra("desc");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.drawing_save) {
            //Save Layout Here
            View save = findViewById(R.id.drawingSave);
            assert save != null;
            Bitmap bitmap = Bitmap.createBitmap(save.getWidth(), save.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable bgDrawable = save.getBackground();
            if (bgDrawable != null)
                bgDrawable.draw(canvas);
            else
                canvas.drawColor(Color.WHITE);
            save.draw(canvas);
            new saveImageAsynceTask().execute(bitmap);
            // done
        }
        return super.onOptionsItemSelected(item);
    }

    private class saveImageAsynceTask extends AsyncTask<Bitmap, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Bitmap... params) {

            //See if can First
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                return false;
            }

            Bitmap bitmapImage = params[0];

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("Doodles", Context.MODE_PRIVATE);
            File mypath = new File(directory, Title + ".jpg");
            FileOutputStream fos = null;
            try {
                // fos = openFileOutput(filename, Context.MODE_PRIVATE);

                fos = new FileOutputStream(mypath);

                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

                if (!save) {
                    //Save to Database
                    ContentValues values = new ContentValues();
                    values.put(DoodleContract.DoodleTable.DOODLETITLE, Title);
                    values.put(DoodleContract.DoodleTable.DOODLEDESCRIPTION, Description);
                    values.put(DoodleContract.DoodleTable.DOODLEFILEPATH, mypath.getAbsolutePath());

                    Uri uri = getContentResolver().insert(DoodleContentProvider.CONTENT_URI, values);
                    save = true;
                }

                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean results) {
            super.onPostExecute(results);
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.drawingCoordinatorLayout);
            Snackbar snackbar = null;
            assert coordinatorLayout != null;

            if (results)
                snackbar = Snackbar.make(coordinatorLayout, "Saved", Snackbar.LENGTH_LONG);

            else
                snackbar = Snackbar.make(coordinatorLayout, "Can't Save", Snackbar.LENGTH_LONG);

            snackbar.show();

        }
    }
}
