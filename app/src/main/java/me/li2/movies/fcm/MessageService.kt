package me.li2.movies.fcm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.li2.movies.HomeActivity
import me.li2.movies.R
import org.json.JSONObject
import timber.log.Timber.d

class MessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        d("new token received: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        d("data payload: ${remoteMessage.data}")
        val dataPayload = JSONObject(remoteMessage.data as Map<String, String>).toString()
        MessageAPI.fromJson(dataPayload)?.let { message ->
            notifyBigPictureNotification(this, message)
        }
    }

    private fun notifyBigPictureNotification(context: Context, message: MessageAPI) {
        val intent = Intent(this, HomeActivity::class.java).also {
            it.action = Intent.ACTION_MAIN
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.putExtra(EXTRA_KEY_MESSAGE, message)
        }
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val localNotification = NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
                .setAutoCancel(NOTIFICATION_AUTO_CANCEL)
                .setSmallIcon(NOTIFICATION_SMALL_ICON)
                .setColor(ContextCompat.getColor(this, NOTIFICATION_ACCENT_COLOR))
                .setSound(RingtoneManager.getDefaultUri(NOTIFICATION_SOUND))
                .setVibrate(NOTIFICATION_VIBRATE)
                .setContentIntent(pi)
                .setContentTitle("${message.title} released on ${message.releaseDate}")
                .setContentText(message.overview)
                .setBigImage(context, message.posterUrl.toUri())
                .build()

        NotificationManagerCompat.from(context).notify(0, localNotification)
    }

    companion object {
        internal const val EXTRA_KEY_MESSAGE = "extra_key_message"
        private const val DEFAULT_CHANNEL_ID = "channel_id_1"
        private const val NOTIFICATION_AUTO_CANCEL = true
        private const val NOTIFICATION_SMALL_ICON = R.drawable.ic_notification_small_icon
        private const val NOTIFICATION_ACCENT_COLOR = R.color.primary
        private const val NOTIFICATION_SOUND = RingtoneManager.TYPE_NOTIFICATION
        private val NOTIFICATION_VIBRATE = longArrayOf(500, 500, 1000)
    }
}