package com.example.tmha.square.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.CreateProjectAcivity;
import com.example.tmha.square.activity.DetailProjectActivity;
import com.example.tmha.square.activity.MainActivity;
import com.example.tmha.square.listener.ListenerItem;
import com.example.tmha.square.listener.ListenerReport;
import com.example.tmha.square.model.Project;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tmha on 6/9/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity mContext;
    private List<Project> mListProject;
    private ListenerReport mListenerReport;
    private final int VIEW_TYPE_ITEM = 0; // View item report
    private final int VIEW_TYPE_lOADING = 1; // view loading
    private boolean isLoad = false;

    public ProjectAdapter(Activity mContext, List<Project> mListProject
            , ListenerReport listenerReport) {
        this.mContext        = mContext;
        this.mListProject     = mListProject;
        this.mListenerReport = listenerReport;
    }

    public void onLoad(){
        isLoad = true;
    }

    public void offLoad(){
        isLoad = false;
    }

    @Override
    public int getItemViewType(int position) {
        //if list data = null then show view loading else load view item report
        return mListProject.get(position)
                == null ? VIEW_TYPE_lOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //load view item report
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_project, parent, false);
            return new ProjectViewHolder(view);
        }else if(viewType == VIEW_TYPE_lOADING){
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.load_more, parent, false);
            return new LoadingViewHoler(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder
            , final int position) {
        if (holder instanceof ProjectViewHolder) {
            final ProjectViewHolder projectHolder = (ProjectViewHolder) holder;
            final Project project = mListProject.get(position);
            //reportHolder.txtIndex.setText(position + "");
            projectHolder.nameProject.setText(project.getmProjectName());
            projectHolder.address.setText(project.getmAddress());
            projectHolder.timeProject.setText(project.getmTimeCreate());
            String picPath = project.getmProjectPhoto();


            Picasso.with(mContext).load(picPath)
                    .error(android.R.drawable.stat_notify_error)
                    .into(projectHolder.imgProject);


//            if(picPath != null){
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(picPath));
//                    projectHolder.imgProject.setImageBitmap(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }


            projectHolder.imgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(mContext,
                            projectHolder.imgMore);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item_report,
                            popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.nav_edit:
                                    Intent intent = new Intent(mContext,
                                            CreateProjectAcivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("project", project);
                                    bundle.putInt("position", position);
                                    intent.putExtra("bundle", bundle);
                                    mContext.startActivityForResult(intent,
                                            MainActivity.REQUEST_CODE_UPDATE);
                                    break;
                                case R.id.nav_delete:
                                    mListenerReport.deleteItem(position);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            projectHolder.setOnClickListener(new ListenerItem() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(mContext, DetailProjectActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("project", project);
                    intent.putExtra("bundle", bundle);
                    mContext.startActivity(intent);
                    mContext.overridePendingTransition(R.anim.left_in, R.anim.right_out);
                }
            });
        }else if (holder instanceof LoadingViewHoler){
//            LoadingViewHoler loadingHoler = (LoadingViewHoler) holder;
//            loadingHoler.prbBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return mListProject.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        TextView nameProject, timeProject, address, txtIndex;
        ImageView imgProject, imgMore;
        ListenerItem mListenerItem;
        public ProjectViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            nameProject     = (TextView) itemView.findViewById(R.id.txtNameProject);
            timeProject     = (TextView) itemView.findViewById(R.id.txtTimeProject);
            address         = (TextView) itemView.findViewById(R.id.txtAddress);
            imgProject      = (ImageView) itemView.findViewById(R.id.imgProject);
            imgMore         = (ImageView) itemView.findViewById(R.id.imgMore);
            // txtIndex        = (TextView) itemView.findViewById(R.id.txtIndex);
        }

        public void setOnClickListener(ListenerItem listenerItem){
            this.mListenerItem = listenerItem;
        }

        @Override
        public void onClick(View v) {
            mListenerItem.onClick(getAdapterPosition());
        }
    }

    public class LoadingViewHoler extends RecyclerView.ViewHolder{
        public ProgressBar prbBar;

        public LoadingViewHoler(View itemView) {
            super(itemView);
            prbBar = (ProgressBar) itemView.findViewById(R.id.progressBar);


        }
    }

}
