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
   private boolean player;
   private boolean playerReady;
   private int level;
   
   public Sprite(String myFileName, boolean isPlayer) {
      x=(int)(Math.random()*13);
      y=(int)(Math.random()*13);
	  fileName = myFileName;
	  player = isPlayer;
	  playerReady = false;
	  level = 0;
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
   public boolean isPlayer() {
	   return player;
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
}