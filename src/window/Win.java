package window;
import javax.swing.*;
import java.awt.*;
public class Win extends JFrame{
	JComboBox<String> comBox;
	JButton button1;
	//JPanel win;
	JLabel label1;
	public Win(){
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screensize.getWidth();
		int height = (int)screensize.getHeight();
		//win = new JPanel();
//		Container con = win.getContentPane();
//		con.setBackground(Color.white);
		setBounds(width/4, height/4, width/2, height/2);
		init();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	void init(){
		setLayout(null);
		label1 = new JLabel("�����б�");
		label1.setBounds(30,30,100,20);
		add(label1);
		
		comBox = new JComboBox<String>();
		comBox.setBounds(100, 30, 100, 20);
		comBox.addItem("ˮ�¼��");
		comBox.addItem("ˮѹ���");
		comBox.addItem("�Ƕȼ��");
		add(comBox);
	}
	public static void main(String args[]){
		Win test = new Win();
		}
}
