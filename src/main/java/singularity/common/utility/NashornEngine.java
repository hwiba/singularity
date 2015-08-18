package singularity.common.utility;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

public class NashornEngine {

	public Object markdownToHtml(String md) throws Throwable {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("nashorn");
		engine.eval(new FileReader("src/main/webapp/js/markdown.js"));
		engine.eval("var toHtml = function(md) { return markdown.toHTML(md, 'Maruku'); }");
		Invocable invocable = (Invocable) engine;
		NashornMarkdown markdown = invocable.getInterface(NashornMarkdown.class); 
		return markdown.toHtml(md);
	}
}
