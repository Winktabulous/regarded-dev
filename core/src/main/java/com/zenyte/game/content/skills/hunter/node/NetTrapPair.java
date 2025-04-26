package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.game.world.object.WorldObject;

public final class NetTrapPair {
    private final WorldObject net;
    private final WorldObject tree;

    public NetTrapPair(WorldObject net, WorldObject tree) {
        this.net = net;
        this.tree = tree;
    }

    public WorldObject getNet() {
        return net;
    }

    public WorldObject getTree() {
        return tree;
    }

    @Override
    public String toString() {
        return "NetTrapPair(net=" + this.getNet() + ", tree=" + this.getTree() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NetTrapPair)) return false;
        final NetTrapPair other = (NetTrapPair) o;
        final Object this$net = this.getNet();
        final Object other$net = other.getNet();
        if (this$net == null ? other$net != null : !this$net.equals(other$net)) return false;
        final Object this$tree = this.getTree();
        final Object other$tree = other.getTree();
        return this$tree == null ? other$tree == null : this$tree.equals(other$tree);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $net = this.getNet();
        result = result * PRIME + ($net == null ? 43 : $net.hashCode());
        final Object $tree = this.getTree();
        result = result * PRIME + ($tree == null ? 43 : $tree.hashCode());
        return result;
    }
}
