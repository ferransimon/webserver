package com.ferran.http;


import com.ferran.http.tokenizer.BasicPathTokenizer;
import com.ferran.http.tokenizer.Tokenizer;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PathTokenizerTest {

    //Class under test
    private Tokenizer basicPathTokenizer = new BasicPathTokenizer();

    @Test
    public void test_get_pattern_from_path_with_path_params(){
        String path = "/test/{userId}/roles";
        String requestedPath = "/test/1234/roles";

        Pattern pathPattern = basicPathTokenizer.tokenize(path);
        Matcher matcher = pathPattern.matcher(requestedPath);
        assertTrue(matcher.find());
        assertEquals("1234", matcher.group(1));
    }
}
