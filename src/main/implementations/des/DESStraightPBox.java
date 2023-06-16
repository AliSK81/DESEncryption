package main.implementations.des;

import main.tables.DESTables;

public class DESStraightPBox extends PBoxImpl {
    private static final int[] PERMUTATION_TABLE = DESTables.STRAIGHT_PERMUTATION_TABLE;

    public DESStraightPBox() {
        super(PERMUTATION_TABLE);
    }
}
