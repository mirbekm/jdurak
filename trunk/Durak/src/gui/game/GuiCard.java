package gui.game;

import game.Card;
import gui.helpers.CardManager;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GuiCard extends JLabel
{
	private Card card;
	private Point origin;
	private boolean isEnabled = true;

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

	@Override
	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;

		if (!isEnabled)
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		this.repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		if (!this.isEnabled)
		{
			Color ppColor = new Color(128, 128, 128, 160); // R, G, B, Alpha
			g.setColor(ppColor);
			g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight()); // X-Position, Y-Position, Width, Height
		}
	}
}
