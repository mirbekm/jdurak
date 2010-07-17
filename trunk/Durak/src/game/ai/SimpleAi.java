package game.ai;

import game.Card;
import game.Durak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleAi extends AbstractAi
{
	private Set<Card> possibleCards;

	public SimpleAi(Durak durak, String name)
	{
		super(durak, name);
		this.possibleCards = new HashSet<Card>();
	}

	@Override
	public DefendCard getNextDefendCard()
	{
		List<Card> cardsToBeDefeated = new ArrayList<Card>(this.getTable().getNotYetDefeatedCards());

		if (this.isAttacker || cardsToBeDefeated.isEmpty())
			return null;

		ArrayList<Card> possibleDefendCards = new ArrayList<Card>();

		boolean lookingForDefenseCard = true;
		Card cardToBeDefeated = null;

		do
		{
			cardToBeDefeated = Collections.min(cardsToBeDefeated);

			for (Card cardDefendingWith : this.possibleCards)
				if (cardDefendingWith.isGreaterThan(cardToBeDefeated))
					possibleDefendCards.add(cardDefendingWith);

			if (possibleDefendCards.isEmpty())
				cardsToBeDefeated.remove(cardToBeDefeated);
			else
				lookingForDefenseCard = false;
		}
		while (lookingForDefenseCard);

		return new DefendCard(Collections.min(possibleDefendCards), cardToBeDefeated);
	}

	@Override
	public Card getNextAttackCard()
	{

		if (this.possibleCards.isEmpty() || !this.isAttacker)
			return null;
		else
			return Collections.min(this.possibleCards, new CardComparatorSortByNumberAndTrump());
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
			// choose the possible cards and add them to the list
			for (Card cardOnHand : this.hand)
				if (this.getTable().canAttackWithThisCard(cardOnHand))
					this.possibleCards.add(cardOnHand);
		}
		// is defender
		else
		{
			for (Card cardToBeDefeated : this.getTable().getNotYetDefeatedCards())
				for (Card cardOnHand : this.hand)
					if (cardOnHand.isGreaterThan(cardToBeDefeated))
						this.possibleCards.add(cardOnHand);
		}

		return !this.possibleCards.isEmpty();
	}

	private void resetLists()
	{
		this.possibleCards.clear();
	}

	/**
	 * Sorts the cards by number and trump. Lowest number and no trump will be first. Highest number or highest trump will be last.
	 * 
	 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
	 * 
	 */
	private class CardComparatorSortByNumberAndTrump implements Comparator<Card>
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
