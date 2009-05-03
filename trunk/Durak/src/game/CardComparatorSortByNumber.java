package game;

import java.util.Comparator;

public class CardComparatorSortByNumber implements Comparator<Card>
{

	@Override
	public int compare(Card cardOne, Card cardTwo)
	{
		if (cardOne.getNumber() == cardTwo.getNumber() && cardOne.getSuit() == cardTwo.getSuit())
			return 0;

		if (cardOne.getNumber() == cardTwo.getNumber())
			return 0;
		else
			return (cardOne.getNumber() > cardTwo.getNumber()) ? 1 : -1;
	}
}
