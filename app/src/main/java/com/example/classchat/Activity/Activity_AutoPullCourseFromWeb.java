package com.example.classchat.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.classchat.Object.MySubject;
import com.example.classchat.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity_AutoPullCourseFromWeb extends AppCompatActivity {
    private WebView webView;
    private String cookie;
    //handler处理反应回来的信息
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(final Message msg){
            switch (msg.what){
                case 1:
                    OkHttpClient client =new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("xnm" ,"2019" )
                            .add("xqm" , "3")
                            .build();
                    Request request =new Request.Builder()
                            .url("http://xsjw2018.scuteo.com/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151")
                            .addHeader("Cookie" , cookie)
                            .post(body)
                            .build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            JSONObject object = JSON.parseObject(response.body().string());
                            JSONArray array = object.getJSONArray("kbList");
                            System.out.println(array.size());
                            //新建一个装subject的列表
                            List<MySubject> mysubjects = new ArrayList<>();
                            //下面的过程就是把这些课都转化成Mysubject对象
                            for(int i = 0 ; i< array.size() ; i++ ){
                                //从kbList中拿到一节课的JSON
                                JSONObject course = array.getJSONObject(i);
                                MySubject subject = new MySubject();
                                //获取课程名称
                                subject.setName(course.getString("kcmc"));

                                //获取课程开始和结束的节数
                                String[] lasttime = course.getString("jcor").split("-");
                                subject.setStart(Integer.parseInt(lasttime[0]));
                                subject.setStep(Integer.parseInt(lasttime[1])  - Integer.parseInt(lasttime[0])  + 1);

                                //获取课程授课教室
                                subject.setRoom(course.getString("cdmc"));
                                //获取老师的名字
                                subject.setTeacher(course.getString("xm"));
                                //获取上课周列表
                                String target = course.getString("zcd");
                                String[] list = target.split("周,");
                                List<Integer> weeklist = new ArrayList<>();
                                for(int k = 0 ; k< list.length ; k++){
                                    //最后一个处理的方式有点不太一样
                                    if( k == list.length -1){
                                        String[] sublist =  list[k].split("-");
                                        int start = Integer.parseInt(sublist[0]);
                                        int end = Integer.parseInt(sublist[1].split("周")[0]);
                                        for( int j = start ; j<=end ; j++)
                                            weeklist.add(j);
                                        break;
                                    }
                                    int start = Integer.parseInt(list[k].split("-")[0]);
                                    int end = Integer.parseInt(list[k].split("-")[1]);
                                    for(int j = start; j<= end ;j ++)
                                        weeklist.add(j);
                                }
                                subject.setWeekList(weeklist);

                                //获取上课是哪一天
                                subject.setDay(Integer.parseInt(course.getString("xqj")));
                                mysubjects.add(subject);

                            }
                            //TODO  这里把上面的 mysubjects 列表存进缓存就可以了

                            //TODO  这里发送信息给 handler
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);

                        }
                    });
                    break;

                case 2:
                    //TODO  这里需要向服务器发出这些课程 由服务器去更新

                    //TODO  发送信息给 handler
                    Message message = new Message();
                    message.what = 3;
                    handler.sendMessage(message);

                    break;
                case 3:
                    //TODO  结束等待转圈画面 跳转回课程表fragment 刷新其页面（广播）
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__auto_pull_course_from_web);
        webView = findViewById(R.id.auto_wv);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view , String url , Bitmap fa){
                super.onPageStarted(view ,url ,fa);
                if(!url.equals("https://sso.scut.edu.cn/cas/login?service=http%3A%2F%2Fxsjw2018.scuteo.com%2Fsso%2Fdriotlogin")){
                    //TODO 让等待的圈圈圈圈转起来
                }
            }

            @Override
            public void onPageFinished(WebView webView ,String url){
                if(!url.equals("https://sso.scut.edu.cn/cas/login?service=http%3A%2F%2Fxsjw2018.scuteo.com%2Fsso%2Fdriotlogin")){
                    CookieManager manager = CookieManager.getInstance();
                    System.out.println(webView.getUrl());
                    cookie = manager.getCookie(webView.getUrl());
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }else {
                    System.out.println("this is origin");
                }
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(true);
        webSettings.setDefaultFontSize(12);
        CookieManager manager = CookieManager.getInstance();
        webView.loadUrl("https://sso.scut.edu.cn/cas/login?service=http%3A%2F%2Fxsjw2018.scuteo.com%2Fsso%2Fdriotlogin");
    }
}
