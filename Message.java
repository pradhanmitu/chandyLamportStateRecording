package snapshot;

public class Message {
	private int senderId;
    private int receiverId;
    private String content;

    public Message(int senderId, int receiverId, String content) {
        this.senderId=senderId;
        this.receiverId=receiverId;
        this.content=content;
//        System.out.println("msg: "+this.content);
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

	public String getContent() {
        return content;
    }
}
