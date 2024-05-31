package com.freelibrary.Paplibrary.file.storage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file,String filename) {


        // Pobranie oryginalnej nazwy pliku
        String originalFilename = file.getOriginalFilename();

        // Pobranie rozszerzenia pliku
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

        // Dodanie rozszerzenia do nazwy pliku docelowego
        String fullFilename = filename + "." + extension;




        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = this.rootLocation.resolve(
                            fullFilename)
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            if (Files.exists(destinationFile)) {
                throw new StorageException("File already exists: " + fullFilename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        try {
            // Przeszukaj katalog w poszukiwaniu pliku z odpowiednim rozszerzeniem
            try (Stream<Path> paths = Files.walk(rootLocation)) {
                Optional<Path> file = paths
                        .filter(path -> path.getFileName().toString().startsWith(filename))
                        .findFirst();
                if (file.isPresent()) {
                    return file.get();
                } else {
                    throw new FileNotFoundException("File with name " + filename + " not found.");
                }
            }
        } catch (IOException e) {
            throw new StorageException("Failed to load file " + filename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());


            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }



    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}