
package com.autism_rehablito_app.repository;

import com.autism_rehablito_app.entity.TherapistNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapistNoteRepository
        extends JpaRepository<TherapistNote, Long> {

    List<TherapistNote>
    findByChildIdAndTherapistIdOrderByCreatedAtDesc(
            Long childId,
            Long therapistId
    );

    List<TherapistNote>
    findBySessionIdAndTherapistIdOrderByCreatedAtDesc(
            Long sessionId,
            Long therapistId
    );
}
