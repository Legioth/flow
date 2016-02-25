/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.server;

import java.io.Serializable;
import java.util.Locale;
import java.util.function.BiConsumer;

import com.vaadin.shared.ApplicationConstants;
import com.vaadin.ui.UI;

/**
 * Contains helper methods for {@link VaadinServlet} and generally for handling
 * {@link VaadinRequest VaadinRequests}.
 *
 */
public class ServletHelper implements Serializable {

    public static final String UPLOAD_URL_PREFIX = "APP/UPLOAD/";
    /**
     * The default SystemMessages (read-only).
     */
    static final SystemMessages DEFAULT_SYSTEM_MESSAGES = new SystemMessages();

    private ServletHelper() {
    }

    private static boolean hasPathPrefix(VaadinRequest request, String prefix) {
        String pathInfo = request.getPathInfo(); // always starts with /

        if (pathInfo == null) {
            return false;
        }

        assert prefix.startsWith("/");

        return pathInfo.startsWith(prefix);
    }

    private static boolean isPathInfo(VaadinRequest request, String string) {
        String pathInfo = request.getPathInfo(); // always starts with /

        if (pathInfo == null) {
            return false;
        }

        assert string.startsWith("/");

        return pathInfo.equals(string);
    }

    /**
     * Returns whether the given request is a file upload request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a file upload request,
     *         <code>false</code> if not
     */
    public static boolean isFileUploadRequest(VaadinRequest request) {
        return hasPathPrefix(request, '/' + UPLOAD_URL_PREFIX);
    }

    /**
     * Returns whether the given request is a published file request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a published file request,
     *         <code>false</code> if not
     */
    public static boolean isPublishedFileRequest(VaadinRequest request) {
        return hasPathPrefix(request,
                '/' + ApplicationConstants.PUBLISHED_FILE_PATH + '/');
    }

    /**
     * Returns whether the given request is a UIDL request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a UIDL request, <code>false</code> if
     *         not
     */
    public static boolean isUIDLRequest(VaadinRequest request) {
        return hasPathPrefix(request,
                '/' + ApplicationConstants.UIDL_PATH + '/');
    }

    /**
     * Returns whether the given request is a app request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a app request, <code>false</code> if
     *         not
     */
    public static boolean isAppRequest(VaadinRequest request) {
        return hasPathPrefix(request,
                '/' + ApplicationConstants.APP_PATH + '/');
    }

    /**
     * Returns whether the given request is a heart beat request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a heart beat request,
     *         <code>false</code> if not
     */
    public static boolean isHeartbeatRequest(VaadinRequest request) {
        return hasPathPrefix(request,
                '/' + ApplicationConstants.HEARTBEAT_PATH + '/');
    }

    /**
     * Returns whether the given request is a push request.
     *
     * @param request
     *            the request to check
     * @return <code>true</code> if it is a push request, <code>false</code> if
     *         not
     */
    public static boolean isPushRequest(VaadinRequest request) {
        return isPathInfo(request, '/' + ApplicationConstants.PUSH_PATH);
    }

    /**
     * Helper to find the most most suitable Locale. These potential sources are
     * checked in order until a Locale is found:
     * <ol>
     * <li>The passed component (or UI) if not null</li>
     * <li>{@link UI#getCurrent()} if defined</li>
     * <li>The passed session if not null</li>
     * <li>{@link VaadinSession#getCurrent()} if defined</li>
     * <li>The passed request if not null</li>
     * <li>{@link VaadinService#getCurrentRequest()} if defined</li>
     * <li>{@link Locale#getDefault()}</li>
     * </ol>
     *
     * @param session
     *            the session that is searched for locale or <code>null</code>
     *            if not available
     * @param request
     *            the request that is searched for locale or <code>null</code>
     *            if not available
     * @return
     */
    public static Locale findLocale(VaadinSession session,
            VaadinRequest request) {

        if (session == null) {
            session = VaadinSession.getCurrent();
        }
        if (session != null) {
            Locale locale = session.getLocale();
            if (locale != null) {
                return locale;
            }
        }

        if (request == null) {
            request = VaadinService.getCurrentRequest();
        }
        if (request != null) {
            Locale locale = request.getLocale();
            if (locale != null) {
                return locale;
            }
        }

        return Locale.getDefault();
    }

    /**
     * Sets no cache headers to the specified response.
     *
     * @param headerSetter
     *            setter for string value headers
     * @param longHeaderSetter
     *            setter for long value headers
     */
    public static void setResponseNoCacheHeaders(
            BiConsumer<String, String> headerSetter,
            BiConsumer<String, Long> longHeaderSetter) {
        headerSetter.accept("Cache-Control", "no-cache, no-store");
        headerSetter.accept("Pragma", "no-cache");
        longHeaderSetter.accept("Expires", 0L);
    }
}