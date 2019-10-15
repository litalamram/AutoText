package com.example.lital.autotext.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.lital.autotext.R;
import com.example.lital.autotext.alarm.AlarmSetter;
import com.example.lital.autotext.db.DbContract;
import com.example.lital.autotext.db.DbCursor;
import com.example.lital.autotext.db.DbMethods;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView mMessageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessageListView = findViewById(R.id.list_messages);
        registerForContextMenu(mMessageListView);
        DbMethods.init(this);
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     *
     * Main Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * Called when an item in the main menu is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_message:
                Log.d(TAG, "Add a new message");
                //start the Add Message activity
                Intent i = new Intent(getApplicationContext(), AddMessageActivity.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * A context menu for the messages list view
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.list_messages) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.item_menu, menu);
            //the selected list view item
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //the selected message id
            long messageID = info.id;
            //get message text
            DbCursor c = DbMethods.getMessage(messageID);
            String message = c.getMessageTxt();
            c.close();

            menu.setHeaderTitle(message);
        }
    }

    /**
     *
     * Called when an item in the context menu is selected
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long messageID = info.id; //the message selected
        int itemID = item.getItemId(); //the menu item selected
        if (itemID == R.id.action_delete_message) { //user requests to delete the message
            //delete the selected message
            AlarmSetter.cancelAlarm(MainActivity.this, messageID);
            DbMethods.deleteMessage(messageID);
            //show toast
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(MainActivity.this, "Message deleted", duration);
            toast.show();
            //update messages list
            updateUI();
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Updates the messages list view
     */
    private void updateUI() {
        Cursor cursor = DbMethods.getAllMessages();
        String[] fromColumns = {DbContract.Messages.COL_MESSAGE_TXT, DbContract.Messages.COL_PHONE_NUM, DbContract.Messages.COL_DATE};
        int[] toViews = {R.id.message_txt, R.id.phone_num, R.id.date};
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_message, cursor, fromColumns, toViews, 0);
        mMessageListView.setAdapter(adapter);
    }


}
