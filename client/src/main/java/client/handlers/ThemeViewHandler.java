package client.handlers;

/**
 * Handler class for retrieving different UI themes using HTML
*/
public class ThemeViewHandler {

    public ThemeViewHandler() {
    }

    public String getDarkWebview() {
        return  """
                    (function() {
                        var style = document.createElement('style');
                        style.innerHTML = `
                            body {
                                background-color: #2e2e2e;
                                color: #ffffff;
                            }
                            a {
                                color: #4e9af1;
                            }
                        `;
                        document.head.appendChild(style);
                    })();
                """;
    }

    public String getLightWebView() {
        return """
                    (function() {
                        var style = document.createElement('style');
                        style.innerHTML = `
                            body {
                                background-color: #ffffff; /* Default white background */
                                color: #000000; /* Default black text */
                            }
                            a {
                                color: #0000ff; /* Default link color */
                            }
                        `;
                        document.head.appendChild(style);
                    })();
                """;
    }
}
