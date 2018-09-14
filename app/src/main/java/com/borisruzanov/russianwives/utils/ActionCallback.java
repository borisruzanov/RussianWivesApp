package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.ActionModel;

import java.util.List;

public interface ActionCallback {

    void getActionList(List<ActionModel> actionModels);

}
