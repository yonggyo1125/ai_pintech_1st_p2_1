package org.koreait.file.repositories;

import com.querydsl.core.BooleanBuilder;
import org.koreait.file.entities.FileInfo;
import org.koreait.file.entities.QFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, QuerydslPredicateExecutor<FileInfo> {

    default List<FileInfo> getList(String gid) {
        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();

        andBuilder.and(fileInfo.gid.eq(gid));

        List<FileInfo> items = (List<FileInfo>)findAll(andBuilder);

        return items;
    }
}
