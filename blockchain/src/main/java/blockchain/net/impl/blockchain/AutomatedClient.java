package blockchain.net.impl.blockchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;
import java.util.HashMap;

public class AutomatedClient implements Runnable{

	private  int counter = 0;
	private  String challenge;
	private  String groupId;
	private  Socket sock = null;
	private  BufferedReader stdIn = null;
	private  BufferedReader in = null;
	private  BufferedWriter out = null;
	private  String password;
	public  AutomatedClient(String groupId, String password) throws Exception {
		try {
			sock = new Socket("localhost", 7777);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())); 
			stdIn = new BufferedReader(new InputStreamReader(System.in));
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		if (sock!=null && in!=null & out!=null) {

			this.groupId = groupId;
			this.password = password;
		}
	}


public  void run(){
	try {

		send(groupId);
		Thread.sleep(1000);
		send(password);
		Thread.sleep(1000);
		runServer();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	try {
		out.close();
		in.close();
		sock.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public  void send(String msg){
	try{
		out.write(msg);
		out.newLine();
		out.flush();
	}
	catch (Exception e){
		e.printStackTrace();
	}
}
public  void runServer(){
	String fromServer;
	try {
		while ((fromServer = in.readLine()) != null) {
			System.out.println(fromServer);
			process(fromServer);
		}
	}
	catch (IOException e){
		e.printStackTrace();
	}
	
}

public  void process(String fromServer) {
	//System.out.println(fromServer);
	int index= fromServer.indexOf("challenge is:");
	if (index != -1) {
		challenge = fromServer.substring(index+14);
		/*try {
			Thread.sleep((int)(Math.random()*20000));
		}
		catch (Exception e) {
			e.printStackTrace();
		}*/
		//System.out.println("challenge "+challenge);
		String[] collStrings = findCollision();
		//System.out.println("first string: "+collStrings[0]);
		//System.out.println("second string: "+collStrings[1]);
		counter = 0;
		send(collStrings[0]);
		send(collStrings[1]);
	}

}

public  String[] findCollision()
{

	SHA256InJava sj = new SHA256InJava();

	HashMap<String, String> hashes = new HashMap<String, String>();
	String hash="";String toHash;
	while(true)
	{
		//toHash=getSaltString();
		toHash=getNext();
		//	System.out.println("Counter = "+counter);

		hash=sj.secretHash(toHash);
		if (!hashes.containsKey(hash))
			hashes.put(hash,toHash);
		else
			break;
	}
	String[] ans = {toHash, hashes.get(hash)};
	return ans;
}

protected  String getNext()
{
	counter++;
	String ans = challenge+groupId;
	ans += String.valueOf(counter);
	return ans;

}

}
