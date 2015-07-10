# Tiramisu

**Tiramisu is the Android application for the Rails School.**

## Architecture

This application uses the RS API to list all the incoming classes. It also reminds user about incoming classes. 

The stack is the following:

* Presentation layer: this Android application has only a single activity managing different fragments. To keep the application maintainable, a custom action bar was preferred instead of inheriting from the `ActionBarActivity`. To ensure communication between the different components, an event bus is used.
* Business:
  + BLL: This layer interacts with both remote API and DAL. It also manages the caching part.
  + DAL: In order to prevent mobile app to fetch classes every time, classes are cached locally. This layer also stores preferences. Instead of using a regular SQLite, this application uses Realm.
* Services and receivers: Those guys manages the automatic reminders and the push notifications. Users can receive a notification one day before class and two hours before.
* Unit testing: no UTs at this point. However, all the layers are built in a way making unit testing easier to set up. 
