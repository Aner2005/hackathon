package blockchain.net.impl.blockchain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import blockchain.net.srv.BlockingConnectionHandler;

public class GUI implements Serializable{

	public boolean collision;
	public String[][] score;
	public LinkedList blocks;
	public String challenge;
	public int difficulty;
	private static final long serialVersionUID = 5581860349790336532L;


	public GUI(boolean collision, String[][] score, LinkedList blocks, String challenge, 
			int difficulty) {
		this.score = score;
		this.blocks = blocks;
		this.challenge = challenge;
		this.difficulty = difficulty;
	}

	public String toString()
	{
		String scoreString = "";
		for (int i=0;i<score.length;i++) {
			scoreString += "\nname: "+score[i][0];
			scoreString += "\nid: "+score[i][1];
			scoreString += "\nscore: "+score[i][2];
		}
		return "Difficulty: "+difficulty+"\nChallenge: "+challenge+"\nscore: "+scoreString;
	}

}
