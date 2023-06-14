package main.implementations.des;

import main.abstractions.Mixer;
import main.abstractions.PBox;
import main.abstractions.SBox;
import main.implementations.Bits;


public class MixerImp implements Mixer {
    private static final int BLOCK_SIZE = 64;
    private static final int SUB_KEY_SIZE = 48;
    private final PBox expansionPBox;
    private final PBox straightPBox;
    private final SBox[] sBoxes;

    public MixerImp(PBox expansionPBox, PBox straightPBox, SBox[] sBoxes) {
        if (sBoxes.length != 8) {
            throw new IllegalArgumentException("The SBoxes array must contain exactly 8 SBoxes.");
        }
        this.expansionPBox = expansionPBox;
        this.straightPBox = straightPBox;
        this.sBoxes = sBoxes;
    }

    @Override
    public Bits mix(Bits data, Bits subKey) {
        validateInputSize(data, subKey);

        Bits leftHalf = data.get(0, BLOCK_SIZE / 2);
        Bits rightHalf = data.get(BLOCK_SIZE / 2, BLOCK_SIZE);

        Bits mixedRightHalf = applyDES(rightHalf, subKey);

        leftHalf.xor(mixedRightHalf);

        Bits result = new Bits(BLOCK_SIZE);
        result.set(0, BLOCK_SIZE / 2, leftHalf);
        result.set(BLOCK_SIZE / 2, BLOCK_SIZE, rightHalf);

        return result;
    }

    private Bits applyDES(Bits input, Bits subKey) {
        Bits expandedInput = expansionPBox.permute(input);
        expandedInput.xor(subKey);

        Bits substitutedInput = new Bits(sBoxes.length * 4);
        for (int i = 0; i < sBoxes.length; i++) {
            Bits block = expandedInput.get(i * 6, (i + 1) * 6);
            Bits substitutedBlock = sBoxes[i].substitute(block);
            for (int j = 0; j < 4; j++) {
                substitutedInput.set(i * 4 + j, substitutedBlock.get(j));
            }
        }

        return straightPBox.permute(substitutedInput);
    }

    private void validateInputSize(Bits data, Bits subKey) {
        if (data.size() != BLOCK_SIZE) {
            throw new IllegalArgumentException("Invalid data size: " + data.size() + " (expected " + BLOCK_SIZE + ")");
        }
        if (subKey.size() != SUB_KEY_SIZE) {
            throw new IllegalArgumentException("Invalid subKey size: " + subKey.size() + " (expected " + SUB_KEY_SIZE + ")");
        }
    }
}