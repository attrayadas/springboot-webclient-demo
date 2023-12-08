package com.attraya.repository;

import com.attraya.model.BookRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMyShowRepository extends JpaRepository<BookRequest, Integer> {

}
