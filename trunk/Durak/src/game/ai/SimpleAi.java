package game.ai;

import game.Card;
import game.Durak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleAi extends AbstractAi
{
	private ArrayList<Card> possibleCards;

	public SimpleAi(Durak durak, String name)
	{
		super(durak, name);
	}

	@Override
	public Card[] getNextDefendCard()
	{
		List<Card> cardsToBeDefeated = this.durak.getTable().getNotYetDefeatedCards();
		if (this.isAttacker || cardsToBeDefeated.isEmpty())
			return null;

		Card cardToBeDefeated = Collections.min(cardsToBeDefeated);

		ArrayList<Card> possibleDefendCards = new ArrayList<Card>();

		for (Card cardDefendingWith : this.possibleCards)
			if (cardDefendingWith.isGreaterThan(cardToBeDefeated))
				possibleDefendCards.add(cardDefendingWith);

		return new Card[] { cardToBeDefeated, Collections.min(possibleDefendCards) };
	}

	@Override
	public Card getNextAttackCard()
	{

		if (this.possibleCards.isEmpty() || !this.isAttacker)
			return null;
		else
			return Collections.min(this.possibleCards, new CardComparator());
	}

	@Override
	public boolean wantsToPlayAnotherCard()
	{
		this.resetLists();

		// TODO don't give away trumps so easily

		if (this.hand.isEmpty()) // no remaining cards
			return false;

		if (this.isAttacker())
		{
			if (this.durak.getTable().getNumbersOnTable().isEmpty())
			{
				this.possibleCards.addAll(this.hand);
			}
			else
			{
				// choose the possible cards and add them to the list
				for (Integer numberOnTable : this.durak.getTable().getNumbersOnTable())
					for (Card cardOnHand : this.hand)
						if (cardOnHand.getNumber() == numberOnTable.intValue())
							this.possibleCards.add(cardOnHand);
			}
		}
		// is defender
		else
		{
			for (Card cardToBeDefeated : this.durak.getTable().getNotYetDefeatedCards())
				for (Card cardOnHand : this.hand)
					if (cardOnHand.isGreaterThan(cardToBeDefeated))
						this.possibleCards.add(cardOnHand);

		}
		return !this.possibleCards.isEmpty();
	}

	private void resetLists()
	{
		this.possibleCards = new ArrayList<Card>();
	}

	private class CardComparator implements Comparator<Card>
	{

		@Override
		public int compare(Card cardOne, Card cardTwo)
		{
			if (cardOne.getNumber() == cardTwo.getNumber() && cardOne.getSuit() == cardTwo.getSuit())
				return 0;

			if (cardOne.isTrump() && !cardTwo.isTrump())
				return 1;
			else if (!cardOne.isTrump() && cardTwo.isTrump())
				return -1;
			else if (cardOne.isTrump() && cardTwo.isTrump())
				return (cardOne.getNumber() > cardTwo.getNumber()) ? 1 : -1;

			if (cardOne.getNumber() == cardTwo.getNumber())
				return 0;
			else
				return (cardOne.getNumber() > cardTwo.getNumber()) ? 1 : -1;
		}
	}
}
