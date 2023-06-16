package main.implementations.eve;

import main.implementations.des.AbstractPBox;
import main.tables.EVETables;

public class EVEInitialPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = EVETables.INITIAL_PERMUTATION_TABLE;

    public EVEInitialPBox() {
        super(PERMUTATION_TABLE);
    }
}
