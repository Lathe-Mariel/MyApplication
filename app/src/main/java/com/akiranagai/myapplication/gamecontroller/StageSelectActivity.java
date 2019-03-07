package com.akiranagai.myapplication.gamecontroller;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.akiranagai.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class StageSelectActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static ListView movingList;

    private static Handler mHandler;


    private int stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        movingList = findViewById(R.id.movingList);
        mHandler = new Handler();

        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StageSelectActivity.this, GLActivity.class);
                intent.putExtra("STAGE_NUMBER", mViewPager.getCurrentItem());
                StageSelectActivity.this.startActivityForResult(intent,1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(Build.VERSION.SDK_INT <= 10){
                    finish();
                }else if(Build.VERSION.SDK_INT >= 21){
                    finishAndRemoveTask();
                }
            }
        });
    }

    public void onActivityResult( int requestCode, int resultCode, Intent intent )
    {
        // startActivityForResult()の際に指定した識別コードとの比較
        if( requestCode == 1 ){

            // 返却結果ステータスとの比較
            if( resultCode == RESULT_OK ){

                // 返却されてきたintentから値を取り出す
                String str = intent.getStringExtra( "key" );
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_stage_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        static ListView[] list = new ListView[7];

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stage_select, container, false);
            int selector = getArguments().getInt(ARG_SECTION_NUMBER);
            ImageView imageView = rootView.findViewById(R.id.imageView);
            list[selector] = rootView.findViewById(R.id.movingList);
            ArrayList messages = new ArrayList<String>();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, messages);
            list[selector].setAdapter(adapter);

            switch(selector){
                case 0:
                    messages.add("Stage0");
                    messages.add("Level1");
                    break;
                case 1:
                    messages.add("Stage1");
                    break;
                case 2:
                    messages.add("Stage2");
                    break;
                case 3:
                    messages.add("Stage3");
                    messages.add("Level 250");
                    //imageView.setImageResource(R.drawable.stage3);
                    break;
                case 4:
                    messages.add("Level 2 室町用");
                    imageView.setImageResource(R.drawable.stage4);
                    break;
                case 5:
                    messages.add("How to play");
                    messages.add("Level 0");
                    imageView.setImageResource(R.drawable.stage5);
                    break;
                case 6:
                    messages.add("Stage6");
                    //imageView.setImageResource(R.drawable.stage6);
                    break;
            }
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int page;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 7;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, final int position, Object object){
            super.setPrimaryItem(container, position, object);
            if(page == position)return;
            final float hide_y = -PlaceholderFragment.list[position].getHeight();

            //前ページのList隠し処理
            for(int i = 0; i < 7; i++){
                if(PlaceholderFragment.list[i] != null)
                PlaceholderFragment.list[i].setY(-PlaceholderFragment.list[i].getHeight());
            }

            page = position;

            new Thread(new Runnable(){
                float y = hide_y;
                public void run(){
                    while(y < 0) {
                        ListView list = PlaceholderFragment.list[position];
                        list.setY(y);
                        Log.d("messaged", "position y: " + y);
                        y += 8;  //１ステップの移動量Y
                        try {
                            Thread.sleep(10);  //インターバル
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            Log.d("messaged", "setPrimaryItem    position: " + position);
        }

        public void destroyItem (ViewGroup container,
                          int position,
                          Object object){
            Log.d("messaged", "destroyItem    posotion: " +position);
        }
    }
}
