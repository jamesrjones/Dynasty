package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.lunasoft.dynasty.tools.mapeditor.TileData.TerrainType;

public class MapEditor {

	private GameMap gameMap;
	private HexMapCanvas hexMapCanvas;

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
	  display.dispose ();
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
    		WizardDialog dialog = new WizardDialog(shell, new CreateMapFromDemWizard());
    		dialog.open();
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
    					TerrainType.OCEAN));
    		}
    	}
    });
	}

	private void layoutMainShell(final Shell shell) {
//		shell.setLayout(new GridLayout(2, false));
		shell.setLayout(new FillLayout());
		Composite controls = new Composite(shell, SWT.PUSH);
		controls.setLayout(new GridLayout(2, false));

		addTerrainButton(controls, "Ocean", TerrainType.OCEAN).setSelection(true);
		addTerrainButton(controls, "Plains", TerrainType.PLAINS);
		addTerrainButton(controls, "Hills", TerrainType.HILLS);
		addTerrainButton(controls, "Mountains", TerrainType.MOUNTAINS);

		hexMapCanvas = new HexMapCanvas(shell, SWT.PUSH);
	}

	private Button addTerrainButton(Composite parent, String labelText,
			final TerrainType terrainType) {
		Label label = new Label(parent, SWT.PUSH);
		label.setText(labelText);
		Button button = new Button(parent, SWT.PUSH | SWT.RADIO);
		button.setText(labelText);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				hexMapCanvas.setSelectedTerrainType(terrainType);
			}
		});
		return button;
	}

	private void setMap(GameMap gameMap) {
		hexMapCanvas.setGameMap(gameMap);
	}
}
