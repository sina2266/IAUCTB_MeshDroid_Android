package iauctb.ir.meshdroid.Underdark;

import java.util.List;

import iauctb.ir.meshdroid.Model.Message;
import io.underdark.transport.Link;

public interface NodeListener {
    public void msgReceived(Message message);
    public void connectedDevicesChanged(List<Link> links, Integer peersCount, Integer linksCount, Link peerThatChange, boolean isConnected);

}
