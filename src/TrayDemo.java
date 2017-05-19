import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class TrayDemo extends JFrame {
	private TrayIcon trayIcon = null; // ����ͼ��
	private SystemTray tray = null; // ������ϵͳ���̵�ʵ��
	public static TrayDemo trayDemo = new TrayDemo();
	TrayDemo() {
		init();
		}
	public void init() {
		if (SystemTray.isSupported()) { // �������ϵͳ֧������
			this.tray();
			}
		this.setSize(300, 200);  
		this.setResizable(false);
		// ���ڹر�ʱ�����¼�  
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				}
			public void windowIconified(WindowEvent e) {    
				try {
					tray.add(trayIcon); // ������ͼ����ӵ�ϵͳ������ʵ����
					//setVisible(false); // ʹ���ڲ�����
					dispose();
					} catch (AWTException ex) {
						ex.printStackTrace();
						}
				}
			});
		}
	private void tray() {
		tray = SystemTray.getSystemTray(); // ��ñ�����ϵͳ���̵�ʵ��
		ImageIcon icon = new ImageIcon("images/30.gif"); // ��Ҫ��ʾ�������е�ͼ��
		PopupMenu pop = new PopupMenu(); // ����һ���Ҽ�����ʽ�˵�
		MenuItem show = new MenuItem("�򿪳���(s)");
		MenuItem exit = new MenuItem("�˳�����(x)");
		pop.add(show);
		pop.add(exit);
		trayIcon = new TrayIcon(icon.getImage(), "��������ϵͳ", pop);
		/**
		 * * ������������������������ͼ����˫��ʱ��Ĭ����ʾ����
		 * */
		trayIcon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // ���˫��
					tray.remove(trayIcon); // ��ϵͳ������ʵ�����Ƴ�����ͼ��
					setExtendedState(JFrame.NORMAL);
					setVisible(true); // ��ʾ����
					toFront();
					}
				}
			});
		show.addActionListener(new ActionListener() { // �������ʾ���ڡ��˵��󽫴�����ʾ����
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon); // ��ϵͳ������ʵ�����Ƴ�����ͼ��
				setExtendedState(JFrame.NORMAL);
				setVisible(true); // ��ʾ����
				toFront();
				}
			});
		exit.addActionListener(new ActionListener() { // ������˳���ʾ���˵����˳�����
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // �˳�����
				}
			});
		}
	/**
	 * * ��ʾ��Ϣ
	 * */
	// ���ص�һ��ʵ������
	public static TrayDemo getInstanceTrayDemo() {
		return trayDemo;
		}
	// �������¼�
	}