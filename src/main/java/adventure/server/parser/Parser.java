package adventure.server.parser;

import adventure.exceptions.inputException.InputErrorException;
import adventure.exceptions.inputException.SyntaxErrorException;
import adventure.server.type.TokenAdjective;
import adventure.server.type.TokenObject;
import adventure.server.type.TokenVerb;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Corona-Extra
 */
public abstract class Parser {
    private final ScannerToken scanner;
    private final List<List<TokenType>> validSentences;

    public Parser(List<List<TokenType>> validSentences, Set<TokenVerb> verbs, Set<TokenObject> objects,
                  Set<TokenAdjective> adjectives) {
        scanner = initScanner(verbs, objects, adjectives);
        this.validSentences = new ArrayList<>(validSentences);
    }

    public ScannerToken getScanner() {
        return scanner;
    }

    public List<TokenType> getTokenType(List<Set<Token>> sentence) {
        List<TokenType> tokens = new ArrayList<>();

        for (Set<Token> i : sentence) {
            i.stream().findFirst().ifPresent(t -> tokens.add(t.getType()));
        }

        return tokens;
    }

    private boolean isValidSentence(List<TokenType> sentence) {
        boolean validSentence = false;

        Iterator<List<TokenType>> validSentenceIterator = validSentences.iterator();
        while (validSentenceIterator.hasNext() && !validSentence) {
            validSentence = validSentenceIterator.next().equals(sentence);
        }

        return validSentence;
    }

    public boolean areValidSentences(List<List<Set<Token>>> sentences) throws SyntaxErrorException {
        Iterator<List<Set<Token>>> iterator;
        List<TokenType> tokenSentence;
        boolean validSentence = true;

        iterator = sentences.iterator();
        while (validSentence && iterator.hasNext()) {
            tokenSentence = getTokenType(iterator.next());
            validSentence = isValidSentence(tokenSentence);
        }

        if (!validSentence || sentences.isEmpty()) {
            throw new SyntaxErrorException();
        }

        return validSentence;
    }

    private TokenVerb getTokenVerb(List<Set<Token>> sentence) {
        TokenVerb token = null;
        Iterator<Set<Token>> i = sentence.iterator();

        while (token == null && i.hasNext()) {
            Set<Token> set = i.next();

            token = (TokenVerb) set.stream()
                    .filter(t -> t.getType().equals(TokenType.VERB))
                    .findFirst()
                    .orElse(null);
        }

        return token;
    }

    private Set<TokenObject> getTokenObject(List<Set<Token>> sentence) {
        Set<TokenObject> token = new HashSet<>();

        for (Set<Token> tokenSet : sentence) {
            if (tokenSet.stream().allMatch(t -> t.getType().equals(TokenType.OBJECT))) {
                tokenSet.forEach(t -> token.add((TokenObject) t));
            }
        }

        return token;
    }

    private TokenAdjective getTokenAdjective(List<Set<Token>> sentence) {
        TokenAdjective token = null;
        Iterator<Set<Token>> i = sentence.iterator();

        while (token == null && i.hasNext()) {
            Set<Token> set = i.next();

            token = (TokenAdjective) set.stream()
                    .filter(t -> t.getType().equals(TokenType.ADJECTIVE))
                    .findFirst()
                    .orElse(null);
        }

        return token;
    }

    private boolean isCorrectAdjective(Set<TokenObject> object, TokenAdjective adjective) {
        boolean isAdjective = false;

        if (adjective != null && !object.isEmpty()) {
            isAdjective = object.stream()
                    .anyMatch(o -> o.getAdjectives().contains(adjective));
        }

        return isAdjective;
    }

    private Set<TokenObject> getObjectWithAdjective(Set<TokenObject> object, TokenAdjective adjective) {
        return object.stream().filter(o -> o.getAdjectives().contains(adjective)).collect(Collectors.toSet());
    }

    public List<ParserOutput> generateParserOutput(Iterator<List<Set<Token>>> sentences) throws InputErrorException {
        List<ParserOutput> parserOutputs = new ArrayList<>(); //For each sentence a ParserOutput is created
        List<Set<Token>> sentence;
        TokenVerb verb;
        Set<TokenObject> object;
        TokenAdjective adjective;

        while (sentences.hasNext()) {
            sentence = sentences.next();
            verb = getTokenVerb(sentence);
            object = getTokenObject(sentence);
            adjective = getTokenAdjective(sentence);

            // Check if the adjective refers to the exact object
            if (!object.isEmpty() && adjective != null && !isCorrectAdjective(object, adjective)) {
                throw new InputErrorException();
            } else if(!object.isEmpty() && adjective != null && isCorrectAdjective(object, adjective)){
                object = getObjectWithAdjective(object, adjective);
            }

            parserOutputs.add(new ParserOutput(verb, object));

        }

        return parserOutputs;
    }

    public abstract ScannerToken initScanner(Set<TokenVerb> verbs, Set<TokenObject> objects, Set<TokenAdjective> adjectives);

    public abstract List<ParserOutput> parse(String stringToParse) throws Exception;

}
