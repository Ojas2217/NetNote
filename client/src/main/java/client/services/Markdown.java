package client.services;

import com.google.inject.Singleton;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import commons.ExceptionType;
import commons.MarkdownRenderException;
import org.springframework.http.HttpStatus;


import java.util.Arrays;
/**
 * Markdown class to parse, render, and print Note content as HTML.
 */

@Singleton
public class Markdown {
    private final HtmlRenderer htmlRenderer;
    private final Parser parser;


    /**
     * Contructor for no-param
     */
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
    public String render(String commonmark) throws MarkdownRenderException {
        try {
            if (htmlRenderer == null) {
                throw new MarkdownRenderException("Error instantiating htmlRenderer", HttpStatus.BAD_REQUEST.value(), ExceptionType.SERVER_ERROR);
            }
            if (htmlRenderer.render(parser.parse(commonmark)).isEmpty()) {
                throw new MarkdownRenderException("Error rendering markdown, Make sure the markdown syntax is valid", HttpStatus.BAD_REQUEST.value(), ExceptionType.INVALID_REQUEST);
            }
            return htmlRenderer.render(parser.parse(commonmark));
        } catch (MarkdownRenderException e) {
            return showDialog(e);
        }
    }

    /**
     * Shows an error dialog based on the type of the Markdown exception
     * @param e
     */
    public String showDialog(MarkdownRenderException e) {
        switch (e.getType()) {
            case SERVER_ERROR -> {
                String html = htmlRenderer.render(parser.parse("Error instantiating html renderer please try again"));
                return html;
            }
            case INVALID_REQUEST -> {
                String html = htmlRenderer.render(parser.parse("Invalid markdown syntax please try again"));
                return html;
            }
        }
        return null;
    }

    public HtmlRenderer getHtmlRenderer() {
        return htmlRenderer;
    }

    public Parser getParser() {
        return parser;
    }
}
