package gui.listeners;

import game.Card;
import gui.game.GuiCard;
import gui.helpers.CardManager;
import gui.libs.DragHandler;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class CardDragSourceListener extends DragHandler//implements DragSourceListener, DragGestureListener
{
	private GuiCard comp;
	private Icon dragIcon;
	
	public CardDragSourceListener(GuiCard comp)
	{
		this(comp, DnDConstants.ACTION_COPY_OR_MOVE);
		this.comp = comp;
		this.dragIcon = new ImageIcon(CardManager.getInstance().getCard(comp.getCard()));
	}
	
	public CardDragSourceListener(JComponent comp, int actions)
	{
		super(comp, actions);
	}
	
	@Override
	protected void dragStarted(DragGestureEvent e)
	{
		GuiCard guiCard = (GuiCard)e.getComponent();
		guiCard.setLocation(guiCard.getOrigin());
	}
	
	@Override
	protected Transferable getTransferable(DragGestureEvent e)
	{
		return new CardTransfer(comp.getCard());
	}
	
    protected Icon getDragIcon(DragGestureEvent e, Point offset) 
    {
        return dragIcon;
    }	
}

class CardTransfer implements Transferable
{
	Card card;
	
	public CardTransfer(Card card)
	{
		this.card = card;
	}
	
	@Override
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException
	{
		return this.card;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return new DataFlavor[]{DataFlavor.imageFlavor};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		return (flavor == DataFlavor.imageFlavor);
	}
	
}
