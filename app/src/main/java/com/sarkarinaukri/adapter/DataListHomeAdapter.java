package com.sarkarinaukri.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.model.JobData;

import java.util.List;


/**
 * Created by anurag on 7/26/2016.
 */

public class DataListHomeAdapter extends RecyclerView.Adapter<DataListHomeAdapter.ViewHolder> {
    private List<JobData> jobList;
    private Context context;
    private ItemListener listener;

    public DataListHomeAdapter(Context context, List<JobData> jobList, ItemListener listener) {
        this.context = context;
        this.jobList = jobList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JobData postData = jobList.get(position);

        if (position == 0 || position == 1) {
            holder.tvTitle.setText(Html.fromHtml(postData.getPost_title() + "<font color='#FF0000'> - New</font>"));
        } else {
            holder.tvTitle.setText(Html.fromHtml(postData.getPost_title()));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public interface ItemListener {
        void viewJob(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvTitle.setTextColor(Color.parseColor("#2591FF"));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.viewJob(getAdapterPosition());
                            tvTitle.setTextColor(Color.parseColor("#000000"));
                        }
                    }, 500);
                }
            });
        }
    }
}