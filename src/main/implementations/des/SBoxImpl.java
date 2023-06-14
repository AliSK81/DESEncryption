package main.implementations.des;

import main.abstractions.SBox;
import main.implementations.Bits;

public class SBoxImpl implements SBox {
    private final int[] substitutionTable;

    public SBoxImpl(int[] substitutionTable) {
        this.substitutionTable = substitutionTable;
    }

    @Override
    public Bits substitute(Bits data) {
        int row = (data.get(0) ? 2 : 0) + (data.get(5) ? 1 : 0);
        int column = (data.get(1) ? 8 : 0) + (data.get(2) ? 4 : 0) +
                (data.get(3) ? 2 : 0) + (data.get(4) ? 1 : 0);
        int value = substitutionTable[row * 16 + column];
        Bits result = new Bits(4);
        for (int i = 0; i < 4; i++) {
            result.set(i, ((value >> (3 - i)) & 1) == 1);
        }
        return result;
    }
}