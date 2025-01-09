package client.services;

import client.state.ResourceBundleHolder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import commons.exceptions.ExceptionType;
import commons.exceptions.MarkdownRenderException;
import org.springframework.http.HttpStatus;


import java.util.Arrays;
import java.util.ResourceBundle;

import static commons.exceptions.ErrorKeys.*;

/**
 * Markdown class to parse, render, and print Note content as HTML.
 */

@Singleton
public class Markdown {
    private final HtmlRenderer htmlRenderer;
    private final Parser parser;
    private final ResourceBundleHolder resourceBundleHolder;

    /**
     * Contructor for no-param
     */
    @Inject
    public Markdown(ResourceBundleHolder resourceBundleHolder) {
        this.resourceBundleHolder = resourceBundleHolder;
        MutableDataSet options = new MutableDataSet();

        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

        parser = Parser.builder(options).build();
        htmlRenderer = HtmlRenderer.builder(options).build();
    }

    @Inject
    public Markdown(HtmlRenderer htmlRenderer,
                    Parser parser,
                    ResourceBundleHolder resourceBundleHolder) {
        this.htmlRenderer = htmlRenderer;
        this.parser = parser;
        this.resourceBundleHolder = resourceBundleHolder;
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
                throw new MarkdownRenderException(
                        "Error instantiating htmlRenderer",
                        HttpStatus.BAD_REQUEST.value(),
                        ExceptionType.SERVER_ERROR
                );
            }
            if (htmlRenderer.render(parser.parse(commonmark)).isEmpty()) {
                throw new MarkdownRenderException(
                        "Error rendering markdown, Make sure the markdown syntax is valid",
                        HttpStatus.BAD_REQUEST.value(),
                        ExceptionType.INVALID_REQUEST
                );
            }
            StringBuilder goodString = new StringBuilder();
            if (commonmark.contains("\\")) {
                for (int i = 0; i < commonmark.length(); i++) {
                    char character = commonmark.charAt(i);
                    goodString.append(character);
                    if (character == '\\') goodString.append(character);
                }
            } else {
                goodString = new StringBuilder(commonmark);
            }
            return htmlRenderer.render(parser.parse(goodString.toString()));
        } catch (MarkdownRenderException e) {
            return showDialog(e, resourceBundleHolder);
        }
    }

    /**
     * Shows an error dialog based on the type of the Markdown exception
     * @param e
     */
    public String showDialog(MarkdownRenderException e, ResourceBundleHolder resourceBundleHolder) {
        ResourceBundle resourceBundle = resourceBundleHolder.getResourceBundle();
        switch (e.getType()) {
            case SERVER_ERROR -> {
                return htmlRenderer.render(parser.parse(resourceBundle.getString(MARKDOWN_SERVER.getKey())));
            }
            case INVALID_REQUEST -> {
                return htmlRenderer.render(parser.parse(resourceBundle.getString(MARKDOWN_INVALID_REQUEST.getKey())));
            }
        }
        return null;
    }
}
