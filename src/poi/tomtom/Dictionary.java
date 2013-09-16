package poi.tomtom;

/**
<h3>Dictionary.</h3>

This dictionary used to decode {@link Poi09 Record 09} and {@link Poi13 Record 13}.
*/
public abstract class Dictionary {

	protected BinaryTree<String> binTree = new BinaryTree<String>();
	protected RedBlackTree<String> rbTree = new RedBlackTree<String>();

	public BinaryTree<String> getBinTree() {
		return binTree;
	}

	protected void put(BitContainer bits, String str) {
		RedBlackTree<String> value = rbTree.add(str);
		binTree.put(bits, str);
	}

	/**
	 * Decodes POI description.

<p/>
The compressed method is the following :
<p/>
Each character of the POI description consume a variable number a bits, using 
a transposition table (see {@link Enc09 Enc09} for the transposition table). 
<p/>
The block of data has to be used as a series of bits. There is a special sequence 
of bit that have a special meaning (End of String).
<br/>
The way the bits are arranged in the block is a little special : each byte must 
reverse the position of his bits as the following :
<br/>
The byte (binary format) 0bABCDEFGH will be transform in 0bHGFEDCBA
<br/>
So 0b10010010 will be transform in 0b01001001
<p/>
Then, the data can be decoded, using the transposition table.
<br/>
For example
<pre>
starting with the bytes : 0x68 0x78 0x3c 0xb2 0x01
This can be transform in binary : 01101000 01111000 00111100 10010010 00000001
and then revert                 : 00010110 00011110 00111100 01001101 10000000
and then, using b-tree table    : 00010 1100 0011 1100 0111 1000 1001 1011 (0000000)
and then                        : station
</pre>

	 *
	 * @return decoded description
	 */
	protected String decode(byte[] name) {
		BitContainer bits = new BitContainer(name.clone(), true);
		StringBuffer result = new StringBuffer();
		try {
			while (!bits.isEmpty()) {
				BinaryTree<String> node = binTree.find(bits);
				String value = node.getValue();
				if (value.length() == 0) {
					/** eos */
					return result.toString();
				}
				result.append(value);
				bits.delete(node.getKey().length());
			}
			return result.toString();
		} catch (BitException e) {
			throw new BitException(e.getMessage() + "," + result.toString());
		}
	}

	/** Encode plain text to byte array */
	byte[] encode(String description) {
		BitContainer result = new BitContainer(new byte[0], true);
		int i = 0;
		while (i < description.length()) {
			BinaryTree<String> found = null; 
			for (BitContainer key: binTree.keySet()) {
				BinaryTree<String> node = binTree.find(key);
				if (description.substring(i).startsWith(node.getValue())) {
					if ((found == null) || (node.getValue().length() > found.getValue().length())) {
						/** next longest node fron the tree*/
						found = node;
					}
				}
			}
			result.append(found.getKey());
			i += found.getValue().length();
		}
		/** eol */
		for (BitContainer key: binTree.keySet()) {
			BinaryTree<String> node = binTree.find(key);
			if (node.getValue().isEmpty()) {
				result.append(node.getKey());
				break;
			}
		}
		return result.buff();
	}
}
