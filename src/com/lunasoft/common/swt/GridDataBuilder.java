package com.lunasoft.common.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class GridDataBuilder {

	private int horizontalAlignment = SWT.BEGINNING;
	private int verticalAlignment = SWT.BEGINNING;
	private boolean grabExcessHorizontalSpace = false;
	private boolean grabExcessVerticalSpace = false;
	private int horizontalSpan = 1;
	private int verticalSpan = 1;

	public GridData build() {
		return new GridData(horizontalAlignment, verticalAlignment,
				grabExcessHorizontalSpace, grabExcessVerticalSpace,
				horizontalSpan, verticalSpan);
	}

	public GridDataBuilder withHorizontalSpan(int horizontalSpan) {
		this.horizontalSpan = horizontalSpan;
		return this;
	}

	public GridDataBuilder withVerticalSpan(int verticalSpan) {
		this.verticalSpan = verticalSpan;
		return this;
	}
}
