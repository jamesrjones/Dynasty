package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.lunasoft.dynasty.tools.mapeditor.TileData.TerrainType;

public class HexMapCanvas extends Canvas {
	private GameMap gameMap;
	private HexGridDimensions hexGridDimensions;
	private Color backgroundColor;
	private Color cursorColor;
	private Color grasslandColor;
	private Color hillsColor;
	private Color oceanColor;
	private Image image;
	private Point cursor;
	private TerrainType selectedTerrainType = TerrainType.OCEAN;

	public HexMapCanvas(Composite parent, int style) {
		super(parent, style);
		backgroundColor = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		cursorColor = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		grasslandColor = getDisplay().getSystemColor(SWT.COLOR_GREEN);
		hillsColor = getDisplay().getSystemColor(SWT.COLOR_GRAY);
		oceanColor = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (image == null) {
					return;
				}
				e.gc.drawImage(image, 0, 0);
				if (cursor != null) {
					double[] center = hexGridDimensions.getCenter(cursor.x, cursor.y);
					Path path = getHexPath(e.gc, (float) center[0], (float) center[1]);
					e.gc.setForeground(cursorColor);
					e.gc.drawPath(path);
				}
			}
		});
		addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				// which hex are we over?
				if (image == null || !image.getBounds().contains(e.x, e.y)) {
					return;
				}
				cursor = hexGridDimensions.getHexContaining(e.x, e.y);
				redraw();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button != 1) {
					return;
				}
				Point hex = hexGridDimensions.getHexContaining(e.x, e.y);
				System.out.println("button pushed: " + e.button);
				gameMap.setTile(hex.x, hex.y, new TileData(selectedTerrainType));
				image = createImage(); // a bit heavy; maybe recreate just the bit that's changing
				redraw();
			}
		});
	}

	public void setGameMap(GameMap gameMap) {
		this.hexGridDimensions = new HexGridDimensions(gameMap.getWidth(),
				gameMap.getHeight(), HexDimensions.withInRadius(10));
		this.gameMap = gameMap;
		this.image = createImage();
		redraw();
		update();
	}

	public void setSelectedTerrainType(TerrainType terrainType) {
		selectedTerrainType = terrainType;
	}

	private Image createImage() {
		if (gameMap == null) {
			return null;
		}
		System.out.println("createImage()");
		int width = (int) hexGridDimensions.getTotalWidth();
		int height = (int) hexGridDimensions.getTotalHeight();
		Image image = new Image(getDisplay(), width, height);
		GC gc = new GC(image);
		gc.setBackground(backgroundColor);
		gc.fillRectangle(image.getBounds());
		for (int i = 0; i < gameMap.getWidth(); i++) {
			for (int j = 0; j < gameMap.getHeight(); j++) {
				double[] center = hexGridDimensions.getCenter(i, j);
				TileData tileData = gameMap.getTile(i, j);
				drawHex(gc, getTerrainColor(tileData.getTerrainType()), (float) center[0],
						(float) center[1]);
			}
		}
		return image;
	}

	private Color getTerrainColor(TerrainType terrainType) {
		switch (terrainType) {
		case OCEAN:
			return oceanColor;
		case HILLS:
		case MOUNTAINS:
			return hillsColor;
		case PLAINS:
			return grasslandColor;
		}
		return null;
	}

	private void drawHex(GC gc, Color color, float cx, float cy) {
		Path path = getHexPath(gc, cx, cy);
		gc.setBackground(color);
		gc.fillPath(path);
	}

	private Path getHexPath(GC gc, float cx, float cy) {
		Path path = new Path(gc.getDevice());
		float circumRadius = (float) hexGridDimensions.getHexDimensions().getCircumRadius();
		float inRadius = (float) hexGridDimensions.getHexDimensions().getInRadius();
		path.moveTo(cx, cy - circumRadius);
		path.lineTo(cx + inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx + inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx, cy + circumRadius);
		path.lineTo(cx - inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx - inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx, cy - circumRadius);
		return path;
	}
}
