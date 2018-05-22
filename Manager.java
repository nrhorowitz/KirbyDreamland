import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Manager {
 private ArrayList<ServerThread> threads;
 private HashTable<Sprite> spriteList;
 private int level;
 private int playersReady;
 private int enemyMoveCount;
 
 
 public Manager() {
  threads = new ArrayList<ServerThread>();
  spriteList = new HashTable<Sprite>(16*16);
  level = 0;
  playersReady = 0;
  enemyMoveCount = 0;
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
    System.out.println("UPDATE");
    if(mySpriteList instanceof HashTable) {
       if(enemyMoveCount > 10) {
          System.out.println("UPDATE: MOVE ENEMIES");
          enemyMoveCount = 0;
          moveEnemies();
       } else {
          enemyMoveCount++;
       }
    }
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
       if(spriteList.get(i) != null) {
          if(spriteList.get(i).getType() == 10) {//enemy
             //find the closest player (index)
             int movement = -1; //NESW
             int enemyX = spriteList.get(i).getX();
             int enemyY = spriteList.get(i).getY();
             int enemyHash = (enemyX * 16) + enemyY;
             int closestPlayer = closestPlayer(enemyX, enemyY);
             //pick x or y to move
             int xy = (int)(Math.random() * 2);//0-x, 1-y
             while(movement == -1) {
                if(xy == 3) {
                   if(spriteList.get(enemyHash+16) == null) {
                      movement = 1;
                   } 
                } else if(xy%2 == 0) {
                   if(spriteList.get(closestPlayer).getX() > enemyX) {
                      if(spriteList.get(enemyHash+16) == null) {
                         movement = 1;
                      } else if(spriteList.get(enemyHash-16) == null) {
                         movement = 3;
                      } else if(spriteList.get(enemyHash+1) == null) {
                         movement = 2;
                      } else if(spriteList.get(enemyHash-1) == null) {
                         movement = 0;
                      }
                   } else if(spriteList.get(closestPlayer).getX() < enemyX) {
                      if(spriteList.get(enemyHash-16) == null) {
                         movement = 3;
                      }
                   } else {
                      xy++;
                   }
                } else {
                   if(spriteList.get(closestPlayer).getY() > enemyY) {
                      if(spriteList.get(enemyHash+1) == null) {
                         movement = 2;
                      }
                   } else if(spriteList.get(closestPlayer).getY() < enemyY) {
                      if(spriteList.get(enemyHash-1) == null) {
                         movement = 0;
                      }
                   } else {
                      xy++;
                   }
                }
             }
             //move sprite if legal move
             Sprite temp = spriteList.pop(i);
             if(movement == 0) {
                temp.move(temp.getX(), temp.getY()-1);
             } else if(movement == 1) {
                temp.move(temp.getX()+1, temp.getY());
             } else if(movement == 2) {
                temp.move(temp.getX(), temp.getY()+1);
             } else if(movement == 3) {
                temp.move(temp.getX()-1, temp.getY());
             }
             spriteList.add(temp);
          }
       }
    }
    update(spriteList);
 }
 private int closestPlayer(int enemyX, int enemyY) {
    Map<Integer, Integer> playerMap = new HashMap<Integer, Integer>(); //distance,index
    int maxDistance = -1;
    for(int i=0; i<spriteList.rawSize(); i++) {
       if(spriteList.getList(i).size() > 0) {
          if(spriteList.get(i).getType() < 4) {
             int currentDistance = (Math.abs(spriteList.get(i).getX()-enemyX))+(Math.abs(spriteList.get(i).getY()-enemyY));
             if(currentDistance > maxDistance) {
                playerMap.put(currentDistance,i);
                maxDistance = currentDistance;
             }
          }
       }
    }
    return playerMap.get(maxDistance);
 }
 public void resetCharacter(int characterType) {
    for(int i=0; i<spriteList.rawSize(); i++) {
       if(spriteList.get(i) != null) {
          if(spriteList.get(i).getType() == characterType) {
             Sprite temp = spriteList.pop(i);
             temp.move(0, 0);
             spriteList.add(temp);
          }
       }
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



