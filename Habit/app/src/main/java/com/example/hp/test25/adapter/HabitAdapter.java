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
import com.example.hp.test25.object.Habit;
import com.example.hp.test25.object.Question;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by HP on 2018-04-22.
 */

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {
    private Context mContext;
    private List<Habit> mHabitList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView habitView,finishView;
        CardView habitCardView;

        public ViewHolder(View view){
            super(view);
            habitCardView = view.findViewById((R.id.habit_cardview));
            habitView = view.findViewById((R.id.habit_textview));
            finishView = view.findViewById((R.id.finish_textview));
        }

    }
    public HabitAdapter(List<Habit> habits){
        mHabitList = habits;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if(mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.habit_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.habitCardView.setOnLongClickListener(new View.OnLongClickListener() {
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
                                Habit habit = mHabitList.get(position);
                                DataSupport.deleteAll(Habit.class,"id=?",habit.getId()+"");
                                mHabitList.remove(position);
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

        holder.finishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Habit habit = mHabitList.get(position);
                habit.setIsFinish(1);
                habit.updateAll("id = ?",""+habit.getId());
                notifyDataSetChanged();

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Habit habit = mHabitList.get(position);
        holder.habitView.setText(habit.getHabit());
        if(habit.getIsFinish() == 0){
            holder.finishView.setText("未完成");
        }else{
            holder.finishView.setText("完成");
        }
    }

    @Override
    public int getItemCount(){
        return mHabitList.size();
    }
}
