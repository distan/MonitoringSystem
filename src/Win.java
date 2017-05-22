import javax.swing.*;
import java.awt.*;
public class Win {
	public static void main(String args[]){	
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		JFrame win = new JFrame("MonitoringSystem");
		Container con = win.getContentPane();
		con.setBackground(Color.white);
		win.setBounds(width/4, height/4, width/2, height/2);
		win.setVisible(true);
		win.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Win testwin = new Win();
		}
	void miniTray(){
		PopupMenu pop = new PopupMenu();
		MenuItem show = new MenuItem("»¹Ô­");
		MenuItem exit = new MenuItem("ÍË³ö");
	}
}
