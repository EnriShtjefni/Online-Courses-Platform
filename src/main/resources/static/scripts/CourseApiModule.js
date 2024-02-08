const CourseApiModule = (function () {

    async function getCourses() {
        const response = await RestServiceModule.prepareRequest('GET', RestServiceModule.baseUrl);
        return RestServiceModule.handleResponse(response);
    }

    async function getCourseById(id) {
        const response = await RestServiceModule.prepareRequest('GET', `${RestServiceModule.baseUrl}/${id}`);
        return RestServiceModule.handleResponse(response);
    }

    async function addCourse(course) {
        const headers = RestServiceModule.prepareRequestHeaders();
        const response = await RestServiceModule.prepareRequest('POST', `${RestServiceModule.baseUrl}/post`, headers, JSON.stringify(course));
        return RestServiceModule.handleResponse(response);
    }

    async function updateCourse(id, updatedCourse) {
        const headers = RestServiceModule.prepareRequestHeaders();
        const response = await RestServiceModule.prepareRequest('PUT', `${RestServiceModule.baseUrl}/modify/${id}`, headers, JSON.stringify(updatedCourse));
        return RestServiceModule.handleResponse(response);
    }

    async function deleteCourse(id) {
        const response = await RestServiceModule.prepareRequest('DELETE', `${RestServiceModule.baseUrl}/delete/${id}`);
        return RestServiceModule.handleDeleteResponse(response);
    }

    async function getFilteredCourses(title, category) {
        const response = await RestServiceModule.prepareRequest('GET', `${RestServiceModule.baseUrl}/filter?titleKeyword=${title}&categoryKeyword=${category}`);
        return RestServiceModule.handleResponse(response);
    }

    return {
        getCourses,
        getCourseById,
        addCourse,
        updateCourse,
        deleteCourse,
        getFilteredCourses
    };
})();
