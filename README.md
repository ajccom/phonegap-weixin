# WeiXin plugin for PhoneGap

This is a prototype of a cross-platform WeiXin PhoneGap plugin. Android
is currently supported. Support for Ios is also planned.

##Install

###Android

1. Edit your package path at line `import [yourPackageName].R`

2. Put `libammsdk.jar` file in `libs` to your `build path`.

###IOS

1. Add `wx[appID]` to your `Info.plist`
![](ios_plist.jpg?raw=true)
2. Add following code to  `openURL` method in `AppDelegate.m` file
```
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    return [WXApi hadleOpenURL:url delegate:self];
}
```

##API

All functions are called on the singleton ChildBrowser instance - accessible
as `navigator.weixin`.

###register
  
if you want to use WeiXin API, first, you should register your app to WeiXin.

```
  navigator.weixin.register(AppId, Success, Fail);
```

you should apply your app id [here](http://open.weixin.qq.com/app/list/?lang=zh_CN).

###unregister

you can unregister your app from WeiXin.

```
  navigator.weixin.unregister(Success, Fail);
```

###openWeixin

you can use it to open WeiXin app.

```
  navigator.weixin.openWeixin(Success, Fail);
```
  
###send

you can send text, image, music, video, webpage with this api.

```
  navigator.weixin.send(args, Success, Fail);
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
	isLowBand: true,//WeiXin will use different API when mobile in low band environment. default false
	imgUrl: 'http://www.baidu.com/img/bdlogo.gif',//if not defined, use 'res/drawable/music.png'
	isSendToTimeline: true}
```

######send video

```
	{type: 'video',
	url: 'http://x.x.x.swf',
	title: 'title',
	desc: 'desc',
	isLowBand: true,
	imgUrl: 'http://www.baidu.com/img/bdlogo.gif',//if not defined, use 'res/drawable/video.png'
	isSendToTimeline: true}
```

######send webpage

```
	{type: 'webpage',
	url: 'http://www.baidu.com',
	title: 'title',
	desc: 'desc',
	imgUrl: 'http://www.baidu.com/img/bdlogo.gif',//if not defined, use 'res/drawable/webpage.png'
	isSendToTimeline: true}
```

######send file

```
	{type: 'file',
	path: 'file:///test.mp3',//file's fullPath
	desc: '我在发本地文件',
	title: '文件',
	imgUrl: 'http://www.baidu.com/img/bdlogo.gif',//if not defined, use 'res/drawable/file.png'
	isSendToTimeline: true}
```

* when send `music`, `video`, `webpage` and `file`, you should put series images to `res` folder,
and named `music.png`, `video.png`, `webpage.png`.
because of WeiXin API need a `thumbData`.

## Thank You Good People

* @Bob Su

## License

what's this...

## 捐赠
如果您觉得这个Plugin对您有帮助，欢迎请ajccom喝一杯咖啡

[![捐赠](https://img.alipay.com/sys/personalprod/style/mc/btn-index.png)](https://me.alipay.com/ajccom)

