package gui.game;

import game.AbstractPlayer;
import game.Card;
import game.Deck;
import gui.WelcomePanel;
import gui.helpers.CardManager;
import gui.helpers.Colors;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DeckPanel extends JPanel
{
	public static final int WIDTH = 225;
	public static final int HEIGHT = 465;

	private JLayeredPane stack;

	private JLabel cardsLeft;
	private JLabel trump;

	private JPanel playerInfo;

	public DeckPanel()
	{
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		this.setBackground(Colors.NORMAL_GREEN);

		this.setLayout(new GridBagLayout());

		this.stack = new JLayeredPane();

		this.stack.setMinimumSize(new Dimension(WIDTH - 10, CardManager.CARD_HEIGHT + 10));
		this.stack.setPreferredSize(new Dimension(WIDTH - 10, CardManager.CARD_HEIGHT + 10));

		this.add(this.stack, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		JPanel panelGameInfo = new JPanel(new GridBagLayout());
		panelGameInfo.setBackground(Colors.LIGHT_GREEN);

		JLabel lblCardsLeft = new JLabel("cards left: ");
		cardsLeft = new JLabel("1");
		JLabel lblTrump = new JLabel("trump suit: ");
		trump = new JLabel("2");

		panelGameInfo.add(lblCardsLeft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		panelGameInfo.add(cardsLeft, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		panelGameInfo.add(lblTrump, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		panelGameInfo.add(trump, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));

		panelGameInfo.setBorder(new LineBorder(Colors.DARK_GREEN, 1));
		this.add(panelGameInfo, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		this.playerInfo = new JPanel(new GridBagLayout());

		this.playerInfo.setBackground(Colors.LIGHT_GREEN);
		this.playerInfo.setBorder(new LineBorder(Colors.DARK_GREEN, 1));

		this.add(this.playerInfo, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		this.stack.setVisible(true);
	}

	private void displayStack(Deck deck)
	{
		JLabel backCard = new JLabel(new ImageIcon(CardManager.getInstance().getBackOfCard()));
		backCard.setBounds(0, 0, CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);

		this.stack.removeAll();

		if (deck.getRemainingCards() > 1)
			this.stack.add(backCard, 0);
		else if (deck.getRemainingCards() != 1)
		{
			JLabel slot = new JLabel(CardManager.getSlot());
			slot.setBounds(0, 0, CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);
			this.stack.add(slot);
		}

		if (deck.hasRemainingCards())
		{
			GuiCard lastCard = new GuiCard(deck.getLastCard());
			lastCard.setCursor(Cursor.getDefaultCursor());
			lastCard.setBounds((deck.getRemainingCards() != 1) ? 45 : 0, 0, CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);

			this.stack.add(lastCard);
		}

		this.stack.repaint();
	}

	public void updateDisplay(Deck deck, List<AbstractPlayer> players)
	{
		// TODO why does this panel sometimes disappear 
		this.displayStack(deck);

		this.cardsLeft.setText("" + deck.getRemainingCards());
		this.trump.setText("<html><b>" + CardManager.getSuitNameAsUnicode(Deck.trumpSuit) + "</b></html>");

		if (Deck.trumpSuit == Card.HEARTS || Deck.trumpSuit == Card.DIAMONDS)
			this.trump.setForeground(new Color(204, 0, 0));
		else
			this.trump.setForeground(new Color(0, 0, 0));

		this.trump.setToolTipText(CardManager.getSuitName(Deck.trumpSuit));

		JLabel lblPlayerName = new JLabel("<html><b>name</b></html>");
		JLabel lblRole = new JLabel("<html><b>role</b></html>");
		JLabel lblPlayerCardsLeft = new JLabel("<html><b>cards</b></html>");

		this.playerInfo.removeAll();

		this.playerInfo.add(new JLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 3, 3, 3), 0, 0));
		this.playerInfo.add(lblPlayerName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 3), 0, 0));
		this.playerInfo.add(lblRole, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 3), 0, 0));
		this.playerInfo.add(lblPlayerCardsLeft, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 0, 3, 3), 0, 0));

		for (int rowPanelPlayerInfo = 1; rowPanelPlayerInfo <= players.size(); rowPanelPlayerInfo++)
		{
			this.playerInfo.add(new JLabel(WelcomePanel.PLAYER_ICONS[rowPanelPlayerInfo - 1]), new GridBagConstraints(0, rowPanelPlayerInfo, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 3), 0, 0));

			this.playerInfo.add(new JLabel(players.get(rowPanelPlayerInfo - 1).getName()), new GridBagConstraints(1, rowPanelPlayerInfo, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 3), 0, 0));

			// TODO player is not automatically a defender
			if (players.get(rowPanelPlayerInfo - 1).isAttacker())
				this.playerInfo.add(new JLabel(CardManager.getImageIcon("images/icons/sword.png")), new GridBagConstraints(2, rowPanelPlayerInfo, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 3), 0, 0));
			else
				this.playerInfo.add(new JLabel(CardManager.getImageIcon("images/icons/shield_silver.png")), new GridBagConstraints(2, rowPanelPlayerInfo, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 0, 3, 3), 0, 0));

			this.playerInfo.add(new JLabel("" + players.get(rowPanelPlayerInfo - 1).getHand().size()), new GridBagConstraints(3, rowPanelPlayerInfo, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(3, 0, 3, 3), 0, 0));
		}

		if (stack != null)
			stack.setToolTipText(deck.getRemainingCards() + " cards left on stack.");

		this.playerInfo.repaint();
	}
}
