package poi.tomtom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
/**
<h3>Implicit Dictionary.</h3> 
Presents a dictionary with implicit transposition table.
<p/>
<table border=1>
  <tr><th>b-tree sequence of bits</th><th>character</th></tr>
  <tr><td>1011                    </td><td>end of string</td></tr>
  <tr><td>0010                    </td><td>space</td></tr>
  <tr><td>01100001110111001001    </td><td>!</td></tr>
  <tr><td>010001100011            </td><td>"</td></tr>
  <tr><td>10101111110000100001110 </td><td>#</td></tr>
  <tr><td>101011111100001001100   </td><td>$</td></tr>
  <tr><td>0000101101110111010011  </td><td>%</td></tr>
  <tr><td>0100010010              </td><td>&amp</td></tr>
  <tr><td>0100011010              </td><td>'</td></tr>
  <tr><td>01001010011             </td><td>(</td></tr>
  <tr><td>01100001100             </td><td>)</td></tr>
  <tr><td>101011111100000100      </td><td>*</td></tr>
  <tr><td>00001011011111          </td><td>+</td></tr>
  <tr><td>1010111011              </td><td>,</td></tr>
  <tr><td>01001011                </td><td>-</td></tr>
  <tr><td>0000110                 </td><td>.</td></tr>
  <tr><td>0000001101              </td><td>/</td></tr>
  <tr><td>10101110001             </td><td>0</td></tr>
  <tr><td>00001011010             </td><td>1</td></tr>
  <tr><td>01000110111             </td><td>2</td></tr>
  <tr><td>10101110101             </td><td>3</td></tr>
  <tr><td>10101111111             </td><td>4</td></tr>
  <tr><td>010010100100            </td><td>5</td></tr>
  <tr><td>101000110110            </td><td>6</td></tr>
  <tr><td>101000110100            </td><td>7</td></tr>
  <tr><td>000010110110            </td><td>8</td></tr>
  <tr><td>101011100001            </td><td>9</td></tr>
  <tr><td>10101111110000011       </td><td>:</td></tr>
  <tr><td>00001011011101010100    </td><td>;</td></tr>
  <tr><td>10101111110000100001111 </td><td>&lt</td></tr>
  <tr><td>10101111110000100001100 </td><td>=</td></tr>
  <tr><td>10101111110000100001101 </td><td>&gt</td></tr>
  <tr><td>101011111100001010110   </td><td>?</td></tr>
  <tr><td>0000101101110111011110  </td><td>@</td></tr>
  <tr><td>1010110                 </td><td>A</td></tr>
  <tr><td>00001010                </td><td>B</td></tr>
  <tr><td>00000010                </td><td>C</td></tr>
  <tr><td>01100011                </td><td>D</td></tr>
  <tr><td>010010101               </td><td>E</td></tr>
  <tr><td>010001000               </td><td>F</td></tr>
  <tr><td>01000111                </td><td>G</td></tr>
  <tr><td>10100000                </td><td>H</td></tr>
  <tr><td>0000101100              </td><td>I</td></tr>
  <tr><td>0100010011              </td><td>J</td></tr>
  <tr><td>000000111               </td><td>K</td></tr>
  <tr><td>10100001                </td><td>L</td></tr>
  <tr><td>00001110                </td><td>M</td></tr>
  <tr><td>0000001100              </td><td>N</td></tr>
  <tr><td>0100011001              </td><td>O</td></tr>
  <tr><td>01000101                </td><td>P</td></tr>
  <tr><td>1010111000000           </td><td>Q</td></tr>
  <tr><td>01100000                </td><td>R</td></tr>
  <tr><td>0000100                 </td><td>S</td></tr>
  <tr><td>000011111               </td><td>T</td></tr>
  <tr><td>01100001101             </td><td>U</td></tr>
  <tr><td>000011110               </td><td>V</td></tr>
  <tr><td>011000010               </td><td>W</td></tr>
  <tr><td>01001010010101          </td><td>X</td></tr>
  <tr><td>1010111111001           </td><td>Y</td></tr>
  <tr><td>01000110000             </td><td>Z</td></tr>
  <tr><td>000010110111010111      </td><td>[</td></tr>
  <tr><td>011000011101110010000   </td><td>\</td></tr>
  <tr><td>000010110111011100      </td><td>]</td></tr>
  <tr><td>101011111100000101001101</td><td>^</td></tr>
  <tr><td>0000101101110101011     </td><td>_</td></tr>
  <tr><td>011000011101110011      </td><td>`</td></tr>
  <tr><td>0011                    </td><td>a</td></tr>
  <tr><td>101010                  </td><td>b</td></tr>
  <tr><td>11010                   </td><td>c</td></tr>
  <tr><td>01101                   </td><td>d</td></tr>
  <tr><td>111                     </td><td>e</td></tr>
  <tr><td>0100100                 </td><td>f</td></tr>
  <tr><td>010011                  </td><td>g</td></tr>
  <tr><td>000001                  </td><td>h</td></tr>
  <tr><td>0111                    </td><td>i</td></tr>
  <tr><td>000010111               </td><td>j</td></tr>
  <tr><td>0000000                 </td><td>k</td></tr>
  <tr><td>00011                   </td><td>l</td></tr>
  <tr><td>010000                  </td><td>m</td></tr>
  <tr><td>1001                    </td><td>n</td></tr>
  <tr><td>1000                    </td><td>o</td></tr>
  <tr><td>011001                  </td><td>p</td></tr>
  <tr><td>1010111001              </td><td>q</td></tr>
  <tr><td>0101                    </td><td>r</td></tr>
  <tr><td>00010                   </td><td>s</td></tr>
  <tr><td>1100                    </td><td>t</td></tr>
  <tr><td>11011                   </td><td>u</td></tr>
  <tr><td>1010011                 </td><td>v</td></tr>
  <tr><td>10100010                </td><td>w</td></tr>
  <tr><td>1010001100              </td><td>x</td></tr>
  <tr><td>01100010                </td><td>y</td></tr>
  <tr><td>1010010                 </td><td>z</td></tr>
  <tr><td>101011111100000101000010</td><td>{</td></tr>
  <tr><td>&nbsp;                  </td><td>|</td></tr>
  <tr><td>101011111100000101000000</td><td>}</td></tr>
  <tr><td>101011111100000101000001</td><td>~</td></tr>

  <tr><td>101011111100001000111111</td><td>&#x0095;  (U+0095) bullet</td></tr>
  <tr><td>0000101101110111011111  </td><td>&#x0098;  (U+0098) tilde</td></tr>
  <tr><td>101011111100001000100111</td><td>&#x00A1;  (U+00A1) inv exclamation</td></tr>
  <tr><td>101011111100001000100100</td><td>&#x00A2;  (U+00A2) cent</td></tr>
  <tr><td>101011111100001000100101</td><td>&#x00A3;  (U+00A3) pound</td></tr>
  <tr><td>101011111100001000111000</td><td>&#x00A6;  (U+00A6) vertical bar</td></tr>
  <tr><td>011000011101110000      </td><td>&#x00AF;  (U+00AF) macron</td></tr>
  <tr><td>000010110111010101010   </td><td>&#x00B0;  (U+00B0) degree</td></tr>
  <tr><td>101011111100001000111100</td><td>&#x00B4;  (U+00B4) acute</td></tr>
  <tr><td>101011111100001000111110</td><td>&#x00B8;  (U+00B8) cedil</td></tr>
  <tr><td>0110000111011100101     </td><td>&#x00BA;  (U+00BA) masculine </td></tr>
  <tr><td>10101111110000100000001 </td><td>&#x00BF;  (U+00BF) inv question</td></tr>
  <tr><td>000010110111010110      </td><td>&#x00C0;  (U+00C0)</td></tr>
  <tr><td>101011111101101         </td><td>&#x00C1;  (U+00C1)</td></tr>
  <tr><td>000010110111011101010   </td><td>&#x00C2;  (U+00C2)</td></tr>
  <tr><td>101011111100001001101   </td><td>&#x00C3;  (U+00C3)</td></tr>
  <tr><td>1010001101010           </td><td>&#x00C4;  (U+00C4)</td></tr>
  <tr><td>01100001110110          </td><td>&#x00C5;  (U+00C5)</td></tr>
  <tr><td>1010111111000010100     </td><td>&#x00C6;  (U+00C6)</td></tr>
  <tr><td>011000011101110010001   </td><td>&#x00C7;  (U+00C7)</td></tr>
  <tr><td>0100101001010001        </td><td>&#x00C8;  (U+00C8)</td></tr>
  <tr><td>1010111111011000        </td><td>&#x00C9;  (U+00C9)</td></tr>
  <tr><td>10101111110000100000110 </td><td>&#x00CA;  (U+00CA)</td></tr>
  <tr><td>00001011011101110100100 </td><td>&#x00CB;  (U+00CB)</td></tr>
  <tr><td>000010110111011101011   </td><td>&#x00CC;  (U+00CC)</td></tr>
  <tr><td>01100001110111010       </td><td>&#x00CD;  (U+00CD)</td></tr>
  <tr><td>00001011011101110100101 </td><td>&#x00CE;  (U+00CE)</td></tr>
  <tr><td>101011111100001000010   </td><td>&#x00CF;  (U+00CF)</td></tr>
  <tr><td>101011111100001000110100</td><td>&#x00D0;  (U+00D0)</td></tr>
  <tr><td>10101111110000101010    </td><td>&#x00D1;  (U+00D1)</td></tr>
  <tr><td>000010110111011101000   </td><td>&#x00D2;  (U+00D2)</td></tr>
  <tr><td>00001011011101110110    </td><td>&#x00D3;  (U+00D3)</td></tr>
  <tr><td>101011111100001010111   </td><td>&#x00D4;  (U+00D4)</td></tr>
  <tr><td>101011111100001000110101</td><td>&#x00D5;  (U+00D5)</td></tr>
  <tr><td>0100011011011           </td><td>&#x00D6;  (U+00D6)</td></tr>
  <tr><td>&nbsp;                  </td><td>&#x00D7;  (U+00D7)</td></tr>
  <tr><td>011000011101111         </td><td>&#x00D8;  (U+00D8)</td></tr>
  <tr><td>000010110111011101110   </td><td>&#x00D9;  (U+00D9)</td></tr>
  <tr><td>011000011101110001      </td><td>&#x00DA;  (U+00DA)</td></tr>
  <tr><td>1010111111000001011011  </td><td>&#x00DB;  (U+00DB)</td></tr>
  <tr><td>00001011011100          </td><td>&#x00DC;  (U+00DC)</td></tr>
  <tr><td>1010111111000010010     </td><td>&#x00DD;  (U+00DD)</td></tr>
  <tr><td>101011111100001000001011</td><td>&#x00DE;  (U+00DE)</td></tr>
  <tr><td>101011110               </td><td>&#x00DF;  (U+00DF)</td></tr>
  <tr><td>0110000111010           </td><td>&#x00E0;  (U+00E0)</td></tr>
  <tr><td>010001101100            </td><td>&#x00E1;  (U+00E1)</td></tr>
  <tr><td>010001101101001         </td><td>&#x00E2;  (U+00E2)</td></tr>
  <tr><td>01000110110101          </td><td>&#x00E3;  (U+00E3)</td></tr>
  <tr><td>1010111110              </td><td>&#x00E4;  (U+00E4)</td></tr>
  <tr><td>1010001101011           </td><td>&#x00E5;  (U+00E5)</td></tr>
  <tr><td>010001100010            </td><td>&#x00E6;  (U+00E6)</td></tr>
  <tr><td>1010111000001           </td><td>&#x00E7;  (U+00E7)</td></tr>
  <tr><td>101000110111            </td><td>&#x00E8;  (U+00E8)</td></tr>
  <tr><td>1010001111              </td><td>&#x00E9;  (U+00E9)</td></tr>
  <tr><td>0000101101110110        </td><td>&#x00EA;  (U+00EA)</td></tr>
  <tr><td>10101111110001          </td><td>&#x00EB;  (U+00EB)</td></tr>
  <tr><td>01001010010110          </td><td>&#x00EC;  (U+00EC)</td></tr>
  <tr><td>10101110100             </td><td>&#x00ED;  (U+00ED)</td></tr>
  <tr><td>00001011011101111       </td><td>&#x00EE;  (U+00EE)</td></tr>
  <tr><td>01100001110111011       </td><td>&#x00EF;  (U+00EF)</td></tr>
  <tr><td>10101111110000100000111 </td><td>&#x00F0;  (U+00F0)</td></tr>
  <tr><td>1010111111010           </td><td>&#x00F1;  (U+00F1)</td></tr>
  <tr><td>10101111110111          </td><td>&#x00F2;  (U+00F2)</td></tr>
  <tr><td>01100001111             </td><td>&#x00F3;  (U+00F3)</td></tr>
  <tr><td>00001011011110          </td><td>&#x00F4;  (U+00F4)</td></tr>
  <tr><td>000010110111010100      </td><td>&#x00F5;  (U+00F5)</td></tr>
  <tr><td>1010001110              </td><td>&#x00F6;  (U+00F6)</td></tr>
  <tr><td>&nbsp;                  </td><td>&#x00F7;  (U+00F7)</td></tr>
  <tr><td>011000011100            </td><td>&#x00F8;  (U+00F8)</td></tr>
  <tr><td>010001101101000         </td><td>&#x00F9;  (U+00F9)</td></tr>
  <tr><td>01001010010111          </td><td>&#x00FA;  (U+00FA)</td></tr>
  <tr><td>101011111100001011      </td><td>&#x00FB;  (U+00FB)</td></tr>
  <tr><td>0100101000              </td><td>&#x00FC;  (U+00FC)</td></tr>
  <tr><td>0100101001010000        </td><td>&#x00FD;  (U+00FD)</td></tr>
  <tr><td>101011111100001000001001</td><td>&#x00FE;  (U+00FE)</td></tr>
  <tr><td>&nbsp;                  </td><td>&#x00FF;  (U+00FF)</td></tr>
  <tr><td>101011111100001000110111</td><td>&#x0141;  (U+0141)</td></tr>
  <tr><td>0000101101110100        </td><td>&#x0142;  (U+0142)</td></tr>

  <tr><td>000010110111010101011   </td><td>?</td></tr>
  <tr><td>010010100101001         </td><td>?</td></tr>
  <tr><td>1010111111000000        </td><td>?</td></tr>
  <tr><td>101011111100000101000011</td><td>?</td></tr>
  <tr><td>1010111111000001010001??</td><td>?</td></tr>
  <tr><td>1010111111000001010010??</td><td>?</td></tr>
  <tr><td>101011111100000101001100</td><td>?</td></tr>
  <tr><td>10101111110000010100111?</td><td>?</td></tr>
  <tr><td>10101111110000010101????</td><td>?</td></tr>
  <tr><td>101011111100000101100???</td><td>?</td></tr>
  <tr><td>1010111111000001011010  </td><td>?</td></tr>
  <tr><td>10101111110000010111????</td><td>?</td></tr>
  <tr><td>10101111110000100000000 </td><td>?</td></tr>
  <tr><td>1010111111000010000001? </td><td>?</td></tr>
  <tr><td>101011111100001000001000</td><td>?</td></tr>
  <tr><td>101011111100001000001010</td><td>?</td></tr>
  <tr><td>1010111111000010001000??</td><td>?</td></tr>
  <tr><td>101011111100001000100110</td><td>?</td></tr>
  <tr><td>101011111100001000101???</td><td>?</td></tr>
  <tr><td>1010111111000010001100??</td><td>?</td></tr>
  <tr><td>101011111100001000110110</td><td>?</td></tr>
  <tr><td>101011111100001000111001</td><td>?</td></tr>
  <tr><td>10101111110000100011101?</td><td>?</td></tr>
  <tr><td>101011111100001000111101</td><td>?</td></tr>
  <tr><td>10101111110000100111    </td><td>?</td></tr>
  <tr><td>1010111111000011        </td><td>'</td></tr>
  <tr><td>1010111111011001        </td><td>?</td></tr>
<table>
<br/>
<i>Note : this table is not complete, and must be complete if others sequences are discovered</i>

*/
public class ImplDictionary extends Dictionary {

	static final String DEFAULT_XML = "codes.xml";

	public ImplDictionary() {
		this(System.getProperty("user.dir") + File.separator + "etc" + File.separator + DEFAULT_XML);
	}

	public ImplDictionary(String fileName) {
		loadFromXml(fileName);
	}

	public void loadFromXml(String fileName) {
		Properties codes = new Properties();
		try {
			codes.loadFromXML(new FileInputStream(fileName));
			for (String key: codes.stringPropertyNames()) {
				if (key.contains("?")) {
					continue;
				}
				String value = codes.getProperty(key);
				put(new BitContainer(key), value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Decodes POI description.
	 * Try to catch unknown codes.
	 *
	 * @return decoded description
	 */
	@Override
	protected String decode(byte[] name) {
		try {
			return super.decode(name);
		} catch (BitException e) {
			/** all process over again */
			BitContainer bits = new BitContainer(name.clone(), true);
			StringBuffer result = new StringBuffer();
			try {
				while (!bits.isEmpty()) {
					BinaryTree<String> node = binTree.find(bits);
					String value = node.getValue();
					if (value.equals(EOS)) {
						return result.toString();
					}
					result.append(value);
					bits.delete(node.getKey().length());
				}
			} catch (BitException ex) {
				int index = Integer.parseInt(ex.getMessage());
				if (bits.toString().substring(index).startsWith("1010111111000011") && // EE 840.2561
						result.toString().equals("d")) // 'artagnan
					return result.toString();
				if (bits.toString().substring(index).startsWith("10101111110000100000010") && // Iberia 840.2561
						(result.toString().contains("clínica") || // dr&#x00AA;
						result.toString().equals("dr"))) // &#x00AA;
					return result.toString();
				if (bits.toString().substring(index).startsWith("10101111110000100000000") && // Iberia 840.2563
						result.toString().endsWith("l")) // ?l
					return result.toString();
				if (bits.toString().substring(index).startsWith("1010111111000001011010")) //? Greece 840.2561
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000110010") && // Southern Africa 840.2587
						(result.toString().endsWith("alte br") || // ücke lodge-selfcatering
						result.toString().equals("kreuz des s") || // ücke lodge-selfcatering
						result.toString().equals("gr"))) // ünewald water hole
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000001000") && // Southern Africa 840.2587
						result.toString().endsWith("jan m")) // hr secondary school
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000100011") && // UK&RI 840.2563
						result.toString().endsWith("d")) // un r'
					return result.toString();
				if (bits.toString().substring(index).startsWith("1010111111000011")) //' Brazil 850.2792
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000101001")) //– Brazil 850.2792
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100000101000011")) //– Brazil 850.2792
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100000101011000") && // CEE 850.2782
						result.toString().endsWith("auto")) //?>420-566630303
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000111010") && // Europe 2GB 850.2801
						result.toString().endsWith("asematien lã")) // Lääkärikeskus
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100000101011000") && // CEE 860.3126
						(result.toString().endsWith("sapori") || // ?nel verde
						result.toString().equals("quelli che mangiano e") || // ?
						result.toString().equals("di") || // ? vino
						result.toString().equals("come una volta ") || // ?
						result.toString().equals("pasticciando") || // ?
						result.toString().equals("re di pi") || // ?zze
						result.toString().equals("if") || // ?
						result.toString().equals("du pareil ") || // ? au même italia
						result.toString().equals("quattro con "))) // ?
					return result.toString();
				if (bits.toString().substring(index).startsWith("10101111110000100000000") && // CEE 860.3126
						result.toString().endsWith("mark")) // ?s restaurant
					return result.toString();
				if (bits.toString().substring(index).startsWith("10101111110000100000011") && // CEE 860.3126
						result.toString().endsWith("candy")) // ?s droom>49-4246-964634
					return result.toString();
				if (bits.toString().substring(index).startsWith("1010111111000000") && // Coimbra 715.1732
						result.toString().endsWith("repsol oliveira de azemã")) // ?is n1
					return result.toString();
				if (bits.toString().substring(index).startsWith("101011111100001000110010") && // Faro 715.1732
						result.toString().endsWith("galp vila real de santo antã")) // ?nio rua francisco sã¡ carneiro
					return result.toString();
				throw new BitException(catchBits(result.toString(), bits.toString().substring(index).toString()));
			}
			return result.toString();
		}
	}

	private String catchBits(String result, String bits) {
		Set<String> unknownKeys = binTree.unknownKeys();
		String found = "";
		for (String key: unknownKeys) {
			/** looking for longest key that fit */
			if (bits.startsWith(key)) {
				found = key;
				break;
			}
		}
		if (found.isEmpty()) {
			return result + " - unknown (" + (result.length() + 1) + ") " + bits;
		}
		List<String> suggestions = new ArrayList<String>();
		while (found.length() < 25) {
			try {
				if (bits.length() < found.length()) break;
				String str = bits.substring(found.length());
				StringBuffer flip = new StringBuffer();
				for (int i = 0; i < ((str.length() + 7) / 8); i++) {
					for (int j = 7; j >= 0; j--) {
						if (i * 8 + j < str.length()) {
							flip.append(str.charAt(i * 8 + j));
						} else {
							flip.append("0");
						}
					}
				}
				BitContainer rest = new BitContainer(flip.toString());
				suggestions.add("key " + found + " : " + result + "?" + decode(rest.buff()));
			} catch (Exception e) {
				/** do nothing */
			}
			if (bits.length() <= found.length()) break;
			found += bits.substring(found.length(), found.length() + 1);
		}
		if (!suggestions.isEmpty()) {
			String str = "";
			for (String suggestion: suggestions)
				str += "\n" + suggestion;
			return str;
		}
		return result + " - unknown (" + (result.length() + 1) + ") " + bits;
	}
}
