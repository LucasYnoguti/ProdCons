package prodcons.v1;

public class Message {
    private final String content;
    private final String producerName;

    public Message(String content, String producerName) {
        this.content = content;
        this.producerName = producerName;
    }

    @Override
    public String toString() {
        return "Message (by P-" + producerName + "): " + content;
    }
}