package game;

import java.util.ArrayList;

/**
 * A human player
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * 
 */
public class Player
{
	private String name;
	private ArrayList<Card> hand;

	private Deck deck;
	private Durak durak;

	private boolean isAttacker = false;

	/**
	 * Create a new player with the given parameters
	 * 
	 * @param durak
	 *            The game logic
	 * @param name
	 *            The name of the player
	 */
	public Player(Durak durak, String name)
	{
		assert (name != null && !name.isEmpty());
		assert (durak != null);

		this.name = name;
		this.durak = durak;

	}

	/**
	 * Start a new game
	 */
	public void newGame()
	{
		this.hand = new ArrayList<Card>();
		this.deck = durak.getTable().getDeck();

		this.fillUp();
	}

	/**
	 * The player checks if he has enough cards. If not he will try to get as many cards from the deck as needed.
	 */
	public void fillUp()
	{
		if (this.deck.hasRemainingCards())
		{
			while (this.deck.hasRemainingCards() && this.hand.size() < durak.getRules().getNumberOfCardsPerPlayer())
			{
				this.hand.add(this.deck.getCard());
			}
		}
	}

	/**
	 * 
	 * @return the name of the player
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * 
	 * @return The cards the player has on his hand
	 */
	public ArrayList<Card> getHand()
	{
		return this.hand;
	}

	/**
	 * Set if this player is one of the attackers
	 * 
	 * @param isAttacker
	 *            <code>true</code> this player becomes one of the attackers
	 */
	public void setIsAttacker(boolean isAttacker)
	{
		this.isAttacker = isAttacker;
	}

	/**
	 * 
	 * @return <code>true</code> if the player is one of the attackers
	 */
	public boolean isAttacker()
	{
		return this.isAttacker;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
