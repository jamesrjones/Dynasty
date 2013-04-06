package com.lunasoft.dynasty.tools;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.LittleEndianDataInputStream;

public class ShapefileReader {
	private static final int FILE_CODE = 9994;
	private static final int VERSION = 1000;

	private static final int SHAPE_TYPE_NULL = 0;
	private static final int SHAPE_TYPE_POLYGON = 5;

	public void parse(BufferedInputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		LittleEndianDataInputStream ledis = new LittleEndianDataInputStream(is);

		int fileCode = dis.readInt();
		if (fileCode != FILE_CODE) {
			throw new IOException("Illegal file code " + fileCode);
		}
		verifiedSkip(is, 20); // skip unused bytes 4-23
		int fileSize = dis.readInt();
		int version = ledis.readInt();
		if (version != VERSION) {
			throw new IOException("Illegal version " + version);
		}
		System.out.println("Reading " + 2 * fileSize + " bytes...");
		int shapeType = ledis.readInt();
		System.out.println("Shape type: " + shapeType);
		double xmin = ledis.readDouble();
		double ymin = ledis.readDouble();
		double xmax = ledis.readDouble();
		double ymax = ledis.readDouble();
		System.out.format("Bounding box: (%f, %f) to (%f, %f)%n",
				xmin, ymin, xmax, ymax);
		verifiedSkip(is, 32); // skip 4 ignored doubles

		int wordsRead = 50;
		for (;;) {
			if (wordsRead >= fileSize) {
				break;
			}
			wordsRead += readRecord(dis, ledis);
			System.out.println("Bytes read: " + 2 * wordsRead);
		}
	}

	private int readRecord(DataInputStream dis, LittleEndianDataInputStream ledis) throws IOException {
		int recordNumber = dis.readInt();
		System.out.println("record #" + recordNumber);
		int numWords = dis.readInt();
//		verifiedSkip(dis, 2 * numWords);
		int shapeType = ledis.readInt();
		System.out.println("shape type: " + shapeType);
		switch (shapeType) {
			case SHAPE_TYPE_NULL:
				// do nothing
				break;
			case SHAPE_TYPE_POLYGON:
				readPolygon(ledis);
				break;
			default:
				throw new IOException("Unrecognized shape type: " + shapeType);
		}
		return 4 + numWords;
	}

	private void readPolygon(LittleEndianDataInputStream ledis) throws IOException {
		readBoundingBox(ledis);
		int numParts = ledis.readInt();
		int numPoints = ledis.readInt();
		System.out.format("Reading polygon with %d parts and %d points%n", numParts, numPoints);
		int[] parts = new int[numParts];
		double[] xPoints = new double[numPoints];
		double[] yPoints = new double[numPoints];
		for (int i = 0; i < numParts; i++) {
			parts[i] = ledis.readInt();
		}
		for (int i = 0; i < numPoints; i++) {
			assert(1 == ledis.readInt());
			xPoints[i] = ledis.readDouble();
			yPoints[i] = ledis.readDouble();
		}
	}

	private void readBoundingBox(LittleEndianDataInputStream ledis) throws IOException {
		verifiedSkip(ledis, 32); // later maybe do something with this
	}

	private void verifiedSkip(InputStream is, long n) throws IOException {
		long total = 0;
		for (;;) {
			long skipped = is.skip(n - total);
			if (skipped == 0) {
				throw new IOException(String.format("Unable to skip %d bytes; skipped %d", n, total));
			}
			total += skipped;
			if (total == n) {
				return;
			}
		}
	}

	public static void main(String[] args) {
		ShapefileReader reader = new ShapefileReader();
		try {
			reader.parse(new BufferedInputStream(System.in));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
