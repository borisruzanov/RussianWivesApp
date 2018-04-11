package com.borisruzanov.russianwives.zTEST;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.borisruzanov.russianwives.R;

import java.util.Random;

public class PageFragment extends Fragment{
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int backColor;

    static PageFragment newInstance(int page){
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);

        Random rnd = new Random();
        backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_user_info_input, null);
        TextView tvPage = (TextView) view.findViewById(R.id.fragment_tv_user_info_input);
        tvPage.setText("Page " + pageNumber);
        tvPage.setBackgroundColor(backColor);

        return view;
    }
}
