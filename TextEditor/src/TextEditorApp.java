
import  java.awt.*;
import  java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import  javax.swing.*;
import  javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class TextEditorApp extends JFrame implements ChangeListener, ItemListener{

	public static void main(String[] arg) {
		new TextEditorApp( );
	}

	JComboBox<String>  fontchoice;
	JSlider  slider;
	JLabel  size;
	Menu  menu;
	MenuBar  bar;
	LineNumberedTextArea  textarea;
	JFrame jf = new JFrame("置換");
	JTextField text1 = new JTextField("置換前の文字列を入力してください");
    JTextField text2 = new JTextField("置換後の文字列を入力してください");
    JButton btn = new JButton("置換");
    JButton btn2 = new JButton("検索");
    JFrame jf2 = new JFrame("検索");
    JTextField text3 = new JTextField("検索文字列を入力してください");
	
	
	public TextEditorApp( ) {
		super( "TextEditor Applicaton");
		setSize( 800, 600 );
		setTitle("新規文書");
		
		setLayout( new BorderLayout() );
		
		//置換
		jf.setSize(400,100);
		jf2.setSize(400,70);
		
	    jf.add(text1, BorderLayout.NORTH);
	    jf.add(text2, BorderLayout.CENTER);
	    jf.add(btn, BorderLayout.SOUTH);
	    
	    jf2.add(text3,BorderLayout.NORTH);
	    jf2.add(btn2,BorderLayout.SOUTH);
		
	    
	    
		
		// property panel(pp)は、プロパティを設定するコンポーネントが置かれる
		JPanel pp = new JPanel();
		pp.setLayout( new BoxLayout( pp, BoxLayout.X_AXIS) );
		
		// フォント選択メニュー
		pp.add( new Label( "Font:") );
		fontchoice = new JComboBox<String>();
		fontsetup();
		fontchoice.addItemListener( this );
		pp.add( fontchoice );
		
		// フォントサイズ調整のスライダー
		pp.add( new Label( "Size:") );
		slider = new JSlider( 8, 64 );
		slider.setPaintTicks( true );
		slider.setMajorTickSpacing( 8 );
		slider.setPaintLabels( true );
		slider.setValue( 24 );
		slider.addChangeListener( this );
		size = new JLabel( " "+slider.getValue() );
		pp.add( size );
		pp.add( slider );
		add( "North", pp );

		// 中央に、行番号付きテキストエリアを置く
		textarea = new LineNumberedTextArea( (String)(fontchoice.getSelectedItem()), slider.getValue());
		add( "Center", textarea.pane );
		
		// Fileメニューをメニューバーに追加する
		menu = new Menu( "File" );
		for ( String item: new String [] {"NEW", "OPEN","OVERWRITE","SAVE AS","CUT","COPY","PASTE","RePlace","SEARCH"} ) {
			MenuItem mi = new MenuItem( item );
			mi.setActionCommand( item );
			mi.addActionListener( new MyActionAdapter( ) );
			menu.add( mi );
		}
		bar = new MenuBar ( );
		bar.add( menu );
		setMenuBar( bar );
		
		
		
		// クローズボックスを押されたら、ウィンドウを閉じて終了する
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible( true );
	}

	// フォントの一覧を選択メニューに登録する
	void fontsetup() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font allfonts [] = ge.getAllFonts();
		for ( Font  f: allfonts ) {
			fontchoice.addItem( f.getName() );
		}
	}
	
	// 選択メニューが選ばれた場合
	@Override
	public void itemStateChanged(ItemEvent e) {
		int  s = slider.getValue();
		textarea.setRowHeight( (String)(fontchoice.getSelectedItem()), s );
		repaint();
	}

	// スライダーが動かされた場合
	@Override
	public void stateChanged(ChangeEvent e) {
		int  s = slider.getValue();
		size.setText( s+"" );
		textarea.setRowHeight( (String)(fontchoice.getSelectedItem()), s );
		repaint();
	}
	
	// メニューの項目が呼ばれた場合
	class MyActionAdapter implements ActionListener {
		//LineNumberedTextArea ta;
		@Override
		public void actionPerformed( ActionEvent ae ) {
			btn.addActionListener(this);
		    btn.setActionCommand("Btn");
		    btn2.addActionListener(this);
		    btn2.setActionCommand("Btn2");
			//MyEditFileAccess mefa = new MyEditFileAccess();
			if(ae.getActionCommand().equals("NEW")){
				setTitle("新規文書");
				textarea.fileNew();
			}
			if(ae.getActionCommand().equals("OPEN")){
				//textarea.setText("");
				textarea.fileNew();
				textarea.fileOpen();
			}
			if(ae.getActionCommand().equals("OVERWRITE")){
				if(EditorStatus.FILENAME.equals("")){
					textarea.fileSave();
				}else{
					textarea.overWrite();
				}
				System.out.println( ae.getActionCommand() );
			}else if(ae.getActionCommand().equals("SAVE AS")){
				textarea.fileSave();
				
				System.out.println( ae.getActionCommand() );				
			}else if(ae.getActionCommand().equals("CUT")){
				textarea.OnCut();		
			}else if(ae.getActionCommand().equals("COPY")){
				textarea.OnCopy();		
			}else if(ae.getActionCommand().equals("PASTE")){
				textarea.OnPaste();		
			}else if(ae.getActionCommand().equals("RePlace")){
				jf.setVisible(true);
				
			}else if(ae.getActionCommand().equals("SEARCH")){
				jf2.setVisible(true);
			}
			else if(ae.getActionCommand().equals("Btn")){
					
					textarea.RePlace(text1.getText(),text2.getText());	
			}else if(ae.getActionCommand().equals("Btn2")){
					System.out.println("Search!");
					String str[] = {text3.getText(),"win"};
					textarea.Search(str);
				
			}
		}
	}
	
	/*class MyEditFileAccess{
		public void fileOpen(LineNumberedTextArea ta){
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
			fc.setFileFilter(filter);
			fc.showOpenDialog(null);
			File f = fc.getSelectedFile();
			try{
				BufferedReader br = new BufferedReader(new FileReader(f));
				String s;
				while ((s = br.readLine()) != null){
					textarea.append(s + '\n');
					br.close();
				}
				
			}catch(IOException e){
				return;
			}
			
			EditorStatus.FILENAME = f.getPath();
			
		}
		
		public void fileSave(LineNumberedTextArea ta){
			JFileChooser fc = new JFileChooser();
			fc.showSaveDialog(null);
			File f = fc.getSelectedFile();
			
			try{
				PrintWriter pw = new PrintWriter(new FileWriter(f,false));
				String s = textarea.getText();
				String st[] = s.split("\n");
				int limit = st.length;
				for(int i=0;i<limit;i++){
					pw.println(st[i]);
				}
				pw.close();
			}catch(IOException e){
				return;
			}
			EditorStatus.FILENAME = f.getPath();
		}
		
		public void overWrite(LineNumberedTextArea ta){
			File f = new File(EditorStatus.FILENAME);
			try{
				PrintWriter pw = new PrintWriter(new FileWriter(f,false));
				String s = textarea.getText();
				String st[] = s.split("\n");
				int limit = st.length;
				for(int i=0;i<limit;i++){
					pw.println(st[i]);
				}
				pw.close();
			}catch(IOException e){
				return;
			}
		}
	}*/
}
class EditorStatus{
	static String FILENAME = "";
}
