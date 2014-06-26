package com.phonegap.plugins.weixin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.ShowMessageFromWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXAppExtendObject;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXEmojiObject;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXMusicObject;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXVideoObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXFileObject;
import com.phonegap.plugin.weixin.Util;

//import com.example.easynotepad.R;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.DialogInterface;
import android.content.ContextWrapper;
import android.widget.EditText;
import android.widget.LinearLayout;


public class WeiXin extends CordovaPlugin implements IWXAPIEventHandler {

        private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

        @Override
        public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
                boolean result = false;
        try {
            if (action.equals("register")) {
                String appId = args.getString(0);
                                this.register(appId);
                                callbackContext.success();
                result = true;
            } else if (action.equals("unregister")) {
                                this.unregister();
                                callbackContext.success();
                result = true;
            }
            else if (action.equals("openWeixin")) {
                                this.open();
                                callbackContext.success();
                                result = true;
            }
                        else if (action.equals("send")) {
                                JSONObject cfg = args.getJSONObject(0);
                                if (cfg.getString("type").equals("text")) {
                                        this.sendText(cfg.getString("text"), cfg.getBoolean("isSendToTimeline"));
                                } else if (cfg.getString("type").equals("image")) {
                                        this.sendImage(cfg.getString("data"), cfg.getString("imageType"), cfg.getBoolean("isSendToTimeline"));
                                } else if (cfg.getString("type").equals("music")) {
                                        this.sendMusic(cfg.getString("url"), cfg.getBoolean("isLowBand"), cfg.getString("title"), cfg.getString("desc"), cfg.getString("imgUrl"), cfg.getBoolean("isSendToTimeline"));
                                } else if (cfg.getString("type").equals("video")) {
                                        this.sendVideo(cfg.getString("url"), cfg.getBoolean("isLowBand"), cfg.getString("title"), cfg.getString("desc"), cfg.getString("imgUrl"), cfg.getBoolean("isSendToTimeline"));
                                } else if (cfg.getString("type").equals("webpage")) {
                                        this.sendWebPage(cfg.getString("url"), cfg.getString("title"), cfg.getString("desc"), cfg.getString("imgUrl"), cfg.getBoolean("isSendToTimeline"));
                                } else if (cfg.getString("type").equals("file")) {
                                        this.sendFile(cfg.getString("path"), cfg.getString("title"), cfg.getString("desc"), cfg.getString("imgUrl"), cfg.getBoolean("isSendToTimeline"));
                                }

                                callbackContext.success();
                                result = true;
            }
            else {
                result = false;
            }
        } catch (JSONException e) {
            callbackContext.error("JSON Exception");
                        result = false;
        }
                return result;
    }

        private IWXAPI api;

        public void register(String appId) {
                Context Activity = this.cordova.getActivity().getApplicationContext();
                api = WXAPIFactory.createWXAPI(Activity, appId, false);
                api.registerApp(appId);
        }

        public void open() {
                api.openWXApp();
        }


        //send text
        public void sendText(String text, boolean isSendToTimeline) {

                try{

                        WXTextObject textObj = new WXTextObject();
                        textObj.text = text;

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = textObj;
                        msg.description = text;

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());//buildTransaction("text");
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //send image
        public void sendImage(String data, String imageType, boolean isSendToTimeline) {

                File file = null;
                String path = "";
                Bitmap bmp = null;
                try{
                        WXImageObject imgObj = new WXImageObject();
                        if (imageType.equals("url")) {
                                imgObj.imageUrl = data;
                                bmp = BitmapFactory.decodeStream(new URL(data).openStream());

                        } else if (imageType.equals("path")) {
                                LOG.d("WeChat Plugin", "file path: " + SDCARD_ROOT + data);
                                path = (SDCARD_ROOT + data).replaceAll(" ", "%20");
                                file = new File(path);
                                if (!file.exists()) {
                                        LOG.d("WeChat Plugin", "file not exists");
                                        return;
                                } else {
                                        LOG.d("WeChat Plugin", "get file @" + path);
                                }
                                imgObj.setImagePath(path);

                                FileInputStream fis = new FileInputStream(path);
                                bmp = BitmapFactory.decodeStream(fis);
                                //bmp = BitmapFactory.decodeFile(path);
                        }

                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imgObj;

                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                        bmp.recycle();
                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = String.valueOf(System.currentTimeMillis());
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //send music
        public void sendMusic(String url, boolean isLowBand, String title, String desc, String imgUrl, boolean isSendToTimeline) {
                WXMusicObject music = new WXMusicObject();
                if (isLowBand == false) {
                        music.musicUrl = url;
                } else {
                        music.musicLowBandUrl = url;
                }
                //"http://staff2.ustc.edu.cn/~wdw/softdown/index.asp/0042515_05.ANDY.mp3";
                //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";

                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = music;
                msg.title = title;
                msg.description = desc;

                Bitmap bmp = null;
                try{
                        if (imgUrl.equals("")) {
                                Context Activity = this.cordova.getActivity().getApplicationContext();
                                Bitmap thumb = BitmapFactory.decodeResource(Activity.getResources(), R.drawable.music);
                                msg.thumbData = Util.bmpToByteArray(thumb, true);
                        } else {
                                bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                bmp.recycle();
                                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                        }

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("music");
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //send video
        public void sendVideo(String url, boolean isLowBand, String title, String desc, String imgUrl, boolean isSendToTimeline) {
                WXVideoObject video = new WXVideoObject();
                if (isLowBand == false) {
                        video.videoUrl = url;
                } else {
                        video.videoLowBandUrl = url;
                }

                WXMediaMessage msg = new WXMediaMessage(video);
                msg.title = title;
                msg.description = desc;
                Bitmap bmp = null;
                try{
                        if (imgUrl.equals("")) {
                                Context Activity = this.cordova.getActivity().getApplicationContext();
                                Bitmap thumb = BitmapFactory.decodeResource(Activity.getResources(), R.drawable.video);
                                msg.thumbData = Util.bmpToByteArray(thumb, true);
                        } else {
                                bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                bmp.recycle();
                                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                        }

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("video");
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                        //LOG.d("!!!!!!!!!!!!!!!!!!!!", video.videoUrl);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //send webpage
        public void sendWebPage(String url, String title, String desc, String imgUrl, boolean isSendToTimeline) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = title;
                msg.description = desc;
                Bitmap bmp = null;
                try{
                        if (imgUrl.equals("")) {
                                Context Activity = this.cordova.getActivity().getApplicationContext();
                                Bitmap thumb = BitmapFactory.decodeResource(Activity.getResources(), R.drawable.webpage);
                                msg.thumbData = Util.bmpToByteArray(thumb, true);
                        } else {
                                bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                bmp.recycle();
                                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                        }

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                        LOG.d("!!!!!!!!!!!!!!!!!!!!", webpage.webpageUrl);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //send webpage
        public void sendFile(String path, String title, String desc, String imgUrl, boolean isSendToTimeline) {
                WXFileObject appdata = new WXFileObject();
                //String pathStr = SDCARD_ROOT + path;

                //LOG.d("!!!!!!!!!!!!!!!!!!!!", pathStr);

                appdata.filePath = SDCARD_ROOT + path;
                //appdata.fileData = Util.readFromFile(pathStr, 0, -1);
                WXMediaMessage msg = new WXMediaMessage(appdata);

                Bitmap bmp = null;
                try{
                        if (imgUrl.equals("")) {
                                Context Activity = this.cordova.getActivity().getApplicationContext();
                                Bitmap thumb = BitmapFactory.decodeResource(Activity.getResources(), R.drawable.file);
                                msg.thumbData = Util.bmpToByteArray(thumb, true);
                        } else {
                                bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                                bmp.recycle();
                                msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
                        }

                        msg.title = title;
                        msg.description = desc;
                        msg.mediaObject = appdata;

                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("appdata");
                        req.message = msg;
                        int wxSdkVersion = api.getWXAppSupportAPI();
                        if (wxSdkVersion >= 0x21020001) {
                                req.scene = isSendToTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
                        } else {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                        }
                        api.sendReq(req);
                        LOG.d("!!!!!!!!!!!!!!!!!!!!", appdata.filePath);
                } catch(Exception e) {
                        e.printStackTrace();
                }
        }

        //unregister
        public void unregister() {
                api.unregisterApp();
        }

        // 微信发送请求到第三方应用时，会回调到该方法
                @Override
                public void onReq(BaseReq req) {
                        LOG.d("req", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        switch (req.getType()) {
                        case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                                LOG.d("onReq", "COMMAND_GETMESSAGE_FROM_WX");
                                System.out.println(ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX);
                                break;
                        case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                                LOG.d("onReq", "COMMAND_SHOWMESSAGE_FROM_WX");
                                System.out.println(ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX);
                                break;
                        default:
                                break;
                        }
                }

                // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
                @Override
                public void onResp(BaseResp resp) {
                        LOG.d("resp", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        int result = 0;

                        switch (resp.errCode) {
                        case BaseResp.ErrCode.ERR_OK:
                                result = 1;
                                break;
                        case BaseResp.ErrCode.ERR_USER_CANCEL:
                                result = 2;
                                break;
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                                result = 3;
                                break;
                        default:
                                result = 4;
                                break;
                        }
                        LOG.d("onResp", "result");
                        System.out.println(result);
                }






                private String buildTransaction(final String type) {
                        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
                }
}
