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

public class ClientScreen extends JPanel implements KeyListener, MouseListener {
   private ObjectOutputStream outObj;
   private String username="";
   private Integer id;
   private ArrayList<Sprite> spriteList;
   private boolean up,left,down,right;
   private int level;
   private boolean readyToBegin;
   private boolean nextLevel = true;
   private BufferedImage myStartImage = null;
	private BufferedImage readyButtonImage0 = null;
	private BufferedImage readyButtonImage1 = null;
   private static final int SPEED = 1;
   private static final int READY_BOX_X = 200;
   private static final int READY_BOX_Y = 605;
   private static final int READY_BOX_LENGTH = 35;
   
   
   public ClientScreen() {
      this.setLayout(null);
      spriteList = new ArrayList<Sprite>();
	  level = 0;
	  id=-1;
      readyToBegin = false;
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
      if(level == 0) {
		 g.drawImage(myStartImage, 0, 0, null);
		 if(readyToBegin) {
			 g.drawImage(readyButtonImage1, READY_BOX_X, READY_BOX_Y, null);
		 } else {
			 g.drawImage(readyButtonImage0, READY_BOX_X, READY_BOX_Y, null);
		 }
		 Font myFont = new Font("Arial",50,50);
		 g.setFont(myFont);
		 if(id == 0) {
			 g.setColor(Color.pink);
		 } else if(id == 1) {
			 g.setColor(Color.orange);
		 } else if(id == 2) {
			 g.setColor(Color.cyan);
		 } else if(id == 3) {
			 g.setColor(Color.red);
		 }
		 g.drawString("WELCOME PLAYER " + (id+1), 50,50);
	  } else if(level == 1) {
		  BufferedImage level1Image = null;
		  try {
			  level1Image = ImageIO.read(new File ("Resources/Level1Background.png"));
		  } catch (FileNotFoundException ex) {
			  System.out.println(ex);
		  } catch (IOException ex) {
			  System.out.println(ex);
		  }
		 g.drawImage(level1Image, 0, 0, null);
		  drawGrid(g);
		  
	  }
	  //always draw characters
	  for(int i=0;i<spriteList.size();i++) {
		  BufferedImage myImage = null;
		  try {
			  myImage = ImageIO.read(new File (spriteList.get(i).getFileName()));
		  } catch (FileNotFoundException ex) {
			  System.out.println(ex);
		  } catch (IOException ex) {
			  System.out.println(ex);
		  }
		 
		 g.drawImage(myImage, spriteList.get(i).getX()*50, spriteList.get(i).getY()*50, null);
	  }
   }
   
   public void createImages() {
	  try {
		  myStartImage = ImageIO.read(new File ("Resources/StartScreen2.png"));
		  readyButtonImage0 = ImageIO.read(new File ("Resources/ReadyButton0.png"));
		  readyButtonImage1 = ImageIO.read(new File ("Resources/ReadyButton1.png"));
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
   
   public void poll() {
      String hostName="localhost";
      int portNumber=1024;
      try {
         Socket serverSocket=new Socket(hostName,portNumber);
         outObj = new ObjectOutputStream(serverSocket.getOutputStream());
         outObj.flush();
         ObjectInputStream inObj = new ObjectInputStream(serverSocket.getInputStream());
         
         id=(Integer) inObj.readObject();
         System.out.println("id: "+id);
         
         while(true) {
            try {
               Thread.sleep(10);
            } catch(InterruptedException ex) {
               Thread.currentThread().interrupt();
            }
			ArrayList<Sprite> fromServer = (ArrayList<Sprite>)inObj.readObject();
			if(level == 0) {
				nextLevel = true;
				for(int i=0; i<fromServer.size(); i++) {
					if(fromServer.get(i).isPlayer()) {
						if(!fromServer.get(i).getPlayerReady()) {
							nextLevel = false;
						}
					}
				}
				if(nextLevel) {
					level = 1;
					
					//this.writeObject(fromServer);
					//fromServer = (ArrayList<Sprite>)inObj.readObject();
				}
			} if(level == 1) {
				if(nextLevel) {
					if(id == 0) {
						fromServer.get(id).move(0,0);
					} else if(id == 1) {
						fromServer.get(id).move(15,0);
					} 
					nextLevel = false;
				}
			}
			
            if(fromServer==null) {
               break;
            }
            spriteList = fromServer;
            
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
   }
   
   private void writeObject(Object o) {
      try {
         outObj.reset();
         outObj.writeObject(o);
      }
      catch(IOException ex) {
         
      }
   }
   
   public void keyPressed(KeyEvent e) {
        //System.out.println(e.getKeyCode());
        if (e.getKeyCode() == 87) { //w
            spriteList.get(id).move(spriteList.get(id).getX(),spriteList.get(id).getY()-SPEED);
        }
        if (e.getKeyCode() == 65) { //a
            spriteList.get(id).move(spriteList.get(id).getX()-SPEED,spriteList.get(id).getY());
        }
        if (e.getKeyCode() == 83) { //s
            spriteList.get(id).move(spriteList.get(id).getX(),spriteList.get(id).getY()+SPEED);
        }
        if (e.getKeyCode() == 68) { //d
            spriteList.get(id).move(spriteList.get(id).getX()+SPEED,spriteList.get(id).getY());
        }
        System.out.println("PLAYER" + id + ": " + readyToBegin);
        this.writeObject(spriteList);
      
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
			spriteList.get(id).setPlayerReady(readyToBegin);
			this.writeObject(spriteList);
		}
 
        repaint();
    }
 
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}