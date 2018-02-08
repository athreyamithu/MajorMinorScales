package com.mathreya.majorminorscales;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mithileshathreya on 2/7/18.
 */

public class WelcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;

    private LinearLayout layoutDots;
    private TextView[] dots;
    private int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        layouts = new int[] {
                R.layout.activity_welcome_page1,
                R.layout.activity_welcome_page2,
                R.layout.activity_welcome_page3 };
        //addBottomDots(0);

    }

//    public class WelcomeActivityAdapter extends PagerAdapter {
//        private LayoutInflater layoutInflater;
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = layoutInflater.inflate(layouts[position], container, false);
//            container.addView(view);
//            return view;
//        }
//    }
}
