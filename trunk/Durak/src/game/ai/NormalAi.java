package game.ai;

import game.Card;
import game.Durak;

public class NormalAi extends AbstractAi
{

	public NormalAi(Durak durak, String name)
	{
		super(durak, name);
	}

	@Override
	public Card getNextAttackCard()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DefendCard getNextDefendCard()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean wantsToPlayAnotherCard()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
