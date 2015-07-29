package com.example.winsonyang.imtest;



import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.android.talk.IMCloudManager;
import com.tencent.android.talk.IMCloudCallback;

import android.os.Handler;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import util.DemoUtil;

public class MainActivity extends ActionBarActivity {

    private String userID = "";
    private Context context;
    private ProgressDialog pd;
    private Button send,login,flashlog;
    private EditText edit_userid,edit_sendnums,edit_time,edit_login;
    private Handler mHandler = new Handler();
    private TextView success,unsuccess,Receive,log;
    private updateRecNum updatenum;
    private ArrayList<String> strLogArray = new ArrayList<String> ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        IMCloudManager.start(getApplicationContext());
        /*String name = IMCloudManager.getLocalUseId(context);

        IMCloudManager.login(getApplicationContext(), name,
                name, new IMCloudCallback() {

                    @Override
                    public void onSuccess(Object data, int flag) {
                        DemoUtil.showToast(
                                "login success with userid = msgmaker",
                                getApplicationContext());
                    }

                    @Override
                    public void onFail(Object data, int errCode,
                                       String msg) {
                        DemoUtil.showToast(
                                "login unsuccess with userid = msgmaker"
                                        + "error = " + msg,
                                getApplicationContext());

                    }
                });*/

        mHandler = new Handler();//创建Handler
        success = (TextView) findViewById(R.id.textView_success);
        unsuccess = (TextView) findViewById(R.id.textView_unsuccess);
        Receive = (TextView) findViewById(R.id.textView_RecNum);



        init();

    }

    private void init() {
        updatenum = new updateRecNum();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.winsonyang.imtest.activity.UPDATE_NUM");
        registerReceiver(updatenum, intentFilter);



        send = (Button) findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_userid = (EditText) findViewById(R.id.editText_userid);
                edit_sendnums = (EditText) findViewById(R.id.editText_sendnums);
                edit_time = (EditText) findViewById(R.id.editText_time);


                //Random random = new Random();


                //Log.d("winson", userid + sendnums + time);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String userid = edit_userid.getText().toString().trim();
                            String sendnums = edit_sendnums.getText().toString().trim();
                            String time = edit_time.getText().toString().trim();
                            //int max=2*60*1000;
                            //int min=1*60*1000;


                            for (int i = 1; i < Integer.parseInt(sendnums); i++) {
                                String SucNums = success.getText().toString();
                                Publish2User(context, userid, String.valueOf(System.currentTimeMillis()));
                                if(Integer.parseInt(SucNums) != i){
                                    i = Integer.parseInt(SucNums);
                                }
                                int s = Integer.parseInt(time) * 1000;
                                Thread.sleep(s);
                            }
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                }).start();
                ///


            }
        });

        login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_login = (EditText) findViewById(R.id.editText_login);
                final String name = edit_login.getText().toString();

                IMCloudManager.login(getApplicationContext(), name,
                        name, new IMCloudCallback() {

                            @Override
                            public void onSuccess(Object data, int flag) {
                                DemoUtil.showToast(
                                        "login success with userid = " + name,
                                        getApplicationContext());
                            }

                            @Override
                            public void onFail(Object data, int errCode,
                                               String msg) {
                                DemoUtil.showToast(
                                        "login unsuccess with userid = " + name
                                                + "error = " + msg,
                                        getApplicationContext());

                            }
                        });
            }
        });
        flashlog = (Button) findViewById(R.id.button_flashlog);
        flashlog.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            writeDataToSD("IMLagTime.txt", strLogArray);
                                        }
                                    }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    private void Publish2User(final Context context,final String UserId, final String content) {
        try {

            /*final Runnable sucRunnable = new Runnable()
            {
                @Override
                public void run(){

                }
            };

            final Runnable unsucRunnable = new Runnable()
            {
                @Override
                public void run(){

                }
            };*/

            IMCloudManager.sendMsgToUserId(context,
                    UserId, content, new IMCloudCallback() {

                        @Override
                        public void onSuccess(Object data, int flag) {

                            //mHandler.post(sucRunnable);
                            //DemoUtil.showToast(
                             //       "sendMsgToUseId success with userid = "
                             //               + UserId,
                              //      context);
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    String SucNums = success.getText().toString();
                                    success.setText(Integer.parseInt(SucNums) + 1 + "") ;
                                }
                            });

                        }

                        @Override
                        public void onFail(Object data, int errCode,
                                           String msg) {
                            //mHandler.post(unsucRunnable);
                            //DemoUtil.showToast(
                            //       "sendMsgToUseId success with userid = "
                            //                + context + "error = "
                            //                + msg, context);

                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // send_content.setText("");
                                    String unSucNums = unsuccess.getText().toString();
                                    unsuccess.setText(Integer.parseInt(unSucNums) + 1 + "");
                                }
                            });
                            //Publish2User(context,UserId,content);
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private class updateRecNum extends BroadcastReceiver{

        @Override
        public void onReceive(Context context,Intent intent){

            int Num = intent.getIntExtra("Num", -1);

            String ReceiveNum = Receive.getText().toString();
            Receive.setText(Integer.parseInt(ReceiveNum) + Num + "") ;

            String logMsg = intent.getStringExtra("msg");
            strLogArray.add(logMsg);

            if(strLogArray.size()>=10){
                writeDataToSD("IMLagTime.txt",strLogArray);
            }

        }



        }

    private void writeDataToSD(String FileName,ArrayList<String> strArray) {
        try {
		    /* 获取File对象，确定数据文件的信息 */
            //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");
            File file = new File(Environment.getExternalStorageDirectory(), "/" + FileName);
            int len = strArray.size();

		    /* 判断sd的外部设置状态是否可以读写 */
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = new FileOutputStream(file, true);

                for (int i = 0; i < len; ++i) {
		        /* 流的对象 *//*  */


		        /* 需要写入的数据 */
                    String message = strArray.get(i) + "\n";

		        /* 将字符串转换成字节数组 */
                    byte[] buffer = message.getBytes();

		        /* 开始写入数据 */
                    fos.write(buffer);



                }
                Log.d("winson", "onIMMessage " + "文件写入成功:" + len);
                /* 关闭流的使用 */
                fos.close();

                strArray.clear();
                //Toast.makeText(MainActivity.this, "文件写入成功", 1000).show();

            }

        } catch (Exception ex) {
            Log.d("winson", "onIMMessage " + "文件写入失败");
            //Toast.makeText(MainActivity.this, "文件写入失败", 1000).show();
        }
    }

}
