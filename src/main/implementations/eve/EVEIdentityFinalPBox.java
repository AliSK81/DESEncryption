package main.implementations.eve;

import main.implementations.des.AbstractPBox;
import main.tables.EVETables;

public class EVEIdentityFinalPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = EVETables.IDENTITY_FINAL_PERMUTATION_TABLE;

    public EVEIdentityFinalPBox() {
        super(PERMUTATION_TABLE);
    }
}
