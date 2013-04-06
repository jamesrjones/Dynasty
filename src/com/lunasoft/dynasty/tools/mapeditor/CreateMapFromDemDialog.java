package com.lunasoft.dynasty.tools.mapeditor;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class CreateMapFromDemDialog extends Dialog {
	private short[][] heights;
	private Image image;
	private Image originalImage;

	public CreateMapFromDemDialog(Shell shell, int style) {
		super(shell, style);
		initImage(shell);
	}

	public CreateMapFromDemDialog(Shell shell) {
		this(shell, 0);
	}

	public GameMap open() {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		FormLayout layout = new FormLayout();
		layout.marginWidth = 3;
		layout.marginHeight = 3;
		shell.setLayout(layout);

		Button openFileButton = new Button(shell, 0);
		openFileButton.setText("Open File");
		openFileButton.pack();

		final Point origin = new Point(0, 0);
		final Canvas preview = new Canvas(shell, SWT.NO_BACKGROUND | SWT.V_SCROLL
				| SWT.H_SCROLL | SWT.NO_REDRAW_RESIZE);

		FormData previewFormData = new FormData();
		previewFormData.right = new FormAttachment(100, 0);
		previewFormData.top = new FormAttachment(0);
		previewFormData.bottom = new FormAttachment(100);
		previewFormData.left = new FormAttachment(20, 0);
		preview.setLayoutData(previewFormData);

		FormData buttonFormData = new FormData();
		buttonFormData.left = new FormAttachment(0, 0);
		openFileButton.setLayoutData(buttonFormData);

		final ScrollBar hBar = preview.getHorizontalBar();
		hBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int hSelection = hBar.getSelection();
				int destX = -hSelection - origin.x;
				Rectangle rect = image.getBounds();
				preview.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x = -hSelection;
			}
		});
		final ScrollBar vBar = preview.getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y;
				Rectangle rect = image.getBounds();
				preview.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSelection;
			}
		});
//		preview.addListener(SWT.Resize, new Listener() {
//			public void handleEvent(Event e) {
//				Rectangle rect = image.getBounds();
//				Rectangle client = preview.getClientArea();
//				hBar.setMaximum(rect.width);
//				vBar.setMaximum(rect.height);
//				hBar.setThumb(Math.min(rect.width, client.width));
//				vBar.setThumb(Math.min(rect.height, client.height));
//				int hPage = rect.width - client.width;
//				int vPage = rect.height - client.height;
//				int hSelection = hBar.getSelection();
//				int vSelection = vBar.getSelection();
//				if (hSelection >= hPage) {
//					if (hPage <= 0) {
//						hSelection = 0;
//					}
//					origin.x = -hSelection;
//				}
//				if (vSelection >= vPage) {
//					if (vPage <= 0) {
//						vSelection = 0;
//					}
//					origin.y = -vSelection;
//				}
//				preview.redraw();
//			}
//		});

		preview.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
//				System.out.println("Painting canvas: " + origin);
				GC gc = e.gc;
				gc.drawImage(image, origin.x, origin.y);

				Rectangle rect = image.getBounds();
				Rectangle client = preview.getClientArea();
				hBar.setMaximum(rect.width);
				vBar.setMaximum(rect.height);
				hBar.setThumb(Math.min(rect.width, client.width));
				vBar.setThumb(Math.min(rect.height, client.height));

//				Rectangle rect = image.getBounds();
//				Rectangle client = preview.getClientArea();
//				int marginWidth = client.width - rect.width;
//				if (marginWidth > 0) {
//					gc.fillRectangle(rect.width, 0, marginWidth, client.height);
//				}
			}
		});

		openFileButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				loadDemFile(shell);
				preview.redraw();
				preview.update();
			}
		});

		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return null;
	}

	private void initImage(Shell shell) {
		int width = 200;
		int height = 150;
		image = new Image(shell.getDisplay(), width, height);
		GC gc = new GC(image);
		gc.fillRectangle(0, 0, width, height);
		gc.drawText("Default Image", 10, 10);
		gc.dispose();
	}

	private void updateImage(Shell shell) {
		int height = heights.length;
		int width = heights[0].length;
		originalImage = new Image(shell.getDisplay(), width, height);
		GC gc = new GC(originalImage);
		gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));
		gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		gc.fillRectangle(0, 0, width, height);
		int numPoints = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (heights[j][i] >= 0) {
					gc.drawPoint(i, j);
					numPoints++;
				}
			}
		}
		gc.dispose();
		System.out.println("# points drawn: " + numPoints);
		double scaleX = ((double)shell.getClientArea().width) / width;
		System.out.println("ScaleX: " + scaleX);
		double scaleY = ((double)shell.getClientArea().height) / height;
		System.out.println("ScaleY: " + scaleY);
		double scale = Math.min(scaleX, scaleY);
		System.out.println("Scale: " + scale);
		System.out.println("Final rect: " + (scale * width) + " x " + (scale * height));
		image = new Image(shell.getDisplay(),
				originalImage.getImageData().scaledTo((int) (scale * width), (int) (scale * height)));
	}

	private void loadDemFile(Shell shell) {
		FileDialog fileDialog = new FileDialog(shell);
		fileDialog.setFilterExtensions(new String[] {"*.dem;*.DEM"});
		fileDialog.setText("Select DEM file to process");
		String dataFileName = fileDialog.open();
		if (dataFileName == null) {
			return;
		}
		System.out.println(dataFileName);
		// TODO: this should be converted to a separate thread for performance, but
		// for now just do it quick and dirty (er, slow and dirty)

		String headerFileName = dataFileName.replace(".DEM", ".HDR");
		try {
			new BufferedInputStream(new FileInputStream(headerFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int NROWS = 6000;
		int NCOLS = 4800;
		heights = new short[NROWS][NCOLS];
		try {
			DataInputStream dis = new DataInputStream(
					new BufferedInputStream(new FileInputStream(dataFileName)));
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
		updateImage(shell);
	}
}
