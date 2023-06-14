package main.implementations.custom;

import main.implementations.des.AbstractPBox;
import main.tables.CustomTables;

public class CustomFinalPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = CustomTables.FINAL_PERMUTATION_TABLE;

    public CustomFinalPBox() { super(PERMUTATION_TABLE); }
}
