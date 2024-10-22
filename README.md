# i18nDotPropertiesGUI

GUI for managing i18n .properties translations

This GUI can help you or your translators to improve the way you edit translations files. This GUI can provide you :
- The key (from source translation)
- The source translation value
- The comments (from source translation)
- An alternative translation value
- The target translation value

All these values (except the key) can be edited directly in the app.

![preview](https://raw.githubusercontent.com/ClementGre/i18nDotPropertiesGUI/master/images/preview.png)
*Used to manage the translations of [PDF4Teachers](https://github.com/ClementGre/PDF4Teachers).*

# Learn how to use the app:

[Read i18nDotPropertiesGUI Wiki](https://github.com/ClementGre/i18nDotPropertiesGUI/wiki)

# Target translation keyboard shortcuts

- Ctrl + Space : Add the next argument into the target translation text field (ex: {0}, {1}...)
- Ctrl/Cmd + Shift + C : Copy the source translation into the target translation text field
- TAB or Shift + Enter : Skip to the next translation

# Dependencies

i18nDotPropertiesGUI works with JavaFX 16 and Gradle Wrapper 7 rc.2.

- **[JMetro 11.6.14](https://pixelduke.com/java-javafx-theme-jmetro/)** : JavaFX theme
- **[ControlsFX 11.1.0](https://controlsfx.github.io/)** : JavaFX new inputs and custom panes