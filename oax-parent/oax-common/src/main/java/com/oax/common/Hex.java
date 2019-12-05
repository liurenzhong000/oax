package com.oax.common;

import java.math.BigInteger;

public class Hex {

    public static String toHexNumber(BigInteger integer) {

        return integer.toString(16);

    }

    public static void main(String[] args) {

        BigInteger integer = new BigInteger("10");

        System.out.println(Hex.toHexNumber(integer));
    }

}
