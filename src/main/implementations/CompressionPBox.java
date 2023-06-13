package main.implementations;

import main.tables.DESTables;

public class CompressionPBox extends AbstractPBox {
    private static final int[] COMPRESSION_TABLE = DESTables.DES_KEY_COMPRESSION_TABLE;

    public CompressionPBox() {
        super(COMPRESSION_TABLE);
    }
}