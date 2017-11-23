package blockchain.net.board;



public class BoardServerMain {
	public static void main(String[] args) {
		String tmp =  args[0];
		int port = Integer.parseInt(tmp);
		new BoardServer(port).serve();
	}
}
