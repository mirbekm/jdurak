package gui.listeners;

import game.Card;
import game.Durak;
import gui.game.GuiCard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DefendMouseListener extends MouseAdapter
{

	private Durak durak;
	private DurakActionListener actionListener;

	public DefendMouseListener(Durak durak, DurakActionListener actionListener)
	{
		this.durak = durak;
		this.actionListener = actionListener;
	}

	public void mouseClicked(MouseEvent e)
	{
		Card card = ((GuiCard) e.getComponent()).getCard();

		List<Card> notYetDefeatedCards = this.durak.getTable().getNotYetDefeatedCards();

		if (notYetDefeatedCards.size() == 1)
			this.actionListener.defendCard(card, notYetDefeatedCards.get(0));

		else
		{
			int suit = card.getSuit();
			int suitCount = 0;
			Card cardToBeDefeated = null;

			for (Card cardInList : notYetDefeatedCards)
				if (cardInList.getSuit() == suit)
				{
					cardToBeDefeated = cardInList;
					suitCount++;
				}

			if (suitCount == 1)
				this.actionListener.defendCard(card, cardToBeDefeated);
		}
	}
}
