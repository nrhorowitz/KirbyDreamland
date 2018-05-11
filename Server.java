import java.io.*;
import java.net.*;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server {
 public static void main(String[] args) throws IOException {
  int portNumber=1024;
  ServerSocket serverSocket=new ServerSocket(1024);
  Manager manager=new Manager();
   
  while(true) {
   System.out.println("waiting for connection");
   
   Socket clientSocket=serverSocket.accept();
   ServerThread myServerThread = new ServerThread(clientSocket,manager);
   Thread myServerThreadThread = new Thread(myServerThread);
   manager.add(myServerThread);
   myServerThreadThread.start();
  }
 }
}