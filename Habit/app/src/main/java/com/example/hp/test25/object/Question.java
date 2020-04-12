package com.example.hp.test25.object;

import org.litepal.crud.DataSupport;

/**
 * Created by hp on 2020/4/9.
 */

public class Question extends DataSupport {
    private String qestion;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQestion() {
        return qestion;
    }

    public void setQestion(String qestion) {
        this.qestion = qestion;
    }
}
