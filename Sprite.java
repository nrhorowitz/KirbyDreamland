import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Sprite implements Serializable {
   private int x,y;
   private String fileName;
   private int type;
   private boolean playerReady;
   private int level;
   private ArrayList<String> itemList;
   
   public Sprite(String myFileName, int myType) {
      x=(int)(Math.random()*14)+1;
      y=(int)(Math.random()*12)+1;
	  fileName = myFileName;
	  type = myType;
	  playerReady = false;
	  level = 0;
	  itemList = new ArrayList<String>();
   }
   
   public Sprite(String myFileName, int myType, int myX, int myY) {
      x=myX;
      y=myY;
     fileName = myFileName;
     type = myType;
     playerReady = false;
     level = 0;
     itemList = new ArrayList<String>();
   }
   
   public void move(int myX,int myY) {
      x = myX;
      y = myY;
   }
   
   public void drawMe(Graphics g) {
      //g.drawImage(image,x,y,null);
   }
   public int getX() {
      return x;
   }
   public int getY() {
      return y;
   }
   public String getFileName() {
	   return fileName;
   }
   public int getType() {
	   return type;
   }
   public int getLevel() {
	   return level;
   }
   public void setLevel(int myLevel) {
	   level = myLevel;
   }
   public void setPlayerReady(boolean myPlayerReady) {
	   playerReady = myPlayerReady;
   }
   public boolean getPlayerReady() {
	   return playerReady;
   }
   public String enemyMove(String previousStep) {
      int random = (int)(Math.random() * 100);
      
      int threshold;
      if(previousStep.equals("left")) {
         threshold = 50;
      } else if(previousStep.equals("right")) {
         
      } else if(previousStep.equals("up")) {
         
      } else if(previousStep.equals("down")) {
         
      }
      if(random < 70) {
         return previousStep;
      }
      return "right";
   }
   public ArrayList<String> getItemList() {
      return itemList;
   }
   public void addItem(String c) {
      itemList.add(c);
   }
   public String getItem(int index) {
      return itemList.get(index);
   }
   @Override
   public int hashCode() {
	   int hashCode = ((x*16)+(y));
	   return hashCode;
   }
   @Override
   public String toString() {
      return (fileName + ":" + hashCode() + ",X:" + x + ",Y:" + y);
   }
}