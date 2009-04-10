package game;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * 
 */
public class Deck
{
	/**
	 * The number of the trump suit. Is either {@link Card#CLUBS}, {@link Card#DIAMONDS}, {@link Card#HEARTS} or {@link Card#SPADES}
	 */
	public static int trumpSuit = -1;

	private ArrayList<Card> deck = null;
	private Card lastCard = null;

	/**
	 * Create a new deck with 4 * <code>cardsPerSuit</code> cards
	 * 
	 * @param cardsPerSuit
	 *            the cards per suit. Has to be greater than 1 and smaller than 15. Counting starts at 14 (ace) and ends at 14 - <code>cardsPerSuit</code>
	 */
	public Deck(int cardsPerSuit)
	{
		this.fillDeckRandomly(cardsPerSuit);
	}

	/**
	 * Fill the deck with the correct amount of cards and shuffle it afterwards
	 * 
	 * @param cardsPerSuit
	 *            the cards per suit. Has to be greater than 1 and smaller than 15. Counting starts at 14 (ace) and ends at 14 - <code>cardsPerSuit</code>
	 */
	private void fillDeckRandomly(int cardsPerSuit)
	{
		assert (cardsPerSuit > 1 && cardsPerSuit < 15);

		this.deck = new ArrayList<Card>();

		for (int suit = 0; suit < 4; suit++)
		{
			for (int number = 15 - cardsPerSuit; number <= 14; number++)
			{
				deck.add(new Card(suit, number));
			}
		}

		Collections.shuffle(this.deck);
		this.lastCard = this.deck.get(0);
		this.deck.remove(0);
		trumpSuit = this.lastCard.getSuit();
	}

	/**
	 * 
	 * @return <code>true</code> if there are cards remaining on the stack.
	 */
	public boolean hasRemainingCards()
	{
		return this.remainingCards() > 0;
	}

	/**
	 * 
	 * @return number of cards remaining on the stack
	 */
	public int remainingCards()
	{
		return ((this.lastCard == null) ? 0 : 1) + this.deck.size();
	}

	/**
	 * 
	 * @return the last card on the stack. <code>null</code> if there is no card left.
	 */
	public Card getLastCard()
	{
		return this.lastCard;
	}

	/**
	 * 
	 * @return a card from the stack. <code>null</code> if there are no more cards left.
	 */
	public Card getCard()
	{
		Card returnCard = null;

		if (this.deck.size() > 0)
		{
			returnCard = this.deck.get(0);
			this.deck.remove(0);
		}
		else if (this.lastCard != null)
		{
			returnCard = this.lastCard;
			this.lastCard = null;
		}

		return returnCard;
	}

	@Override
	public String toString()
	{
		return "remaining cards on deck: " + this.remainingCards();
	}
}
