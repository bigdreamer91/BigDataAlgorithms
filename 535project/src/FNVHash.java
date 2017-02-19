

/**
 *
 * @author Lei Qi & Geethanjali Jeevanatham
 */


public class FNVHash {

    private static final long FNV_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV_64_PRIME = 0x100000001b3L;

    public FNVHash() {}   

    public long hash64(String s) {
        long rv = FNV_64_INIT;
        int len = s.length();
        for(int i = 0; i < len; i++) {
            rv ^= s.charAt(i);
            rv *= FNV_64_PRIME;
        }
        return rv;
    }

    public static void main(String[] args)
	{
		String s = "qilei";
		FNVHash func = new FNVHash();
		System.out.println(func.hash64(s) % 20000000);
	}
}

   