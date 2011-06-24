//
//  mSocketChatClientAppDelegate.h
//  mSocketChatClient
//
//  Created by Sakura on 11/06/08.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class mSocketChatClientViewController;

@interface mSocketChatClientAppDelegate : NSObject <UIApplicationDelegate> {

}

@property (nonatomic, retain) IBOutlet UIWindow *window;

@property (nonatomic, retain) IBOutlet mSocketChatClientViewController *viewController;

@end
