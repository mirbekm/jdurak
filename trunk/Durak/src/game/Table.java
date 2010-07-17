package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The most important part of the game. Most of the game logic is provided by the table
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * @version $Date$ - Revision: $Rev$
 */
public class Table
{
	private Deck deck = null;

	private ArrayList<AbstractPlayer> players = null;

	private AbstractPlayer defender = null;
	private ArrayList<AbstractPlayer> attackers = null;
	private ArrayList<AbstractPlayer> winners = null;
	private Durak durak;

	private ArrayList<Card> cardsOfAttackerOneOnTable;
	private ArrayList<Card> cardsOfAttackerTwoOnTable;
	private ArrayList<Card> cardsOfDefender;
	private HashMap<Card, Card> defendedCards;
	private HashSet<Integer> numbersOnTable;

	/**
	 * Create a new table
	 * 
	 * @param players
	 *            The players. The size has to be >= 2
	 * @param durak
	 *            The reference to the durak game
	 */
	public Table(ArrayList<AbstractPlayer> players, Durak durak)
	{
		assert (players.size() >= 2);

		this.durak = durak;
		this.players = players;

		this.newGame();
	}

	/**
	 * Start a new game. Reset all lists etc. Choose the new active player.
	 */
	public void newGame()
	{
		this.attackers = new ArrayList<AbstractPlayer>();
		this.winners = new ArrayList<AbstractPlayer>();

		this.deck = new Deck(this.durak.getRules().getNumberOfCardsPerSuit());

		this.resetLists();

		for (AbstractPlayer player : players)
			player.newGame(this.deck);

		Card currentLowestTrump = null;
		AbstractPlayer playerToStart = null;
		for (AbstractPlayer player : players)
		{
			if (currentLowestTrump == null)
			{
				currentLowestTrump = player.getLowestTrump();
				playerToStart = player;
			}
			else if (player.getLowestTrump() != null && !player.getLowestTrump().isGreaterThan(currentLowestTrump))
			{
				currentLowestTrump = player.getLowestTrump();
				playerToStart = player;
			}
		}

		if (playerToStart == null)
			playerToStart = players.get(0);

		int indexOfPlayerToStart = this.players.indexOf(playerToStart);

		this.attackers.add(playerToStart);
		playerToStart.setIsAttacker(true);

		this.defender = this.players.get((indexOfPlayerToStart + 1) % this.players.size());
		this.defender.setIsDefender(true);

		if (this.players.size() > 2)
		{
			AbstractPlayer playerLeftToDefender = this.players.get((this.players.indexOf(this.defender) + this.players.size() - 1) % this.players.size());
			AbstractPlayer playerRightToDefender = this.players.get((this.players.indexOf(this.defender) + 1) % this.players.size());

			if (playerLeftToDefender.equals(playerToStart))
				this.attackers.add(playerRightToDefender);
			else
				this.attackers.add(playerLeftToDefender);

			this.attackers.get(1).setIsAttacker(true);
		}

	}

	private void resetLists()
	{
		this.cardsOfAttackerOneOnTable = new ArrayList<Card>();
		this.cardsOfAttackerTwoOnTable = new ArrayList<Card>();
		this.cardsOfDefender = new ArrayList<Card>();
		this.defendedCards = new HashMap<Card, Card>();
		this.numbersOnTable = new HashSet<Integer>();
	}

	/**
	 * 
	 * @return the list of cards from attacker one, that are on the table
	 */
	public List<Card> getCardsOfAttackerOneOnTable()
	{
		return Collections.unmodifiableList(this.cardsOfAttackerOneOnTable);
	}

	/**
	 * 
	 * @return the list of cards from attacker two, that are on the table. <code>null</code> if there is no second attacker
	 */
	public List<Card> getCardsOfAttackerTwoOnTable()
	{
		if (this.attackers.size() > 1)
			return Collections.unmodifiableList(this.cardsOfAttackerTwoOnTable);
		else
			return null;
	}

	/**
	 * 
	 * @return the list of defended cards with its defenders
	 */
	public Map<Card, Card> getDefendedCards()
	{
		return Collections.unmodifiableMap(this.defendedCards);
	}

	/**
	 * defend a card
	 * 
	 * @param cardToBeDefeated
	 *            The card that has to be defeated
	 * @param cardDefendingWith
	 *            The card the player is defending with
	 */
	public void defend(Card cardToBeDefeated, Card cardDefendingWith)
	{
		assert (cardToBeDefeated != null & cardDefendingWith != null);

		if (!this.defendedCards.containsKey(cardToBeDefeated))
		{
			if (cardDefendingWith.isGreaterThan(cardToBeDefeated))
			{
				this.defender.getHand().remove(cardDefendingWith);
				this.numbersOnTable.add(cardDefendingWith.getNumber());
				this.cardsOfDefender.add(cardDefendingWith);
				this.defendedCards.put(cardToBeDefeated, cardDefendingWith);
			}
			else
			{
				// TODO defending card is actually smaller than the card to be defended
			}
		}
		else
		{
			// TODO card is already defeated
		}
	}

	/**
	 * attack with a card
	 * 
	 * @param attackingCard
	 *            the attacking card
	 * @param attackingPlayer
	 *            the player that is attacking
	 */
	public void attack(Card attackingCard, AbstractPlayer attackingPlayer)
	{
		assert (attackingCard != null);
		assert (attackingPlayer != null);

		if (!this.attackers.contains(attackingPlayer))
			// TODO proper error message
			throw new IllegalArgumentException(attackingPlayer + " is not an attacking player!");

		if (this.numbersOnTable.isEmpty() || this.numbersOnTable.contains(attackingCard.getNumber()))
		{
			ArrayList<Card> cardsOfAttacker = this.cardsOfAttackerOneOnTable;
			if (this.attackers.size() > 1 && this.attackers.get(1).equals(attackingPlayer))
				cardsOfAttacker = this.cardsOfAttackerTwoOnTable;

			cardsOfAttacker.add(attackingCard);

			this.numbersOnTable.add(attackingCard.getNumber());
		}
		else
		{
			// TODO proper error message
			System.err.println("the card cannot be played because there is no card by this number");
		}

	}

	/**
	 * 
	 * @param card
	 *            The card that the player is trying to attack with
	 * @return <code>true</code> if the player can attack with the card
	 */
	public boolean canAttackWithThisCard(Card card)
	{
		boolean defenderHasEnoughCards = this.defender.getHand().size() - 1 - this.getNotYetDefeatedCards().size() >= 0;
		boolean numberIsAlreadyOnTable = this.numbersOnTable.contains(card.getNumber());
		boolean noCardsOnTable = this.numbersOnTable.isEmpty();
		boolean firstAttacker = this.attackers.get(0).equals(this.ownerOfAttackingCard(card));

		if (noCardsOnTable)
			return firstAttacker;

		return defenderHasEnoughCards && numberIsAlreadyOnTable;
	}

	/**
	 * 
	 * @param defendingCard
	 *            The defending card. Not <code>null</code>!
	 * @param cardToBeDefeated
	 *            The card to be defeated. Not <code>null</code>!
	 * @return <code>true</code> if the defending can defeat the other card
	 */
	public boolean canDefendWithCard(Card defendingCard, Card cardToBeDefeated)
	{
		assert (defendingCard != null);
		assert (cardToBeDefeated != null);

		return defendingCard.isGreaterThan(cardToBeDefeated) && !this.defendedCards.containsKey(cardToBeDefeated);
	}

	private AbstractPlayer ownerOfAttackingCard(Card card)
	{
		for (AbstractPlayer attacker : this.attackers)
			if (attacker.getHand().contains(card))
				return attacker;

		return null;
	}

	/**
	 * Get the deck
	 * 
	 * @return The deck
	 */
	public Deck getDeck()
	{
		return this.deck;
	}

	/**
	 * Get the list of players, that are still in the game
	 * 
	 * @return The list of players, that are still in the game
	 */
	public List<AbstractPlayer> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}

	/**
	 * Get the list of attackers. Either one or two attackers are in the list
	 * 
	 * @return The list of attackers.
	 */
	public List<AbstractPlayer> getAttackers()
	{
		return Collections.unmodifiableList(attackers);
	}

	/**
	 * Get the defending Player
	 * 
	 * @return The defender
	 */
	public AbstractPlayer getDefender()
	{
		return this.defender;
	}

	/**
	 * End a turn.
	 */
	public void endTurn()
	{
		boolean defenderHasWon = this.defendedCards.size() == this.cardsOfAttackerOneOnTable.size() + this.cardsOfAttackerTwoOnTable.size();

		for (AbstractPlayer player : this.players)
		{
			player.setIsAttacker(false);
			player.setIsDefender(false);
		}

		// fill up
		if (!defenderHasWon)
		{
			this.defender.getHand().addAll(this.cardsOfDefender);
			this.defender.getHand().addAll(this.cardsOfAttackerOneOnTable);
			this.defender.getHand().addAll(this.cardsOfAttackerTwoOnTable);
		}

		for (AbstractPlayer player : this.attackers)
			player.fillUp(this.deck);

		this.defender.fillUp(this.deck);

		// check if any of the remaining players has won
		boolean foundPlayer = false;
		for (AbstractPlayer player : this.players)
			if (player.getHand().size() == 0)
				foundPlayer = this.winners.add(player);

		if (foundPlayer)
			this.players.removeAll(this.winners);

		this.resetLists();

		// Choose the next players
		int indexOfOldDefender = this.players.indexOf(this.defender);

		if (defenderHasWon)
			this.defender = this.players.get((indexOfOldDefender + 1) % this.players.size());
		else
			this.defender = this.players.get((indexOfOldDefender + 2) % this.players.size());

		this.defender.setIsDefender(true);

		this.attackers.clear();
		this.attackers.add(this.players.get((this.players.indexOf(this.defender) + this.players.size() - 1) % this.players.size()));

		if (this.players.size() > 2)
			this.attackers.add(this.players.get((this.players.indexOf(this.defender) + 1) % this.players.size()));

		for (AbstractPlayer attacker : this.attackers)
			attacker.setIsAttacker(true);
	}

	public HashSet<Integer> getNumbersOnTable()
	{
		return this.numbersOnTable;
	}

	public List<Card> getNotYetDefeatedCards()
	{
		ArrayList<Card> notYetDefendedCards = new ArrayList<Card>();

		for (Card card : this.cardsOfAttackerOneOnTable)
			if (!defendedCards.containsKey(card))
				notYetDefendedCards.add(card);

		if (!this.cardsOfAttackerTwoOnTable.isEmpty())
			for (Card card : this.cardsOfAttackerTwoOnTable)
				if (!defendedCards.containsKey(card))
					notYetDefendedCards.add(card);

		return Collections.unmodifiableList(notYetDefendedCards);
	}

	/**
	 * 
	 * @return <code>true</code> if the game is over
	 */
	public boolean isGameOver()
	{
		return this.players.size() <= 1;
	}

	/**
	 * Get the list of the winners
	 * 
	 * @return The list of the winners
	 */
	public List<AbstractPlayer> getWinners()
	{
		return Collections.unmodifiableList(this.winners);
	}

	/**
	 * 
	 * @return The rules
	 */
	public Rules getRules()
	{
		return this.durak.getRules();
	}
}
