package com.ekopa.android.app.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ekopa.android.app.R;
public class LoansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        TableLayout stk = (TableLayout) findViewById(R.id.loan_table);

//        TableRow tbrow0 = new TableRow(this);
//        tbrow0.setPadding(15,15,15,15);
//        tbrow0.setBackgroundColor(getResources().getColor(R.color.green));
//
//        TextView tv0 = new TextView(this);
//        tv0.setText(" Date ");
//        tv0.setTextColor(Color.BLACK);
//        tbrow0.addView(tv0);
//
//        TextView tv1 = new TextView(this);
//        tv1.setText(" Amount ");
//        tv1.setTextColor(Color.BLACK);
//        tbrow0.addView(tv1);
//
//        TextView tv2 = new TextView(this);
//        tv2.setText(" Status ");
//        tv2.setTextColor(Color.BLACK);
//        tbrow0.addView(tv2);
//
////        TextView tv3 = new TextView(this);
////        tv3.setText(" Stock Remaining ");
////        tv3.setTextColor(Color.WHITE);
////        tbrow0.addView(tv3);
//
//        stk.addView(tbrow0);
        for (int i = 0; i < 30; i++) {

            TableRow tbrow = new TableRow(this);

            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setText("Product " + i);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);

            TextView t3v = new TextView(this);
            t3v.setText("Rs." + i);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);

            stk.addView(tbrow);
        }

    }
}
