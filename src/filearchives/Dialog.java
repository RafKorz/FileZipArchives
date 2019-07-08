package filearchives;
/**
 *
 * @author Rafał Korzeniec
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Dialog extends JDialog {
public Dialog(JFrame parent)
{
    super (parent, true);//po włączeniu apki nie da się przejść niżej
    initComponents();
    
    int szer = (int)parent.getBounds().getWidth();
    int wys = (int)parent.getBounds().getHeight();
    
    int szerRamki = this.getSize().width;
    int wysRamki = this.getSize().height;
    
    this.setLocation(parent.getBounds().x+(szer-szerRamki)/2, 
            parent.getBounds().y+(wys-wysRamki/2));
}
    public void initComponents()
{
    this.setTitle("To jest ramka numer"+(++i));
    this.setBounds(200, 200, 500, 200);   
   
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
}   
    public static void main(String[] args) {
        new JFrame().setVisible(true);
    } 
    static int i = 0;
}

