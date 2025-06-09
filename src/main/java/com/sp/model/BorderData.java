package com.sp.model;

import lombok.NoArgsConstructor;
import org.eclipse.swt.SWT;

/**
 * Indicates the region that a control belongs to.
 */
@NoArgsConstructor
public final class BorderData {

    public int region = SWT.CENTER;

    public BorderData(int region) {
        this.region = region;
    }

}
