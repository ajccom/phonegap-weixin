# WeiXin plugin for PhoneGap

This is a prototype of a cross-platform WeiXin PhoneGap plugin. Android
is currently supported. Support for Ios is also planned.

##Install

###Android

1.As with most Cordova/PhoneGap APIs, functionality is not available until the
`deviceready` event has fired on the document. The `weixin.js` file
should be included _after_ the `cordova-x.x.x.js` file.

  <script src="js/cordova-2.3.0.js"></script>
  <script src="js/weixin.js"></script>

2.Put `WeiXin.java` to your plugin path, default is `com.phonegap.plugins.weixin.WeiXin`.
And edit your package path at line `import [yourPackageName].R` and `import [yourPackageName].Util`.

Put `Util.java` to your main package, it will be imported in `WeiXin.java`.

3.Put jar file in `libs` to your project and don't forget to `Add to build path`.

4.Open your `config.xml` file and add:

  `<plugin name="WeiXin" value="com.phonegap.plugins.weixin.WeiXin"/>`

###IOS

wait... 

##API

All functions are called on the singleton ChildBrowser instance - accessible
as `window.plugins.weixin`.

###register
  
if you want to use WeiXin API, first, you should register your app to WeiXin.

```
  window.plugins.weixin.register(AppId, Success, Fail);
```

you should apply your app id [here](http://open.weixin.qq.com/app/list/?lang=zh_CN).

###unregister

you can unregister your app from WeiXin.

```
  window.plugins.weixin.unregister(Success, Fail);
```

###openWeixin

you can use it to open WeiXin app.

```
  window.plugins.weixin.openWeixin(Success, Fail);
```
  
###send

you can send text, image, music, video, webpage with this api.

```
  window.plugins.weixin.send(args, Success, Fail);
```

#####arguments detail

######send text

```
	{type: 'text',
	text: 'I want to send text',
	isSendToTimeline: true} //if true, send to "朋友圈", else send to WeiXin friends.
```

######send image

```
	{type: 'image',
	imageType: 'path',//you can also use 'url' to send image.
	data: '/test.png',//SD card path or Url
	isSendToTimeline: true}
```

######send music

```
	{type: 'music',
	url: 'http://x.x.x/test.mp3',
	title: 'title',
	desc: 'desc',
	isLowBand: true,//WeiXin will use different API when mobile in low band environment.
	isSendToTimeline: true}
```

######send video

```
	{type: 'video',
	url: 'http://x.x.x.swf',
	title: 'title',
	desc: 'desc',
	isLowBand: true,
	isSendToTimeline: true}
```

######send webpage

```
	{type: 'webpage',
	url: 'http://www.baidu.com',
	title: 'title',
	desc: 'desc',
	isSendToTimeline: true}
```

* when send `music`, `video`, `webpage`, you should put series images to `res` folder,
and named `music.png`, `video.png`, `webpage.png`.
because of WeiXin API need a `thumbData`.

## Thank You Good People

* @Bob Su

## License

what's this...

## 捐赠
如果您觉得这个Plugin对您有帮助，欢迎请ajccom喝一杯咖啡

[![捐赠](https://img.alipay.com/sys/personalprod/style/mc/btn-index.png)](https://me.alipay.com/ajccom)

