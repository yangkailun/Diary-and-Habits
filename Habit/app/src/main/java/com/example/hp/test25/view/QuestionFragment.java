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
import com.example.hp.test25.adapter.QuestionAdapter;
import com.example.hp.test25.object.Budget;
import com.example.hp.test25.object.Question;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    public static QuestionAdapter adapter;
    private List<Question> questionList = new ArrayList<>();
    private FloatingActionButton questionAdd;
    private EditText questionEdit;

    public QuestionFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        questionList = DataSupport.findAll(Question.class);

        RecyclerView recyclerView = view.findViewById(R.id.question_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(adapter);


        questionAdd = view.findViewById(R.id.question_add);
        questionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionList.isEmpty()){
                    LitePal.getDatabase();
                }
                showQuestionDialog(adapter);
            }
        });

        return view;
    }

    private void showQuestionDialog(final QuestionAdapter adapter){
        final Question question = new Question();
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("灵魂拷问")
                .customView(R.layout.dialog_question,true)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(TextUtils.isEmpty(questionEdit.getText())){
                            Toast.makeText(getActivity(),"信息不全",Toast.LENGTH_SHORT).show();
                        }else {
                                question.setQestion(questionEdit.getText().toString());

                                //添加唯一主键
                                SharedPreferences pref = getActivity().getSharedPreferences("question_id", Context.MODE_PRIVATE);
                                int questionId = pref.getInt("id",0);
                                question.setId(questionId);
                                question.save();
                                questionList.add(question);
                                adapter.notifyDataSetChanged();

                                SharedPreferences.Editor editor = pref.edit();  //主键加1
                                editor.putInt("id",++questionId);
                                editor.apply();
                            }
                        }
                })
                .build();
        questionEdit = dialog.getCustomView().findViewById(R.id.question);

        dialog.show();

    }

}
