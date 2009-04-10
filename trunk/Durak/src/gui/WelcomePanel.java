package gui;

import gui.helpers.Colors;
import gui.listeners.DurakActionListener;

import java.awt.Checkbox;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class WelcomePanel extends JPanel
{
	private JTextField txtPlayerOne;
	private JComboBox cmbComputerOne, cmbComputerTwo, cmbComputerThree, cmbComputerFour, cmbComputerFive;
	private JSlider sldNumberOfCards;
	private JCheckBox chkTransfer;
	
	public WelcomePanel(DurakActionListener actionListener)
	{
		this.setMinimumSize(new Dimension(DurakPanel.WIDTH, DurakPanel.HEIGHT));
		this.setPreferredSize(new Dimension(DurakPanel.WIDTH, DurakPanel.HEIGHT));
		
		this.setLayout(new GridBagLayout());
		
//		this.setBackground(Colors.NORMAL_GREEN);

		JLabel lblTitle = new JLabel("JDurak");
		lblTitle.setFont(new Font("Verdana", Font.BOLD, 66));
		JLabel lblSubTitle = new JLabel("- don't be the last one standing -");
		lblSubTitle.setFont(new Font("Verdana", Font.BOLD, 30));

		JPanel panelPlayers = new JPanel(new GridBagLayout());
		
//		panelPlayers.setBackground(Colors.NORMAL_GREEN);
		
		TitledBorder panelPlayersBorder = new TitledBorder(new LineBorder(Colors.LIGHT_GREEN, 1), "Players: ");
		panelPlayersBorder.setTitleFont(new Font("Verdana",Font.BOLD, 14));
		panelPlayers.setBorder(panelPlayersBorder);
		
		
		
		this.txtPlayerOne = new JTextField("Player 1");
		this.cmbComputerOne = new JComboBox(new String[]{"Computer 1 - easy", "Computer 1 - normal", "Computer 1 - hard", "no player"});
		this.cmbComputerOne.setEnabled(false);
		this.cmbComputerTwo = new JComboBox(new String[]{"Computer 2 - easy", "Computer 2 - normal", "Computer 2 - hard", "no player"});
		this.cmbComputerTwo.setSelectedIndex(3);
		this.cmbComputerTwo.setEnabled(false);
		this.cmbComputerThree = new JComboBox(new String[]{"Computer 3 - easy", "Computer 3 - normal", "Computer 3 - hard", "no player"});
		this.cmbComputerThree.setSelectedIndex(3);
		this.cmbComputerThree.setEnabled(false);
		this.cmbComputerFour = new JComboBox(new String[]{"Computer 4 - easy", "Computer 4 - normal", "Computer 4 - hard", "no player"});
		this.cmbComputerFour.setSelectedIndex(3);
		this.cmbComputerFour.setEnabled(false);
		this.cmbComputerFive = new JComboBox(new String[]{"Computer 5 - easy", "Computer 5 - normal", "Computer 5 - hard", "no player"});
		this.cmbComputerFive.setSelectedIndex(3);
		this.cmbComputerFive.setEnabled(false);		
		
		int rowPanelPlayers = 0;
		
		panelPlayers.add(this.txtPlayerOne, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));
		panelPlayers.add(this.cmbComputerOne, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));
		panelPlayers.add(this.cmbComputerTwo, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));
		panelPlayers.add(this.cmbComputerThree, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));
		panelPlayers.add(this.cmbComputerFour, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));
		panelPlayers.add(this.cmbComputerFive, new GridBagConstraints(0, rowPanelPlayers++, 1,1, 1.0,0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3,3,3,3), 0,0));

		JPanel panelRules = new JPanel(new GridBagLayout());
		
//		panelRules.setBackground(Colors.NORMAL_GREEN);
		
		TitledBorder panelRulesBorder = new TitledBorder(new LineBorder(Colors.LIGHT_GREEN, 1), "Rules: ");
		panelRulesBorder.setTitleFont(new Font("Verdana",Font.BOLD, 14));
		panelRules.setBorder(panelRulesBorder);
		
		JLabel lblNumberOfCards = new JLabel("Cards: ");
		
		this.sldNumberOfCards = new JSlider(36, 52, 36);
		this.sldNumberOfCards.setSnapToTicks(true);
		this.sldNumberOfCards.setPaintTicks(true);
		this.sldNumberOfCards.setMinorTickSpacing(4);

		this.chkTransfer = new JCheckBox("transfer");
		this.chkTransfer.setEnabled(false);
		
		panelRules.add(lblNumberOfCards, new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0));
		panelRules.add(this.sldNumberOfCards, new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));
		panelRules.add(this.chkTransfer, new GridBagConstraints(0,1,2,1,1.0,0.0,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0,0));

		
		int rowBackgroundPanel = 0;

		JButton btnPlay = new JButton("play");
		btnPlay.setMinimumSize(new Dimension(90,35));
		btnPlay.setPreferredSize(new Dimension(90,35));
		btnPlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		btnPlay.addActionListener(actionListener);
		btnPlay.setActionCommand("" + DurakActionListener.ACTION_NEW_GAME);


		this.add(lblTitle, new GridBagConstraints(0, rowBackgroundPanel++, 2,1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0), 4,4));
		this.add(lblSubTitle, new GridBagConstraints(0, rowBackgroundPanel++, 2,1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,50,0), 0,0));
		this.add(panelPlayers, new GridBagConstraints(0, rowBackgroundPanel, 1,1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 0,0));
		this.add(panelRules, new GridBagConstraints(1, rowBackgroundPanel++, 1,1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(10,10,10,10), 0,0));
		this.add(btnPlay, new GridBagConstraints(0, rowBackgroundPanel++, 2,1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10,10,10,10), 0,0));

		
		this.setVisible(true);
	}
	
	public String getPlayerName()
	{
		if(!this.txtPlayerOne.getText().isEmpty())
			return this.txtPlayerOne.getText();
		return "Player 1";
	}
	
	public int getNumberOfCards()
	{
		return this.sldNumberOfCards.getValue();
	}
	
}
