package com.example.classchat.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.classchat.Fragment.Fragment_ClassBox;
import com.example.classchat.Fragment.Fragment_Forum;
import com.example.classchat.Fragment.Fragment_SelfInformationCenter;
import com.example.classchat.Fragment.Fragment_Shopping;
import com.example.classchat.R;
import com.example.classchat.Util.SharedUtil;
import com.example.classchat.Util.Util_NetUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    //    private Toolbar mToolbar;
    private BottomNavigationView mBottomNavigationView;
    private int lastIndex;

    List<Fragment> mFragments;
    AlertDialog builder=null;

    private String correctId;
    private boolean isAuthentation;
    private String password;
    private String imageUrl;
    private String nickName;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("温馨提示：")
                    .setMessage("您是否要退出程序？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    MainActivity.this.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    builder.dismiss();
                                }
                            }).show();
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        correctId = intent.getStringExtra("userId");
        getUserInfo();

//        initView();
        initBottomNavigation();
        initData();
    }

//    public void initView() {
//        mToolbar = findViewById(R.id.toolbar);
//
//    }

    public void initData() {
//        setSupportActionBar(mToolbar);
        mFragments = new ArrayList<>();
        mFragments.add(new Fragment_ClassBox());
        mFragments.add(new Fragment_Forum());
        mFragments.add(new Fragment_Shopping());
        mFragments.add(new Fragment_SelfInformationCenter());
        // 初始化展示MessageFragment
        setFragmentPosition(0);
    }

    public void initBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.bv_bottomNavigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_classtable:
                        setFragmentPosition(0);
                        break;
                    case R.id.menu_forum:
                        setFragmentPosition(1);
                        break;
                    case R.id.menu_market:
                        setFragmentPosition(2);
                        break;
                    case R.id.menu_home:
                        setFragmentPosition(3);
                        break;
                    default:
                        break;
                }
                // 这里注意返回true,否则点击失效
                return true;
            }
        });
    }


    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.ll_frameLayout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    public String getId() {
        return correctId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    private void getUserInfo() {
        RequestBody requestBody = new FormBody.Builder()
                .add("userId", correctId)
                .build();

        Util_NetUtil.sendOKHTTPRequest("", requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                JSONObject jsonObject = JSON.parseObject(response.toString());
                nickName = jsonObject.getString("nickname");
                imageUrl = jsonObject.getString("ico");
                isAuthentation = Boolean.parseBoolean(jsonObject.getString("authentationstatus"));
            }
        });
    }
}