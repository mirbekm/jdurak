package gui.game;

import gui.helpers.Colors;
import gui.listeners.DurakActionListener;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TurnPanel extends JPanel
{
	public static final int WIDTH = 225;
	public static final int HEIGHT = 185;

	private JButton btnSwitchPlayer = new JButton("switch player");
	private JButton btnEndTurn = new JButton("end turn");

	public TurnPanel(DurakActionListener actionListener)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		this.setBackground(Colors.DARK_GREEN);

		this.setLayout(new GridBagLayout());

		int activeRow = 0;

		this.btnSwitchPlayer.addActionListener(actionListener);
		this.btnSwitchPlayer.setActionCommand("" + DurakActionListener.ACTION_SWITCH_PLAYER);

		this.btnEndTurn.addActionListener(actionListener);
		this.btnEndTurn.setActionCommand("" + DurakActionListener.ACTION_END_TURN);

		this.add(this.btnSwitchPlayer, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.btnEndTurn, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		this.setVisible(true);
	}
}