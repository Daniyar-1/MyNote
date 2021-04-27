package com.example.mynote.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.mynote.R;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;


public class BoardFragment extends Fragment implements PageAdapter.OnBoardClickListeners {

    private SpringDotsIndicator springDotsIndicator;
    private PageAdapter adapter;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new PageAdapter();
        viewPager.setAdapter(adapter);
        adapter.setOnBoardClickListeners(this);
        springDotsIndicator = view.findViewById(R.id.spring_dots_indicator);
        springDotsIndicator.setViewPager(viewPager);



        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });

    }

    @Override
    public void onStartClick() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_boardFragment_to_phoneFragment);
    }

    @Override
    public void onSkipClick() {
        viewPager.setCurrentItem(2);
    }

    @Override
    public void onNextClick() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }
}