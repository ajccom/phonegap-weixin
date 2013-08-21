/**
 * WeiXin
 * version 0.9
 * author: Jian Chen / ajccom
 * https://github.com/ajccom/phonegap-weixin
 * tested cordova version - 2.3.0
 **/
(function(cordova){
	function WeiXin() {};

	WeiXin.prototype.register = function(appid, success, error) {
		cordova.exec(success, error, "WeiXin", "register", [appid]);
	};
	
	WeiXin.prototype.unregister = function(success, error) {
		cordova.exec(success, error, "WeiXin", "unregister", []);
	};
	
	WeiXin.prototype.openWeixin = function(success, error) {
		cordova.exec(success || null, error || null, "WeiXin", "openWeixin", []);
	};
	
	WeiXin.prototype.send = function(arg, success, error) {
		if (typeof arg.isSendToTimeline === 'undefined') {arg.isSendToTimeline = true}
		if (arg.type === 'image' && typeof arg.imageType === 'undefined') {arg.imageType = 'url'}
		if (arg.type === 'music' || arg.type === 'video' || arg.type === 'webpage') {
			if (typeof arg.title === 'undefined') {arg.title = ''}
			if (typeof arg.desc === 'undefined') {arg.desc = ''}
			if (typeof arg.imgUrl === 'undefined') {arg.imgUrl = ''}
			if (typeof arg.isLowBand === 'undefined') {arg.isLowBand = false}
		}
		cordova.exec(success, error, "WeiXin", "send", [arg]);
	};
	
	if(!window.plugins) {
		window.plugins = {};
	}
	if (!window.plugins.weixin) {
		window.plugins.weixin = new WeiXin();
	}
	
})(window.PhoneGap || window.Cordova || window.cordova);