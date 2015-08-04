package singularity.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import singularity.SingularityApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@WebAppConfiguration
public class NashornEngineTest {
	private static final Logger logger = LoggerFactory.getLogger(NashornEngineTest.class);
	
	@Test
	public void markdownToHtml() throws Throwable {
		NashornEngine ns = new NashornEngine();
		logger.warn("\n========\n nashorn : ## Markdown => {}\n========\n", ns.toHtml("## Markdown"));
		assertEquals(ns.toHtml("## Markdown"), "<h2>Markdown</h2>");
		
	}

}
