package url.test;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.URL;


@SuppressWarnings("serial")
public  class UrlFile extends Frame 
    implements ActionListener
{   //设置窗口
     public URL url;
     Label lblHost,lblIp,lblPort,lblProtocol,lblFile;
     TextField FileName;
     Button getButton;
     
     @SuppressWarnings("deprecation")
	public UrlFile(){
    	 super("利用URL得到网络文件");
    	 setSize(600,600);
    	 setBackground(Color.lightGray);
    	 setGUI();
    	 addWindowListener(new WindowAdapter(){
    		 public void windowClosing(WindowEvent e){
    			 System.exit(0);
    		 }
    	 });
    	 pack();
    	 show();
    	 
     }
     //实现构件的布局和实例化
	private void setGUI(){
		Panel pNorth = new Panel();
		pNorth.add(new Label("URL文件："));
		pNorth.add(FileName = new TextField());
		FileName.setColumns(25);
		pNorth.add(getButton = new Button("读取文件信息"));
		getButton.addActionListener(this);
		this.add("North",pNorth);
		Panel pCenter = new Panel();
		pCenter.setLayout(new GridLayout(5,2));
		pCenter.add(new Label("主机名：",Label.RIGHT));
		pCenter.add(lblHost = new Label("",Label.LEFT));
		pCenter.add(new Label("主机IP地址：",Label.RIGHT));
		pCenter.add(lblIp = new Label("",Label.LEFT));
		pCenter.add(new Label("端口：",Label.RIGHT));
		pCenter.add(lblPort = new Label("",Label.LEFT));
		pCenter.add(new Label("协议名称：",Label.RIGHT));
		pCenter.add(lblProtocol = new Label("",Label.LEFT));
		pCenter.add(new Label("文件名：",Label.RIGHT));
		pCenter.add(lblFile = new Label("",Label.LEFT));
		this.add("Center",pCenter);
	}
	public void actionPerformed(ActionEvent e){
		Object obj = e.getSource();
		if(obj == getButton){
			if(url != null){
				url = null;
			}
			try{
				url = new URL(FileName.getText());
				getInfo();
				saveFile();
			}catch(Exception ex){
				System.out.println(ex);
			}
		}
	}
	private void getInfo(){
		try{
			lblHost.setText(url.getHost());
			lblProtocol.setText(url.getProtocol());
			InetAddress host = InetAddress.getByName(url.getHost());
			lblIp.setText(host.getHostAddress());
			Integer port = new Integer(url.getPort());
			lblPort.setText(port.toString());
			lblFile.setText(url.getPath());
		}catch(Exception e){
			System.out.println(e);
		}
	}
	private void saveFile(){
		try{
			DataInputStream inStream = new DataInputStream(url.openStream());
			File file = new File("."+url.getFile());
			mkdir(file.getParent());
			DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
			int count = 0;
			try{
				while(true){
					outputStream.writeByte((int)inStream.readByte());
				}
			}catch(EOFException eofEX){
				inStream.close();
				outputStream.flush();
				outputStream.close();
				System.out.println(eofEX);
			}
			
		}catch(Exception e){
			System.out.println(e);
		}
	}
	private void mkdir(String strfileName){
		try{
			File newFile = new File(strfileName);
			File parentFile = new File(newFile.getParent());
			if(!parentFile.exists()) mkdir(newFile.getParent());
			newFile.mkdir();
		}catch(Exception e){}
	}
	
	public static void main(String[] args){
		new UrlFile();
	}

}
