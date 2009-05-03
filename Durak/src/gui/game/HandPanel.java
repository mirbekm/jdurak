package gui.game;

import game.AbstractPlayer;
import game.Card;
import game.CardComparatorSortByNumber;
import gui.helpers.CardManager;
import gui.listeners.CardDragSourceListener;
import gui.listeners.DurakMouseListener;

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
	public static final int WIDTH = 700;
	public static final int HEIGHT = 200;

	private JLayeredPane handPane;
	private HandPanelMouseListener mouseListener;
	private DurakMouseListener cardMouseListener;
	private AbstractPlayer player;

	public HandPanel(DurakMouseListener cardMouseListener)
	{
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(new Color(57, 97, 22));

		this.setLayout(new GridBagLayout());
		this.mouseListener = new HandPanelMouseListener();
		this.cardMouseListener = cardMouseListener;

		handPane = new JLayeredPane();
		handPane.setAutoscrolls(true);

		this.add(handPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
		handPane.setVisible(true);
	}

	public void updateDisplay(AbstractPlayer player)
	{
		this.handPane.removeAll();

		this.player = player;

		ArrayList<Card> cards = player.getHand();

		int panelWidth = (this.getWidth() == 0) ? WIDTH : this.getWidth();

		if (this.cardMouseListener.getDurakActionListener().getDurakWindow().getDurakPanel().getTurnPanel().sortBySuit())
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
			guiCard.addMouseListener(this.mouseListener);

			if (player.isAttacker())
				guiCard.addMouseListener(this.cardMouseListener);

			new CardDragSourceListener(guiCard);

			this.handPane.add(guiCard, 0);
		}

		this.repaint(0, 0, this.getWidth(), this.getHeight());
	}

	public void repaint()
	{
		super.repaint();
		this.updateDisplay();
	}

	public void updateDisplay()
	{
		if (this.player != null)
			this.updateDisplay(this.player);
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
