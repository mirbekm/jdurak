package game.ai;

import game.Card;
import game.Durak;

import java.util.ArrayList;
import java.util.Collections;
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
			return Collections.min(this.possibleCards);
	}

	@Override
	public boolean wantsToPlayAnotherCard()
	{
		this.resetLists();

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
					if (cardOnHand.compareTo(cardToBeDefeated) > 0)
						this.possibleCards.add(cardOnHand);

		}
		return !this.possibleCards.isEmpty();
	}

	private void resetLists()
	{
		this.possibleCards = new ArrayList<Card>();
	}
}
