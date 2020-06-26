package com.sarkarinaukri.qnaModule.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarkarinaukri.R;

import java.util.List;


/**
 * Created by anurag on 7/26/2016.
 */

public class CorrectAnswerListAdapter extends RecyclerView.Adapter<CorrectAnswerListAdapter.ViewHolder> {
    private List<String> correctAnswerList;
    private Context context;
    private ItemListener listener;
    private int selectedPosition = -1;

    public CorrectAnswerListAdapter(Context context, List<String> correctAnswerList, ItemListener listener) {
        this.context = context;
        this.correctAnswerList = correctAnswerList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.correct_answer_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvCorrectAnswer.setText(correctAnswerList.get(position));
        if (selectedPosition == position) {
            holder.tvCorrectAnswer.setTextColor(Color.parseColor("#ffffff"));
            holder.llCorrectAnswer.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
        } else {
            holder.tvCorrectAnswer.setTextColor(Color.parseColor("#000000"));
            holder.llCorrectAnswer.setBackground(context.getResources().getDrawable(R.drawable.black_circle));
        }
    }

    @Override
    public int getItemCount() {
        return correctAnswerList.size();
    }


    public interface ItemListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llCorrectAnswer;
        private final TextView tvCorrectAnswer;

        public ViewHolder(View itemView) {
            super(itemView);
            llCorrectAnswer = (LinearLayout) itemView.findViewById(R.id.llCorrectAnswer);
            tvCorrectAnswer = (TextView) itemView.findViewById(R.id.tvCorrectAnswer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}