//
//  WeiXin.m
//
//  Created by Richie.Min on 6/26/14.
//
//

#import "WeiXin.h"

@implementation WeiXin

- (id)init{
    if(self = [super init]){
        _scene = WXSceneSession;
    }
    return self;
}

- (void)register:(CDVInvokedUrlCommand* )command
{
    CDVPluginResult *result;
    NSString *message;

    self.appKey = [command.arguments objectAtIndex:0];

    if (!self.appKey)
    {
        message = @"App key was null.";
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:message];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return ;
    }

    [WXApi registerApp:self.appKey];

    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)unregister:(CDVInvokedUrlCommand* )command
{
    CDVPluginResult *result;

    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];

}

- (void)openWeixin:(CDVInvokedUrlCommand*) command{
    CDVPluginResult *result;

    [WXApi openWXApp];

    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)send:(CDVInvokedUrlCommand*) command{
    CDVPluginResult *result;
    self.pendingCommand = command;
    NSString *type = [self parseStringFromJS:command.arguments keyFromJS:@"type"];
    NSString *isSendToTimeline = [self parseStringFromJS:command.arguments keyFromJS:@"isSendToTimeline"];
    if([isSendToTimeline  isEqual: @"1"]){
        [self changeScene:WXSceneTimeline];
    }else{
        [self changeScene:WXSceneSession];
    }
    if([type  isEqual: @"text"]){
        [self sendTextContent];
    }else if([type  isEqual: @"image"]){
        [self sendImageContent];
    }else if([type  isEqual: @"music"]){
        [self sendMusicContent];
    }else if([type  isEqual: @"video"]){
        [self sendVideoContent];
    }else if([type  isEqual: @"webpage"]){
        [self sendLinkContent];
    }else if([type  isEqual: @"file"]){
        [self sendFileContent];
    }else{
        result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        return ;
    }
}

-(void) changeScene:(NSInteger)scene
{
    _scene = scene;
}

- (void) sendTextContent
{
    NSString *text = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"text"];
    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.text = text;
    req.bText = YES;
    req.scene = _scene;

    [WXApi sendReq:req];
}

- (void) sendImageContent
{
    WXMediaMessage *message = [WXMediaMessage message];
    [message setThumbImage:[UIImage imageNamed:@"res5thumb.png"]];

    WXImageObject *ext = [WXImageObject object];
    
    NSString *imageType = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"imageType"];
    NSString *filePath = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"data"];
    NSString *title = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"title"];
    UIImage *image;
    if([imageType isEqual:@"url"]){
        image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:filePath]]];
    }else if([imageType isEqual:@"path"]){
        image = [UIImage imageWithData: [NSData dataWithContentsOfFile: filePath]];
    }
    ext.imageData = UIImagePNGRepresentation(image);

    message.mediaObject = ext;
    message.title = title;
    
    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = _scene;

    [WXApi sendReq:req];
}

-(void) sendMusicContent
{
    NSString *title = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"title"];
    NSString *description = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"desc"];
    NSString *url = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"url"];
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[UIImage imageNamed:@"res3.jpg"]];
    WXMusicObject *ext = [WXMusicObject object];
    ext.musicDataUrl = url;

    message.mediaObject = ext;

    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = _scene;

    [WXApi sendReq:req];
}

-(void) sendVideoContent
{
    NSString *title = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"title"];
    NSString *description = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"desc"];
    NSString *url = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"url"];
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[UIImage imageNamed:@"res7.jpg"]];

    WXVideoObject *ext = [WXVideoObject object];
    ext.videoUrl = url;

    message.mediaObject = ext;

    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = _scene;

    [WXApi sendReq:req];
}

- (void) sendLinkContent
{
    NSString *title = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"title"];
    NSString *description = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"desc"];
    NSString *url = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"url"];
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[UIImage imageNamed:@"res2.png"]];

    WXWebpageObject *ext = [WXWebpageObject object];
    ext.webpageUrl = url;

    message.mediaObject = ext;

    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = _scene;

    [WXApi sendReq:req];
}

- (void)sendFileContent
{
    NSString *title = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"title"];
    NSString *description = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"desc"];
    NSString *path = [self parseStringFromJS:self.pendingCommand.arguments keyFromJS:@"path"];
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = title;
    message.description = description;
    [message setThumbImage:[UIImage imageNamed:@"res2.jpg"]];

    WXFileObject *ext = [WXFileObject object];
    ext.fileExtension = @"pdf";
    NSString* filePath = [[NSBundle mainBundle] pathForResource:path ofType:@"pdf"];
    ext.fileData = [NSData dataWithContentsOfFile:filePath];

    message.mediaObject = ext;

    SendMessageToWXReq* req = [[SendMessageToWXReq alloc] init];
    req.bText = NO;
    req.message = message;
    req.scene = _scene;

    [WXApi sendReq:req];
}

# pragma mark -
# pragma mark MISC
# pragma mark -

- (BOOL)existCommandArguments:(NSArray*)comArguments{
    NSMutableArray *commandArguments=[[NSMutableArray alloc] initWithArray:comArguments];
    if (commandArguments && commandArguments.count > 0) {
        return TRUE;
    }else{
        return FALSE;
    }
}

- (NSString*)parseStringFromJS:(NSArray*)commandArguments keyFromJS:(NSString*)key{
    if([self existCommandArguments:commandArguments]){
        NSString *value = [[commandArguments objectAtIndex:0] valueForKey:key];
        if(value){
            return [NSString stringWithFormat:@"%@",value];
        }else{
            return @"";
        }
    }else{
        return @"";
    }
}


@end
