package edu.gatech.cs4911.mintyfresh.test;

import android.content.Context;
import android.test.AndroidTestCase;

import java.io.File;
import java.io.FileInputStream;

import com.caverock.androidsvg.SVG;

public class SvgLoadTest extends AndroidTestCase {
    Context context;
    String filename;

    public void setUp() throws Exception {
        super.setUp();

        context = getContext();
        filename = "CUL_1.svg";
    }

    public void tearDown() throws Exception {

    }

    public void testFileOk() throws Exception {
        File svgFile = new File(context.getFilesDir(), filename);
        assertTrue(svgFile.exists());
        assertTrue(svgFile.isFile());
    }

    public void testSvgLoad() throws Exception {
        File svgFile = new File(context.getFilesDir(), filename);
        SVG svg = SVG.getFromInputStream(new FileInputStream(svgFile));
        assertNotNull(svg);
    }

}