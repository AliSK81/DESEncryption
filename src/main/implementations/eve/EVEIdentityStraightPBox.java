package main.implementations.eve;

import main.implementations.des.AbstractPBox;
import main.tables.EVETables;

public class EVEIdentityStraightPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = EVETables.IDENTITY_STRAIGHT_PERMUTATION_TABLE;

    public EVEIdentityStraightPBox() {
        super(PERMUTATION_TABLE);
    }
}
