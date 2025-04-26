package com.zenyte.game.util.math;

import com.zenyte.game.util.Utils;
import it.unimi.dsi.fastutil.doubles.Double2IntArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Arrays;
import java.util.Comparator;

public class ProbabilityCollection<T extends Probability> {

    public static boolean LOG = false;
    private double totalWeight;
    private final ObjectArrayList<T> items = new ObjectArrayList<>();
    private final ObjectArrayList<Double> tables = new ObjectArrayList<>();
    private final Double2IntArrayMap probabilityCount = new Double2IntArrayMap();

    @SafeVarargs
    public ProbabilityCollection(T... items) {
        this.items.addAll(Arrays.asList(items));
        update();
    }

    public void addAll(T... items) {
        this.items.addAll(Arrays.asList(items));
        update();
    }
    public void addAll(ObjectArrayList<T> items) {
        this.items.addAll(items);
        update();
    }

    public void add(T item) {
        this.add(item, true);
    }

    public void add(T item, boolean updateWeights) {
        this.items.add(item);
        if (updateWeights)
            update();
    }

    public void remove(T item) {
        this.items.remove(item);
        update();
    }

    public ObjectArrayList<T> items() {
        return items;
    }

    public void update() {
        tables.clear();
        totalWeight = 0;

        for (T item : items) {
            if(!tables.contains(item.getProbability())) {
                tables.add(item.getProbability());
                probabilityCount.put(item.getProbability(), items.stream().filter(it -> it.getProbability() == item.getProbability()).toList().size());
            }
        }

        if(!tables.contains(1D))
            tables.add(1D);//the always

        for (Double table : tables) {
            totalWeight+=table;
        }
    }

    public T rollItem() {
        double value = Math.random() * totalWeight;
        double weight = 0;

        double s = 1;

        for (Double table : tables) {
            weight += table;
            if(value < weight) {
                s = table;
                break;
            }
        }
        double table = s;


        ObjectArrayList<T> loot = new ObjectArrayList<>();

        for (T item : items) {
            if(item.getProbability() == table)
                loot.add(item);
        }

        if(loot.isEmpty())
            return null;

        return Utils.random(loot);
    }

    public void clean() {
        this.items.clear();
        this.totalWeight = 0;
    }

    public void sort() {
        items.sort(Comparator.comparingDouble(Probability::getProbability));
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public int getTableSize(double probability) {
        return probabilityCount.get(probability);
    }
}


