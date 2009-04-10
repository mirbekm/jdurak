package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Table
{
	private Deck deck = null;

	private ArrayList<Player> players = null;

	private Player defender = null;
	private ArrayList<Player> attackers = null;
	private Player activePlayer = null;
	private Durak durak;

	private ArrayList<Card> cardsOfAttackerOneOnTable;
	private ArrayList<Card> cardsOfAttackerTwoOnTable;
	private ArrayList<Card> cardsOfDefender;
	private HashMap<Card, Card> defendedCards;
	private HashSet<Integer> numbersOnTable;

	public Table(ArrayList<Player> players, Durak durak)
	{
		this.durak = durak;
		this.players = players;

		this.newGame();
	}

	public void newGame()
	{
		this.attackers = new ArrayList<Player>();

		this.deck = new Deck(this.durak.getRules().getNumberOfCardsPerSuit());
		this.activePlayer = this.players.get(0);

		this.attackers.add(this.players.get(0));
		this.players.get(0).setIsAttacker(true);

		this.defender = this.players.get(1);
		this.players.get(1).setIsAttacker(false);

		this.resetLists();
	}

	private void resetLists()
	{
		this.cardsOfAttackerOneOnTable = new ArrayList<Card>();
		this.cardsOfAttackerTwoOnTable = new ArrayList<Card>();
		this.cardsOfDefender = new ArrayList<Card>();
		this.defendedCards = new HashMap<Card, Card>();
		this.numbersOnTable = new HashSet<Integer>();
	}

	public ArrayList<Card> getCardsOfAttackerOneOnTable()
	{
		return this.cardsOfAttackerOneOnTable;
	}

	public ArrayList<Card> getCardsOnTableOfAttackerTwo()
	{
		if (this.attackers.size() > 1)
			return this.cardsOfAttackerTwoOnTable;
		else
			return null;
	}

	public HashMap<Card, Card> getDefendedCards()
	{
		return this.defendedCards;
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

	public void attack(Card attackingCard)
	{
		assert (attackingCard != null);

		if (this.numbersOnTable.contains(attackingCard.getNumber()) || this.numbersOnTable.isEmpty())
		{
			Player attackingPlayer = null;
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

	public boolean canAttackWithThisCard(Card card)
	{
		//		return true;
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

	public ArrayList<Player> getPlayers()
	{
		return this.players;
	}

	public Player getActivePlayer()
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
		//TODO choose right player
		//TODO game is for 2 players only atm

		if (this.defendedCards.size() == this.cardsOfAttackerOneOnTable.size() + this.cardsOfAttackerTwoOnTable.size())
		{
			this.resetLists();

			int numberOfActivePlayer = this.players.indexOf(this.activePlayer);
			this.activePlayer = this.players.get((numberOfActivePlayer + 1) % this.players.size());

			this.defender = this.players.get(numberOfActivePlayer);
			this.activePlayer.setIsAttacker(true);
			this.defender.setIsAttacker(false);

			this.attackers.clear();
			this.attackers.add(this.activePlayer);

			System.out.println("defender has won");
			System.out.println("new active player: " + this.activePlayer.getName() + " is Attacker " + this.activePlayer.isAttacker());
		}
		else
		{
			this.defender.getHand().addAll(this.cardsOfDefender);
			this.defender.getHand().addAll(this.cardsOfAttackerOneOnTable);
			this.defender.getHand().addAll(this.cardsOfAttackerTwoOnTable);

			this.resetLists();

			int numberOfActivePlayer = this.players.indexOf(this.activePlayer);
			this.activePlayer = this.players.get((numberOfActivePlayer + 2) % this.players.size());

			System.out.println("defender has lost:");
			System.out.println("new active player: " + this.activePlayer.getName() + " is Attacker " + this.activePlayer.isAttacker());
		}

		for (Player player : this.attackers)
			player.fillUp();
		this.defender.fillUp();
	}
}
