package dev.bnorm.librettist.text

import de.cketti.codepoints.deluxe.CodePoint

fun CodePoint.isWhitespace(): Boolean =
    isBasic && value.toChar().isWhitespace()
