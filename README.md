# fastercharset
Simple wrapper for Charset that tries to optimizes encoding and decoding for fixed length charsets:

Runtime in ms for 1 million encodings of "The quick brown fox jumps over the lazy dog":

    ** String.getBytes() **
    getBytes(US_ASCII): 44
    getBytes(FasterCharset(US_ASCII)): 57
    getBytes(ISO_8859_1): 47
    getBytes(FasterCharset(ISO_8859_1)): 53
    getBytes(CP277): 135
    getBytes(FasterCharset(CP277)): 53
    
    ** Charset.Encode() **
    getBytes(US_ASCII): 43
    getBytes(FasterCharset(US_ASCII)): 45
    getBytes(ISO_8859_1): 43
    getBytes(FasterCharset(ISO_8859_1)): 44
    getBytes(CP277): 123
    getBytes(FasterCharset(CP277)): 43
