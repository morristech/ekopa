package com.ekopa.android.app.invite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ekopa.android.app.R;
import com.ekopa.android.app.helper.PrefManager;

import java.util.ArrayList;

public class SendInvite extends AppCompatActivity {

    TextView contactsDisplay, txtSelectedContacts;
    Button btnSend;
    EditText txtInvite;
    String referalCode;
    PrefManager prefManager;
    ArrayList<Contact> selectedContacts;
    final int CONTACT_PICK_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invite);


        contactsDisplay = (TextView) findViewById(R.id.txt_selected_contacts);
        txtInvite = (EditText) findViewById(R.id.txt_invite);
        btnSend = (Button) findViewById(R.id.btn_send);
        prefManager = new PrefManager(this);
        referalCode = prefManager.getUserDetails().get(prefManager.KEY_REFERRAL_CODE);

        selectedContacts = getIntent().getParcelableArrayListExtra("SelectedContacts");
        String display="";
        for(int i=0;i<selectedContacts.size();i++){

//            display += (i+1)+". "+selectedContacts.get(i).toString()+"\n";
            if(i == (selectedContacts.size() - 1)){
                display += selectedContacts.get(i).toString().split("#")[0].trim();
            }else{
                display += selectedContacts.get(i).toString().split("#")[0].trim()+", ";
            }

        }
//        referalCode = "1234";
        contactsDisplay.setText(display);
        txtInvite.setText(getResources().getString(R.string.txt_invite)+" "+referalCode);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<selectedContacts.size();i++){
                    String receiverPhone = selectedContacts.get(i).toString().split("#")[1].trim();
                    String message = txtInvite.getText().toString().trim();
                    sendSMS(receiverPhone,message);
                }
            }
        });
        getSupportActionBar().setTitle(selectedContacts.size()+" Contacts Selected");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendSMS(String phoneNo, String msg){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
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
