document.addEventListener("DOMContentLoaded", function () {
    const energyButtons = document.querySelectorAll(".energy-btn");
    const monthButtons = document.querySelectorAll(".month-btn");
    const selectedEnergy = document.getElementById("selectedEnergy");
    const selectedMonth = document.getElementById("selectedMonth");
    const unitLabel = document.getElementById("unit-label");
    const yearDisplay = document.getElementById("year-display");
    const selectedYear = document.getElementById("selectedYear");
    const prevYearBtn = document.getElementById("prevYear");
    const nextYearBtn = document.getElementById("nextYear");

    let usedMonths = [];
    let currentYear = new Date().getFullYear();

    // 초기 연도 설정
    if (!selectedYear.value || selectedYear.value === "0") {
        selectedYear.value = currentYear;
        yearDisplay.innerText = currentYear;
    }

    // 년도, 에너지 타입 -> 사용된 월 조회
    function fetchUsedMonths(year, energyId) {
        fetch(`/energyUsed/used-months/${year}/${energyId}`)
            .then(response => response.json())
            .then(data => {
                console.log("서버 응답 데이터: ", data);
                if (Array.isArray(data)) {
                    usedMonths = data;
                } else {
                    usedMonths = [];
                    console.error("서버 응답이 배열이 아님: ", data);
                }
                updateMonthButtons();
            })
            .catch(error => {
                console.error("사용된 월 조회 실패: ", error);
                usedMonths = [];
            });
    }

    // 초기에 모든 월 선택 비활성화
    monthButtons.forEach(btn => btn.disabled = true);

    // 에너지원 선택 -> 월 선택 활성화
    energyButtons.forEach(button => {
        button.addEventListener("click", function () {
            selectedEnergy.value = this.getAttribute("data-energy");
            unitLabel.textContent = this.getAttribute("data-unit");

            energyButtons.forEach(btn => btn.classList.remove("selected"));
            this.classList.add("selected");

            fetchUsedMonths(selectedYear.value, selectedEnergy.value);
        });
    });

    // 월 버튼 상태 업데이트
    function updateMonthButtons() {
        const currentMonth = new Date().getMonth() + 1;
        const selectedYearValue = parseInt(selectedYear.value, 10);
        monthButtons.forEach(btn => {
            let month = parseInt(btn.getAttribute("data-month"));

            if (usedMonths.includes(month)) {
                btn.disabled = true;
                btn.classList.add("blocked");
            } else if (selectedYearValue > currentYear || (selectedYearValue === currentYear && month > currentMonth)) {
                btn.disabled = true;
                btn.classList.add("blocked");
            } else {
                btn.disabled = false;
                btn.classList.remove("blocked");
            }
        });
    }

    // 월 선택
    monthButtons.forEach(button => {
        button.addEventListener("click", function () {
            if (!selectedEnergy) return;  // 에너지를 선택하지 않을 경우 클릭 X
            selectedMonth.value = this.getAttribute("data-month");

            monthButtons.forEach(btn => btn.classList.remove("selected"));
            this.classList.add("selected");
        });
    });

    // 연도 변경 버튼 상태 업데이트
    function updateYearButtons() {
        let displayedYear = parseInt(yearDisplay.innerText);
        prevYearBtn.disabled = displayedYear <= 2000;
        nextYearBtn.disabled = displayedYear >= currentYear;
        fetchUsedMonths(selectedYear.value, selectedEnergy.value);
    }

    prevYearBtn.addEventListener("click", function () {
        let displayedYear = parseInt(yearDisplay.innerText);
        if (displayedYear > 2000) {
            yearDisplay.innerText = displayedYear - 1;
            selectedYear.value = displayedYear - 1;
            updateYearButtons();
        }
    });

    nextYearBtn.addEventListener("click", function () {
        let displayedYear = parseInt(yearDisplay.innerText);
        if (displayedYear < currentYear) {
            yearDisplay.innerText = displayedYear + 1;
            selectedYear.value = displayedYear + 1;
            updateYearButtons();
        }
    });

    updateYearButtons(); // 초기 상태 설정
});