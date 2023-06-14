package main.implementations.custom;

import main.implementations.des.AbstractPBox;
import main.tables.CustomTables;

public class CustomFinalReversePBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = CustomTables.FINAL_PERMUTATION_TABLE;

    public CustomFinalReversePBox() { super(PERMUTATION_TABLE); }
}
