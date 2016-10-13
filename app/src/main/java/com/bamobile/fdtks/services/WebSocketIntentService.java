package com.bamobile.fdtks.services;



import java.net.URI;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bamobile.fdtks.R;
import com.bamobile.fdtks.activities.MainActivity;
import com.bamobile.fdtks.application.FdTksApplication;
import com.bamobile.fdtks.entities.Camion;
import com.bamobile.fdtks.socket.WebSocket;
import com.bamobile.fdtks.socket.WebSocket.WebSocketConnectionObserver;
import com.bamobile.fdtks.socket.WebSocketConnection;
import com.bamobile.fdtks.socket.WebSocketException;

public class WebSocketIntentService extends Service implements WebSocketConnectionObserver {

	static final String TAG = "de.tavendo.autobahn.echo";
	// private static final String PREFS_NAME = "AutobahnAndroidEcho";

	private WebSocket mConnection = new WebSocketConnection();
	public String uri;
	private Context context;

	private NotificationManager nm;	

	public WebSocketIntentService(){
		start();
	}
	
	public WebSocketIntentService(Context context){
		start();
		this.context = context;
	}
	
	public WebSocket getConn() {
		return mConnection;
	}
	
	public void start() {
		Log.v(TAG, "start web socket");

		String wsuri = "wss://fdtrcksarg-bamobile.rhcloud.com:8443/webSocket";
		uri = wsuri;

		try {
			mConnection.connect(URI.create(uri), this);
		} catch (WebSocketException e) {
			Log.d(TAG, e.toString());
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(WebSocketCloseNotification code, String reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextMessage(String payload) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context
						.getApplicationContext());
		boolean receive = prefs.getBoolean("notifications", false);
		boolean fav = false;
		String[] parts = payload.split("-");
		for (Camion cam : FdTksApplication.getCamiones()){
			if(cam.getNombre().equals(parts[0])){
				fav = true;
			}
		}
		if(receive && fav){
		Intent intent = new Intent(context,
				MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(
				context, 0, intent, 0);
		Log.v("PAYLOAD", payload);
		Uri uri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Street Food")
				.setContentText(payload).setContentIntent(pi)
				.setSound(uri).setAutoCancel(true);

		// Intent resultIntent = new
		// Intent(EchoClientService.this.getApplicationContext(),
		// LoginActivity.class);
		// TaskStackBuilder stackBuilder =
		// TaskStackBuilder.create(EchoClientService.this);
		// stackBuilder.addParentStack(LoginActivity.class);
		// stackBuilder.addNextIntent(resultIntent);
		// PendingIntent resultPendingIntent =
		// stackBuilder.getPendingIntent(0,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// builder.setContentIntent(resultPendingIntent);

		nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, builder.build());

		// am = (ActivityManager)
		// ctx.getSystemService(Context.ACTIVITY_SERVICE);
		// List<RunningAppProcessInfo> procInfos =
		// am.getRunningAppProcesses();
		// for(int i = 0; i < procInfos.size(); i++){
		// if(procInfos.get(i).processName.equals("com.dev.suma_intranet_v1"))
		// {
		// Intent dialogIntent = new Intent(EchoClientService.this,
		// LoginActivity.class);
		// dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// getApplication().startActivity(dialogIntent);
		// }
		// }
		}
	}
	

	@Override
	public void onRawTextMessage(byte[] payload) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBinaryMessage(byte[] payload) {
		// TODO Auto-generated method stub
		
	}

}
