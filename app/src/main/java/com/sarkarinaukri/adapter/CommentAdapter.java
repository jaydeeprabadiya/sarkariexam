package com.sarkarinaukri.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.model.CommentDetails;
import com.sarkarinaukri.model.JobData;

import java.util.ArrayList;
import java.util.List;



public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private ArrayList<CommentDetails.data> dataArrayList;
    private Context context;


    public CommentAdapter(Context context, ArrayList<CommentDetails.data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;

    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {

        holder.tvName.setText(dataArrayList.get(position).fullName);
        holder.tvComment.setText(dataArrayList.get(position).comment);


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface ItemListener {
        void viewJob(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView tvName;
        private TextView tvComment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvComment=itemView.findViewById(R.id.tvAnswerDetail);


        }
    }
}
