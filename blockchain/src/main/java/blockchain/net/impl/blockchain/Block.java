package blockchain.net.impl.blockchain;

import java.io.Serializable;

public class Block implements Serializable{
	public int difficulty;
	public String solvingGroupName;
	public String groupId;
	public String string1;
	public String string2;
	public String challenge;
	private static final long serialVersionUID = 5581860349790336531L;
	
	public Block(int difficulty, String groupName,String groupId, String string1, String string2,
			String challenge) {
		this.difficulty = difficulty;
		this.solvingGroupName = groupName;
		this.groupId = groupId;
		this.string1 = string1;
		this.string2 = string2;
		this.challenge = challenge;
	}
	
	public String toString() {
		return "Difficulty: "+difficulty+"\nSolving Group Name: "+solvingGroupName+"\nSolving Group ID: "
				+groupId+"\nFirst string: "+string1+"\nSecond string: "+string2+"\nChallenge: "+challenge;
	}
}
