package game.ai;

import game.Card;

public interface AiInterface
{
	public boolean wantsToPlayAnotherCard();

	public Card getNextAttackCard();
}
