package main.abstractions;

import main.implementations.Bits;

public interface Mixer {
    Bits mix(Bits data, Bits subKey);
}