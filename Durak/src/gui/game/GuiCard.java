package gui.game;

import java.awt.Cursor;
import java.awt.Point;

import game.Card;
import gui.helpers.CardManager;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
 
public class GuiCard extends JLabel
{
	private Card card;
	private Point origin;
	
	public GuiCard(Card card)
	{
		super(new ImageIcon(CardManager.getInstance().getCard(card)));
		this.setSize(CardManager.CARD_WIDTH, CardManager.CARD_HEIGHT);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.card = card;
	}
	
	public Card getCard()
	{
		return this.card;
	}
	
	public void setOrigin(Point origin)
	{
		this.origin = origin;
	}
	
	public Point getOrigin()
	{
		return this.origin;
	}
}
