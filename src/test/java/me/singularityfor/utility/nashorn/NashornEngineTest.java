package me.singularityfor.utility.nashorn;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Order on 2015. 11. 4..
 */
@Slf4j
public class NashornEngineTest {

    NashornEngine ns = new NashornEngine();

    @Test
    public void testMarkdownToHtml() throws Throwable {
        assertEquals(ns.markdownToHtml("##Markdown"), "\n<h2>Markdown</h2>\n\n");
    }
}