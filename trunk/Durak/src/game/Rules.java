package game;

public class Rules
{
	private int numberOfCardsPerSuit = 9;
	private int numberOfCardsPerPlayer = 16;
	private boolean transferringAllowed = false;

	public Rules(int numberOfCards)
	{
		this.setNumberOfCards(numberOfCards);
	}

	public int getNumberOfCards()
	{
		return numberOfCardsPerSuit * 4;
	}

	public void setNumberOfCards(int numberOfCards)
	{
		assert (numberOfCards % 4 == 0);
		assert (numberOfCards >= 36 && numberOfCards <= 52);

		this.numberOfCardsPerSuit = numberOfCards / 4;
	}

	public int getNumberOfCardsPerSuit()
	{
		return numberOfCardsPerSuit;
	}

	public int getNumberOfCardsPerPlayer()
	{
		return numberOfCardsPerPlayer;
	}

	public void setNumberOfCardsPerPlayer(int numberOfCardsPerPlayer)
	{
		assert (numberOfCardsPerPlayer >= 6 && numberOfCardsPerPlayer <= 10);

		this.numberOfCardsPerPlayer = numberOfCardsPerPlayer;
	}

	public boolean isTransferringAllowed()
	{
		return transferringAllowed;
	}

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
