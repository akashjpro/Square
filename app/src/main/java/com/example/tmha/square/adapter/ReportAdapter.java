package com.example.tmha.square.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.DetailProjectActivity;
import com.example.tmha.square.listener.ListenerItem;
import com.example.tmha.square.model.Report;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by tmha on 6/22/2017.
 */

public class ReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Report> mReportList;
    DetailProjectActivity mContext;

    public ReportAdapter(DetailProjectActivity mContext, List<Report> mReportList) {
        this.mReportList = mReportList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Report report = mReportList.get(position);
        if (holder instanceof  ReportViewHolder){
            ReportViewHolder reportViewHolder = (ReportViewHolder) holder;
            reportViewHolder.txtTiltle.setText(report.getmReportName());
            try {
                JSONArray jsonArray = new JSONArray(report.getmAlbum());
                String picPath = jsonArray.get(0).toString();
                Picasso.with(mContext).load(picPath)
                        .error(android.R.drawable.stat_notify_error)
                        .into(reportViewHolder.imgPhoto);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            reportViewHolder.setOnClickListener(new ListenerItem() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(mContext, DetailReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("report", report);
                    intent.putExtra("bundle", bundle);
                    mContext.startActivityForResult(intent, mContext.REQUEST_CODE_DELETE);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mReportList.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTiltle;
        ImageView imgPhoto;
        ListenerItem listenerItem;
        public ReportViewHolder(View itemView) {
            super(itemView);
            txtTiltle = (TextView) itemView.findViewById(R.id.txtTitleReport);
            imgPhoto  = (ImageView) itemView.findViewById(R.id.imgReport);
            itemView.setOnClickListener(this);
        }

        public void setOnClickListener(ListenerItem listener){
            this.listenerItem = listener;
        }

        @Override
        public void onClick(View v) {
            listenerItem.onClick(getAdapterPosition());
        }
    }
}
