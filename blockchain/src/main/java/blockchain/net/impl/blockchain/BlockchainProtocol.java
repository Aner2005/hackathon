package blockchain.net.impl.blockchain;

import blockchain.net.api.MessagingProtocol;
import java.util.regex.Pattern;

public class BlockchainProtocol implements MessagingProtocol<String> {
	private String state = "login";
	public boolean shouldTerminate = false;
	private String firstStr;
	private String secondStr;
	private String firstHash;
	private String secondHash;
	//private String challenge;
	private String groupName;
	private Connections<String> conns;
	private int connectionId;

	public void start(int connectionId, Connections<String> conns) {
		this.conns = (Connections<String>)conns;
		this.connectionId = connectionId;
	}
	
	public String getState(){
		return state;
	}
	
	@Override
	public String process(String msg) {
		String output = null;
		if (msg==null)
			output = "Please enter your group ID";
		else {
			msg = msg.replace("\n", "").replace("\r", "");
			if (state.equals("login")) {
				if (!Pattern.matches("^[0-9]{3}$",msg))
					output = "illegal group ID. Please enter your group ID (three digits)";
				else {
					this.groupName = msg;
					output = "Please enter your password";
					state = "password";
				}
			}
			else if (state.equals("password")) {
				try{
					int passwd= Integer.parseInt(msg);
					if (Integer.decode("0x"+conns.password.secretHash(groupName))==passwd) {
						conns.addGroup(connectionId, groupName); 
						output = "The challenge is: "+conns.getChallenge()+"\nThe difficulty is: "+
								conns.sj.getDifficulty()+"\nPlease return two strings that collide";
						state = "first";
					}
					else
						output = "Incorrect password. Please enter the correct password";
				}
				catch(Exception e){
					output = "Incorrect password. Please enter the correct password";
				};
				
			}
			else if (state.equals("first")) {
				firstStr = msg;
				firstHash = conns.sj.secretHash(msg);
				state = "second";
			}
			else {
				secondStr = msg;
				secondHash = conns.sj.secretHash(msg);
				synchronized (conns){
				String isCollision = verify();
				if (isCollision.equals("collision")) {

					String newChallenge = firstStr.substring(conns.getChallenge().length()+
							groupName.length())+secondStr.substring(conns.getChallenge().length()+
									groupName.length());
					newChallenge  = conns.sj.secretHash(newChallenge);
					conns.updateScore(groupName, firstStr, secondStr);
					conns.setChallenge(newChallenge);
					String messageAll = "Announcement: A collision has been found by "+
							conns.getGroupName(groupName)+".\n"
							+ "The colliding strings are:\n"+firstStr+"\n"+secondStr+
							"\nThe new challenge is: "+newChallenge+"\nThe difficulty is: "
							+conns.sj.getDifficulty();
					//+"\nThe score is: "+conns.printScore();
					output = " $$$$$$    $$$$$$    $$$$$$   $$$$$$  \n$$    $$  $$    $$  $$    $$  $$"
							+ "    $$\n$$        $$    $$  $$    $$  $$    $$\n$$  $$$$  $$    $$  $$    "
							+ "$$  $$    $$\n$$    $$  $$    $$  $$    $$  $$    $$\n $$$$$$    $$$$$$    "
							+ "$$$$$$   $$$$$$  \n  \n   $$$$$   $$$$$$   $$$$$$$   $$\n      $$  $$    $$"
							+ "  $$    $$  $$\n      $$  $$    $$  $$    $$  $$\n      $$  $$    $$  "
							+ "$$$$$$    $$\n$$    $$  $$    $$  $$    $$  $$\n$$    $$  $$    $$  $$    "
							+ "$$    \n $$$$$$    $$$$$$   $$$$$$$   $$\n\nYou found a collision!"
							+ "\nYour score is: "
							+conns.getScore(groupName)+"\n\n"+messageAll;
					conns.broadcast(messageAll, connectionId);
					
				}
				else
					output = isCollision;
				}
				state = "first";
				
			}

		}
		return output;
	}

	private String verify() {
		String ans;
		String challenge = conns.getChallenge();
		if (firstStr.equals(secondStr)) 
			ans = "The strings are the same";
		else if (firstStr.length()<challenge.length()||secondStr.length()<challenge.length())
			ans = "The strings do not include the given challenge.\nThe challenge is: "+challenge;
		else if (!(firstStr.substring(0,challenge.length()).equals(challenge)
				&& secondStr.substring(0,challenge.length()).equals(challenge))) 
			ans = "The strings do not include the given challenge.\nThe challenge is: "+challenge;
		else if (firstStr.length()<challenge.length()+groupName.length() ||
				secondStr.length()<challenge.length()+groupName.length())
			ans = "The strings do not include the group number";
		else if (!(groupName.equals(firstStr.substring(challenge.length(),challenge.length()+
				groupName.length()))&&groupName.equals(secondStr.substring(challenge.length(),
						challenge.length()+groupName.length()))))
			ans = "The strings do not include the group number";
		else if (!firstHash.equals(secondHash))
			ans = "This is not a collision";
		else
			ans = "collision";
		return ans;
	}

	/*public void setChallenge(String newChallenge) {
		challenge = newChallenge;
	}*/

	/*public void sendBroadcast(String msg) {
		conns.broadcast(msg);
	}*/
	public void send(String msg){
		conns.send(connectionId,msg);
	}
	
	@Override
	public boolean shouldTerminate() {
		return shouldTerminate;
	}

	public void terminate(){
		shouldTerminate = true;
	}
}

