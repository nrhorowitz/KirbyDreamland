import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

public class Manager {
 private ArrayList<ServerThread> threads;
 private HashTable<Sprite> spriteList;
 private int level;
 private int playersReady;
 
 
 public Manager() {
  threads = new ArrayList<ServerThread>();
  spriteList = new HashTable<Sprite>(16*16);
  level = 0;
  playersReady = 0;
 }
 
 public void add(ServerThread st) {
	 if(level == 0) {
		 threads.add(st);
	 }
 }
 
 public void remove(ServerThread st) {
  threads.remove(st);
 }
 
 public HashTable<Sprite> getSpriteList() {
    return(spriteList);
 }
 
 public int addPlayer() {
   String myImage;
   int count = spriteList.size();
   if(count == 0) {
	   myImage = "Resources/Kirby1A.png";
   } else if(count == 1) {
	   myImage = "Resources/Kirby2A.png";
   } else if(count == 2) {
	   myImage = "Resources/Kirby3A.png";
   } else {
	   myImage = "Resources/Kirby4A.png";
   } 
   Sprite addSprite = new Sprite(myImage,count);
   if(spriteList.getList(addSprite.hashCode()).size() == 0) {
      spriteList.add(addSprite);
   }
   return(addSprite.hashCode());
 }
 
 public void updateSpriteList(HashTable<Sprite> mySpriteList) {
	 spriteList = mySpriteList;
	 update(spriteList);
 }

 public void update(Object mySpriteList) {
  for(int i=0;i<threads.size();i++) {
    threads.get(i).send(mySpriteList);
  }
 }
 
 public int getLevel() {
    return level;
 }
 public int getPlayersReady() {
    return playersReady;
 }
 public void setPlayersReady(int myPlayersReady) {
    playersReady = myPlayersReady;
    if(playersReady == threads.size()) {
       //everyone ready
       level++;
       update("level:" + level);
       beginLevel(level);
    }
 }
 public void moveEnemies() {
    for(int i=0; i<spriteList.rawSize(); i++) {
       
    }
 }
 private void beginLevel(int myLevel) {
    if(myLevel == 1) {
	   for(int i=0; i<spriteList.rawSize(); i++) {
		   if(spriteList.get(i) != null) {
			   if(spriteList.get(i).getType() == 0) {
				   Sprite temp = spriteList.pop(i);
				   temp.move(0, 0);
				   spriteList.add(temp);
			   } else if(spriteList.get(i).getType() == 1) {
               Sprite temp = spriteList.pop(i);
               temp.move(15, 0);
               spriteList.add(temp);
            } else if(spriteList.get(i).getType() == 2) {
               Sprite temp = spriteList.pop(i);
               temp.move(0, 13);
               spriteList.add(temp);
            } else if(spriteList.get(i).getType() == 3) {
               Sprite temp = spriteList.pop(i);
               temp.move(15, 13);
               spriteList.add(temp);
            }
		   }
	   }
	   String myImage = "Resources/Enemy2A.png";
	   int enemyCount = 0;
	   while(enemyCount < 3) {
	      int x = (int)(Math.random()*14)+1;
	      int y = (int)(Math.random()*12)+1;
	      Sprite addSprite = new Sprite("Resources/Enemy2A.png", 10, x, y);
	      if(spriteList.getList(addSprite.hashCode()).size() == 0) {
	         spriteList.add(addSprite);
	         enemyCount++;
	      }
	   }
	   int foodCount = 1;
      while(foodCount < 7) {
         int x = (int)(Math.random()*14)+1;
         int y = (int)(Math.random()*12)+1;
         Sprite addSprite = new Sprite("Resources/ItemFood" + foodCount + ".png", 20, x, y);
         if(spriteList.getList(addSprite.hashCode()).size() == 0) {
            spriteList.add(addSprite);
            foodCount++;
         }
      }
    }
  }
}



