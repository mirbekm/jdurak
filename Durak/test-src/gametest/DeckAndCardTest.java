package gametest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import game.Card;
import game.Deck;
import game.Rules;

import org.junit.Before;
import org.junit.Test;

public class DeckAndCardTest
{
	private Deck deck;

	@Before
	public void setUp()
	{
		this.deck = new Deck(Rules.MAX_AMOUNT_OF_CARDS_PER_SUIT);
	}

	@Test
	public void testSize()
	{
		for (int i = 0; i < Rules.MAX_AMOUNT_OF_CARDS; i++)
			assertNotNull(this.deck.getCard());

		assertNull(this.deck.getCard());
	}

	@Test
	public void testCardComparison()
	{
		int notTrumpSuit = (Deck.trumpSuit + 2) % 4;
		Card lowerCard = new Card(notTrumpSuit, 2);
		Card greaterCard = new Card(notTrumpSuit, 10);

		assertTrue(greaterCard.isGreaterThan(lowerCard));

		Card otherCard = new Card(notTrumpSuit, 10);

		assertEquals(0, greaterCard.compareTo(otherCard));

		assertTrue(new Card(Deck.trumpSuit, 2).isGreaterThan(greaterCard));
		assertTrue(new Card(Deck.trumpSuit, 10).isGreaterThan(greaterCard));
		assertTrue(new Card(Deck.trumpSuit, 14).isGreaterThan(greaterCard));

		lowerCard = new Card(notTrumpSuit, 7);
		greaterCard = new Card(notTrumpSuit, 8);

		assertTrue(greaterCard.isGreaterThan(lowerCard));

		greaterCard = new Card(notTrumpSuit, 14);
		otherCard = new Card((Deck.trumpSuit + 3) % 4, 2);

		assertTrue(greaterCard.compareTo(otherCard) < 0);

		assertTrue(new Card(notTrumpSuit, 7).compareTo(new Card(Deck.trumpSuit, 7)) < 0);

	}

	@Test
	public void testRemainingCards()
	{
		assertEquals(Rules.MAX_AMOUNT_OF_CARDS, this.deck.getRemainingCards());

		for (int i = 0; i < Rules.MAX_AMOUNT_OF_CARDS - 1; i++)
			this.deck.getCard();

		assertEquals(1, this.deck.getRemainingCards());

		assertSame(this.deck.getLastCard(), this.deck.getCard());

		assertEquals(0, this.deck.getRemainingCards());
	}
}
