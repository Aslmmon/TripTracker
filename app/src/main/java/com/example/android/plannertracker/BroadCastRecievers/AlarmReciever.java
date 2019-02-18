package com.example.android.plannertracker.BroadCastRecievers;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.android.plannertracker.NewPlan;

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent goToAlarmDialogClass = new Intent(context,AlarmDialog.class);
        goToAlarmDialogClass.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(goToAlarmDialogClass);

        Toast.makeText(context, "This is alarm", Toast.LENGTH_SHORT).show();

    }


}
