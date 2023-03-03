package com.chatapp.model;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

public class Client extends JFrame {
	
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
	//Declare Component
	private JLabel heading = new JLabel("Client Area");
	private JTextArea messageArea = new JTextArea();
	private JTextField messageInput = new JTextField();
	private Font font = new Font("Roboto",Font.PLAIN,20);
	
	public Client() {
		
		try {
			
			
			System.out.println("Sending request to server");
			socket = new Socket("127.0.0.1",7777);
			System.out.println("Connection done");
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream()); // to display or write into object
			
			createGUI();
			handleEvents();
			startReading();
//			startWriting();
			
		}catch(Exception e) {
//			e.printStackTrace();
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
				System.out.println("keyreleased");
				if(e.getKeyCode() == 10) {
					String contentToSend = messageInput.getText();
					messageArea.append("Client: "+contentToSend+ "\n");
					out.println(contentToSend);
					out.flush();
					messageInput.setText("");
					messageInput.requestFocus();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("keyreleased");
			}
		});
		
	}
	private void createGUI() {
		// TODO Auto-generated method stub
		this.setTitle("Client Messager[END]");
		this.setSize(600,700);
		this.setLocationRelativeTo(null);
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
		
		Runnable r1 =() ->{
			System.out.println("reader started ....");
			try {
			while(true) { //continous reading of data
				
				String msg = br.readLine();
				if(msg.equals("exit")) {
					System.out.println("Server terminated the chat");
					JOptionPane.showMessageDialog(this,"Server Terminated the chat");
					messageInput.setEnabled(false);
					socket.close();
					break;
				}
//				System.out.println("Server :"+ msg);
				messageArea.append("Server :"+ msg+ "\n");
				
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
			System.out.println("connection is closed");
			}catch(Exception e) {
				e.printStackTrace();
//				System.out.println("connection is closed");
			}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) {
		System.out.println("going to client");
		new Client();
	}
}
