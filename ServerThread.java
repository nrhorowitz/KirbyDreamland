import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

public class ServerThread implements Runnable {
 private Socket clientSocket;
 private Manager manager;
 private String username;
 private ObjectOutputStream outObj;
 private HashTable<Sprite> spriteList;
 private Integer id;
 
   public ServerThread(Socket clientSocket,Manager manager) {
      this.clientSocket=clientSocket;
      this.manager=manager;
      spriteList = new HashTable<Sprite>(14*16);

      id = manager.addPlayer();
   }
 
   public void run() {
      System.out.println(Thread.currentThread().getName()+": connected");
      try {
         InputStream input=clientSocket.getInputStream();
         outObj = new ObjectOutputStream(clientSocket.getOutputStream());
         outObj.flush();
         ObjectInputStream inObj = new ObjectInputStream(input);
         
         outObj.writeObject(id);
         outObj.writeObject(manager.getSpriteList());
         
         while(true) {
            try {
               Thread.sleep(10);
            } catch(Exception e) {
               
            }
			Object objectFromServer = inObj.readObject();
			if(objectFromServer instanceof String) {
				String fromServer = (String)objectFromServer;
				System.out.println(fromServer);
			} else { //HashTable
				spriteList = (HashTable<Sprite>)objectFromServer;
				//System.out.println("read");
				manager.update(spriteList);
				if(spriteList==null) {
				   break;
				}
			}
            
         }
         
         outObj.flush();
         outObj.close();
         System.out.println(Thread.currentThread().getName()+": closed");
         manager.remove(this);
      } catch(IOException ex) {
         System.out.println("Error listening for connection");
         System.out.println(ex.getMessage());
         manager.remove(this);
      } catch(ClassNotFoundException ex) {
         System.out.println("class not found");
         System.out.println(ex.getMessage());
         manager.remove(this);
      }
   }
 
 public String getUsername() {
  return(username);
 }
 
 public void updateSprite(Sprite mySprite, int id) {
	 //spriteList.set(id,mySprite);
 }
 
 public void send(Object p) {
  try{
   outObj.reset();
   
   outObj.writeObject(p);
  } catch(IOException e) {
   System.out.println("Error listening for connection");
   System.out.println(e.getMessage());
  }
 }
}