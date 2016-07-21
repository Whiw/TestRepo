package com.young.android.bleproject;

/**
 * Created by young on 2016-06-09.
 */
public class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair() {
        this.first = null;
        this.second = null;
    }
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
    public boolean isAvailable() {
        if (first == null || second == null) {
            return true;
        }
        else
            return false;
    }


}