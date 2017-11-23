package blockchain.net.board;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import blockchain.net.srv.ConnectionHandler;

public class BoardConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    public final  ObjectProtocol protocol;
    private final ObjectEncoderDecoder encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private volatile boolean connected = true;

    public BoardConnectionHandler(Socket sock, ObjectEncoderDecoder reader, ObjectProtocol protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {

        try (Socket sock = this.sock) { //just for automatic closing
            int read;

            in = new BufferedInputStream(sock.getInputStream());

            while (connected && (read = in.read()) >= 0) {
                Serializable nextMessage = encdec.decodeNextByte((byte) read);
                if (nextMessage != null) {
                    protocol.process(nextMessage);
                    }
                }
            }

         catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

}
