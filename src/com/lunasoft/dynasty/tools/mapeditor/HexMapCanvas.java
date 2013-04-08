package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.lunasoft.dynasty.tools.mapeditor.TileData.TerrainType;

public class HexMapCanvas extends Canvas {
	private GameMap gameMap;
	private HexDimensions hexDimensions = HexDimensions.withInRadius(10);
	private Color backgroundColor;
	private Color grasslandColor;
	private Color hillsColor;
	private Color oceanColor;
	private Image image;

	public HexMapCanvas(Composite parent, int style) {
		super(parent, style);
		backgroundColor = getDisplay().getSystemColor(SWT.COLOR_BLACK);
		grasslandColor = getDisplay().getSystemColor(SWT.COLOR_GREEN);
		hillsColor = getDisplay().getSystemColor(SWT.COLOR_GRAY);
		oceanColor = getDisplay().getSystemColor(SWT.COLOR_BLUE);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (image == null) {
					return;
				}
				System.out.println("paintControl()");
				e.gc.drawImage(image, 0, 0);
			}
		});
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
		this.image = createImage();
		redraw();
		update();
	}

	private Image createImage() {
		if (gameMap == null) {
			return null;
		}
		System.out.println("createImage()");
		int width = (int) ((2 * gameMap.getWidth() + 1) * hexDimensions.getInRadius());
		int height = (int) ((3 * gameMap.getHeight() / 2) * hexDimensions.getCircumRadius()
				+ hexDimensions.getCircumRadius() - hexDimensions.getInRadius()) + 3;
		Image image = new Image(getDisplay(), width, height);
		GC gc = new GC(image);
		gc.setBackground(backgroundColor);
		gc.fillRectangle(image.getBounds());
		for (int i = 0; i < gameMap.getWidth(); i++) {
			for (int j = 0; j < gameMap.getHeight(); j++) {
				float cx = (float) ((i * 2 + 1) * hexDimensions.getInRadius());
				float cy = (float) ((j * Math.sqrt(3) + 1) * hexDimensions.getInRadius());
				if (j % 2 == 1) {
					cx += hexDimensions.getInRadius();
				}
				TileData tileData = gameMap.getTile(i, j);
				drawHex(gc, getTerrainColor(tileData.getTerrainType()), cx, cy);
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
		Path path = new Path(gc.getDevice());
		float circumRadius = (float) hexDimensions.getCircumRadius();
		float inRadius = (float) hexDimensions.getInRadius();
		path.moveTo(cx, cy - circumRadius);
		path.lineTo(cx + inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx + inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx, cy + circumRadius);
		path.lineTo(cx - inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx - inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx, cy - circumRadius);
		gc.setBackground(color);
		gc.fillPath(path);
	}
}
