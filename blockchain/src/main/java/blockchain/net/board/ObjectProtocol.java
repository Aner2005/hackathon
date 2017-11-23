package blockchain.net.board;

import java.io.Serializable;
import blockchain.net.impl.blockchain.GUI;

public class ObjectProtocol {

	private ScoreBoard sb;

    public void process(Serializable msg) {
        sb.update((GUI)msg);
        
    }
    
    public void setScoreboard(ScoreBoard sb) {
    	this.sb = sb;
    }
    
}
