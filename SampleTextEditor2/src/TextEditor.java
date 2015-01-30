import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.filechooser.*;

class TextEditor extends JFrame implements ActionListener {

	// プライベート変数
	private JToolBar toolBar;
	private JLabel statusBar;
	private JTextArea textArea;
	private JFileChooser fileChooser = new JFileChooser();

	//////////////////////////////////////////////////////////
	// 初期化
	//////////////////////////////////////////////////////////

	// コンストラクタ
	TextEditor() {

		// Look&FeelをWindowsモードにする
		OnLookAndFeel("Windows");

		// タイトルバーを設定する
		setTitle("新規文書");

		// メニューバーを作成する
		InitMenuBar();

		// ツールバーを作成する
		InitToolBar();

		// ステータスバーを作成する
		InitStatusBar();

		// テキストエリアを作成する
		InitTextArea(400, 500);

		// ウィンドウを閉じたときにアプリケーションを終了する
		setDefaultCloseOperation(this.EXIT_ON_CLOSE);

		// ウィンドウを適切な大きさにする
		pack();

		// テキストエリアにフォーカスを当てる
		textArea.grabFocus();

		// ウィンドウを可視化する
		setVisible(true);
	}

	// メニューバーを作成する
	void InitMenuBar() {

		// メニューバー
		JMenuBar menuBar = new JMenuBar();
		getRootPane().setJMenuBar(menuBar);

		// [ファイル]
		JMenu menuFile = new JMenu("ファイル(F)");
		menuFile.setMnemonic('F');
		menuBar.add(menuFile);

		// [ファイル]-[新規作成]
		JMenuItem menuNew = new JMenuItem("新規作成(N)");
		menuNew.setMnemonic('N');
		menuNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		menuNew.setActionCommand("New");
		menuNew.addActionListener(this);
		menuFile.add(menuNew);

		// [ファイル]-[開く]
		JMenuItem menuOpen = new JMenuItem("開く(O)...");
		menuOpen.setMnemonic('O');
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		menuOpen.setActionCommand("Open");
		menuOpen.addActionListener(this);
		menuFile.add(menuOpen);

		// [ファイル]-[上書き保存]
		JMenuItem menuSave = new JMenuItem("上書き保存(S)");
		menuSave.setMnemonic('S');
		menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		menuSave.setActionCommand("Save");
		menuSave.addActionListener(this);
		menuFile.add(menuSave);

		// [ファイル]-[名前をつけて保存]
		JMenuItem menuSaveAs = new JMenuItem("名前を付けて保存(A)...");
		menuSaveAs.setMnemonic('A');
		menuSaveAs.setActionCommand("SaveAs");
		menuSaveAs.addActionListener(this);
		menuFile.add(menuSaveAs);

		// [ファイル]-[印刷]
		JMenuItem menuPrint = new JMenuItem("印刷(P)...");
		menuPrint.setMnemonic('P');
		menuPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		menuPrint.setActionCommand("Print");
		menuPrint.addActionListener(this);
		menuFile.add(menuPrint);

		// [ファイル]-[終了]
		JMenuItem menuExit = new JMenuItem("終了(X)");
		menuExit.setMnemonic('X');
		menuExit.setActionCommand("Exit");
		menuExit.addActionListener(this);
		menuFile.add(menuExit);

		// [編集]
		JMenu menuEdit = new JMenu("編集(E)");
		menuEdit.setMnemonic('E');
		menuBar.add(menuEdit);

		// [編集]-[切り取り]
		JMenuItem menuCut = new JMenuItem("切り取り(T)");
		menuCut.setMnemonic('T');
		menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		menuCut.setActionCommand("Cut");
		menuCut.addActionListener(this);
		menuEdit.add(menuCut);

		// [編集]-[コピー]
		JMenuItem menuCopy = new JMenuItem("コピー(C)");
		menuCopy.setMnemonic('C');
		menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
		menuCopy.setActionCommand("Copy");
		menuCopy.addActionListener(this);
		menuEdit.add(menuCopy);

		// [編集]-[貼り付け]
		JMenuItem menuPaste = new JMenuItem("貼り付け(P)");
		menuPaste.setMnemonic('P');
		menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK));
		menuPaste.setActionCommand("Paste");
		menuPaste.addActionListener(this);
		menuEdit.add(menuPaste);

		// [表示]
		JMenu menuView = new JMenu("表示(V)");
		menuView.setMnemonic('V');
		menuBar.add(menuView);

		// [表示]-[ツールバー]
		JCheckBoxMenuItem menuToolBar = new JCheckBoxMenuItem("ツールバー(T)", true);
		menuToolBar.setMnemonic('T');
		menuToolBar.setActionCommand("ToolBar");
		menuToolBar.addActionListener(this);
		menuView.add(menuToolBar);

		// [表示]-[ステータスバー]
		JCheckBoxMenuItem menuStatusBar = new JCheckBoxMenuItem("ステータスバー(S)", true);
		menuStatusBar.setMnemonic('S');
		menuStatusBar.setActionCommand("StatusBar");
		menuStatusBar.addActionListener(this);
		menuView.add(menuStatusBar);

		// [表示]-[見栄え]
		JMenu menuLookFeel = new JMenu("見栄え(L)");
		menuLookFeel.setMnemonic('L');
		menuView.add(menuLookFeel);

		// [表示]-[見栄え]-[Metal]
		JMenuItem menuMetal = new JMenuItem("Metal(M)");
		menuMetal.setMnemonic('M');
		menuMetal.setActionCommand("Metal");
		menuMetal.addActionListener(this);
		menuLookFeel.add(menuMetal);

		// [表示]-[見栄え]-[Windows]
		JMenuItem menuWindows = new JMenuItem("Windows(W)");
		menuWindows.setMnemonic('W');
		menuWindows.setActionCommand("Windows");
		menuWindows.addActionListener(this);
		menuLookFeel.add(menuWindows);

		// [表示]-[見栄え]-[Motif]
		JMenuItem menuMotif = new JMenuItem("Motif(T)");
		menuMotif.setMnemonic('T');
		menuMotif.setActionCommand("Motif");
		menuMotif.addActionListener(this);
		menuLookFeel.add(menuMotif);

		// [ヘルプ]
		JMenu menuHelp = new JMenu("ヘルプ(H)");
		menuHelp.setMnemonic('H');
		menuBar.add(menuHelp);

		// [ヘルプ]-[バージョン情報]
		JMenuItem menuAboud = new JMenuItem("バージョン情報(A)...");
		menuAboud.setMnemonic('A');
		menuAboud.setActionCommand("About");
		menuAboud.addActionListener(this);
		menuHelp.add(menuAboud);
	}

	// ツールバーを作成する
	void InitToolBar() {
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
//		toolBar.add(new JButton(new ImageIcon("new.gif")));

		JButton btnNew = new JButton("新");
		JButton btnOpen = new JButton("開");
		JButton btnSave = new JButton("保");
		JButton btnAbout = new JButton("？");

		btnNew.setActionCommand("New");
		btnOpen.setActionCommand("Open");
		btnSave.setActionCommand("Save");
		btnAbout.setActionCommand("About");

		btnNew.addActionListener(this);
		btnOpen.addActionListener(this);
		btnSave.addActionListener(this);
		btnAbout.addActionListener(this);

		toolBar.add(btnNew);
		toolBar.add(btnOpen);
		toolBar.add(btnSave);
		toolBar.addSeparator();
		toolBar.add(btnAbout);

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	// ステータスバーを作成する
	void InitStatusBar() {
		statusBar = new JLabel("　");
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}

	// テキストエリアを作成する
	void InitTextArea(int width, int height) {
		textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
 		textArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(width, height));
		getContentPane().add(scroll, BorderLayout.CENTER);
	}

	//////////////////////////////////////////////////////////
	// イベント処理
	//////////////////////////////////////////////////////////

	// イベント処理
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if (cmd.equals("New")) {
			OnNew();
		} else if (cmd.equals("Open")) {
			OnOpen();
		} else if (cmd.equals("Save")) {
			OnSave();
		} else if (cmd.equals("SaveAs")) {
			OnSaveAs();
		} else if (cmd.equals("Print")) {
			// 未実装
		} else if (cmd.equals("Exit")) {
			OnExit();
		} else if (cmd.equals("Cut")) {
			OnCut();
		} else if (cmd.equals("Copy")) {
			OnCopy();
		} else if (cmd.equals("Paste")) {
			OnPaste();
		} else if (cmd.equals("ToolBar")) {
			OnToolBar(((JCheckBoxMenuItem)ae.getSource()).getState());
		} else if (cmd.equals("StatusBar")) {
			OnStatusBar(((JCheckBoxMenuItem)ae.getSource()).getState());
		} else if (cmd.equals("Metal")) {
			OnLookAndFeel(cmd);
		} else if (cmd.equals("Windows")) {
			OnLookAndFeel(cmd);
		} else if (cmd.equals("Motif")) {
			OnLookAndFeel(cmd);
		} else if (cmd.equals("About")) {
			OnAbout();
		}
	}

	// [ファイル]-[新規作成]
	void OnNew() {
		fileChooser.setSelectedFile(null);
		setTitle("新規文書");
		textArea.setText("");
	}

	// [ファイル]-[開く]
	void OnOpen() {
		int returnVal = fileChooser.showOpenDialog(this);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				FileReader fr = new FileReader(file);
				textArea.read(fr, null);
				setTitle(file.getAbsolutePath());
				fr.close();
			}
		} catch(Exception ex) {}
	}

	// [ファイル]-[上書き保存]
	void OnSave() {
		if (fileChooser.getSelectedFile() == null) {
			OnSaveAs();
		} else {
			try {
				FileWriter fw = new FileWriter(fileChooser.getSelectedFile());
				fw.write(textArea.getText());
				fw.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// [ファイル]-[名前を付けて保存]
	void OnSaveAs() {
		int returnVal = fileChooser.showSaveDialog(this);
		try {
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				FileWriter fw = new FileWriter(fileChooser.getSelectedFile());
				fw.write(textArea.getText());
				fw.close();
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	// [ファイル]-[終了]
	void OnExit() {
		System.exit(0);
	}

	// [編集]-[切り取り]
	void OnCut() {
		OnCopy();
		textArea.replaceRange(null, textArea.getSelectionStart(), textArea.getSelectionEnd());
	}

	// [編集]-[コピー]
	void OnCopy() {
		Clipboard cb = getToolkit().getSystemClipboard();
		StringSelection strSel = new StringSelection(textArea.getSelectedText());
		cb.setContents(strSel, strSel);
	}

	// [編集]-[貼り付け]
	void OnPaste() {
		Clipboard cb = getToolkit().getSystemClipboard();
		Transferable data = cb.getContents(this);
		if ((data != null) && data.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			try {
				String str = (String)data.getTransferData(DataFlavor.stringFlavor);
				textArea.replaceRange(str, textArea.getSelectionStart(), textArea.getSelectionEnd());
			} catch (Exception e) { }
		}
	}

	// [表示]-[ツールバー]
	void OnToolBar(boolean b) {
		toolBar.setVisible(b);
	}

	// [表示]-[ステータスバー]
	void OnStatusBar(boolean b) {
		statusBar.setVisible(b);
	}

	// [表示]-[ルック＆フィール]
	void OnLookAndFeel(String type) {
		String lookAndFeel = "";
		if (type.equals("Metal")) {
			lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
		} else if (type.equals("Windows")) {
			lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		} else if (type.equals("Motif")) {
			lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		}
		try {
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception ex) { }
	}

	// [ヘルプ]-[バージョン情報]
	void OnAbout() {
		JOptionPane.showMessageDialog(this, "TextEditor Ver0.1", "TextEditor", JOptionPane.INFORMATION_MESSAGE);
	}

	//////////////////////////////////////////////////////////
	// メインルーチン
	//////////////////////////////////////////////////////////

	// メインルーチン
	public static void main(String[] args) {
		new TextEditor();
	}
}

