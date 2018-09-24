// Generated code from Butter Knife. Do not modify!
package com.borisruzanov.russianwives.ui.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.borisruzanov.russianwives.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FilterDialogFragment_ViewBinding implements Unbinder {
  private FilterDialogFragment target;

  private View view2131296311;

  private View view2131296307;

  @UiThread
  public FilterDialogFragment_ViewBinding(final FilterDialogFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.button_search, "method 'onSearchClicked'");
    view2131296311 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSearchClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_cancel, "method 'onCancelClicked'");
    view2131296307 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCancelClicked();
      }
    });
    target.spinners = Utils.listOf(
        Utils.findRequiredViewAsType(source, R.id.spinner_gender, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_age, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_country, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_relationship_statuses, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_body_types, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_ethnicities, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_faith_types, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_smoke_statuses, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_drink_statuses, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_have_kids_statuses, "field 'spinners'", Spinner.class), 
        Utils.findRequiredViewAsType(source, R.id.spinner_want_kids_statuses, "field 'spinners'", Spinner.class));
  }

  @Override
  @CallSuper
  public void unbind() {
    FilterDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.spinners = null;

    view2131296311.setOnClickListener(null);
    view2131296311 = null;
    view2131296307.setOnClickListener(null);
    view2131296307 = null;
  }
}
