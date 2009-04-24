package gametest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import game.Card;
import game.Deck;
import game.Durak;
import game.Player;
import game.Rules;
import game.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TableTest
{
	private Table table;
	private Durak durak;
	private ArrayList<Player> players;
	private Rules rules;
	private final int SIZE_OF_DECK = 52;

	@Before
	public void setUpBeforeClass()
	{
		this.durak = new Durak();
		this.players = new ArrayList<Player>();
		this.players.add(new Player(durak, "Player One"));
		this.players.add(new Player(durak, "Player Two"));
		this.durak.newGame(players, rules = new Rules(SIZE_OF_DECK));
		this.table = this.durak.getTable();

		this.table.newGame();
	}

	@Test
	public void testAllListsEmpty()
	{
		assertTrue(this.table.getCardsOfAttackerOneOnTable().isEmpty());
		assertNull(this.table.getCardsOfAttackerTwoOnTable());
		assertTrue(this.table.getDefendedCards().isEmpty());
		assertTrue(this.table.getNotDefeatedCards().isEmpty());
		assertTrue(this.table.getNumbersOnTable().isEmpty());
	}

	@Test
	public void testPlayerHasRightAmountOfCards()
	{
		int expected = this.rules.getNumberOfCardsPerPlayer() * this.players.size();
		int actual = 0;
		for (Player player : this.players)
			actual += player.getHand().size();

		assertEquals(expected, actual);

		assertEquals(this.rules.getNumberOfCards() - expected, this.table.getDeck().getRemainingCards());
	}

	@Test
	public void testAttackRoundOne()
	{
		int notTrumpSuit = (Deck.trumpSuit + 2) % 4;

		Player attacker = this.players.get(0);

		// no cards on table - attack with 8
		Card lowCard = new Card(notTrumpSuit, 8);
		assertTrue(this.table.canAttackWithThisCard(lowCard));
		this.table.attack(lowCard, attacker);

		// 8 on the table attack with 8 of other suit
		Card otherCard = new Card((Deck.trumpSuit + 3) % 4, 8);
		assertTrue(this.table.canAttackWithThisCard(otherCard));
		this.table.attack(otherCard, attacker);

		// 2x 8 on table, attack with 6
		Card falseCard = new Card(notTrumpSuit, 6);
		assertFalse(this.table.canAttackWithThisCard(falseCard));
	}

	@Test
	public void testDefendRoundOne()
	{
		int suitOne = (Deck.trumpSuit + 2) % 4;
		int suitTwo = (Deck.trumpSuit + 3) % 4;

		Player attacker = this.players.get(0);

		Card attackCardOne = new Card(suitOne, 8);
		Card attackCardTwo = new Card(suitTwo, 8);
		Card attackCardThree = new Card(Deck.trumpSuit, 8);

		this.table.attack(attackCardOne, attacker);
		this.table.attack(attackCardTwo, attacker);
		this.table.attack(attackCardThree, attacker);

		Card defendCardOne = new Card(suitOne, 9);
		Card defendCardTwo = new Card(suitTwo, 14);
		Card defendCardThree = new Card(Deck.trumpSuit, 5);
		Card defendCardFour = new Card(Deck.trumpSuit, 8);
		Card defendCardFive = new Card(Deck.trumpSuit, 10);
		Card falseDefendCard = new Card(suitOne, 4);

		// test card one
		assertTrue(this.table.canDefendWithCard(defendCardOne, attackCardOne));
		assertFalse(this.table.canDefendWithCard(defendCardTwo, attackCardOne));
		assertTrue(this.table.canDefendWithCard(defendCardThree, attackCardOne));
		assertTrue(this.table.canDefendWithCard(defendCardFour, attackCardOne));
		assertTrue(this.table.canDefendWithCard(defendCardFive, attackCardOne));
		assertFalse(this.table.canDefendWithCard(falseDefendCard, attackCardOne));

		// test trump
		assertFalse(this.table.canDefendWithCard(defendCardThree, attackCardThree));
		assertFalse(this.table.canDefendWithCard(defendCardFour, attackCardThree));
		assertTrue(this.table.canDefendWithCard(defendCardFive, attackCardThree));

		assertEquals(3, this.table.getNotDefeatedCards().size());

		this.table.defend(attackCardOne, defendCardOne);

		assertEquals(2, this.table.getNotDefeatedCards().size());

		this.table.defend(attackCardTwo, defendCardTwo);

		assertEquals(1, this.table.getNotDefeatedCards().size());

		this.table.defend(attackCardThree, defendCardFive);

		assertEquals(0, this.table.getNotDefeatedCards().size());

		assertEquals(defendCardOne, this.table.getDefendedCards().get(attackCardOne));
		assertEquals(defendCardTwo, this.table.getDefendedCards().get(attackCardTwo));
		assertEquals(defendCardFive, this.table.getDefendedCards().get(attackCardThree));
	}

	@Test
	public void testFillUp()
	{
		Player attacker = this.players.get(0);

		int originalCardsOnDeck = this.table.getDeck().getRemainingCards();

		Card attackCard = attacker.getHand().get(new Random().nextInt(attacker.getHand().size()));

		ArrayList<Card> possibleCards = new ArrayList<Card>();

		attacker.attackWith(attackCard);

		for (Card card : attacker.getHand())
			if (this.table.canAttackWithThisCard(card))
				possibleCards.add(card);

		for (Card card : possibleCards)
			attacker.attackWith(card);

		int numberOfCards = possibleCards.size() + 1;

		attacker.fillUp(this.table.getDeck());

		assertEquals(originalCardsOnDeck - numberOfCards, this.table.getDeck().getRemainingCards());
	}

	@Test
	public void testAttackRoundTwo()
	{
		int notTrumpSuit1 = (Deck.trumpSuit + 2) % 4;
		int notTrumpSuit2 = (Deck.trumpSuit + 3) % 4;

		Player attacker = this.players.get(0);

		Card attack11 = new Card(notTrumpSuit1, 8);
		Card attack12 = new Card(notTrumpSuit2, 8);

		this.table.attack(attack11, attacker);
		this.table.attack(attack12, attacker);

		Card defend11 = new Card(notTrumpSuit1, 9);
		Card defend12 = new Card(notTrumpSuit2, 10);

		this.table.defend(attack11, defend11);
		this.table.defend(attack12, defend12);

		Card attack21 = new Card(notTrumpSuit1, 10);
		Card attack22 = new Card(notTrumpSuit2, 9);

		assertTrue(this.table.canAttackWithThisCard(attack21));
		assertTrue(this.table.canAttackWithThisCard(attack22));
		assertTrue(this.table.canAttackWithThisCard(new Card(Deck.trumpSuit, 10)));
		assertTrue(this.table.canAttackWithThisCard(new Card(Deck.trumpSuit, 9)));
	}

	@Test
	public void testDefenderWins()
	{
		Player attacker = this.players.get(0);

		Card attackCard = Collections.min(attacker.getHand());
		attacker.attackWith(attackCard);

		Player defender = this.players.get(1);
		Card defendCard = Collections.max(defender.getHand());

		assertTrue(this.table.canDefendWithCard(defendCard, attackCard));
		this.table.defend(attackCard, defendCard);

		assertEquals(this.rules.getNumberOfCardsPerPlayer() - 1, attacker.getHand().size());
		assertEquals(this.rules.getNumberOfCardsPerPlayer() - 1, defender.getHand().size());

		this.table.endTurn();

		assertEquals(this.rules.getNumberOfCardsPerPlayer(), attacker.getHand().size());
		assertEquals(this.rules.getNumberOfCardsPerPlayer(), defender.getHand().size());

		assertEquals(defender, this.table.getActivePlayer());
	}

	@Test
	public void attackerWins()
	{
		Player attacker = this.players.get(0);

		Card attackCard = attacker.getHand().get(new Random().nextInt(attacker.getHand().size()));

		ArrayList<Card> possibleCards = new ArrayList<Card>();

		attacker.attackWith(attackCard);

		for (Card card : attacker.getHand())
			if (this.table.canAttackWithThisCard(card))
				possibleCards.add(card);

		for (Card card : possibleCards)
			attacker.attackWith(card);

		this.table.endTurn();

		Player defender = this.players.get(1);

		int numberOfCardsPlayed = 1 + possibleCards.size();

		assertEquals(this.rules.getNumberOfCardsPerPlayer(), attacker.getHand().size());
		assertEquals(this.rules.getNumberOfCardsPerPlayer() + numberOfCardsPlayed, defender.getHand().size());

		assertEquals(attacker, this.table.getActivePlayer());
	}

}
