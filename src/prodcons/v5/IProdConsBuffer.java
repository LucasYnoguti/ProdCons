package prodcons.v5;

public interface IProdConsBuffer {
    /**
     * Put the message m in the prodcons buffer
     **/
    public void put(Message m) throws InterruptedException;
    /**
     * Retrieve a message from the prodcons buffer, following a fifo order
     **/
    public Message get() throws InterruptedException;
    /**
     * Retrieve n consecutive messages from the prodcons buffer
     **/
    public Message[] get(int k) throws InterruptedException;

    /**
     * Returns the number of messages currently available in
     * the buffer
     **/
    public int nmsg();

    /**
     * Returns the total number of messages that have
     * been put in the buffer since its creation
     **/
    public int totmsg();

    /**
     * Signal to the buffer that a producer is done
     */
    public void producerDone() throws InterruptedException;
}

