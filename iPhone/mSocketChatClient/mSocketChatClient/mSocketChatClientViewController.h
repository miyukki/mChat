//
//  mSocketChatClientViewController.h
//  mSocketChatClient
//
//  Created by Sakura on 11/06/08.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NSStreamEx.h"

@interface mSocketChatClientViewController : UIViewController<NSStreamDelegate> {
    IBOutlet UITextField *uiNickFld;
    IBOutlet UITextField *uiMessageFld;
    IBOutlet UIButton *uiConnectBtn;
    IBOutlet UIButton *uiSendBtn;
    IBOutlet UITextView *uiLog;
    
    NSInputStream *inputStream;
	NSOutputStream *outputStream;
    
    NSString *msAddr;
    NSInteger msPort;
}

- (void)socketConect:(NSString *)addr port:(NSInteger)port;
- (void)socketDisconnect;
- (void)socketSend:(NSString *)text;

- (IBAction)backgroundTapped:(id)sender;
- (IBAction)connectBtnTapped:(id)sender;
- (IBAction)sendBtnTapped:(id)sender;

@end
