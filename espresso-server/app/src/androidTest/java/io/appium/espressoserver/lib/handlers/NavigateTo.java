/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appium.espressoserver.lib.handlers;

import androidx.test.espresso.EspressoException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.PickerActions;
import io.appium.espressoserver.lib.handlers.exceptions.AppiumException;
import io.appium.espressoserver.lib.helpers.AndroidLogger;
import io.appium.espressoserver.lib.model.Element;
import io.appium.espressoserver.lib.model.NavigateToParams;
import io.appium.espressoserver.lib.model.ScrollToPageParams;
import io.appium.espressoserver.lib.model.SetTimeParams;

public class NavigateTo implements RequestHandler<NavigateToParams, Void> {

    @Override
    public Void handle(NavigateToParams params) throws AppiumException {
        ViewInteraction viewInteraction = Element.getViewInteractionById(params.getElementId());
        Integer menuItemId = params.getMenuItemId();
        try {
            viewInteraction.perform(NavigationViewActions.navigateTo(menuItemId));
        } catch (Exception e) {
            if (e instanceof EspressoException) {
                throw new AppiumException(String.format("Could not navigate to menu item %s. Reason: %s", menuItemId, e));
            }
            throw e;
        }
        return null;
    }
}
