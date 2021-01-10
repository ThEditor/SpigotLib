package me.tigerhix.lib.scoreboard.type;

import org.bukkit.entity.Player;

/**
 * Represents an advanced scoreboard that can display up to 48 characters in a single entry.
 *
 * @author TigerHix
 */
public interface Sidebar {

    /**
     * Activate the scoreboard.
     */
    void activate();

    /**
     * Deactivate the scoreboard.
     */
    void deactivate();

    /**
     * Determine if the scoreboard has been already activated.
     *
     * @return activated
     */
    boolean isActivated();

    /**
     * Returns the handler for this scoreboard.
     *
     * @return handler
     */
    SidebarHandler getHandler();

    /**
     * Set the handler for this scoreboard.
     *
     * @param handler handler
     */
    Sidebar setHandler(SidebarHandler handler);

    /**
     * Returns the update interval (default = 10L).
     *
     * @return update interval
     */
    long getUpdateInterval();

    /**
     * Set the update interval.
     *
     * @param updateInterval update interval
     * @return this
     */
    Sidebar setUpdateInterval(long updateInterval);

    /**
     * Returns the holder of this scoreboard.
     *
     * @return holder
     */
    Player getHolder();

}
