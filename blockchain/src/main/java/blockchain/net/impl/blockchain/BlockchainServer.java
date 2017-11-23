package blockchain.net.impl.blockchain;

import blockchain.net.srv.Server;
import blockchain.net.impl.blockchain.LineMessageEncoderDecoder;

public class BlockchainServer {
	public static void main(String[] args) {
		String tmp =  args[0];
		int port = Integer.parseInt(tmp);
		Server.threadPerClient(port,() -> new BlockchainProtocol(),   () -> new LineMessageEncoderDecoder()).serve();
	}
}
