package main.implementations.eve;

import main.implementations.des.AbstractPBox;
import main.tables.EVETables;

public class EVEFinalReversePBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = EVETables.REVERSED_FINAL_PERMUTATION_TABLE;

    public EVEFinalReversePBox() {
        super(PERMUTATION_TABLE);
    }
}
