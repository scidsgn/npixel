package com.npixel.base.bitmap;

public enum ColorBlendMode {
    // Alpha composite
    NORMAL, BEHIND,

    // Darken/burn
    MULTIPLY, COLORBURN, LINEARBURN, DARKER,
    // Lighten/dodge
    SCREEN, COLORDODGE, LINEARDODGE, LIGHTER,

    // Overlay
    OVERLAY, HARDLIGHT, SOFTLIGHT, LINEARLIGHT, VIVIDLIGHT, PINLIGHT,

    // Extra
    SUBTRACT, DIVIDE, DIFFERENCE
}
