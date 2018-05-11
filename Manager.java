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
 private ArrayList<Sprite> spriteList;
 
 public Manager() {
  threads = new ArrayList<ServerThread>();
  spriteList = new ArrayList<Sprite>();
 }
 
 public void add(ServerThread st) {
  threads.add(st);
 }
 
 public void remove(ServerThread st) {
  threads.remove(st);
 }
 
 public ArrayList<Sprite> getSpriteList() {
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
   spriteList.add(new Sprite(myImage, true));
   return(spriteList.size()-1);
 }
 
 public void updatePlayers(ArrayList<Sprite> mySpriteList) {
	 spriteList = mySpriteList;
 }

 
 public void update(ArrayList<Sprite> mySpriteList) {
  for(int i=0;i<threads.size();i++) {
    threads.get(i).send(mySpriteList);
  }
 }
}