package com.rxtx;
import gnu.io.*;
import java.io.*; 
import java.util.*;  
 
 
public class SerialReader extends Observable implements Runnable,SerialPortEventListener
    {
    static CommPortIdentifier portId;
    int delayRead = 100;
    int numBytes; // buffer�е�ʵ�������ֽ���
    private static byte[] readBuffer = new byte[1024]; // 4k��buffer�ռ�,���洮�ڶ��������
    static Enumeration portList;
    InputStream inputStream;
    OutputStream outputStream;
    static SerialPort serialPort;
    HashMap serialParams;
    Thread readThread;//������static���͵�
    //�˿��Ƿ����
    boolean isOpen = false;
    // �˿ڶ��������¼�������,�ȴ�n������ٶ�ȡ,�Ա�������һ���Զ���
    public static final String PARAMS_DELAY = "delay read"; // ��ʱ�ȴ��˿�����׼����ʱ��
    public static final String PARAMS_TIMEOUT = "timeout"; // ��ʱʱ��
    public static final String PARAMS_PORT = "port name"; // �˿�����
    public static final String PARAMS_DATABITS = "data bits"; // ����λ
    public static final String PARAMS_STOPBITS = "stop bits"; // ֹͣλ
    public static final String PARAMS_PARITY = "parity"; // ��żУ��
    public static final String PARAMS_RATE = "rate"; // ������

    public boolean isOpen(){
    	return isOpen;
    }
    /**
     * ��ʼ���˿ڲ����Ĳ���.
     * @throws SerialPortException 
     * 
     * @see
     */
    public SerialReader()
    {
    	isOpen = false;
    }

    public void open(HashMap params) 
    { 
    	serialParams = params;
    	if(isOpen){
    		close();
    	}
        try
        {
            // ������ʼ��
            int timeout = Integer.parseInt( serialParams.get( PARAMS_TIMEOUT )
                .toString() );
            int rate = Integer.parseInt( serialParams.get( PARAMS_RATE )
                .toString() );
            int dataBits = Integer.parseInt( serialParams.get( PARAMS_DATABITS )
                .toString() );
            int stopBits = Integer.parseInt( serialParams.get( PARAMS_STOPBITS )
                .toString() );
            int parity = Integer.parseInt( serialParams.get( PARAMS_PARITY )
                .toString() );
            delayRead = Integer.parseInt( serialParams.get( PARAMS_DELAY )
                .toString() );
            String port = serialParams.get( PARAMS_PORT ).toString();
            // �򿪶˿�
            portId = CommPortIdentifier.getPortIdentifier( port );
            serialPort = ( SerialPort ) portId.open( "SerialReader", timeout );
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener( this );
            serialPort.notifyOnDataAvailable( true );
            serialPort.setSerialPortParams( rate, dataBits, stopBits, parity );
            
            isOpen = true;
        }
        catch ( PortInUseException e )
        {
           // �˿�"+serialParams.get( PARAMS_PORT ).toString()+"�Ѿ���ռ��";
        }
        catch ( TooManyListenersException e )
        {
           //"�˿�"+serialParams.get( PARAMS_PORT ).toString()+"�����߹���";
        }
        catch ( UnsupportedCommOperationException e )
        {
           //"�˿ڲ������֧��";
        }
        catch ( NoSuchPortException e )
        {
          //"�˿�"+serialParams.get( PARAMS_PORT ).toString()+"������";
        }
        catch ( IOException e )
        {
           //"�򿪶˿�"+serialParams.get( PARAMS_PORT ).toString()+"ʧ��";
        }
        serialParams.clear();
        Thread readThread = new Thread( this );
        readThread.start();
    }

     
    public void run()
    {
        try
        {
            Thread.sleep(50);
        }
        catch ( InterruptedException e )
        {
        }
    } 
    public void start(){
   	  try {  
      	outputStream = serialPort.getOutputStream();
   	     } 
   	catch (IOException e) {}
   	try{ 
   	    readThread = new Thread(this);
     	readThread.start();
   	} 
   	catch (Exception e) {  }
   }  //start() end


   public void run(String message) {
   	try { 
   		Thread.sleep(4); 
           } 
   	 catch (InterruptedException e) {  } 
   	 try {
   		 if(message!=null&&message.length()!=0)
   		 { 	 
   			 System.out.println("run message:"+message);
   	        outputStream.write(message.getBytes()); //�����ڷ������ݣ���˫��ͨѶ�ġ�
   		 }
   	} catch (IOException e) {}
   } 
    

    public void close() 
    { 
        if (isOpen)
        {
            try
            {
            	serialPort.notifyOnDataAvailable(false);
            	serialPort.removeEventListener();
                inputStream.close();
                serialPort.close();
                isOpen = false;
            } catch (IOException ex)
            {
            //"�رմ���ʧ��";
            }
        }
    }
    
    public void serialEvent( SerialPortEvent event )
    {
        try
        {
            Thread.sleep( delayRead );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
        switch ( event.getEventType() )
        {
            case SerialPortEvent.BI: // 10
            case SerialPortEvent.OE: // 7
            case SerialPortEvent.FE: // 9
            case SerialPortEvent.PE: // 8
            case SerialPortEvent.CD: // 6
            case SerialPortEvent.CTS: // 3
            case SerialPortEvent.DSR: // 4
            case SerialPortEvent.RI: // 5
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 1
                try
                {
                    // ��ζ�ȡ,���������ݶ���
                     while (inputStream.available() > 0) {
                     numBytes = inputStream.read(readBuffer);
                     }
                     
                     //��ӡ���յ����ֽ����ݵ�ASCII��
                     for(int i=0;i<numBytes;i++){
                    	// System.out.println("msg[" + numBytes + "]: [" +readBuffer[i] + "]:"+(char)readBuffer[i]);
                     }
//                    numBytes = inputStream.read( readBuffer );
                    changeMessage( readBuffer, numBytes );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    // ͨ��observer pattern���յ������ݷ��͸�observer
    // ��buffer�еĿ��ֽ�ɾ�����ٷ��͸�����Ϣ,֪ͨ�۲���
    public void changeMessage( byte[] message, int length )
    {
        setChanged();
        byte[] temp = new byte[length];
        System.arraycopy( message, 0, temp, 0, length );
        notifyObservers( temp );
    }

    static void listPorts()
    {
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() )
        {
            CommPortIdentifier portIdentifier = ( CommPortIdentifier ) portEnum
                .nextElement();
            
        }
    }
    
    
    public void openSerialPort(String message)
    {
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String port="COM1";
        String rate = "9600";
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( SerialReader.PARAMS_PORT, port ); // �˿�����
        params.put( SerialReader.PARAMS_RATE, rate ); // ������
        params.put( SerialReader.PARAMS_DATABITS,dataBit  ); // ����λ
        params.put( SerialReader.PARAMS_STOPBITS, stopBit ); // ֹͣλ
        params.put( SerialReader.PARAMS_PARITY, parityInt ); // ����żУ��
        params.put( SerialReader.PARAMS_TIMEOUT, 100 ); // �豸��ʱʱ�� 1��
        params.put( SerialReader.PARAMS_DELAY, 100 ); // �˿�����׼��ʱ�� 1��
        try {
			open(params);//�򿪴���
			//LoginFrame cf=new LoginFrame();
			//addObserver(cf);
			//Ҳ����������һ��ͨ��LoginFrame���󶨴��ڵ�ͨѶ���.
			if(message!=null&&message.length()!=0)
			 {
				String str="";
				for(int i=0;i<10;i++)
				{
					str+=message;
				}
				 start(); 
			     run(str);  
			 } 
		} catch (Exception e) { 
		}
    }

    static String getPortTypeName( int portType )
    {
        switch ( portType )
        {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }

     
    public  HashSet<CommPortIdentifier> getAvailableSerialPorts()//����static
    {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while ( thePorts.hasMoreElements() )
        {
            CommPortIdentifier com = ( CommPortIdentifier ) thePorts
                .nextElement();
            switch ( com.getPortType() )
            {
                case CommPortIdentifier.PORT_SERIAL:
                    try
                    {
                        CommPort thePort = com.open( "CommUtil", 50 );
                        thePort.close();
                        h.add( com );
                    }
                    catch ( PortInUseException e )
                    {
                        System.out.println( "Port, " + com.getName()
                            + ", is in use." );
                    }
                    catch ( Exception e )
                    {
                        System.out.println( "Failed to open port "
                            + com.getName() + e );
                    }
            }
        }
        return h;
    }
}