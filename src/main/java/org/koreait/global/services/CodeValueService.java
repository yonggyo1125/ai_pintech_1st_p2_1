package org.koreait.global.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.koreait.global.entities.CodeValue;
import org.koreait.global.repositories.CodeValueRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class CodeValueService {
    private final CodeValueRepository repository;
    private final ObjectMapper om;

    /**
     * JSON 문자열로 변환후 저장
     *
     * @param code
     * @param value
     */
    public void save(String code, Object value) {
        CodeValue item = repository.findById(code).orElseGet(CodeValue::new);

        try {
            String json = om.writeValueAsString(value);

            item.setCode(code);
            item.setValue(json);

            repository.saveAndFlush(item);

        } catch (JsonProcessingException e) {}
    }
}
