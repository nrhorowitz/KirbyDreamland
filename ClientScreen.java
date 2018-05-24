import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ClientScreen extends JPanel implements KeyListener, MouseListener {
   private ObjectOutputStream outObj;
   private String username = "";
   private Integer id;
   private HashTable<Sprite> spriteList;
   private Map<String, BufferedImage> imageStrings;
   private boolean up,left,down,right;
   private int level;
   private int type;
   private boolean readyToBegin;
   private boolean nextLevel = true;
   private boolean completeOnce = true;
   private BufferedImage myStartImage = null;
   private BufferedImage readyButtonImage0 = null;
   private BufferedImage readyButtonImage1 = null;
   
   private static final int SPEED = 1;
   private static final int READY_BOX_X = 200;
   private static final int READY_BOX_Y = 605;
   private static final int READY_BOX_LENGTH = 35;
   private static final int INVENTORY_X = 810;
   private static final int INVENTORY_X_SCALE = 45;
   private static final int INVENTORY_Y = 40;
   private static final int INVENTORY_Y_SCALE = 130;
   private static final int INVENTORY_Y_ITEM_SCALE = 50;
   
   private static final String LEVEL_0_MUSIC = "Resources/Level0.wav";
   
   
   public ClientScreen() {
      type = -1;
      this.playSound(LEVEL_0_MUSIC);
      this.setLayout(null);
      spriteList = new HashTable<Sprite>(16*16);
     level = 0;
     id=-1;
      readyToBegin = false;
     imageStrings = new HashMap<String,BufferedImage>();
      this.addKeyListener(this);
     this.addMouseListener(this);
      this.setFocusable(true);
     createImages();
	 
   }
   
   public Dimension getPreferredSize() 
   {
        return new Dimension(1100,700);
    }
 
    public void paintComponent(Graphics g)
    {
      this.requestFocusInWindow();
      id = getLocation(type);
      drawBackground(g);
      drawHashTable(g);
   }
    
    public void drawHashTable(Graphics g) {
       for(int i=0; i<spriteList.rawSize(); i++) {
          for(int j=spriteList.getList(i).size()-1; j>=0; j--) {
             g.drawImage(imageStrings.get(spriteList.getList(i).get(j).getFileName()), spriteList.getList(i).get(j).getX()*50, spriteList.getList(i).get(j).getY()*50, null);
          }
       }
    }
   
   public void createImages() {
     try {
        BufferedImage myStartImage = ImageIO.read(new File ("Resources/StartScreen2.png"));
        BufferedImage readyButtonImage0 = ImageIO.read(new File ("Resources/ReadyButton0.png"));
        BufferedImage readyButtonImage1 = ImageIO.read(new File ("Resources/ReadyButton1.png"));
        BufferedImage kirby1AImage = ImageIO.read(new File ("Resources/Kirby1A.png"));
        BufferedImage kirby2AImage = ImageIO.read(new File ("Resources/Kirby2A.png"));
        BufferedImage kirby3AImage = ImageIO.read(new File ("Resources/Kirby3A.png"));
        BufferedImage kirby4AImage = ImageIO.read(new File ("Resources/Kirby4A.png"));
        BufferedImage level1BackgroundImage = ImageIO.read(new File ("Resources/Level1Background.png"));
        BufferedImage enemy2AImage = ImageIO.read(new File ("Resources/Enemy2A.png"));
        BufferedImage food1AImage = ImageIO.read(new File ("Resources/ItemFood1.png"));
        BufferedImage food2AImage = ImageIO.read(new File ("Resources/ItemFood2.png"));
        BufferedImage food3AImage = ImageIO.read(new File ("Resources/ItemFood3.png"));
        BufferedImage food4AImage = ImageIO.read(new File ("Resources/ItemFood4.png"));
        BufferedImage food5AImage = ImageIO.read(new File ("Resources/ItemFood5.png"));
        BufferedImage food6AImage = ImageIO.read(new File ("Resources/ItemFood6.png"));
        BufferedImage doorImage = ImageIO.read(new File ("Resources/Door.png"));
        BufferedImage level2BackgroundImage = ImageIO.read(new File ("Resources/space.png"));
        BufferedImage treeObstacleImage = ImageIO.read(new File ("Resources/treeobstacle.png"));
        imageStrings.put("Resources/StartScreen2.png",myStartImage);
        imageStrings.put("Resources/ReadyButton0.png",readyButtonImage0);
        imageStrings.put("Resources/ReadyButton1.png",readyButtonImage1);
        imageStrings.put("Resources/Kirby1A.png", kirby1AImage);
        imageStrings.put("Resources/Kirby2A.png", kirby2AImage);
        imageStrings.put("Resources/Kirby3A.png", kirby3AImage);
        imageStrings.put("Resources/Kirby4A.png", kirby4AImage);
        imageStrings.put("Resources/Level1Background.png", level1BackgroundImage);
        imageStrings.put("Resources/Enemy2A.png", enemy2AImage);
        imageStrings.put("Resources/ItemFood1.png", food1AImage);
        imageStrings.put("Resources/ItemFood2.png", food2AImage);
        imageStrings.put("Resources/ItemFood3.png", food3AImage);
        imageStrings.put("Resources/ItemFood4.png", food4AImage);
        imageStrings.put("Resources/ItemFood5.png", food5AImage);
        imageStrings.put("Resources/ItemFood6.png", food6AImage);
        imageStrings.put("Resources/Door.png", doorImage);
        imageStrings.put("Resources/space.png", level2BackgroundImage);
        imageStrings.put("Resources/treeobstacle.png", treeObstacleImage);
        
     } catch (FileNotFoundException ex) {
        System.out.println(ex);
     } catch (IOException ex) {
        System.out.println(ex);
     }
   }
   
   public void drawGrid(Graphics g) {
      g.setColor(Color.black);
      for(int i=0; i<=(800/50); i++) {
         g.drawLine((i*50),0,(i*50),700);
      }
      for(int i=0; i<=(700/50); i++) {
         g.drawLine(0,(i*50),800,(i*50));
      }
   }
   
   public void drawInventory(Graphics g) {
      Font myFont = new Font("Arial",40,40);
      g.setFont(myFont);
      for(int i=0; i<spriteList.rawSize(); i++) {
         if(spriteList.getList(i).size() > 0) {
            if(spriteList.get(i).getType() == 0) {
               g.setColor(Color.pink);
            } else if(spriteList.get(i).getType() == 1) {
               g.setColor(Color.orange);
            } else if(spriteList.get(i).getType() == 2) {
               g.setColor(Color.cyan);
            } else if(spriteList.get(i).getType() == 3) {
               g.setColor(Color.red);
            }
            if(spriteList.get(i).getType() < 4) {
               g.drawString("PLAYER " + (spriteList.get(i).getType()+1) + ":", INVENTORY_X, INVENTORY_Y + (INVENTORY_Y_SCALE*spriteList.get(i).getType()));
               for(int j=0; j<spriteList.get(i).getItemList().size(); j++) {
                  g.drawImage(imageStrings.get(spriteList.get(i).getItem(j)), INVENTORY_X + (INVENTORY_X_SCALE * j) - ((j/6) * (INVENTORY_X_SCALE * 6)), INVENTORY_Y + (INVENTORY_Y_SCALE*spriteList.get(i).getType()) + (INVENTORY_Y_ITEM_SCALE * (j/6)), null);
               }
            }
         }
      }
   }
   
   public void drawBackground(Graphics g) {
      if(level == 0) {
         g.drawImage(imageStrings.get("Resources/StartScreen2.png"),0,0,null);
         if(readyToBegin) {
            g.drawImage(imageStrings.get("Resources/ReadyButton1.png"), READY_BOX_X, READY_BOX_Y, null);
         } else {
            g.drawImage(imageStrings.get("Resources/ReadyButton0.png"), READY_BOX_X, READY_BOX_Y, null);
         }
         Font myFont = new Font("Arial",50,50);
         g.setFont(myFont);
         if(spriteList == null) {
            return;
         }
         if(spriteList.size() == 0) {
            return;
         }
         //System.out.println("ID: " + id + " SPRITELIST: " + spriteList);
         if(spriteList.get(id).getType() == 0) {
            g.setColor(Color.pink);
         } else if(spriteList.get(id).getType() == 1) {
            g.setColor(Color.orange);
         } else if(spriteList.get(id).getType() == 2) {
            g.setColor(Color.cyan);
         } else if(spriteList.get(id).getType() == 3) {
            g.setColor(Color.red);
         }
         g.drawString("WELCOME PLAYER " + (spriteList.get(id).getType()+1), 50,50);
      } else if(level == 1) {
         g.drawImage(imageStrings.get("Resources/Level1Background.png"),0,0,null);
         drawGrid(g);
         drawInventory(g);
      } else if(level == 2) {
         g.drawImage(imageStrings.get("Resources/space.png"), 0, 0, null);
      }
   }
   
   public int getLocation(int myType) {
      for(int i=0; i<spriteList.rawSize(); i++) {
         for(int j=0; j<spriteList.getList(i).size(); j++) {
            if(spriteList.get(i, j).getType() == myType) {
               return spriteList.get(i, j).hashCode();
            }
         }
      }
      return -1;
   }
   
   public int poll() {
      //String hostName="10.200.4.192";//CHANGE TO IP
      String hostName = "localhost";
      //MVLA FACULTY - 10.200.4.192
      int portNumber=1023;
      try {
         Socket serverSocket=new Socket(hostName,portNumber);
         outObj = new ObjectOutputStream(serverSocket.getOutputStream());
         outObj.flush();
         ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());
         
         id=(Integer) inObj.readObject();
         if(id == -1) {
            System.out.println("GAME ALREADY STARTED! Please connect later");
            return -1;
         }
         while(true) {
            try {
               Thread.sleep(10);
            } catch(InterruptedException ex) {
               Thread.currentThread().interrupt();
            }
            Object objectFromServer = inObj.readObject();
            if(objectFromServer instanceof String) {
               String fromServer = (String)objectFromServer;
               //System.out.println(fromServer);
               String[] commands = fromServer.split(":");
               if(commands[0].equals("level")) {
                  int levelSet = Integer.parseInt(commands[1]);
                  level = levelSet;
               } else if(commands[0].equals("sound")) {
				   String fileToPlay = commands[1];
				   System.out.println("PLAY " + fileToPlay);
				   playSound(fileToPlay);
			   }
            } else { //is a HashTable
               HashTable<Sprite> fromServer = (HashTable<Sprite>)objectFromServer;
               if((fromServer==null) || (id == -1)) {
                  break;
               }
               spriteList = fromServer;
               if(completeOnce) {
                  type = spriteList.get(id).getType();
                  System.out.println("TYPE" + type);
                  completeOnce = false;
               }
            }
            repaint();
         }
      } catch(UnknownHostException e) {
         System.err.println("Unkown Host Exception " + hostName);
         System.exit(1);
      } catch(IOException e) {
         System.err.println("Error connecting to " + hostName);
         System.exit(1);
      } catch(ClassNotFoundException e) {
         System.err.println("Error class not found " + hostName);
         System.exit(1);
      }
      return 0;
   }
   
   private void writeObject(Object o) {
      try {
         outObj.reset();
         outObj.writeObject(o);
      }
      catch(IOException ex) {
         
      }
   }
   
   public void playSound(String fileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(fileName);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
   
   public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
      try {
         outObj.reset();
         if(e.getKeyCode() == 87) { //w
            outObj.writeObject(id + ":up");
         } else if(e.getKeyCode() == 65) { //a
            outObj.writeObject(id + ":left");
         } else if(e.getKeyCode() == 83) { //s
            outObj.writeObject(id + ":down");
         } else if(e.getKeyCode() == 68) { //d
            outObj.writeObject(id + ":right");
         }
      }
      catch(IOException ex) {}
        // if (e.getKeyCode() == 87) { //w
            // spriteList.get(id).move(spriteList.get(id).getX(),spriteList.get(id).getY()-SPEED);
        // }
        // if (e.getKeyCode() == 65) { //a
            // spriteList.get(id).move(spriteList.get(id).getX()-SPEED,spriteList.get(id).getY());
        // }
        // if (e.getKeyCode() == 83) { //s
            // spriteList.get(id).move(spriteList.get(id).getX(),spriteList.get(id).getY()+SPEED);
        // }
        // if (e.getKeyCode() == 68) { //d
            // spriteList.get(id).move(spriteList.get(id).getX()+SPEED,spriteList.get(id).getY());
        // }
        //this.writeObject(spriteList);
      
        repaint();
    }
 
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
   
   public void mousePressed(MouseEvent e) {
 
        //Print location of x and y
        //System.out.println("X: " + e.getX() + ", Y: " + e.getY());
 
        //Check if mouse pressed position is in the brown box
        if (e.getX() >= READY_BOX_X && e.getX() <= (READY_BOX_X+READY_BOX_LENGTH) && e.getY() >= READY_BOX_Y && e.getY() <= (READY_BOX_Y+READY_BOX_LENGTH)) {
           readyToBegin = !readyToBegin;
           String readyMessage = id + ":" + "level" + ":" + level + ":" + readyToBegin;
           try {
              outObj.writeObject(readyMessage);
           } catch(IOException ex) {}
        }
 
        repaint();
    }
 
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}