package com.ferran.http.tokenizer;


import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasicPathTokenizer implements Tokenizer {

    private final static String PATH_SEPARATOR = "/";
    private final static String ALL_CHAR = "*";
    private final static String ALL_PATTERN = "?[a-zA-Z0-9]*";
    private final static String PARAM_PATTERN = "([a-zA-Z0-9]+)";
    private final static String PATH_PARAMS_PATTERM = "/?\\\\??[[a-zA-Z0-9]=[a-zA-Z0-9/]]*$";

    @Override
    public Pattern tokenize(String path) {
        LinkedList<String> listOfTokens = Stream.of(path.split(PATH_SEPARATOR)).filter(token -> !token.isEmpty()).map(
                token -> {
                    if(token.equals(ALL_CHAR)){
                       return ALL_PATTERN;
                    } else if(token.startsWith("{")){
                        return PARAM_PATTERN;
                    }else {
                        return token;
                    }
                }
        ).collect(Collectors.toCollection(LinkedList<String>::new));
        listOfTokens.addFirst("");
        return Pattern.compile("^"+String.join(PATH_SEPARATOR, listOfTokens)+PATH_PARAMS_PATTERM);
    }
}
