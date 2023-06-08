package main.implementations;

import main.abstractions.Mixer;
import main.abstractions.PBox;
import main.abstractions.SBox;


public class MixerImp implements Mixer {
    private static final int BLOCK_SIZE = 64;
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
        Bits leftHalf = data.get(0, BLOCK_SIZE / 2);
        Bits rightHalf = data.get(BLOCK_SIZE / 2, BLOCK_SIZE);

        Bits mixedRightHalf = applyDES(rightHalf, subKey);

        leftHalf.xor(mixedRightHalf);

        Bits result = new Bits(BLOCK_SIZE);
        result.or(leftHalf);
        result.set(BLOCK_SIZE / 2, BLOCK_SIZE);
        result.or(rightHalf);

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
}