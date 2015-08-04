package singularity.utility;

import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class NashornEngine {

	public Object toHtml(String md) throws Throwable {
		ScriptEngineManager engineManager = new ScriptEngineManager();
		ScriptEngine engine = engineManager.getEngineByName("nashorn");
		engine.eval(new FileReader("src/main/webapp/js/markdown.js"));
		//engine.eval("function mdToHtml (md) { return markdown.ToHTML(md); }");
		engine.eval("var toHtml = function(md) { return markdown.toHTML(md); }");
		Invocable invocable = (Invocable) engine;
		//return invocable.invokeFunction("toHtml", md);
		NashornMarkdown markdown = invocable.getInterface(NashornMarkdown.class); 
		return markdown.toHtml(md);
	}
}
