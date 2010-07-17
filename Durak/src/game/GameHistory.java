package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * 
 */
public class GameHistory
{
	private ArrayList<Card> cardsOutOfTheGame;
	private HashMap<AbstractPlayer, HashSet<Card>> knownCardsOfPlayer;

	public GameHistory()
	{
		this.cardsOutOfTheGame = new ArrayList<Card>();
		this.knownCardsOfPlayer = new HashMap<AbstractPlayer, HashSet<Card>>();
	}

	public List<Card> getCardsOutOfTheGame()
	{
		return Collections.unmodifiableList(this.cardsOutOfTheGame);
	}

	public Set<Card> getKnownCardsFromPlayer(AbstractPlayer player)
	{
		if (this.knownCardsOfPlayer.get(player) == null)
			return Collections.unmodifiableSet(new HashSet<Card>());

		return Collections.unmodifiableSet(this.knownCardsOfPlayer.get(player));
	}

	public void addDefeatedCards(Collection<Card> defeatedCards)
	{
		this.cardsOutOfTheGame.addAll(defeatedCards);

		for (HashSet<Card> set : this.knownCardsOfPlayer.values())
			if (set != null)
				set.removeAll(defeatedCards);
	}

	public void addCardsToPlayer(Collection<Card> cards, AbstractPlayer player)
	{
		if (this.knownCardsOfPlayer.get(player) == null)
			this.knownCardsOfPlayer.put(player, new HashSet<Card>());

		this.knownCardsOfPlayer.get(player).addAll(cards);
	}

	public void removeCardsFromPlayer(Collection<Card> cards, AbstractPlayer player)
	{
		this.knownCardsOfPlayer.get(player).removeAll(cards);
	}
}
