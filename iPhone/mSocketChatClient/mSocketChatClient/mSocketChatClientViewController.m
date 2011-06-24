//
//  mSocketChatClientViewController.m
//  mSocketChatClient
//
//  Created by Sakura on 11/06/08.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "mSocketChatClientViewController.h"

@implementation mSocketChatClientViewController

- (void)dealloc
{
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    [self socketDisconnect];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (IBAction)backgroundTapped:(id)sender
{
    [uiMessageFld resignFirstResponder];
}

- (IBAction)connectBtnTapped:(id)sender
{
    [self socketConect:msAddr port:msPort];
}

- (IBAction)sendBtnTapped:(id)sender
{
    NSString *message = [NSString stringWithFormat:@"%@:%@%@", [uiNickFld text], [uiMessageFld text], @"\t"];
    [self socketSend:message];
    [uiMessageFld setText:@""];
}

- (void)socketConect:(NSString *)addr port:(NSInteger)port
{
    [NSStream getStreamsToHostNamed:addr port:port inputStream:&inputStream outputStream:&outputStream];
    
    [uiNickFld setEnabled:false];
    [uiConnectBtn setEnabled:false];
    
    [inputStream retain];
    [outputStream retain];
    [inputStream setDelegate:self];
    [outputStream setDelegate:self];
    [inputStream scheduleInRunLoop:[NSRunLoop currentRunLoop]
                       forMode:NSDefaultRunLoopMode];
    [outputStream scheduleInRunLoop:[NSRunLoop currentRunLoop]
                       forMode:NSDefaultRunLoopMode];
    [inputStream open];
    [outputStream open];
}

- (void)socketDisconnect
{
    [inputStream close];
    [outputStream close];
    [inputStream release];
    [outputStream release];
}

- (void)socketSend:(NSString *)text
{
    // [名前]:[本文]\t
    const uint8_t *rawstring = (const uint8_t *)[text UTF8String];
    [outputStream write:rawstring maxLength:strlen((char *)rawstring)];
}

- (void)stream:(NSStream *)stream handleEvent:(NSStreamEvent)eventCode {
    switch(eventCode) {
        case NSStreamEventHasBytesAvailable:
        {
            NSMutableData *data = [[NSMutableData alloc] init];
            
            uint8_t buf[1024];
            unsigned int len = 0;
            len = [(NSInputStream *)stream read:buf maxLength:1024];
            if(len) {    
                [data appendBytes:(const void *)buf length:len];
                int bytesRead;
                bytesRead += len;
            }
            
            NSString *str = [[NSString alloc] initWithData:data 
                                                  encoding:NSUTF8StringEncoding];
            NSString *message = [NSString stringWithFormat:@"%@\r\n%@", str, [uiLog text]];
            [uiLog setText: message];
            
            [str release];
            [data release];
            data = nil;
        } break;
    }
}

@end
