const request = require("sync-request");

function hacerRequestHttp(method, url, body) {
    if (method === "PUT" || method === "POST" || method === "PATCH")
        return JSON.parse(
            request(method, encodeURI(url), {
                body: body,
                headers: {
                    "Content-Type": "application/json",
                },
            }).getBody("utf8")
        );

    if (method === "GET")
        return JSON.parse(request(method, encodeURI(url)).getBody("utf8"));
}

module.exports = {
    hacerRequestHttp,
};
