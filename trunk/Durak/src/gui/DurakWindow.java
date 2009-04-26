package gui;

import game.Durak;
import game.Table;
import gui.listeners.DurakActionListener;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class DurakWindow extends JFrame
{
	private WelcomePanel welcomePanel;
	private DurakPanel durakPanel;
	private DurakActionListener actionListener;

	public DurakWindow()
	{
		super("Durak - 0.01a");
		this.setMinimumSize(new Dimension(DurakPanel.WIDTH, DurakPanel.HEIGHT));
		this.setPreferredSize(new Dimension(DurakPanel.WIDTH, DurakPanel.HEIGHT));

		this.setIconImage(new ImageIcon(Object.class.getResource("/images/icons/durak.png")).getImage());

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setLookAndFeel();

		this.actionListener = new DurakActionListener(new Durak(), this);
		this.welcomePanel = new WelcomePanel(this.actionListener);
		this.durakPanel = new DurakPanel(this.actionListener);

		this.setPanel(this.welcomePanel);

		this.setJMenuBar(new Menu(this.actionListener));

		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

		this.pack();

		//		super.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (super.getSize().width / 2), (Toolkit.getDefaultToolkit().getScreenSize().height / 2) - (super.getSize().height / 2));
		this.setVisible(true);
	}

	public WelcomePanel getWelcomePanel()
	{
		return this.welcomePanel;
	}

	public DurakPanel getDurakPanel()
	{
		return this.durakPanel;
	}

	public void showDurakPanel()
	{
		this.setPanel(this.durakPanel);
	}

	public void showWelcomePanel()
	{
		this.setPanel(this.welcomePanel);
	}

	public void newGame(Table table)
	{
		this.durakPanel.newGame(table);
	}

	/**
	 * The panel in the parameter becomes the new contentpane
	 * 
	 * @param panel
	 *            the panel thats ought to be shown
	 */
	private void setPanel(JPanel panel)
	{
		this.remove(this.getContentPane());
		this.setContentPane(panel);
		this.getContentPane().setVisible(true);
	}

	/**
	 * Set the OS-specific Look & Feel
	 * 
	 */
	private void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e1)
		{
			// nothing
		}
	}

	private class Menu extends JMenuBar
	{
		public Menu(DurakActionListener actionListener)
		{
			JMenu menuGame = new JMenu("Game");
			menuGame.setMnemonic('G');

			JMenuItem newGame = new JMenuItem("New", new ImageIcon(Object.class.getResource("/images/icons/durak.png")));
			newGame.setMnemonic('N');
			newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
			newGame.addActionListener(actionListener);
			newGame.setActionCommand("" + DurakActionListener.ACTION_SHOW_SETTINGS);

			JMenuItem quitGame = new JMenuItem("Exit", new ImageIcon(Object.class.getResource("/images/icons/bullet_cross.png")));
			quitGame.setMnemonic('X');
			quitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
			quitGame.setActionCommand("" + DurakActionListener.ACTION_QUIT);
			quitGame.addActionListener(actionListener);

			menuGame.add(newGame);
			menuGame.addSeparator();
			menuGame.add(quitGame);

			this.add(menuGame);
		}
	}
}
