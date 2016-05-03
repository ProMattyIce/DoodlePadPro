package mrh5493.edu.psu.doodlepadpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

import mrh5493.edu.psu.database.DoodleContentProvider;
import mrh5493.edu.psu.database.DoodleContract;

public class Drawing extends AppCompatActivity implements OnClickListener {

    String Title;
    String Description;
    Boolean save = false;

    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Title = intent.getStringExtra("title");
        Description = intent.getStringExtra("desc");

        assert toolbar != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Title);

        drawView = (DrawingView) findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        assert paintLayout != null;
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        assert drawBtn != null;
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(mediumBrush);

        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        assert eraseBtn != null;
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton) findViewById(R.id.new_btn);
        assert newBtn != null;
        newBtn.setOnClickListener(this);

        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        assert saveBtn != null;
        saveBtn.setOnClickListener(this);

    }
//    public void paintClicked(View view) {
//
//        drawView.setErase(false);
//        drawView.setBrushSize(drawView.getLastBrushSize());
//        if(view!=currPaint){
//            ImageButton imgView = (ImageButton)view;
//            String color = view.getTag().toString();
//            drawView.setColor(color);
//            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
//            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
//            currPaint=(ImageButton)view;
//        }
//    }

    public void paintClicked(View view) {

        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.draw_btn) {
            //draw button clicked
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.erase_btn) {
            //switch to erase - choose size

            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    // drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    //   drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    //   drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            brushDialog.show();
        } else if (view.getId() == R.id.new_btn) {
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();

        } else if (view.getId() == R.id.save_btn) {
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    View save = findViewById(R.id.drawing);
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
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }
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
//        if (id == R.id.drawing_save) {
//            //Save Layout Here
//
//            View save = findViewById(R.id.drawing);
//            assert save != null;
//            Bitmap bitmap = Bitmap.createBitmap(save.getWidth(), save.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            Drawable bgDrawable = save.getBackground();
//            if (bgDrawable != null)
//                bgDrawable.draw(canvas);
//            else
//                canvas.drawColor(Color.WHITE);
//            save.draw(canvas);
//            new saveImageAsynceTask().execute(bitmap);
//            // done
//        }
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
            File directory = cw.getDir("Doodles", Context.MODE_WORLD_READABLE);
            File mypath = new File(directory, Title + ".jpg");

            if (!save) {
                while (mypath.exists()) {
                    int randomNum = (int) (Math.random() * Integer.MAX_VALUE);
                    mypath = new File(directory, Title + randomNum + ".jpg");
                }
            }
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
