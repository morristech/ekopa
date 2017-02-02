package com.ekopa.android.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ekopa.android.app.R;
import com.ekopa.android.app.adapter.MoreInfoRecyclerAdapter;
import com.ekopa.android.app.fragment.InformationContentFragment;
import com.ekopa.android.app.fragment.InformationListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpFAQActivity extends AppCompatActivity implements MoreInfoRecyclerAdapter.MoreInfoRecyclerAdapterListener{
    HashMap<String, List<String>> listDataChild;
    String[] information_titles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_faq);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment, new InformationListFragment());
        tx.commit();

        prepareListData();


    }

    private void prepareListData() {

        listDataChild = new HashMap<>();

        //Adding header data
        information_titles = getResources().getStringArray(R.array.information);


        //Adding child data
        List<String> requirements = new ArrayList<>();
        requirements.add("All you need to apply for a loan is your mobile money account, android phone and your ID.");

        List<String> approvalRejection = new ArrayList<>();
        approvalRejection.add("Loan decisions are based on many sources of information including income, repayment of other loans, mpesa usage . You provide us with the information when you use the app. This enables us to build a credit score for you");

        List<String> amounts = new ArrayList<>();
        amounts.add("We offer loans up to a maximum of 20,000ksh. At a 10% loan processing fee. You can only increase your loan limit by making your loan payment on time.");

        List<String> payment = new ArrayList<>();
        payment.add("How to make payments for ekopa loan:\n" +
                "1.\t Go to you safaricom menu and select M-pesa.\n" +
                "2.\tFrom your mpesa menu select lipa na mpesa.\n" +
                "3.\tSelect pay bill option.\n" +
                "4.\tEnter the business number 811139.\n" +
                "5.\tEnter your mobile money number on which you received the loan from ekopa as the account number.\n" +
                "6.\tEnter the amount you are paying.\n" +
                "7.\tEnter your mpesa pin.\n" +
                "8.\tConfirm whether the details are correct and press OK.\n");

        List<String> latePay = new ArrayList<>();
        latePay.add("Paying each instalment by due date helps you build your credit score and increase your loan limit. Late payment limits chances of getting subsequent loans. In the event you default we forward your name to credit reference bureau (CRB).");

        List<String> emergency = new ArrayList<>();
        emergency.add("An emergency loan is an instant loan sent to subsequent customers. Once you request the loan you only qualify for 50% of the previous amount you borrowed. You will always receive the loan even if you are still servicing your current loan with ekopa.");

        List<String> other = new ArrayList<>();
        other.add("In case we have not answered your questions. Kindly let us know so that we can help you.");


        listDataChild.put(information_titles[0], requirements); // Header, Child data
        listDataChild.put(information_titles[1], approvalRejection);
        listDataChild.put(information_titles[2], amounts);
        listDataChild.put(information_titles[3], payment);
        listDataChild.put(information_titles[4], latePay);
        listDataChild.put(information_titles[5], emergency);
        listDataChild.put(information_titles[6], other);
    }

    @Override
    public void onBackPressed() {
        //int count = getFragmentManager().getBackStackEntryCount();

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count==0){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }


    }

    @Override
    public void onClick(int position) {
        String title = information_titles[position];
        String content = listDataChild.get(title).get(0);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        //tx.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        new InformationContentFragment();
        tx.replace(R.id.fragment, InformationContentFragment.newInstance(title, content)).addToBackStack("list");
        tx.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

}
