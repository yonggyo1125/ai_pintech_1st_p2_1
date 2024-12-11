package org.koreait.global.paging;

public class Pagination {
    /**
     *
     * @param page : 현재 페이지 번호
     * @param total : 총 레코드 갯수
     * @param ranges : 페이지 구간 갯수
     * @param limit : 한 페이지당 출력될 레코드 갯수
     */
    public Pagination(int page, int total, int ranges, int limit) {
        // 페이징 기본값 처리
        page = Math.max(page, 1);
        total = Math.max(total, 0);
        ranges = ranges < 1 ? 10 : ranges;
        limit = limit < 1 ? 20 : limit;

        if (total == 0) {
            return;
        }

        // 전체 페이지 갯수
        int totalPages = (int)Math.ceil(total / (double)limit);

        // 구간 번호 - 0, 1, 2
        int rangeCnt = (page - 1) / ranges;
        int firstRangePage = rangeCnt * ranges + 1; // 현재 구간의 시작 페이지 번호
        int lastRangePage = firstRangePage + ranges - 1; // 현재 구간의 마지막 페이지 번호
        lastRangePage = Math.min(lastRangePage, totalPages);

        int prevRangeFirstPage = 0, nextRangeFirstPage = 0; // 이전 구간 시작 페이지 번호, 다음 구간 시작 페이지 번호
        if (rangeCnt > 0) {
            prevRangeFirstPage = (rangeCnt - 1) * ranges + 1;
        }
    }

}
