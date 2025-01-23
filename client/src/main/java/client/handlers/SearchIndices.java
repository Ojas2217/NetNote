package client.handlers;

import javafx.util.Pair;

/**
 * Handler class for handling the SearchContent searchIndices
 * <p>
 * The {@code SearchIndices} class stores all found indices of a certain NoteSearchResult
 * </p>
 */
public class SearchIndices {
    private final int startIndex;
    private final int endIndex;
    private final int lineNr;
    private final int lineOffset;

    /**
     * The constructor that initializes all fields
     *
     * @param startIndex the starting index of the string that was found
     * @param endIndex the end index of the string that was found
     * @param lineNrOffset a pair containing the lineNr and lineOffset of the string that was found
     */
    public SearchIndices(int startIndex, int endIndex, Pair<Integer, Integer> lineNrOffset) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.lineNr = lineNrOffset.getKey();
        this.lineOffset = lineNrOffset.getValue();
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public int getLineNr() {
        return lineNr;
    }

    public int getLineOffset() {
        return lineOffset;
    }
}
