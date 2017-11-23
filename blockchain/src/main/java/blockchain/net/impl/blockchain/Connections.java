package blockchain.net.impl.blockchain;

import blockchain.net.srv.BlockingConnectionHandler;
import blockchain.net.board.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;


public class Connections<T> {
	private HashMap<Integer,BlockingConnectionHandler<T>> conList; //connected users
	private HashMap<Integer, String> groupList;
	public HashMap<String, Integer> score;
	private HashMap<String, String> groupNames;
	public LinkedList blocks;
	private int counter = 0;
	private String challenge;
	public SHA256InJava sj;
	public SHA256InJava password;
	private int version;
	private int futureDifficulty = -1;
	private BoardClient bc = null;

	public Connections(){
		conList = new HashMap<Integer,BlockingConnectionHandler<T>>();
		groupList = new HashMap<Integer, String>();
		groupNames = new HashMap<String, String>();
		score = new HashMap<String, Integer>();
		challenge = "hackathon";
		sj = new SHA256InJava();
		password = new SHA256InJava();
		version = 1;
		blocks = new LinkedList();
		groupNames();
	}

	public void setBoardClient(BoardClient bc) {
		this.bc = bc;
	}

	public synchronized int add(BlockingConnectionHandler<T> conHand){
		counter++;
		conList.put(counter, conHand);
		return counter;
	}

	public void printGroupList(){
		System.out.println(groupList.values());	
	}

	public synchronized boolean addGroup(int connId, String groupname) {
		if (groupList.containsValue(groupname)) {
			for (Integer key: groupList.keySet()) {
				if (groupList.get(key).equals(groupname)) {
					conList.get(key).protocol.send("Aborting connection. "
							+ "A new connection with same group ID has been established.");
					conList.get(key).terminate();
					groupList.remove(key);
					conList.remove(key);
					break;
				}
			}
		}
		groupList.put(connId, groupname);
		score.putIfAbsent(groupname, 0);
		return true;
	}
	public void send(int connId, T msg) {
		conList.get(connId).send(msg);
	}
	
	
	public int getScore(String groupname){
		return score.get(groupname);
	}

	public synchronized void updateBoard(boolean collision){
		GUI gui = new GUI(collision,sortedScore(),blocks,challenge,sj.getDifficulty());
		try{
		bc.send(gui);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized void updateScore(String groupName, String string1, String string2) {
		Block b = new Block(sj.getDifficulty(),getGroupName(groupName),groupName, string1, string2, 
				challenge);
		blocks.add(b);
		System.out.println(b);
		int newScore = score.get(groupName)+100;
		score.replace(groupName,newScore);
		if (futureDifficulty!=-1) {
			sj.setDifficulty(futureDifficulty);
			futureDifficulty = -1;
		}
		backup();
		//updateBoard(true);
	}

	public synchronized String getChallenge(){
		return challenge;
	}

	public synchronized void backup(){
		try{
			FileOutputStream saveFile = new FileOutputStream("backup/"+Integer.toString(version)+".sav");
			version++;
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(challenge);
			save.writeObject(score);
			save.writeObject(blocks);
			save.writeInt(sj.getDifficulty());
			save.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loadBackup(String fileName){
		try{
			FileInputStream saveFile = new FileInputStream("backup/"+fileName);
			ObjectInputStream save = new ObjectInputStream(saveFile);
			this.challenge = (String)save.readObject();
			score = (HashMap<String, Integer>)save.readObject();
			blocks = (LinkedList)save.readObject();
			setDifficultyNow(save.readInt());
			save.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public synchronized void printBlocks() {
		for (int i=0;i<blocks.size();i++)
			System.out.println((Block)blocks.get(i));
	}

	public synchronized Object[] sortScore(){
		Object[] toSort = score.entrySet().toArray();
		Arrays.sort(toSort, new Comparator() {
			public int compare(Object o1, Object o2) {
				int i =  ((Map.Entry<String, Integer>) o2).getValue()
						.compareTo(((Map.Entry<String, Integer>) o1).getValue());
				if (i!=0)
					return i;
				return ((Map.Entry<String, Integer>) o2).getKey()
						.compareTo(((Map.Entry<String, Integer>) o1).getKey());
			}
		});
		return toSort;
	}
	
	public String[][] sortedScore(){
		Object[] sorted = sortScore();
		String[][] ans = new String[sorted.length][3];
		for (int i=0;i<sorted.length;i++) {
			ans[i][2] = ""+((Map.Entry<String, Integer>) sorted[i]).getValue();
			ans[i][1] = ((Map.Entry<String, Integer>) sorted[i]).getKey();
			ans[i][0] = getGroupName(((Map.Entry<String, Integer>) sorted[i]).getKey());
		}
		return ans;
	}
	
	public synchronized String printScore(Object[] sorted) {
		String scores = "";
		for (Object e: sorted)
			scores = scores+getGroupName(((Map.Entry<String, Integer>) e).getKey()) + " : "
					+ ((Map.Entry<String, Integer>) e).getValue()+"\n";
		return scores;
	}

	public synchronized void broadcast(T msg, int connId) {
		for (Integer key: conList.keySet()) {
			if (key!=connId&&(!conList.get(key).protocol.getState().equals("password"))&&
					(!conList.get(key).protocol.getState().equals("login")))
				conList.get(key).send(msg);
		}
	}

	public synchronized void setChallenge(String challenge) {
		this.challenge = challenge;
		backup();
		updateBoard(true);
	}

	public void setDifficulty(int d) {
		futureDifficulty = d;
	}

	public void setDifficultyNow(int d){
		sj.setDifficulty(d);
		updateBoard(false);
	}
	
	private void groupNames(){
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader("groupNames.csv"));
			while ((line = br.readLine())!=null) {
				String[] groups = line.split(",");
				groupNames.put(groups[1], groups[0]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getGroupName(String groupId) {
		String ans = groupNames.get(groupId);
		if (ans == null) {
			ans = "anonymous";
		}
		return ans;
	}
	
	public void reset() {
		for (String key: score.keySet())
			score.replace(key,0);
		setDifficultyNow(1);
		updateBoard(false);
		backup();
	}
	
	/*public boolean removeGroup(int connId) {
	System.out.println("before removal");
	printGroupList();
	conList.remove(connId);
	groupList.remove(connId);
	System.out.println("after removal");
	printGroupList();
	return true;
}*/
	

}
