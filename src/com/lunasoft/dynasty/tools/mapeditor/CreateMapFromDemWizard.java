package com.lunasoft.dynasty.tools.mapeditor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Tracker;

import com.google.common.base.Preconditions;

public class CreateMapFromDemWizard extends Wizard implements IPageChangingListener {

	private boolean needToReloadDem = false;
	private FileMetadata fileMetadata = null;
	private short[][] heights = null;
	private Image fullSizeImage;
	private Rectangle selectedBounds;

	public void addPages() {
		addPage(new SelectFilePage("Select File"));
		addPage(new SelectRegionPage("Select Region"));
		addPage(new HexifyPage("Hexify"));
		WizardDialog dialog = (WizardDialog) getContainer();
		dialog.addPageChangingListener(this);
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	FileMetadata getFileMetadata() {
		return fileMetadata;
	}

	void setFileMetadata(FileMetadata fileMetadata) {
		this.fileMetadata = fileMetadata;
		this.needToReloadDem = true;
	}

	private class SelectFilePage extends WizardPage {

		private Label fileNameValue;
		private Label numRowsValue;
		private Label numColsValue;

		protected SelectFilePage(String pageName) {
			super(pageName);
			setTitle(pageName);
		}

		@Override
		public void createControl(Composite parent) {
			Composite pageControl = new Composite(parent, 0);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			pageControl.setLayout(layout);
			Button openFileButton = new Button(pageControl, SWT.PUSH);
			openFileButton.setText("Open File");
			openFileButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));

			Label fileNameLabel = new Label(pageControl, SWT.PUSH);
			fileNameLabel.setText("File name:");
			fileNameValue = new Label(pageControl, SWT.PUSH);
	
			Label numRowsLabel = new Label(pageControl, SWT.PUSH);
			numRowsLabel.setText("Number of rows:");
			numRowsValue = new Label(pageControl, SWT.PUSH);
			Label numColsLabel = new Label(pageControl, SWT.PUSH);
			numColsLabel.setText("Number of columns:");
			numColsValue = new Label(pageControl, SWT.PUSH);

			openFileButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					FileDialog fileDialog = new FileDialog(getShell());
					fileDialog.setFilterExtensions(new String[] {"*.dem;*.DEM"});
					fileDialog.setText("Select DEM file to process");
					String dataFileName = fileDialog.open();
					if (dataFileName == null) {
						return;
					}
					FileMetadata oldFileMetadata = getFileMetadata();
					if (oldFileMetadata != null && dataFileName.equals(oldFileMetadata.dataFileName)) {
						return;
					}
					String headerFileName = dataFileName.replace(".DEM", ".HDR");
					try {
						FileMetadata fileMetadata = loadFileMetadata(headerFileName);
						fileMetadata.dataFileName = dataFileName;
						setFileMetadata(fileMetadata);
					} catch (FileNotFoundException err) {
						MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
						mb.setMessage("File not found: " + headerFileName);
						mb.open();
					} catch (IOException err) {
						MessageBox mb = new MessageBox(getShell(), SWT.ICON_ERROR);
						mb.setMessage("Error reading header file: " + err.getMessage());
						mb.open();
					}
					setMetadataValues(getFileMetadata());
				}
			});

			setMetadataValues(getFileMetadata());

			setControl(pageControl);
			setPageComplete(false);
		}

		private FileMetadata loadFileMetadata(String headerFileName) throws FileNotFoundException,
				IOException {
			BufferedReader reader = new BufferedReader(new FileReader(headerFileName));
			FileMetadata fileMetadata = new FileMetadata();
			fileMetadata.headerFileName = headerFileName;
			for (;;) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				String[] pieces = line.split("\\s+");
				if (pieces[0].equals("NROWS")) {
					fileMetadata.nrows = Integer.parseInt(pieces[1]);
				} else if (pieces[0].equals("NCOLS")) {
					fileMetadata.ncols = Integer.parseInt(pieces[1]);
				} else if (pieces[0].equals("ULXMAP")) {
					fileMetadata.minLongitude = Double.parseDouble(pieces[1]);
				} else if (pieces[0].equals("ULYMAP")) {
					fileMetadata.maxLatitude = Double.parseDouble(pieces[1]);
				} else if (pieces[0].equals("XDIM")) {
					fileMetadata.degreesPerCol = Double.parseDouble(pieces[1]);
					System.out.format("Parsed '%s' into %f%n", pieces[1], fileMetadata.degreesPerCol);
				} else if (pieces[0].equals("YDIM")) {
					fileMetadata.degreesPerRow = Double.parseDouble(pieces[1]);
					System.out.format("Parsed '%s' into %f%n", pieces[1], fileMetadata.degreesPerRow);
				}
			}
			assert(fileMetadata.nrows > 0);
			assert(fileMetadata.ncols > 0);
			setPageComplete(true);
			return fileMetadata;
		}

		private void setMetadataValues(FileMetadata fileMetadata) {
			if (fileMetadata != null) {
				fileNameValue.setText(fileMetadata.headerFileName);
				fileNameValue.pack();
				numRowsValue.setText("" + fileMetadata.nrows);
				numRowsValue.pack();
				numColsValue.setText("" + fileMetadata.ncols);
				numColsValue.pack();
			} else {
				fileNameValue.setText("<None>");
				numRowsValue.setText("n/a");
				numColsValue.setText("n/a");
			}
		}
	}

	private class SelectRegionPage extends WizardPage {
		private ProgressBar progressBar;
		private Canvas imageCanvas;
		private Image resizedImage;
		private Tracker tracker;

		protected SelectRegionPage(String pageName) {
			super(pageName);
			setTitle(pageName);
		}

		@Override
		public void createControl(Composite parent) {
			Composite pageControl = new Composite(parent, SWT.NONE);
			pageControl.setLayout(new GridLayout(3, false));

			Label minLatitudeLabel = new Label(pageControl, SWT.PUSH);
			minLatitudeLabel.setText("Min latitude:");
			minLatitudeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			final Label minLatitudeValue = new Label(pageControl, SWT.PUSH);
			minLatitudeValue.setText("n/a.....");
			minLatitudeValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

			imageCanvas = new Canvas(pageControl, SWT.PUSH);
			imageCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));

			Label maxLatitudeLabel = new Label(pageControl, SWT.PUSH);
			maxLatitudeLabel.setText("Max latitude:");
			maxLatitudeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			final Label maxLatitudeValue = new Label(pageControl, SWT.PUSH);
			maxLatitudeValue.setText("n/a.....");
			maxLatitudeValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

			Label minLongitudeLabel = new Label(pageControl, SWT.PUSH);
			minLongitudeLabel.setText("Min longitude:");
			minLongitudeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
			final Label minLongitudeValue = new Label(pageControl, SWT.PUSH);
			minLongitudeValue.setText("n/a.....");
			minLongitudeValue.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));

			Label maxLongitudeLabel = new Label(pageControl, SWT.PUSH);
			maxLongitudeLabel.setText("Max longitude:");
			maxLongitudeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
			final Label maxLongitudeValue = new Label(pageControl, SWT.PUSH);
			maxLongitudeValue.setText("n/a.....");
			maxLongitudeValue.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

			progressBar = new ProgressBar(pageControl, SWT.HORIZONTAL | SWT.PUSH);
			progressBar.setMinimum(0);
			progressBar.setMaximum(100);
			progressBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

			setControl(pageControl);
			setPageComplete(false);

			imageCanvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if (resizedImage == null) {
						return;
					}
					e.gc.drawImage(resizedImage, 0, 0);
					if (tracker != null && !tracker.isDisposed()) {
						Rectangle rect = tracker.getRectangles()[0];
						e.gc.drawRectangle(rect.x, rect.y, rect.width, rect.height);
					}
				}
			});
			imageCanvas.addMouseListener(new MouseAdapter() {
				public void mouseDown(MouseEvent e) {
					// (re-)create Tracker and start tracking
					tracker = new Tracker(imageCanvas, SWT.RESIZE);
					tracker.setStippled(true);
					tracker.setRectangles(new Rectangle[] {new Rectangle(e.x, e.y, 0, 0)});
					if (tracker.open()) {
						FileMetadata fileMetadata = getFileMetadata();
						Rectangle bounds = imageCanvas.getClientArea();
						bounds = resizedImage.getBounds();
						Rectangle tracked = tracker.getRectangles()[0];
						System.out.println("Tracked to " + tracked + "; bounds are " + bounds);
						imageCanvas.redraw();
						imageCanvas.update();
						int x = screenToActual(tracked.x, bounds.width,
								fileMetadata.ncols);
						int y = screenToActual(tracked.y, bounds.height,
								fileMetadata.nrows);
						int width = screenToActual(tracked.width, bounds.width,
								fileMetadata.ncols);
						int height = screenToActual(tracked.height, bounds.height,
								fileMetadata.nrows);
						selectedBounds = new Rectangle(x, y, width, height);

						double maxLatitude = fileMetadata.maxLatitude
								- selectedBounds.y * fileMetadata.degreesPerRow;
						double minLatitude = fileMetadata.maxLatitude
								- (selectedBounds.y + selectedBounds.height) * fileMetadata.degreesPerRow;
						double minLongitude = fileMetadata.minLongitude
								+ selectedBounds.x * fileMetadata.degreesPerCol;
						double maxLongitude = fileMetadata.minLongitude
								+ (selectedBounds.x + selectedBounds.width) * fileMetadata.degreesPerCol;
						System.out.format("actual bounds: (%d, %d, %d, %d)%n"
								+ "min/max long/lat: (%.1f, %.1f, %.1f, %.1f)%n", 
								x, y, width, height, minLongitude, maxLongitude, minLatitude, maxLatitude);

						maxLatitudeValue.setText(String.format("%.1f", maxLatitude));
						minLatitudeValue.setText(String.format("%.1f", minLatitude));
						minLongitudeValue.setText(String.format("%.1f", minLongitude));
						maxLongitudeValue.setText(String.format("%.1f", maxLongitude));
						setPageComplete(true);
					}
				}
			});
		}

		private int screenToActual(int screenX, int screenMax, int actualMax) {
			return (int) ((screenX / (double) screenMax) * actualMax);
		}

		void updateProgressBar(final int amount) {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (progressBar.isDisposed()) {
						return;
					}
					progressBar.setSelection(amount);
				}
			});
		}

		void createFullImage() {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					progressBar.setSelection(0);
					FileMetadata fileMetadata = getFileMetadata();
					Display display = getShell().getDisplay();
					fullSizeImage = new Image(display, fileMetadata.ncols,
							fileMetadata.nrows);
					GC gc = new GC(fullSizeImage);
					gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
					gc.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
					gc.fillRectangle(0, 0, fileMetadata.ncols, fileMetadata.nrows);
					for (int i = 0; i < fileMetadata.nrows; i++) {
						for (int j = 0; j < fileMetadata.ncols; j++) {
							if (heights[i][j] >= 0) {
								gc.drawPoint(j, i);
							}
						}
						progressBar.setSelection(50 + 50 * (i+1) / fileMetadata.nrows);
					}
					gc.dispose();
				}
			});
		}

		void showCanvas() {
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					resizedImage = sizeImageToFit(fullSizeImage, imageCanvas.getClientArea());
					imageCanvas.redraw();
					imageCanvas.update();
				}
			});
		}
	}

	private class HexifyPage extends WizardPage {
		private Canvas imageCanvas;
		private Image resizedImage;
		private Image selectedImage;
		private double hexSize;

		protected HexifyPage(String pageName) {
			super(pageName);
			setTitle(pageName);
		}

		@Override
		public void createControl(Composite parent) {
			Composite pageControl = new Composite(parent, SWT.NONE);
			pageControl.setLayout(new GridLayout(2, false));

			final Scale scale = new Scale(pageControl, SWT.PUSH | SWT.VERTICAL);
			scale.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false));
			scale.setMaximum(20);
			scale.setMinimum(2);
			scale.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					hexSize = scale.getSelection();
					imageCanvas.redraw();
					imageCanvas.update();
				}
			});

			imageCanvas = new Canvas(pageControl, SWT.PUSH);
			imageCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			imageCanvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					if (selectedImage == null) {
						selectedImage = new Image(getShell().getDisplay(),
								selectedBounds.width, selectedBounds.height);
						GC gc = new GC(selectedImage);
						gc.drawImage(fullSizeImage, selectedBounds.x, selectedBounds.y,
								selectedBounds.width, selectedBounds.height,
								0, 0, selectedBounds.width, selectedBounds.height);
						gc.dispose();
					}
					if (resizedImage == null) {
						resizedImage = sizeImageToFit(selectedImage, imageCanvas.getClientArea());
						hexSize = 10.0;
						scale.setSelection((int) hexSize);
					}
					e.gc.drawImage(resizedImage, 0, 0);
					drawHexGrid(e.gc, hexSize, resizedImage.getBounds());
				}
			});

			setControl(pageControl);
		}
	}

	private static class FileMetadata {
		String headerFileName;
		String dataFileName;
		int nrows;
		int ncols;
		double minLongitude;
		double maxLatitude;
		double degreesPerCol;
		double degreesPerRow;
	}

	@Override
	public void handlePageChanging(PageChangingEvent e) {
		System.out.println("Changing page");
		if (!(e.getTargetPage() instanceof SelectRegionPage)) {
			return;
		}
		if (!needToReloadDem) {
			return;
		}
		final SelectRegionPage page = (SelectRegionPage) e.getTargetPage();
		Thread imageLoader = new Thread(new Runnable() {
			public void run() {
				Preconditions.checkNotNull(fileMetadata);
				heights = new short[fileMetadata.nrows][fileMetadata.ncols];
				try {
					DataInputStream dis = new DataInputStream(
							new BufferedInputStream(new FileInputStream(fileMetadata.dataFileName)));
					for (int i = 0; i < fileMetadata.nrows; i++) {
						for (int j = 0; j < fileMetadata.ncols; j++) {
							heights[i][j] = dis.readShort();
						}
						page.updateProgressBar((50 * (i + 1)) / fileMetadata.nrows);
					}
					needToReloadDem = false;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				page.createFullImage();
				page.showCanvas();
			}
		});
		imageLoader.start();
	}

	private Image sizeImageToFit(Image originalImage, Rectangle rect) {
		Rectangle originalBounds = originalImage.getBounds();
		System.out.println("Fit rect: " + rect);
		double scaleX = ((double)rect.width) / originalBounds.width;
		System.out.println("ScaleX: " + scaleX);
		double scaleY = ((double)rect.height) / originalBounds.height;
		System.out.println("ScaleY: " + scaleY);
		double scale = Math.min(scaleX, scaleY);
		System.out.println("Scale: " + scale);
		System.out.format("Scaling from %d x %d to %d x %d%n",
				fileMetadata.ncols, fileMetadata.nrows,
				(int) (scale * originalBounds.width), (int) (scale * originalBounds.height));
		return new Image(getShell().getDisplay(),
				originalImage.getImageData().scaledTo((int) (scale * originalBounds.width),
						(int) (scale * originalBounds.height)));
	}

	private void drawHexGrid(GC gc, double hexSize, Rectangle bounds) {
		HexDimensions hexDimensions = HexDimensions.withCircumRadius(hexSize);
		Path path = new Path(gc.getDevice());
		gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_GRAY));
		gc.setClipping(bounds);
		int imax = 1 + (int) (bounds.width / hexDimensions.getInRadius() / 2);
		int jmax = 1 + (int) (bounds.height / hexDimensions.getInRadius() / Math.sqrt(3));
		for (int i = 0; i < imax; i++) {
			for (int j = 0; j < jmax; j++) {
				float cx = (float) ((i * 2 + 1) * hexDimensions.getInRadius());
				float cy = (float) ((j * Math.sqrt(3) + 1) * hexDimensions.getInRadius());
				if (j % 2 == 1) {
					cx += hexDimensions.getInRadius();
				}
				drawHex(path, hexDimensions, cx, cy);
			}
		}
		gc.drawPath(path);
		
	}

	private void drawHex(Path path, HexDimensions hexDimensions, float cx, float cy) {
		float circumRadius = (float) hexDimensions.getCircumRadius();
		float inRadius = (float) hexDimensions.getInRadius();
		path.moveTo(cx, cy - circumRadius);
		path.lineTo(cx + inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx + inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx, cy + circumRadius);
		path.lineTo(cx - inRadius, cy + 0.5f * circumRadius);
		path.lineTo(cx - inRadius, cy - 0.5f * circumRadius);
		path.lineTo(cx, cy - circumRadius);
	}
}
