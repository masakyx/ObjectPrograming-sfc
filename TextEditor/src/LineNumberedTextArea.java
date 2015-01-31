

import  java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import  javax.swing.*;
import  javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import  javax.swing.table.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;

public class LineNumberedTextArea extends JTextArea{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JScrollPane pane;
	JTextArea textarea;
	JTable rowheader;
	
	final int initialRowCount = 1;

	public LineNumberedTextArea( String fontname, int initialheight ) {
		textarea = new JTextArea( initialRowCount, 40 );
		textarea.addCaretListener( new TextAreaCaretListener() );
		
		rowheader = new JTable( initialRowCount, 1 );
		TableColumnModel tcm = rowheader.getColumnModel();
		TableColumn tc = tcm.getColumn( 0 );
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(SwingConstants.CENTER);
		tc.setCellRenderer(r);
		tc.setHeaderValue( "" );
		rowheader.setColumnModel( tcm );
		for ( int n=1; n <=rowheader.getRowCount(); n++ ) {
			rowheader.setValueAt( new Integer(n), n-1, 0);
		}
		setRowHeight(  fontname, initialheight );

		pane = new JScrollPane( textarea );
		pane.setRowHeaderView(rowheader);
		pane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowheader.getTableHeader());
		Dimension size = new Dimension(rowheader.getPreferredSize().width/2, textarea.getPreferredSize().height);
		pane.getRowHeader().setPreferredSize( size ); 
	}
	
	void  setRowHeight( String fontname, int size ) {
		Font  f = new Font( fontname, Font.PLAIN, size );
		textarea.setFont( f );
		FontMetrics  fm = textarea.getFontMetrics( f );
		rowheader.setRowHeight( fm.getHeight() );
	}
	
	class TextAreaCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			int rows = textarea.getLineCount();
			int rrows = rowheader.getRowCount();
			//System.out.println( rows+":"+rrows );
			DefaultTableModel model = (DefaultTableModel)rowheader.getModel();
			if ( rows > rrows ) {
				// 足すときも、現在の行から
				// 行番号を付け替える
				int current = countRows( e.getDot() );
				int diff = rows - rrows;
				for ( int i=current; i < current+diff; i++ ) {
					model.insertRow( i, new Integer[] {new Integer(i+1)} );
				}
				rowheader.setModel( model );
				renumbering( current );
				rowheader.updateUI();
				pane.updateUI();
			} else if ( rows < rrows ){
				// e.getDot()から、現在の行を\nを数えることで判別
				// 現在の行から、余分な行数分だけ削除
				// 現在の行から、行番号を振り直す
				int current = countRows( e.getDot() );
				int diff = rrows - rows;
				//System.out.println( "cur:" + current + " diff:" + diff );
				for ( int i=current+diff-1; i >= current; i-- ) {
					model.removeRow( i );
				}			
				rowheader.setModel( model );
				renumbering( current );
				rowheader.updateUI();
				pane.updateUI();
			}
		}
	}
	
	int countRows( int position) {
		String  text = textarea.getText();
		String  lines [] = text.substring( 0, position ).split( "\n" );
		if ( lines == null ) { return 0; } else { return lines.length; }
	}
	
	void renumbering( int current ) {
		for ( int i=current; i < rowheader.getRowCount(); i++ ) {
			rowheader.setValueAt( new Integer(i+1), i, 0 );
		}
	}
	public void fileNew(){
		JFileChooser fc = new JFileChooser();
		fc.setSelectedFile(null);
		textarea.setText(null);
	}
	
	public void fileOpen(){
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt","text");
		fc.setFileFilter(filter);
		fc.showOpenDialog(null);
		File f = fc.getSelectedFile();
		try{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s;
			while ((s = br.readLine()) != null){
				textarea.append(s);
				textarea.append("\n");
				}
			br.close();
			/*FileReader fr = new FileReader(f);
			textarea.read(fr,null);*/
			//initialRowCount = textarea.getLineCount();
			//rowheader = new JTable( textarea.getLineCount(), 1 );
			
			System.out.println(textarea.getLineCount());
			//fr.close();
		}catch(IOException e){
			return;
		}
		
		EditorStatus.FILENAME = f.getPath();
		
	}
	
	public void fileSave(){
		JFileChooser fc = new JFileChooser();
		fc.showSaveDialog(null);
		File f = fc.getSelectedFile();
		
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(f,false));
			String s = textarea.getText();
			String[] st = s.split("\n");
			int limit = st.length;
			for(int i=0;i<limit;i++){
				pw.println(st[i]);
			}
			pw.close();
			/*FileWriter fw = new FileWriter(f);
			fw.write(textarea.getText());
			fw.close();*/
		}catch(IOException e){
			return;
		}
		EditorStatus.FILENAME = f.getPath();
	}
	
	public void overWrite(){
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
	// [編集]-[切り取り]
		void OnCut() {
			OnCopy();
			textarea.replaceRange(null, textarea.getSelectionStart(), textarea.getSelectionEnd());
		}

		// [編集]-[コピー]
		void OnCopy() {
			Clipboard cb = getToolkit().getSystemClipboard();
			StringSelection strSel = new StringSelection(textarea.getSelectedText());
			cb.setContents(strSel, strSel);
		}

		// [編集]-[貼り付け]
		void OnPaste() {
			Clipboard cb = getToolkit().getSystemClipboard();
			Transferable data = cb.getContents(this);
			if ((data != null) && data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					String str = (String)data.getTransferData(DataFlavor.stringFlavor);
					textarea.replaceRange(str, textarea.getSelectionStart(), textarea.getSelectionEnd());
				} catch (Exception e) { }
			}
		}
		String gettext(){
			String textt = textarea.getText();
			return textt;
		}
		void RePlace(String str1,String str2){
			String str = textarea.getText().replaceAll(str1, str2);
			fileNew();
			String[] st = str.split("\n");
			int limit = st.length;
			for(int i=0;i<limit;i++){
				textarea.append(st[i]+"\n");
			}
			
		}
		void Search(String str[]){
			Highlighter.HighlightPainter[] highlightPainter = {
					  new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW),
					  new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN)
					};
					 try{
					    Highlighter hilite = textarea.getHighlighter();
					    hilite.removeAllHighlights();
					    Document doc = textarea.getDocument();
					    String text = doc.getText(0, doc.getLength());
					    for (int i = 0; i < str.length; i++) {
					      int pos = 0;
					      while ((pos = text.indexOf(str[i], pos)) >= 0) {
					        hilite.addHighlight(pos, pos+str[i].length(), highlightPainter[i]);
					        pos += str[i].length();
					      }
					    }
					 }catch (BadLocationException e) {
						    e.printStackTrace();
					  }
		}
		
}

