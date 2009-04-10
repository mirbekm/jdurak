package gui.listeners;

import gui.DurakPanel;
import gui.game.GuiCard;
import gui.helpers.CardManager;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class DurakMouseListener extends MouseInputAdapter
{
	private DurakActionListener actionListener;
	
	public DurakMouseListener(DurakActionListener actionListener)
	{
		this.actionListener = actionListener;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		this.actionListener.attackWith((GuiCard)e.getComponent());
	}
}
