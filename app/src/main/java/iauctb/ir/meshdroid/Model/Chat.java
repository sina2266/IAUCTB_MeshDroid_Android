package iauctb.ir.meshdroid.Model;

import java.util.ArrayList;
import java.util.List;

import iauctb.ir.meshdroid.Model.Message;

public class Chat {
    public String frequencyName;
    public List<Message> messages;
    public List<String> peers;


    public Chat(String frequencyName) {
        this.frequencyName = frequencyName;
        this.messages = new ArrayList<>();
        this.peers = new ArrayList<>();
    }
    public Chat(){
        this("default");
    }
}

