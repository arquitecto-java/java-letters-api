package com.arquitectojava.letters.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

    public SimpleCORSFilter() {
        log.info("SimpleCORSFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        /*
        * Access-Control-Allow-Origin (required) - This header must be included in all valid CORS responses; omitting the header will
        * cause the CORS request to fail. The value of the header can either echo the Origin request header (as in the example above),
        * or be a '*' to allow requests from any origin. If you’d like any site to be able to access your data, using '*' is fine.
        * But if you’d like finer control over who can access your data, use an actual value in the header.
        * */
        //response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Origin", "*");
        /*
        * Access-Control-Allow-Credentials (optional) - By default, cookies are not included in CORS requests. Use this header to indicate
        * that cookies should be included in CORS requests. The only valid value for this header is true (all lowercase). If you don't need
        * cookies, don't include this header (rather than setting its value to false).
        * */
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /*
        * Access-Control-Allow-Methods (required) - Comma-delimited list of the supported HTTP methods. Note that although the preflight
        * request only asks permisions for a single HTTP method, this reponse header can include the list of all supported HTTP methods.
        * This is helpful because the preflight response may be cached, so a single preflight response can contain details about multiple
        * request types.
        * */
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT");
        /*
        * Access-Control-Max-Age (optional) - Making a preflight request on *every* request becomes expensive, since the browser is making
        * two requests for every client request. The value of this header allows the preflight response to be cached for a specified number
        * of seconds.
        * */
        //response.setHeader("Access-Control-Max-Age", "3600");
        /*
        * Access-Control-Allow-Headers (required if the request has an Access-Control-Request-Headers header) - Comma-delimited list of
        * the supported request headers. Like the Access-Control-Allow-Methods header above, this can list all the headers supported by
        * the server (not only the headers requested in the preflight request).
        * */
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        /*
        * Access-Control-Expose-Headers (optional) - The XMLHttpRequest 2 object has a getResponseHeader() method that returns the value
        * of a particular response header. During a CORS request, the getResponseHeader() method can only access simple response headers.
        * Simple response headers are defined as follows:
        *
        * Cache-Control
        * Content-Language
        * Content-Type
        * Expires
        * Last-Modified
        * Pragma
        * If you want clients to be able to access other headers, you have to use the Access-Control-Expose-Headers header. The value of
        * this header is a comma-delimited list of response headers you want to expose to the client.
        * */

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}