package com.lunasoft.dynasty.tools.mapeditor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

public class CreateBlankMapDialog extends Dialog {

	private int width;
	private int height;

	public CreateBlankMapDialog(Shell parent) {
		super(parent);
	}

	@Override
  protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label widthLabel = new Label(parent, SWT.PUSH);
		widthLabel.setText("Width in hexes");
		final Spinner widthSpinner = new Spinner(parent, SWT.PUSH);
		widthSpinner.setMinimum(8);

		Label heightLabel = new Label(parent, SWT.PUSH);
		heightLabel.setText("Height in hexes");
		final Spinner heightSpinner = new Spinner(parent, SWT.PUSH);
		heightSpinner.setIncrement(2);
		heightSpinner.setMinimum(8);

		widthSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				width = Integer.parseInt(widthSpinner.getText());
			}
		});
		heightSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				height = Integer.parseInt(heightSpinner.getText());
			}
		});

		widthSpinner.setSelection(16);
		heightSpinner.setSelection(12);

		return parent;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
