// Generated code from Butter Knife. Do not modify!
package com.borisruzanov.russianwives.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.borisruzanov.russianwives.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FriendActivity_ViewBinding implements Unbinder {
  private FriendActivity target;

  @UiThread
  public FriendActivity_ViewBinding(FriendActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FriendActivity_ViewBinding(FriendActivity target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_list_friendDescription, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FriendActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
  }
}
