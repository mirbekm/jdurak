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
import gui.helpers.CardManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class DurakActionListener implements ActionListener
{
	public static final int ACTION_NEW_GAME = 0;
	public static final int ACTION_QUIT = 1;
	public static final int ACTION_SHOW_SETTINGS = 2;
	public static final int ACTION_END_TURN = 3;
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
		players.add(new SimpleAi(this.durak, "Computer 2"));

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

	private void updateDisplay()
	{
		Table table = this.durak.getTable();

		if (this.isGameOver())
			this.notifyGameHasEnded();

		this.durakWindow.getDurakPanel().getHandPanel().updateDisplay(table.getPlayers().get(0)); // TODO change to getActivePlayerfor HotSeat
		this.durakWindow.getDurakPanel().getTablePanel().updateDisplay(table.getCardsOfAttackerOneOnTable(), table.getCardsOfAttackerTwoOnTable(), table.getDefendedCards(), table.getAttackers());
		this.durakWindow.getDurakPanel().getDeckPanel().updateDisplay(table.getDeck(), table.getPlayers());

		this.updateButtons();

		boolean autoReply = this.durakWindow.getDurakPanel().getTurnPanel().autoReply();

		if (autoReply && this.oneOfTheComputersWantsToMove())
			this.nextMove();

		this.durakWindow.repaint();
	}

	private ArrayList<AbstractAi> getComputers()
	{
		ArrayList<AbstractAi> computers = new ArrayList<AbstractAi>();

		for (AbstractPlayer player : this.durak.getTable().getAttackers())
			if (player instanceof AbstractAi)
				computers.add((AbstractAi) player);

		if (this.durak.getTable().getDefender() instanceof AbstractAi)
			computers.add((AbstractAi) this.durak.getTable().getDefender());

		return computers;
	}

	private boolean oneOfTheComputersWantsToMove()
	{
		boolean oneOfComputersWantsToMove = false;

		for (AbstractAi ai : getComputers())
			oneOfComputersWantsToMove |= ai.wantsToPlayAnotherCard();

		return oneOfComputersWantsToMove;
	}

	private void updateButtons()
	{
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
				if (this.oneOfTheComputersWantsToMove())
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

					if (this.oneOfTheComputersWantsToMove())
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(false);
					else
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setVisible(true);

					if (this.oneOfTheComputersWantsToMove())
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(true);
					else
						this.durakWindow.getDurakPanel().getTurnPanel().getBtnNextMove().setVisible(false);
				}

				if (this.durak.getTable().getNotYetDefeatedCards().isEmpty())
				{
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setText("end turn");
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setIcon(CardManager.getImageIcon("images/icons/flag_green.png"));
				}
				else
				{
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setText("end turn - take cards");
					this.durakWindow.getDurakPanel().getTurnPanel().getBtnEndTurn().setIcon(CardManager.getImageIcon("images/icons/arrow_turn_right.png"));
				}
			}
		}
	}

	private boolean isGameOver()
	{
		boolean gameOver = false;

		if (this.durak.getTable().getDeck().hasRemainingCards())
			return false;
		else
			for (AbstractPlayer player : this.durak.getTable().getPlayers())
				if (player.getHand().size() == 0)
					gameOver = true;

		return gameOver && this.durak.getTable().getNotYetDefeatedCards().isEmpty();
	}

	private void notifyGameHasEnded()
	{
		AbstractPlayer winningPlayer = null;
		String winningText = "";

		for (AbstractPlayer player : this.durak.getTable().getPlayers())
			if (player.getHand().size() == 0)
				if (winningPlayer == null)
					winningPlayer = player;
				else
					; // draw ?

		if (winningPlayer.equals(this.durak.getTable().getPlayers().get(0)))
			winningText = "Congratulations! You have won!";
		else
			winningText = "You have lost! " + winningPlayer + " has won the match!";

		Object[] options = { "quit game", "new game" };
		int n = JOptionPane.showOptionDialog(this.durakWindow, winningText, "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (n == JOptionPane.YES_OPTION)
			this.quit();
		else
			this.showSettings();
	}

	private void endTurn()
	{
		//TODO only 1on1

		if (!this.isGameOver())
		{
			if (this.oneOfTheComputersWantsToMove())
			{
				System.err.println("computer is not finished yet!");
			}
			else
			{
				this.durak.getTable().endTurn();
				this.durakWindow.getDurakPanel().getTablePanel().endTurn();
				this.updateDisplay();
			}
		}
		else
			this.notifyGameHasEnded();
	}

	private void nextMove()
	{
		for (AbstractPlayer attacker : this.durak.getTable().getAttackers())
		{
			if (attacker instanceof AbstractAi)
			{
				AbstractAi computer = (AbstractAi) attacker;
				while (computer.wantsToPlayAnotherCard())
				{
					Card attackCard = computer.getNextAttackCard();
					if (attackCard != null)
						computer.attackWith(attackCard);
					else
						System.err.println("problem with attack method in ai"); // TODO proper error message
				}

			}
		}

		if (this.durak.getTable().getDefender() instanceof AbstractAi)
		{
			AbstractAi computer = (AbstractAi) this.durak.getTable().getDefender();

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

		// TODO table.getPlayers().get(0) <- in mp games this should be the correct player not just the first in list

		if (this.durak.getTable().canAttackWithThisCard(card))
		{
			this.durak.getTable().getPlayers().get(0).attackWith(card);
			this.updateDisplay();
		}
	}

	public void defendCard(Card cardDefendingWith, Card cardToBeDefeated)
	{
		this.durak.getTable().defend(cardToBeDefeated, cardDefendingWith);
		this.updateDisplay();
	}
}
