package org.example.storage.repository;

import org.example.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    Page<Event> findByTitle(String title, Pageable pageable);

    Page<Event> findByDate(Date date, Pageable pageable);
}