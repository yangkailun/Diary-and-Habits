package com.example.hp.test25.object;

import org.litepal.crud.DataSupport;

/**
 * Created by hp on 2020/4/9.
 */

public class Habit extends DataSupport {
    private String habit;
    private int id, isFinish;

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }
}
