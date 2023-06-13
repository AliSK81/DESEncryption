package main.implementations;

import main.abstractions.EncryptionMode;
import main.abstractions.Encryptor;

public class ECBEncryptionMode implements EncryptionMode {

    private static final int BLOCK_SIZE = 64;
    private final Encryptor encryptor;

    public ECBEncryptionMode(DESEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public Bits encrypt(Bits plaintext, Bits key, Bits iv) {
        Bits paddedPlaintext = plaintext.pad(BLOCK_SIZE);

        Bits ciphertext = Bits.empty();

        for (Bits block : paddedPlaintext.split(BLOCK_SIZE)) {
            Bits encryptedBlock = encryptor.encrypt(block, key);

            ciphertext = ciphertext.concat(encryptedBlock);
        }

        return ciphertext;
    }

    @Override
    public Bits decrypt(Bits ciphertext, Bits key, Bits iv) {
        Bits plaintext = Bits.empty();

        for (Bits block : ciphertext.split(BLOCK_SIZE)) {
            Bits decryptedBlock = encryptor.decrypt(block, key);

            plaintext = plaintext.concat(decryptedBlock);
        }

        return plaintext.trim();
    }
}