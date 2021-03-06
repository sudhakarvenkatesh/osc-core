/*******************************************************************************
 * Copyright (c) Intel Corporation
 * Copyright (c) 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.osc.core.broker.view;

import org.osc.core.broker.service.api.ImportPluginServiceApi;
import org.osc.core.broker.service.api.server.ServerApi;
import org.osc.core.broker.view.common.StyleConstants;
import org.osc.core.broker.view.maintenance.PluginsLayout;
import org.osc.core.broker.view.util.ViewUtil;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Component(service={PluginView.class}, scope=ServiceScope.PROTOTYPE)
public class PluginView extends VerticalLayout implements View {

    private static final String HELP_PLUGIN_GUID = "GUID-65309889-62B2-43BE-81CE-6A4B650AAFEE.html";

    TabSheet subMenu = null;
    TabSheet tabs = new TabSheet();

    @Reference
    ImportPluginServiceApi importPluginService;

    @Reference
    ServerApi server;

    @Activate
    void start(BundleContext ctx) throws Exception {
        setSizeFull();
        addStyleName(StyleConstants.BASE_CONTAINER);
        
        VerticalLayout component = createComponent("Plugins", "Plugins",
                new PluginsLayout(ctx, this.importPluginService, this.server),
                HELP_PLUGIN_GUID);
        addComponent(component);
        setExpandRatio(component, 1L);
    }

    private VerticalLayout createComponent(String caption, String title, FormLayout content, String guid) {
        VerticalLayout tabSheet = new VerticalLayout();
        Panel panel = new Panel();
        // creating subHeader inside panel
        panel.setContent(content);
        panel.setSizeFull();
        tabSheet.addComponent(ViewUtil.createSubHeader(title, guid));
        tabSheet.addComponent(panel);
        return tabSheet;
    }

    @Override
    public void enter(ViewChangeEvent event) {
    }

}