package textotex.textotex;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class notificationData {
    private final String mConvName;
    private final String mFirstName;
    private final String mDate;
    private final String mContent;
    private final int mUserID;
    private final int mConversationID;
    private boolean shown;

    public notificationData(String convName, String firstName, String date, String content, int userID, int conversationID) {
        this.mConvName = convName;
        this.mFirstName = firstName;
        this.mDate = date;
        this.mContent = content;
        this.mUserID = userID;
        this.mConversationID = conversationID;
    }

    public String getConvName() { return this.mConvName; }
    public String getFirstName() { return this.mFirstName; }
    public String getDate() { return this.mDate; }
    public String getContent() { return this.mContent; }

    public void show(Context context) {
// prepare intent which is triggered if the
// notification is selected
        if(!shown) {

            Intent intent = new Intent(context, Chatroom.class);

            Bundle b = new Bundle();
            b.putInt("conversationID", this.mConversationID);
            b.putString("conversationName", this.mConvName);

            intent.putExtra("conversationID", this.mConversationID);
            intent.putExtra("conversationName", this.mConvName);

            PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT, b);

// build notification
// the addAction re-use the same intent to keep the example short
            Notification n = new Notification.Builder(context)
                    .setContentTitle(this.getConvName())
                    .setContentText(this.getContent())
                    .setSmallIcon(R.drawable.logo)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(0, n);

            shown = true;
        }
    }

}
