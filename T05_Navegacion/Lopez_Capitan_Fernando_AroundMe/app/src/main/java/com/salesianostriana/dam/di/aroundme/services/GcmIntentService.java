package com.salesianostriana.dam.di.aroundme.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.salesianostriana.dam.di.aroundme.R;
import com.salesianostriana.dam.di.aroundme.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */


// En este IntentService se hace el tratamiento del mensaje recibido de GCM
// En este caso el mensaje que recibimos contiene un JSON con el tiempo que hace
// en la ciudad que hemos registrado en el Servidor remoto que controla las
// notificaciones

public class GcmIntentService extends IntentService {
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                Log.i("JSON WEATHER", "JSON: " + extras.getString("notificacion"));
                JSONArray array;
                String nick;
                String mensaje;

                try {
                    array = new JSONArray("["+extras.getString("notificacion")+"]");

                    nick = array.getJSONObject(0).getString("nickname");
                    mensaje = array.getJSONObject(0).getString("mensaje");

                    Log.i("MENSAJE RECIBIDO",array.toString());
                    Log.i("RECIBIDO DE: ",nick);
                    Log.i("CONTENIDO: ", mensaje);

                    showNotificacion(nick, mensaje);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);


    }

    protected  void showNotificacion(String recibidopor, final String contenido) {
        NotificationCompat.Builder mBuilder;

        // Definimos un identificador único para las notificaciones
        int mNotificationId = 1;
        NotificationManager mNotifyMgr = null;

        // Instancio el servicio NotificationManager,
        // para gestionar notificaciones.
        mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Intent accionIntent = new Intent(this, MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, accionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_mensajes)
                        .setContentTitle("@" + recibidopor)
                        .setContentText(contenido);

        mBuilder.setContentIntent(pIntent);
        mBuilder.setAutoCancel(true);

        // Creo la notificación
        mNotifyMgr.notify(mNotificationId, mBuilder.build());



    }
}
