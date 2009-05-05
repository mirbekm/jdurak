package gui.game;

import gui.helpers.CardManager;
import gui.helpers.Colors;
import gui.listeners.DurakActionListener;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

public class TurnPanel extends JPanel
{
	public static final int WIDTH = 250;
	public static final int HEIGHT = 200;

	private JButton btnNextMove = new JButton("compute next move");

	private JButton btnEndTurn = new JButton("end turn");

	private JCheckBox chkAutoReply = new JCheckBox("<html><b>auto reply</b></html>");
	private JRadioButton radSortBySuit = new JRadioButton("suit", true);
	private JRadioButton radSortByNumber = new JRadioButton("numbers");
	private ButtonGroup grpSortBy = new ButtonGroup();

	public TurnPanel(DurakActionListener actionListener)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));

		this.setBackground(Colors.DARK_GREEN);

		this.setLayout(new GridBagLayout());

		int activeRow = 0;

		this.btnNextMove.addActionListener(actionListener);
		this.btnNextMove.setActionCommand("" + DurakActionListener.ACTION_NEXT_MOVE);
		this.btnNextMove.setIcon(CardManager.getImageIcon("images/icons/hourglass.png"));

		this.btnEndTurn.addActionListener(actionListener);
		this.btnEndTurn.setActionCommand("" + DurakActionListener.ACTION_END_TURN);
		this.btnEndTurn.setIcon(CardManager.getImageIcon("images/icons/flag_green.png"));

		this.chkAutoReply.addActionListener(actionListener);
		this.chkAutoReply.setActionCommand("" + DurakActionListener.ACTION_UPDATE_DISPLAY);
		this.chkAutoReply.setRolloverEnabled(false);
		this.chkAutoReply.setContentAreaFilled(false);

		this.grpSortBy.add(this.radSortBySuit);
		this.grpSortBy.add(this.radSortByNumber);
		this.radSortBySuit.addActionListener(actionListener);
		this.radSortByNumber.addActionListener(actionListener);
		this.radSortByNumber.setActionCommand("" + DurakActionListener.ACTION_UPDATE_HAND_PANEL);
		this.radSortBySuit.setActionCommand("" + DurakActionListener.ACTION_UPDATE_HAND_PANEL);
		this.radSortBySuit.setOpaque(false);
		this.radSortByNumber.setOpaque(false);

		JPanel pnlSortBy = new JPanel(new GridBagLayout());
		pnlSortBy.setBackground(Colors.LIGHT_GREEN);

		pnlSortBy.setBorder(BorderFactory.createTitledBorder(new LineBorder(Colors.NORMAL_GREEN, 1), "sort cards by: "));
		pnlSortBy.add(this.radSortBySuit, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 2), 2, 2));
		pnlSortBy.add(this.radSortByNumber, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 2, 2));

		this.add(this.chkAutoReply, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(pnlSortBy, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		this.add(this.btnNextMove, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		this.add(this.btnEndTurn, new GridBagConstraints(0, activeRow++, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		this.setVisible(true);
	}

	public void newGame()
	{
		this.chkAutoReply.setSelected(true);
		this.btnEndTurn.setVisible(false);
		this.btnNextMove.setVisible(false);
		this.btnEndTurn.setText("end turn");
		this.btnNextMove.setText("compute next move");
	}

	public JButton getBtnEndTurn()
	{
		return this.btnEndTurn;
	}

	public JButton getBtnNextMove()
	{
		return this.btnNextMove;
	}

	public boolean autoReply()
	{
		return this.chkAutoReply.isSelected();
	}

	public boolean sortBySuit()
	{
		return this.radSortBySuit.isSelected();
	}
}
