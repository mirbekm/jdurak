package gui.helpers;

import game.Card;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CardManager
{
	public static final int CARD_WIDTH = 97;
	public static final int CARD_HEIGHT = 151;
	private static CardManager cardManager = null;
	private BufferedImage cardMap;

	private CardManager()
	{
		try
		{
			this.cardMap = ImageIO.read(this.getImageURL("images/cardmap.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("Couldn't load the images.");
		}
	}

	public static CardManager getInstance()
	{
		if (cardManager == null)
			cardManager = new CardManager();

		return cardManager;
	}

	public Image getCard(Card card)
	{
		assert (card != null);

		return this.cardMap.getSubimage((card.getNumber() == 14) ? 0 : (card.getNumber() - 1) * (CARD_WIDTH) - 1, card.getSuit() * (CARD_HEIGHT), CARD_WIDTH, CARD_HEIGHT);
	}

	public Image getBackOfCard()
	{
		return this.cardMap.getSubimage(2 * CARD_WIDTH - 1, 4 * CARD_HEIGHT - 1, CARD_WIDTH, CARD_HEIGHT);
	}

	public static ImageIcon getSlot()
	{
		return getImageIcon("images/slot.png");
	}

	public static String getSuitName(int suit)
	{
		assert (suit >= 0 && suit < 4);

		switch (suit)
		{
			case Card.CLUBS:
				return "clubs";
			case Card.DIAMONDS:
				return "diams";
			case Card.HEARTS:
				return "hearts";
			case Card.SPADES:
				return "spades";
		}

		return "";
	}

	public static String getSuitNameAsUnicode(int suit)
	{
		assert (suit >= 0 && suit < 4);

		switch (suit)
		{
			case Card.CLUBS:
				return "&#9827;";
			case Card.DIAMONDS:
				return "&#9830;";
			case Card.HEARTS:
				return "&#9829;";
			case Card.SPADES:
				return "&#9824;";
		}

		return "";
	}

	public static ImageIcon getImageIcon(String name)
	{
		return new ImageIcon(CardManager.getInstance().getImageURL(name));
	}

	public URL getImageURL(String name)
	{
		return getClass().getClassLoader().getResource(name);
	}
}
