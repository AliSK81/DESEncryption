package main.implementations.eve;

import main.implementations.des.AbstractPBox;
import main.tables.EVETables;

public class EVEFinalPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = EVETables.FINAL_PERMUTATION_TABLE;

    public EVEFinalPBox() {
        super(PERMUTATION_TABLE);
    }
}
