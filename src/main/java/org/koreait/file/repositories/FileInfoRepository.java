package org.koreait.file.repositories;

import com.querydsl.core.BooleanBuilder;
import org.koreait.file.constants.FileStatus;
import org.koreait.file.entities.FileInfo;
import org.koreait.file.entities.QFileInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.asc;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long>, QuerydslPredicateExecutor<FileInfo> {


    default List<FileInfo> getList(String gid, String location, FileStatus status) {
        status = Objects.requireNonNullElse(status, FileStatus.ALL);

        QFileInfo fileInfo = QFileInfo.fileInfo;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(fileInfo.gid.eq(gid)); // 필수

        if (StringUtils.hasText(location)) { // 선택
            andBuilder.and(fileInfo.location.eq(location));
        }

        // 파일 작업 완료 상태
        if (status != FileStatus.ALL) {
            andBuilder.and(fileInfo.done.eq(status == FileStatus.DONE));
        }

        return (List<FileInfo>)findAll(andBuilder, Sort.by(asc("createdAt")));
    }

    default List<FileInfo> getList(String gid, String location) {
        return getList(gid, location, FileStatus.DONE);
    }

    default List<FileInfo> getList(String gid) { // 파일 그룹작업 완료된 파일
        return getList(gid, null);
    }
}
