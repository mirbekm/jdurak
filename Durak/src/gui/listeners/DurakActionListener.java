package gui.listeners;

import game.Card;
import game.Durak;
import game.Player;
import game.Rules;
import game.Table;
import gui.DurakWindow;
import gui.game.GuiCard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DurakActionListener implements ActionListener
{
	public static final int ACTION_NEW_GAME = 0;
	public static final int ACTION_QUIT = 1;
	public static final int ACTION_SHOW_SETTINGS = 2;

	public static final int ACTION_END_TURN = 3;

	public static final int ACTION_SWITCH_PLAYER = 4;

	private Durak durak;
	private DurakWindow durakWindow;

	public DurakActionListener(Durak durak, DurakWindow durakWindow)
	{
		this.durak = durak;
		this.durakWindow = durakWindow;
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent)
	{
		int action = Integer.parseInt(actionEvent.getActionCommand());

		switch (action)
		{
		case ACTION_NEW_GAME:
			this.newGame();
			break;

		case ACTION_QUIT:
			this.quit();
			break;

		case ACTION_SHOW_SETTINGS:
			this.showSettings();
			break;

		case ACTION_SWITCH_PLAYER:
			this.switchPlayer();
			break;

		case ACTION_END_TURN:
			this.endTurn();
			break;
		}
	}

	private void newGame()
	{
		String playerName = this.durakWindow.getWelcomePanel().getPlayerName();
		int numberOfCards = this.durakWindow.getWelcomePanel().getNumberOfCards();

		ArrayList<Player> players = new ArrayList<Player>();

		players.add(new Player(this.durak, playerName));
		players.add(new Player(this.durak, "Computer 1"));

		this.durak.newGame(players, new Rules(numberOfCards));
		this.durakWindow.showDurakPanel();
		this.durakWindow.newGame(this.durak.getTable());
	}

	private void quit()
	{
		this.durakWindow.dispose();
		System.exit(0);
	}

	private void showSettings()
	{
		this.durakWindow.showWelcomePanel();
	}

	public void attackWith(GuiCard guiCard)
	{
		assert (guiCard != null);

		this.attackWith(guiCard.getCard());
	}

	private void switchPlayer()
	{
		this.durak.getTable().switchPlayer();
		this.updateDisplay();
	}

	private void updateDisplay()
	{
		Table table = this.durak.getTable();

		this.durakWindow.getDurakPanel().getHandPanel().updateDisplay(table.getActivePlayer());
		this.durakWindow.getDurakPanel().getTablePanel().updateDisplay(table.getCardsOfAttackerOneOnTable(), table.getCardsOfAttackerTwoOnTable(), table.getDefendedCards(), table.getActivePlayer());
		this.durakWindow.getDurakPanel().getDeckPanel().updateDisplay(this.durak.getTable().getDeck(), this.durak.getTable().getPlayers());
	}

	private void endTurn()
	{
		this.durak.getTable().endTurn();
		this.updateDisplay();
		this.durakWindow.getDurakPanel().getTablePanel().endTurn();
	}

	public boolean canDefendWithCard(Card defendingCard, Card cardToBeDefeated)
	{
		return this.durak.getTable().canDefendWithCard(defendingCard, cardToBeDefeated);
	}

	public boolean canAttackWithThisCard(Card card)
	{
		return this.durak.getTable().canAttackWithThisCard(card);
	}

	public void attackWith(Card card)
	{
		assert (card != null);

		if (this.durak.getTable().canAttackWithThisCard(card))
		{
			this.durak.getTable().attack(card);
			this.updateDisplay();
		}
	}

	public void defendCard(Card cardDefendingWith, Card cardToBeDefeated)
	{
		this.durak.getTable().defend(cardToBeDefeated, cardDefendingWith);
		this.updateDisplay();
	}
}
