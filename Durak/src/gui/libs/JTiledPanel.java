package gui.libs;

import java.awt.*;
import javax.swing.*;

/**
 * A small extension to JPanel, meant to allow the JPanel to support a tiling image background. The tiled background is correctly drawn inside any Border that the panel might have. Note that JTiledPanel containers are always opaque. If you
 * give the tiling image as null, then JTiledPanel behaves exactly like an opaque JPanel.
 */
public class JTiledPanel extends JPanel
{
	private Image tileimage;
	private int tilewidth;
	private int tileheight;
	private Rectangle rb;
	private Insets ri;

	/**
	 * Create a JTiledPanel with the given image. The tile argument may be null, you can set it later with setTileImage(). Note that a JTiledPanel is always opaque.
	 */
	public JTiledPanel(Image tile)
	{
		super();
		setTileImage(tile);
		setOpaque(true);
		rb = new Rectangle(0, 0, 0, 0);
		ri = new Insets(0, 0, 0, 0);
	}

	/**
	 * Create a JTiledPanel with the given image and layout manager and double buffering status. Either or both of the first two arguments may be null.
	 */
	public JTiledPanel(Image tile, LayoutManager mgr, boolean isDB)
	{
		super(mgr, isDB);
		setTileImage(tile);
		setOpaque(true);
		rb = new Rectangle(0, 0, 0, 0);
		ri = new Insets(0, 0, 0, 0);
	}

	/**
	 * Get the current tiling image, or null if there isn't any right now.
	 */
	public Image getTileImage()
	{
		return tileimage;
	}

	/**
	 * Set the current tiling image. To prevent tiling, call this method with null. Note that this method does NOT call repaint for you; if you want the panel to repaint immediately, you must call repaint() yourself.
	 */
	public void setTileImage(Image tile)
	{
		tileimage = tile;
		tilewidth = 0;
		tileheight = 0;
	}

	/**
	 * Paint this component, including the tiled background image, if any.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (tileimage != null && tilewidth <= 0)
		{
			tileheight = tileimage.getHeight(this);
			tilewidth = tileimage.getWidth(this);
		}
		if (tileimage != null && tilewidth > 0)
		{
			Color bg = getBackground();
			getBounds(rb);
			Insets riv = getInsets(ri);
			rb.translate(riv.left, riv.top);
			rb.width -= (riv.left + riv.right);
			rb.height -= (riv.top + riv.bottom);
			Shape ccache = g.getClip();
			g.clipRect(rb.x, rb.y, rb.width, rb.height);
			int xp, yp;
			for (yp = rb.y; yp < rb.y + rb.height; yp += tileheight)
			{
				for (xp = rb.x; xp < rb.x + rb.width; xp += tilewidth)
				{
					g.drawImage(tileimage, xp, yp, bg, this);
				}
			}
			g.setClip(ccache);
		}
	}
}
