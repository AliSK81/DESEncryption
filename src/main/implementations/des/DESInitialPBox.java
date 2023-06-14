package main.implementations.des;

import main.tables.DESTables;

public class DESInitialPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = DESTables.DES_INITIAL_PERMUTATION_TABLE;

    public DESInitialPBox() {
        super(PERMUTATION_TABLE);
    }
}
