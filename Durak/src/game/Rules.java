package game;

/**
 * The rules for a game of durak.
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * @version $Date$ - Revision: $Rev$
 */
public class Rules
{
	public static int MIN_AMOUNT_OF_CARDS = 32;
	public static int MAX_AMOUNT_OF_CARDS = 52;

	public static int MIN_AMOUNT_OF_CARDS_PER_SUIT = 8;
	public static int MAX_AMOUNT_OF_CARDS_PER_SUIT = 13;

	private int numberOfCardsPerSuit = 8;
	private int numberOfCardsPerPlayer = 6;
	private boolean isTransferringAllowed = false;
	private boolean fiveCardsAtStartup = true;

	private boolean doHelp = true;

	/**
	 * Create a set of rules with all default values but the number of cards
	 * 
	 * @param numberOfCards
	 *            The number of cards in the game. Has to be >= {@link Rules#MIN_AMOUNT_OF_CARDS} and <= {@link Rules#MAX_AMOUNT_OF_CARDS} and furthermore
	 *            <code>numberOfCards</code> % 4 has to be 0
	 */
	public Rules(int numberOfCards)
	{
		this.setNumberOfCards(numberOfCards);
	}

	/**
	 * The number of cards in the game
	 * 
	 * @return a value between {@value Rules#MIN_AMOUNT_OF_CARDS} and {@value Rules#MAX_AMOUNT_OF_CARDS} (incl). return value % 4 = 0
	 */
	public int getNumberOfCards()
	{
		return numberOfCardsPerSuit * 4;
	}

	/**
	 * Set the number of cards in the game
	 * 
	 * @param numberOfCards
	 *            The number of cards in the game. Has to be >= {@value Rules#MIN_AMOUNT_OF_CARDS} and <= {@value Rules#MAX_AMOUNT_OF_CARDS} and furthermore
	 *            <code>numberOfCards</code> % 4 has to be 0
	 */
	public void setNumberOfCards(int numberOfCards)
	{
		assert (numberOfCards % 4 == 0);
		assert (numberOfCards >= Rules.MIN_AMOUNT_OF_CARDS && numberOfCards <= Rules.MAX_AMOUNT_OF_CARDS);

		this.numberOfCardsPerSuit = numberOfCards / 4;
	}

	/**
	 * 
	 * @return the number of cards per suit. from {@value Rules#MIN_AMOUNT_OF_CARDS_PER_SUIT} to {@value Rules#MAX_AMOUNT_OF_CARDS_PER_SUIT}
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
		return isTransferringAllowed;
	}

	/**
	 * If this rule is active you can transfer cards.
	 * 
	 * @param bool
	 *            <code>true</code> whether transferring should be allowed
	 */
	public void setTransferringAllowed(boolean bool)
	{
		this.isTransferringAllowed = bool;
	}

	@Override
	public String toString()
	{
		return "number of cards: " + this.getNumberOfCards() + "\n" + "number of cards per player: " + this.numberOfCardsPerPlayer + "\n" + "number of cards per suit: " + this.numberOfCardsPerSuit;
	}

	/**
	 * If this rule is active, the defender can only be attacked by 5 cards maximum until there are at least two cards out of the game.
	 * 
	 * @return <code>true</code> means that the rule is active
	 */
	public boolean isFiveCardsAtStartup()
	{
		return this.fiveCardsAtStartup;
	}

	/**
	 * Activate or deactivate the rule.
	 * 
	 * @param isFiveCardsAtStartup
	 *            <code>true</code> to activate the rule
	 */
	public void setFiveCardsAtStartup(boolean isFiveCardsAtStartup)
	{
		this.fiveCardsAtStartup = isFiveCardsAtStartup;
	}

	/**
	 * If this is <code>true</code> the player gets a visual help, which cards he can play.
	 * 
	 * @return <code>true</code> for visual help
	 */
	public boolean doHelp()
	{
		return this.doHelp;
	}

	/**
	 * Set to <code>true</code> if you want to activate the visual help
	 * 
	 * @param doHelp
	 *            <code>true</code> to activate the visual help
	 */
	public void setDoHelp(boolean doHelp)
	{
		this.doHelp = doHelp;
	}
}
