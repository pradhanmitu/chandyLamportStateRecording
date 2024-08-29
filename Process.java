package snapshot;

import java.util.ArrayList;

public class Process extends Thread{
	private String processName;
    private int id;
    private int state;
    private boolean markerReceived;
    private boolean canSentMarker;
    private ArrayList<Message> incomingMessages;
    private ArrayList<Message> messages;
    private ArrayList<Message> sendMessage;

    public Process(int id, String processName) {
    	this.processName = processName;
        this.id=id;
        this.state=0;
        this.markerReceived=false;
        this.canSentMarker=false;
        this.incomingMessages=new ArrayList<Message>();
        this.messages=new ArrayList<Message>();
        this.sendMessage = new ArrayList<Message>(messages);
    }
    
    public int getProcessId() {
        return id;
    }

    public String getProcessName() {
    	return processName;
    }
    
    public boolean isCanSentMarker() {
		return canSentMarker;
	}

	public void setCanSentMarker() {
		this.canSentMarker = true;
	}
	
    public boolean isMarkerReceived() {
		return markerReceived;
	}
    
    public void setState(int state) {
        this.state=state;
    }

    public int getProcessState() {
        return state;
    }

    public void setMessageList(Message message) {
		messages.add(message);
	}

    public void setSendMessageList() {
    	for (Message msg : messages) {
    	    sendMessage.add(msg); // Assuming YourObjectType has a copy constructor
    	}
	}
    
	public ArrayList<Message> getIncomingMessages() {
		return incomingMessages;
	} 

	public void run() {
		if(!messages.isEmpty()) {
			int index = (int) Math.floor(Math.random() * messages.size());
			Message msg = messages.get(index);
			try {
				Thread.sleep((int) Math.floor(Math.random() * (5000 - 1000) + 1000));
				System.out.println("Process "+this.processName+"("+this.getProcessId()+") Send Message ("+msg.getContent()+") to Process "+InputGraph.processes.get(msg.getReceiverId()-1).processName+"("+InputGraph.processes.get(msg.getReceiverId()-1).getProcessId()+")");
				messages.remove(msg);
				InputGraph.processes.get(msg.getReceiverId()-1).receiveMessage(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    
    public void receiveMessage(Message message) throws InterruptedException {
    	if(message.getContent().equalsIgnoreCase("Marker")) {
    		this.canSentMarker = true;
    		if(isMarkerReceived())
    			recordChannelStatus(message);
    		else {
    			this.markerReceived = true;
	    		Thread.sleep((int) Math.floor(Math.random() * (5000 - 1000) + 1000));
	    		this.recordState();
    		}
    	}
    	else {
            incomingMessages.add(message);
    	}
    }    

    private void recordChannelStatus(Message msg) throws InterruptedException {
    	int i = 0;
    	for(Message m : InputGraph.processes.get(msg.getSenderId()-1).messages ) {
    		if(m.getReceiverId() == this.id)
	    		if(!this.incomingMessages.contains(m)) { 
	    			System.out.println("Channel (Ch"+m.getSenderId()+m.getReceiverId()+") "+m.getContent());
	    			i++;
	    		}
    	}
    	if(i == 0)
    		System.out.println("Channel (Ch"+msg.getSenderId()+msg.getReceiverId()+") NULL");
	}

	private void sendMarker() throws InterruptedException {
    	if(this.canSentMarker == true) {
	    	for(int i = 0; i<InputGraph.processes.size(); i++) {
	    		if(InputGraph.arr[this.id-1][i] == 1) {
	    			Process p = InputGraph.processes.get(i);
	    			System.out.println("Process "+this.processName+"("+this.getProcessId()+") Send Marker to Process "+p.processName);
	    			Message marker = new Message(this.id, p.id,"Marker");
	    			p.receiveMessage(marker);
	    		}
	    	}
	    	this.canSentMarker = false;
    	}
    }

    public void recordState() throws InterruptedException {
    	System.out.println("SnapShot of Process "+this.getProcessName()+"("+this.getProcessId()+")");
    	System.out.println("SENT MESSAGE");
    	if(this.sendMessage.isEmpty())
    		System.out.println("NULL");
    	else {
	    	for(Message msg : sendMessage) {
	    		System.out.println(InputGraph.processes.get(msg.getReceiverId()-1).processName+" "+msg.getContent());
	    	}
    	}
    	System.out.println("RECEIVED MESSAGE");
    	if(this.incomingMessages.isEmpty())
    		System.out.println("NULL");
    	else {
	    	for(Message msg : incomingMessages) {
	    		System.out.println(InputGraph.processes.get(msg.getSenderId()-1).processName+" "+msg.getContent());
	    	}
    	}
    	sendMarker();
    }
}
