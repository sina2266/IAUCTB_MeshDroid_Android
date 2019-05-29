package iauctb.ir.meshdroid.Underdark;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Random;

import iauctb.ir.meshdroid.Model.Message;
import io.underdark.Underdark;
import io.underdark.transport.Link;
import io.underdark.transport.Transport;
import io.underdark.transport.TransportKind;
import io.underdark.transport.TransportListener;
import io.underdark.util.nslogger.NSLogger;
import io.underdark.util.nslogger.NSLoggerAdapter;

public class Node implements TransportListener
{
    private boolean running;
    private long nodeId;
    private int appId = 226600;
    private Transport transport;
    private Context context;

    private NodeListener nodeListener;

    private ArrayList<Link> links = new ArrayList<>();
    private int framesCount = 0;

    public void setNodeListener(NodeListener nodeListener) {
        this.nodeListener = nodeListener;
    }

    public Node(Context context)
    {
        this.context = context;

        do
        {
            nodeId = new Random().nextLong();
        } while (nodeId == 0);

        if(nodeId < 0)
            nodeId = -nodeId;


        EnumSet<TransportKind> kinds = EnumSet.of(TransportKind.BLUETOOTH, TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.BLUETOOTH);

        this.transport = Underdark.configureTransport(
                appId,
                nodeId,
                this,
                null,
                context,
                kinds
        );
    }



    public void reInit(int appId){
        EnumSet<TransportKind> kinds = EnumSet.of(TransportKind.BLUETOOTH, TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.WIFI);
        //kinds = EnumSet.of(TransportKind.BLUETOOTH);

        this.transport = Underdark.configureTransport(
                appId,
                nodeId,
                this,
                null,
                context,
                kinds
        );
    }



    public void start()
    {
        if(running)
            return;

        running = true;
        transport.start();
    }

    public void stop()
    {
        if(!running)
            return;

        running = false;
        transport.stop();
    }

    public ArrayList<Link> getLinks()
    {
        return links;
    }

    public void clearLinks(){
        links = new ArrayList<>();
    }

    public int getFramesCount()
    {
        return framesCount;
    }

    public void broadcastFrame(byte[] frameData)
    {
        if(links.isEmpty())
            return;

        ++framesCount;
   //TODO     activity.refreshFrames();

        for(Link link : links)
            link.sendFrame(frameData);
    }

    //region TransportListener
    @Override
    public void transportNeedsActivity(Transport transport, ActivityCallback callback)
    {
        //callback.accept(activity);
    }

    @Override
    public void transportLinkConnected(Transport transport, Link link)
    {
        links.add(link);
  //      activity.refreshPeers();
        if (links == null){
            if (nodeListener != null) {
                nodeListener.connectedDevicesChanged(links,0,0,link,true);
            }
        } else {
            if (nodeListener != null) {
                nodeListener.connectedDevicesChanged(links, links.size(), links.size(), link, true);
            }
        } // IT'S very bad coding, i know but the clock is 3:00 AM :(
    }

    @Override
    public void transportLinkDisconnected(Transport transport, Link link)
    {
        links.remove(link);
 //       activity.refreshPeers();

        if (links == null){
            if (nodeListener != null) {
                nodeListener.connectedDevicesChanged(links,0,0,link,true);
            }
        } else {
            if (nodeListener != null) {
                nodeListener.connectedDevicesChanged(links, links.size(), links.size(), link, true);
            }
        } // IT'S very bad coding, i know but the clock is 3:00 AM :(


        if(links.isEmpty())
        {
            framesCount = 0;
  //          activity.refreshFrames();
        }
    }

    @Override
    public void transportLinkDidReceiveFrame(Transport transport, Link link, byte[] frameData)
    {
        ++framesCount;
 //       activity.refreshFrames();
        if (nodeListener != null){
            try {
                String tempString = new String(frameData,"UTF-16");


                Message mMessage = new Message();
                Gson gson = new Gson();

                mMessage = gson.fromJson(tempString,new TypeToken<Message>(){}.getType());
                mMessage.isItMyMsg = false;
                nodeListener.msgReceived(mMessage);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }
    //endregion
} // Node


