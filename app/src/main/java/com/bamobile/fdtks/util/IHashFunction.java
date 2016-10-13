package com.bamobile.fdtks.util;

/*
Copyright (c) 2010-2012 Thomas Zink

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/



/**
* @author thomas zink
*/
public interface IHashFunction {

/**
 * Hashes the specified byte array using a specified  initial value and
 * starting at a specified offset using a specified number of bytes.
 *
 * @param init initial hash value
 * @param b    byte array to hash
 * @param off  offset into array
 * @param len  number of bytes to hash
 * @return the hash value
 */
public long hash(long init, final byte[] b, int off, int len);

/**
 * Hashes the specified Object.
 *
 * @param o the object to hash
 * @return the hash value of the specified object
 * @throws IOException
 */
//public long hash(Object o) throws IOException;

/**
 * Hashes the specified byte.
 *
 * @param b the byte to hash
 * @return the hash value of specified byte
 */
public long hash(int b);

/**
 * Hashes the specified array of bytes.
 *
 * @param b the byte array to hash
 * @return the hash value
 */
public long hash(byte[] b);

/**
 * Hashes a number of bytes of the specified array using an offset.
 *
 * @param b   the byte array to hash
 * @param off the start offset of the data
 * @param len the number of bytes to use for the update
 * @return the hash value
 */
public long hash(byte[] b, int off, int len);


/**
 * Updates checksum with specified object.
 *
 * @param o the object to hash
 * @throws IOException
 */
//public void update(Object o);

/**
 * Updates the checksum with specified array of bytes.
 *
 * @param b the byte array to update the checksum with
 */
public void update(byte[] b);

// Just for reference. This comes from Checksum
//public void update(byte[] bytes, int i, int i1);
//public void update(int i);
//public long getValue();
//public void reset();

// these could be useful
//public long update(Long val, final byte[] b, final int off, final int len);
//public long hash(final long initial, final byte b);
//public long hash(final long initial, final byte[] b);
}

