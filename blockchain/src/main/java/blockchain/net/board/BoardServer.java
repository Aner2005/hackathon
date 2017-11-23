package blockchain.net.board;

import java.awt.EventQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import blockchain.net.impl.blockchain.GUI;

public class BoardServer {
	private final int port;
	private ServerSocket sock;
	private ScoreBoard sb;

	public BoardServer(int port){
		this.port = port;
	}
	public void serve(){
		try (ServerSocket serverSock = new ServerSocket(port)) {
			
			this.sock = serverSock; 
			GUI gui = new GUI(false,new String[0][2], new LinkedList(), "",0);
	        EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						sb = new ScoreBoard(gui);
						sb.setVisible(true);
						

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Socket clientSock = serverSock.accept();
			BoardConnectionHandler handler = new BoardConnectionHandler(
					clientSock,
					new ObjectEncoderDecoder(),
					new ObjectProtocol());
            new Thread(handler).start();
            handler.protocol.setScoreboard(sb);
            
	}
		catch (Exception e){
			e.printStackTrace();
		}
}}
