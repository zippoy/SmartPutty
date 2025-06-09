package com.sp.control;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import java.io.OutputStream;
import java.io.PrintStream;

public class MyPrintStream extends PrintStream {

    private Text text;

    public MyPrintStream(OutputStream out, Text text) {
        super(out);
        this.text = text;
    }

    /**
     *
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);

        /*  */
        Display.getDefault().syncExec(new Thread() {
            @Override
            public void run() {
                /*  */
                text.append(message + "\n");
            }
        });
    }

}