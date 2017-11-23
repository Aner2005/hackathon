package blockchain.net.api;

import blockchain.net.impl.blockchain.*;

public interface MessagingProtocol<T> {
 
    /**
     * process the given message 
     * @param msg the received message
     * @return the response to send or null if no response is expected by the client
     */
    T process(T msg);
 
    /**
     * @return true if the connection should be terminated
     */
    String getState();
    boolean shouldTerminate();
    void terminate();
    void start(int connectionId, Connections<T> connections);
    void send(String msg);
    /*void setChallenge(String challenge);
    
    /*void sendBroadcast(String msg);*/
}