package controlunitsubsystem.api;

public interface CommChannel {

    /**
     * Send a message represented by a string (without new line).
     * 
     * Asynchronous model.
     * 
     * @param msg
     */
    void sendMsg(String msg);

    /**
     * To receive a message.
     * 
     * Blocking behavior.
     */
    String receiveMsg() throws InterruptedException;

    /**
     * To check if a message is available.
     * 
     * @return
     */
    boolean isMsgAvailable();

    public void close();

}
