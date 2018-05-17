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
 
 public Manager() {
  threads = new ArrayList<ServerThread>();
  spriteList = new HashTable<Sprite>(14*16);
 }
 
 public void add(ServerThread st) {
  threads.add(st);
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
 }

 public void update(Object mySpriteList) {
  for(int i=0;i<threads.size();i++) {
    threads.get(i).send(mySpriteList);
  }
 }
}