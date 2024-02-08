const TaskModule = (function () {
    const saveButton = document.getElementById("saveButton");
    const clearButton = document.getElementById("clearButton");
    const inputId = document.getElementById("inputId");
    const inputTitle = document.getElementById("inputTitle");
    const inputCategory = document.getElementById("inputCategory");
    const inputContent = document.getElementById("inputContent");
    const closeButton = document.querySelector(".modal-header .close");

    function resetForm() {
        inputId.value = "";
        inputTitle.value = "";
        inputCategory.value = "";
        inputContent.value = "";
    }

    function closeModal() {
        const exitModal = document.getElementById("formModal");
        $(exitModal).modal("hide");
        resetForm();
    }

    function getCourseById(id) {
        CourseApiModule.getCourseById(id)
            .then((course) => {
                inputId.value = course.courseId;
                inputTitle.value = course.courseTitle;
                inputCategory.value = course.courseCategory.categoryId;
                inputContent.value = course.courseContent;
            })
            .catch((error) => console.error("Error fetching course by ID:", error.statusMessage));
    }

    function saveCourse() {
        const courseTitle = inputTitle.value.trim();
        const courseCategory = inputCategory.value;
        const courseContent = inputContent.value.trim();

        console.log(
            "Updating course with title:",
            courseTitle,
            "categoryId:",
            courseCategory,
            "content:",
            courseContent
        );

        if (!courseTitle || !courseCategory || !courseContent) {
            alert("Please fill in all fields before saving.");
            return;
        }

        const course = {
            courseTitle,
            courseCategory: {categoryId: courseCategory},
            courseContent,
        };

        if (inputId.value) {
            CourseApiModule.updateCourse(inputId.value, course)
                .then((data) => {
                    console.log("Update successful. Server response:", data);
                    closeModal();
                    AppModule.renderAllCourses();
                })
                .catch((error) => {
                    console.log(error);
                    if (error && error.statusCode === 400) {
                        alert(error.statusMessage);
                    } else {
                        console.error("Error updating course:", error.statusMessage);
                    }
                });
        } else {
            CourseApiModule.addCourse(course)
                .then((data) => {
                    closeModal();
                    AppModule.renderAllCourses();
                })
                .catch((error) => {
                    console.log(error);
                    if (error && error.statusCode === 400) {
                        alert(error.statusMessage);
                    } else {
                        console.error("Error adding a new course:", error.statusMessage);
                    }
                });
        }
    }


    function deleteCourse(id) {
        CourseApiModule.deleteCourse(id)
            .then(() => {
                AppModule.renderAllCourses()
            })
            .catch((error) => console.error("Error deleting course:", error.statusMessage));
    }

    saveButton.addEventListener("click", saveCourse);
    clearButton.addEventListener("click", resetForm);
    closeButton.addEventListener("click", resetForm);

    return {
        getCourseById,
        deleteCourse,
    };
})();
