package gui;

import game.Table;
import gui.game.DeckPanel;
import gui.game.HandPanel;
import gui.game.TablePanel;
import gui.game.TurnPanel;
import gui.helpers.Colors;
import gui.listeners.DurakActionListener;
import gui.listeners.DurakMouseListener;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class DurakPanel extends JPanel
{
	public static final int WIDTH = 900;
	public static final int HEIGHT = 650;

	private DeckPanel deckPanel;
	private HandPanel handPanel;
	private TablePanel tablePanel;
	private TurnPanel turnPanel;
	private DurakMouseListener durakMouseListener;

	public DurakPanel(DurakActionListener actionListener)
	{
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		this.setLayout(new GridBagLayout());

		this.durakMouseListener = new DurakMouseListener(actionListener);

		this.deckPanel = new DeckPanel();
		this.handPanel = new HandPanel(this.durakMouseListener);
		this.tablePanel = new TablePanel(actionListener);
		this.turnPanel = new TurnPanel(actionListener);

		this.add(this.tablePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.add(this.handPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
		this.add(this.deckPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.NORTHEAST, GridBagConstraints.VERTICAL, new Insets(1, 1, 1, 1), 0, 0));
		this.add(this.turnPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 0, 0));

		this.setBackground(Colors.DARK_GREEN);

		this.setVisible(true);
	}

	public void newGame(Table table)
	{
		// TODO table.getPlayers().get(0) <- in multiplayer games this should be the correct player

		this.handPanel.updateDisplay(table.getPlayers().get(0));
		this.deckPanel.updateDisplay(table.getDeck(), table.getPlayers(), table.getWinners());
		this.tablePanel.newGame(table.getPlayers(), table.getPlayers().get(0), table.getAttackers());
		this.turnPanel.newGame();
	}

	public HandPanel getHandPanel()
	{
		return this.handPanel;
	}

	public TablePanel getTablePanel()
	{
		return this.tablePanel;
	}

	public DeckPanel getDeckPanel()
	{
		return this.deckPanel;
	}

	public TurnPanel getTurnPanel()
	{
		return this.turnPanel;
	}
}
