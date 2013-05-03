package com.lunasoft.dynasty.tools.mapeditor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.lunasoft.common.swt.GridDataBuilder;
import com.lunasoft.dynasty.tools.mapeditor.TileData.ReliefType;
import com.lunasoft.dynasty.tools.mapeditor.TileData.VegetationType;

public class MapEditor {

	private HexMapCanvas hexMapCanvas;
	private Canvas previewCanvas;
	private Map<VegetationType, Button> vegetationTypeButtons
			= Maps.newEnumMap(VegetationType.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MapEditor().run();
	}

	private void run() {
		Display display = new Display();
    Shell shell = new Shell(display);
    createMainMenu(shell);
    layoutMainShell(shell);

	  shell.open();
	  while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) {
	    	display.sleep();
	    }
	  }
	  display.dispose();
	}

	private void createMainMenu(final Shell shell) {
    Menu menu = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menu);
    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
    fileItem.setText("&File");
    Menu submenu = new Menu(shell, SWT.DROP_DOWN);
    fileItem.setMenu(submenu);

    MenuItem createFromDemItem = new MenuItem(submenu, SWT.PUSH);
    createFromDemItem.setText("Create from DEM...");
    createFromDemItem.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		CreateMapFromDemWizard wizard = new CreateMapFromDemWizard();
    		WizardDialog dialog = new WizardDialog(shell, wizard);
    		if (Window.OK == dialog.open()) {
    			hexMapCanvas.setGameMap(wizard.getGameMap());
    		}
    	}
    });

    MenuItem createBlankMapItem = new MenuItem(submenu, SWT.PUSH);
    createBlankMapItem.setText("Create new...");
    createBlankMapItem.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		CreateBlankMapDialog dialog = new CreateBlankMapDialog(shell);
    		if (Window.OK == dialog.open()) {
    			setMap(GameMap.ofTerrain(false, dialog.getWidth(), dialog.getHeight(),
    					ReliefType.WATER));
    		}
    	}
    });

    MenuItem saveMapItem = new MenuItem(submenu, SWT.PUSH);
    saveMapItem.setText("Save as...");
    saveMapItem.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
    		String fileName = saveDialog.open();
    		if (fileName == null) {
    			return;
    		}
    		try {
    			FileOutputStream fos = new FileOutputStream(fileName);
    			ObjectOutputStream oos = new ObjectOutputStream(fos);
    			oos.writeObject(hexMapCanvas.getGameMap());
    			fos.close();
    		} catch (FileNotFoundException ex) {
    			ex.printStackTrace();
    		} catch (IOException ex) {
					ex.printStackTrace();
				}
    	}
    });

    MenuItem loadMapItem = new MenuItem(submenu, SWT.PUSH);
    loadMapItem.setText("Open...");
    loadMapItem.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
    		FileDialog openDialog = new FileDialog(shell, SWT.OPEN);
    		String fileName = openDialog.open();
    		if (fileName == null) {
    			return;
    		}
    		try {
    			FileInputStream fis = new FileInputStream(fileName);
    			ObjectInputStream ois = new ObjectInputStream(fis);
    			GameMap gameMap = (GameMap) ois.readObject();
    			hexMapCanvas.setGameMap(gameMap);
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
    		} catch (FileNotFoundException ex) {
    			ex.printStackTrace();
    		} catch (IOException ex) {
					ex.printStackTrace();
				}
    	}
    });
	}

	private void layoutMainShell(final Shell shell) {
		shell.setLayout(new GridLayout(3, false));

		Composite reliefControls = new Composite(shell, SWT.NONE);
		reliefControls.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		reliefControls.setLayout(new GridLayout(1, false));

		addReliefButton(reliefControls, "Water", ReliefType.WATER).setSelection(true);
		addReliefButton(reliefControls, "Plains", ReliefType.PLAINS);
		addReliefButton(reliefControls, "Hills", ReliefType.HILLS);
		addReliefButton(reliefControls, "Mountains", ReliefType.MOUNTAINS);

		Composite vegetationControls = new Composite(shell, SWT.NONE);
		vegetationControls.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		vegetationControls.setLayout(new GridLayout(1, false));

		addVegetationButton(vegetationControls, "None", VegetationType.NONE).setSelection(true);
		addVegetationButton(vegetationControls, "Desert", VegetationType.DESERT).setEnabled(false);
		addVegetationButton(vegetationControls, "Forest", VegetationType.FOREST).setEnabled(false);
		addVegetationButton(vegetationControls, "Grassland", VegetationType.GRASSLAND).setEnabled(false);

// TODO: do this later
//		addMoreRowsButton(controls, "+2 Rows Top", false);
//		addMoreRowsButton(controls, "+2 Rows Bottom", true);

		hexMapCanvas = new HexMapCanvas(shell, SWT.NONE);
		hexMapCanvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		previewCanvas = new Canvas(shell, SWT.NONE);
		previewCanvas.setLayoutData(new GridDataBuilder().withHorizontalSpan(2).build());

		previewCanvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				hexMapCanvas.previewHex(e.gc, previewCanvas.getBounds());
			}
		});
	}

	private Button addReliefButton(Composite parent, String labelText,
			final ReliefType reliefType) {
		Button button = new Button(parent, SWT.RADIO);
		button.setText(labelText);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button selectedButton = null;
				for (VegetationType vegetationType : VegetationType.values()) {
					Button button = vegetationTypeButtons.get(vegetationType);
					if (button == null) {
						continue;
					}
					if (button.getSelection()) {
						selectedButton = button;
					}
					button.setEnabled(
							reliefType.getValidVegetationTypes().contains(vegetationType));
				}
				if (selectedButton != null && !selectedButton.getEnabled()) {
					selectedButton.setSelection(false);
					VegetationType firstValidVegetationType = Iterables.getFirst(
							reliefType.getValidVegetationTypes(), null);
					vegetationTypeButtons.get(firstValidVegetationType).setSelection(true);
					hexMapCanvas.setSelectedVegetationType(firstValidVegetationType);
				}
				hexMapCanvas.setSelectedReliefType(reliefType);
				previewCanvas.redraw();
			}
		});
		return button;
	}

	private Button addVegetationButton(Composite parent, String labelText,
			final VegetationType vegetationType) {
		Button button = new Button(parent, SWT.RADIO);
		button.setText(labelText);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				hexMapCanvas.setSelectedVegetationType(vegetationType);
				previewCanvas.redraw();
			}
		});
		vegetationTypeButtons.put(vegetationType, button);
		return button;
	}

	private Button addMoreRowsButton(Composite parent, String labelText,
			final boolean addToBottom) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(labelText);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// implement later
			}
		});
		return button;
	}

	private void setMap(GameMap gameMap) {
		hexMapCanvas.setGameMap(gameMap);
	}
}
