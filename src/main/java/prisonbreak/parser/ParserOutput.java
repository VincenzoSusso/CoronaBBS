/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prisonbreak.parser;

import prisonbreak.type.TokenAdjective;
import prisonbreak.type.TokenObject;
import prisonbreak.type.TokenVerb;

/**
 * @author pierpaolo
 */
public class ParserOutput {
    private TokenVerb verb;
    private TokenObject object;
    private TokenAdjective adjective;

    public ParserOutput(TokenVerb verb) {
        this.verb = verb;
        this.object = null;
        this.adjective = null;
    }

    public ParserOutput(TokenVerb verb, TokenObject object) {
        this.verb = verb;
        this.object = object;
        this.adjective = null;
    }

    public ParserOutput(TokenVerb verb, TokenObject object, TokenAdjective adjective) {
        this.verb = verb;
        this.object = object;
        this.adjective = adjective;
    }

    public void setVerb(TokenVerb verb) {
        this.verb = verb;
    }

    public TokenVerb getVerb() {
        return verb;
    }

    public void setObject(TokenObject object) {
        this.object = object;
    }

    public TokenObject getObject() {
        return object;
    }
}
