package gui.game;

import game.Card;
import game.Player;
import gui.helpers.CardManager;
import gui.helpers.Colors;
import gui.libs.JTiledPanel;
import gui.listeners.DurakActionListener;
import gui.listeners.TableDropTargetListenerAsAttacker;
import gui.listeners.TableDropTargetListenerAsDefender;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class TablePanel extends JTiledPanel
{
	public static final int WIDTH = 675;
	public static final int HEIGHT = 465;

	private JLayeredPane layeredPanelAttackerOne;
	private JLayeredPane layeredPanelAttackerTwo;
	private JScrollPane panelAttackerOne;
	private JScrollPane panelAttackerTwo;

	private ArrayList<Card> cardsFromAttackerOne;
	private ArrayList<Card> cardsFromAttackerTwo;
	private HashMap<Card, Card> defendedCards;

	private DurakActionListener actionListener;

	private DropTarget dropTargetPanelAttackerOne;

	private Player activePlayer;

	public TablePanel(DurakActionListener actionListener)
	{
		super(new ImageIcon(Object.class.getResource("/images/baize.png")).getImage());

		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		this.setLayout(new GridBagLayout());
		this.actionListener = actionListener;

		this.layeredPanelAttackerOne = new JLayeredPane();
		this.layeredPanelAttackerTwo = new JLayeredPane();

		this.setVisible(true);
	}

	public void endTurn()
	{
		this.layeredPanelAttackerOne.removeAll();
		this.layeredPanelAttackerOne.repaint();
		this.layeredPanelAttackerTwo.removeAll();
		this.layeredPanelAttackerTwo.repaint();
	}

	public void newGame(ArrayList<Player> players, final Player activePlayer)
	{
		this.activePlayer = activePlayer;

		this.panelAttackerOne = new JScrollPane(this.layeredPanelAttackerOne, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
		{
			private static final long serialVersionUID = 2966561330804255184L;

			@Override
			public void repaint()
			{
				super.repaint();
				updateDisplay(cardsFromAttackerOne, cardsFromAttackerTwo, defendedCards, activePlayer);
			}
		};
		this.panelAttackerTwo = new JScrollPane(this.layeredPanelAttackerTwo, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
		{
			private static final long serialVersionUID = -5128878109927587432L;

			@Override
			public void repaint()
			{
				super.repaint();
				updateDisplay(cardsFromAttackerOne, cardsFromAttackerTwo, defendedCards, activePlayer);
			}
		};

		panelAttackerOne.getViewport().setOpaque(false);
		panelAttackerOne.setBackground(Colors.LIGHT_GREEN);
		panelAttackerTwo.setBackground(Colors.LIGHT_GREEN);
		panelAttackerTwo.getViewport().setOpaque(false);

		// TODO add Drop Target ?
		this.dropTargetPanelAttackerOne = new DropTarget(this.panelAttackerOne, new TableDropTargetListenerAsAttacker(this.actionListener, this.panelAttackerOne));
		this.dropTargetPanelAttackerOne.setActive(false);

		//TODO show correct name
		TitledBorder borderPanelAttackerOne = new TitledBorder(new LineBorder(Colors.DARK_GREEN), "Cards from Player 1");
		TitledBorder borderPanelAttackerTwo = new TitledBorder(new LineBorder(Colors.DARK_GREEN), "Cards from Computer 2");

		panelAttackerOne.setViewportBorder(borderPanelAttackerOne);
		panelAttackerTwo.setViewportBorder(borderPanelAttackerTwo);

		this.add(panelAttackerOne, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		if (players.size() > 2)
			this.add(panelAttackerTwo, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
	}

	public void updateDisplay(ArrayList<Card> cardsFromAttackerOne, ArrayList<Card> cardsFromAttackerTwo, HashMap<Card, Card> defendedCards, Player activePlayer)
	{
		this.cardsFromAttackerOne = cardsFromAttackerOne;
		this.cardsFromAttackerTwo = cardsFromAttackerTwo;
		this.defendedCards = defendedCards;
		this.activePlayer = activePlayer;

		if (this.dropTargetPanelAttackerOne != null)
		{
			if (activePlayer != null && activePlayer.isAttacker())
				this.dropTargetPanelAttackerOne.setActive(true);
			else
				this.dropTargetPanelAttackerOne.setActive(false);
		}

		if (cardsFromAttackerOne != null && cardsFromAttackerOne.size() > 0)
		{
			this.layeredPanelAttackerOne.removeAll();
			int xOffset = CardManager.CARD_WIDTH + 4;

			int cardsAttackerOne = cardsFromAttackerOne.size();
			int cardsPerRow = (this.panelAttackerOne.getWidth() - 10) / (xOffset);
			int rows = (this.panelAttackerOne.getHeight() - 10) / (CardManager.CARD_HEIGHT + 20);
			int rowSpacing = (this.panelAttackerOne.getHeight() - 10) - rows * CardManager.CARD_HEIGHT;

			if (cardsAttackerOne > cardsPerRow * rows)
			{
				if (cardsAttackerOne % rows == 0)
					cardsPerRow = cardsAttackerOne / rows;
				else
					cardsPerRow = (cardsAttackerOne / rows) + 1;
			}

			int actualCardsPerRow = 0;
			int actualRow = 0;
			for (int cardNumber = 0; cardNumber < cardsFromAttackerOne.size(); cardNumber++)
			{
				Card card = cardsFromAttackerOne.get(cardNumber);
				GuiCard guiCard = new GuiCard(card);
				guiCard.setCursor(Cursor.getDefaultCursor());

				if (actualCardsPerRow >= cardsPerRow)
				{
					actualCardsPerRow = 0;
					actualRow++;
				}

				guiCard.setBounds((actualCardsPerRow) * (xOffset), actualRow * (CardManager.CARD_HEIGHT + rowSpacing / rows), CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);

				GuiCard cardDefendedWith = null;
				if (defendedCards.containsKey(card))
				{
					cardDefendedWith = new GuiCard(defendedCards.get(card));
					cardDefendedWith.setBounds((actualCardsPerRow) * (xOffset), (actualRow * (CardManager.CARD_HEIGHT + rowSpacing / rows)) + 35, CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);
					cardDefendedWith.setCursor(Cursor.getDefaultCursor());
				}
				else
				{
					if (!this.activePlayer.isAttacker())
						new DropTarget(guiCard, new TableDropTargetListenerAsDefender(actionListener, guiCard));
				}

				actualCardsPerRow++;

				this.layeredPanelAttackerOne.add(guiCard, 0);

				if (cardDefendedWith != null)
					this.layeredPanelAttackerOne.add(cardDefendedWith, 0);
			}

			this.layeredPanelAttackerOne.setPreferredSize(new Dimension((cardsPerRow * CardManager.CARD_WIDTH) + (cardsPerRow * 4), this.layeredPanelAttackerOne.getHeight()));
			this.layeredPanelAttackerOne.revalidate();
		}

		if (cardsFromAttackerTwo != null)
		{
			// TODO go code that freakin AI you lazy bastard
		}

		// TODO defeat me some cards baby
	}

}
