package main.implementations;

import main.tables.DESTables;

public class ParityDropPBox extends AbstractPBox {
    private static final int[] PARITY_BIT_DROP_TABLE = DESTables.DES_KEY_PARITY_BIT_DROP_PERMUTATION_TABLE;

    public ParityDropPBox() {
        super(PARITY_BIT_DROP_TABLE);
    }

}