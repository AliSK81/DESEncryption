package main.implementations.custom;

import main.implementations.des.AbstractPBox;
import main.tables.CustomTables;

public class CustomStraightPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = CustomTables.IDENTITY_PERMUTATION_TABLE;

    public CustomStraightPBox() {
        super(PERMUTATION_TABLE);
    }
}
