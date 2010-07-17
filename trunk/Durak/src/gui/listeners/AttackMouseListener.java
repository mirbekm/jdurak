package gui.listeners;

import gui.game.GuiCard;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputAdapter;

public class AttackMouseListener extends MouseInputAdapter
{
	private DurakActionListener actionListener;

	public AttackMouseListener(DurakActionListener actionListener)
	{
		this.actionListener = actionListener;
	}

	public void mouseClicked(MouseEvent e)
	{
		this.actionListener.attackWith((GuiCard) e.getComponent());
	}

	public DurakActionListener getDurakActionListener()
	{
		return this.actionListener;
	}
}
