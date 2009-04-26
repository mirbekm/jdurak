package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
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
	private AbstractPlayer activePlayer = null;
	private Durak durak;

	private ArrayList<Card> cardsOfAttackerOneOnTable;
	private ArrayList<Card> cardsOfAttackerTwoOnTable;
	private ArrayList<Card> cardsOfDefender;
	private HashMap<Card, Card> defendedCards;
	private HashSet<Integer> numbersOnTable;

	public Table(ArrayList<AbstractPlayer> players, Durak durak)
	{
		this.durak = durak;
		this.players = players;

		this.newGame();
	}

	public void newGame()
	{
		this.attackers = new ArrayList<AbstractPlayer>();

		this.deck = new Deck(this.durak.getRules().getNumberOfCardsPerSuit());
		this.activePlayer = this.players.get(0);

		//TODO lowest trump to start
		this.attackers.add(this.players.get(0));
		this.players.get(0).setIsAttacker(true);

		this.defender = this.players.get(1);
		this.players.get(1).setIsAttacker(false);

		this.resetLists();

		for (AbstractPlayer player : players)
			player.newGame(this.deck);
	}

	private void resetLists()
	{
		this.cardsOfAttackerOneOnTable = new ArrayList<Card>();
		this.cardsOfAttackerTwoOnTable = new ArrayList<Card>();
		this.cardsOfDefender = new ArrayList<Card>();
		this.defendedCards = new HashMap<Card, Card>();
		this.numbersOnTable = new HashSet<Integer>();
	}

	public List<Card> getCardsOfAttackerOneOnTable()
	{
		return Collections.unmodifiableList(this.cardsOfAttackerOneOnTable);
	}

	public List<Card> getCardsOfAttackerTwoOnTable()
	{
		if (this.attackers.size() > 1)
			return Collections.unmodifiableList(this.cardsOfAttackerTwoOnTable);
		else
			return null;
	}

	public Map<Card, Card> getDefendedCards()
	{
		return Collections.unmodifiableMap(this.defendedCards);
	}

	public void defend(Card cardToBeDefeated, Card cardDefendingWith)
	{
		assert (cardToBeDefeated != null & cardDefendingWith != null);

		if (!defendedCards.containsKey(cardToBeDefeated))
		{
			if (cardDefendingWith.isGreaterThan(cardToBeDefeated))
			{
				this.defender.getHand().remove(cardDefendingWith);
				this.numbersOnTable.add(cardDefendingWith.getNumber());
				this.cardsOfDefender.add(cardDefendingWith);
				defendedCards.put(cardToBeDefeated, cardDefendingWith);
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

	@Deprecated
	public void attack(Card attackingCard)
	{
		assert (attackingCard != null);

		if (this.numbersOnTable.contains(attackingCard.getNumber()) || this.numbersOnTable.isEmpty())
		{
			AbstractPlayer attackingPlayer = null;
			ArrayList<Card> cardsOfAttacker = null;

			if (attackers.get(0).getHand().contains(attackingCard))
			{
				attackingPlayer = attackers.get(0);
				cardsOfAttacker = this.cardsOfAttackerOneOnTable;
			}
			else if (attackers.size() > 1 && attackers.get(1).getHand().contains(attackingCard))
			{
				attackingPlayer = attackers.get(1);
				cardsOfAttacker = this.cardsOfAttackerTwoOnTable;
			}
			else
			{
				// TODO the card has no owner
			}

			attackingPlayer.getHand().remove(attackingCard);
			cardsOfAttacker.add(attackingCard);

			if (this.numbersOnTable.isEmpty())
				this.numbersOnTable.add(attackingCard.getNumber());
		}
		else
		{
			// TODO the card cannot be played because there is no card by this number
		}
	}

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

	public boolean canAttackWithThisCard(Card card)
	{
		return this.numbersOnTable.contains(card.getNumber()) || this.numbersOnTable.isEmpty();
	}

	public boolean canDefendWithCard(Card defendingCard, Card cardToBeDefeated)
	{
		return defendingCard.isGreaterThan(cardToBeDefeated) && !this.defendedCards.containsKey(cardToBeDefeated);
	}

	public Deck getDeck()
	{
		return this.deck;
	}

	public List<AbstractPlayer> getPlayers()
	{
		return Collections.unmodifiableList(this.players);
	}

	public List<AbstractPlayer> getAttackers()
	{
		return Collections.unmodifiableList(attackers);
	}

	public AbstractPlayer getActivePlayer()
	{
		return this.activePlayer;
	}

	public void switchPlayer()
	{
		int numberOfActivePlayer = this.players.indexOf(this.activePlayer);
		this.activePlayer = this.players.get((numberOfActivePlayer + 1) % this.players.size());
	}

	public void endTurn()
	{
		//TODO choose right player - done?
		//TODO game is for 2 players only atm

		if (this.defendedCards.size() == this.cardsOfAttackerOneOnTable.size() + this.cardsOfAttackerTwoOnTable.size())
		{
			// defender has won
			this.resetLists();

			int numberOfActivePlayer = this.players.indexOf(this.activePlayer);
			this.activePlayer = this.players.get((numberOfActivePlayer + 1) % this.players.size());

			this.defender = this.players.get(numberOfActivePlayer);
			this.activePlayer.setIsAttacker(true);
			this.defender.setIsAttacker(false);

			this.attackers.clear();
			this.attackers.add(this.activePlayer);
			// TODO add more attackers here
		}
		else
		{
			// defender has lost
			this.defender.getHand().addAll(this.cardsOfDefender);
			this.defender.getHand().addAll(this.cardsOfAttackerOneOnTable);
			this.defender.getHand().addAll(this.cardsOfAttackerTwoOnTable);

			this.resetLists();

			int numberOfActivePlayer = this.players.indexOf(this.activePlayer);
			this.activePlayer = this.players.get((numberOfActivePlayer + 2) % this.players.size());
		}

		for (AbstractPlayer player : this.players)
			player.fillUp(this.deck);
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

		// TODO add player two
		if (!this.cardsOfAttackerTwoOnTable.isEmpty())
			System.err.println("see TODO!");

		return Collections.unmodifiableList(notYetDefendedCards);
	}
}
