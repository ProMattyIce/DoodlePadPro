package mrh5493.edu.psu.doodlepadpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

import mrh5493.edu.psu.database.DoodleContentProvider;

public class ShareImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ImageView Doodle = (ImageView) findViewById(R.id.shareimageImage);
        TextView Title = (TextView) findViewById(R.id.shareimageTitle);
        TextView Desciption = (TextView) findViewById(R.id.shareimageDesciption);

        final Intent intent = getIntent();

        Title.setText(intent.getStringExtra("Title"));
        Desciption.setText(intent.getStringExtra("Desc"));

        final File imgFile = new File(intent.getStringExtra("Image"));
        Toast.makeText(this, imgFile.getAbsolutePath() + " ", Toast.LENGTH_LONG).show();
        Picasso.with(this).load(imgFile.getAbsoluteFile()).fit().into(Doodle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(DoodleContentProvider.CONTENT_URI, "title=?", new String[]{intent.getStringExtra("Title")});
                finish();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
