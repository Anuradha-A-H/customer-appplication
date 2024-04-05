const originalFetch = window.fetch;
window.fetch = async function(url, options) {
    // Get the token from localStorage
    const token = localStorage.getItem("token");

    // If token is present, add it to the request headers
    if (token) {
        options = options || {};
        options.headers = options.headers || {};
        options.headers["Authorization"] = "Bearer " + token;
    }

    // Call the original fetch function with the modified options
    return originalFetch(url, options);
};