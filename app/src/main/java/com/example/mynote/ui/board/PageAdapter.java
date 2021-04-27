package com.example.mynote.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mynote.R;
import com.example.mynote.utils.Prefs;

public class PageAdapter extends PagerAdapter {

    private String[] titles = new String[]{"TO-DO LIST!", "SHARE YOUR CRAZY IDEA^_^", "FLEXIBILITY"};
    private String[] descriptions = new String[]{
            "Here you can write down something important or make a schedule for tomorrow:)",
            "Also, You can easily share with your report, list or schedule and it's convenient ",
            "Your note with you at home, at work, even at the resort"};
    private OnBoardClickListeners onBoardClickListeners;

    public interface OnBoardClickListeners {
        void onStartClick();

        void onSkipClick();

        void onNextClick();
    }

    public void setOnBoardClickListeners(OnBoardClickListeners onBoardClickListeners) {
        this.onBoardClickListeners = onBoardClickListeners;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.page_board, container, false);

        TextView textTitle = view.findViewById(R.id.textTitle);
        textTitle.setText(titles[position]);

        TextView textDesc = view.findViewById(R.id.textDesc);
        textDesc.setText(descriptions[position]);

        Button btnSkip = view.findViewById(R.id.btnSkip);

        Button btnStart = view.findViewById(R.id.btnGetStarted);

        ImageButton btnNext = view.findViewById(R.id.btnNext);



        LottieAnimationView lottieAnimationView = view.findViewById(R.id.anim_board1);

        switch (position) {
            case 0:
                lottieAnimationView.playAnimation();

                btnStart.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.VISIBLE);

                break;
            case 1:
                lottieAnimationView.setAnimation("board2.json");
                lottieAnimationView.playAnimation();

                btnStart.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.VISIBLE);


                break;
            case 2:
                lottieAnimationView.setAnimation("board3.json");
                lottieAnimationView.playAnimation();

                btnStart.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                break;

        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Prefs(container.getContext()).setShown();
                onBoardClickListeners.onStartClick();
            }

        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardClickListeners.onNextClick();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardClickListeners.onSkipClick();
            }
        });
        container.addView(view);
        return view;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
