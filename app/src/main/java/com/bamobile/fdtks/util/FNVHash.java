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
 * Implements a hash function proposed by Fowler, Noll, Vo.
 * The FNVHash is designed to be fast while maintaining a low collision rate
 * See [1] for more information.
 * <p/>
 * [1] http://www.isthe.com/chongo/tech/comp/fnv/
 *
 * @author zink
 */
public final class FNVHash extends HashFunction {

    protected long SEED = 0x811C9DC5L;

    public long hash(long init, final byte[] b, int off, int len) {
        long hash = init;
        while (--len >= 0) {
            hash = (hash * SEED) ^ b[off++];
        }
        return hash;
    }
}

