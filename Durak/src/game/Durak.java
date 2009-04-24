package game;

import java.util.ArrayList;

public class Durak
{
	private Rules rules;
	private Table table;

	public void newGame(ArrayList<Player> players, Rules rules)
	{
		assert (players != null && !players.isEmpty());
		assert (rules != null);

		this.rules = rules;
		this.table = new Table(players, this);
	}

	public Rules getRules()
	{
		return this.rules;
	}

	public Table getTable()
	{
		return this.table;
	}
}
