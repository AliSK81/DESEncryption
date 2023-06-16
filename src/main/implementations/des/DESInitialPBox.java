package main.implementations.des;

import main.tables.DESTables;

public class DESInitialPBox extends PBoxImpl {
    private static final int[] PERMUTATION_TABLE = DESTables.INITIAL_PERMUTATION_TABLE;

    public DESInitialPBox() {
        super(PERMUTATION_TABLE);
    }
}
