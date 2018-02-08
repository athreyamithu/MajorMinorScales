package com.mathreya.majorminorscales;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        layoutDots = (LinearLayout) findViewById(R.id.layoutDots);
        nextButton = (Button) findViewById(R.id.btn_next);
        layouts = new int[] {
                R.layout.activity_welcome_page1,
                R.layout.activity_welcome_page2,
                R.layout.activity_welcome_page3 };
        addBottomDots(0);
        viewPager.setAdapter(new WelcomeActivityAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                if (position == layouts.length - 1) {
                    nextButton.setText(getString(R.string.welcome_btn_gotIt));
                } else nextButton.setText(getString(R.string.welcome_btn_next));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int arg0) {}
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < layouts.length - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else launchHomeScreen();
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] inactiveDots = getResources().getIntArray(R.array.array_dot_inactive);
        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            } else dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(inactiveDots[currentPage]);
            layoutDots.addView(dots[i]);
        }
        if (dots.length > 0) dots[currentPage].setTextColor(getResources().getColor(R.color.dot_light_screen));
    }

    private void launchHomeScreen() {
        getSharedPreferences(getString(R.string.shared_preferences), MODE_PRIVATE)
                .edit()
                .putBoolean(getString(R.string.preference_first_time), false)
                .apply();
        startActivity(new Intent(WelcomeActivity.this, NavActivity.class));
        finish();
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
