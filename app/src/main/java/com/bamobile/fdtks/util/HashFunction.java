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


// http://www.docjar.com/docs/api/java/util/package-index.html

/**
 * Implements an abstract hash function class. The class implements the
 * IHashFunction which in turn implements java.util.zip.Checksum.
 * <p/>
 * A HashFunction can be used to hash objects independently from any internal
 * state using the provided hash methods. It can also be used to generate
 * checksums over any sequence of objects or bytes using the update methods.
 * In this case the checksum is saved in the field value.
 * <p/>
 * NOTE that hash functions do NOT necessarily produce the same checksum value
 * when consecutively updating chunks of data compared to a single hash over
 * the whole data.
 * Given a byte sequency b, it's different to consecutively updating the
 * checksum using chunks of b, than using b for a single hash or update.
 * <p/>
 * All hash functions should extend HashFunction and must implement at least
 * the abstract method hash(long,byte[],int,int) which is the final entry
 * point of all hash and update methods.
 *
 * @author zink
 */
public abstract class HashFunction implements IHashFunction {

    /**
     * initial hash value
     */
    protected long initial = 0L;
    /**
     * current hash value
     */
    protected Long value;

    // abstracts, must be implemented by children
    abstract public long hash(long init, final byte[] b, int off, int len);

    // IHashFunction methods, independant of instance    
    /*public final long hash (final Object o) throws IOException {
    return hash(Convert.objectToBytes(o));
    }*/
    public final long hash(final int b) {
        return hash(initial, new byte[]{(byte) (b & 0xff)}, 0, 1);
    }

    public final long hash(final byte[] b) {
        return hash(initial, b, 0, b.length);
    }

    public final long hash(final byte[] b, final int off, final int len) {
        return hash(initial, b, off, len);
    }

    // update methods, update the instance's hash value
    synchronized public final void update(final byte[] b) {
        long v = (value == null) ? initial : value.longValue();
        value = new Long(hash(v, b, 0, b.length));
    }

    /*synchronized public final void update(final Object o) throws IOException {
    update(Convert.objectToBytes(o));
    }*/
    synchronized public final void update(final int b) {
        update(new byte[]{(byte) (b & 0xff)});
    }

    synchronized public final void update(final byte[] b, final int off, final int len) {
        long v = (value == null) ? initial : value.longValue();
        value = new Long(hash(v, b, off, len));
    }

    // get and reset
    synchronized public final long getValue() {
        return value.longValue();
    }

    synchronized public void reset() {
        value = null;
    }

    // equals, hashcode, tostring
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HashFunction other = (HashFunction) obj;
        if (this.initial != other.initial) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (this.initial ^ (this.initial >>> 32));
        hash = 41 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return getClass().getName() + "{" + "initial=" + initial + ", value=" + value + '}';
    }
}

