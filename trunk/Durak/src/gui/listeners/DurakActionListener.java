package gui.listeners;

import game.AbstractPlayer;
import game.Card;
import game.Durak;
import game.Player;
import game.Rules;
import game.Table;
import game.ai.AbstractAi;
import game.ai.SimpleAi;
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

	public static final int ACTION_NEXT_MOVE = 5;

	public static final int ACTION_UPDATE_DISPLAY = 6;

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

		case ACTION_NEXT_MOVE:
			this.nextMove();
			break;

		case ACTION_UPDATE_DISPLAY:
			this.updateDisplay();
			break;
		}
	}

	private void newGame()
	{
		String playerName = this.durakWindow.getWelcomePanel().getPlayerName();
		int numberOfCards = this.durakWindow.getWelcomePanel().getNumberOfCards();

		ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();

		players.add(new Player(this.durak, playerName));
		//		players.add(new Player(this.durak, "Computer 1")); TODO uncomment for hot seat game
		players.add(new SimpleAi(this.durak, "Computer 1"));

		// TODO add more computers

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

		this.durakWindow.getDurakPanel().getHandPanel().updateDisplay(this.durak.getTable().getPlayers().get(0)); // TODO change to getActivePlayerfor HotSeat
		this.durakWindow.getDurakPanel().getTablePanel().updateDisplay(table.getCardsOfAttackerOneOnTable(), table.getCardsOfAttackerTwoOnTable(), table.getDefendedCards(), this.durak.getTable().getAttackers());
		this.durakWindow.getDurakPanel().getDeckPanel().updateDisplay(this.durak.getTable().getDeck(), this.durak.getTable().getPlayers());

		this.updateButtons();

		// TODO 1on1 only atm
		AbstractAi computer = (AbstractAi) this.durak.getTable().getPlayers().get(1);
		if (this.durakWindow.getDurakPanel().getTurnPanel().autoReply() && computer.wantsToPlayAnotherCard())
			this.nextMove();

		this.durakWindow.repaint();
	}

	private void updateButtons()
	{
		//TODO only 1on1
		AbstractAi computer = (AbstractAi) this.durak.getTable().getPlayers().get(1);
		AbstractPlayer player = this.durak.getTable().getPlayers().get(0);

		if (player.isAttacker() && this.durak.getTable().getNumbersOnTable().isEmpty())
		{
			this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(false);
			this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(false);

			//TODO what about winning ?
		}
		else
		{
			if (player.isAttacker())
			{
				if (computer.wantsToPlayAnotherCard())
				{
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(false);
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(true);
				}
				else
				{
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(true);
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(false);
				}
			}
			else
			{
				if (this.durak.getTable().getNumbersOnTable().isEmpty())
				{
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(false);
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(true);
				}
				else
				{
					if (this.durak.getTable().getNotYetDefeatedCards().isEmpty())
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setText("end turn");
					else
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setText("end turn - take cards");

					if (computer.wantsToPlayAnotherCard())
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(false);
					else
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(true);

					if (computer.wantsToPlayAnotherCard())
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(true);
					else
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(false);
				}
			}
		}
	}

	private void endTurn()
	{
		//TODO only 1on1

		AbstractAi computer = (AbstractAi) this.durak.getTable().getPlayers().get(1);

		if (computer.wantsToPlayAnotherCard())
		{
			// TODO proper notification
			System.err.println("computer is not finished yet!");
		}
		else
		{
			this.durak.getTable().endTurn();
			this.durakWindow.getDurakPanel().getTablePanel().endTurn();
			this.updateDisplay();
		}
	}

	private void nextMove()
	{
		AbstractAi computer = (AbstractAi) this.durak.getTable().getPlayers().get(1);

		//		ArrayList<Card> hand = computer.getHand();
		//		Collections.sort(hand);
		//		System.out.println("Computer - isAttacker: " + computer.isAttacker());
		//		System.out.println(hand);
		//		System.out.println(computer.wantsToPlayAnotherCard());
		//		Card[] defense = computer.getNextDefendCard();
		//		if (defense != null)
		//			System.out.println(defense[0] + " < " + defense[1]);

		if (computer.isAttacker())
		{
			while (computer.wantsToPlayAnotherCard())
			{
				Card attackCard = computer.getNextAttackCard();
				if (attackCard != null)
					computer.attackWith(attackCard);
				else
					System.err.println("problem with attack method in ai"); // TODO proper error message
			}
		}
		else
		{
			while (computer.wantsToPlayAnotherCard())
			{
				Card[] defense = computer.getNextDefendCard();
				if (defense != null && defense.length == 2)
					this.durak.getTable().defend(defense[0], defense[1]);
				else
					System.err.println("problem with defense method in ai"); // TODO proper error
			}
		}

		this.updateDisplay();
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
			this.durak.getTable().getActivePlayer().attackWith(card);
			this.updateDisplay();
		}
	}

	public void defendCard(Card cardDefendingWith, Card cardToBeDefeated)
	{
		this.durak.getTable().defend(cardToBeDefeated, cardDefendingWith);
		this.updateDisplay();
	}
}
