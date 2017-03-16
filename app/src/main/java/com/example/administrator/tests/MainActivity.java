package com.example.administrator.tests;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.example.administrator.tests.Fragmnt.BooksFragment;
import com.example.administrator.tests.Fragmnt.GitHubFragment;
import com.example.administrator.tests.Fragmnt.LikeFragment;
import com.example.administrator.tests.Fragmnt.RecordFragment;
import com.example.mylibrary.BottomNavigationItem;
import com.example.mylibrary.BottomNavigationView;
import com.example.mylibrary.OnBottomNavigationItemClickListener;



public class MainActivity extends FragmentActivity{

    private BottomNavigationView bottomNavigationView;
//    private List<Fragment> fragmentList;
    RecordFragment recordFragment;
    LikeFragment likeFragment;
    BooksFragment booksFragment;
    GitHubFragment gitHubFragment;
    // 定义FragmentManager对象管理器
//    private FragmentManager fm;
//    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fragmentManager = getSupportFragmentManager();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);



//        recordFragment = new RecordFragment();
//        likeFragment = new LikeFragment();
//        booksFragment = new BooksFragment();
//        gitHubFragment = new GitHubFragment();

        int[] image = {R.mipmap.home, R.mipmap.photo,
                R.mipmap.chat, R.mipmap.message};
        int[] color = {ContextCompat.getColor(this, R.color.fourthColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.fourthColor)};

        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("Record", color[0], image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("Like", color[1], image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("Books", color[2], image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("GitHub", color[3], image[3]);


        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        recordFragment = RecordFragment.newInstance();
                        break;
                    case 1:
                        likeFragment = LikeFragment.newInstance();
                        break;
                    case 2:
                        booksFragment = BooksFragment.newInstance();
                        break;
                    case 3:
                        gitHubFragment = GitHubFragment.newInstance();
                        break;
                }
            }
        });
    }

}

