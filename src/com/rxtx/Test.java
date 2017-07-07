package com.rxtx;
 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import gnu.io.SerialPort;
import java.util.Observer;
import java.util.*;

public class Test implements Observer{ 
	SerialReader sr=new SerialReader(); 
	private Co con = new Co();
	private String record="";
	private int cnt=0;
	
	private Lock lock = new ReentrantLock();
	
    public Test()
    {
       openSerialPort(""); //�򿪴��ڡ�
    }
    public void update(Observable o, Object arg){
    	String mt=new String((byte[])arg);  
    	lock.lock();
    	if (cnt==3){
    		System.out.println(record);
    		con.Add(record);
    		System.out.println("-----------");
    		record = "";
    		cnt=0;
    	}
    	cnt++;
    	record = record + mt.replace("\n", "")+"\n";
    	lock.unlock();
    	//System.out.println(mt); //�������� 
    } 
    
    /**
     * �����ڷ�������,ʵ��˫��ͨѶ.
     * @param string message
     */
    public void send(String message)
    {
    	Test test = new Test();
    	test.openSerialPort(message);
    }
	
    /**
     * �򿪴���
     * @param String message
     */
	public void openSerialPort(String message)
    { 
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port="COM8";
        String rate = "115200";
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( SerialReader.PARAMS_PORT, port ); // �˿�����
        params.put( SerialReader.PARAMS_RATE, rate ); // ������
        params.put( SerialReader.PARAMS_DATABITS,dataBit  ); // ����λ
        params.put( SerialReader.PARAMS_STOPBITS, stopBit ); // ֹͣλ
        params.put( SerialReader.PARAMS_PARITY, parityInt ); // ����żУ��
        params.put( SerialReader.PARAMS_TIMEOUT,100 ); // �豸��ʱʱ�� 1��
        params.put( SerialReader.PARAMS_DELAY, 100 ); // �˿�����׼��ʱ�� 1��
        try {
			sr.open(params);
		    sr.addObserver(this);
			if(message!=null&&message.length()!=0)
			 {  
				sr.start();  
				sr.run(message);  
			 } 
		} catch (Exception e) { 
		}
    }
    
	 public String Bytes2HexString(byte[] b) { 
		   String ret = ""; 
		   for (int i = 0; i < b.length; i++) { 
			     String hex = Integer.toHexString(b[i] & 0xFF); 
			     if (hex.length() == 1) { 
			       hex = '0' + hex; 
				     } 
			     ret += hex.toUpperCase(); 
			   }
		return ret;
	   }

	  public  String hexString2binaryString(String hexString) {
	  if (hexString == null || hexString.length() % 2 != 0)
		 return null;
		 String bString = "", tmp;
		 for (int i = 0; i < hexString.length(); i++) {
		 tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			  bString += tmp.substring(tmp.length() - 4);
		  }
		 return bString;
	  } 
} 
 
