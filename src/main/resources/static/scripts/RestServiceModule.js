class CustomErrorObject {
    constructor(statusMessage, statusCode) {
        this.statusMessage = statusMessage;
        this.statusCode = statusCode;
    }
}

const RestServiceModule = (function () {
    const baseUrl = '/api/courses';

    async function handleResponse(response) {
        if (!response.ok) {
            const errorData = await response.text();
            throw new CustomErrorObject(errorData, response.status);
        }
        const data = await response.json();
        return data;
    }

    async function handleDeleteResponse(response) {
        if (!response.ok) {
            const errorData = await response.text();
            throw new CustomErrorObject(errorData, response.status);
        } else {
            console.log('Course deleted successfully');
        }
    }

    function prepareRequestHeadersDefault(contentType = 'application/json') {
        return {
            'Content-Type': contentType,
        };
    }

    function prepareRequest(method, url, headers, body) {
        return fetch(url, {
            method: method,
            headers: headers,
            body: body,
        });
    }

    return {
        baseUrl,
        handleResponse,
        prepareRequestHeaders: prepareRequestHeadersDefault,
        prepareRequest,
        handleDeleteResponse,
    };
})();