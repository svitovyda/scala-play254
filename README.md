# Simple Scala Play 2.5.4 application

Motivation for this project was to create simplest `Play 2.5.4` application without `Guice` injection, which become obvious by default from `2.5.4` version of `Play framework`.
I think Scala doesn't need any additional implicit dependency injections and compile-time dependency makes development and mocking in autotests way more simple and predictable.

It contains few example implementations:
* actor that contains counter of all views as its state and gives it to controller that responds to REST HTTP request
* some settings
* manifests info and `environment` variable

_There may be some strange code/settings/dependencies from my work projects, I built this project for publishing without putting into it too much time and effort.
The whole system may look overcomplicated as there is no business behind it.
Counter may not work correctly, its just a small example of actor._
