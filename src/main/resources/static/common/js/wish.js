

window.addEventListener("DOMContentLoaded", function() {
    const wishButtons = document.getElementsByClassName("wish-btn");
    for (const el of wishButtons) {
        el.addEventListener("click", function() {
            /**
            * 1. 로그인 상태 체크 - 클래스에 guest가 포함되어 있으면 미로그인 상태
            * 2. 미로그인 상태 -> 로그인 페이지 주소 이동, 로그인 완료시에는 현재 페이지로 다시 이동
            */
            if (this.classList.contains("guest")) { // 미로그인 상태
                alert("로그인이 필요한 서비스 입니다.");
                const { pathname, search } = location;
                const redirectUrl = search ? pathname + search : pathname;

                location.href= commonLib.url(`/member/login?redirectUrl=${redirectUrl}`);

                return;
            }
        });
    }
});