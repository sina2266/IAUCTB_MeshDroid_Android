package iauctb.ir.meshdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cunoraz.gifview.library.GifView;
import com.medialablk.easygifview.EasyGifView;

import java.util.List;

import iauctb.ir.meshdroid.Model.Message;
import iauctb.ir.meshdroid.Underdark.NodeListener;
import io.underdark.transport.Link;
import pl.droidsonroids.gif.GifImageView;

public class SearchingActivity extends AppCompatActivity {

    ImageView freqGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching);

       freqGif = findViewById(R.id.searching_activity__gif);
       //freqGif.setGifFromResource(R.mipmap.freq_gif);

        Glide.with(getApplicationContext()).load(R.mipmap.freq_gif).into(freqGif);

        if (((MeshDroid) getApplication()).node.getLinks().size() > 0) {
            startActivity(new Intent(SearchingActivity.this, MainActivity.class));
            finish();
        }

        ((MeshDroid) getApplication()).node.setNodeListener(new NodeListener() {
            @Override
            public void msgReceived(Message message) {

            }

            @Override
            public void connectedDevicesChanged(List<Link> links, Integer peersCount, Integer linksCount, Link peerThatChange, boolean isConnected) {
                if (links.size() > 0) {
                    startActivity(new Intent(SearchingActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MeshDroid) getApplication()).node.start();

    }

    @Override
    public void onBackPressed() {
        ((MeshDroid) getApplication()).node.stop();
        ((MeshDroid) getApplication()).node.clearLinks();
        super.onBackPressed();

    }
}
