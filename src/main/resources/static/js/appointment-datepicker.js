flatpickr("#datepicker", {
    dateFormat: "d-m-Y",
    minDate: "today",
    locale: {
        firstDayOfWeek: 1
    },
    disable: [
        function(date) {
            return (date.getDay() === 0 || date.getDay() === 6);
        }
    ]
});
