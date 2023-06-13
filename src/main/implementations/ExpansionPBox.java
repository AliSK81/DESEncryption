package main.implementations;

import main.tables.DESTables;

public class ExpansionPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = DESTables.DES_EXPANSION_PERMUTATION_TABLE;

    public ExpansionPBox() {
        super(PERMUTATION_TABLE);
    }
}
