package client.services;

import client.state.ResourceBundleHolder;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownTest {
    private static ResourceBundleHolder holder = new ResourceBundleHolder();
    @BeforeAll
    public static void setUp() {
        holder.setResourceBundle(ResourceBundle.getBundle("language", Locale.US));
    }

    @Test
    public void renderBody() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser, holder);

        String result = markdown.render("Team 86 is the *best* team!\n");
        String expected = "<p>Team 86 is the <em>best</em> team!</p>\n";
        assertEquals(expected, result);
    }

    @Test
    public void renderEmpty() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser, holder);
        assertEquals("<p>Invalid Markdown syntax please try again</p>"+"\n", markdown.render(""));
    }

    @Test
    public void renderLine() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser, holder);

        String result = markdown.render("---");
        String expected = "<hr />\n";
        assertEquals(expected, result);
    }

    @Test
    public void renderNull() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser, holder);
        assertEquals("<p>Invalid Markdown syntax please try again</p>"+"\n", markdown.render(null));
    }

    @Test
    public void renderTable() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser, holder);

        String result = markdown.render("day|time|spent\n" +
                ":---|:---:|--:\n" +
                "feb. 7. wed|18:00|4h 40m\n" +
                "apr. 3. thu|20:00|4h\n" +
                "dec. 7. wed|20:40|9h 20m\n" +
                "total:|| 23h");
        String expected = "<p>day|time|spent\n" +
                ":---|:---:|--:\n" +
                "feb. 7. wed|18:00|4h 40m\n" +
                "apr. 3. thu|20:00|4h\n" +
                "dec. 7. wed|20:40|9h 20m\n" +
                "total:|| 23h</p>\n";
        assertEquals(expected, result);
    }
}
