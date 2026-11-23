
package com.autism_rehablito_app.config;

import com.autism_rehablito_app.entity.Branch;
import com.autism_rehablito_app.repository.BranchRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminBranchDataInitializer {

    @Bean
    CommandLineRunner initializeAdminBranches(
            BranchRepository branchRepository) {

        return args -> {

            // Patna Branches
            createBranchIfNotExists(
                    branchRepository,
                    "Patna(Rajendra Nagar)",
                    "Rajendra Nagar"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Patna(Ram Nagri)",
                    "Ram Nagri"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Patna(Anishabad, Phulwari)",
                    "Anishabad, Phulwari"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Patna(Boring Road)",
                    "Boring Road"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Patna(Zero mile)",
                    "Zero mile"
            );

            // Other Branches
            createBranchIfNotExists(
                    branchRepository,
                    "Hajipur(Hathsarganj)",
                    "Hathsarganj"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Darbhanga(MaulaGanj)",
                    "MaulaGanj"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Madhubani(Police line)",
                    "Police line"
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Samastipur",
                    ""
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Siwan",
                    ""
            );

            createBranchIfNotExists(
                    branchRepository,
                    "Gorakhpur(Nauka Vihar)",
                    "Nauka Vihar"
            );
        };
    }

    private void createBranchIfNotExists(
            BranchRepository branchRepository,
            String branchName,
            String address) {

        if (branchRepository
                .findByBranchName(branchName)
                .isEmpty()) {

            Branch branch = new Branch();

            branch.setBranchName(branchName);
            branch.setAddress(address);

            branchRepository.save(branch);
        }
    }
}
