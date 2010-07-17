package gui.listeners;

import game.AbstractPlayer;
import game.Card;
import game.Durak;
import game.Player;
import game.Rules;
import game.Table;
import game.ai.AbstractAi;
import game.ai.DefendCard;
import game.ai.PabloAi;
import game.ai.SimpleAi;
import gui.DurakWindow;
import gui.WelcomePanel.ComputerAi;
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
	public static final int ACTION_UPDATE_HAND_PANEL = 7;

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

		case ACTION_UPDATE_HAND_PANEL:
			this.durakWindow.getDurakPanel().getHandPanel().updateDisplay(this.durak.getTable().getPlayers().get(0), this.durak.getTable());
			break;
		}
	}

	private void newGame()
	{
		String playerName = this.durakWindow.getWelcomePanel().getPlayerName();
		int numberOfCards = this.durakWindow.getWelcomePanel().getNumberOfCards();
		boolean isHelpWanted = this.durakWindow.getWelcomePanel().isHelpWanted();

		ArrayList<AbstractPlayer> players = new ArrayList<AbstractPlayer>();

		players.add(new Player(this.durak, playerName));

		for (ComputerAi computer : this.durakWindow.getWelcomePanel().getComputers())
		{
			switch (computer.getType())
			{
			case EASY:
			case NORMAL:
			case HARD:
				players.add(new SimpleAi(this.durak, computer.toString()));
				break;
			case PABLO:
				players.add(new PabloAi(this.durak, computer.toString()));
				break;
			}
		}
		this.durak.newGame(players, new Rules(numberOfCards));
		this.durak.getRules().setDoHelp(isHelpWanted);

		this.durakWindow.showDurakPanel();
		this.durakWindow.newGame(this.durak.getTable());

		this.nextMove();
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

		if (this.durak.getTable().isGameOver())
			this.notifyGameHasEnded();

		AbstractPlayer player = table.getPlayers().get(0);
		if (player instanceof Player)
		{
			if (this.durak.getTable().getAttackers().contains(player) || this.durak.getTable().getDefender().equals(player))
				this.durakWindow.getDurakPanel().getHandPanel().setEnabled(true);
			else
				this.durakWindow.getDurakPanel().getHandPanel().setEnabled(false);
		}

		this.durakWindow.getDurakPanel().getHandPanel().updateDisplay(table.getPlayers().get(0), this.durak.getTable());
		this.durakWindow.getDurakPanel().getTablePanel().updateDisplay(table.getCardsOfAttackerOneOnTable(), table.getCardsOfAttackerTwoOnTable(), table.getDefendedCards(), table.getAttackers());
		this.durakWindow.getDurakPanel().getDeckPanel().updateDisplay(table.getDeck(), table.getPlayers(), table.getWinners());

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

			// TODO what about winning ?
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

	private void notifyGameHasEnded()
	{
		String winningText = "";

		if (this.durak.getTable().getPlayers().size() == 1)
		{
			AbstractPlayer durakPlayer = null;
			durakPlayer = this.durak.getTable().getPlayers().get(0);

			int place = 1;
			for (AbstractPlayer player : this.durak.getTable().getWinners())
			{
				winningText += player.getName() + " scored " + place;

				switch (place)
				{
				case 1:
					winningText += "st\n";
					break;
				case 2:
					winningText += "nd\n";
					break;
				case 3:
					winningText += "rd\n";
					break;
				default:
					winningText += "th\n";
				}

				place++;
			}

			winningText += durakPlayer.getName() + " is the durak!";
		}
		Object[] options = { "quit game", "new game" };
		int n = JOptionPane.showOptionDialog(this.durakWindow, winningText, "Game Over!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

		if (n == JOptionPane.YES_OPTION)
			this.quit();
		else
			this.showSettings();
	}

	private void endTurn()
	{
		if (!this.durak.getTable().isGameOver())
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
				DefendCard defense = computer.getNextDefendCard();
				if (defense != null)
					this.durak.getTable().defend(defense.getCardToBeDefeated(), defense.getCardDefendingWith());
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

	public DurakWindow getDurakWindow()
	{
		return this.durakWindow;
	}
}
