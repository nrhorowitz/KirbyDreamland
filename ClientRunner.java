import javax.swing.JFrame;
 
public class ClientRunner {
    public static void main(String args[]) {
 
        JFrame frame = new JFrame("Kirby's Dreamland (Pls don't sue me)");

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      //frame.setUndecorated(true);
      frame.setVisible(true);
      
      //Create panel and add it to the frame
      ClientScreen sc = new ClientScreen();
      
      frame.add(sc);
      frame.pack();
      frame.setResizable(false);
      
      int close = sc.poll();
      if(close == -1) {
         frame.setVisible(false);
      }
      //sc.animate();
    }
}