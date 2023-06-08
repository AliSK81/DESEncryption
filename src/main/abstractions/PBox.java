package main.abstractions;

import main.implementations.Bits;

public interface PBox {
    Bits permute(Bits data);
}