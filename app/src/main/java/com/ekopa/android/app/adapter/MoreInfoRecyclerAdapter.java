package com.ekopa.android.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekopa.android.app.R;


public class MoreInfoRecyclerAdapter extends RecyclerView.Adapter<MoreInfoRecyclerAdapter.MyViewHolder> {

    private String[] information;
    private MoreInfoRecyclerAdapterListener  mListener;

    public MoreInfoRecyclerAdapter(String[] strings, Context context) {
        mListener = (MoreInfoRecyclerAdapterListener) context;
        this.information = strings;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.tv_more_info_title);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_information, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(information[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return information.length;
    }

    public interface MoreInfoRecyclerAdapterListener{
        void onClick(int position);
    }
}