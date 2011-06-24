package com.applest.mSocketChatClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class mSocketChatClientActivity extends Activity {
	
	private Socket socket;
	private InputStream socketInputStream;
	private OutputStream socketOutputStream;
	private String Message;
	private Pattern socketMessagePattern = Pattern.compile("(.+)?:(.+)?\0");
	
	private EditText uiNickFld;
	private EditText uiMessageFld;
	private Button uiConnectBtn;
	private Button uiSendBtn;
	private TextView uiLogTxt;
	
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// 画面維持
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// UIセット
		uiNickFld = (EditText) findViewById(R.id.uiNickFld);
		uiMessageFld = (EditText) findViewById(R.id.uiMessageFld);
		uiConnectBtn = (Button) findViewById(R.id.uiConnectBtn);
		uiSendBtn = (Button) findViewById(R.id.uiSendBtn);
		uiLogTxt = (TextView) findViewById(R.id.uiLogTxt);
		
		// ConnectBtnイベントハンドラ
		uiConnectBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				connectBtnOnClick(v);
			}
		});
		
		// SendBtnイベントハンドラ
		uiSendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendBtnOnClick(v);
			}
		});
	}
	
	public void connectBtnOnClick(View v) {
		socketConnect(msAddr, msPort);
	}
	
	public void sendBtnOnClick(View v) {
        byte[] w=new byte[1024];
        String sendText = uiNickFld.getText().toString() + ":" + uiMessageFld.getText().toString() + "\0";
		try {
			w = sendText.getBytes("UTF8");
	        socketOutputStream.write(w);
	        socketOutputStream.flush();
	        uiMessageFld.setText("",TextView.BufferType.NORMAL);
		} catch (Exception e) {
			Log.e("Err", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void socketConnect(String host, int port) {
		try {
			socket = new Socket(host, port);
			socketInputStream = socket.getInputStream();
			socketOutputStream = socket.getOutputStream();
			
			(new Thread(){public void run() {
				socketLoop();
			}}).start();
			
			uiNickFld.setEnabled(false);
			uiConnectBtn.setEnabled(false);
		} catch (Exception e) {
			Log.e("Err", e.getMessage());
			e.printStackTrace();
			
			// エラーダイアログ
			AlertDialog.Builder AlertDlgBldr = new AlertDialog.Builder(this); 
			AlertDlgBldr.setTitle("エラー");
			AlertDlgBldr.setMessage("接続できなかったっぽいよ！");
			AlertDialog AlertDlg = AlertDlgBldr.create();
			AlertDlg.show();
			
		}
	}
	
	public void socketLoop() {
		try {
			int size;
			byte[] w=new byte[1024];        
			String receiveTxt = "";
			String messageTxt = "";
			while (socket!=null && socket.isConnected()) {
				//データの受信
				size = socketInputStream.read(w);
				if (size <= 0) continue;
				
				receiveTxt = receiveTxt + new String(w,0,size,"UTF-8");
				
				Matcher matcher_message = socketMessagePattern.matcher(receiveTxt);
				
				if(matcher_message.matches()) {
					messageTxt = matcher_message.group(1) + ">" + matcher_message.group(2);
					Message = messageTxt;
					handler.post(new Runnable(){
						public void run() {
							//Coomment +
							uiLogTxt.setText(Message + System.getProperty("line.separator") + uiLogTxt.getText());
						}
					});
					receiveTxt = "";
				}
			}
		} catch (Exception e) {
			Log.e("Err", e.getMessage());
			e.printStackTrace();
		}
	}
}