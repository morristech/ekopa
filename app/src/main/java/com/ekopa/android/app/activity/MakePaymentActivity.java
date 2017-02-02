package com.ekopa.android.app.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ekopa.android.app.R;

public class MakePaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppCompatButton btnPayNow = (AppCompatButton) findViewById(R.id.btnPayNow);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(MakePaymentActivity.this)
                        .title("Enter amount to pay")
                        .content(getString(R.string.amount))
                        .inputType(InputType.TYPE_CLASS_NUMBER)//
                        .input(null, null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                // Do something
                                Toast.makeText(MakePaymentActivity.this, "Ksh. "+input.toString()+" will be sent.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

}
