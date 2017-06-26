package com.example.tmha.square.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.model.Project;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmha on 6/8/2017.
 */

public class WorkHeadAdapter extends RecyclerView.Adapter<WorkHeadAdapter.WorkHeadViewHolder> {
    private Activity mContext;
    private List<Project> mListReport;

    public class WorkHeadViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        ImageView imgWork;

        public WorkHeadViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.titleWorkHead);
            imgWork  = (ImageView) itemView.findViewById(R.id.imgWorkHead);
        }
    }

    public WorkHeadAdapter(Activity mContext, List<Project> mListReport) {
        this.mContext = mContext;
        this.mListReport = mListReport;
    }

    @Override
    public WorkHeadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_work_head, parent, false);
        return new WorkHeadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkHeadViewHolder holder, int position) {
        //get item report from list report
        Project report = mListReport.get(position);
        holder.txtTitle.setText(report.getmProjectName());

        //bye to bitmap
        String picPath = report.getmProjectPhoto();

        Picasso.with(mContext).load(picPath)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgWork);
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(picPath));
//            holder.imgWork.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return mListReport.size();
    }


}
