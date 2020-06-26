package com.sarkarinaukri.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.model.PostData;

import java.util.ArrayList;

public class JobsAdapter extends ArrayAdapter<PostData> {

    private Context mContext;
    private SparseBooleanArray mSelectedItemsIds;


    public JobsAdapter(Context context, int resource, ArrayList<PostData> objects) {

        super(context, resource, objects);
        mSelectedItemsIds = new SparseBooleanArray();
        mContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customView = convertView;
        JobViewHolder jobViewHolder;

        if (customView == null){
            customView = LayoutInflater.from(getContext()).inflate(R.layout.job_list_item,parent,false);
            jobViewHolder = new JobViewHolder();

            jobViewHolder.jobTitleTV = customView.findViewById(R.id.job_title);

            customView.setTag(jobViewHolder);
        }else{
            jobViewHolder = (JobViewHolder) customView.getTag();
        }

        PostData jobItem = getItem(position);

        if(jobItem != null) {
            String jobTitle = jobItem.getTitle().trim();
            jobViewHolder.jobTitleTV.setText(jobTitle);

        }

        return customView;
    }


    static class JobViewHolder{
        TextView jobTitleTV;
        View dateContainer;
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    private void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
