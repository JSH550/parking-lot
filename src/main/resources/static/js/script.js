


let markers = []; // 마커 배열

let map; //지도 DOM을 저장할 객체, 전역변수로 선언

let debounceTimer; // 디바운스를 위한 타이머 변수

document.addEventListener("DOMContentLoaded", function () {

    const searchInput = document.getElementById("searchInput");
    const searchToolbar = document.getElementById("searchToolbar");
    const clearSearch = document.getElementById("clearSearch");
    const searchResults = document.getElementById("searchResults");
    const mapContainer = document.getElementById("map");
    const locationBtn = document.getElementById("reloadLocation");//현재위치 재검색 버튼


    // 검색창 클릭 시 UI 변경 (지도 숨기기)
    searchInput.addEventListener("focus", function () {
        searchToolbar.classList.add("active"); // 검색 UI 표시
        mapContainer.style.display = "none"; // 지도 숨기기
        searchResults.style.display = "block"; // 검색 결과 표시 (중요)
        locationBtn.style.display="none";//현재위치 재검색 버튼 숨기기

    });
// ✅ 검색어 입력 시 디바운스를 적용하여 서버 요청
    searchInput.addEventListener("input", function () {
        clearTimeout(debounceTimer); // 기존 타이머 초기화

        debounceTimer = setTimeout(async function () {
            const query = searchInput.value.trim();
            if (query.length === 0) {
                searchResults.innerHTML = ""; // 검색 결과 초기화
                return;
            }

            const searchData = await fetchSearchResults(query);
            displaySearchResults(searchData);
        }, 300); // 0.2초(200ms) 동안 입력 없으면 실행
    });

//  X 버튼 클릭 시 검색창 초기화 및 지도 표시
    clearSearch.addEventListener("click", function () {
        searchInput.value = ""; // 검색창 내용 삭제
        searchResults.innerHTML = ""; // 검색 결과 초기화
        searchToolbar.classList.remove("active"); // 검색 UI 숨기기
        searchResults.style.display = "none"; // 검색 결과 숨기기 (중요)
        mapContainer.style.display = "block"; // 지도 다시 표시
        locationBtn.style.display="block";//현재위치 재검색 버튼 숨기기

    });


    //  지도 로드 및 위치 업데이트
    getUserLocation();
});


/**
 * 서버에서 받아온 검색 결과를 DOM 으로 표시하는 함수
 * - 검색 결과 클릭시, 해당 주차장 위치로 중심좌표 이동
 * @param data 주차장 정보를 담고있는 객체
 */

function displaySearchResults(data) {
    const searchResults = document.getElementById("searchResults");
    searchResults.innerHTML = ""; // 기존 리스트 초기화
    searchResults.style.display = "block"; // 검색 결과 다시 표시 (중요)

    if (!data || data.length === 0) {
        searchResults.innerHTML = "<li>검색 결과가 없습니다.</li>";
        return;
    }

    data.forEach(parking => {
        const li = document.createElement("li");
        li.classList.add("search-item"); // 스타일 적용

        // 주차장 이름
        const title = document.createElement("div");
        title.classList.add("title");
        title.innerText = parking.name;

        // 주차장 주소 (작은 글씨, 회색 스타일)
        const subtitle = document.createElement("div");
        subtitle.classList.add("subtitle");
        subtitle.innerText = parking.address || "주소 정보 없음"; // 주소가 없을 경우 예외 처리

        // 클릭 시 지도 이동
        li.addEventListener("click", function () {
            moveToParkingLocation(parking); // 선택한 주차장으로 이동
            closeSearch(); // 검색 종료 후 지도 다시 표시
        });

        li.appendChild(title);
        li.appendChild(subtitle);
        searchResults.appendChild(li);
    });
}


/**
 * 검색창을 숨기고 지도를 다시 표시하는 함수
 */
function closeSearch() {
    const searchToolbar = document.getElementById("searchToolbar");
    const mapContainer = document.getElementById("map");
    const searchResults = document.getElementById("searchResults");
    const locationBtn = document.getElementById("reloadLocation");//현재위치 재검색 버튼


    searchToolbar.classList.remove("active");
    mapContainer.style.display = "block"; // 지도 다시 표시
    searchResults.style.display = "none"; // 검색 결과 숨기기
    locationBtn.style.display="block";//현재위치 재검색 버튼 숨기기


}

/**
 * 네이버 지도 객체를 초기화하는 함수
 * @param latitude 위도 기본값 서울
 * @param longitude 경도 기본값 서울
 */
function initMap(latitude = 37.5665, longitude = 126.9780) {
    const mapOptions = {
        center: new naver.maps.LatLng(latitude, longitude),
        zoom: 16
    };

    map = new naver.maps.Map("map", mapOptions);
}

/**
 * 현재 표시되는 지도 객체의 가로 세로 거리를 계산하는 좌표
 * @returns 계산된 값을 반환(미터 단위)
 */
function getMapDistance() {
    if (!map) return null;

    // ✅ 현재 지도 화면의 좌표 범위 가져오기
    const bounds = map.getBounds();
    const southWest = bounds.getSW(); // 남서쪽 좌표
    const northEast = bounds.getNE(); // 북동쪽 좌표

    // ✅ 두 좌표 사이의 거리 계산 (미터 단위)
    const distance = getDistance(southWest, northEast);

    console.log("지도 화면의 거리:", distance, "m");
    return distance; // distance 값을 반환 (미터 단위)
}

//  좌표 간 거리 계산 함수 (하버사인 공식 사용)
function getDistance(coord1, coord2) {
    const R = 6371; // 지구 반지름 (km)
    const lat1 = coord1.lat() * (Math.PI / 180);
    const lon1 = coord1.lng() * (Math.PI / 180);
    const lat2 = coord2.lat() * (Math.PI / 180);
    const lon2 = coord2.lng() * (Math.PI / 180);

    const dLat = lat2 - lat1;
    const dLon = lon2 - lon1;

    const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
        Math.cos(lat1) * Math.cos(lat2) *
        Math.sin(dLon / 2) * Math.sin(dLon / 2);

    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = R * c * 1000; // 미터(m) 단위 반환

    return Math.round(distance); // 반올림하여 정수 반환
}

/**
 * 유저가 입력한 검색어로, 검색 API 를 호출 결과를 받아오는 함수
 * @param searchWord 유저가 입력한 검색어
 * @returns data 검색 결과가 담긴 배열 형태의 객체
 */
async function fetchSearchResults(searchWord) {
    console.log("검색 요청:", searchWord);
    const apiUrl = `/api/parkings?searchWord=${encodeURIComponent(searchWord)}`;

    try {
        const response = await fetch(apiUrl);
        //응답이 ok(200번) 아닐경우 에러로 던짐
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const data = await response.json();//응답 JSON 으로 변환
        console.log("검색 결과:", data);
        return data;//데이터 반환
    } catch (error) {
        console.error("검색 요청 실패:", error);
        return [];
    }
}


/**
 * 지도의 중심좌표를 변경하는 함수
 * @param parking
 * @returns {Promise<void>}
 */
async function  moveToParkingLocation(parking) {
    const searchResults = document.getElementById("searchResults");
    searchResults.style.display = "none"; // 검색 결과 숨기기
    document.getElementById("searchInput").value = parking.name; // 선택한 주차장 이름 입력창에 표시

    //받아온 값으로 position 객체 생성
    const position = new naver.maps.LatLng(parking.latitude, parking.longitude);
    map.setCenter(position); //센터 위치 주차장으로 변경
    map.setZoom(16);//줌 변경

    //주변 검색하는 기능도 필요할듯?

    const parkingData = await fetchLocationDataFromAPI(parking.latitude, parking.longitude);
    if (parkingData) {
        displayParkingSpots(parkingData);
    }

    showParkingDetails(parking);
}

/**
 * 사용자의 현재 위치를 가져와 근처의 주차장 정보를 지도에 표시하는 함수
 */
function getUserLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            async function (position) {
                const userLat = position.coords.latitude;
                const userLng = position.coords.longitude;
                console.log("사용자 위치:", userLat, userLng);

                initMap(userLat, userLng);
                const parkingData = await fetchLocationDataFromAPI(userLat, userLng);
                if (parkingData) {
                    displayParkingSpots(parkingData);
                }
            },
            function (error) {
                let errorMessage = "위치 정보를 가져올 수 없습니다.";
                switch (error.code) {
                    case error.PERMISSION_DENIED:
                        errorMessage = "위치 정보 제공이 차단되었습니다. 브라우저 설정을 확인하세요.";
                        break;
                    case error.POSITION_UNAVAILABLE:
                        errorMessage = "위치 정보를 사용할 수 없습니다.";
                        break;
                    case error.TIMEOUT:
                        errorMessage = "위치 정보를 가져오는 데 시간이 초과되었습니다.";
                        break;
                    default:
                        errorMessage = "알 수 없는 오류가 발생했습니다.";
                }
                console.warn(errorMessage);
                initMap(); // 기본 위치(서울) 사용
            }
        );
    } else {
        console.warn("Geolocation을 지원하지 않습니다.");
        initMap(); // 기본 위치 사용
    }
}


/**
 * 사용자의 좌표로 주차장 정보를 받아오는 API 호출하는 함수
 * @param lat 위도
 * @param lng 경도
 * @returns {Promise<any|null>} 응답 데이터
 */
async function fetchLocationDataFromAPI(lat, lng) {
    console.log("fetchLocationDataFromAPI 실행");
    const apiUrl = `/api/parkings/locations`;

    const distance = getMapDistance(); //  현재 지도 거리 가져오기

    const data = {
        distance: `${distance}m`, // 미터 단위로 전달
        latitude: lat,
        longitude: lng
    };

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const responseData = await response.json();
        console.log("API 응답 데이터:", responseData);
        return responseData;
    } catch (error) {
        console.error("API 요청 실패:", error);
        return null;
    }
}
// ✅ 오버레이 배열 추가 (마커 배열과 동일한 개념)
let overlays = [];

// 지도에 주차장 오버레이 추가
function displayParkingSpots(parkingData) {
    // 기존 마커 및 오버레이 삭제
    markers.forEach(marker => marker.setMap(null));
    markers = [];

    overlays.forEach(overlay => overlay.setMap(null)); // ✅ 기존 오버레이 지도에서 제거
    overlays = []; //  오버레이 배열 초기화

    if (!parkingData || parkingData.length === 0) {
        console.warn("주차장 데이터가 없습니다.");
        return;
    }

    console.log("주차장 데이터 개수:", parkingData.length);

    parkingData.forEach(parking => {
        const position = new naver.maps.LatLng(parking.latitude, parking.longitude);

        //  오버레이 클래스 정의
        class FreeOverlay extends naver.maps.OverlayView {
            constructor(position, parkingInfo) {
                super();
                this.position = position;
                this.parkingInfo = parkingInfo;
                this.div = null;
            }

            onAdd() {

                this.div = document.createElement("div"); // div 초기화
                this.div.classList.add("parking-overlay"); // 기본 스타일 적용

                // 가격에 따른 스타일 변경
                if (this.parkingInfo.feePerHour===0 || this.parkingInfo.feePerHour === "무료") {
                    this.div.innerHTML = "무료";
                    this.div.classList.add("free-parking");
                } else {
                    this.div.innerHTML = `${this.parkingInfo.feePerHour}원`;
                    this.div.classList.add("paid-parking");
                }


                //  오버레이 클릭 시 상세 정보 창 표시
                this.div.addEventListener("click", () => {
                    console.log("오버레이 클릭됨!");
                    showParkingDetails(this.parkingInfo);
                });

                const overlayLayer = this.getPanes().overlayLayer;
                overlayLayer.appendChild(this.div);
            }

            //지도위에 그리기
            draw() {
                if (!this.div) return;
                const projection = this.getProjection();
                const positionPixel = projection.fromCoordToOffset(this.position);

                this.div.style.left = `${positionPixel.x - 20}px`;
                this.div.style.top = `${positionPixel.y - 30}px`;
            }

            onRemove() {
                if (this.div && this.div.parentNode) {
                    this.div.parentNode.removeChild(this.div);
                    this.div = null;
                }
            }
        }

        // 📌 오버레이 객체 생성 및 지도에 추가
        const freeOverlay = new FreeOverlay(position, parking);
        freeOverlay.setMap(map);
        overlays.push(freeOverlay); // ✅ 오버레이 배열에 추가

    });
}


/**
 * 주차장의 상세정보창을 표시하는 함수
 * - 표시되는 항목: 주차장이름, 주소, 시간당요금, 운영시간
 * @param parking 주차장 정보가 담긴 객체
 */
function showParkingDetails(parking) {
    console.log(" 상세 정보 창 열기!", parking);

    const detailsContainer = document.getElementById("parkingDetails");

    if (!detailsContainer) {
        console.error(" 상세 정보 창 요소를 찾을 수 없습니다.");
        return;
    }

    document.getElementById("parkingName").innerText = parking.name;
    document.getElementById("parkingAddress").innerText = `주소: ${parking.address}`;

    let fee = "무료"
    if (parking.feePerHour!==0){
        fee = `시간당 요금 : ${parking.feePerHour}`
    }
    document.getElementById("parkingFee").innerText = `${fee}`;

    // 운영 시간 추가
    const operatingHoursContainer = document.getElementById("parkingOperatingHours");

    if (operatingHoursContainer) {
        console.log("operatingHoursContainer 열림");

        // 기존 내용 초기화
        operatingHoursContainer.innerHTML = "";

        if (parking.operatingHours && parking.operatingHours.length > 0) {
            parking.operatingHours.forEach((oh) => {
                const hourItem = document.createElement("div");
                hourItem.classList.add("operating-hour-item");
                let displayText = "";

                switch (oh.dataType) {
                    case "WEEKDAY":
                        displayText = `📅 평일 운영 시간: ${oh.openTime} - ${oh.closeTime}`;
                        break;
                    case "SATURDAY":
                        displayText = `🛒 토요일 운영 시간: ${oh.openTime} - ${oh.closeTime}`;
                        break;
                    case "HOLIDAY":
                        displayText = `🎉 공휴일 운영 시간: ${oh.openTime} - ${oh.closeTime}`;
                        break;
                    default:
                        displayText = `❓ 알 수 없는 운영 시간: ${oh.openTime} - ${oh.closeTime}`;
                }

                hourItem.innerText = `${displayText}`;
                operatingHoursContainer.appendChild(hourItem);
            });
        } else {
            operatingHoursContainer.innerText = "운영 시간 정보 없음";
        }
    }

    // ✅ 슬라이드 업 애니메이션 적용
    detailsContainer.classList.add("show");
}

//  상세 정보 창 닫기 버튼 이벤트 추가
document.getElementById("closeDetails").addEventListener("click", () => {
    const detailsContainer = document.getElementById("parkingDetails");

    if (!detailsContainer) {
        console.error("❌ 상세 정보 창 요소를 찾을 수 없습니다.");
        return;
    }
    // 슬라이드 다운 애니메이션 적용
    detailsContainer.classList.remove("show");


});




// "현재 위치 재검색" 버튼 클릭 이벤트 리스너 추가
document.getElementById("reloadLocation").addEventListener("click", async function () {
    console.log("현재 위치 재검색 버튼 클릭됨");

    if (!map) {
        console.error("지도 객체가 초기화되지 않았습니다.");
        return;
    }

    //현재 지도 중심 좌표 가져오기
    const center = map.getCenter();
    const centerLat = center.lat();
    const centerLng = center.lng();
    console.log("현재 지도 중심 좌표:", centerLat, centerLng);

    // 기존 마커 제거
    markers.forEach(marker => marker.setMap(null));
    markers = [];

    // 현재 지도 중심을 기준으로 주변 주차장 데이터 가져오기
    const parkingData = await fetchLocationDataFromAPI(centerLat, centerLng);

    if (parkingData) {
        displayParkingSpots(parkingData); // ✅ 새 마커 추가
    }
});




