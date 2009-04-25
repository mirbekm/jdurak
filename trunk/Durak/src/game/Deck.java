package game;

import java.util.Collections;
import java.util.Stack;

/**
 * The deck. The deck is filled with a number of cards (specified in the rules). The deck will be shuffled instantly.
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * @version $Date$ - Revision: $Rev$
 */
public class Deck
{
	/**
	 * The number of the trump suit. Is either {@link Card#CLUBS}, {@link Card#DIAMONDS}, {@link Card#HEARTS} or {@link Card#SPADES}
	 */
	public static int trumpSuit = -1;

	private Stack<Card> deck = null;
	private Card lastCard = null;

	/**
	 * Create a new deck with 4 * <code>cardsPerSuit</code> cards
	 * 
	 * @param cardsPerSuit
	 *            the cards per suit. Has to be greater than {@value Rules#MIN_AMOUNT_OF_CARDS_PER_SUIT} and smaller than {@value Rules#MAX_AMOUNT_OF_CARDS_PER_SUIT} (incl). <code>cardsPerSuit</code> % 4 has to be 0
	 */
	public Deck(int cardsPerSuit)
	{
		assert (cardsPerSuit >= Rules.MIN_AMOUNT_OF_CARDS_PER_SUIT && cardsPerSuit <= Rules.MAX_AMOUNT_OF_CARDS_PER_SUIT);
		assert (cardsPerSuit % 4 == 0);

		this.fillDeckRandomly(cardsPerSuit);
	}

	/**
	 * Fill the deck with the correct amount of cards and shuffle it afterwards
	 * 
	 * @param cardsPerSuit
	 *            the cards per suit. Has to be greater than {@value Rules#MIN_AMOUNT_OF_CARDS_PER_SUIT} and smaller than {@link Rules#MAX_AMOUNT_OF_CARDS_PER_SUIT} (incl). <code>cardsPerSuit</code> % 4 has to be 0
	 */
	private void fillDeckRandomly(int cardsPerSuit)
	{
		assert (cardsPerSuit >= Rules.MIN_AMOUNT_OF_CARDS_PER_SUIT && cardsPerSuit <= Rules.MAX_AMOUNT_OF_CARDS_PER_SUIT);
		assert (cardsPerSuit % 4 == 0);

		this.deck = new Stack<Card>();
		for (int suit = 0; suit < 4; suit++)
			for (int number = 15 - cardsPerSuit; number <= 14; number++)
				deck.push(new Card(suit, number));

		Collections.shuffle(this.deck);
		this.lastCard = this.deck.pop();
		trumpSuit = this.lastCard.getSuit();
	}

	/**
	 * 
	 * @return <code>true</code> if there are cards remaining on the stack.
	 */
	public boolean hasRemainingCards()
	{
		return this.getRemainingCards() > 0;
	}

	/**
	 * 
	 * @return number of cards remaining on the stack
	 */
	public int getRemainingCards()
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
			returnCard = this.deck.pop();
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
		return "remaining cards on deck: " + this.getRemainingCards();
	}
}
