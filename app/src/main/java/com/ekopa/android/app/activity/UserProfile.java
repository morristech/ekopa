package com.ekopa.android.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ekopa.android.app.R;
import com.ekopa.android.app.helper.ImageTransformation;
import com.ekopa.android.app.helper.PrefManager;

public class UserProfile extends AppCompatActivity {

    PrefManager prefManager;
    ImageView user_image;
    Picasso picasso;
    TextView tvNumber1, tvNumber2, tvNumber3, tvNumber4, tvNumber5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        picasso = Picasso.with(this);
        user_image = (ImageView) findViewById(R.id.user_image);
        tvNumber1 = (TextView) findViewById(R.id.tvNumber1);
//        tvNumber2 = (TextView) findViewById(R.id.tvNumber2);
        tvNumber3 = (TextView) findViewById(R.id.tvNumber3);
//        tvNumber4 = (TextView) findViewById(R.id.tvNumber4);
        tvNumber5 = (TextView) findViewById(R.id.tvNumber5);

        tvNumber1.setText(prefManager.getUserDetails().get(prefManager.KEY_PHONE));
//        tvNumber2.setText("0718000659");
        tvNumber3.setText(prefManager.getUserDetails().get(prefManager.KEY_IDNUMBER));
//        tvNumber4.setText("mail@almodad.com");
        tvNumber5.setText(prefManager.getUserDetails().get(prefManager.KEY_DOB));

        picasso.load(prefManager.getUserDetails().get(prefManager.KEY_IMAGEURL))
                .resizeDimen(R.dimen.user_image_width, R.dimen.user_image_height)
                .centerCrop()
                .placeholder(R.drawable.user_avatar)
                .transform(new ImageTransformation())
                .into(user_image);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Edit Account Information", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setTitle(prefManager.getUserDetails().get(prefManager.KEY_NAME));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
