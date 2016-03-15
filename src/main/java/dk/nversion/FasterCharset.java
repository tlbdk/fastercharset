package dk.nversion;

import sun.nio.cs.ArrayEncoder;
import sun.nio.cs.US_ASCII;
import sun.nio.cs.ext.IBM277;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

// http://stackoverflow.com/questions/2726071/efficient-way-to-calculate-byte-length-of-a-character-depending-on-the-encoding

public class FasterCharset extends Charset {
    private Charset charset;
    private CharsetEncoder encoder;

    public FasterCharset(Charset charset) {
        super(charset.name(), charset.aliases().toArray(new String[charset.aliases().size()]));
        this.charset = charset;

        if(charset instanceof US_ASCII) {
            final byte[] charTable = new byte[128];
            createTable(charTable);
            encoder = new Encoder(charset, charTable);

        } else if(charset.equals(StandardCharsets.ISO_8859_1) || charset instanceof IBM277) {
            final byte[] charTable = new byte[256];
            createTable(charTable);
            encoder = new Encoder(charset, charTable);

        } else {
            encoder = charset.newEncoder();
        }
    }

    protected FasterCharset(String canonicalName, String[] aliases) {
        super(canonicalName, aliases);
    }

    @Override
    public boolean contains(Charset cs) {
        return charset.contains(cs);
    }

    @Override
    public CharsetDecoder newDecoder() {
        return charset.newDecoder();
    }

    @Override
    public CharsetEncoder newEncoder() {
        return encoder;
    }

    private void createTable(byte[] bytes) {
        for(int i = 0; i < bytes.length; i++) {
            String character = Character.toString((char) i);;
            bytes[i] = character.getBytes(charset)[0];
        }
    }

    private static class Encoder extends CharsetEncoder implements ArrayEncoder {
        private byte[] charTable;

        private Encoder(Charset charset, byte[] charTable) {
            super(charset, 1.0F, 1.0F);
            this.charTable = charTable;
        }

        protected Encoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar) {
            super(cs, averageBytesPerChar, maxBytesPerChar);
        }

        protected Encoder(Charset cs, float averageBytesPerChar, float maxBytesPerChar, byte[] replacement) {
            super(cs, averageBytesPerChar, maxBytesPerChar, replacement);
        }

        @Override
        protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
            return null;
        }
        
        @Override
        public int encode(char[] chars, int offset, int length, byte[] bytes) {
        	length = Math.min(length, chars.length);
            for(int i = offset; i < offset + length; i++) {
                bytes[i] = charTable[chars[i]];
            }
            return length;
        }
    }
}


