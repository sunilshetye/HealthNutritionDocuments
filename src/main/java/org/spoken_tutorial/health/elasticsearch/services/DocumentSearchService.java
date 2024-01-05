package org.spoken_tutorial.health.elasticsearch.services;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spoken_tutorial.health.elasticsearch.contentfile.ContentsfromFile;
import org.spoken_tutorial.health.elasticsearch.models.DocumentSearch;
import org.spoken_tutorial.health.elasticsearch.repositories.DocumentSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class DocumentSearchService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentSearchService.class);

    @Autowired
    private DocumentSearchRepository repo;

    @Autowired
    private ContentsfromFile contentsfromFile;

    public ResponseEntity<DocumentSearch> addContent(@PathVariable String path, @PathVariable String documentType,
            @PathVariable String documentTypeId) {
        DocumentSearch tut = new DocumentSearch();

        Parser parser = new AutoDetectParser();
        String docContent = "";

        try {
            docContent = contentsfromFile.extractContent(parser, path);
            tut.setDocumentContent(docContent);
            tut.setDocumentType(documentType);
            tut.setDocumentId(documentTypeId);
            tut = repo.save(tut);

        } catch (Exception e) {

            logger.error("Error in the document Content: {}", docContent, e);
        }

        return new ResponseEntity<>(tut, HttpStatus.OK);
    }
}
