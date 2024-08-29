package snapshot;

public class ChandyLamportStateRecording {

    public static void main(String[] args) throws InterruptedException {
        // Input processes
    	InputGraph input=new InputGraph();
    	InputGraph.getGraphFromFile();

    	for(Process p :InputGraph.processes) {
    		p.setSendMessageList();
    		p.start();
    	}
        // Initiator initiates the snapshot
    	int i;
        for(i=0;i<InputGraph.processes.size();i++) {
        	if(input.isInitiator(InputGraph.arr, i, InputGraph.processes))
        		break;
        }
        
        InputGraph.processes.get(i).receiveMessage(new Message(0,0,"Marker"));;
    }	
}
