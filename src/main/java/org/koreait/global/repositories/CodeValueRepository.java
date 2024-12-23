package org.koreait.global.repositories;

import org.koreait.global.entities.CodeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeValueRepository extends JpaRepository<CodeValue, String> {
}
