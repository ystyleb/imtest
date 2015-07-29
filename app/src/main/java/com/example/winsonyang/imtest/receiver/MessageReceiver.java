package com.example.winsonyang.imtest.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.winsonyang.imtest.MainActivity;
import com.example.winsonyang.imtest.R;
import com.tencent.android.talk.IMCloudBaseReceiver;
import com.tencent.android.talk.IMCloudCallback;
import com.tencent.android.talk.IMCloudManager;
import com.tencent.android.talk.IMMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Date;

import util.DemoUtil;

/**
 * Created by winsonyang on 2015/7/16.
 */
public class MessageReceiver extends IMCloudBaseReceiver {

    private Intent intentNum = new Intent(
            "com.example.winsonyang.imtest.activity.UPDATE_NUM");
    /*private Intent intentFilter = new Intent(
            "com.qq.imdemo.activity.IM_LISTVIEW");

    private Intent updateDetail = new Intent(
            "com.qq.imdemo.activity.UPDATE_DETAIL");*/
    public static final String LogTag = "IMCloudReceiver";
    private Handler mHandler = new Handler();

    @Override
    public void onIMMessage(final Context context, final IMMessage message) {


        //updateDetail.putExtra("im_message", message);
        //context.sendBroadcast(updateDetail);

        //winson add
        long lag = 0;
        String LocalUseId = IMCloudManager.getLocalUseId(context);
        long ReceiveTime = System.currentTimeMillis();
        String regStr = "^[+-]?[1-9][0-9]*$|^0$";


        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        // new Date()为获取当前系统时间
        if(message.content!=null&&message.content.matches(regStr)){
            long PushTime = Long.parseLong(message.content);
            lag = ReceiveTime - PushTime;
            String msg = df.format(new Date()) + " " + String.valueOf(lag) + " " + message.msgId;
            intentNum.putExtra("msg",msg);
            //writeDataToSD("IMLagTime.txt",msg );
            message.content = message.content + "  lag:" + lag;
        }

        if(!LocalUseId.equals("n5")) {
            //DemoUtil.showNotifation(context, message);
        }

        intentNum.putExtra("Num",1);
        context.sendBroadcast(intentNum);


        /*if(LocalUseId.matches(regStr)){
            final int ToUserId = Integer.parseInt(LocalUseId)+1;
            final String meg = LocalUseId +"->"+ToUserId;


            if(LocalUseId.equals("1000")){
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ///
                        try {
                            int max=2*60*1000;
                            int min=1*60*1000;
                            for(int i =0; i<10000; i++){
                                Publish2User(context,String.valueOf(ToUserId),meg+"  i="+i+ "  " + String.valueOf(df.format(new Date())));

                                Random random = new Random();

                                int s = random.nextInt(max)%(max-min+1) + min;
                                Thread.sleep(s);}
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
            else if(LocalUseId.equals("99994")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ///
                        try {

                            for(int i =0; i<1000; i++){
                                Publish2User(context,message.content,String.valueOf(System.currentTimeMillis()));

                                int s = 4*1000;
                                Thread.sleep(s);}
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
            else if(LocalUseId.equals("99992")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ///
                        try {

                            for(int i =0; i<1000; i++){
                                Publish2User(context,message.content,String.valueOf(System.currentTimeMillis()));

                                int s = 2*1000;
                                Thread.sleep(s);}
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            else{

                if(message.fromUser.equals(String.valueOf(ToUserId-2))){
                    Publish2User(context,String.valueOf(ToUserId),meg + "  " + String.valueOf(df.format(new Date())));

                    Publish2User(context,String.valueOf(LocalUseId),String.valueOf(System.currentTimeMillis()));
                }

            }
        }*/


        //winson add end



        /*Object object = DataHelper.getSettings(context.getApplicationContext(),
                DataHelper.USER_ID);
        if (object != null) {
            IMListInfo user = (IMListInfo) object;
            boolean flag = false;
            for (IMMessage key : user.getUserAll()) {
                int msgType = message.msgType;
                int mt = key.msgType;
                if (msgType == mt) {
                    switch (msgType) {
                        case 0:
                            if (key.fromUser.equals(message.fromUser)) {
                                flag = true;
                                key.content = message.content;
                                key.utime = message.utime;
                                key.type = 0;
                                key.msgType = message.msgType;
                                key.contentType = message.contentType;
                                key.fromUser = message.fromUser;
                                key.toUser = message.toUser;
                            }
                            break;
                        case 1:
                            if (key.toUser.equals(message.toUser)) {
                                flag = true;
                                key.content = message.content;
                                key.utime = message.utime;
                                key.type = 0;
                                key.contentType = message.contentType;
                                key.msgType = message.msgType;
                                key.fromUser = message.fromUser;
                                key.toUser = message.toUser;
                            }
                            break;
                        default:
                            break;
                    }
                }

            }
            if (!flag) {
                IMMessage im = new IMMessage();
                im.type = 0;
                im.content = message.content;
                im.utime = message.utime;
                im.fromUser = message.fromUser;
                im.msgType = message.msgType;
                im.toUser = message.toUser;
                im.contentType = message.contentType;
                user.getUserAll().add(im);
                context.sendBroadcast(intentContact);
            }
            DataHelper.putSettings(context.getApplicationContext(),
                    DataHelper.USER_ID, user);
        } else {
            IMListInfo ili = new IMListInfo();
            IMMessage im = new IMMessage();
            im.type = 0;
            im.content = message.content;
            im.utime = message.utime;
            im.fromUser = message.fromUser;
            im.msgType = message.msgType;
            im.contentType = message.contentType;
            im.toUser = message.toUser;
            ili.getUserAll().add(im);
            DataHelper.putSettings(context.getApplicationContext(),
                    DataHelper.USER_ID, ili);
            context.sendBroadcast(intentContact);
        }
        // intentFilter.putExtra("fromUser", message.fromUser);
        intentFilter.putExtra("im_message", message);
        context.sendBroadcast(intentFilter);

    }

    //winson add
    private void Publish2User(final Context context,final String UserId,String content) {
        try {
            IMCloudManager.sendMsgToUserId(context,
                    UserId, content, new IMCloudCallback() {

                        @Override
                        public void onSuccess(Object data, int flag) {

                            DemoUtil.showToast(
                                    "sendMsgToUseId success with userid = "
                                            + UserId,
                                    context);
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // send_content.setText("");
                                }
                            });

                        }

                        @Override
                        public void onFail(Object data, int errCode,
                                           String msg) {

                            DemoUtil.showToast(
                                    "sendMsgToUseId success with userid = "
                                            + context + "error = "
                                            + msg, context);
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // send_content.setText("");
                                }
                            });
                        }
                    });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    private void Publish2Group(final Context context) {

    }

    private void writeDataToSD(String FileName,String content){
        try{
		    /* 获取File对象，确定数据文件的信息 */
            //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");
            File file  = new File(Environment.getExternalStorageDirectory(),"/"+FileName);

		    /* 判断sd的外部设置状态是否可以读写 */
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

		        /* 流的对象 *//*  */
                FileOutputStream fos = new FileOutputStream(file,true);

		        /* 需要写入的数据 */
                String message = content+"\n";

		        /* 将字符串转换成字节数组 */
                byte[] buffer = message.getBytes();

		        /* 开始写入数据 */
                fos.write(buffer);

		        /* 关闭流的使用 */

                fos.close();
                Log.d(LogTag, "onIMMessage " + "文件写入成功:" + message);
                //Toast.makeText(MainActivity.this, "文件写入成功", 1000).show();
            }

        }catch(Exception ex){
            Log.d(LogTag, "onIMMessage " + "文件写入失败");
            //Toast.makeText(MainActivity.this, "文件写入失败", 1000).show();
        }

    }
    //winson add end
}

