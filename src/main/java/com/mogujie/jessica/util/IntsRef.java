package com.mogujie.jessica.util;


/** Represents int[], as a slice (offset + length) into an
 *  existing int[].  The {@link #ints} member should never be null; use
 *  {@link #EMPTY_INTS} if necessary.
 *
 *  @lucene.internal */
public final class IntsRef implements Comparable<IntsRef> {

  public static final int[] EMPTY_INTS = new int[0];

  public int[] ints;
  public int offset;
  public int length;

  public IntsRef() {
  }

  public IntsRef(int capacity) {
    ints = new int[capacity];
  }

  public IntsRef(int[] ints, int offset, int length) {
    this.ints = ints;
    this.offset = offset;
    this.length = length;
  }

  public IntsRef(IntsRef other) {
    copy(other);
  }

  @Override
  public Object clone() {
    return new IntsRef(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 0;
    final int end = offset + length;
    for(int i = offset; i < end; i++) {
      result = prime * result + ints[i];
    }
    return result;
  }
  
  @Override
  public boolean equals(Object other) {
    return this.intsEquals((IntsRef) other);
  }

  public boolean intsEquals(IntsRef other) {
    if (length == other.length) {
      int otherUpto = other.offset;
      final int[] otherInts = other.ints;
      final int end = offset + length;
      for(int upto=offset;upto<end;upto++,otherUpto++) {
        if (ints[upto] != otherInts[otherUpto]) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  /** Signed int order comparison */
  public int compareTo(IntsRef other) {
    if (this == other) return 0;

    final int[] aInts = this.ints;
    int aUpto = this.offset;
    final int[] bInts = other.ints;
    int bUpto = other.offset;

    final int aStop = aUpto + Math.min(this.length, other.length);

    while(aUpto < aStop) {
      int aInt = aInts[aUpto++];
      int bInt = bInts[bUpto++];
      if (aInt > bInt) {
        return 1;
      } else if (aInt < bInt) {
        return -1;
      }
    }

    // One is a prefix of the other, or, they are equal:
    return this.length - other.length;
  }

  public void copy(IntsRef other) {
    if (ints == null) {
      ints = new int[other.length];
    } else {
      ints = ArrayUtil2.grow(ints, other.length);
    }
    System.arraycopy(other.ints, other.offset, ints, 0, other.length);
    length = other.length;
    offset = 0;
  }

  public void grow(int newLength) {
    if (ints.length < newLength) {
      ints = ArrayUtil2.grow(ints, newLength);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append('[');
    final int end = offset + length;
    for(int i=offset;i<end;i++) {
      if (i > offset) {
        sb.append(' ');
      }
      sb.append(Integer.toHexString(ints[i]));
    }
    sb.append(']');
    return sb.toString();
  }
}
