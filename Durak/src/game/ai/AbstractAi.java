package game.ai;

import game.AbstractPlayer;
import game.Card;
import game.Durak;

/**
 * The abstract Ai. All AIs derive from this class
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * 
 */
public abstract class AbstractAi extends AbstractPlayer
{
	/**
	 * Creates a new AI. See the {@link SimpleAi} for some hints how to implement an AI.
	 * 
	 * @param durak
	 *            The reference to the durak game. Not <code>null</code>!
	 * @param name
	 *            The name of the AI. Neither <code>null</code> nor empty.
	 */
	public AbstractAi(Durak durak, String name)
	{
		super(durak, name);
	}

	/**
	 * Return <code>true</code> if the AI wants to play another card. Return <code>false</code> if the AI doesn't want or is not able to play another card.
	 * 
	 * @return <code>true</code> if the AI wants to play another card. <code>false</code> if the AI is not able or doesn't want to play another card.
	 */
	public abstract boolean wantsToPlayAnotherCard();

	/**
	 * Implement your attack strategy here.
	 * 
	 * @return Return the next attack card or <code>null</code> if the AI doesn't have an appropriate card
	 */
	public abstract Card getNextAttackCard();

	/**
	 * Implement your defending strategy here
	 * 
	 * @return The next defend card or <code>null</code> if the AI cannot defend.
	 */
	public abstract DefendCard getNextDefendCard();

}
