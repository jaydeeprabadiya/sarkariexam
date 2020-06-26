package com.sarkarinaukri.qnaModule.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sarkarinaukri.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by anurag on 7/26/2016.
 */

public class OptionListAdapter extends RecyclerView.Adapter<OptionListAdapter.ViewHolder> {
    private List<String> optionList;
    private Context context;
    private ItemListener listener;

    public OptionListAdapter(Context context, List<String> optionList, ItemListener listener) {
        this.context = context;
        this.optionList = optionList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_options, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvOption.setText(optionList.get(position));
        holder.editText.setHint("Option " + optionList.get(position));

        if (position == 0 || position == 1) {
            holder.ibCancel.setVisibility(View.GONE);
        }
        if (position == 2 && Collections.max(optionList).equalsIgnoreCase("C")) {
            holder.ibCancel.setVisibility(View.VISIBLE);
        } else if (position == 2) {
            holder.ibCancel.setVisibility(View.GONE);
        }
        if (position == 3 && Collections.max(optionList).equalsIgnoreCase("D")) {
            holder.ibCancel.setVisibility(View.VISIBLE);
        } else if (position == 3) {
            holder.ibCancel.setVisibility(View.GONE);
        }
        if (position == 4 && Collections.max(optionList).equalsIgnoreCase("E")) {
            holder.ibCancel.setVisibility(View.VISIBLE);
        } else if (position == 4) {
            holder.ibCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public interface ItemListener {
        void onDeleteView(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final EditText editText;
        private final ImageButton ibCancel;
        private final TextView tvOption;

        public ViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.editText);
            ibCancel = (ImageButton) itemView.findViewById(R.id.ibCancel);
            tvOption = (TextView) itemView.findViewById(R.id.tvOption);

            ibCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    listener.onDeleteView(getAdapterPosition());
                }
            });
        }
    }
}