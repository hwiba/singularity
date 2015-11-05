package me.singularityfor.utility.nashorn;

import lombok.extern.slf4j.Slf4j;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Order on 2015. 11. 4..
 */
@Slf4j
public class NashornEngine {

    public Object markdownToHtml(String md) throws Throwable {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        engine.eval(
                new FileReader("./src/main/resources/static/js/jspm_packages/npm/micromarkdown@0.3.4-a/dist/micromarkdown.min.js")
        );
        engine.eval("var toHtml = function(md) { return micromarkdown.parse(md); }");
        Invocable invocable = (Invocable) engine;
        NashornMarkdown markdown = invocable.getInterface(NashornMarkdown.class);
        return markdown.toHtml(md);
    }

}
