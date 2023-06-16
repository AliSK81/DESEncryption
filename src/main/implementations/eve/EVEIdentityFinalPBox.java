package main.implementations.eve;

import main.implementations.des.PBoxImpl;
import main.tables.EVETables;

public class EVEIdentityFinalPBox extends PBoxImpl {
    private static final int[] PERMUTATION_TABLE = EVETables.IDENTITY_FINAL_PERMUTATION_TABLE;

    public EVEIdentityFinalPBox() {
        super(PERMUTATION_TABLE);
    }
}
