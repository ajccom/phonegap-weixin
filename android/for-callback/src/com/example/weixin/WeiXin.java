/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.example.weixin;

import android.os.Bundle;
import android.widget.Toast;

import org.apache.cordova.*;
import android.content.Context;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WeiXin extends CordovaActivity/** implements IWXAPIEventHandler**/
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.init();
        // Set by <content src="index.html" /> in config.xml
        //this.sendJavascript("alert('111')");
        super.loadUrl(Config.getStartUrl());
        
        //super.loadUrl("file:///android_asset/www/index.html");
    }
    
    /**
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
		
		//this.webView.sendJavascript("navigator.weixin.callback()");
	}

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
		
		Toast.makeText(WeiXin.this,"测试信息",
			     Toast.LENGTH_SHORT).show();
		//this.webView.sendJavascript("navigator.weixin.callback()");
		//LOG.d("onResp", "result");
		LOG.d("WeChat Plugin", "weixin_info");
		//System.out.println(result);
	}**/
    
}

