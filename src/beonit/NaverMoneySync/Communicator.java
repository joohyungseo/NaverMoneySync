package beonit.NaverMoneySync;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class Communicator extends Service {
	private Context context = this;
	ProgressThread progressThread;

	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("beonit", "Service on create");
	}
	
//	public void writeReq() {
//		SharedPreferences prefs = context.getSharedPreferences("NaverMoneySync", Context.MODE_PRIVATE);
//		String id = prefs.getString("naverID", null);
//		String passwd = null;
//		try {
//			passwd = SimpleCrypto.decrypt("SECGAL", prefs.getString("naverPasswd", null));
//		} catch (Exception e) {
//			Log.e("beonit", "simple crypto decrypt fail");
//			e.printStackTrace();
//		}
//		String failsStrs = prefs.getString("items", null);
//		ArrayList<String> items = new ArrayList<String>();
//    	for( String item : failsStrs.split(";") )
//    		items.add(item);
//		// ����
//		Log.i("beonit", "recv to remote service");
//		QuickWriterNaver writer = new QuickWriterNaver(id, passwd, context);
//		writer.setFailSave(true);
//		writer.setResultNoti(true);
//		Log.i("beonit", "ProgressThread" + items);
//		progressThread = new ProgressThread(mHandler, writer, items);
//		progressThread.start();
//	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// Return the interface
		Log.e("beonit", "on bind");
		return mBinder;
	}
	
	private final ICommunicator.Stub mBinder = new ICommunicator.Stub() {

		@Override
		public void onRecvSMS()
				throws RemoteException {
			SharedPreferences prefs = context.getSharedPreferences("NaverMoneySync", Context.MODE_PRIVATE);
			String items = prefs.getString("items", "");
			String id = prefs.getString("naverID", null);
			String passwd = null;
			try {
				passwd = SimpleCrypto.decrypt("SECGAL", prefs.getString("naverPasswd", null));
			} catch (Exception e) {
				Log.e("beonit", "simple crypto decrypt fail");
				e.printStackTrace();
			}
			// ����
			Log.i("beonit", "recv to remote service");
		    QuickWriterNaver writer = new QuickWriterNaver(id, passwd, context);
			writer.setFailSave(true);
			writer.setResultNoti(true);
			Log.i("beonit", "ProgressThread" + items);
		    ProgressThread progressThread = new ProgressThread(mHandler, writer, items);
			progressThread.start();
		}

		@Override
		public void test() throws RemoteException {
			Log.i("beonit", "test service");
		}
		
	};

	// send
    private Handler mHandler = new SyncHandler(); 
    public class SyncHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case QuickWriterNaver.WRITE_READY:
				Log.i("beonit", "WRITE_READY");
				break;
			case QuickWriterNaver.WRITE_LOGIN_ATTEMPT:
				Log.i("beonit", "WRITE_LOGIN_ATTEMPT");
				break;
			case QuickWriterNaver.WRITE_LOGIN_SUCCESS:
				Log.i("beonit", "WRITE_LOGIN_SUCCESS");
				break;
			case QuickWriterNaver.WRITE_WRITING:
				Log.i("beonit", "WRITE_WRITING");
				break;
			case QuickWriterNaver.WRITE_SUCCESS:
				Log.i("beonit", "WRITE_SUCCESS");
				break;
			case QuickWriterNaver.WRITE_LOGIN_FAIL:
				Log.i("beonit", "WRITE_LOGIN_FAIL");
				break;
			case QuickWriterNaver.WRITE_FAIL:
				Log.i("beonit", "WRITE_FAIL");
				break;
			case QuickWriterNaver.WRITE_FAIL_REGISTER:
				Log.i("beonit", "WRITE_FAIL_REGISTER");
				break;
			default:
				break;
			}
		}
	};
}