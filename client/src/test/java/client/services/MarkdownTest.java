package client.services;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MarkdownTest {

    @Test
    public void renderBody() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser);

        String result = markdown.render("Team 86 is the *best* team!\n");
        String expected = "<p>Team 86 is the <em>best</em> team!</p>\n";
        assertEquals(expected, result);
    }

    @Test
    public void renderEmpty() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser);

        String result = markdown.render("");
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void renderLine() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser);

        String result = markdown.render("---");
        String expected = "<hr />\n";
        assertEquals(expected, result);
    }

    @Test
    public void renderNull() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser);

        String result = markdown.render(null);
        assertEquals("", result);
    }

    @Test
    public void renderTable() {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Markdown markdown = new Markdown(renderer, parser);

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
