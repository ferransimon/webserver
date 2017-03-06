package com.ferran.http.tokenizer;


import java.util.regex.Pattern;

public interface Tokenizer {

    Pattern tokenize(String text);

}
