const AppModule = (function () {

    function renderAllCourses() {
        CourseApiModule.getCourses()
            .then((courses) => CardModule.populateCards(courses))
            .catch((error) => console.error("Error fetching all courses:", error.statusMessage));
    }

    function init() {
        renderAllCourses();

        const courseCategorySelect = document.getElementById(
            "courseCategorySelect"
        );
        const searchInput = document.querySelector(".search-input input");
        const searchIcon = document.querySelector(".search-icon");

        function filterCourses() {
            const selectedCategoryId = courseCategorySelect.value;
            const searchTerm = searchInput.value;

            CourseApiModule.getFilteredCourses(searchTerm, selectedCategoryId)
                .then((courses) => CardModule.populateCards(courses))
                .catch((error) =>
                    console.error("Error fetching filtered courses:", error.statusMessage)
                );
        }

        searchIcon.addEventListener("click", filterCourses);
    }

    return {
        init,
        renderAllCourses,
    };
})();

AppModule.init();
