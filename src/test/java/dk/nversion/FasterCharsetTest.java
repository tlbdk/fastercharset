package dk.nversion;

import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import sun.nio.cs.ArrayEncoder;

public class FasterCharsetTest {

    @Test
    public void testGetBytes() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        int warmup = 10000000;
        int iterations = 1000000;

        for(Object bytes : new Object[] { null, new byte[sample.length()] } ) {
            System.out.println(bytes == null ? "** String.getBytes() **" : "** Charset.Encode() **");
            System.out.println( "getBytes(US_ASCII): " + getBytes(StandardCharsets.US_ASCII, sample, (byte[])bytes, iterations, warmup));
            System.out.println( "getBytes(FasterCharset(US_ASCII)): " + getBytes(new FasterCharset(StandardCharsets.US_ASCII), sample,  (byte[])bytes, iterations, warmup));
            System.out.println( "getBytes(ISO_8859_1): " + getBytes(StandardCharsets.ISO_8859_1, sample, (byte[])bytes, iterations, warmup));
            System.out.println( "getBytes(FasterCharset(ISO_8859_1)): " + getBytes(new FasterCharset(StandardCharsets.ISO_8859_1), sample, (byte[])bytes, iterations, warmup));
            System.out.println( "getBytes(CP277): " + getBytes(Charset.forName("cp277"), sample, (byte[])bytes, iterations, warmup));
            System.out.println( "getBytes(FasterCharset(CP277)): " + getBytes(new FasterCharset(Charset.forName("cp277")), sample, (byte[])bytes, iterations, warmup));
        }
    }

    private long getBytes(Charset charset, String sample, byte[] bytes, int iterations, int warmup) {
        long start;
        long result = Long.MAX_VALUE;
        if(bytes == null) {
            for (int i = 0; i < warmup; i++) {
                bytes = sample.getBytes(charset);
            }
            for(int j = 0; j < 3; j++) {
                System.gc();
                start = System.currentTimeMillis();
                for (int i = 0; i < iterations; i++) {
                    bytes = sample.getBytes(charset);
                }
                long subResult = System.currentTimeMillis() - start;
                result = subResult < result ? subResult : result;
            }

        } else {
            ArrayEncoder encoder = ((ArrayEncoder)charset.newEncoder());
            for (int i = 0; i < warmup; i++) {
                encoder.encode(sample.toCharArray(), 0, sample.length(), bytes);
            }
            for(int i = 0; i < 3; i++) {
                System.gc();
                start = System.currentTimeMillis();
                for (int j = 0; j < iterations; j++) {
                    encoder.encode(sample.toCharArray(), 0, sample.length(), bytes);
                }
                long subResult = System.currentTimeMillis() - start;
                result = subResult < result ? subResult : result;
            }
        }
        return result;
    }

    @Test
    public void testGetBytesUS_ASCII() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes = sample.getBytes(new FasterCharset(StandardCharsets.US_ASCII));
        assertArrayEquals(sample.getBytes(StandardCharsets.US_ASCII), bytes);
    }

    @Test
    public void testGetBytesISO_8859_1() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes = sample.getBytes(new FasterCharset(StandardCharsets.ISO_8859_1));
        assertArrayEquals(sample.getBytes(StandardCharsets.ISO_8859_1), bytes);
    }

    @Test
    public void testGetBytesCP277() throws Exception {
        String sample = "The quick brown fox jumps over the lazy dog";
        byte[] bytes = sample.getBytes(new FasterCharset(Charset.forName("cp277")));
        assertArrayEquals(sample.getBytes("cp277"), bytes);
    }
}
