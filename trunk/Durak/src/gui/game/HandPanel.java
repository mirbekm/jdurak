package gui.game;

import game.AbstractPlayer;
import game.Card;
import game.CardComparatorSortByNumber;
import game.Table;
import game.ai.AbstractAi;
import gui.helpers.CardManager;
import gui.listeners.AttackMouseListener;
import gui.listeners.CardDragSourceListener;
import gui.listeners.DefendMouseListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class HandPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 700;
	public static final int HEIGHT = 200;

	private JLayeredPane handPane;
	private HandPanelMouseListener mouseListener;
	private AttackMouseListener attackMouseListener;
	private DefendMouseListener defendMouseListener;

	private AbstractPlayer player;
	private boolean isEnabled = false;
	private Table table;

	public HandPanel(AttackMouseListener cardMouseListener, DefendMouseListener defendMouseListener)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(new Color(57, 97, 22));

		this.setLayout(new GridBagLayout());
		this.mouseListener = new HandPanelMouseListener();
		this.attackMouseListener = cardMouseListener;
		this.defendMouseListener = defendMouseListener;

		this.handPane = new JLayeredPane();
		this.handPane.setAutoscrolls(true);

		this.add(this.handPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		this.handPane.setVisible(true);
	}

	public void reset()
	{
		this.handPane.removeAll();
	}

	@Override
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}

	public void updateDisplay(AbstractPlayer player, Table table)
	{
		this.handPane.removeAll();

		this.player = player;
		this.table = table;

		if (!(player instanceof AbstractAi))
		{
			ArrayList<Card> cards = player.getHand();

			int panelWidth = (this.getWidth() == 0) ? WIDTH : this.getWidth();

			if (this.attackMouseListener.getDurakActionListener().getDurakWindow().getDurakPanel().getTurnPanel().sortBySuit())
				Collections.sort(cards);
			else
				Collections.sort(cards, new CardComparatorSortByNumber());

			int counter = 0;
			int xOffset = 17;

			if ((panelWidth - 10) / CardManager.CARD_WIDTH >= cards.size())
				xOffset = CardManager.CARD_WIDTH + 2;
			else
				xOffset = ((panelWidth - 12) - (CardManager.CARD_WIDTH + 1)) / (cards.size() - 1);

			for (Card card : cards)
			{
				GuiCard guiCard = new GuiCard(card);
				guiCard.setBounds((counter++) * (xOffset), 0, CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);

				boolean isCardEnabled = this.isEnabled;

				if (isCardEnabled && this.table.getRules().doHelp())
				{
					if (player.isAttacker() && !table.canAttackWithThisCard(card))
						isCardEnabled = false;
					else if (player.isDefender())
					{
						boolean cardEnabled = false;

						for (Card cardToBeDefeated : table.getCardsOfAttackerOneOnTable())
							cardEnabled |= table.canDefendWithCard(card, cardToBeDefeated);

						if (table.getCardsOfAttackerTwoOnTable() != null)
							for (Card cardToBeDefeated : table.getCardsOfAttackerTwoOnTable())
								cardEnabled |= table.canDefendWithCard(card, cardToBeDefeated);

						isCardEnabled = cardEnabled;
					}
				}

				if (isCardEnabled)
					guiCard.addMouseListener(this.mouseListener);
				guiCard.setEnabled(isCardEnabled);

				if (player.isAttacker() && isCardEnabled)
					guiCard.addMouseListener(this.attackMouseListener);

				if (player.isDefender() && isCardEnabled)
					guiCard.addMouseListener(this.defendMouseListener);

				if (player.isAttacker() || player.isDefender() && isCardEnabled)
					new CardDragSourceListener(guiCard);

				this.handPane.add(guiCard, 0);
			}

			this.repaint(0, 0, this.getWidth(), this.getHeight());
		}
	}

	public void repaint()
	{
		super.repaint();
		this.updateDisplay();
	}

	public void updateDisplay()
	{
		if (this.player != null)
			this.updateDisplay(this.player, this.table);
	}

	public void removeCard(GuiCard card)
	{
		this.handPane.remove(card);
		this.handPane.repaint();
	}

	private class HandPanelMouseListener extends MouseInputAdapter
	{
		public void mouseEntered(MouseEvent e)
		{
			GuiCard card = (GuiCard) e.getComponent();
			card.setOrigin(card.getLocation());
			Rectangle bounds = card.getBounds();
			bounds.y = bounds.y + 10;
			card.setBounds(bounds);
		}

		public void mouseExited(MouseEvent e)
		{
			GuiCard card = (GuiCard) e.getComponent();
			Rectangle bounds = card.getBounds();
			bounds.y = bounds.y - 10;
			card.setBounds(bounds);
		}
	}
}
