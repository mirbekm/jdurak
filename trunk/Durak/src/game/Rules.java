package game;

public class Rules
{
	public static int MIN_AMOUNT_OF_CARDS = 36;
	public static int MAX_AMOUNT_OF_CARDS = 56;

	private int numberOfCardsPerSuit = 9;
	private int numberOfCardsPerPlayer = 16;
	private boolean transferringAllowed = false;

	/**
	 * Create a set of rules with all default values but the number of cards
	 * 
	 * @param numberOfCards
	 *            The number of cards in the game. Has to be >= {@link Rules#MIN_AMOUNT_OF_CARDS} and <= {@link Rules#MAX_AMOUNT_OF_CARDS} and furthermore <code>numberOfCards</code> % 4 has to be 0
	 */
	public Rules(int numberOfCards)
	{
		this.setNumberOfCards(numberOfCards);
	}

	/**
	 * The number of cards in the game
	 * 
	 * @return a value between 36 and 52 (incl). return value % 4 = 0
	 */
	public int getNumberOfCards()
	{
		return numberOfCardsPerSuit * 4;
	}

	/**
	 * Set the number of cards in the game
	 * 
	 * @param numberOfCards
	 *            The number of cards in the game. Has to be >= {@value Rules#MIN_AMOUNT_OF_CARDS} and <= {@value Rules#MAX_AMOUNT_OF_CARDS} and furthermore <code>numberOfCards</code> % 4 has to be 0
	 */
	public void setNumberOfCards(int numberOfCards)
	{
		assert (numberOfCards % 4 == 0);
		assert (numberOfCards >= Rules.MIN_AMOUNT_OF_CARDS && numberOfCards <= Rules.MAX_AMOUNT_OF_CARDS);

		this.numberOfCardsPerSuit = numberOfCards / 4;
	}

	/**
	 * 
	 * @return the number of cards per suit. from 9 to 14
	 */
	public int getNumberOfCardsPerSuit()
	{
		return numberOfCardsPerSuit;
	}

	/**
	 * 
	 * @return the number of cards per player. default: 6
	 */
	public int getNumberOfCardsPerPlayer()
	{
		return numberOfCardsPerPlayer;
	}

	/**
	 * 
	 * @param numberOfCardsPerPlayer
	 *            the number of cards per player. from 6 to 10 (incl)
	 */
	public void setNumberOfCardsPerPlayer(int numberOfCardsPerPlayer)
	{
		assert (numberOfCardsPerPlayer >= 6 && numberOfCardsPerPlayer <= 10);

		this.numberOfCardsPerPlayer = numberOfCardsPerPlayer;
	}

	/**
	 * 
	 * @return <code>true</code> transferring is allowed
	 */
	public boolean isTransferringAllowed()
	{
		return transferringAllowed;
	}

	/**
	 * 
	 * @param bool
	 *            <code>true</code> wether transferring should be allowed
	 */
	public void setTransferringAllowed(boolean bool)
	{
		this.transferringAllowed = bool;
	}

	@Override
	public String toString()
	{
		return "number of cards: " + this.getNumberOfCards() + "\n" + "number of cards per player: " + this.numberOfCardsPerPlayer + "\n" + "number of cards per suit: " + this.numberOfCardsPerSuit;
	}
}
