package br.com.cubekode.jilotrack.tracker.servlet;

import java.security.Principal;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.cubekode.jilotrack.track.Tracking;
import br.com.cubekode.jilotrack.tracker.Tracker;

public class TrackerServletInfo {

    public static final String REQUEST_TRACKER_ATTRIBUTE = "br.com.cubekode.apptrack.tracker";

    public static void trackServlet(ServletRequest req) {

        Tracking tracking = Tracker.get();

        if (isTrackingRequest(req)) {
            return;
        }

        req.setAttribute(REQUEST_TRACKER_ATTRIBUTE, Boolean.TRUE.toString());

        tracking.setTag("Request", Boolean.TRUE.toString());
        tracking.setTag("Request.ContentType", req.getContentType());
        tracking.setTag("Request.LocalAddr", req.getLocalAddr());
        tracking.setTag("Request.LocalName", req.getLocalName());
        tracking.setTag("Request.Protocol", req.getProtocol());
        tracking.setTag("Request.RemoteAddr", req.getRemoteAddr());
        tracking.setTag("Request.RemoteHost", req.getRemoteHost());
        tracking.setTag("Request.RemotePort", Integer.toString(req.getRemotePort()));
        tracking.setTag("Request.ServerPort", Integer.toString(req.getServerPort()));
        tracking.setTag("Request.Locale", req.getLocale() != null ? req.getLocale().toString() : "");

        if (req instanceof HttpServletRequest) {

            HttpServletRequest httpReq = (HttpServletRequest) req;

            trackHttpHeader(httpReq, tracking);
            trackHttpRequest(httpReq, tracking);
            trackHttpPrincipal(httpReq, tracking);
            trackHttpSession(httpReq, tracking);
        }

        trackRequestParams(req, tracking);
    }

    private static void trackHttpHeader(HttpServletRequest httpReq, Tracking tracking) {

        Enumeration<String> names = httpReq.getHeaderNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String header = httpReq.getHeader(name);
            tracking.setTag("Request.Header." + name, header == null ? "" : header);
        }
    }

    private static void trackRequestParams(ServletRequest req, Tracking tracking) {

        Map<String, String[]> parameterMap = req.getParameterMap();

        if (parameterMap != null) {
            for (Entry<String, String[]> p : parameterMap.entrySet()) {
                tracking.setTag("Request.Param." + p.getKey(), p.getValue() == null ? "[]" : Arrays.toString(p.getValue()));
            }
        }
    }

    static void trackHttpRequest(HttpServletRequest req, Tracking tracking) {

        tracking.setTag("Request.ContextPath", req.getContextPath());
        tracking.setTag("Request.Method", req.getMethod());
        tracking.setTag("Request.PathInfo", req.getPathInfo());
        tracking.setTag("Request.PathTranslated", req.getPathTranslated());
        tracking.setTag("Request.RemoteUser", req.getRemoteUser());
        tracking.setTag("Request.RequestURI", req.getRequestURI());
        tracking.setTag("Request.RequestURL", req.getRequestURL().toString());
        tracking.setTag("Request.QueryString", req.getQueryString());
        tracking.setTag("Request.ServletPath", req.getServletPath());
    }

    private static void trackHttpSession(HttpServletRequest req, Tracking tracking) {

        HttpSession session = req.getSession(false);

        if (session != null) {
            tracking.setTag("Request.Session.Id", session.getId());
            tracking.setTag("Request.Session.CreationTime", Long.toString(session.getCreationTime()));
            tracking.setTag("Request.Session.LastAccessedTime", Long.toString(session.getLastAccessedTime()));
            tracking.setTag("Request.Session.MaxInactiveInterval", Integer.toString(session.getMaxInactiveInterval()));
            tracking.setTag("Request.Session.New", Boolean.toString(session.isNew()));
        } else {
            tracking.setTag("Request.Session.Id", "0");
        }
    }

    private static void trackHttpPrincipal(HttpServletRequest req, Tracking tracking) {

        Principal principal = req.getUserPrincipal();

        if (principal != null) {
            tracking.setTag("Request.Principal", principal.getName());
        } else {
            tracking.setTag("Request.Principal", "");
        }
    }

    public static boolean isTrackingRequest(ServletRequest req) {
        return req.getAttribute(REQUEST_TRACKER_ATTRIBUTE) != null;
    }
}
