package mrh5493.edu.psu.doodlepadpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText title = (EditText) findViewById(R.id.addImageTitleEditText);
        final EditText descption = (EditText) findViewById(R.id.addImageDescriptionEditText);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String setTitle = SP.getString("title", "TITLE");

        title.setText(setTitle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!title.getText().toString().equals("")) {
                    Intent intent = new Intent(AddImage.this, Drawing.class);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("desc", descption.getText().toString());
                    startActivity(intent);
                } else
                    Toast.makeText(AddImage.this, "Title can not be empty", Toast.LENGTH_SHORT).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
