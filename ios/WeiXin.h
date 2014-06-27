//
//  WeiXin.h
//
//  Created by Richie.Min on 6/26/14.
//
//

#import <Cordova/CDV.h>
#import "WXApi.h"
#import "WXApiObject.h"

@interface WeiXin : CDVPlugin <WXApiDelegate>
{
    enum WXScene _scene;
}
- (void)register: (CDVInvokedUrlCommand* )command;
- (void)unregister: (CDVInvokedUrlCommand* )command;
- (void)openWeixin: (CDVInvokedUrlCommand* )command;
- (void)send: (CDVInvokedUrlCommand* )command;

@property (nonatomic, strong) CDVInvokedUrlCommand *pendingCommand;

@property NSString* appKey;

@end
