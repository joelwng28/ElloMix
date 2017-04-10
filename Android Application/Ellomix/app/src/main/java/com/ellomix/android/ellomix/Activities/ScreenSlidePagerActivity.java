package com.ellomix.android.ellomix.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ellomix.android.ellomix.Fragments.ChatFragment;
import com.ellomix.android.ellomix.Fragments.ChatListFragment;
import com.ellomix.android.ellomix.Fragments.ProfileFragment;
import com.ellomix.android.ellomix.Fragments.SearchFragment;
import com.ellomix.android.ellomix.Fragments.TimelineFragment;
import com.ellomix.android.ellomix.R;
import com.ellomix.android.ellomix.Style.CustomViewPager;

/**
 * Created by abetorres on 12/10/16.
 */

public class ScreenSlidePagerActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 4;

    private CustomViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private int[] imageResId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_messsages,
            R.drawable.ic_profile
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.fragment_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mPager);
        for (int i = 0; i < NUM_PAGES; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                //TODO: Missing Upload(2)
                case 0:
                    //timeline
                    return TimelineFragment.newInstance();
                case 1:
                    //search
                    return SearchFragment.newInstance();
//                case 2:
//                    //upload
//
                case 2:
                    // chat
                    return ChatListFragment.newInstance();
                case 3:
                    //profile
                    return ProfileFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            Drawable image = getResources().getDrawable(imageResId[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString("  ");
//            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//            sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
//        }

    }
}
