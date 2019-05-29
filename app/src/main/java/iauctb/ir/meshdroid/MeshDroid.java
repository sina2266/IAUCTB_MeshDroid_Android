package iauctb.ir.meshdroid;

import android.app.Application;

import iauctb.ir.meshdroid.Underdark.Node;

public class MeshDroid extends Application {

    public Node node;

    @Override
    public void onCreate() {
        super.onCreate();

        node = new Node(this);

    }
}
