package snapshot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class InputGraph {
	static int v,e, arr[][]=new int[100][100];
	static ArrayList<Process> processes=new ArrayList<Process>();
	
	public static void getGraphFromFile() {
		try {
            // File reading
			String filePath = "C:\\Users\\Mitu Pradhan\\Desktop\\Java\\operating system\\snapshotchandy\\src\\snapshot\\input.txt";
            File f = new File(filePath);
            Scanner fr = new Scanner(f);
            //it will help to maintain process id
            int k=1;

            int lineNumber = 1; // line counter of the file
            while (fr.hasNextLine()) {
                String data = fr.nextLine();
                
                //file representation: at line 2 all vertices are there separated by space
                if (lineNumber == 2) {
                    String[] ver = data.split(" "); //split the line based on space and form an array
                    v = ver.length;
                    for (String vertex : ver) {  //from the array one by one value is taken and added into the list of vertices
                    	processes.add(new Process(k++,vertex));
                    }
                } 
                //at line 4 in the file : how many edges are there is given
                else if (lineNumber == 4) {
                    e = Integer.parseInt(data.trim());  //take that line position data and convert into integer to take no of edge of the graph
                } 
                //from line 6 upto end in the file the adjacency of vertices means edges are given
                else if (lineNumber >= 6 && lineNumber<=6+e) { // edges start at line 6
                    String[] edge = data.split(" ");  //split that data (line of text) based on space and store into an array
                    if (edge.length >= 2) {
                        String v1 = edge[0];  //first position value in the array stored into v1
                        String v2 = edge[1];  //second position value in the array stored into v2
                        //index1 and index2 maintains the position of the processes in adjacency matrix
                        int index1=(int)v1.charAt(0)-65;
                        int index2=(int)v2.charAt(0)-65;
                        if (index1 != -1 && index2 != -1) {
                            arr[index1][index2] = 1; // Update the adjacency matrix for the edge
                        } else {
                            System.out.println("Invalid vertices entered: " + v1 + ", " + v2);
                        }
                    } else {
                    	fr.nextLine();
                    	while (fr.hasNextLine()){
                        	String line=fr.nextLine();
                        	String words[]=line.split(",");
                        	int sender=Integer.valueOf(words[0]);
                        	int receiver=Integer.valueOf(words[1]);
                        	String message=words[2];
                        	//since the index of array processes start from 0 and the id of process start from 1
                        	sendMessage(processes.get(sender-1),processes.get(receiver-1),message);
                        }
                    }
                }
                lineNumber++;
            }
            fr.close();                 
		}
			catch (FileNotFoundException e) {
				e.printStackTrace();
		}
	}
    
    public boolean isInitiator(int[][] adjMatrix, int start, ArrayList<Process> vertices) {
    	boolean isSink = false;
        for (int i = 0; i < v; i++) {
        	int outDegree = 0;
            for (int j = 0; j < v; j++) {
            	//since row-wise connection means out-degree
            	if(arr[i][j] == 1)
            		outDegree += 1;
            }
            if(outDegree == 0)
            	isSink = true;
        }
    	
        if(isSink)
        	return false;
        else {
	        boolean[] visited = new boolean[v]; //an array is maintained for the vertices to check its status visited or not
	        Queue<Integer> queue = new LinkedList<>(); //queue to insert the adjacent nodes
	
	        visited[start] = true; //for the starting node mark as visited
	        queue.add(start); //insert start into queue
	
	        int count=1;
	        while (!queue.isEmpty()) {
	            int curr = queue.poll();
	            //System.out.print(vertices.get(curr) + " ");
	
	            for (int i = 0; i < v; i++) {
	            	//check if the it is adjacent and as well as its status is unvisited then entered
	                if (adjMatrix[curr][i] == 1 && !visited[i]) {
	                    visited[i] = true;
	                    count++;
	                    queue.add(i);
	                }
	            }
	        }
	        return (count == v);
        }
    }
	
	public static void sendMessage(Process sender,Process receiver,String messageContent) {
        Message message=new Message(sender.getProcessId(),receiver.getProcessId(),messageContent);
        sender.setMessageList(message);
    }
}
