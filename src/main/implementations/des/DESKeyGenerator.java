package main.implementations.des;

import main.abstractions.KeyGenerator;
import main.abstractions.PBox;
import main.implementations.Bits;
import main.tables.DESTables;

public class DESKeyGenerator implements KeyGenerator {
    private static final int[] SHIFT_TABLE = DESTables.DES_KEY_SHIFT_TABLE;
    private static final int ROUNDS = 16;
    private final PBox parityDropPBox;
    private final PBox compressionPBox;


    public DESKeyGenerator(PBox parityDropPBox, PBox compressionPBox) {
        this.parityDropPBox = parityDropPBox;
        this.compressionPBox = compressionPBox;
    }

    @Override
    public Bits[] generateSubKeys(Bits key) {
        Bits[] subKeys = new Bits[ROUNDS];

        Bits permutedKey = parityDropPBox.permute(key);

        subKeys[0] = shiftKey(permutedKey, 0);

        for (int round = 1; round < ROUNDS; round++) {
            Bits shiftedKey = shiftKey(subKeys[round - 1], round);
            subKeys[round] = shiftedKey;
        }

        for (int round = 0; round < ROUNDS; round++) {
            subKeys[round] = compressionPBox.permute(subKeys[round]);
        }

        return subKeys;
    }

    private Bits shiftKey(Bits key, int round) {
        int shiftCount = SHIFT_TABLE[round];

        Bits leftHalf = key.getFirstHalf();
        leftHalf.circularShiftLeft(shiftCount);

        Bits rightHalf = key.getSecondHalf();
        rightHalf.circularShiftLeft(shiftCount);

        return leftHalf.concat(rightHalf);
    }

}