package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MapEditor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = new Display();
    final Shell shell = new Shell(display);
    Menu menu = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menu);
    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
    fileItem.setText("&File");
    Menu submenu = new Menu(shell, SWT.DROP_DOWN);
    fileItem.setMenu(submenu);
    MenuItem item = new MenuItem(submenu, SWT.PUSH);
    item.setText("Create from DEM");
    item.addSelectionListener(new SelectionAdapter() {
    	@Override
    	public void widgetSelected(SelectionEvent e) {
//    		CreateMapFromDemDialog dialog = new CreateMapFromDemDialog(shell);
//    		dialog.setText("Create Map from DEM");
//    		GameMap map = dialog.open();
    		WizardDialog dialog = new WizardDialog(shell, new CreateMapFromDemWizard());
    		dialog.open();
    	}
    });
//    shell.setLayout(new FillLayout());
//    shell.addControlListener(new ControlAdapter() {
//    	public void controlResized(ControlEvent e) {
//    		label.setBounds(shell.getClientArea());
//    	}
//    });
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
      	display.sleep();
      }
    }
    display.dispose ();
	}

}
