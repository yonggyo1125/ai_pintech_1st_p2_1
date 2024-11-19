/**
* 주소 검색 후 후속 처리 (회원 가입 양식)
*
*/
function callbackAddressSearch(data) {
    if (!data) {
        return;
    }

    const { zipCode, address } = data;

    frmJoin.zipCode.value = zipCode;
    frmJoin.address.value = address;
}