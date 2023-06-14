package main.implementations.custom;

import main.implementations.des.AbstractPBox;
import main.tables.CustomTables;

public class CustomFinalIdentityPBox extends AbstractPBox {
    private static final int[] PERMUTATION_TABLE = CustomTables.IDENTITY_FINAL_PERMUTATION_TABLE;

    public CustomFinalIdentityPBox() { super(PERMUTATION_TABLE); }
}
