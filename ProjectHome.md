# Tomtom POI description #

**Important : POIs from Tomtom are copyrighted and you CAN'T use the information in this page to exploit-modify-diffuse natives POIs or do anything that Tomtom license prohibit.**

However, it seems that the license permit to replace native POI.DAT with your own file, based on private or free data.

Thanks to [Laurent Licour](http://www.licour.com)

## Poi Record description ##

There are 3 kinds of records :

  * Info Records : ...
  * Area Records : they divide the map in subsquares. This kind of record is a container of other records - area record or POIs records
  * POIs Records : they contain a POI.

## Info Records ##

They are presented only in DAT file, but not in OV2.

### Category Records ###

The POIs are organized in categories. There are different categories (Restaurants, Petrol Station, Parking...) that are hard-coded.


The format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 4 bytes | **C:** The count of categories |
| 4 `*` C bytes | **D:** The id of categories |
| 4 `*` C bytes | **O:** The offset in file of categories |
| 4 bytes | **E:** The end of the categories |

Each category is encoded as :

  * a category ID : an unique integer that refer to the category
  * a category description : a simple string, depending on the language. This description is different in each language

_Note: This list is not complete._

| _id_ | _English description_ |
|:-----|:----------------------|
| 1C8E | Car repair/garage     |
| 1C8F | Petrol station        |
| 1C90 | Car rental facility   |
| 1C91 | Parking garage        |
| 1C92 | Hotel/Motel           |
| 1C93 | Restaurant            |
| 1C94 | Tourist information office |
| 1C95 | Museum/Art gallery    |
| 1C96 | Theater               |
| 1C97 | Cultural center       |
| 1C98 | Sports center         |
| 1C99 | Hospital/Clinic       |
| 1C9A | Police station        |
| 1C9C | Post office           |
| 1C9E | Primacy               |
| 1CA9 | Scenic/Panoramic view |
| 1CAA | Swimming pool         |
| 1CAB | Place of worship      |
| 1CAD | Casino                |
| 1CAE | Cinema                |
| 1CB5 | Winery                |
| 1CB8 | Ferry terminal        |
| 1CC0 | Camping ground        |
| 1CC5 | Embassy               |
| 1CC6 | Border crossing       |
| 1CC7 | Government office     |
| 1CC9 | Open parking          |
| 1CCD | Shopping center       |
| 1CCE | Stadium               |
| 1CD0 | Tourist attraction    |
| 1CD1 | College/University    |
| 1CD3 | City center           |
| 1CD4 | Railway station       |
| 1CD7 | Airport               |
| 1CD9 | Exhibition center     |
| 1CE3 | Rest area             |
| 1CE5 | Cash dispenser        |
| 2488 | Company               |
| 248D | Beach                 |
| 2490 | Ice skating ring      |
| 2491 | Shop                  |
| 2492 | Park and recreation area |
| 2493 | Courthouse            |
| 2494 | Mountain peak         |
| 2495 | Opera                 |
| 2497 | Concert hall          |
| 2499 | Tennis court          |
| 249B | Water sport           |
| 249D | Doctor                |
| 249E | Dentist               |
| 249F | Veterinarian          |
| 24A1 | Convention center     |
| 24A2 | Leisure center        |
| 24A3 | Nightlife             |
| 24A4 | Marina/Yacht basin    |
| 2648 |                       |
| 2649 | Legal/other           |
| 26AE | Amusement Park        |
| 26B2 | Church                |
| 26B6 | Car dealer            |
| 26B7 | Golf course           |
| 26B9 | Library               |
| 26C7 | Zoo                   |
| 26CA | Rent car parking      |
| 26CF | Mountain pass         |
| 26FC |                       |

## Area Records ##

The logic of Area Record is to divide the complete map (we say level 0) in more subsquares (level 1), each level 1's square is divide in more subsquares (level 2) and so on. There are many sublevels. The last Area Record contains several POI Records, but no more that 10 (this seems to be a rule). Also, the way the areas are cut is not fixed.

Each Area record has a specific size, depending of the POI density in the Area.

### Record 01 ###
This record is an **Area Record**. His goal is to determine a square.

The format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** the record type = **0x01** |
| 4 bytes | **S:** The total size of this record in bytes (including the 21 bytes header) |
| 4 bytes | **X1:** Longitude1 |
| 4 bytes | **Y1:** Latitude1 |
| 4 bytes | **X2:** Longitude2 |
| 4 bytes | **Y2:** Latitude2 |
| N bytes | **N:** Blocks of data containing other records |

Longitude and latitude are in decimal degrees. This value has to be divide by 100000.

## POI Records ##

A POI is a simple association of 3 kinds of data :

  * a longitude
  * a latitude
  * a description

There are multiple methods used by Tomtom to compress (or not) the description. There are as many POI record types as encoding methods.

### Record 02, 03 & 15 ###

This record is a **POI Record**. This record is used in .OV2 file. It is a plain text record
The format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x02**, **0x03** or **0x0f** |
| 4 bytes | **S:** the total size of this record |
| 4 bytes | **X:** Longitude |
| 4 bytes | **Y:** Latitude |
| S-13 bytes | **N:** Plain text POI description |

### Record 04 & 20 ###

This record is an **POI Record**. There is no POI description.

The format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x04** or **0x14** |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |

#### Encodes latitude ####

Latitudes in POI Records are encoded on 3 bytes unsigned value (little indian) (in decimal degrees).

The final value is obtain by this formula (considering X as the 3 bytes value) :
<pre>
Latitude = (X/100000 - 80)<br>
</pre>
#### Encodes longitude ####

Longitudes in POI Records are encoded on 3 bytes unsigned value (little indian) (in decimal degrees).

Longitude could be obtain with the same formula as latitude, but there is some corrective value: 3 bytes only let store 16777216 values (so nearly 167° on 360°). Depending on the area zone the POI exists (refer to his parent Area Record), an offset should be applied.

The primary value is obtain by this formula (considering X as the 3 bytes value) :

_Note : let compute integer value to minimize floating approximations_

Let apply this formula
<pre>
Longitude = (X - 8000000)<br>
</pre>
until Longitude inside the 2 longitude values stored in his parent Area Record.
If Longitude is under the -180°, then add 360°.

Europe don't have to applied these offset, but it's the case for USA.

For example :
<pre>
For an Area Record of (-165.12345, 55.12345) - (-151.12345, 58.12345)  (Alaska)<br>
The POI Longitude is : 0x6C9785  that is 8755052 in decimal<br>
8755052 - 8000000 =    755052<br>
755052 - 8000000 =  -7244948<br>
-7244948 - 8000000 = -15244948   the value !!!<br>
</pre>

### Record 05 & 21 ###

This Record is an POI Record. The POI description is a short numeric value.

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x05** or **0x15** |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| 2 bytes | **N:** unsigned 16 bits numeric value (little indian) |

### Record 06 & 22 ###

This Record is an POI Record. The POI description is a numeric value.

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x06** or **0x16** |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| 3 bytes | **N:** unsigned 24 bits numeric value (little indian) |

### Record 07 & 23 ###

This Record is an POI Record. The POI description is plain text. It isn't compressed.

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x07** or **0x17** |
| 1 byte  | **S:**  the size of this record, minus 8 (the size of the fixed part of the record) |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| S bytes | **N:** Plain text POI description |

### Record 08 & 24 ###

This Record is an POI Record. The POI description is compressed (compression type 08)

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x08** or **0x18** |
| 1 byte  | **S:**  the size of this record, minus 8 (the size of the fixed part of the record) |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| S bytes | **N:** encoded POI description |

#### Decodes POI description (type 8) ####

The compressed method is the following :

  * if (next byte & 0x80) == 0
> > The next 2 bytes can be decompressed in 3 letters, each letter encoded on 5 bits (1 + 3\*5 = 2 bytes)
> > These 5 bits (32 values) must be transposed in letter (see letters0 for transposition table).


> these 2 bytes (binary format) present 3 characters : 0AAAAABB BBBCCCCC

<pre>
example :<br>
29 64 2C E8 24<br>
00101001 01100100   00101100 11101000   0100100<br>
0 01010 01011 00100 0 01011 00111 01000 0 01001 (00)<br>
s t a t i o n<br>
</pre>

  * if (next byte & 0x80) == 0x80
> > The next byte encode one character, with a transcription of the first quartet (4 high bits) (see letters8 for transposition table)
> > The second quartet is remain unchanged

#### Transposition table when (byte & 0x80) == 0 ####

This table refers to what 5 bits must be transposed in what letter.

| _5 bits value_ | _Transposition letter_ |
|:---------------|:-----------------------|
| 00000          | not used               |
| 00001          | .                      |
| 00010          | space                  |
| 00011          | S                      |
| 00100          | a                      |
| 00101          | e                      |
| 00110          | r                      |
| 00111          | i                      |
| 01000          | o                      |
| 01001          | n                      |
| 01010          | s                      |
| 01011          | t                      |
| 01100          | l                      |
| 01101          | d                      |
| 01110          | c                      |
| 01111          | h                      |
| 10000          | u                      |
| 10001          | m                      |
| 10010          | g                      |
| 10011          | p                      |
| 10100          | b                      |
| 10101          | k                      |
| 10110          | f                      |
| 10111          | z                      |
| 11000          | v                      |
| 11001          | A                      |
| 11010          | C                      |
| 11011          | B                      |
| 11100          | M                      |
| 11101          | P                      |
| 11110          | G                      |
| 11111          | -                      |

#### Transposition table when (byte & 0x80) == 0x80 ####

This table refers transcription of the first quartet (4 high bits)

| _quartet source_ | _quartet destination_ |
|:-----------------|:----------------------|
| 1000             | 0010                  |
| 1001             | 0011                  |
| 1010             | 0100                  |
| 1011             | 0101                  |
| 1100             | 0110                  |
| 1101             | 0111                  |
| 1110             | 1110                  |
| 1111             | 1111                  |

### Record 09 & 25 ###

This Record is an POI Record. The POI description is compressed (compression type 09)

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x09** or **0x19** |
| 1 byte  | **S:**  the size of this record, minus 8 (the size of the fixed part of the record) |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| S bytes | **N:** encoded POI description |

#### Decodes POI description (type 09) ####

The compressed method is the following :

Each character of the POI description consume a variable number a bits, using a transposition table (see Enc09 for the transposition table).

The block of data has to be used as a series of bits. There is a special sequence of bit that have a special meaning (End of String).

The way the bits are arranged in the block is a little special : each byte must reverse the position of his bits as the following :

The byte (binary format) 0bABCDEFGH will be transform in 0bHGFEDCBA

So 0b10010010 will be transform in 0b01001001

Then, the data can be decoded, using the transposition table.

For example
<pre>
starting with the bytes : 0x68 0x78 0x3c 0xb2 0x01<br>
This can be transform in binary : 01101000 01111000 00111100 10010010 00000001<br>
and then revert                 : 00010110 00011110 00111100 01001101 10000000<br>
and then, using b-tree table    : 00010 1100 0011 1100 0111 1000 1001 1011 (0000000)<br>
and then                        : station<br>
</pre>

### Record 10 & 26 ###

This Record is an POI Record. The POI description is compressed (compression type 10)

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x0A** or **0x1A** |
| 1 byte  | **S:**  the size of this record, minus 8 (the size of the fixed part of the record) |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| S bytes | **N:** encoded POI description |

#### Decodes POI description (type 10) ####

The compressed method is the following :

Each pairs of 2 bytes (little indian encoding) can be decompressed in 3 character. The optional last byte give one character.
The characters can be obtained by calculate the modulus-40 three times on the 2 bytes, and 1 time on the 1 byte. This give 3 indexes on a transposition table :

if modulus is 0, this is consider as the end of string.

For example
<pre>
starting with the bytes : 0x59 0x20, this give the value 0x2059 (8281)<br>
get the modulus 1 : 8281 % 40 = 1           : A<br>
get the modulus 2 : (8281/40) % 40 = 7      : G<br>
get the modulus 3 : ((8281/40)/40) % 40 = 5 : E<br>
</pre>

#### Record 10 transposition table ####

| _code_ | _text character_ |
|:-------|:-----------------|
| 0      | a                |
| 1      | b                |
| 2      | c                |
| 3      | d                |
| 4      | e                |
| 5      | f                |
| 6      | g                |
| 7      | h                |
| 8      | i                |
| 9      | j                |
| 10     | k                |
| 11     | l                |
| 12     | m                |
| 13     | n                |
| 14     | o                |
| 15     | p                |
| 16     | q                |
| 17     | r                |
| 18     | s                |
| 19     | t                |
| 20     | u                |
| 21     | v                |
| 22     | w                |
| 23     | x                |
| 24     | y                |
| 25     | z                |
| 26     | 0                |
| 27     | 1                |
| 28     | 2                |
| 29     | 3                |
| 30     | 4                |
| 31     | 5                |
| 32     | 6                |
| 33     | 7                |
| 34     | 8                |
| 35     | 9                |
| 36     | space            |
| 37     | .                |
| 38     | -                |

### Record 12 & 28 ###

This Record is an POI Record. The POI description is compressed (compression type 12)

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x0C** or **0x1C** |
| 1 byte  | **S:**  the size of this record, minus 8 (the size of the fixed part of the record) |
| 3 bytes | **X:** [Encoded Longitude](#Encodes_longitude.md) |
| 3 bytes | **Y:** [Encoded Latitude](#Encodes_latitude.md) |
| S bytes | **N:** encoded POI description |

#### Decodes POI description (type 12) ####

These records have 2 blocks of data

  * Text Block that contain the POI description (alphabetic characters). These characters are coded on 5 bits
  * Phone number (numeric characters). These characters are coded on 4 bits

Each block have a special 5-bit code that is an end of string. After this code, the 2nd block directly begin, until the special 4-bit code that is the end of the phone number.

The way the bits are arranged in the block is a little special : the coded string data have to be first reverse. Then, the group of 5 bits (and then 4 bits) have to be parse from the end of the reversed data, as the following :
The bytes (binary format) 0bABCDEFGH 0bIJKLMNOP 0bQRSTUVWX will be reversed in 0bQRSTUVWX 0bIJKLMNOP 0bABCDEFGH, and the the groups of 5-bits will be (QRST) UVWXI JKLMN OPABC DEFGH

Then, the data can be decoded, using the transposition table, and the resulting clear text have to be reverse.

For example
<pre>
starting with the bytes : 0x51 0x02 0x89 0x5c 0xd3 0x21 0x03<br>
This can be transform in binary : 01010001 00000010 10001001 01011100 11010011 00100001 00000011<br>
and the revert string           : 00000011 00100001 11010011 01011100 10001001 00000010 01010001<br>
and then, using the tables      : 0000 0011 0010 0001 11010 01101 01110 01000 10010 00000 10010 10001<br>
and then                        : "210" & "noitats"<br>
and then, using b-tree table    : Description:"station" & Phone Number:"012"<br>
</pre>

#### Record 12 transposition text table ####

**Caution** : this table DOES NOT contain letter **Q** !

| _5-bit code_ | _text character_ |
|:-------------|:-----------------|
| 00000        | a                |
| 00001        | b                |
| 00010        | c                |
| 00011        | d                |
| 00100        | e                |
| 00101        | f                |
| 00110        | g                |
| 00111        | h                |
| 01000        | i                |
| 01001        | j                |
| 01010        | k                |
| 01011        | l                |
| 01100        | m                |
| 01101        | n                |
| 01110        | o                |
| 01111        | p                |
| 10000        | r                |
| 10001        | s                |
| 10010        | t                |
| 10011        | u                |
| 10100        | v                |
| 10101        | w                |
| 10110        | x                |
| 10111        | y                |
| 11000        | z                |
| 11001        | space            |
| 11010        | end of string    |
| 11011        | (                |
| 11100        | )                |
| 11101        | &                |
| 11110        | '                |
| 11111        | -                |

#### Record 12 transposition numeric table ####

| _4-bit code_ | _Numeric character_ |
|:-------------|:--------------------|
| 0000         | End of String       |
| 0001         | 0                   |
| 0010         | 1                   |
| 0011         | 2                   |
| 0100         | 3                   |
| 0101         | 4                   |
| 0110         | 5                   |
| 0111         | 6                   |
| 1000         | 7                   |
| 1001         | 8                   |
| 1010         | 9                   |
| 1011         | -                   |
| 1100         | (                   |
| 1101         | )                   |
| 1110         | +                   |
| 1111         | #                   |

### Record 13 ###

This Record is an POI Record. The POI description is compressed (compression type 13)

The Format is the following :

| _Bytes_ | _Description_ |
|:--------|:--------------|
| 1 byte  | **T:** The Record Type = **0x0D** or **0x1D** |
| 1 byte  | **S:**  the size of this record, minus 11 (the size of the fixed part of the record) |
| 4 bytes | **X:** Longitude |
| 4 bytes | **Y:** Latitude |
| 1 byte  | **C:** code   |
| S bytes | **N:** encoded POI description |

#### code ####

Describes how many of the first bytes of the description are .... _(unknown! probably related with crpoi.dat)_

| _Value (binary)_ | _Size (bytes)_ |
|:-----------------|:---------------|
| xxxxxx00         | 0              |
| xxxxxx01         | 1              |
| xxxxxx10         | 3              |
| xxxxxx11         | 4              |

## Special ##

### Dictionary ###

This is a structure of dictionary used to decode Record 13.

| _bytes_ | _description_ |
|:--------|:--------------|
| 4 bytes | 50 4f 49 53 = "POIS" |
| 14 bytes | 01 00 01 00 00 00 00 00 00 00 00 00 00 00 |
| 1 byte  | I: size in bits of id |
| 2 bytes | K: size in bytes of keys description |
| 2 bytes | N: keys number. X letters + 0x200 entries |
| K bytes | keys description |
| 2 bytes | 01 01         |
| 1 byte  | string size length in bits |
| 1 byte  | B: number of bits per char |
| 4 byte  | A: id value of biggest single char |
| 4 byte  | unknown       |
| 4 bytes | M: size in bytes of entries description |
| M bytes | entries description |

#### Keys description ####

Initially the bytes of the keys description are transform in reverse order bits from 0bABCDEFGH to 0bHGFEDCBA (in binary format).
The **K** bytes describe **N** keys.

The keys are described in a pair of data:

  * appendix - number of set bits finish with a clear bit. Example 1110
  * id - **I** bits in reverse order

The first key is composed by the first few set bits, without the follows clear bit at the end.

The id of first key is 0000000000. The first key is **end-of-string** for the Record 13 description.

Each other key is composed by the bits of previous key next to the last set bit and concatenated with reversed order bits of appendix of the current key.

Example:
<pre>
the previous key is 1100110<br>
the current appendix is 110<br>
the bits next to the last set bit 11001<br>
reverse bits of appendix  011<br>
and the new key will be 11001011<br>
</pre>

All keys with id up to value A presents a single char with ASCII value composed by reversed order bits of the id.

Example: if the id is 0010011000, reverse it bits 0001100100 presents hex value 0x64 - the ASCII value of small latin letter d.

The rest keys are for the entries.

#### Entries description ####

Initially the bytes of the entries description are transform in reverse order bits from 0bABCDEFGH to 0bHGFEDCBA (in binary format).

The **M** bytes describe 0x200 entries (except at beginning 20 unknown bits 00000000010000000001).

Every next entry pairs to the key with next id.

Every entry starts with length of the string - 10 bits long, bits are in reverse order.
The entry is a string composed by ASCII chars with fixed **B** number of bits in reverse order. Example: station
<pre>
1110000000 11001110 00101110 10000110 00101110 10010110 11110110 01110110<br>
</pre>
On that manner the entry is a pair of a key and a string: 010000110 - "station"

Both, letters and entries, with the keys, present a dictionary table used to decode the Record 13 description. It is similar to the table of the Record 09.
