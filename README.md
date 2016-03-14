# Shopping List

This is a simple shopping list app which can manage shopping lists for different users.

## Extra Features
- When users input a user id, shopping list name or item, the displaying list makes a responsive simple search
- The done button is shown when users swipe left the corresponding list item. A strike-through marks an item has been bought.

## Demo
Just down load debug [apk](https://github.com/czheng035/Shopping-List/blob/master/app-debug.apk) and install it on your phone, API level 16-23 support.

![Demo](https://github.com/czheng035/Shopping-List/blob/master/demo.gif)

## Data Model
![Data Model](https://github.com/czheng035/Shopping-List/blob/master/data-model.png)

## Test
The project provides a content provider test with a few test cases at [Test Link](https://github.com/czheng035/Shopping-List/tree/master/app/src/androidTest/java/com/knowroaming/czheng035/shoppinglist/data).

## Third-party library
The project contains a [library module](https://github.com/daimajia/AndroidSwipeLayout) for the swipe-to-delete action. Their jcenter() dependency seems out-of-date and buggy so I import the whole source project.