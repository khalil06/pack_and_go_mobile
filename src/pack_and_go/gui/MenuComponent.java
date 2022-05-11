package pack_and_go.gui;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import pack_and_go.Components.ButtonComponent;
import pack_and_go.Components.RadioButtonComponent;
import pack_and_go.gui.PersonalityTest;

public class MenuComponent {
    public Form getMenu() {
        Form menu = new Form("Personality Test", new BoxLayout(BoxLayout.Y_AXIS));
        // sensing vs intuition questions
        String[] menuItems = {
                "Personality Test",
                "Hotels",
                "Activities",
                "User",
                "Restaurants",
        };
        for (String menuItem : menuItems) {
            Button menuItemButton = new ButtonComponent().getButton(menuItem);
            menuItemButton.addActionListener(evt ->
                    {
                        switch (menuItem) {
                            case "Personality Test":
                                PersonalityTest.createPersonalityTestForm().show();
                                break;
                            default:
                                // code block
                        }
                    }
            );
            menu.add(menuItemButton);
        }
        return menu;
    }
}
