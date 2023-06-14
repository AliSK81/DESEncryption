package main.implementations.des;

import main.abstractions.Encryptor;
import main.abstractions.KeyGenerator;
import main.abstractions.Mixer;
import main.abstractions.PBox;
import main.implementations.Bits;

public class DESEncryptor implements Encryptor {
    private static final int BLOCK_SIZE = 64;
    private static final int KEY_SIZE = 64;
    private static final int ROUNDS = 16;
    private final Mixer mixer;
    private final PBox initialPBox;
    private final PBox finalPBox;
    private final KeyGenerator keyGenerator;

    public DESEncryptor(Mixer mixer, PBox initialPBox, PBox finalPBox, KeyGenerator keyGenerator) {
        this.mixer = mixer;
        this.initialPBox = initialPBox;
        this.finalPBox = finalPBox;
        this.keyGenerator = keyGenerator;
    }

    public Bits encrypt(Bits plaintext, Bits key) {
        validateInputSize(plaintext, key);

        Bits data = initialPBox.permute(plaintext);
        Bits[] subKeys = keyGenerator.generateSubKeys(key);

        for (int round = 0; round < ROUNDS; round++) {
            Bits roundKey = subKeys[round];
            data = mixer.mix(data, roundKey);
            if (round != ROUNDS - 1) data.swapHalves();
        }

        return finalPBox.permute(data);
    }

    public Bits decrypt(Bits ciphertext, Bits key) {
        validateInputSize(ciphertext, key);

        Bits data = initialPBox.permute(ciphertext);
        Bits[] subKeys = keyGenerator.generateSubKeys(key);

        for (int round = ROUNDS - 1; round >= 0; round--) {
            Bits roundKey = subKeys[round];
            data = mixer.mix(data, roundKey);
            if (round != 0) data.swapHalves();
        }

        return finalPBox.permute(data);
    }

    private void validateInputSize(Bits data, Bits key) {
        if (data.size() != BLOCK_SIZE) {
            throw new IllegalArgumentException("Invalid data size: " + data.size() + " (expected " + BLOCK_SIZE + ")");
        }
        if (key.size() != KEY_SIZE) {
            throw new IllegalArgumentException("Invalid key size: " + key.size() + " (expected " + KEY_SIZE + ")");
        }
    }
}