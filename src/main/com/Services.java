package main.com;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class Services
{
	private static ServerSocket MyServer;
	public static GlobalVars gVars;
	public static String SERVERIP = "192.168.43.107";
	public static final int SERVERPORT = 4012;


	/**
	 * @param args
	 * @throws IOException
	 */
	public static void ServerThread()
	{
		try
		{
			if (SERVERIP != null)
	        {
				MyServer = new ServerSocket(SERVERPORT);
	            while (true)

	            {
	            // listen for incoming clients
	            	System.out.print("Listening...\n");
	            	Socket client = MyServer.accept();
	            	System.out.print("Connected.\n");
	            	try
	            	{
	            		ObjectInputStream in = new ObjectInputStream(client.getInputStream());
	            		Object buffer;
	            		while ((buffer=in.readObject())!= null)
	            		{
	            			String temp=buffer.toString();
	            			if (temp.equals("Lock"))
	            			{
	            				for(int i=0;i<3;i++)
	            					gVars.SensorXYZ[i]=0;
	            				continue;
	            			}
	            			if (temp.equals("Sen"))
	            			{
	            				buffer=in.readObject();
	            				temp=buffer.toString();
	            				gVars.SensorXYZ[0]=Integer.parseInt(temp);

	            				buffer=in.readObject();
	            				temp=buffer.toString();
	            				gVars.SensorXYZ[1]=Integer.parseInt(temp);
	            				buffer=in.readObject();
	            				temp=buffer.toString();
	            				gVars.SensorXYZ[2]=Integer.parseInt(temp);
	            			}
	            			else
	            			{
	            				Robot KeyControl=new Robot();
	            				int key_value=Integer.parseInt(temp);
	            				KeyControl.keyPress(key_value);
	            				KeyControl.keyRelease(key_value);
	            			}

	            		}
	            	}
	            	catch (Exception e)
	            	{
	            		System.out.print("Connection interrupted. Please reconnect your phones.\n");
	            		e.printStackTrace();
	            	}
	            }
	        }
	        else
	        {
	        	System.out.print("Couldn't detect internet connection.\n");
	        }
	    }
	    catch (Exception e)
	    {
	    	System.out.print("Error.\n");
	    	e.printStackTrace();
	    }
		System.out.print("Stoped.\n");
	}

	//-----------------------------------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, AWTException
	{
		InetAddress thisIp =InetAddress.getLocalHost();
		System.out.println("IP:"+thisIp.getHostAddress());
		//SERVERIP =thisIp.getHostAddress();
		gVars=new GlobalVars();
		Thread KeyUP=new Thread(
				new Runnable() {
					@Override
					public void run()
					{
						try
						{
							Robot KeyControl=new Robot();
							while (true)
							{
								Thread.sleep(1);
								if (gVars.SensorXYZ[2]>2)
								{
									KeyControl.keyPress(KeyEvent.VK_UP);
									if (gVars.SensorXYZ[2]<6)
									{
										Thread.sleep(Math.abs(gVars.SensorXYZ[2]*gVars.koffSeconds));
										KeyControl.keyRelease(KeyEvent.VK_UP);
										Thread.sleep(Math.abs(gVars.koffSeconds*(10-gVars.SensorXYZ[2])));
									}
								}
							}
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						catch (AWTException e)
						{
							e.printStackTrace();
						}
					}
				});
		Thread KeyDown=new Thread(
				new Runnable() {
					@Override
					public void run()
					{
						try
						{
							Robot KeyControl=new Robot();
							while (true)
							{
								Thread.sleep(1);
								if (gVars.SensorXYZ[2]<-2)
								{
									KeyControl.keyPress(40);
									if (gVars.SensorXYZ[2]>-6)
									{
										Thread.sleep(Math.abs(gVars.SensorXYZ[2]*gVars.koffSeconds));
										KeyControl.keyRelease(40);
										Thread.sleep(Math.abs(gVars.koffSeconds*(10+gVars.SensorXYZ[2])));
									}
								}
							}
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						catch (AWTException e)
						{
							e.printStackTrace();
						}
					}
				});
		Thread KeyLeft=new Thread(
				new Runnable() {
					@Override
					public void run()
					{
						try
						{
							Robot KeyControl=new Robot();
							while (true)
							{
								Thread.sleep(1);
								if (gVars.SensorXYZ[1]<-2)
								{
									KeyControl.keyPress(KeyEvent.VK_LEFT);
									if (gVars.SensorXYZ[1]<-6) continue;
									Thread.sleep(Math.abs(gVars.SensorXYZ[1]*gVars.koffSeconds));
									KeyControl.keyRelease(KeyEvent.VK_LEFT);
									Thread.sleep(Math.abs(gVars.koffSeconds*(1+gVars.SensorXYZ[2])));
								}
							}
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						catch (AWTException e)
						{
							e.printStackTrace();
						}
					}
				});
		Thread KeyRight=new Thread(
				new Runnable() {
					@Override
					public void run()
					{
						try
						{
							Robot KeyControl=new Robot();
							while (true)
							{
								Thread.sleep(1);
								if (gVars.SensorXYZ[1]>2)
								{
									KeyControl.keyPress(KeyEvent.VK_RIGHT);
									if (gVars.SensorXYZ[1]>6) continue;
									Thread.sleep(Math.abs(gVars.SensorXYZ[1]*gVars.koffSeconds));
									KeyControl.keyRelease(KeyEvent.VK_RIGHT);
									Thread.sleep(Math.abs(gVars.koffSeconds*(10-gVars.SensorXYZ[1])));
								}
							}
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						catch (AWTException e)
						{
							e.printStackTrace();
						}
					}
				});
		KeyUP.start();
		KeyDown.start();
		KeyLeft.start();
		KeyRight.start();


		/*JFrame frm = new JFrame();
        frm.setSize(100, 100);
        JTextField tf = new JTextField();
        tf.setSize(80, 25);
        tf.setLocation(10, 10);
        tf.addKeyListener(new KeyListener()
            {
            public void keyPressed(KeyEvent e)
            {
                System.out.println("keyPressed");
                System.out.println(e.getKeyCode());
            }

            public void keyReleased(KeyEvent e)
            {
                System.out.println("keyReleased");
                System.out.println(e.getKeyCode());
            }

            public void keyTyped(KeyEvent e)
            {
                System.out.println("keyTyped");
                System.out.println(e.getKeyChar());
            }
        });

        frm.getContentPane().setLayout(null);
        frm.getContentPane().add(tf);
        frm.setVisible(true);
        frm.setDefaultCloseOperation(frm.EXIT_ON_CLOSE);

		*/
		ServerThread();
		KeyUP.stop();
		KeyDown.stop();
		KeyLeft.stop();
		KeyRight.stop();
	}
	//-----------------------------------------------------------------------------------------------
}
