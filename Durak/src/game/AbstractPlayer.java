package game;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The abstract player. All players have to derive from this class.
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * @version $Date$ - Revision: $Rev$
 */
public abstract class AbstractPlayer
{
	protected String name;
	protected ArrayList<Card> hand;

	protected Durak durak;

	protected boolean isAttacker = false;
	private boolean isDefender;

	/**
	 * Create a new player with the given parameters
	 * 
	 * @param durak
	 *            The game logic
	 * @param name
	 *            The name of the player
	 */
	public AbstractPlayer(Durak durak, String name)
	{
		assert (name != null && !name.isEmpty());
		assert (durak != null);

		this.name = name;
		this.durak = durak;

	}

	/**
	 * Start a new game
	 */
	public void newGame(Deck deck)
	{
		this.hand = new ArrayList<Card>();

		this.fillUp(deck);
	}

	/**
	 * The player checks if he has enough cards. If not he will try to get as many cards from the deck as needed.
	 */
	public void fillUp(Deck deck)
	{
		while (deck.hasRemainingCards() && this.hand.size() < this.durak.getRules().getNumberOfCardsPerPlayer())
			this.hand.add(deck.getCard());
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
	 * Set if this player the defender
	 * 
	 * @param isAttacker
	 *            <code>true</code> this player becomes one the defender
	 */
	public void setIsDefender(boolean isDefender)
	{
		this.isDefender = isDefender;
	}

	/**
	 * 
	 * @return <code>true</code> if the player is one of the attackers
	 */
	public boolean isAttacker()
	{
		return this.isAttacker;
	}

	/**
	 * 
	 * @return <code>true</code> if the player is the defender
	 */
	public boolean isDefender()
	{
		return this.isDefender;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public Card attackWith(Card card)
	{
		if (!this.hand.contains(card))
			return null;

		this.durak.getTable().attack(card, this);
		this.hand.remove(card);

		return card;
	}

	public Card getLowestTrump()
	{
		if (this.hand.isEmpty())
			return null;
		else
		{
			ArrayList<Card> listOfTrumps = new ArrayList<Card>();
			for (Card card : this.hand)
			{
				if (card.getSuit() == Deck.trumpSuit)
					listOfTrumps.add(card);
			}

			if (listOfTrumps.isEmpty())
				return null;
			else
				return Collections.min(listOfTrumps);
		}
	}
}
