package game.ai;

import game.AbstractPlayer;
import game.Card;
import game.Durak;

public abstract class AbstractAi extends AbstractPlayer
{
	public AbstractAi(Durak durak, String name)
	{
		super(durak, name);
	}

	public abstract boolean wantsToPlayAnotherCard();

	public abstract Card getNextAttackCard();

	public abstract Card[] getNextDefendCard();
}
