package iauctb.ir.meshdroid;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import iauctb.ir.meshdroid.Adapter.MsgAdapter;
import iauctb.ir.meshdroid.Model.Chat;
import iauctb.ir.meshdroid.Model.Message;
import iauctb.ir.meshdroid.Underdark.Node;
import iauctb.ir.meshdroid.Underdark.NodeListener;
import io.underdark.transport.Link;

public class MainActivity extends AppCompatActivity {


    private final static int PERMISSION_REQUEST_COARSE_LOCATION = 101;

    Chat mChat;
    RecyclerView chatRv;
    MsgAdapter msgAdapter;
    EditText msgBox;
    ImageView emojiBtn, backBtn, sendBtn, moreBtn;
    TextView title, peersCountTxt;

    Node node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_REQUEST_COARSE_LOCATION);
        }


        chatRv = findViewById(R.id.activity_main__msg_chat_rv);
        msgBox = findViewById(R.id.activity_main__msg_box_et);
        emojiBtn = findViewById(R.id.activity_main__msg_emoji_btn);
        backBtn = findViewById(R.id.activity_main__toolbar_back_btn);
        sendBtn = findViewById(R.id.activity_main__msg_send_btn);
        moreBtn = findViewById(R.id.activity_main__toolbar_more_btn);
        title = findViewById(R.id.activity_main__toolbar_title);


        mChat = new Chat();

        msgAdapter = new MsgAdapter(mChat.messages);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        mLayoutManager.setStackFromEnd(true);
        chatRv.setLayoutManager(mLayoutManager);
        chatRv.setAdapter(msgAdapter);

        node = ((MeshDroid) getApplication()).node;

        node.setNodeListener(new NodeListener() {
            @Override
            public void msgReceived(Message message) {
                msgAdapter.appendMessage(message);
            }

            @Override
            public void connectedDevicesChanged(List<Link> links, Integer peersCount, Integer linksCount, Link peerThatChange, boolean isConnected) {
                if (links.size() == 0) {
                    startActivity(new Intent(MainActivity.this,SearchingActivity.class));
                    finish();
                } else {
                    peersCountTxt.setText(String.valueOf(linksCount));
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgBox.getText() != null && msgBox.getText().length() > 0) {
                    Message tempMessage = new Message();
                    tempMessage.text = msgBox.getText().toString();
                    tempMessage.isItMyMsg = true;
                    tempMessage.time = new Date().getTime();
                    tempMessage.id = "-1";
                    tempMessage.peerName = "None";

                    msgAdapter.appendMessage(tempMessage);
                    msgBox.setText("");

                    Gson gson = new Gson();
                    // byte[] = gson.toJson(tempMessage).getBytes(StandardCharsets.UTF_8);
                    String tempJson = gson.toJson(tempMessage);
                    byte[] msgBytes = tempJson.getBytes(Charset.forName("UTF-16"));
                    node.broadcastFrame(msgBytes);

                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        node.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        node.clearLinks();

        //if (node != null)
        //    node.stop();
    }

    @Override
    protected void onDestroy() {
        node.clearLinks();

        if (node != null)
            node.stop();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, yay! Start the Bluetooth device scan.
                } else {
                    // Alert the user that this application requires the location permission to perform the scan.
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        node.stop();
        node.clearLinks();
        super.onBackPressed();
    }
}
