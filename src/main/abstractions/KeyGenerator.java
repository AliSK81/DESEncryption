package main.abstractions;

import main.implementations.Bits;


public interface KeyGenerator {
    Bits[] generateSubKeys(Bits key);
}