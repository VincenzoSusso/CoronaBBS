package prisonbreak.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import prisonbreak.type.TokenAdjective;
import prisonbreak.type.TokenObject;
import prisonbreak.type.TokenVerb;

public class ParserIta extends Parser {

    public ParserIta(Set<TokenVerb> verbs, Set<TokenObject> objects, Set<TokenAdjective> adjectives) {

        super(initValidsentences(), verbs, objects, adjectives);
    }

    private static List<List<TokenType>> initValidsentences() {
        List<List<TokenType>> validsentences = new ArrayList<>();

        // Accepted sentences form
        validsentences.add(new ArrayList<>(Collections.singletonList(TokenType.VERB)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.OBJECT)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.ARTICLE, TokenType.OBJECT)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.OBJECT, TokenType.ADJECTIVE)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.ADJECTIVE, TokenType.OBJECT)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.ARTICLE, TokenType.OBJECT, TokenType.ADJECTIVE)));
        validsentences.add(new ArrayList<>(Arrays.asList(TokenType.VERB, TokenType.ARTICLE, TokenType.ADJECTIVE, TokenType.OBJECT)));

        return validsentences;
    }

    private List<List<Token>> separatesentences(Iterator<Token> iterator) {
        List<List<Token>> sentences = new ArrayList<>();

        List<Token> sentence = new ArrayList<>();

        while (iterator.hasNext()) {
            Token token = iterator.next();

            // If there is a junction then it means that there is another sentence
            if (token.getType().equals(TokenType.JUNCTION)) {

                // The sentence cannot end with a junction so i add the token to produce a SyntaxErrorException
                if (!iterator.hasNext()) {
                    sentence.add(token);
                }

                sentences.add(sentence);
                sentence = new ArrayList<>();
            } else {
                sentence.add(token);
            }
        }

        if (!sentence.isEmpty()) {
            sentences.add(sentence);
        }

        return sentences;
    }

    @Override
    public ScannerToken initScanner(Set<TokenVerb> verbs, Set<TokenObject> objects, Set<TokenAdjective> adjectives) {
        ScannerTokenIta scanner = new ScannerTokenIta();

        scanner.setSkipCharacters(new HashSet<>(Arrays.asList("\n", "\t", "\r", " ")));
        scanner.setVerbs(verbs);
        scanner.setObjects(objects);
        scanner.setArticles(new HashSet<>(Arrays.asList("il", "lo", "l'", "la", "i", "gli", "le", "un", "uno", "una", "un'", "del", "dello", "della", "dei", "degli", "delle")));
        scanner.setAdjectives(adjectives);
        scanner.setJunctions(new HashSet<>(Arrays.asList("e", "dopodiche", "dopodiché", "dopodiche'", "dopodichè", "successivamente", "poi")));

        return scanner;
    }

    @Override
    public List<ParserOutput> parse(String stringToParse) throws Exception {
        List<List<Token>> sentences; //Each Token list is one sentence combined with another from TokenType.Junction

        // Check the syntax of the sentence
        getScanner().setsentence(stringToParse);
        sentences = separatesentences(getScanner().tokenize());
        areValidsentences(sentences);

        return generateParserOutput(sentences.iterator());
    }

}
