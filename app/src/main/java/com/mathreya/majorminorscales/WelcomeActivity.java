package com.mathreya.majorminorscales;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
        viewPager.setAdapter(new WelcomeActivityAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //addBottomDots(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
    }

    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[layouts.length];
        int[] activeDots = getResources().getIntArray(R.array.array_dot_active),
         inactiveDots = getResources().getIntArray(R.array.array_dot_inactive);
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            //dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
        }
    }

    public class WelcomeActivityAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) { return view == obj; }

        @Override
        public int getCount() { return layouts.length; }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
