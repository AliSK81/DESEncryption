package main.implementations.des;

import main.tables.DESTables;

public class DESFinalPBox extends PBoxImpl {
    private static final int[] PERMUTATION_TABLE = DESTables.FINAL_PERMUTATION_TABLE;

    public DESFinalPBox() {
        super(PERMUTATION_TABLE);
    }
}
