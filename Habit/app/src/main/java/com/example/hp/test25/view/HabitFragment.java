package com.example.hp.test25.view;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.hp.test25.R;
import com.example.hp.test25.adapter.BudgetAdapter;
import com.example.hp.test25.adapter.HabitAdapter;
import com.example.hp.test25.adapter.QuestionAdapter;
import com.example.hp.test25.object.Budget;
import com.example.hp.test25.object.Habit;
import com.example.hp.test25.object.Question;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitFragment extends Fragment {

    public static HabitAdapter adapter;
    private List<Habit> habitList = new ArrayList<>();
    private FloatingActionButton habitAdd, habitRestart;
    private EditText habitEdit;

    public HabitFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit, container, false);

        habitList = DataSupport.findAll(Habit.class);

        RecyclerView recyclerView = view.findViewById(R.id.habit_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new HabitAdapter(habitList);
        recyclerView.setAdapter(adapter);


        habitAdd = view.findViewById(R.id.habit_add);
        habitRestart = view.findViewById((R.id.habit_restart));
        habitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(habitList.isEmpty()){
                    LitePal.getDatabase();
                }
                showHabitDialog(adapter);
            }
        });
        habitRestart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //轻松把所有的isFinish设置为初始值0
                Habit habit = new Habit();
                habit.setToDefault("isFinish");
                habit.updateAll();
                habitList.clear();//必须先清空，再添加，这个List不能变
                habitList.addAll(DataSupport.findAll(Habit.class));//不加这一句，只是更新数据库，下面的一句不会立即刷新页面
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void showHabitDialog(final HabitAdapter adapter){
        final Habit habit = new Habit();
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("习惯")
                .customView(R.layout.dialog_habit,true)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(TextUtils.isEmpty(habitEdit.getText())){
                            Toast.makeText(getActivity(),"信息不全",Toast.LENGTH_SHORT).show();
                        }else {
                            habit.setHabit(habitEdit.getText().toString());

                            //添加唯一主键
                            SharedPreferences pref = getActivity().getSharedPreferences("habit_id", Context.MODE_PRIVATE);
                            int habitId = pref.getInt("id",0);
                            habit.setId(habitId);
                            habit.setIsFinish(0);
                            habit.save();
                            habitList.add(habit);
                            adapter.notifyDataSetChanged();

                            SharedPreferences.Editor editor = pref.edit();  //主键加1
                            editor.putInt("id",++habitId);
                            editor.apply();
                        }
                    }
                })
                .build();
        habitEdit = dialog.getCustomView().findViewById(R.id.habit);

        dialog.show();

    }

}
