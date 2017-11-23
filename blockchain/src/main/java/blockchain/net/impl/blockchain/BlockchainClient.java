package blockchain.net.impl.blockchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class BlockchainClient implements Runnable{

	private static Socket sock = null;
	private static BufferedReader stdIn = null;
	private static BufferedReader in = null;
	private static BufferedWriter out = null;
	public static void main(String[] args) throws IOException {

		if (args.length < 1) {
			System.out.println("you must supply one argument: host");
			System.exit(1);
		}
		//BufferedReader and BufferedWriter automatically using UTF-8 encoding
		try {
			sock = new Socket(args[0], 7777);
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
			try {
				new Thread(new BlockchainClient()).start();
				while (true) {
					out.write(stdIn.readLine());
					out.newLine();
					out.flush();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			out.close();
			in.close();
			sock.close();
		}
	}

	public void run(){
		String fromServer;
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
