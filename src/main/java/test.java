
public class test {

	static long decode(String s, String symbols) {
	    final int B = symbols.length();
	    long num = 0;
	    for (char ch : s.toCharArray()) {
	        num *= B;
	        num += symbols.indexOf(ch);
	    }
	    return num;
	}
	static String encode(long num, String symbols) {
	    final int B = symbols.length();
	    StringBuilder sb = new StringBuilder();
	    while (num != 0) {
	        sb.append(symbols.charAt((int) (num % B)));
	        num /= B;
	    }
	    return sb.reverse().toString();
	}
	public static void main(String[] args) {
	    //String range = "0123456789abcdefghijklmnopABCD#";
	    //System.out.println(decode(encode(123456789L, range), range));
	    // prints "123456789"

	    //System.out.println(encode(255L, "0123456789ABCDEF"));
	    // prints "FF"

	    System.out.println(encode(2147483647L, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
	    System.out.println(decode("9SUA9T", "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
	    // prints "64"
	}

}
