package main.implementations;

import main.abstractions.Encryptor;
import main.abstractions.KeyGenerator;
import main.abstractions.Mixer;
import main.abstractions.PBox;


public class DESEncryptor implements Encryptor {
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
        Bits data = initialPBox.permute(plaintext);
        Bits[] subKeys = keyGenerator.generateSubKeys(key);

        for (int round = 0; round < 16; round++) {
            Bits roundKey = subKeys[round];
            data = mixer.mix(data, roundKey);
            if (round != 15) data.swapHalves();
        }

        return finalPBox.permute(data);
    }

    public Bits decrypt(Bits ciphertext, Bits key) {
        Bits data = finalPBox.permute(ciphertext);
        Bits[] subKeys = keyGenerator.generateSubKeys(key);

        for (int round = 15; round >= 0; round--) {
            Bits roundKey = subKeys[round];
            data = mixer.mix(data, roundKey);
            if (round != 0) data.swapHalves();
        }

        return initialPBox.permute(data);
    }
}