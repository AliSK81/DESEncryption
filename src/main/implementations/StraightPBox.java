package main.implementations;

import main.tables.DESTables;

public class StraightPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = DESTables.DES_STRAIGHT_PERMUTATION_TABLE;

    public StraightPBox() {
        super(PERMUTATION_TABLE);
    }
}
