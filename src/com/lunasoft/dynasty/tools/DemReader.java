package com.lunasoft.dynasty.tools;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DemReader {

	private static int NROWS = 6000;
	private static int NCOLS = 4800;
	private static short NODATA = -9999;

	public static void main(String[] args) {
		DemReader demReader = new DemReader();
		try {
			demReader.parse(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parse(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		long numNoData = 0;
		short maxValue = 0;
		for (int i = 0; i < NROWS; ++i) {
			for (int j = 0; j < NCOLS; ++j) {
				short value = dis.readShort();
				if (value == NODATA) {
					numNoData++;
				} else if (value > maxValue) {
					maxValue = value;
				}
			}
			if (i % 100 == 0) {
				System.out.println("Read row#" + i);
			}
		}
		System.out.println("Read entire file.");
		System.out.format("Read %d 'nodata' records.%n", numNoData);
		System.out.format("Max value: %d%n", maxValue);
	}
}
