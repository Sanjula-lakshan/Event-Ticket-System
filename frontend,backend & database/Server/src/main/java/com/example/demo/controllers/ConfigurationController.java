package com.example.demo.controllers;

import com.example.demo.models.Configuration;
import com.example.demo.repositories.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/configuration")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationRepository configurationRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Configuration>> getAllConfigurations() {
        List<Configuration> configurations = configurationRepository.findAll();
        return ResponseEntity.ok(configurations);
    }

    @PostMapping
    public ResponseEntity<String> saveConfiguration(@Valid @RequestBody Configuration configuration) {
        configuration.setId(null);

        configurationRepository.save(configuration);

        return ResponseEntity.status(HttpStatus.CREATED).body("Configuration saved successfully");
    }


    @PutMapping
    public ResponseEntity<String> updateConfiguration(@Valid @RequestBody Configuration configuration) {

        Optional<Configuration> extConfiguration = configurationRepository.findById(configuration.getId());

        if (extConfiguration.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration with id " + configuration.getId() + " not found");
        }

        configurationRepository.save(configuration);
        return ResponseEntity.status(HttpStatus.CREATED).body("Configuration Updated Successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteConfiguration(@Valid @RequestBody Configuration configuration) {

        Optional<Configuration> extConfiguration = configurationRepository.findById(configuration.getId());

        if (extConfiguration.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration with id " + configuration.getId() + " not found");
        }

        configurationRepository.deleteById(configuration.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Configuration Deleted Successfully");
    }

}
