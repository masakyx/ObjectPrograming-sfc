
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
public class TextEditorApp extends JFrame implements ChangeListener, ItemListener {

	public static void main(String[] arg) {
		new TextEditorApp( );
	}

	JComboBox<String>  fontchoice;
	JSlider  slider;
	JLabel  size;
	Menu  menu;
	MenuBar  bar;
	LineNumberedTextArea  textarea;
	
	public TextEditorApp( ) {
		super( "TextEditor Applicaton");
		setSize( 800, 600 );
		
		setLayout( new BorderLayout() );
		
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
		for ( String item: new String [] {"NEW", "OPEN","OVERWRITE","SAVE AS"} ) {
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
		JTextArea ta;
		@Override
		public void actionPerformed( ActionEvent ae ) {
			MyEditFileAccess mefa = new MyEditFileAccess();
			if(ae.getActionCommand().equals("NEW")){
				ta.setText("");
			}
			if(ae.getActionCommand().equals("OPEN")){
				ta.setText("");
				mefa.fileOpen(ta);
			}
			if(ae.getActionCommand().equals("OVERWRITE")){
				if(EditorStatus.FILENAME.equals("")){
					mefa.fileSave(ta);
				}else{
					mefa.overWrite(ta);
				}
				System.out.println( ae.getActionCommand() );
			}else if(ae.getActionCommand().equals("SAVE AS")){
				mefa.fileSave(ta);
				
				System.out.println( ae.getActionCommand() );				
			}
		}
	}
	class MyEditFileAccess{
		public void fileOpen(JTextArea ta){
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
			fc.setFileFilter(filter);
			fc.showOpenDialog(null);
			File f = fc.getSelectedFile();
			try{
				BufferedReader br = new BufferedReader(new FileReader(f));
				String s;
				while ((s = br.readLine()) != null){
					ta.append(s + '\n');
					br.close();
				}
				
			}catch(IOException e){
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
				for(int i=0;i<limit;i++){
					pw.println(st[i]);
				}
				pw.close();
			}catch(IOException e){
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
				for(int i=0;i<limit;i++){
					pw.println(st[i]);
				}
				pw.close();
			}catch(IOException e){
				return;
			}
		}
	}
}
class EditorStatus{
	static String FILENAME = "";
}
