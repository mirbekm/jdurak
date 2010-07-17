package gui.libs;

import gui.helpers.Colors;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

public class JTransparentPanel extends JPanel
{
	public JTransparentPanel()
	{
		this.setBackground(Colors.DARK_GREEN);
		this.setOpaque(false);
		this.addMouseListener(new JTransparentPanelMouseListener());
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Color ppColor = new Color(64, 64, 64, 160); // R, G, B, Alpha
		g.setColor(ppColor);
		g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight()); // X-Position, Y-Position, Width, Height
	}

	private class JTransparentPanelMouseListener extends MouseInputAdapter
	{
		@Override
		public void mouseMoved(MouseEvent e)
		{
			super.mouseMoved(e);
			JTransparentPanel.this.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		}
	}
}
