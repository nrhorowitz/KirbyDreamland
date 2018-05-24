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
      spriteList = new HashTable<Sprite>(16*16);

      id = manager.addPlayer();
      manager.update(manager.getSpriteList());
   }
 
   @SuppressWarnings("unchecked")
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
            //update SpriteList to be the same as the manager spritelist
            spriteList = manager.getSpriteList();
            
   			Object objectFromServer = inObj.readObject();
   			if(objectFromServer instanceof String) {
   				String fromServer = (String)objectFromServer;
   				//System.out.println(fromServer);
   				String[] commandArray = fromServer.split(":");
   				int uniqueId = Integer.parseInt(commandArray[0]);
   				//System.out.println("UNIQUEID: " + uniqueId + " SPRITE:" + spriteList.get(uniqueId) + "\nLIST:" + spriteList);
   				String commandMessage = commandArray[1];
   				if(commandMessage.equals("level")) {
   				   String readyToBeginMessage = commandArray[3];
   				   if(readyToBeginMessage.equals("true")) {
   				      manager.setPlayersReady(manager.getPlayersReady()+1);
   				   } else if(readyToBeginMessage.equals("false")) {
   				      manager.setPlayersReady(manager.getPlayersReady()-1);
   				   }
   				} else if(commandMessage.equals("up")) {
   				   if(spriteList.get(uniqueId).getY() > 0) {
   				      if(spriteList.getList((uniqueId-1)).size() == 0) {
   				         Sprite temp = spriteList.pop(uniqueId);
   				         temp.move(temp.getX(),temp.getY()-1);
   				         spriteList.add(temp);
   				      } else {
   				         if(spriteList.get(uniqueId-1).getType() == 20) { //cake
   				            Sprite temp = spriteList.pop(uniqueId);
   				            Sprite food = spriteList.pop(uniqueId-1);
                           temp.move(temp.getX(),temp.getY()-1);
                           temp.addItem(food.getFileName());
                           spriteList.add(temp);
						   send("sound:Resources/kirbymove.wav");
   				         } else if(spriteList.get(uniqueId-1).getType() == 10) { //enemy
                           Sprite temp = spriteList.pop(uniqueId);
                           Sprite food = spriteList.pop(uniqueId-1);
                           temp.move(temp.getX(),temp.getY()-1);
                           temp.addItem(food.getFileName());
                           spriteList.add(temp);
						   send("sound:Resources/kirbymove.wav");
                        } else if(spriteList.get(uniqueId-1).getType() == 30) { //door
                           Sprite temp = spriteList.pop(uniqueId);
                           temp.move(temp.getX(),temp.getY()-1);
                           spriteList.addToFront(temp);//ADDTOFRONT
                           send("sound:Resources/kirbymove.wav");
                           manager.checkPlayersReady();
                        }
   				      }
   				   }
   				} else if(commandMessage.equals("down")) {
   				   if(spriteList.get(uniqueId).getY() < 13) {
   				      if(spriteList.getList((uniqueId+1)).size() == 0) {
   				         Sprite temp = spriteList.pop(uniqueId);
                        temp.move(temp.getX(),temp.getY()+1);
                        spriteList.add(temp);
   				      } else {
                        if(spriteList.get(uniqueId+1).getType() == 20) { //cake
                           Sprite temp = spriteList.pop(uniqueId);
                           Sprite food = spriteList.pop(uniqueId+1);
                           temp.move(temp.getX(),temp.getY()+1);
                           temp.addItem(food.getFileName());
                           spriteList.add(temp);
						   send("sound:Resources/kirbymove.wav");
                        } else if(spriteList.get(uniqueId+1).getType() == 10) { //enemy
                           manager.resetCharacter(spriteList.get(uniqueId).getType());
						   send("sound:Resources/kirbydamage.wav");
                        } else if(spriteList.get(uniqueId+1).getType() == 30) { //door
                           Sprite temp = spriteList.pop(uniqueId);
                           temp.move(temp.getX(),temp.getY()+1);
                           spriteList.addToFront(temp);//ADDTOFRONT
                           send("sound:Resources/kirbymove.wav");
                           manager.checkPlayersReady();
                        }
                     }
   				   }
               } else if(commandMessage.equals("left")) {
                  if(spriteList.get(uniqueId).getX() > 0) {
                     if(spriteList.getList((uniqueId-16)).size() == 0) {
                        Sprite temp = spriteList.pop(uniqueId);
                        temp.move(temp.getX()-1,temp.getY());
                        spriteList.add(temp);
                     } else {
                        if(spriteList.get(uniqueId-16).getType() == 20) { //cake
                           Sprite temp = spriteList.pop(uniqueId);
                           Sprite food = spriteList.pop(uniqueId-16);
                           temp.move(temp.getX()-1,temp.getY());
                           temp.addItem(food.getFileName());
                           spriteList.add(temp);
						   send("sound:Resources/kirbymove.wav");
                        } else if(spriteList.get(uniqueId-16).getType() == 10) { //enemy
                           manager.resetCharacter(spriteList.get(uniqueId).getType());
						   send("sound:Resources/kirbydamage.wav");
                        } else if(spriteList.get(uniqueId-16).getType() == 30) { //door
                           Sprite temp = spriteList.pop(uniqueId);
                           temp.move(temp.getX()-1,temp.getY());
                           spriteList.addToFront(temp);//ADDTOFRONT
                           send("sound:Resources/kirbymove.wav");
                           manager.checkPlayersReady();
                        }
                     }
                  }
               } else if(commandMessage.equals("right")) {
                  if(spriteList.get(uniqueId).getX() < 15) {
                     if(spriteList.getList((uniqueId+16)).size() == 0) {
                        Sprite temp = spriteList.pop(uniqueId);
                        temp.move(temp.getX()+1,temp.getY());
                        spriteList.add(temp);
                     } else {
                        if(spriteList.get(uniqueId+16).getType() == 20) { //cake
                           Sprite temp = spriteList.pop(uniqueId);
                           Sprite food = spriteList.pop(uniqueId+16);
                           temp.move(temp.getX()+1,temp.getY());
                           temp.addItem(food.getFileName());
                           spriteList.add(temp);
						   send("sound:Resources/kirbymove.wav");
                        } else if(spriteList.get(uniqueId+16).getType() == 10) { //enemy
                           manager.resetCharacter(spriteList.get(uniqueId).getType());
						   send("sound:Resources/kirbydamage.wav");
                        } else if(spriteList.get(uniqueId+16).getType() == 30) { //door
                           Sprite temp = spriteList.pop(uniqueId);
                           temp.move(temp.getX()+1,temp.getY());
                           spriteList.addToFront(temp);//ADDTOFRONT
                           send("sound:Resources/kirbymove.wav");
                           manager.checkPlayersReady();
                        }
                     }
                  }
               }
   				manager.updateSpriteList(spriteList);
   			} else { //HashTable
   				spriteList = (HashTable<Sprite>)objectFromServer;
   				if(spriteList==null) {
   				   break;
   				}
   			}
   			manager.update(spriteList);
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