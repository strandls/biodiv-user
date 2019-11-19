package com.strandls.user.util;

public class PasswordEncoder {

	    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
	                                                '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	    private static final int HEX_RIGHT_SHIFT_COEFFICIENT = 4;
	    private static final int HEX_HIGH_BITS_BITWISE_FLAG = 0x0f;


	    private static String encodingAlgorithm = "MD5";

	    private static String characterEncoding = "UTF-8";

	    /**
	     * Instantiates a new default password encoder.
	     *
	     * @param encodingAlgorithm the encoding algorithm
	     */
	    public PasswordEncoder(String encodingAlgorithm) {
	        this.encodingAlgorithm = encodingAlgorithm;
	    }

	/*
	 * public static String encode(String password) { if (password == null) { return
	 * null; }
	 * 
	 * MessageDigest messageDigest; try {
	 * 
	 * messageDigest = MessageDigest.getInstance(encodingAlgorithm);
	 * messageDigest.update(password.getBytes(characterEncoding)); final byte[]
	 * digest = messageDigest.digest(); return getFormattedText(digest);
	 * 
	 * } catch (NoSuchAlgorithmException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } catch (UnsupportedEncodingException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return null; }
	 */

	    /**
	     * Takes the raw bytes from the digest and formats them correct.
	     *
	     * @param bytes the raw bytes from the digest.
	     * @return the formatted bytes.
	     */
	    private static String getFormattedText(final byte[] bytes) {
	        final StringBuilder buf = new StringBuilder(bytes.length * 2);

	        for (int j = 0; j < bytes.length; j++) {
	            buf.append(HEX_DIGITS[(bytes[j] >> HEX_RIGHT_SHIFT_COEFFICIENT) & HEX_HIGH_BITS_BITWISE_FLAG]);
	            buf.append(HEX_DIGITS[bytes[j] & HEX_HIGH_BITS_BITWISE_FLAG]);
	        }
	        return buf.toString();
	    }	
}
