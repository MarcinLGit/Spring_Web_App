package com.freelibrary.Paplibrary.file;

import java.io.IOException;
import java.util.stream.Collectors;

import com.freelibrary.Paplibrary.book.BookDto;
import com.freelibrary.Paplibrary.book.BookRepository;
import com.freelibrary.Paplibrary.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.freelibrary.Paplibrary.file.storage.StorageFileNotFoundException;
import com.freelibrary.Paplibrary.file.storage.StorageService;

@Controller
@RequestMapping("/file")
public class FileUploadController {


	@Autowired
	private BookService bookService;

	@Autowired
	private BookRepository bookRepository;

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
						path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
								"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		return "uploadForm";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {


		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();


		int dotIndex = file.getFilename().lastIndexOf('.');

		String extension = file.getFilename().substring(dotIndex);
		// Pobranie tytułu książki z obiektu BookDto
		BookDto bookDto = bookService.findBookByHash(filename);
		String title = bookDto.getTitle();
		String author = bookDto.getAuthor();
		System.out.println(extension);
		String newFileName = title+"_"+author+extension;


		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + newFileName + "\"")
				.body(file);
	}


	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file,
								   RedirectAttributes redirectAttributes) {

		storageService.store(file,"x");
		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/file/";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
