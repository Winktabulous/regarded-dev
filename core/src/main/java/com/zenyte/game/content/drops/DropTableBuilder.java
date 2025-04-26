package com.zenyte.game.content.drops;

import com.zenyte.game.content.drops.table.DropTable;

/**
 * @author Kris | 22/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DropTableBuilder {
    
    private final DropTable table = new DropTable();
    
    public DropTableBuilder append(final int id) {
        return append(id, 1, 1, 1);
    }
    
    public DropTableBuilder append(final int id, final int weight) {
        return append(id, 1, 1, weight);
    }
    
    public DropTableBuilder append(final int id, final int amount, final int weight) {
        return append(id, amount, amount, weight);
    }
    
    public DropTableBuilder append(final int id, final int minAmount, final int maxAmount, final int weight) {
        table.append(id, weight, minAmount, maxAmount);
        return this;
    }
    
    public DropTable build() {
        return table;
    }
    
}
