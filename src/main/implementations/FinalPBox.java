package main.implementations;

import main.tables.DESTables;

public class FinalPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = DESTables.DES_FINAL_PERMUTATION_TABLE;

    public FinalPBox() {
        super(PERMUTATION_TABLE);
    }
}
