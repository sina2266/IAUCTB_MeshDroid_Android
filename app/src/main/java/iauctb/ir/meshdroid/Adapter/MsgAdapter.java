package iauctb.ir.meshdroid.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import iauctb.ir.meshdroid.Model.Message;
import iauctb.ir.meshdroid.R;

public class MsgAdapter extends RecyclerView.Adapter{

    List<Message> messages;

    final private static int MY_MSG = 1;
    final private static int YOUR_MSG = 2;


    public MsgAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void appendMessage(Message message){
        if (messages == null){
            messages = new ArrayList<>();
        }
        messages.add(message);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MY_MSG){
        return new MyMsgViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_msg,parent,false));
        } else {
            return new YourMsgViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_your_msg,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (messages.get(position).isItMyMsg){
            ((MyMsgViewHolder) holder).myMsgTxt.setText(messages.get(position).text);
        } else {
            ((YourMsgViewHolder) holder).yourMsgTxt.setText(messages.get(position).text);
            ((YourMsgViewHolder) holder).yourName.setText(messages.get(position).peerName);
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isItMyMsg ? MY_MSG : YOUR_MSG;
    }

    private class MyMsgViewHolder extends RecyclerView.ViewHolder{
        TextView myMsgTxt;
        public MyMsgViewHolder(View itemView) {
            super(itemView);
            myMsgTxt = itemView.findViewById(R.id.row_my_msg__txt);
        }
    }


    private class YourMsgViewHolder extends RecyclerView.ViewHolder{
        TextView yourMsgTxt;
        TextView yourName;
        public YourMsgViewHolder(View itemView) {
            super(itemView);
            yourMsgTxt = itemView.findViewById(R.id.row_your_msg__txt);
            yourName = itemView.findViewById(R.id.row_your_msg__name);

        }
    }
}
