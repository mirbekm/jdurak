package gui.listeners;

import game.Card;
import gui.game.GuiCard;
import gui.libs.DragHandler;
import gui.libs.DropHandler;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;

public class TableDropTargetListenerAsDefender extends DropHandler //implements DropTargetListener
{
	private DurakActionListener actionListener;
	
	public TableDropTargetListenerAsDefender(DurakActionListener actionListener, Component comp)
	{
		super(comp, DnDConstants.ACTION_COPY_OR_MOVE, new DataFlavor[]{DataFlavor.imageFlavor});
		
		this.actionListener = actionListener;
	}
	
	@Override
	protected boolean canDrop(DropTargetEvent e, int action, Point location)
	{
		try
		{
			Card cardDefendingWith = (Card)DragHandler.getTransferable(e).getTransferData(DataFlavor.imageFlavor);
			Card cardToBeDefeated = ((GuiCard)e.getDropTargetContext().getDropTarget().getComponent()).getCard();
			
			return this.actionListener.canDefendWithCard(cardDefendingWith, cardToBeDefeated);
		}
		catch (UnsupportedFlavorException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		return false;
	}
	
	@Override
	protected void drop(DropTargetDropEvent dtde, int action) throws UnsupportedFlavorException, IOException
	{
		try
		{
			Card cardDefendingWith = (Card)DragHandler.getTransferable(dtde).getTransferData(DataFlavor.imageFlavor);
			Card cardToBeDefeated = ((GuiCard)dtde.getDropTargetContext().getDropTarget().getComponent()).getCard();
			actionListener.defendCard(cardDefendingWith, cardToBeDefeated);
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}

}
