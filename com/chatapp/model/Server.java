package com.chatapp.model;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
public class Server extends JFrame {
	
	ServerSocket server;
	Socket socket;
	
	BufferedReader br;
	PrintWriter out;
	
	
	//Declare Component
		private JLabel heading = new JLabel("Server Area");
		private JTextArea messageArea = new JTextArea();
		private JTextField messageInput = new JTextField();
		private Font font = new Font("Roboto",Font.PLAIN,20);
		
	public Server() {
		try {
			createGUI();
			
			
			server = new ServerSocket(7777);//first create a server
			System.out.println("Server is ready to accept connection");
			System.out.println("waiting ..");
			socket = server.accept(); //accepting the request in socket
			//read the data 
			//data will come in bytes 
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out = new PrintWriter(socket.getOutputStream()); // to display or write into object
			
			
			
			handleEvents();
			startReading();
			startWriting();
			
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}
	private void handleEvents() {
		messageInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("key released"+e.getKeyCode());
				System.out.println("keyreleased"+e.getKeyCode());
				if(e.getKeyCode() == 10) {
					String contentToSend = messageInput.getText();
					messageArea.append("Server :"+contentToSend+ "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				}
		});
		
	}
	private void createGUI() {
		// TODO Auto-generated method stub
		
		this.setTitle("Server Messager[END]");
		this.setSize(600,700);
//		this.setLocationRelativeTo();
//		Point point = b.getLocationOnScreen();
        //dialog.setLocationRelativeTo(b); // Shows over button, as doc says
//        dialog.setLocation(new Point(point.x, point.y + b.getHeight()));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//coding for component
		heading.setFont(font);
		messageArea.setFont(font);
		messageInput.setFont(font);
		heading.setIcon(new ImageIcon("clogo.png"));
		heading.setHorizontalTextPosition(SwingConstants.CENTER);
		heading.setVerticalTextPosition(SwingConstants.BOTTOM);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		messageArea.setEditable(false);
		messageInput.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		this.setLayout(new BorderLayout());
		
		//adding components to frame
		
		this.add(heading,BorderLayout.NORTH);
		JScrollPane jscrollPane = new JScrollPane(messageArea);
		this.add(jscrollPane, BorderLayout.CENTER);
		this.add(messageInput,BorderLayout.SOUTH);
		this.setVisible(true);
	}
	public void startReading() {
		//threads - > two threads one for reading and one for writing
		//read the data
//		createGUI();
		Runnable r1 =() ->{
			System.out.println("reader started ....");
			try {
			while(true) { //continous reading of data
				
				String msg = br.readLine();
				if(msg.equals("exit")) {
					System.out.println("Client terminated the chat");
					JOptionPane.showMessageDialog(this,"Server Terminated the chat");
					messageInput.setEnabled(false);
					break;
				}
//				System.out.println("Client :"+ msg);
				messageArea.append("Client :"+msg+ "\n");
				
			}
			}catch(Exception e) {
//				e.printStackTrace();
				System.out.println("connection is closed");
			}
			
		};
		new Thread(r1).start();
	}
	public void startWriting() {
		//user takes the data and sent to client
//		createGUI();
		Runnable r2 =() ->{
			System.out.println("writter started");
			try {
			while(!socket.isClosed()) {
				
					BufferedReader br1 =  new BufferedReader(new InputStreamReader(System.in));
					
					String content = br1.readLine();
					
					out.println(content);
					out.flush();
				
					if(content.equals("exit")) {
						socket.close();
						break;
					}
				}
			}catch(Exception e) {
//				e.printStackTrace();
				System.out.println("connection is closed");
			}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) {
		System.out.println("going to start server!!");
		new Server();
	}
}
