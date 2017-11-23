package blockchain.net.srv;

import blockchain.net.impl.blockchain.*;
import blockchain.net.board.*;
import blockchain.net.api.MessageEncoderDecoder;
import blockchain.net.api.MessagingProtocol;
import java.util.regex.Pattern;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

	private final int port;
	private final Supplier<MessagingProtocol<T>> protocolFactory;
	private final Supplier<MessageEncoderDecoder<T>> encdecFactory;
	private ServerSocket sock;
	private Connections<T> conns;
	private int counter =0;

	public BaseServer(
			int port,
			Supplier<MessagingProtocol<T>> protocolFactory,
			Supplier<MessageEncoderDecoder<T>> encdecFactory) {

		this.port = port;
		this.protocolFactory = protocolFactory;
		this.encdecFactory = encdecFactory;
		this.sock = null;
		this.conns = new Connections<T>();
	}

	@Override
	public void serve() {

		try (ServerSocket serverSock = new ServerSocket(port)) {

			this.sock = serverSock; //just to be able to close
		
			/*EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ScoreBoard frame = new ScoreBoard(conns);
						frame.setVisible(true);
						conns.setScoreBoard(frame);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});*/
			
			
			
			new Thread(new Runnable(){ public void run(){
				Scanner sc = new Scanner(System.in);
				while (true) {
					String command = sc.nextLine();
					command = command.replace("\n", "").replace("\r", "");
					if (Pattern.matches("scoreboard .*", command)) {
						String host = command.substring(11);
						try  {
							BoardClient bc = new BoardClient(host,8888);
							conns.setBoardClient(bc);
							conns.updateBoard(false);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					else if (Pattern.matches("^[0-9]+$",command)) {
						try{
							int difficulty = Integer.parseInt(command);
							if (difficulty>=0 && difficulty<=10)
								conns.setDifficulty(difficulty);}
						catch(Exception e){e.printStackTrace();}
					}
					else if (Pattern.matches("^.+sav$",command))
						conns.loadBackup(command);
					else if (Pattern.matches("blocks", command))
						conns.printBlocks();
					else if (Pattern.matches("reset", command))
						conns.reset();
					else if (Pattern.matches("score", command))
						System.out.println(conns.printScore(conns.sortScore()));
					else if (Pattern.matches("difficulty", command))
						System.out.println(conns.sj.getDifficulty());
					else if (Pattern.matches("challenge", command))
						System.out.println(conns.getChallenge());
					else if (Pattern.matches("setChallenge .*", command)) {
						String newChallenge = command.substring(13);
						conns.setChallenge(newChallenge); 
					}
					else if (Pattern.matches("setDifficultyNow .*", command)){
						try{
							int d = Integer.parseInt(command.substring(17));
							conns.setDifficultyNow(d);}
						catch (Exception e) {e.printStackTrace();}
					}
					else if (Pattern.matches("groups", command)) {
						conns.printGroupList();
					}
					else if (Pattern.matches("backup", command))
						conns.backup();
					else if (Pattern.matches("updateBoard", command))
						conns.updateBoard(false);
					
				}
			}
			}).start();
			while (!Thread.currentThread().isInterrupted()) {

				Socket clientSock = serverSock.accept();
				counter += 1;
				System.out.println("Number of connections: "+counter);
				BlockingConnectionHandler<T> handler = new BlockingConnectionHandler<>(
						clientSock,
						encdecFactory.get(),
						protocolFactory.get());
				handler.connectionsSet(conns);
				execute(handler);
			}
		} catch (Exception ex) {
		}

		System.out.println("server closed!!!");
	}

	@Override
	public void close() throws IOException {
		if (sock != null)
			sock.close();
	}

	protected abstract void execute(BlockingConnectionHandler<T>  handler);

}
