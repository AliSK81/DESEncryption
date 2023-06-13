package main.implementations;

import main.tables.DESTables;

public class InitialPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = DESTables.DES_INITIAL_PERMUTATION_TABLE;

    public InitialPBox() {
        super(PERMUTATION_TABLE);
    }
}
