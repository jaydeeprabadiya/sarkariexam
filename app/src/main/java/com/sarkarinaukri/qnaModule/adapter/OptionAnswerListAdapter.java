package com.sarkarinaukri.qnaModule.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sarkarinaukri.R;
import com.sarkarinaukri.model.Option;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anurag on 7/26/2016.
 */

public class OptionAnswerListAdapter extends RecyclerView.Adapter<OptionAnswerListAdapter.ViewHolder> {
    private List<Option> optionList;
    private Context context;
    private String rightOption;
    private String givenOption;
    private List<String> optionNumberList = new ArrayList<>();
    private List<ViewHolder> viewHolders = new ArrayList<>();
    private int totalAnswer = 0;
    private ProgressListener listener;

    public OptionAnswerListAdapter(Context context, String rightOption, String givenOption, List<Option> optionList, ProgressListener listener) {
        this.context = context;
        this.optionList = optionList;
        this.rightOption = rightOption;
        this.givenOption = givenOption;
        this.listener = listener;


        optionNumberList.add("A");
        optionNumberList.add("B");
        optionNumberList.add("C");
        optionNumberList.add("D");
        optionNumberList.add("E");

        try {
            for (int i = 0; i < optionList.size(); i++) {
                totalAnswer = totalAnswer + Integer.parseInt(optionList.get(i).getTotalAnswer());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer_options, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        viewHolders.add(holder);

        holder.tvAnswer.setText(optionList.get(position).getOption());
        holder.tvOption.setText(optionNumberList.get(position));

        if (!TextUtils.isEmpty(givenOption)) {
            int perUserPercentage = 100 / totalAnswer;
            if (TextUtils.isEmpty(rightOption)) {
                holder.tvPercentage.setVisibility(View.VISIBLE);
                holder.tvPercentage.setText(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()) + "%");
                holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.grey_progress_drawable));
                }
            } else {
                holder.tvPercentage.setVisibility(View.GONE);
                if (givenOption.equalsIgnoreCase(rightOption) && givenOption.equalsIgnoreCase(optionList.get(position).getId())) {
                    holder.progressBar.setSecondaryProgress(100);
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.right_vector_icon);
                    holder.tvOption.setTextColor(Color.parseColor("#000000"));
                    holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.green_progress_drawable));
                    }
                } else if (!givenOption.equalsIgnoreCase(rightOption) && givenOption.equalsIgnoreCase(optionList.get(position).getId())) {
                    holder.progressBar.setSecondaryProgress(100);
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                    holder.tvOption.setTextColor(Color.parseColor("#000000"));
                    holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.red_circle));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                    }
                } else if (rightOption.equalsIgnoreCase(optionList.get(position).getId())) {
                    holder.progressBar.setSecondaryProgress(100);
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.right_vector_icon);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.green_progress_drawable));
                    }
                }
            }
        }

        holder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(givenOption)) {
                    totalAnswer = totalAnswer + 1;
                    int perUserPercentage = 100 / totalAnswer;
                    int addCount = Integer.parseInt(optionList.get(position).getTotalAnswer()) + 1;
                    optionList.get(position).setTotalAnswer("" + addCount);

                    givenOption = optionList.get(position).getId();
                    notifyDataSetChanged();
                    listener.onProgressClickListener(position, givenOption);
                }
            }
        });

        /*if (!TextUtils.isEmpty(givenOption)) {
            if (!TextUtils.isEmpty(rightOption)) {
                int perUserPercentage = 100 / totalAnswer;
                holder.tvPercentage.setVisibility(View.GONE);
                if (rightOption.equalsIgnoreCase(optionList.get(position).getId())) {
                    //holder.progressBar.setSecondaryProgress(100);
                    if (Integer.parseInt(optionList.get(position).getTotalAnswer()) == 0) {
                        holder.progressBar.setSecondaryProgress(100);
                    } else {
                        holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                    }
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.right_vector_icon);

                    holder.tvOption.setTextColor(Color.parseColor("#000000"));
                    holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.green_circle));
                } else if (!givenOption.equalsIgnoreCase(rightOption) && givenOption.equalsIgnoreCase(optionList.get(position).getId())) {
                    //holder.progressBar.setSecondaryProgress(100);
                    holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                    }

                    holder.tvOption.setTextColor(Color.parseColor("#000000"));
                    holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.red_circle));
                } else {
                    holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                    holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                    holder.ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                    }
                }
            } else {
                int perUserPercentage = 100 / totalAnswer;
                holder.tvPercentage.setVisibility(View.VISIBLE);
                holder.tvPercentage.setText(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()) + "%");
                holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.grey_progress_drawable));
                }
            }
        }


        holder.progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(givenOption)) {
                    if (!TextUtils.isEmpty(rightOption)) {

                        totalAnswer = totalAnswer + 1;
                        int perUserPercentage = 100 / totalAnswer;
                        int addCount = Integer.parseInt(optionList.get(position).getTotalAnswer()) + 1;
                        optionList.get(position).setTotalAnswer("" + addCount);

                        if (rightOption.equalsIgnoreCase(optionList.get(position).getId())) {
                            holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                            holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                            holder.ivRightOrWrong.setImageResource(R.drawable.right_vector_icon);

                            holder.tvOption.setTextColor(Color.parseColor("#000000"));
                            holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.green_circle));

                            for (int i = 0; i < optionList.size(); i++) {
                                if (!rightOption.equalsIgnoreCase(optionList.get(i).getId())) {
                                    viewHolders.get(i).progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(i).getTotalAnswer()));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        viewHolders.get(i).progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                                    }
                                    viewHolders.get(i).ivRightOrWrong.setVisibility(View.VISIBLE);
                                    viewHolders.get(i).ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                                }
                            }
                        } else {
                            holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                            }

                            holder.tvOption.setTextColor(Color.parseColor("#000000"));
                            holder.llOption.setBackground(context.getResources().getDrawable(R.drawable.red_circle));

                            holder.ivRightOrWrong.setVisibility(View.VISIBLE);
                            holder.ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                            for (int i = 0; i < optionList.size(); i++) {
                                if (rightOption.equalsIgnoreCase(optionList.get(i).getId())) {
                                    if (Integer.parseInt(optionList.get(i).getTotalAnswer()) == 0) {
                                        viewHolders.get(i).progressBar.setSecondaryProgress(100);
                                    } else {
                                        viewHolders.get(i).progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(i).getTotalAnswer()));
                                    }
                                    viewHolders.get(i).ivRightOrWrong.setVisibility(View.VISIBLE);
                                    viewHolders.get(i).ivRightOrWrong.setImageResource(R.drawable.right_vector_icon);
                                } else {
                                    viewHolders.get(i).progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(i).getTotalAnswer()));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        viewHolders.get(i).progressBar.setProgressDrawable(context.getDrawable(R.drawable.red_progress_drawable));
                                    }

                                    viewHolders.get(i).ivRightOrWrong.setVisibility(View.VISIBLE);
                                    viewHolders.get(i).ivRightOrWrong.setImageResource(R.drawable.wrong_vector_icon);
                                }
                            }
                        }
                        givenOption = optionList.get(position).getId();
                    } else {
                        holder.ivRightOrWrong.setVisibility(View.GONE);
                        totalAnswer = totalAnswer + 1;
                        int perUserPercentage = 100 / totalAnswer;

                        int addCount = Integer.parseInt(optionList.get(position).getTotalAnswer()) + 1;
                        optionList.get(position).setTotalAnswer("" + addCount);

                        for (int i = 0; i < optionList.size(); i++) {
                            if (Integer.parseInt(optionList.get(i).getTotalAnswer()) == 0) {
                                viewHolders.get(i).progressBar.setSecondaryProgress(0);
                                viewHolders.get(i).tvPercentage.setVisibility(View.VISIBLE);
                                viewHolders.get(i).tvPercentage.setText("0%");
                            } else {
                                viewHolders.get(i).progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(i).getTotalAnswer()));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    viewHolders.get(i).progressBar.setProgressDrawable(context.getDrawable(R.drawable.grey_progress_drawable));
                                }
                                viewHolders.get(i).tvPercentage.setVisibility(View.VISIBLE);
                                viewHolders.get(i).tvPercentage.setText(perUserPercentage * Integer.parseInt(optionList.get(i).getTotalAnswer()) + "%");
                            }
                        }

                        if (Integer.parseInt(optionList.get(position).getTotalAnswer()) == 0) {
                            holder.progressBar.setSecondaryProgress(perUserPercentage);
                            holder.tvPercentage.setVisibility(View.VISIBLE);
                            holder.tvPercentage.setText("0%");
                        } else {
                            holder.progressBar.setSecondaryProgress(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.progressBar.setProgressDrawable(context.getDrawable(R.drawable.grey_progress_drawable));
                        }
                        holder.tvPercentage.setVisibility(View.VISIBLE);
                        holder.tvPercentage.setText(perUserPercentage * Integer.parseInt(optionList.get(position).getTotalAnswer()) + "%");

                        givenOption = optionList.get(position).getId();
                    }

                    listener.onProgressClickListener(position, givenOption);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAnswer;
        private final TextView tvOption;
        private final TextView tvPercentage;
        private final LinearLayout llOption;
        private final ImageView ivRightOrWrong;
        private final ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAnswer = (TextView) itemView.findViewById(R.id.tvAnswer);
            tvOption = (TextView) itemView.findViewById(R.id.tvOption);
            tvPercentage = (TextView) itemView.findViewById(R.id.tvPercentage);
            llOption = (LinearLayout) itemView.findViewById(R.id.llOption);
            ivRightOrWrong = (ImageView) itemView.findViewById(R.id.ivRightOrWrong);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public interface ProgressListener {
        void onProgressClickListener(int position, String userGivenOption);
    }

}