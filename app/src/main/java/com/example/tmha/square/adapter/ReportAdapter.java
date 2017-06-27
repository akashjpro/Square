package com.example.tmha.square.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.CreateReportActivity;
import com.example.tmha.square.activity.DetailProjectActivity;
import com.example.tmha.square.activity.DetailReportActivity;
import com.example.tmha.square.activity.MainActivity;
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

    public ReportAdapter(DetailProjectActivity mContext,
                         List<Report> mReportList) {
        this.mReportList = mReportList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 int position) {
        final Report report = mReportList.get(position);
        if (holder instanceof  ReportViewHolder){
            final ReportViewHolder reportViewHolder
                    = (ReportViewHolder) holder;
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


            reportViewHolder.setOnLongListener(new ListenerItem() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(mContext,
                            DetailReportActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("report", report);
                    bundle.putSerializable("position", position);
                    intent.putExtra("bundle", bundle);
                    mContext.startActivityForResult(intent,
                            mContext.REQUEST_CODE_DELETE);
                    mContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }

                @Override
                public void onLongClick(final int position) {
                    PopupMenu popupMenu
                            = new PopupMenu(mContext,
                            reportViewHolder.txtTiltle);
                    popupMenu.getMenuInflater()
                            .inflate(R.menu.menu_item_report,
                                    popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(
                            new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.nav_edit:
                                        editReport(report, position);
                                    break;
                                case R.id.nav_delete:
                                        deleteReport(report, position);
                                    break;
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mReportList.size();
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        TextView txtTiltle;
        ImageView imgPhoto;
        ListenerItem mListenerItem;
        public ReportViewHolder(View itemView) {
            super(itemView);
            txtTiltle = (TextView) itemView.findViewById(R.id.txtTitleReport);
            imgPhoto  = (ImageView) itemView.findViewById(R.id.imgReport);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setOnClickListener(ListenerItem listener){
            this.mListenerItem = listener;
        }

        public void setOnLongListener(ListenerItem listener){
            this.mListenerItem = listener;
        }

        @Override
        public void onClick(View v) {
            this.mListenerItem.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            this.mListenerItem.onLongClick(getAdapterPosition());
            return false;
        }
    }

    private void editReport( Report report,  int position){
        Intent intent = new Intent(mContext,
                CreateReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("report", report);
        bundle.putInt("position", position);
        intent.putExtra("bundle", bundle);
        mContext.startActivityForResult(intent,
                mContext.REQUEST_CODE_UPDATE);
    }

    /**
     * delete report
     * @param report
     * @param position
     */
    private void deleteReport(final Report report, final int position){
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Thong bao");
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.btnCancel)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnOk)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result =
                        MainActivity.database
                                .deleteReport(String.valueOf(report.getmID()),
                                        null);
                if (result){
                    Toast.makeText(mContext,
                            "Delete success",
                            Toast.LENGTH_SHORT).show();
                            mContext.deleteReport(position);
                    dialog.dismiss();
                }else {
                    Toast.makeText(mContext,
                            "Delete fail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
