package it.unibo.vocago.model.user.api;

import it.unibo.vocago.model.vocabulary.api.Vocabulary;

public interface User {

    String getUserName();

    Vocabulary getVocabulary();
    
    String getFirstLanguage();

    String getSecondLanguage();
    
}
