package game;

/**
 * A human player
 * 
 * @author Andreas Krings - <a href="mailto:info@ankri.de">info@ankri.de</a> - <a href="http://www.ankri.de" target="_blank">ankri.de</a>
 * @version $Date$ - Revision: $Rev$
 */
public class Player extends AbstractPlayer
{
	/**
	 * Create a new player with the given parameters
	 * 
	 * @param durak
	 *            The game logic
	 * @param name
	 *            The name of the player
	 */
	public Player(Durak durak, String name)
	{
		super(durak, name);
	}

}
