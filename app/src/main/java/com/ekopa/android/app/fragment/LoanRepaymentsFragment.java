package com.ekopa.android.app.fragment;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ekopa.android.app.R;
import com.ekopa.android.app.api.ApiClient;
import com.ekopa.android.app.api.ApiInterface;
import com.ekopa.android.app.helper.PrefManager;
import com.ekopa.android.app.model.Data;
import com.ekopa.android.app.model.Payment;
import com.ekopa.android.app.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoanRepaymentsFragment extends Fragment {

    View rootView;
    private static final int HIGHLIGHT_COLOR = 0x999be6ff;

    //declare public static to access from external classes
        public static List<Payment> payments = new ArrayList<>();
    // list of data items
    private List<ListData> mDataList= new ArrayList<>();

    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ListView listView;
    PrefManager prefManager;

    public LoanRepaymentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_issued_loans, container, false);
        // initialize the builder based on the "TYPE"
        mDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .round();

        prefManager = new PrefManager(getActivity());
        // init the list view and its adapter
        listView = (ListView) rootView.findViewById(R.id.listView);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending request...");
        progressDialog.show();

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.customerStatus(prefManager.getUserDetails().get(prefManager.KEY_USERTOKEN));
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().getStatus_code().equals(200)) {
                    onRequestSuccess(response.body());
                } else if (response.body().getStatus_code().equals(400)) {
                    onRequestFailed("Service not available");
                } else if (response.body().getStatus_code().equals(404)) {
                    onRequestFailed("Service not available");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                onRequestFailed(t.getMessage());
            }
        });

        return rootView;
    }

    public void onRequestSuccess(ResponseModel rm) {
        Data data = rm.getData();
        //loan requests
        payments = data.getPayments();
        for(int i=0;i< payments.size(); i++){
            mDataList.add(new ListData(payments.get(i).getRepayment_amount(),payments.get(i).getRepayment_date()));
        }
        listView.setAdapter(new SampleAdapter());
    }
    public void onRequestFailed(String message) {
        if (!message.equals("")) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }
    private class SampleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public ListData getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.payments_list_item_layout, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ListData item = getItem(position);

            // provide support for selected state
            updateCheckedState(holder, item);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when the image is clicked, update the selected state
                    ListData data = getItem(position);
                    data.setChecked(!data.isChecked);
                    updateCheckedState(holder, data);
                }
            });
            holder.txtAmount.setText("KES "+item.amount);
            holder.txtRequestDate.setText("Payed on "+item.requestDate);

            return convertView;
        }

        private void updateCheckedState(ViewHolder holder, ListData item) {
            if (item.isChecked) {
                holder.imageView.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
                holder.view.setBackgroundColor(HIGHLIGHT_COLOR);
                holder.checkIcon.setVisibility(View.VISIBLE);
            }
            else {
                if(item.requestDate != null && item.requestDate.length() > 0) {
                    TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.requestDate.charAt(0)), mColorGenerator.getColor(item.requestDate));
                    holder.imageView.setImageDrawable(drawable);
                    holder.view.setBackgroundColor(Color.TRANSPARENT);
                    holder.checkIcon.setVisibility(View.GONE);
                }
            }
        }
    }

    private static class ViewHolder {

        private View view;

        private ImageView imageView;

        private TextView txtAmount, txtRequestDate;

        private ImageView checkIcon;

        private ViewHolder(View view) {
            this.view = view;
            imageView = (ImageView) view.findViewById(R.id.imageView);
            txtAmount = (TextView) view.findViewById(R.id.loan_amount);
            txtRequestDate = (TextView) view.findViewById(R.id.request_date);
            checkIcon = (ImageView) view.findViewById(R.id.check_icon);
        }
    }

    private static class ListData {

        private String requestDate;

        private double amount;
        private boolean isChecked;

        public ListData(double amount, String requestDate) {
            this.amount = amount;
            this.requestDate = requestDate;
        }
        public void setChecked(boolean isChecked) {
            this.isChecked = isChecked;
        }
    }

}
