package singularity.common.utility;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import singularity.app.SingularityApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SingularityApplication.class)
@WebAppConfiguration
public class NashornEngineTest {

	@Test
	public void markdownToHtml() throws Throwable {
		NashornEngine ns = new NashornEngine();
		assertEquals(ns.markdownToHtml("## Markdown"), "<h2>Markdown</h2>");
	}

}
