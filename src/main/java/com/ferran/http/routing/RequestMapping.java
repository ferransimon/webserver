package com.ferran.http.routing;


import com.ferran.http.MediaType;
import com.ferran.http.RequestMethod;
import com.ferran.http.tokenizer.Tokenizer;
import com.ferran.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class RequestMapping {

    private String path;
    private Pattern pathPattern;
    private List<RequestMethod> requestMethod;
    private String acceptMediaType;
    private String produceMediaType;
    private RequestHandler handler;
    private boolean isAnonimous;
    private Set<User.Role> roles;

    private RequestMapping(RequestMappingBuilder builder){
        this.path = builder.path;
        this.pathPattern = builder.pathPattern;
        this.requestMethod = builder.requestMethod;
        this.acceptMediaType = builder.acceptMediaType;
        this.produceMediaType = builder.produceMediaType;
        this.handler = builder.handler;
        this.roles = builder.roles;
        this.isAnonimous = builder.isAnonimous;
    }

    public String getPath() {
        return path;
    }

    public Pattern getPathPattern() {
        return pathPattern;
    }

    public List<RequestMethod> getRequestMethod() {
        return requestMethod;
    }

    public String getAcceptMediaType() {
        return acceptMediaType;
    }

    public String getProduceMediaType() {
        return produceMediaType;
    }

    public RequestHandler getHandler() {
        return handler;
    }

    public Set<User.Role> getRoles() {
        return roles;
    }

    public boolean isAnonimous() {
        return isAnonimous;
    }

    public boolean match(String requestURI){
        boolean match = this.pathPattern.matcher(requestURI).find();
        return match;
    }

    public static class RequestMappingBuilder {
        private final String path;
        private final Pattern pathPattern;
        private final List<RequestMethod> requestMethod;
        private final RequestHandler handler;
        private boolean isAnonimous = false;
        private String acceptMediaType = MediaType.ALL;
        private String produceMediaType = MediaType.TEXT_HTML;
        private Set<User.Role> roles = Collections.emptySet();

        public RequestMappingBuilder(String path, List<RequestMethod> requestMethod, RequestHandler handler, Tokenizer tokenizer) {
            this.path = path;
            this.pathPattern = tokenizer.tokenize(path);
            this.requestMethod = requestMethod;
            this.handler = handler;
        }

        public RequestMappingBuilder withAcceptMediaType(String mediaType){
            this.acceptMediaType = mediaType;
            return this;
        }

        public RequestMappingBuilder withProduceMediaType(String mediaType){
            this.produceMediaType = mediaType;
            return this;
        }

        public RequestMappingBuilder withRoles(Set<User.Role> roles){
            this.roles = roles;
            return this;
        }

        public RequestMappingBuilder isAnonimous(boolean isAnonimous){
            this.isAnonimous = isAnonimous;
            return this;
        }

        public RequestMapping build(){
            return new RequestMapping(this);
        }
    }
}
