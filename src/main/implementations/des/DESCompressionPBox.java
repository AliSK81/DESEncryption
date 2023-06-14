package main.implementations.des;

import main.tables.DESTables;

public class DESCompressionPBox extends AbstractPBox {
    private static final int[] COMPRESSION_TABLE = DESTables.KEY_COMPRESSION_TABLE;

    public DESCompressionPBox() {
        super(COMPRESSION_TABLE);
    }
}