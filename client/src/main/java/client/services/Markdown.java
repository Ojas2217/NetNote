package client.services;

import com.google.inject.Singleton;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;

@Singleton
public class Markdown {
    private final HtmlRenderer htmlRenderer;
    private final Parser parser;

    public Markdown() {
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        parser = Parser.builder(options).build();
        htmlRenderer = HtmlRenderer.builder(options).build();
    }

    public Markdown(HtmlRenderer htmlRenderer,
                    Parser parser) {
        this.htmlRenderer = htmlRenderer;
        this.parser = parser;
    }

    /**
     * Returns rendered version (HTML) of the Note content.
     *
     * @param commonmark
     * @return HTML markdown
     */
    public String render(String commonmark) {
        return htmlRenderer.render(parser.parse(commonmark));
    }

    public HtmlRenderer getHtmlRenderer() {
        return htmlRenderer;
    }

    public Parser getParser() {
        return parser;
    }
}
