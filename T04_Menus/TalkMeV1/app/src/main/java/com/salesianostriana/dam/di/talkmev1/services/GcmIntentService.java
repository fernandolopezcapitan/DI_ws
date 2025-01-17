package com.salesianostriana.dam.di.talkmev1.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.salesianostriana.dam.di.talkmev1.R;

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

                Log.i("JSON Message", "JSON: " + extras.getString("Mensaje nuevo"));
                JSONArray jsonMensaje;
                try {
                    jsonMensaje = new JSONArray("[{"+extras.getString("alerta")+"]}");
                    String mensajeRecibido = jsonMensaje.getJSONObject(0).getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
                    String ciudad = jsonMensaje.getJSONObject(0).getJSONArray("list").getJSONObject(0).getString("name");

                    //showToast(mensajeRecibido);
                    showNotificacion(mensajeRecibido, ciudad);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //showToast("Weather recibido");
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected  void showNotificacion(final String tiempo, final String ciudad) {
        NotificationCompat.Builder mBuilder;

        // Definimos un identificador único para las notificaciones
        int mNotificationId = 1;
        NotificationManager mNotifyMgr = null;

        // Instancio el servicio NotificationManager,
        // para gestionar notificaciones.
        mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        Log.i("SERVICIO", "Tiempo: " + tiempo);
        /*
        int icono = R.drawable.ic_notify_cloud;

        if(tiempo.equals("Rain")) {
            icono = R.drawable.ic_notify_rain;
        } else if (tiempo.equals("Clear")) {
            icono = R.drawable.ic_notify_sun;
        }

        mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(icono)
                        .setContentTitle("El tiempo en "+ ciudad)
                        .setContentText(tiempo);

        // Creo la notificación
        mNotifyMgr.notify(mNotificationId, mBuilder.build());*/
    }
}
