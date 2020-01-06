package com.borisruzanov.russianwives.eventbus;

import java.util.ArrayList;
import java.util.List;

public class ListStringEvent {
    ArrayList<String> list;

    public ListStringEvent(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
