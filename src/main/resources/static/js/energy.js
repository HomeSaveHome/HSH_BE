document.addEventListener("DOMContentLoaded", function () {
    const energyButtons = document.querySelectorAll(".energy-btn");
    const monthButtons = document.querySelectorAll(".month-btn");
    const selectedEnergy = document.getElementById("selectedEnergy");
    const selectedMonth = document.getElementById("selectedMonth");
    const unitLabel = document.getElementById("unit-label");
    const yearDisplay = document.getElementById("year-display");
    const selectedYear = document.getElementById("selectedYear");

    // 에너지원 선택
    energyButtons.forEach(button => {
        button.addEventListener("click", function () {
            selectedEnergy.value = this.getAttribute("data-energy");
            unitLabel.textContent = this.getAttribute("data-unit");

            energyButtons.forEach(btn => btn.classList.remove("active"));
            this.classList.add("active");
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

    document.getElementById("prevYear").addEventListener("click", function () {
        let currentYear = parseInt(yearDisplay.innerText);
        yearDisplay.innerText = currentYear - 1;
        selectedYear.value = currentYear - 1;
    });

    document.getElementById("nextYear").addEventListener("click", function () {
        let currentYear = parseInt(yearDisplay.innerText);
        yearDisplay.innerText = currentYear + 1;
        selectedYear.value = currentYear + 1;
    });
});