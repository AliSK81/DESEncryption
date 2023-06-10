package main.implementations;

import main.abstractions.DESEncryptionMode;

import java.util.List;

public class DESEncryptionCBCMode implements DESEncryptionMode {

    private final DESEncryptor desEncryptor;

    public DESEncryptionCBCMode(DESEncryptor desEncryptor) {
        this.desEncryptor = desEncryptor;
    }

    @Override
    public Bits encrypt(Bits plaintext, Bits key, Bits iv) {
        // Split the plaintext into blocks
        int blockSize = 64;
        List<Bits> plaintextBlocks = plaintext.split(blockSize);

        // Initialize the ciphertext and the previous block
        Bits ciphertext = Bits.empty();
        Bits previousBlock = iv;

        // Encrypt each block using CBC mode
        for (Bits block : plaintextBlocks) {
            // XOR the block with the previous ciphertext block or the IV
            block.xor(previousBlock);

            // Encrypt the XOR-ed block
            Bits encryptedBlock = desEncryptor.encrypt(block, key);

            // Add the encrypted block to the ciphertext
            ciphertext = ciphertext.concat(encryptedBlock);

            // Update the previous block
            previousBlock = encryptedBlock;
        }

        return ciphertext;
    }

    @Override
    public Bits decrypt(Bits ciphertext, Bits key, Bits iv) {
        // Split the ciphertext into blocks
        int blockSize = 64;
        List<Bits> ciphertextBlocks = ciphertext.split(blockSize);

        // Initialize the plaintext and the previous block
        Bits plaintext = Bits.empty();
        Bits previousBlock = iv;

        // Decrypt each block using CBC mode
        for (Bits block : ciphertextBlocks) {
            // Decrypt the block
            Bits decryptedBlock = desEncryptor.decrypt(block, key);

            // XOR the decrypted block with the previous ciphertext block or the IV
            decryptedBlock.xor(previousBlock);

            // Add the XOR-ed block to the plaintext
            plaintext = plaintext.concat(decryptedBlock);

            // Update the previous block
            previousBlock = block;
        }

        return plaintext;
    }
}