package main.abstractions;

import main.implementations.Bits;

public interface DESEncryptionMode {
    Bits encrypt(Bits plaintext, Bits key, Bits iv);
    Bits decrypt(Bits ciphertext, Bits key, Bits iv);
}
