package com.borisruzanov.russianwives.mvp.model.interactor;

import com.borisruzanov.russianwives.utils.IsDataLoadingCallback;
import com.borisruzanov.russianwives.utils.UsersListCallback;

/**
 * Created by Михаил on 05.05.2018.
 */

public interface ISearchInteractor {

    void getUsers(String nodeId, UsersListCallback usersListCallback, IsDataLoadingCallback isDataLoadingCallback);

    boolean getDataLoadingStatus();

    void setDataLoading(boolean isDataLoading);

}
