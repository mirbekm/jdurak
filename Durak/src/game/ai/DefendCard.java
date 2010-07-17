package game.ai;

import game.Card;

/**
 * The pair of cards which belong together while defending
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * 
 */
public class DefendCard
{
	private Card cardDefendingWith;
	private Card cardToBeDefeated;

	/**
	 * Create a new pair
	 * 
	 * @param cardDefendingWith
	 *            The card the player is defending with. Not <code>null</code>!
	 * @param cardToBeDefeated
	 *            The card that is to be defeated. Not <code>null</code>!
	 */
	public DefendCard(Card cardDefendingWith, Card cardToBeDefeated)
	{
		assert (cardDefendingWith != null);
		assert (cardToBeDefeated != null);

		this.cardDefendingWith = cardDefendingWith;
		this.cardToBeDefeated = cardToBeDefeated;
	}

	/**
	 * 
	 * @return The card the player is defending with
	 */
	public Card getCardDefendingWith()
	{
		return this.cardDefendingWith;
	}

	/**
	 * 
	 * @return The card that is going to be defeated
	 */
	public Card getCardToBeDefeated()
	{
		return this.cardToBeDefeated;
	}
}
