package com.xirtam.test;

public class EnumTest {
	enum A {
		a, b, c, d;
	}

	public static void main(String[] args) {
		A aa = A.valueOf(A.class, "a");
		if (aa == A.a) {
			System.out.println(aa + " is equals 'a'");
		} else {
			System.out.println(aa + " is NOT equals 'a'");
		}
	}
}