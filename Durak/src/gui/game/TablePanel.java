package gui.game;

import game.AbstractPlayer;
import game.Card;
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
import java.util.List;
import java.util.Map;

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

	private List<Card> cardsFromAttackerOne;
	private List<Card> cardsFromAttackerTwo;
	private Map<Card, Card> defendedCards;

	private DurakActionListener actionListener;

	private DropTarget dropTargetPanelAttackerOne;

	private TitledBorder borderPanelAttackerOne;

	private AbstractPlayer humanPlayer;

	public TablePanel(DurakActionListener actionListener)
	{
		super(CardManager.getImageIcon("images/baize.png").getImage());

		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		this.setLayout(new GridBagLayout());
		this.actionListener = actionListener;

		this.setVisible(true);
	}

	public void endTurn()
	{
		this.layeredPanelAttackerOne.removeAll();
		this.layeredPanelAttackerOne.repaint();
		this.layeredPanelAttackerTwo.removeAll();
		this.layeredPanelAttackerTwo.repaint();
	}

	public void newGame(List<AbstractPlayer> players, final AbstractPlayer activePlayer, final List<AbstractPlayer> attackers)
	{
		this.cardsFromAttackerOne = null;
		this.cardsFromAttackerTwo = null;
		this.defendedCards = null;

		if (this.layeredPanelAttackerOne == null)
			this.layeredPanelAttackerOne = new JLayeredPane();
		else
			this.layeredPanelAttackerOne.removeAll();

		if (this.layeredPanelAttackerTwo == null)
			this.layeredPanelAttackerTwo = new JLayeredPane();
		else
			this.layeredPanelAttackerTwo.removeAll();

		this.humanPlayer = players.get(0);

		if (this.panelAttackerOne == null)
			this.panelAttackerOne = new JScrollPane(this.layeredPanelAttackerOne, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
			{
				private static final long serialVersionUID = 2966561330804255184L;

				@Override
				public void repaint()
				{
					super.repaint();
					updateDisplay(cardsFromAttackerOne, cardsFromAttackerTwo, defendedCards, attackers);
				}
			};

		if (this.panelAttackerTwo == null)
			this.panelAttackerTwo = new JScrollPane(this.layeredPanelAttackerTwo, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
			{
				private static final long serialVersionUID = -5128878109927587432L;

				@Override
				public void repaint()
				{
					super.repaint();
					updateDisplay(cardsFromAttackerOne, cardsFromAttackerTwo, defendedCards, attackers);
				}
			};

		panelAttackerOne.getViewport().setOpaque(false);
		panelAttackerOne.setBackground(Colors.LIGHT_GREEN);
		panelAttackerTwo.setBackground(Colors.LIGHT_GREEN);
		panelAttackerTwo.getViewport().setOpaque(false);

		// TODO add Drop Target ?
		this.dropTargetPanelAttackerOne = new DropTarget(this.panelAttackerOne, new TableDropTargetListenerAsAttacker(this.actionListener, this.panelAttackerOne));
		this.dropTargetPanelAttackerOne.setActive(false);

		this.borderPanelAttackerOne = new TitledBorder(new LineBorder(Colors.DARK_GREEN), "Cards from: " + activePlayer.getName());
		//TODO show correct name
		TitledBorder borderPanelAttackerTwo = new TitledBorder(new LineBorder(Colors.DARK_GREEN), "Cards from Computer 2");

		panelAttackerOne.setViewportBorder(borderPanelAttackerOne);
		panelAttackerTwo.setViewportBorder(borderPanelAttackerTwo);

		this.add(panelAttackerOne, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		if (players.size() > 2)
			this.add(panelAttackerTwo, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

	}

	public void updateDisplay(List<Card> cardsFromAttackerOne, List<Card> cardsFromAttackerTwo, Map<Card, Card> defendedCards, List<AbstractPlayer> attackers)
	{

		this.cardsFromAttackerOne = cardsFromAttackerOne;
		this.cardsFromAttackerTwo = cardsFromAttackerTwo;
		this.defendedCards = defendedCards;

		if (this.dropTargetPanelAttackerOne != null)
		{
			if (attackers.get(0) != null && attackers.get(0).isAttacker())
				this.dropTargetPanelAttackerOne.setActive(true);
			else
				this.dropTargetPanelAttackerOne.setActive(false);
		}

		if (cardsFromAttackerOne != null && !cardsFromAttackerOne.isEmpty())
		{
			this.updatePanel(this.layeredPanelAttackerOne, this.panelAttackerOne, cardsFromAttackerOne, defendedCards);
		}

		if (cardsFromAttackerTwo != null && !cardsFromAttackerOne.isEmpty())
		{
			// TODO make multiple opponents possible first
		}

		// TODO defeat me some cards baby - ???
	}

	private void updatePanel(JLayeredPane layeredPane, JScrollPane scrollPane, List<Card> cardsFromAttacker, Map<Card, Card> defendedCards)
	{
		layeredPane.removeAll();
		int xOffset = CardManager.CARD_WIDTH + 4;

		int cardsAttackerOne = cardsFromAttacker.size();
		int cardsPerRow = (scrollPane.getWidth() - 10) / (xOffset);
		int rows = (scrollPane.getHeight() - 10) / (CardManager.CARD_HEIGHT + 20);
		int rowSpacing = (scrollPane.getHeight() - 10) - rows * CardManager.CARD_HEIGHT;

		if (cardsAttackerOne > cardsPerRow * rows)
		{
			if (cardsAttackerOne % rows == 0)
				cardsPerRow = cardsAttackerOne / rows;
			else
				cardsPerRow = (cardsAttackerOne / rows) + 1;
		}

		int actualCardsPerRow = 0;
		int actualRow = 0;
		for (int cardNumber = 0; cardNumber < cardsFromAttacker.size(); cardNumber++)
		{
			Card card = cardsFromAttacker.get(cardNumber);
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
				if (!this.humanPlayer.isAttacker())
					new DropTarget(guiCard, new TableDropTargetListenerAsDefender(this.actionListener, guiCard));
			}

			actualCardsPerRow++;

			layeredPane.add(guiCard, 0);

			if (cardDefendedWith != null)
				layeredPane.add(cardDefendedWith, 0);
		}
	}

}
