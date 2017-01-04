import java.net.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.Date;

public class Client extends JFrame implements ItemListener {
	private static final long serialVersionUID = 1L;

	public JPanel jp1;
	public JPanel jp2;
	public JPanel jp3;
	public JButton jb;
	public JButton jb3;
	public JButton jb4;
	public JTextArea jta1;
	public JTextArea jta2;
	public JScrollPane jsp1;
	public JScrollPane jsp2;
	public DataOutputStream dos;
	public DataInputStream dis;
	public List list1;
	public List list2;
	public List list3;
	public JLabel image1;
	public JLabel image2;

	public static String s = "";

	public Client() {
		try {

			Socket s1 = new Socket("localhost", 1679);// connected with
														// server，local
														// server， port number
														// 8888

			jp1 = new JPanel();
			jp2 = new JPanel();
			jp3 = new JPanel();
			jp1.setBackground(Color.gray);
			jp3.setBackground(Color.darkGray);
			jp2.setBackground(Color.gray);
			jb = new JButton(" send");
			jb3 = new JButton(" clear");
			jb4 = new JButton(" history");
			list1 = new List(3, false);
			list2 = new List(3, false);
			list3 = new List(3, false);
			ImageIcon icon1 = new ImageIcon("img1.jpg");
			ImageIcon icon2 = new ImageIcon("img2.jpg");
			image1 = new JLabel(icon1);
			image2 = new JLabel(icon2);
			this.setLayout(new BorderLayout());
			this.add(jp1, "North");
			this.add(jp2, "South");
			this.add(jp3, "Center");

			this.setTitle(" Client");

			jta1 = new JTextArea(12, 35);
			jsp1 = new JScrollPane(jta1);
			jta1.setEditable(false);
			String s = " chatting request, IP address:\n" + s1.getInetAddress()
					+ "\n";
			jta1.append(s);

			jp1.add(jsp1);
			jp1.add(image1);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();

			String fontName[] = ge.getAvailableFontFamilyNames();
			int sizeName[] = { 5, 10, 13, 16, 19, 21, 24, 27, 30, 35, 40, 45,
					50, 55 };
			String colorName[] = { " red", "black ", "blue", " green",
					"yellow ", "gray", " cyan" };

			for (int i = 0; i < fontName.length; i++) {
				list1.add(fontName[i]);
			}
			for (int i = 0; i < sizeName.length; i++) {
				list2.add(Integer.toString(sizeName[i]));
			}
			for (int i = 0; i < colorName.length; i++) {
				list3.add(colorName[i]);
			}

			// jp3.add(list1);
			jp3.add(list2);
			jp3.add(list3);
			list1.addItemListener(this);
			list2.addItemListener(this);
			list3.addItemListener(this);
			jta2 = new JTextArea(10, 30);
			jsp2 = new JScrollPane(jta2);
			jp2.add(jsp2);
			jp2.add(jb);
			jp2.add(image2);
			jp3.add(jb4);
			jp3.add(jb3);

			clearScreen1 qp = new clearScreen1(jta1);
			jb3.addActionListener(qp); // Monitor clear screen button

			jb4.addActionListener(new inforRecord1()); // messages recording
														// button

			InputStream is = s1.getInputStream();
			dis = new DataInputStream(is); // Processing the input stream object

			OutputStream os = s1.getOutputStream();
			dos = new DataOutputStream(os); // Processing the output stream
											// object

			new MyThreadRead1(dis, jta1).start(); // Start reading from the
													// input stream data thread
			MyAction1 t = new MyAction1(dos, jta2, jta1);
			jb.addActionListener(t);// Send button
			this.pack();
			this.setLocation(700, 200);

			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {// main

		new Client();
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == list1)

		{
			String name = list1.getSelectedItem();
			String size = list2.getSelectedItem();
			Font f = new Font(name, Font.BOLD, Integer.parseInt(size));

			jta1.setFont(f);
			jta2.setFont(f);
		} else if (e.getSource() == list2) {
			String name = list1.getSelectedItem();
			String size = list2.getSelectedItem();
			Font f = new Font(name, Font.BOLD, Integer.parseInt(size));

			jta1.setFont(f);
			jta2.setFont(f);
		} else if (e.getSource() == list3) {
			String color = list3.getSelectedItem();
			if (color.equals(" red")) {
				jta1.setForeground(Color.red);
				jta2.setForeground(Color.red);
			} else if (color.equals(" cyan")) {
				jta1.setForeground(Color.cyan);
				jta2.setForeground(Color.cyan);
			} else if (color.equals(" blue")) {
				jta1.setForeground(Color.blue);
				jta2.setForeground(Color.blue);
			} else if (color.equals(" green")) {
				jta1.setForeground(Color.green);
				jta2.setForeground(Color.green);
			} else if (color.equals(" yellow")) {
				jta1.setForeground(Color.yellow);
				jta2.setForeground(Color.yellow);
			} else if (color.equals(" gray")) {
				jta1.setForeground(Color.gray);
				jta2.setForeground(Color.gray);
			} else {
				jta1.setForeground(Color.black);
				jta2.setForeground(Color.black);
			}
		}

	}
}

class MyAction1 implements ActionListener {// Processing Send buttom
	private DataOutputStream dos;
	private JTextArea jta2;
	private JTextArea jta1;

	public MyAction1(DataOutputStream dos, JTextArea jta2, JTextArea jta1) {
		this.dos = dos;
		this.jta2 = jta2;
		this.jta1 = jta1;
	}

	public void actionPerformed(ActionEvent e) {
		new MyThreadWriter1(dos, jta2, jta1).start();// Start to write the data
														// from the program to
														// the output stream
														// thread
	}
}

class MyThreadWriter1 extends Thread {// processing output stream
	private DataOutputStream dos;
	private JTextArea jta2;
	private JTextArea jta1;

	public MyThreadWriter1(DataOutputStream dos, JTextArea jta2, JTextArea jta1) {
		this.dos = dos;
		this.jta2 = jta2;
		this.jta1 = jta1;

	}

	public void run() {

		String info;

		try {

			info = jta2.getText();

			if (info != "")

			{
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd   HH-mm-ss");
				Date date = new Date();
				String st = dateformat.format(date);// date formated
				String st1 = "\nClient :";
				String st2 = "\n";
				jta1.append(st + st1 + info + st2);

				Client.s += st + st1 + info + st2;
				dos.writeUTF(info);

				jta2.setText("");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class MyThreadRead1 extends Thread {// read from stream
	private DataInputStream dis;
	private JTextArea jta1;

	public MyThreadRead1(DataInputStream dis, JTextArea jta1) {
		this.dis = dis;
		this.jta1 = jta1;

	}

	public void run() {

		while (true) {
			try {
				SimpleDateFormat dateformat = new SimpleDateFormat(
						"yyyy-MM-dd   HH:mm:ss");
				Date date = new Date();
				String st = dateformat.format(date);
				String str = dis.readUTF();
				String st1 = "\nServer :";
				String st2 = "\n";
				jta1.append(st + st1 + str + st2);
				Client.s += st + st1 + str + st2;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class clearScreen1 implements ActionListener {// clear screen
	private JTextArea jta;

	public clearScreen1(JTextArea jta) {
		this.jta = jta;
	}

	public void actionPerformed(ActionEvent e) {
		jta.setText("");
	}
}

class inforRecord1 extends JFrame implements ActionListener {//
	private static final long serialVersionUID = 3L;
	public JPanel jp1;
	public JTextArea jta;
	public JScrollPane jsp;

	public JLabel jl;
	public JPanel jp2;

	public void actionPerformed(ActionEvent e) {
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp2.setBackground(Color.darkGray);
		jp1.setBackground(Color.gray);
		jta = new JTextArea(10, 30);
		jsp = new JScrollPane(jta);
		jl = new JLabel(" history :", JLabel.CENTER);
		jl.setFont(new Font("Serif", Font.BOLD, 25));
		jl.setForeground(Color.red);
		this.setLayout(new BorderLayout());
		this.add(jp1, "South");
		this.add(jp2, "North");

		jp2.add(jl);
		jp1.add(jsp);

		this.setVisible(true);
		this.setLocation(1000, 100);
		this.pack();
		this.setTitle(" history");
		jta.setEditable(false);
		jta.setText(Client.s);

	}
}
