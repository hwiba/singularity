package me.singularityfor.utility.nashorn.markdown;

import me.singularityfor.SingularityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

/**
 * Created by hyva on 2015. 11. 4..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@Transactional
@WebAppConfiguration
public class NashornEngineTest {

    @Resource private NashornEngine nashornEngine;

    @Test
    public void testMarkdownToHtml() throws Throwable {
        assertEquals(nashornEngine.markdownToHtml("##Markdown"), "\n<h2>Markdown</h2>\n\n");
    }
}