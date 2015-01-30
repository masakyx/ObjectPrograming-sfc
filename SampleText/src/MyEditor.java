import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

class MyEditor {
	public static void main(String args[]){
		JTextArea ta = new JTextArea();
		ta.setFont(new Font("Dialog",Font.PLAIN,12));
		ta.setTabSize(4);
		EditorMenu em = new EditorMenu(ta);
		EditFace ef = new EditFace(ta,em);
	}
}

class EditFace extends JFrame {
	
	EditFace(JTextArea ta,EditorMenu em){
		super("はじめてのJava入門");
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(em,BorderLayout.NORTH);
		JScrollPane sp = new JScrollPane(ta);
		c.add(sp,BorderLayout.CENTER);
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}

class EditorMenu extends JMenuBar {
	
	EditorMenu(JTextArea ta){
		super();
		
		FileMenu m1 = new FileMenu(ta);
		EditMenu m2 = new EditMenu(ta);
		ViewMenu m3 = new ViewMenu(ta);
              HelpMenu m4 = new HelpMenu(ta);

		add(m1);
		add(m2);
		add(m3);
              add(m4);
	}
}

class FileMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
	JMenuItem mi4;
	JMenuItem mi5;
	
	FileMenu(JTextArea ta){
		super("ファイル");
		this.ta = ta;
		
		mi1 = new JMenuItem("新規作成");
		mi2 = new JMenuItem("開く");
		mi3 = new JMenuItem("上書き保存");
		mi4 = new JMenuItem("名前をつけて保存");
		mi5 = new JMenuItem("閉じる");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		mi5.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
		add(mi4);
		add(mi5);
	}
	
	public void actionPerformed(ActionEvent e){
		EditFileAccess efa = new EditFileAccess();
		
		Object o = e.getSource();
		
		if (o == mi1){
			ta.setText("");
		}else if (o == mi2){
			ta.setText("");
			efa.fileOpen(ta);
		}else if (o == mi3){
			if (EditorStatus.FILENAME.equals("")){
				efa.fileSave(ta);
			}else{
				efa.overWrite(ta);
			}
		}else if (o == mi4){
			efa.fileSave(ta);
		}else if (o == mi5){
			System.exit(0);
		}
	}
}

class EditMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
        JMenuItem mi4;
	
	EditMenu(JTextArea ta){
		super("編集");
		this.ta = ta;
		
		mi1 = new JMenuItem("切り取り");
		mi2 = new JMenuItem("コピー");
		mi3 = new JMenuItem("ペースト");
              mi4 = new JMenuItem("すべて選択");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
              mi4.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
              add(mi4);
	}
	
	public void actionPerformed(ActionEvent e){
		
		Object o = e.getSource();
		
		if (o == mi1){
			ta.cut();
		}else if (o == mi2){
			ta.copy();
		}else if (o == mi3){
			ta.paste();
		}else if (o == mi4){
                        ta.selectAll();

             }
	}
}

class ViewMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	JMenuItem mi3;
	JMenuItem mi4;
	
	ViewMenu(JTextArea ta){
		super("表示");
		this.ta = ta;
		
		mi1 = new JMenuItem("文字を拡大");
		mi2 = new JMenuItem("文字を縮小");
		mi3 = new JMenuItem("フォントのサイズを指定");
		mi4 = new JMenuItem("タブのサイズを指定");
		
		mi1.addActionListener(this);
		mi2.addActionListener(this);
		mi3.addActionListener(this);
		mi4.addActionListener(this);
		
		add(mi1);
		add(mi2);
		add(mi3);
		add(mi4);
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		
		if (o == mi1){
			int size = ta.getFont().getSize();
			size++;
			String name = ta.getFont().getName();
			int style = ta.getFont().getStyle();
			ta.setFont(new Font(name,style,size));
		}else if (o == mi2){
			int size = ta.getFont().getSize();
			size--;
			String name = ta.getFont().getName();
			int style = ta.getFont().getStyle();
			ta.setFont(new Font(name,style,size));
		}else if (o == mi3){
			String sizeString = JOptionPane.showInputDialog
                                 ("フォントサイズを入力してください");
			if (sizeString == null){
				return;
			}else{
				int size = Integer.parseInt(sizeString);
				String name = ta.getFont().getName();
				int style = ta.getFont().getStyle();
				ta.setFont(new Font(name,style,size));
			}
		}else if (o == mi4){
			String tabString = JOptionPane.showInputDialog
                                     ("タブサイズを入力してください");
			if (tabString == null){
				return;
			}else{
				int tabs = Integer.parseInt(tabString);
				ta.setTabSize(tabs);
			}
		}
	}
}

class HelpMenu extends JMenu implements ActionListener {
	JTextArea ta;
	JMenuItem mi1;
	JMenuItem mi2;
	
	HelpMenu(JTextArea ta){
		super("ヘルプ");
		this.ta = ta;
		
		mi1 = new JMenuItem("バージョン情報");
	
		
		mi1.addActionListener(this);
	       
		
		add(mi1);
		
		
	}
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		
		if (o == mi1){
			JOptionPane.showMessageDialog(
                  (Component)null,
                  "はじめてのJava入門\nVersjon 1\nCopyright (C) 2012 by takeharu narita\nuser takeharu narita\n-------------------------------------------------\nhttp://www1.bbiq.jp/takeharu/",
                  "バージョン情報",
                  JOptionPane.INFORMATION_MESSAGE);
		}

        
	}
}

class EditFileAccess {
	public void fileOpen(JTextArea ta){
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String s;
			while ((s = br.readLine()) != null){
				ta.append(s + '\n');
			}
		}
		catch(IOException e){
			return;
		}
		
		EditorStatus.FILENAME = f.getPath();
	}
	
	public void fileSave(JTextArea ta){
		JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(null);
		File f = fc.getSelectedFile();
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(f,false));
			
			String s = ta.getText();
			String st[] = s.split("\n");
			int limit = st.length;
			for (int i = 0;i < limit;i++){
				pw.println(st[i]);
			}
			
			pw.close();
		}
		catch(IOException e){
			return;
		}
		
		EditorStatus.FILENAME = f.getPath();
	}
	
	public void overWrite(JTextArea ta){
		File f = new File(EditorStatus.FILENAME);
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(f,false));
			
			String s = ta.getText();
			String st[] = s.split("\n");
			int limit = st.length;
			for (int i = 0;i < limit;i++){
				pw.println(st[i]);
			}
			
			pw.close();
		}
		catch(IOException e){
			return;
		}
	}
}
class EditorStatus {
	static String FILENAME = "";
}
