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
package com.vaadin.hummingbird.uitest.ui;

import com.vaadin.hummingbird.dom.Element;
import com.vaadin.hummingbird.util.JsonUtil;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import elemental.json.Json;

public class ExecJavaScriptUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        Element root = getElement();

        Element alertButton = createJsButton("Alert", "alertButton",
                "window.alert($0)", "Hello world");
        Element focusButton = createJsButton("Focus Alert button",
                "focusButton", "$0.focus()", alertButton);
        Element swapText = createJsButton("Swap button texts", "swapButton",
                "(function() {var t = $0.textContent; $0.textContent = $1.textContent; $1.textContent = t;})()",
                alertButton, focusButton);
        Element logButton = createJsButton("Log", "logButton",
                "console.log($0)", JsonUtil.createArray(
                        Json.create("Hello world"), Json.create(true)));

        root.appendChild(alertButton, focusButton, swapText, logButton);
    }

    private Element createJsButton(String text, String id, String script,
            Object... arguments) {
        Element element = new Element("button").setTextContent(text)
                .setAttribute("id", id);

        element.addEventListener("click",
                e -> getPage().executeJavaScript(script, arguments));

        return element;
    }
}