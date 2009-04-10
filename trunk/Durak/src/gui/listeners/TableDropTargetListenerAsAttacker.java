package gui.listeners;

import game.Card;
import gui.libs.DragHandler;
import gui.libs.DropHandler;

import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;

public class TableDropTargetListenerAsAttacker extends DropHandler //implements DropTargetListener
{
	private DurakActionListener actionListener;
	
	public TableDropTargetListenerAsAttacker(DurakActionListener actionListener, Component comp)
	{
		super(comp, DnDConstants.ACTION_COPY_OR_MOVE, new DataFlavor[]{DataFlavor.imageFlavor});
		
		this.actionListener = actionListener;
	}

	protected boolean canDrop(DropTargetEvent dte, int action, Point location)
	{
		try
		{
			Card card = (Card)DragHandler.getTransferable(dte).getTransferData(DataFlavor.imageFlavor);
			return this.actionListener.canAttackWithThisCard(card);
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void drop(DropTargetDropEvent dtde, int action) throws UnsupportedFlavorException, IOException
	{
		try
		{
			this.actionListener.attackWith((Card)dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));
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
