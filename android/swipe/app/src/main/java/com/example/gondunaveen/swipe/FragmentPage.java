package com.example.gondunaveen.swipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentPage extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View view;
    Bundle bundle = getArguments();
    int pageNumber=bundle.getInt("pageNumber");

    view = inflater.inflate(R.layout.page_fragment_layout,container,false);
        TextView textview = (TextView)view.findViewById(R.id.pageNumber);
        textview.setText(Integer.toString(pageNumber));

        return view;
// return super.onCreateView(inflater, container, savedInstanceState);
    }
}
