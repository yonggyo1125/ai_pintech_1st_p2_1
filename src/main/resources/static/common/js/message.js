const webSocket = new WebSocket(`ws://${location.host}/msg`);


webSocket.addEventListener("message", function(data) {
    const email = commonLib.getMeta("user");

    const { item, totalUnRead } = JSON.parse(data.data);
    let isShow = false;
    if (item.notice || (email && email === item?.receiver?.email)) { // 공지사항
        isShow = true;
    }

    if (isShow) { // 메세지 팝업
        commonLib.message("쪽지가 왔습니다.");
    }
});
