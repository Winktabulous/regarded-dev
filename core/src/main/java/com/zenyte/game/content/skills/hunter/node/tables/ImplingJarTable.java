package com.zenyte.game.content.skills.hunter.node.tables;

import com.zenyte.game.content.drops.DropTableBuilder;
import com.zenyte.game.item.Item;

/**
 * @author Kris | 22/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface ImplingJarTable {

    Item roll();

    default DropTableBuilder builder() {
        return new DropTableBuilder();
    }

}
