package mrh5493.edu.psu.doodlepadpro;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import mrh5493.edu.psu.database.DoodleContentProvider;
import mrh5493.edu.psu.database.DoodleContract;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<DoodleDEF> adapterResults;
    HashMap<String, Bitmap> saveResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Go to Drawing Screen here
                Intent intent = new Intent(MainActivity.this, Drawing.class);
                startActivity(intent);
            }
        });


        adapterResults = new ArrayList<>();
        saveResults = new HashMap<>();

        recyclerView = (RecyclerView) findViewById(R.id.ImagesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ImageAdapter imageAdapter = new ImageAdapter(adapterResults);
        recyclerView.setAdapter(imageAdapter);

        new imageAsyncTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        saveResults.clear();
        new imageAsyncTask().execute();
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

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


        private final ArrayList<DoodleDEF> adapterResults;

        public ImageAdapter(ArrayList<DoodleDEF> adapterResults) {
            this.adapterResults = adapterResults;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imagecardview, parent, false);
            ImageViewHolder imageViewHolder = new ImageViewHolder(view);
            return imageViewHolder;
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {

            holder.Title.setText(adapterResults.get(position).getTitle());

            if (saveResults.containsKey(adapterResults.get(position).getImage()))
                holder.Doodle.setImageBitmap(saveResults.get(adapterResults.get(position).getImage()));
            else
                new BitmapWorkerTask(holder.Doodle).execute(adapterResults.get(position).getImage());

        }

        @Override
        public int getItemCount() {
            return adapterResults.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {

            CardView CardView;
            TextView Title;
            ImageView Doodle;

            public ImageViewHolder(View itemView) {
                super(itemView);
                CardView = (CardView) itemView.findViewById(R.id.imageCardView);
                Title = (TextView) itemView.findViewById(R.id.cardTitle);
                Doodle = (ImageView) itemView.findViewById(R.id.cardImage);
            }

            //End ImageViewHolder
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            String data = params[0];
            Bitmap bitmap = BitmapFactory.decodeFile(data);

            saveResults.put(data, bitmap);

            return bitmap;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private class DoodleDEF {
        private String Title;
        private String Description;
        private String Image;

        public DoodleDEF() {

        }

        public DoodleDEF(String title, String description, String image) {
            Title = title;
            Description = description;
            Image = image;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }
    }

    private class imageAsyncTask extends AsyncTask<Void, Void, ArrayList<DoodleDEF>> {

        @Override
        protected ArrayList<DoodleDEF> doInBackground(Void... params) {

            ArrayList<DoodleDEF> retVal = new ArrayList<>();

            String[] projection = {DoodleContract.DoodleTable.DOODLETITLE, DoodleContract.DoodleTable.DOODLEDESCRIPTION, DoodleContract.DoodleTable.DOODLEFILEPATH};
            Cursor query = getContentResolver().query(DoodleContentProvider.CONTENT_URI, projection, null, null, null);
            assert query != null;
            query.moveToFirst();
            while (!query.isAfterLast()) {
                DoodleDEF temp = new DoodleDEF();

                temp.setTitle(query.getString(0));
                Log.v("GET", query.getColumnName(2));
                temp.setImage(query.getString(2));

                retVal.add(temp);
                query.moveToNext();
            }

            return retVal;
        }


        @Override
        protected void onPostExecute(ArrayList<DoodleDEF> doodleDEFs) {
            super.onPostExecute(doodleDEFs);

            ImageAdapter adpater = new ImageAdapter(doodleDEFs);
            recyclerView.setAdapter(adpater);

        }
    }
}
