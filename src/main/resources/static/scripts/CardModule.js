const CardModule = (function () {
    const cardContainer = document.querySelector(".row");

    function populateCards(courses) {
        cardContainer.innerHTML = "";

        const template = document.getElementById("card-template").innerHTML;

        courses.forEach((course) => {
            const card = document.createElement("div");
            card.classList.add("course-col");

            const isAdminDashboard = window.location.pathname.includes("admin-dashboard");

            const renderedCard = Mustache.render(template, {
                courseTitle: course.courseTitle,
                courseAuthor: course.courseAuthor,
                courseDate: course.courseDate,
                courseCategory: course.courseCategory,
                courseContent: course.courseContent,
                courseId: course.courseId,
                isAdminDashboard: isAdminDashboard,
            });

            card.innerHTML = renderedCard;

            const editButton = card.querySelector(".edit-button");
            const deleteButton = card.querySelector(".delete-button");
            const confirmDeleteButton = document.getElementById("confirmDeleteButton");

            if (isAdminDashboard) {
                editButton.addEventListener("click", function () {
                    TaskModule.getCourseById(course.courseId);
                });

                deleteButton.addEventListener("click", function () {
                    document.getElementById("deleteModal").dataset.courseId = course.courseId;
                    $("#deleteModal").modal("show");
                });

                confirmDeleteButton.addEventListener("click", function () {
                    const courseIdToDelete = document.getElementById("deleteModal").dataset.courseId;
                    TaskModule.deleteCourse(courseIdToDelete);
                    $("#deleteModal").modal("hide");
                });
            }

            cardContainer.appendChild(card);
        });
    }

    return {
        populateCards,
    };
})();
