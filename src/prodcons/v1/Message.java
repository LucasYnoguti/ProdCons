package prodcons.v1;

public class Message {
    private static int idCounter = 1;
    private final int id;
    private final String content;
    private final long producerId;

    private static synchronized int getNextId() {
        return idCounter++;
    }

    public Message(String content, long producerId) {
        this.content = content;
        this.producerId = producerId; // Le stocker
        this.id = getNextId();
    }

    public int getId() {
        return id;
    }

    public long getProducerId() {
        return producerId;
    }

    @Override
    public String toString() {
        return "Message #" + id + " (by P-" + producerId + "): " + content;
    }
}