package main.implementations.des;

import main.tables.DESTables;

public class DESParityDropPBox extends AbstractPBox {
    private static final int[] PARITY_BIT_DROP_TABLE = DESTables.KEY_PARITY_BIT_DROP_PERMUTATION_TABLE;

    public DESParityDropPBox() {
        super(PARITY_BIT_DROP_TABLE);
    }

}