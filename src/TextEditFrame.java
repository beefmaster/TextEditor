import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

public class TextEditFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JMenuItem saveitem;
	private InfoDialog dialog;
	private JTextArea txtarea;
	private JPanel txtpanel,countpanel;
	private JFileChooser chooser,savechooser;
	private File opened;
	private String copied;
	private JPopupMenu popup;
	private JLabel count;
	private JButton bsave;
	
	public TextEditFrame()
	{
		setTitle("TextEdit");
		setSize(500,300);
		centerFrame();
		/*
		 * Menu
		 */
		JMenuBar menu = new JMenuBar();
		setJMenuBar(menu);
		JMenu filemenu = new JMenu("File");
		menu.add(filemenu);
		
		JMenuItem openitem = new JMenuItem("Apri",new ImageIcon("images/open.png"));
		chooser = new JFileChooser();
		chooser.setFileFilter(new TxtFilter());
		openitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		openitem.addActionListener( new FileOpenListener() );
		filemenu.add(openitem);
		
		saveitem = new JMenuItem("Salva",new ImageIcon("images/save.png"));
		saveitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		saveitem.addActionListener(new FileSaveListener());
		filemenu.add(saveitem);
		saveitem.setEnabled(false);
		
		JMenuItem saveasitem = new JMenuItem("Salva come...",new ImageIcon("images/saveas.png"));
		savechooser = new JFileChooser();
		savechooser.setFileFilter(new TxtFilter());
		saveasitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		saveasitem.addActionListener(new FileSaveAsListener());
		filemenu.add(saveasitem);
		
		filemenu.addSeparator();
		
		JMenuItem exititem = new JMenuItem("Esci",new ImageIcon("images/exit.png"));
		exititem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		exititem.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		} );
		filemenu.add(exititem);
		
		JMenu modmenu = new JMenu("Modifica");
		menu.add(modmenu);
		
		JMenuItem selectitem = new JMenuItem("Seleziona tutto");
		selectitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		selectitem.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e)
			{
				txtarea.selectAll();
			}
			
		});
		modmenu.add(selectitem);
		
		modmenu.addSeparator();
		
		JMenuItem cutitem = new JMenuItem("Taglia",new ImageIcon("images/cut.png"));
		cutitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		cutitem.addActionListener(new CutListener());
		modmenu.add(cutitem);
		
		JMenuItem copyitem = new JMenuItem("Copia",new ImageIcon("images/copy.png"));
		copyitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		copyitem.addActionListener(new CopyListener());
		modmenu.add(copyitem);
		
		JMenuItem pasteitem = new JMenuItem("Incolla",new ImageIcon("images/paste.png"));
		pasteitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		pasteitem.addActionListener(new PasteListener());
		modmenu.add(pasteitem);
		
		JMenu infmenu = new JMenu("?");
		menu.add(infmenu);
		
		JMenuItem infitem = new JMenuItem("Info",new ImageIcon("images/info.png"));
		infitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		infitem.addActionListener(new InfoListener());
		infmenu.add(infitem);
		
		popup = new JPopupMenu();
		
		JMenuItem taglia = new JMenuItem("Taglia",new ImageIcon("images/cut.png"));
		taglia.addActionListener(new CutListener());
		popup.add(taglia);
		
		JMenuItem copia = new JMenuItem("Copia",new ImageIcon("images/copy.png"));
		copia.addActionListener(new CopyListener());
		popup.add(copia);
		
		JMenuItem incolla = new JMenuItem("Incolla",new ImageIcon("images/paste.png"));
		incolla.addActionListener(new PasteListener());
		popup.add(incolla);
		
		MouseListener popupListener = new PopupListener();
		
		JToolBar tools = new JToolBar("Strumenti");
		
		JButton bopenf = new JButton(new ImageIcon("images/open.png"));
		bopenf.addActionListener(new FileOpenListener());
		bopenf.setToolTipText("Apri File");
		tools.add(bopenf);
		
		bsave = new JButton(new ImageIcon("images/save.png"));
		bsave.addActionListener(new FileSaveListener());
		bsave.setToolTipText("Salva");
		bsave.setEnabled(false);
		tools.add(bsave);
		
		JButton bsaveas = new JButton(new ImageIcon("images/saveas.png"));
		bsaveas.addActionListener(new FileSaveAsListener());
		bsaveas.setToolTipText("Salva come...");
		tools.add(bsaveas);
		
		tools.addSeparator();
		
		JButton bcut = new JButton(new ImageIcon("images/cut.png"));
		bcut.addActionListener(new CutListener());
		bcut.setToolTipText("Taglia");
		tools.add(bcut);
		
		JButton bcopy = new JButton(new ImageIcon("images/copy.png"));
		bcopy.addActionListener(new CopyListener());
		bcopy.setToolTipText("Copia");
		tools.add(bcopy);
		
		JButton bpaste = new JButton(new ImageIcon("images/paste.png"));
		bpaste.addActionListener(new PasteListener());
		bpaste.setToolTipText("Incolla");
		tools.add(bpaste);
		
		tools.addSeparator();
		
		JButton binfo = new JButton(new ImageIcon("images/info.png"));
		binfo.addActionListener( new InfoListener() );
		binfo.setToolTipText("Info");
		tools.add(binfo);
		
		JButton bexit = new JButton(new ImageIcon("images/exit.png"));
		bexit.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}  );
		bexit.setToolTipText("Exit");
		tools.add(bexit);
		
		add(tools, BorderLayout.NORTH);
		
		
		/*
		 * Pannello JTextArea
		 */
		txtpanel = new JPanel();
		txtpanel.setLayout(new BorderLayout());
		txtarea = new JTextArea();
		Dimension fd = this.getSize();
		txtarea.setBounds(new Rectangle(fd));
		txtarea.setBounds(new Rectangle(fd));
		txtarea.setLineWrap(true);
		JScrollPane scrolltxt = new JScrollPane(txtarea);
		txtpanel.add(scrolltxt,BorderLayout.CENTER);
		
		txtarea.addMouseListener(popupListener);
		
		add(txtpanel);
		
		countpanel = new JPanel();
		count = new JLabel();
		Count c  = new Count();
		Thread t = new Thread(c);
		t.start();
		countpanel.add(count);
		add(countpanel, BorderLayout.SOUTH);
		
		
		
	}
	
	private class Count implements Runnable
	{
		
		public void run()
		{
			while(true)
			{
				if(txtarea.getText() == null) count.setText("0");
				String s = txtarea.getText();
				StringTokenizer st = new StringTokenizer(s," "+"\n");
				count.setText( "Parole: " + st.countTokens() + " Caratteri: " + s.length() );
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	private class PopupListener extends MouseAdapter 
	{
	    public void mousePressed(MouseEvent e)
	    {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e)
	    {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e)
	    {
	        if (e.isPopupTrigger())
	        {
	            popup.show(e.getComponent(), e.getX(), e.getY());
	        }
	    }
	}
	
	private class InfoListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			if (dialog == null) dialog = new InfoDialog(TextEditFrame.this);
			dialog.setVisible(true);
						
		}
		
	}
	
	private class InfoDialog extends JDialog
	{
		private static final long serialVersionUID = 1L;
		
		public InfoDialog(JFrame fr)
		{
			super( fr, "Info", true );
			
			add(new JLabel("<html><h1><i>STK Inc.</html></h1></i>"), BorderLayout.CENTER );
			JButton ok = new JButton("Ok");
			ok.addActionListener( new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setVisible(false);
				}
			});
			
			JPanel infop = new JPanel();
			infop.add(ok);
			add(infop,BorderLayout.SOUTH);
			setSize(200,100);
		}
		
		
	}
	
	public void centerFrame()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		Dimension frameSize = getSize();
		setLocation ((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	private class CutListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			
			copied = txtarea.getSelectedText();
			txtarea.replaceSelection("");
			
		}
		
	}
	
	private class PasteListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			
			txtarea.insert(copied, txtarea.getCaretPosition() );
			
		}
		
	}
	
	private class CopyListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e)
		{
			
			copied = txtarea.getSelectedText();
			
		}
		
	}
	
	private class FileSaveAsListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent e) 
		{
			
			int ris = savechooser.showSaveDialog(TextEditFrame.this);
			if(ris == JFileChooser.APPROVE_OPTION)
			{
				saveitem.setEnabled(true);
				bsave.setEnabled(true);
				setTitle("TextEdit - " + savechooser.getSelectedFile());
				FileWriter filew = null;
				try {
					filew = new FileWriter(savechooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				PrintWriter scrivi = new PrintWriter(filew);
				scrivi.print(txtarea.getText());
				scrivi.close();
				
			}
			
		}
		
		
		
	}
	
	private class FileSaveListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			
			FileWriter filew = null;
			try {
				filew = new FileWriter(opened);
			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			PrintWriter scrivi = new PrintWriter(filew);
			
			String ris = txtarea.getText();
			
			scrivi.print(ris);
			
			scrivi.close();
			
		}
		
	}
	
	private class FileOpenListener implements ActionListener
	{
		FileReader f;
		public void actionPerformed(ActionEvent e)
		{
			
			int ris = chooser.showOpenDialog(TextEditFrame.this);
			
			if(ris == JFileChooser.APPROVE_OPTION)
			{
				saveitem.setEnabled(true);
				bsave.setEnabled(true);
				opened = chooser.getSelectedFile();
				setTitle("TextEdit - " + opened);
				try {
					f = new FileReader(chooser.getSelectedFile());
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
				BufferedReader br = new BufferedReader(f);
				
				try {
					String s = br.readLine();
					while( s != null )
					{
						if( txtarea.getText() == null ) txtarea.setText( s + "\n" );
						txtarea.append( s + "\n");
						s = br.readLine();
						
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
		
	}
	
	private class TxtFilter extends FileFilter
	{
		
		public boolean accept(File f) 
		{
			return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
		}

		public String getDescription()
		{
			return "Txt File";
		}
			
	}
	
	public static int countWord(String in)
	{
		StringTokenizer st = new StringTokenizer(in," ");
		
		return st.countTokens();
		
	}
	
}
