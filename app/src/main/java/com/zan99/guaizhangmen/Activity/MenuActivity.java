package com.zan99.guaizhangmen.Activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.zan99.guaizhangmen.Activity.Guai.GuaiActivity;
import com.zan99.guaizhangmen.Activity.Men.MenActivity;
import com.zan99.guaizhangmen.Activity.Zhang.ZhangActivity;
import com.zan99.guaizhangmen.Model.MenModel;
import com.zan99.guaizhangmen.R;
import com.zan99.guaizhangmen.Util.L;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/27.
 */

public class MenuActivity extends TabActivity {

    @BindView(R.id.tab_menu_message)
    TextView tab_menu_message;

    private TabHost tabHost;
    public static String uploadid="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        initView();
        //存储请求二维码的id
        uploadid();
        //请求memberid 和 二维码id
        init();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        //存储请求二维码的id
        uploadid();
        //请求memberid 和 二维码id
        init();
    }


    private void init(){
        SharedPreferences pref = getSharedPreferences(MenModel.FILENAME,MODE_PRIVATE);
        String memberId = L.decrypt(pref.getString("member_id",""),MenModel.LKEY);
        MenuActivity.uploadid = L.decrypt(pref.getString("uploadid",""),MenModel.LKEY);

        if(memberId!=""){
            MenModel.member_id = memberId;
        }


    }


    private void uploadid(){
        SharedPreferences.Editor editor = getSharedPreferences(MenModel.FILENAME, MODE_PRIVATE).edit();
        editor.putString("uploadid", L.encrypt(uploadid,MenModel.LKEY));
        editor.commit();
    }




    // 加载视图
    private void initView() {
//        tab_menu_message.setVisibility(View.VISIBLE);

        // 加载底部菜单
        initTabView();

        // 关联事件
        initEvent();
    }

    /*
     *  关联底部TabView
     */
    private void initTabView() {
        String guai=this.getString(R.string.guai);
        System.out.println("guai:"+guai);
        tabHost = this.getTabHost();

        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, GuaiActivity.class);
        spec=tabHost.newTabSpec("zhangmenketang").setIndicator("zhangmenketang").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ZhangActivity.class);
        spec = tabHost.newTabSpec("dakafangtan").setIndicator("dakafangtan").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, MenActivity.class);
        spec=tabHost.newTabSpec("gerenzhongxin").setIndicator("gerenzhongxin").setContent(intent);
        tabHost.addTab(spec);

    }

    // 注册点击事件
    private void initEvent() {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgMenus);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateTab(checkedId);
                switch (checkedId) {
                    case R.id.guai:
                        tabHost.setCurrentTabByTag("zhangmenketang");
                        break;
                    case R.id.zhang:
                        tabHost.setCurrentTabByTag("dakafangtan");
                        break;
                    case R.id.men:
                        tabHost.setCurrentTabByTag("gerenzhongxin");

                        break;
                }
            }
        });
    }

    private void updateTab(int checkedId) {

        int[] androidid={R.id.guai,R.id.zhang,R.id.men};
        int[] drawableid={R.drawable.guai2,R.drawable.zhang2,R.drawable.men2};
        int[] drawablehoverid={R.drawable.guai2hover,R.drawable.zhang2hover,R.drawable.men2hover};
        for(int i=0;i<androidid.length;i++){
            int aid=androidid[i];

            RadioButton radioButton= (RadioButton) findViewById(aid);
            if(aid == checkedId){
                radioButton.setTextColor(Color.parseColor("#c71521"));
                Drawable top = getResources().getDrawable(drawablehoverid[i]);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            }else{
                radioButton.setTextColor(Color.parseColor("#a7a7a7"));
                Drawable top = getResources().getDrawable(drawableid[i]);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            }
        }
    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(this);      //调用双击退出函数
        }
        return false;  //不会执行退出事件
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public static void exitBy2Click(Activity activity) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            activity.finish();
            System.exit(0);
        }
    }


}
