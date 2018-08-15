// Generated code from Butter Knife. Do not modify!
package com.borisruzanov.russianwives.Adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.borisruzanov.russianwives.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatsAdapter$RequestsItemHolder_ViewBinding implements Unbinder {
  private ChatsAdapter.RequestsItemHolder target;

  @UiThread
  public ChatsAdapter$RequestsItemHolder_ViewBinding(ChatsAdapter.RequestsItemHolder target,
      View source) {
    this.target = target;

    target.name = Utils.findRequiredViewAsType(source, R.id.item_request_name, "field 'name'", TextView.class);
    target.country = Utils.findRequiredViewAsType(source, R.id.item_request_country, "field 'country'", TextView.class);
    target.age = Utils.findRequiredViewAsType(source, R.id.item_request_age, "field 'age'", TextView.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.item_request_image, "field 'image'", ImageView.class);
    target.btnAccept = Utils.findRequiredViewAsType(source, R.id.item_request_btn_accept, "field 'btnAccept'", ImageView.class);
    target.btnDecline = Utils.findRequiredViewAsType(source, R.id.item_request_btn_decline, "field 'btnDecline'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChatsAdapter.RequestsItemHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.name = null;
    target.country = null;
    target.age = null;
    target.image = null;
    target.btnAccept = null;
    target.btnDecline = null;
  }
}
