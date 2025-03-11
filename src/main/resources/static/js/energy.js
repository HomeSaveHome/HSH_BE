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

    // 에너지원 선택
    energyButtons.forEach(button => {
        button.addEventListener("click", function () {
            selectedEnergy.value = this.getAttribute("data-energy");
            unitLabel.textContent = this.getAttribute("data-unit");

            energyButtons.forEach(btn => btn.classList.remove("selected"));
            this.classList.add("selected");
        });
    });

    // 월 선택
    monthButtons.forEach(button => {
        button.addEventListener("click", function () {
            selectedMonth.value = this.getAttribute("data-month");

            monthButtons.forEach(btn => btn.classList.remove("selected"));
            this.classList.add("selected");
        });
    });

    // 년도 선택
    const currentYear = new Date().getFullYear();

    if (!selectedYear.value || selectedYear.value === "0") {
        selectedYear.value = currentYear;
        yearDisplay.innerText = currentYear;
    }

    function updateYearButtons() {
        let displayedYear = parseInt(yearDisplay.innerText);
        prevYearBtn.disabled = displayedYear <= 2000;
        nextYearBtn.disabled = displayedYear >= currentYear;
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