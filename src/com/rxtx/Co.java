package com.rxtx;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.sql.PreparedStatement;

public class Co {
	private ArrayList<Element> Con = new ArrayList<Element>();
	final int COUNT =1;
	public boolean Equal(Element a,Element b){
		if ( a.turb==b.turb && a.temperature==b.temperature && a.ph==b.ph )
			return true;
		return false;
	}

	private String Transfr(String s){
		s = _Sort(s);
		if (s.equals("")) return "";
		String [] arr = s.split("\n");
		String ans ="";
		for ( int i=0; i<arr.length; ++i ){
			if (arr[i].startsWith("H")){
				if ( arr[i].length()>3 )  return "";
				ans = ans + arr[i].substring(1,3)+" ";
			}else{
				if ( arr[i].startsWith("Z") && arr[i].length()>8 ) return "";
				if ( arr[i].startsWith("T") && arr[i].length()>9 ) return "";
				ans = ans + arr[i].substring(5,7)+" ";	
			}
		}
		return ans;	
	}

	private String _Sort(String s){
		String [] ss = s.split("\n");
		String _turb = "";
		String _temp = "";
		String _ph = "";
		for ( int i=0; i<ss.length; ++i ){
			if (ss[i].startsWith("Z")) _turb=ss[i];
			if (ss[i].startsWith("H")) _ph = ss[i];
			if (ss[i].startsWith("T")) _temp = ss[i];
		}
		System.out.println("_turb="+_turb+" _temp="+_temp+" _ph="+_ph);
		if ( _turb=="" || _temp=="" || _ph=="" ) return "";
		return _turb+"\n"+_temp+"\n"+_ph;
	}

	public boolean Add(String r){
		String record = Transfr(r);
		if ( record.equals("") ) return false;
		
		if ( Con.size()>=COUNT ){
			if (Save()){
				System.out.println("synchronized database successfully...");
			}
			else{
				System.out.println("synchronized error...");
				return false;
			}
			Con.clear();
		}
		String [] d = record.split(" ");
		int turb_ = Integer.parseInt(d[0]);
		int temperature_ = Integer.parseInt(d[1]);
		int ph_ = Integer.parseInt(d[2]);
		Element new_e = new Element(turb_,temperature_,ph_);
		Con.add(new_e);
		System.out.println("add successfully...");
		return true;
	}

	//debug print()
	public void Print(){
		Element e = Con.get(Con.size()-1);
		System.out.println(e.turb+" "+e.temperature+" "+e.ph+" "+e.nowTime);
	}

	//更新数据库
	private boolean Save(){
		int error_flag=0;
		Connection con = getConnection();
		try{
			Statement sql = con.createStatement();
			sql.execute("create table new_schema.datas(turb int not null,temperature int not null,ph int not null,time datetime not null);");	
		}
		catch(Exception ee){}
		for ( int i=0; i<Con.size(); ++i ){
			Element e = Con.get(i);
			try{
				String sqlq = "insert into new_schema.datas(turb,temperature,ph,time) values(?,?,?,?)";
				PreparedStatement pre =(PreparedStatement) con.prepareStatement(sqlq);
				pre.setInt(1,e.turb);
				pre.setInt(2,e.temperature);
				pre.setInt(3, e.ph);
				pre.setTimestamp(4,e.nowTime); 
				pre.execute();
			}
			catch ( Exception eee){
				System.out.println("1");
				return false;
			}
		}
		try{
			//con.commit();
			con.close(); 
		}
		catch (Exception ex){
			System.out.println("2");
			return false;
		}
		return true;
	}

	public static Connection getConnection(){
		String driver="com.mysql.jdbc.Driver";       
   	 	String url="jdbc:mysql://127.0.0.1:3306/";    
    	String user="root";  						  
    	String password="WANG0916shijing";    
        Connection conn = null;
        try{
        	Class.forName(driver);  
        }
        catch ( ClassNotFoundException e ){
        	System.out.println("Driver error");
        }     
        try{ 
        	conn = DriverManager.getConnection(url, user, password);
	        if(!conn.isClosed())          
	          	System.out.println("Succeeded connecting to the Database!");
	        else
	          	System.out.println("Failed connecting to the Database!");
	        return conn;  
	    }
	    catch ( Exception e ){
	    	System.out.println("Failed connecting to the Database!");
	    }
	    return conn;
	}
}

class Element{
	public int turb;
	public int ph;
	public int temperature;
	public Timestamp nowTime;
	Element(int t,int tp,int po){
		turb = t;
		temperature = tp;
		ph = po;
		nowTime = new Timestamp(new Date().getTime());
	}
}