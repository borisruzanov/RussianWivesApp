// Generated code from Butter Knife. Do not modify!
package com.borisruzanov.russianwives.ui.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.borisruzanov.russianwives.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FriendsFragment_ViewBinding implements Unbinder {
  private FriendsFragment target;

  @UiThread
  public FriendsFragment_ViewBinding(FriendsFragment target, View source) {
    this.target = target;

    target.recyclerRequests = Utils.findRequiredViewAsType(source, R.id.friends_fragment_recycler_requests, "field 'recyclerRequests'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FriendsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerRequests = null;
  }
}
