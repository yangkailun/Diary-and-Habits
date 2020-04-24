package com.example.hp.test25.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.hp.test25.R;
import com.example.hp.test25.object.Budget;
import com.example.hp.test25.object.Question;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by HP on 2018-04-22.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context mContext;
    private List<Question> mQuestionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView questionView;
        CardView questionCardView;

        public ViewHolder(View view){
            super(view);
            questionCardView = view.findViewById((R.id.question_cardview));
            questionView = view.findViewById((R.id.question_textview));
        }

    }
    public QuestionAdapter(List<Question> questions){
        Collections.shuffle(questions);
        mQuestionList = questions;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.questionCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(mContext)
                        .title("删除")
                        .content("确定删除吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int position = holder.getAdapterPosition();
                                Question question = mQuestionList.get(position);
                                DataSupport.deleteAll(Question.class,"id=?",question.getId()+"");
                                mQuestionList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        }).show();
                return false;
            }
        });

        holder.questionCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budgetStr = "";
                int position = holder.getAdapterPosition();
                Question question = mQuestionList.get(position);

                budgetStr=question.getQestion();
                new MaterialDialog.Builder(mContext)
                        .title("灵魂拷问")
                        .content(budgetStr)
                        .positiveText("自省了")
                        .show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Question question = mQuestionList.get(position);
        holder.questionView.setText(question.getQestion());
    }

    @Override
    public int getItemCount(){
        return mQuestionList.size();
    }
}
