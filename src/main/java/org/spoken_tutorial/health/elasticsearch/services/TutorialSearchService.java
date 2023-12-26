package org.spoken_tutorial.health.elasticsearch.services;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.spoken_tutorial.health.elasticsearch.contentfile.ContentsfromFile;
import org.spoken_tutorial.health.elasticsearch.models.TutorialSearch;
import org.spoken_tutorial.health.elasticsearch.repositories.TutorialSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class TutorialSearchService {
    @Autowired
    TutorialSearchRepository repo;

    public ResponseEntity<TutorialSearch> addContent(@PathVariable String path, @PathVariable String documentType,
            @PathVariable String documentTypeId) {
        TutorialSearch tut = new TutorialSearch();

        Parser parser = new AutoDetectParser();
        String docContent = "";

        try {
            docContent = ContentsfromFile.extractContentForElastic(parser, path);
            tut.setDocumentContent(docContent);
            tut.setDocumentType(documentType);
            tut.setDocumentId(documentTypeId);
            tut = repo.save(tut);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(tut, HttpStatus.OK);
    }
}
