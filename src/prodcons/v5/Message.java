package prodcons.v5;

public class Message {
    private static int idCounter = 1;
    private final int id;
    private final long producerId;

    private static synchronized int getNextId() {
        return idCounter++;
    }

    public Message(long producerId) {
        this.producerId = producerId;
        this.id = getNextId();
    }

    public int getId() {
        return id;
    }

    public long getProducerId() {
        return producerId;
    }
}
