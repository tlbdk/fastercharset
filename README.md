# fastercharset
Simple wrapper for Charset that tries to optimizes encoding and decoding for fixed length charsets:

This code does not make things go faster on Java 8:

    getBytes(US_ASCII): 51
    getBytesFasterCharset(US_ASCII): 50
    getBytesUS_ASCIILookupTable: 56
    encode(US_ASCII): 15
    FasterCharset.encode(US_ASCII): 34
    encodeUS_ASCIILookupTable: 42
    encode(ISO_8859_1): 13
    FasterCharset.encode(ISO_8859_1): 36
    encode(CP277): 12
    FasterCharset.encode(CP277): 38
