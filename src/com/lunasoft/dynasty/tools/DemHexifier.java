package com.lunasoft.dynasty.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.collect.ImmutableMap;
import com.google.common.math.DoubleMath;

public class DemHexifier {

	private static final String DEMFILE = "/home/jsquared/Downloads/gtopo30/W020N90.DEM";
	private static final double MIN_LONGITUDE = -20;
	private static final double MAX_LONGITUDE = 20;
	private static final double MIN_LATITUDE = 40;
	private static final double MAX_LATITUDE = 90;
	private static final int NROWS = 6000;
	private static final int NCOLS = 4800;
	private static final double DEGREES_PER_ROW = (MAX_LATITUDE - MIN_LATITUDE) / NROWS;
	private static final double DEGREES_PER_COL = (MAX_LONGITUDE - MIN_LONGITUDE) / NCOLS;

	private static final double HEX_SLOPE = (4 * Math.sqrt(3) - 3) / 6;
	private static final short NODATA = -9999;
	private static short[][] heights = new short[NROWS][NCOLS];

	private final double minLongitude;
	private final double maxLongitude;
	private final double minLatitude;
	private final double maxLatitude;
	private final double hexRadius;
	private final BufferedImage image;
	private int outputCount = 0;

	private DemHexifier(double minLongitude, double maxLongitude, double minLatitude,
			double maxLatitude, double hexRadius) {
		this.minLongitude = minLongitude;
		this.maxLongitude = maxLongitude;
		this.minLatitude = minLatitude;
		this.maxLatitude = maxLatitude;
		this.hexRadius = hexRadius;
		this.image = new BufferedImage(NCOLS, NROWS, BufferedImage.TYPE_INT_RGB);
	}

	private HexData[][] parse() {
		double colGap = 2.0 * hexRadius;
		double rowGap = Math.sqrt(3.0) * hexRadius;
		double circumRadius = (2.0 / Math.sqrt(3.0)) * hexRadius;

		int numHexRows = (int) ((maxLatitude - minLatitude) / rowGap);
		int numHexCols = (int) ((maxLongitude - minLongitude) / colGap);
		HexData[][] hexData = new HexData[numHexRows][numHexCols];

		for (int j = 0; j < numHexRows; j++) {
			double offsetX = (j % 2 == 0) ? 0.0 : hexRadius;
			double centerY = minLatitude + rowGap * j;
			for (int i = 0; i < numHexCols; i++) {
				double centerX = minLongitude + colGap * i + offsetX;
				System.out.format("Processing hex [%d, %d]; center = [%f, %f]%n",
						i, j, centerX, centerY);

				// hex is laid out like this:
				//         top
				//         ^
				//       /   \  upper corners
				//      |  .  |
				//      |     |
				//       \   /  lower corners
				//         v
				//         bottom

				double westEdge = centerX - hexRadius;
				double eastEdge = centerX + hexRadius;

				int minCol = fromLongitude(westEdge) + 1;
				int centerCol = fromLongitude(centerX);
				int maxCol = fromLongitude(eastEdge);

//				System.out.format("minCol = %d, centerCol = %d, maxCol = %d%n",
//						minCol, centerCol, maxCol);
				// y-y0 = (y1-y0)/(x1-x0) (x-x0)
				hexData[j][i] = new HexData();
				for (int col = minCol; col <= centerCol; col++) {
					double x = toLongitude(col);
					double southEdge = -HEX_SLOPE * (x - westEdge) + centerY - 0.5 * circumRadius;
					double northEdge = HEX_SLOPE * (x - westEdge) + centerY + 0.5 * circumRadius;
					int minRow = fromLatitude(northEdge) + 1;
					int maxRow = fromLatitude(southEdge);
					for (int row = minRow; row <= maxRow; row++) {
						processPointInHex(j, i, row, col, hexData[j][i]);
					}
				}
				for (int col = centerCol + 1; col <= maxCol; col++) {
					double x = toLongitude(col);
					double southEdge = HEX_SLOPE * (x - eastEdge) + centerY - 0.5 * circumRadius;
					double northEdge = -HEX_SLOPE * (x - eastEdge) + centerY + 0.5 * circumRadius;
					int minRow = fromLatitude(northEdge) + 1;
					int maxRow = fromLatitude(southEdge);
					for (int row = minRow; row <= maxRow; row++) {
						processPointInHex(j, i, row, col, hexData[j][i]);
					}
				}
			}
		}
		try {
			ImageIO.write(image, "gif", new File("output.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hexData;
	}

	private void processPointInHex(int hexRow, int hexCol, int gridRow, int gridCol,
			HexData hexData) {
		if (outputCount < 1000) {
			System.out.format("Hex [%d, %d] contains point [%d, %d] at [%f, %f]%n", hexRow, hexCol,
					gridCol, gridRow, toLongitude(gridCol), toLatitude(gridRow));
			outputCount++;
		}
		int rgb = 0x0000ff;
		if (heights[gridRow][gridCol] >= 0) {
			int convertedHeight = (int) ((255.0 / 5000.0) * heights[gridRow][gridCol]);
			rgb = 0xffff00 | convertedHeight;
		}
		image.setRGB(gridCol, gridRow, rgb);

		hexData.addSample(heights[gridRow][gridCol]);
	}

	private double toLatitude(int row) {
		return MAX_LATITUDE - DEGREES_PER_ROW * row;
	}

	private double toLongitude(int col) {
		return DEGREES_PER_COL * col + MIN_LONGITUDE;
	}

	private int fromLatitude(double latitude) {
		return DoubleMath.roundToInt((MAX_LATITUDE - latitude) / DEGREES_PER_ROW,
				RoundingMode.DOWN);
	}

	private int fromLongitude(double longitude) {
		return DoubleMath.roundToInt((longitude - MIN_LONGITUDE) / DEGREES_PER_COL,
				RoundingMode.DOWN);
	}

	public static void main(String[] args) {
		System.out.print("Initializing...");
		try {
			DataInputStream dis = new DataInputStream(
					new BufferedInputStream(new FileInputStream(DEMFILE)));
			for (int i = 0; i < NROWS; i++) {
				for (int j = 0; j < NCOLS; j++) {
					heights[i][j] = dis.readShort();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" Done.");

		System.out.print("Hexifying...");
		DemHexifier demHexifier = new DemHexifier(-7, 3, 50, 60, 0.05);
//		DemHexifier demHexifier = new DemHexifier(-15, 15, 41, 71, 0.1);
		HexData[][] hexMap = demHexifier.parse();
		System.out.println(" Done.");

		int sizeX = (int) ((hexMap.length + 5) * 2 * HexData.INRADIUS);
		int sizeY = (int) ((hexMap[0].length + 5) * 2 * HexData.INRADIUS);
		BufferedImage mapImage = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = mapImage.createGraphics();
		for (int i = 0; i < hexMap.length; i++) {
			for (int j = 0; j < hexMap[i].length; j++) {
				HexData data = hexMap[i][j];
				System.out.format("Hex %d, %d%n", i, j);
				System.out.format("  # pts = %d [%d real]%n", data.numPoints, data.numRealPoints);
				System.out.format("  elevation (avg / min / max / stdev) = %f / %f / %f / %f%n",
						mean(data.totalElevation, data.numRealPoints),
						data.minElevation, data.maxElevation,
						stdev(data.sumSquares, data.totalElevation, data.numRealPoints));
				data.draw(graphics2d, hexMap.length - 1 - i, j);
			}
		}
		try {
			ImageIO.write(mapImage, "gif", new File("map.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static double mean(double total, int num) {
		if (num == 0) {
			return Double.NaN;
		}
		return total / num;
	}

	private static double stdev(double sumsq, double total, int num) {
		if (num == 0) {
			return Double.NaN;
		}
		return Math.sqrt((sumsq - (total * total / num)) / num);
	}

	private enum TerrainType {
		OCEAN,
		PLAINS,
		HILLS,
		MOUNTAINS;
	}

	private static class HexData {
		static final double INRADIUS = 3.5;
		static final double CIRCUMRADIUS = (2.0 / Math.sqrt(3)) * INRADIUS;

		static final Map<TerrainType, Color> TERRAIN_COLORS = ImmutableMap.<TerrainType, Color>builder()
				.put(TerrainType.OCEAN, Color.BLUE)
				.put(TerrainType.PLAINS, Color.GREEN)
				.put(TerrainType.HILLS, Color.GRAY)
				.put(TerrainType.MOUNTAINS, Color.DARK_GRAY)
				.build();

		int numPoints = 0; // number of points in hex
		int numRealPoints = 0; // number of points with data in hex
		double totalElevation = 0;
		double minElevation = Double.NaN;
		double maxElevation = Double.NaN;
		double sumSquares = 0;

		void addSample(short height) {
			numPoints++;
			if (height == NODATA) {
				return;
			}
			numRealPoints++;
			totalElevation += height;
			sumSquares += height * height;
			if (Double.isNaN(minElevation) || height < minElevation) {
				minElevation = height;
			}
			if (Double.isNaN(maxElevation) || height > maxElevation) {
				maxElevation = height;
			}
		}

		TerrainType getTerrainType() {
			if (numRealPoints * 5 < numPoints) {
				return TerrainType.OCEAN;
			}
			if (maxElevation > 2000) {
				return TerrainType.MOUNTAINS;
			}
			if (stdev(sumSquares, totalElevation, numRealPoints) > 100) {
				return TerrainType.HILLS;
			}
			return TerrainType.PLAINS;
		}

		// draw this hex at the specified point
		void draw(Graphics2D graphics, int i, int j) {
			double cx = (j * 2 + 1) * INRADIUS;
			double cy = (i * Math.sqrt(3) + 1) * INRADIUS;
			if (i % 2 == 1) {
				cx += INRADIUS;
			}
			Path2D.Double path = new Path2D.Double();
			path.moveTo(cx, cy - CIRCUMRADIUS);
			path.lineTo(cx + INRADIUS, cy - 0.5 * CIRCUMRADIUS);
			path.lineTo(cx + INRADIUS, cy + 0.5 * CIRCUMRADIUS);
			path.lineTo(cx, cy + CIRCUMRADIUS);
			path.lineTo(cx - INRADIUS, cy + 0.5 * CIRCUMRADIUS);
			path.lineTo(cx - INRADIUS, cy - 0.5 * CIRCUMRADIUS);
			path.lineTo(cx, cy - CIRCUMRADIUS);
			graphics.setPaint(TERRAIN_COLORS.get(getTerrainType()));
			graphics.fill(path);
			graphics.setPaint(Color.WHITE);
			graphics.draw(path);
		}
	}
}
