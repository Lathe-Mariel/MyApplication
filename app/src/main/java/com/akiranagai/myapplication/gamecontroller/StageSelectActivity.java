package com.akiranagai.myapplication.gamecontroller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.constraint.solver.widgets.Helper;
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
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;

import com.akiranagai.myapplication.R;

import java.io.File;
import java.util.ArrayList;

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

    //設定変数群
    /**
     * 常時　入力用　十字が画面表示されるか
     */
    private boolean alwaysDrawCross;
    /**
     * ゲームループ インターバル値　ゼロモード
     */
    private boolean maxMode;


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
                int stageNumber = mViewPager.getCurrentItem();
                if(stageNumber < 0 || stageNumber > SectionsPagerAdapter.PAGES-1)return;
                Intent intent = new Intent(StageSelectActivity.this, GLActivity.class);
                intent.putExtra("STAGE_NUMBER", stageNumber);
                intent.putExtra("ALWAYS_DRAW_CROSS", alwaysDrawCross);
                intent.putExtra("MAX_MODE", maxMode);
                StageSelectActivity.this.startActivityForResult(intent,1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
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
                int stageNumber = intent.getIntExtra("stageNumber", 0);
                PlaceholderFragment.checkHighScore(stageNumber, this);
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
        static ListView[] list = new ListView[SectionsPagerAdapter.PAGES];

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

        public static int[] checkHighScore(int stageNumber, Activity activity){
            if(stageNumber == 0)return new int[]{0};
            SQLiteAccess data = new SQLiteAccess(activity);
            Cursor cursor = data.getAllNotes(stageNumber);
            cursor.moveToFirst();
            int highScore=0;
            int count=0;
            int sum=0;
            if(cursor.getCount() !=0) {
                do {
                    int score = cursor.getInt(cursor.getColumnIndex("score"));
                    highScore = score > highScore ? score : highScore;
                    count++;
                    sum += score;
                } while (cursor.moveToNext());
            }
            int average = 0;
            try {
                average = sum / count;
            }catch(ArithmeticException e){
                e.printStackTrace();

            }
            data.close();
            return new int[] {highScore, count, average};  //最高点, プレイ回数, 累積点
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView=null;
            ImageView imageView=null;
            ArrayList<String> messages=null;

            int selector = getArguments().getInt(ARG_SECTION_NUMBER);
            if(selector==StageSelectActivity.SectionsPagerAdapter.PAGES-1){  //セッティングページ生成
                rootView = inflater.inflate(R.layout.setting, container, false);
                Button clearButton = (Button)rootView.findViewById(R.id.clearButton);
                clearButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            SQLiteOpenHelper helper = new SQLiteAccess(getActivity());
                            SQLiteDatabase.deleteDatabase(getContext().getDatabasePath(helper.getDatabaseName()));
                        } catch (Exception e) {
                        }
                    }
                });

            }else {
                rootView = inflater.inflate(R.layout.fragment_stage_select, container, false);

                imageView = rootView.findViewById(R.id.imageView);
                list[selector] = rootView.findViewById(R.id.movingList);
                messages = new ArrayList<String>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, messages);
                list[selector].setAdapter(adapter);
            }

            int score[] = checkHighScore(selector, getActivity());  //データベースの該当ステージテーブル サーベイ

            switch(selector){
                case 0:
                    messages.add("How to play");
                    imageView.setImageResource(R.drawable.stage0);
                    break;
                case 1:  //桜
                    messages.add("Difficulty: 5Level");
                    messages.add("Sand Box");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    break;
                case 2:  //神社夜
                    messages.add("Difficulty: 11Level");
                    messages.add("Shrine");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    break;
                case 3:  //アクア
                    messages.add("Difficulty: 250Level");
                    messages.add("Aquarium");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    //imageView.setImageResource(R.drawable.stage3);
                    break;
                case 4:  //欧州
                    messages.add("Difficulty: 2Level");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    imageView.setImageResource(R.drawable.stage4);
                    break;
                case 5:  //宇宙
                    messages.add("Difficulty: 100Level");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    list[selector].setBackgroundColor(Color.argb(100,140,140,255));
                    imageView.setImageResource(R.drawable.stage5);
                    break;
                case 6:
                    messages.add("Difficulty: 700Level");
                    messages.add("池袋");
                    messages.add("Hi Score: " + score[0]);
                    messages.add("Average: " + score[2]);
                    messages.add("Play Times: " + score[1]);
                    //list[selector].setBackgroundColor(Color.argb(100,140,140,255));
                    imageView.setImageResource(R.drawable.ike);
                    break;
                case 7:

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
        static final int PAGES = 8;

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
            return PAGES;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, final int position, Object object){
            super.setPrimaryItem(container, position, object);

            if(page == position)return;  //setPrimaryItem()はシステムから３回呼び出されるので、２，３回目は何もしない
            if(page == PAGES){  //設定項目　保存

                alwaysDrawCross = ((RadioButton)findViewById(R.id.drawRadio)).isChecked();
                maxMode = ((Switch)findViewById(R.id.MaxSwitch)).isChecked();
            }
            final Handler handler = new Handler();
            //前ページのList隠し処理
            for(int i = 0; i < PAGES; i++){
                if(PlaceholderFragment.list[i] != null)
                PlaceholderFragment.list[i].setY(-PlaceholderFragment.list[i].getHeight());
            }

            page = position;
            if(position ==PAGES-1)return;  //最終頁はリストビュー　スライド無し
            final float hide_y = -PlaceholderFragment.list[position].getHeight();

            new Thread(new Runnable(){
                float y = hide_y;
                public void run(){
                    while(y < 0) {
                        final ListView list = PlaceholderFragment.list[position];
                        handler.post(new Runnable(){
                            public void run(){
                                list.setY(y);
                            }
                        });

                        //Log.d("messaged", "position y: " + y);
                        y += 8;  //１ステップの移動量Y
                        try {
                            Thread.sleep(10);  //インターバル
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            //Log.d("messaged", "setPrimaryItem    position: " + position);
            System.gc();
        }

        public void destroyItem (ViewGroup container,
                          int position,
                          Object object){
            //Log.d("messaged", "destroyItem    posotion: " +position);
        }
    }
}
