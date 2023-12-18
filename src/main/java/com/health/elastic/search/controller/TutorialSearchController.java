package com.health.elastic.search.controller;

import java.util.Optional;

import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.health.elastic.search.contentfile.ContentsfromFile;
import com.health.elastic.search.model.TutorialSearch;
import com.health.elastic.search.repository.TutorialSearchRepository;
import com.health.elastic.search.service.TutorialSearchService;


@RestController
public class TutorialSearchController {
	
	@Autowired
	TutorialSearchRepository repo;
	
	@Autowired
	TutorialSearchService tutSearchService;
	
	@PostMapping("/addDocument/{path}/{documentType}/{documentTypeId}")
	public int addDocument(@PathVariable String path, @PathVariable String documentType, @PathVariable String documentTypeId) {
		
		TutorialSearch tut= new TutorialSearch();
		
		Parser parser= new AutoDetectParser();
		String docContent="";
	
		try {
			docContent=ContentsfromFile.extractContentForElastic(parser, path);
			tut.setDocumentContent(docContent);
		}
		catch(Exception e) {
			
		}
		
		tut.setDocumentType(documentType);
		tut.setDocumentTypeId(documentTypeId);
		repo.save(tut);
		return 1;
		
	}
	
	@PutMapping("/updateDocument/{path}/{documentType}/{documentTypeId}")
	public int updateDocument(@PathVariable String path,@PathVariable String documentType,@PathVariable String documentTypeId) {
		
		TutorialSearch tut = repo.findByDocumentTypeAndDocumentTypeIdAllIgnoreCase(documentType, documentTypeId);
		Parser parser= new AutoDetectParser();
		String docContent="";
	
		try {
			docContent=ContentsfromFile.extractContentForElastic(parser, path);
			tut.setDocumentContent(docContent);
			
			repo.save(tut);
		}
		catch(Exception e) {
			
		}
		
		
		
		return 1;
		
	}
	
	@GetMapping("/findByDocumentTypeAndDocumentTypeId/{documentType}/{documentTypeId}")
	public TutorialSearch findByDocumentTypeAndDocumentTypeId(@PathVariable String documentType,@PathVariable String documentTypeId) {
		TutorialSearch tut = repo.findByDocumentTypeAndDocumentTypeIdAllIgnoreCase(documentType, documentTypeId);
		return tut;
	}
	
	
	
	@DeleteMapping("/deleteDocument/{id}")
	public String deleteDocument(@PathVariable String id) {	
		repo.deleteById(id);
		return "deleted";
		
	}
	
	
	
	
	
	
	/**Testing */
	
	@PostMapping("/addDocumentTest/{content}/{documentType}/{documentTypeId}")
	public int addDocumentTest(@PathVariable String content, @PathVariable String documentType, @PathVariable String documentTypeId) {
		TutorialSearch tut= new TutorialSearch();
		//tut.setId(repo.findMaxId().getId());
		tut.setId("1");
		tut.setDocumentContent(content);
		tut.setDocumentType(documentType);
		tut.setDocumentTypeId(documentTypeId);
		repo.save(tut);
		return 1;
		
	}
	

	@PutMapping("/updateDocumentTest/{content}/{documentType}/{documentTypeId}")
	public int updateDocumentTest(@PathVariable String content,@PathVariable String documentType,@PathVariable String documentTypeId) {
		
		TutorialSearch tut = repo.findByDocumentTypeAndDocumentTypeIdAllIgnoreCase(documentType, documentTypeId);
		
		tut.setDocumentContent(content);
		
		repo.save(tut);
		return 1;
		
	}

}
