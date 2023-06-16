package main.implementations.des;

import main.tables.DESTables;

public class DESExpansionPBox extends PBoxImpl {
    private static final int[] PERMUTATION_TABLE = DESTables.EXPANSION_PERMUTATION_TABLE;

    public DESExpansionPBox() {
        super(PERMUTATION_TABLE);
    }
}
