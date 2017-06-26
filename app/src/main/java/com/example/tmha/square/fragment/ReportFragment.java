package com.example.tmha.square.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tmha.square.R;
import com.example.tmha.square.activity.MainActivity;
import com.example.tmha.square.adapter.ProjectAdapter;
import com.example.tmha.square.listener.ListenerReport;
import com.example.tmha.square.model.Project;

import java.util.ArrayList;
import java.util.List;

import static com.example.tmha.square.activity.MainActivity.database;

/**
 * Created by tmha on 6/8/2017.
 */

public class ReportFragment extends Fragment
        implements ListenerReport{
    //private LinearLayoutManager mLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRecyclerView;
    private List<Project> mListReport;
    private ProjectAdapter mReportAdapter;
    private TextView mTxtTotalProject;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private int mRow = 10;
    private int mIndex = 0;


    private   boolean isLoad;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_report, container, false);
        initialControl();
        refresh();
        loadMore();
        return mView;
    }

    private void loadMore() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemcount = mGridLayoutManager.getChildCount();
                int totalItemCount   = mGridLayoutManager.getItemCount();
                final int pastVisibleItemCount = mGridLayoutManager.findFirstVisibleItemPosition();

                if((mListReport.size()> mIndex) & (visibleItemcount + pastVisibleItemCount>= totalItemCount -5)) {

                    mIndex = mIndex + mRow;
                    ArrayList<Project> arrayList = MainActivity.database.getLimitproject(mRow, mIndex);
                    for (int i =0 ; i< arrayList.size(); i ++){
                        mListReport.add(arrayList.get(i));
                        mReportAdapter.notifyItemInserted(mIndex);
                    }
                    //mReportAdapter.notifyDataSetChanged();

//                        mListReport.add(null);
//                        mReportAdapter.notifyItemInserted(mListReport.size() - 1);
////                        // add item null -> loadding
////                        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
////                            @Override
////                            public int getSpanSize(int position) {
//////                                if (position == mListReport.size() - 1)
//////                                    return 2;
//////                                else
////                                    return 1;
////                            }
////                        });
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //remove item loadding
//                                mListReport.remove(mListReport.size() - 1);
//                                mReportAdapter.notifyItemRemoved(mListReport.size() - 1);
//                                mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                                    @Override
//                                    public int getSpanSize(int position) {
//                                        return 1;
//                                    }
//                                });
//                                int size1 = mListReport.size();
//
//
//                                //add data
//                                if (size - size1 <= 10) {
//                                    for (int i = size1; i < size; i++) {
//                                        mListReport.add(mListAllReport.get(i));
//                                        mTxtTotalProject.setText(mListReport.size() + "");
//                                    }
//                                    mReportAdapter.notifyDataSetChanged();
//                                } else {
//                                    for (int i = size1; i < size1 + 10; i++) {
//                                        mListReport.add(mListAllReport.get(i));
//                                        mTxtTotalProject.setText(mListReport.size() + "");
//                                    }
//                                    mReportAdapter.notifyDataSetChanged();
//                                }
//
//                                isLoad = false;
//                            }
//                        }, 500);
//
//                        isLoad = true;

                }
            }
        });


    }

    /**
     * initial all control and get data
     */
    private void initialControl() {
        mListReport = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerViewReport);
        mTxtTotalProject = (TextView) mView.findViewById(R.id.txtTotalProject);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
//                LinearLayoutManager.VERTICAL, false);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 1);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), mGridLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mListReport = database.getLimitproject(mRow, mIndex);
        mTxtTotalProject.setText(mListReport.size() + "");

//        mRecyclerView.addOnScrollListener(MainActivity.showHideToolbarListener
//                = new RecyclerViewUtils.ShowHideToolbarOnScrollingListener(MainActivity.mToolbar, getActivity()));

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mReportAdapter = new ProjectAdapter(getActivity(), mListReport, this);
        mRecyclerView.setAdapter(mReportAdapter);
    }

    /**
     * refresh data
     */
    private void refresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListReport.clear();
                mListReport.addAll(database.getLimitproject(10, 0));
                mReportAdapter.notifyDataSetChanged();
                //Stop progress indicator when update finish
                mSwipeRefreshLayout.setRefreshing(false);
                mTxtTotalProject.setText(mListReport.size() + "");
                //set index = 0 for load back
                mIndex = 0;
            }
        });
    }

    /**
     * Delete item report use interface listener
     * @param position
     */
    @Override
    public void deleteItem(int position) {
        int id = mListReport.get(position).getmID();
        boolean resultDeleteReport = database.deleteReport(null, String.valueOf(id));
        if (resultDeleteReport){
        boolean result = database.deleteProject(String.valueOf(id));
            if(result) {
                Toast.makeText(getActivity(), "Delete success", Toast.LENGTH_SHORT).show();
                mListReport.remove(position);
                mReportAdapter.notifyDataSetChanged();
                mTxtTotalProject.setText(mListReport.size() + "");
            }else {
                Toast.makeText(getActivity(), "Delete fail!!!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), "Delete fail!!!", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateReport(int position, Project project) {
        // Project report = database.getProject(String.valueOf(id));
        mListReport.set(position, project);
        mReportAdapter.notifyItemChanged(position);
    }

    /**
     * add new report
     */
    public void addReport(Project project) {
        mListReport.add(0, project);
        mIndex++;
        mReportAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mTxtTotalProject.setText(mListReport.size() + "");
    }



}
