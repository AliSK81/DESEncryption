package main.implementations.custom;

import main.implementations.des.AbstractPBox;
import main.tables.CustomTables;

public class CustomInitialPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = CustomTables.INITIAL_PERMUTATION_TABLE;

    public CustomInitialPBox() { super(PERMUTATION_TABLE); }
}
