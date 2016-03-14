package dk.nversion;

import org.junit.Test;
import sun.nio.cs.ArrayEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class FasterCharsetTest {

    @Test
    public void testGetBytes() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        int warmup = 100000000;
        int iterations = 1000000;

        getBytes(StandardCharsets.US_ASCII, sample, warmup);
        System.gc();
        System.out.println( "getBytes(US_ASCII): " + getBytes(StandardCharsets.US_ASCII, sample, iterations));

        getBytes(new FasterCharset(StandardCharsets.US_ASCII), sample, warmup);
        System.gc();
        System.out.println( "getBytesFasterCharset(US_ASCII): " + getBytes(new FasterCharset(StandardCharsets.US_ASCII), sample, iterations));

        getBytesUS_ASCIILookupTable(sample, warmup);
        System.gc();
        System.out.println( "getBytesUS_ASCIILookupTable: " + getBytesUS_ASCIILookupTable(sample, iterations));

        getBytes((ArrayEncoder)StandardCharsets.US_ASCII.newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "encode(US_ASCII): " + getBytes((ArrayEncoder)StandardCharsets.US_ASCII.newEncoder(), sample, new byte[sample.length()], iterations));

        getBytes((ArrayEncoder)new FasterCharset(StandardCharsets.US_ASCII).newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "FasterCharset.encode(US_ASCII): " + getBytes((ArrayEncoder)new FasterCharset(StandardCharsets.US_ASCII).newEncoder(), sample, new byte[sample.length()], iterations));

        encodeUS_ASCIILookupTable(sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "encodeUS_ASCIILookupTable: " + encodeUS_ASCIILookupTable(sample,  new byte[sample.length()], iterations));

        getBytes((ArrayEncoder)StandardCharsets.ISO_8859_1.newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "encode(ISO_8859_1): " + getBytes((ArrayEncoder)StandardCharsets.ISO_8859_1.newEncoder(), sample, new byte[sample.length()], iterations));

        getBytes((ArrayEncoder)new FasterCharset(StandardCharsets.ISO_8859_1).newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "FasterCharset.encode(ISO_8859_1): " + getBytes((ArrayEncoder)new FasterCharset(StandardCharsets.ISO_8859_1).newEncoder(), sample, new byte[sample.length()], iterations));

        getBytes((ArrayEncoder)Charset.forName("cp277").newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "encode(CP277): " + getBytes((ArrayEncoder)Charset.forName("cp277").newEncoder(), sample, new byte[sample.length()], iterations));

        getBytes((ArrayEncoder)new FasterCharset(Charset.forName("cp277")).newEncoder(), sample, new byte[sample.length()], warmup);
        System.gc();
        System.out.println( "FasterCharset.encode(CP277): " + getBytes((ArrayEncoder)new FasterCharset(Charset.forName("cp277")).newEncoder(), sample, new byte[sample.length()], iterations));
    }

    private long getBytes(Charset charset, String sample, int iterations) {
        final long start = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            byte[] test = sample.getBytes(charset);
        }
        return System.currentTimeMillis() - start;
    }

    private long getBytes(ArrayEncoder encoder, String sample, byte[] bytes, int iterations) {
        final long start = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            encoder.encode(sample.toCharArray(), 0, 0, bytes);
        }
        return System.currentTimeMillis() - start;
    }

    @Test
    public void testGetBytesUS_ASCIILookupTable() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes =  new byte[sample.length()];
        getBytesUS_ASCIILookupTable(sample.toCharArray(), bytes);
        assertArrayEquals(sample.getBytes(StandardCharsets.US_ASCII), bytes);
    }

    @Test
    public void testGetBytesISO_8859_1() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes =  new byte[sample.length()];
        ((ArrayEncoder)new FasterCharset(StandardCharsets.ISO_8859_1).newEncoder()).encode(sample.toCharArray(), 0, 0, bytes);
        assertArrayEquals(sample.getBytes(StandardCharsets.ISO_8859_1), bytes);
    }

    @Test
    public void testGetBytesCP277() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes =  new byte[sample.length()];
        ((ArrayEncoder)new FasterCharset(Charset.forName("cp277")).newEncoder()).encode(sample.toCharArray(), 0, 0, bytes);
        assertArrayEquals(sample.getBytes("cp277"), bytes);
    }

    final static byte[] US_ASCIITable = new byte[127];
    static {
        for(int i = 0; i < 127; i++) {
            String character = Character.toString((char) i);;
            US_ASCIITable[i] = character.getBytes(StandardCharsets.US_ASCII)[0];
        }
    }

    private void getBytesUS_ASCIILookupTable(char[] sample, byte[] bytes) {
        for(int i = 0; i < sample.length; i++) {
            bytes[i] = US_ASCIITable[sample[i]];
        }
    }

    private long getBytesUS_ASCIILookupTable(String sample, int iterations) {
        final long start = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            byte[] bytes = new byte[sample.length()];
            getBytesUS_ASCIILookupTable(sample.toCharArray(), bytes);
        }
        return System.currentTimeMillis() - start;
    }

    private long encodeUS_ASCIILookupTable(String sample, byte[] bytes, int iterations) {
        final long start = System.currentTimeMillis();
        for(int i = 0; i < iterations; i++) {
            getBytesUS_ASCIILookupTable(sample.toCharArray(), bytes);
        }
        return System.currentTimeMillis() - start;
    }
}