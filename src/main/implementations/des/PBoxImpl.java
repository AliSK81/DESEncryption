package main.implementations.des;

import main.abstractions.PBox;
import main.implementations.Bits;

public class PBoxImpl implements PBox {
    protected final int[] permutationTable;

    public PBoxImpl(int[] permutationTable) {
        this.permutationTable = permutationTable;
    }

    @Override
    public Bits permute(Bits data) {
        Bits result = new Bits(permutationTable.length);
        for (int i = 0; i < permutationTable.length; i++) {
            int bitIndex = permutationTable[i] - 1;
            boolean bitValue = data.get(bitIndex);
            result.set(i, bitValue);
        }
        return result;
    }
}