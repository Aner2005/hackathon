package blockchain.net.srv;

import blockchain.net.api.MessageEncoderDecoder;
import blockchain.net.api.MessagingProtocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import blockchain.net.impl.blockchain.*;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    public final MessagingProtocol<T> protocol;
    private final MessageEncoderDecoder<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;
    private Connections<T> conns;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoder<T> reader, MessagingProtocol<T> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {
    	
        try (Socket sock = this.sock) { //just for automatic closing
        	int connectionId = conns.add(this);
        	protocol.start(connectionId,conns);
        	int read;
            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());
            T firstResponse = protocol.process(null);
            out.write(encdec.encode(firstResponse));
            out.flush();
            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                T nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    T response = protocol.process(nextMessage);
                    if (response != null) {
                        out.write(encdec.encode(response));
                        out.flush();
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            //conns.removeGroup(connectionId);
            //System.out.println("group removed");
        }
    }
    
    public void terminate(){
    	protocol.terminate();
    	
    }
    
    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }
    
    public void connectionsSet(Connections<T> conns){
    	this.conns = conns;
    }
    
    /*public void updateChallenge(String challenge) {
    	protocol.setChallenge(challenge);
    }
    
    /*public void sendBroadcast(String bcast){
    	protocol.sendBroadcast(bcast);
    }*/
    
    public void send(T msg) {
    	try {
    		out.write(encdec.encode(msg));
    		out.flush();
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }
}
